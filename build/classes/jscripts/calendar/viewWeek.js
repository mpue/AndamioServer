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
 * This is the calendar script for show week. No classes, no bounds but who
 * cares.
 */

var dayHours = new Array();
var appointmentArray = new Array();

var appointmentColors = new Array();

appointmentColors[0] = '#B0EA81';
appointmentColors[1] = '#EAE881';
appointmentColors[2] = '#81EACE';
appointmentColors[3] = '#81C3EA';
appointmentColors[4] = '#8199EA';
appointmentColors[5] = '#A581EA';
appointmentColors[6] = '#D781EA';
appointmentColors[7] = '#9019AA';
appointmentColors[8] = '#3D19AA';
appointmentColors[9] = '#196EAA';
appointmentColors[10] = '#19AAA6';
appointmentColors[11] = '#19AA45';
appointmentColors[12] = '#62AA19';
appointmentColors[13] = '#88AA19';
appointmentColors[14] = '#AAA819';
appointmentColors[15] = '#F2EE13';

var hours = new Array();

hours[0] = '06:00';
hours[1] = '06:30';
hours[2] = '07:00';
hours[3] = '07:30';
hours[4] = '08:00';
hours[5] = '08:30';
hours[6] = '09:00';
hours[7] = '09:30';
hours[8] = '10:00';
hours[9] = '10:30';
hours[10] = '12:00';
hours[11] = '12:30';
hours[12] = '13:00';
hours[13] = '13:30';
hours[14] = '14:00';
hours[15] = '14:30';
hours[16] = '15:00';
hours[17] = '15:30';
hours[18] = '16:00';
hours[19] = '16:30';
hours[20] = '17:00';
hours[21] = '17:30';
hours[22] = '18:00';
hours[23] = '18:30';
hours[24] = '19:00';

/**
 * creates a calendar grid based on a given day, a given month and a given year
 * 
 * @param width
 *            the with of a panel for each day
 * @param height
 *            the height of a panel for each day
 */
