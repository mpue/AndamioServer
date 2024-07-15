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
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class implements a persisted tree node.
 * 
 * @author Matthias Pueski
 */
public class Node implements Serializable, Comparable<Node> {

	private static final long serialVersionUID = -5753960609985066324L;

	public static final int TYPE_CONTENT = 1;
	public static final int TYPE_PLUGIN = 2;
	public static final int TYPE_LINK = 3;
	public static final int TYPE_DOMAIN = 4;

	protected static final String PATH_DELIMITER = "/";

	protected Long id;
	protected String name;
	protected String displayName;
	protected int position;
	protected int type;
	protected String image;
	protected String pluginid;
	protected String pluginparams;
	protected Boolean firstchild;
	protected String linkurl;
	protected String path;
	protected boolean restricted;
	protected Node parent;
	protected Content content;
	protected boolean exportable;
	protected String rel;
	protected String targetName;

	protected SortedSet<Node> children = new TreeSet<Node>();;
	protected Set<Usergroup> groups = new HashSet<Usergroup>();

	/**
	 * Determines if this node is restricted to any group
	 * 
	 * @return
	 */
	public boolean isRestricted() {
		return getGroups().size() > 0;
	}

	/**
	 * Determines the full path of this node until the root node is reached
	 * 
	 * @return the path
	 */
	public String getPath() {

		path = "";
		evalPath(this);
		path = path.substring(0, path.length() - 1);

		return path;
	}

	/**
	 * Searches the path for a node ending at the root node
	 * 
	 * @param n the node to evaluate the path for
	 */

	protected void evalPath(Node n) {

		if (n != null) {
			path = n.getName() + PATH_DELIMITER + path;
			if (n.getParent() != null)
				evalPath(n.getParent());
		}

	}

	/**
	 * gets the id of the node
	 * 
	 * @return the id of the node
	 */
	public Long getId() {

		return id;
	}

	/**
	 * Sets the id of the node
	 * 
	 * @param id the id to set
	 */
	public void setId(Long id) {

		this.id = id;
	}

	/**
	 * Gets the name of the node
	 * 
	 * @return
	 */
	public String getName() {

		return name;
	}

	/**
	 * Sets the name of the node
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {

		this.name = name;
	}

	/**
	 * Gets all the children of this node
	 * 
	 * @return the associated chilren
	 */
	public Set<Node> getChildren() {

		return children;
	}

	/**
	 * Sets the children of this node
	 * 
	 * @param children
	 */
	public void setChildren(SortedSet<Node> children) {

		this.children = children;
	}

	/**
	 * Gets the parent of this node
	 * 
	 * @return
	 */
	public Node getParent() {

		return parent;
	}

	/**
	 * Sets the parent of this node
	 * 
	 * @param parent the parent to set
	 */
	public void setParent(Node parent) {

		this.parent = parent;
	}

	/**
	 * Gets the content asscociated with this node
	 * 
	 * @return
	 */
	public Content getContent() {

		return content;
	}

	/**
	 * Sets the content of this node
	 * 
	 * @param content the content to set
	 */
	public void setContent(Content content) {

		this.content = content;
	}

	/**
	 * Gets the position of this node
	 * 
	 * @return the position of this node
	 */
	public int getPosition() {

		return position;
	}

	/**
	 * Sets the position of this node
	 * 
	 * @param position the position to set
	 */
	public void setPosition(int position) {

		this.position = position;
	}

	/**
	 * Checks if this node is a root node or not
	 * 
	 * @return true if this node is root false if not
	 */
	public boolean isRootNode() {

		return (this.getParent() != null) ? false : true;
	}

	/**
	 * Gets the type of this node, this can either be a content or a plugin
	 * 
	 * @return the type of this node
	 */
	public int getType() {

		return type;
	}

	/**
	 * Sets the type of this node, this can either be a content or a plugin
	 * 
	 * @param type the type to set
	 */
	public void setType(int type) {

		this.type = type;
	}

	/**
	 * @return the pluginid
	 */
	public String getPluginid() {

		return pluginid;
	}

	/**
	 * @param pluginid the pluginid to set
	 */
	public void setPluginid(String pluginid) {

		this.pluginid = pluginid;
	}

	/**
	 * @return the pluginparams
	 */
	public String getPluginparams() {

		return pluginparams;
	}

	/**
	 * @param pluginparams the pluginparams to set
	 */
	public void setPluginparams(String pluginparams) {

		this.pluginparams = pluginparams;
	}

	/**
	 * @return the groups
	 */
	public Set<Usergroup> getGroups() {

		return groups;
	}

	/**
	 * @param groups the groups to set
	 */
	public void setGroups(Set<Usergroup> groups) {

		this.groups = groups;
	}

	public int compareTo(Node o) {

		if (o.getPosition() > getPosition())
			return -1;
		else if (o.getPosition() < getPosition())
			return 1;
		else
			return 0;

	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}
		if (obj instanceof Node) {
			Node node = (Node) obj;

			if (node.getParent() == null) {
				if (this.name.equals(node.getName()) && this.getParent() == null)
					return true;
			}
			else {
				if (this.getParent() == null)
					return false;
				else if (this.name.equals(node.getName()) && this.parent.equals(node.getParent()))
					return true;
			}

		}
		return false;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		if (name == null)
			return 42;

		return this.name.hashCode();

	}

	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * @return the firstchild
	 */
	public Boolean getFirstchild() {
		return firstchild;
	}

	/**
	 * @param firstchild the firstchild to set
	 */
	public void setFirstchild(Boolean firstchild) {
		this.firstchild = firstchild;
	}

	/**
	 * @return the exportable
	 */
	public boolean isExportable() {
		return exportable;
	}

	/**
	 * Checks if a given node is a hidden node, a node is hidden if it contains any underslash in
	 * its name.
	 * 
	 * @return true if the node is hidden, false if not
	 */
	public boolean isHidden() {
		return (getName().indexOf("_") >= 0);
	}

	/**
	 * @param exportable the exportable to set
	 */
	public void setExportable(boolean exportable) {
		this.exportable = exportable;
	}

	public boolean hasChildren() {
		if (children == null)
			return false;

		return (getChildren().size() > 0);
	}

	public String getLinkurl() {
		return linkurl;
	}

	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the rel
	 */
	public String getRel() {
		return rel;
	}
	
	/**
	 * @param rel the rel to set
	 */
	public void setRel(String rel) {
		this.rel = rel;
	}
	
	/**
	 * @return the targetName
	 */
	public String getTargetName() {
		return targetName;
	}

	/**
	 * @param targetName the targetName to set
	 */
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
	
}
