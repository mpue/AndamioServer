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

		if (selection =='Windows') {
			setVisible('Windows', true);
			setVisible('MacOS', false);
			setVisible('Linux', false);
		}
		else if (selection =='MacOS') {
			setVisible('MacOS', true);
			setVisible('Linux', false);
			setVisible('Windows', false);
		}	
		else {  // Linux
			setVisible('Linux', true);
			setVisible('MacOS', false);
			setVisible('Windows', false);
		}
		
	}	
	
</script>

<div id="downloadEDC">
<h1>Download und Installation des iOMEDICO Online Dokumentationssystem iostudy office edc</h1>
<p align="justify">
Die Installation erfolgt in drei Schritten. Zun&auml;chst m&uuml;ssen Sie wie beschrieben 
Java installieren bzw. pr&uuml;fen ob Java bereits auf Ihrem Rechner installiert ist.
</p>
<p align="justify">
Bitte f&uuml;hren Sie die folgenden Schritte in der beschriebenen Reihenfolge
aus. Falls Sie auf Schwierigkeiten sto&szlig;en, z&ouml;gern Sie bitte nicht, sich
jederzeit mit uns in Verbindung zu setzen!
</p>
<p align="justify">
Falls Sie auf Schwierigkeiten sto&szlig;en, z&ouml;gern Sie bitte nicht, sich jederzeit mit uns in Verbindung zu
setzen!
</p>
</div>


<p> 
<h1>Schritt 1 : Bitte w&auml;hlen sie Ihr Betriebssystem:</h1>
</p>
<p>
	<select name="type" id="selectType" onkeyup="processSelection(this.options[this.selectedIndex].value);" onchange="processSelection(this.options[this.selectedIndex].value);">
		<option value="Windows">Windows</option>
		<option value="MacOS">MacOS</option>
		<option value="Linux">Linux</option>
	</select>	
</p>

<div id="Windows">
	
	<h1>Schritt 2: Java Installation unter Windows :</h1>
	<p align="justify">
		Sie ben&ouml;tigen
		<ul>
			<li>Mindestens Windows XP</li>
			<li>JAVA 1.6 Update 10 oder h&ouml;her</li>
		</ul>
	</p>
	<p>&Uuml;berpr&uuml;fen Sie, ob Java bereits auf Ihrem Rechner installiert ist
	und wenn ja in welcher Version. &Ouml;ffnen Sie hierzu folgenden Link in
	Ihrem Standard-Internet-Browser:</p>
	<p>
		<a href="http://java.com/de/download/installed.jsp" target="_blank">http://java.com/de/download/installed.jsp</a>
	</p>	
	<p align="justify">Klicken Sie dort auf <b>&uuml;berpr&uuml;fen</b>. Die &Uuml;berpr&uuml;fung
		dauert ca 20sek bis 1 min. Nach abgeschlossener &Uuml;berpr&uuml;fung erhalten Sie
		eine Meldung &uuml;ber die installierte Java Version. Sollten Sie kein Java installiert haben oder 
		Java in einer Version &lt; 6 Update 10 installiert haben, klicken Sie auf <b>Jetzt herunterladen</b>
		und folgen Sie den Anweisungen am Bildschirm.
	</p>
	<p align="justify">		
		<b>ACHTUNG</b>: Java kann kann nur mit Administratorenrechten installiert werden!!!
	</p>	
	<p align="justify">
		Wenn Sie Java in einer Version &ge; 6 Update 10 installiert haben, m&uuml;ssen Sie kein Java mehr installieren.
	</p>
</div>
<div id="MacOS" style="display : none;">
	<h1>Schritt 2: Java Installation unter MacOS :</h1>
</div>

<div id="Linux" style="display : none;">
	<h1>Schritt 2: Java Installation unter Linux :</h1>
	<p>Die Installation von Java unter Linux kann <b>ausschliesslich mit root-Rechten erfolgen</b>.
	Hierzu kann man f&uuml;r gew&ouml;hnlich mit dem Befehl <b>su</b> Superuser werden.<p>Melden Sie sich also 
	als root an Ihrem System an, oder wechseln Sie den Benutzer mit <b>su</b>. Unter Debian und Ubuntu Linux kann 
	Java mit:</p>
	<p><b>apt-get install sun-java6-jre</b></p> installiert werden.</p>
	<p>Benutzer aller anderen Distributionen k&ouml;nnen unter folgender Adresse ein selbst-extrahierendes Archiv herunterladen:</p>
	<p>http://www.java.com/de/download/manual.jsp</p>
	<p>W&auml;hlen Sie dort den Archivtyp, der f&uuml;r Ihre Linux-Distribution in Frage kommt. Nach dem Herunterladen muss
	das Archiv noch mit dem Befehl <b>chmod 755</b> ausf&uuml;hrbar gemacht werden. Danach kann die Installation gestartet werden.</p>
	<p><b>Wichtig : </b> Dieser Installationstyp richtet keine symbolischen Links oder Pfade für Sie ein! Dies muss manuell von Ihnen 
	durchgef&uuml;hrt werden. Die Installationsroutine entpackt lediglich das Java Runtime Environment in das Verzeichnis, von dem aus 
	es gestartet wurde. </p> 
	

</div>

<div id="edc">
	<h1>Schritt 3: Installation des iOMEDICO Online Dokumentationssystems iostudy office edc</h1>
	<p align="justify">
		<ol>
			<li>Melden Sie sich mit Ihrem gewohnten Login an Ihrem Computer an.</li>
			<li>Rufen Sie folgenden Link in Ihrem Standard Internet Browser auf (z.B. im Internet Explorer, Firefox oder Safari):
				<a href="http://www.iomedico.org/edc6/io5client.jnlp" target="_blank">http://www.iomedico.org/edc6/io5client.jnlp</a> (Der Download von iostudy office edc startet automatisch (Dauer ca. 3 Minuten bei DSL))</li>		
			<li>Nach Beendigung des Downloads erhalten Sie eine Meldung, dass io5client.jar auf G&uuml;ltigkeit &uuml;berpr&uuml;ft wird.</li>
			<li>Im Fenster Warnung-Sicherheit aktivieren Sie bitte das Kontrollk&auml;stchen &quot;Inhalten dieses Urhebers immer vertrauen&quot; und klicken auf Ausf&uuml;hren.</li>		
			<li>Geben Sie Ihre pers&ouml;nliche Identifikations-Nummer (PIN) ein und best&auml;tigen Sie Ihre Eingabe mit einem Klick auf OK. (Die pers&ouml;nliche Identifikationsnummer (PIN) wurde Ihnen per Fax zugestellt.)</li>
			<li>iostudy office edc wird gestartet und Sie k&ouml;nnen Sich mit Ihren Benutzerkenndaten einloggen. Ihr Kennwort finden Sie in dem Dokument Benutzerkennung.</li>		
			<li>Bitte w&aunl;hlen Sie eine der vorgegebenen Sicherheitsfragen oder erstellen Sie Ihre eigene und beantworten diese. Falls Sie Ihr Kennwort vergessen, werden Sie dazu aufgefordert die Sicherheitsfrage zu beantworten.</li>
			<li>Nun werden Sie aufgefordert Ihr Passwort zu &auml;ndern.</li>		
			<li>Hiernach ist die Installation abgeschlossen und iostudy office EDC kann k&uuml;nftig mitteld Doppelklick auf das Desktopsymbol mit der Bezeichnung
				<b>iostudy office EDC 5.0</b> gestartet werden. Ein entsprechender Eintrag findet sich auch in Ihrem Startmen&uuml;.</li>
		</ol>
	</p>
	 
</div>

