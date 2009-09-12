

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

        org.drugref.ca.dpd.fetch.DPDImport dpdImport = new org.drugref.ca.dpd.fetch.DPDImport();

        //String[] entities = {"CdActiveIngredients"};
        dpdImport.doItDifferent();//executeOn(entities);
/*
        
=======
>>>>>>> other
        Vector paramsListDrugs = new Vector();
        Hashtable haListDrugs=new Hashtable();
        paramsListDrugs.addElement("coumarin");
<<<<<<< local
        paramsListDrugs.addElement(haListDrugs);
=======
>>>>>>> other
        System.out.println("params "+paramsListDrugs);
<<<<<<< local
        Object vecListDrugs = (Object) callWebserviceLite("list_drugs",paramsListDrugs);
=======
        Object vecListDrugs = (Object) callWebserviceLite("list_search_element",paramsListDrugs);
>>>>>>> other
        System.out.println("vec "+vecListDrugs);
<<<<<<< local
        out.write("list_drugs result: " + vecListDrugs + "<hr>");
=======
        out.write("list_search_element result: " + vecListDrugs + "\n");

        Vector paramsGetDrug = new Vector();
        boolean bool=true;
        paramsGetDrug.addElement("12188");
        paramsGetDrug.addElement(bool);
        System.out.println("params   "+paramsGetDrug);
        Object vecGetDrug = (Object) callWebserviceLite("get_drug",paramsGetDrug);
        System.out.println("vec "+vecGetDrug);
        out.write("get_drug : "+vecGetDrug + "\n");
>>>>>>> other

        Vector paramsListDrugElementRoute = new Vector();
        paramsListDrugElementRoute.addElement("abc");
        paramsListDrugElementRoute.addElement("ra");
        System.out.println("params   "+paramsListDrugElementRoute);
<<<<<<< local
        Object vecListDrugElementRoute = (Object) callWebserviceLite("list_drug_element_route",paramsListDrugElementRoute);
=======
        Object vecListDrugElementRoute = (Object) callWebserviceLite("list_search_element_route",paramsListDrugElementRoute);
>>>>>>> other
        System.out.println("vec "+vecListDrugElementRoute);
<<<<<<< local
        out.write("list_drug_element_route result: "+vecListDrugElementRoute + "<hr>");
=======
        out.write("list_drug_element_route result: "+vecListDrugElementRoute + "\n");
>>>>>>> other

        Vector paramsListBrandsFromElement = new Vector();
        paramsListBrandsFromElement.addElement("26440");
        System.out.println("params  "+paramsListBrandsFromElement);
        Object vecListBrandsFromElement = (Object) callWebserviceLite("list_brands_from_element",paramsListBrandsFromElement);
        System.out.println("vec "+vecListDrugElementRoute);
<<<<<<< local
        out.write("list_brands_from_element result: "+vecListBrandsFromElement + "<hr>");
=======
        out.write("list_brands_from_element result: "+vecListBrandsFromElement + "\n");
>>>>>>> other

<<<<<<< local
        Vector paramsListSearchElementSelectCategories = new Vector();
=======
       Vector paramsListSearchElementSelectCategories = new Vector();
>>>>>>> other
        String searchStr = "a,pro, bb, co";
        Vector catVec = new Vector();
        catVec.addElement(11);
        catVec.addElement(13);
        paramsListSearchElementSelectCategories.addElement(searchStr);
        paramsListSearchElementSelectCategories.addElement(catVec);
        System.out.println("params   "+paramsListSearchElementSelectCategories);
        Object vecListSearchElementSelectCategories = (Object) callWebserviceLite("list_search_element_select_categories",paramsListSearchElementSelectCategories);
        System.out.println("vec "+vecListSearchElementSelectCategories);
<<<<<<< local
        out.write("list_search_element_select_categories: "+vecListSearchElementSelectCategories + "<hr>");
=======
        out.write("list_search_element_select_categories: "+vecListSearchElementSelectCategories + "\n");
>>>>>>> other

        Vector paramsGetDrugForm = new Vector();
        catVec.addElement(11);
        catVec.addElement(13);
        paramsGetDrugForm.addElement("12188");
        System.out.println("params   "+paramsGetDrugForm);
<<<<<<< local
        Object vecGetDrugForm = (Object) callWebserviceLite("getDrugForm",paramsGetDrugForm);
=======
        Object vecGetDrugForm = (Object) callWebserviceLite("get_form",paramsGetDrugForm);
>>>>>>> other
        System.out.println("vec "+vecGetDrugForm);
<<<<<<< local
        out.write("getDrugForm : "+vecGetDrugForm + "<hr>");
=======
        out.write("getDrugForm : "+vecGetDrugForm + "\n");
>>>>>>> other

        Vector paramsGetGenericName = new Vector();
<<<<<<< local
        paramsGetGenericName.addElement("17570");
=======
        paramsGetGenericName.addElement("18638");
>>>>>>> other
        System.out.println("params    "+paramsGetGenericName);
<<<<<<< local
        Object vecGetGenericName = (Object) callWebserviceLite("getGenericName",paramsGetGenericName);
=======
        Object vecGetGenericName = (Object) callWebserviceLite("get_generic_name",paramsGetGenericName);
>>>>>>> other
        System.out.println("vec "+vecGetGenericName);
<<<<<<< local
        out.write("getGenericName : "+vecGetGenericName + "<hr>");

        Vector paramsListDrugElement = new Vector();
        paramsListDrugElement.addElement("coumarin");
        System.out.println("params  "+paramsListDrugElement);
        Object vecListDrugElement = (Object) callWebserviceLite("list_drug_element",paramsListDrugElement);
        System.out.println("vec "+vecListDrugElement);
        out.write("list_drug_element : "+vecListDrugElement + "<hr>");
=======
        out.write("getGenericName : "+vecGetGenericName + "\n");
>>>>>>> other

        Vector paramsListDrugClass = new Vector();
        Vector p=new Vector();
        p.addElement(123);
        p.addElement(456);
        paramsListDrugClass.addElement(p);
        System.out.println("params   "+paramsListDrugClass);
        Object vecListDrugClass = (Object) callWebserviceLite("list_drug_class",paramsListDrugClass);
        System.out.println("vec "+vecListDrugClass);
<<<<<<< local
        out.write("list_drug_class : "+vecListDrugClass + "<hr>");

        Vector paramsGetDrug = new Vector();
        boolean bool=true;
        paramsGetDrug.addElement("12188");
        paramsGetDrug.addElement(bool);
        System.out.println("params   "+paramsGetDrug);
        Object vecGetDrug = (Object) callWebserviceLite("getDrug",paramsGetDrug);
        System.out.println("vec "+vecGetDrug);
        out.write("getDrug : "+vecGetDrug + "<br>");

=======
        out.write("list_drug_class : "+vecListDrugClass + "\n");
>>>>>>> other

        Vector paramsGetAlergyWarnings = new Vector();
        String drugs="73063";
        Vector allergies = new Vector();
        paramsGetAlergyWarnings.addElement(drugs);
        paramsGetAlergyWarnings.addElement(allergies);
        System.out.println("params    "+paramsGetAlergyWarnings);
<<<<<<< local
        Object vecGetAlergyWarnings = (Object) callWebserviceLite("getAlergyWarnings",paramsGetAlergyWarnings);
=======
        Object vecGetAlergyWarnings = (Object) callWebserviceLite("get_allergy_warnings",paramsGetAlergyWarnings);
>>>>>>> other
        System.out.println("vec " + vecGetAlergyWarnings);
<<<<<<< local
        out.write("getAlergyWarnings : " + vecGetAlergyWarnings + "<hr>");
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
            XmlRpcClientLite server = new XmlRpcClientLite("http://localhost:8080/DrugrefService");
            //System.out.println("yyyyyyyyyyyyyyyyy");
            object = (Object) server.execute(procedureName, params);
            System.out.println("Object in callWebserviceLite:       "+object);
         }catch (Exception exception) {
             //System.out.println("ggggggggggg");
            System.out.println(exception.getClass().getName()+ "message in exception: "+exception.getMessage());
            exception.getStackTrace();
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
