/*
 *
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 *
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */

package org.drugref.ca.dpd.fetch;

import java.util.Hashtable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import org.apache.openjpa.persistence.OpenJPAPersistence;
import org.apache.openjpa.persistence.OpenJPAQuery;
import org.apache.openjpa.persistence.jdbc.JDBCFetchPlan;
import org.drugref.ca.dpd.CdDrugProduct;
import org.drugref.ca.dpd.CdDrugSearch;
import org.drugref.ca.dpd.LinkGenericBrand;

/**
 *
 * @author jaygallagher
 */
public class ConfigureSearchData {
   
    public void importSearchData(EntityManager em){
        ////Importing Brand Information
        importAllBrandName(em);
        
        ////print "Import ATC Names"
        importAllATCCodeName(em);

        ////Import AFHC Names
        importAllAHFSCodeName(em);

        ////Import Generic Data
        importGenerics(em);
        
        ////Import Ingredients
        importAllIngredients(em);


        ////Cleaning up Search Names
        cleanUpSearchNames(em);


        
        //print "Should check for duplicates and ones that need forms and strength added ie ones from generic companys with the form and strength is not included in the name"


    }

    /*
     def import_all_brand_name(self):
		con = dbapi.connect(database=self._db)
                cur = con.cursor()
		query = "select * from cd_drug_product"
                print query
                cur.execute(query)
                results = cur.fetchall()
                if len(results)>0:
                        ida = ImportSearchData()
                        for result in results:
                        	ida.insert_into_drug_search(con,result['drug_code'],result['brand_name'],'13')
      */
      public void importAllBrandName(EntityManager em){
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        System.out.println("Importing Brand Names");
        Query queryDrugProduct = em.createQuery("SELECT cdp from CdDrugProduct cdp");
        OpenJPAQuery kq = OpenJPAPersistence.cast(queryDrugProduct);
        JDBCFetchPlan fetch = (JDBCFetchPlan) kq.getFetchPlan();
        fetch.setFetchBatchSize(20);

        List<CdDrugProduct> productList = queryDrugProduct.getResultList();
       
        System.out.println("Have got all the products in memory");
        
        for (CdDrugProduct drug:productList){
            CdDrugSearch drugSearch = new CdDrugSearch();

            String str=drug.getBrandName();
            
          
            drugSearch.setDrugCode(""+drug.getDrugCode());
            drugSearch.setName(str);
            drugSearch.setCategory(13);
            em.persist(drugSearch);
            em.flush();
            em.clear();
            
        }
        p("DONE Import Brand Name");
        tx.commit();
      }

      /*
        def import_all_ATC_code_name(self):
                con = dbapi.connect(database=self._db)
                cur = con.cursor()
                query = "select distinct tc_atc_number, tc_atc from cd_therapeutic_class"

                print query
                cur.execute(query)
                results = cur.fetchall()
                if len(results)>0:
                        ida = ImportSearchData()
                        for result in results:
                                if result['tc_atc_number'] != "" :
                                        ida.insert_into_drug_search(con,result['tc_atc_number'],result['tc_atc'],'8')

       */
       public void importAllATCCodeName(EntityManager em){
           EntityTransaction tx = em.getTransaction();
        tx.begin();
        System.out.println("Import ATC Code Name");
        //Query queryDrugProduct = em.createNativeQuery("SELECT distinct cdp.tc_Atc, cdp.tc_Atc_Number from Cd_Therapeutic_Class cdp");

        Query queryDrugProduct = em.createQuery("SELECT distinct cdp.tcAtc, cdp.tcAtcNumber from CdTherapeuticClass cdp");
        OpenJPAQuery kq = OpenJPAPersistence.cast(queryDrugProduct);
        JDBCFetchPlan fetch = (JDBCFetchPlan) kq.getFetchPlan();
        fetch.setFetchBatchSize(20);

        List<Object[]> productList = queryDrugProduct.getResultList();

        //System.out.println("Have got all the products in memory");

        for (Object[] drug:productList){
        //    System.out.println(drug.getClass().getName()+" "+drug[0]);

            CdDrugSearch drugSearch = new CdDrugSearch();
            drugSearch.setDrugCode(""+drug[1]);
            drugSearch.setName(""+drug[0]);
            drugSearch.setCategory(8);
            em.persist(drugSearch);
            em.flush();
            em.clear();
             
             
        }
        System.out.println("DONE Import ATC Code Name");
        tx.commit();
       }
       /*
        def import_all_AHFS_code_name(self):
                con = dbapi.connect(database=self._db)
                cur = con.cursor()
                query = "select distinct tc_ahfs_number, tc_ahfs from cd_therapeutic_class"

                print query
                cur.execute(query)
                results = cur.fetchall()
                if len(results)>0:
                        ida = ImportSearchData()
                        for result in results:
                                if result['tc_ahfs_number'] != "" :
                                        ida.insert_into_drug_search(con,result['tc_ahfs_number'],result['tc_ahfs'],'10')

        */

