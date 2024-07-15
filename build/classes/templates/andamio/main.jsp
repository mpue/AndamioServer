<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/pagetags" prefix="page" %>

<%@ page import="java.*" %>
<html:html xhtml="true">
<head>
<title><page:pagetitle/></title>

<!-- Bis hier hin nix ändern -->

<!-- Ab hier kommen die Meta Tags -->
<meta name="DC.title" content="" />
<meta name="DC.creator" content="" />
<meta name="city" content=""/> 
<meta name="country" content="" />
<meta name="state" content="" />
<meta name="zipcode" content="" />

<!-- Nicht ändern -->
<meta name="robots" content="all" />
<meta http-equiv="Content-Language" content="de"/>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" />
<meta name="description" content="<bean:write name="MainpageForm" property="content.description" filter="false"/>" />
<meta name="DC.subject" content="<bean:write name="MainpageForm" property="content.description" filter="false"/>" />

<!-- compliance patch for microsoft browsers -->
<!--[if lt IE 7]>
		<script src="../../jscripts/IE7_0_9/ie7-standard-p.js" type="text/javascript">
		<script src="../../jscripts/IE7_0_9/ie7-css3-selectors.js" type="text/javascript"></script>
		<script src="../../jscripts/IE7_0_9/ie7-css-strict.js" type="text/javascript"></script>			
		</script>
		<![endif]-->


<!-- Lightbox2 -->
<script type="text/javascript" src="/<page:config property="localPath"/>templates/pueski/lightbox2js/prototype.js"></script>
<script type="text/javascript" src="/<page:config property="localPath"/>templates/pueski/lightbox2js/scriptaculous.js?load=effects"></script>
<script type="text/javascript" src="/<page:config property="localPath"/>templates/pueski/lightbox2js/lightbox.js"></script>
<link rel="stylesheet" href="/<page:config property="localPath"/>templates/pueski/css/lightbox.css" type="text/css" media="screen" />

<!-- Style -->
<page:stylesheet/>
<link type="text/css" rel="stylesheet" href="/<page:config property="localPath"/>jscripts/syntax/SyntaxHighlighter.css"></link>
<link rel="icon" href="/<page:config property="localPath"/>templates/<page:config property="template"/>/favicon.ico" type="image/x-icon">
<link rel="shortcut icon" href="/<page:config property="localPath"/>templates/<page:config property="template"/>/favicon.ico" type="image/x-icon"> 
<script language="javascript" src="/<page:config property="localPath"/>jscripts/syntax/shCore.js"></script>
<script language="javascript" src="/<page:config property="localPath"/>jscripts/syntax/shBrushJava.js"></script>
<script language="javascript" src="/<page:config property="localPath"/>jscripts/syntax/shBrushCpp.js"></script>
<script language="javascript" src="/<page:config property="localPath"/>jscripts/syntax/shBrushXml.js"></script>
<script language="javascript">

function setHeight(id,height) {	
	var element = document.getElementById(id);
	element.style.height = height + 'px';	
}
function setWidth(id,width) {
	var element = document.getElementById(id);
	element.style.width = width + 'px';	
}

/**
 *	Adjust the content on body load complete
 */
function adjustContent() {				
	setHeight('page',window.innerHeight-150);
	setHeight('forum',window.innerHeight-300);
	setWidth('forum',window.innerWidth/1.6);
}

function syntaxOn() {
	dp.SyntaxHighlighter.ClipboardSwf = '../../jscripts/syntax/clipboard.swf';
	dp.SyntaxHighlighter.HighlightAll('code');
}

function submitComment() {

	var url = 'users/UserComment.do?do=addPageComment';
	
	var oOptions = {
		method : "post",
		parameters : "content_id=<bean:write name="MainpageForm" property="content.id" filter="false"/>&comment="+$('comment').value,
		onSuccess : function(oXHR, oJson) {
			location.reload(true);	
		},
		onFailure : function(oXHR, oJson) {
			alert("Could not create comment.");
		}
	};
	
	var oRequest = new Ajax.Request(url, oOptions);
	
}

