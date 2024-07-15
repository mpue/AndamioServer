/**

	WeberknechtCMS - Open Source Content Management
	Written and maintained by Matthias Pueski 
	
	Copyright (c) 2003-2011 Matthias Pueski
	
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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.pmedv.context.AppContext;
import org.pmedv.session.SessionCollector;
import org.pmedv.session.UserSession;
import org.springframework.context.ApplicationContext;


public class AdminAction extends DispatchAction {
	
	private static final Log log = LogFactory.getLog(AdminAction.class);
	
	public ActionForward triggerSessionCollector(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		final ApplicationContext ctx = AppContext.getApplicationContext();
		final SessionCollector   collector = (SessionCollector)ctx.getBean("sessionCollector");

		for (Iterator<UserSession> sessionIterator = collector.getSessions().values().iterator();sessionIterator.hasNext();) {
			UserSession session = (UserSession) sessionIterator.next();
							
			try  {
				
				long inactiveTime = (System.currentTimeMillis() - session.getSession().getLastAccessedTime()) / 1000; 				
				log.info("User " + session.getLogin() + " is " + inactiveTime + " seconds inactive.");
				
				if ( inactiveTime > session.getSession().getMaxInactiveInterval() - 1 ) {
				
					log.info("User "+session.getLogin()+" no longer active, removing.");
					collector.getSessions().remove(session.getLogin());
					
				}						
			}
			catch (IllegalStateException i) {
				collector.getSessions().remove(session.getLogin());						
			}
				
		}
			
		log.info("SessionCollector finished.");
					
		
		
		PrintWriter out;
		
		try {
			out = response.getWriter();
			out.write("<h1>Done.</h1>");
			out.flush();			
		}
		catch (IOException e) {
			log.error("Could not write to outputstream");
		}
		
		return null;
	}
	
	public ActionForward triggerLogoutAllUsers(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		final ApplicationContext ctx = AppContext.getApplicationContext();
		final SessionCollector   collector = (SessionCollector)ctx.getBean("sessionCollector");

		log.info("Info logging out all users.");
		
		for (Iterator<UserSession> sessionIterator = collector.getSessions().values().iterator();sessionIterator.hasNext();) {
			UserSession session = (UserSession) sessionIterator.next();
							
			try  {

				log.info("Removing "+session.getLogin());
				collector.getSessions().remove(session.getLogin());
						
			}
			catch (IllegalStateException i) {
				collector.getSessions().remove(session.getLogin());						
			}
				
		}
			
		log.info("SessionCollector finished.");
					
		
		
		PrintWriter out;
		
		try {
			out = response.getWriter();
			out.write("<h1>Done.</h1>");
			out.flush();			
		}
		catch (IOException e) {
			log.error("Could not write to outputstream");
		}
		
		return null;
	}

	
}
