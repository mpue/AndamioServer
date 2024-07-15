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
		
		<link rel="stylesheet" href="http://<page:config property="hostname"/>/<page:config property="localPath"/>templates/<page:config property="template"/>/css/template.css" type="text/css">		
		<link rel="stylesheet" href="http://<page:config property="hostname"/>/<page:config property="localPath"/>templates/<page:config property="template"/>/css/tabcontent.css" type="text/css">
		<link rel='stylesheet' type='text/css' href="/<page:config property="localPath"/>jscripts/window/window.css" />
		<link rel="shortcut icon" type="image/x-icon" href="/<page:config property="localPath"/>templates/homepage/images/fav.ico"/>
		<script type="text/javascript" src="/<page:config property="localPath"/>scripts/tabcontent.js"></script>
		<script type="text/javascript" src="/<page:config property="localPath"/>scripts/prototype.js"></script>
		<script type="text/javascript" src="/<page:config property="localPath"/>scripts/scriptaculous.js"></script>		
		<script type="text/javascript" src="/<page:config property="localPath"/>scripts/window/window.js"></script>		
		<script type="text/javascript" src="/<page:config property="localPath"/>templates/<page:config property="template"/>/jscripts/main.js"></script>
		
	</head>
		<body onload="adjustContent();initDrag();">
		
		<div id="outer">
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
									<page:language languages="DE,EN,FR"></page:language>
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
					&nbsp;
				</td>
				<td class="top_menu_right" valign="middle">					
					<div id="pathway">
					<h1>&nbsp;<page:pathway exclude="menu1" separator="/"/></h1>
					<div class="1pxheight"></div>
					<div class="hr"></div>										
					</div>										
				</td>
			</tr>
			<tr>
				<td class="menu">
								
					<!-- BEGIN MENU-->
						
						<page:xmenu 					
							forceLevelIndent="true"
							orientation="vertical"							
							type="studiesportal"
							startNode="menu1"
							exclude="studienportal/indikationen"
							selected="<%= request.getParameter(\"nodeName\")%>"
							mode="website">
						</page:xmenu>						

					<!-- END MENU-->
					<div class="address">
						<page:staticContent path="menu2/Anschrift"/>
					</div>	
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
					</td></tr></table>
					</div>
				</td>
			</tr>
		</table>
		</div>
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
