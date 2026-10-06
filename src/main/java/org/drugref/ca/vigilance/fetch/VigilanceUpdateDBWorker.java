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

public class VigilanceUpdateDBWorker implements Runnable {
    private static final Logger logger = MiscUtils.getLogger();

    private static final String[] REQUIRED_FILES = {"fgenPlus.dat", "generxplus.dat", "nomprodPlus.dat"};
    private static final String ACTION_UPDATE = "vigilance update db";

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

    private String findInList(List<String> availableFiles, String expectedName) {
        for (String available : availableFiles) {
            if (available.equalsIgnoreCase(expectedName)) {
                return available;
            }
        }
        return null;
    }

    private void updateStatus(String updateId, String status, String progress, long startTime) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        map.put("progress", progress);
        map.put("startTime", startTime);
        map.put("elapsedSeconds", (System.currentTimeMillis() - startTime) / 1000);
        Drugref.VIGILANCE_UPDATE_STATUS.put(updateId, map);
    }

    private void cleanupTempDir(File dir) {
        try {
            FileUtils.deleteDirectory(dir);
            logger.debug("Cleaned up temp directory: {}", dir.getAbsolutePath());
        } catch (Exception e) {
            logger.warn("Failed to cleanup temp directory: {}", dir.getAbsolutePath(), e);
        }
    }
}
