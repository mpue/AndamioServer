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
package org.pmedv.jobs.core;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.context.AppContext;
import org.pmedv.jobs.SchedulerControl;
import org.pmedv.session.SessionCollector;
import org.pmedv.session.UserSession;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

/**
 * This job cchecks the existing user sessions periodically. It is triggered
 * by the {@link SchedulerControl} and runs in a 10 minutes interval.
 * 
 * @author Matthias Pueski
 *
 */
public class SessionCollectorJob implements Job {
	
	private static final ApplicationContext ctx = AppContext.getApplicationContext();
	private static final SessionCollector collector = (SessionCollector)ctx.getBean("sessionCollector");
	
	private static final Log log = LogFactory.getLog(SessionCollectorJob.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

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
		
	}


}
