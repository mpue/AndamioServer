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
package org.pmedv.jobs;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.jobs.core.CleanupJob;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.springframework.context.ApplicationContext;

/**
 * The <code>SchedulerControl</code> controls the quartz scheduler and initializes it at system
 * startup. This instance is create during startup from within the {@link ApplicationContext}
 * 
 * @author Matthias Pueski
 * 
 */
public class SchedulerControl {

	private static final Log log = LogFactory.getLog(SchedulerControl.class);

	private static Scheduler scheduler;

	/**
	 * @return the scheduler
	 */
	public Scheduler getScheduler() {
		return scheduler;
	}

	public SchedulerControl() {

		SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

		try {
			scheduler = schedFact.getScheduler();
			scheduler.start();
			log.debug("Starting scheduler.");

			if (scheduler.isStarted())
				startDefaultTaskTriggers();

		}
		catch (SchedulerException e) {
			log.error("Scheduler could not be started.");
		}

	}

	/**
	 * Start the default system tasks.
	 */
	private void startDefaultTaskTriggers() {

		/**
		 * Triggers cleanup of export directory, every 10 minutes
		 */

		SimpleTrigger cleanupTrigger = new SimpleTrigger("cleanupExport", null, new Date(), null, SimpleTrigger.REPEAT_INDEFINITELY,
				10L * 60L * 1000L);
				
		JobDetail cleanupJob = new JobDetail("cleanUp", Scheduler.DEFAULT_GROUP, CleanupJob.class);
		cleanupJob.setDescription("Periodical cleanup");

		try {
			scheduler.scheduleJob(cleanupJob, cleanupTrigger);
		}
		catch (SchedulerException e) {
			log.error("Could not start default jobs.");
		}
	}

	/**
	 * Shutdown hook for the <code>SchedulerControl</code>
	 */
	public void close() {

		try {
			log.info("Stopping scheduler.");
			scheduler.shutdown();
		}
		catch (SchedulerException e) {
			log.error("Could not stop scheduler.");
		}

	}

}
