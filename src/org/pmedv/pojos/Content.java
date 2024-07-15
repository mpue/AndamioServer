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

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * <p>
 * This class represents the content itself inside the content management.
 * <p>
 * The main HTML content is stored inside the field <b>contentstring</b>.
 * </p>
 * <p>
 * At the moment the content is mapped as many-to-many relation to menuitems, thus every content can
 * be linked to multiple menuitems, which can be fetched with e.g. content.getMenuitems()
 * </p>
 * <p>
 * hibernate mapping: /resources/Content.hbm.xml
 * <p>
 * 
 * @author Matthias Pueski
 * 
 */
public class Content implements Serializable {

	public Content() {

	}

	private static final long serialVersionUID = 8021805709123380926L;

	public static final int MENUETYPE_NEW = 1;
	public static final int MENUETYPE_OLD = 2;

	private long id;
	
	private String contentname;
	private String pagename;
	private String description;
	private String contentstring;
	private String metatags;
	private Date lastmodified;
	private String lastmodifiedby;	
	private Date created;
	private boolean commentsAllowed = false;
	
	private SortedSet<UserComment> userComments  = new TreeSet<UserComment>();
	
	
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	private Revision currentrevision;

	/**
	 * @return the currentrevision
	 */
	public Revision getCurrentrevision() {
		return currentrevision;
	}

	/**
	 * @param currentrevision
	 *            the currentrevision to set
	 */
	public void setCurrentrevision(Revision currentrevision) {
		this.currentrevision = currentrevision;
	}

	/* The nodes a content belongs to */
	private Set<Node> nodes = new HashSet<Node>();

	/* This field represents the different revisions a content can have */
	private Set<Revision> revisions = new HashSet<Revision>();

	/**
	 * @return the revisions
	 */
	public Set<Revision> getRevisions() {
		return revisions;
	}

	/**
	 * @param revisions
	 *            the revisions to set
	 */
	public void setRevisions(Set<Revision> revisions) {
		this.revisions = revisions;
	}

	/**
	 * @return Returns the content.
	 */
	public String getContentstring() {
		return contentstring;
	}

	/**
	 * @param content
	 *            The content to set.
	 */
	public void setContentstring(String contentstring) {
		this.contentstring = contentstring;
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
	 * @return Returns the id.
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return Returns the name.
	 */
	public String getContentname() {
		return contentname;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setContentname(String name) {
		this.contentname = name;
	}

	/**
	 * @return the nodes associated with this content
	 */
	public Set<Node> getNodes() {
		return nodes;
	}

	/**
	 * @param nodes
	 *            the nodes to set
	 */
	public void setNodes(Set<Node> nodes) {
		this.nodes = nodes;
	}

	/**
	 * @return the pagename
	 */
	public String getPagename() {
		return pagename;
	}

	/**
	 * Sets the pagename
	 * 
	 * @param pagename
	 *            the pagename to set
	 */
	public void setPagename(String pagename) {
		this.pagename = pagename;
	}

	public String getMetatags() {
		return metatags;
	}

	public void setMetatags(String metatags) {
		this.metatags = metatags;
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

	public String getLastmodifiedby() {
		return lastmodifiedby;
	}

	
	public void setLastmodifiedby(String lastmodifiedby) {
		this.lastmodifiedby = lastmodifiedby;
	}

	/**
	 * @return the userComments
	 */
	public SortedSet<UserComment> getUserComments() {
		return userComments;
	}

	/**
	 * @param userComments the userComments to set
	 */
	public void setUserComments(SortedSet<UserComment> userComments) {
		this.userComments = userComments;
	}

	/**
	 * @return the commentsAllowed
	 */
	public boolean isCommentsAllowed() {
		return commentsAllowed;
	}

	/**
	 * @param commentsAllowed the commentsAllowed to set
	 */
	public void setCommentsAllowed(boolean commentsAllowed) {
		this.commentsAllowed = commentsAllowed;
	}	
	
}
