<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/pagetags" prefix="page" %>

<% String login = (String)request.getSession().getAttribute("login"); %>

<html:html xhtml="true" lang="de">

	<head>
		<title>iOMEDICO - kolibri</title>
		
		<link rel="stylesheet" href="/<page:config property="localPath"/>templates/kolibri/css/template.css" type="text/css">		
		<link rel="stylesheet" href="/<page:config property="localPath"/>templates/kolibri/css/tabcontent.css" type="text/css">
		<link rel='stylesheet' type='text/css' href="/<page:config property="localPath"/>jscripts/window/window.css" />
		<link rel="shortcut icon" type="image/x-icon" href="/<page:config property="localPath"/>templates/kolibri/images/fav.ico"/>
		<script type="text/javascript" src="/<page:config property="localPath"/>scripts/tabcontent.js"></script>
		<script type="text/javascript" src="/<page:config property="localPath"/>scripts/prototype.js"></script>
		<script type="text/javascript" src="/<page:config property="localPath"/>scripts/scriptaculous.js"></script>		
		<script type="text/javascript" src="/<page:config property="localPath"/>scripts/window/window.js"></script>
		<script type="text/javascript" src="/<page:config property="localPath"/>scripts/autocomplete.js"></script>
		<script type="text/javascript" src="/<page:config property="localPath"/>scripts/patients/patients.js"></script>		

		<script type="text/javascript">
		
			function setHeight(id,height) {
				var element = document.getElementById(id);
				element.style.height = height;
			}

			/**
			 *	Adjust the height of the content on body load complete
			 */
			
			function adjustContent() {				
				var height = window.innerHeight - 228;
				setHeight('content',height);
				setHeight('page',window.innerHeight);
			}

			/**
			 * Allows to submit a form field by pressing enter,
			 * this is needed for the login password field.			
			 */
			function submitEnter(myfield,e)	{

				var keycode;
				if (window.event) keycode = window.event.keyCode;
				else if (e) keycode = e.which;
				else return true;

				if (keycode == 13) {
				   myfield.form.submit();
				   return false;
				}
				else
				   return true;
			}

			function hideDiv(id) {
				Effect.Fade(id, { duration: 0.5 });
			}

	                   
        </script>
			
		</script>
		
	</head>
		
	<body onload="adjustContent();initAutoComplete();">
		
		<div id="homepage"><a class="homepage" href="http://studienportal:8080/andamio" target="_self">Zum Studienportal</a></div>
		<div id="autocomplete_choices" class="autocomplete"></div>
				
		<%@include file="patients.jsp" %>				
				
		<table id="page" class="page" border="0">
			<tr height="150px">
				<td colspan="2" class="header">
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
						<li><a href="#" rel="mainMenu" class="selected">Mainmenu</a></li>
						<logic:present name="permission" scope="session">
						 
						<li><a href="#" rel="projects">Projects</a></li>							
						<!--
						<li><a href="#" rel="otherStudies">Projekte</a></li>
						 -->
						</logic:present>
					</ul>
				</td>
				<td class="top_menu_right" valign="middle">					
					<div id="pathway">
					<h1>&nbsp;<page:pathway exclude="kolibri" separator="->"/></h1>
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
							startNode="Mainmenu"											
							selected="<%= request.getParameter(\"nodeName\")%>"
							mode="website">
						</page:xmenu>						
						
					</div>
					
					<logic:present name="permission" scope="session">
					
					<div id="projects" class="tabcontent">

						<page:xmenu 					
							forceLevelIndent="true"
							orientation="vertical"
							type="studiesportal"
							startNode="Projects"							
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
					<!-- <p><i><page:lastModified showUser="true" showDate="true"/></i></p>  -->
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