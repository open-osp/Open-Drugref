/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drugref.ca.dpd;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import java.util.Vector;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.drugref.util.JpaUtils;
import org.drugref.util.MiscUtils;
import org.springframework.stereotype.Repository;

/**
 *  org.drugref.DrugrefTest
 * @author jackson
 */
@Repository
public class TablesDao {

    public TablesDao() {
    }

    public List listSearchElement(String str) {
        EntityManager em = JpaUtils.createEntityManager();

        str = str.replace(",", " ");
        str = str.replace("'", "");
        String[] strArray = str.split("\\s+");

        for (int i = 0; i < strArray.length; i++) {
            System.out.println(strArray[i]);
        }

        String queryStr = "select cds.id, cds.category, cds.name from CdDrugSearch cds where ";

        for (int i = 0; i < strArray.length; i++) {
            queryStr = queryStr + "upper(cds.name) like " + "'" + "%" + strArray[i].toUpperCase() + "%" + "'";
            if (i < strArray.length - 1) {
                queryStr = queryStr + " and ";
            }
        }
        List results = new ArrayList();
        System.out.println(queryStr);
        try {
             
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            Query query = em.createQuery(queryStr);
            System.out.println("333333333333333");
            results = query.getResultList();
            System.out.println("22222222222222222222");
            tx.commit();
            
        } finally {
            JpaUtils.close(em);
        }
        System.out.println(results);
        return (results);
    }

    public List listSearchElementRoute(String str, String route) {
        EntityManager em = JpaUtils.createEntityManager();

        str = str.replace(",", " ");
        str = str.replace("'", "");
        String[] strArray = str.split("\\s+");

        route = route.replace(",", " ");
        route = route.replace("'", "");
        String[] routeArray = route.split("\\s+");

//for test only
        for (int i = 0; i < strArray.length; i++) {
            System.out.println(strArray[i]);
        }
        for (int i = 0; i < routeArray.length; i++) {
            System.out.println(routeArray[i]);
        }

        String queryTwo = "select cds.id, cds.category, cds.name from CdDrugSearch cds where ";

        for (int i = 0; i < strArray.length; i++) {
            queryTwo = queryTwo + " upper(cds.name) like " + "'" + "%" + strArray[i].toUpperCase() + "%" + "' ";
            if (i < strArray.length - 1) {
                queryTwo = queryTwo + " and ";
            }
        }

        String queryOne = "select cr.drugCode from CdRoute cr where ";
        for (int i = 0; i < routeArray.length; i++) {
            queryOne = queryOne + "upper(cr.routeOfAdministration) like " + "'" + "%" + routeArray[i].toUpperCase() + "%" + "'";
            if (i < routeArray.length - 1) {
                queryOne = queryOne + " or ";
            }
        }

        List resultOne = null;
        List resultTwo = null;
        System.out.println("queryOne :" + queryOne);
        try {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            Query queryFirst = em.createQuery(queryOne);
            //@SuppressWarnings("unchecked")
            resultOne = queryFirst.getResultList();
            ArrayList<String> strListOne = new ArrayList<String>();

            for (int i = 0; i < resultOne.size(); i++) {
                String element = resultOne.get(i).toString();
                strListOne.add(element);
            }
            //System.out.println(strListOne);


            System.out.println("queryTwo: " + queryTwo);
            Query querySecond = em.createQuery(queryTwo + " and cds.drugCode in (:array) order by cds.name");
            querySecond.setParameter("array", strListOne);
            resultTwo = querySecond.getResultList();
            tx.commit();
        } finally {
            JpaUtils.close(em);
        }
        System.out.println("results:" + resultTwo);
        return (resultTwo);
    }

