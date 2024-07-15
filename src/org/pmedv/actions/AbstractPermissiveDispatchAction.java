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

import java.util.Iterator;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.actions.DispatchAction;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.cms.daos.ProcessDAO;
import org.pmedv.context.AppContext;
import org.pmedv.session.SessionCollector;
import org.pmedv.session.UserSession;
import org.springframework.context.ApplicationContext;
 

/**
 *
 * This is an abstract subclass of org.apache.struts.actions.DispatchAction
 * and extends it by permission checking for user actions.
 * 
 * @author Matthias Pueski
 * @see AbstractPermissiveAction
 */

public class AbstractPermissiveDispatchAction extends DispatchAction {
	
	protected String name;
	private ProcessDAO processDAO;
	private org.pmedv.pojos.Process process;

	protected static Log log = LogFactory.getLog(AbstractPermissiveDispatchAction.class);

	private static final ApplicationContext ctx = AppContext.getApplicationContext();
	private static final SessionCollector collector = (SessionCollector)ctx.getBean("sessionCollector");
	
	public AbstractPermissiveDispatchAction () {

	}
	
	/**
	 * @param name
	 */
	public AbstractPermissiveDispatchAction(String name) {
		processDAO = DAOManager.getInstance().getProcessDAO();
		this.process = (org.pmedv.pojos.Process) processDAO.findByName(name);
		this.name = name;
		
	}
	
	/**
	 * @param name
	 */
	public AbstractPermissiveDispatchAction(String name,Class<?> action) {
		processDAO = DAOManager.getInstance().getProcessDAO();
		this.process = (org.pmedv.pojos.Process) processDAO.findByName(name);
		this.name = name;
		
	}

	/**
	 * Checks if a user is allowed to request some specific process.
	 * Returns true, if so and false if not. Permissions are set set in 
	 * 
	 * org.pmedv.action.AdministrationAction.java
	 * 
	 * @param request The actual servlet request of the action
	 * @param processName the process name
	 * @return
	 */

	protected boolean isAllowedToProcess(HttpServletRequest request,String processName) {

		boolean cookieLogin = false;
		
		try {
			
			UserSession session = null;
			
			org.pmedv.pojos.Process currentProcess = (org.pmedv.pojos.Process)DAOManager.getInstance().getProcessDAO().findByName(processName);
			
			if (currentProcess.getActive() == null) return false;
			if (!currentProcess.getActive())        return false;
			
			for (int i=0;i < request.getCookies().length;i++) {
				if (request.getCookies()[i].getName().equals("sessionID")) {
					for (Iterator<?> sessionIterator = collector.getSessions().keySet().iterator();sessionIterator.hasNext();) {
						
						String key = (String) sessionIterator.next();						
						UserSession manager = (UserSession)collector.getSessions().get(key);
					
						if (manager.getSession().getId().equals(request.getCookies()[i].getValue())) {          
							session = manager;
							log.debug("Valid existing session, fetching.");
							cookieLogin = true;
						}
					}					
				}
			}

			if (session == null)
				session = new UserSession();

			if (!cookieLogin) {			
				session.init(false, request);
				session.getAttributes();
			}
						
			log.debug("Checking permission for user :"+session.getLogin());
			
			ResourceBundle resources = ResourceBundle.getBundle("MessageResources");
			String permission = (String)session.getSession().getAttribute(resources.getString(currentProcess.getName()));
			
			if (permission.equalsIgnoreCase("true")) {
				log.debug("User is allowed for : "+currentProcess.getName());
				session.getSession().setAttribute("processicon",currentProcess.getIcon() );
				session.getSession().setAttribute("processname",resources.getString(currentProcess.getName()));
				return true;
			}
			else {
				log.debug("User is not allowed to proceed.");
				return false;
			}
		} 
		catch (NullPointerException n) {
			log.debug("Something went wrong whilst logging in.");
			return false;
		}

	}
	
	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return
	 */
	public org.pmedv.pojos.Process getProcess() {
		return process;
	}

	/**
	 * @param process
	 */
	public void setProcess(org.pmedv.pojos.Process process) {
		this.process = process;
	}

}
