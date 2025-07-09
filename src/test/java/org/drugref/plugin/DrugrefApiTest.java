/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.drugref.plugin;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import junit.framework.TestCase;
import org.drugref.Drugref;
import org.drugref.ca.dpd.DrugrefDao;
import org.drugref.ca.vigilance.VigilanceDao;
import org.drugref.util.DrugrefProperties;
import org.junit.Assert;

/**
 *
 * @author jackson
 */
public class DrugrefApiTest extends TestCase {
    
    public DrugrefApiTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // instantiate properties
        DrugrefProperties.getInstance("/Users/denniswarren/drugref2.properties");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of addfunc method, of class DrugrefApi.
     */
    public void atestAddfunc(DrugrefApi instance) {
        System.out.println("addfunc");
        TestFuncSearch func=new TestFuncSearch();

        String requires = "key";
        Vector provides = new Vector();
        provides.addElement("attribute");
        provides.addElement("attribute2");
        
        instance.addfunc(func, requires, provides);


        TestFuncSearch func2=new TestFuncSearch();
        String requires2 = "_search_key";
        Vector provides2 = new Vector();
        provides2.addElement("found");
        provides2.addElement("key");
        //DrugrefApi instance2 = new DrugrefApi();
        instance.addfunc(func2, requires2, provides2);

        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of addquery method, of class DrugrefApi.
     */
    public void atestAddquery() {
        System.out.println("addquery");
        /*
        System.out.println("addquery");
        DrugrefApi instance = new DrugrefApi();
        instance.addquery();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
         */
    }

    /**
     * Test of get method, of class DrugrefApi.
     */
    public void testOperatorSearch() {
        System.out.println("SEARCH");
        Drugref drugref = new Drugref(DrugrefDao.class);
        Vector out = drugref.list_search_element3("-\"CALCIUM CARBONATE\" +600* +tab");
        System.out.println(out);
    }

    public void testParameterSearch() {
        System.out.println("SEARCH");
        Drugref drugref = new Drugref(DrugrefDao.class);
        Vector out = drugref.list_search_element3_right("carbid");
        System.out.println(out);
    }

    public void testGetGenericDrug() {
        System.out.println("GET");
        Drugref drugref = new Drugref(DrugrefDao.class);
        Vector out = drugref.get_drug_2("20374", Boolean.FALSE);
        System.out.println(out);
    }

    public void atestGetWildcard() {
       // System.out.println("getWildcard");
       // DrugrefApi instance = new DrugrefApi();
       // String expResult = "";
       // String result = instance.getWildcard();
        Hashtable ha=new Hashtable();

        Holbrook api=new Holbrook();

        Object obj=new Object();
        Vector key=new Vector();
        key.addElement("N02BE01");
        key.addElement("N05BA01");
        key.addElement("N05BA12");

        obj=api.get("interactions_byATC",key);
//        p("obj",obj.toString());
//
//        ha=(Hashtable)api.legend("effect");
//        p("ha_effect",ha.toString());
//
//        ha=(Hashtable)api.legend("significance");
//        p("ha_significance",ha.toString());
//
//        ha=(Hashtable)api.legend("evidence");
//        p("ha_evidence",ha.toString());

    }

    /**
     * Test of getName method, of class DrugrefApi.
     */
    public void atestGetName() {
        System.out.println("getName");
        DrugrefApi instance = new DrugrefApi();
        String expResult = "";
        String result = instance.getName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getVersion method, of class DrugrefApi.
     */
    public void testGetVersion() {
        System.out.println("getVersion");
        DrugrefApi instance = new DrugrefApi();
        String expResult = "";
        String result = instance.getVersion();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of listCapabilities method, of class DrugrefApi.
     */
    public void testListCapabilities() {
        System.out.println("listCapabilities");
        DrugrefApi instance = new DrugrefApi();
        Hashtable expResult = null;
        Hashtable result = instance.listCapabilities();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public void testGetAllergies() {
        System.out.println("testGetAllergies");

        Vector vec = new Vector();
        Hashtable product = new Hashtable();
        product.put("id","1");
        product.put("description","Remeron RD");
        product.put("type","13");
        product.put("ATC","N06AX11");
        product.put("uuid", "2248542");

        Hashtable substance = new Hashtable();
        substance.put("id","2");
        substance.put("description","SULFONAMIDES");
        substance.put("type","11");
        substance.put("ATC","J01E");
        substance.put("uuid", "20374");

        Hashtable generic = new Hashtable();
        generic.put("id","3");
        generic.put("description","TRIMETHOPRIME");
        generic.put("type","11");
        generic.put("ATC","");
        generic.put("uuid","");

        Hashtable generic2 = new Hashtable();
        generic2.put("id","4");
        generic2.put("description","cetirizine");
        generic2.put("type","11");
        generic2.put("ATC","");
        generic2.put("uuid", "");

        Hashtable notdrug = new Hashtable();
        notdrug.put("id","5");
        notdrug.put("description","strawberries");
        notdrug.put("type","8");
        notdrug.put("ATC","");
        notdrug.put("uuid", "");

        vec.add(product);
        vec.add(substance);
        vec.add(generic);
        vec.add(generic2);
        vec.add(notdrug);

        // anti depressants
        String prescribingAtc = "N06AX16";

        Drugref drugref = new Drugref(DrugrefDao.class);
        Vector vector = drugref.get_allergy_warnings(prescribingAtc, vec);

        Assert.assertEquals( ((Vector) ((Hashtable) vector.get(0)).get("warnings")).get(0), product.get("id") );

        // sulfa
        prescribingAtc = "J01EA01";
        vector = drugref.get_allergy_warnings(prescribingAtc, vec);

        Assert.assertEquals( ((Vector) ((Hashtable) vector.get(0)).get("warnings")).get(0), substance.get("id") );


        // ANTIHISTAMINES
        prescribingAtc = "R06AE07";
        vector = drugref.get_allergy_warnings(prescribingAtc, vec);

        Assert.assertEquals( ((Vector) ((Hashtable) vector.get(0)).get("warnings")).get(0), generic2.get("id") );

    }

}
