<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<%@include file="globals.jsp" %>
	<%@include file="i18nCommon.jsp" %>

	<!-- Functions common to all pages -->

	<script type="text/javascript" src="../jscripts/common.js"></script>

	<!-- Ext JS -->
	
	<link rel="stylesheet" type="text/css" href="../jscripts/extjs/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="../jscripts/extjs/upload/css/Ext.ux.UploadDialog.css" />
 	<script type="text/javascript" src="../jscripts/extjs/adapter/ext/ext-base.js"></script>
    <script type="text/javascript" src="../jscripts/extjs/ext-all.js"></script>
	<script type="text/javascript" src="../jscripts/extjs/upload/Ext.ux.UploadDialog.js"></script>
	<script type="text/javascript" src="../jscripts/edit_area/edit_area_full.js"></script>
	    
	<link rel="stylesheet" type="text/css" href="../jscripts/extjs/resources/css/xtheme-gray.css" /> 
	
	<script type="text/javascript">var currentDir = "<%= request.getParameter("dir") %>";</script>
	
	<!-- File browser logic -->
	
    <script type="text/javascript" src="showFileBrowser_init.js"></script>
    
    <script type="text/javascript">Ext.onReady(Weberknecht.filebrowser.init, Weberknecht.filebrowser);</script>

    <script type="text/javascript" src="showFileBrowser.js"></script>
	
	<!-- Viewport Initialization -->

        
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
		
		/*  Main panel icon */
		.panel-icon {
			background:transparent url(themes/experience/icons/panel/files.png);
			height: 32px;
			width: 32px;
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
		
		.refresh-button .x-btn-text-icon {
			background:transparent url(themes/experience/icons/small/refresh.png);
			height: 16px;
			width: 16px;
		}
		
		
		.copy-button .x-btn-text-icon {
			background:transparent url(themes/experience/icons/small/disk_multiple.png);
			height: 16px;
			width: 16px;
		}
		
        .upload-button .x-btn-text-icon {
            background:url(themes/experience/icons/small/folder_go.png);
            height: 16px;
            width: 16px;                        
        }

        .create-dir-button .x-btn-text-icon {
            background:url(themes/experience/icons/small/folder_add.png);
            height: 16px;
            width: 16px;                        
        }		

        .mainmenu-button .x-btn-text-icon {
            background:url(themes/experience/icons/small/arrow_left.png);
            height: 16px;
            width: 16px;                        
        }		
		
		
    </style>
	<title id="pagetitle"><%= title %></title>
	<link rel="stylesheet" href="./themes/<%= session.getAttribute("template") %>/style.css" type="text/css">

</head>
<body>			  
	
	<!-- div für den Bereich ganz oben -->
	<div id="north" style="background-color:#f0f0f0; margin-bottom:0px; margin-top:0px; margin-left:0px; margin-right:0px; ">
		<UI:iconbar type="basic"></UI:iconbar>
  	</div>
  	
	<!-- div für die Hilfe, ganz rechts -->
  	<div id="hilfe" style="background-color:#f0f0f0; margin:0px; margin-bottom:0px; margin-top:0px; margin-left:0px; margin-right:0px; ">
    	The help system is currently not available.
  	</div>
  	
	<div id="editWindow" class="x-hidden">
	    <div class="x-window-header">Edit file</div>
	    <div id="editFile"></div>
	</div>
  	
	
 </body>
</html>
