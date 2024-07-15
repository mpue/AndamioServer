
<html:form action="/admin/LoginDialog" autocomplete="off" >
	<html:hidden property="do" value="login" />
	<page:target />
	<table class="loginbox" align="center" cellpadding="3px"
		cellspacing="0px">
		<tr>
			<td class="loginText" align="center">Benutzer</td>
			<td align="center"><html:text property="username"
				styleClass="inputBox" size="10" /></td>
			<td class="loginText" align="center">Passwort</td>
			<td align="center"><html:password property="password"
				styleClass="inputBox" size="10"
				onkeypress="return submitEnter(this,event)" /></td>
			<td align="center">
			<div class="clear"><a class="button" href="#"
				onclick="this.blur();document.forms['LoginForm'].submit();return false;"><span><bean:message
				key="button.login" /></span></a></div>
			</td>
		</tr>
	</table>
</html:form>
