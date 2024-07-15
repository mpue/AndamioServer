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
		<script type="text/javascript" src="/<page:config property="localPath"/>scripts/window/window.js"></script>
		
	</head>
	<body>
		<logic:present name="permission" scope="session">
			<logic:messagesPresent>
				<div id="errors">
		   			<html:errors/>		
		   		</div>	
			</logic:messagesPresent>	
			<form name="FileUploadForm" method="post" action="FileUpload.do" enctype="multipart/form-data" class="FileUploadForm">
				<input type="hidden" name="directory" id="directory" value="<%= request.getSession().getAttribute("directory") %>"/>
				<html:hidden property="do" value="upload"/>
          		<input type="hidden" name="redirect" value="/<page:config property="localPath"/>/upload/FileUpload.do?do=success">
				<h3>Please select a file to upload.</h3>          		
          		<table class="upload">
          			<tr>
          				<td>File</td>
          				<td><input type="file" name="uploadFile"/></td>
          				<td colspan="2"><html:submit>Upload</html:submit></td>
          			</tr>
          			<tr>
          			   <td>Name</td>
          			   <td colspan="3"><input name="name" id="name" size="40"/></td>          			   
          			</tr>
                    <tr>
                       <td>Description</td>
                       <td colspan="3"><input name="description" id="description" size="40"/></td>                       
                    </tr>
          			
          		</table>
          		
			</form>
		</logic:present>
		<logic:notPresent name="permission" scope="session">
			<p>You must be logged in in order to upload files.</p>		
		</logic:notPresent>
	</body>
</html:html>