package org.drugref.ca.vigilance;

import org.apache.logging.log4j.Logger;
import org.apache.openjpa.persistence.QueryImpl;
import org.drugref.Category;
import org.drugref.ca.dpd.CdDrugProduct;
import org.drugref.ca.dpd.CdDrugSearch;
import org.drugref.ca.dpd.CdTherapeuticClass;
import org.drugref.ca.TablesDao;
import org.drugref.util.JpaUtils;
import org.drugref.util.MiscUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
public class VigilanceDao implements TablesDao, Serializable {
    Logger logger = MiscUtils.getLogger();
    /*
     * alternate is French which was not considered as a choice during initial development
     * Future development should consider the system language French or English
     */
    private static String language = "English";

    /**
     * Shortest word the FULLTEXT index holds, from the server's innodb_ft_min_token_size.
     */
    private static final int MINIMUM_TOKEN_LENGTH = 3;

    /**
     * One search token: a quoted phrase with its optional sign, or a run of non-space
     * characters. The phrase alternative comes first so that the quotes are kept together.
     */
    private static final Pattern SEARCH_TOKEN = Pattern.compile("[+-]?\"[^\"]*\"|\\S+");

    /**
     * Letters and digits make up words, and every other character separates them. The search
     * term's words, and the word it is ranked on, must both be found by this one rule.
     */
    private static final Pattern NON_WORD = Pattern.compile("[^\\p{L}\\p{N}]+");

    /**
     * Text holding at least one letter or digit, for telling a real quoted phrase from an empty
     * or punctuation-only one. Matched against the whole string.
     */
    private static final Pattern HAS_WORD_CHARACTER = Pattern.compile(".*[\\p{L}\\p{N}].*");
    private String name;
    private String version;

    public VigilanceDao() {
        this.name = "Vigilance Sante: vigilance.ca";
        this.version = "1.0";
    }

    @Override
    public String identify() {
        if(this.name == null) {
            return "";
        }
        return this.name;
    }

    @Override
    public String version() {
        if(this.version == null) {
            return "";
        }
        return this.version;
    }

    @Override
    public Vector list_available_services() {
        return new Vector();
    }

    @Override
    public Hashtable list_capabilities() {
        return new Hashtable();
    }

    @Override
    public Object fetch(String attribute, Vector key, Vector services, boolean feelingLucky) {
        return new Vector();
    }

    @Override
    public Integer getDrugIdFromDIN(String DIN) {
        return 0;
    }

    @Override
    public String getDINFromDrugId(Integer drugId) {
        return "";
    }

    @Override
    public Integer getDrugpKeyFromDIN(String DIN) {
        return 0;
    }

    @Override
    public Integer getDrugpKeyFromDrugId(Integer drugId) {
        return 0;
    }

    @Override
    public List<Integer> getInactiveDrugs() {
        return new Vector();
    }

    @Override
    public CdDrugProduct getDrugProduct(String drugcode) {
        return new CdDrugProduct();
    }

    @Override
    public CdTherapeuticClass getATCCodes(String drugcode) {
        return new CdTherapeuticClass();
    }

    /**
     * Get all possible ATC codes for given drug name search
     * @param drugname
     * @return
     */
    private Set<String> searchATCCodesByDrugname(String drugname) {
        Vector searchResults = this.listSearchAll(drugname);
        Set<String> allergyATCList = new HashSet<>();
        Set<String> genericCodeList = new HashSet<>();

        // collect a set of generic codes from the results
        for(Object resultMap : searchResults) {
            Map result = new Hashtable<>((Hashtable) resultMap);
            genericCodeList.add((String) result.get("drugCode"));
        }

        // collect a set of ATC codes from the generic codes
        for(String genericCode : genericCodeList) {
            String atcCode = getATCCode(genericCode);
            if(atcCode != null){
                allergyATCList.add(atcCode);
            }
        }

        return allergyATCList;
    }

    /**
     *
     * @param genericCode Drug genericcode
     * @return Single ATC for given generic code
     */
    private String getATCCode(String genericCode) {
        EntityManager em = JpaUtils.createEntityManager();
        String sql = "SELECT DISTINCT list.ATC FROM (" +
                "SELECT ATC AS `ATC` " +
                "FROM vig_nomprodPlus " +
                "WHERE GENcode = ?1" +
                " UNION " +
                "SELECT ATCcode AS `ATC` " +
                "FROM vig_fgenPlus " +
                "WHERE GENcode = ?1" +
                ") list";

        Query query = em.createNativeQuery(sql);
        query.setParameter(1, genericCode);
        List results = query.getResultList();
        return results.isEmpty() ? null : (String) results.get(0);
    }

