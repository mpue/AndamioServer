<%@include file="pageheader.jsp" %>	

	<body>
	<div id="logo">		
		<logic:present name="permission" scope="session">
		<%@include file="logoutbox.jsp" %>															
		</logic:present>
		<logic:notPresent name="permission" scope="session">
		<%@include file="loginbox.jsp" %>								
		</logic:notPresent>			
	</div>

	<table class="Title" align="left"> 
		<tr>
			<td align=left  valign="middle" width="40%">
				<h2 style="color:#000">Foren&uuml;bersicht</h2>
			</td>
			<td align="right" width="60%">
			</td>
		</tr>
	</table>		
	<table border="0" width="500px"><tr><td></td></tr></table>
	<logic:present name="permission" scope="session">
		<table class="standardT" align="center">	
			<tr>
				<td class="desc_left" align="left"  valign="middle">
					<div class="clear">
						<a class="button" href="#" onclick="this.blur();document.location.href='Mainpage.do?do=showUnreadThreads';return false;"><span>Ungelesene Beitr&auml;ge</span></a>
					</div>				
				</td>
				<td class="desc_left" align="left"  valign="middle">
					<div class="clear">
						<a class="button" href="#" onclick="this.blur();document.location.href='Mainpage.do?do=markAllForumsRead';return false;"><span>Alle Beitr&auml;ge als gelesen markieren</span></a>
					</div>							
				</td>
				<td>
					<div class="clear">
						<a class="button" href="#" onclick="this.blur();document.location.href='../users/MailboxAction.do?do=showFolder';return false;"><span>Private Nachrichten</span></a>
					</div>							
				</td>
				<td class="desc_left" align="left"  valign="middle">
					<div class="clear">
						<a class="button" href="#" onclick="this.blur();document.location.href='Mainpage.do?do=showMemberList';return false;"><span>Mitgliederliste</span></a>
					</div>							
				</td>
				<td class="desc_left" align="left"  valign="middle">
					<div class="clear">
						<a class="button" href="#" onclick="this.blur();document.location.href='../users/UserAction.do?do=showProfile&user_id=<page:value scope="session" property="userId"/>';return false;"><span>Profil</span></a>
					</div>																
				</td>
			</tr>
		</table>
	</logic:present>
	<logic:iterate name="ForumMainpageForm" id="forum" property="forums">
	<table class="Title" align="left"> 
		<tr>
			<td align="left"  valign="middle">
				<h2 style="color:#000"><bean:write name="forum" property="name" /></h2>
			</td>
		</tr>
	</table>
	<table class="standardT" align="center">	
		<tr>
			<td class="desc_left" align="left"  valign="middle">
				  <display:table id="row" name="pageScope.forum.categories" requestURI="" export="false" sort="list" class="standardInner" decorator="org.pmedv.decorator.CategoryDecorator">
				  <display:column style="width:30%" property="name" href="Mainpage.do?do=showCategory" paramId="category_id" paramProperty="id" titleKey="table.category.name" sortable="true" />
				  <display:column style="width:40%" property="description" titleKey="table.category.description" sortable="true" />
				  <display:column style="width:10%" property="numthreads" title="Threads" sortable="true" />				  
				  <display:column style="width:10%" property="numpostings" title="Beitr&auml;ge" sortable="true" />				  
				  <display:setProperty name="basic.msg.empty_list" value="Keine Eintr&auml;ge vorhanden."/>			  
				</display:table>
			</td>
		</tr>
	</table>	
	</logic:iterate>

	<p>
		Letzte Anmeldung : <page:value scope="session" property="lastActivity"/>
	</p>
			
	<%@include file="pagefooter.jsp" %>
	
	</body>

</html>
		