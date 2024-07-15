<%@ page 
	language="java" 
	contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" 
%>
<%@page import="org.pmedv.pojos.*"%>
<%@page import="org.pmedv.beans.objects.DataType"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.pmedv.context.AppContext"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="java.util.MissingResourceException"%><html>

<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/tags/struts-bean"  prefix="bean" %>
<%@ taglib uri="/tags/struts-html"  prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/pagetags" prefix="page" %>

<%
	ResourceBundle resources = ResourceBundle.getBundle("MessageResources");

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Benutzerprofil f&uuml;r <bean:write name="UserProfileForm" property="user.name"/></title>
<link rel="stylesheet" href="/<page:config property="localPath"/>users/templates/<page:config property="template"/>/css/template.css" type="text/css">
<link rel="icon" href="/<page:config property="localPath"/>templates/<page:config property="localPath"/>/favicon.ico" type="image/x-icon">
<link rel="shortcut icon" href="/<page:config property="localPath"/>templates/<page:config property="localPath"/>/favicon.ico" type="image/x-icon"> 

<script type="text/javascript" src="../jscripts/tiny_mce/tiny_mce.js"></script>


<script type="text/javascript">


tinyMCE.init({
	mode : "exact",
	elements  : "pageContent,commentText",
	language : "de",
	theme : "advanced",
	plugins : "emotions,advimage",
	theme_advanced_disable : "styleselect,formatselect",
	theme_advanced_buttons1_add : "undo,redo,emotions,image",
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
	<div id="logo"></div>
	<br>	
	<html:form action="/users/UserAction.do">
	<table class="standardT" align="center">
		<tr>
			<td class="desc_left" align="left"  valign="middle">
				<div class="clear">
					<a class="button" href="#" onclick="this.blur();document.location.href='../forum/Mainpage.do?do=showForums';return false;"><span>Foren&uuml;bersicht</span></a>
				</div>																
			</td>
			<td class="desc_left" align="left"  valign="middle">
				<div class="clear">
					<a class="button" href="#" onclick="this.blur();document.location.href='MailboxAction.do?do=showFolder';return false;"><span>Private Nachrichten</span></a>
				</div>							
			</td>
			<td class="desc_left" align="left"  valign="middle">
				<logic:present name="permission" scope="session">
					<logic:notEqual value="true" name="editing" scope="session">
						<logic:equal value="true" name="owner" scope="session">
							<a class="button" href="#" onclick="this.blur();document.UserProfileForm.submit();return false;"><span>Profil bearbeiten</span></a>
						</logic:equal>
					</logic:notEqual>
					<logic:equal value="true" name="editing" scope="session">
						<a class="button" href="#" onclick="this.blur();document.UserProfileForm.submit();return false;"><span>Profil speichern</span></a>
					</logic:equal>
				</logic:present>
			</td>
			<td class="desc_left" align="left"  valign="middle" width="50%">
				&nbsp;
			</td>									
		</tr>		
	</table>
	<table class="Title" align="left"> 
		<tr>
			<td align=left  valign="middle">
				<h2 style="color:#000">
				<logic:equal value="true" name="owner" scope="session">
					Ihr Benutzerprofil
				</logic:equal>
				<logic:notEqual value="true" name="owner" scope="session">
					Benutzerprofil von <bean:write name="UserProfileForm" property="user.name"/>
				</logic:notEqual>
				</h2>
			</td>
		</tr>
	</table>	
	
	<table border="0" align="center" width="90%" cellpadding="10px">
		<tr>
			<td>
								
				<logic:equal value="false" name="editing" scope="session">
					<html:hidden property="do" value="editProfile"/>
				</logic:equal>
				<logic:equal value="true" name="editing" scope="session">
					<html:hidden property="do" value="saveProfile"/>
				</logic:equal>				
				<html:hidden name="UserProfileForm" property="id"/>
			
				<table border="0">
					<tr>
						<td colspan="1">
							Stadt :
						</td>
						<td colspan="3">
							<logic:equal value="true" name="editing" scope="session">
								<html:text property="city"/>
							</logic:equal>
							<logic:equal value="false" name="editing" scope="session">
								<bean:write name="UserProfileForm"  property="city" filter="false"/>
							</logic:equal>
						</td>
					</tr>
			
					<tr>
						<td colspan="1">
							Land :
						</td>
						<td colspan="3">
							<logic:equal value="true" name="editing" scope="session">
								<html:text property="country"/>
							</logic:equal>
							<logic:equal value="false" name="editing" scope="session">
								<bean:write name="UserProfileForm"  property="country" filter="false"/>
							</logic:equal>
						</td>
					</tr>
				<logic:iterate id="attribute" name="UserProfileForm"  property="attributes" type="org.pmedv.pojos.Attribute">
					<tr>
						<td colspan="1">
							<%
								try {							
									out.print(resources.getString(attribute.getKey()));
								}
								catch (MissingResourceException m) {
									out.print(attribute.getKey());
								}
							
							%>							 
						</td>
						<td colspan="3">
							<logic:equal value="true" name="editing" scope="session">
								<%
									if (attribute.getDataType().equals(DataType.BOOLEAN)) {
										if (attribute.getValue().equalsIgnoreCase("true")) {
								%>
											<input type="checkbox" name="<bean:write name="attribute" property="key"/>" checked="checked">
								<%
										}
										else {
								%>											
											<input type="checkbox" name="<bean:write name="attribute" property="key"/>">
								<%			
										}
								%>										
								<% 
									}
									else {								
								%>
									<input type="text" name="<bean:write name="attribute" property="key"/>" value="<bean:write name="attribute" property="value"/>"/>
								<%
									}
								%>									
								
							</logic:equal>
							<logic:equal value="false" name="editing" scope="session">
								<%
									if (attribute.getDataType().equals(DataType.BOOLEAN)) {
										if (attribute.getValue().equalsIgnoreCase("true")) {
								%>							
											Ja
								<%
										}
										else {
								%>		
											nein		
								<%
										}
									}
									else {
								%>		
										
										<bean:write name="attribute" property="value"/>
								<%												
									}
								%>		
							</logic:equal>
						</td>
					</tr>		
				</logic:iterate>		
				</table>				
			</td>
		</tr>
	</table>
	<table class="Title" align="left"> 
		<tr>
			<td class="desc_left" align="left"  valign="middle">		
					<h2 style="color:#000">&Uuml;ber mich</h2>
			</td>
		</tr>
	</table>
	<br><br>
	<table border="0" align="center" width="90%" cellpadding="10px">
		<tr>
			<td>
				<logic:equal value="true" name="editing" scope="session"><textarea name="pageContent" id="pageContent" cols="50" rows="10"/><bean:write name="UserProfileForm"  property="pageContent" filter="false"/></textarea></logic:equal>													
				<logic:equal value="false" name="editing" scope="session"><bean:write name="UserProfileForm"  property="pageContent" filter="false"/></logic:equal>				
			</td>		
		</tr>
	</table>		
	</html:form>
	<table class="Title" align="left"> 
		<tr>
			<td class="desc_left" align="left"  valign="middle" colspan="5">		
					<h2 style="color:#000">Benutzerbilder</h2>
			</td>
		</tr>
	</table>
	<table border="0" align="center" width="90%" cellpadding="10px">
		<tr>
			<td width="20%" class="linkPanel">
				<div class="clear">
					<a class="button" href="#" onclick="this.blur();document.location.href='UserAction.do?do=addAvatar';return false;"><span>Bild hinzuf&uuml;gen</span></a>
				</div>																

			</td>
			<td width="20%" class="linkPanel">
				&nbsp;
			</td>		
			<td width="20%" class="linkPanel">
				&nbsp;
			</td>		
			<td width="20%" class="linkPanel">
				&nbsp;
			</td>		
			<td width="20%" class="linkPanel">
				&nbsp;
			</td>		
					
		<tr>
			<td colspan="5">
				<logic:notEmpty name="UserProfileForm" property="avatars">
					<logic:iterate id="avatar" name="UserProfileForm"  property="avatars" type="org.pmedv.pojos.Avatar">
						<img src="/andamio/users/<bean:write name="UserProfileForm" property="user.name"/>/avatars/<%= avatar.getFilename() %>"/>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty name="UserProfileForm" property="avatars">
					Keine Bilder vorhanden.
				</logic:empty>
			</td>
		</tr>	
	</table>
	<table class="Title" align="left"> 
		<tr>
			<td class="desc_left" align="left"  valign="middle" colspan="5">		
				<h2 style="color:#000">Kommentare</h2>
			</td>
		</tr>
	</table>
	<table border="0" align="center" width="90%" cellpadding="10px">
		<tr>
			<td>			
				<jsp:useBean id="UserProfileForm" type="org.pmedv.forms.UserProfileForm" scope="request"/>
				<html:form action="/users/UserComment.do">								
				<html:hidden property="do" value="addComment"/>						
				<html:hidden property="id" name="UserProfileForm"/>			
				<logic:notEmpty name="UserProfileForm" property="comments">
					<logic:iterate id="comment" name="UserProfileForm"  property="comments" type="org.pmedv.pojos.UserComment">
						<p><i>Kommentar von : <bean:write name="comment" property="author" filter="false"/> am : <bean:write name="comment" property="created" filter="false"/></i></p>
						<hr size="1"/>
						<bean:write name="comment" property="text" filter="false"/>
						<logic:equal value="true" name="owner" scope="session">
							<a href="UserComment.do?do=delete&comment_id=<bean:write name="comment" property="id" filter="false"/>">L&ouml;schen</a>
						</logic:equal>										
					</logic:iterate>								
				</logic:notEmpty>
				<logic:empty name="UserProfileForm" property="user.userComments">
					Keine Kommentare vorhanden.
				</logic:empty>
			</td>
		</tr>
		<tr>					
			<td>
				<logic:present name="permission" scope="session">
					<textarea name="commentText" id="commentText" cols="50" rows="5"/></textarea>
				</logic:present> 
			</td>					
		</tr>
		<tr>
			<td>						
				<logic:present name="permission" scope="session">
					<div class="clear">
						<a class="button" href="#" onclick="this.blur();document.UserCommentForm.submit();return false;"><span>Kommentar senden</span></a>
					</div>																
				</logic:present>				 
			</td>					
		</tr>				
	</table>			
	</html:form>
	
</body>
</html>