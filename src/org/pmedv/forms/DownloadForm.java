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
package org.pmedv.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.apache.struts.upload.MultipartRequestHandler;

public class DownloadForm {

	private String name = null;

	private String description = null;

	private String path = null;

	private String filename = null;

	private int hits = 0;
	
	private FormFile myFile = null;

	/**
	 * Reset all properties to their default values.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	 */

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		name = null;
		description = null;
		path = null;
		filename = null;
		myFile = null;
		hits = 0;
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
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return Returns the filename.
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename The filename to set.
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @return Returns the path.
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path The path to set.
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return Returns the myFile.
	 */
	public FormFile getMyFile() {
		return myFile;
	}

	/**
	 * @param myFile The myFile to set.
	 */
	public void setMyFile(FormFile myFile) {
		this.myFile = myFile;
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {

		ActionErrors errors = new ActionErrors();
		//has the maximum length been exceeded?
		Boolean maxLengthExceeded = (Boolean) request
				.getAttribute(MultipartRequestHandler.ATTRIBUTE_MAX_LENGTH_EXCEEDED);

		if ((maxLengthExceeded != null) && (maxLengthExceeded.booleanValue())) {
			
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"maxLengthExceeded"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"maxLengthExplanation"));
		}

		if (getDescription() == null || getDescription().length() < 1) {
			errors.add("description", new ActionMessage(
					"error.description.required"));
		}
		if (getName() == null || getName().length() < 1) {
			errors.add("name", new ActionMessage("error.name.required"));
		}

		return errors;

	}

	/**
	 * @return the hits
	 */
	public int getHits() {
		return hits;
	}

	/**
	 * @param hits the hits to set
	 */
	public void setHits(int hits) {
		this.hits = hits;
	}

}
