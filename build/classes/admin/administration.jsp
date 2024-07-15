<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<head>
	<%@include file="globals.jsp" %>
	<%@include file="i18nCommon.jsp" %>
    	
	<title id="pagetitle"><%= title %></title>
	
	<link rel="stylesheet" href="./themes/<%= session.getAttribute("template") %>/style.css" type="text/css">
	
	<!-- Ext JS -->
	<link rel="stylesheet" type="text/css" href="../jscripts/extjs/resources/css/ext-all.css" />
 	<script type="text/javascript" src="../jscripts/extjs/adapter/ext/ext-base.js"></script>
    <script type="text/javascript" src="../jscripts/extjs/ext-all.js"></script>
	<link rel="stylesheet" type="text/css" href="../jscripts/extjs/resources/css/xtheme-gray.css" />
	
	<!-- Viewport Initialisierung -->
    <script type="text/javascript" src="administration_init.js"></script>
	

    <script type="text/javascript">
		
		/* Initialisierung des Viewports */
        Ext.onReady(Weberknecht.administration.init, Weberknecht.administration); 
				
    </script>
        
	<style type="text/css">
		
		html, body 
		{
	        font:normal 12px verdana;
	        margin:5px;
	        padding:5px;
	        border:0 none;
	        overflow:hidden;
	        height:100%;
	        background-color:#f0f0f0;
	    }
		
		p {
		    margin:5px;
		}
		
		/*  Icon zur Darstellung in der Titelleiste des obersten Panels (ganz oben links). */
		.contents-icon {
			background:transparent url(themes/experience/icons/small/contents.png);
			height: 16px;
			width: 16px;
		}
		.node-icon {
			background:transparent url(themes/experience/icons/small/node.png);
			height: 16px;
			width: 16px;
		}
		.contacts-icon {
			background:transparent url(themes/experience/icons/small/contacts.png);
			height: 16px;
			width: 16px;
		}
		.galleries-icon {
			background:transparent url(themes/experience/icons/small/galeries.png);
			height: 16px;
			width: 16px;
		}
		.filemanager-icon {
			background:transparent url(themes/experience/icons/small/disks.png);
			height: 16px;
			width: 16px;
		}
		.chart-icon {
			background:transparent url(themes/experience/icons/small/chart.png);
			height: 16px;
			width: 16px;
		}
		.users-icon {
			background:transparent url(themes/experience/icons/small/users.png);
			height: 16px;
			width: 16px;
		}
		.users-icon {
			background:transparent url(themes/experience/icons/small/users.png);
			height: 16px;
			width: 16px;
		}
		.usergroups-icon {
			background:transparent url(themes/experience/icons/small/usergroups.png);
			height: 16px;
			width: 16px;
		}
		.config-icon {
			background:transparent url(themes/experience/icons/small/config.png);
			height: 16px;
			width: 16px;
		}
		.forums-icon {
			background:transparent url(themes/experience/icons/small/document_cup.png);
			height: 16px;
			width: 16px;
		}
		.plug-icon {
			background:transparent url(themes/experience/icons/small/plug.png);
			height: 16px;
			width: 16px;
		}
		.download-icon {
			background:transparent url(themes/experience/icons/small/download.png);
			height: 16px;
			width: 16px;
		}
		.languages-icon {
			background:transparent url(themes/experience/icons/small/languages.png);
			height: 16px;
			width: 16px;
		}
		.processes-icon {
			background:transparent url(themes/experience/icons/small/processes.png);
			height: 16px;
			width: 16px;
		}
		.shop-icon {
			background:transparent url(themes/experience/icons/small/shop.png);
			height: 16px;
			width: 16px;
		}
		.node_add-icon {
			background:transparent url(themes/experience/icons/small/node_add.png);
			height: 16px;
			width: 16px;
		}
		
    </style>


