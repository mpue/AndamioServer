<%@include file="pageheader.jsp" %>	

	<body>
	<div id="logo"></div>
	<table class="Title" align="left"> 
		<tr>
			<td class="desc_left" align="left"  valign="middle">
				<h2 style="color:#000">Thread&uuml;bersicht : <bean:write name="ForumMainpageForm" property="thread.name"/></h2>
			</td>
		</tr>
	</table>
	<table class="standardT" align="center">	
		<tr>
			<td class="desc_left" align="left"  valign="middle" colspan="7">
				  <display:table id="row" name="ForumMainpageForm.thread.postings" requestURI="" export="false" sort="list"  class="standardInner" decorator="org.pmedv.decorator.PostingDecorator" pagesize="10">
				  <display:column style="width:10%" property="userInfo" title="" sortable="false" class="userInfo" />
				  <display:column style="width:50%" property="posting" sortable="false" title="" class="posting"/>				  
				  <display:setProperty name="basic.msg.empty_list" value="Keine Eintr&auml;ge vorhanden."/>			
				  <display:setProperty name="paging.banner.all_items_found" value=""/>				
				  <display:setProperty name="paging.banner.one_item_found" value=""/>
				  <display:setProperty name="paging.banner.onepage" value=""/>    
				  <display:setProperty name="paging.banner.placement">bottom</display:setProperty>
				</display:table>
			</td>
		</tr>
		<tr>
			<td class="desc_left" align="left"  valign="middle">
				<logic:present name="permission" scope="session">
					<div class="clear">
						<a class="button" href="#" onclick="this.blur();document.location.href='CreatePosting.do?thread_id=<%=request.getParameter("thread_id") %>';return false;"><span>Antworten</span></a>
					</div>					
				</logic:present>
				<logic:notPresent name="permission" scope="session">
					&nbsp;
				</logic:notPresent>						
			</td>
			<td class="desc_left" align="left"  valign="middle">
				<div class="clear">
					<a class="button" href="#" onclick="this.blur();document.location.href='Mainpage.do?do=showForums';return false;"><span>Foren&uuml;bersicht</span></a>
				</div>			
			</td>
			<td class="desc_left" align="left"  valign="middle">
				<div class="clear">
					<a class="button" href="#" onclick="this.blur();document.location.href='Mainpage.do?do=showCategory&category_id=<bean:write name="ForumMainpageForm" property="thread.category.id"/>';return false;"><span>Kategorie&uuml;bersicht</span></a>
				</div>									
			</td>
			<td>
				&nbsp;
			</td>
			<td>
				&nbsp;
			</td>
			<td>
				&nbsp;
			</td>
			<td>
				<a name="bottom"></a>
			</td>

		</tr>		
	</table>	

	
	
	<%@include file="pagefooter.jsp" %>
	
	</body>

</html>
		