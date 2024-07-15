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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.pmedv.beans.objects.DirectoryObject;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.Config;

public class ConfigShowForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	private ArrayList<DirectoryObject> templateDirectoryContents;
	private ArrayList<DirectoryObject> adminTemplateDirectoryContents;
	private List<?> availableNodes = DAOManager.getInstance().getNodeDAO().findAllItems();

	/**
	 * Reset all properties to their default values.
	 * 
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	 */

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		config = new Config();
	}

	private Config config;

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public boolean equals(Object rhs) {
		return config.equals(rhs);
	}

	public int hashCode() {
		assert false : "hashCode not designed";
		return 42;
	}

	public long getId() {
		return config.getId();
	}

	public void setID(long id) {
		config.setId(id);
	}

	/**
	 * @return Returns the from.
	 */
	public String getFromadress() {
		return config.getFromadress();
	}

	/**
	 * @param from The from to set.
	 */
	public void setFromadress(String fromadress) {
		config.setFromadress(fromadress);
	}

	/**
	 * @return Returns the hostname.
	 */
	public String getHostname() {
		return config.getHostname();
	}

	/**
	 * @param hostname The hostname to set.
	 */
	public void setHostname(String hostname) {
		config.setHostname(hostname);
	}

	/**
	 * @return Returns the localPath.
	 */
	public String getLocalPath() {
		return config.getLocalPath();
	}

	/**
	 * @param localPath The localPath to set.
	 * 
	 */
	public void setLocalPath(String localPath) {
		config.setLocalPath(localPath);
	}

	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return config.getPassword();
	}

	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		config.setPassword(password);
	}

	/**
	 * @return Returns the smtphost.
	 */
	public String getSmtphost() {
		return config.getSmtphost();
	}

	/**
	 * @param smtphost The smtphost to set.
	 */
	public void setSmtphost(String smtphost) {
		config.setSmtphost(smtphost);
	}

	/**
	 * @return Returns the username.
	 */
	public String getUsername() {
		return config.getUsername();
	}

	/**
	 * @param username The username to set.
	 */
	public void setUsername(String username) {
		config.setUsername(username);
	}

	/**
	 * @return Returns the importpath.
	 */
	public String getImportpath() {
		return config.getImportpath();
	}

	/**
	 * @param importpath The importpath to set.
	 */
	public void setImportpath(String importpath) {
		config.setImportpath(importpath);
	}

	/**
	 * @return Returns the imagepath.
	 */
	public String getImagepath() {
		return config.getImagepath();
	}

	/**
	 * @param imagepath The imagepath to set.
	 */
	public void setImagepath(String imagepath) {
		config.setImagepath(imagepath);
	}

	/**
	 * @return Returns the imageurl.
	 */
	public String getImageurl() {
		return config.getImageurl();
	}

	/**
	 * @param imageurl The imageurl to set.
	 */
	public void setImageurl(String imageurl) {
		config.setImageurl(imageurl);
	}

	public String getKeywords() {
		return config.getKeywords();
	}

	public void setKeywords(String keywords) {
		config.setKeywords(keywords);
	}

	/**
	 * @return Returns the basepath.
	 */
	public String getBasepath() {
		return config.getBasepath();
	}

	/**
	 * @param basepath The basepath to set.
	 */
	public void setBasepath(String basepath) {
		config.setBasepath(basepath);
	}

	/**
	 * @return Returns the imagepath.
	 */
	public String getGallerypath() {
		return config.getGallerypath();
	}

	/**
	 * @param imagepath The imagepath to set.
	 */
	public void setGallerypath(String gallerypath) {
		config.setGallerypath(gallerypath);
	}

	/**
	 * @return Returns the sitename.
	 */
	public String getSitename() {
		return config.getSitename();
	}

	/**
	 * @param sitename
	 */

	public void setSitename(String sitename) {
		config.setSitename(sitename);
	}

	/**
	 * @return Returns the sitename.
	 */
	public String getTemplate() {
		return config.getTemplate();
	}

	/**
	 * @param sitename
	 */

	public void setTemplate(String template) {
		config.setTemplate(template);
	}

	public ArrayList<DirectoryObject> getTemplateDirectoryContents() {
		return this.templateDirectoryContents;
	}

	public void setTemplateDirectoryContents(ArrayList<DirectoryObject> templateDirectoryContents) {
		this.templateDirectoryContents = templateDirectoryContents;
	}

	public ArrayList<DirectoryObject> getAdminTemplateDirectoryContents() {
		return this.adminTemplateDirectoryContents;
	}

	public void setAdminTemplateDirectoryContents(ArrayList<DirectoryObject> templateDirectoryContents) {
		this.adminTemplateDirectoryContents = templateDirectoryContents;
	}

	/**
	 * @return the maxAvatarHeight
	 */
	public int getMaxAvatarHeight() {
		return config.getMaxAvatarHeight();
	}

	/**
	 * @param maxAvatarHeight the maxAvatarHeight to set
	 */
	public void setMaxAvatarHeight(int maxAvatarHeight) {
		config.setMaxAvatarHeight(maxAvatarHeight);
	}

	/**
	 * @return the maxAvatarWidth
	 */
	public int getMaxAvatarWidth() {
		return config.getMaxAvatarWidth();
	}

	/**
	 * @param maxAvatarWidth the maxAvatarWidth to set
	 */
	public void setMaxAvatarWidth(int maxAvatarWidth) {
		config.setMaxAvatarWidth(maxAvatarWidth);
	}

	/**
	 * @return the maxFileUploadSize
	 */
	public int getMaxFileUploadSize() {
		return config.getMaxFileUploadSize();
	}

	/**
	 * @param maxFileUploadSize the maxFileUploadSize to set
	 */
	public void setMaxFileUploadSize(int maxFileUploadSize) {
		config.setMaxFileUploadSize(maxFileUploadSize);
	}

	/**
	 * @return the maxImageUploadSize
	 */
	public int getMaxImageUploadSize() {
		return config.getMaxImageUploadSize();
	}

	/**
	 * @param maxImageUploadSize the maxImageUploadSize to set
	 */
	public void setMaxImageUploadSize(int maxImageUploadSize) {
		config.setMaxImageUploadSize(maxImageUploadSize);
	}

	/**
	 * @return the downloadpath
	 */
	public String getDownloadpath() {
		return config.getDownloadpath();
	}

	/**
	 * @param downloadpath the downloadpath to set
	 */
	public void setDownloadpath(String downloadpath) {
		config.setDownloadpath(downloadpath);
	}

	/**
	 * @return the admintemplate
	 */
	public String getAdmintemplate() {
		return config.getAdmintemplate();
	}

	/**
	 * @param admintemplate the admintemplate to set
	 */
	public void setAdmintemplate(String admintemplate) {
		config.setAdmintemplate(admintemplate);
	}

	/**
	 * @return the startnode
	 */
	public String getStartnode() {
		return config.getStartnode();
	}

	/**
	 * @param startnode the startnode to set
	 */
	public void setStartnode(String startnode) {
		config.setStartnode(startnode);
	}

	/**
	 * @return the availableNodes
	 */
	public List<?> getAvailableNodes() {
		return availableNodes;
	}

	/**
	 * @param availableNodes the availableNodes to set
	 */
	public void setAvailableNodes(List<?> availableNodes) {
		this.availableNodes = availableNodes;
	}

	/**
	 * @return the productimagepath
	 */
	public String getProductimagepath() {
		return config.getProductimagepath();
	}

	/**
	 * @param productimagepath the productimagepath to set
	 */
	public void setProductimagepath(String productimagepath) {
		config.setProductimagepath(productimagepath);
	}

	public String getContentpath() {
		return config.getContentpath();
	}

	public void setContentpath(String contentpath) {
		config.setContentpath(contentpath);
	}

	/**
	 * @return the maintenanceMode
	 */
	public boolean isMaintenanceMode() {
		return config.isMaintenanceMode();
	}

	/**
	 * @param maintenanceMode the maintenanceMode to set
	 */
	public void setMaintenanceMode(boolean maintenanceMode) {
		config.setMaintenanceMode(maintenanceMode);
	}

}
