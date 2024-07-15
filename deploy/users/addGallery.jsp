<%@ taglib uri="/tags/struts-bean"  prefix="bean" %>
<%@ taglib uri="/tags/struts-html"  prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<html>
	<head>
		<title>Gallerie hinzuf&uuml;gen</title>
	</head>
	<body>		

	<html:form action="/users/GalleryAction.do">
		<html:hidden property="do" value="addGallery"/>
		<div align="center">
		<logic:messagesPresent>
			<html:errors />			
		</logic:messagesPresent>
		<div align="center">
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
					<html:submit value="Anlegen" />
				</td>
			</tr>
			
		</table>				
		</html:form>
	</body>
</html>