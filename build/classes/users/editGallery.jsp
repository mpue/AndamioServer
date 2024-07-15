<%@ taglib uri="/tags/struts-bean"  prefix="bean" %>
<%@ taglib uri="/tags/struts-html"  prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<html>
	<head>
		<title>Gallerie bearbeiten</title>
	</head>
	<body>		

	<html:form action="/users/GalleryAction.do">
		<html:hidden property="do" value="saveGallery"/>
		<html:hidden property="id" name="GalleryForm"/>
		<table border="0" cellspacing="5px" cellpadding="0" align="center">
			<tr>
				<td class="text_left">
					Galeriename*:
				</td>
				<td class="text_left">
					<html:text property="galleryname" name="GalleryForm" size="20"/>
				</td>
			</tr>
			<tr>
				<td class="text_left">
					Beschreibung:
				</td>
				<td class="text_left">
					<html:text property="description" name="GalleryForm" size="20"/>
				</td>
			</tr>
	
			<tr>
				<td class="text_left" colspan="2">
					Galerietext (wird über den Bildern dargestellt):
				</td>
			</tr>
			<tr>
				<td colspan="2" align="center">
					<html:textarea property="gallerytext" name="GalleryForm"/>
				</td>
			</tr>
			<tr>
				<td colspan="2" align="center">
					<html:submit value="Speichern" />
				</td>
			</tr>
			
		</table>				
		</html:form>
		<p>
			<a href="GalleryAction.do?do=addImage&gallery_id=<%= request.getParameter("gallery_id") %>">Bild hinzuf&uuml;gen</a>		
		</p>
		<% int i=0; %>
		<table border="1" cellspacing="5px" cellpadding="0" align="center">
			<tr>
			<logic:iterate id="image" name="GalleryForm" property="images" type="org.pmedv.pojos.Image">
				<td>
					<p align="center">
						<img width="160" src="<%= request.getSession().getAttribute("login")%>/galleries/<bean:write name="GalleryForm" property="galleryname" filter="false"/>/<bean:write name="image" property="filename" filter="false"/>"/>
					</p>
					<p align="center">
						<bean:write name="image" property="name" filter="false"/>
					</p>
					
					<p align="center">
						<a href="GalleryAction.do?do=deleteImage&image_id=<bean:write name="image" property="id" filter="false"/>&gallery_id=<bean:write name="GalleryForm" property="id" filter="false"/>" target="_self">l&ouml;schen</a>
					</p> 					
				<td>
			<% if (++i%4==0) {%>
			</tr>
			<tr>
			<%} %>
			</logic:iterate>
			<tr>
		</table>
	</body>
</html>