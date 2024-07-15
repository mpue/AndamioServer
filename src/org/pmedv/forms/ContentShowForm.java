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

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.Content;

public class ContentShowForm extends AbstractActionForm {

	private static final long serialVersionUID = 1L;
	
	private Content content;
	
	public Content getContent() {
		return content;
	}

	public String getTemplate() {
		return ((Config)DAOManager.getInstance().getConfigDAO().findByID(1)).getTemplate();
	}
	
	
	public void setContent(Content content) {
		this.content = content;
	}

	public boolean equals(Object rhs) {
		return content.equals(rhs);
	}
	
	public int hashCode() {
		assert false : "hashCode not designed";
		return 42; 
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		content = new Content();
    }

	public long getId() {
		return content.getId();
	}

	/**
	 * 
	 * @param id The id to set
	 */
	public void setID(int id) {
		content.setId(id);
	}
	
	public String getPagename() {
		return content.getPagename();
	}

	public void setPagename(String pagename) {
		content.setPagename ( pagename );
	}
	
	public String getContentname() {
		return content.getContentname();
	}

	public void setContentname(String contentname) {
		content.setContentname ( contentname );
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return content.getDescription();
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		content.setDescription( description );
	}

	public String getContentstring() {
		return content.getContentstring();
	}

	public void setContentstring(String contentstring) {
		content.setContentstring( contentstring );
	}
	
	public String getMetatags() {
		return content.getMetatags();
	}
	
	public void setMetatags(String metatags) {
		content.setMetatags(metatags);
	}
	
	/**
	 * @return the commentsAllowed
	 */
	public boolean isCommentsAllowed() {
		return content.isCommentsAllowed();
	}

	/**
	 * @param commentsAllowed the commentsAllowed to set
	 */
	public void setCommentsAllowed(boolean commentsAllowed) {
		content.setCommentsAllowed(commentsAllowed);
	}
	

}
