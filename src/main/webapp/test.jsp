

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="org.apache.xmlrpc.*,java.util.*" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!!!!!!</h1>


        <%
        Vector paramsListDrugs = new Vector();
        Hashtable haListDrugs=new Hashtable();
        paramsListDrugs.addElement("ESIDRIX");
        System.out.println("params "+paramsListDrugs);
        Object vecListDrugs = callWebserviceLite("list_search_element3",paramsListDrugs);
        System.out.println("vec "+vecListDrugs.toString());
        out.write("list_search_element result: " + vecListDrugs + "\n");

     /*   Vector paramsGetDrug = new Vector();
        boolean bool=true;
        paramsGetDrug.addElement("12188");
        paramsGetDrug.addElement(bool);
        System.out.println("params   "+paramsGetDrug);
        Object vecGetDrug = (Object) callWebserviceLite("get_drug",paramsGetDrug);
        System.out.println("vec "+vecGetDrug);
        out.write("get_drug : "+vecGetDrug + "\n");

        Vector paramsListDrugElementRoute = new Vector();
        paramsListDrugElementRoute.addElement("abc");
        paramsListDrugElementRoute.addElement("ra");
        System.out.println("params   "+paramsListDrugElementRoute);
        Object vecListDrugElementRoute = (Object) callWebserviceLite("list_search_element_route",paramsListDrugElementRoute);
        System.out.println("vec "+vecListDrugElementRoute);
        out.write("list_drug_element_route result: "+vecListDrugElementRoute + "\n");

        Vector paramsListBrandsFromElement = new Vector();
        paramsListBrandsFromElement.addElement("26440");
        System.out.println("params  "+paramsListBrandsFromElement);
        Object vecListBrandsFromElement = (Object) callWebserviceLite("list_brands_from_element",paramsListBrandsFromElement);
        System.out.println("vec "+vecListDrugElementRoute);
        out.write("list_brands_from_element result: "+vecListBrandsFromElement + "\n");

       Vector paramsListSearchElementSelectCategories = new Vector();
        String searchStr = "a,pro, bb, co";
        Vector catVec = new Vector();
        catVec.addElement(11);
        catVec.addElement(13);
        paramsListSearchElementSelectCategories.addElement(searchStr);
        paramsListSearchElementSelectCategories.addElement(catVec);
        System.out.println("params   "+paramsListSearchElementSelectCategories);
        Object vecListSearchElementSelectCategories = (Object) callWebserviceLite("list_search_element_select_categories",paramsListSearchElementSelectCategories);
        System.out.println("vec "+vecListSearchElementSelectCategories);
        out.write("list_search_element_select_categories: "+vecListSearchElementSelectCategories + "\n");

        Vector paramsGetDrugForm = new Vector();
        catVec.addElement(11);
        catVec.addElement(13);
        paramsGetDrugForm.addElement("12188");
        System.out.println("params   "+paramsGetDrugForm);
        Object vecGetDrugForm = (Object) callWebserviceLite("get_form",paramsGetDrugForm);
        System.out.println("vec "+vecGetDrugForm);
        out.write("getDrugForm : "+vecGetDrugForm + "\n");

        Vector paramsGetGenericName = new Vector();
        paramsGetGenericName.addElement("18638");
        System.out.println("params    "+paramsGetGenericName);
        Object vecGetGenericName = (Object) callWebserviceLite("get_generic_name",paramsGetGenericName);
        System.out.println("vec "+vecGetGenericName);
        out.write("getGenericName : "+vecGetGenericName + "\n");

        Vector paramsListDrugClass = new Vector();
        Vector p=new Vector();
        p.addElement(123);
        p.addElement(456);
        paramsListDrugClass.addElement(p);
        System.out.println("params   "+paramsListDrugClass);
        Object vecListDrugClass = (Object) callWebserviceLite("list_drug_class",paramsListDrugClass);
        System.out.println("vec "+vecListDrugClass);
        out.write("list_drug_class : "+vecListDrugClass + "\n");
  */

    /*    Vector paramsGetAlergyWarnings = new Vector();
        String drugs="73063";
        Vector allergies = new Vector();
        paramsGetAlergyWarnings.addElement(drugs);
        paramsGetAlergyWarnings.addElement(allergies);
        System.out.println("params    "+paramsGetAlergyWarnings);
        Object vecGetAlergyWarnings = (Object) callWebserviceLite("get_allergy_warnings",paramsGetAlergyWarnings);
        System.out.println("vec " + vecGetAlergyWarnings);
        out.write("getAlergyWarnings : " + vecGetAlergyWarnings + "\n");
*/
   /*    Vector paramsGetAlergyWarnings2 = new Vector();
        String drugs2="B01AA03";
        Vector allergies2 = new Vector();
        Hashtable ht=new Hashtable();
        ht.put("type","11");
        ht.put("description","BLACK ALDER");
        ht.put("id","0");
        allergies2.add(ht) ;
        */
     /*   ht=new Hashtable();
        ht.put("type","11");
        ht.put("description","BLACK ROOT");
        ht.put("id","1");
        allergies2.add(ht);
        */
   /*     ht=new Hashtable();
        ht.put("type","0");
        ht.put("description","CA2");
        ht.put("id","2");
        allergies2.add(ht);
        ht=new Hashtable();
        ht.put("type","0");
        ht.put("description","CUSTOM_ALLERGY");
        ht.put("id","3");
        allergies2.add(ht);
        ht=new Hashtable();
        ht.put("type","11");
        ht.put("description","MUSTARD BLACK");
        ht.put("id","4");
        allergies2.add(ht);
*/
        /*
        ht=new Hashtable();
        ht.put("type","13");
        ht.put("description","ACTONEL 35MG");
        ht.put("id","5");
        allergies2.add(ht);
*/

    /*    paramsGetAlergyWarnings2.addElement(drugs2);
        paramsGetAlergyWarnings2.addElement(allergies2);
        System.out.println("params    "+paramsGetAlergyWarnings2);
        Object vecGetAlergyWarnings2 = (Object) callWebserviceLite("get_allergy_warnings",paramsGetAlergyWarnings2);
        System.out.println("vec " + vecGetAlergyWarnings2);
        out.write("getAlergyWarnings : " + vecGetAlergyWarnings2 + "\n");
    */
        /*Vector paramsFakeFetch = new Vector();
        System.out.println("params    " + paramsFakeFetch);
        Object vecFakeFetch = (Object) callWebserviceLite("fake_fetch",paramsFakeFetch);
        System.out.println("vec " + vecFakeFetch);
        out.write("getFakeFetch : " + vecFakeFetch + "\n");
        */

        Vector paramsFetch = new Vector();
        paramsFetch.addElement("interactions_byATC");
        Vector key=new Vector();
        //key= ['B01AA03', 'D10AF52', 'C10BX02', 'A12AX', 'H02AB07', 'B01AA03', 'M04AA01', 'C09DA01', 'C09AA01']
        key.addElement("D01AC08");
        key.addElement("J01FA01");
        key.addElement("B01AA03");
        key.addElement("B01AA03");
        key.addElement("S01AA17");

        paramsFetch.addElement(key);
        //
        //Vector services=new Vector();
        //boolean b=true;
        //paramsFetch.addElement(services);
        //paramsFetch.addElement(b);
        System.out.println("params    " + paramsFetch.toString());
        Object vecFetch = (Object) callWebserviceLite("fetch",paramsFetch);
        System.out.println("vec " + vecFetch);
        out.write("getFetch : " + vecFetch + "\n");


   /*    Vector paramsFetch = new Vector();
        paramsFetch.addElement("warnings_byATC,bulletins_byATC,interactions_byATC,get_guidelines");
        Vector key=new Vector();

        //key= [D01AC08, J01FA01, B01AA03]
        key.addElement("D01AC08");
        key.addElement("J01FA01");
        key.addElement("B01AA03");

        paramsFetch.addElement(key);
        paramsFetch.addElement("jaygallagher@gmail.com");
        //
        //Vector services=new Vector();
        //boolean b=true;
        //paramsFetch.addElement(services);
        //paramsFetch.addElement(b);
        System.out.println("params    " + paramsFetch.toString());
        Object vecFetch = (Object) callWebserviceLite("fetch",paramsFetch);
        System.out.println("vec " + vecFetch);
        out.write("getFetch : " + vecFetch + "\n");
*/

        %>

    </body>
