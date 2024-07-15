/**

	WeberknechtCMS - Open Source Content Management
	Written and maintained by Matthias Pueski 
	
	Copyright (c) 2003-2011 Matthias Pueski
	
	This program is free software; you can redistribute it and/or
	modify it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 2
	of the License, or (at your option) any later version.
	
	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

 */

/**
 * This is the main calendar script. No classes, no bounds but who cares.
 */

var selectedDay = 0;
var selectedAppointment = 0;
var selectedMonth = 0;
var selectedYear = 0;
var selectedHour = null;

var url;
var params;
var month;
var year;
var day;

var dialogTitle = 'Create new appointment on ';

var defaultColor = "#D1CACA";
var sundayColor = "#FF7878";

var default2Color = "#EEEEEE";
var sunday2Color = "#FCA9A9";

var whiteColor = "#FFFFFF";
var blueColor = "#3769F2";

var newColor;

var selectedColor = "#AAAAAA";
var selectedSundayColor = "#F43333";
var selectedAppointmentColor = "#B78484";

var currentCalendar;

var dayNames = new Array(7);

dayNames[0] = 'Sunday';
dayNames[1] = 'Monday';
dayNames[2] = 'Tuesday';
dayNames[3] = 'Wednesday';
dayNames[4] = 'Thursday';
dayNames[5] = 'Friday';
dayNames[6] = 'Saturday';

var monthNames = new Array(12);

monthNames[0] = 'January';
monthNames[1] = 'February';
monthNames[2] = 'March';
monthNames[3] = 'April';
monthNames[4] = 'May';
monthNames[5] = 'June';
monthNames[6] = 'July';
monthNames[7] = 'August';
monthNames[8] = 'September';
monthNames[9] = 'October';
monthNames[10] = 'November';
monthNames[11] = 'December';

var minutes = new Array(2);
minutes[0] = '00';
minutes[1] = '30';

/**
 * creates a div with an id a given width , height, absolute position a border
 * and optional html content.
 * 
 * @param id
 *            the id of the div inside the DOM
 * @param divWidth
 *            the width of the div
 * @param divHeight
 *            the height of the div
 * @param divLeft
 *            the absolute left of the div
 * @param the
 *            absolute top of the div
 * @param border
 *            should this div have a border?
 * @param html
 *            the optional html content
 */

function createDiv(id, divWidth, divHeight, divLeft, divTop, border, html) {

	var newdiv = document.createElement('div');
	newdiv.setAttribute('id', id);

	if (divWidth) {
		newdiv.style.width = divWidth;
	}

	if (divHeight) {
		newdiv.style.height = divHeight;
	}

	if ((divLeft || divTop) || (divLeft && divTop)) {
		newdiv.style.position = "absolute";

		if (divLeft) {
			newdiv.style.left = divLeft;
		}

		if (divTop) {
			newdiv.style.top = divTop;
		}
	}

	if (border)
		newdiv.style.border = "1px solid #000";

	if (html) {
		newdiv.innerHTML = html;
	} else {
		newdiv.innerHTML = "";
	}

	return newdiv;
}

function createHolidayText(text) {
	var holiDayText = document.createElement('div');
	holiDayText.setAttribute('class', 'holiday');
	holiDayText.innerHTML = text;
	return holiDayText;
}

/**
 * Gives the index of the first day of the month
 */
function getMonthBeginDay(month, year) {
	return new Date(year, month - 1, 1).getDay();
}

function getWeekBeginDay(year, month, day) {
	var currentDate = new Date(year, month, day);
	return currentDate.getDate() - currentDate.getDay();
}

/**
 * Checks if a year is a leap year
 */
function isLeap(year) {
	return ((year % 4 == 0) && !(year % 100 == 0)) || (year % 400 == 0);
}

/**
 * Gives the lenght of a given month in a specific year regarding if it is a
 * leap year or not.
 */
