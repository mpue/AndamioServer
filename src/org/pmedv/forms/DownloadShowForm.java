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

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.pmedv.pojos.Download;

public class DownloadShowForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	private Download download;

	/**
	 * Reset all properties to their default values.
	 * 
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	 */

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		download = new Download();
	}

	/**
	 * @return the name of the download
	 */
	public String getName() {
		return download.getName();
	}

	/**
	 * @param name the name of the download to set
	 */
	public void setName(String name) {
		download.setName(name);
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return download.getDescription();
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		download.setDescription(description);
	}

	/**
	 * @return Returns the filename.
	 */
	public String getFilename() {
		return download.getFilename();
	}

	/**
	 * @param filename The filename to set.
	 */
	public void setFilename(String filename) {
		download.setFilename(filename);
	}

	/**
	 * @return Returns the path.
	 */
	public String getPath() {
		return download.getPath();
	}

	/**
	 * @param path The path to set.
	 */
	public void setPath(String path) {
		download.setPath(path);
	}

	/**
	 * @return Returns the path.
	 */
	public long getId() {
		return download.getId();
	}

	/**
	 * @param path The path to set.
	 */
	public void setId(long id) {
		download.setId(id);
	}

	/**
	 * @return Returns the download.
	 */
	public Download getDownload() {
		return download;
	}

	/**
	 * @param download The download to set.
	 */
	public void setDownload(Download download) {
		this.download = download;
	}

	/**
	 * @return the hits
	 */
	public long getHits() {
		return download.getHits();
	}

	/**
	 * @param hits the hits to set
	 */
	public void setHits(int hits) {
		download.setHits(hits);
	}
	
	/**
	 * @return Returns the ranking.
	 */
	public float getRanking() {
		return download.getRanking();
	}

	/**
	 * @param ranking
	 *            The ranking to set.
	 */
	public void setRanking(float ranking) {
		download.setRanking(ranking);
	}
	
	/**
	 * @return the uploader
	 */
	public String getUploader() {
		return download.getUploader();
	}

	/**
	 * @param uploader
	 *            the uploader to set
	 */
	public void setUploader(String uploader) {
		download.setUploader(uploader);
	}

	/**
	 * @return the uploadTime
	 */
	public Date getUploadTime() {
		return download.getUploadTime();
	}

	/**
	 * @param uploadTime
	 *            the uploadTime to set
	 */
	public void setUploadTime(Date uploadTime) {
		download.setUploadTime(uploadTime);
	}

	public String getTags() {
		return download.getTags();
	}

	public void setTags(String tags) {
		download.setTags(tags);
	}

	public boolean isPublicfile() {
		return download.isPublicfile();
	}

	public void setPublicfile(boolean publicfile) {
		download.setPublicfile(publicfile);
	}

	public String getMimetype() {
		return download.getMimetype();
	}

	public void setMimetype(String mimetype) {
		download.setMimetype(mimetype);
	}

	public long getFilesize() {
		return download.getFilesize();
	}

	public void setFilesize(long filesize) {
		download.setFilesize(filesize);
	}

	public long getDownloads() {
		return download.getDownloads();
	}

	public void setDownloads(long downloads) {
		download.setDownloads(downloads);
	}

	public String getAuthor() {
		return download.getAuthor();
	}

	public void setAuthor(String author) {
		download.setAuthor(author);
	}

	public String getLicense() {
		return download.getLicense();
	}

	public void setLicense(String license) {
		download.setLicense(license);
	}

	public boolean isDownloadable() {
		return download.isDownloadable();
	}

	public void setDownloadable(boolean downloadable) {
		download.setDownloadable(downloadable);
	}

	/**
	 * @return the domain
	 */
	public String getDomain() {
		return download.getDomain();
	}

	/**
	 * @param domain the domain to set
	 */
	public void setDomain(String domain) {
		download.setDomain(domain);
	}
	

}
