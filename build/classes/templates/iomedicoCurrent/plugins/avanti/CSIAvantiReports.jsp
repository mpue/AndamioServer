<%@ taglib uri="/tags/pagetags" prefix="page" %>
	
	<style type="text/css">
	
		table.Patients {
			width: 90%;	
		}
	
		tr.odd {
			background-color: #BBBBBB;
		}
		
		td.birthDate {
			text-align: left;	
		}
	
		td.button {
			text-align: center;
			width : 120px;		
		}
		
		td.active {
			text-align: center;
			width : 90px;		
		}
		
		td.check {
			text-align: center;
			width : 20px;		
		}
		td.button {
			text-align: center;
			width : 60px;		
		}
		
		
		span.pagebanner {
			padding-top: 10px;	
		}
		
		input.portalButton {
			text-align : center;
		    background-color : #d6e3f4;
		    border: 1px solid #000000;
		}
	
	</style>				
	
	<script type="text/javascript">

	var left = (window.screen.width / 2) - (160 + 10);
	var top  = (window.screen.height / 2) - 250;

	var messageWindow;
	
	function getReports(url) {

		messageWindow = dhtmlwindow.open('divbox', 'div', 'reportMessageDiv', 'Reports generieren', 'width=320px,height=200px,left='+left+'px,top='+top+'px,resize=0,scrolling=0');
		
		if (window.XMLHttpRequest) { // Non-IE browsers
			req = new XMLHttpRequest();
				
			if (req.overrideMimeType) {
	           	req.overrideMimeType('text/xml');
			}
	                    
	      	req.onreadystatechange = readyStateChange;
	      	
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
		if (req.readyState == 4) { // Complete
	    	if (req.status == 200) { // OK response        		
	    		messageWindow.hide();
				document.location.href = 'AvantiCSIAdmin.html&plugin_page=getReport';  		
	   		}
	   		else {
	   			alert("Report konnte nicht gestartet werden.");
	    	}
		}
	}	

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
	     
	    post_request.open('POST', url, true);
	    post_request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	    post_request.setRequestHeader("Content-length", parameters.length);
	    post_request.setRequestHeader("Connection", "close");
	   	post_request.send(parameters);
	}

	function toggleProcessed(randomNumber) {
		
		var poststr = "randomNumber="+randomNumber;
		doPostRequest('AvantiCSIAdmin.html&plugin_page=toggleProcessed&', poststr);
		
	}

	function triggerBatchReports() {

		var inputs = document.getElementsByTagName("input"); 		
		var selectedIds = "";
		
		for (var i = 0; i < inputs.length; i++) {

			if (inputs[i].id.substr(0,13) == 'cCsiSelected_') {

				if (inputs[i].checked) {

					var id = inputs[i].id.split("_")[1];

					selectedIds += id;
					selectedIds += ',';
					
				}				
				  				
			}
			
		}

		selectedIds = selectedIds.substring(0,selectedIds.length - 1);
		
		var poststr = "selectedIds="+selectedIds;		
		var url = 'AvantiCSIAdmin.html&plugin_page=triggerBatchReport&'+poststr;		

		getReports(url);		
		
	}
	
	function processSelection(selection) {

		// check the alignment on a number of cells in a table. 
 
		var inputs = document.getElementsByTagName("input"); 

		if (selection == 'all') {
			
			for (var i = 0; i < inputs.length; i++) {
				 
				if (inputs[i].id.substr(0,13) == 'cCsiSelected_') {
					inputs[i].checked = true;
				}			
			}

		}	
		else if(selection =='allNonProcessed') {

			// cCsiProcessed_987003
			
			for (var i = 0; i < inputs.length; i++) {

				if (inputs[i].id.substr(0,13) == 'cCsiSelected_') {

					inputs[i].checked = false;

					var id = 'cCsiProcessed_'+inputs[i].id.split("_")[1];
					var checkBox = document.getElementById(id);

					if (!checkBox.checked)
						inputs[i].checked = true;
					
				}
				
			}

		}	
		else if(selection =='allProcessed') {
			
			for (var i = 0; i < inputs.length; i++) {

				if (inputs[i].id.substr(0,13) == 'cCsiSelected_') {

					inputs[i].checked = false;

					var id = 'cCsiProcessed_'+inputs[i].id.split("_")[1];
					var checkBox = document.getElementById(id);

					if (checkBox.checked)
						inputs[i].checked = true;
					
				}
				
			}

		}	
		else if(selection =='allPaid') {
			
			for (var i = 0; i < inputs.length; i++) {

				if (inputs[i].id.substr(0,13) == 'cCsiSelected_') {

					inputs[i].checked = false;

					var id = 'cCsiPaid_'+inputs[i].id.split("_")[1];
					var checkBox = document.getElementById(id);

					if (checkBox.checked)
						inputs[i].checked = true;
					
				}
				
			}

		}	
		
		// csiPaid_
		
		
	}

	function savePatient(randomNumber) {

		var input = document.getElementById('cCsiPaidAmount_'+randomNumber);
		var amount = input.value;		
		var poststr = "randomNumber="+randomNumber+"&amount="+amount;
		
		doPostRequest('AvantiCSIAdmin.html&plugin_page=savePatient&', poststr);
		
	}

	function togglePaid(randomNumber) {

		var input = document.getElementById('cCsiPaidAmount_'+randomNumber);
		var checkbox =  document.getElementById('cCsiPaid_'+randomNumber);

		if (checkbox.checked) {
			input.disabled = false;
		}
		else {
			input.disabled = true;
		}

		var poststr = "randomNumber="+randomNumber;
		doPostRequest('AvantiCSIAdmin.html&plugin_page=togglePaid&', poststr);
		
	}
		
	
	</script>
	
	<div id="reportMessageDiv" style="display:none">
		<table border="0" align="center" cellspacing="0" cellpadding="10px">
			<tr height="25px">
				<td align="center">
					&nbsp;
				</td>
			</tr>

			<tr>
				<td align="center">
					<h3>Bitte warten, die Reports werden generiert.</h3>
				</td>
			</tr>
			<tr>
				<td align="center">
					<img src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/loading.gif" alt="loading" border="0"/>
				</td>
			</tr>
		</table>
	</div>  
	
		<h1>&Uuml;bersicht aller Avanti CSI Teilnehmer</h1>					
	<hr size="1" width="98%" align="left"/>
	<p style="margin-top:10px"></p>
	<p align="left">
		Massenreports : 
		<select name="reportsType" id="reportType" onchange="processSelection(this.options[this.selectedIndex].value);">
			<option value="none">Bitte w&auml;hlen</option>
			<option value="all">Alle</option>
			<option value="allNonProcessed">Alle nicht bearbeiteten</option>
			<option value="allProcessed">Alle bearbeiteten</option>
			<option value="allPaid">Alle erstatteten</option>
			<option value="choice">Auswahl</option>
		</select>
		<input type="button" value="Report starten" class="portalButton" onclick="triggerBatchReports();"/>
	</p>																		

	##PATIENTS##
	

