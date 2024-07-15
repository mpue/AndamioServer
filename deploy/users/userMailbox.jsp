<%@ page 
	language="java" 
	contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" 
%>
<%@page import="org.pmedv.pojos.*"%>
<%@page import="org.pmedv.beans.objects.DataType"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.pmedv.context.AppContext"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="java.util.MissingResourceException"%>
<%@page import="org.pmedv.forms.UserMailboxForm"%><html>

<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/tags/struts-bean"  prefix="bean" %>
<%@ taglib uri="/tags/struts-html"  prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/pagetags" prefix="page" %>

<%
	ResourceBundle resources = ResourceBundle.getBundle("MessageResources");
	UserMailboxForm form = (UserMailboxForm)request.getAttribute("UserMailboxForm");

%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Benutzerprofil f&uuml;r <bean:write name="UserMailboxForm" property="user.name"/></title>

 
<link rel="stylesheet" href="style.css" type="text/css"/>
<link rel="stylesheet" href="../jscripts/window/window.css" type="text/css" />
<link rel="stylesheet" href="/<page:config property="localPath"/>users/templates/<page:config property="template"/>/css/template.css" type="text/css">
<link rel="icon" href="/<page:config property="localPath"/>templates/<page:config property="template"/>/favicon.ico" type="image/x-icon">
<link rel="shortcut icon" href="/<page:config property="localPath"/>templates/<page:config property="template"/>/favicon.ico" type="image/x-icon"> 
<script type="text/javascript" src="../jscripts/window/window.js"></script>
<script type="text/javascript" src="../jscripts/common.js"></script>
<script type="text/javascript" src="../jscripts/tiny_mce/tiny_mce.js"></script>
<script type="text/javascript" src="/<page:config property="localPath"/>scripts/prototype.js"></script>
<script type="text/javascript" src="/<page:config property="localPath"/>scripts/scriptaculous.js"></script>		
 


<script type="text/javascript">
var messageWindow;
var viewMessageWindow;

var init = false;

var left = (window.screen.width / 2) - (320 + 10);
var top  = (window.screen.height / 2) - 340;

function getMessage(url) {
	if (window.XMLHttpRequest) { // Non-IE browsers
		req = new XMLHttpRequest();
			
		if (req.overrideMimeType) {
           	req.overrideMimeType('text/xml');
		}
                    
      	req.onreadystatechange = readyStateChange;
      	try {
        	req.open("GET", url, true);
		} 
		catch (e) {
			alert(e);
		}

      	req.send(null);

    } 
    else if (window.ActiveXObject) { // IE
	   	req = new ActiveXObject("Microsoft.XMLHTTP");

    	if (req) {
	        req.onreadystatechange = readyStateChange;
       		req.open("GET", url, true);
	        req.send();
	    }
    }
}
  	
function readyStateChange() {
	if (req.readyState == 4) { // Complete
    	if (req.status == 200) { // OK response        	

        	var myObject = eval('(' + req.responseText + ')');

	    	viewMessageWindow = dhtmlwindow.open('divbox', 'div', 'viewMessageDiv', 'Nachricht anzeigen', 'width=640px,height=480px,left='+left+'px,top='+top+'px,resize=1,scrolling=1');
	    	// tinyMCE.execCommand('mceRemoveControl',false,'_message');
	    	document.getElementById('_subject').value = myObject.subject;
	    	document.getElementById('_from').value = myObject.from;
	    	document.getElementById('_message').value = myObject.body;
	    	// tinyMCE.execCommand('mceAddControl',false,'_message');
   		}
   		else {
   			alert("Ein Fehler ist aufgetreten, macht aber nichts.");
    	}
	}
}

function confirmDelete(messageId) {

	if (confirm("Sind Sie sicher sicher, dass Sie die Nachricht entfernen wollen?"))
		document.location.href = 'MailboxAction.do?do=deleteMessage&message_id='+messageId;
	
}

function showMessage(messageId) {
	var messageUrl =  'MailboxAction.do?do=showMessage&message_id='+messageId;
	getMessage(messageUrl); 	
}

