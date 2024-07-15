<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/pagetags" prefix="page" %>
<%@ page import="org.pmedv.web.ServerUtil"%>

<% String login = (String)request.getSession().getAttribute("login"); %>

<html:html xhtml="true" lang="de">

	<head>
		<title>iOMEDICO - Studienportal</title>
		
		<link rel="stylesheet" href="/<page:config property="localPath"/>templates/studienportal/css/template.css" type="text/css">		
		<link rel="stylesheet" href="/<page:config property="localPath"/>templates/studienportal/css/tabcontent.css" type="text/css">
		<link rel='stylesheet' type='text/css' href="/<page:config property="localPath"/>jscripts/window/window.css" />
		<link rel="shortcut icon" type="image/x-icon" href="/<page:config property="localPath"/>templates/studienportal/images/fav.ico"/>
		<script type="text/javascript" src="/<page:config property="localPath"/>scripts/tabcontent.js"></script>
		<script type="text/javascript" src="/<page:config property="localPath"/>scripts/prototype.js"></script>
		<script type="text/javascript" src="/<page:config property="localPath"/>scripts/scriptaculous.js"></script>		
		<script type="text/javascript" src="/<page:config property="localPath"/>scripts/window/window.js"></script>		
		<script type="text/javascript" src="/<page:config property="localPath"/>templates/studienportal/jscripts/main.js"></script>
		
	</head>
		<body onload="adjustContent();initDrag();">
		
		<div id="homepage"><a class="homepage" href="http://kolibri:8080/andamio" target="_self">Zum Kolibri Portal</a></div>
		
		<div id="messageDiv" style="display:none">
			<table border="0" align="center" cellspacing="0" cellpadding="0px" width="100%" height="257px">
				<tr height="25px">
					<td align="left" colspan="2" valign="top">
						<div style="color:#000;padding-left : 120px;padding-top:4px;font-size : 10pt;">Dateien herunterladen</div>
					</td>
				</tr>
				<tr>
					<td align="center">
						<p class="message"><b>Klicken Sie auf einen Eintrag<br> um die Datei herunterzuladen.</b></p>
					</td>
				</tr>
				<tr height="140px">
					<td align="center" valign="top">
						<div id="filelist">
						</div>
					</td>
				</tr>
				<tr>
					<td align="center">
						<input type="button" value="Schliessen" onclick="hideDiv('messageDiv');">
					</td>
				</tr>			
			</table>
		</div> 		
		
		<table id="page" class="page" border="0">
			<tr height="150px">
				<td colspan="2" id="header" class="header">
					<div align="right">
						<table border="0">
							<tr valign="middle" height="30px">
								<td align="right" valign="middle">
								<logic:present name="permission" scope="session">
									<div id="logoutbox">
										<%@include file="logoutbox.jsp" %>
									</div>
								</logic:present>																			                                               
								<logic:notPresent name="permission" scope="session">
									<div id="loginbox">
										<%@include file="loginbox.jsp" %>
									</div>
								</logic:notPresent>  				
								</td>
							</tr>
							<tr valign="middle">
								<td align="right" valign="middle">
									<div id="searchbox">
										<%@include file="searchbox.jsp" %>
									</div>
								</td>
							</tr>
						</table>
					</div>
				</td>				
			</tr>
			<tr height="39px" valign="middle">
				<td class="top_menu_left">
					<ul id="studies" class="shadetabs">
						<li><a href="#" rel="mainMenu" class="selected">Allgemeines</a></li>
						<logic:present name="permission" scope="session">
						<li><a href="#" rel="myStudies">Indikationen</a></li>							
						<!-- <li><a href="#" rel="otherStudies">Weitere Studien</a></li>  -->
						</logic:present>
					</ul>
				</td>
				<td class="top_menu_right" valign="middle">					
					<div id="pathway">
					<h1>&nbsp;<page:pathway exclude="studienportal" separator="/"/></h1>
					<div class="1pxheight"></div>
					<div class="hr"></div>										
					</div>										
				</td>
			</tr>
			<tr>
				<td class="menu">
								
					<!-- BEGIN MENU-->
								
										
					<div id="mainMenu" class="tabcontent">
						
						<page:xmenu 					
							forceLevelIndent="true"
							orientation="vertical"							
							type="studiesportal"
							startNode="studienportal"
							exclude="studienportal/indikationen"
							selected="<%= request.getParameter(\"nodeName\")%>"
							mode="website">
						</page:xmenu>						
						
					</div>
					
					<logic:present name="permission" scope="session">
					
					<div id="myStudies" class="tabcontent">

						<page:xmenu 					
							forceLevelIndent="true"
							orientation="vertical"
							type="studiesportal"
							startNode="studienportal/indikationen"
							selected="<%= request.getParameter(\"nodeName\")%>"
							mode="website">
						</page:xmenu>
						
					</div>
					
					
					<div id="otherStudies" class="tabcontent">

						<page:xmenu 					
							forceLevelIndent="true"
							orientation="vertical"
							type="studiesportal"
							startNode="Weitere Studien"
							exclude=""
							selected="<%= request.getParameter(\"nodeName\")%>"
							mode="website">
						</page:xmenu>
						
					</div>
					
					</logic:present>
					
					<script type="text/javascript">
					
						var tabs=new ddtabcontent("studies");
						tabs.setpersist(true);
						tabs.setselectedClassTarget("link"); //"link" or "linkparent"
						tabs.init();
					
					</script>

					<!-- END MENU-->
					
				</td>
				<td class="content">
					<div id="content">
					<bean:write name="MainpageForm" property="content.contentstring" filter="false"/>
					<logic:notEmpty name="MainpageForm" property="searchResults"> 
					<p>
						<logic:iterate id="foundNode" name="MainpageForm" property="searchResults">
							<a href="<%= foundNode %>.html&highlight=<%= request.getParameter("search") %>" target="_self"><%= foundNode %></a><br/>
						</logic:iterate>
					<p>
					</logic:notEmpty>
					<table border="0" align="center" width="100%"><tr><td align="left">
					<p><i><page:lastModified showUser="true" showDate="true"/></i></p>
					</td></tr></table>
					</div>
				</td>
			</tr>
		</table>
		
		<%
			if (request.getSession().getAttribute("login") != null) {
				String message = (String)request.getSession().getAttribute("login");
				if (message.equalsIgnoreCase("failed")) {							
				%>
					<script type="text/javascript">
						alert("Login failed.");
					</script>
				<%
				request.getSession().setAttribute("login", "");
				}
			}
		%>		
	</body>
	
</html:html>