           public void p(String str, String s) {
        System.out.println(str + "=" + s);
    }

    public void p(String str) {
        System.out.println(str);
    }
       public void importAllAHFSCodeName(EntityManager em){
           EntityTransaction tx = em.getTransaction();
        tx.begin();
        System.out.println("Import AHFS Code Name");
        //Query queryDrugProduct = em.createNativeQuery("SELECT distinct cdp.tc_Atc, cdp.tc_Atc_Number from Cd_Therapeutic_Class cdp");

        Query queryDrugProduct = em.createQuery("SELECT distinct cdp.tcAhfs, cdp.tcAhfsNumber from CdTherapeuticClass cdp");
        OpenJPAQuery kq = OpenJPAPersistence.cast(queryDrugProduct);
        JDBCFetchPlan fetch = (JDBCFetchPlan) kq.getFetchPlan();
        fetch.setFetchBatchSize(20);

        List<Object[]> productList = queryDrugProduct.getResultList();

        //System.out.println("Have got all the products in memory");

        for (Object[] drug:productList){
       //     System.out.println(drug.getClass().getName()+" "+drug[0]);
            CdDrugSearch drugSearch = new CdDrugSearch();
            drugSearch.setDrugCode(""+drug[1]);
            drugSearch.setName(""+drug[0]);
            drugSearch.setCategory(10);
            em.persist(drugSearch);
            em.flush();
            em.clear();


        }
        System.out.println("DONE Import AHFS Code Name");
        tx.commit();
       }


