
<!-- Generated by ExtGen v1.3 -->

<%@page import="
   java.util.*,
   javax.naming.*,
   java.io.*,
   de.hybris.platform.jalo.order.*,
   de.hybris.platform.jalo.order.price.*,
   de.hybris.platform.jalo.util.*,
   de.hybris.platform.jalo.product.*,
   de.hybris.platform.jalo.*,
   de.hybris.platform.core.*,
   de.hybris.platform.util.*,
   de.hybris.platform.oci.jalo.*,
   de.hybris.platform.oci.constants.*,
   de.hybris.platform.oci.jalo.interfaces.*" %>	   
   
<html>
<head>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="docu.css" rel="stylesheet" type="text/css">
<meta content="DocBook XSL Stylesheets V1.68.1" name="generator">
</head>
<%
	
	// open session with WebSessionFunctions or reuse old one
	JaloSession jaloSession = null;
	try
	{
		jaloSession = WebSessionFunctions.getSession( request );
	}
	catch( JaloSystemNotInitializedException e )
	{
		out.println("<html>");
		out.println("<head> <title>WebModule Generated by ExtGen v1.3 (oci)</title>");
		out.println("<link rel=\"stylesheet\" type=\"text/css\" media=\"all\" href=\"/admin/styles.css\"></head>");
		out.println("<body>");
		out.println("<h2><font color=\"red\">System not initialized</font></h2>");
		out.println("Please go to this <a href=\"/systemcreator/initialize.jsp\">page</a> for system initialization");
		out.println("</body></html>");
		out.flush();
		return;
	}
			
%>

<%!

	public Collection searchProducts( String searchstring, int count )
	{
		JaloSession.getCurrentSession().getTypeManager().getComposedType(Product.class);
		Map values = Collections.EMPTY_MAP;
		
		String query = "SELECT {p:PK} FROM {product AS p}";
		if( searchstring != null )
		{
			query += " WHERE {p:code} LIKE ?searchstring OR {p:name:o} LIKE '%"+ searchstring + "%' OR {p:description:o} LIKE '%"+ searchstring + "%'";
			values = new HashMap();
			values.put("searchstring", "%" + searchstring + "%");
		}
		
		return JaloSession.getCurrentSession().getFlexibleSearch().search(
			query,                                       // the query text
			values,                                      // no values needed here
			Collections.singletonList( Product.class ),  // the result signature list
			true,                                        // fail on unknown (untyped) fields
			true,                                        // dont need total
			0, count                                     // result range
		).getResult();
	}
%>