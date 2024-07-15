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
package org.pmedv.pojos;

/**
 * This class provides all basic configuration values that are essential for the system
 * 
 * @author Matthias Pueski
 *
 *  TODO : Maybe we should change this to a simple hash map, so that we can store as many values as we need and
 *  build our configuration view dynamically. 
 */


public class Config {

	public Config() {
		
	}
	
	private Long id;
	private String hostname;		
	private String localPath;		
	private String smtphost;
	private String fromadress;
	private String username;		
	private String password;
	private String importpath;
	private String imagepath;
	private String downloadpath;
	private String productimagepath;
	private String imageurl;	
	private String basepath;
	private String keywords;
	private String gallerypath;
	private String sitename;
	private String template;
	private String admintemplate;
	private String startnode;
	private String contentpath;
	private int    maxAvatarHeight;
	private int    maxAvatarWidth;	
	private int    maxImageUploadSize;
	private int	   maxFileUploadSize;
	private boolean maintenanceMode;

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
	 * @return Returns the id.
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(Long id) {
		this.id = id;
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
	 * Returns the basepath of the application relative to the context path
	 * 
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
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
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