    public List listBrandsFromElement(String drugID) {
        EntityManager em = JpaUtils.createEntityManager();

        String drugCode = "";
        Integer category;
        List results = null;

        try {
            EntityTransaction tx = em.getTransaction();
            tx.begin();


            CdDrugSearch cdsResult = (CdDrugSearch) em.createQuery("select cds from CdDrugSearch cds where cds.id = " + drugID).getSingleResult();
            System.out.println("adfffffffffffffffffffff");

            System.out.println(cdsResult.getDrugCode() + " -- " + cdsResult.getName() + " :: " + cdsResult.getCategory());

            if (cdsResult != null) {
                drugCode = cdsResult.getDrugCode();
                category = cdsResult.getCategory();
            } else {
                List nullList = null;
                return nullList;
            }

            if (category == 8) {

                Query queryOne = em.createQuery("select tc from CdTherapeuticClass tc where tc.tcAtcNumber=(:drugCode)");
                queryOne.setParameter("drugCode", Integer.parseInt(drugCode.trim()));
                List<CdTherapeuticClass> listOne = queryOne.getResultList();

                for (int i = 0; i < listOne.size(); i++) {
                    Query query = em.createQuery("select sd.name,sd.category ,sd.id   from CdDrugSearch sd where (:tcDrugCode)= sd.drugCode order by sd.name");
                    query.setParameter("tcDrugCode", listOne.get(i).getDrugCode());
                    List resultTwo=query.getResultList();
                    results.add(resultTwo);
                }
            } else if (category == 10) {
                Query queryOne = em.createQuery("select tc from CdTherapeuticClass tc where tc.tcAhfsNumber=(:drugCode)");
                queryOne.setParameter("drugCode", Integer.parseInt(drugCode.trim()));
                List<CdTherapeuticClass> listOne = queryOne.getResultList();

                for (int i = 0; i < listOne.size(); i++) {
                    Query query = em.createQuery("select sd.name,sd.category ,sd.id   from CdDrugSearch sd where (:tcDrugCode)= sd.drugCode order by sd.name");
                    query.setParameter("tcDrugCode", listOne.get(i).getDrugCode());
                    List resultTwo=query.getResultList();
                    results.add(resultTwo);
                }
            } else {//category=11 or 12
                System.out.println("aaaaaaa");
                Query query = em.createQuery("select sd.name,sd.category ,sd.id     from LinkGenericBrand lgb, CdDrugSearch sd where lgb.id = (:drugCode) and lgb.drugCode = sd.drugCode order by sd.name");
                query.setParameter("drugCode", Integer.parseInt(drugCode.trim()));
                results = query.getResultList();
            }
        } finally {
            JpaUtils.close(em);
        }
        System.out.println(results);
        return (results);
    }

    public List listSearchElementSelectCategories(String str, Vector cat) {
        EntityManager em = JpaUtils.createEntityManager();

        str = str.replace(",", " ");
        str = str.replace("'", "");
        String[] strArray = str.split("\\s+");

        for (int i = 0; i < strArray.length; i++) {
            System.out.println(strArray[i]);
        }

        String queryStr = "select cds.id, cds.category, cds.name from CdDrugSearch cds where ";

        for (int i = 0; i < strArray.length; i++) {
            queryStr = queryStr + "upper(cds.name) like " + "'" + "%" + strArray[i].toUpperCase() + "%" + "'";
            queryStr = queryStr + " and ";
        }

        queryStr = queryStr + "(";

        for (int i = 0; i < cat.size(); i++) {
            queryStr = queryStr + "cds.category= " + cat.get(i);
            if (i < (cat.size() - 1)) {
                queryStr = queryStr + " or ";
            }
        }
        queryStr = queryStr + ") order by cds.category, cds.name";
        System.out.println(queryStr);

        List results = null;

        try {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            Query query = em.createQuery(queryStr);
            //@SuppressWarnings("unchecked")
            results = query.getResultList();
            tx.commit();
        } finally {
            JpaUtils.close(em);
        }
        System.out.println(results);
        return (results);
    }

    public List getGenericName(String drugID) {
        EntityManager em = JpaUtils.createEntityManager();
        List results = null;

        EntityTransaction tx = em.getTransaction();
        tx.begin();


        Query queryOne = em.createQuery("select lgb.id from LinkGenericBrand lgb ,CdDrugSearch cds where lgb.drugCode = cds.drugCode and cds.id=(:ID)");
        queryOne.setParameter("ID", Integer.parseInt(drugID));
        List resultDrugCode = queryOne.getResultList();

        System.out.println("adfffffffffffffffffffff");

        if (resultDrugCode != null) {
            Query queryTwo = em.createQuery("select cds.name,cds.category,cds.id from CdDrugSearch cds where cds.drugCode in (:drugCodeList)");
            queryTwo.setParameter("drugCodeList", MiscUtils.toStringArrayList(resultDrugCode));
            results = queryTwo.getResultList();
            JpaUtils.close(em);
            System.out.println(results);
            return results;
        } else {
            List nullList = null;
            return nullList;
        }
    }