function getLengthOfMonth(month, year) {

	var length = 0;

	switch (month) {
	case 1:
	case 3:
	case 5:
	case 7:
	case 8:
	case 10:
	case 12:
		return 31;
	case 4:
	case 6:
	case 9:
	case 11:
		return 30;
	case 2:
		if (isLeap(year))
			return 29;
		else
			return 28;
	}

	return length;

}
/**
 * Triggers the dialog for creating a new appointment
 */
function createAppointment() {
	var startTimeSelect = $('startTime');
	var endTimeSelect = $('endTime');
	var appColorSelect = $('appColor');
	appColorSelect.style.background = "#04B404";
	appColorSelect.value = "#04B404";
	var index;
	if (selectedHour) {
		index = selectedHour.split(":")[0] * 2;
		if (selectedHour.split(":")[1] == "30")
			index++;
	}else{
		index = 16;
	}
	
	startTimeSelect.selectedIndex = index;
	endTimeSelect.selectedIndex = index+2;
	var date = Math.abs(selectedDay) + "." + selectedMonth + "."
			+ selectedYear;
	$('calendarDate').value = date;
	$('dialogTitle').innerHTML = dialogTitle + date;
	$('dialogSubtitle').innerHTML = 'Please enter the details for the new appointment';
	$('currentDate').innerHTML = date;
	closeContext();
	hideDiv('divContext');
	showDiv('appointmentDiv');
	$('appSubmit').onclick = submitForm;
	$('appCancel').onclick = hideAppointmentDiv;
}

/**
 * Edits an existing appointment
 * 
 * @param id
 */
function editAppointment(id) {

	var url = "http://##HOSTNAME##/##LOCALPATH##CalendarAction.do?do=getAppointment&appointment_id="
			+ id;

	new Ajax.Request(
			url,
			{
				method : 'get',
				onComplete : function(transport) {
					if (200 == transport.status) {
						var responseText = transport.responseText;
						var appointment = responseText.evalJSON();

						var start = appointment.start;
						var end = appointment.end;

						var starttime = start.hours;
						starttime += ":";
						if (start.minutes < 10)
							starttime += '0';
						starttime += start.minutes;

						var endtime = end.hours;
						endtime += ":";
						if (end.minutes < 10)
							endtime += '0';
						endtime += end.minutes;

						selectOptionByValue($('startTime'), starttime);
						selectOptionByValue($('endTime'), endtime);
						var newYear = appointment.start.year+1900;
						var newMonth = appointment.start.month+1;
						
						$('calendarDate').value = appointment.start.date + "." + newMonth + "." + newYear;

						$('dialogTitle').innerHTML = 'Edit appointment';
						$('dialogSubtitle').innerHTML = 'Please enter the new details for the appointment';
						$('shortDescription').value = appointment.shortDescription;
						$('longDescription').value = appointment.longDescription;
						$('appSubmit').onclick = saveAppointment;
						$('appCancel').onclick = hideAppointmentDiv;
						$('appointmentId').value = appointment.id;
						$('appColor').style.background = appointment.color;
						$('appColor').value = appointment.color;
						showDiv('appointmentDiv');
					} else {
						alert("An error occured during connect.\n\nPlease check the server settings.\n\nError code : "
								+ transport.status);
					}
				}
			});
}

function hideAppointmentDiv() {
	hideDiv('appointmentDiv');
	$('shortDescription').value = '';
	$('longDescription').value = '';
	$('currentDate').innerHTML = '';
}

function hideCalendarDiv() {
	hideDiv('calendarDiv');
	$('name').value = '';
	$('description').value = '';
}

function selectOptionByValue(selObj, val) {
	var A = selObj.options, L = A.length;
	while (L) {
		if (A[--L].value == val) {
			selObj.selectedIndex = L;
			L = 0;
		}
	}
}

