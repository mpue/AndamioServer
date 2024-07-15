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

import org.apache.struts.action.ActionForm;

/**
 * This abstract form is a subclass of org.apache.struts.action.ActionForm
 * 
 * Inside this CMS every form has an icon and a name in order to display 
 * a title bar inside the jsp view. 
 * 
 * @author Matthias Pueski
 *
 */
public abstract class AbstractActionForm extends ActionForm {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String icon;
	
	/**
	 * @return the icon for this form
	 */
	public String getIcon() {
		return icon;
	}
	
	/**
	 * Sets the icon for this form
	 * 
	 * @param icon
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	/**
	 * @return the name of this form
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of this form
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}	

}
