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
/**
 * 
 */
package org.pmedv.forms;

import org.apache.struts.action.ActionForm;

/**
 * 	The struts Action form for the installation process
 * 
 * @author Matthias Pueski
 *
 */

public class InstallForm extends ActionForm {
	
	private static final long serialVersionUID = 3282751420677250547L;
	
	private String hostname     = null;		
	private String localPath    = null;		
	private String smtphost     = null;
	private String fromadress   = null;
	private String username     = null;		
	private String password     = null;
	private String importpath   = null;
	private String imagepath    = null;
	private String imageurl     = null;
	private String downloadpath = null;
	private String keywords     = null;
	private String basepath     = null;
	private String gallerypath  = null;
	private String productimagepath = null;
	private String sitename     = null;
	private String template     = null;
	private String adminpass    = null;
	private String adminemail   = null;
	private String admintemplate = null;
	private String startnode	 = null;
	private String contentpath   = null;
	private String maxAvatarHeight    = null;
	private String maxAvatarWidth     = null;
	private String maxImageUploadSize = null;
	private String maxFileUploadSize  = null;

	private String dbUrl = null;
	private String dbUser = null;
	private String dbPass = null;
	
	public String getBasepath() {
		return basepath;
	}
	public void setBasepath(String basepath) {
		this.basepath = basepath;
	}
	public String getFromadress() {
		return fromadress;
	}
	public void setFromadress(String fromadress) {
		this.fromadress = fromadress;
	}
	public String getGallerypath() {
		return gallerypath;
	}
	public void setGallerypath(String gallerypath) {
		this.gallerypath = gallerypath;
	}
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public String getImagepath() {
		return imagepath;
	}
	public void setImagepath(String imagepath) {
		this.imagepath = imagepath;
	}
	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	public String getImportpath() {
		return importpath;
	}
	public void setImportpath(String importpath) {
		this.importpath = importpath;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getLocalPath() {
		return localPath;
	}
	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSitename() {
		return sitename;
	}
	public void setSitename(String sitename) {
		this.sitename = sitename;
	}
	public String getSmtphost() {
		return smtphost;
	}
	public void setSmtphost(String smtphost) {
		this.smtphost = smtphost;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAdminemail() {
		return adminemail;
	}
	public void setAdminemail(String adminemail) {
		this.adminemail = adminemail;
	}
	public String getAdminpass() {
		return adminpass;
	}
	public void setAdminpass(String adminpass) {
		this.adminpass = adminpass;
	}	
	
	/**
	 * @return the maxAvatarHeight
	 */
	public String getMaxAvatarHeight() {
		return maxAvatarHeight;
	}
	/**
	 * @param maxAvatarHeight the maxAvatarHeight to set
	 */
	public void setMaxAvatarHeight(String maxAvatarHeight) {
		this.maxAvatarHeight = maxAvatarHeight;
	}
	/**
	 * @return the maxAvatarWidth
	 */
	public String getMaxAvatarWidth() {
		return maxAvatarWidth;
	}
	/**
	 * @param maxAvatarWidth the maxAvatarWidth to set
	 */
	public void setMaxAvatarWidth(String maxAvatarWidth) {
		this.maxAvatarWidth = maxAvatarWidth;
	}
	/**
	 * @return the maxFileUploadSize
	 */
	public String getMaxFileUploadSize() {
		return maxFileUploadSize;
	}
	/**
	 * @param maxFileUploadSize the maxFileUploadSize to set
	 */
	public void setMaxFileUploadSize(String maxFileUploadSize) {
		this.maxFileUploadSize = maxFileUploadSize;
	}
	/**
	 * @return the maxImageUploadSize
	 */
	public String getMaxImageUploadSize() {
		return maxImageUploadSize;
	}
	/**
	 * @param maxImageUploadSize the maxImageUploadSize to set
	 */
	public void setMaxImageUploadSize(String maxImageUploadSize) {
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
	 * @return the productimagepath
	 */
	public String getProductimagepath() {
		return productimagepath;
	}
	/**
	 * @param productimagepath the productimagepath to set
	 */
	public void setProductimagepath(String productimagepath) {
		this.productimagepath = productimagepath;
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
	 * @return the dburl
	 */
	public String getDbUrl() {
		return dbUrl;
	}

	/**
	 * @param dbUrl the url of the database
	 */
	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	/**
	 * @return the dbUser
	 */
	public String getDbUser() {
		return dbUser;
	}
	/**
	 * @param dbUser the dbUser to set
	 */
	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}
	/**
	 * @return the dbPass
	 */
	public String getDbPass() {
		return dbPass;
	}
	/**
	 * @param dbPass the dbPass to set
	 */
	public void setDbPass(String dbPass) {
		this.dbPass = dbPass;
	}
	
}
