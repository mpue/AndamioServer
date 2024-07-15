
<script type="text/javascript">

	function submit() {
		
		if (document.insuranceForm.insurance.value == 1) {
			alert(unescape('Bitte w%E4hlen Sie einen g%FCltigen Kostentr%E4ger!'));
			return;
		}
		if (document.insuranceForm.startDose.value == 0) {
			alert(unescape('Bitte w%E4hlen Sie eine g%FCltige Startdosis!'));
			return;
		}
		if (document.insuranceForm.avastin.checked == false) {
			alert(unescape('Bitte best%E4tigen Sie, dass sich der Patient aktuell noch \nunter Avastin-haltiger Therapie befindet!'));
			return;
		}
		if (document.insuranceForm.therapyconform.checked == false) {
			alert(unescape('Um den Patienten zu registrieren m%FCssen Sie die Therapiekonformit%E4t best%E4tigen!'));			
		}				
		else {
			document.insuranceForm.submit();	
		}
		
	}

	var participateDiv = document.getElementById('participateDiv');

	participateDiv.style.display = '##DISPLAY##';

</script>

<form action="AvantiCSI.html" name="insuranceForm" method="post">
	<input type="hidden" name="plugin_page" value="patients">
	<input type="hidden" name="plugin_action" value="update">
	<input type="hidden" name="plugin_randomNumber" value="##RANDOMNUMBER##">
	
	<table border="0" cellpadding="0" cellspacing="5" width="80%" align="left">
		<tr>
			<td align="left" colspan="3">
				<h1>Patienten Registrieren / Bearbeiten </h1>
			</td>
		</tr>
		<tr>
			<td align="left" colspan="3">
				<hr size="1" width="100%" align="left"/>
			</td>
		</tr>
		<tr>
			<td>
				<h2>Kostentr&auml;ger</h2>
			</td>
			<td colspan="2">
				<select name="insurance">
					##INSURANCES##		
				</select>
			</td>
		</tr>
		<tr>
			<td>
				<h2>Startdosis</h2>
			</td>
			<td colspan="2">
				<select name="startDose">
					##STARTDOSE##
				</select>
			</td>
		</tr>
		<tr>
			<td>
				<h2>zulassungskonforme Therapie</h2>
			</td>
			<td colspan="2">
				##THERAPYCONFORM##			
			</td>	
		</tr>
	</table>
	
	<!-- if the patient not already participates-->
	<div id="participateDiv">
		<table border="0" cellpadding="0" cellspacing="5" width="80%" align="left">		
			<tr>
				<td colspan="3"><p align="justify">
					<h2>Nur PatientInnen die sich zum Zeitpunkt der heutigen
					Registrierung noch unter Avastin-haltiger Therapie befinden, k&ouml;nnen
					registriert werden. Befindet sich die Pat. aktuell noch unter
					Avastin-haltiger Therapie?</h2>
				</td>	
			</tr>	
			<tr>
				<td>
					<h2>Bitte best&auml;tigen</h2>
				</td>
				<td colspan="2">
					<input type="checkbox" name="avastin">
				</td>	
			</tr>
		</table>	
	</div>

	<!-- end if the patient not already participates-->

	<table border="0" cellpadding="0" cellspacing="5" width="80%" align="left">	
		<tr>
			<td align="left" colspan="3">
				<hr size="1" width="100%" align="left"/>
			</td>
		</tr>
		<tr>
			<td>
				&nbsp;
			</td>
			<td align="right" valign="middle">				 	
				<a href="#" onclick="submit();"><b>Speichern</b></a>
			</td>
			<td align="right" valign="middle">
				<a href="AvantiCSI.html&page=patients"><b>Zur&uuml;ck</b></a>
			</td>
		</tr>
		<tr>
			<td colspan="3">
				<h1>Insgesamt verabreicht  : ##CURRENTDOSE## mg</h1>
			</td>
		</tr>
	</table>			 
</form>


