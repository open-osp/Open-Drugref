<%-- 
    Document   : Update
    Created on : Sep 12, 2009, 12:30:21 PM
    Author     : jaygallagher
--%>

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
        <h1>Update Drugref Database!</h1>
         <%
        org.drugref.ca.dpd.fetch.DPDImport dpdImport = new org.drugref.ca.dpd.fetch.DPDImport();
        long timeDataImport=0L;
        timeDataImport=dpdImport.doItDifferent();
        timeDataImport=(timeDataImport/1000)/60;
        org.drugref.ca.dpd.fetch.TempNewGenericImport newGenericImport=new org.drugref.ca.dpd.fetch.TempNewGenericImport();
        long timeGenericImport=0L;
        timeGenericImport=newGenericImport.run();//in miliseconds
        timeGenericImport=(timeGenericImport/1000)/60;
        HashMap hm=dpdImport.numberTableRows();
        Set keys=hm.keySet();
        Iterator iter=keys.iterator();
        %>
        <table border="1">
            <tr>
                <th>Table Name</th>
                <th>Number of Rows</th>
            </tr>
         <%
        while(iter.hasNext()){
            String s=(String)iter.next();
            Long v=(Long)hm.get(s);%>
            <tr>
                <td><%=s%></td>
                <td><%=v%></td>
            </tr>
        <%
        }%>

        </table>
        <p>Time spent on importing data: <%=timeDataImport%> minutes</p>
        <p>Time spent on new generic import: <%=timeGenericImport%> minutes</p>
    </body>
</html>
