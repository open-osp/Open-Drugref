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

@Repository
public class VigilanceDao implements TablesDao, Serializable {
    Logger logger = MiscUtils.getLogger();
    /*
     * alternate is French which was not considered as a choice during initial development
     * Future development should consider the system language French or English
     */
    private static String language = "English";

    @Override
    public String identify() {
        return "";
    }

    @Override
    public String version() {
        return "";
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
        Hashtable<String, Object> singleResult = (Hashtable) query.getSingleResult();
        CdDrugSearch cdDrugSearch = new CdDrugSearch();
        if(singleResult != null) {
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
        if(ingredientOnly) {
            return listSearchIngredient(keyword);
        } else {
            return listSearchAll(keyword);
        }
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

        sql.append("SELECT uuid AS `id`, ");
        sql.append("CAST(?3 AS NCHAR) AS `category`,");
        sql.append("GENcode AS `drugCode`,");
        sql.append("CONCAT(");
        sql.append("genericName").append(language);
        sql.append(", ").append("' ', ");
        sql.append("IFNULL(");
        sql.append("strength").append(language);
        sql.append(",'')");
        sql.append(", ").append("' ', ");
        sql.append("IFNULL(");
        sql.append("form").append(language);
        sql.append(",'')");
        sql.append(") AS `name` ");
        sql.append("FROM vig_generxPlus ");
        sql.append("WHERE MATCH(");
        sql.append("lowercaseGenericName").append(language).append(", ");
        sql.append("strength").append(language).append(", ");
        sql.append("form").append(language);
        sql.append(") ");
        sql.append("AGAINST (?1 IN BOOLEAN MODE) ");

        Query query = em.createNativeQuery(sql.toString(), Hashtable.class);
        String parameters = parseParameters(keyword);
        query.setParameter(1, parameters);
        query.setParameter(2, Category.BRAND.getOrdinal());
        query.setParameter(3, Category.AI_GENERIC.getOrdinal());
        List results = query.getResultList();
        Vector<Hashtable<String, Object>> resultList = new Vector<Hashtable<String,Object>>(results);
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
                    allergyATCList.addAll(searchATCCodesByDrugname(allergyDescription));
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
        Hashtable result = new Hashtable<>((Hashtable) query.getSingleResult());
        parseComponents(result);

        /*
         * For whatever reason, the OSCAR interface needs the route info formatted this way.
         */
        String drugroute = (String) result.get("drugRoute");
        Vector routeVector = new Vector();
        routeVector.addElement(drugroute);
        result.put("drugRoute", routeVector);

        returnRows.addElement(result);
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
        Hashtable result = new Hashtable<>((Hashtable) query.getSingleResult());
        parseComponents(result);

        /*
         * For whatever reason, the OSCAR interface needs the route info formatted this way.
         */
        String drugroute = (String) result.get("drugRoute");
        Vector<Object> routeVector = new Vector<>();
        routeVector.addElement(drugroute);
        result.put("drugRoute", routeVector);

        Vector<Object> returnRows = new Vector<>();
        returnRows.addElement(result);
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