function moveAppointment(id, startTime, endTime, day, month, year) {
	var url = "http://##HOSTNAME##/##LOCALPATH##CalendarAction.do?do=moveAppointment";
	var oOptions = {
		method : "post",
		parameters : "appointment_id=" + id + "&startTime=" + startTime + "&endTime=" + endTime + "&day=" + day + "&month=" + month
				+ "&year=" + year,
		onSuccess : function(oXHR, oJson) {
			clearAppointments();
			getAppointments();
		},
		onFailure : function(oXHR, oJson) {
			alert("Could not move appointment.");
		}
	};
	var oRequest = new Ajax.Request(url, oOptions);
}

function deleteAppointment() {
	var url = "http://##HOSTNAME##/##LOCALPATH##CalendarAction.do?do=deleteAppointment";
	var oOptions = {
		method : "post",
		parameters : "appointment_id=" + selectedAppointment,
		onSuccess : function(oXHR, oJson) {
			closeContext();
			hideDiv('divContext');
			clearAppointments();
			getAppointments();
		},
		onFailure : function(oXHR, oJson) {
			alert("Could not delete appointment.");
		}
	};
	var oRequest = new Ajax.Request(url, oOptions);
}

function getCalendarWeek(date) {
	var thisWeek = "";
	var firstOfJanuary = new Date(date.getFullYear(), 0, 1);
	firstOfJanuary = firstOfJanuary.getTime() - (firstOfJanuary.getDay() + 1)
			* (24 * 60 * 60 * 1000);
	thisWeek = Math.ceil((date.getTime() - firstOfJanuary)
			/ (7 * 24 * 60 * 60 * 1000)) - 1;
	return thisWeek;
}

function createNewCalendar() {
	$('calendarDialogTitle').innerHTML = 'Create new calendar';
	$('calendarDialogSubtitle').innerHTML = 'Please enter the details for the new calendar';
	showDiv('calendarDiv');
	$('calendarAppSubmit').onclick = submitCalendarForm;
	$('calendarAppCancel').onclick = hideCalendarDiv;
}

function center(id) {
	var pageX = (document.all) ? document.body.offsetWidth : window.width;
	var pageY = (document.all) ? document.body.offsetHeight : window.height;
	var objRef = document.getElementById(id);
	var objW = objRef.offsetWidth;
	var objH = objRef.offsetHeight;
	objRef.style.left = ((pageX / 2) - (objW / 2)) + "px";
	objRef.style.top = ((pageY / 2) - (objH / 2)) + "px";
}

function getAppointments() {

	var data;
	var url = 'http://##HOSTNAME##/##LOCALPATH##CalendarAction.do?do=getAppointments&username=##USERNAME##';

	new Ajax.Request(
			url,
			{
				method : 'get',
				onComplete : function(transport) {
					if (200 == transport.status) {
						data = transport.responseText;
						processAppointments(data);
					} else {
						alert("An error occured during connect.\n\nPlease check the server settings.\n\nError code : "
								+ transport.status);
					}
				}
			});

}

function submitForm() {

	hideDiv('appointmentDiv');

	var shortDesc = $('shortDescription').value;
	var longDesc = $('longDescription').value;
	var color = $('appColor').value;

	var startTimeSelect = $('startTime');
	var startTime = startTimeSelect[startTimeSelect.selectedIndex].value;

	var endTimeSelect = $('endTime');
	var endTime = endTimeSelect[endTimeSelect.selectedIndex].value;

//	var date = +Math.abs(selectedDay) + "." + selectedMonth + "."
//			+ selectedYear;
	
	var date = $('calendarDate').value;
	var url = "http://##HOSTNAME##/##LOCALPATH##CalendarAction.do?do=createAppointment&username=##USERNAME##";

	var oOptions = {
		method : "post",
		parameters : "shortDescription=" + shortDesc + "&longDescription="
				+ longDesc + "&date=" + date + "&startTime=" + startTime
				+ "&endTime=" + endTime+"&color="+color,
		onSuccess : function(oXHR, oJson) {
			clearAppointments();
			getAppointments();
			$('shortDescription').value = '';
			$('longDescription').value = '';
			$('currentDate').innerHTML = '';
		},
		onFailure : function(oXHR, oJson) {
			alert("Could not create appointment.");
		}
	};

	var oRequest = new Ajax.Request(url, oOptions);

}

