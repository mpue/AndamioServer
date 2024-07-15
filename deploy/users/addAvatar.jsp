<%@ taglib uri="/tags/struts-bean"  prefix="bean" %>
<%@ taglib uri="/tags/struts-html"  prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/pagetags" prefix="page" %>
<html>
	<head>
		<link rel="stylesheet" href="/<page:config property="localPath"/>users/templates/<page:config property="template"/>/css/template.css" type="text/css">
		<link rel="icon" href="/<page:config property="localPath"/>templates/<page:config property="localPath"/>/favicon.ico" type="image/x-icon">
		<link rel="shortcut icon" href="/<page:config property="localPath"/>templates/<page:config property="localPath"/>/favicon.ico" type="image/x-icon"> 
	
		<title>Bild hinzuf&uuml;gen</title>
	</head>
	<body>		
		<div id="logo"></div>
		<br>	
		<html:form enctype="multipart/form-data" action="/users/UserAction.do">
		<html:hidden property="do" value="uploadAvatar"/>   		
		<html:hidden property="id" name="UserProfileForm"/>
		<table class="Title" align="left"> 
			<tr>
				<td class="desc_left" align="left"  valign="middle">		
						<h2 style="color:#000">Benutzerbild hinzuf&uuml;gen</h2>
				</td>
			</tr>
		</table>		
		<br>
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
						Als Standard verwenden
					</td>					
					<td>
						<html:checkbox property="defaultImage" name="UserProfileForm"/>
					</td>				
				</tr>
				<tr>
					<td class="text_left" colspan="2">
						Beschreibung : 
					</td>					
				</tr>				
				<tr>
					<td class="text_left" colspan="2">
						<html:textarea property="description" name="UserProfileForm" cols="85" rows="5"/>
					</td>					
				</tr>
				
				<tr>	
					<td>
						<div class="clear">
							<a class="button" href="#" onclick="this.blur();document.UserProfileForm.submit();return false;"><span>Bild speichern</span></a>
						</div>						
					</td>
					<td>
						<div class="clear">
							<a class="button" href="#" onclick="this.blur();document.location.href='UserAction.do?do=showProfile&user_id=<page:value scope="session" property="userId"/>';return false;"><span>Zur&uuml;ck zum Profil</span></a>
						</div>						
					</td>
				</tr>	
			</table>
		</html:form>						
	</body>
</html>