<%@page contentType="text/html" pageEncoding="MacRoman"%>
<%@page import="java.io.BufferedReader,java.io.FileReader,java.io.IOException,java.lang.Object,java.util.Hashtable,java.util.Vector,org.apache.xmlrpc.XmlRpcClientLite,org.apache.xmlrpc.XmlRpcException" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%String str = request.getParameter("searchVal");
if (str == null) str = "";
%>

<html >
<head>
<meta http-equiv="Content-Type" content="text/html; charset=MacRoman">
<title>JSP Page</title>
</head>
<body onload="document.getElementById('search').focus();">
<h1>Hello World!</h1>

<form action="index.jsp">
<input type="text" id="search" name="searchVal" value="<%=str%>" />
<input type="submit"/>
</form>
<hr>
<%
if (request.getParameter("searchVal") != null){



Vector params = new Vector();
params.addElement(request.getParameter("searchVal"));
Object obj = callWebserviceLite("list_search_element2",params);
System.out.println("obj "+obj.getClass().getName());

if(obj instanceof org.apache.xmlrpc.XmlRpcException){
org.apache.xmlrpc.XmlRpcException xmle = ( org.apache.xmlrpc.XmlRpcException) obj;
xmle.printStackTrace();
System.out.println("PROB with "+str);

}else{
Vector vec = (Vector) obj;

%>

<h3><%=request.getParameter("searchVal")%> -- <%=getDef( vec)%></h3>
<table>



<tr><th colspan="3" align="left">NEW GEN</th></tr>
<% for (Object Hash: vec){
Hashtable h = (Hashtable) Hash;
String cat = ""+h.get("category");
if(!cat.equals("18")) continue;
%>
<tr>
<td><%=h.get("category")%></td>
<td><%=h.get("id")%></td>
<td><%=h.get("name")%></td>
</tr>
<%}%>
<tr><th colspan="3" align="left">NEW GEN COMP</th></tr>
<% for (Object Hash: vec){
Hashtable h = (Hashtable) Hash;
String cat = ""+h.get("category");
if(!cat.equals("19")) continue;
%>
<tr>
<td><%=h.get("category")%></td>
<td><%=h.get("id")%></td>
<td><%=h.get("name")%></td>
</tr>
<%}%>


<tr><th colspan="3" align="left">ATC</th></tr>
<% for (Object Hash: vec){
Hashtable h = (Hashtable) Hash;
String cat = ""+h.get("category");
if(!cat.equals("8")) continue;
%>
<tr>
<td><%=h.get("category")%></td>
<td><%=h.get("id")%></td>
<td><%=h.get("name")%></td>
</tr>
<%}%>





<tr><th colspan="3" align="left">AHFS</th></tr>
<% for (Object Hash: vec){
Hashtable h = (Hashtable) Hash;
String cat = ""+h.get("category");
if(!cat.equals("10")) continue;
%>
<tr>
<td><%=h.get("category")%></td>
<td><%=h.get("id")%></td>
<td><%=h.get("name")%></td>
</tr>
<%}%>
<tr><th colspan="3" align="left">GEN ORIG</th></tr>
<% for (Object Hash: vec){
Hashtable h = (Hashtable) Hash;
String cat = ""+h.get("category");
if(!cat.equals("12")) continue;
%>
<tr>
<td><%=h.get("category")%></td>
<td><%=h.get("id")%></td>
<td><%=h.get("name")%></td>
</tr>
<%}%>
<tr><th colspan="3" align="left">GEN COMP ORIG</th></tr>
<% for (Object Hash: vec){
Hashtable h = (Hashtable) Hash;
String cat = ""+h.get("category");
if(!cat.equals("11")) continue;
%>
<tr>
<td><%=h.get("category")%></td>
<td><%=h.get("id")%></td>
<td><%=h.get("name")%></td>
</tr>
<%}%>
<tr><th colspan="3" align="left">INGREDIENT</th></tr>
<% for (Object Hash: vec){
Hashtable h = (Hashtable) Hash;
String cat = ""+h.get("category");
if(!cat.equals("14")) continue;
%>
<tr>
<td><%=h.get("category")%></td>
<td><%=h.get("id")%></td>
<td><%=h.get("name")%></td>
</tr>
<%}%>


<tr><th colspan="3" align="left">BRAND</th></tr>
<% for (Object Hash: vec){
Hashtable h = (Hashtable) Hash;
String cat = ""+h.get("category");
if(!cat.equals("13")) continue;
%>
<tr>
<td><%=h.get("category")%></td>
<td><%=h.get("id")%></td>
<td><%=h.get("name")%></td>
</tr>
<%}%>




</table>
<%}%>







<% }


%>

<pre>


Return Extra Data Points
-Route
-Form ( is now part of the drug code )
-Derived Brand eg (Brand name that was searched that lead to this generic)
-Example Brand (To show with the generic name for clarification)
-

How to order results. (Should a relevance be returned or decided upon at the oscar side?)

Check Inactive Drugs

1.Yasmin 21 or Yasmin 28.. how would you know the generic?

2.Does compound import need to be changed?
</pre>

</body>
</html>



<%!

/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/





/**
*
* @author jaygallagher
*/



private String getDef(Vector vec){
int brand = 0;
int atc = 0;
int ahfs = 0;
int gen = 0;
int genCompound = 0;
int ingred = 0;
int newGen = 0;
int newGenComp = 0;
int somethingElse = 0;



for(Object obj:vec){
Hashtable h = (Hashtable) obj;
String num = ""+ h.get("category");
if (num.equals("")){
num = "0";
}
Integer cat = new Integer(num);
if(cat == 13){
brand++;
}else if(cat == 8){
atc++;
}else if(cat == 10){
ahfs++;
}else if(cat == 12){
gen++;
}else if(cat == 11){
genCompound++;
}else if(cat == 14){
ingred++;
}else if(cat == 18){
newGen++;
}else if(cat == 19){
newGenComp++;
}else{
somethingElse++;
}

}

return " brand = "+ brand +" atc = "+atc+" ahfs = "+ahfs+" gen = "+gen+" genCompound = "+genCompound+" ingred = "+ingred+" newGen = "+newGen+" newGenComp = "+newGenComp+" else ="+somethingElse;


}


private Object callWebserviceLite(String procedureName,Vector params) throws Exception{
// System.out.println("#CALLDRUGREF-"+procedureName);
Object object = null;
try{
//System.out.println("server_url :"+server_url);
XmlRpcClientLite server = new XmlRpcClientLite("http://localhost:8080/drugref2/DrugrefService");
object = server.execute(procedureName, params);
}catch (XmlRpcException exception) {

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
}
return object;
}




%>