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

import java.util.Date;

/**
 * <p>
 * This class represents a revision of a content inside the content management.
 * <p>
 * The main HTML content is stored inside the field <b>contentstring</b>.
 * </p>
 * <p>
 * hibernate mapping: /resources/Revision.hbm.xml
 * <p>
 * 
 * @author Matthias Pueski
 * 
 */
public class Revision {

	public Revision() {

	}

	private Long id;
	private String contenttext;
	private String author;
	private boolean published;
	private Date lastmodified;
	private Content content;

	/**
	 * @return the content
	 */
	public Content getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(Content content) {
		this.content = content;
	}

	/**
	 * @return Returns the content.
	 */
	public String getContenttext() {
		return contenttext;
	}

	/**
	 * @param content
	 *            The content to set.
	 */
	public void setContenttext(String contenttext) {
		this.contenttext = contenttext;
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
	 * @return the lastmodified
	 */
	public Date getLastmodified() {
		return lastmodified;
	}

	/**
	 * @param lastmodified
	 *            the lastmodified to set
	 */
	public void setLastmodified(Date lastmodified) {
		this.lastmodified = lastmodified;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author
	 *            the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return the published
	 */
	public boolean isPublished() {
		return published;
	}

	/**
	 * @param published
	 *            the published to set
	 */
	public void setPublished(boolean published) {
		this.published = published;
	}

}
