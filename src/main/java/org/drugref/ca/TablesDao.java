  /*
 *
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General License for more details. * * You should have received a copy of the GNU General License
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
package org.drugref.ca;

import java.util.*;

import org.drugref.ca.dpd.CdDrugProduct;
import org.drugref.ca.dpd.CdDrugSearch;
import org.drugref.ca.dpd.CdTherapeuticClass;

public interface TablesDao {

      String identify();

     
      String version();

     
      Vector list_available_services();

     
      Hashtable list_capabilities();

     
      Object fetch(String attribute, Vector key, Vector services, boolean feelingLucky);

     
      Integer getDrugIdFromDIN(String DIN);

     @Deprecated
      String getDINFromDrugId(Integer drugId);

     
      Integer getDrugpKeyFromDIN(String DIN);

     
      Integer getDrugpKeyFromDrugId(Integer drugId);

     
      List<Integer> getInactiveDrugs();

      @Deprecated
      CdDrugProduct getDrugProduct(String drugcode);

      @Deprecated
      CdTherapeuticClass getATCCodes(String drugcode);

     
      CdDrugSearch getSearchedDrug(int id);

      CdDrugSearch getSearchedDrug(String id);
     
      List<CdDrugSearch> getListAICodes(List<String> listOfDrugCodes);

     
      List<String> findLikeDins(String din);

      @Deprecated
      List listDrugsbyAIGroup(String aiGroup);

     
      List<CdDrugSearch> listDrugsbyAIGroup2(String aiGroup);

     
      Vector listSearchElement2(String str);

     
      String getFirstDinInAIGroup(String aiGroupNo);

      Vector listSearchElement4(String keyword, boolean rightOnly);

     
      Vector listSearchElement3(String str);

     
      Vector listSearchElement(String str);

     
      Vector listSearchElementRoute(String str, String route);

     
      Vector listBrandsFromElement(String drugID);

     
      Vector listSearchElementSelectCategories(String str, Vector cat);

     
      Vector listSearchElementSelectCategories(String str, Vector cat, boolean wildcardLeft, boolean wildcardRight);

     
      Vector getGenericName(String drugID);

     
      Vector getInactiveDate(String pKey);

     
      Vector getForm(String pKey);

     
      Vector listDrugClass(Vector Dclass);

     
      Vector getAllergyWarnings(String atcCode, Vector allergies);

     
      Vector getMadeGenericExample(String groupno, String formCode, boolean html);

     
      Vector getDrug(String pKey, boolean html);

     
      Vector getDrugByDrugCode(String pKey, String formCode, boolean html);

     
      Vector getAllergyClasses(Vector allergies);

     
      Vector getTcATC(String atc);

    
}
