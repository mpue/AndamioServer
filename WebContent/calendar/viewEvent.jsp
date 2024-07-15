<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/pagetags" prefix="page" %>
<%@ page import="org.pmedv.web.ServerUtil"%>
<%@ page import="org.pmedv.pojos.calendar.Event"%>
<%@ page import="java.*" %>
<%@ page import="java.text.SimpleDateFormat" %>

<html:html xhtml="true">

	<%
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");	
		Event event = (Event)request.getSession().getAttribute("event");	
	%>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Events</title>	
		<link href="/<page:config property="localPath"/>/calendar/css/style.css" rel="stylesheet" type="text/css" />
		<link rel='stylesheet' type='text/css' href="/<page:config property="localPath"/>jscripts/window/window.css" />
    	<link rel="stylesheet" type="text/css" href="/<page:config property="localPath"/>jscripts/jcal/css/jscal2.css" />
    	<link rel="stylesheet" type="text/css" href="/<page:config property="localPath"/>jscripts/jcal/css/border-radius.css" />
    	<link rel="stylesheet" type="text/css" href="/<page:config property="localPath"/>jscripts/jcal/css/steel/steel.css" />

		<script type="text/javascript" src="/<page:config property="localPath"/>scripts/prototype.js"></script>
		<script type="text/javascript" src="/<page:config property="localPath"/>scripts/scriptaculous.js"></script>		
		<script type="text/javascript" src="/<page:config property="localPath"/>scripts/contextMenu.js"></script>
		<script type="text/javascript" src="/<page:config property="localPath"/>scripts/window/window.js"></script>
	    <script type="text/javascript" src="/<page:config property="localPath"/>jscripts/jcal/js/jscal2.js"></script>
    	<script type="text/javascript" src="/<page:config property="localPath"/>jscripts/jcal/js/lang/en.js"></script>

		<script type="text/javascript"><!--
			
			var data;
		
			function init() {
				getEvent(<%= request.getParameter("id")%>);
				new Draggable('eventDetails', { scroll: window });
			}			


			
			// user clicked participate
			// TODO : Validate that a name and at least one date was given
			function participate(id)  {
				
				var dates = document.getElementsByName("new_selectDate");				
				var participant = $('participant').value;
				var dateParam = "";
				
				for (var i=0;i < dates.length;i++) {
					
					if (dates[i].id.substr(0,4) == "date" && dates[i].checked) {
						
						if (dates[i].value) {
							dateParam += dates[i].value;
							dateParam += ",";
						}

					}
				}
				
				dateParam = dateParam.substr(0,dateParam.length-1);
				
				var url ="/andamio/EventAction.do?do=participate&id="+id;				
				var oOptions = {
				    method: "post",
				    parameters: "participant="+participant+"&dates="+dateParam,
				    onSuccess: function (oXHR, oJson) {
				    	$('participants').value ='';
				    	getEvent(id);
				    },
				    onFailure: function (oXHR, oJson) {
				        alert("Could not create event.");
				    }
				};
				
				var oRequest = new Ajax.Request(url, oOptions);	

			}
			// get the event from the server
			function getEvent(id) {
				
				var table = document.getElementById('participants');
				if (table)
					$('eventDetails').removeChild(table);
				
				var url = '/andamio/EventAction.do?do=getEvent&id='+id;
				
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
				
				var e = data.evalJSON();
				var eventDetails = $('eventDetails');
				
				if (!e) {
					eventDetails.innerHTML = 'event is empty.';
					return;
				}
				
				// make a new table
				var table = document.createElement("table");
				table.id = 'participants';
				table.className = 'dates';
				
				
				// create the title row containing the dates
				
				var dates = "";
				var tr = table.insertRow(0);							    
				
				// dummy cell on the left
				var td = tr.insertCell(0);
				td.className = 'date';
				td.innerHTML = '&nbsp;';
				
		    	for(var i = 0;i < e.possibleDates.length;i++) {
		    		var dateString =  e.possibleDates[i].date.date+"."+(e.possibleDates[i].date.month+1)+"."+(e.possibleDates[i].date.year+1900);
					td = tr.insertCell(i+1);   
					td.className = 'date';
			        td.innerHTML = dateString;
		    	}
				// dummy cell on the right							    
				td = tr.insertCell(e.possibleDates.length+1);
				td.className = 'date';
				td.innerHTML = '&nbsp;';
				
				// now build the checkboxes for each participant
				for(var i = 0;i < e.participants.length;i++) {
				
					var trp = table.insertRow(1+i);				
					var tdName = trp.insertCell(0);
					tdName.className = 'date';
					tdName.innerHTML = e.participants[i].lastname;
					
					for(var j = 0;j < e.possibleDates.length;j++) {
						var tdp =  trp.insertCell(j+1);
						var dateString =  e.possibleDates[j].date.date+"."+(e.possibleDates[j].date.month+1)+"."+(e.possibleDates[j].date.year+1900);
						
						var match = false;
						// now we need to find the possible dates of the participant that match with the possible dates
						for (var k = 0; k < e.participants[i].possibleDates.length;k++) {							
							var d = e.participants[i].possibleDates[k].date.date+"."+(e.participants[i].possibleDates[k].date.month+1)+"."+(e.participants[i].possibleDates[k].date.year+1900);							
							if (d == dateString) {
								match = true;
								break;
							}
							
						}
						// found a match, mark it checked
						if (match)						
							tdp.innerHTML = '<input type="checkbox" id="date_'+dateString+'" value="'+dateString+'" name="selectDate" checked="checked" disabled="disabled"/>';
						else
							tdp.innerHTML = '<input type="checkbox" id="date_'+dateString+'" value="'+dateString+'" name="selectDate" disabled="disabled" />';
						tdp.className = 'date';
					}
					// another blank column on the right side
					var blank = trp.insertCell(e.possibleDates.length+1);
					blank.className = 'date';
					blank.innerHTML = '&nbsp;';


				}
				
				// now build the row for a new participant
		    	var tr1 = table.insertRow(e.participants.length+1);		    	
		    	var inputBox = '<input type="text" id="participant" name="participant" size="20"/>';
				var td1 = tr1.insertCell(0);
				td1.innerHTML = inputBox;
		    	// the checkboxes for the selectable dates
		    	for(var i = 0;i < e.possibleDates.length;i++) {		    		
		    		var dateString =  (e.possibleDates[i].date.year+1900)+"-"+(e.possibleDates[i].date.month+1)+"-"+e.possibleDates[i].date.date
					td1 = tr1.insertCell(i+1);   
					td1.className = 'date';
					td1.innerHTML = '<input type="checkbox" id="date_'+dateString+'" value="'+dateString+'" name="new_selectDate"/>'; 
		    	}
							
				var td2 = tr1.insertCell(e.possibleDates.length+1);   
				td2.className = 'date';
				td2.innerHTML = '<input type="button" name="submit" onclick="participate(<%= request.getParameter("id")%>);" value="Participate"/>'; 
		    	
				// fill the page with information about the event
			    $('eventDetails').appendChild(table);				
			    $('title').innerHTML = '<h1>Event details for '+e.title+'</h1>';
			    $('location').innerHTML = '<h3>Location : '+e.location+'</h3>';
			    $('description').innerHTML = '<p>Description : '+e.description+'</p>';
			    // mark the optimal matching column if there is any
			    findOptimalMatch(e);
			}				
		
			/**
			 * Finds the column which matches for all participants and changes the style
			 * in ordor to colorize this column.
			 */
			
			 // TODO: What if multiple columns match ???
			function findOptimalMatch(e) {
				var table = document.getElementById("participants");				
				var best = 0;
				var col  = 0; 
				
				// find the column with the most hits
				for (var i = 0; i < e.possibleDates.length;i++) {
					
					var count = 0;
					
					for (var j = 1; j < table.rows.length-1;j++) {						
						var row = table.rows[j];
						var cell = row.cells[i];
						if (cell.firstChild.checked) {
							count++;							
						}
					}
					
					if (count > best) {
						best = count;
						col = i-1;
					}
				}				
				// if the column contains a match for all participants mark it.
				if (best == e.participants.length && e.participants.length >= 2) {				
					for (var j = 1; j < table.rows.length-1;j++) {						
						var row = table.rows[j];
						var cell = row.cells[col+1];
						cell.className = 'match';
					}					
				}
				
			}
			
			
		--></script>
		
	</head>
	<body onload="init();">
		<a href="calendar/events.jsp">Back to event list</a>
		<div id="title"></div>
		<div id="location"></div>
		<div id="description"></div>
		<div id="eventDetails">
		</div>			   			 			
	</body>
	
</html:html>