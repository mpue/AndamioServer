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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Download implements Comparable<Download> {

	private Long id;
	private String name;
	private String description;
	private String path;
	private String filename;
	private float ranking;
	private long hits;
	private long downloads;
	private String uploader;
	private String author;
	private String license;
	private Date uploadTime;
	private String tags;
	private String mimetype;
	private String domain;
	private String contributors;
	private long filesize;
	private boolean publicfile;
	private boolean downloadable;
	
	private static final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy kk:mm");

	private Set<DownloadCategory> categories = new HashSet<DownloadCategory>();

	public Download() {

	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
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
	 * @param description
	 *            The description to set.
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
	 * @param filename
	 *            The filename to set.
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @return Returns the id.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return Returns the path.
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 *            The path to set.
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return Returns the ranking.
	 */
	public float getRanking() {
		return ranking;
	}

	/**
	 * @param ranking
	 *            The ranking to set.
	 */
	public void setRanking(float ranking) {
		this.ranking = ranking;
	}

	/**
	 * @return the hits
	 */
	public long getHits() {
		return hits;
	}

	/**
	 * @param hits
	 *            the hits to set
	 */
	public void setHits(long hits) {
		this.hits = hits;
	}

	/**
	 * @return the uploader
	 */
	public String getUploader() {
		return uploader;
	}

	/**
	 * @param uploader
	 *            the uploader to set
	 */
	public void setUploader(String uploader) {
		this.uploader = uploader;
	}

	/**
	 * @return the uploadTime
	 */
	public Date getUploadTime() {
		return uploadTime;
	}

	/**
	 * @param uploadTime
	 *            the uploadTime to set
	 */
	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	/**
	 * @return the categories
	 */
	public Set<DownloadCategory> getCategories() {
		return categories;
	}

	/**
	 * @param categories
	 *            the categories to set
	 */
	public void setCategories(Set<DownloadCategory> categories) {
		this.categories = categories;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public boolean isPublicfile() {
		return publicfile;
	}

	public void setPublicfile(boolean publicfile) {
		this.publicfile = publicfile;
	}

	public String getMimetype() {
		return mimetype;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	public long getFilesize() {
		return filesize;
	}

	public void setFilesize(long filesize) {
		this.filesize = filesize;
	}

	public long getDownloads() {
		return downloads;
	}

	public void setDownloads(long downloads) {
		this.downloads = downloads;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public boolean isDownloadable() {
		return downloadable;
	}

	public void setDownloadable(boolean downloadable) {
		this.downloadable = downloadable;
	}

	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * @param domain the domain to set
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	
	
	public String getContributors() {
		return contributors;
	}

	public void setContributors(String contributors) {
		this.contributors = contributors;
	}

	public String getUploadTimeFormatted() {
		if (uploadTime != null) {
			return dateFormat.format(uploadTime);
		}
		return "undefined";
	}

	@Override
	public int compareTo(Download o) {
		if (o.getUploadTime().after(uploadTime)) {
			return 1;
		}
		else if(uploadTime.after(getUploadTime())) {
			return -1;
		}
		return 0;		
	}
	
}
