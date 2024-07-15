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

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.plugins.PluginHelper;
import org.pmedv.pojos.Node;


@SuppressWarnings("unchecked")
public class TreeForm extends ActionForm {

	public TreeForm() {
		if (!PluginHelper.isInitialized())
			PluginHelper.initIPluginClasses(this.getClass().getClassLoader());
		
		availablePlugins = PluginHelper.getPluginList();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String name;
	
	private String image;
	
	private int position;
	
	private int type = 1;
	
	private String pluginid;
	
	private String pluginparams;
	
	private Boolean firstchild;
	
	private String contentName;
	
	private List availableNodes = DAOManager.getInstance().getNodeDAO().findAllItems();
	private List availableContents = DAOManager.getInstance().getContentDAO().findAllItems();
	private List availablePlugins;
	
	private String parent;
	
	/* (non-Javadoc)
	 * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.ServletRequest)
	 */
	@Override
	public void reset(ActionMapping mapping, ServletRequest request) {
		name = null;
		type = 1;
	}
	
	
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		
		ActionErrors errors = new ActionErrors();

		if (getName() == null || getName().length() < 1) {
			errors.add("name", new ActionMessage("error.name.required"));
		}
		
		if (getType() == Node.TYPE_CONTENT) {
			
			if (getContentName() == null || getContentName().length() < 1) {
				errors.add("contentname", new ActionMessage("error.node.contentname.required"));
			}
			
		}
		else if (getType() == Node.TYPE_PLUGIN) {
			if (getPluginid()== null || getPluginid().length() < 1) {
				errors.add("pluginid", new ActionMessage("error.node.pluginid.required"));
			}			
		}
		
		try {
			Node node = (Node)DAOManager.getInstance().getNodeDAO().findByName(getName());
			
			if (node != null)
				errors.add("exists",new ActionMessage("error.node.alreadyexists"));
		}
		catch (NullPointerException n) {
			
		}

		return errors;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List getAvailableNodes() {
		return availableNodes;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getType() {
		return type;
	}

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
	 * @return the availablePlugins
	 */
	public List getAvailablePlugins() {
		return availablePlugins;
	}

	/**
	 * @param availablePlugins the availablePlugins to set
	 */
	public void setAvailablePlugins(List availablePlugins) {
		this.availablePlugins = availablePlugins;
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
	 * @return the availableContents
	 */
	public List getAvailableContents() {
		return availableContents;
	}

	/**
	 * @return the contentName
	 */
	public String getContentName() {
		return contentName;
	}

	/**
	 * @param contentName the contentName to set
	 */
	public void setContentName(String contentName) {
		this.contentName = contentName;
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
}
