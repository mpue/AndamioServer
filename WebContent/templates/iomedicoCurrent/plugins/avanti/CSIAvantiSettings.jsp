	
<script type="text/javascript">					

	function doPostRequest(url, parameters) {
	  	
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

	   	post_request.onreadystatechange = readyStateChange;
	     
	    post_request.open('POST', url, true);
	    post_request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	    post_request.setRequestHeader("Content-length", parameters.length);
	    post_request.setRequestHeader("Connection", "close");
	   	post_request.send(parameters);
	}	

	function readyStateChange()  {

		var messages = document.getElementById('messages');
		messages.style.display = 'block'		
		messages.innerHTML = '&Auml;nderungen gespeichert:';
		
	}
	
	function submit() {

		var mailProperties = document.getElementById('mailProperties').value;
		var mailContent = document.getElementById('mailContent').value;
		
		var poststr = "mailProperties="+mailProperties+"&mailContent="+mailContent;
		
		doPostRequest('AvantiCSIAdmin.html&plugin_page=settings&action=saveSettings&', poststr);
		
	}			

</script>		
					
<div class="messages" id="messages" style="display : none;">	
</div>

<input type="hidden" name="action" value="saveSettings">
<table border="0" align="center" cellpadding="5px" cellspacing="0">
	<tr>
		<td>
			Konfiguration
		</td>
		<td align="right">
			<input type="button" onclick="submit();" value="Speichern">
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<textarea rows="20" cols="75" name="mailProperties" id="mailProperties">##MAILPROPERTIES##</textarea>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			Mail Vorlage
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<textarea rows="20" cols="75" name="mailContent" id="mailContent">##MAILCONTENT##</textarea>												
		</td>
	</tr>
</table
								
