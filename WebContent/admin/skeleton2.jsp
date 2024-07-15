<%@include file="header.jsp" %>	
<%@include file="dojoincludes.jsp" %>

   <body class="tundra">

   <html:form action="/admin/DoSomething">

		<table class="Title" align="center">
			<tr>
				<td width="5%">
					<img src="./themes/<%= session.getAttribute("template") %>/icons/<%= session.getAttribute("processicon") %>" border="0" align="left">			
				</td>
				<td class="desc_left">
					&nbsp;
				</td>
			</tr>
		</table>	
		<table class="standardT" align="center">	
			<tr>
				<td colspan="4">
					<%@include file="menubar.jsp" %>			
				</td>
			</tr>		
			<tr>
				<td colspan="4">
					<buttonbar:buttonbar 
						name="SomeForm" 
						type="dojo" 
						align="left" 
						width="100%"/>
				</td>
			</tr>
			<logic:messagesPresent>
				<tr>
					<td colspan="4">
					   <html:errors/>			
					</td>		
				</tr>
			</logic:messagesPresent>					
			<tr>
				<td colspan="4">
					<table border="0" cellspacing="5px" cellpadding="0" align="center" width="100%">
						<tr>
							<td class="text_left">
								<bean:message key="some.message"/>
							</td>
							<td>
								<html:text property="someProperty" size="30" styleClass="bs1"/>
							</td>
						</tr>	
					</table>
				</td>
			</tr>
		</table>
		<%@include file="pagefooter.jsp" %>	
	   </html:form>
   </body>
</html>
 