    /**
     * Fetches a single search result from the
     * generic or brand tables based on the unique identifier of a drug.
     * Searches are isolated to the generxPlus (generic) or nomprodPlus (Brand)
     * tables.
     */
    @Override
    public CdDrugSearch getSearchedDrug(String id) {
        EntityManager em = JpaUtils.createEntityManager();
        String sql = "SELECT productID AS `id`, " +
                "GENcode AS `drugCode`, " +
                "CAST(?2 AS NCHAR) AS `category`, " +
                "ProductName" + language + " AS `name` " +
                "FROM vig_nomprodPlus " +
                "WHERE productID = ?1" +
                " UNION " +
                "SELECT uuid AS `id`, " +
                "GENcode AS `drugCode`, " +
                "CAST(?3 AS NCHAR) AS `category`, " +
                "genericName" + language + " AS `name` " +
                "FROM vig_generxPlus " +
                "WHERE uuid = ?1" +
                " UNION " +
                "SELECT uniqueIdentifier AS `id`, " +
                "GENcode AS `drugCode`, " +
                "CAST(?4 AS NCHAR) AS `category`, " +
                "genericName" + language + " AS `name` " +
                "FROM vig_fgenPlus " +
                "WHERE uniqueIdentifier = ?1";

        Query query = em.createNativeQuery(sql, Hashtable.class);
        query.setParameter(1, id);
        query.setParameter(2, Category.BRAND.getOrdinal());
        query.setParameter(3, Category.AI_GENERIC.getOrdinal());
        query.setParameter(4, Category.GENERIC.getOrdinal());

        CdDrugSearch cdDrugSearch = new CdDrugSearch();
        List<Hashtable<String, Object>> results = query.getResultList();
        if(!results.isEmpty()) {
            Hashtable<String, Object> singleResult = results.get(0);
            cdDrugSearch.setUuid((String) singleResult.get("id"));
            cdDrugSearch.setDrugCode((String) singleResult.get("drugCode"));
            cdDrugSearch.setCategory(Integer.parseInt((String) singleResult.get("category")));
            cdDrugSearch.setName((String) singleResult.get("name"));
        }
        return cdDrugSearch;
    }

    public CdDrugSearch getSearchedDrug(int id) {
        return getSearchedDrug(id+"");
    }

    @Override
    public List<CdDrugSearch> getListAICodes(List<String> listOfDrugCodes) {
        return new Vector();
    }

    @Override
    public List<String> findLikeDins(String din) {
        return new Vector();
    }

    @Override
    public List listDrugsbyAIGroup(String aiGroup) {
        return new Vector();
    }

    @Override
    public List<CdDrugSearch> listDrugsbyAIGroup2(String aiGroup) {
        return new Vector();
    }

    @Override
    public Vector listSearchElement2(String str) {
        return listSearchElement4(str, false);
    }

    @Override
    public String getFirstDinInAIGroup(String aiGroupNo) {
        return "";
    }

    /**
     * Keyword searches are done with MySQL Full Text Indexes. This method
     * will not work if the Full Text indexes are not created in the MySQL database.
     */
    @Override
    public Vector listSearchElement4(String keyword, boolean ingredientOnly){
        return listSearchBrandAndGeneric(keyword);
    }

