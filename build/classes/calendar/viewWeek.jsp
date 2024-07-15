<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/pagetags" prefix="page"%>

<html:html xhtml="true">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>View week</title>
	<%@include file="globalJavaScripts.jsp"%>
	<script type="text/javascript"
		src="/<page:config property="localPath"/>scripts/calendar/viewWeek.js"></script>
</head>
<body>
	<logic:present name="permission" scope="session">
		<%@include file="globalHeader.jsp"%>

		<table border="0" align="center" cellspacing="0" cellpadding="5px">
			<tr>
				<td><input type="button" name="lastWeek" id="lastWeek"
					class="last" value="Last week" onclick="lastWeek()">
				</td>
				<td><input type="text" name="currentDate" id="currentDate"
					size="20" disabled="true">
				</td>
				<td><input type="button" name="nextWeek" id="nextWeek"
					class="next" value="Next week" onClick="nextWeek()">
				</td>
			</tr>
		</table>
	</logic:present>
	<%@include file="globalDivs.jsp"%>
</body>
<script type="text/javascript">
	<logic:present name="permission" scope="session">
		getCalendars();
// 		init();
		isAdmin();
	</logic:present>
</script>
</html:html>