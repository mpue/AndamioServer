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
		<title>Faqs</title>	
		<link href="css/style.css" rel="stylesheet" type="text/css" />
		<link rel='stylesheet' type='text/css' href="/<page:config property="localPath"/>jscripts/window/window.css" />
    	<link rel="stylesheet" type="text/css" href="/<page:config property="localPath"/>jscripts/jcal/css/jscal2.css" />
    	<link rel="stylesheet" type="text/css" href="/<page:config property="localPath"/>jscripts/jcal/css/border-radius.css" />
    	<link rel="stylesheet" type="text/css" href="/<page:config property="localPath"/>jscripts/jcal/css/win2k/win2k.css" />

		<script type="text/javascript" src="/<page:config property="localPath"/>scripts/prototype.js"></script>
		<script type="text/javascript" src="/<page:config property="localPath"/>scripts/scriptaculous.js"></script>		
		<script type="text/javascript" src="/<page:config property="localPath"/>scripts/contextMenu.js"></script>
		<script type="text/javascript" src="/<page:config property="localPath"/>scripts/window/window.js"></script>
	    <script type="text/javascript" src="/<page:config property="localPath"/>jscripts/jcal/js/jscal2.js"></script>
    	<script type="text/javascript" src="/<page:config property="localPath"/>jscripts/jcal/js/lang/en.js"></script>

		<script type="text/javascript">
		
		var admin = false;
		
		function checkAdmin() {
			
			var url = '/andamio/admin/ListUsers.do?do=getUsergroupsAsJSON';
			
			new Ajax.Request(url, {
				method: 'get',
				onComplete: function(transport) {
					if (200 == transport.status) {
				    	var responseText = transport.responseText;
				  		var groups = responseText.evalJSON();
				  		
				  		for (var i = 0; i < groups.length;i++) {
				  			if (groups[i].name == 'admin') {
				  				admin = true;
				  				break;
				  			}
				  		}
				  		getFaqs();
				  		
				  	}
				  	else {
						alert("An error occured during connect.\n\nPlease check the server settings.\n\nError code : "+transport.status);
				  	}
		  	   	}
			});				
			
		}
		
		function getFaqs() {
			
			var table = document.getElementById('faqs');
			if (table)
				$('faqList').removeChild(table);
			
			var url = '/andamio/FaqAction.do?do=getFaqs';
			
			new Ajax.Request(url, {
				method: 'get',
				onComplete: function(transport) {
					if (200 == transport.status) {
				    	data = transport.responseText;
				  		processData();					    
				  	}
				  	else {
						alert("An error occured during connect.\n\nPlease check the server settings.\n\nError code : "+transport.status);
				  	}
		  	   	}
			});				
			
		}
		
		function createFaq_() {
			var question = $('question').value;
			alert(question);
			var answer  = $('answer').value;
			var url ="/andamio/FaqAction.do?do=createFaq&question="+question+"&answer="+answer;				
			new Ajax.Request(url, {
				method: 'get',
				onComplete: function(transport) {
					if (200 == transport.status) {
						$('question').value = "";
						$('answer').value = "";
						getFaqs();					  		
				  	}
				  	else {
						alert("An error occured during connect.\n\nPlease check the server settings.\n\nError code : "+transport.status);
				  	}
		  	   	}
			});	
		}
		
		function deleteFaq(id) {
			
			var url = '/andamio/FaqAction.do?do=deleteFaq&id='+id;
			
			new Ajax.Request(url, {
				method: 'get',
				onComplete: function(transport) {
					if (200 == transport.status) {
						getFaqs();					  		
				  	}
				  	else {
						alert("An error occured during connect.\n\nPlease check the server settings.\n\nError code : "+transport.status);
				  	}
		  	   	}
			});				
			
		}
		
		function processData() {
			
			var faqs = eval(data); 				
			var faqList = $('faqList');
			
			if (faqs.length == 0) {
				faqList.innerHTML = 'no faqs found.';
				return;
			}
			var table = document.createElement("table");
			table.id = 'faqs';
			
			var tr = table.insertRow(0);
			tr.className = 'eventTitle';
			var td1 = tr.insertCell(0);      
	        td1.innerHTML = 'Question';
			var td2 = tr.insertCell(1);      
	        td2.innerHTML = 'Answer';
			
			if (admin) {
	 			var td3 = tr.insertCell(2);      
	 			td3.innerHTML = '';				
			}
				
		    for(i = 0;i < faqs.length;i++) {
				var tr = table.insertRow(i+1);
				
				if (i%2 == 0)
					tr.className = 'odd';
				else
					tr.className = 'even';
				
				var td1 = tr.insertCell(0);      
		        td1.innerHTML = faqs[i].question;
				var td2 = tr.insertCell(1);      
		        td2.innerHTML = faqs[i].answer;
		        
		        if (admin) {
					var td3 = tr.insertCell(2);      
			        td3.innerHTML = '<a href="#" onclick="deleteFaq('+faqs[i].id+')">delete</a>';			        	
		        }
		        
			}
		    $('faqList').appendChild(table);
			
		}
		
		function hideDiv(id) {
			Effect.Fade(id, { duration: 0.3 });
		}
		function showDiv(id) {
			Effect.Appear(id, { duration: 0.3 });
		}	
		
		</script>
		
	</head>
	<body onload="checkAdmin();">
		<h2>Faq list</h2>
		<p>
			<input type="button" value="Create faq" onclick="showDiv('faqDiv');"/>
		</p>
		<div id="faqList">
		</div>		
		<div id="faqDiv" style="display:none">
			<table border="0" align="center" cellspacing="0" cellpadding="0px" width="100%" height="400px">
				<tr height="35px">
					<td align="left" colspan="2" valign="top">
						<div class="dialogTitleFaq" id="dialogTitleFaq">Create new faq</div>
					</td>
				</tr>
				<tr valign="top" height="30px">
					<td align="center" colspan="2">
						<div class="dialogSubtitleFaq">
						Please enter the details for the new faq
						</div>
					</td>
				</tr>
				<tr height="160px">
					<td align="center" valign="top" colspan="2">
						<div id="faqForm">
							<table border="0" cellpadding="5px" cellspacing="0" align="center" width="95%">
								<tr>
									<td>
										Question
									</td>
									<td colspan="2">
										<textarea rows="3" cols="20" name="question" id="question" class="formInput"></textarea>
									</td>
								</tr>
								<tr>
									<td>
										Answer
									</td>
									<td colspan="2">
										<textarea rows="3" cols="20" name="answer" id="answer" class="formInput"></textarea>
									</td>
								</tr>													
							</table>						
						</div>
					</td>
				</tr>
				<tr>
					<td align="center" width="50%">
						<input type="button" value="Ok" onclick="hideDiv('faqDiv');createFaq_();" class="formButton">
					</td>
					<td align="center" width="50%">
						<input type="button" value="Cancel" onclick="hideDiv('faqDiv');" class="formButton">
					</td>					
				</tr>			
			</table>
		</div>   	
	</body>
	
</html:html>