       /*
        def import_Generics(self):
                con = dbapi.connect(database=self._db)
                cur = con.cursor()
                cur2 = con.cursor()
                query = "select drug_code from cd_drug_product"
                ida = ImportSearchData()
                genHash = {}
                cur.execute(query)
                results = cur.fetchall()
                if len(results)>0:
                        for result in results:
                                get_ingredient_query = "select ingredient from cd_active_ingredients where drug_code = '%s' " % result['drug_code']
                                cur2.execute(get_ingredient_query)
                                ingredients = cur2.fetchall()
                                bool = 0
                                compositeGeneric = 0
                                genName = ""
                                if len(ingredients) >0:
                                        for ingredient in ingredients:
                                                if bool:
                                                        genName = genName + "/ "
                                                        compositeGeneric = 1
                                                genName = genName + ingredient['ingredient']
                                                bool = 1
                                        if genHash.has_key(genName):
                                                #print " just add %s to linking table " % genName
                                                ida.insert_into_link_generic_brand(genHash[genName],result['drug_code'])
                                        else:
                                           if compositeGeneric:
                                                ida.insert_into_drug_search(con,'',genName ,'12')
                                           else:
                                                ida.insert_into_drug_search(con,'',genName ,'11')
                                           id_drug = ida.getCurrVal(con,'cd_drug_search_id_seq')
                                           genHash[genName] = id_drug
                        cur.execute("update cd_drug_search set drug_code = id where category = 11 or category = 12")
                        con.commit()
        *
        
        def insert_into_link_generic_brand(self, id_drug, drug_code):
		con = dbapi.connect(database=self._db)
                cur = con.cursor()
                query = "insert into link_generic_brand (id,drug_code) values (%s,%s) "
                """print query"""
                cur.execute(query,id_drug,drug_code)
                con.commit()
        */
        public void importGenerics(EntityManager em){
            Hashtable genHash = new Hashtable();
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            System.out.println("Import Generics");
            Query queryDrugProduct = em.createQuery("SELECT cdp.drugCode from CdDrugProduct cdp");
            OpenJPAQuery kq = OpenJPAPersistence.cast(queryDrugProduct);
            JDBCFetchPlan fetch = (JDBCFetchPlan) kq.getFetchPlan();
            fetch.setFetchBatchSize(20);

            List<Integer> productList = queryDrugProduct.getResultList();

            //System.out.println("Have got all the products in memory");

            for (Integer drug:productList){

                Query ingredientQuery = em.createQuery("select cai.ingredient from CdActiveIngredients cai where cai.drugCode =(:drugCode)");

                ingredientQuery.setParameter("drugCode", drug);
                List<String> ingredients = ingredientQuery.getResultList();
                int bool = 0;
                int compositeGeneric = 0;
                String genName = "";
                if (ingredients.size() > 0){
                    for (String ingredient:  ingredients){
                            if ( bool ==1){
                                    genName = genName + "/ ";
                                    compositeGeneric = 1;
                            }
                            genName = genName + ingredient;
                            bool = 1;
                    }
                    if (genHash.containsKey(genName)){
                       LinkGenericBrand genBrand = new LinkGenericBrand();
                       Integer genBrandId = (Integer) genHash.get(genName);
                       genBrand.setId(genBrandId);
                       genBrand.setDrugCode(""+drug);
                       em.persist(genBrand);
                       em.flush();
                       em.clear();//#print " just add %s to linking table " % genName
                                                //ida.insert_into_link_generic_brand(genHash[genName],result['drug_code'])
                    }else{
                       CdDrugSearch drugSearch = new CdDrugSearch();
                       //drugSearch.setDrugCode(""+drug.getDrugCode());
                        drugSearch.setName(genName);
                        if (compositeGeneric == 1){
                            drugSearch.setCategory(12);
                        }else{
                            drugSearch.setCategory(11);
                        }
                        em.persist(drugSearch);
                        em.flush();


                        Integer drugId = drugSearch.getId();
                   //     System.out.println("ID WAS"+drugId);
                        genHash.put(genName,drugId);
                        em.clear();

                        if(genHash.containsKey(genName)){
                            LinkGenericBrand genBrand = new LinkGenericBrand();
                            Integer genBrandId = (Integer) genHash.get(genName);
                            genBrand.setId(genBrandId);
                            genBrand.setDrugCode(""+drug);
                            em.persist(genBrand);
                            em.flush();
                    //        System.out.println("id added to genBrand="+genBrandId+" || drugCode added to genBrand="+drug.toString());
                            em.clear();
                        }                  
                    }
                }                
            }
            Query updateQuery = em.createQuery("update CdDrugSearch cds set cds.drugCode = cds.id where cds.category = 11 or cds.category = 12");
            updateQuery.executeUpdate();
            em.flush();
            em.clear();
             System.out.println("DONE Import Generics");
            tx.commit();
        }

     



