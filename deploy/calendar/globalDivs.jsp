<a href="/andamio/calendar/events.jsp" target="calendarFrame">Show events</a>

<div id="content" class="content">
	<logic:notPresent name="permission" scope="session">
			You must be logged in in order to see the calendar.
	</logic:notPresent>
</div>

<div id="currentDate" style="display:block"></div>

<div id="divContext" style="border: 1px solid gray; display: none; position: absolute" class="shadow">
	<ul class="cmenu">
		<li class="appointment"><a id="addAppointment" href="#" onClick="createAppointment();">New Appointment</a></li>
		<li class="open"><a id="openMonth" href="#" onClick="openMonth();">Open month</a></li>
		<li class="open"><a id="openWeek" href="#" onClick="openWeek();">Open week</a></li>
		<li class="open"><a id="openDay" href="#" onClick="openDay();">Open day</a></li>		
		<li class="topSep"></li>
		<li class="delete"><a id="delete" href="#" onClick="deleteAppointment();">Delete</a></li>
	</ul>
</div>	

<div id="appointmentDiv" style="display:none" class="appointmentDiv">
	<table border="0" align="center" cellspacing="0" cellpadding="0px" width="100%" height="407px">
		<tr height="25px">
			<td align="left" colspan="2" valign="top">
				<div class="dialogTitle" id="dialogTitle"></div>
			</td>
		</tr>
		<tr valign="top">
			<td align="center" colspan="2">
				<div class="dialogSubtitle" id="dialogSubtitle"></div>
			</td>
			<td>
				<input type="hidden" id="appointmentId" name="appointmentId"/>
			</td>
		</tr>
		<tr height="190px">
			<td align="center" valign="top" colspan="2">
				<div id="appointmentForm">
					<table border="0" cellpadding="5px" cellspacing="0" align="center" wifth="95%">
						<tr>
							<td>
								Start time:
							</td>
							<td>
								<select name="startTime" id="startTime">											
								</select>										
							</td>
						</tr>				
						<tr>
							<td>
								End time:
							</td>
							<td>
								<select name="endTime" id="endTime">											
								</select>										
							</td>
						</tr>
						<tr>
							<td>
								Appointment date:
							</td>
							<td>
								<input id="calendarDate" name="calendarDate" type="text" maxlength="10" size="10" onfocus="cal.showCal(this);" />									
							</td>
						</tr>						
						<tr>
							<td>
								Color:
							</td>
							<td>
								 <input id="appColor" onClick="showDiv('selectColorDiv');">						
							</td>
						</tr>																											
						<tr>
							<td>
								Short description
							</td>
							<td>
								<input type="text" size="25" name="shortDescription" id="shortDescription" class="formInput"/>										
							</td>
						</tr>				
						<tr>
							<td colspan="2">
								Detailed description
							</td>
						</tr>				
						<tr>
							<td colspan="2">
								<textarea rows="5" cols="50" name="longDescription" id="longDescription" class="formInput"></textarea>
							</td>
						</tr>															
					</table>						
				</div>
			</td>
		</tr>
		<tr>
			<td align="center" width="50%">
				<input type="button" value="Ok" id="appSubmit" class="formButton">
			</td>
			<td align="center" width="50%">
				<input type="button" value="Cancel" id="appCancel" class="formButton">
			</td>
		</tr>			
	</table>
</div>   		
<div id="calendarDiv" style="display:none" class="createCalendarDiv">
	<table border="0" align="center" cellspacing="0" cellpadding="0px"
		width="100%" height="267px">
		<tr height="25px">
			<td align="left" colspan="2" valign="top">
				<div class="dialogTitle" id="calendarDialogTitle"></div></td>
		</tr>
		<tr valign="top">
			<td align="center" colspan="2">
				<div class="dialogSubtitle" id="calendarDialogSubtitle"></div></td>
			<td><input type="hidden" id="calendarId" name="calendarId" /></td>
		</tr>
		<tr height="130px">
			<td align="center" valign="top" colspan="2">
				<div id="calendarForm">
					<table border="0" cellpadding="5px" cellspacing="0" align="center"
						wifth="95%">
						<tr>
							<td>Name</td>
							<td><input type="text" size="25" name="name" id="name"
								class="formInput" /></td>
						</tr>
						<tr>
							<td colspan="2">Description</td>
						</tr>
						<tr>
							<td colspan="2"><textarea rows="5" cols="50"
									name="description" id="description" class="formInput"></textarea>
							</td>
						</tr>
					</table>
				</div>
			</td>
		</tr>
		<tr>
			<td align="center" width="50%"><input type="button" value="Ok"
				id="calendarAppSubmit" class="formButton">
			</td>
			<td align="center" width="50%"><input type="button"
				value="Cancel" id="calendarAppCancel"  class="formButton">
			</td>
		</tr>
	</table>
