var theobject = null; //This gets a value as soon as a resize start

function resizeObject() {
	this.el        = null; //pointer to the object
	this.dir    = "";      //type of current resize (n, s, e, w, ne, nw, se, sw)
	this.grabx = null;     //Some useful values
	this.graby = null;
	this.width = null;
	this.height = null;
	this.left = null;
	this.top = null;
}
	

//Find out what kind of resize! Return a string inlcluding the directions
function getDirection(el, event) {
	var yPos, dir;
	dir = "";
	
	var realPosition = el.offsetTop+el.parentNode.offsetTop+el.offsetHeight;
	
	if (event.pageY < realPosition && event.pageY > realPosition-20)
		dir += "s";

	return dir;
}

function doDown(event) {
	var el = event.target;

	if (el == null || el.className != "appointmentWeek") {
		theobject = null;
		return;
	}		

	dir = getDirection(el,event);
	if (dir == "") return;

	theobject = new resizeObject();
		
	theobject.el = el;
	theobject.dir = dir;

//	theobject.grabx = event.pageX;
	theobject.graby = event.pageY;
//	theobject.width = el.offsetWidth;
	theobject.height = el.offsetHeight;
//	theobject.left = el.offsetLeft;
	theobject.top = el.offsetTop;

	window.returnValue = false;
	window.cancelBubble = true;
}

function doUp(event) {
//	alert(event.clientX);

	if (theobject != null) {

		var currentHourDiv = null;
		for ( var dayHour = 0; dayHour < dayHours.length; dayHour++) {
			if (dayHours[dayHour].offsetTop < theobject.el.offsetTop+theobject.el.offsetHeight
					&& dayHours[dayHour].offsetTop+dayHours[dayHour].offsetHeight >= theobject.el.offsetTop+theobject.el.offsetHeight) {
				if (dayHours[dayHour].offsetLeft < theobject.el.offsetLeft+theobject.el.offsetWidth
						&& dayHours[dayHour].offsetLeft+dayHours[dayHour].offsetWidth >= theobject.el.offsetLeft+theobject.el.offsetWidth) {
					currentHourDiv = dayHours[dayHour].id;
					break;
				}
			}
		}
		if (currentHourDiv != null) {
			var appointment_id = theobject.el.id.split("_")[1];
			var hour = currentHourDiv.split("_")[1];
			var day = currentHourDiv.split("_")[3];
			var month = currentHourDiv.split("_")[5];
			var year = currentHourDiv.split("_")[7];

			moveAppointment(appointment_id, "", hour, day, month, year);

			theobject = null;
		}
	}
}

function doMove(event) {
	var el, xPos, yPos, str, xMin, yMin;
	xMin = 8; //The smallest width possible
	yMin = 20; //             height

	el = event.target;
	currentHourDiv = null;

	if (el.className == "appointmentWeek") {
		str = getDirection(el,event);
		//Fix the cursor	
		if (str == "") str = "default";
		else str += "-resize";
		el.style.cursor = str;
	}
	
	//Dragging starts here
	if(theobject != null) {
		currentHourDiv = event.target.id;
//		if (dir.indexOf("e") != -1)
//			theobject.el.style.width = Math.max(xMin, theobject.width + event.clientX - theobject.grabx) + "px";
	
		if (dir.indexOf("s") != -1)
			theobject.el.style.height = Math.max(yMin, theobject.height + event.pageY - theobject.graby) + "px";
//
//		if (dir.indexOf("w") != -1) {
//			theobject.el.style.left = Math.min(theobject.left + event.clientX - theobject.grabx, theobject.left + theobject.width - xMin) + "px";
//			theobject.el.style.width = Math.max(xMin, theobject.width - event.clientX + theobject.grabx) + "px";
//		}
//		if (dir.indexOf("n") != -1) {
//			theobject.el.style.top = Math.min(theobject.top + event.clientY - theobject.graby, theobject.top + theobject.height - yMin) + "px";
//			theobject.el.style.height = Math.max(yMin, theobject.height - event.clientY + theobject.graby) + "px";
//		}
		
		window.returnValue = false;
		window.cancelBubble = true;
	} 
}


function getReal(el, type, value) {
	temp = el;
	while ((temp != null) && (temp.tagName != "BODY")) {
		if (eval("temp." + type) == value) {
			el = temp;
			return el;
		}
		temp = temp.parentElement;
	}
	return el;
}

