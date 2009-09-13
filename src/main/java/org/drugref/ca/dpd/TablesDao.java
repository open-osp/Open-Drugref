/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drugref.ca.dpd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import java.util.Vector;
import javax.persistence.Query;
import org.drugref.util.JpaUtils;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.drugref.util.MiscUtils;
import java.util.Enumeration;
import org.drugref.plugin.*;

/**
 *  org.drugref.DrugrefTest
 * @author jackson
 */
@Repository
public class TablesDao {

    private String plugindir;
    private String name;
    private String version;
    private Vector plugins = new Vector();
    private Hashtable services = new Hashtable();
    private Hashtable provided = new Hashtable();
    private String db;
    private String user;
    private String pwd;

    public TablesDao() {

        this.plugindir = "plugins";
        this.name = "Drugref Service";
        this.version = "1.0";
        this.services.put("version", this.version);
        this.db = "drugref2";
        this.user = null;
        this.pwd = null;


            //creating an object of holbrook as plugin instead of importing from files.
            Holbrook holbrook = new Holbrook();
            this.plugins.addElement(holbrook);
  //          PluginImpl module = new PluginImpl

    }

    public String identify() {
        return this.name;
    }

    public String version() {
        return this.version;
    }

    public List list_available_services() {
        List lt = new ArrayList();
        //TODO: implement
        return lt;
    }

    public Hashtable list_capabilities() {
        return this.provided;
    }

    private void log(String msg) {
        System.out.println(msg);
    }

   /* public Object fetch(String attribute, Vector key, Vector services, boolean feelingLucky) {
        feelingLucky = true;
        Hashtable results = new Hashtable();
        Hashtable haError = new Hashtable();
        Hashtable ha = new Hashtable();

        Vector providers = new Vector();
        Vector myservices = new Vector();

        try {
            providers = new Vector((Vector) this.provided.get(attribute));
        } catch (Exception e) {
            //e.printStackTrace();
            String val = attribute + " not provided by an registered service";
            haError.put("Error", val);
            return haError;
        }


        if (services.size() > 0) {
            Collections.copy(myservices, services);
        } else {
            Enumeration em = this.services.keys();
            while (em.hasMoreElements()) {
                myservices.addElement(em.nextElement());
            }
        }
        Hashtable module = new Hashtable();
        for (int i = 0; i < myservices.size(); i++) {

            Hashtable service = new Hashtable((Hashtable) this.services.get(myservices.get(i)));
            module = new Hashtable(service);

            ApiHolbrook ah = (ApiHolbrook) module.get("plugin");
            Object result = ah.holBrook.get(attribute, key);

            if (result instanceof Vector) {
                Vector vec = (Vector) result;
                if (vec.size() > 0) {
                    results.put(service, vec);
                }
                if (feelingLucky) {
                    return results;
                }
            } else if (result instanceof Hashtable) {
                Hashtable ha2 = (Hashtable) result;
                if (ha2.size() > 0) {
                    results.put(service, ha2);
                }
                if (feelingLucky) {
                    return results;
                }
            }


        }
        return results;
    }
*/
    //   public get(String attribute, String key){
//
    //   }
    public Vector listSearchElement(String str) {
        //EntityManagerFactory emf = (EntityManagerFactory) SpringUtils.getBean("entityManagerFactory");
        //EntityManager em = emf.createEntityManager();
        System.out.println("before create em in listSearchElement");
        EntityManager em = JpaUtils.createEntityManager();
        System.out.println("created entity manager");

        str = str.replace(",", " ");
        str = str.replace("'", "");
        String[] strArray = str.split("\\s+");

        for (int i = 0; i < strArray.length; i++) {
            System.out.println(strArray[i]);
        }

        //String queryStr = "select cds.id, cds.category, cds.name from CdDrugSearch cds where ";
        String queryStr = "select cds from CdDrugSearch cds where ";
        for (int i = 0; i < strArray.length; i++) {
            queryStr = queryStr + "upper(cds.name) like " + "'" + "%" + strArray[i].toUpperCase() + "%" + "'";
            if (i < strArray.length - 1) {
                queryStr = queryStr + " and ";
            }
        }
        List<CdDrugSearch> results = new ArrayList();
        queryStr = queryStr + " order by cds.name";
        System.out.println(queryStr);
        try {
            System.out.println("before tx definition");
            EntityTransaction tx = em.getTransaction();
            System.out.println("after txt definition");
            tx.begin();
            Query query = em.createQuery(queryStr);
            System.out.println("before query");

            results = query.getResultList();

            tx.commit();

        } catch (Exception e) {
            System.out.println("EXCEPTION-HERE");
            e.printStackTrace();
        } finally {
            JpaUtils.close(em);
        }
        if (results.size() > 0) {
            Vector vec = new Vector();
            for (int i = 0; i < results.size(); i++) {
                Hashtable ha = new Hashtable();
                ha.put("name", results.get(i).getName());
                ha.put("category", results.get(i).getCategory());
                ha.put("id", results.get(i).getId());
                vec.addElement(ha);
            }

            System.out.println(results);
            return (vec);
        } else {
            Vector defaultVec = new Vector();
            Hashtable ha = new Hashtable();
            ha.put("id", "0");
            ha.put("category", "");
            ha.put("name", "None found");
            defaultVec.addElement(ha);
            return defaultVec;
        }
    }

