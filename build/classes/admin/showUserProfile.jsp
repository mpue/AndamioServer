<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/tags/struts-bean"  prefix="bean" %>
<%@ taglib uri="/tags/struts-html"  prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/iconbar-displayer" prefix="UI" %>
<%@ taglib uri="/tags/buttonbar-displayer" prefix="buttonbar" %>
<%@ taglib uri="/tags/userlist-displayer" prefix="userlist" %>

<%@ page session="true" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 

<logic:notPresent name="permission" scope="session">
	<logic:redirect action="/admin/LoginDialog?do=login" />
</logic:notPresent>		

<%@page import="org.pmedv.forms.MainpageForm"%>
<%@page import="org.pmedv.forms.ContentShowForm"%>
<%@page import="org.pmedv.session.*"%> 
<%@page import="org.pmedv.pojos.*" %>
<%@page import="org.pmedv.cms.daos.*" %>
<%@page import="java.util.*" %>

<%
	ConfigDAO cfgDAO = new ConfigDAO();

	Config cfg = (Config) cfgDAO.findByID(1);

	String title = "Weberknecht Administration on ";
	title += cfg.getHostname();
	title += " - ";
	title += (String) session.getAttribute("processname");
%>
<html>

<head>

	<!-- Ext JS -->
	<link rel="stylesheet" type="text/css" href="../jscripts/extjs/resources/css/ext-all.css" />
 	<script type="text/javascript" src="../jscripts/extjs/adapter/ext/ext-base.js"></script>
    <script type="text/javascript" src="../jscripts/extjs/ext-all.js"></script>
	
	<!-- Viewport Initialisierung -->
    <script type="text/javascript" src="showUserProfile.js"></script>
    <script type="text/javascript" src="showUserProfile_init.js"></script>

    <script type="text/javascript">
		
		/* Initialisierung des Viewports */
        Ext.onReady(Weberknecht.userprofile.init, Weberknecht.userprofile); 
				
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
			background:transparent url(themes/experience/icons/users.png);
			height: 32px;
			width: 32px;
		}
		
    </style>
	<title id="pagetitle"><%= title %></title>
	<link rel="stylesheet" href="./themes/<%= session.getAttribute("template") %>/style.css" type="text/css">

</head>
<body>			  
	
	<!-- div für den Bereich ganz oben -->
	<div id="north" style="background-color:f0f0f0; margin-bottom:0px; margin-top:0px; margin-left:0px; margin-right:0px; ">
		<UI:iconbar type="basic"></UI:iconbar>
  	</div>
  	
	<!-- div für die Hilfe, ganz rechts -->
  	<div id="hilfe" style="background-color:f0f0f0; margin:0px; margin-bottom:0px; margin-top:0px; margin-left:0px; margin-right:0px; ">
    	The help system is currently not available.
  	</div>

	<form id="userformupdate"></form>
	
 </body>
</html>
