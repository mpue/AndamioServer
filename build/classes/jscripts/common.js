var screenWidth  = 0;
var screenHeight = 0;
var html;

function getScreenDimensions() {

	//IE
	if(!window.innerWidth)
	{
		//strict mode
		if(!(document.documentElement.clientWidth == 0))
		{
			screenWidth  = document.documentElement.clientWidth;
			screenHeight = document.documentElement.clientHeight;
		}
		//quirks mode
		else
		{
			screenWidth  = document.body.clientWidth;
			screenHeight = document.body.clientHeight;
		}
	}
	//w3c
	else
	{
		screenWidth  = window.innerWidth;
		screenHeight = window.innerHeight;
	}

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

function copyFile(source,dest,targetName) {
	var poststr = "source=" + source + "&dest=" +dest +"&targetName=" +targetName;
	makePOSTRequest('ListDirectories.do?do=copyFile&', poststr);
}

function deleteFile(filename) {
	var poststr = "filename=" + filename;
	makePOSTRequest('ListDirectories.do?do=deleteFile&', poststr);
}

function downloadFile(filename,dir) {
	document.location.href = 'ListDirectories.do?do=download&dir='+dir+"&filename="+filename;	
}
