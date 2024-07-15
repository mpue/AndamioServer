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

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.pmedv.pojos.Node;

public class NodegroupAddForm extends ActionForm {
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Node node;
	private List<?> usergroups;
	
	 /**
	 * @return Returns the usergroups
	 */
	public List<?> getUsergroups() {
		return usergroups;
	}
	 
	/**
	* @param usergroups2 The usergroups to set.
	*/
	 
	public void setUsergroups(List<?> usergroups2) {
		this.usergroups = usergroups2;
	}
	 
	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		usergroups = null;
	}

	/**
	 * @return Returns the current node
	 */
	public Node getNode() {
		return node;
	}

	/**
	 * @param The current node
	 */
	public void setNode(Node node) {
		this.node = node;
	}
}
