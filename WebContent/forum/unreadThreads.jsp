<%@include file="pageheader.jsp" %>	

	<body>

	<div id="logo"></div>
	<table class="Title" align="left"> 
		<tr>
			<td class="desc_left" align="left"  valign="middle">
				<h2 style="color:#000">Ungelesene Beitr&auml;ge</h2>
			</td>
		</tr>
	</table>
	<table class="standardT" align="center">	
		<tr>
			<td class="desc_left" align="left"  valign="middle" colspan="7">
				  <display:table id="row" name="ForumMainpageForm.unreadThreads" requestURI="" export="false" sort="list" class="standardInner" decorator="org.pmedv.decorator.ThreadDecorator" defaultorder="descending" defaultsort="3">
				  <display:column style="width:70%" property="name" href="Mainpage.do?do=showThread" paramProperty="id" paramId="thread_id" titleKey="table.category.name" sortable="true" />
				  <display:column style="width:15%" property="user.name"/>
				  <display:column style="width:15%"  property="date"/>
				  <display:setProperty name="basic.msg.empty_list" value="Keine Eintr&auml;ge vorhanden."/>			  
				</display:table>
			</td>
		</tr>
		<tr>
			<td class="desc_left" align="left"  valign="middle">
				<div class="clear">
					<a class="button" href="#" onclick="this.blur();document.location.href='Mainpage.do?do=showForums';return false;"><span>Foren&uuml;bersicht</span></a>
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
				&nbsp;
			</td>
			<td>
				&nbsp;
			</td>
			<td>
				&nbsp;
			</td>

		</tr>
	</table>	


	<%@include file="pagefooter.jsp" %>
	
	</body>

</html>
		