<%@ page 
	language="java" 
	contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" 
%>
<%@page import="org.pmedv.pojos.*"%>

<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/tags/struts-bean"  prefix="bean" %>
<%@ taglib uri="/tags/struts-html"  prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Benutzergalerie von <bean:write name="GalleryForm" property="user.name" filter="false"/> : <bean:write name="GalleryForm" property="galleryname" filter="false"/></title>
<link rel="stylesheet" href="style.css" type="text/css"/>
<script type="text/javascript" src="../jscripts/tiny_mce/tiny_mce.js"></script>


<script type="text/javascript">


tinyMCE.init({
	mode : "exact",
	elements  : "pageContent,commentText",
	language : "de",
	theme : "advanced",
	theme_advanced_disable : "styleselect,formatselect",
	theme_advanced_buttons1_add : "undo,redo",
	// link,unlink,image,bullist,numlist,indent,outdent,undo,redo,cleanup,help,anchor",
	theme_advanced_buttons2 : "",
	theme_advanced_buttons3 : "",					
	theme_advanced_toolbar_location : "top",
	theme_advanced_toolbar_align : "center",
	theme_advanced_resizing : true,
	theme_advanced_statusbar_location : 'bottom',
	theme_advanced_resize_horizontal : true,
	apply_source_formatting : false,
	auto_reset_designmode : false,
	force_br_newlines : true,
	remove_linebreaks : false, 
	force_p_newlines : false,	
	relative_urls : false,
	convert_urls : false	
});


</script>

</head>
<body>

	<table class="userProfile">
		<tr class="userProfile">
			<td colspan="5">
				<h1>Benutzergalerie von <bean:write name="GalleryForm" property="user.name" filter="false"/> : <bean:write name="GalleryForm" property="galleryname" filter="false"/></h1>
			</td>
		</tr>
		<tr class="userProfile">
			<td width="20%">
				<b><a href="../index.jsp" target="_self">Startseite</a></b>				
			</td>
			<td width="20%">
				<b><a href="UserAction.do?do=showProfile&user_id=<%= request.getParameter("user_id") %>" target="_self">Profil</a></b>
			</td>
			<td width="20%">
				<b>Bilder</b>
			</td>
			<td width="20%">
				<b><a href="MailboxAction.do?do=showFolder" target="_self">Deine Post</a></b>
			</td>
			<td width="20%">
				<b>Blah</b>
			</td>			
		</tr>
		<tr class="userProfile">
			<td colspan="1">
			</td>
			<td colspan="4">
				<logic:empty property="images" name="GalleryForm">
					<br/><p>Noch keine Bilder vorhanden.</p>
				</logic:empty>
				<logic:notEmpty property="images" name="GalleryForm">
					<% int i=0; %>
					<table border="1" cellspacing="5px" cellpadding="0" align="center">
						<tr>
						<logic:iterate id="image" name="GalleryForm" property="images" type="org.pmedv.pojos.Image">
							<td>
								<p align="center">
									<img width="160" src="./<bean:write name="GalleryForm" property="user.name" filter="false"/>/galleries/<bean:write name="GalleryForm" property="galleryname" filter="false"/>/<bean:write name="image" property="filename" filter="false"/>"/>
								</p>
								<p align="center">
									<bean:write name="image" property="name" filter="false"/>
								</p>							
							<td>
						<% if (++i%4==0) {%>
						</tr>
						<tr>
						<%} %>
						</logic:iterate>
						<tr>
					</table>
				</logic:notEmpty>			
			</td>
		</tr>
		<tr>
			<td colspan="5">
				<b><a href="UserAction.do?do=showGalleries&user_id=<%= session.getAttribute("userId")%>" target="_self">Zur&uuml;ck zur Galerie&uuml;bersicht</a></b>
			</td>
		</tr>

	</table>
	
</body>
</html>