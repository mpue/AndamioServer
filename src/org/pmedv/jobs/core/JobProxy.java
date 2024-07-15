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


/**
 * This is a Proxy Object for the jobs.
 * 
 * It is needed to simplify the JSON handling.
 * 
 * 
 * @author Matthias Pueski
 *
 */

public class JobProxy {

	private String name;
	private String description;
	private String fireTime;
	private int jobRunTime;
	private int progress;
	
	/**
	 * @return the progress
	 */
	public int getProgress() {
		return progress;
	}
	/**
	 * @param progress the progress to set
	 */
	public void setProgress(int progress) {
		this.progress = progress;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the fireTime
	 */
	public String getFireTime() {
		return fireTime;
	}
	/**
	 * @param fireTime the fireTime to set
	 */
	public void setFireTime(String fireTime) {
		this.fireTime = fireTime;
	}
	/**
	 * @return the jobRunTime
	 */
	public int getJobRunTime() {
		return jobRunTime;
	}
	/**
	 * @param jobRunTime the jobRunTime to set
	 */
	public void setJobRunTime(int jobRunTime) {
		this.jobRunTime = jobRunTime;
	}

}