function submitCalendarForm() {

	hideDiv('calendarDiv');

	var name = $('name').value;
	var description = $('description').value;

	var url = "http://##HOSTNAME##/##LOCALPATH##CalendarAction.do?do=createCalendar&username=##USERNAME##";

	var oOptions = {
		method : "post",
		parameters : "name=" + name + "&description=" + description,
		onSuccess : function(oXHR, oJson) {
			$('name').value = '';
			$('description').value = '';
			document.location.href = location.href;
		},
		onFailure : function(oXHR, oJson) {
			alert("Could not create calendar.");
		}
	};

	var oRequest = new Ajax.Request(url, oOptions);

}

function saveAppointment() {

	hideDiv('appointmentDiv');

	var shortDesc = $('shortDescription').value;
	var longDesc = $('longDescription').value;
	var id = $('appointmentId').value;
	var startTimeSelect = $('startTime');
	var startTime = startTimeSelect[startTimeSelect.selectedIndex].value;
	var endTimeSelect = $('endTime');
	var endTime = endTimeSelect[endTimeSelect.selectedIndex].value;
	var color = $('appColor').value;
	var date = $('calendarDate').value;

	var url = "http://##HOSTNAME##/##LOCALPATH##CalendarAction.do?do=saveAppointment&appointment_id="
			+ id;

	var oOptions = {
		method : "post",
		parameters : "shortDescription=" + shortDesc + "&longDescription="
				+ longDesc + "&date=" + date + "&startTime=" + startTime + "&endTime=" + endTime+"&color="+color,
		onSuccess : function(oXHR, oJson) {
			clearAppointments();
			getAppointments();
			$('shortDescription').value = '';
			$('longDescription').value = '';
			$('currentDate').innerHTML = '';
		},
		onFailure : function(oXHR, oJson) {
			alert("Could not save appointment.");
		}
	};

	var oRequest = new Ajax.Request(url, oOptions);

}

function initDrag() {
	new Draggable('appointmentDiv', {
		scroll : window
	});
}
function hideDiv(id) {
	Effect.Fade(id, {
		duration : 0.3
	});
}
function showDiv(id) {
	Effect.Appear(id, {
		duration : 0.3
	});
}

function createTimes(selectId) {
	var select = $(selectId);
	for (i = 0; i < 48; i += 2) {
		select.options[i] = new Option(i / 2 + ':' + minutes[0], i / 2 + ':'
				+ minutes[0]);
		select.options[i + 1] = new Option(i / 2 + ':' + minutes[1], i / 2
				+ ':' + minutes[1]);
	}
}

function init() {
	
	url = location.href.split("?")[0].replace('#', '');
	params = location.href.split("?")[1];

	var week;
	if (params) {
		day = parseInt(params.split(".")[0]);
		month = parseInt(params.split(".")[1]);
		year = parseInt(params.split(".")[2]);
		week = getCalendarWeek(new Date(year, month - 1, day));
	} else {
		var date = new Date();
		day = date.getDate();
		month = date.getMonth() + 1;
		year = date.getYear() + 1900;
		week = getCalendarWeek(date);
	}
	
	if ($('useCalendar').options.length > 0){
		createCalendar(week, day, month, year, 120, 120);
		initContext();
		getAppointments();
		initDrag();
		createTimes('startTime');
		createTimes('endTime');
	}
	else{
		$('content').innerHTML = "You are not in a calendar group.";
	}
}

