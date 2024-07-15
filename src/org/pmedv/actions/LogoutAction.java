/**

	WeberknechtCMS - Open Source Content Management
	Written and maintained by Matthias Pueski 
	
	Copyright (c) 2009 Matthias Pueski
	
	This program is free software; you can redistribute it and/or
	modify it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 2
	of the License, or (at your option) any later version.
	
	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

*/
package org.pmedv.actions;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.pmedv.cms.common.CMSProperties;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.context.AppContext;
import org.pmedv.forms.LogoutForm;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.User;
import org.pmedv.session.SessionCollector;
import org.pmedv.session.UserSession;
import org.pmedv.web.ServerUtil;
import org.springframework.context.ApplicationContext;
 

public class LogoutAction  extends DispatchAction {

	private static final ApplicationContext ctx = AppContext.getApplicationContext();
	private static final SessionCollector collector = (SessionCollector)ctx.getBean("sessionCollector");
	
	/**
	 * Performs logout action
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	
	public ActionForward logout(
			ActionMapping mapping,
		    ActionForm form,			
		    HttpServletRequest request,
		    HttpServletResponse response) throws Exception{
			      
    	LogoutForm logoutForm = (LogoutForm)form;
    	
    	Config config = (Config) DAOManager.getInstance().getConfigDAO().findByID(1);

    	// Clear cookies
    	
    	Cookie cookies[] = request.getCookies();

		if (cookies.length > 0) {
		
    		for ( int i = 0; i < cookies.length; i++ ) {
    			if (cookies[i].getName().equals("username")) {
    				cookies[i] = new Cookie("username",null);
    				cookies[i].setMaxAge(0);
    				response.addCookie(cookies[i]);
    			}
    			if (cookies[i].getName().equals("password")) {
    				cookies[i] = new Cookie("password",null);
    				cookies[i].setMaxAge(0);
    				response.addCookie(cookies[i]);
    			}
    		}

		}
    	    	
    	log.debug("Logging out : " + logoutForm.getUsername());
		log.debug("killing session.");		

		UserSession session = new UserSession();
		session.init(false, request);
		session.getAttributes();
		
		User user = (User)DAOManager.getInstance().getUserDAO().findByName(session.getLogin());
		
		if (user != null) {
			user.setLastActivity(new Date());
			DAOManager.getInstance().getUserDAO().update(user);
		}		

		try {
			collector.getSessions().remove(logoutForm.getUsername());			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			session.kill();	
		}
		catch (IllegalStateException e) {
			log.error("Something weird happened while killing session.");
		}

		String target = request.getParameter("target");
		
		if (target.equals("Adminpage")) {
	        
			ActionForward af = new ActionForward();

			af.setPath("/admin/Mainpage.do");

			return af;
		}
		else if (target.equals("forum")) {
	        
			ActionForward af = new ActionForward();

			af.setPath("/forum/index.jsp");

			return af;
		}
		else if (target.endsWith(".html")) {
			String redirect = CMSProperties.getInstance().getAppProps().getProperty("protocol") + "://"
				+ ServerUtil.getHostUrl(request) + "/" + config.getLocalPath() + target;					
			log.info("redirecting to : "+redirect);					
			response.sendRedirect(redirect);		
			return null;
		}
		else  {
	        ActionForward af = new ActionForward();

	        af.setPath("/index.jsp");

	        return af;
			
		}
		
	}
	
}


