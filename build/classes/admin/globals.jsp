<%@page import="org.pmedv.pojos.*" %>
<%@page import="org.pmedv.cms.daos.*" %>
<%@ taglib uri="/tags/struts-bean"  prefix="bean" %>
<%@ taglib uri="/tags/struts-html"  prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/pagetags" prefix="page" %>
<%@ taglib uri="/tags/userlist-displayer" prefix="userlist" %>
<%@ taglib uri="/tags/iconbar-displayer" prefix="UI" %>
<%@ taglib uri="/tags/buttonbar-displayer" prefix="buttonbar" %>

<%@ page session="true" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 

<logic:notPresent name="permission" scope="session">
	<logic:redirect action="/admin/LoginDialog?do=login" />
</logic:notPresent>		

<%@page import="org.pmedv.forms.MainpageForm"%>
<%@page import="org.pmedv.session.*"%> 
<%@page import="java.util.*" %>
	
<%

ConfigDAO cfgDAO = DAOManager.getInstance().getConfigDAO();

Config cfg = (Config) cfgDAO.findByID(1);

String title = cfg.getSitename() + " Administration on ";
title += cfg.getHostname();
title += " - ";

if (request.getServletPath().endsWith("administration.jsp")) {
	title += "Administration";
}
else {
	title += (String) session.getAttribute("processname");	
}


title += " - User "+  session.getAttribute("login"); 
 
%>

<script type="text/javascript">		
    var processName = "<%= (String) session.getAttribute("processname") %>";
	var hostname = "<%= cfg.getHostname() %>";
	var localpath = "<%= cfg.getLocalPath()  %>";
	var docBaseUrl = "<%= "http://"+cfg.getHostname()+"/"+cfg.getLocalPath()+"/" %>"; 
	var docBaseUrlNoSlash = "<%= "http://"+cfg.getHostname()+"/"+cfg.getLocalPath()%>";
	var contentCss = "<%= "http://"+cfg.getHostname()+"/"+cfg.getLocalPath()+"templates/"+cfg.getTemplate()+"/css/template.css" %>";
	var maxInactiveInterval = <%= session.getMaxInactiveInterval() %>-5;	
</script>