        /*
         def import_all_Ingredients(self):
                con = dbapi.connect(database=self._db)
                cur = con.cursor()
                query = "select  distinct ingredient, active_ingredient_code  from cd_active_ingredients"

                print query
                cur.execute(query)
                results = cur.fetchall()
                if len(results)>0:
                        ida = ImportSearchData()
                        for result in results:
                                if result['ingredient'] != "" :
                                        ida.insert_into_drug_search(con,result['active_ingredient_code'],result['ingredient'],'14')

         */
         public void importAllIngredients(EntityManager em){
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            System.out.println("Import Ingredients");

            Query queryDrugProduct = em.createQuery("SELECT distinct cai.ingredient, cai.activeIngredientCode from CdActiveIngredients cai");
            OpenJPAQuery kq = OpenJPAPersistence.cast(queryDrugProduct);
            JDBCFetchPlan fetch = (JDBCFetchPlan) kq.getFetchPlan();
            fetch.setFetchBatchSize(20);

            List<Object[]> productList = queryDrugProduct.getResultList();

            //System.out.println("Have got all the products in memory");

            for (Object[] drug:productList){
            //    System.out.println(drug.getClass().getName()+" "+drug[0]);

                CdDrugSearch drugSearch = new CdDrugSearch();
                drugSearch.setDrugCode(""+drug[1]);
                drugSearch.setName(""+drug[0]);
                drugSearch.setCategory(14);
                em.persist(drugSearch);
                em.flush();
                em.clear();


            }
System.out.println("DONE Import Ingredients");
            tx.commit();
       }


       /*
        """
        Used to find duplicate names in the database and add there strength if its not included in the drug name.
        EG 4 duplicates of FOSAMAX would be come FOSAMAX 70MG, FOSAMAX 10MG, FOSAMAX 40MG, FOSAMAX 5MG
        """
        def clean_up_search_names(self):
                isd = ImportSearchData()
                con = dbapi.connect(database=self._db)
                cur = con.cursor()
                query = "select name ,count(name) as name_count from cd_drug_search where category = '13' group by name order by name_count desc"
                cur.execute(query)
                results = cur.fetchall()
                drug_code_name = []
                if len(results)>0:
                        for result in results:
                                name = result['name']
                                name_count = result['name_count']
                                if name_count > 1:
                                        drug_code_list = isd.get_drug_codes_for_name(con,name)
                                        for code in drug_code_list:
                                                strens = isd.get_strengths_from_drug_code(con,code)
                                                suggName = isd.suggest_new_name(name,strens)
                                                if suggName != "":
                                                        drug_code_name.append({'drug_code':code,'name':suggName})

                isd.update_name_by_drug_code(drug_code_name)
        */
        public void cleanUpSearchNames(EntityManager em){
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            System.out.println("Clean Up Search Names");

            Query queryDrugProduct = em.createQuery("select cds.name ,count(cds.name)  from CdDrugSearch cds where cds.category = 13 group by cds.name ");
            OpenJPAQuery kq = OpenJPAPersistence.cast(queryDrugProduct);
            JDBCFetchPlan fetch = (JDBCFetchPlan) kq.getFetchPlan();
            fetch.setFetchBatchSize(20);

            List<Object[]> productList = queryDrugProduct.getResultList();

            System.out.println("Have got all the products in memory");

            for (Object[] drug:productList){
                //System.out.println(drug.getClass().getName()+" "+drug[0]);
                String name = ""+drug[0];
                Long num = (Long) drug[1];
                if (num > 1){
              //      System.out.println("Donig something with "+drug[0]);
                    List<String> drugCodeList = getDrugCodeForName( em, ""+drug[0]);
                    Query updateDrugSearch = em.createQuery("update CdDrugSearch cds set cds.name = (:NAME) where cds.drugCode = (:DRUGCODE) and cds.category = 13");
                    for (String code :drugCodeList){
                //        System.out.println("Going to work on "+code);
                                                List<Object[]> strens = getStrengthsFromDrugCode(em,code);
                                   //             System.out.println(code+":"+strens.size());
                                                String suggName = getSuggestedNewName(name, strens);
                                //                System.out.println(suggName);
                                                if (!suggName.equals("")){
                                                    updateDrugSearch.setParameter("NAME", suggName);
                                                    updateDrugSearch.setParameter("DRUGCODE", code);
                                                    updateDrugSearch.executeUpdate();
                                                    em.flush();
                                                    
                                                }
                                                //       drug_code_name.append({'drug_code':code,'name':suggName})

                    }
                }
                
            }

            tx.commit();

        }


