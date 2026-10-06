/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.drugref.plugin;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
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

        vec.add(product);

        Hashtable substance = new Hashtable();
        substance.put("id","2");
        substance.put("description","SULFONAMIDES");
        substance.put("type","11");
        substance.put("ATC","J01E");
        substance.put("uuid", "20374");

        vec.add(substance);

        Hashtable substanceNoAtc = new Hashtable();
        substanceNoAtc.put("id","3");
        substanceNoAtc.put("description","PENICILLINS");
        substanceNoAtc.put("type","11");
        substanceNoAtc.put("ATC","");
        substanceNoAtc.put("uuid", "20192");

        vec.add(substanceNoAtc);

        Hashtable ingredient = new Hashtable();
        ingredient.put("id","4");
        ingredient.put("description","Polysporin");
        ingredient.put("type","11");
        ingredient.put("ATC","");
        ingredient.put("uuid","");

        vec.add(ingredient);

        Hashtable generic2 = new Hashtable();
        generic2.put("id","5");
        generic2.put("description","cetirizine");
        generic2.put("type","11");
        generic2.put("ATC","");
        generic2.put("uuid", "");

        vec.add(generic2);

        Hashtable notdrug = new Hashtable();
        notdrug.put("id","6");
        notdrug.put("description","strawberries");
        notdrug.put("type","8");
        notdrug.put("ATC","");
        notdrug.put("uuid", "");

        vec.add(notdrug);

        Drugref drugref = new Drugref(VigilanceDao.class);

        // anti depressants
        String prescribingAtc = "N06AX16";
        Vector vector = drugref.get_allergy_warnings(prescribingAtc, vec);
        System.out.println("AD: " + ((Vector) ((Hashtable) vector.get(0)).get("warnings")).get(0));
        Assert.assertEquals( ((Vector) ((Hashtable) vector.get(0)).get("warnings")).get(0), product.get("id") );

        // sulfa
        prescribingAtc = "J01EA01";
        vector = drugref.get_allergy_warnings(prescribingAtc, vec);
        System.out.println("SULFA: " + ((Vector) ((Hashtable) vector.get(0)).get("warnings")).get(0));
        Assert.assertEquals( ((Vector) ((Hashtable) vector.get(0)).get("warnings")).get(0), substance.get("id") );

        // PENICILLINS NO ATC
        prescribingAtc = "J01CE01";
        vector = drugref.get_allergy_warnings(prescribingAtc, vec);
        System.out.println("PENICILLINS NO ATC: " + ((Vector) ((Hashtable) vector.get(0)).get("warnings")).get(0));
        Assert.assertEquals( ((Vector) ((Hashtable) vector.get(0)).get("warnings")).get(0), substanceNoAtc.get("id") );


        prescribingAtc = "S03AA30";
        vector = drugref.get_allergy_warnings(prescribingAtc, vec);
        System.out.println("Ingredient: " + ((Vector) ((Hashtable) vector.get(0)).get("warnings")).get(0));
        Assert.assertEquals( ((Vector) ((Hashtable) vector.get(0)).get("warnings")).get(0), ingredient.get("id") );

        // ANTIHISTAMINES
        prescribingAtc = "R06AE07";
        vector = drugref.get_allergy_warnings(prescribingAtc, vec);
        System.out.println("Allergy: " + ((Vector) ((Hashtable) vector.get(0)).get("warnings")).get(0));
        Assert.assertEquals( ((Vector) ((Hashtable) vector.get(0)).get("warnings")).get(0), generic2.get("id") );