function createCalendar(week, day, month, year, width, height) {
	var dateField = document.getElementById('currentDate');
	var value = monthNames[month - 1] + " " + year + " (" + week + ")";
	dateField.value = value;
	document.title = "View week for ##USERNAME## - " + value;
	height = height / 4.18;
	// the div to put the stuff into
	var contentDiv = document.getElementById("content");
	
	contentDiv.onmousedown = mouseDown;
	contentDiv.onmouseup   = doUp;
	contentDiv.onmousemove = doMove;
	
	while (contentDiv.hasChildNodes()) {
		contentDiv.removeChild(contentDiv.lastChild);
	}
	// start and end day of the current month
	var start = getWeekBeginDay(year, month-1, day);
	var end = getLengthOfMonth(month,year);
	// today ...
	var today = new Date();
	// header for the days
	for (var currentDay = 0; currentDay < 7; currentDay++) {
		var x = currentDay * width + 50;
		var y = 0;
//		var div = createDiv("dayheader_" + currentDay, width + "px", "25px", x + "px",
//				y + "px", true, start+currentDay+ dayNames[currentDay]);
		var div = createDiv("dayheader_" + currentDay, width + "px", "25px", x + "px",
				y + "px", true,dayNames[currentDay]);
		div.style.background = whiteColor;
		div.style.verticalAlign = 'center';
		div.style.lineHeight = '160%';
		div.style.fontWeight = 'bold';
		if (start + currentDay == today.getDate() && today.getMonth() == month - 1
				&& today.getYear() + 1900 == year) {
			div.style.color = blueColor;
		}
		div.style.textAlign = 'center';
		contentDiv.appendChild(div);
	}
	// check current calendar week
	var div = createDiv("hourheader", "49px", "25px", "0px", "25px", true, "");
	div.style.background = whiteColor;
	contentDiv.appendChild(div);
	// header for the weeks
	for ( var hour = 0; hour < hours.length; hour++) {
		var x = 0;
		var y = hour * height + 25;
		var div;
		if (hour % 2 == 0)
			div = createDiv("hourheader_" + hour, "50px", height + "px", x
					+ "px", y + "px", true, hours[hour]);
		else
			div = createDiv("hourheader_" + hour, "50px", height + "px", x
					+ "px", y + "px", true, "");
		div.style.background = whiteColor;
		div.style.verticalAlign = 'center';
		div.style.lineHeight = '200%';
		div.style.fontWeight = 'bold';
		div.style.textAlign = 'center';
		contentDiv.appendChild(div);
	}
	// wee need to counth the days
	var i = 1;
	// go through every week and day and create the grid
	for ( var hour = 0; hour < hours.length; hour++) {
		for ( var currentDay = 0; currentDay < 7; currentDay++) {
			// the x-coordinate of the layer being created
			var x = currentDay * width + 50;
			// the y-coordinate of the layer being created
			var y = hour * height + 25;
			// the layer we are going to create
			var div;
			// check if the current day is inside the current month, if not
			// create an empty layer
			var current1Day = start+currentDay;
			if (current1Day > 0 && current1Day <= end) {
				div = createDiv("hour_" +hours[hour] +"_day_" + current1Day + "_month_" + month
						+ "_year_" + year, width + "px", height + "px", x
						+ "px", y + "px", true, "");
			}
			else if (current1Day <= 0) {
				var previousMonth;
				var previousYear;
				if (month != 1) {
					previousMonth = month - 1;
					previousYear = year;
				} else {
					previousYear = year - 1;
					previousMonth = 12;
				}
				var currentPreviousMonthDay = getLengthOfMonth(
						previousMonth, previousYear)
						+ current1Day;
				div = createDiv("hour_" +hours[hour] +"_day_-" + currentPreviousMonthDay
						+ "_month_" + previousMonth + "_year_"
						+ previousYear, width + "px", height + "px", x
						+ "px", y + "px", true, "");
			} else {
				var nextMonth;
				var nextYear;
				if (month != 12) {
					nextMonth = month + 1;
					nextYear = year;
				} else {
					nextYear = year + 1;
					nextMonth = 1;
				}
				var currentNextMonthDay = current1Day - end;
				div = createDiv("hour_" +hours[hour] +"_day_+" + currentNextMonthDay + "_month_"
						+ nextMonth + "_year_" + nextYear, width + "px",
						height + "px", x + "px", y + "px", true,
						"");
			}
			// layer callbacks
			// div.onmousedown = mouseDown;

			// finally create div and put it into the global array
			contentDiv.appendChild(div);
			dayHours[i-1] = div;
			createDroppable(div.id);
			// count the days ...	
			i++;
		}
	}
	setDefaultColors(day);
}

function setDefaultColors(currentDay){
	// start and end day of the current month
	var start = getWeekBeginDay(year, month-1, currentDay);
	var end   = getLengthOfMonth(month,year);
	// set default color 
	var color = defaultColor;
	var color2 = default2Color;
	
	// wee need to counth the days
	var i = 1;
	
	for ( var hour = 0; hour < hours.length; hour++) {
		for ( var day = 0; day < 7; day++) {
			// determine color
			if (day == 0) {
				color = sundayColor;
				color2 = sunday2Color;
			}
			else {
				color = defaultColor;
				color2 = default2Color;
			}
			// check if the current day is inside the current month, if not create an empty layer
			var current1Day = start+day;
			if (current1Day > 0 && current1Day <= end) {
				checkHoliday(current1Day,month,year,color,dayNames[day]);
				dayHours[i-1].style.background = newColor;				
			}
			else if (current1Day <= 0){
				var previousMonth;
				var previousYear;
				if (month != 1){
					previousMonth = month-1;
					previousYear = year;
				}
				else{
					previousYear = year-1;
					previousMonth = 12;
				}
				var currentPreviousMonthDay = getLengthOfMonth(previousMonth,previousYear)+current1Day;
				checkHoliday(currentPreviousMonthDay,previousMonth,previousYear,color2,dayNames[day]);
				dayHours[i-1].style.background = newColor;
			}
			else{
				var nextMonth;
				var nextYear;
				if (month != 12){
					nextMonth = month+1;
					nextYear = year;
				}
				else{
					nextYear = year+1;
					nextMonth = 1;
				}
				var currentNextMonthDay = current1Day-end;
				checkHoliday(currentNextMonthDay,nextMonth,nextYear,color2,dayNames[day]);
				dayHours[i-1].style.background = newColor;
			}
		i++;
		}
	}
}

