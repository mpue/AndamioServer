<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/tags/struts-bean"  prefix="bean" %>
<%@ taglib uri="/tags/struts-html"  prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/pagetags" prefix="page" %>
<%@ page session="true" language="java" contentType="text/html; charset=ISO-8859-1" 
	pageEncoding="ISO-8859-1" import="org.pmedv.session.*"%>
	 
<html>
<head><title><page:config property="sitename"/> - Maintenance</title>
<link rel="stylesheet" href="http://<page:config property="hostname"/>/<page:config property="localPath"/>templates/kolibri/css/template.css" type="text/css">

</head>

<body style="margin : 20px">
<p>
<h1>We're sorry!</h1>
</p>
<p>
<b>
This site is currently not available due to maintenance reasons. We'll be back soon.
</b>
</p>

</body>
</html>