    public Vector listSearchElementRoute(String str, String route) {
        EntityManager em = JpaUtils.createEntityManager();

        str = str.replace(",", " ");
        str = str.replace("'", "");
        String[] strArray = str.split("\\s+");

        route = route.replace(",", " ");
        route = route.replace("'", "");
        String[] routeArray = route.split("\\s+");

        String queryOne = "select cr.drugCode from CdRoute cr where ";
        for (int i = 0; i < routeArray.length; i++) {
            queryOne = queryOne + "upper(cr.routeOfAdministration) like " + "'" + "%" + routeArray[i].toUpperCase() + "%" + "'";
            if (i < routeArray.length - 1) {
                queryOne = queryOne + " or ";
            }
        }

        List resultOne = new ArrayList();
        List<CdDrugSearch> resultTwo = new ArrayList();
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

            //String queryTwo = "select cds.id, cds.category, cds.name from CdDrugSearch cds where ";
            String queryTwo = "select cds from CdDrugSearch cds where ";
            for (int i = 0; i < strArray.length; i++) {
                queryTwo = queryTwo + " upper(cds.name) like " + "'" + "%" + strArray[i].toUpperCase() + "%" + "' ";
                if (i < strArray.length - 1) {
                    queryTwo = queryTwo + " and ";
                }
            }
            System.out.println("queryTwo: " + queryTwo);
            Query querySecond = em.createQuery(queryTwo + " and cds.drugCode in (:array) order by cds.name");
            querySecond.setParameter("array", strListOne);
            resultTwo = querySecond.getResultList();
            tx.commit();
        } finally {
            JpaUtils.close(em);
        }
        System.out.println("results:" + resultTwo);
        if (resultTwo.size() > 0) {
            Vector vec = new Vector();
            for (int i = 0; i < resultTwo.size(); i++) {
                Hashtable ha = new Hashtable();
                ha.put("name", resultTwo.get(i).getName());
                ha.put("category", resultTwo.get(i).getCategory());
                ha.put("id", resultTwo.get(i).getId());
                vec.addElement(ha);
            }

            return (vec);
        } else {
            Vector defaultVec = new Vector();
            Hashtable ha = new Hashtable();
            ha.put("id", "0");
            ha.put("category", "");
            ha.put("name", "None found");
            defaultVec.addElement(ha);
            return defaultVec;
        }
    }

    public Vector listBrandsFromElement(String drugID) {
        EntityManager em = JpaUtils.createEntityManager();

        String drugCode = "";
        Integer category;
        List<CdDrugSearch> results = new ArrayList();

        try {
            EntityTransaction tx = em.getTransaction();
            tx.begin();


            CdDrugSearch cdsResult = (CdDrugSearch) em.createQuery("select cds from CdDrugSearch cds where cds.id = " + drugID).getSingleResult();


            System.out.println(cdsResult.getDrugCode() + " -- " + cdsResult.getName() + " :: " + cdsResult.getCategory());

            if (cdsResult != null) {
                drugCode = cdsResult.getDrugCode();
                category = cdsResult.getCategory();
            } else {
                Vector vec = new Vector();
                return vec;
            }

            if (category == 8) {

                Query queryOne = em.createQuery("select tc from CdTherapeuticClass tc where tc.tcAtcNumber=(:drugCode)");
                queryOne.setParameter("drugCode", Integer.parseInt(drugCode.trim()));
                List<CdTherapeuticClass> listOne = queryOne.getResultList();

                for (int i = 0; i < listOne.size(); i++) {
                    Query query = em.createQuery("select sd   from CdDrugSearch sd where (:tcDrugCode)= sd.drugCode order by sd.name");
                    query.setParameter("tcDrugCode", listOne.get(i).getDrugCode());
                    CdDrugSearch cds = (CdDrugSearch) query.getSingleResult();

                    results.add(cds);

                }
            } else if (category == 10) {
                Query queryOne = em.createQuery("select tc from CdTherapeuticClass tc where tc.tcAhfsNumber=(:drugCode)");
                queryOne.setParameter("drugCode", drugCode);
                List<CdTherapeuticClass> listOne = queryOne.getResultList();

                for (int i = 0; i < listOne.size(); i++) {
                    Query query = em.createQuery("select sd  from CdDrugSearch sd where (:tcDrugCode)= sd.drugCode order by sd.name");
                    query.setParameter("tcDrugCode", listOne.get(i).getDrugCode().toString());
                    CdDrugSearch cds = (CdDrugSearch) query.getSingleResult();
                    results.add(cds);

                }
            } else {//category=11 or 12
                System.out.println("category is 11 or 12");
                Query query = em.createQuery("select sd from LinkGenericBrand lgb, CdDrugSearch sd where lgb.id = (:drugCode) and lgb.drugCode = sd.drugCode order by sd.name");
                query.setParameter("drugCode", Integer.parseInt(drugCode.trim()));
                System.out.println("drugCode=" + Integer.parseInt(drugCode.trim()));
                results = query.getResultList();

            }
        } catch (Exception e) {
            System.out.println("in listBrandsFromElement exception");
            e.printStackTrace();
        } finally {
            JpaUtils.close(em);
        }
        if (results.size() > 0) {
            Vector vec = new Vector();
            for (int j = 0; j < results.size(); j++) {
                Hashtable ha = new Hashtable();
                ha.put("name", results.get(j).getName());
                ha.put("category", results.get(j).getCategory());
                ha.put("id", results.get(j).getId());
                vec.addElement(ha);
            }

            System.out.println(vec);
            return (vec);
        } else {
            Vector defaultVec = new Vector();
            Hashtable ha = new Hashtable();
            ha.put("name", "None found");
            ha.put("category", "");
            ha.put("id", "0");
            defaultVec.addElement(ha);
            return defaultVec;
        }
    }

    public Vector listSearchElementSelectCategories(String str, Vector cat) {
        EntityManager em = JpaUtils.createEntityManager();

        str = str.replace(",", " ");
        str = str.replace("'", "");
        String[] strArray = str.split("\\s+");
        for (int i = 0; i < strArray.length; i++) {
            System.out.println(strArray[i]);
        }

        String queryStr = "select cds from CdDrugSearch cds where ";

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

        List<CdDrugSearch> results = new ArrayList();

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
        if (results.size() > 0) {
            Vector vec = new Vector();
            for (int j = 0; j < results.size(); j++) {
                Hashtable ha = new Hashtable();
                ha.put("name", results.get(j).getName());
                ha.put("category", results.get(j).getCategory());
                ha.put("id", results.get(j).getId());
                vec.addElement(ha);
            }
            System.out.println(vec);
            return (vec);
        } else {
            Vector defaultVec = new Vector();
            Hashtable ha = new Hashtable();
            ha.put("id", "0");
            ha.put("category", "");
            ha.put("name", "None found");
            defaultVec.addElement(ha);
            return defaultVec;
        }
    }

    public Vector getGenericName(String drugID) {
        System.out.println("in getGenericName.drugID=" + drugID);
        Vector vec = new Vector();
        EntityManager em = JpaUtils.createEntityManager();
        List<CdDrugSearch> results = new ArrayList();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Query queryOne = em.createQuery("select lgb.id from LinkGenericBrand lgb ,CdDrugSearch cds where lgb.drugCode = cds.drugCode and cds.id=(:ID)");
        List resultDrugCode = new ArrayList();
        try {
            queryOne.setParameter("ID", Integer.parseInt(drugID));
            resultDrugCode = queryOne.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("size of list=" + resultDrugCode.size());
        if (resultDrugCode == null) {
            System.out.println("resultDrugCode is null!");
        }
        if (resultDrugCode.size() > 0) {
            Query queryTwo = em.createQuery("select cds from CdDrugSearch cds where cds.drugCode in (:drugCodeList)");
            queryTwo.setParameter("drugCodeList", MiscUtils.toStringArrayList(resultDrugCode));
            results = queryTwo.getResultList();
            JpaUtils.close(em);
            System.out.println("in if");
            System.out.println(results);
            for (int j = 0; j < results.size(); j++) {
                Hashtable ha = new Hashtable();
                ha.put("name", results.get(j).getName());
                ha.put("category", results.get(j).getCategory());
                ha.put("id", results.get(j).getId());
                vec.addElement(ha);
            }
            for (int i = 0; i < vec.size(); i++) {
                System.out.println("if:vec.get(i)=" + vec.get(i));
            }
            return vec;
        } else {
            System.out.println("in else , getGenericName.");
            Vector defaultVec = new Vector();
            Hashtable ha = new Hashtable();
            ha.put("id", "0");
            ha.put("name", "None found");
            defaultVec.addElement(ha);
            return defaultVec;
        }
    }
    //change to return Hashtable??

    public Vector getForm(String pKey) {
        EntityManager em = JpaUtils.createEntityManager();
        List<CdForm> results = new ArrayList();
        Vector vec = new Vector();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Query queryOne = em.createQuery("select cds.drugCode from CdDrugSearch cds where cds.id = (:ID)");
        queryOne.setParameter("ID", Integer.parseInt(pKey));
        List resultDrugCode = queryOne.getResultList();



        if (resultDrugCode != null) {
            Query queryTwo = em.createQuery("select cf from CdForm cf where cf.drugCode in (:drugCodeList)");
            queryTwo.setParameter("drugCodeList", MiscUtils.toIntegerArrayList(resultDrugCode));
            results = queryTwo.getResultList();
            JpaUtils.close(em);

            System.out.println(results);

            for (int i = 0; i < results.size(); i++) {
                Hashtable ha = new Hashtable();
                ha.put("pharmaceutical_cd_form", results.get(0).getPharmaceuticalCdForm());
                ha.put("pharm_cd_form_code", results.get(0).getPharmCdFormCode());
                vec.addElement(ha);
            }
            return vec;
        } else {
            Vector defaultVec = new Vector();
            Hashtable ha = new Hashtable();
            ha.put("pharm_cd_form_code", "");
            ha.put("pharmaceutical_cd_form", "None found");
            defaultVec.addElement(ha);
            return defaultVec;
        }
    }

    public Vector listDrugClass(Vector Dclass) {
        EntityManager em = JpaUtils.createEntityManager();
        List results = new ArrayList();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        String q1 = "select cds from  CdDrugSearch cds where ";
        for (int i = 0; i < Dclass.size(); i++) {
            q1 = q1 + " cds.id = " + Dclass.get(i);
            if (i < Dclass.size() - 1) {
                q1 = q1 + " or ";
            }
        }
        System.out.println(q1);
        Vector vec = new Vector();
        Query queryOne = em.createQuery(q1);
        List<CdDrugSearch> listOne = queryOne.getResultList();
        if (listOne.size() > 0) {
            for (int i = 0; i < listOne.size(); i++) {
                Query queryTwo = em.createQuery("select cds2 from CdDrugSearch cds2 where cds2.drugCode in (select tc.tcAhfsNumber from   CdTherapeuticClass tc where tc.drugCode = (:cdIntDrugCode)) order by cds2.id");
                queryTwo.setParameter("cdIntDrugCode", Integer.parseInt(listOne.get(i).getDrugCode().trim()));
                CdDrugSearch resultTwo = (CdDrugSearch) queryTwo.getSingleResult();
                Hashtable ha = new Hashtable();
                ha.put("id_class", resultTwo.getId());
                ha.put("name", resultTwo.getName());
                ha.put("id_drug", listOne.get(i).getId());
                vec.add(ha);
            }

            JpaUtils.close(em);

            return vec;
        } else {
            Vector defaultVec = new Vector();
            Hashtable ha = new Hashtable();
            ha.put("pharm_cd_form_code", "");
            ha.put("pharmaceutical_cd_form", "None found");
            defaultVec.addElement(ha);
            return defaultVec;
        }
    }

    public Vector getAllergyWarnings(String atcCode, Vector allergies) {
        List results = new ArrayList();
        Vector vec = new Vector();
        Hashtable ha = new Hashtable();
        Vector warning = new Vector();
        ha.put("warnings", warning);
        vec.add(ha);
        if (atcCode.matches("") || atcCode.matches("null")) {
            return vec;
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
        return vec;
    }

    public Vector getDrug(String pKey, boolean html) {
        String productId = "";
        String origId = pKey;
        Vector returnRows = new Vector();

        try {
            EntityManager em = JpaUtils.createEntityManager();
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            Query queryDrugCode = em.createQuery("select cds.drugCode from CdDrugSearch cds where cds.id=(:pKey)");
            queryDrugCode.setParameter("pKey", Integer.parseInt(pKey));
            String resultDrugCode = "";
            resultDrugCode = (String) queryDrugCode.getSingleResult();
            if (!resultDrugCode.matches("")) {
                pKey = resultDrugCode;
            }
            Query queryName = em.createQuery("select cai.ingredient from CdActiveIngredients cai where cai.drugCode=(:pKey)");
            queryName.setParameter("pKey", Integer.parseInt(pKey));
            Query queryAtc = em.createQuery("select ctc.tcAtcNumber from CdTherapeuticClass ctc where ctc.drugCode = (:pKey)");
            queryAtc.setParameter("pKey", Integer.parseInt(pKey));
            Query queryProduct = em.createQuery("select cds.name from CdDrugSearch cds where cds.id=(:origId)");
            queryProduct.setParameter("origId", Integer.parseInt(origId));
            Query queryRegionalIdentifier = em.createQuery("select cdp from CdDrugProduct cdp where cdp.drugCode = (:pKey)");
            queryRegionalIdentifier.setParameter("pKey", Integer.parseInt(pKey));
            Query queryComponent = em.createQuery("select cai from CdActiveIngredients cai where cai.drugCode=(:pKey)");
            queryComponent.setParameter("pKey", Integer.parseInt(pKey));


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
            //System.out.println("reginoal Identifier " + regionalIdentifier);
            String ingredient = "";
            String strength = "";
            String strengthUnit = "";

            Hashtable ha = new Hashtable();
            Vector component = new Vector();
            List<CdActiveIngredients> resultComponent = queryComponent.getResultList();
            if (resultComponent.size() > 0) {
                for (int i = 0; i < resultComponent.size(); i++) {
                    ingredient = resultComponent.get(i).getIngredient();
                    strength = resultComponent.get(i).getStrength();
                    strengthUnit = resultComponent.get(i).getStrengthUnit();
                }
                ha.put("name", ingredient);
                ha.put("strength", Float.valueOf(strength.trim()).floatValue());
                ha.put("unit", strengthUnit);
                component.addElement(ha);
            }
            Hashtable ha2 = new Hashtable();
            ha2.put("name", name);
            ha2.put("atc", atc);
            ha2.put("product", product);
            ha2.put("regional_identifier", regionalIdentifier);
            ha2.put("components", component);

            returnRows.addElement(ha2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnRows;
    }
}
