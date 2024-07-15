/**
 * WeberknechtCMS - Open Source Content Management Written and maintained by Matthias Pueski
 * 
 * Copyright (c) 2009 Matthias Pueski
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 */
package org.pmedv.actions;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.pmedv.cms.common.CMSProperties;
import org.pmedv.cms.common.GlobalForwards;
import org.pmedv.cms.common.Params;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.context.AppContext;
import org.pmedv.core.crypto.MD5Crypter;
import org.pmedv.forms.LoginForm;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.User;
import org.pmedv.pojos.calendar.Calendar;
import org.pmedv.session.SessionCollector;
import org.pmedv.session.UserSession;
import org.pmedv.web.ServerUtil;
import org.springframework.context.ApplicationContext;

/**
 * <p>
 * The <code>LoginAction</code> takes care of the user login.
 * </p>
 * <p>
 * This version contains a check against the ioStudy Office EDC system. Thus any user known to the
 * EDC can login with the edc login credentials.
 * </p>
 * 
 * @author Matthias Pueski
 * 
 */
public class LoginAction extends DispatchAction {

	private static final ApplicationContext ctx = AppContext.getApplicationContext();
	private static final SessionCollector collector = (SessionCollector) ctx.getBean("sessionCollector");
	private static final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy kk:mm");
	private static final ResourceBundle resources = ResourceBundle.getBundle("MessageResources");
	
	boolean propsLoaded = false;
	boolean cookiesEnabled = false;

	public LoginAction() {
		
		if (CMSProperties.getInstance().isPropsLoaded()) {
			cookiesEnabled = Boolean.valueOf(CMSProperties.getInstance().getAppProps().getProperty("cookiesEnabled"));	
		}
		
	}

