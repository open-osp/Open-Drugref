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

package org.drugref.ca.dpd.fetch;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import org.drugref.ca.dpd.CdDrugSearch;
import org.drugref.util.JpaUtils;

/**
 * used to insert the "made" generic drugs to the database.
 * @author jaygallagher
 */
public class TempNewGenericImport {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        TempNewGenericImport nt = new TempNewGenericImport();
        Long l=nt.run();
        System.out.println("***time spent="+l);
    }
    public void p(String str, String s) {
        System.out.println(str + "=" + s);
    }

    public void p(String str) {
        System.out.println(str);
    }
     public long run() {
        long startTime = System.currentTimeMillis();
         int coumpoundsWGeneric = 0;
            int coumpounds = 0;
            int singles = 0;
            int singlesWGeneric = 0;
            int nameAltered = 0;
        EntityManager entityManager = JpaUtils.createEntityManager();
        try {
            EntityTransaction tx = entityManager.getTransaction();
            tx.begin();
            String s="1";
            String initialSQL = "select distinct ai_group_no from cd_drug_product where number_of_ais = '1' order by ai_group_no";
            Query query = entityManager.createNativeQuery(initialSQL);

            List<String> aiGroupList = query.getResultList();

           
            for (String obj: aiGroupList){
                int numIng = Integer.parseInt(obj.substring(0, 2));

               // p("obj",obj);
               // p("numIng",Integer.toString(numIng));
                
                if (numIng >1){
                    coumpounds++;
                    Query formQuery = entityManager.createNativeQuery("select distinct pharmaceutical_CD_form from cd_drug_product, cd_form where  cd_drug_product.drug_code=cd_form.drug_code and cd_drug_product.ai_group_no = '"+obj+"'");
                    List<String> forms = formQuery.getResultList();

                    for(String drugForm: forms){
                        Query sgQuery = entityManager.createNativeQuery("select distinct cd_drug_product.ai_group_no,cd_active_ingredients.ingredient,cd_active_ingredients.strength, cd_active_ingredients.strength_unit,cd_form.pharmaceutical_CD_form, cd_form.pharm_CD_form_code   from cd_drug_product, cd_form, cd_active_ingredients where cd_drug_product.drug_code = cd_active_ingredients.drug_code and cd_drug_product.drug_code = cd_form.drug_code and  ai_group_no like '"+obj+"' and cd_form.pharmaceutical_CD_form = '"+drugForm+"'") ;
                        List<Object[]> sgList = sgQuery.getResultList();
                        StringBuffer sb = new StringBuffer();
                        boolean first = true;
                        String form = "";
                        String aiNum = "";
                        String formCode = "";
                        for(Object[] drug:sgList){
                            if(!first){
                                sb.append("/");
                            }
                            sb.append(removeBrackets(""+drug[1])+" "+drug[2]+drug[3]);

                            if (!form.equals("") && !form.equals(""+drug[4])){
                                nameAltered++;
                            }
                            form = ""+drug[4];
                            formCode = ""+drug[5];
                            aiNum = ""+drug[0];

                            first = false;
                        }
                        sb.append(" "+form);
                        System.out.println("*"+aiNum+" "+sb.toString());
                        CdDrugSearch drugSearch = new CdDrugSearch();
                         drugSearch.setName(sb.toString());
                         drugSearch.setDrugCode(aiNum+"+"+formCode);
                         drugSearch.setCategory(19);
                        /*
                         entityManager.persist(drugSearch);
                        entityManager.flush();
                        entityManager.clear();
                         */
                    }
                }else{
                    singles++;
                    Query sgQuery = entityManager.createNativeQuery("select distinct cd_drug_product.ai_group_no,cd_active_ingredients.ingredient,cd_active_ingredients.strength, cd_active_ingredients.strength_unit,cd_form.pharmaceutical_CD_form, cd_form.pharm_CD_form_code   from cd_drug_product, cd_form, cd_active_ingredients where cd_drug_product.drug_code = cd_active_ingredients.drug_code and cd_drug_product.drug_code = cd_form.drug_code and  ai_group_no like '"+obj+"%'") ;
                    List<Object[]> sgList = sgQuery.getResultList();
                    for(Object[] drug:sgList){
                        String drugName = ""+drug[1];
                        int lastBracket = drugName.lastIndexOf("(");
                        if (lastBracket != -1){
                            drugName = drugName.substring(0,lastBracket);
                            //nameAltered++;
                        }
                        CdDrugSearch drugSearch = new CdDrugSearch();
                        drugSearch.setName(drugName+" "+drug[2]+drug[3]+" "+drug[4]);
                        drugSearch.setDrugCode(""+drug[0]+"+"+drug[5]);
                        drugSearch.setCategory(18);
                        entityManager.persist(drugSearch);
                        entityManager.flush();
                        entityManager.clear();
                        //System.out.println(drug[0]+" "+drugName+" "+drug[2]+drug[3]+" "+drug[4]);
                    }
                }
            }

            tx.commit();
        } finally {

            System.out.println("closing entityManager");
            JpaUtils.close(entityManager);
            System.out.println("entityManagerClosed");
        }
        System.out.println("COMP GEN "+coumpoundsWGeneric+" COMP "+coumpounds+" single "+singles+" singleWGen "+singlesWGeneric+" NAme Altered "+nameAltered);
        long endTime = System.currentTimeMillis();
        return endTime - startTime;

    }
     
    String removeBrackets(String drugName){
        int lastBracket = drugName.lastIndexOf("(");
        if (lastBracket != -1){
            drugName = drugName.substring(0,lastBracket);
            //nameAltered++;
        }
        return drugName;
    }




}
