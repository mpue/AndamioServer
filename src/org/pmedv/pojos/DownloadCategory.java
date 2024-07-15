package org.pmedv.pojos;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.pmedv.pojos.forums.Category;

public class DownloadCategory implements Serializable, Comparable<Category> {

	private long id;
	private String name;
	private String description;

	private Set<Download> downloads = new HashSet<Download>();

	public DownloadCategory() {
		
	}
	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the downloads
	 */
	public Set<Download> getDownloads() {
		return downloads;
	}

	/**
	 * @param downloads the downloads to set
	 */
	public void setDownloads(Set<Download> downloads) {
		this.downloads = downloads;
	}

	@Override
	public int compareTo(Category o) {
		return o.getName().compareTo(name);
	}

}
