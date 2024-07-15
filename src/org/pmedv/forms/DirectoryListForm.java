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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
@SuppressWarnings("unchecked")
public class DirectoryListForm extends ActionForm {
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List directoryContents;
	private String currentDir = "";
	private String enterDir = "";
	private String newDir = "";
	private String totalSize = "";
	private int numDirs = 0;
	private int numFiles = 0;
	private String fileContent = "";
		
	
	/**
	 * @return Returns the customers.
	 */
	 public List getDirectoryContents() {
	  return directoryContents;
	 }
	 
	 /**
	 * @param customers The customers to set.
	 */
	 
	 public void setDirectoryContents(List dirctoryContents) {
	  this.directoryContents = dirctoryContents;
	 }
	 
	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		directoryContents = null;
		currentDir = null;
		enterDir = null;
		newDir   = null;
		fileContent = null;
		totalSize = "";
	}

	/**
	 * @return Returns the currentDir.
	 */
	public String getCurrentDir() {
		return currentDir;
	}

	/**
	 * @param currentDir The currentDir to set.
	 */
	public void setCurrentDir(String currentDir) {
		this.currentDir = currentDir;
	}

	/**
	 * @return Returns the enterDir.
	 */
	public String getEnterDir() {
		return enterDir;
	}

	/**
	 * @param enterDir The enterDir to set.
	 */
	public void setEnterDir(String enterDir) {
		this.enterDir = enterDir;
	}

	/**
	 * @return Returns the newDir.
	 */
	public String getNewDir() {
		return newDir;
	}

	/**
	 * @param newDir The newDir to set.
	 */
	public void setNewDir(String newDir) {
		this.newDir = newDir;
	}

	/**
	 * @return Returns the totalSize.
	 */
	public String getTotalSize() {
		return totalSize;
	}

	/**
	 * @param totalSize The totalSize to set.
	 */
	public void setTotalSize(String totalSize) {
		this.totalSize = totalSize;
	}

	/**
	 * @return Returns the numDirs.
	 */
	public int getNumDirs() {
		return numDirs;
	}

	/**
	 * @param numDirs The numDirs to set.
	 */
	public void setNumDirs(int numDirs) {
		this.numDirs = numDirs;
	}

	/**
	 * @return Returns the numFiles.
	 */
	public int getNumFiles() {
		return numFiles;
	}

	/**
	 * @param numFiles The numFiles to set.
	 */
	public void setNumFiles(int numFiles) {
		this.numFiles = numFiles;
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

}
