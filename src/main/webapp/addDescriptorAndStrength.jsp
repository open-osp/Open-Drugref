<%@page contentType="text/html" pageEncoding="MacRoman"%>
<%@page import="java.util.*" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=MacRoman">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Added Descriptor and Strength</h1>
         <%
        org.drugref.ca.dpd.fetch.DPDImport dpdImport = new org.drugref.ca.dpd.fetch.DPDImport();
        List addedDescriptor=dpdImport.addDescriptorToSearchName();
        List addedStrength=dpdImport.addStrengthToBrandName();
        %>
        <div  style="color: blue;" >Number of drug names being added descriptor: <a><%=addedDescriptor.size()%></a></div>
        <br><div id="des" style="display:none">ids in cd_drug_search <%=addedDescriptor%></div>
        <br><div style="color: blue;">Number of drug names being added strength: <a><%=addedStrength.size()%></a></div>
        <br><div id="str" style="display:none">ids in cd_drug_search <%=addedStrength%></div>
    </body>
</html>