function nextWeek() {

	var monthLength = getLengthOfMonth(month, year);
	if ((day + 7) > monthLength) {
		if (month < 12) {
			month++;
		} else {
			month = 1;
			year++;
		}
		day = day + 7 - monthLength;
	} else {
		day = day + 7;
	}

	document.location.href = url + "?" + day + "." + month + "." + year;
}

function lastWeek() {

	if ((day - 7) < 1) {
		if (month > 1) {
			month--;
		} else {
			month = 12;
			year--;
		}
		var monthLength = getLengthOfMonth(month, year);
		day = day - 7 + monthLength;
	} else {
		day = day - 7;
	}

	document.location.href = url + "?" + day + "." + month + "." + year;
}

//callback for the mouse down event
function mouseDown(evt) {
	setDefaultColors(day);
	
	if (evt.target.id.indexOf('app') == -1) {
		
		selectedAppointment = 0;
		var selectedHourString = evt.target.id.split("_")[1];
		var selectedDayString = evt.target.id.split("_")[3];
		
		selectedHour = selectedHourString;
		selectedMonth =  parseInt(evt.target.id.split("_")[5]);
		selectedYear =  parseInt(evt.target.id.split("_")[7]);
		selectedDay = parseInt(selectedDayString);
		
		// start and end day of the current month
//		var start = getWeekBeginDay(selectedYear, selectedMonth-1, selectedDay);
//		var end = getLengthOfMonth(selectedMonth,selectedYear);
//		
//		var lengthPreviousMonth = getLengthOfMonth(selectedMonth-1,selectedYear);
//		
//		if (selectedDayString.indexOf('-')>-1)
//			selectedDayInt = Math.abs(selectedDay)+start-lengthPreviousMonth-1;
//		else if (selectedDayString.indexOf('+')>-1)
//			selectedDayInt = start+end+Math.abs(selectedDay)-1;
//		else
//			selectedDayInt = selectedDay;
		var selectedDayInt = new Date(selectedYear, selectedMonth-1, Math.abs(selectedDay)).getDay();
		if (selectedDayInt % 7 == 0)
			newColor = selectedSundayColor;
		else
			checkHoliday(Math.abs(selectedDay),selectedMonth,selectedYear,selectedColor,dayNames[(selectedDayInt % 7)]);
		for (var dayHour = 0; dayHour < dayHours.length; dayHour++){
			if (dayHours[dayHour].id.split("_")[3]==selectedDayString && dayHours[dayHour].id.split("_")[5]==selectedMonth && dayHours[dayHour].id.split("_")[7]==selectedYear)
				dayHours[dayHour].style.background = newColor;
		}
//		evt.target.style.background = newColor;
	}
	else {
		if (evt.target.id.indexOf('appH') > -1){
			selectedHour = evt.target.parentNode.id.split("_")[3];
			selectedMonth = evt.target.parentNode.id.split("_")[7];
			selectedYear = evt.target.parentNode.id.split("_")[9];
			selectedDay = evt.target.parentNode.id.split("_")[5];
		}else{
			selectedHour = evt.target.id.split("_")[3];
			selectedMonth = evt.target.id.split("_")[7];
			selectedYear = evt.target.id.split("_")[9];
			selectedDay = evt.target.id.split("_")[5];
		}
		
		selectedAppointment = parseInt(evt.target.id.split("_")[1]);
		doDown(evt);
	}
}

