<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@include file="globals.jsp" %>
<%@include file="i18nCommon.jsp" %>
<%@include file="contentEditorI18n.jsp" %>

  	<title><%=title%></title>
  	
	<!-- Ext JS -->

	<link rel="stylesheet" type="text/css" href="../jscripts/extjs/resources/css/ext-all.css" />
 	<script type="text/javascript" src="../jscripts/extjs/adapter/ext/ext-base.js"></script>
    <script type="text/javascript" src="../jscripts/extjs/ext-all.js"></script>
	<script type="text/javascript" src="../jscripts/extjs/upload/Ext.ux.UploadDialog.js"></script>

	<link rel="stylesheet" type="text/css" href="../jscripts/extjs/resources/css/xtheme-gray.css" />
	<link rel="stylesheet" type="text/css" href="../jscripts/extjs/upload/css/Ext.ux.UploadDialog.css" />
	
    <!-- Tiny Mce (V3/V2) -->
    
	<script type="text/javascript" src="../jscripts/tiny_mce/tiny_mce.js"></script>
	<script type="text/javascript" src="contentEditor_MCE_config.js"></script>

    <script type="text/javascript" src="../jscripts/contentEditor.js"></script>
	
	<!-- west panel with tree -->
	
    <script type="text/javascript" src="contentEditor_west_tree.js"></script>
	
	<!-- center panel with editor -->
	
    <script type="text/javascript" src="contentEditor_center_editor.js"></script>
	
	<!-- viewport initialization -->
	
    <script type="text/javascript" src="contentEditor_init.js"></script>

    <script type="text/javascript">
		
        Ext.onReady(Weberknecht.app.init, Weberknecht.app);
		tinyInit();
				
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
	    
		#tree-div
		{
	    	float:left;
	    	margin-left:0px;
			margin-top:0px;
			background-color:#f0f0f0; /* hellgrau */
	    	border-color:#f0f0f0; /* hellgrau */
			border:0px;
			overflow:auto;
	    }
	
		
		/* Icons for the tree nodes */

	    .content .x-tree-node-icon {
			background:transparent url(themes/experience/icons/small/contents.png)!important;
			height: 16px;
			width: 16px;			
		}
		
		.content-button .x-btn-text-icon {
            background:url(themes/experience/icons/small/contents.png);
            height: 16px;
            width: 16px;            
            
        }
        
	    .content_restricted .x-tree-node-icon {
			background:transparent url(themes/experience/icons/small/contents_restricted.png)!important;
			height: 16px;
			width: 16px;			
		}

	    .link .x-tree-node-icon {
			background:transparent url(themes/experience/icons/small/link_offline.png) !important;
			height: 16px;
			width: 16px;
  		 }
  		 
	    .plugin .x-tree-node-icon {
			background:transparent url(themes/experience/icons/small/plug.png) !important;
			height: 16px;
			width: 16px;			
		}
		
		.mainmenu-button .x-btn-text-icon {
            background:url(themes/experience/icons/small/arrow_left.png);
            height: 16px;
            width: 16px;                        
        }		

        .plugin-button .x-btn-text-icon {
            background:url(themes/experience/icons/small/plug.png);
            height: 16px;
            width: 16px;            
            
        }

        .export-button .x-btn-text-icon {
            background:url(themes/experience/icons/small/folder_go.png);
            height: 16px;
            width: 16px;                        
        }

        .import-button .x-btn-text-icon {
            background:url(themes/experience/icons/small/folder_add.png);
            height: 16px;
            width: 16px;                        
        }

        .save-button .x-btn-text-icon {
            background:url(themes/experience/icons/small/disk.png);
            height: 16px;
            width: 16px;                        
        }

        .preview-button .x-btn-text-icon {
            background:url(themes/experience/icons/small/zoom.png);
            height: 16px;
            width: 16px;                        
        }
        
        .tags-button .x-btn-text-icon {
            background:url(themes/experience/icons/small/page_white_text.png);
            height: 16px;
            width: 16px;                        
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
		.up-icon {
			background:transparent url(themes/experience/icons/small/element_up.png);
			height: 16px;
			width: 16px;
		}
		.down-icon {
			background:transparent url(themes/experience/icons/small/element_down.png);
			height: 16px;
			width: 16px;
		}
		.refresh-icon {
			background:transparent url(themes/experience/icons/small/refresh.png);
			height: 16px;
			width: 16px;
		}

		
		/* Panel icon on top left */
		.panel-icon {
			background:transparent url(themes/experience/icons/panel/contents.png);
			height: 32px;
			width: 32px;
		}
		
    </style>


</head>
<body>			  
	<input type="hidden" name="id" id="id">
			
	<!-- toolbar div -->
	<div id="north" style="background-color:#f0f0f0; margin-bottom:0px; margin-top:0px; margin-left:0px; margin-right:0px; ">
		<UI:iconbar type="basic"></UI:iconbar>
  	</div>
  	
	<!-- help div -->
  	<div id="hilfe" style="background-color:#f0f0f0; margin:0px; margin-bottom:0px; margin-top:0px; margin-left:0px; margin-right:0px; ">
    	The help system is currently not available.
  	</div>

	<!-- main panel div -->
	<div id="main-div" class="x-hide-display">
	</div>
  	<div id="south" style="background-color:#f0f0f0; margin:0px; margin-bottom:0px; margin-top:0px; margin-left:0px; margin-right:0px; ">
  		<bean:message key="status.users"/> : <userlist:userlist></userlist:userlist>
  		<p>
	  		<form name="TimerForm">
				Sekunden bis zum Sitzungsende : <input name="time" class="bs1" size="4" readonly="readonly" />
			</form>	
		</p>  		  		
  	</div>

	<!-- div for the node tree on the left hand -->
	<div id="tree-div">
	</div>

	<div id="contentWindow" class="x-hidden">
	    <div class="x-window-header"><bean:message key="dialog.title.createcontent"/></div>
	    <div id="createContent"></div>
	</div>

	<div id="pluginWindow" class="x-hidden">
	    <div class="x-window-header"><bean:message key="dialog.title.createplugin"/></div>
	    <div id="addPlugin"></div>
	</div>	

	<div id="pluginEditWindow" class="x-hidden">
	    <div class="x-window-header"><bean:message key="dialog.title.editplugin"/></div>
	    <div id="editPlugin"></div>
	</div>	
	
	<div id="linkWindow" class="x-hidden">
	    <div class="x-window-header"><bean:message key="dialog.title.createlink"/></div>
	    <div id="addLink"></div>
	</div>	

	<div id="linkEditWindow" class="x-hidden">
	    <div class="x-window-header"><bean:message key="dialog.title.editlink"/></div>
	    <div id="editLink"></div>
	</div>		

	<div id="uploadWindow" class="x-hidden">
	    <div class="x-window-header"><bean:message key="dialog.title.uploadfile"/></div>
	</div>

 </body>
</html>
