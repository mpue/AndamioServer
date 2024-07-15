<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>

<head>
	<%@include file="globals.jsp" %>
	<title id="pagetitle"><%= title %></title>
	<!-- Ext JS -->
	<link rel="stylesheet" type="text/css" href="../jscripts/extjs/resources/css/ext-all.css" />
 	<script type="text/javascript" src="../jscripts/extjs/adapter/ext/ext-base.js"></script>
    <script type="text/javascript" src="../jscripts/extjs/ext-all.js"></script>
	<link rel="stylesheet" type="text/css" href="../jscripts/extjs/resources/css/xtheme-gray.css" />
	

    <script type="text/javascript" src="../jscripts/common.js"></script>
    <script type="text/javascript" src="showUserList.js"></script>
    <script type="text/javascript" src="showUserList_init.js"></script>

    <script type="text/javascript">
		
		/* Initialisierung des Viewports */
        Ext.onReady(Weberknecht.userlist.init, Weberknecht.userlist); 
				
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

		.usergroups-icon {
			background:transparent url(themes/experience/icons/small/usergroups.png);
			height: 16px;
			width: 16px;
		}

		.delete-icon {
			background:transparent url(themes/experience/icons/small/delete.png);
			height: 16px;
			width: 16px;
		}
		.edit-icon {
			background:transparent url(themes/experience/icons/small/edit.png);
			height: 16px;
			width: 16px;
		}
		.refresh-icon {
			background:transparent url(themes/experience/icons/small/refresh.png);
			height: 16px;
			width: 16px;
		}
		
		/*  Icon zur Darstellung in der Titelleiste des obersten Panels (ganz oben links). */
		.panel-icon {
			background:transparent url(themes/experience/icons/panel/users.png);
			height: 32px;
			width: 32px;
		}
		
		.mainmenu-button .x-btn-text-icon {
            background:url(themes/experience/icons/small/arrow_left.png);
            height: 16px;
            width: 16px;                        
        }
		.adduser-button .x-btn-text-icon {
            background:url(themes/experience/icons/small/user_add.png);
            height: 16px;
            width: 16px;                        
        }
		.save-button .x-btn-text-icon {
            background:url(themes/experience/icons/small/disk.png);
            height: 16px;
            width: 16px;                        
        }
        
		
		
    </style>
	
	<link rel="stylesheet" href="./themes/<%= session.getAttribute("template") %>/style.css" type="text/css">

</head>
<body>			  
	
	<!-- div f�r den Bereich ganz oben -->
	<div id="north" style="background-color:f0f0f0; margin-bottom:0px; margin-top:0px; margin-left:0px; margin-right:0px; ">
		<UI:iconbar type="basic"></UI:iconbar>
  	</div>
  	
	<!-- div f�r die Hilfe, ganz rechts -->
  	<div id="hilfe" style="background-color:f0f0f0; margin:0px; margin-bottom:0px; margin-top:0px; margin-left:0px; margin-right:0px; ">
    	The help system is currently not available.
  	</div>

	<div id="userWindow" class="x-hidden">
	    <div class="x-window-header">Benutzer anlegen</div>
	    <div id="createUser"></div>
	</div>

    <div id="userEditWindow" class="x-hidden">
	    <div class="x-window-header">Benutzer bearbeiten</div>
	    <div id="editUser"></div>
	</div>

    <div id="userAttributesWindow" class="x-hidden">
	    <div class="x-window-header">Attribute bearbeiten</div>
	    <div id="userAttributes"></div>
	</div>

	<div id="confirmWindow" class="x-hidden">
		
	    <div class="x-window-header">L&ouml;schen best&auml;tigen</div>
	     <div id="confirmMessage">Wollen Sie den Benutzer wirklich l&ouml;schen?</div>
	</div>

	<form id="usergridupdate"></form>
	
 </body>
</html>
