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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.pmedv.cms.common.GlobalForwards;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.context.AppContext;
import org.pmedv.forms.AdminPanelForm;
import org.pmedv.pojos.Process;
import org.pmedv.pojos.User;
import org.pmedv.pojos.Usergroup;
import org.pmedv.session.SessionCollector;
import org.pmedv.session.UserSession;
import org.springframework.context.ApplicationContext;
 

/**
 * <p>
 * This is the administration action
 * </p>
 * <p>
 * gets all processes visible to the user and shows the administration panel.
 * </p>
 * 
 * @author Matthias Pueski
 */
public class AdministrationAction extends Action {
	
	protected static final Log log = LogFactory.getLog(AdministrationAction.class);
	
	private static final ApplicationContext ctx = AppContext.getApplicationContext();
	private static final SessionCollector collector = (SessionCollector)ctx.getBean("sessionCollector");
	
	public ActionForward execute(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		// Locale.setDefault(Locale.ENGLISH);
		
		AdminPanelForm adminPanelForm = (AdminPanelForm) form;
		ResourceBundle resources = ResourceBundle.getBundle("MessageResources",Locale.getDefault());	
		
		UserSession session = new UserSession();
		session.init(false,request);
		session.getAttributes();	
		
		// First find all existing processes and create an empty user process list.
		
		List<?> processes = DAOManager.getInstance().getProcessDAO().findAllItems();
		List<Process> userProcesses = new ArrayList<Process>();
		
		// Fetch user and finally check if he exists or has already been logged on
		
		String username = session.getLogin();
		
		if (username == null ) {
			
			if (!collector.getSessions().containsKey(username)) {
				ActionForward af = new ActionForward();
				af.setPath("/admin/LoginDialog.do?do=login");		
				return af;				
			}
			else {
				
				// create a fresh session
				
				UserSession userSession = new UserSession();
				userSession.init(true,request);
			    String permission = "true";
			    userSession.setPermission(permission);   
			    userSession.setLogin(username);
			    userSession.setAttributes();

			}
		}
		
		User user = (User)DAOManager.getInstance().getUserDAO().findByName(username);
		
		if (user != null) {
			
			// get all groups, the user belongs to
			
			Set<Usergroup> groups = user.getGroups();			
			
			// user belongs to at least one group
			
			if (groups.size() > 0) {
				
				// Iterate through all existing processes
				
				Iterator<?> processIterator = processes.iterator();
				
				while (processIterator.hasNext()) {
		
					Process currentProcess = (Process) processIterator.next();
					
					log.debug("assigned process :"+currentProcess.getName());
					
					// For each process iterate through all groups the user belongs to
					
					Iterator<Usergroup> userGroupIterator = groups.iterator();								
					
					while (userGroupIterator.hasNext()) {
						
						Usergroup currentGroup = (Usergroup) userGroupIterator.next();						
						log.debug("user belongs to group : " + currentGroup.getName());
						
						if (currentProcess.getGroups().contains(currentGroup))
							if (currentProcess.getActive() != null) 
								if (currentProcess.getActive()) {
									userProcesses.add(currentProcess);
									currentProcess.setName(resources.getString(currentProcess.getName()));
								}
					}	
				}
			}
		}
		
		// store user processes inside the form and redirect to admin panel
		
		adminPanelForm.setProcesses(userProcesses);
		adminPanelForm.setProcessCount(userProcesses.size());
		
		if (!session.permissionsAreSet()) {
			session.setProcessPermissions(userProcesses);
		}
		
		return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);
		
	}
	

}
