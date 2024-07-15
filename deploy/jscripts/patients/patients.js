var data;
var selectedDoctorId;

function movePatient(id) {
	
	var url = '/andamio/admin/DoctorAction.do?do=getJSONWithoutMeta';
	
	new Ajax.Request(url, {
		method: 'get',
		onComplete: function(transport) {
			if (200 == transport.status) {
		    	data = transport.responseText;
		  		processData(id);					    
		  	}
		  	else {
				alert("An error occured during connect.\n\nPlease check the server settings.\n\nError code : "+transport.status);
		  	}
  	   	}
	});				
	
}

function processData(id) {
	
	var doctors = eval(data); 	

	var doctorData = '';
	
	for(var i=0; i < doctors.length; i++) {
	    
		var doctor = doctors[i];

		doctorData += '<a href="#" onclick="movePatientTo('+id+','+doctor.id+')">'			
		doctorData += doctor.lastname+","+doctor.firstname;
		doctorData +="</a>";
		doctorData +="<br>";
		
	}
	
	var doctorList = document.getElementById('doctorList');
	doctorList.innerHTML = doctorData;

	Effect.Appear('patientDiv', { duration: 0.5 });
	
}				

function movePatientTo(id, doctorId) {
	
	var url = "/andamio/admin/DoctorAction.do?do=movePatient";

	var poststr =  
		"&id=" + id +
		"&doctorId=" +doctorId;
	
	new Ajax.Request(url, {
		method: 'post',
		postBody : poststr,
		onComplete: function() {
			hideDiv('patientDiv');
			deleteRow('patients',id);				
	  	}
	});				
	
}

function addRow(id, pId) {
	var myTable = document.getElementById(id);
	var tBody = myTable.getElementsByTagName('tbody')[0];
	var tr = document.createElement('tr');
	tr.id = pId;
	tr.style.display = 'none';
	tr.className = 'new';
	var td1 = document.createElement('td');
	var td2 = document.createElement('td');
	var td3 = document.createElement('td');
	var td4 = document.createElement('td');
	var td5 = document.createElement('td');
	var td6 = document.createElement('td');
	td1.className = 'randomNumber';
	td1.className = 'center';
	td3.className = 'button';
	td4.className = 'button';
	td5.className = 'button';
	td6.className = 'button';
	td1.innerHTML = pId;			
	td2.innerHTML = '';
	td3.innerHTML = '<div class="clear"><a class="button" href="#" onclick="this.blur();movePatient('+pId+');return false;"><span>Bewegen</span></a></div>';
	td4.innerHTML = '<div class="clear"><a class="button" href="#" onclick="this.blur();editPatient('+pId+');return false;"><span>Bearbeiten</span></a></div>';
	td5.innerHTML = '<div class="clear"><a class="button" href="#" onclick="this.blur();deletePatient('+pId+');return false;"><span>L&ouml;schen</span></a></div>';
	td6.innerHTML = '<div class="clear"><a class="button" href="#" onclick="this.blur();acceptPatient('+pId+');return false;"><span>Annehmen</span></a></div>';
			tr.appendChild (td1);
			tr.appendChild (td2);
			tr.appendChild (td3);
			tr.appendChild (td4);
			tr.appendChild (td5);				
			tr.appendChild (td6);
			tBody.appendChild(tr);			
			Effect.Appear(pId, { duration: 1.0 });
		}
		
        function deleteRow(tableId,rowId) {
            try {
	            var table = document.getElementById(tableId);
 
	            for(var i=0; i<table.rows.length; i++) {
	                var row = table.rows[i];						
	                
	                if (row.id == rowId) {	
	                	Effect.Fade(row.id, { duration: 1.0, afterFinish: function(effect) { table.deleteRow(i); } });							
	                	break;
	                }
	 
	            }
            }
            catch(e) {
                alert(e);
            }
        }			
		
        function acceptRow(tableId,rowId) {
            try {
	            var table = document.getElementById(tableId);
 
	            for(var i=0; i<table.rows.length; i++) {
	                var row = table.rows[i];						
	                
	                if (row.id == rowId) {	
	    				if (i%2 == 0)
	    					row.className = 'even';				
				else
					row.className = 'odd';							
                	break;
                }
 
            }
        }
        catch(e) {
            alert(e);
        }
    }		        
    
	function editPatient(id) {
		alert(id);
	}
	
	function deletePatient(id) {
		
		var url = "/andamio/admin/DoctorAction.do?do=deletePatient";
	var poststr = "&id=" + id;
	
	new Ajax.Request(url, {
		method: 'post',
		postBody : poststr,
		onComplete: function() {
			hideDiv('patientDiv');						
			deleteRow('patients',id);				
	  	}
	});	
	
}

function acceptPatient(id) {
	
	var url = "/andamio/admin/DoctorAction.do?do=acceptPatient";
	var poststr = "&id=" + id;
	
	new Ajax.Request(url, {
		method: 'post',
		postBody : poststr,
		onComplete: function() {
			hideDiv('patientDiv');						
			acceptRow('patients',id);				
	  	}
	});	
	
}			
		
function addPatient(id) {
	Effect.Appear('addPatient', { duration: 0.5	, afterFinish: function(effect) { document.getElementById("inpRandomNumber").focus(); } } );					
	selectedDoctorId = id;				
}
						
function createPatient() {
	
	var id = document.getElementById("inpRandomNumber").value;
	
	var url = "/andamio/admin/DoctorAction.do?do=addPatient";
	var poststr = "&randomNumber=" + id + "&id="+selectedDoctorId;
	
	new Ajax.Request(url, {
		method: 'post',
		postBody : poststr,
		onComplete: function() {
			Effect.Fade('addPatient', { duration: 0.5	, afterFinish: function(){
				addRow('patients',id); } } );						
			document.getElementById("inpRandomNumber").value = "";
	  	}
	});	

}	
