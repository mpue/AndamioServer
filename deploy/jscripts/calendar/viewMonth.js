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
 * This is the calendar script for show month. No classes, no bounds but who cares.
 */

var days = new Array(42);

var numOfDays = 7;
var numOfWeeks = 6;

/**
 *  creates a calendar grid based on a given day, a given month and a given year
 *  
 *  @param width the with of a panel for each day
 *  @param height the height of a panel for each day
 */	 
function createCalendar(week, day,month,year, width, height) {
	var dateField = document.getElementById('currentDate');
	var value = monthNames[month - 1] + " " + year;
	dateField.value = value;
	document.title = "View month for ##USERNAME## - " + value;
	// the div to put the stuff into
	var contentDiv = document.getElementById("content");
	while (contentDiv.hasChildNodes()) {
		contentDiv.removeChild(contentDiv.lastChild);
	}
	// start and end day of the current month
	var start = getMonthBeginDay(month,year);
	var end   = getLengthOfMonth(month,year);
	// header for the days 
	for (var day = 0; day < 7; day++) {
		var x = day*width+50;
		var y = 0;
		var div = createDiv("dayheader_"+day, width+"px", "25px", x+"px", y+"px", true, dayNames[day]);
		div.style.background = whiteColor;
		div.style.verticalAlign ='center';
		div.style.lineHeight = '160%';
		div.style.fontWeight = 'bold';
		div.style.textAlign = 'center';
		contentDiv.appendChild(div);
	}	
	// check current calendar week
	var startDate = new Date(year,month-1,1);
	var calendarWeek = getCalendarWeek(startDate);
	// empty panal for the top left
	var div = createDiv("weekheader", "49px", "25px", "0px", "25px", true, "");
	div.style.background = whiteColor;
	contentDiv.appendChild(div);	
	// header for the weeks
	for (var week = 0; week < 6; week++) {
		var x = 0;
		var y = week*height+25;
		var div = createDiv("weekheader_"+day, "50px", height+"px", x+"px", y+"px", true, calendarWeek);
		div.style.background = whiteColor;
		div.style.verticalAlign ='center';
		div.style.lineHeight = '600%';
		div.style.fontWeight = 'bold';
		div.style.textAlign = 'center';		
		contentDiv.appendChild(div);
		calendarWeek++;
	}
	// wee need to counth the days
	var i = 1;
	// today ...
	var today = new Date();	
	// go through every week and day and create the grid
	for (var week = 0; week < 6; week++) {
		for (var day = 0; day < 7; day++) {
			// the x-coordinate of the layer being created
			var x = day*width+50;
			// the y-coordinate of the layer being created
			var y = week*height+25;		
			// the layer we are going to create
			var div;
			// check if the current day is inside the current month, if not create an empty layer
			if (start >= 0) {
				var currentDay = i - start;
				if (currentDay > 0 && currentDay <= end) {
					div = createDiv("day_"+currentDay+"_month_"+month+"_year_"+year, width+"px", height+"px", x+"px", y+"px", true,currentDay);					
				}
				else if (currentDay <= 0){
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
					var currentPreviousMonthDay = getLengthOfMonth(previousMonth,previousYear)+currentDay;
					div = createDiv("day_-"+currentPreviousMonthDay+"_month_"+previousMonth+"_year_"+previousYear, width+"px", height+"px", x+"px", y+"px", true,currentPreviousMonthDay);
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
					var currentNextMonthDay = currentDay-end;
					div = createDiv("day_+"+currentNextMonthDay+"_month_"+nextMonth+"_year_"+nextYear, width+"px", height+"px", x+"px", y+"px", true,currentNextMonthDay);
				}
				if (currentDay == today.getDate() && today.getMonth() == month-1 && today.getYear()+1900 == year ) {
					div.style.color = blueColor;
					div.style.fontWeight = 'bold';
				}
			}
//			else {
//				div = createDiv("day_"+i, width+"px", height+"px", x+"px", y+"px", color, true,i);				
//				if (i == today.getDate() && today.getMonth() == month-1 && today.getYear()+1900 == year ) {
//					div.style.color = '#f00';
//					div.style.fontWeight = 'bold';
//				}				
//			}
			// layer callbacks
			div.onmousedown = mouseDown;
						
			// finally create div and put it into the global array
			contentDiv.appendChild(div);			
			days[i-1] = div;
			createDroppable(div.id);
			// count the days ...
			i++;
		}
	}
	setDefaultColors(true);
}	

function setDefaultColors(withName){
	// start and end day of the current month
	var start = getMonthBeginDay(month,year);
	var end   = getLengthOfMonth(month,year);
	// set default color 
	var color = defaultColor;
	var color2 = default2Color;
	
	// wee need to counth the days
	var i = 1;
	
	for (var week = 0; week < 6; week++) {
		for (var day = 0; day < 7; day++) {
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
			var currentDay = i - start;
			if (currentDay > 0 && currentDay <= end) {
//				alert(currentDay);
				var text = checkHoliday(currentDay,month,year,color,dayNames[day]);
				if (text && withName)
					days[i - 1].appendChild(createHolidayText(text));
				days[i-1].style.background = newColor;				
			}
			else if (currentDay <= 0){
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
				var currentPreviousMonthDay = getLengthOfMonth(previousMonth,previousYear)+currentDay;
				var text = checkHoliday(currentPreviousMonthDay,previousMonth,previousYear,color2,dayNames[day]);
				if (text && withName)
					days[i - 1].appendChild(createHolidayText(text));
				days[i-1].style.background = newColor;
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
				var currentNextMonthDay = currentDay-end;
				var text = checkHoliday(currentNextMonthDay,nextMonth,nextYear,color2,dayNames[day]);
				if (text && withName) 
					days[i - 1].appendChild(createHolidayText(text));
				days[i-1].style.background = newColor;
			}
		i++;
		}
	}
}


