<%@include file="pageheader.jsp" %>	

	<body>

	<div id="logo"></div>
	<br><br>
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
						<a class="button" href="#" onclick="this.blur();document.location.href='Mainpage.do?do=showForums';return false;"><span>Foren&uuml;bersicht</span></a>
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

	<table class="Title" align="left"> 
		<tr>
			<td align=left  valign="middle">
				<h2 style="color:#000">Mitgliederliste</h2>
			</td>
		</tr>
	</table>

	<table class="standardT" align="center">	
		<tr>
			<td class="desc_left" align="left"  valign="middle" colspan="7">
				  <display:table id="row" name="ForumMainpageForm.users" requestURI="" export="false" sort="list" class="standardInner" defaultorder="descending" defaultsort="3">				  
				  <display:column style="width:15%" property="name"/>
				  <display:column style="width:15%"  property="joinDate"/>
				  <display:setProperty name="basic.msg.empty_list" value="Keine Eintr&auml;ge vorhanden."/>			  
				</display:table>
			</td>
		</tr>
	</table>	


	<%@include file="pagefooter.jsp" %>
	
	</body>

</html>
		