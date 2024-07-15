<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@page import="org.pmedv.pojos.Config"%>
<%@page import="org.pmedv.cms.daos.DAOManager"%>
<%@page import="org.pmedv.cms.daos.ConfigDAO"%>
<%@page import="org.pmedv.cms.common.CMSProperties"%>
<%@page import="org.pmedv.web.ServerUtil"%>


<%
ConfigDAO cfgDAO = DAOManager.getInstance().getConfigDAO();
Config cfg = (Config) cfgDAO.findByID(1);

if (cfg != null)
    response.sendRedirect(CMSProperties.getInstance().getAppProps().getProperty("protocol")+"://"+ServerUtil.getHostUrl(request)+"/"+cfg.getLocalPath()+cfg.getStartnode()+".html");
else
	response.sendRedirect("index.html");
%>