function nextMonth() {
	if (month < 12) {
		month++;		
	}
	else {
		month=1;
		year++;
	}	
	
	document.location.href = url+"?"+day+"."+month+"."+year;		
}


function createAppointmentLink(appointment) {
	
	var link = document.createElement('div');
	link.setAttribute('href','#');
	link.setAttribute('id','app_'+appointment.id);
	link.setAttribute('class','appointmentMonth');
	
	var shortDescriptions = appointment.shortDescription.split(" ");
	for (var j = 0; j < shortDescriptions.length;j++){
		link.innerHTML += " ";
		if (shortDescriptions[j].length > 15){
			for (var i = 0; i < shortDescriptions[j].length/14;i++){
				link.innerHTML += shortDescriptions[j].substr(i*14, 14);
				if (shortDescriptions[j].substr(i*14).length > 14)
					link.innerHTML += "-\n";				
			}
		}
		else
			link.innerHTML += shortDescriptions[j];
	}
	
	link.ondblclick = function() {
		editAppointment(appointment.id);
	};
	
	return link;
}


function lastMonth() {
	if (month > 1) {
 		month--;
	}
	else {
		month=12;
		year--;
	}
	
	document.location.href = url+"?"+day+"."+month+"."+year;
	
}

function processAppointments(data) {
		
	var appointments = eval(data); 	
	
	for (var i = 0; i < numOfDays*numOfWeeks;i++) {
		if (days[i].innerHTML) {
			var currentDate = parseInt(days[i].id.split("_")[1]);
			var currentMonth = parseInt(days[i].id.split("_")[3]);
			var currentYear = parseInt(days[i].id.split("_")[5]);
			var appointmentCount = 0;
			for(var j=0; j < appointments.length; j++) {	
				var appointment = appointments[j];
				if (appointment.start.date == Math.abs(currentDate) && appointment.start.month+1 == currentMonth && appointment.start.year+1900 == currentYear) {					
					if (appointmentCount>2)
						break;
					days[i].appendChild(createAppointmentLink(appointment));
					// days[i].innerHTML += createAppointmentLink(appointment);
					new Draggable('app_'+appointment.id, { scroll: window });
					appointmentCount++;
				}
			}				
		}
			
	}
}

function clearAppointments() {
	for (var i = 0; i < numOfDays*numOfWeeks;i++) {
		var date = Math.abs(parseInt(days[i].id.split("_")[1]));	
		if (date > 0)
			days[i].innerHTML = date;
		else
			days[i].innerHTML = "";
	}
	setDefaultColors(true);
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
			var day = id.split("_")[1];
			var month = id.split("_")[3];
			var year = id.split("_")[5];

			moveAppointment(appointment_id, "", "", day, month, year);
		}
	});
}

//callback for the mouse down event
function mouseDown(evt) {
	selectedHour = null;
	setDefaultColors(false);

	if (evt.target.id.indexOf('day') > -1) {
		selectedAppointment = 0;
		var selectedDayString = evt.target.id.split("_")[1];
		
		selectedMonth =  parseInt(evt.target.id.split("_")[3]);
		selectedYear =  parseInt(evt.target.id.split("_")[5]);
		selectedDay = parseInt(selectedDayString);
		
		// start and end day of the current month
		var start = getMonthBeginDay(month,year);
		var end   = getLengthOfMonth(month,year);
		
		var lengthPreviousMonth = getLengthOfMonth(month,selectedYear);
		var selectedDayInt = null;
		if (selectedDayString.indexOf('-')>-1)
			selectedDayInt = Math.abs(selectedDay)+start-lengthPreviousMonth-1;
		else if (selectedDayString.indexOf('+')>-1)
			selectedDayInt = start+end+Math.abs(selectedDay)-1;
		else
			selectedDayInt = selectedDay+start-1;
//		alert(selectedDayInt);
		if (selectedDayInt % 7 == 0)
			newColor = selectedSundayColor;
		else
			checkHoliday(Math.abs(selectedDay),selectedMonth,selectedYear,selectedColor,dayNames[(selectedDayInt % 7)]);
		evt.target.style.background = newColor;
	}
	else if(evt.target.id.indexOf('app') > -1) {
		evt.target.parentNode.style.background = selectedAppointmentColor;
		
		selectedMonth =  parseInt(evt.target.parentNode.id.split("_")[3]);
		selectedYear =  parseInt(evt.target.parentNode.id.split("_")[5]);
		selectedDay = parseInt(evt.target.parentNode.id.split("_")[1]);
//		selectedDay = 0;
//		selectedMonth = 0;
//		selectedYear = 0;
		selectedAppointment = parseInt(evt.target.id.split("_")[1]);
	}
}
