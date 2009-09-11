/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drugref.plugin;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import org.drugref.ca.dpd.Interactions;
import org.drugref.util.JpaUtils;

/**
 *
 * @author jackson
 */
public class ApiHolbrook extends PluginImpl {

    //public void drugrefApiHolbrook(String host="localhost", Integer port=8123,String database="drugref2", String user="drugref", String pwd="drugref", String backend="postgres")
    public class Holbrook {

        public void Holbrook() {
            String host = "localhost";
            Integer port = 8123;
            String database = "drugref2";
            String user = "drugref";
            String pwd = "drugref";
            String backend = "postgres";
            
            ApiBase ab=new ApiBase();
            ApiBase.DrugrefApi abd = ab.new  DrugrefApi(host, port, database, user, pwd, backend) ;
            checkInteractionsATC cia = new checkInteractionsATC();
            Vector vec = new Vector();
            vec.addElement("interaction_byATC");
            abd.addfunc(cia, "_search_interactions", vec);
        }

        public Hashtable legend(String keyword) {
            Hashtable ha = new Hashtable();
            if (keyword.equals("effect")) {
                ha.put("a", "augments (no clinical effect)");
                ha.put("A", "augments");
                ha.put("i", "inhibits (no clinical effect)");
                ha.put("I", "inhibits");
                ha.put("n", "has no effect on");
                ha.put("N", "has no effect on");
                ha.put(" ", "unkown effect on");
            } else if (keyword.equals("significance")) {
                ha.put("1", "minor");
                ha.put("2", "moderate");
                ha.put("3", "major");
                ha.put(" ", "unknown");
            } else if (keyword.equals("evidence")) {
                ha.put("P", "poor");
                ha.put("F", "fair");
                ha.put("G", "goog");
                ha.put(" ", "unknown");
            }
            return ha;
        }

        public class checkInteractionsATC {

            public Vector checkInteractionsATC(Vector druglist) {
                Vector interactions = new Vector();
                for (int i = 0; i < druglist.size(); i++) {
                    Vector idrugs = new Vector(druglist);
                    String drug = (String) druglist.get(i);
                    idrugs.removeElement(drug);
                    for (int j = 0; j < idrugs.size(); j++) {

                        String query = "select hi from Interactions where affectingatc = (:affecting) and affectedatc = (:affected)";
                        /*Hashtable params=new Hashtable();
                        params.put("affecting", drug);
                        params.put("affected",idrugs.get(j));
                         */
                        //start to run query
                        EntityManager em = JpaUtils.createEntityManager();
                        EntityTransaction tx = em.getTransaction();
                        tx.begin();
                        Query queryOne = em.createQuery(query);
                        queryOne.setParameter("affecting", drug);
                        queryOne.setParameter("affected", idrugs.get(j));
                        List<Interactions> results = queryOne.getResultList();
                        tx.commit();
                        

                        // Vector results = this.runquery(query, params);
                        for (int q = 0; q < results.size(); q++) {
                            Hashtable interaction = new Hashtable();
                            Interactions result = results.get(q);
                            interaction.put("affectingatc", result.getAffectingatc());
                            interaction.put("affectedatc", result.getAffectedatc());
                            interaction.put("affectingdrug", result.getAffectingdrug());
                            interaction.put("affecteddrug", result.getAffecteddrug());
                            interaction.put("significance", result.getSignificance());
                            interaction.put("effect", result.getEffect());
                            interaction.put("evidence", result.getEvidence());
                            interaction.put("comment", result.getComment());
                            interactions.addElement(interaction);
                        }
                    }
                }
                return interactions;
            }
        }
    }

    public class drugrefPlugin {
        private PluginImpl dpi = new PluginImpl();
        private ApiHolbrook dab = new ApiHolbrook();

        public drugrefPlugin() {
            
            this.dpi.setName("Holbrook Drug Interactions");
            this.dpi.setVersion("1.0");
            this.dpi.setPlugin(dab);
            ApiBase ab=new ApiBase();

            ApiBase.DrugrefApi da=ab.new DrugrefApi();
            da = (ApiBase.DrugrefApi) this.dpi.getPlugin();
//            this.dpi.setProvides(da.listCapabilities());
        }
    }
}
