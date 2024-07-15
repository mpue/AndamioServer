   <html:form action="/admin/LoginDialog" autocomplete="off" >
   		<html:hidden property="do" value="login"/>   
   		<html:hidden property="target" value="frontpage"/>   
		<table class="loginbox" align="center" cellpadding="3px" cellspacing="0px">	
			<tr>
				<td class="loginText" align="center">
					Benutzer
				</td>
				<td align="center">
					<html:text property="username" styleClass="login" size="10"/>
				</td>
				<td class="loginText" align="center">
					Passwort
				</td>
				<td align="center">
					<html:password property="password" styleClass="login" size="10"/>
				</td>
				<td align="center">
					 <html:submit styleClass="loginButton" property="" onclick="processLogin();"><bean:message key="button.login"/></html:submit>
				</td>				
			</tr>
		</table>	
  </html:form>					
