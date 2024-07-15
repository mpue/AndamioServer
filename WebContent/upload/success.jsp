<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/pagetags" prefix="page" %>
<%@ page import="org.pmedv.web.ServerUtil"%>

<%@ page import="java.*" %>
<html:html xhtml="true">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Upload</title>	
		<link href="css/style.css" rel="stylesheet" type="text/css" />
		<link rel='stylesheet' type='text/css' href="/<page:config property="localPath"/>jscripts/window/window.css" />
		<script type="text/javascript" src="/<page:config property="localPath"/>scripts/prototype.js"></script>
		<script type="text/javascript" src="/<page:config property="localPath"/>scripts/scriptaculous.js"></script>		
		<script type="text/javascript" src="/<page:config property="localPath"/>scripts/contextMenu.js"></script>
		<script type="text/javascript" src="/<page:config property="localPath"/>scripts/calendar/calendar.js"></script>
		<script type="text/javascript" src="/<page:config property="localPath"/>scripts/window/window.js"></script>
		
	</head>
	<body>
		<p>File successfully uploaded.</p>
		<p><a href="/andamio/upload/FileUpload.do?do=prepareUpload" target="_self">Back to upload</a></p>
	</body>
</html:html>