function isAdmin(){
	var url = 'http://##HOSTNAME##/##LOCALPATH##CalendarAction.do?do=isAdmin&username=##USERNAME##';
	new Ajax.Request(
			url,
			{
				method : 'get',
				onComplete : function(transport) {
					if (200 == transport.status) {
						if (transport.responseText == "false"){
							$('createCalendar').setAttribute("type", "hidden")
							$('shareCalendar').setAttribute("type", "hidden");
							$('useCalendarTD').setAttribute("style", "display:none");
						}
						
					} else {
						alert("An error occured during connect.\n\nPlease check the server settings.\n\nError code : "
								+ transport.status);
					}
				}
			});
}

function getCalendars() {
	var url = 'http://##HOSTNAME##/##LOCALPATH##CalendarAction.do?do=getCalendars&username=##USERNAME##';
	new Ajax.Request(
			url,
			{
				method : 'get',
				onComplete : function(transport) {
					if (200 == transport.status) {
						var responseText = transport.responseText;
						var calendars = responseText.evalJSON();
						if (calendars)
							getCurrentCalendar(calendars);
					} else {
						alert("An error occured during connect.\n\nPlease check the server settings.\n\nError code : "
								+ transport.status);
					}
				}
			});
}

function getCurrentCalendar(calendars) {
	var url = 'http://##HOSTNAME##/##LOCALPATH##CalendarAction.do?do=getCurrentCalendar';
	new Ajax.Request(
			url,
			{
				method : 'get',
				onComplete : function(transport) {
					if (200 == transport.status) {
						var responseText = transport.responseText;
						currentCalendar = responseText.evalJSON().data;
						var calendarSelect = $('useCalendar');
						for (i = 0; i < calendars.length; i++) {
							calendarSelect.options[i] = new Option(
									calendars[i].name, calendars[i].id);
							// alert(calendar);
							if (currentCalendar != null
									&& currentCalendar.id == calendars[i].id) {
								calendarSelect.selectedIndex = i;
							}
						}
						init();
					} else {
						alert("An error occured during connect.\n\nPlease check the server settings.\n\nError code : "
								+ transport.status);
					}
				}
			});
}

function selectCalendar() {
	var calendarSelect = $('useCalendar');
	var calendarId = calendarSelect.options[calendarSelect.selectedIndex].value;
	var url = 'http://##HOSTNAME##/##LOCALPATH##CalendarAction.do?do=selectCalendar&calendarId='
			+ calendarId;
	new Ajax.Request(
			url,
			{
				method : 'get',
				onComplete : function(transport) {
					if (200 == transport.status) {
						clearAppointments();
						getAppointments();
					} else {
						alert("An error occured during connect.\n\nPlease check the server settings.\n\nError code : "
								+ transport.status);
					}
				}
			});
}

function selectGroup() {
	var goupSelect = $('useGroup');
	var groupId = goupSelect.options[goupSelect.selectedIndex].value;
	var submitButton = $('shareCalendarAppSubmit');
	var userSelect = $('useUsers');
	while (userSelect.firstChild) {
		userSelect.removeChild(userSelect.firstChild);
	}
	if (groupId == "") {
		userSelect.disabled = true;
		submitButton.disabled = true;
	} else {
		var url = 'http://##HOSTNAME##/##LOCALPATH##CalendarAction.do?do=selectGroup&username=##USERNAME##&groupId='
				+ groupId;
		new Ajax.Request(
				url,
				{
					method : 'get',
					onComplete : function(transport) {
						if (200 == transport.status) {
							var responseText = transport.responseText;
							var users = responseText.evalJSON();
							userSelect.options[0] = new Option("All", "");
							for (i = 0; i < users.length; i++) {
								userSelect.options[i + 1] = new Option(
										users[i].name, users[i].id);
							}
							userSelect.selectedIndex = 0;
							userSelect.disabled = false;
							submitButton.disabled = false;
						} else {
							alert("An error occured during connect.\n\nPlease check the server settings.\n\nError code : "
									+ transport.status);
						}
					}
				});
	}
}

