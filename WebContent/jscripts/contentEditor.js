var dirty = false;
var lastMetaDataUrl   = '';
var lastHTMLUrl   = '';

String.prototype.trim = function () {
	return this.split( /\s/ ).join( " " );
}
String.prototype.convertHTMLEntity = function () {
	var myString = this;
	myString = myString.replace( /\&amp;/g, '&' );
	myString = myString.replace( /\&lt;/g, '<' );
	myString = myString.replace( /\&gt;/g, '>' );
	myString = myString.replace( /\&quot;/g, '"' );
	myString = myString.replace( /\&copy;/g, '©' );
	myString = myString.replace( /\&reg;/g, '®' );
	myString = myString.replace( /\&laquo;/g, '«' );
	myString = myString.replace( /\&raqou;/g, '»' );
	myString = myString.replace( /\&apos;/g, "'" );
	return myString;
}

function retrieveMetadata(url) {
	
	// Wurde etwas am Inhalt veraendert? Dirty?
	
	if (dirty) {
		lastMetaDataUrl = url;		
	
	} else {
		if (window.XMLHttpRequest) { // Non-IE browsers
			req = new XMLHttpRequest();
			
			if (req.overrideMimeType) {
	           	req.overrideMimeType('text/xml');
	        }
	                   
	     	req.onreadystatechange = processStateChangeMeta;
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
		        req.onreadystatechange = processStateChangeMeta;
	       		req.open("GET", url, true);
		        req.send();
	   		}
		}
	}
}

function retrieveMetadataFromLastUrl() {
			
	if (window.XMLHttpRequest) { // Non-IE browsers
		req = new XMLHttpRequest();
		
		if (req.overrideMimeType) {
           	req.overrideMimeType('text/xml');
        }
                   
     	req.onreadystatechange = processStateChangeMeta;
     	try {
       		req.open("GET", lastMetaDataUrl, true);
	    } 
	    catch (e) {
	        alert(e);
		}

     	req.send(null);
   	} 
   	else if (window.ActiveXObject) { // IE
    	req = new ActiveXObject("Microsoft.XMLHTTP");

     	if (req) {
	        req.onreadystatechange = processStateChangeMeta;
       		req.open("GET", lastMetaDataUrl, true);
	        req.send();
   		}
	}
}
  	
