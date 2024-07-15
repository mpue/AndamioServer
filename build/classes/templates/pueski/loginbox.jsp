					   <html:form action="/admin/LoginDialog" autocomplete="off" >
					   		<html:hidden property="do" value="login"/>   
					   		<html:hidden property="target" value="frontpage"/>   
							<table class="loginbox" align="center" cellpadding="10px">
								<tr>	
									<td class="boxheader" align="center" colspan="2">
										<b>Login</b>
									</td>
								</tr>
								<tr>
									<td class="text_center_small" align="center">
										Username 
									</td>
									<td align="center">
										<html:text property="username" styleClass="input" size="15"/>
									</td>
								</tr>
								<tr>
									<td class="text_center_small" align="center">
										Password 
									</td>
									<td align="center">
										<html:password property="password" styleClass="password" size="15"/>
									</td>
								</tr>
								<tr>
									<td align="center" colspan="2">
										 <html:submit styleClass="button-top" property="">Login</html:submit>
									</td>				
								</tr>

							</table>	
					  </html:form>					
