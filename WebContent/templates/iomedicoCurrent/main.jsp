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
	<title><page:pagetitle/></title>
	<meta name="DC.title" content="" />
	<meta name="DC.creator" content="" />
	<meta name="city" content=""/> 
	<meta name="country" content="" />
	<meta name="state" content="" />
	<meta name="zipcode" content="" />
	<meta name="robots" content="all" />
	<meta http-equiv="Content-Language" content="de"/>
	<meta http-equiv="content-type" content="text/html;charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />	
	<meta name="description" content="<bean:write name="MainpageForm" property="content.description" filter="false"/>" />
	<meta name="DC.subject" content="<bean:write name="MainpageForm" property="content.description" filter="false"/>" />
	<link rel="shortcut icon" type="image/x-icon" href="/<page:config property="localPath"/>templates/iomedicoCurrent/images/fav.ico"/>
	<link rel="stylesheet" type="text/css" href="/<page:config property="localPath"/>templates/iomedicoCurrent/css/main.css" />
	<script type="text/javascript" src="/<page:config property="localPath"/>templates/iomedicoCurrent/scripts/ie7-standard.js"></script>
	<link rel='stylesheet' type='text/css' href="/<page:config property="localPath"/>templates/iomedicoCurrent/css/ms-fixup.css" />
	<link rel='stylesheet' type='text/css' href="/<page:config property="localPath"/>jscripts/window/window.css" />
	<script type="text/javascript" src="/<page:config property="localPath"/>templates/iomedicoCurrent/scripts/main.js"></script>
	<script type="text/javascript" src="http://<page:config property="hostname"/>/<page:config property="localPath"/>templates/iomedicoCurrent/scripts/ypSlideOutMenus.js"></script>
	<script type="text/javascript" src="/<page:config property="localPath"/>templates/iomedicoCurrent/scripts/reportScripts.js"></script>
	<script type="text/javascript" src="/<page:config property="localPath"/>templates/iomedicoCurrent/plugins/avanti/editPatient.js"></script>
	<script type="text/javascript" src="/<page:config property="localPath"/>jscripts/prototype.js"></script>
	<script type="text/javascript" src="/<page:config property="localPath"/>jscripts/scriptaculous.js"></script>		
	<script type="text/javascript" src="/<page:config property="localPath"/>jscripts/window/window.js"></script>
	<script type="text/javascript" charset="">

	var left = (window.screen.width / 2) - (160 + 10);
	var top  = (window.screen.height / 2) - 250;

	var initialsId = '';
	
	var messageWindow;
	var data;

	var _centerID;
	var _patientID; 
	var _patientNumber;
	
	function openIOFilesList(id) {
		getAttachments(id);
	}


	function getAttachments(id) {

		var url = 'http://<%= ServerUtil.getHostUrl(request) %>/<page:config property="localPath"/>IOFileAction.do?do=getAttachments&docId='+id;

		new Ajax.Request(url, {
			method: 'get',
  		       onComplete: function(transport) {
				  if (200 == transport.status) {
				      data = transport.responseText;
				  	  processAttachments(id);					    
				  }
				  else {
					  alert("An error occured during connect.\n\nPlease check the server settings.\n\nError code : "+transport.status);
				  }
		  	   }
		});
		
	}
	
	function processLogin() {
		Effect.Appear('waitForLoginDiv', { duration: 0.5 });
	}
	
	function processAttachments(id) {
		
		var attachments = eval(data); 	

		var attachmentData = '';
		
		for(var i=0; i < attachments.length; i++) {
		    
			var attachment = attachments[i];

			attachmentData += '<a href="http://<%= ServerUtil.getHostUrl(request) %>/<page:config property="localPath"/>IOFileAction.do?do=getAttachment&docID='+id+'&attachmentID='+attachments[i].attachID+'&name='+escape(attachment.name)+'">'			
			attachmentData += attachment.name;
			attachmentData +="</a>";
			attachmentData +="<br>";
			
		}
		
		var filelist = document.getElementById('filelist');
		filelist.innerHTML = attachmentData;

		Effect.Appear('messageDiv', { duration: 0.5 });
		
	}	

	function hideDiv(id) {
		Effect.Fade(id, { duration: 0.5 });
	}

	function openPatientInitialsEdit(centerID, patientID, patientNumber) {

		_centerID      = centerID;
		_patientID     = patientID;
		_patientNumber = patientNumber; 

		center('patientInitialsDiv');
		Effect.Appear('patientInitialsDiv', { duration: 0.5 });		

		var randomNrDiv = document.getElementById('randomNr');
		randomNrDiv.innerHTML = patientNumber;
		initialsId = 'initials_'+centerID+"_"+patientID;

		var initials = document.getElementById(initialsId);
		document.getElementById('patientInitials').value = initials.innerHTML;		
		document.getElementById('patientInitials').focus();
		
	}

	function processInitials() {
		
		var initials = document.getElementById(initialsId);
		initials.innerHTML = document.getElementById('patientInitials').value;		
		hideDiv('patientInitialsDiv');
		
		var url = "http://<%= ServerUtil.getHostUrl(request) %>/<page:config property="localPath"/>IOFileAction.do?do=setInitials";

		var poststr =  "initials=" + encodeURI( document.getElementById('patientInitials').value) +
		"&patientName=" + '' +
    	"&centerID="     + encodeURI( _centerID ) +
    	"&patientID="    + encodeURI( _patientID ) +
    	"&patientNumber="   + encodeURI( _patientNumber );
		
		new Ajax.Request(url, {
			method: 'post',
			postBody : poststr,
			onComplete: function() {
				Effect.Pulsate(initialsId, { pulses: 5, duration: 1.5 });		
		  	}
		});
		
	}

	function center(id) {
	    var pageX = (document.all)?document.body.offsetWidth:window.width;
	    var pageY = (document.all)?document.body.offsetHeight:window.height;
        var objRef = document.getElementById(id);
        var objW = objRef.offsetWidth;
        var objH = objRef.offsetHeight;
        objRef.style.left = ((pageX/2)-(objW/2))+"px";
        objRef.style.top = ((pageY/2)-(objH/2))+"px";
	}	

	function initDrag() {
		new Draggable('patientInitialsDiv', { scroll: window });
		new Draggable('messageDiv', { scroll: window });		
	}
	
	</script>	
	
  </head>
  
	  <style type="text/css">
	
		table.Patients {
			width: 90%;	
		}
	
		tr.odd {
			background-color: #BBBBBB;
		}
		
		td.birthDate {
			text-align: left;	
		}
	
		td.button {
			text-align: center;
			width : 120px;		
		}
		
		td.active {
			text-align: center;
			width : 90px;		
		}
		
		span.pagebanner {
			padding-top: 10px;	
		}
		
		input.portalButton {
			text-align : center;
		    background-color : #d6e3f4;
		    border: 1px solid #000000;
		}
	
		#messageDiv {
			background-image : url("/<page:config property="localPath"/>templates/iomedicoCurrent/images/Dialog.gif");
			z-index: 1;		
			position : absolute;
			width: 394px;
			height: 257px; 
			left:50%;
			margin-left:-197px;				
			top:50%;
			margin-top:-128px;			
		}
		
		#patientInitialsDiv {
			background-image : url("/<page:config property="localPath"/>templates/iomedicoCurrent/images/Dialog.gif");
			z-index: 1;		
			position : absolute;
			width: 394px;
			height: 257px; 
			left:50%;
			margin-left:-197px;				
			top:50%;
			margin-top:-128px;			
		}
		
		#waitForLoginDiv {
			background-image : url("/<page:config property="localPath"/>templates/iomedicoCurrent/images/Dialog.gif");
			z-index: 1;		
			position : absolute;
			width: 394px;
			height: 257px; 
			left:50%;
			margin-left:-197px;				
			top:50%;
			margin-top:-128px;			
		}
	
	</style>
  
  <body onload="Effect.Appear('credentials', { duration: 0.7 });initDrag();">  
  
	<div id="messageDiv" style="display:none">
		<table border="0" align="center" cellspacing="0" cellpadding="0px" width="100%" height="257px">
			<tr height="25px">
				<td align="left" colspan="2" valign="top">
					<div style="color:#ffffff;padding-left : 35px;padding-top:3px;">Dateien herunterladen</div>
				</td>
			</tr>
			<tr>
				<td align="center">
					<h3>Klicken Sie auf einen Eintrag um die Datei herunterzuladen.</h3>
				</td>
			</tr>
			<tr height="140px">
				<td align="center" valign="top">
					<div id="filelist">
					</div>
				</td>
			</tr>
			<tr>
				<td align="center">
					<input type="button" value="Schliessen" onclick="hideDiv('messageDiv');">
				</td>
			</tr>			
		</table>
	</div>   
	
	<div id="patientInitialsDiv" style="display:none">
		<table border="0" align="center" cellspacing="0" cellpadding="0px" width="80%" height="257px">
			<tr height="25px" valign="top">
				<td align="left" colspan="2" valign="top">
					<div style="color:#ffffff;padding-top:3px;">Patienteninitialen &auml;ndern</div>
				</td>
			</tr>
			<tr height="35px">
				<td align="center" colspan="2">
					<h3>Bitte geben sie den neuen Wert ein</h3>
				</td>
			</tr>
			<tr height="68px">
				<td align="left">
					Random Nr.
				</td>
				<td>
					<div id="randomNr">
					</div>
				</td>
			</tr>
			<tr height="68px">
				<td align="left" valign="middle" style="margin-left : 10px;">
					Initialen
				</td>
				<td align="left" valign="middle">
					<input type="text" name="patientInitials" size="6" id="patientInitials"/>
				</td>
			</tr>
			<tr height="35px">
				<td align="center">
					<input type="button" value="Speichern" onclick="processInitials();">
				</td>
				<td align="center">
					<input type="button" value="Abbrechen" onclick="hideDiv('patientInitialsDiv')">
				</td>
				
			</tr>			
		</table>
	</div>  	
	
	<div id="waitForLoginDiv" style="display:none">
		<table border="0" align="center" cellspacing="0" cellpadding="0px" width="100%" height="257px">
			<tr height="25px">
				<td align="left" colspan="2" valign="top">
					<div style="color:#ffffff;padding-left : 35px;padding-top:3px;">Anmeldung</div>
				</td>
			</tr>
			<tr>
				<td align="center">
					<h3>Bitte warten</h3>
					<h3>Die Anmeldung wird durchgef&uuml;hrt.</h3>
				</td>
			</tr>
			<tr height="140px">
				<td align="center" valign="top">
					<img src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/loading.gif" alt="loading" border="0"/>
				</td>
			</tr>
		</table>
	</div>   
	
  
  	<div id="credentials" style="display:none;">
		<logic:present name="permission" scope="session">
		<div id="logoutbox">
			<%@include file="logoutbox.jsp" %>
		</div>
		</logic:present>																			                                               
		<logic:notPresent name="permission" scope="session">
		<div id="loginbox">
			<%@include file="loginbox.jsp" %>
		</div>
		</logic:notPresent>
	</div>

	<table border="0" cellpadding="0" cellspacing="0" width="100%">
	  <tr>
		<td align="center" valign="top">
		  <table border="0" cellpadding="0" cellspacing="0" width="1050">
  			<tr>
			  <td rowspan="2" width="10"><img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/null.gif" height="1" width="10"/></td>
			  <td rowspan="1" width="10"><img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/header_left_top.png" width="10"/></td>
			  <td rowspan="1" width="287"><img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/man_top.png" usemap="#home1" width="287"/></td>
			  <td rowspan="1" align="right" background="/<page:config property="localPath"/>templates/iomedicoCurrent/images/blue_top.png" valign="top" width="100%">
				<div id="language_chooser">
					<page:language languages="de,en"/>
				</div>
			  </td>
			  <td rowspan="1" width="10"><img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/header_right_top.png"/></td>
			  <td rowspan="2" width="10"><img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/null.gif" height="1" width="10"/></td>
  			</tr>
  			<tr>
			  <td rowspan="1" width="10"><img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/header_left.png" width="10"/></td>
			  <td rowspan="1"><img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/man_bottom.png" usemap="#home2"/></td>
			  <td rowspan="1" bgcolor="#d6e3f4"><img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/header_topw.png"/></td>
			  <td rowspan="1"><img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/header_right.png"/></td>
  			</tr>
		  </table>
		  <table border="0" cellpadding="0" cellspacing="0" width="1050" height="100px">
			<tr>
			  <td rowspan="1" width="10"><img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/null.gif" height="1" width="10"/></td>
			  <td rowspan="1" background="/<page:config property="localPath"/>templates/iomedicoCurrent/images/b_l.png" width="10"><img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/null.gif" height="1" width="10"/></td>
			  <td rowspan="1" bgcolor="#d6e3f4" valign="bottom" width="285"><img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/m_1w.png"/></td>
			  <td rowspan="1" bgcolor="#d6e3f4" valign="bottom" width="34"><img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/m_3w.png"/></td>
			  <td rowspan="1" bgcolor="#d6e3f4" width="100%">
			  </td>
			  <td rowspan="3" background="/<page:config property="localPath"/>templates/iomedicoCurrent/images/b_r.png" width="10">
			  	<img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/null.gif" height="1" width="10"/>
			  </td>
			  <td rowspan="3" width="10"><img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/null.gif" height="1" width="10"/></td>
			</tr>
			<tr>
			  <td width="20" rowspan="2" colspan="2" valign="top"><img alt=""  src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/l_3w.png" /></td>
			  <td rowspan="2" bgcolor="#ffffff" width="285"><center><img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/iologo_studien_portal.png"/></center></td>
			  <td rowspan="1" height="17" valign="top"><img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/m_4w.png"/></td>
			  <td rowspan="1" background="/<page:config property="localPath"/>templates/iomedicoCurrent/images/m_5w.png" height="17">
			  	&nbsp;
			  </td>
			</tr>
			<tr>
			  <td rowspan="1" bgcolor="#ffffff" valign="top" width="34"><img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/null.gif" height="66" width="34"/></td>
			  <td rowspan="1" bgcolor="#ffffff" valign="top" width="100%">
				<div id="breadcrumb"></div>
			  </td>
			</tr>

		  </table>

		  <!-- NAVBAR-->
		  <table border="0" cellpadding="0" cellspacing="0" width="1050" align="center">
			<tr>
			  <td rowspan="3" width="10"><img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/null.gif" height="1" width="10"/></td>
			  <td rowspan="1" background="/<page:config property="localPath"/>templates/iomedicoCurrent/images/b_1.png" width="10">
			  <img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/b_1.png" height="10" width="10"/></td>
			  <td colspan="2" bgcolor="#ffffff" width="285px" valign="top">
				<div id="leftboxlist">
					<div id="leftbox">					
						<table border="0" cellpadding="0" cellspacing="0" width="250">
							<tr>
								<td bgcolor="#f69731" width="150"><span class="lb_title">Hauptmen&uuml;</span></td>
								<td><img
									src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/lb1.png" /></td>
								<td bgcolor="#ffffff"><img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/null.gif"
									height="17" width="70" /></td>
							</tr>
							<tr>
								<td colspan="3" bgcolor="#f69731"><img alt=""
									src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/null.gif"
									height="2" width="250" /></td>
							</tr>
							<tr>
								<td colspan="3" align="left" bgcolor="#f0f2f4" valign="top"	width="250" valign="top">
									 
									<!-- BEGIN MENU-->
									
									<page:xmenu 					
										forceLevelIndent="true"
										orientation="vertical"
										type="studiesportal"
										startNode="Menu"
										exclude="Menu/Registrieren"
										selected="<%= request.getParameter(\"nodeName\")%>"
										mode="website">
									</page:xmenu>

									<!-- END MENU-->
								
								</td>
							</tr>
							<tr>
								<td colspan="3" bgcolor="#f69731"><img alt=""
									src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/null.gif"
									height="2" width="250" /></td>
							</tr>
						</table>
					</div>
					
					<br/>
					
					<logic:present name="permission" scope="session">
					<div id="leftbox">					
						<table border="0" cellpadding="0" cellspacing="0" width="250">
							<tr>
								<td bgcolor="#f69731" width="150"><span class="lb_title">Meine	Studien</span></td>
								<td><img
									src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/lb1.png" /></td>
								<td bgcolor="#ffffff"><img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/null.gif"
									height="17" width="70" /></td>
							</tr>
							<tr>
								<td colspan="3" bgcolor="#f69731"><img alt=""
									src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/null.gif"
									height="2" width="250" /></td>
							</tr>
							<tr>
								<td colspan="3" align="left" bgcolor="#f0f2f4" valign="top"	width="250" valign="top">
									 
									<!-- BEGIN MENU-->
									
									<page:xmenu 					
										forceLevelIndent="true"
										orientation="vertical"
										type="studiesportal"
										exclude="Menu,Beschreibungen,Roche,Module,Weitere Studien"
										selected="<%= request.getParameter(\"nodeName\")%>"
										mode="website">
									</page:xmenu>

									<!-- END MENU-->
								
								</td>
							</tr>
							<tr>
								<td colspan="3" bgcolor="#f69731"><img alt=""
									src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/null.gif"
									height="2" width="250" /></td>
							</tr>
						</table>
					</div>
					
					<br/>
					
					<div id="leftbox">					
						<table border="0" cellpadding="0" cellspacing="0" width="250">
							<tr>
								<td bgcolor="#f69731" width="150"><span class="lb_title">Weitere Studien</span></td>
								<td><img
									src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/lb1.png" /></td>
								<td bgcolor="#ffffff"><img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/null.gif"
									height="17" width="70" /></td>
							</tr>
							<tr>
								<td colspan="3" bgcolor="#f69731"><img alt=""
									src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/null.gif"
									height="2" width="250" /></td>
							</tr>
							<tr>
								<td colspan="3" align="left" bgcolor="#f0f2f4" valign="top"	width="250" valign="top">
									 
									<!-- BEGIN MENU-->
									
									<page:xmenu 					
										forceLevelIndent="true"
										orientation="vertical"
										type="studiesportal"
										startNode="Weitere Studien"
										selected="<%= request.getParameter(\"nodeName\")%>"
										mode="website">
									</page:xmenu>

									<!-- END MENU-->
								
								</td>
							</tr>
							<tr>
								<td colspan="3" bgcolor="#f69731"><img alt=""
									src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/null.gif"
									height="2" width="250" /></td>
							</tr>
						</table>
					</div>					
					
					<br/>
					
					<div id="leftbox">					
						<table border="0" cellpadding="0" cellspacing="0" width="250">
							<tr>
								<td bgcolor="#f69731" width="150"><span class="lb_title">Module</span></td>
								<td><img
									src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/lb1.png" /></td>
								<td bgcolor="#ffffff"><img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/null.gif"
									height="17" width="70" /></td>
							</tr>
							<tr>
								<td colspan="3" bgcolor="#f69731"><img alt=""
									src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/null.gif"
									height="2" width="250" /></td>
							</tr>
							<tr>
								<td colspan="3" align="left" bgcolor="#f0f2f4" valign="top"	width="250" valign="top">
									 
									<!-- BEGIN MENU-->
									
									<page:xmenu 					
										forceLevelIndent="true"
										orientation="vertical"
										type="studiesportal"
										startNode="Module"
										selected="<%= request.getParameter(\"nodeName\")%>"
										mode="website">
									</page:xmenu>

									<!-- END MENU-->
								
								</td>
							</tr>
							<tr>
								<td colspan="3" bgcolor="#f69731"><img alt=""
									src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/null.gif"
									height="2" width="250" /></td>
							</tr>
						</table>
					</div>					
					
					</logic:present>
					
				</div>			  
			  </td>
			  <td colspan="2" bgcolor="#ffffff" width="100%" valign="top">
				<div class="heading">
					<h1>&nbsp;<page:pathway exclude="Menu" separator="/"/></h1>
					<div class="hr"></div>
				</div>
				<div class="content">					
					<bean:write name="MainpageForm" property="content.contentstring" filter="false"/>
					<logic:notEmpty name="MainpageForm" property="foundNodes"> 
					<p>
						<logic:iterate id="foundNode" name="MainpageForm" property="foundNodes" type="org.pmedv.pojos.Node">
							<a href="<%= foundNode.getPath() %>.html" target="_self"><%= foundNode.getName() %></a><br/>
						</logic:iterate>
					<p>
					</logic:notEmpty>
				</div>
			  </td>
			  <td rowspan="1" background="/<page:config property="localPath"/>templates/iomedicoCurrent/images/b_r.png" width="10"><img alt="" src="/<page:config property="localPath"/>/templates/iomedicoCurrent/images/null.gif" height="1" width="10"/></td>
			  <td rowspan="3" width="10"><img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/null.gif" height="1" width="10"/></td>
			</tr>
		  </table>

		  <table border="0" cellpadding="0" cellspacing="0" width="1050">
			<tr>
			  <td rowspan="3" width="10"><img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/null.gif" height="1" width="10"/></td>
			  <td rowspan="1" background="/<page:config property="localPath"/>templates/iomedicoCurrent/images/b_1.png" width="10">
			  <img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/b_1.png" height="10" width="10"/></td>
			  <td colspan="2" bgcolor="#ffffff" width="285"><img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/null.gif" height="1" width="285"/></td>
			  <td colspan="2" bgcolor="#ffffff" width="100%"><img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/null.gif" height="1" width="1"/></td>
			  <td rowspan="1" background="/<page:config property="localPath"/>templates/iomedicoCurrent/images/b_r.png" width="10"><img alt="" src="/<page:config property="localPath"/>/templates/iomedicoCurrent/images/null.gif" height="1" width="10"/></td>
			  <td rowspan="3" width="10"><img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/null.gif" height="1" width="10"/></td>
			</tr>
			<tr>
			  <td rowspan="1" background="/<page:config property="localPath"/>templates/iomedicoCurrent/images/b_2.png" width="10">
			  	<img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/b_2.png" height="17" width="10"/></td>
			  <td rowspan="1" background="/<page:config property="localPath"/>templates/iomedicoCurrent/images/b_3.png" width="10">
			  	<img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/null.gif" height="1" width="10"/></td>
			  <td colspan="1" background="/<page:config property="localPath"/>templates/iomedicoCurrent/images/b_5.png" height="17" width="285"></td>
			  <td colspan="1" bgcolor="#ffffff" valign="bottom" width="35"><img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/b_6w.png"/></td>
			  <td colspan="1" bgcolor="#ffffff" width="100%"><img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/null.gif" height="1" width="1"/></td>
			  <td rowspan="1" background="/<page:config property="localPath"/>templates/iomedicoCurrent/images/b_10.png" width="10">
			  	<img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/null.gif" height="1" width="10"/>
			  </td>
			</tr>
			<tr>
			  <td colspan="3" bgcolor="#39387b" width="305"><img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/null.gif" height="1" width="10"/></td>
			  <td colspan="1" width="35"><img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/b_7.png"/></td>
			  <td colspan="1" background="/<page:config property="localPath"/>templates/iomedicoCurrent/images/b_8.png" width="100%">
			  	<img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/null.gif" height="1" width="1"/></td>
			  <td rowspan="1" background="/<page:config property="localPath"/>templates/iomedicoCurrent/images/b_9.png" width="10">
			  <img alt="" src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/null.gif" height="1" width="10"/></td>
			</tr>
		  </table>
		</td>
	  </tr>
	</table>
	<div id="footer">
		<a class="footer" href="http://www.weberknechtcms.org" target="_blank">WeberknechtCMS</a> - <b>Version 3.1.2.iOMEDICO</b>
		<br><br>
	</div>

	<map name="home1" id="home1">
	  <area shape="poly" coords="134,70,133,60,102,70,89,70,141,48,147,24,158,27,160,35,155,46,209,30,216,38,199,38,160,53,155,70" href="http://<page:config property="hostname"/>/<page:config property="localPath"/><page:config property="startnode"/>.html" alt="" />
	</map>
	<map name="home2" id="home2">
	  <area shape="poly" coords="83,39,91,36,129,44,149,26,214,37,232,27,216,29,152,13,154,0,133,0,125,34,92,27" href="http://<page:config property="hostname"/>/<page:config property="localPath"/><page:config property="startnode"/>.html" alt="" />
	</map>
	<%
		if (request.getSession().getAttribute("login") != null) {
			String message = (String)request.getSession().getAttribute("login");
			if (message.equalsIgnoreCase("failed")) {							
			%>
				<script type="text/javascript">
					alert("Login failed.");
				</script>
			<%
			request.getSession().setAttribute("login", "");
			}
		}
	%>


  </body>
</html:html>