</div>
<div id="shareCalendarDiv" style="display:none" class="shareCalendarDiv">
	<table border="0" align="center" cellspacing="0" cellpadding="0px"
		width="100%" height="207px">
		<tr height="25px">
			<td align="left" colspan="2" valign="top">
				<div class="dialogTitle" id="shareCalendarDialogTitle"></div></td>
		</tr>
		<tr valign="top">
			<td align="center" colspan="2">
				<div class="dialogSubtitle" id="shareCalendarDialogSubtitle"></div></td>
			<td><input type="hidden" id="shareCalendarId" name="calendarId" /></td>
		</tr>
		<tr height="100px">
			<td align="center" valign="top" colspan="2">
				<div id="shareCalendarForm">
					<table border="0" cellpadding="5px" cellspacing="0" align="center"
						wifth="95%">
						<tr>
							<td>Select group</td>
							<td><select name="useGroup" id="useGroup" onchange="selectGroup()"></td>
						</tr>
						<tr>
							<td>Select user(s)</td>
							<td><select name="useUsers" id="useUsers" disabled="true" multiple="true"></td>
						</tr>
					</table>
				</div>
			</td>
		</tr>
		<tr>
			<td align="center" width="50%"><input type="button" value="Ok"
				id="shareCalendarAppSubmit" class="formButton" disabled="true">
			</td>
			<td align="center" width="50%"><input type="button"
				value="Cancel" id="shareCalendarAppCancel"  class="formButton">
			</td>
		</tr>
	</table>
</div>
<div id="selectColorDiv" style="display:none" class="selectColorDiv">
	<table border="0" align="center" cellspacing="0" cellpadding="0px" width="100%" height="415px">
  		<tr height="25px">
			<td align="left" colspan="2" valign="top">
				<div class="dialogTitle" id="selectColorDialogTitle">Select color</div>
			</td>
		</tr>
		<tr valign="top">
			<td align="center" colspan="2">
				<div class="dialogSubtitle" id="selectColorDialogSubtitle">Select color to use</div>
			</td>
		</tr>
		<tr>
			<td align="center" valign="top" colspan="2">
			  	<table width="375" border="0" cellspacing="1" cellpadding="0" align="center">
					
					<tr height="24">
						<td bgColor="#8a0808" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#b40404" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#df0101" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#ff0000" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#fe2e2e" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#fa5858" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#f78181" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#f5a9a9" onClick="selectColor(this.bgColor)">&nbsp;</td>
					</tr>
					<tr height="24">
						<td bgColor="#8a4b08" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#b45f04" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#df7401" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#ff8000" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#fe9a2e" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#faac58" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#f7be81" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#f5d0a9" onClick="selectColor(this.bgColor)">&nbsp;</td>
					</tr>
					<tr height="24">
						<td bgColor="#868A08" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#AEB404" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#D7DF01" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#FFFF00" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#F7FE2E" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#F4FA58" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#F3F781" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#F2F5A9" onClick="selectColor(this.bgColor)">&nbsp;</td>
					</tr>
					<tr height="24">
						<td bgColor="#4B8A08" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#5FB404" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#74DF00" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#80FF00" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#9AFE2E" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#ACFA58" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#BEF781" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#D0F5A9" onClick="selectColor(this.bgColor)">&nbsp;</td>
					</tr>
					<tr height="24">
						<td bgColor="#088A08" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#04B404" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#01DF01" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#00FF00" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#2EFE2E" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#58FA58" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#81F781" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#A9F5A9" onClick="selectColor(this.bgColor)">&nbsp;</td>
					</tr>
					<tr height="24">
						<td bgColor="#088A4B" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#04B45F" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#01DF74" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#00FF80" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#2EFE9A" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#58FAAC" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#81F7BE" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#A9F5D0" onClick="selectColor(this.bgColor)">&nbsp;</td>
					</tr>
					<tr height="24">
						<td bgColor="#088A85" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#04B4AE" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#01DFD7" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#00FFFF" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#2EFEF7" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#58FAF4" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#81F7F3" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#A9F5F2" onClick="selectColor(this.bgColor)">&nbsp;</td>
					</tr>
					<tr height="24">
						<td bgColor="#084B8A" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#045FB4" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#0174DF" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#0080FF" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#2E9AFE" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#58ACFA" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#81BEF7" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#A9D0F5" onClick="selectColor(this.bgColor)">&nbsp;</td>
					</tr>
					<tr height="24">
						<td bgColor="#08088A" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#0404B4" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#0101DF" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#0000FF" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#2E2EFE" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#5858FA" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#8181F7" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#A9A9F5" onClick="selectColor(this.bgColor)">&nbsp;</td>
					</tr>
					<tr height="24">
						<td bgColor="#4B088A" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#5F04B4" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#7401DF" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#8000FF" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#9A2EFE" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#AC58FA" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#BE81F7" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#D0A9F5" onClick="selectColor(this.bgColor)">&nbsp;</td>
					</tr>
					<tr height="24">
						<td bgColor="#8A0886" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#B404AE" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#DF01D7" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#FF00FF" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#FE2EF7" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#FA58F4" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#F781F3" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#F5A9F2" onClick="selectColor(this.bgColor)">&nbsp;</td>
					</tr>
					<tr height="24">
						<td bgColor="#8A084B" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#B4045F" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#DF0174" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#FF0080" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#FE2E9A" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#FA58AC" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#F781BE" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#F5A9D0" onClick="selectColor(this.bgColor)">&nbsp;</td>
					</tr>
					<tr height="24">
						<td bgColor="#000000" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#151515" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#2e2e2e" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#585858" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#848484" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#bdbdbd" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#e6e6e6" onClick="selectColor(this.bgColor)">&nbsp;</td>
						<td bgColor="#FFFFFF" onClick="selectColor(this.bgColor)">&nbsp;</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td align="center" width="50%"/>
			<td align="center" width="50%">
				<input type="button" value="Cancel" onClick="hideDiv('selectColorDiv')" class="formButton">
			</td>
		</tr>
	</table>	
</div>