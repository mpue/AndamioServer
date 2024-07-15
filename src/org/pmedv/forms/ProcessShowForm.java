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

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author Matthias Pueski <br>
 * <br>
 *         This class represents a process show form inside the content
 *         management system
 * 
 *         Every process is listed in the administration panel as a Link
 * 
 */
public class ProcessShowForm extends ActionForm {

	private org.pmedv.pojos.Process process;

	/**
	 * Reset all properties to their default values.
	 * 
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.process = new org.pmedv.pojos.Process();
	}

	public boolean equals(Object rhs) {
		return this.process.equals(rhs);
	}

	public int hashCode() {
		assert false : "hashCode not designed";
		return 42;
	}

	public Long getId() {
		return this.process.getId();
	}

	public void setId(Long id) {
		this.process.setId(id);
	}

	/**
	 * Determines whether a process is active or not
	 * 
	 * @return active flag of the process
	 */
	public Boolean getActive() {
		return this.process.getActive();
	}

	/**
	 * Sets a process active/inactive
	 * 
	 * @param active the flag to be set
	 */
	public void setActive(Boolean active) {
		this.process.setActive(active);
	}

	/**
	 * Returns the icon of the process
	 * 
	 * @return the icon
	 */
	public String getIcon() {
		return this.process.getIcon();
	}

	/**
	 * Sets the icon of the desired process
	 * 
	 * @param icon
	 */
	public void setIcon(String icon) {
		this.process.setIcon(icon);
	}

	/**
	 * Returns the name of the process
	 * 
	 * @return the name
	 */
	public String getName() {
		return this.process.getName();
	}

	/**
	 * Sets the name of this process
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.process.setName(name);
	}

	/**
	 * Determines the target of the process, this has to be an URL or an action
	 * 
	 * @return the target
	 */
	public String getTarget() {
		return this.process.getTarget();
	}

	/**
	 * Sets the target of the process, this has to be an URL or an action
	 * 
	 * @param target
	 */
	public void setTarget(String target) {
		this.process.setTarget(target);
	}

	/**
	 * returns the selected process
	 * 
	 * @return
	 */
	public org.pmedv.pojos.Process getProcess() {
		return this.process;
	}

	/**
	 * sets the selected process
	 * 
	 * @param process
	 */
	public void setProcess(org.pmedv.pojos.Process process) {
		this.process = process;
	}

	/**
	 * @return the groups the process belongs to
	 */
	@SuppressWarnings("unchecked")
	public Set getGroups() {
		return this.process.getGroups();
	}

	/**
	 * @param groups
	 */
	@SuppressWarnings("unchecked")
	public void setGroups(Set groups) {
		this.process.setGroups(groups);
	}

	/**
	 * @return the position
	 */
	public int getPosition() {
		return process.getPosition();
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(int position) {
		this.process.setPosition(position);
	}

}
