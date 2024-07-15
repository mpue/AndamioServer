<div id="patientDiv" style="display:none">
	<table border="0" align="center" cellspacing="0" cellpadding="0px" width="100%" height="257px">
		<tr height="25px">
			<td align="left" colspan="2" valign="top">
				<div style="color:#000;padding-left : 120px;padding-top:4px;font-size : 10pt;">Bitte das Ziel ausw&auml;hlen</div>
			</td>
		</tr>
		<tr height="180px">
			<td align="left" valign="top">
				<div id="doctorList">
				</div>
			</td>
		</tr>
		<tr>
			<td align="center">
				<input type="button" value="Schliessen" onclick="hideDiv('patientDiv');">
			</td>
		</tr>			
	</table>
</div> 			

<div id="addPatient" style="display:none">
	<table border="0" align="center" cellspacing="0" cellpadding="0px" width="100%" height="257px">
		<tr height="25px">
			<td align="left" colspan="2" valign="top">
				<div style="color:#000;padding-left : 120px;padding-top:4px;font-size : 10pt;">Patient anlegen</div>
			</td>
		</tr>
		<tr height="180px">
			<td align="left" valign="top" colspan="2">
				<div id="patientForm">
					<table border="0">
						<tr>
							<td>
								Random Nummer
							</td>
							<td>
								<input type="text" id="inpRandomNumber" size="20" class="input">
							</td>
						</tr>
					</table>
				</div>
			</td>
		</tr>
		<tr>
			<td align="center">
				<input type="button" value="Anlegen" onclick="createPatient();">
			</td>
			<td align="center">
				<input type="button" value="Abbrechen" onclick="hideDiv('addPatient')">
			</td>
			
		</tr>			
	</table>
</div> 				
