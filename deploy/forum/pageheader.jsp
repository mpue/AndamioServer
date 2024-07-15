<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/tags/struts-bean"  prefix="bean" %>
<%@ taglib uri="/tags/struts-html"  prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/pagetags" prefix="page" %>
<%@ page session="true" language="java" contentType="text/html; charset=ISO-8859-1" 
	pageEncoding="ISO-8859-1" import="org.pmedv.session.*"%>
<%@page import="org.pmedv.pojos.*" %>
<%@page import="org.pmedv.cms.daos.*" %>
	 
<html>
<head><title><page:config property="sitename"/> - Forum</title>
<link rel="stylesheet" href="./style.css" type="text/css">
<link rel="stylesheet" href="/<page:config property="localPath"/>forum/templates/<page:config property="template"/>/css/template.css" type="text/css">
<link rel="icon" href="/<page:config property="localPath"/>templates/<page:config property="template"/>/favicon.ico" type="image/x-icon">
<link rel="shortcut icon" href="/<page:config property="localPath"/>templates/<page:config property="template"/>/favicon.ico" type="image/x-icon"> 
<script src="../jscripts/toolTips.js" type="text/javascript"></script>
<script src="../jscripts/rowHandler.js" type="text/javascript"></script>
<script type="text/javascript" src="/<page:config property="localPath"/>scripts/prototype.js"></script>
<script type="text/javascript" src="/<page:config property="localPath"/>scripts/scriptaculous.js"></script>		
	
<script type="text/javascript" src="../jscripts/tiny_mce/tiny_mce.js"></script>
<!-- compliance patch for microsoft browsers -->
<!--[if lt IE 7]>
<script src="../jscripts/IE7_0_9/ie7-standard-p.js" type="text/javascript">
<script src="../jscripts/IE7_0_9/ie7-css3-selectors.js" type="text/javascript"></script>
<script src="../jscripts/IE7_0_9/ie7-css-strict.js" type="text/javascript"></script>			
</script>
<![endif]-->	
</head>

<%
	ConfigDAO cfgDAO = DAOManager.getInstance().getConfigDAO();
	Config cfg = (Config) cfgDAO.findByID(1);

%>

	<script type="text/javascript">
	
	
	tinyMCE.init({
		mode : "exact",
		elements  : "postingtext",
		language : "de",
		theme : "advanced",
		plugins : "emotions,advimage",
		// theme_advanced_disable : "styleselect,formatselect",
		theme_advanced_buttons1_add : "undo,redo,emotions,image",		
		theme_advanced_buttons2 : "link,unlink,image,bullist,numlist,indent,outdent,undo,redo",
		theme_advanced_buttons3 : "",					
		theme_advanced_toolbar_location : "top",
		theme_advanced_toolbar_align : "center",
		theme_advanced_resizing : true,
		theme_advanced_statusbar_location : 'bottom',
		theme_advanced_resize_horizontal : true,
		apply_source_formatting : false,
		auto_reset_designmode : false,
		force_br_newlines : true,
		remove_linebreaks : false, 
		force_p_newlines : false,	
		relative_urls : false,
		content_css : "style.css",
		convert_urls : false	
	});

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
	
	</script>