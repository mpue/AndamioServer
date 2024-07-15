	<%@ page import="org.pmedv.cms.common.Constants" %> 
	<table class="Footer" align="center"> 
		<tr>
			<td align="center" valign="middle" style="color:#FFFFFF; font-size: 9pt;">
				Angemeldete Benutzer : <userlist:userlist></userlist:userlist>
			</td>
		<tr>
			<td align="center" valign="middle" style="color:#FFFFFF; font-size: 10pt;">
				| <%= Constants.VERSION %>
				| 2007 <%= Constants.APPNAME %> 
				| <a href="mailto:pueski@gmx.de" style="color:#FFFFFF;">Matthias P&uuml;ski</a> 
				| <a href="mailto:thorsten@schminkel.de" style="color:#FFFFFF;">Thorsten Schminkel</a> 
				| <a href="<%= Constants.APPURL %>" style="color:#FFFFFF;" target="_blank"><%= Constants.APPURL %></a> |
			</td>
		</tr>
	</table>
