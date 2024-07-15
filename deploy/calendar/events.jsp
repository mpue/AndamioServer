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
		<title>Events</title>	
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
		
			var data;
			var cal;
			var lastEventId;
			
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
					  		getEvents();
					  		
					  	}
					  	else {
							alert("An error occured during connect.\n\nPlease check the server settings.\n\nError code : "+transport.status);
					  	}
			  	   	}
				});				
				
			}
			
			function deleteEvent(id) {
				
				var url = '/andamio/EventAction.do?do=deleteEvent&id='+id;
				
				new Ajax.Request(url, {
					method: 'get',
					onComplete: function(transport) {
						if (200 == transport.status) {
					  		getEvents();					  		
					  	}
					  	else {
							alert("An error occured during connect.\n\nPlease check the server settings.\n\nError code : "+transport.status);
					  	}
			  	   	}
				});				
				
			}
			
			function getEvents() {
				
				var table = document.getElementById('events');
				if (table)
					$('eventList').removeChild(table);
				
				var url = '/andamio/EventAction.do?do=getEvents';
				
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

			function processData() {
				
				var events = eval(data); 				
				var eventList = $('eventList');
				
				if (events.length == 0) {
					eventList.innerHTML = 'no events found.';
					return;
				}
				var table = document.createElement("table");
				table.id = 'events';
				
				var tr = table.insertRow(0);
				tr.className = 'eventTitle';
				var td1 = tr.insertCell(0);      
		        td1.innerHTML = 'Title';
				var td2 = tr.insertCell(1);      
		        td2.innerHTML = 'Location';
				var td3 = tr.insertCell(2);      
		        td3.innerHTML = 'Description';
				var td4 = tr.insertCell(3);      
		        td4.innerHTML = 'User';
				var td5 = tr.insertCell(4);      
				td5.innerHTML = '';
				
				if (admin) {
					var td5 = tr.insertCell(5);      
					td5.innerHTML = '';					
				}
					
			    for(i = 0;i < events.length;i++) {
					var tr = table.insertRow(i+1);
					
					if (i%2 == 0)
						tr.className = 'odd';
					else
						tr.className = 'even';
					
					var td1 = tr.insertCell(0);      
			        td1.innerHTML = events[i].title;
					var td2 = tr.insertCell(1);      
			        td2.innerHTML = events[i].location;
					var td3 = tr.insertCell(2);      
			        td3.innerHTML = events[i].description;
					var td4 = tr.insertCell(3);      
			        td4.innerHTML = events[i].username;
					var td5 = tr.insertCell(4);      
			        td5.innerHTML = '<a href="/andamio/EventAction.do?do=viewEvent&id='+events[i].id+'">view</a>'
	                
			        if (admin) {
						var td6 = tr.insertCell(5);      
				        td6.innerHTML = '<a href="#" onclick="deleteEvent('+events[i].id+')">delete</a>';			        	
			        }
			        
				}
			    $('eventList').appendChild(table);
				
			}	
			
			function createEvent_() {
				var title = $('title').value;
				var location  = $('location').value;
				var description = $('description').value;
				var dates = $('dates').value;
				var url ="/andamio/EventAction.do?do=createEvent";				
				var oOptions = {
				    method: "post",
				    parameters: "title="+title+"&location="+location+"&description="+description+"&dates="+dates,
				    onSuccess: function (oXHR, oJson) {
				    	var response = oXHR.responseText;
				    	var nEvent = response.evalJSON();
				    	lastEventId = nEvent.id;
				    	prepareInvitations(lastEventId);
				    	$('title').value ='';
				    	$('location').value = '';
				    	$('description').value = '';
				    	$('dates').value = '';

				    },
				    onFailure: function (oXHR, oJson) {
				        alert("Could not create event.");
				    }
				};
				
				var oRequest = new Ajax.Request(url, oOptions);	

			}			
			       
			function hideDiv(id) {
				Effect.Fade(id, { duration: 0.3 });
			}
			function showDiv(id) {
				Effect.Appear(id, { duration: 0.3 });
			}						
			
			function init() {
				new Draggable('eventDiv', { scroll: window });
				new Draggable('inviteDiv', { scroll: window });
			    cal = Calendar.setup({
			        trigger    : "calendar-trigger",
			        weekNumbers   : true,
			        selectionType : Calendar.SEL_MULTIPLE,
			        selection     : Calendar.dateToInt(new Date()),				        
			        inputField : "dates"
			    });
				
			}
			
			function prepareInvitations(id) {
				lastEventId = id;
				showDiv('inviteDiv');				
			}
			
			function createInvitations() {
				
				var addresses = $('addresses').value;
				var url ="/andamio/EventAction.do?do=sendInvitations&id="+lastEventId;				
				var oOptions = {
				    method: "post",
				    parameters: "&addresses="+addresses,
				    onSuccess: function (oXHR, oJson) {				    	
				    	var response = oXHR.responseText.evalJSON();			
				    	if (response.success) {
				    		showMessage('Success','Invitation successful',response.message);	
				    	}
				    	else {
				    		alert(response.message);
				    	}
				    	
				    	$('title').value ='';
				    	
				    },
				    onFailure: function (oXHR, oJson) {
				        alert("Could not create event.");
				    }
				};
				
				var oRequest = new Ajax.Request(url, oOptions);					
				
			}
			
			function showMessage(title,subtitle,text) {			
				$('messageTitle').innerHTML = title;
				$('messageSubTitle').innerHTML = subtitle;
				$('messageForm').innerHTML = text;				
				showDiv('messageDiv');
			}
			
			function showEvent(id) {
				document.location.href = '/andamio/EventAction.do?do=viewEvent&id='+id;				
			}

		</script>
		
	</head>
	<body onload="checkAdmin();init();">	
		<logic:notPresent name="permission" scope="session">
			You must be logged in in order to see the event list.
		</logic:notPresent>		
		<logic:present name="permission" scope="session">
			<a href="/andamio/calendar/index.jsp" target="calendarFrame">Show calendar</a>
		
			<h2>Event list</h2>
			<p>
				<input type="button" value="Create event" onclick="showDiv('eventDiv');"/>
			</p>
			<div id="eventList">
			</div>			
			<div id="eventDiv" style="display:none">
				<table border="0" align="center" cellspacing="0" cellpadding="0px" width="100%" height="400px">
					<tr height="35px">
						<td align="left" colspan="2" valign="top">
							<div class="dialogTitleEvent" id="dialogTitleEvent">Create new event</div>
						</td>
					</tr>
					<tr valign="top" height="30px">
						<td align="center" colspan="2">
							<div class="dialogSubtitleEvent">
							Please enter the details for the new event
							</div>
						</td>
					</tr>
					<tr height="160px">
						<td align="center" valign="top" colspan="2">
							<div id="eventForm">
								<table border="0" cellpadding="5px" cellspacing="0" align="center" width="95%">
									<tr>
										<td>
											Title
										</td>
										<td>
											<input type="text" size="25" name="title" id="title" class="formInput"/>										
										</td>
									</tr>
									<tr>
										<td>
											Location
										</td>
										<td>
											<input type="text" size="25" name="location" id="location" class="formInput"/>										
										</td>
									</tr>
									<tr valign="top">
										<td>
											Available dates:
										</td>
										<td>
											<div id="availableDates">
												<input type="text" id="dates" name="dates"/><button id="calendar-trigger">...</button>											
											</div>			
										</td>
									</tr>				
																									
									<tr>
										<td colspan="2">
											Description
										</td>
									</tr>				
									<tr>
										<td colspan="2">
											<textarea rows="5" cols="50" name="description" id="description" class="formInput"></textarea>
										</td>
									</tr>															
								</table>						
							</div>
						</td>
					</tr>
					<tr>
						<td align="center" width="50%">
							<input type="button" value="Ok" onclick="hideDiv('eventDiv');createEvent_();" class="formButton">
						</td>
						<td align="center" width="50%">
							<input type="button" value="Cancel" onclick="hideDiv('eventDiv');" class="formButton">
						</td>					
					</tr>			
				</table>
			</div>   	
			<div id="inviteDiv" style="display:none">
				<table border="0" align="center" cellspacing="0" cellpadding="0px" width="100%" height="300px">
					<tr height="35px">
						<td align="left" colspan="2" valign="top">
							<div class="dialogTitleEvent" id="dialogTitleEvent">Send invitations</div>
						</td>
					</tr>
					<tr valign="top" height="30px">
						<td align="center" colspan="2">
							<div class="dialogSubtitleEvent">
							Please enter the email adresses for invitations. 
							</div>
						</td>
					</tr>
					<tr height="130px">
						<td align="center" valign="top" colspan="2">
							<div id="invitationForm">
								<textarea rows="10" cols="50" name="addresses" id="addresses" class="formInput"></textarea>
							</div>
						</td>
					</tr>
					<tr>
						<td align="center" width="50%">
							<input type="button" value="Ok" onclick="hideDiv('inviteDiv');createInvitations();" class="formButton">
						</td>
						<td align="center" width="50%">
							<input type="button" value="Cancel" onclick="hideDiv('inviteDiv');" class="formButton">
						</td>					
					</tr>			
				</table>
			</div>   	
			<div id="messageDiv" style="display:none">
				<table border="0" align="center" cellspacing="0" cellpadding="0px" width="100%" height="200px">
					<tr height="35px">
						<td align="left" colspan="2" valign="top">
							<div class="messageTitle" id="messageTitle"></div>
						</td>
					</tr>
					<tr valign="top" height="30px">
						<td align="center" colspan="2">
							<div id="messageSubTitle" class="messageSubTitle"> 
							</div>
						</td>
					</tr>
					<tr height="100px">
						<td align="center" valign="top" colspan="2">
							<div id="messageForm">							
							</div>
						</td>
					</tr>
					<tr>
						<td align="center" width="50%">
							<input type="button" value="Ok" onclick="hideDiv('messageDiv');showEvent(lastEventId);" class="formButton">
						</td>
						<td align="center" width="50%">
							<input type="button" value="Cancel" onclick="hideDiv('messageDiv');" class="formButton">
						</td>					
					</tr>			
				</table>
			</div> 					 
		</logic:present>
	
					 
	</body>
	
</html:html>