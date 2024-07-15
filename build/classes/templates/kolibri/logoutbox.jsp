
<html:form action="/admin/Logout">
	<html:hidden property="do" value="logout" />
	<page:target />
	<page:hiddenInput scope="session" property="login" />
	<table class="logoutbox" align="center">
		<logic:present name="permission" scope="session">
			<tr>
				<td class="loginText" align="center" valign="middle"><b>Angemeldet
				als :</b></td>
				<td align="center" class="loginText" valign="middle"><b> <page:value scope="session" property="fullUserName"/>
				</b></td>
				<td align="center" valign="middle">
				<div class="clear"><a class="button" href="#"
					onclick="this.blur();document.forms['LogoutForm'].submit();return false;"><span><bean:message
					key="button.logout" /></span></a></div>
				</td>
			</tr>
	</table>
	</logic:present>
</html:form>

