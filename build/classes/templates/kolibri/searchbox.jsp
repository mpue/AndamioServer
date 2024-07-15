  <html:form action="/Mainpage">
  		<html:hidden property="do" value="searchNodes"/>   
  		<html:hidden property="target" value="frontpage"/>   
	<table class="searchbox" cellpadding="8px" cellspacing="0px">	
		<tr>
			<td width="100px">
				<html:text property="search" styleClass="inputBox" styleId="search" size="20" />
			</td>
			<td width="100px" align="left">
				<div class="clear">
					<a class="button" href="#" onclick="this.blur();document.forms['MainpageForm'].submit();return false;"><span>Suchen</span></a>
				</div>
			</td>				
		</tr>

	</table>	
 </html:form>					
