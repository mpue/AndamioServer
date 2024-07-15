var localPath = '##LOCALPATH##';
var hostname  = '##HOSTNAME##';

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

function sendMessage() {

	var poststr =  "contact_users=" + encodeURI( document.getElementById("contact_users").value) +
     	           "&vorname="     + encodeURI( document.getElementById("vorname").value) +
         	       "&nachname="    + encodeURI( document.getElementById("nachname").value) +
             	   "&anschrift="   + encodeURI( document.getElementById("anschrift").value) +
              	   "&plz="         + encodeURI( document.getElementById("plz").value) +
             	   "&ort="         + encodeURI( document.getElementById("ort").value) +
             	   "&land="        + encodeURI( document.getElementById("land").value) +             	   
             	   "&telefon="     + encodeURI( document.getElementById("telefon").value) +
             	   "&telefax="     + encodeURI( document.getElementById("telefax").value) +
             	   "&email="       + encodeURI( document.getElementById("email").value) +
               	   "&message="     + encodeURI( document.getElementById("message").value);
	
 	makePOSTRequest('http://##HOSTNAME##/##LOCALPATH##siteContact.do?do=send&', poststr);
	toggleVisible("contactForm");
	toggleVisible("messageAfterSend");
}

function toggleVisible(docId) {
	thisId = document.getElementById(docId);
	if(thisId != null) {
		if(thisId.style.display == 'none') thisId.style.display = 'block';
		else thisId.style.display = 'none';
	}
}