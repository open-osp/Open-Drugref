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
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

@Repository
public class VigilanceDao implements TablesDao {

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

    /**
     * Fetches a single search result based on the unique identifier of a drug.
     */
    @Override
    public CdDrugSearch getSearchedDrug(int id) {
        EntityManager em = JpaUtils.createEntityManager();
        String sql = "SELECT NomprodPlus.productID AS id, NomprodPlus.GENcode AS drugCode, CAST(?1 AS NCHAR) AS category, " +
                "NomprodPlus.ProductNameEnglish AS name " +
                "FROM NomprodPlus " +
                "WHERE NomprodPlus.productID = ?2";
        Query query = em.createNativeQuery(sql, Hashtable.class);
        query.setParameter(2, id);
        query.setParameter(1, Category.BRAND.getOrdinal());
        Hashtable<String, Object> singleResult = (Hashtable) query.getSingleResult();
        CdDrugSearch cdDrugSearch = new CdDrugSearch();
        cdDrugSearch.setId((Integer) singleResult.get("id"));
        cdDrugSearch.setDrugCode((String) singleResult.get("drugCode"));
        cdDrugSearch.setCategory(Integer.parseInt((String) singleResult.get("category")));
        cdDrugSearch.setName((String) singleResult.get("name"));
        return cdDrugSearch;
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
    public Vector listSearchElement4(String keyword, boolean rightOnly){
        EntityManager em = JpaUtils.createEntityManager();
        Assert.notNull(keyword, "Search value cannot be null.");
        String searchstring = keyword.trim();
                //.replaceAll("\\p{Punct}", "");
        // searchstring += "*";
        String sql = "SELECT NomprodPlus.productID AS id, CAST(?2 AS NCHAR) AS category," +
                "NomprodPlus.GENcode AS drugCode, NomprodPlus.productNameEnglish AS name " +
                "FROM NomprodPlus WHERE MATCH(nomprodPlus.productNameEnglish) AGAINST (?1 IN BOOLEAN MODE)" +
                " UNION " +
                "SELECT FgenPlus.uniqueIdentifier AS id, CAST(?3 AS NCHAR) AS category," +
                "FgenPlus.GENcode AS drugCode, FgenPlus.genericNameEnglish AS name " +
                "FROM FgenPlus WHERE MATCH(FgenPlus.genericNameEnglish, fgenPlus.genericSimpleNameEnglish) AGAINST (?1 IN BOOLEAN MODE)" +
                "LIMIT 50";
        // score > 2
        // boolean mode + - * ""
        Query query = em.createNativeQuery(sql, Hashtable.class);
        query.setParameter(1, searchstring);
        query.setParameter(2, Category.BRAND.getOrdinal());
        query.setParameter(3, Category.AI_GENERIC.getOrdinal());
        Vector<Hashtable<String, Object>> resultList = new Vector<>(query.getResultList());
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
        return null;
    }

    @Override
    public Vector getMadeGenericExample(String groupno, String formCode, boolean html) {
        return null;
    }


    /**
     * Fetches a drug from the FgenPlus (Generics) table based on Category 18
     */
    @Override
    public Vector getDrug(String pKey, boolean html) {
        EntityManager em = JpaUtils.createEntityManager();
        String sql = "SELECT FgenPlus.genericNameEnglish AS name, " +
                "FgenPlus.ATCcode AS atc, " +
                "NomprodPlus.productID AS regional_identifier, " +
                "NomprodPlus.productNameEnglish AS product, " +
                "NomprodPlus.formEnglish AS drugForm, " +
                "FgenPlus.genericSimpleNameEnglish AS components " +
                "FROM NomprodPlus " +
                "INNER JOIN FgenPlus " +
                "ON (FgenPlus.GENcode = NomprodPlus.GENcode) " +
                "WHERE NomprodPlus.productID = ?1";
        Query query = em.createNativeQuery(sql, Hashtable.class);
        query.setParameter(1, pKey);
        Vector returnRows = new Vector();
        Vector components = new Vector();
        Hashtable result = (Hashtable) query.getSingleResult();
        components.addElement(result.get("components"));
        result.put("components", components);
        returnRows.addElement(result);
        return returnRows;
    }

    @Override
    public Vector getDrugByDrugCode(String pKey, String formCode, boolean html) {
        return null;
    }

    @Override
    public Vector getAllergyClasses(Vector allergies) {
        return null;
    }

    @Override
    public Vector getTcATC(String atc) {
        return null;
    }
}