function writeMessage() {
		
	messageWindow = dhtmlwindow.open('divbox', 'div', 'messageDiv', 'Nachricht schreiben', 'width=640px,height=480px,left='+left+'px,top='+top+'px,resize=1,scrolling=1');
	initAutoComplete();
	// tinyMCE.execCommand('mceAddControl',false,'message');

}

function sendMessage() {

	var subject = document.messageForm.subject.value;
	var receiver = document.messageForm.receiver.value;
	var message = document.messageForm.message.value;
	
	var poststr = "subject=" + subject+ "&receiver="+receiver+"&message="+message;
	makePOSTRequest('MailboxAction.do?do=sendMessage&', poststr);

	messageWindow.hide();
	
}

function replyMessage(id) {
	
	var subject = document.forms["messageForm_"+id].subject.value;
	var receiver = document.forms["messageForm_"+id].receiver.value;
	var message = document.forms["messageForm_"+id].message.value;
	var message_id = document.forms["messageForm_"+id].message_id.value;
	
	var poststr = "subject=" + subject+ "&receiver="+receiver+"&message="+message+"&message_id="+message_id;
	makePOSTRequest('MailboxAction.do?do=replyMessage&', poststr);
	
}

function initAutoComplete() {
	new Ajax.Autocompleter("receiver", "autocomplete_choices", "/andamio/forum/Mainpage.do?do=autoComplete", {
		  paramName: "search", 
		  minChars: 2, 
	});				
}

tinyMCE.init({
	language : "de",
	theme : "advanced",
	plugins : "emotions,advimage",
	// theme_advanced_disable : "styleselect,formatselect",
	theme_advanced_buttons1_add : "undo,redo,emotions,image",		
	theme_advanced_buttons2 : "link,unlink,image,bullist,numlist,indent,outdent,undo,redo",
	theme_advanced_buttons3 : "",					
	theme_advanced_toolbar_location : "top",
	theme_advanced_toolbar_align : "center",
	theme_advanced_resizing : false,
	theme_advanced_statusbar_location : 'bottom',
	theme_advanced_resize_horizontal : false,
	apply_source_formatting : false,
	auto_reset_designmode : false,
	force_br_newlines : true,
	remove_linebreaks : false, 
	force_p_newlines : false,	
	relative_urls : false,
	content_css : "style.css",
	convert_urls : false	
});

</script>

