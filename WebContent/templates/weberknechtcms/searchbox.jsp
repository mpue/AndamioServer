					   <html:form action="/Mainpage">
					   		<html:hidden property="do" value="searchNodes"/>   
					   		<html:hidden property="target" value="frontpage"/>   
							<table class="loginbox" align="center" cellpadding="3px" cellspacing="0px">	
								<tr>
									<td class="boxheader" align="center">
										<b>Suchbegriff</b>
									</td>
								</tr>											
								<tr>								
									<td align="center">
										<html:text property="search" styleClass="bs1" size="15"/>
									</td>
								</tr>
								<tr height="5px">
									<td align="center">
										 <html:submit styleClass="bs2" property=""><bean:message key="button.search"/></html:submit>
									</td>				
								</tr>

							</table>	
					  </html:form>					
