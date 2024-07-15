<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/tags/struts-bean"  prefix="bean" %>
<%@ taglib uri="/tags/struts-html"  prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page session="true" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<head>
  <title><bean:message key="login.title"/></title>
	
	<!-- Ext JS -->
	<link rel="stylesheet" type="text/css" href="../jscripts/extjs/resources/css/ext-all.css" />
 	<script type="text/javascript" src="../jscripts/extjs/adapter/ext/ext-base.js"></script>
    <script type="text/javascript" src="../jscripts/extjs/ext-all.js"></script>
	<link rel="stylesheet" type="text/css" href="../jscripts/extjs/resources/css/xtheme-gray.css" />
	
	<!-- Viewport Initialisierung -->
    <script type="text/javascript" src="login_init.js"></script>
	

    <script type="text/javascript">
		
		/* Initialisierung des Viewports */
        Ext.onReady(Weberknecht.login.init, Weberknecht.login); 
				
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
		.title-icon {
			background:transparent url(themes/experience/decorations/weberknecht_logo.png);
			height: 37px;
			width: 300px;
		}
		
    </style>


</head>
<body>			  
	
	<div id="loginWindow" class="x-hidden">
	</div>
	
 </body>
</html>
