<%@ taglib uri="/tags/pagetags" prefix="page" %>

		<script type="text/javascript" src="http://<page:config property="hostname"/>/<page:config property="localPath"/>scripts/MultiContactPlugin.js"></script>
		<script type="text/javascript" src="http://<page:config property="hostname"/>/<page:config property="localPath"/>scripts/prototype.js"></script>

		<input type="hidden" id="contactList" name="contactList" value=""></input>
		
		<table border="0" align="center" width="100%" style="margin-top: 15px;">

			<tr class="multiContactTitle">
				<td colspan="2" align="left" > 
					<table border="0" align="left" width="100%">
						<tr>
							<td>
								<span style="padding-left:10px">Select your country</span> 
							</td>	
							<td>
								<select name="country" onkeyup="processSelection(this.options[this.selectedIndex].value);" onchange="processSelection(this.options[this.selectedIndex].value);">
									<option value="0">Please select</option>
								</select>					
							</td>	
						</tr>	
					</table>		
				</td>		
				<td>
					<h2 style="padding-top: 10px; padding-left: 10px;">Your Partners </h2>
				</td>	
			</tr>			
			<tr>
				<td valign="top" width="60%" colspan="2">					
					<div id="contactForm">
					<table border="0" cellspacing="0" cellpadding="5" align=left width="60%">
			
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
								<input type="text" class="input" name="vorname" id="vorname" size="45">
							</td>
						</tr>
						<tr>
							<td class="text_left">
								Nachname*
							</td>
							<td>
								<input type="text" class="input" name="nachname" id="nachname" size="45">							
							</td>						
						</tr>
						<tr>
							<td class="text_left">
								Anschrift
							</td>
							<td>
								<input type="text" class="input" name="anschrift" id="anschrift" size="45">														
							</td>						
						</tr>
						<tr>
							<td class="text_left">
								Postleitzahl
							</td>
							<td>
								<input type="text" class="input" name="plz" id="plz" size="45">																					
							</td>						
						</tr>
						<tr>
							<td class="text_left">
								Ort
							</td>
							<td>
								<input type="text" class="input" name="ort" id="ort" size="45">																					
							</td>						
						</tr>
					
						<tr>
							<td class="text_left">
								Land
							</td>
							<td>
								<input type="text" class="input" name="land" id="land" size="45">
							</td>
						</tr>
						<tr>
							<td class="text_left">
								Telefon
							</td>
							<td>
								<input type="text" class="input" name="telefon" id="telefon" size="45">
							</td>
						</tr>
						<tr>
							<td class="text_left">
								Telefax
							</td>
							<td>
								<input type="text" class="input" name="telefax" id="telefax" size="45">
							</td>						
						</tr>
						<tr>
							<td class="text_left">
								eMail-Adresse*
							</td>
							<td>
								<input type="text" class="input" name="email" id="email" size="45">
							</td>						
						</tr>						
						<tr>
							<td class="text_left" valign="top">
								Ihre Nachricht*
							</td>
							<td>
								<textarea rows="8" cols="52" class="input" name="message" id="message"></textarea>
							</td>						
						</tr>						
						<tr>
							<td align="center" colspan="2">
								<input type="button" value="Absenden" class="button-top" name="send" id="send" onclick="sendMessage();">
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
				</td>
				<td valign="top">		
					<div id="users">
						<p>
							Please select a country
						</p>		
					</div>				
				</td>
			
			</tr>
		
		</table>
		
		<script type="text/javascript">
			getCountries();
		</script>
		
		
		