        /*
         """
        Function should try to suggest an altername for the drug, using its strength and forms
        loop through strengths (eg 2,3 14.3) and text search the drug name to see if they can be found ie Coumadin 4mg '4' would be found

        if none of the strengths are found and there is only 1 ingredient
                suggest name strength strength_unit

        """
        def suggest_new_name(self,name,strengths):
                suggested_name = ""
                numStrenFoundInName = 0
                for strens in strengths:
                        strenFoundInName = name.rfind(strens['strength'])
                        if strenFoundInName != -1:
                                numStrenFoundInName = numStrenFoundInName + 1

                if numStrenFoundInName == 0 and len(strengths) == 1:
                        suggested_name = "%s %s%s" % (name, strengths[0]['strength'], strengths[0]['strength_unit'])

                return suggested_name
         */
        private String getSuggestedNewName(String name,List<Object[]> strengths){
            String suggested_name = "";
            int  numStrenFoundInName = 0;
                for (Object[] strens : strengths){
                        boolean strenFoundInName = name.contains(""+strens[1]);
                        if (strenFoundInName){
                                numStrenFoundInName++;
                        }

                }
                if (numStrenFoundInName == 0 && strengths.size() == 1){
                        Object[] strens = strengths.get(0);
                        suggested_name = name+" " +strens[1]+""+strens[2];
                }
                return suggested_name;
        }


        /*
                def get_drug_codes_for_name(self,con,name):
                cur = con.cursor()
                query = "select drug_code from cd_drug_search where category = '13' and  name = %s "
                drug_code_list = []
                cur.execute(query,name)
                results = cur.fetchall()
                if len(results)>0:
                        for result in results:
                                drug_code_list.append(result['drug_code'])

                return drug_code_list
        */
        private List<String> getDrugCodeForName(EntityManager em, String name){
            
            Query queryDrugProduct = em.createQuery("select cds.drugCode from CdDrugSearch cds where cds.category = 13 and cds.name = (:NAME)");

            queryDrugProduct.setParameter("NAME", name);

            List<String> productList = queryDrugProduct.getResultList();

            return productList;
        }

