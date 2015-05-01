package org.drugref;

import java.util.Hashtable;
import java.util.Vector;

import junit.framework.TestCase;

import org.drugref.util.SpringUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DrugrefTest extends TestCase {

	protected void setUp() throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring_config.xml");
        SpringUtils.beanFactory = context;
	}
	
	public void testGetAllergyClasses() {
		System.out.println("test");
		Drugref d = new Drugref();
		Vector allergies = new Vector();
		Hashtable h = new Hashtable();
		h.put("type", "11");
		h.put("id", "52180");	
		h.put("description", "FEBUXOSTAT");	
		allergies.add(h);
		Vector result = d.get_allergy_classes(allergies);
		assertEquals(result.size(),1);
		assertEquals((String)result.get(0),"M04AA03");		
		
		/* No longer an active drug
		allergies = new Vector();
		h = new Hashtable();
		h.put("type", "13");
		h.put("id", "1598");	
		h.put("description", "ULORIC");	
		allergies.add(h);
		result = d.get_allergy_classes(allergies);
		assertEquals(result.size(),1);		
		assertEquals((String)result.get(0),"M04AA03");
		*/
		
		allergies = new Vector();
		h = new Hashtable();
		h.put("type", "8");
		h.put("id", "1");	
		h.put("description", "FEBUXOSTAT");	
		allergies.add(h);
		result = d.get_allergy_classes(allergies);
		assertEquals(result.size(),1);		
		assertEquals((String)result.get(0),"M04AA03");
		
		allergies = new Vector();
		h = new Hashtable();
		h.put("type", "10");
		h.put("id", "1");	
		h.put("description", "PENICILLINS");	
		allergies.add(h);
		result = d.get_allergy_classes(allergies);
		System.out.println(result.size());
		//assertEquals(result.size(),1);		
		//assertEquals((String)result.get(0),"M04AA03");
		
	}
}
