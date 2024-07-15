<%@ taglib uri="/tags/pagetags" prefix="page" %>

		<script type="text/javascript" src="http://<page:config property="hostname"/>/<page:config property="localPath"/>ScriptServlet?name=contactPlugin.js"></script>
		<div id="contactForm">
		
		<table border="0" cellspacing="0" cellpadding="5" align=left>

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
					<input type="text" class="inputBox" name="vorname" id="vorname" size="52">
				</td>
			</tr>
			<tr>
				<td class="text_left">
					Nachname*
				</td>
				<td>
					<input type="text" class="inputBox" name="nachname" id="nachname" size="52">							
				</td>						
			</tr>
			<tr>
				<td class="text_left">
					Anschrift
				</td>
				<td>
					<input type="text" class="inputBox" name="anschrift" id="anschrift" size="52">														
				</td>						
			</tr>
			<tr>
				<td class="text_left">
					Postleitzahl
				</td>
				<td>
					<input type="text" class="inputBox" name="plz" id="plz" size="52">																					
				</td>						
			</tr>
			<tr>
				<td class="text_left">
					Ort
				</td>
				<td>
					<input type="text" class="inputBox" name="ort" id="ort" size="52">																					
				</td>						
			</tr>
		
			<tr>
				<td class="text_left">
					Land
				</td>
				<td>
					<input type="text" class="inputBox" name="land" id="land" size="52">
				</td>
			</tr>
			<tr>
				<td class="text_left">
					Telefon
				</td>
				<td>
					<input type="text" class="inputBox" name="telefon" id="telefon" size="52">
				</td>
			</tr>
			<tr>
				<td class="text_left">
					Telefax
				</td>
				<td>
					<input type="text" class="inputBox" name="telefax" id="telefax" size="52">
				</td>						
			</tr>
			<tr>
				<td class="text_left">
					eMail-Adresse*
				</td>
				<td>
					<input type="text" class="inputBox" name="email" id="email" size="52">
				</td>						
			</tr>						
			<tr>
				<td class="text_left" valign="top">
					Ihre Nachricht*
				</td>
				<td>
					<textarea rows="8" cols="50" class="inputBox" name="message" id="message"></textarea>
				</td>						
			</tr>						
			<tr>
				<td align="center" colspan="2">
					<a class="button" href="#" onclick="this.blur();sendMessage();return false;"><span>Absenden</span></a>
				</td>	
			</tr>
		</table>		
		</div>
		<div id="messageAfterSend" style="display:none;">
		<p><b>Vielen Dank!</b></p>
		<p>
			Wir haben Ihre Nachricht erhalten und werden uns in K&uuml;rze mit Ihnen in Verbindung setzen.
		</p>
		<p>Herzlichst, Ihr iOMEDICO-Team</p>	
		<p align="center"><a href="Willkommen.html" target="_self">Zur&uuml;ck zur Hauptseite</a></p>
		</div>
		