</html>
<%!

private Object callWebserviceLite(String procedureName,Vector params) throws Exception{
        // System.out.println("#CALLDRUGREF-"+procedureName);
         Object object = new Object();
         try{
             System.out.println("in callWebserviceLite");
            // System.out.println("server_url :"+server_url);
           // XmlRpcClientLite server = new XmlRpcClientLite("http://localhost:8084/DrugrefService");
             XmlRpcClientLite server = new XmlRpcClientLite("http://localhost:8080/drugref2/DrugrefService");
            // XmlRpcClientLite server = new XmlRpcClientLite("http://localhost:4082/drugref2/DrugrefService");
            System.out.println("procedureName="+procedureName);
            //XmlRpcClientLite server = new XmlRpcClientLite("http://localhost:4080/drugref2/DrugrefService");
            System.out.println("server's url :"+server.getURL());
            System.out.println("server="+server);
            //server.
            /*try{
                String[] args=new String[3];
                args[0]="http://localhost:8084/DrugrefService";
                args[1]=procedureName;
                args[2]="testParam";
                System.out.println("before main");
                server.main(args);
                System.out.println("after main");
            }
            catch(Exception e){
                System.out.println("main's e: ");
                e.printStackTrace();
            }*/
            object = server.execute(procedureName, params);
            System.out.println("Object in callWebserviceLite:       "+object);
         }catch (Exception exception) {
             exception.printStackTrace();
             /*
                System.err.println("JavaClient: XML-RPC Fault #" +
                                   Integer.toString(exception.code) + ": " +
                                   exception.toString());
                                   exception.printStackTrace();

                throw new Exception("JavaClient: XML-RPC Fault #" +
                                   Integer.toString(exception.code) + ": " +
                                   exception.toString());

         } catch (Exception exception) {
                System.err.println("JavaClient: " + exception.toString());
                exception.printStackTrace();
                throw new Exception("JavaClient: " + exception.toString());
 */
         }
         return object;
     }



%>
