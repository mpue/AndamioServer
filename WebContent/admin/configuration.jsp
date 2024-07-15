<%@include file="pageheader.jsp" %>

   <html:html locale="true">
   <head>

   <title>Konfiguration</title>
	    <html:base/>
		<link rel="stylesheet" href="./style.css" type="text/css">
   </head>


	<body>
	    <html:form action="/admin/Install.do">
	    <html:hidden property="do" value="saveConfig"/>

		<table width="750px" class="setup">
	       <tr>
	           <td align="left" colspan="2">
	               <img src="images/header.png">               
	           </td>
	       </tr>
	       <tr height="15px">
	           <td class="text_center" colspan="2">
	               &nbsp;           
	           </td>       
	       </tr>       
	       <tr height="50px">
	           <td class="text_center" colspan="2">
	               <img src="./themes/experience/icons/dashboard/config.png"  border="0" align="middle">           
	           </td>       
	       </tr>
	       <tr height="15px">
	           <td class="text_center" colspan="2">
	               &nbsp;           
	           </td>       
	       </tr>
			<tr height="30px">
				<td class="text_center" colspan="2">
					<h2>Systemkonfiguration</h2>
				</td>
			</tr>
			<tr>
				<td colspan="2" class="text_left">
					Hier k&ouml;nnen Sie die Konfiguration Ihres Systems durchf&uuml;hren,
					bitte f&uuml;llen Sie das Formular so vollständig wie möglich aus.<br><br>
					Sie können die Einstellungen auch zu einem späteren Zeitpunkt vornehmen,
					beachten Sie aber, da&szlig; das System erst einwandfrei funktioniert,
					wenn alle erforderlichen Einstellungen vorgenommen wurden.  
				</td>
			</tr>
            <tr height="15px">
                <td class="text_center" colspan="2">
                    &nbsp;
                </td>
            </tr>			
			<tr height="30px">
				<td class="text_left">
					Seitenbezeichnung
				</td>
				<td class="text_left">
					<html:text property="sitename" styleClass="bs1" size="60"/> 
				</td>
			</tr>
			<tr height="30px">
				<td class="text_left">
					Hostname
				</td>
				<td class="text_left">
					<html:text property="hostname" styleClass="bs1" size="60"/> 
				</td>
			</tr>
			<tr height="30px">
				<td class="text_left">
					Lokaler Pfad
				</td>
				<td class="text_left">
					<html:text property="localPath" styleClass="bs1" size="60"/> 
				</td>
			</tr>	
			<tr height="30px">
				<td class="text_left">
					SMTP Server
				</td>
				<td class="text_left">
					<html:text property="smtphost" styleClass="bs1" size="60"/> 
				</td>
			</tr>	
			<tr height="30px">
				<td class="text_left">
					Mail Absender
				</td>
				<td class="text_left">
					<html:text property="fromadress" styleClass="bs1" size="60"/> 
				</td>
			</tr>	
			<tr height="30px">
				<td class="text_left">
					SMTP Benutzername
				</td>
				<td class="text_left">
					<html:text property="username" styleClass="bs1" size="60"/> 
				</td>
			</tr>	
			<tr height="30px">
				<td class="text_left">
					SMTP Passwort
				</td>
				<td class="text_left">
					<html:text property="password" styleClass="bs1" size="60"/> 
				</td>
			</tr>	
			<tr height="30px">
				<td class="text_left">
					Basispfad
				</td>
				<td class="text_left">
					<html:text property="basepath" styleClass="bs1" size="60"/> 
				</td>
			</tr>
			<tr height="30px">
				<td class="text_left">
					Admin eMail
				</td>
				<td class="text_left">
					<html:text property="adminemail" styleClass="bs1" size="60"/> 
				</td>
			</tr>
			<tr height="30px">
				<td class="text_left">
					Admin Passwort
				</td>
				<td class="text_left">
					<html:text property="adminpass" styleClass="bs1" size="60"/> 
				</td>
			</tr>
             <tr height="10px">
                 <td class="text_left" colspan="2">
                     <html:hidden property="dbUser"/>
                     <html:hidden property="dbPass"/>
                     <html:hidden property="dbUrl"/>  
                 </td>
             </tr>       		
			<tr height="50px">
				<td class="text_center" colspan="2">
					<html:submit styleClass="bs2">Installieren</html:submit>
				</td>			
			</tr>
		</table>
   </html:form>
   </body>
</html:html>
