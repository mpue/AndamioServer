	   
<%@page import="org.pmedv.session.UserSession"%>

<html:form action="/admin/Logout">
  <html:hidden property="do" value="logout"/>   
  <html:hidden property="target" value="forum"/>   
  	      		
 		<%
 	   		UserSession userSession = new UserSession();
 			userSession.init(false,request);
 			userSession.getAttributes();
 	   	%>
     		
<input type="hidden" name="username" value="<%= userSession.getLogin() %>">
     		
<table class="loginbox">
	<logic:present name="permission" scope="session">
	<tr>
		<td width="50%">
			&nbsp;
		</td>	
		<td class="text_center_small" align="right">
			<b>Eingeloggt als:</b>
		</td>
		<td align="right" class="text_center_small">
			<page:value scope="session" property="fullUserName"/>		
		</td>
		</logic:present>
		<logic:notPresent name="permission" scope="session">
		<td class="text_center_small" align="right">
			<br>Sie sind nicht angemeldet.<br><br>
		</td>
		</logic:notPresent>	
		<td align="right">
			<div class="clear">
				<a class="button" href="#" onclick="this.blur();document.LogoutForm.submit();return false;"><span><bean:message key="button.logout"/></span></a>
			</div>									
		</td>				
	</tr>
</table>

</html:form>