function submitShareCalendarForm() {
	var goupSelect = $('useGroup');
	var userSelect = $('useUsers');
	var groupId = goupSelect.options[goupSelect.selectedIndex].value;
	var userId = userSelect.options[userSelect.selectedIndex].value;

	var selected = new Array();
	for ( var i = 0; i < userSelect.options.length; i++) {
		if (i == 0 && userSelect.options[i].selected) {
			for ( var j = 1; j < userSelect.options.length; j++)
				selected.push(userSelect.options[j].value);
			break;
		} else if (i != 0)
			selected.push(userSelect.options[i].value);
	}
	var selectedJSONString = JSON.stringify(selected);
	var url = 'http://##HOSTNAME##/##LOCALPATH##CalendarAction.do?do=shareCalender&users='
			+ selectedJSONString;
	new Ajax.Request(
			url,
			{
				method : 'get',
				onComplete : function(transport) {
					if (200 == transport.status) {
						hideShareCalendarDiv();
					} else {
						alert("An error occured during connect.\n\nPlease check the server settings.\n\nError code : "
								+ transport.status);
					}
				}
			});
}

function hideShareCalendarDiv() {
	var submitButton = $('shareCalendarAppSubmit');
	var userSelect = $('useUsers');
	userSelect.disabled = true;
	submitButton.disabled = true;
	hideDiv('shareCalendarDiv');
}

function shareCalendar() {
	var url = 'http://##HOSTNAME##/##LOCALPATH##CalendarAction.do?do=getGroups&username=##USERNAME##';
	new Ajax.Request(
			url,
			{
				method : 'get',
				onComplete : function(transport) {
					if (200 == transport.status) {
						var responseText = transport.responseText;
						var groups = responseText.evalJSON();

						var groupSelect = $('useGroup');
						groupSelect.options[0] = new Option("-----", "");
						for (i = 0; i < groups.length; i++) {
							groupSelect.options[i + 1] = new Option(
									groups[i].name, groups[i].id);
						}

						$('shareCalendarDialogTitle').innerHTML = 'Share calendar';
						$('shareCalendarDialogSubtitle').innerHTML = 'Please choose users and groups to share the calendar';
						showDiv('shareCalendarDiv');
						$('shareCalendarAppSubmit').onclick = submitShareCalendarForm;
						$('shareCalendarAppCancel').onclick = hideShareCalendarDiv;
					} else {
						alert("An error occured during connect.\n\nPlease check the server settings.\n\nError code : "
								+ transport.status);
					}
				}
			});
}

function openMonth() {
	document.location.href = "http://##HOSTNAME##/##LOCALPATH##calendar/viewMonth.jsp?"
			+ Math.abs(selectedDay) + "." + selectedMonth + "." + selectedYear;
}

function openWeek() {
	document.location.href = "http://##HOSTNAME##/##LOCALPATH##calendar/viewWeek.jsp?"
			+ Math.abs(selectedDay) + "." + selectedMonth + "." + selectedYear;
}

function openDay() {
	document.location.href = "http://##HOSTNAME##/##LOCALPATH##calendar/viewDay.jsp?"
			+ Math.abs(selectedDay) + "." + selectedMonth + "." + selectedYear;
}

