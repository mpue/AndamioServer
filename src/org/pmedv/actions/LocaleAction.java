package org.pmedv.actions;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.pmedv.cms.common.CMSProperties;
import org.pmedv.cms.daos.ConfigDAO;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.Config;
import org.pmedv.web.ServerUtil;


public class LocaleAction extends DispatchAction {
	
	private static final Log log = LogFactory.getLog(LocaleAction.class);
	
	public ActionForward setLocale(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String locale = request.getParameter("locale");
		String path   = request.getParameter("path");
		
		ConfigDAO cfgDAO = DAOManager.getInstance().getConfigDAO();
		Config cfg = (Config) cfgDAO.findByID(1);
		
		if (locale != null)  {
			
			HttpSession session = request.getSession();
			
			// TODO : If locale is the systems default, set it back to null!
			// -> System.getProperty("user.language")
			
			if (session != null) {
				Locale l = new Locale(locale);
				log.info("setting locale to : "+locale);
				session.setAttribute("currentLocale",l);
				Locale.setDefault(l);
			}
			
		}
		
		String protocol = CMSProperties.getInstance().getAppProps().getProperty("protocol");

		if (cfg != null && path != null)
		    response.sendRedirect(protocol+"://"+ServerUtil.getHostUrl(request)+"/"+cfg.getLocalPath()+path+".html");
		else
			response.sendRedirect("index.jsp");
		
		return null;
 
	}

}
