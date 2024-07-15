<%@include file="pageheader.jsp" %>	

	<script type="text/javascript">
	
		function submitData() {
			$('addAttachment').value = 'false';
			document.PostingForm.submit()
		}
	
		function submitAttachment() {
			$('addAttachment').value = 'true';		
			document.PostingForm.submit()
		}
		
	
	</script>

	<body>
	<div id="logo"></div>
	<html:form action="/forum/CreatePosting" enctype="multipart/form-data">
	<input type="hidden" name="category_id" value="<%= request.getParameter("category_id") %>"/>
	<input type="hidden" name="thread_id" value="<%= request.getParameter("thread_id") %>"/>
	<input type="hidden" name="addAttachment" id="addAttachment" value="false"/>      
	
	<table class="Title" align="left">
		<tr>	
			<td class="desc_left">
				<h2 style="color:#000">Neuen Beitrag erstellen</h2>
			</td>
		</tr>
	</table>
	<br><br>
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
							<html:textarea property="postingtext" rows="20" cols="135" styleClass="posting"></html:textarea>
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
	<table class="Title" align="left">
		<tr>	
			<td class="desc_left">
				<h2 style="color:#000">Attachments</h2>
			</td>
		</tr>
	</table>
	
	<table class="standardT" align="center">
		<tr>
			<td align="left" width="30%">
				<input type="file" size="30" name="uploadFile"/>
			</td>
			<td align="left">
				<div class="clear">
					<a class="button" href="#" onclick="this.blur();submitAttachment();return false;"><span>Attachment hinzuf&uuml;gen</span></a>
				</div>			
			</td>
		</tr>
		<logic:iterate name="PostingForm" id="attachment" property="attachments">
		<tr>
			<td colspan="2">
				<bean:write name="attachment" property="name" /> 
			</td>
		</tr>			
		</logic:iterate>
	</table>				
	<p>&nbsp;</p>
	<table border="0" cellpadding="0" cellspacing="0" align="center">
		<tr>
			<td align="center">
				<div class="clear">
					<a class="button" href="#" onclick="this.blur();submitData();return false;"><span><bean:message key="button.save"/></span></a>
				</div>							
			</td>				
		</tr>
	</table>

	<%@include file="pagefooter.jsp" %>
	</html:form>
	</body>
</html>