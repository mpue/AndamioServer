function openPatientInitialsEdit(centerID, patientID, patientNumber)
{
 	initials = document.getElementById('initials_'+centerID+'_'+patientID).innerHTML;
 	
 	/*	Change request: Removal of "Name"
 	patientName = document.getElementById('patientName_'+centerID+'_'+patientID).innerHTML;
 	*/
 	
 	document.getElementById('InitialsBoxPatientNumber').innerHTML = patientNumber;
 	
 	document.getElementById('InitialsFormRandomNumber').value = patientNumber;
 	
	document.getElementById('InitialsCenterCoordinates').style.visibility='visible';
	document.getElementById('InitialsCenterCoordinates').style.zIndex=1;
	document.getElementById('InitialsCloseX').style.visibility='visible';
	document.getElementById('InitialsCloseB').style.visibility='visible';
	document.getElementById('InitialsSaveB').style.visibility='visible';
	document.getElementById('InitialsDialog').style.visibility='visible';
	document.getElementById('Initials').style.visibility = 'visible';
	
	document.getElementById('InitialsFormPatientID').value=patientID;
	document.getElementById('InitialsFormCenterID').value=centerID;
	
	document.getElementById('initialsEditBox').focus();
	
	if(initials != 'empty')
		document.getElementById('initialsEditBox').value = initials;
	else
		document.getElementById('initialsEditBox').value = '';
	
	/*	Change request: Removal of "Name"
	if(patientName != 'empty')
		document.getElementById('nameEditBox').value = patientName;
	else
		document.getElementById('nameEditBox').value = '';
	*/
}

function closePatientInitialsEdit()
{
	document.getElementById('InitialsCenterCoordinates').style.visibility='visible';
	document.getElementById('InitialsCenterCoordinates').style.zIndex=-1;
	document.getElementById('InitialsCloseX').style.visibility='hidden';
	document.getElementById('InitialsCloseB').style.visibility='hidden';
	document.getElementById('InitialsSaveB').style.visibility='hidden';
	document.getElementById('InitialsDialog').style.visibility='hidden';
	document.getElementById('Initials').style.visibility='hidden';
}

function validatePatient(centerID, patientID)
{
	return (centerID != null && centerID != '' &&
		patientID != null && patientID != '');
}


function validateInitials(initials)
{
	return initials != null && initials.length == 4 &&
		initials == initials.match(/^[A-Z\u00C4\u00D6\u00DC\u00DF]+$/);
}

function validateName(name)
{
	return name != null && name.length != 0 && 
		name == name.match(/^[A-Za-z.\-\u0020\u00C4\u00D6\u00DC\u00DF]+$/);
}


function onSubmitInitials()
{
	initials = document.getElementById('initialsEditBox').value;
	
	/*	Change request: Removal of "Name"
	patientName = document.getElementById('nameEditBox').value;
	*/
	
	patientID = document.getElementById('InitialsFormPatientID').value;
	centerID = document.getElementById('InitialsFormCenterID').value;
	result = true;

	if(!validatePatient(centerID, patientID))
	{
		alert("Patient identification is incorrect!");
		result = false;
	}
	
		 
	if(result && (initials != null) && (initials.length != 0) && !validateInitials(initials))
	{
		alert("Incorrect patient initials!\nInitials must be four capital letters.");
		result = false;
	}

	/*	Change request: Removal of "Name"
	if(result && (patientName != null) && (patientName.length != 0) && !validateName(patientName))
	{
		alert("Incorrect patient name!\nName must be only letters, dot, dash and space.");
		result = false;
	}
	*/
	
	
	if(result)
	{
		var screenInitials;
		if(initials == null || initials.length == 0 )
			screenInitials = "empty"
		else
			screenInitials = initials;
			
		document.getElementById('initials_'+centerID+'_'+patientID).innerHTML = screenInitials;
		
		/*	Change request: Removal of "Name"
		var screenName;	
		if(patientName == null || patientName.length == 0)
			screenName = "empty";
		else
			screenName = patientName;
			
		document.getElementById('patientName_'+centerID+'_'+patientID).innerHTML = screenName;
		*/
			
		closePatientInitialsEdit();
	}
	
	return result;
}

function savePatientInitials()
{
	if(onSubmitInitials() == true)
		document.getElementById('initialsform').submit();
}