    //change to return Hashtable??
    public List getForm(Integer pKey) {
        EntityManager em = JpaUtils.createEntityManager();
        List results = null;

        EntityTransaction tx = em.getTransaction();
        tx.begin();


        Query queryOne = em.createQuery("select cds.drugCode from CdDrugSearch cds where cds.id = (:ID)");
        queryOne.setParameter("ID", pKey);
        List resultDrugCode = queryOne.getResultList();

        System.out.println("adfffffffffffffffffffff" + resultDrugCode);

        if (resultDrugCode != null) {
            Query queryTwo = em.createQuery("select cf.pharmCdFormCode,cf.pharmaceuticalCdForm from CdForm cf where cf.drugCode in (:drugCodeList)");
            System.out.println("oooooooooo ");
            queryTwo.setParameter("drugCodeList", MiscUtils.toIntegerArrayList(resultDrugCode));
            System.out.println("pppppppppppp");
            results = queryTwo.getResultList();
            JpaUtils.close(em);
            System.out.println(results);
            return results;
        } else {
            List nullList = null;
            return nullList;
        }
    }

    public List listDrugClass(Vector Dclass) {
        EntityManager em = JpaUtils.createEntityManager();
        List results = new ArrayList();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        //example
        Query queryOne = em.createQuery("select cds from  CdDrugSearch cds where cds.id =123 or cds.id=456");
        List<CdDrugSearch> listOne = queryOne.getResultList();
        System.out.println("zzzzzzzzzzzzzz     " + listOne.size());
        for (int i = 0; i < listOne.size(); i++) {
            Query queryTwo = em.createQuery("select cds2 from CdDrugSearch cds2 where cds2.drugCode in (select tc.tcAhfsNumber from   CdTherapeuticClass tc where tc.drugCode = (:cdIntDrugCode))");
            queryTwo.setParameter("cdIntDrugCode", Integer.parseInt(listOne.get(i).getDrugCode().trim()));
            CdDrugSearch resultTwo = (CdDrugSearch) queryTwo.getSingleResult();
            String row = listOne.get(i).getId().toString() + ", " + resultTwo.getDrugCode() + ", " + resultTwo.getName();
            //System.out.println("rowwwwwwwwwwwww      "+row);
            // System.out.println("rowwwwwwwwwwwww      "+i);
            Vector strVec = new Vector();
            strVec.add(row);
            results.add(strVec);
            //System.out.println("rowwwwwwwwwwwww      "+i);
            }

        JpaUtils.close(em);
        return results;
    }

    public List getAllergyWarnings(String atcCode, Vector allergies) {
        List results = new ArrayList();
        if (atcCode.matches("") || atcCode.matches("null")) {
            return results;
        }
        EntityManager em = JpaUtils.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Enumeration e = allergies.elements();
        while (e.hasMoreElements()) {
            Hashtable alleHash = new Hashtable((Hashtable) e.nextElement());
            String aType = (String) alleHash.get("type");
            String aDesc = (String) alleHash.get("description");
            String aId = (String) alleHash.get("id");
            if (aType.matches("8")) {

                Query query = em.createQuery("select tc.tcAtcNumber from CdTherapeuticClass tc where tc.tcAtcNumber= (:atcCode) and tcAtc=(:aDesc)");
                query.setParameter("atcCode", atcCode);
                query.setParameter("aDesc", aDesc);

                List resultTcAtcNumber = query.getResultList();
                if (resultTcAtcNumber.size() > 0) {
                    results.add(aId);
                }

            } else if (aType.matches("10")) {
                Query query = em.createQuery("select tc.tcAtcNumber from CdTherapeuticClass tc where tc.tcAtcNumber=(:atcCode) and tc.tcAhfs=(:aDesc)");
                query.setParameter("atcCode", atcCode);
                query.setParameter("aDesc", aDesc);
                List resultTcAtcNumber = query.getResultList();
                if (resultTcAtcNumber.size() > 0) {
                    results.add(aId);
                }
            } else if (aType.matches("14")) {
                System.out.println("aType=14 is not implemented yet");
            } else if (aType.matches("11") || aType.matches("12")) {
                Query query = em.createQuery("select tc.tcAtcNumber from CdDrugSearch cds, LinkGenericBrand lgb, CdTherapeuticClass tc where tc.tcAtcNumber =(:atcCode) and cds.name=(:aDesc) " +
                        "and cds.drugCode=lgb.id and lgb.drug_code=tc.drug_code");
                query.setParameter("atcCode", atcCode);
                query.setParameter("aDesc", aDesc);
                List resultTcAtcNumber = query.getResultList();
                if (resultTcAtcNumber.size() > 0) {
                    results.add(aId);
                }
            } else if (aType.matches("13")) {
                Query query = em.createQuery("select tc.tcAtcNumber from CdDrugSearch cds,CdTherapeuticClass tc where tc.tcAtcNumber =(:atcCode) and cds.name=(:aDesc) and cds.drugCode=tc.drugCode ");
                query.setParameter("atcCode", atcCode);
                query.setParameter("aDesc", aDesc);
                List resultTcAtcNumber = query.getResultList();
                if (resultTcAtcNumber.size() > 0) {
                    results.add(aId);
                }
            } else {
                System.out.println("No Match.");
            }
            JpaUtils.close(em);

        }
        return results;
    }

