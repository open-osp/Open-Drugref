package org.drugref.ca.vigilance.fetch;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.drugref.Drugref;
import org.drugref.ca.dpd.history.HistoryUtil;
import org.drugref.util.MiscUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Performs the full Vigilance database update.
 * <p>
 * Started from {@link Drugref#updateDB()} as {@code new Thread(new VigilanceUpdateDBWorker()).start()}
 * when {@code database.integration=vigilance}; gated by the shared {@link Drugref#UPDATE_DB} flag
 * so that only one update can run at a time.
 * <p>
 * Flow:
 * <ol>
 *   <li>Clears {@link Drugref#VIGILANCE_UPDATE_STATUS} and generates a UUID update id used as the status key.</li>
 *   <li>Creates a temp directory under {@code java.io.tmpdir} ({@code vigilance-update-<updateId>}).</li>
 *   <li>Authenticates against the Vigilance API via
 *       {@link VigilanceApiClient#authenticate()} (OAuth2 password grant with client
 *       credentials from the {@code vigilance.*} properties).</li>
 *   <li>Fetches the file list, extracts the {@code .dat} names and downloads the required files
 *       {@code fgenPlus.dat}, {@code generxplus.dat} and {@code nomprodPlus.dat}, falling back to the
 *       canonical names when the API lists different ones.</li>
 *   <li>Loads the files into the {@code vig_*} MySQL tables via
 *       {@link VigilanceDataParser#parseAndLoad(File, File, File, String)}, which returns per-table row counts.</li>
 *   <li>Writes a {@code History} row via {@code HistoryUtil.addUpdateHistory("vigilance update db")} and
 *       publishes {@code vigilance_tableRowNum} and {@code vigilance_timeImportMinutes} to
 *       {@link Drugref#DB_INFO}.</li>
 * </ol>
 * Progress entries and the final {@code completed}/{@code failed} status are published to
 * {@link Drugref#VIGILANCE_UPDATE_STATUS} under the update id; on failure the error message is
 * additionally stored in {@link Drugref#DB_INFO} under {@code vigilance_last_error}.
 * <p>
 * In all cases the {@code finally} block resets {@link Drugref#UPDATE_DB} to {@code false} and
 * deletes the temp directory, so a failed run never blocks subsequent updates.
 */
public class VigilanceUpdateDBWorker implements Runnable {
    private static final Logger logger = MiscUtils.getLogger();

    /** Data files that must be downloaded from the Vigilance API before the update can run. */
    private static final String[] REQUIRED_FILES = {"fgenPlus.dat", "generxplus.dat", "nomprodPlus.dat"};
    /** Action label written to the {@code History} table for a Vigilance update. */
    private static final String ACTION_UPDATE = "vigilance update db";

    /**
     * Runs the Vigilance update pipeline.
     * <p>
     * Never throws: all failures are logged, captured in the failed status entry of
     * {@link Drugref#VIGILANCE_UPDATE_STATUS} and stored under {@code vigilance_last_error}
     * in {@link Drugref#DB_INFO}.
     */
    @Override
    public void run() {
        Drugref.VIGILANCE_UPDATE_STATUS.clear();
        String updateId = UUID.randomUUID().toString();
        long startTime = System.currentTimeMillis();
        File tempDir = new File(System.getProperty("java.io.tmpdir"), "vigilance-update-" + updateId);

        updateStatus(updateId, "running", "Starting Vigilance database update...", startTime);

        try {
            if (!tempDir.mkdirs()) {
                throw new Exception("Failed to create temp directory " + tempDir.getAbsolutePath());
            }

            VigilanceApiClient client = new VigilanceApiClient();

            updateStatus(updateId, "running", "Authenticating with Vigilance API...", startTime);
            String token = client.authenticate();

            updateStatus(updateId, "running", "Fetching file list...", startTime);
            String listResponse = client.getFileList(token);
            List<String> availableFiles = client.findDatFiles(listResponse);
            logger.info("Vigilance file list contains {} .dat files: {}", availableFiles.size(), availableFiles);

            updateStatus(updateId, "running", "Downloading data files...", startTime);
            File[] downloadedFiles = new File[REQUIRED_FILES.length];
            for (int i = 0; i < REQUIRED_FILES.length; i++) {
                String expectedName = REQUIRED_FILES[i];
                String actualName = findInList(availableFiles, expectedName);
                if (actualName == null) {
                    logger.warn("File {} not found in Vigilance file list, attempting direct download", expectedName);
                    actualName = expectedName;
                }
                File destFile = new File(tempDir, expectedName);
                client.downloadFile(token, actualName, destFile);
                downloadedFiles[i] = destFile;
            }

            updateStatus(updateId, "running", "Loading database...", startTime);
            Map<String, Integer> rowCounts = VigilanceDataParser.parseAndLoad(
                    downloadedFiles[0], downloadedFiles[1], downloadedFiles[2], updateId);

            HistoryUtil historyUtil = new HistoryUtil();
            historyUtil.addUpdateHistory(ACTION_UPDATE);

            long elapsedMinutes = (System.currentTimeMillis() - startTime) / 60000;

            Drugref.DB_INFO.put("vigilance_tableRowNum", rowCounts);
            Drugref.DB_INFO.put("vigilance_timeImportMinutes", elapsedMinutes);

            Map<String, Object> status = new HashMap<>();
            status.put("status", "completed");
            status.put("progress", "Vigilance database update completed successfully");
            status.put("startTime", startTime);
            status.put("endTime", System.currentTimeMillis());
            status.put("elapsedMinutes", elapsedMinutes);
            status.put("tableRowCounts", rowCounts);
            Drugref.VIGILANCE_UPDATE_STATUS.put(updateId, status);

            logger.info("Vigilance database update completed in {} minutes: {}", elapsedMinutes, rowCounts);

        } catch (Exception e) {
            logger.error("Vigilance database update failed", e);

            long elapsedMinutes = (System.currentTimeMillis() - startTime) / 60000;
            Map<String, Object> status = new HashMap<>();
            status.put("status", "failed");
            status.put("progress", "Vigilance database update failed: " + e.getMessage());
            status.put("startTime", startTime);
            status.put("endTime", System.currentTimeMillis());
            status.put("elapsedMinutes", elapsedMinutes);
            status.put("error", e.getMessage());
            Drugref.VIGILANCE_UPDATE_STATUS.put(updateId, status);

            Drugref.DB_INFO.put("vigilance_last_error", e.getMessage());

        } finally {
            Drugref.UPDATE_DB.set(false);
            cleanupTempDir(tempDir);
        }
    }

    /**
     * Finds an expected file name in the API's file list, ignoring case.
     *
     * @param availableFiles file names returned by {@link VigilanceApiClient#findDatFiles(String)}
     * @param expectedName   one of {@link #REQUIRED_FILES}
     * @return the matching listed name, or {@code null} if not present (the caller then
     *         falls back to the expected name)
     */
    private String findInList(List<String> availableFiles, String expectedName) {
        for (String available : availableFiles) {
            if (available.equalsIgnoreCase(expectedName)) {
                return available;
            }
        }
        return null;
    }

    /**
     * Publishes an intermediate progress entry ({@code status}, {@code progress},
     * {@code startTime}, {@code elapsedSeconds}) to {@link Drugref#VIGILANCE_UPDATE_STATUS}
     * under the given update id.
     *
     * @param updateId  key of the status map
     * @param status    current state ({@code running} while in progress)
     * @param progress  human-readable description of the step in progress
     * @param startTime epoch millis at which the update started
     */
    private void updateStatus(String updateId, String status, String progress, long startTime) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        map.put("progress", progress);
        map.put("startTime", startTime);
        map.put("elapsedSeconds", (System.currentTimeMillis() - startTime) / 1000);
        Drugref.VIGILANCE_UPDATE_STATUS.put(updateId, map);
    }

    /**
     * Recursively deletes the temporary download directory.
     * <p>
     * Failures are logged and swallowed so that cleanup can never mask the original
     * outcome of the update.
     *
     * @param dir temp directory created at the start of {@link #run()}
     */
    private void cleanupTempDir(File dir) {
        try {
            FileUtils.deleteDirectory(dir);
            logger.debug("Cleaned up temp directory: {}", dir.getAbsolutePath());
        } catch (Exception e) {
            logger.warn("Failed to cleanup temp directory: {}", dir.getAbsolutePath(), e);
        }
    }
}
