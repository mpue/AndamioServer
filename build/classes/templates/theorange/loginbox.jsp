					   <html:form action="/admin/LoginDialog" autocomplete="off" >
					   		<html:hidden property="do" value="login"/>   
					   		<html:hidden property="target" value="frontpage"/>   
							<table class="loginbox" align="center" cellpadding="5px">
								<tr>	
									<td class="boxheader" align="left" colspan="2">
										<b>Login</b>
									</td>
								</tr>
								<tr>
									<td class="text_center_small" align="center">
										Username 
									</td>
									<td align="center">
										<html:text property="username" styleClass="textInput" size="15"/>
									</td>
								</tr>
								<tr>
									<td class="text_center_small" align="center">
										Password 
									</td>
									<td align="center">
										<html:password property="password" styleClass="textInput" size="15"/>
									</td>
								</tr>
								<tr>
									<td align="center" colspan="2">
    					                  <button id="loginButton"><bean:message key="button.login"/></button>
									</td>				
								</tr>

							</table>	
					  </html:form>					
