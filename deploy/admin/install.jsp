<%@include file="pageheader.jsp" %>
<%@page import="org.pmedv.util.*,org.pmedv.cms.daos.*,org.pmedv.pojos.*" %>

<%

	ConfigDAO cfgDAO = DAOManager.getInstance().getConfigDAO();
	Config _config = (Config) cfgDAO.findByID(1);
	
	if (_config != null) {

%>

<jsp:forward page="Mainpage.do"></jsp:forward>

<%
	}

%>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>WeberknechtCMS Installation</title>
<link rel="stylesheet" href="./style.css" type="text/css">
</head>
<body>
	<table class="setup">
		<tr>
			<td align="left" colspan="2">
                <img src="images/header.png">				
			</td>
		</tr>
        <tr height="15px">
            <td class="text_center">
                &nbsp;           
            </td>       
        </tr>		
		<tr height="50px">
			<td class="text_center">
				<img src="./themes/experience/icons/dashboard/config.png"  border="0" align="middle">			
			</td>		
		</tr>
        <tr height="15px">
            <td class="text_center">
                &nbsp;           
            </td>       
        </tr>
		<tr>			
			<td class="text_center">
				Herzlich Willkommen bei der Installation von andamio <br><br>
				Bitte klicken sie auf "Installieren" um mit der Installation zu beginnen.<br><br>
			</td>		
		</tr>	
		<tr height="50px">
			<td class="text_center" colspan="2" valign="middle">
                <a href="Install.do?do=init"><input type="button" name="" value="Installieren" class="bs2"></a>
			</td>				
		</tr>
	</table>

</body>
</html>