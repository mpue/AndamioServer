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
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.core.util.IProgressMonitor;
import org.pmedv.util.FileHelper;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CopyJob implements Job,IProgressMonitor{
	
	private static final Log log = LogFactory.getLog(CopyJob.class);
	
	private int progress;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		
		String source = dataMap.getString("source");
		String destination = dataMap.getString("destination");
				
		try {
			log.debug("Starting copy from "+source+" to "+destination);			
			FileHelper.copyFile(new File(source), new File(destination),this);			
		} 
		catch (IOException e) {
			log.debug("Error copying "+source+" to "+destination);
			e.printStackTrace();
		}
		
	}

	@Override
	public int getProgress() {
		return progress;
	}

	@Override
	public void setProgress(int progress) {
		this.progress = progress;
	}

}
