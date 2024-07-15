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
import org.pmedv.actions.FormAction;
import org.pmedv.pojos.Image;

public class GImageShowForm {

	private static final long serialVersionUID = 1L;

	private Image gimage;

    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
	
    public void reset(ActionMapping mapping, HttpServletRequest request) {
    	gimage = new Image();	
    }


	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return gimage.getDescription();
	}
	
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		gimage.setDescription ( description );
	}
	
	/**
	 * @return Returns the filename.
	 */
	public String getFilename() {
		return gimage.getFilename();
	}
	
	/**
	 * @param filename The filename to set.
	 */
	public void setFilename(String filename) {
		gimage.setFilename ( filename );
	}
	
	/**
	 * @return Returns the path.
	 */
	public String getPath() {
		return gimage.getPath();
	}
	
	/**
	 * @param path The path to set.
	 */
	public void setPath(String path) {
		gimage.setPath ( path );
	}

	
	/**
	 * @return Returns the path.
	 */
	public long getId() {
		return gimage.getId();
	}
	
	/**
	 * @param path The path to set.
	 */
	public void setId(long id) {
		gimage.setId ( id );
	}

	/**
	 * @return Returns the image.
	 */
	public Image getGImage() {
		return gimage;
	}


	/**
	 * @param mapimage The image to set.
	 */
	public void setGImage(Image gimage) {
		this.gimage = gimage;
	}
	
}
