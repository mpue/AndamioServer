var messageWindow;
var data;
var bgImages = new Array();
var currentImage = 0;

function expandNode(id) {
	
	var element = document.getElementById(id);	
	element.style.display = 'block';
	
}
bgImages[0] = 'url(/andamio/templates/studienportal/images/logo-studienportal_st.png)';
bgImages[1] = 'url(/andamio/templates/studienportal/images/logo-studienportal_pills.png)';
bgImages[2] = 'url(/andamio/templates/studienportal/images/logo-studienportal_world.png)';

function setHeight(id,height) {
	var element = document.getElementById(id);
	element.style.height = height;
}

/**
 *	Adjust the height of the content on body load complete
 */

function adjustContent() {				
	var height = window.innerHeight - 228;
	setHeight('content',height);
	setHeight('page',window.innerHeight-20);
}

/**
 * Allows to submit a form field by pressing enter,
 * this is needed for the login password field.			
 */
function submitEnter(myfield,e)	{

	var keycode;
	if (window.event) keycode = window.event.keyCode;
	else if (e) keycode = e.which;
	else return true;

	if (keycode == 13) {
	   myfield.form.submit();
	   return false;
	}
	else
	   return true;
}

function changeImage() {				
	document.getElementById('header').style.backgroundImage = bgImages[currentImage];
	if (currentImage < 2)
		currentImage++;
	else
		currentImage=0;
}

function openIOFilesList(id) {
	getAttachments(id);
}		

function getAttachments(id) {

	var url = '/andamio/IOFileAction.do?do=getAttachments&docId='+id;

	new Ajax.Request(url, {
		method: 'get',
		       onComplete: function(transport) {
			  if (200 == transport.status) {
			      data = transport.responseText;
			  	  processAttachments(id);					    
			  }
			  else {
				  alert("An error occured during connect.\n\nPlease check the server settings.\n\nError code : "+transport.status);
			  }
	  	   }
	});
	
}			

function processAttachments(id) {
	
	var attachments = eval(data); 	

	var attachmentData = '';
	
	for(var i=0; i < attachments.length; i++) {
	    
		var attachment = attachments[i];

		attachmentData += '<a href="/andamio/IOFileAction.do?do=getAttachment&docID='+id+'&attachmentID='+attachments[i].attachID+'&name='+escape(attachment.name)+'">'			
		attachmentData += attachment.name;
		attachmentData +="</a>";
		attachmentData +="<br>";
		
	}
	
	var filelist = document.getElementById('filelist');
	filelist.innerHTML = attachmentData;

	Effect.Appear('messageDiv', { duration: 0.5 });
	
}	

function hideDiv(id) {
	Effect.Fade(id, { duration: 0.5 });
}

function initDrag() {
	new Draggable('messageDiv', { scroll: window });		
}	