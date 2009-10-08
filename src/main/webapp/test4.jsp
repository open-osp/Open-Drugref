<%@page import="java.util.*,net.sf.json.*,org.drugref.ca.dpd.*"  %><%
Enumeration en = request.getParameterNames();
while(en.hasMoreElements()){
    System.out.println(">"+en.nextElement());
 }

String searchStr = request.getParameter("query");
if (searchStr == null){
    searchStr = request.getParameter("name");
    }
System.out.println("searc "+searchStr);
TablesDao queryDao = new TablesDao();
System.out.println("CALLING listSearchElement3");
Vector<Hashtable> vec=queryDao.listSearchElement3(searchStr);
System.out.println("VEC "+vec.size());

if ( request.getParameter("name") !=null ){
//List list = new ArrayList();
    for (Hashtable h: vec){
       //list.add( h.get("name") );
        Integer id = (Integer) h.get("id");
        System.out.println("querd "+queryDao.getSearchedDrug(id));
       out.write( h.get("name") +"\n");
    }
}else{
/* list.add( "JSON" );
 list.add( "1" );
 list.add( "2.0" );
 list.add( "true" );
 *
 */
Hashtable d = new Hashtable();
d.put("results",vec);
response.setContentType("text/x-json");
//JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON( vec );

JSONObject jsonArray = (JSONObject) JSONSerializer.toJSON( d );
jsonArray.write(out);

}
%>