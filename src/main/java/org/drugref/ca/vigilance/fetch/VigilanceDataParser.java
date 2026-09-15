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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VigilanceDataParser {
    private static final Logger logger = MiscUtils.getLogger();
    private static final int BATCH_SIZE = 1000;

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

    private static void validateFile(File file, String[] expectedColumns) throws Exception {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String headerLine = reader.readLine();
            if (headerLine == null) {
                throw new Exception("File " + file.getName() + " is empty (no header line)");
            }

            String[] headerColumns = headerLine.split("\t");
            if (headerColumns.length != expectedColumns.length) {
                throw new Exception("Column count mismatch in " + file.getName() +
                    ": expected " + expectedColumns.length + ", got " + headerColumns.length);
            }

            String line;
            boolean hasData = false;
            while ((line = reader.readLine()) != null) {
                if (line.length() > 0) {
                    hasData = true;
                    break;
                }
            }
            if (!hasData) {
                throw new Exception("File " + file.getName() + " contains no data rows");
            }
        }
    }

    private static void updateStatus(String updateId, String status, String progress, long startTime) {
        Map<String, Object> statusMap = new HashMap<>();
        statusMap.put("status", status);
        statusMap.put("progress", progress);
        statusMap.put("startTime", startTime);
        statusMap.put("elapsedSeconds", (System.currentTimeMillis() - startTime) / 1000);
        Drugref.VIGILANCE_UPDATE_STATUS.put(updateId, statusMap);
    }

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
                    new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                conn.setAutoCommit(false);

                String headerLine = reader.readLine();
                if (headerLine == null) {
                    logger.warn("File {} is empty", file.getName());
                    return 0;
                }

                String[] headerColumns = headerLine.split("\t");
                if (headerColumns.length != columns.length) {
                    throw new Exception("Column count mismatch in " + file.getName() +
                        ": expected " + columns.length + ", got " + headerColumns.length);
                }

                String line;
                int lineNumber = 1;
                int batchCount = 0;

                while ((line = reader.readLine()) != null) {
                    lineNumber++;
                    String[] values = line.split("\t", -1);

                    if (values.length != columns.length) {
                        throw new Exception("Line " + lineNumber + " in " + file.getName() +
                            ": expected " + columns.length + " columns, got " + values.length);
                    }

                    for (int i = 0; i < values.length; i++) {
                        stmt.setString(i + 1, values[i]);
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

    private static void createFulltextIndexes() throws Exception {
        EntityManager em = JpaUtils.createEntityManager();

        String[][] indexes = {
            {"ft_nomprod_en", "vig_nomprodPlus", "productNameEnglish, strengthEnglish, formEnglish"},
            {"ft_nomprod_fr", "vig_nomprodPlus", "productNameFrench, strengthFrench, formFrench"},
            {"ft_generx_en", "vig_generxPlus", "lowercaseGenericNameEnglish, strengthEnglish, formEnglish"},
            {"ft_generx_fr", "vig_generxPlus", "lowercaseGenericNameFrench, strengthFrench, formFrench"},
            {"ft_fgen_en", "vig_fgenPlus", "genericNameEnglish"},
            {"ft_fgen_fr", "vig_fgenPlus", "genericNameFrench"}
        };

        try {
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
        } finally {
            JpaUtils.close(em);
        }
    }

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