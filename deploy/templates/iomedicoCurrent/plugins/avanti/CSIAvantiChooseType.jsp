<%@ taglib uri="/tags/pagetags" prefix="page" %>
<script type="text/javascript">

	function setVisible(docId, visible) {
		thisId = document.getElementById(docId);
		if(thisId != null) {
			if (visible)
				thisId.style.display = 'block';
			else
				thisId.style.display = 'none';
		}
	}

	function processSelection(selection) {

		if (selection =='arzt') {
			setVisible('messageKlinik', false);
			setVisible('messageArzt', true);
		}
		else {
			setVisible('messageKlinik', true);
			setVisible('messageArzt', false);

		}	
		
	}	
	
	
</script>


<h1>Teilnahme an der Cost Sharing Initiative (AVANTI)</h1>
<br/>
<h2>Schritt 1 : Bitte w&auml;hlen Sie Ihren Bereich</h2>
<form action="AvantiCSI.html" name="typeForm" method="post">
<input type="hidden" name="plugin_page" value="participate">
<table border="0" cellpadding="5" cellspacing="5" width="90%" align="left">
<tr>
	<td>
		Sie sind ein :
	</td>
	<td>
		<select name="type" id="selectType" onchange="processSelection(this.options[this.selectedIndex].value);">
			<option value="arzt">Niedergelassener Therapeut</option>
			<option value="klinik">Krankenhaus-/Ambulanzt&auml;tiger</option>
		</select>		
	</td>	
	<td align="right" valign="middle"><a href="#" onclick="document.typeForm.submit();"><img src="/<page:config property="localPath"/>templates/iomedicoCurrent/images/right16x16.png" valign="middle"/></a></td>
	<!-- <td align="right" valign="middle" width="30px"><a href="#" onclick="document.typeForm.submit();"><b>Weiter</b></a></td>  -->
	<td align="right" valign="middle" width="30px"><input type="submit" value="Weiter"></td> 
</tr>
<tr>
	<td colspan="4">
		<div id="messageKlinik">
			<p align="justify">
				<b>Hinweis</b>: Sollten Sie bei Dokumentation von Patientinnen in der
				NIS AVANTI und diese in einer Krankenhaus- oder Ambulanzumgebung
				behandelt werden, so ist ein CSI-Vertrag mit der Roche Pharma AG
				Vorraussetzung f&uuml;r eine sp&auml;tere Erstattung.
			</p>
			<p align="justify">
				Bitte &uuml;berpr&uuml;fen Sie das
				Vorliegen eines solchen Vertrages mit Ihrer Verwaltung. Bei Interesse
				oder Fragen k&ouml;nnen Sie sich gerne an Jessica Th&uuml;rmer
				(jessica.thuermer@roche.com) wenden.
			</p>
		</div>
		<div id="messageArzt">
			<p align="justify">
				<b>Hinweis</b>: Bei der Dokumentation von
				Patientinnen in der niedergelassenen Praxisumgebung ist ein Vertrag
				zwischen der Roche Pharma AG und der jeweiligen Krankenkasse der
				Patientin Voraussetzung f&uuml;r eine Erstattung.
			</p>
			<p align="justify"> 
				Bei der &quot;CSI-Registrierung&quot; von Patientinnen werden Ihnen 
				die Krankenkassen angezeigt, mit denen aktuell ein Vertrag besteht. 
				Die Liste wird laufend aktualisiert. Bitte pr&uuml;fen Sie ggf. regelm&auml;ssig f&uuml;r die
				Patientinnen, ob sich die M&ouml;glichkeit neu ergeben hat.
			</p>
		</div>
	</td>
</tr>
</table>			 
</form>

<script type="text/javascript">
	setVisible('messageKlinik', false);
</script>