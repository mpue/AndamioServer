<table border="0" align="center" cellspacing="0" cellpadding="5px">
	<tr>
		<td><input type="button" name="createCalendar"
			id="createCalendar" class="createCalendar" value="Create calendar"
			onclick="createNewCalendar()">
		</td>
		<td id="useCalendarTD">Select calendar to use: <select name="useCalendar" id="useCalendar" onchange="selectCalendar()"></select>
		</td>
		<td><input type="button" name="shareCalendar"
			id="shareCalendar" class="shareCalendar" value="Share calendar"
			onclick="shareCalendar()">
		</td>
	</tr>
</table>
