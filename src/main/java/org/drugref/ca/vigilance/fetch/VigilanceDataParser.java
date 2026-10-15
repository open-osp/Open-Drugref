package org.drugref.ca.vigilance.fetch;

import org.apache.logging.log4j.Logger;
import org.drugref.Drugref;
import org.drugref.util.JpaUtils;
import org.drugref.util.MiscUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parses the downloaded Vigilance {@code .dat} files and loads them into the MySQL
 * {@code vig_*} tables.
 * <p>
 * Files are ISO-8859-1 encoded, tab-separated. An optional header line — one whose first
 * field matches the first expected column name — is skipped. Each data row must have at
 * least as many fields as the table column list; trailing extra fields are ignored (a
 * single warning is logged).
 * <p>
 * Flow (see {@link #parseAndLoad(File, File, File, String)}):
 * <ol>
 *   <li>Validate all three files.</li>
 *   <li>Truncate {@code vig_fgenPlus}, {@code vig_generxPlus} and {@code vig_nomprodPlus}.</li>
 *   <li>Load each file with batched prepared-statement inserts.</li>
 *   <li>Create the FULLTEXT indexes used by the Vigilance search queries (skipped when present).</li>
 * </ol>
 * Progress is published to {@link Drugref#VIGILANCE_UPDATE_STATUS} under the given update id.
 */
public class VigilanceDataParser {
    private static final Logger logger = MiscUtils.getLogger();

    /** Number of rows per {@code executeBatch()} / commit when loading a table. */
    private static final int BATCH_SIZE = 1000;

    /** Column layout of {@code vig_fgenPlus}; also the minimum field count of {@code fgenPlus.dat} rows. */
    private static final String[] FGENPLUS_COLUMNS = {
        "GENcode", "GENcodeDetail", "monographID", "genericNameFrench",
        "genericSimpleNameFrench", "genericNameEnglish", "genericSimpleNameEnglish",
        "usualNameFrench", "usualNameEnglish", "allergenStatus", "ATCcode",
        "AHFSold", "AHFSnew", "defaultIndication", "tallManFrench",
        "tallManEnglish", "actitityIndicator", "allergenType", "isDrug",
        "uniqueIdentifier", "dosageValidationRoutes", "cytotoxicity", "excipient",
        "comparativeDrugChart", "priceChart", "isDoseAccordingToWeight",
        "TMcodesOfCCDD", "precisionIndicator", "obsolescenceIndicator",
        "mgmntIndicatorProblematicDrugs", "mgmtIndicatorDrugsInProfile"
    };

    /** Column layout of {@code vig_generxPlus}; also the minimum field count of {@code generxplus.dat} rows. */
    private static final String[] GENERXPLUS_COLUMNS = {
        "uuid", "GENcode", "genericNameFrench", "genericNameEnglish",
        "strengthFrench", "formFrench", "strengthEnglish", "formEnglish",
        "doseUnitsPerDoseFormUnit", "doseUnits", "defaultFrequency",
        "defaultRouteOfAdmin", "ceRxFormCode", "ceRxRouteCode",
        "lowercaseGenericNameFrench", "lowercaseStrengthFrench", "lowercaseFormFrench",
        "lowercaseGenericNameEnglish", "lowercaseStrengthEnglish", "lowercaseFormEnglish",
        "activityIndicator", "dosageValidated", "NTPCodesOfCCDD",
        "SIGUnitsSingularFrench", "SIGUnitsPluralFrench",
        "SIGUnitsSingularEnglish", "SIGUnitsPluralEnglish",
        "usualNameFrench", "usualNameEnglish"
    };

    /** Column layout of {@code vig_nomprodPlus}; also the minimum field count of {@code nomprodPlus.dat} rows. */
    private static final String[] NOMPRODPLUS_COLUMNS = {
        "productID", "GENcode", "productNameFrench", "strengthFrench",
        "formFrench", "productNameEnglish", "strengthEnglish", "formEnglish",
        "manufacturer", "discontinued", "legalStatusPQ", "supply",
        "pseudoDIN", "RAMQcoverage", "unitCost", "productType",
        "inInstitutionList", "SIGcode", "defaultExpirationDays", "monographID",
        "medicationInformationSheet", "auxiliarySheet", "auxiliaryLabels",
        "administrationRoute", "defaultRouteDosageValidation", "ATC", "AHFS",
        "CRxForm", "CRxRoute", "CRxSIG", "CRxQuantity", "usualQuantity",
        "usualDuration", "productNameCapitalizedFrench", "lowercaseStrengthFrench",
        "lowercaseFormFrench", "productNameCapitalizedEnglish", "lowercaseStrengthEnglish",
        "lowercaseFormEnglish", "prescriptionOptometrists", "prescriptionPodiatrist",
        "prescriptionMidwives", "prescriptionNursePractitioner", "exceptionalMedicationCodes",
        "exceptionalMedicationForms", "defaultAdministrationRouteDoseValidationMACT",
        "legalStatusCanada", "productSpecification", "prescriptionNurses",
        "albertaCoverage", "britishColumbiaCoverage", "princeEdwardIslandCoverage",
        "manitobaCoverage", "newBrunswickCoverage", "novaScotiaCoverage",
        "ontarioCoverage", "saskatchewanCoverage", "newfoundlandCoverage",
        "nonInsuredHealthBenefitsCoverage", "defaultExpirationDays2",
        "tallManProductNameFrench", "tallManProductNameEnglish", "NTPcodeOfCCDD",
        "productMonographFilenameFrench", "productMonographFilenameEnglish",
        "prescriptionRespiratoryTherapists", "marketingStatus", "PTAAlert",
        "prescriptionDietetician", "pediatricDosage", "defaultChronicity"
    };

    /**
     * Validates, truncates and loads the three Vigilance data files.
     *
     * @param fgenPlusFile    downloaded {@code fgenPlus.dat}
     * @param generxPlusFile  downloaded {@code generxplus.dat}
     * @param nomprodPlusFile downloaded {@code nomprodPlus.dat}
     * @param updateId        key under which progress is published in
     *                        {@link Drugref#VIGILANCE_UPDATE_STATUS}
     * @return map of table name ({@code vig_fgenPlus}, {@code vig_generxPlus},
     *         {@code vig_nomprodPlus}) to the number of rows loaded
     * @throws Exception if a file is empty, has too few columns, or any load step fails
     */
    public static Map<String, Integer> parseAndLoad(File fgenPlusFile, File generxPlusFile, File nomprodPlusFile, String updateId) throws Exception {
        Map<String, Integer> rowCounts = new HashMap<>();

        long startTime = System.currentTimeMillis();

        updateStatus(updateId, "validating", "Validating uploaded files...", startTime);
        validateFile(fgenPlusFile, FGENPLUS_COLUMNS);
        validateFile(generxPlusFile, GENERXPLUS_COLUMNS);
        validateFile(nomprodPlusFile, NOMPRODPLUS_COLUMNS);

        updateStatus(updateId, "parsing", "Truncating tables...", startTime);

        truncateTables();

        updateStatus(updateId, "parsing", "Loading vig_fgenPlus...", startTime);
        int fgenCount = loadTable(fgenPlusFile, "vig_fgenPlus", FGENPLUS_COLUMNS, updateId, startTime);
        rowCounts.put("vig_fgenPlus", fgenCount);

        updateStatus(updateId, "parsing", "Loading vig_generxPlus...", startTime);
        int generxCount = loadTable(generxPlusFile, "vig_generxPlus", GENERXPLUS_COLUMNS, updateId, startTime);
        rowCounts.put("vig_generxPlus", generxCount);

        updateStatus(updateId, "parsing", "Loading vig_nomprodPlus...", startTime);
        int nomprodCount = loadTable(nomprodPlusFile, "vig_nomprodPlus", NOMPRODPLUS_COLUMNS, updateId, startTime);
        rowCounts.put("vig_nomprodPlus", nomprodCount);

        updateStatus(updateId, "indexing", "Creating FULLTEXT indexes...", startTime);
        createFulltextIndexes();

        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
        logger.info("Vigilance data load completed in {} seconds: fgenPlus={}, generxPlus={}, nomprodPlus={}",
            elapsed, fgenCount, generxCount, nomprodCount);

        return rowCounts;
    }

    /**
     * Checks that the file is readable and that its first data row has at least
     * {@code expectedColumns.length} tab-separated fields.
     *
     * @throws Exception if the file is empty or the first data row has too few columns
     */
    private static void validateFile(File file, String[] expectedColumns) throws Exception {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.ISO_8859_1))) {

            String line;
            int lineNumber = 0;
            boolean hasData = false;
            boolean firstNonBlank = true;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.length() == 0) {
                    continue;
                }
                if (firstNonBlank && isHeaderLine(line, expectedColumns)) {
                    firstNonBlank = false;
                    continue;
                }
                firstNonBlank = false;
                String[] values = line.split("\t", -1);
                if (values.length < expectedColumns.length) {
                    throw new Exception("Line " + lineNumber + " in " + file.getName() +
                        ": expected at least " + expectedColumns.length + " columns, got " + values.length);
                }
                hasData = true;
                break;
            }
            if (!hasData) {
                throw new Exception("File " + file.getName() + " is empty or contains no data rows");
            }
        }
    }

    /** True when the line is a header row: its first field equals the first expected column name. */
    private static boolean isHeaderLine(String line, String[] expectedColumns) {
        String[] values = line.split("\t", -1);
        return values.length > 0 && values[0].equals(expectedColumns[0]);
    }

    /**
     * Publishes a {@code status}/{@code progress}/{@code startTime}/{@code elapsedSeconds}
     * entry to {@link Drugref#VIGILANCE_UPDATE_STATUS} under the update id.
     */
    private static void updateStatus(String updateId, String status, String progress, long startTime) {
        Map<String, Object> statusMap = new HashMap<>();
        statusMap.put("status", status);
        statusMap.put("progress", progress);
        statusMap.put("startTime", startTime);
        statusMap.put("elapsedSeconds", (System.currentTimeMillis() - startTime) / 1000);
        Drugref.VIGILANCE_UPDATE_STATUS.put(updateId, statusMap);
    }

    /** Truncates all three {@code vig_*} tables in a single transaction. */
    private static void truncateTables() throws Exception {
        EntityManager em = JpaUtils.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.createNativeQuery("TRUNCATE TABLE vig_fgenPlus").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE vig_generxPlus").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE vig_nomprodPlus").executeUpdate();
            tx.commit();
            logger.info("Truncated all Vigilance tables");
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            JpaUtils.close(em);
        }
    }

    /**
     * Loads one {@code .dat} file into a table with batched prepared-statement inserts.
     * <p>
     * Rows are committed in batches of {@link #BATCH_SIZE}; empty fields are inserted as
     * {@code NULL}; progress is updated every 10,000 rows.
     *
     * @return the number of rows inserted
     */
    private static int loadTable(File file, String tableName, String[] columns, String updateId, long startTime) throws Exception {
        int totalRows = 0;

        EntityManager em = JpaUtils.createEntityManager();

        String columnList = String.join(", ", columns);
        String placeholders = String.join(", ", java.util.Collections.nCopies(columns.length, "?"));
        String sql = "INSERT INTO " + tableName + " (" + columnList + ") VALUES (" + placeholders + ")";

        Connection conn = null;
        try {
            conn = em.unwrap(java.sql.Connection.class);
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), StandardCharsets.ISO_8859_1));
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                conn.setAutoCommit(false);

                String line;
                int lineNumber = 0;
                int batchCount = 0;
                boolean warnedExtraColumns = false;
                boolean firstNonBlank = true;

                while ((line = reader.readLine()) != null) {
                    lineNumber++;
                    if (line.length() == 0) {
                        continue;
                    }
                    if (firstNonBlank && isHeaderLine(line, columns)) {
                        firstNonBlank = false;
                        continue;
                    }
                    firstNonBlank = false;
                    String[] values = line.split("\t", -1);

                    if (values.length < columns.length) {
                        throw new Exception("Line " + lineNumber + " in " + file.getName() +
                            ": expected at least " + columns.length + " columns, got " + values.length);
                    }

                    if (values.length > columns.length && !warnedExtraColumns) {
                        warnedExtraColumns = true;
                        logger.warn("Rows in {} contain {} columns; loading only the first {} columns",
                            file.getName(), values.length, columns.length);
                    }

                    for (int i = 0; i < columns.length; i++) {
                        String value = values[i];
                        if (value.length() == 0) {
                            stmt.setNull(i + 1, Types.NULL);
                        } else {
                            stmt.setString(i + 1, value);
                        }
                    }
                    stmt.addBatch();
                    batchCount++;

                    if (batchCount >= BATCH_SIZE) {
                        stmt.executeBatch();
                        conn.commit();
                        totalRows += batchCount;
                        batchCount = 0;

                        if (totalRows % 10000 == 0) {
                            long elapsed = (System.currentTimeMillis() - startTime) / 1000;
                            updateStatus(updateId, "parsing",
                                String.format("Loading %s: %d rows...", tableName, totalRows), startTime);
                        }
                    }
                }

                if (batchCount > 0) {
                    stmt.executeBatch();
                    conn.commit();
                    totalRows += batchCount;
                }
            }
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackEx) {
                logger.error("Rollback failed", rollbackEx);
            }
            throw new Exception("Failed to load " + tableName, e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException ignored) {
                }
                try {
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
            JpaUtils.close(em);
        }

        logger.info("Loaded {} rows into {}", totalRows, tableName);
        return totalRows;
    }

    /**
     * Creates the six FULLTEXT indexes required by the Vigilance search queries
     * (English/French pairs on the name/strength/form columns), skipping any that already exist.
     */
    private static void createFulltextIndexes() throws Exception {
        EntityManager em = JpaUtils.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        String[][] indexes = {
            {"ft_nomprod_en", "vig_nomprodPlus", "productNameEnglish, strengthEnglish, formEnglish"},
            {"ft_nomprod_fr", "vig_nomprodPlus", "productNameFrench, strengthFrench, formFrench"},
            {"ft_generx_en", "vig_generxPlus", "lowercaseGenericNameEnglish, strengthEnglish, formEnglish"},
            {"ft_generx_fr", "vig_generxPlus", "lowercaseGenericNameFrench, strengthFrench, formFrench"},
            {"ft_fgen_en", "vig_fgenPlus", "genericNameEnglish"},
            {"ft_fgen_fr", "vig_fgenPlus", "genericNameFrench"}
        };

        try {
            tx.begin();
            for (String[] index : indexes) {
                String indexName = index[0];
                String table = index[1];
                String columns = index[2];
                if (fulltextIndexExists(em, table, indexName)) {
                    logger.info("FULLTEXT index already present, skipping: {} on {}", indexName, table);
                    continue;
                }
                String indexSql = "ALTER TABLE " + table + " ADD FULLTEXT " + indexName + " (" + columns + ")";
                em.createNativeQuery(indexSql).executeUpdate();
                logger.info("Created FULLTEXT index: {}", indexSql);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            JpaUtils.close(em);
        }
    }

    /** Checks {@code information_schema.statistics} for an existing index of the given name on the table. */
    private static boolean fulltextIndexExists(EntityManager em, String table, String indexName) {
        Query query = em.createNativeQuery(
            "SELECT COUNT(*) FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = ?1 AND index_name = ?2")
            .setParameter(1, table)
            .setParameter(2, indexName);
        List<?> results = query.getResultList();
        Object count = results.get(0);
        return count instanceof Number && ((Number) count).longValue() > 0;
    }
}