function deleteComment(commentId) {

	var url = 'users/UserComment.do?do=deletePageComment';
	
	var oOptions = {
		method : "post",
		parameters : "content_id=<bean:write name="MainpageForm" property="content.id" filter="false"/>&comment_id="+commentId,
		onSuccess : function(oXHR, oJson) {
			location.reload(true);	
		},
		onFailure : function(oXHR, oJson) {
			alert("Could not delete comment.");
		}
	};
	
	var oRequest = new Ajax.Request(url, oOptions);
	
}


</script>

</head>
<body onload="syntaxOn();adjustContent();">

	<table class="main" cellpadding="0" cellspacing="0" id="page">

		<tr class="title">
			<td class="pagetitle" colspan="2">
				<page:config property="sitename"></page:config>
			</td>
			<td  height="95px" class="logo">
				<div align="right">
					<%@include file="searchbox.jsp" %>
										<div class="languages">
						<page:language languages="de,en"></page:language>
					</div>					
				</div>

			</td>
		</tr>
		
		<!-- Menü links, Inhalt rechts -->
		
		<tr valign="top">
		
			<!-- Menü -->
		
			<td valign="top" width="150px" class="menuleft">
				
				<div class="menu">
					<page:xmenu 					
						forceLevelIndent="true"
						orientation="vertical"
						selected="<%= request.getParameter(\"nodeName\")%>"
						mode="blog">
					</page:xmenu>
				</div>
				<br/><br/>				
			</td>
			
			<!-- Inhalt -->
			
			<td valign="top" width="924px">
				<div class="heading">
					<h1>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<page:pathway separator="/"/></h1>
					<div class="hr"></div>
				</div>
				<div class="content" id="content">					
					<bean:write name="MainpageForm" property="content.contentstring" filter="false"/>
					<logic:notEmpty name="MainpageForm" property="searchResults"> 
					<p>
						<logic:iterate id="foundNode" name="MainpageForm" property="searchResults">
							<a href="<%= foundNode %>.html&highlight=<%= request.getParameter("search") %>" target="_self"><%= foundNode %></a><br/>
						</logic:iterate>
					<p>
					</logic:notEmpty>
					<p><i><page:lastModified showUser="true" showDate="true"/></i></p>
				</div>
				<logic:equal value="true" name="MainpageForm" property="content.commentsAllowed">
					<logic:present name="permission" scope="session">
					<div class="comments">
						<div>
							<textarea class="commentbox" rows="4" cols="100" id="comment" name="comment"></textarea>										
						</div>
						<div class="submitComment">
							<input type="button" onclick="submitComment();" value="Add comment" class="button-top"/>					
						</div>
					</div>
					</logic:present>				
					<div class="comments">
						<p>
							<b>Comments :</b>
						</p>
						<logic:notEmpty name="MainpageForm" property="content.userComments">									
							<logic:iterate id="comment" name="MainpageForm" property="content.userComments">
								<p class="author"><bean:write name="comment" property="author"/> wrotes on <bean:write name="comment" property="humanReadableTime"/>
									<logic:equal value="admin" name="login" scope="session">
										<input type="button" class="button-top" onclick="deleteComment(<bean:write name="comment" property="id"/>);" value="Delete"/>
									</logic:equal>							
								</p>
								<p class="comment"><bean:write name="comment" property="text"/></p>							
							</logic:iterate>
						</logic:notEmpty>
						<logic:empty name="MainpageForm" property="content.userComments">
							No comments found.
						</logic:empty>
					</div>
				</logic:equal>
			</td>
			<td valign="top" width="150px" class="menuright">
				<div align="center" style="margin-left: 10px;margin-top: 22px ">
					<logic:present name="permission" scope="session">
					<%@include file="logoutbox.jsp" %>															
					</logic:present>
					<logic:notPresent name="permission" scope="session">
					<%@include file="loginbox.jsp" %>								
					</logic:notPresent>
				</div>
			</td>
		</tr>
	</table>
	<div class="footer">
		Powered by <a href="http://www.andamiocms.org" target="_blank"><b>AndamioCMS</b></a> - Copyright 2003-2012 Matthias Pueski 
	</div>
	
</body>
</html:html> 