    public List getDrug(String pKey, Boolean html) {
        String productId = "";
        String origId = pKey;

        EntityManager em = JpaUtils.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Query queryDrugCode = em.createQuery("select cds.drug_code from cd_drug_search where cds.id=(:pKey)");
        queryDrugCode.setParameter("pKey", pKey);
        String resultDrugCode = "";
        resultDrugCode = (String) queryDrugCode.getSingleResult();
        if (!resultDrugCode.matches("")) {
            pKey = resultDrugCode;
        }
        Query queryName = em.createQuery("select cai.ingredient as cai.name from CdActiveIngredients cai where cai.drugCode=(:pKey)");
        queryName.setParameter("pKey", pKey);
        Query queryAtc = em.createQuery("select ctc.tcAtcNumber as ctc.atccode from cd_therapeutic_class ctc where drug_code = (:pKey)");
        queryAtc.setParameter("pKey", pKey);
        Query queryProduct = em.createQuery("select cds.name from CdDrugSearch cds where cds.id=(:origId)");
        queryProduct.setParameter("origId", origId);
        Query queryRegionalIdentifier = em.createQuery("select cdp from CdDrugProduct cdp where cdp.drugDode = (:pKey)");
        queryRegionalIdentifier.setParameter("pKey", pKey);
        Query queryComponent = em.createQuery("select cai from CdActiveIngredients where cai.drugCode=(:pKey)");
        queryComponent.setParameter("pKey", pKey);

        String name = "";
        String atc = "";
        String product = "";
        String ProductId = "";
        String regionalIdentifier = "";

        List resultName = queryName.getResultList();
        if (resultName.size() > 0) {
            for (int i = 0; i < resultName.size(); i++) {
                name = (String) resultName.get(i);
            }
        }

        List resultAtc = queryAtc.getResultList();
        if (resultAtc.size() > 0) {
            for (int i = 0; i < resultAtc.size(); i++) {
                atc = (String) resultAtc.get(i);
            }
        }

        List resultProduct = queryProduct.getResultList();
        if (resultProduct.size() > 0) {
            for (int i = 0; i < resultProduct.size(); i++) {
                product = (String) resultProduct.get(i);
            }
        }

        List<CdDrugProduct> resultRegionalIdentifier = queryRegionalIdentifier.getResultList();
        if (resultRegionalIdentifier.size() > 0) {
            for (int i = 0; i < resultRegionalIdentifier.size(); i++) {
                productId = resultRegionalIdentifier.get(i).getDrugCode().toString();
                regionalIdentifier = resultRegionalIdentifier.get(i).getDrugIdentificationNumber().toString();
            }
        }
        String ingredient = "";
        String strength = "";
        String strengthUnit = "";

        // Hashtable ha=new Hashtable();
        String str = "";
        List<CdActiveIngredients> resultComponent = queryComponent.getResultList();
        if (resultComponent.size() > 0) {
            for (int i = 0; i < resultComponent.size(); i++) {
                ingredient = resultComponent.get(i).getIngredient();
                strength = resultComponent.get(i).getStrength();
                strengthUnit = resultComponent.get(i).getStrengthUnit();
            }
            //ha.put("name", ingredient);
            //ha.put("strength", Float.valueOf(strength.trim()).floatValue());
            //ha.put("unit",strengthUnit);
            str = "name " + ingredient + " strength " + strength + " unit " + strengthUnit;
        }
        Vector returnRows = new Vector();
        returnRows.addElement("name " + name);
        returnRows.addElement("atc " + atc);
        returnRows.addElement("product " + product);
        returnRows.addElement("regional_identifier " + regionalIdentifier);
        returnRows.addElement("components " + str);

        return returnRows;
    }
}

/*public List<CachedProgram> findAll() {
Query query = entityManager.createQuery("select x from " + modelClass.getSimpleName() + " x");
@SuppressWarnings("unchecked")
List<CachedProgram> results = query.getResultList();

return (results);
}

@AdditionalSchemaGenerationSql
public String[] getAdditionalCustomSql() {
String[] sql = {
"alter table " + modelClass.getSimpleName() + " add foreign key (integratorFacilityId) references Facility(id)"
};

return (sql);
}*/
//}

