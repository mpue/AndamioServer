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

import org.apache.struts.action.ActionForm;

public class FileForm extends ActionForm {

	private String dir = null;
	private String filename = null;
	private String fileContent = null;
	private String newName = null;

	public void reset() {

		dir = null;
		filename = null;
		fileContent = null;
		newName = null;

	}

	/**
	 * @return Returns the dir.
	 */
	public String getDir() {
		return dir;
	}

	/**
	 * @param dir The dir to set.
	 */
	public void setDir(String dir) {
		this.dir = dir;
	}

	/**
	 * @return Returns the fileContent.
	 */
	public String getFileContent() {
		return fileContent;
	}

	/**
	 * @param fileContent The fileContent to set.
	 */
	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
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
	 * @return the new name of the file
	 */
	public String getNewName() {
		return newName;
	}

	/**
	 * Sets the new name of the file
	 * 
	 * @param newName the name to set
	 */
	public void setNewName(String newName) {
		this.newName = newName;
	}

}
