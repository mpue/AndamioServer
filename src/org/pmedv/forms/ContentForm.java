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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.pmedv.cms.daos.DAOManager;

public class ContentForm extends AbstractActionForm
{

	private static final long serialVersionUID = 1L;

	private String contentname;
	private String pagename;
	private String description;
	private String contentstring;
	private String metatags;
	private boolean commentsAllowed;
	
	private String menuname;
	private Boolean insertInMenu = false;
	private int menutype = 1;
	
	private List<?> availableMenusOld = new ArrayList<Object>();
	private List<?> availableMenusNew = new ArrayList<Object>();
	
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
	
	public ContentForm() {
		availableMenusNew = DAOManager.getInstance().getNodeDAO().findAllItems();
	}
	
    public void reset(ActionMapping mapping, HttpServletRequest request) {
    	this.contentname   		= null; //Inhaltsname
    	this.pagename 			= null; //Seitentitel	
    	this.description   		= null; //Beschreibung
    	this.contentstring 		= null; //Inhalt (HTML)
    	this.insertInMenu  		= false;
    }

	/**
	 * @return Returns the content.
	 */
	public String getContentstring() {
		return contentstring;
	}

	/**
	 * @param content The content to set.
	 */
	public void setContentstring(String contentstring) {
		this.contentstring = contentstring;
	}

	/**
	 * @return Returns the contentname.
	 */
	public String getContentname() {
		return contentname;
	}

	/**
	 * @param contentname The contentname to set.
	 */
	public void setContentname(String contentname) {
		this.contentname = contentname;
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

	public String getMetatags() {
		return metatags;
	}
	
	public void setMetatags(String metatags) {
		this.metatags = metatags;
	}
	
	
	public List<?> getAvailableMenusOld() {
		return availableMenusOld;
	}

	public void setAvailableMenusOld(List<?> availableMenus) {
		this.availableMenusOld = availableMenus;
	}
	
	public List<?> getAvailableMenusNew() {
		return availableMenusNew;
	}

	public void setAvailableMenusNew(List<?> availableMenus) {
		this.availableMenusNew = availableMenus;
	}

	public String getMenuname() {
		return menuname;
	}

	public void setMenuname(String menuname) {
		this.menuname = menuname;
	}

	public Boolean getInsertInMenu() {
		return insertInMenu;
	}

	public void setInsertInMenu(Boolean insertInMenu) {
		this.insertInMenu = insertInMenu;
	}

	public String getPagename() {
		return pagename;
	}

	public void setPagename(String pagename) {
		this.pagename = pagename;
	}

	public int getMenutype() {
		return menutype;
	}

	public void setMenutype(int menutype) {
		this.menutype = menutype;
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