function checkHoliday(currentDay, currentMonth, currentYear, currentColor,
		dayName) {
	var text = null;
	// if (currentColor != sundayColor && currentColor != sunday2Color &&
	// currentColor != selectedSundayColor){
	if (currentDay == 1 && currentMonth == 1)
		text = 'Neujahrstag';
	else if (currentDay == 6 && currentMonth == 1)
		text = 'Heilige drei K&ouml;nige';
	else if (currentDay == 1 && currentMonth == 5)
		text = 'Tag der Arbeit';
	else if (currentDay == 15 && currentMonth == 8)
		text = 'Mari&auml; Himmelfahrt';
	else if (currentDay == 3 && currentMonth == 10)
		text = 'Tag der Deutschen Einheit';
	else if (currentDay == 31 && currentMonth == 10)
		text = 'Reformationstag';
	else if (currentDay == 1 && currentMonth == 11)
		text = 'Allerheiligen';
	else if (currentDay == 25 && currentMonth == 12)
		text = '1. Weihnachtsfeiertag';
	else if (currentDay == 26 && currentMonth == 12)
		text = '2 .Weihnachtsfeiertag';
	else if (currentMonth == 11 && currentDay < 23 && (currentDay + 8) > 23
			&& dayName == dayNames[3])
		text = 'Bu&szlig;- und Bettag';

	if (!text) {
		var easterDate = getEasterDay(currentYear);
		var currentDate = new Date(currentYear, currentMonth - 1, currentDay);
		// Set 1 day in milliseconds
		var one_day = 1000 * 60 * 60 * 24;
		if (Math.ceil((easterDate.getTime() - currentDate.getTime())
				/ (one_day)) == 2)
			text = 'Karfreitag';
		else if (Math.ceil((easterDate.getTime() - currentDate.getTime())
				/ (one_day)) == 0)
			text = 'Ostersonntag';
		else if (Math.ceil((currentDate.getTime() - easterDate.getTime())
				/ (one_day)) == 1)
			text = 'Ostermontag';
		else if (Math.ceil((currentDate.getTime() - easterDate.getTime())
				/ (one_day)) == 39)
			text = 'Christi Himmelfahrt';
		else if (Math.ceil((currentDate.getTime() - easterDate.getTime())
				/ (one_day)) == 49)
			text = 'Pfingstsonntag';
		else if (Math.ceil((currentDate.getTime() - easterDate.getTime())
				/ (one_day)) == 50)
			text = 'Pfingstmontag';
		else if (Math.ceil((currentDate.getTime() - easterDate.getTime())
				/ (one_day)) == 60)
			text = 'Fronleichnam';
	}
	// }
	if (text)
		setCurrentColor(currentColor);
	else
		newColor = currentColor;
	return text;
}

function setCurrentColor(currentColor) {
	if (currentColor == defaultColor)
		newColor = sundayColor;
	else if (currentColor == default2Color)
		newColor = sunday2Color;
	else if (currentColor == selectedColor)
		newColor = selectedSundayColor;
	else
		newColor = currentColor;
}

/**
 * Gets the date of easter sunday for a given year
 * 
 * @see <a
 *      href="http://de.wikipedia.org/wiki/Gau%C3%9Fsche_Osterformel">Wikipedia</a>
 * @param y
 *            the year to get the date for
 * 
 * @return the date on which is easter sunday
 */
function getEasterDay(y) {
	var c = Math.floor(y / 100);
	var n = y - 19 * Math.floor(y / 19);
	var k = Math.floor((c - 17) / 25);
	var i = c - Math.floor(c / 4) - Math.floor((c - k) / 3) + 19 * n + 15;
	i = i - 30 * Math.floor((i / 30));
	i = i
			- Math.floor(i / 28)
			* (1 - Math.floor(i / 28) * Math.floor(29 / (i + 1))
					* Math.floor((21 - n) / 11));
	var j = y + Math.floor(y / 4) + i + 2 - c + Math.floor(c / 4);
	j = j - 7 * Math.floor(j / 7);
	var l = i - j;
	var m = 3 + Math.floor((l + 40) / 44);
	var d = l + 28 - 31 * Math.floor(m / 4);

	return new Date(y, m - 1, d);
}

function getRandom( min, max ) {
	if( min > max ) {
		return( -1 );
	}
	if( min == max ) {
		return( min );
	}
 
        return( min + parseInt( Math.random() * ( max-min+1 ) ) );
}

function selectColor(color){
	var appColorSelect = $('appColor');
	appColorSelect.style.background = color
	appColorSelect.value = color;
	hideDiv('selectColorDiv');
}
