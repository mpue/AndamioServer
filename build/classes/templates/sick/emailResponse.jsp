<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Kontaktanfrage</title>
</head>
<body bgcolor="#EEEEFF">
<div id="subject">Nachricht von : ##FROM##</div>
<p>Nachricht an  : ##CURRENTUSER##</p>
<p>Nachricht von : ##FROM##"</p>
<p>Anschrift     : ##ADDRESS##</p>
<p>Telefon       : ##TELEPHONE## </p>
<p>Telefax       : ##FAX##</p>
<p>Email         : ##EMAIL##</p>

<p>Nachricht :</p>
<p>
##MESSAGE##
</p>

</body>
</html>