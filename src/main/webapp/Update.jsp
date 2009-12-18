<%-- 
    Document   : Update
    Created on : Sep 12, 2009, 12:30:21 PM
    Author     : jaygallagher
--%>

<%@page contentType="text/html" pageEncoding="MacRoman"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=MacRoman">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Updating Drugref Database!</h1>
         <%
        org.drugref.ca.dpd.fetch.DPDImport dpdImport = new org.drugref.ca.dpd.fetch.DPDImport();
        dpdImport.doItDifferent();
        org.drugref.ca.dpd.fetch.TempNewGenericImport newGenericImport=new org.drugref.ca.dpd.fetch.TempNewGenericImport();
        newGenericImport.run();
        %>
    </body>
</html>