function clearAppointments() {
	var contentDiv = document.getElementById("content");
	for(var j=0; j < appointmentArray.length; j++) {
		var currentDiv = document.getElementById(appointmentArray[j]);
		if (currentDiv)
			contentDiv.removeChild(currentDiv);
	}
	appointmentArray.clear;
//	setDefaultColors(selectedDay);
}

function processAppointments(data) {
	var contentDiv = document.getElementById("content");		
	var appointments = eval(data);
	var appDayHours = new Hash();
	
	var appointmentLeft;
	var appointmentTop;
	var appointmentWidth;
	var appointmentHeight;
	var position;
	var currentDayHour;
	
	for(var j=0; j < appointments.length; j++) {
		appointments = eval(data); 	
		
		appointmentLeft=null;
		appointmentTop=null;
		appointmentWidth=null;
		appointmentHeight=0;
		currentDayHour = "";
		
		position = null;
		for (var dayHour = 0; dayHour < dayHours.length; dayHour++){
			var currentTime = dayHours[dayHour].id.split("_")[1];
			var currentDate = parseInt(dayHours[dayHour].id.split("_")[3]);
			var currentMonth = parseInt(dayHours[dayHour].id.split("_")[5]);
			var currentYear = parseInt(dayHours[dayHour].id.split("_")[7]);
			var appointment = appointments[j];
			if (appointment.start.date == Math.abs(currentDate) && appointment.start.month+1 == currentMonth 
					&& appointment.start.year+1900 == currentYear) {					
//				alert(appointment.start.hours);
//				alert(currentHour.split(":")[0]);
				var currentMinutes= parseInt((currentTime.split(":")[1]),10)+parseInt((currentTime.split(":")[0]*60),10);			
//				if (currentHour.indexOf("0") == 0)
//					currentHour = currentHour.replace("0","");
				var appointmentStartMinutes = appointment.start.hours*60+appointment.start.minutes;
				var appointmentEndMinutes = appointment.end.hours*60+appointment.end.minutes;
//				alert(currentMinutes);
//				alert(appointmentMinutes);
				if (appointmentStartMinutes<=currentMinutes && appointmentEndMinutes>=currentMinutes){				
					if (!appointmentLeft){
						appointmentLeft = parseFloat(dayHours[dayHour].style.left.substring(0, dayHours[dayHour].style.left.length-2));
						appointmentTop = parseFloat(dayHours[dayHour].style.top.substring(0, dayHours[dayHour].style.top.length-2));
						appointmentWidth = parseFloat(dayHours[dayHour].style.width.substring(0, dayHours[dayHour].style.width.length-2))/2;
					}
					appointmentHeight = parseFloat(appointmentHeight,10)
						+parseFloat(dayHours[dayHour].style.height.substring(0, dayHours[dayHour].style.height.length-2),10);
					if (!position){
						if (!appDayHours.get(dayHours[dayHour].id+"_1"))
							position = 1;
						else if (!appDayHours.get(dayHours[dayHour].id+"_2"))
							position = 2;
						else{
							position = 1;
							if (appDayHours.get(dayHours[dayHour].id+"_"+position) && appDayHours.get(dayHours[dayHour].id+"_2")){
								if (appDayHours.get(dayHours[dayHour].id+"_"+position)<appDayHours.get(dayHours[dayHour].id+"_2"))
									position = 2;
							}
						}
					}
					appDayHours.set(dayHours[dayHour].id+"_"+position, appointmentEndMinutes);
					currentDayHour = dayHours[dayHour].id;
					if (position==2 && parseFloat(appointmentLeft)
							==parseFloat(dayHours[dayHour].style.left.substring(0, dayHours[dayHour].style.left.length-2)))
						appointmentLeft = parseFloat(appointmentLeft)
							+parseFloat(dayHours[dayHour].style.width.substring(0, dayHours[dayHour].style.width.length-2))/2;
//					if (!appDayHours.get(dayHours[dayHour].id) || appDayHours.get(dayHours[dayHour].id) == 2)
//						appDayHours.set(dayHours[dayHour].id, 1);
//					else if (appDayHours.get(dayHours[dayHour].id) == 1){
//						appointmentLeft = parseFloat(appointmentLeft)+parseFloat(dayHours[dayHour].style.width)/2;
//						appDayHours.set(dayHours[dayHour].id, 2);
				}
			}
		}
		if (appointmentLeft){
			appointmentArray.push('app_' + appointment.id+"_"+currentDayHour);
			var appColor = appointment.color;
			if (!appColor)
				appColor = appointmentColors[getRandom(0,appointmentColors.length - 1)];
			contentDiv.appendChild(createAppointmentLink(appointment, currentDayHour, appointmentWidth+"px", appointmentHeight+"px",
					appointmentLeft+"px", appointmentTop+"px", appColor));
			// days[i].innerHTML += createAppointmentLink(appointment);
			new Draggable('appH_'+appointment.id, { scroll: window })
		}
	}
}

