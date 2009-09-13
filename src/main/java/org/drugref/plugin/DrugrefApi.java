/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drugref.plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import org.drugref.util.JpaUtils;

/**
 *
 * @author jackson
 */
    //public void drugrefApiBase (String host="localhost", Integer port=8123, String database="drugref",String user="drugref",String pwd="drugref",String backend ="postgres") {
    public class DrugrefApi {

        private String name;
        private String version;
        private String versiondate;
        private String notcasesensitive;
        private String dbapi;
        private String db;
        private String wildcard;
        private Vector languages = new Vector();
        private Vector countires = new Vector();
        private Hashtable params = new Hashtable();
        private Hashtable queries = new Hashtable();
        private Hashtable cache = new Hashtable();
        private Hashtable provided = new Hashtable();
        private Hashtable required = new Hashtable();
        private Hashtable funcs = new Hashtable();

        public  DrugrefApi(){}
        
        public  DrugrefApi(String host, Integer port, String database, String user, String pwd, String backend) {
            this.name = "generic adapter";
            this.version = "0.2";
            this.versiondate = "11.01.2005";
            this.params.put("host", host);
            this.params.put("port", port);
            this.params.put("database", database);
            this.params.put("user", user);
            this.params.put("pwd", pwd);
            this.params.put("backend", backend);
            this.dbapi = null;
            this.db = null;
            this.wildcard = "*";
            this.languages.addElement("?");
            this.countires.addElement("?");
            if (backend.equals("postgres")) {
                this.wildcard = "%";
                this.notcasesensitive = "ilike";
            }
        }


        public void addfunc(Object func, String requires, Vector provides) {

            for (int i = 0; i < provides.size(); i++) {
                Hashtable ha = new Hashtable();
                ha.put("func", func);
                ha.put("requires", requires);
                this.provided.put(provides.get(i), ha);
            }
        }

        public void addquery(){

        }
    


  public Object get(String attribute, Vector key){
                        Hashtable haError=new Hashtable();
                        String valStr="Attribute "+attribute+" not available";
                        haError.put("Error", valStr);
            try {
                Vector attr=(Vector)this.cache.get(attribute);
                if (attr.get(1).equals(key)){
                                //print "attr[0]=",attr[0];
                                return attr.get(0);
                   }
            } catch(Exception e){}
            Hashtable queryinfo=new Hashtable();
            try{
                       //check whether we can provide this attribute in principle (= service provider available)

                         queryinfo = new Hashtable((Hashtable) this.provided.get(attribute));
                        //print "queryinfo=",queryinfo
            } catch(Exception e){
                        //print "error1"
                        
                        return haError;
            }
            Vector results=new Vector();
                try{
                        //if service provider available, try fiurst to retrieve the attribute by registered function
                        Object funcName = queryinfo.get("func") ;

/*                        if (funcName instanceof ApiHolbrook.Holbrook.checkInteractionsATC){
                                ApiHolbrook ah=new ApiHolbrook();
                                ApiHolbrook.Holbrook ahh= ah.new Holbrook();
                                ApiHolbrook.Holbrook.checkInteractionsATC ahhc = ahh.new checkInteractionsATC();
                                results=ahhc.checkInteractionsATC(key);
                        }*/
                }catch(Exception e){
                    try{
                        String query=queryinfo.get("query").toString();
                        Hashtable params=new Hashtable();
                        params.put(queryinfo.get("requires"), key);
                        results=runquery(query,params);

                    }
                    catch(Exception exception){
                        results=null;
                    }
                }

                if(results.equals(null)){
                    Hashtable htError=new Hashtable();
                    htError.put("Error", "Not Found");
                    return htError;//return a hashtable
                }
                String checkStr=(String)queryinfo.get("requires");
                if(checkStr.startsWith("_search")){
                    return results;//return a vector
                }
                else{
                    assert results.size()==1:results.size();
                    Hashtable result=new Hashtable((Hashtable)results.get(0));
                    Enumeration resultKeys=result.keys();
                    while(resultKeys.hasMoreElements()){
                        String attrib=resultKeys.nextElement().toString();
                        //List valList=new ArrayList();
                        //valList.add(result.get(attrib));
                        //valList.add(key);
                        Vector valVec=new Vector();
                        valVec.addElement(result.get(attrib));
                        valVec.addElement(key);
                        this.cache.put(attrib, valVec);
                        try{
                            Vector attr=(Vector)this.cache.get(attrib);
                            Vector attrElm=new Vector((Vector)attr.get(1));
                            if (attrElm.equals(key)){
                                return attr.get(0);
                            }
                        }
                        catch(Exception e){ }
                    }
                }
                return haError;
 }

        private void connect(){}

        private Vector runquery(String query, Hashtable params){
            Vector returnVal=new Vector();
                        //start to run query
                        EntityManager em = JpaUtils.createEntityManager();
                        EntityTransaction tx = em.getTransaction();
                        tx.begin();
                        Query queryOne = em.createQuery(query);
                        Enumeration enm=params.keys();
                        while(enm.hasMoreElements()){
                            String keyHashtable=(String)enm.nextElement();
                            queryOne.setParameter(keyHashtable, params.get(keyHashtable));
                        }
                        List results = new ArrayList(queryOne.getResultList());
                        tx.commit();

                        Collections.copy(returnVal,results);
                        return returnVal;
        }

        public String getWildcard(){
            return this.wildcard;
        }

        public String getName(){
            return this.name;
        }

        public String getVersion(){
            return this.getVersion();
        }

        public Hashtable listCapabilities(){
            return this.provided;
        }

    }

