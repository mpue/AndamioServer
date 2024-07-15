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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.pmedv.cms.common.GlobalForwards;
import org.pmedv.context.AppContext;
import org.pmedv.jobs.SchedulerControl;
import org.pmedv.jobs.core.CopyJob;
import org.pmedv.jobs.core.JobProxy;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;

/**
 * The job action provides all function needed to control the scheduler 
 * and the pending jobs. It provides a job list as well as the redirect 
 * to the web based job browser.
 * 
 * @author Matthias Pueski
 *
 */
public class JobAction extends ObjectListAction {

	public JobAction() {
		super("menu.jobs");
	}
	
	/**
	 * Redirects to the job browser page
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward showBrowser (
			ActionMapping mapping,
		    ActionForm form,
		    HttpServletRequest request,
		    HttpServletResponse response) throws Exception{
		
		if (!isAllowedToProcess(request, getName())) return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);
		
		return mapping.findForward(GlobalForwards.SHOW_JOB_BROWSER);
		
	}
	
	/**
	 * Creates a list of jobs coded as JSOn objects
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward list(
			ActionMapping mapping,
		    ActionForm form,
		    HttpServletRequest request,
		    HttpServletResponse response) throws Exception{
		
		if (!isAllowedToProcess(request, getName())) return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);

		SchedulerControl sc  = (SchedulerControl)AppContext.getApplicationContext().getBean("schedulerControl");
		Scheduler s = sc.getScheduler();
				
		List<?> executingJobs = s.getCurrentlyExecutingJobs();
		
		ArrayList<JobProxy> jobs = new ArrayList<JobProxy>();
		
		for (Object o : executingJobs) {
			
			JobExecutionContext j = (JobExecutionContext) o;			
			
			JobProxy jobProxy = new JobProxy();
			
			log.debug("Found job class : "+j.getJobDetail().getJobClass());
			
			if (j.getJobInstance() instanceof CopyJob) {
				
				CopyJob copyJob = (CopyJob)j.getJobInstance();				
				jobProxy.setProgress(copyJob.getProgress());
				
			}
			else
				jobProxy.setProgress(0);
			
			jobProxy.setDescription(j.getJobDetail().getDescription());
			jobProxy.setName(j.getJobDetail().getName());
			jobProxy.setJobRunTime((int)j.getJobRunTime()/1000);
			jobProxy.setFireTime(j.getFireTime().toString());
			
			jobs.add(jobProxy);
			
		}
		
		writeJSONList(JobProxy.class, jobs,true,"id", request, response);
		
		return null;
	}
	

}
