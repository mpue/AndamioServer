function makePOSTRequest(url, parameters) {
  	
  	var post_request = false;
  	
   	if (window.XMLHttpRequest) { // Mozilla, Safari,...
      	post_request = new XMLHttpRequest();
       	if (post_request.overrideMimeType) {
           	post_request.overrideMimeType('text/html');
	   	}
   	} 
    else if (window.ActiveXObject) { // IE
	    try {
	       	post_request = new ActiveXObject("Msxml2.XMLHTTP");
	    } 
	    catch (e) {
			try {
	        	post_request = new ActiveXObject("Microsoft.XMLHTTP");
			}
	        catch (e) {}
	    }
   	}
   	if (!post_request) {
		alert('Cannot create XMLHTTP instance');
       	return false;
	}
     
    post_request.open('POST', url, true);
    post_request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    post_request.setRequestHeader("Content-length", parameters.length);
    post_request.setRequestHeader("Connection", "close");
   	post_request.send(parameters);
}

function toggleActive(randomNumber) {
	
	var buttonid = 'bCsiParticipant_'+randomNumber;
	var button = document.getElementById(buttonid);

	if (button.value == 'Registrieren') {
		button.value = 'Deregistrieren';

		document.location.href = 'AvantiCSI.html&plugin_page=editPatient&plugin_randomNumber='+randomNumber;
		
	}
	else {

		button.value = 'Registrieren';		

		var checkboxid = 'cCsiParticipant_'+randomNumber;
		var checkbox = document.getElementById(checkboxid);

		if (checkbox.checked == false)
			checkbox.checked = true;
		else
			checkbox.checked = false; 

		var poststr = "randomNumber="+randomNumber;
		makePOSTRequest('AvantiCSI.html&plugin_page=toggleActive&', poststr);

	}

	
}