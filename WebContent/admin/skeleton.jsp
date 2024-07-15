<%@include file="header.jsp" %>	

	<body>

	<table class="Title" align="center"> 
		<tr>
			<td width="5%">
				<img src="./themes/<%= session.getAttribute("template") %>/icons/<%= session.getAttribute("processicon") %>" border="0" align="left">			
			</td>
			<td class="desc_left" align=center  valign="middle">
				Title
			</td>
		</tr>
	</table>

	<table class="standardT" border="0" align="center" width="70%"> 
		<tr>
			<td align="left" width="10%">
				<html:link href="index.jsp">
					<html:button styleClass="bs2" property=""><bean:message key="button.mainmenu"/></html:button>
				</html:link>
			</td>
			<td align="left" width="10%">
				<html:link action="">
					<html:button styleClass="bs2" property=""><bean:message key="button.new"/></html:button>
				</html:link>
			</td>
			<td align="left" width="10%">
				&nbsp;
			</td>			
			<td align="left" width="70%">
				&nbsp;
			</td>
			
		</tr>

		<tr>
			<td colspan="4">
				<%@include file="menubar.jsp" %>			
			</td>
		</tr>

		<tr>
			<td colspan="4">
			</td>
		</tr>
	</table>
	<%@include file="pagefooter.jsp" %>
	</body>

</html>
		