    /**
     * Searches the brand and generic product tables, and nothing else.
     * <p>
     * The ingredient table is deliberately not searched. Its rows describe active
     * ingredients rather than products, so they carry no product identifier and cannot be
     * turned into a prescription by the caller.
     * <p>
     * Rows that would display an identical label are collapsed here rather than by the
     * caller, so that the surviving row is chosen deliberately. The generic record is
     * preferred over the brand record for a shared label. Results are ordered so that an
     * exact match on the search term comes before combination products, which in turn come
     * before manufacturer-branded variants.
     *
     * @param keyword String the search term as typed by the user
     * @return Vector of Hashtable rows carrying id, category, drugCode and name
     */
    private Vector listSearchBrandAndGeneric(String keyword) {
        EntityManager em = JpaUtils.createEntityManager();
        Assert.notNull(keyword, "Search value cannot be null.");

        // Column names carry the language as a suffix. They are resolved once here so that an
        // identifier is never split across two append calls in the middle of the statement.
        String genericName = "genericName" + language;
        String lowercaseGenericName = "lowercaseGenericName" + language;
        String productName = "productName" + language;
        String productNameCapitalized = "productNameCapitalized" + language;
        String strength = "strength" + language;
        String form = "form" + language;

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT `id`, `category`, `drugCode`, `name` FROM (");
        sql.append("SELECT `id`, `category`, `drugCode`, `name`, rankTier, ");
        sql.append("ROW_NUMBER() OVER (PARTITION BY `name` ORDER BY preferred, `id`) AS rowNumber ");
        sql.append("FROM (");

        // Generic products. Preferred over the brand record when both carry the same label.
        sql.append("SELECT uuid AS `id`, ");
        sql.append("CAST(?3 AS NCHAR) AS `category`, ");
        sql.append("GENcode AS `drugCode`, ");
        sql.append("CONCAT(").append(genericName).append(", ' ', ");
        sql.append("IFNULL(").append(strength).append(",''), ' ', ");
        sql.append("IFNULL(").append(form).append(",'')) AS `name`, ");
        sql.append("0 AS preferred, ");
        sql.append("CASE WHEN ").append(genericName).append(" = ?2 THEN 0 ");
        sql.append("WHEN ").append(genericName).append(" LIKE CONCAT(?2,'%') THEN 1 ");
        sql.append("ELSE 2 END AS rankTier ");
        sql.append("FROM vig_generxPlus ");
        sql.append("WHERE MATCH(").append(lowercaseGenericName).append(", ");
        sql.append(strength).append(", ").append(form).append(") ");
        sql.append("AGAINST (?1 IN BOOLEAN MODE)");

        sql.append(" UNION ALL ");

        // Brand products.
        sql.append("SELECT productID AS `id`, ");
        sql.append("CAST(?4 AS NCHAR) AS `category`, ");
        sql.append("GENcode AS `drugCode`, ");
        sql.append("CONCAT(").append(productNameCapitalized).append(", ' ', ");
        sql.append("IFNULL(").append(strength).append(",''), ' ', ");
        sql.append("IFNULL(").append(form).append(",'')) AS `name`, ");
        sql.append("1 AS preferred, ");
        sql.append("CASE WHEN ").append(productNameCapitalized).append(" = ?2 THEN 0 ");
        sql.append("WHEN ").append(productNameCapitalized).append(" LIKE CONCAT(?2,'%') THEN 1 ");
        sql.append("ELSE 2 END AS rankTier ");
        sql.append("FROM vig_nomprodPlus ");
        sql.append("WHERE MATCH(").append(productName).append(", ");
        sql.append(strength).append(", ").append(form).append(") ");
        sql.append("AGAINST (?1 IN BOOLEAN MODE)");

        sql.append(") u");
        sql.append(") d WHERE rowNumber = 1 ");
        sql.append("ORDER BY rankTier, `name`");

        Query query = em.createNativeQuery(sql.toString());
        query.setParameter(1, parseSearchParameters(keyword));
        query.setParameter(2, firstSearchWord(keyword));
        query.setParameter(3, Category.AI_GENERIC.getOrdinal());
        query.setParameter(4, Category.BRAND.getOrdinal());

        List<Object[]> results = query.getResultList();
        Vector<Hashtable<String, Object>> resultList = new Vector<>();

        for (Object[] row : results) {
            Hashtable<String, Object> ha = new Hashtable<>();
            ha.put("id", row[0]);
            ha.put("category", row[1]);
            ha.put("drugCode", row[2]);
            ha.put("name", row[3]);
            resultList.add(ha);
        }

        JpaUtils.close(em);
        return resultList;
    }

    /**
     * All search results are returned as a list of drug ingredients.
     * Example: searching for a brand name will return only the ingredients
     * for the brand name.
     * @return
     */
    private Vector listSearchIngredient(String keyword) {

        EntityManager em = JpaUtils.createEntityManager();
        Assert.notNull(keyword, "Search value cannot be null.");

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("uuid AS `id`,");
        sql.append("CAST(?2 AS NCHAR) AS category,");
        sql.append("GENcode AS `drugCode`,CONCAT(genericNameEnglish, ' ', IFNULL(strengthEnglish,''), ' ', IFNULL(formEnglish,'')) AS `name`,");
        sql.append("genericNameEnglish,");
        sql.append("strengthEnglish ");
        sql.append("FROM vig_generxPlus ");
        sql.append("WHERE GENcode IN (");
        sql.append("SELECT GENcode ");
        sql.append("FROM (");
        sql.append("SELECT GENcode ");
        sql.append("FROM vig_nomprodPlus ");
        sql.append("WHERE MATCH(productNameEnglish, strengthEnglish, formEnglish) against (?1 IN BOOLEAN MODE)");
        sql.append(" UNION ");
        sql.append("SELECT GENcode ");
        sql.append("FROM vig_generxPlus ");
        sql.append("WHERE MATCH(lowercaseGenericNameEnglish, strengthEnglish, formEnglish) AGAINST (?1 IN BOOLEAN MODE)");
        sql.append(") gencodes ");
        sql.append("GROUP BY gencodes.GENcode having count(gencodes.GENcode) > -1 ");
        sql.append(") ORDER BY genericNameEnglish, strengthEnglish;");

        Query query = em.createNativeQuery(sql.toString(), Hashtable.class);
        String parameters = parseParameters(keyword);
        query.setParameter(1, parameters);
        query.setParameter(2, Category.AI_GENERIC.getOrdinal());
        List results = query.getResultList();
        Vector<Hashtable<String, Object>> resultList = new Vector<Hashtable<String,Object>>(results);
        JpaUtils.close(em);
        return resultList;
    }

    /**
     * Any search will return all results brand, ingredient, generic, etc...
     * @return
     */
    private Vector listSearchAll(String keyword) {
        EntityManager em = JpaUtils.createEntityManager();
        Assert.notNull(keyword, "Search value cannot be null.");

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT productID AS id, CAST(?2 AS NCHAR) AS `category`,");
        sql.append("GENcode AS `drugCode`,");
        sql.append("CONCAT(");
        sql.append("productNameCapitalized").append(language);
        sql.append(", ").append("' ', ");
        sql.append("IFNULL(");
        sql.append("strength").append(language);
        sql.append(",'')");
        sql.append(", ").append("' ', ");
        sql.append("IFNULL(");
        sql.append("form").append(language);
        sql.append(",'')");
        sql.append(") AS `name` ");
        sql.append("FROM vig_nomprodPlus ");
        sql.append("WHERE MATCH(");
        sql.append("productName").append(language).append(", ");
        sql.append("strength").append(language).append(", ");
        sql.append("form").append(language);
        sql.append(") ");
        sql.append("AGAINST (?1 IN BOOLEAN MODE)");

        sql.append(" UNION ");

        sql.append("SELECT IFNULL(gx.uuid, '') AS `id`, ");
        sql.append("CAST(?3 AS NCHAR) AS `category`,");
        sql.append("IFNULL(gx.GENcode, g.GENcode) AS `drugCode`,");
        sql.append("CONCAT(");
        sql.append("IFNULL(gx.genericName").append(language);
        sql.append(",g.genericName").append(language).append(")");
        sql.append(", ").append("' ', ");
        sql.append("IFNULL(");
        sql.append("gx.strength").append(language);
        sql.append(",'')");
        sql.append(", ").append("' ', ");
        sql.append("IFNULL(");
        sql.append("gx.form").append(language);
        sql.append(",'')");
        sql.append(") AS `name` ");
        sql.append("FROM vig_fgenPlus g ");
        sql.append("LEFT JOIN vig_generxPlus gx ON (g.GENcode = gx.GENcode) ");
        sql.append("WHERE MATCH(");
        sql.append("gx.lowercaseGenericName").append(language).append(", ");
        sql.append("gx.strength").append(language).append(", ");
        sql.append("gx.form").append(language);
        sql.append(") ");
        sql.append("AGAINST (?1 IN BOOLEAN MODE) ");
        sql.append("OR MATCH(");
        sql.append("g.genericName").append(language);
        sql.append(") ");
        sql.append("AGAINST (?1 IN BOOLEAN MODE)");

        Query query = em.createNativeQuery(sql.toString());
        String parameters = parseParameters(keyword);
        query.setParameter(1, parameters);
        query.setParameter(2, Category.BRAND.getOrdinal());
        query.setParameter(3, Category.AI_GENERIC.getOrdinal());
        
        List<Object[]> results = query.getResultList();
        Vector<Hashtable<String, Object>> resultList = new Vector<>();
        
        for (Object[] row : results) {
            Hashtable<String, Object> ha = new Hashtable<>();
            ha.put("id", row[0]);
            ha.put("category", row[1]);
            ha.put("drugCode", row[2]);
            ha.put("name", row[3]);
            resultList.add(ha);
        }
        
        JpaUtils.close(em);
        return resultList;
    }

    @Override
    public Vector listSearchElement3(String str) {
        return listSearchElement4(str, false);
    }

    @Override
    public Vector listSearchElement(String str) {
        return listSearchElement4(str, false);
    }

    @Override
    public Vector listSearchElementRoute(String str, String route) {
        return new Vector();
    }

    @Override
    public Vector listBrandsFromElement(String drugID) {
        return new Vector();
    }

    /**
     * Method used for searching drug database for drugs that are identified as
     * allergens.
     * @param str
     * @param cat
     * @return Vector selection of drug names; generic and brand
     */
    @Override
    public Vector listSearchElementSelectCategories(String str, Vector cat) {

        EntityManager em = JpaUtils.createEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("vig_nomprodPlus.productNameCapitalized").append(language).append(" AS 'name',");
        sql.append("vig_nomprodPlus.productID AS 'id',");
        sql.append("CAST(?2 AS NCHAR) AS 'category',");
        sql.append("vig_nomprodPlus.GENcode AS 'drugCode',");
        sql.append("IFNULL(vig_nomprodPlus.ATC,'') as ATC, ");
        sql.append("IFNULL(pharmacological.genericName").append(language).append(",'') AS 'pharmacological', ");
        sql.append("IFNULL(chemical.genericName").append(language).append(",'') AS 'chemical', ");
        sql.append("IFNULL(substance.genericName").append(language).append(",'') AS 'substance' ");
        sql.append("FROM vig_nomprodPlus ");
        sql.append("LEFT JOIN vig_fgenPlus pharmacological ");
        sql.append("ON (pharmacological.ATCcode = SUBSTRING(vig_nomprodPlus.ATC,1,3)) ");
        sql.append("LEFT JOIN vig_fgenPlus chemical ");
        sql.append("ON (chemical.ATCcode = SUBSTRING(vig_nomprodPlus.ATC,1,4)) ");
        sql.append("LEFT JOIN vig_fgenPlus substance ");
        sql.append("ON (substance.ATCcode = SUBSTRING(vig_nomprodPlus.ATC,1,5) OR substance.ATCcode = vig_nomprodPlus.ATC) ");
        sql.append("WHERE MATCH(productName").append(language).append(") AGAINST (?1 IN BOOLEAN MODE) ");
        sql.append("GROUP BY vig_nomprodPlus.GENcode HAVING COUNT(vig_nomprodPlus.GENcode) > -1 ");

        sql.append("UNION ALL ");

        sql.append("SELECT ");
        sql.append("vig_fgenPlus.genericName").append(language).append(" AS 'name',");
        sql.append("vig_fgenPlus.uniqueIdentifier AS `id`,");
        sql.append("CAST(?3 AS NCHAR) AS `category`,");
        sql.append("vig_fgenPlus.GENcode AS `drugCode`,");
        sql.append("IFNULL(vig_fgenPlus.ATCcode,'') AS ATC, ");
        sql.append("IFNULL(pharmacological.genericName").append(language).append(",'') AS 'pharmacological', ");
        sql.append("IFNULL(chemical.genericName").append(language).append(",'') AS 'chemical', ");
        sql.append("IFNULL(substance.genericName").append(language).append(",'') AS 'substance' ");
        sql.append("FROM vig_fgenPlus ");
        sql.append("LEFT JOIN vig_fgenPlus pharmacological ");
        sql.append("ON (pharmacological.ATCcode = SUBSTRING(vig_fgenPlus.ATCcode,1,3)) ");
        sql.append("LEFT JOIN vig_fgenPlus chemical ");
        sql.append("ON (chemical.ATCcode = SUBSTRING(vig_fgenPlus.ATCcode,1,4)) ");
        sql.append("LEFT JOIN vig_fgenPlus substance ");
        sql.append("ON (substance.ATCcode = SUBSTRING(vig_fgenPlus.ATCcode,1,5) OR substance.ATCcode = vig_fgenPlus.ATCcode) ");
        sql.append("WHERE MATCH(vig_fgenPlus.genericName").append(language).append(") AGAINST (?1 IN BOOLEAN MODE) ");
        sql.append("GROUP BY vig_fgenPlus.GENcode HAVING COUNT(vig_fgenPlus.GENcode) > -1");

        Query query = em.createNativeQuery(sql.toString(), Hashtable.class);
        String parameters = parseParameters(str);
        query.setParameter(1, parameters);
        query.setParameter(2, Category.BRAND.getOrdinal());
        query.setParameter(3, Category.GENERIC.getOrdinal());
        List results = query.getResultList();
        Vector<Hashtable<String, Object>> resultList = new Vector<Hashtable<String,Object>>(results);
        JpaUtils.close(em);
        return resultList;
    }

    @Override
    public Vector listSearchElementSelectCategories(String str, Vector cat, boolean wildcardLeft, boolean wildcardRight) {
        return listSearchElementSelectCategories(str, cat);
    }

    @Override
    public Vector getGenericName(String drugID) {
        return new Vector();
    }

    @Override
    public Vector getInactiveDate(String pKey) {
        return new Vector();
    }

    @Override
    public Vector getForm(String pKey) {
        return new Vector();
    }

    @Override
    public Vector listDrugClass(Vector Dclass) {
        return new Vector();
    }

    /**
     * Compare a list of patient allergies against the ATC code of the drug
     * being prescribed and return a list of potential allergies against the drug.
     * Identifiers from old datasets cannot be depended on. If the drug allergy is missing
     * the ATC an ATC will be derived from the description of the drug.  Accuracy is not 100%.
     *
     * @param prescribedATCCode ATC code of drug currently being prescribed
     * @param allergies list of patient allergies
     * @return list of potential drug allergies
     */
    @Override
    public Vector getAllergyWarnings(String prescribedATCCode, Vector allergies) {

        Hashtable hashtable = new Hashtable();
        Vector warning = new Vector();
        Vector missing = new Vector();

        logger.info("Checking {} allergies against ATC code {}", allergies.size(), prescribedATCCode);

        for(Object allergyMap : allergies) {

            logger.info("Checking {}", allergyMap);

            Map allergy = new Hashtable<>((Hashtable) allergyMap);
            ArrayList<String> allergyATCList = new ArrayList<>();
            String allergyATC = (String) allergy.get("ATC");

            if( allergyATC == null || allergyATC.isEmpty() ) {
                // get ATC by drug description
                String allergyDescription = (String) allergy.get("description");
                if(allergyDescription != null) {
                    Set results = searchATCCodesByDrugname(allergyDescription);
                    allergyATCList.addAll(results);
                }
            } else {
                allergyATCList.add(allergyATC.trim());
            }

            prescribedATCCode = prescribedATCCode.trim();

            for(String allergyATCvalue : allergyATCList) {

//                String category = "";
                String substance = "empty";
                String ingredient = "empty";

                allergyATCvalue = allergyATCvalue.trim();

//                if(allergyATCvalue.length() > 2) {
//                    category = allergyATCvalue.substring(0, 3);
//                }

                if(allergyATCvalue.length() > 3) {
                    substance = allergyATCvalue.substring(0, 4);
                }

                if(allergyATCvalue.length() > 4) {
                    ingredient = allergyATCvalue.substring(0, 5);
                }

                if(
                        prescribedATCCode.equals(allergyATCvalue)
//                        || prescribedATCCode.startsWith(category)
                        || prescribedATCCode.startsWith(ingredient)
                        || prescribedATCCode.startsWith(substance)
                 ) {
                    warning.add(allergy.get("id"));
                }
            }
        }

        hashtable.put("warnings", warning);
        hashtable.put("missing", missing);

        Vector result = new Vector();
        result.add(hashtable);
        return result;
    }

    @Override
    public Vector getMadeGenericExample(String uuid, String gencode, boolean html) {
        return getDrugByDrugCode(uuid, gencode, html);
    }


    /**
     * Fetch brandname drugs by productId in NomprodPlus
     * Return product and all generic information for specific
     * drug signature.
     *
     * @param pKey
     * @param html
     * @return
     */
    @Override
    public Vector getDrug(String pKey, boolean html) {
        EntityManager em = JpaUtils.createEntityManager();
        String sql = "SELECT vig_fgenPlus.genericName" + language + " AS `name`, " +
                "vig_fgenPlus.genericName" + language + " AS `genericName`, " +
                "IFNULL(ATCcode, '') AS `atc`, " +
                "IFNULL(vig_fgenPlus.TMcodesOfCCDD, '') AS `tmCodesOfCCDD`, " +
                "CONCAT(vig_nomprodPlus.productName" + language + ", ' ', IFNULL( vig_nomprodPlus.strength" + language + ", ''), ' ', IFNULL( vig_nomprodPlus.form" + language + ", '')) AS `productName`, " +
                "vig_nomprodPlus.form" + language + " AS `drugForm`, " +
                "vig_fgenPlus.genericName" + language + " AS `components`, " +
                "vig_fgenPlus.GENcodeDetail AS `componentsGenCode`, " +
                "IFNULL(vig_generxPlus.ceRxRouteCode, '') AS `drugRoute`, " +
                "IFNULL(vig_generxPlus.strength" + language + ", '') AS `strength`, " +
                "IFNULL(vig_generxPlus.doseUnits, '') AS `unit`, " +
                "IFNULL(vig_generxPlus.uuid,'') AS `regional_identifier`, " +
                "vig_fgenPlus.GENcode  AS `gcnCode`, " +
                "IFNULL(vig_fgenPlus.uniqueIdentifier,'') AS `genericPlusUniqueId`, " +
                "vig_nomprodPlus.productId AS productID " +
                "FROM vig_nomprodPlus " +
                "JOIN vig_fgenPlus " +
                "ON (vig_nomprodPlus.GENcode = vig_fgenPlus.GENcode) " +
                "LEFT JOIN vig_generxPlus " +
                "ON (vig_generxPlus.uuid = CONCAT(vig_fgenPlus.GENcode, '##', IFNULL(vig_nomprodPlus.strength" + language + ",''),'##', IFNULL(vig_nomprodPlus.form" + language + ",''))) " +
                "WHERE productID = ?1";
        Query query = em.createNativeQuery(sql, Hashtable.class);
        query.setParameter(1, pKey);
        Vector<Object> returnRows = new Vector<>();

        List<Hashtable> results = query.getResultList();
        if(!results.isEmpty()) {
            Hashtable result = new Hashtable<>(results.get(0));
            parseComponents(result);

            /*
             * For whatever reason, the OSCAR interface needs the route info formatted this way.
             */
            String drugroute = (String) result.get("drugRoute");
            Vector routeVector = new Vector();
            routeVector.addElement(drugroute);
            result.put("drugRoute", routeVector);

            returnRows.addElement(result);
        }
        return returnRows;
    }

    /**
     * Fetch generic drugs from generxPlus by UUID.
     *
     * UUID: [Generic Code]##[Strength signature]##[Form]
     *
     * @param uuid
     * @param gencode
     * @param html
     * @return
     */
    @Override
    public Vector getDrugByDrugCode(String uuid, String gencode, boolean html) {
        EntityManager em = JpaUtils.createEntityManager();
        String sql = "SELECT IFNULL(vig_generxPlus.usualName" + language + ", vig_generxPlus.genericName" + language + ") AS `product`, " +
                "IFNULL(vig_fgenPlus.genericName" + language + ",'') AS `genericName`, " +
                "IFNULL(vig_fgenPlus.TMcodesOfCCDD, '') AS `tmCodesOfCCDD`, " +
                "IFNULL(vig_fgenPlus.ATCcode,'') AS `atc`, " +
                "CONCAT(vig_generxPlus.genericName" + language + ", ' ', IFNULL( vig_generxPlus.strength" + language + ", '')) AS `name`, " +
                "IFNULL(vig_fgenPlus.genericName" + language + ",'') AS `components`, " +
                "IFNULL(vig_fgenPlus.GENcodeDetail,'') AS `componentsGenCode`, " +
                "vig_generxPlus.lowercaseForm" + language + " AS `drugForm`, " +
                "vig_generxPlus.ceRxRouteCode AS `drugRoute`, " +
                "vig_generxPlus.strength" + language + " AS `strength`, " +
                "IFNULL(vig_generxPlus.doseUnits, '') AS `unit`, " +
                "IFNULL(vig_generxPlus.uuid,'') AS `regional_identifier`, " +
                "IFNULL(vig_fgenPlus.GENcode,'')  AS `gcnCode`, " +
                "IFNULL(vig_fgenPlus.uniqueIdentifier,'') AS `genericPlusUniqueId`" +
                "FROM vig_generxPlus " +
                "LEFT JOIN vig_fgenPlus " +
                "ON (vig_generxPlus.GENcode = vig_fgenPlus.GENcode) " +
                "WHERE vig_generxPlus.uuid LIKE ?1 " +
                " UNION " +
                "SELECT COALESCE(vig_generxPlus.usualName" + language + ", vig_generxPlus.genericName" + language + ", '') AS `product`, " +
                "vig_fgenPlus.genericName" + language + " AS `genericName`, " +
                "IFNULL(vig_fgenPlus.TMcodesOfCCDD, '') AS `tmCodesOfCCDD`, " +
                "vig_fgenPlus.ATCcode AS `atc`, " +
                "CONCAT(IFNULL(vig_generxPlus.genericName" + language + ", vig_fgenPlus.genericName" + language + "), ' ', IFNULL( vig_generxPlus.strength" + language + ", '')) AS `name`, " +
                "vig_fgenPlus.genericName" + language + " AS `components`, " +
                "vig_fgenPlus.GENcodeDetail AS `componentsGenCode`, " +
                "IFNULL(vig_generxPlus.lowercaseForm" + language + ",'') AS `drugForm`, " +
                "IFNULL(vig_generxPlus.ceRxRouteCode,'') AS `drugRoute`, " +
                "IFNULL(vig_generxPlus.strength" + language + ",'') AS `strength`, " +
                "IFNULL(vig_generxPlus.doseUnits, '') AS `unit`, " +
                "IFNULL(vig_generxPlus.uuid, vig_fgenPlus.uniqueIdentifier) AS `regional_identifier`, " +
                "vig_fgenPlus.GENcode  AS `gcnCode`, " +
                "IFNULL(vig_fgenPlus.uniqueIdentifier,'') AS `genericPlusUniqueId`" +
                "FROM vig_fgenPlus " +
                "LEFT JOIN vig_generxPlus " +
                "ON (vig_generxPlus.GENcode = vig_fgenPlus.GENcode) " +
                "WHERE vig_fgenPlus.uniqueIdentifier = ?1 ";

        Query query = em.createNativeQuery(sql, Hashtable.class);
        query.setParameter(1, uuid);
        Vector<Object> returnRows = new Vector<>();

        List<Hashtable> results = query.getResultList();
        if(!results.isEmpty()) {
            Hashtable result = new Hashtable<>(results.get(0));
            parseComponents(result);

            /*
             * For whatever reason, the OSCAR interface needs the route info formatted this way.
             */
            String drugroute = (String) result.get("drugRoute");
            Vector<Object> routeVector = new Vector<>();
            routeVector.addElement(drugroute);
            result.put("drugRoute", routeVector);

            returnRows.addElement(result);
        }
        return returnRows;
    }

    @Override
    public Vector getAllergyClasses(Vector allergies) {
        return new Vector();
    }

    @Override
    public Vector getTcATC(String atc) {
        return new Vector();
    }

    private void parseComponents(Hashtable result) {
        Vector<Object> components = new Vector<>();
        String[] names = ((String) result.get("components")).split("\\+");
        String[] codes = ((String) result.get("componentsGenCode")).split("\\+");
        String[] strengths = ((String) result.get("strength")).split("\\+");
        String resultCode = (String) result.get("regional_identifier");
        String unit = (String) result.get("unit");
        if(unit == null) {
            unit = "";
        }
        String genericName = (String) result.get("genericName");

        for(int i = 0; i < names.length; i++) {
            Hashtable component = new Hashtable();
            component.put("name", names[i].trim());
            String code = codes[i].trim();
            if(code.equalsIgnoreCase(genericName)) {
                component.put("code", resultCode);
            } else {
                component.put("code", codes[i].trim());
            }

            // the strength is likely empty if the array length is
            // not the same as the component array length
            if(strengths.length == names.length) {
                String strength = strengths[i];
                component.put("strength", strength.replaceAll(unit, "").trim());
            } else {
                component.put("strength", "");
            }

            component.put("unit", unit);
            components.addElement(component);
        }

        result.put("components", components);
        // return result;
    }

    /**
     * Searches in the Vigilance database are formed with 3 parameters separated with a comma:
     * [product/generic name], [strength], [form]
     * ie
     * tylenol, 500, tablet
     */
    /**
     * The first word of the search term, used to rank results by how well they match what was
     * typed.
     * <p>
     * This has to be the first word rather than the whole term, because the ranking compares it
     * against a product name with "=" and "LIKE ...%": "amoxicillin 500" matches no name either
     * way, which would drop every row into the same tier and leave the results in name order.
     * Ranking on "amoxicillin" keeps the plain strengths above the branded variants.
     *
     * @param keyword String the search term as typed by the user
     * @return String the first word long enough to be indexed, or the term with its punctuation
     *         removed when it has no such word
     */
    private String firstSearchWord(String keyword) {
        for (String word : NON_WORD.split(keyword.trim())) {
            if (word.length() >= MINIMUM_TOKEN_LENGTH) {
                return word;
            }
        }
        return NON_WORD.matcher(keyword.trim()).replaceAll("");
    }

    /**
     * Splits a user's search term into FULLTEXT boolean-mode operands, requiring every word.
     * <p>
     * {@link #parseParameters} splits on commas only, so "apo atorvastatin" became the single
     * operand "+apo atorvastatin*", which asks for "apo" and merely prefers "atorvastatin":
     * every product from that manufacturer came back. Splitting on whitespace as well makes
     * both words required.
     * <p>
     * Every character that is not a letter or digit separates words, rather than a chosen list
     * of separators. FULLTEXT indexes punctuation as a word separator anyway, so this matches
     * how the data is stored: "INSULIN-ASPART-RAPID" is three indexed words. It also means a
     * stray operator character cannot reach the query, where it would either be read as an
     * operator ("pms-amoxicillin" as "pms AND NOT amoxicillin") or fail outright ("apo-"
     * becoming the invalid "+apo-*").
     * <p>
     * Words below {@link #MINIMUM_TOKEN_LENGTH} are dropped because the index does not hold
     * them, and requiring one with a wildcard matches far too much: "children's" split into
     * "children" and "s" would ask for a word beginning with "s" as well.
     * <p>
     * Operators the caller wrote are honoured per token rather than diverting the whole term:
     * a leading "+" or "-" applies to every word in that token, and a quoted phrase is passed
     * whole. Handing the term to {@link #parseParameters} instead, as this method first did,
     * meant an operator anywhere disabled the word split for everything else, so
     * "amoxicillin 500 -penta" returned the same rows as "amoxicillin -penta". It was also
     * positionally unstable, because {@link #addOperators} appends its wildcard once at the
     * end: adding a trailing token silently un-wildcarded the one before it.
     * <p>
     * "OR" is not carved out, because MySQL boolean mode has no such operator. It was only
     * ever an optional word, so "apo OR pms" returned exactly the rows holding "apo".
     * <p>
     * This is deliberately separate from {@link #parseParameters}, which is shared with
     * {@link #listSearchAll} on the allergy-checking path. Requiring every word there could
     * narrow an allergy description enough to drop a warning, so that parsing is left alone.
     *
     * @param keyword String the search term as typed by the user
     * @return String the boolean-mode expression to match against
     */
    private String parseSearchParameters(String keyword) {
        StringBuilder parameterBuilder = new StringBuilder();
        Matcher tokens = SEARCH_TOKEN.matcher(keyword.toLowerCase());

        while (tokens.find()) {
            String token = tokens.group();
            String sign = "+";
            if (token.startsWith("-")) {
                sign = "-";
                token = token.substring(1);
            } else if (token.startsWith("+")) {
                token = token.substring(1);
            }

            if (isQuotedPhrase(token)) {
                // A phrase goes to the query whole: it matches adjacent words, and neither the
                // word minimum nor a wildcard applies inside quotes.
                parameterBuilder.append(sign).append(token).append(" ");
                continue;
            }

            for (String word : NON_WORD.split(token)) {
                if (word.length() >= MINIMUM_TOKEN_LENGTH) {
                    parameterBuilder.append(sign).append(word).append("*").append(" ");
                }
            }
        }

        if (parameterBuilder.length() == 0) {
            // Every word was below the index's minimum length, as in "b-12". Retry with the
            // punctuation removed rather than searching for nothing, so "b-12" finds "B12".
            String collapsed = NON_WORD.matcher(keyword.toLowerCase()).replaceAll("");
            if (!collapsed.isEmpty()) {
                parameterBuilder.append("+").append(collapsed).append("*");
            }
        }

        return parameterBuilder.toString().trim();
    }

    /**
     * True for a token that is a complete quoted phrase with something in it. An unbalanced
     * quote is not one, and is left to the word split, which discards the stray character.
     *
     * @param token String one whitespace-delimited token, any leading sign already removed
     * @return boolean true when the token should be passed to the query as a phrase
     */
    private boolean isQuotedPhrase(String token) {
        return token.length() > 2
                && token.startsWith("\"")
                && token.endsWith("\"")
                && HAS_WORD_CHARACTER.matcher(token.substring(1, token.length() - 1)).matches();
    }


    private String parseParameters(String keyword) {
        StringTokenizer stringTokenizer = new StringTokenizer(keyword, ",", false);
        StringBuilder parameterBuilder = new StringBuilder();
        if(stringTokenizer.countTokens() > 0) {
            // keyword strength and form
            while (stringTokenizer.hasMoreTokens()) {
                addOperators(stringTokenizer.nextToken().trim().toLowerCase(), parameterBuilder);
            }
        } else {
            addOperators(keyword.trim().toLowerCase(), parameterBuilder);
        }
        return parameterBuilder.toString().trim();
    }

    private void addOperators(String parameter, StringBuilder builder) {
        if(!parameter.startsWith("+")&&!parameter.startsWith("-")) {
            builder.append("+");
        }
        if(!parameter.endsWith("*")) {
            if(parameter.endsWith("\"")) {
                builder.append(parameter);
            } else {
                builder.append(parameter);
                builder.append("*");
            }
        } else {
            builder.append(parameter);
        }
        builder.append(" ");
    }
}
