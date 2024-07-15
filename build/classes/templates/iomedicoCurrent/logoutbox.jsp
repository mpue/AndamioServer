	   <html:form action="/admin/Logout">
	   <html:hidden property="do" value="logout"/>   
	   <html:hidden property="target" value="Mainpage"/>   
     		
		<input type="hidden" name="username" value="<%= (String)request.getSession().getAttribute("login") %>">
	      		
		<table class="logoutbox" align="center">
		<logic:present name="permission" scope="session">
			<tr>
				<td class="loginText" align="center" valign="middle">
					<b>Angemeldet als :</b>
				</td>
				<td align="center" class="" valign="middle">
					<b>
						<page:value scope="session" property="fullUserName"/>
					</b>
				</td>
				<td align="center" valign="middle">
					<html:submit styleClass="loginButton" property=""><bean:message key="button.logout"/></html:submit>
				</td>				
			</tr>
		</table>
		</logic:present>		
		</html:form>
		
