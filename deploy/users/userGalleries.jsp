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
<title>Benutzerprofil f&uuml;r <bean:write name="UserProfileForm" property="user.name"/></title>
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
				<h1>Benutzergalerien von <bean:write name="UserProfileForm" property="user.name"/></h1>
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
				<logic:equal value="true" name="owner" scope="session">			
					<p><a href="GalleryAction.do?do=createGallery">Neue Bildergalerie anlegen</a></p>
				</logic:equal>
				<logic:empty property="galleries" name="UserProfileForm">
					<br/><p>Noch keine Galerien vorhanden.</p>
				</logic:empty>
				<logic:notEmpty property="galleries" name="UserProfileForm">
				<table border="0" cellspacing="5" cellpadding="5" width="50%" align="left">
					<logic:iterate id="gallery" name="UserProfileForm"  property="galleries" type="org.pmedv.pojos.Gallery">
						<tr>
							<td>								
								<a href="GalleryAction.do?do=showGallery&gallery_id=<bean:write name="gallery" property="id" filter="false"/>&user_id=<%= request.getParameter("user_id") %>">
									<bean:write name="gallery" property="galleryname" filter="false"/>&nbsp;
								</a>
							</td>
							<logic:equal value="true" name="owner" scope="session">
								<td>
									<a href="GalleryAction.do?do=deleteGallery&gallery_id=<bean:write name="gallery" property="id" filter="false"/>">L&ouml;schen</a>
								</td>
								<td>
									<a href="GalleryAction.do?do=editGallery&gallery_id=<bean:write name="gallery" property="id" filter="false"/>">Bearbeiten</a>
								</td>
							</logic:equal>
						</tr>												
					</logic:iterate>
				</table>
				</logic:notEmpty>
			</td>
			<td colspan="4">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td colspan="1">
				&nbsp;
			</td>
			<td colspan="4">
				&nbsp;
			</td>
		</tr>

	</table>
	
</body>
</html>