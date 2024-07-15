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
package org.pmedv.jobs.samples;

import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;

public class JobSample {

	public static void main(String[] args) throws InterruptedException {

		SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

		Scheduler sched;
		
		try {
			sched = schedFact.getScheduler();
			sched.start();

			JobDetail jobDetail = new JobDetail("myJob", Scheduler.DEFAULT_GROUP,DumbJob.class); 

			Trigger trigger = TriggerUtils.makeImmediateTrigger(3, 1000);
			trigger.setStartTime(new Date());
			trigger.setName("myTrigger");

			JobDetail anotherJobDetail = new JobDetail("anotherJob", Scheduler.DEFAULT_GROUP,HeavyJob.class);
			
			Trigger anotherTrigger = TriggerUtils.makeImmediateTrigger(10, 1000);
			anotherTrigger.setStartTime(new Date());
			anotherTrigger.setName("anotherTrigger");
			
			sched.scheduleJob(jobDetail, trigger);
			sched.scheduleJob(anotherJobDetail, anotherTrigger);
			
			System.out.println("Ready.");
			
		} 
		catch (SchedulerException e) {
			e.printStackTrace();
		}

	}

}
