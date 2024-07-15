<%@include file="pageheader.jsp" %>	
	<body>
	<html:form action="/forum/Mainpage">
	<html:hidden property="id"/>
	<html:hidden property="do" value="savePosting"/>
	<table class="Title" align="center">
		<tr>	
			<td class="desc_left">
				Posting bearbeiten
			</td>
		</tr>
	</table>
	
	<table class="standardT" align="center">	
		<logic:messagesPresent>
		<tr>
			<td colspan="4">
			   <html:errors/>			
			</td>
		</tr>
		</logic:messagesPresent>		
		<tr>
			<td colspan="4" align="center">
				<table class="innerform" align="center">
					<tr>
						<td class="text_left">
							Titel
						</td>
						<td class="text_left">
							<html:text styleClass="bs1" size="50" property="title"/>
						</td>
					</tr>

					<tr>
						<td class="text_left" colspan="2">
							Text
						</td>
					</tr>
					<tr>
						<td class="text_left" colspan="2">
							<html:textarea property="postingtext" rows="20" cols="135" styleClass="posting">
							</html:textarea>
						</td>
					</tr>


					<tr>
						<td class="text_left" colspan="2">
							<input type="hidden" name="thread_id" value="<%= request.getParameter("thread_id") %>">
						</td>
					</tr>


				</table>
			</td>
		</tr>
	</table>								
		<table class="Title" align="center">
			<tr>
				<td align="center">
					<html:submit property=""><bean:message key="button.save"/></html:submit>
				</td>				
			</tr>
		</table>
	<%@include file="pagefooter.jsp" %>
	</html:form>
	</body>
</html>