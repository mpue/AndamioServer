<%@ taglib uri="/tags/pagetags" prefix="page" %>

		<script type="text/javascript" src="http://<page:config property="hostname"/>/<page:config property="localPath"/>scripts/contactPlugin.js"></script>
		<div id="contactForm">
		
		<table border="0" cellspacing="0" cellpadding="5" align=center width="60%">

            <tr>
                <td colspan="2">
                    <p>
                    Hier k&ouml;nnen Sie unkompliziert Kontakt zu uns aufnehmen, f&uuml;llen Sie hierzu das Formular so vollst&auml;ndig wie
                    m&ouml;glich aus.</p>
                    <p>
                    Die mit einem Stern gekennzeichneten Felder sind Pflichtfelder.
                    </p>
                </td>
            </tr>
			<tr>
				<td class="text_left">
					Vorname*
				</td>
				<td class="text_left">
					<input type="text" class="bs2" name="vorname" id="vorname" size="52">
				</td>
			</tr>
			<tr>
				<td class="text_left">
					Nachname*
				</td>
				<td>
					<input type="text" class="bs2" name="nachname" id="nachname" size="52">							
				</td>						
			</tr>
			<tr>
				<td class="text_left">
					Anschrift
				</td>
				<td>
					<input type="text" class="bs2" name="anschrift" id="anschrift" size="52">														
				</td>						
			</tr>
			<tr>
				<td class="text_left">
					Postleitzahl
				</td>
				<td>
					<input type="text" class="bs2" name="plz" id="plz" size="52">																					
				</td>						
			</tr>
			<tr>
				<td class="text_left">
					Ort
				</td>
				<td>
					<input type="text" class="bs2" name="ort" id="ort" size="52">																					
				</td>						
			</tr>
		
			<tr>
				<td class="text_left">
					Land
				</td>
				<td>
					<input type="text" class="bs2" name="land" id="land" size="52">
				</td>
			</tr>
			<tr>
				<td class="text_left">
					Telefon
				</td>
				<td>
					<input type="text" class="bs2" name="telefon" id="telefon" size="52">
				</td>
			</tr>
			<tr>
				<td class="text_left">
					Telefax
				</td>
				<td>
					<input type="text" class="bs2" name="telefax" id="telefax" size="52">
				</td>						
			</tr>
			<tr>
				<td class="text_left">
					eMail-Adresse*
				</td>
				<td>
					<input type="text" class="bs2" name="email" id="email" size="52">
				</td>						
			</tr>						
			<tr>
				<td class="text_left" valign="top">
					Ihre Nachricht*
				</td>
				<td>
					<textarea rows="8" cols="50" class="bs2" name="message" id="message"></textarea>
				</td>						
			</tr>						
			<tr>
				<td align="center" colspan="2">
					<input type="button" value="Absenden" class="bs2" name="send" id="send" onclick="sendMessage();">
				</td>	
			</tr>
		</table>		
		</div>
		<div id="messageAfterSend" style="display:none;">
		<p><b>Vielen Dank!</b></p>
		<p>
			Wir haben Ihre Nachricht erhalten und werden uns in K&uuml;rze mit Ihnen in Verbindung setzen.
		</p>
		<p>Herzlichst, Ihr Weberknecht-Team</p>	
		<p align="center"><a href="Willkommen.html" target="_self">Zur&uuml;ck zur Hauptseite</a></p>
		</div>
		