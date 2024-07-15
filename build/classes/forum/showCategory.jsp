<%@include file="pageheader.jsp" %>	

	<body>
	<div id="logo"></div>
	<table class="Title" align="left"> 
		<tr>
			<td align="left"  valign="middle">
				<h1 style="color:#000">Threadübersicht - <bean:write name="ForumMainpageForm" property="category.name" /></h1>
			</td>
		</tr>
	</table>
	<br/>
	

	 <%
	 	boolean admin = false;
	 
	 	String login = (String)request.getSession().getAttribute("login");
	 	
	 	if (login != null) {

	 		User user = (User)DAOManager.getInstance().getUserDAO().findByName(login);
		 	
			Usergroup group = new Usergroup();
			group.setName("admin");
		
			if (user.getGroups().contains(group)) {
				admin = true;
			}
	 	
	 	}
	 	
	 	
	 %>

	<table class="standardT" align="center">	
		<tr>
			<td class="desc_left" align="left"  valign="middle" colspan="8">
				  <display:table id="row" name="ForumMainpageForm.category.threads" requestURI="" export="false" sort="list" class="standardInner" decorator="org.pmedv.decorator.ThreadDecorator" defaultorder="descending" defaultsort="3">
				  <display:column style="width:70%" property="name" href="Mainpage.do?do=showThread" paramProperty="id" paramId="thread_id" titleKey="table.category.name" sortable="true" />
				  <display:column style="width:15%" property="user.name"/>
				  <display:column style="width:15%"  property="date"/>
				  <% if (admin) { %>				  
				  <display:column property="deleteLink" title=""/>
				  <% } %>
				  <display:setProperty name="basic.msg.empty_list" value="Keine Eintr&auml;ge vorhanden."/>
				</display:table>
			</td>
		</tr>
		<tr>
			<td class="desc_left" align="left"  valign="middle">
				<logic:present name="permission" scope="session">
					<div class="clear">
						<a class="button" href="#" onclick="this.blur();document.location.href='CreateThread.do?category_id=<%=request.getParameter("category_id") %>';return false;"><span>Neuer Thread</span></a>
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
		