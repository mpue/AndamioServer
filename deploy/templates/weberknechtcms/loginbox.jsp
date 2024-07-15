
<html:form action="/admin/LoginDialog" autocomplete="off" >
	<html:hidden property="do" value="login" />
	<page:target />
	<table class="loginbox" align="center" cellpadding="3px"
		cellspacing="0px">
		<tr>
			<td class="boxheader" align="center"><b>Login</b></td>
		</tr>
		<tr>
			<td class="text_center_small" align="center">Username</td>
		</tr>
		<tr>
			<td align="center"><html:text property="username"
				styleClass="bs1" size="15" /></td>
		</tr>

		<tr>
			<td class="text_center_small" align="center">Password</td>
		</tr>

		<tr>
			<td align="center"><html:password property="password"
				styleClass="bs1" size="15" /></td>
		</tr>
		<tr height="5px">
			<td align="center"><html:submit styleClass="bs2" property="">
				<bean:message key="button.login" />
			</html:submit></td>
		</tr>

	</table>
</html:form>
