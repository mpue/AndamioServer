<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>				
				
<script type="text/javascript">

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

		document.location.href = 'AvantiCSI.html&plugin_page=editPatient&plugin_randomNumber=='+randomNumber;
		
	}
	else {

		button.value = 'Registrieren';		

		var checkboxid = 'cCsiParticipant_'+randomNumber;
		var checkbox = document.getElementById(checkboxid);

		if (checkbox.checked == false)
			checkbox.checked = true;
		else
			checkbox.checked = false; 

		var poststr = "plugin_randomNumber="+randomNumber;
		makePOSTRequest('AvantiCSI.html&plugin_page=toggleActive&', poststr);

	}

	
}

</script>
				
				
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
	
	span.pagebanner {
		padding-top: 10px;	
	}
	
	input.portalButton {
		text-align : center;
	    background-color : #d6e3f4;
	    border: 1px solid #000000;
	}

</style>				
<h1>&Uuml;bersicht Ihrer Avanti CSI Teilnehmer</h1>					
<hr size="1" width="90%" align="left"/>				
<display:table id="row" 
		name="sessionScope.Patients" 
		class="Patients" 		
		decorator="org.pmedv.decorator.PatientDecorator" 
		requestURI="/AvantiCSI.html" 
		pagesize="20">
		
	<display:column property="randomNumber" title="Patienten ID" sortable="true" class="randomNumber" />
	<display:column property="lastDoc" title="letzte Dokumentation" sortable="true" class="birthDate"/>
	<display:column property="active" title="Teilnahme CSI?*" sortable="false" class="active"/>
	<display:column property="registerDeregisterLink" title="" sortable="false" class="button"/>
	<display:column property="editLink" title="" sortable="false" class="button"/>
	
	<display:setProperty name="paging.banner.placement" value="bottom" />	
	<display:setProperty name="paging.banner.full">
		<hr size="1" width="90%" align="left"/>
	 	<span class="pagelinks"> [<a href="{1}">Erste</a>/ <a href="{2}">Vorige</a>] {0} [ <a href="{3}">N&auml;chste</a>/ <a href="{4}">Letzte </a>]</span>
	</display:setProperty>
	<display:setProperty name="paging.banner.first">
		<hr size="1" width="90%" align="left"/>
		<span class="pagelinks"> [Erste/Vorige] {0} [ <a href="{3}">N&auml;chste</a>/ <a href="{4}">Letzte</a>] </span>
	</display:setProperty>	
	<display:setProperty name="paging.banner.last">
		<hr size="1" width="90%" align="left"/>
		<span class="pagelinks">[ <a href="{1}">Erste</a>/ <a href="{2}">Vorige</a>] {0} [N&auml;chste/Letzte] </span>
	</display:setProperty>
	<display:setProperty name="paging.banner.item_name">
		Patient		
	</display:setProperty>
	<display:setProperty name="paging.banner.items_name">
		Patienten
	</display:setProperty>
	<display:setProperty name="paging.banner.no_items_found">
		<span class="pagebanner"> Keine {0} gefunden. </span>
	</display:setProperty>
	<display:setProperty name="paging.banner.one_item_found">
		<span class="pagebanner"> Ein {0} gefunden. </span>
	</display:setProperty>
	<display:setProperty name="paging.banner.all_items_found">
		<span class="pagebanner"> {0} {1} gefunden.</span>  	
	</display:setProperty>
	<display:setProperty name="paging.banner.some_items_found">
		<span class="pagebanner"> {0} {1} gefunden, zeige {2} bis {3}. </span>  	
	</display:setProperty>
	<display:setProperty name="basic.msg.empty_list">
		<span class="pagebanner"> Keine Patienten gefunden.</span>
	</display:setProperty>
	
</display:table>
<p>
</p>
<p><i>*Patienten, die bereits mindestens 10g Bevacizumab erhalten haben, sowie Patienten die die Therapie abgebrochen oder beendet haben k&ouml;nnen nicht mehr registriert oder deregistriert werden.</i></p>
