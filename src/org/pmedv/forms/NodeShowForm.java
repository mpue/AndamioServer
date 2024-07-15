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
package org.pmedv.forms;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.plugins.PluginHelper;
import org.pmedv.pojos.Node;
import org.pmedv.pojos.Usergroup;

/**
 * @author Matthias Pueski
 * 
 */
public class NodeShowForm extends ActionForm {

	public NodeShowForm() {

		if (!PluginHelper.isInitialized())
			PluginHelper.initIPluginClasses(this.getClass().getClassLoader());

		availablePlugins = PluginHelper.getPluginList();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Node node;

	private String contentName;
	private Long parentId;

	private List<?> availableContents = DAOManager.getInstance().getContentDAO().findAllItems();
	private List<?> availableNodes = DAOManager.getInstance().getNodeDAO().findAllItems();
	private List<?> availablePlugins;

	private List<?> paramDescriptors;

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		node = new Node();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object rhs) {
		return this.node.equals(rhs);
	}

	public int hashCode() {
		assert false : "hashCode not designed";
		return 42;
	}

	/**
	 * @return
	 */
	public Long getId() {
		return node.getId();
	}

	/**
	 * @param id
	 */
	public void setId(Long id) {
		node.setId(id);
	}

	/**
	 * @return
	 */
	public String getName() {
		return node.getName();
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		node.setName(name);
	}

	/**
	 * @return
	 */
	public int getPosition() {
		return node.getPosition();
	}

	/**
	 * @param position
	 */
	public void setPosition(int position) {
		node.setPosition(position);
	}

	/**
	 * @return
	 */
	public int getType() {
		return node.getType();
	}

	/**
	 * @param type
	 */
	public void setType(int type) {
		node.setType(type);
	}

	/**
	 * @return
	 */
	public Node getNode() {
		return node;
	}

	/**
	 * @param node
	 */
	public void setNode(Node node) {
		this.node = node;
	}

	/**
	 * @return
	 */
	public List<?> getAvailableContents() {
		return availableContents;
	}

	/**
	 * @param availableContents
	 */
	public void setAvailableContents(List<?> availableContents) {
		this.availableContents = availableContents;
	}

	/**
	 * @return
	 */
	public String getContentName() {
		return contentName;
	}

	/**
	 * @param contentName
	 */
	public void setContentName(String contentName) {
		this.contentName = contentName;
	}

	/**
	 * @return
	 */
	public List<?> getAvailableNodes() {
		return availableNodes;
	}

	/**
	 * @param availableNodes
	 */
	public void setAvailableNodes(List<?> availableNodes) {
		this.availableNodes = availableNodes;
	}

	/**
	 * @return
	 */
	public Long getParentId() {
		return parentId;
	}

	/**
	 * @param parentId
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the pluginid
	 */
	public String getPluginid() {
		return node.getPluginid();
	}

	/**
	 * @param pluginid the pluginid to set
	 */
	public void setPluginid(String pluginid) {
		node.setPluginid(pluginid);
	}

	/**
	 * @return the pluginparams
	 */
	public String getPluginparams() {
		return node.getPluginparams();
	}

	/**
	 * @return the image
	 */
	public String getImage() {
		return node.getImage();
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(String image) {
		node.setImage(image);
	}

	/**
	 * @param pluginparams the pluginparams to set
	 */
	public void setPluginparams(String pluginparams) {
		node.setPluginparams(pluginparams);
	}

	/**
	 * @return the availablePlugins
	 */
	public List<?> getAvailablePlugins() {
		return availablePlugins;
	}

	/**
	 * @param availablePlugins the availablePlugins to set
	 */
	public void setAvailablePlugins(List<?> availablePlugins) {
		this.availablePlugins = availablePlugins;
	}

	/**
	 * @return the groups the node belongs to
	 */
	public Set<Usergroup> getGroups() {
		return this.node.getGroups();
	}

	/**
	 * @param groups
	 */
	public void setGroups(Set<Usergroup> groups) {
		this.node.setGroups(groups);
	}

	/**
	 * @return the firstchild
	 */
	public Boolean getFirstchild() {
		return node.getFirstchild();
	}

	/**
	 * @param firstchild the firstchild to set
	 */
	public void setFirstchild(Boolean firstchild) {
		node.setFirstchild(firstchild);
	}

	public List<?> getParamDescriptors() {
		return paramDescriptors;
	}

	public void setParamDescriptors(List<?> paramDescriptors) {
		this.paramDescriptors = paramDescriptors;
	}

	public String getLinkurl() {
		return node.getLinkurl();
	}

	public void setLinkurl(String linkurl) {
		node.setLinkurl(linkurl);
	}

	/**
	 * @return the exportable
	 */
	public boolean isExportable() {
		return node.isExportable();
	}

	/**
	 * @param exportable the exportable to set
	 */
	public void setExportable(boolean exportable) {
		node.setExportable(exportable);
	}

	public void setTargetName(String targetName) {
		node.setTargetName(targetName);
	}

	public String getTargetName() {
		return node.getTargetName();
	}

	public String getRel() {
		return node.getRel();
	}

	public void setRel(String rel) {
		node.setRel(rel);
	}
}
