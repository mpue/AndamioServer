	   <html:form action="/admin/Logout">
	   <html:hidden property="do" value="logout"/>   
	   <page:target />   
     		
		<input type="hidden" name="username" value="<%= (String)request.getSession().getAttribute("login") %>">
	      		
		<table class="loginbox" align="center">
		<logic:present name="permission" scope="session">
			<tr>
				<td class="text_center_small" align="center">
					<b>Logged in as:</b>
				</td>
			</tr>						
			<tr>
				<td align="center" class="text_center_small">
					<%= (String)request.getSession().getAttribute("login") %>
				</td>
			</tr>		
			<tr>
				<td align="center" class="text_center_small">
					<p><a href="users/UserAction.do?do=showProfile&user_id=<%=request.getSession().getAttribute("userId") %>">Your profile</a></p>
				</td>
			</tr>						
							
		</logic:present>
		<logic:notPresent name="permission" scope="session">
			<tr>
				<td class="text_center_small" align="center">
					<br>You are not logged in.<br><br>
				</td>
			</tr>						
		</logic:notPresent>	
			<tr>
				<td align="center">
					<html:submit styleClass="bs2" property=""><bean:message key="button.logout"/></html:submit>
				</td>				
			</tr>
		</table>
		
		</html:form>
		