function makeRequest(url) {
	if (window.XMLHttpRequest) { // Non-IE browsers
		req = new XMLHttpRequest();
			
		if (req.overrideMimeType) {
           	req.overrideMimeType('text/xml');
		}
                    
      	req.onreadystatechange = processStateChangeMeta;
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
	if (html.readyState == 4) { // Complete
    	if (html.status == 200) { // OK response
   		}
   		else {
			//alert(unescape("Problem: " + req.statusText));
    	}
	}
}

function processStateChangeMeta() {
	
    if (req.readyState == 4) { // Complete
      	if (req.status == 200) { // OK response
		        
	        var xml = req.responseXML;
	        var root = xml.documentElement;
	        
	        var content_idValueFound 	= false;
	        var descriptionValueFound 	= false;
	        var pagenameValueFound 		= false;
	        var contentnameValueFound 	= false;
	        var metatagsValueFound      = false;
				
	        for (i=0;i<root.childNodes.length;i++) {	
	        	var name  = root.getElementsByTagName("property")[i].getAttribute("name");
	        	var value = root.getElementsByTagName("property")[i].getAttribute("value");
	        	
		        if (name == "content_id") {
		        	content_idValueFound = true;
		        	document.getElementById("id").value = value;
		        }
			        			        
		        if (name == "description") {
		        	descriptionValueFound = true;
		        	document.getElementById("description").value = value;
		        }
			        
			    if (name == "pagename") {
			    	pagenameValueFound = true;
					document.getElementById("pagename").value = value;
			    }			
			        
				if (name == "contentname") {
					contentnameValueFound = true;
					document.getElementById("contentname").value = value;
				}
				if (name == "metatags") {
					metatagsValueFound = true;
					document.getElementById("metatags").value = value;
				}		        
				
		    }
		    
		    if ( content_idValueFound == false) {
		    	document.getElementById("id").value = "";
		    }
		    if ( descriptionValueFound == false) {
		    	document.getElementById("description").value = "";
		    }
		    if ( pagenameValueFound == false) {
		    	document.getElementById("pagename").value = "";
		    }
		    if ( contentnameValueFound == false) {
		    	document.getElementById("contentname").value = "";
		    }
		    if ( metatagsValueFound == false) {
		    	document.getElementById("metatags").value = "";
		    }

    	}
    	else {
       		//alert(unescape("Problem: " + req.statusText));
    	}
	}
}
 	
 	
 	
 	
function retrieveHTML(url) {
	
	if (dirty) {		
		lastHTMLUrl = url;			
	} 
	else {
		if (window.XMLHttpRequest) { // Non-IE browsers
			html = new XMLHttpRequest();
			                    
	   		html.onreadystatechange = processStateChange;
	   		try {
	       		html.open("GET", url, true);
		    } 
		    catch (e) {
		        alert(e);
			}
	
	   		html.send(null);
	
	   	} 
	   	else if (window.ActiveXObject) { // IE
	    	html = new ActiveXObject("Microsoft.XMLHTTP");
	
	   		if (html) {
		        html.onreadystatechange = processStateChange;
	       		html.open("GET", url, true);
		        html.send();
		    }
   		}
	}
}

function retrieveHTMLFromLastUrl() {

	if (window.XMLHttpRequest) { // Non-IE browsers
		html = new XMLHttpRequest();
		                    
   		html.onreadystatechange = processStateChange;
   		try {
       		html.open("GET", lastHTMLUrl, true);
	    } 
	    catch (e) {
	        alert(e);
		}

   		html.send(null);

   	} 
   	else if (window.ActiveXObject) { // IE
    	html = new ActiveXObject("Microsoft.XMLHTTP");

   		if (html) {
	        html.onreadystatechange = processStateChange;
       		html.open("GET", lastHTMLUrl, true);
	        html.send();
	    }
  	}
  	dirty=false;
	
}

function processStateChange() {
	
    if (html.readyState == 4) { // Complete
    
    	if (html.status == 200) { // OK response

      		tinyMCE.execCommand('mceRemoveControl',false,'contentstring');
	      	document.getElementById("contentstring").value = html.responseText;
	      	tinyMCE.execCommand('mceAddControl',false,'contentstring');
	      	
   		}
   		else {
       		//alert(unescape("Problem: " + req.statusText));
   		}
    
	}
    
}
  	
function saveContent() {
   	saver();
   	// alert('saved.');
 	dijit.byId('SaveConfirmDialog').show();
}

function saveContentWithoutConfirm() {
   	saver();
   	retrieveMetadataFromLastUrl();
}

function saver() {
	var poststr = "&id=" + encodeURI( document.getElementById("id").value) +
				   "&commentsAllowed="+document.getElementById("commentsAllowed").checked+
     	           "&description=" + encodeURI( document.getElementById("description").value) +
         	       "&pagename=" + encodeURI( document.getElementById("pagename").value) +
             	   "&contentname=" + encodeURI( document.getElementById("contentname").value) +
             	   "&metatags=" + encodeURIComponent(document.getElementById("metatags").value) +
               	   "&contentstring=" +  encodeURIComponent(tinyMCE.getContent());
	
 	makePOSTRequest('ShowContent.do?do=save', poststr);
 	tinyMCE.triggerSave();
 	dirty = false;
 	remainingTime = 3600; // reset session timer
}

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

function changeCallback() {
	
	if (!nothingSelected)	
		dirty = true;
}
	
function confirmAction() {
       return confirm(unescape("Der Inhalt wurde ver%E4ndert, %C4nderungen verwerfen?"))
}
    
function setDirty() {
	dirty = true;
}
    
function deleteContent() {
	var poststr = "content_id=" + encodeURI( document.getElementById("id").value);
 	makePOSTRequest('ShowContent.do?do=delete&', poststr);
	setTimeout("refresh()",500);
}

function nodeUp(node) {
	var poststr = "node_id=" + node.attributes.node_id;
 	makePOSTRequest('ShowNode.do?do=elementUp&async=true&', poststr);

}

function nodeDown(node) {
	var poststr = "node_id=" + node.attributes.node_id;
 	makePOSTRequest('ShowNode.do?do=elementDown&async=true&', poststr);
}

function removeContentAndNode(node) {
	var poststr = "content_id=" + node.attributes.content_id;
 	makePOSTRequest('ShowContent.do?do=delete&async=true&', poststr);
}

function removeNode(node) {
	var poststr = "node_id=" + node.attributes.node_id;
	makePOSTRequest('ShowNode.do?do=delete&async=true&', poststr);
}	

function removeGroup(node_id,group_id) {
	var poststr = "node_id=" + node_id + "&usergroup_id="+group_id;
	makePOSTRequest('ShowNode.do?do=removeGroup&async=true&', poststr);
}	

function addGroup(node_id,group_id) {
	var poststr = "node_id=" + node_id + "&usergroup_id="+group_id;
	makePOSTRequest('ShowNode.do?do=addGroup&async=true&', poststr);
}	

function importNodes() {
	var poststr = "replace=false&async=true";
	makePOSTRequest('ShowNode.do?do=importNodes&', poststr);
}

function refresh() {
	window.location.href = 'ShowContent.do?do=editContents';
}


