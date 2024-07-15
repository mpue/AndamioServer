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

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.jobs.SchedulerControl;
import org.pmedv.pojos.Config;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * This job cleans up the export directory periodically. It is triggered
 * by the {@link SchedulerControl} and runs in a 10 minutes interval.
 * 
 * @author Matthias Pueski
 *
 */
public class CleanupJob implements Job {
	
	private static final Log log = LogFactory.getLog(CleanupJob.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		JobDataMap dataMap = context.getJobDetail().getJobDataMap();

		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);
		
		if (config != null) {

			try {
				String rootDirectory = config.getBasepath();
				
				File[] exportFiles = new File(rootDirectory + "export/").listFiles();

				for (int i = 0; i < exportFiles.length; i++) {
					
					if (!exportFiles[i].isDirectory()) 
						exportFiles[i].delete();
				}
			}
			catch (Exception e) {
				return;
			}
			
			
			log.info("Cleanup job finished.");
			
		}
		else {
			log.info("No configuration found, skipping!");
		}
		
		
	}


}