</head>
<body onLoad="">
	
	<div id="messageDiv" style="display:none">
			
			<form id="messageForm" name="messageForm">
			
			<table border="0" align="center" width="90%" cellpadding="10px">
				<tr>
					<td colspan="2">
						Hier k&ouml;nnen Sie Ihre Nachricht verfassen:
					</td>
				</tr>
				<tr>
					<td>
						Empf&auml;nger
					</td>
					<td>
						<input type="text" size="50" name="receiver" id="receiver"/>
						<div id="autocomplete_choices" class="autocomplete"></div>
					</td>
				</tr>
				<tr>
					<td>
						Betreff
					</td>
					<td>
						<input type="text" size="50" name="subject"/>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						Nachricht
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<textarea rows="12" cols="70" name="message" id="message"></textarea>						
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<input type="button" value="Abschicken" onClick="sendMessage();"/>
					</td>
				</tr>
			</table>

		</form>		
	</div>
	
	<div id="viewMessageDiv" style="display:none">
			
		<table border="0" align="center" width="90%" cellpadding="10px">
			<tr>
				<td>
					Von
				</td>
				<td>
					<input type="text" size="50" name="_from" id="_from"/>
				</td>
			</tr>

			<tr>
				<td>
					Betreff
				</td>
				<td>
					<input type="text" size="50" name="_subject" id="_subject"/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					Nachricht
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<textarea rows="12" cols="70" name="_message" id="_message"></textarea>						
				</td>
			</tr>
		</table>
			
	</div>
	
	<div id="logo"></div>
	<table class="userProfile" align="center">
		<tr class="profileTitle">
			<td colspan="5" valign="middle" class="userProfile" valign="middle">
				Ihre Post - <%= resources.getString(form.getCurrentFolder().getName()) %>
			</td>
		</tr>
		<tr class="odd">
			<td width="20%" class="linkPanel">
				<div class="clear">
					<a class="button" href="#" onclick="this.blur();document.location.href='../forum/Mainpage.do?do=showForums';return false;"><span>Foren&uuml;bersicht</span></a>
				</div>												
			</td>
			<td width="20%">
				<div class="clear">
					<a class="button" href="#" onclick="this.blur();document.location.href='UserAction.do?do=showProfile&user_id=<%= form.getUser().getId() %>';return false;"><span>Profil</span></a>
				</div>												
			</td>
			<td width="20%">
				<div class="clear">
					<a class="button" href="#" onclick="this.blur();writeMessage();return false;"><span>Nachricht schreiben</span></a>
				</div>						    			   
			</td>					
			<td width="20%">
				&nbsp;
			</td>
			<td width="20%">
				&nbsp;
			</td>				
		</tr>
		<tr class="userProfile">
			<td colspan="1" valign="top" class="userProfile">
				<h2>Ordner</h2>			
				<logic:iterate id="folder" name="UserMailboxForm" property="folder" type="org.pmedv.beans.objects.FolderBean">
					<a href="MailboxAction.do?do=showFolder&name=<bean:write name="folder" property="name"/>">
					<%= resources.getString(folder.getName()) %>
					<br/></a>		
				</logic:iterate>
			</td>
			<td colspan="4" valign="top" class="userProfile">
				<h2>Nachrichten</h2>
				
				<display:table id="row" 
						name="UserMailboxForm.messages" 
						class="messages" 					
						decorator="org.pmedv.decorator.MessageDecorator"	
						requestURI="/users/MailboxAction.do" 
						pagesize="10">
						
					<display:column property="messageLink" title="Betreff" sortable="true" class="subject" />
					<display:column property="from" title="Von" sortable="true" class="from"/>
					<display:column property="receivedDate" title="Empfangsdatum" sortable="true" class="received"/>
					<display:column property="deleteLink" title="" sortable="false" class="deleteLink"/>
					
					<display:setProperty name="paging.banner.placement" value="bottom" />	
					<display:setProperty name="paging.banner.full">
						<hr size="1" width="90%" align="left"/>
					 	<span class="pagelinks"> [<a href="{1}">Erste</a>/ <a href="{2}">Vorige</a>] {0} [ <a href="{3}">N&auml;chste</a>/ <a href="{4}">Letzte </a>]</span>
					</display:setProperty>
					<display:setProperty name="paging.banner.first">
						<hr size="1" width="90%" align="left"/>
						<span class="pagelinks"> [Erste/Vorige] {0} [ <a href="{3}">N&auml;chste</a>/ <a href="{4}">Letzte</a>] </span>
					</display:setProperty>	
					<display:setProperty name="paging.banner.last">
						<hr size="1" width="90%" align="left"/>
						<span class="pagelinks">[ <a href="{1}">Erste</a>/ <a href="{2}">Vorige</a>] {0} [N&auml;chste/Letzte] </span>
					</display:setProperty>
					<display:setProperty name="paging.banner.item_name">
						Nachricht		
					</display:setProperty>
					<display:setProperty name="paging.banner.items_name">
						Nachrichten
					</display:setProperty>
					<display:setProperty name="paging.banner.no_items_found">
						<span class="pagebanner"> Keine {0} gefunden. </span>
					</display:setProperty>
					<display:setProperty name="paging.banner.one_item_found">
						<span class="pagebanner"> Eine {0} gefunden. </span>
					</display:setProperty>
					<display:setProperty name="paging.banner.all_items_found">
						<span class="pagebanner"> {0} {1} gefunden.</span>  	
					</display:setProperty>
					<display:setProperty name="paging.banner.some_items_found">
						<span class="pagebanner"> {0} {1} gefunden, zeige {2} bis {3}. </span>  	
					</display:setProperty>
					<display:setProperty name="basic.msg.empty_list">
						<span class="pagebanner"> Keine Nachrichten gefunden.</span>
					</display:setProperty>
					
				</display:table>				

			</td>
		</tr>
		<tr class="odd">
			<td colspan="5" class="linkPanel">
				&nbsp;			
			</td>
		</tr>

	</table>
	
</body>
</html>