	/**
	 * Perform password check and login action
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward login(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		for (Iterator<String> sessionIterator = collector.getSessions().keySet().iterator(); sessionIterator.hasNext();) {

			String key = (String) sessionIterator.next();

			UserSession manager = (UserSession) collector.getSessions().get(key);
			log.debug("Found existing sessionID :" + manager.getSession().getId());

		}

		LoginForm loginForm = (LoginForm) form;
		Config config = (Config) DAOManager.getInstance().getConfigDAO().findByID(1);

		// Fetch login credentials from login form and assign to locals

		String target = "";

		String username = null;
		String password = null;

		boolean alreadyCrypted = false;

		try {

			if (request.getParameter(Params.TARGET) != null)
				target = request.getParameter(Params.TARGET);

			// Check all cookies and get username and pass from cookie, if it exists
			if (cookiesEnabled) {
				Cookie cookies[] = request.getCookies();
				if (cookies != null && cookies.length > 0) {
					log.debug("reading cookies");
					for (int i = 0; i < cookies.length; i++) {
						if (cookies[i].getName().equals("username")) {
							username = cookies[i].getValue();
						}
						if (cookies[i].getName().equals("password")) {
							password = cookies[i].getValue();
							alreadyCrypted = true;
						}
					}
				}

			}

			// if they are null, there are no corresponding cookies, so we fetch the login
			// credentials from the form

			log.debug("username : " + username);
			log.debug("password : " + password);

			if (username == null) {
				log.debug("Username is null, fetching from form");
				username = loginForm.getUsername();
			}
			if (password == null) {
				log.debug("Password is null, fetching from form");
				password = loginForm.getPassword();
			}

			// If they are still null, the were not given and we redirect to the login dialog

			if (username == null || password == null) {

				log.debug("username/password still null, trying to read request parameters.");

				if ((request.getParameter("username") != null) && (request.getParameter("password") != null)) {
					username = request.getParameter("username");
					password = request.getParameter("password");
				}
				else
					return mapping.findForward(GlobalForwards.SHOW_LOGIN_DIALOG);
			}

			
			// Check if user exists and he is active
			User user = (User) DAOManager.getInstance().getUserDAO().findByName(username);

			if (user == null || !user.getActive()) {
				if (target != null && target.equalsIgnoreCase("frontpage")) {
					log.debug("Login from Mainpage failed");
					request.getSession().setAttribute("login", "failed");
					ActionForward af = new ActionForward();
					af.setPath("/index.jsp");
					return af;
				}
				else {
					return mapping.findForward(GlobalForwards.SHOW_LOGIN_DIALOG);
				}
			}

			// Create MD5 signature from password if password was not taken from cookie and compare
			// to the key from the db

			String encryptedPassword = null;

			if (!alreadyCrypted) {
				byte[] passWordHash = MD5Crypter.createMD5key(password);
				encryptedPassword = MD5Crypter.createMD5String(passWordHash);
			}
			else {
				encryptedPassword = password;
			}

			String encryptedPassFromDB = user.getPassword();

			if (encryptedPassword.equals(encryptedPassFromDB)) {
				
				request.getSession().setAttribute("login", "success");
				UserSession session = new UserSession();
				session.init(true, request);
				String permission = "true";
				session.setPermission(permission);
				session.setLogin(username);
				session.setPass(password);
				session.setAttributes();
				session.setUser(user);
				
				request.getSession().setAttribute("userId", user.getId());
				request.getSession().setAttribute("fullUserName", user.getLastName() + ", " + user.getFirstName());
				
				Long currentCal = null;
				if (user.getCalendars() != null){
				for (Calendar cal : user.getCalendars()) {
					if (cal.getName().equals(user.getName() + ".default")){
						currentCal = cal.getId();
						break;
					}					
				}
				if (currentCal == null)
					for (Calendar cal : user.getCalendars()) {
							currentCal = cal.getId();
							break;			
					}
				}
				request.getSession().setAttribute("currentCal", currentCal);
				
				if (user.getLastActivity() != null)
					request.getSession().setAttribute("lastActivity", dateFormat.format(user.getLastActivity()));
				else
					request.getSession().setAttribute("lastActivity", resources.getString("never"));

				// Create cookies for permanent login

				if (cookiesEnabled) {
					String sessionID = session.getSession().getId();

					Cookie nameCookie = new Cookie("username", username);
					Cookie passCookie = new Cookie("password", encryptedPassFromDB);

					log.debug("SessionID : " + sessionID);

					Cookie sessionCookie = new Cookie("sessionID", sessionID);

					nameCookie.setMaxAge(60 * 60 * 24 * 365);
					passCookie.setMaxAge(60 * 60 * 24 * 365);
					sessionCookie.setMaxAge(60 * 60 * 24 * 365);

					response.addCookie(nameCookie);
					response.addCookie(passCookie);
					response.addCookie(sessionCookie);
				}

				session.getSession().setAttribute("config", config);
				session.getSession().setAttribute("template", config.getAdmintemplate());

				if (!collector.getSessions().containsKey(username)) {
					collector.getSessions().put(username, session);
				}
				else {
					collector.getSessions().remove(username);
					collector.getSessions().put(username, session);
				}

				if (target.equalsIgnoreCase("frontpage")) {
					ActionForward af = new ActionForward();
					af.setPath("/index.jsp");
					log.debug("Logging in from Mainpage");

					return af;
				}
				else if (target.equalsIgnoreCase("forum")) {
					ActionForward af = new ActionForward();
					af.setPath("/forum/index.jsp");
					log.debug("Logging in from Forum");

					return af;
				}
				else if (target.endsWith(".html")) {
					String redirect = CMSProperties.getInstance().getAppProps().getProperty("protocol") + "://"
						+ ServerUtil.getHostUrl(request) + "/" + config.getLocalPath() + target;					
					log.info("redirecting to : "+redirect);					
					response.sendRedirect(redirect);		
					return null;
				}
				else {

					if (request.getParameter("async") != null) {

						PrintWriter out;

						try {
							out = response.getWriter();
							out.print("{\"success\":true,\"message\":\"Login erfolgreich.\"}");
							out.flush();

						}
						catch (IOException e) {
							e.printStackTrace();
						}
						return null;
					}
					else
						return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);
				}

			}
			else {

				log.info("Login failed.");
				request.getSession().setAttribute("login", "failed");

				if (target.equalsIgnoreCase("frontpage")) {
					log.debug("Login from Mainpage failed");
					ActionForward af = new ActionForward();
					af.setPath("/index.jsp");
					return af;
				}
				else if (target.equalsIgnoreCase("forum")) {
					log.debug("Login from Mainpage failed");
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
				else {
					if (request.getParameter("async") != null) {

						PrintWriter out;

						try {
							out = response.getWriter();
							out.print("{\"failure\":true,\"message\":\"Login fehlgeschlagen.\"}");
							out.flush();
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						return null;
					}
					else {
						log.debug("Login from Admin Panel failed");
						return mapping.findForward(GlobalForwards.SHOW_LOGIN_DIALOG);
					}
				}
			}
		}
		catch (NullPointerException n) {

			log.info("Login failed.");
			request.getSession().setAttribute("login", "failed");

			n.printStackTrace();

			if (target.equalsIgnoreCase("frontpage")) {
				log.debug("Login from Mainpage failed");
				ActionForward af = new ActionForward();
				af.setPath("/index.jsp");
				return af;
			}
			else {
				log.debug("Login from Admin Panel failed");
				return mapping.findForward(GlobalForwards.SHOW_LOGIN_DIALOG);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public ActionForward logout(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		UserSession session = new UserSession();

		session.init(false, request);
		session.getAttributes();

		User user = (User)DAOManager.getInstance().getUserDAO().findByName(session.getLogin());
		
		if (user != null) {
			user.setLastActivity(new Date());
			DAOManager.getInstance().getUserDAO().update(user);
		}
		
		collector.getSessions().remove(session.getLogin());
		session.kill();

		return mapping.findForward(GlobalForwards.SHOW_LOGIN_DIALOG);
	}

}
