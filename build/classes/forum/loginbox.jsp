 <html:form action="/admin/LoginDialog" autocomplete="off" >
 		<html:hidden property="do" value="login"/>   
 		<html:hidden property="target" value="forum"/>   
<table class="loginbox">	
	<tr>
		<td width="50%">
		&nbsp;
		</td>
		<td class="boxheader" width="150px">
			<b>Benutzer Login : </b>
		</td>
		<td class="text_center_small" align="right">
			<bean:message key="form.login.username"/>
		</td>
		<td align="right">
			<html:text property="username" styleClass="bs1" size="20"/>
		</td>
		<td class="text_center_small" align="right">
			<bean:message key="form.login.password"/>
		</td>
		<td align="right">
			<html:password property="password" styleClass="bs1" size="20" onkeypress="return submitEnter(this,event)"/>
		</td>
		<td align="right">
			<div class="clear">
				<a class="button" href="#" onclick="this.blur();document.LoginForm.submit();return false;"><span>Anmelden</span></a>
			</div>									
		</td>				
	</tr>

</table>	
</html:form>					
