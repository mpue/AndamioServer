<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/pagetags" prefix="page" %>

<html:html> 
<head>
<title><page:pagetitle/></title>

<bean:write name="MainpageForm" property="content.metatags" filter="false"/>

<!-- compliance patch for microsoft browsers -->
<!--[if lt IE 7]>
		<script src="./jscripts/IE7_0_9/ie7-standard-p.js" type="text/javascript">
		<script src="./jscripts/IE7_0_9/ie7-css3-selectors.js" type="text/javascript"></script>
		<script src="./jscripts/IE7_0_9/ie7-css-strict.js" type="text/javascript"></script>			
		</script>
		<![endif]-->
<link rel="shortcut icon" href="templates/weberknechtcms/images/fricke-favicon.ico" />
<link href="/<page:config property="localPath"/>templates/weberknechtcms/css/template.css" rel="stylesheet" type="text/css" />

<!-- Lightbox2 -->
<script language="javascript" type="text/javascript">
	var fileLoadingImage = "";		
	var fileBottomNavCloseImage = "";
</script>
<script type="text/javascript" src="/<page:config property="localPath"/>templates/weberknechtcms/lightbox2js/prototype.js"></script>
<script type="text/javascript" src="/<page:config property="localPath"/>templates/weberknechtcms/lightbox2js/scriptaculous.js?load=effects"></script>
<script type="text/javascript" src="/<page:config property="localPath"/>templates/weberknechtcms/lightbox2js/lightbox.js"></script>
<link rel="stylesheet" href="/<page:config property="localPath"/>templates/weberknechtcms/css/lightbox.css" type="text/css" media="screen" />

<script type="text/javascript" src="/<page:config property="localPath"/>templates/weberknechtcms/styleswitcher.js"></script>
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
								<tr>
									<td width="15px">&nbsp;</td>
									<td valign="top">
										&nbsp;
									</td>
								</tr>
							</table>
							<span class="article_seperator">&nbsp;</span>
						</div>
				</div>
			</div>

			<div id="topmenu">
				&nbsp;
			</div>
			<div id="content">
				<div class="padding">
					<span class="pathway">						
						<page:pathway separator="/" exclude="localhost"></page:pathway>
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
				     <page:xmenu  
				        selected="<%= request.getParameter(\"nodeName\")%>"
				        forceLevelIndent="false"
				        startNode="localhost"				        			         
				        mode="website">
		              </page:xmenu>
          			</div>
                    <div align="center" style="margin-top: 22px ">
	                <logic:present name="permission" scope="session">
		            	<%@include file="logoutbox.jsp" %>
		            </logic:present>																			                                               
		            <logic:notPresent name="permission" scope="session">
				   		<%@include file="loginbox.jsp" %>
					</logic:notPresent>
			       </div>
			<!--End of mainmenu -->	
			
						
				</div>
			</div>
			<div id="footer">
			<!--Start of footer <a href="http://www.weberknechtcms.de" target="_blank"></a> -->
				
				Developed by Thorsten Schminkel &amp; Matthias PÃ¼ski
				<br/>
				Designed by Haiku Ramblings
				
			<!--End of footer -->
			</div>
		</div>
 	</div>
</div>
</body>
</html:html> 