function createAppointmentLink(appointment, additionId, divWidth, divHeight, divLeft, divTop, color) {
	var link = document.createElement('div');

	link.setAttribute('href', '#');
	link.setAttribute('id', 'app_' + appointment.id+"_"+additionId);
	link.setAttribute('class', 'appointmentWeek');

	if (divWidth) {
		link.style.width = divWidth;
	}
	
	if (color) {
		link.style.background = color;
	}

	if (divHeight) {
		link.style.height = divHeight;
	}

	if ((divLeft || divTop) || (divLeft && divTop)) {
		link.style.position = "absolute";

		if (divLeft) {
			link.style.left = divLeft;
		}

		if (divTop) {
			link.style.top = divTop;
		}
	}
	
	var linkHeader = document.createElement('div');
	
	var appointmentStartMinutes;
	if (appointment.start.minutes == 0)
		appointmentStartMinutes = appointment.start.minutes+"0-";
	else
		appointmentStartMinutes = appointment.start.minutes+"-";
	var appointmentEndMinutes=null;
	if (appointment.end.minutes == 0)
		appointmentEndMinutes = appointment.end.minutes+"0\n";
	else
		appointmentEndMinutes = appointment.end.minutes+"\n";
	linkHeader.innerHTML = appointment.start.hours + ":" +appointmentStartMinutes +appointment.end.hours+ ":"+appointmentEndMinutes;
	var shortDescriptions = appointment.shortDescription.split(" ");
	for (var j = 0; j < shortDescriptions.length;j++){
		linkHeader.innerHTML += " ";
		if (shortDescriptions[j].length > 9){
			for (var i = 0; i < shortDescriptions[j].length/8;i++){
				linkHeader.innerHTML += shortDescriptions[j].substr(i*8, 8);
				if (shortDescriptions[j].substr(i*8).length > 8)
					linkHeader.innerHTML += "-\n";				
			}
		}
		else
			linkHeader.innerHTML += shortDescriptions[j];
	}

	linkHeader.style.cursor = "move";
	
	linkHeader.ondblclick = function() {
		editAppointment(appointment.id);
	};
	
	linkHeader.setAttribute('id', 'appH_' + appointment.id);
	linkHeader.setAttribute('class', 'appointmentWeekHeader');
	
	link.appendChild(linkHeader);
	
	return link;
}

/**
 * Creates a droppable target for an appointment
 */
function createDroppable(id) {

	Droppables.add(id, {
		// accept: 'draggable',
		hoverclass : 'hover',
		onDrop : function(element) {
			var appointment_id = element.id.split("_")[1];
			var hour = id.split("_")[1];
			var day = id.split("_")[3];
			var month = id.split("_")[5];
			var year = id.split("_")[7];

			moveAppointment(appointment_id, hour, "", day, month, year);
		}
	});
}
