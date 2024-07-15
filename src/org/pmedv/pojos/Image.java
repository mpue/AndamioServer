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
package org.pmedv.pojos;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * An image object is part of an image gallery.
 * </p>
 * 
 * @see {@link Gallery}
 * 
 * @author Matthias Pueski
 * 
 */
public class Image implements Comparable<Image> {

	private Long id;
	private String name;
	private String description;
	private String path;
	private String filename;
	private float ranking;
	private long lastmodified;
	private Gallery gallery;
	
	private Set<UserComment> userComments  = new HashSet<UserComment>();

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
	 * @return the gallery
	 */
	public Gallery getGallery() {
		return gallery;
	}

	/**
	 * @param gallery the gallery to set
	 */
	public void setGallery(Gallery gallery) {
		this.gallery = gallery;
	}

	public Image() {

	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set.
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
	 * @param filename The filename to set.
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
	 * @param id The id to set.
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
	 * @param path The path to set.
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
	 * @param ranking The ranking to set.
	 */
	public void setRanking(float ranking) {
		this.ranking = ranking;
	}

	public long getLastmodified() {
		return lastmodified;
	}

	public void setLastmodified(long lastmodified) {
		this.lastmodified = lastmodified;
	}

	public Set<UserComment> getUserComments() {
		return userComments;
	}

	
	public void setUserComments(Set<UserComment> userComments) {
		this.userComments = userComments;
	}
	
	
	public boolean equals(Image obj) {

		if (this == obj) {
			return true;
		}
		if (obj instanceof Image) {
			Image image = (Image) obj;
			if (this.filename.equals(image.getFilename()))
				return true;
		}
		return false;
	}

	@Override
	public int compareTo(Image o) {
		return (StringUtils.upperCase(getName()).compareTo(StringUtils.upperCase(o.getName())));
	}

}
