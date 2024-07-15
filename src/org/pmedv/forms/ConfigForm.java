/**
 * WeberknechtCMS - Open Source Content Management Written and maintained by Matthias Pueski
 * 
 * Copyright (c) 2009 Matthias Pueski
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 */
package org.pmedv.forms;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.pmedv.beans.objects.DirectoryObject;

/**
 * This is the struts model class for the base configuration of the system
 * 
 * @author Matthias Pueski
 * 
 * @version 20.3.2006
 * 
 */
public class ConfigForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	private String hostname = null;
	private String localPath = null;
	private String smtphost = null;
	private String fromadress = null;
	private String username = null;
	private String password = null;
	private String importpath = null;
	private String imagepath = null;
	private String downloadpath = null;
	private String imageurl = null;
	private String keywords = null;
	private String basepath = null;
	private String gallerypath = null;
	private String sitename = null;
	private String template = null;
	private String admintemplate = null;
	private String startnode = null;
	private String contentpath = null;
	private int maxAvatarHeight = 0;
	private int maxAvatarWidth = 0;
	private int maxImageUploadSize = 0;
	private int maxFileUploadSize = 0;

	private boolean maintenanceMode = false;

	private ArrayList<DirectoryObject> templateDirectoryContents;
	private ArrayList<DirectoryObject> adminTemplateDirectoryContents;

	/**
	 * Reset all properties to their default values.
	 * 
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	 */

	public void reset(ActionMapping mapping, HttpServletRequest request) {

		this.hostname = null;
		this.localPath = null;
		this.smtphost = null;
		this.fromadress = null;
		this.username = null;
		this.password = null;
		this.importpath = null;
		this.imagepath = null;
		this.downloadpath = null;
		this.imageurl = null;
		this.keywords = null;
		this.basepath = null;
		this.gallerypath = null;
		this.sitename = null;
		this.template = null;
		this.admintemplate = null;
		this.contentpath = null;
		this.maxAvatarHeight = 0;
		this.maxAvatarWidth = 0;
		this.maxImageUploadSize = 0;
		this.maxFileUploadSize = 0;
		this.maintenanceMode = false;

		templateDirectoryContents = new ArrayList<DirectoryObject>();
		adminTemplateDirectoryContents = new ArrayList<DirectoryObject>();

	}

	/**
	 * @return Returns the from.
	 */
	public String getFromadress() {
		return fromadress;
	}

	/**
	 * @param from The from to set.
	 */
	public void setFromadress(String fromadress) {
		this.fromadress = fromadress;
	}

	/**
	 * @return Returns the hostname.
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * @param hostname The hostname to set.
	 */
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	/**
	 * @return Returns the localPath.
	 */
	public String getLocalPath() {
		return localPath;
	}

	/**
	 * @param localPath The localPath to set.
	 */
	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return Returns the smtphost.
	 */
	public String getSmtphost() {
		return smtphost;
	}

	/**
	 * @param smtphost The smtphost to set.
	 */
	public void setSmtphost(String smtphost) {
		this.smtphost = smtphost;
	}

	/**
	 * @return Returns the username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username The username to set.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return Returns the importpath.
	 */
	public String getImportpath() {
		return importpath;
	}

	/**
	 * @param importpath The importpath to set.
	 */
	public void setImportpath(String importpath) {
		this.importpath = importpath;
	}

	/**
	 * @return Returns the imagepath.
	 */
	public String getImagepath() {
		return imagepath;
	}

	/**
	 * @param imagepath The imagepath to set.
	 */
	public void setImagepath(String imagepath) {
		this.imagepath = imagepath;
	}

	/**
	 * @return Returns the imageurl.
	 */
	public String getImageurl() {
		return imageurl;
	}

	/**
	 * @param imageurl The imageurl to set.
	 */
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	/**
	 * @return Returns the keywords.
	 */
	public String getKeywords() {
		return keywords;
	}

	/**
	 * @param keywords The keywords to set.
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	/**
	 * @return Returns the basepath.
	 */
	public String getBasepath() {
		return basepath;
	}

	/**
	 * @param basepath The basepath to set.
	 */
	public void setBasepath(String basepath) {
		this.basepath = basepath;
	}

	/**
	 * @return Returns the gallerypath.
	 */
	public String getGallerypath() {
		return gallerypath;
	}

	/**
	 * @param gallerypath The gallerypath to set.
	 */
	public void setGallerypath(String gallerypath) {
		this.gallerypath = gallerypath;
	}

	public String getSitename() {
		return sitename;
	}

	public void setSitename(String sitename) {
		this.sitename = sitename;
	}

	/**
	 * @return the current template of the site
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * @param template the template to set
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	public ArrayList<DirectoryObject> getTemplateDirectoryContents() {
		return templateDirectoryContents;
	}

	public void setTemplateDirectoryContents(ArrayList<DirectoryObject> templateDirectoryContents) {
		this.templateDirectoryContents = templateDirectoryContents;
	}

	/**
	 * @return the contents of the admin template directory
	 */
	public ArrayList<DirectoryObject> getAdminTemplateDirectoryContents() {
		return adminTemplateDirectoryContents;
	}

	/**
	 * Sets the content of the admin template directory
	 * 
	 * @param templateDirectoryContents
	 */
	public void setAdminTemplateDirectoryContents(ArrayList<DirectoryObject> templateDirectoryContents) {
		this.adminTemplateDirectoryContents = templateDirectoryContents;
	}

	/**
	 * @return the maxAvatarHeight
	 */
	public int getMaxAvatarHeight() {
		return maxAvatarHeight;
	}

	/**
	 * @param maxAvatarHeight the maxAvatarHeight to set
	 */
	public void setMaxAvatarHeight(int maxAvatarHeight) {
		this.maxAvatarHeight = maxAvatarHeight;
	}

	/**
	 * @return the maxAvatarWidth
	 */
	public int getMaxAvatarWidth() {
		return maxAvatarWidth;
	}

	/**
	 * @param maxAvatarWidth the maxAvatarWidth to set
	 */
	public void setMaxAvatarWidth(int maxAvatarWidth) {
		this.maxAvatarWidth = maxAvatarWidth;
	}

	/**
	 * @return the maxFileUploadSize
	 */
	public int getMaxFileUploadSize() {
		return maxFileUploadSize;
	}

	/**
	 * @param maxFileUploadSize the maxFileUploadSize to set
	 */
	public void setMaxFileUploadSize(int maxFileUploadSize) {
		this.maxFileUploadSize = maxFileUploadSize;
	}

	/**
	 * @return the maxImageUploadSize
	 */
	public int getMaxImageUploadSize() {
		return maxImageUploadSize;
	}

	/**
	 * @param maxImageUploadSize the maxImageUploadSize to set
	 */
	public void setMaxImageUploadSize(int maxImageUploadSize) {
		this.maxImageUploadSize = maxImageUploadSize;
	}

	/**
	 * @return the downloadpath
	 */
	public String getDownloadpath() {
		return downloadpath;
	}

	/**
	 * @param downloadpath the downloadpath to set
	 */
	public void setDownloadpath(String downloadpath) {
		this.downloadpath = downloadpath;
	}

	/**
	 * @return the admintemplate
	 */
	public String getAdmintemplate() {
		return admintemplate;
	}

	/**
	 * @param admintemplate the admintemplate to set
	 */
	public void setAdmintemplate(String admintemplate) {
		this.admintemplate = admintemplate;
	}

	/**
	 * @return the startnode
	 */
	public String getStartnode() {
		return startnode;
	}

	/**
	 * @param startnode the startnode to set
	 */
	public void setStartnode(String startnode) {
		this.startnode = startnode;
	}

	/**
	 * @return the contentpath
	 */
	public String getContentpath() {
		return contentpath;
	}

	/**
	 * @param contentpath the contentpath to set
	 */
	public void setContentpath(String contentpath) {
		this.contentpath = contentpath;
	}

	/**
	 * @return the maintenanceMode
	 */
	public boolean isMaintenanceMode() {
		return maintenanceMode;
	}

	/**
	 * @param maintenanceMode the maintenanceMode to set
	 */
	public void setMaintenanceMode(boolean maintenanceMode) {
		this.maintenanceMode = maintenanceMode;
	}

}
