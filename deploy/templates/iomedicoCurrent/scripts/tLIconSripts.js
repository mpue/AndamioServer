function changeTLIcon(studyNumber)
{
	iconElement = document.getElementById('TLIcon_' + studyNumber);
	iconName = iconElement.name;
	sitesNeeded = false;
	
	if(iconName == 'greenIcon')
	{
		iconElement.name = 'redIcon';
		iconElement.src = 'html/images/RedTrafficLightIcon.gif';
	}
	else
	{
		iconElement.name = 'greenIcon';
		iconElement.src = 'html/images/GreenTrafficLightIcon.gif';
		sitesNeeded = true;
	}
	
	document.getElementById('UpdateFrame').src = 'UpdateStudySitesRequest.do?studyNumber='+studyNumber+
													'&sitesNeeded='+sitesNeeded;
}