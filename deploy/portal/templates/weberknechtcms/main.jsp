<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/xmenu-displayer" prefix="xmenu" %>
<%@ taglib uri="/tags/pathway-displayer" prefix="pathway" %>
<%@ taglib uri="/tags/static-content-displayer" prefix="static-content" %>
<%@ taglib uri="/tags/pagetitle-displayer" prefix="pagetitle" %>
<%@ taglib uri="/tags/config" prefix="cms" %>
<%@page import="org.pmedv.cms.daos.*" %>
<%@page import="org.pmedv.pojos.*" %>
<%@page import="org.pmedv.util.NodeHelper" %>

<%@page import="java.util.*" %>
<%@page import="java.*" %>

<html:html> 
<head>
<title><pagetitle:pagetitle nodeName="<%= request.getParameter(\"nodeName\") %>" /></title>

<!-- Google Indexing -->
<meta name="verify-v1" content="6vG9oZ0TwSO3yCDGcECGs5FuLx9CN+Zf/Wk9v3xlQWE=" />

<meta http-equiv="Content-Language" content="de"/>
<meta name="description" content="CMS, Content-Management-System, Homepage-Assistenten, Homepage, Homepagebau, Qualified Google Advertising"/>
<meta name="city" content="Leinfelden-Echterdingen"/>
<meta name="country" content="Germany"/>
<meta name="state" content="Baden-Würtemberg"/>
<meta name="zipcode" content="70771"/>

<link rel="shortcut icon" href="templates/weberknechtcms/images/fricke-favicon.ico" />
<link href="/<cms:config property="localPath"/>templates/weberknechtcms/css/template_css.css" rel="stylesheet" type="text/css" />

<!-- Lightbox2 -->
<script language="javascript" type="text/javascript">
	var fileLoadingImage = "";		
	var fileBottomNavCloseImage = "";
</script>
<script type="text/javascript" src="/<cms:config property="localPath"/>templates/weberknechtcms/lightbox2js/prototype.js"></script>
<script type="text/javascript" src="/<cms:config property="localPath"/>templates/weberknechtcms/lightbox2js/scriptaculous.js?load=effects"></script>
<script type="text/javascript" src="/<cms:config property="localPath"/>templates/weberknechtcms/lightbox2js/lightbox.js"></script>
<link rel="stylesheet" href="/<cms:config property="localPath"/>templates/weberknechtcms/css/lightbox.css" type="text/css" media="screen" />

<script type="text/javascript" src="/<cms:config property="localPath"/>templates/weberknechtcms/styleswitcher.js"></script>
<script type="text/javascript"><!--//--><![CDATA[//><!--
		
		sfHover = function() {
			var sfEls = document.getElementById("mainlevelmainnav").getElementsByTagName("LI");
			for (var i=0; i<sfEls.length; i++) {
				sfEls[i].onmouseover=function() {
					this.className+=" sfhover";
				}
				sfEls[i].onmouseout=function() {
					this.className=this.className.replace(new RegExp(" sfhover\\b"), "");
				}
			}
		}
		if (window.attachEvent) window.attachEvent("onload", sfHover);
		
		//--><!]]></script>
</head>
<body id="page_bg">
<a name="up" id="up"></a>

<div id="center">
	<div id="main_bg">
  	<div id="content_bg">
			<div id="header">
				<div id="newflash">
						<div>
							<table class="contentpaneopen">
								<!--  tr>
									<td width="15px">&nbsp;</td>
									<td valign="top">
										<static-content:static-content contentName="Neuigkeiten" contentType="headline" />
									</td>
								</tr -->
								
								<tr>
									<td width="15px">&nbsp;</td>
									<td valign="top">
										<static-content:static-content contentName="Neuigkeiten" contentType="content" />
									</td>
								</tr>
							</table>
							<span class="article_seperator">&nbsp;</span>
						</div>
				</div>
			</div>

			<div id="topmenu">
				<!-- ul id="mainlevel-nav">
					<li><a href="http://www.joomlaportal.de" target="_blank" class="mainlevel-nav" >Forum</a></li>
					<li><a href="http://www.komponenten.joomlademo.de" class="mainlevel-nav" >K-Demo</a></li>
					<li><a href="http://www.module.joomlademo.de" class="mainlevel-nav" >M-Demo</a></li>
					<li><a href="http://www.joomlaos.de" target="_blank" class="mainlevel-nav" >JoomlaOS</a></li>
				</ul -->
			</div>
			<div id="content">
				<div class="padding">
					<span class="pathway">						
						<pathway:pathway nodeName="<%=NodeHelper.locateNodeByName(request.getParameter(\"nodeName\")).getName() %>" separator=""/>
					</span>
					
					<div class="highlight">
					</div>
					
					<br />
					
					<table class="contentpaneopen">
				<tr>
			<td valign="top" colspan="2">
                <h1><bean:write name="MainpageForm" property="content.contentname" filter="false"/></h1>
                <span class="contentpane">
                    <bean:write name="MainpageForm" property="content.contentstring" filter="false"/>
                </span>
                    			</td>
		</tr>
				</table>		
		<span class="article_seperator">&nbsp;</span>
				</div>				
			</div>
			<div id="navigation">
				<div class="padding">
				
				
			<!--Start of mainmenu -->
          	<div class="moduletablemainnav">
          		<div class="likeh3">&nbsp;</div>

          	</div>
			<!--End of mainmenu -->	
			
						
				</div>
			</div>
			<div id="footer">
			<!--Start of footer <a href="http://www.weberknechtcms.de" target="_blank"></a> -->
				
				Developed by Thorsten Schminkel &amp; Matthias Püski
				<br/>
				Designed by Haiku Ramblings
				
			<!--End of footer -->
			</div>
		</div>
 	</div>
</div>
</body>
</html:html> 