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
 * <OSCAR TEAM>
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster Unviersity
 * Hamilton
 * Ontario, Canada
 */
package org.drugref;

import java.util.Hashtable;
import org.drugref.ca.dpd.TablesDao;
import java.util.Vector;
import org.drugref.ca.dpd.CdDrugProduct;
import org.drugref.ca.dpd.CdDrugSearch;
import org.drugref.ca.dpd.CdTherapeuticClass;
import org.drugref.util.SpringUtils;

/**
 *
 * @author jaygallagher & jacksonbi
 */

//TO DO: fetch
public class Drugref {
        TablesDao queryDao = (TablesDao) SpringUtils.getBean("tablesDao");

        public Vector list_search_element(String searchStr){
                //TablesDao queryDao = new TablesDao();
                Vector vec=queryDao.listSearchElement(searchStr);
                return vec;
        }

        /*
         * This is a testing method for now!
         *
         * This should take the search string, find all the possible matches.
         * For each match get the ai code. ( first 7 characters only ) Then research all the available types using this key.
         */
        public Vector list_search_element2(String searchStr){
                //TablesDao queryDao = new TablesDao();
                Vector vec=queryDao.listSearchElement2(searchStr);
                return vec;
        }

        public Vector list_search_element3(String searchStr){
                //TablesDao queryDao = new TablesDao();
                Vector vec=queryDao.listSearchElement3(searchStr);
                return vec;
        }


        /*
         * Returns ATC, DIN, Route, Form
         */
        public Vector get_drug_2(String pkey,boolean html){
                System.out.println("IN get_drug_2"+pkey);
                Hashtable returnHash = new Hashtable();
                Integer id = Integer.parseInt(pkey);
                
                CdDrugSearch cds = queryDao.getSearchedDrug(id);
                if (cds != null){
                    cds.getDrugCode();
                    cds.getCategory();
                    returnHash.put("drugCode",cds.getDrugCode());
                    returnHash.put("cat",cds.getCategory());
                    System.out.println("drugCode "+cds.getDrugCode()+ " category "+cds.getCategory());

                    if (cds.getCategory() == 13){
                       return queryDao.getDrug(pkey, true);
                    }else if (cds.getCategory() == 18 || cds.getCategory() == 19){
                       int pl = cds.getDrugCode().indexOf("+");
                       String aiCode = cds.getDrugCode().substring(0, pl);
                       String formCode = cds.getDrugCode().substring(pl+1);
                       return queryDao.getMadeGenericExample(aiCode,formCode,false);
//                       String[] code = cds.getDrugCode().split("\\+");
//                       return queryDao.getMadeGenericExample(code[0],code[1],false);
                    }
                }
                return null;
        }



        public Vector list_search_element_route(String str, String route) {
                //TablesDao queryDao = new TablesDao();
                Vector vec=queryDao.listSearchElementRoute(str,route);
                return vec;
        }

        public Vector list_brands_from_element(String drugID) {
                System.out.println("in drugref.java list_brands_from_element");
                System.out.println("drugID="+drugID);
                //TablesDao queryDao = new TablesDao();
                Vector vec=queryDao.listBrandsFromElement(drugID);
                System.out.println("after listBrandsFromElement.");
                for(int i=0;i<vec.size();i++){
                        System.out.println("vector="+vec.get(i));
                }
                return vec;
        }

        public Vector list_search_element_select_categories(String str, Vector cat) {
                //TablesDao queryDao = new TablesDao();
                Vector vec=queryDao.listSearchElementSelectCategories(str,cat);
                return vec;
        }

       public Vector get_generic_name(String drugID) {
                System.out.println("in get_generic_name,drugref.java");
                //TablesDao queryDao = new TablesDao();
                Vector vec=new Vector();
                try{
                    vec=queryDao.getGenericName(drugID);
                }
                catch(Exception e){e.printStackTrace();}
                for (int i=0; i<vec.size();i++){
                        System.out.println("the returned vec: vec.get(i)="+vec.get(i));
                }
                return vec;
        }
       public Vector get_form(String pKey) {
                //TablesDao queryDao = new TablesDao();
                Vector vec=queryDao.getForm(pKey);
                return vec;
        }
        public Vector list_drug_class(Vector Dclass) {
                //TablesDao queryDao = new TablesDao();
                Vector vec=queryDao.listDrugClass(Dclass);
                return vec;
        }
        public Vector get_allergy_warnings(String atcCode, Vector allergies) {

                //TablesDao queryDao = new TablesDao();
                Vector vec=queryDao.getAllergyWarnings(atcCode,allergies);
                return vec;
        }
        public Vector get_drug(String pKey, boolean html) {
                //TablesDao queryDao = new TablesDao();
                Vector vec=queryDao.getDrug(pKey,html);
                return vec;
        }
}