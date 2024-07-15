	   <html:form action="/admin/Logout">
	   <html:hidden property="do" value="logout"/>   
	   <html:hidden property="target" value="Mainpage"/>   
     		
		<input type="hidden" name="username" value="<%= (String)request.getSession().getAttribute("login") %>">
	      		
		<table class="loginbox" align="center" cellpadding="15px">
		<logic:present name="permission" scope="session">
			<tr>
				<td class="text_center_small" align="center">
					<b>Eingeloggt als:</b>
				</td>
			</tr>						
			<tr>
				<td align="center" class="text_center_small">
					<%= (String)request.getSession().getAttribute("login") %>
				</td>
			</tr>						
		</logic:present>
		<logic:notPresent name="permission" scope="session">
			<tr>
				<td class="text_center_small" align="center">
					<br>Sie sind nicht angemeldet.<br><br>
				</td>
			</tr>						
		</logic:notPresent>	
			<tr>
				<td align="center">
                    <button id="logoutButton"><bean:message key="button.logout"/></button>
				</td>				
			</tr>
		</table>
		
		</html:form>