        /*
        def update_name_by_drug_code(self,drug_code_name):
                con = dbapi.connect(database=self._db)
                cur = con.cursor()
                query = "update cd_drug_search set name = %s where drug_code = %s and category = '13' "
                for pair in drug_code_name:
                        cur.execute(query,pair['name'],pair['drug_code'])
                con.commit()

        

        def get_strengths_from_drug_code(self,con,drug_code):
                ret = []
                cur = con.cursor()
                query = "select ingredient, strength, strength_unit from cd_active_ingredients where drug_code = %s"
                cur.execute(query,drug_code)
                results = cur.fetchall()
                if len(results)>0:
                        ret = queryresult2rowdict(results)

                return ret

        */
        private List<Object[]> getStrengthsFromDrugCode(EntityManager em, String drugCode){

            Query queryDrugProduct = em.createQuery("select cai.ingredient, cai.strength, cai.strengthUnit from CdActiveIngredients cai where cai.drugCode = (:drugCode)");

            queryDrugProduct.setParameter("drugCode", new Integer(drugCode));

            List<Object[]> productList = queryDrugProduct.getResultList();

            return productList;
        }
}
 /*
#!/usr/bin/python
############################################################
# Create tables to help searching the Canadian DPD
#
# (c) by McMaster University, released under the GPL free software
# license, details see http://www.gnu.org
############################################################

import string
from pyPgSQL import PgSQL as dbapi

def queryresult2rowdict(queryresult):
	rows = []
	for row in queryresult:
		d = {}
		for f in row.keys():
			d[f]=row[f]
		rows.append(d)
	return rows


class ImportSearchData:
        """This class allows to search a PostgreSQL database for drug names, ATC codes, and
        drug-drug interaction information"""

        def __init__(self, database="drugref2", user=None, password=None):
                """works only if the Postgres server is local.
                Parameters are database name, username and password"""
                self._db = database
                self._user = user
                self._pwd = password





        """
        Function should try to suggest an altername for the drug, using its strength and forms
        loop through strengths (eg 2,3 14.3) and text search the drug name to see if they can be found ie Coumadin 4mg '4' would be found

        if none of the strengths are found and there is only 1 ingredient
                suggest name strength strength_unit

        """
        def suggest_new_name(self,name,strengths):
                suggested_name = ""
                numStrenFoundInName = 0
                for strens in strengths:
                        strenFoundInName = name.rfind(strens['strength'])
                        if strenFoundInName != -1:
                                numStrenFoundInName = numStrenFoundInName + 1

                if numStrenFoundInName == 0 and len(strengths) == 1:
                        suggested_name = "%s %s%s" % (name, strengths[0]['strength'], strengths[0]['strength_unit'])

                return suggested_name


	def insert_into_drug_search(self, drug_code, name, cat):
		con = dbapi.connect(database=self._db)
                cur = con.cursor()
                query = "insert into cd_drug_search (drug_code, category,name) values (%s,%s,%s) "
                """print query"""
                cur.execute(query,drug_code,cat,name)
                con.commit()

        def insert_into_drug_search(self,con, drug_code, name, cat):
                cur = con.cursor()
                query = "insert into cd_drug_search (drug_code, category,name) values (%s,%s,%s) "
                """print query"""
                cur.execute(query,drug_code,cat,name)
                con.commit()

        """def insert_into_drug_search_with_key(self, drug_code, name, cat):
		con = dbapi.connect(database=self._db)
                cur = con.cursor()
                query = "insert into cd_drug_search (id,drug_code, category,name) values (%s,%s,%s,%s) "
                #print query
                cur.execute(query,drug_code,drug_code,cat,name)
                con.commit()

        def insert_into_drug_search_with_key(self,con, drug_code, name, cat):
                cur = con.cursor()
                query = "insert into cd_drug_search (id,drug_code, category,name) values (%s,%s,%s,%s) "
                #print query
                cur.execute(query,drug_code,drug_code,cat,name)
                con.commit()
        """


        """
        create table link_generic_brand(
            id integer,
            drug_code varchar(30));


        def getNextVal(self,seq):
                con = dbapi.connect(database=self._db)
                cur = con.cursor()
                id = 0
                query = "select nextval('%s'::text) as n" % seq
                cur.execute(query)
                results = cur.fetchall()
                if len(results)>0:
                        for result in results:
                                id = result['n']

                return id

        def getCurrVal(self,con,seq):
                cur = con.cursor()
                id = 0
                query = "select currval('%s'::text) as n" % seq
                cur.execute(query)
                results = cur.fetchall()
                if len(results)>0:
                        for result in results:
                                id = result['n']

                return id


if __name__=='__main__' :
	import sys

        idata = ImportSearchData()
        print "Importing Brand Information"
        idata.import_all_brand_name()
        print "Should check for duplicates and ones that need forms and strength added ie ones from generic companys with the form and strength is not included in the name"
        print "Import ATC Names"
        idata.import_all_ATC_code_name()
        print "Import AFHC Names"
        idata.import_all_AHFS_code_name()
        print "Import Generic Data"
        idata.import_Generics()
        print "Import Ingredients"
        idata.import_all_Ingredients()
        print "Cleaning up Search Names"
        idata.clean_up_search_names()

*/
