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
        <h1>Hello World!!</h1>


        <%
        Vector params = new Vector();
        params.addElement("2345");
         Object vec =  callWebserviceLite("get_generic_name",params);
        out.write(""+vec);
        %>

    </body>
</html>
<%!

private Object callWebserviceLite(String procedureName,Vector params) throws Exception{
        // System.out.println("#CALLDRUGREF-"+procedureName);
         Object object = null;
         try{
            // System.out.println("server_url :"+server_url);
            XmlRpcClientLite server = new XmlRpcClientLite("http://localhost:8080/DrugrefService");
            object = server.execute(procedureName, params);
         }catch (Exception exception) {
            System.out.println(exception.getClass().getName());
            exception.getStackTrace();
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