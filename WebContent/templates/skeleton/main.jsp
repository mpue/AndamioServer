<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/pagetags" prefix="page" %>


<html:html> 
	<head>
		<title>			
			<page:pagetitle/>
		</title>

		<bean:write name="MainpageForm" property="content.metatags" filter="false"/>
		<link rel="shortcut icon" type="image/x-icon" href="/<page:config property="localPath"/>templates/skeleton/images/fav.ico"/>
		<link href="/<page:config property="localPath"/>templates/<page:config property="template"/>/css/template.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="/<page:config property="localPath"/>templates/<page:config property="template"/>/lightbox2js/prototype.js"></script>
		<script type="text/javascript" src="/<page:config property="localPath"/>templates/<page:config property="template"/>/lightbox2js/scriptaculous.js?load=effects"></script>
		<script type="text/javascript" src="/<page:config property="localPath"/>templates/<page:config property="template"/>/lightbox2js/lightbox.js"></script>		
		<link rel="stylesheet" href="/<page:config property="localPath"/>templates/<page:config property="template"/>/css/lightbox.css" type="text/css" media="screen" />

	</head>

	<body>
		<table border="1" cellspacing="0px" cellpadding="10px" align="center" width="100%">
			<tr>
				<td colspan="2" class="header">					
					<h1> <page:pagetitle/></h1>
				</td>
				<td>
					<div align="right" style="margin-top: 22px ">
						<%@include file="searchbox.jsp" %>
					</div>							
				</td>
			</tr>
			<tr>
				<td valign="top" width="15%">
					<page:xmenu  
			        	selected="<%= request.getParameter(\"nodeName\")%>"
			        	forceLevelIndent="true"  
			        	type="list"
			        	startNode="kolibri"
			        	mode="website">
					</page:xmenu>			
				</td>
				<td valign="top" width="70%">
					<p>					
						<logic:empty name="MainpageForm" property="foundNodes">										
						<h2><page:pathway separator="/"/></h2>
						</logic:empty>
						<logic:notEmpty name="MainpageForm" property="foundNodes">
							<h2><bean:write name="MainpageForm" property="content.description" filter="false"/></h2>
						</logic:notEmpty>
					</p>
					<p class="title">
										
					</p>
					<hr/>				
					<p class="content">
						<bean:write name="MainpageForm" property="content.contentstring" filter="false"/>
						
					</p>
					<logic:notEmpty name="MainpageForm" property="foundNodes"> 
					<p class="content">
						<logic:iterate id="foundNode" name="MainpageForm" property="foundNodes" type="org.pmedv.pojos.Node">
							<a href="<%= foundNode.getPath() %>.html" target="_self"><%= foundNode.getName() %></a><br/>
						</logic:iterate>
					<p>
					</logic:notEmpty>
					<p>
				    <logic:notEmpty name="MainpageForm" property="lastModified"> 
						<font size="1"><b><i>Last change : <bean:write name="MainpageForm" property="lastModified"/></i></b></font>								
				    </logic:notEmpty>
				    <logic:notEmpty name="MainpageForm" property="content.lastmodifiedby"> 
						<font size="1"><b><i>by <bean:write name="MainpageForm" property="content.lastmodifiedby"/></i></b></font>								
				    </logic:notEmpty>				    
					</p>
				<td valign="top">
					<logic:present name="permission" scope="session">
						<%@include file="logoutbox.jsp" %>
					</logic:present>																			                                               
					<logic:notPresent name="permission" scope="session">
						<%@include file="loginbox.jsp" %>
					</logic:notPresent>	
				</td>
			</tr>				
		</table>
	</body>
</html:html> 