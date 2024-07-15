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

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 * @author Matthias P�ski <br>
 * <br>
 *         This class represents a process form inside the content management
 *         system
 * 
 *         Every process is listed in the administration panel as a Link
 * 
 */
public class ProcessForm extends ActionForm {

	private static final Log log = LogFactory.getLog(ProcessForm.class);
	private static final ResourceBundle resources = ResourceBundle.getBundle("MessageResources");

	private String name = null;
	private String target = null;
	private String icon = null;
	private Boolean active = null;
	private int position = 0;

	/**
	 * Reset all properties to their default values.
	 * 
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.name = null;
		this.target = null;
		this.icon = null;
		this.active = null;
		this.position = 0;
	}

	/**
	 * Validation
	 */

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		if (getName() == null || getName().length() < 1) {
			errors.add("name", new ActionMessage("error.name.required"));
		}
		else {
			try {
				String key = resources.getString(getName());
				log.debug("validating process creation of process " + key);
			}
			catch (MissingResourceException m) {
				errors.add("name", new ActionMessage("error.key.doesntexist"));
			}
		}

		return errors;
	}

	/**
	 * Determines whether a process is active or not
	 * 
	 * @return active flag of the process
	 */
	public Boolean getActive() {
		return active;
	}

	/**
	 * Sets a process active/inactive
	 * 
	 * @param active the flag to be set
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
	 * Returns the icon of the process
	 * 
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * Sets the icon of the desired process
	 * 
	 * @param icon
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * Returns the name of the process
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this process
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Determines the target of the process, this has to be an URL or an action
	 * 
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * Sets the target of the process, this has to be an URL or an action
	 * 
	 * @param target
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}

}
