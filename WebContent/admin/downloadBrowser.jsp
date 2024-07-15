<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
	<%@include file="globals.jsp" %>
	<title id="pagetitle"><%= title %></title>
	<!-- Ext JS -->
	<link rel="stylesheet" type="text/css" href="../jscripts/extjs/resources/css/ext-all.css" />
	<script type="text/javascript" src="/<page:config property="localPath"/>scripts/prototype.js"></script>
	<script type="text/javascript" src="/<page:config property="localPath"/>scripts/scriptaculous.js"></script>			
 	<script type="text/javascript" src="../jscripts/extjs/adapter/ext/ext-base.js"></script>
    <script type="text/javascript" src="../jscripts/extjs/ext-all.js"></script>
	<link rel="stylesheet" type="text/css" href="../jscripts/extjs/resources/css/xtheme-gray.css" />
	
	<!-- Viewport Initialisierung -->
    <script type="text/javascript" src="downloadBrowser.js"></script>
    <script type="text/javascript" src="downloadBrowser_init.js"></script>

    <script type="text/javascript">
		
		/* Initialisierung des Viewports */
        Ext.onReady(Weberknecht.downloadBrowser.init, Weberknecht.downloadBrowser); 
				
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
		.panel-icon {
			background:transparent url(themes/experience/icons/panel/downloads.png);
			height: 32px;
			width: 32px;
		}
		.mainmenu-button .x-btn-text-icon {
            background:url(themes/experience/icons/small/arrow_left.png);
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
    <div id="downloadEditWindow" class="x-hidden">
	    <div class="x-window-header">Download bearbeiten</div>
	    <div id="editDownload"></div>
	</div>


	<form id="gridupdate"></form>
	
 </body>
</html>
