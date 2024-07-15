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

<%= request.getAttribute("userAgent") %>

</head>
<body>

	<table class="userProfile">
		<tr class="userProfile">
			<td colspan="5">
				<logic:equal value="true" name="owner" scope="session">
					<h1>Dein Benutzerprofil</h1>
				</logic:equal>
				<logic:notEqual value="true" name="owner" scope="session">
					<h1>Benutzerprofil von <bean:write name="UserProfileForm" property="user.name"/></h1>
				</logic:notEqual>
			</td>
		</tr>
		<tr class="userProfile">
			<td width="20%">
				<b><a href="../index.jsp" target="_self">Startseite</a></b>				
			</td>
			<td width="20%">
				<b>Profil</b>
			</td>
			<td width="20%">
				<b><a href="UserAction.do?do=showGalleries&user_id=<%= request.getParameter("user_id")%>" target="_self">Bilder</a></b>
			</td>
			<td width="20%">
				&nbsp;
			</td>
			<td width="20%">
				&nbsp;
			</td>			
		</tr>
		<tr class="userProfile">
			<td colspan="1">
				&nbsp;
			</td>
			<td colspan="4">
				<html:form action="/users/UserAction.do">				
				<logic:equal value="false" name="editing" scope="session">
					<html:hidden property="do" value="editProfile"/>
				</logic:equal>
				<logic:equal value="true" name="editing" scope="session">
					<html:hidden property="do" value="saveProfile"/>
				</logic:equal>				
				<html:hidden name="UserProfileForm" property="id"/>
			
				<table class="userProfile">
					<tr>
						<td colspan="1">
							Stadt 
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
							Land 
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
					<tr>
						<td colspan="4">
							<logic:present name="permission" scope="session">
								<logic:notEqual value="true" name="editing" scope="session">
									<logic:equal value="true" name="owner" scope="session">
										<p><a href="#" onclick="document.UserProfileForm.submit()">bearbeiten</a></p>
									</logic:equal>
								</logic:notEqual>
								<logic:equal value="true" name="editing" scope="session">
									<p><a href="#" onclick="document.UserProfileForm.submit()">speichern</a></p>
								</logic:equal>
							</logic:present>
							<logic:equal value="true" name="editing" scope="session"><textarea name="pageContent" id="pageContent" cols="50" rows="10"/><bean:write name="UserProfileForm"  property="pageContent" filter="false"/></textarea></logic:equal>													
							<logic:equal value="false" name="editing" scope="session"><bean:write name="UserProfileForm"  property="pageContent" filter="false"/></logic:equal>				
						</td>		
					</tr>
				</table>
				</html:form>
			</td>
		</tr>
		<tr>
			<td colspan="1">
				&nbsp;
			</td>
			<td colspan="4">
			
				<jsp:useBean id="UserProfileForm" type="org.pmedv.forms.UserProfileForm" scope="request"/>
				<html:form action="/users/UserComment.do">								
				<html:hidden property="do" value="addComment"/>						
				<html:hidden property="id" name="UserProfileForm"/>			
				<table class="userProfile">
					<tr>
						<td>
							Kommentare : 
						</td>					
					</tr>
					<tr>
						<td>
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
								<hr size="1"/>
								Keine Kommentare vorhanden.
							</logic:empty>
						</td>					
					</tr>			
					<tr>
						<td colspan="4">
							<logic:present name="permission" scope="session">
								<textarea name="commentText" id="commentText" cols="50" rows="5"/></textarea>
							</logic:present> 
						</td>					
					</tr>
					<tr>
						<td colspan="4">
							<logic:present name="permission" scope="session">
								<p><a href="#" onclick="document.UserCommentForm.submit()">Kommentar senden</a></p>
							</logic:present>				 
						</td>					
					</tr>				
				</table>			
				</html:form>
			</td>
		</tr>

	</table>
	
</body>
</html>