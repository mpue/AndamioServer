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
<head><title id="pagetitle"><%= title %></title>
<link rel="stylesheet" href="./themes/<%= session.getAttribute("template") %>/style.css" type="text/css">
<script src="../jscripts/toolTips.js" type="text/javascript"></script>
<script src="../jscripts/rowHandler.js" type="text/javascript"></script>	
<link rel="StyleSheet" href="../jscripts/dtree.css" type="text/css">
<script type="text/javascript" src="../jscripts/dtree.js"></script>


<!-- compliance patch for microsoft browsers -->
<!--[if lt IE 7]>
<script src="../jscripts/IE7_0_9/ie7-standard-p.js" type="text/javascript">
<script src="../jscripts/IE7_0_9/ie7-css3-selectors.js" type="text/javascript"></script>
<script src="../jscripts/IE7_0_9/ie7-css-strict.js" type="text/javascript"></script>			
</script>
<![endif]-->	


</head>
	