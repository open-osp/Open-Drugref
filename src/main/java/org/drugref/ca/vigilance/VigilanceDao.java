package org.drugref.ca.vigilance;

import org.drugref.Category;
import org.drugref.ca.dpd.CdDrugProduct;
import org.drugref.ca.dpd.CdDrugSearch;
import org.drugref.ca.dpd.CdTherapeuticClass;
import org.drugref.ca.TablesDao;
import org.drugref.util.JpaUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.*;

@Repository
public class VigilanceDao implements TablesDao, Serializable {

    @Override
    public String identify() {
        return null;
    }

    @Override
    public String version() {
        return null;
    }

    @Override
    public Vector list_available_services() {
        return null;
    }

    @Override
    public Hashtable list_capabilities() {
        return null;
    }

    @Override
    public Object fetch(String attribute, Vector key, Vector services, boolean feelingLucky) {
        return null;
    }

    @Override
    public Integer getDrugIdFromDIN(String DIN) {
        return null;
    }

    @Override
    public String getDINFromDrugId(Integer drugId) {
        return null;
    }

    @Override
    public Integer getDrugpKeyFromDIN(String DIN) {
        return null;
    }

    @Override
    public Integer getDrugpKeyFromDrugId(Integer drugId) {
        return null;
    }

    @Override
    public List<Integer> getInactiveDrugs() {
        return null;
    }

    @Override
    public CdDrugProduct getDrugProduct(String drugcode) {
        return null;
    }

    @Override
    public CdTherapeuticClass getATCCodes(String drugcode) {
        return null;
    }

    /*
     * alternate is French which was not considered as a choice during initial development
     * Future development should consider the system language French or English
     */
    private static String language = "English";

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
                "WHERE uuid = ?1";
        Query query = em.createNativeQuery(sql, Hashtable.class);
        query.setParameter(1, id);
        query.setParameter(2, Category.BRAND.getOrdinal());
        query.setParameter(3, Category.AI_GENERIC.getOrdinal());
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
        return null;
    }

    @Override
    public List<String> findLikeDins(String din) {
        return null;
    }

    @Override
    public List listDrugsbyAIGroup(String aiGroup) {
        return null;
    }

    @Override
    public List<CdDrugSearch> listDrugsbyAIGroup2(String aiGroup) {
        return null;
    }

    @Override
    public Vector listSearchElement2(String str) {
        return listSearchElement4(str, false);
    }

    @Override
    public String getFirstDinInAIGroup(String aiGroupNo) {
        return null;
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

        sql.append("LIMIT 50");

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
        return null;
    }

    @Override
    public Vector listBrandsFromElement(String drugID) {
        return null;
    }

    @Override
    public Vector listSearchElementSelectCategories(String str, Vector cat) {
        return null;
    }

    @Override
    public Vector listSearchElementSelectCategories(String str, Vector cat, boolean wildcardLeft, boolean wildcardRight) {
        return null;
    }

    @Override
    public Vector getGenericName(String drugID) {
        return null;
    }

    @Override
    public Vector getInactiveDate(String pKey) {
        return null;
    }

    @Override
    public Vector getForm(String pKey) {
        return null;
    }

    @Override
    public Vector listDrugClass(Vector Dclass) {
        return null;
    }

    @Override
    public Vector getAllergyWarnings(String atcCode, Vector allergies) {
        for(Object allergyMap : allergies) {
            Map allergy = new Hashtable<>((Hashtable) allergyMap);
            String type = (String) allergy.get("type");
            String description = (String) allergy.get("description");
            String id = (String) allergy.get("id");

        }
        return null;
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
                "IFNULL(vig_fgenPlus.TMcodesOfCCDD, '') AS `regional_identifier`, " +
                "CONCAT(vig_nomprodPlus.productName" + language + ", ' ', IFNULL( vig_nomprodPlus.strength" + language + ", ''), ' ', IFNULL( vig_nomprodPlus.form" + language + ", '')) AS `productName`, " +
                "vig_nomprodPlus.form" + language + " AS `drugForm`, " +
                "vig_fgenPlus.genericName" + language + " AS `components`, " +
                "vig_fgenPlus.GENcodeDetail AS `componentsGenCode`, " +
                "IFNULL(vig_generxPlus.ceRxRouteCode, '') AS `drugRoute`, " +
                "IFNULL(vig_generxPlus.strength" + language + ", '') AS `strength`, " +
                "IFNULL(vig_generxPlus.doseUnits, '') AS `unit`, " +
                "IFNULL(vig_generxPlus.uuid,'') AS `genrxId`, " +
                "vig_fgenPlus.GENcode  AS `genericId`, " +
                "vig_fgenPlus.uniqueIdentifier AS `genericUniqueId`, " +
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
                "vig_fgenPlus.genericName" + language + " AS `genericName`, " +
                "IFNULL(vig_fgenPlus.TMcodesOfCCDD, '') AS `regional_identifier`, " +
                "vig_fgenPlus.ATCcode AS `atc`, " +
                "CONCAT(vig_generxPlus.genericName" + language + ", ' ', IFNULL( vig_generxPlus.strength" + language + ", '')) AS `name`, " +
                "vig_fgenPlus.genericName" + language + " AS `components`, " +
                "vig_fgenPlus.GENcodeDetail AS `componentsGenCode`, " +
                "vig_generxPlus.lowercaseForm" + language + " AS `drugForm`, " +
                "vig_generxPlus.ceRxRouteCode AS `drugRoute`, " +
                "vig_generxPlus.strength" + language + " AS `strength`, " +
                "IFNULL(vig_generxPlus.doseUnits, '') AS `unit`, " +
                "IFNULL(vig_generxPlus.uuid,'') AS `genrxId`, " +
                "vig_fgenPlus.GENcode  AS `genericId`, " +
                "vig_fgenPlus.uniqueIdentifier AS `genericUniqueId`" +
                "FROM vig_generxPlus " +
                "JOIN vig_fgenPlus " +
                "ON (vig_generxPlus.GENcode = vig_fgenPlus.GENcode) " +
                "WHERE vig_generxPlus.uuid = ?1";
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
        return null;
    }

    @Override
    public Vector getTcATC(String atc) {
        return null;
    }

    private Hashtable parseComponents(Hashtable result) {
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
        return result;
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