</head>
<body>			  

	<jsp:useBean id="AdminPanelForm" type="org.pmedv.forms.AdminPanelForm" scope="request"/>
	<%
		if (AdminPanelForm.getProcessCount()%2 > 0) {
			AdminPanelForm.setProcessCount(AdminPanelForm.getProcessCount()+1);
	
			org.pmedv.pojos.Process emptyProcess = new org.pmedv.pojos.Process();
			
			emptyProcess.setIcon("leer.gif");
			emptyProcess.setName("");
			emptyProcess.setTarget("#");
			emptyProcess.setActive(true);
			
			AdminPanelForm.getProcesses().add(emptyProcess);
		}
	
	%>
	
  	
	<!-- div für die Hilfe, ganz rechts -->
  	<div id="hilfe" style="margin:0px; margin-bottom:0px; margin-top:3px; margin-left:3px; margin-right:3px; ">	
  		<p class="help">
  		Dies ist die <b>Haupt&uuml;bersicht</b>.
  		</p> 
  		<p class="help">
  		von hier aus gelangen sie zu den einzelnen Modulen und Aufgaben. Sie können Die Module direkt
  		durch Klicken auf das jeweilige Symbol oder &uuml;ber das Men&uuml; oben auf dieser Seite erreichen.
  		</p>
  		<p class="help">
  			Wenn Sie sich innerhalb eines Moduls befinden, k&ouml;nnen Sie mit einem Klick auf den Button <b>Hauptmenu</b> 
  			in der oberen Leiste wieder zu dieser Ansicht zur&uuml;gelangen.
  		</p>
  	</div>

	<!-- Haupt div für das Panel -->
	<div id="main-div" class="x-hide-display">
		<table class="administration" align="center" border="0" cellspacing="15px" >
			<tr height="25px">
				<td class="text_center">
					&nbsp;
				</td>	
	
				<td class="text_left">
					&nbsp;
				</td>
			</tr>
			
			<tr>
				<td width="50%">
					<table border="0" cellspacing="0" cellpadding="10px" align="center" width="100%">
						<logic:iterate id="process" name="AdminPanelForm" property="processes" length="<%= Integer.toString(AdminPanelForm.getProcessCount()/2) %>">				
							<tr height="90px">
								<td valign="middle" align="center">
									<a href="<bean:write name="process" property="target" filter="false"/>" target="_self">
										<img src="./themes/<%= session.getAttribute("template") %>/icons/dashboard/<bean:write name="process" property="icon" filter="false"/>" border="0" align="middle">	
							         </a>
								</td>								
								<td class="text_left">
									<a id="<bean:write name="process" property="name" filter="false"/>" href="<bean:write name="process" property="target" filter="false"/>" target="_self">
										<bean:write name="process" property="name" filter="false"/>
								    </a>
								</td>
							</tr>
						</logic:iterate>
					</table>			
				</td>
				<td width="50%">
					<table border="0" cellspacing="0" cellpadding="10px" align="center" width="80%">
						<logic:iterate id="process" name="AdminPanelForm" property="processes" offset="<%= Integer.toString(AdminPanelForm.getProcessCount()/2) %>">				
							<tr height="90px">
								<td valign="middle" align="center">
									<a href="<bean:write name="process" property="target" filter="false"/>" target="_self">
										<img src="./themes/<%= session.getAttribute("template") %>/icons/dashboard/<bean:write name="process" property="icon" filter="false"/>" border="0" align="middle">	
							         </a>
								</td>								
								<td class="text_left">
									<a id="<bean:write name="process" property="name" filter="false"/>" href="<bean:write name="process" property="target" filter="false"/>" target="_self">
										<bean:write name="process" property="name" filter="false"/>
									</a>
								</td>
							</tr>
						</logic:iterate>
					</table>			
				</td>

			</tr>
			<tr height="25px">
				<td class="text_center">
					&nbsp;
				</td>	
	
				<td class="text_left">
				   <html:form action="/admin/Logout">
					   <html:hidden property="do" value="logout"/>   
					   <html:hidden property="target" value="Adminpage"/>   
					   	      		
				   		<%
							UserSession userSession = new UserSession();
				   			userSession.init(false,request);
				   			userSession.getAttributes();
				  		%>
					      		
						<input type="hidden" name="username" value="<%= userSession.getLogin() %>">
					</html:form>
				</td>
			</tr>
		</table>	
	
	</div>
  	<div id="south" style="background-color:#f0f0f0; margin:0px; margin-bottom:0px; margin-top:0px; margin-left:0px; margin-right:0px; ">
  		Angemeldete Benutzer : <userlist:userlist></userlist:userlist>
  	</div>
	

	
 </body>
</html>
