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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import org.drugref.ca.dpd.TablesDao;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author jaygallagher & jacksonbi
 */

//TO DO: getDrug ,getAllergyWarnings, fetch?

public class Drugref {

    public Vector list_drugs(String searchStr,Hashtable tags) {
        List list = new ArrayList();
        TablesDao queryDao = new TablesDao();
        list = queryDao.listSearchElement(searchStr);
        Vector vec=new Vector(list);
        System.out.print(vec);
        return vec;
    }

    public Vector list_drug_element_route(String searchStr, String route) throws Exception {

        List list = new ArrayList();
        TablesDao queryDao = new TablesDao();
        list = queryDao.listSearchElementRoute(searchStr, route);
        //System.out.println("list               "+list);
        Vector vec = new Vector(list);
        //System.out.println("list size "+list.size());
        //for (int i = 0; i < result.length; i++) {
        //  System.out.println("result               " + result[i]);
        //}
        return vec;
    }

    public Vector list_brands_from_element(String drugID) throws Exception {
        List list = new ArrayList();
        TablesDao queryDao = new TablesDao();
        list = queryDao.listBrandsFromElement(drugID);
        //System.out.println("list               "+list);
        Vector vec = new Vector(list);
        //System.out.println("list size "+list.size());
        //for (int i = 0; i < result.length; i++) {
        //    System.out.println("result               " + result[i]);
        // }
        return vec;

    }

    public Vector list_search_element_select_categories(String str, Vector cat) {
        List list = null;
        TablesDao queryDao = new TablesDao();
        list = queryDao.listSearchElementSelectCategories(str, cat);
        //System.out.println("list               "+list);
        Vector vec = new Vector(list);
        return vec;
    }

    public Hashtable getDrugForm(int pKey) {
        List list = new ArrayList();
        TablesDao queryDao = new TablesDao();
        list = queryDao.getForm(pKey);
        //System.out.println("list               "+list);
        Object[] result = null;
        result = list.toArray();
        //System.out.println("list size "+list.size());
        for (int i = 0; i < result.length; i++) {
            System.out.println("result               " + result[i]);
        }

        Vector vec = new Vector(list);

        Enumeration enume = vec.elements();
        while (enume.hasMoreElements()) {
            System.out.println("aaaaaaaaaaaaaaaa" + enume.nextElement().toString());
        }
        Hashtable returnVal = (Hashtable) vec.get(0);

        return returnVal;

    }


    public Hashtable getGenericName(String pKey) throws Exception {
        TablesDao queryDao = new TablesDao();
        List list = queryDao.getGenericName(pKey);
        Vector vec = new Vector(list);
        Hashtable returnVal = (Hashtable) vec.get(0);
        return returnVal;
    }

    public Vector list_drug_element(String searchStr) throws Exception {
        Vector params = new Vector();
        params.addElement(searchStr);
        TablesDao queryDao = new TablesDao();
        List list = queryDao.listSearchElement(searchStr);
        Vector vec = new Vector(list);
        return vec;
    }

    public Vector list_drug_class(Vector dclass) {
        List list = new ArrayList();
        TablesDao queryDao = new TablesDao();
        list = queryDao.listDrugClass(dclass);
        Vector vec = new Vector(list);
        return vec;

    }

    public Hashtable getDrug(String pKey, Boolean boolVal) throws Exception{
        List list= new ArrayList();
         System.out.println(pKey + "  "+boolVal);
         TablesDao queryDao = new TablesDao();
        list = queryDao.getDrug(pKey,boolVal);
        Vector vec = new Vector(list);
        Hashtable returnVal=(Hashtable) vec.get(0);
        return returnVal;
     }

    public Vector getAlergyWarnings(String drugs,Vector allergies) throws Exception{
        List list= new ArrayList();
         System.out.println(drugs + "  "+allergies);
         TablesDao queryDao = new TablesDao();
        list = queryDao.getAllergyWarnings(drugs,allergies);
        Vector vec = new Vector(list);
        return vec;
     }
}
