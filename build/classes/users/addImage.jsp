<%@ taglib uri="/tags/struts-bean"  prefix="bean" %>
<%@ taglib uri="/tags/struts-html"  prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<html>
	<head>
		<title>Bild hinzuf&uuml;gen</title>
	</head>
	<body>		
		<html:form enctype="multipart/form-data" action="/users/GalleryAction.do">
		<html:hidden property="do" value="uploadImage"/>   		
		<html:hidden property="id" name="GalleryForm"/>
		<table border="0" cellspacing="5px" cellpadding="0" align="center" width="50%">
				<tr>
					<td class="text_left" >
						Dateiname*:
					</td>					
					<td>
						<html:file  styleClass="bs2" property="file" size="30"/>
					</td>
				</tr>
				<tr>
					<td class="text_left" >
						Name des Bildes : 
					</td>					
					<td>
						<html:text property="imageName" name="GalleryForm" size="30"/>
					</td>
				</tr>
				<tr>
					<td class="text_left" colspan="2">
						Beschreibung : 
					</td>					
				</tr>

				<tr>
					<td class="text_left" colspan="2">
						<html:textarea property="imageDescription" name="GalleryForm" cols="85" rows="5"/>
					</td>					
				</tr>
				
				<tr>	
					<td colspan="2">
						<html:submit value="Anlegen" />
					</td>
				</tr>	
			</table>
		</html:form>						
	</body>
</html>