//        prescribingAtc = "J01CE01";
//        vector = drugref.get_allergy_warnings(prescribingAtc, vec);
//        System.out.println("PENICILLINS: " + ((Vector) ((Hashtable) vector.get(0)).get("warnings")).get(0));
//        Assert.assertEquals( ((Vector) ((Hashtable) vector.get(0)).get("warnings")).get(0), generic2.get("id") );

    }

    /**
     * The second argument of listSearchElement4 means "starts with" on the DPD backend but
     * meant "ingredient only" on this one, so the two backends disagreed about what the same
     * flag asks for. Vigilance now ignores it and OpenO relies on that: it sends true
     * unconditionally to get "starts with" out of DPD. If the ingredient branch is ever
     * reinstated here, Vigilance search breaks silently in OpenO. This pins the invariant.
     */
    public void testSearchIgnoresIngredientOnlyFlag() {
        Drugref drugref = new Drugref(VigilanceDao.class);

        Vector withFlag = drugref.list_search_element3_right("amoxicillin");
        Vector withoutFlag = drugref.list_search_element3("amoxicillin");

        Assert.assertFalse("search returned nothing, the fixture data is missing", withoutFlag.isEmpty());
        // compare the row count first: a mismatch here prints a readable number rather than
        // two full result sets
        Assert.assertEquals("the ingredientOnly flag must not change the number of results",
                withoutFlag.size(), withFlag.size());
        Assert.assertEquals("the ingredientOnly flag must not change the results", withoutFlag, withFlag);
    }

    /**
     * Every row the search returns must be prescribable. A row with a blank id is posted to
     * OpenO as drugId= and fails when the user tries to add it, so a blank id here is a row
     * the user can see but cannot use.
     */
    public void testSearchReturnsNoUnprescribableRows() {
        Drugref drugref = new Drugref(VigilanceDao.class);

        Vector results = drugref.list_search_element3("amoxicillin");
        Assert.assertFalse("search returned nothing, the fixture data is missing", results.isEmpty());

        for (Object row : results) {
            Object id = ((Hashtable) row).get("id");
            Assert.assertNotNull("row has no id: " + row, id);
            Assert.assertFalse("row has a blank id and cannot be prescribed: " + row,
                    id.toString().trim().isEmpty());
        }
    }

    /**
     * Two rows that display the same label are indistinguishable to the user, so only one of
     * them should be offered.
     */
    public void testSearchReturnsNoDuplicateLabels() {
        Drugref drugref = new Drugref(VigilanceDao.class);

        Vector results = drugref.list_search_element3("amoxicillin");
        Assert.assertFalse("search returned nothing, the fixture data is missing", results.isEmpty());

        Map<String, Object> seen = new HashMap<String, Object>();
        for (Object row : results) {
            String name = String.valueOf(((Hashtable) row).get("name"));
            Assert.assertFalse("duplicate label offered twice: " + name, seen.containsKey(name));
            seen.put(name, row);
        }
    }

    /**
     * Every word the user types has to be required. The search term is split into words on any
     * character that is not a letter or digit, matching how FULLTEXT indexes the data, so
     * "apo atorvastatin" asks for both words. Splitting on commas alone made it one operand,
     * "+apo atorvastatin*", which requires "apo" and merely prefers "atorvastatin": that
     * returned every product from the manufacturer, 961 rows for this term.
     */
    public void testSearchRequiresEveryWord() {
        Drugref drugref = new Drugref(VigilanceDao.class);

        Vector both = drugref.list_search_element3("apo atorvastatin");
        Assert.assertFalse("search returned nothing, the fixture data is missing", both.isEmpty());

        Vector manufacturerOnly = drugref.list_search_element3("apo");
        Assert.assertTrue("requiring both words must return fewer rows than the manufacturer alone",
                both.size() < manufacturerOnly.size());

        for (Object row : both) {
            String name = String.valueOf(((Hashtable) row).get("name")).toLowerCase();
            Assert.assertTrue("row matches only one of the words: " + name,
                    name.contains("apo") && name.contains("atorvastatin"));
        }
    }

    /**
     * A hyphen is a word separator, not the boolean NOT operator. FULLTEXT stores
     * "INSULIN-ASPART-RAPID" as three words, so a hyphenated term has to be searched as
     * separate words. Left whole, "pms-amoxicillin" reads as "pms AND NOT amoxicillin".
     */
    public void testSearchTreatsHyphenAsSeparator() {
        Drugref drugref = new Drugref(VigilanceDao.class);

        Vector hyphenated = drugref.list_search_element3("pms-amoxicillin");
        Assert.assertFalse("hyphenated search returned nothing", hyphenated.isEmpty());
        Assert.assertEquals("a hyphen and a space must mean the same thing",
                drugref.list_search_element3("pms amoxicillin").size(), hyphenated.size());

        for (Object row : hyphenated) {
            String name = String.valueOf(((Hashtable) row).get("name")).toLowerCase();
            Assert.assertTrue("hyphen was read as an exclusion: " + name, name.contains("amoxicillin"));
        }
    }

    /**
     * The typeahead searches on every keystroke, so half-typed terms reach this code. None of
     * them may raise: a failure here reaches the browser as an unparseable response and the
     * dropdown silently stops appearing. "apo-" is the case reported from the interface.
     */
    public void testSearchAcceptsAnyPunctuation() {
        Drugref drugref = new Drugref(VigilanceDao.class);

        String[] terms = {"apo-", "amoxicillin /", "acetaminophen (extra strength)", "children's",
                          "tylenol #3", "hydrocortisone 1%", "insulin 70/30", "b-12",
                          "-", "+", "*", "\"", "%", "(", "~", ">", "<", "@", "\"ab", "   ", ""};
        for (String term : terms) {
            try {
                Assert.assertNotNull("null result for: " + term, drugref.list_search_element3(term));
            } catch (Exception e) {
                Assert.fail("search raised on '" + term + "': " + e.getMessage());
            }
        }
    }

    /**
     * Operators the caller wrote are honoured, a quoted phrase as a phrase and a leading "-" as
     * an exclusion. They are applied per word rather than diverting the whole term, so they also
     * compose with plain words: see the two tests below.
     */
    public void testSearchPreservesExplicitBooleanSyntax() {
        Drugref drugref = new Drugref(VigilanceDao.class);

        Assert.assertFalse("quoted phrase returned nothing",
                drugref.list_search_element3("\"amoxicillin*\"").isEmpty());

        Vector excluded = drugref.list_search_element3("+amoxicillin -apo");
        Assert.assertFalse("excluding a manufacturer returned nothing", excluded.isEmpty());
        for (Object row : excluded) {
            String name = String.valueOf(((Hashtable) row).get("name")).toLowerCase();
            Assert.assertFalse("the -apo exclusion was not honoured: " + name, name.contains("apo "));
        }
    }

    /**
     * Words below the index's minimum token size are dropped rather than required. Not because
     * they cannot match: a short wildcard does match, "+70*" finds 320 rows on its own. Because
     * a one or two character prefix matches the wrong things, and is usually a fragment of a
     * word rather than a word. Requiring the "s" from "children's" cuts that search from 333
     * rows to 72. Dropping every short fragment is uniform and predictable, which a rule based
     * on where the fragment came from would not be: "insulin 70" and "tylenol #3" are the same
     * shape, and keeping the fragment helps the first and hurts the second.
     */
    public void testSearchIgnoresFragmentsTooShortToBeIndexed() {
        Drugref drugref = new Drugref(VigilanceDao.class);

        int insulin = drugref.list_search_element3("insulin").size();
        Assert.assertTrue("search returned nothing, the fixture data is missing", insulin > 0);
        Assert.assertEquals("a fragment too short to be indexed must not narrow the search",
                insulin, drugref.list_search_element3("insulin 70").size());
        Assert.assertEquals("nor may it empty the search",
                insulin, drugref.list_search_element3("insulin 70/30").size());
    }

    /**
     * An operator no longer disables the word split for the rest of the term. Before, any "+",
     * "-" or quote handed the whole term to the legacy parser, so "amoxicillin 500 -penta"
     * returned exactly the rows of "amoxicillin -penta" and the strength was ignored.
     */
    public void testSearchCombinesOperatorsWithWords() {
        Drugref drugref = new Drugref(VigilanceDao.class);

        Vector exclusionOnly = drugref.list_search_element3("amoxicillin -penta");
        Assert.assertFalse("search returned nothing, the fixture data is missing",
                exclusionOnly.isEmpty());

        Vector withStrength = drugref.list_search_element3("amoxicillin 500 -penta");
        Assert.assertFalse("combining a strength with an exclusion returned nothing",
                withStrength.isEmpty());
        Assert.assertTrue("the strength must narrow the search, not be ignored alongside the exclusion",
                withStrength.size() < exclusionOnly.size());

        for (Object row : withStrength) {
            String name = String.valueOf(((Hashtable) row).get("name")).toLowerCase();
            Assert.assertTrue("row does not carry the strength that was asked for: " + name,
                    name.contains("500"));
            Assert.assertFalse("the exclusion was not honoured: " + name, name.contains("penta"));
        }
    }

    /**
     * A quoted phrase composes with the words beside it. Before, the quote alone diverted the
     * term, so the trailing word was optional and could not narrow anything.
     */
    public void testSearchCombinesQuotedPhraseWithWords() {
        Drugref drugref = new Drugref(VigilanceDao.class);

        Vector phrase = drugref.list_search_element3("\"amlodipine atorvastatin\"");
        Assert.assertFalse("the phrase alone returned nothing, the fixture data is missing",
                phrase.isEmpty());

        Vector phraseAndWord = drugref.list_search_element3("\"amlodipine atorvastatin\" 500");
        Assert.assertTrue("a word beside a phrase must narrow the search",
                phraseAndWord.size() < phrase.size());

        for (Object row : phraseAndWord) {
            String name = String.valueOf(((Hashtable) row).get("name")).toLowerCase();
            Assert.assertTrue("row does not carry the word asked for beside the phrase: " + name,
                    name.contains("500"));
        }
    }

    /**
     * Every word carries its own wildcard, so a word's meaning does not depend on its position.
     * The legacy parser appended one wildcard to the end of the whole term, so adding a trailing
     * token silently un-wildcarded the one before it: "+amoxicillin +500" matched the 500MG
     * products, while "+amoxicillin +500 -penta" required an exact "500" token and returned three
     * unrelated combination products instead. Excluding a word that appears nowhere must
     * therefore change nothing at all.
     */
    public void testSearchWildcardsEveryWordNotOnlyTheLast() {
        Drugref drugref = new Drugref(VigilanceDao.class);

        Vector plain = drugref.list_search_element3("amoxicillin 500");
        Assert.assertFalse("search returned nothing, the fixture data is missing", plain.isEmpty());

        Vector withInertExclusion = drugref.list_search_element3("amoxicillin 500 -zzzzzz");
        Assert.assertEquals("excluding a word that appears nowhere must not change the result",
                plain.size(), withInertExclusion.size());
    }

}
