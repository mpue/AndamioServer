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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.apache.struts.upload.MultipartRequestHandler;

public class UploadForm extends ActionForm {

	/**
	 * The value of the text the user has sent as form data
	 */
	protected String theText;

	/**
	 * The value of the embedded query string parameter
	 */
	protected String queryParam;

	/**
	 * Whether or not to write to a file
	 */
	protected boolean writeFile;

	/**
	 * The file that the user has uploaded
	 */
	protected FormFile theFile;

	/**
	 * The file path to write to
	 */
	protected String filePath;

	protected String directory;

	protected Boolean replaceNodes;

	/**
	 * Retrieve the value of the text the user has sent as form data
	 */
	public String getTheText() {
		return theText;
	}

	/**
	 * Set the value of the form data text
	 */
	public void setTheText(String theText) {
		this.theText = theText;
	}

	/**
	 * Retrieve the value of the query string parameter
	 */
	public String getQueryParam() {
		return queryParam;
	}

	/**
	 * Set the value of the query string parameter
	 */
	public void setQueryParam(String queryParam) {
		this.queryParam = queryParam;
	}

	/**
	 * Retrieve a representation of the file the user has uploaded
	 */
	public FormFile getTheFile() {
		return theFile;
	}

	/**
	 * Set a representation of the file the user has uploaded
	 */
	public void setTheFile(FormFile theFile) {
		this.theFile = theFile;
	}

	/**
	 * Set whether or not to write to a file
	 */
	public void setWriteFile(boolean writeFile) {
		this.writeFile = writeFile;
	}

	/**
	 * Get whether or not to write to a file
	 */
	public boolean getWriteFile() {
		return writeFile;
	}

	/**
	 * Set the path to write a file to
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * Get the path to write a file to
	 */
	public String getFilePath() {
		return filePath;
	}

	public void reset() {
		writeFile = false;
	}

	/**
	 * Check to make sure the client hasn't exceeded the maximum allowed upload
	 * size inside of this validate method.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

		ActionErrors errors = null;
		// has the maximum length been exceeded?
		Boolean maxLengthExceeded = (Boolean) request.getAttribute(MultipartRequestHandler.ATTRIBUTE_MAX_LENGTH_EXCEEDED);

		if ((maxLengthExceeded != null) && (maxLengthExceeded.booleanValue())) {
			errors = new ActionErrors();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("maxLengthExceeded"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("maxLengthExplanation"));
		}
		return errors;

	}

	/**
	 * @return Returns the directory.
	 */
	public String getDirectory() {
		return directory;
	}

	/**
	 * @param directory The directory to set.
	 */
	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public Boolean getReplaceNodes() {
		return replaceNodes;
	}

	public void setReplaceNodes(Boolean replaceNodes) {
		this.replaceNodes = replaceNodes;
	}

}
