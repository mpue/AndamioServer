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
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.pmedv.actions.FormAction;
import org.pmedv.pojos.Gallery;

public class GalleryShowForm {
			
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Gallery gallery;
	private List<?> availableGalleries;
	private int target_gallery_id;
	private int numGalleries;
	
	public boolean equals(Object rhs) {
		return gallery.equals(rhs);
	}

	public int hashCode() {
		assert false : "hashCode not designed";
		return 42; 
	}


	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		gallery = new Gallery();
		availableGalleries = new ArrayList<Object>();
    }
	
	
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return gallery.getDescription();
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		gallery.setDescription ( description );
	}

	/**
	 * @return Returns the galleryname.
	 */
	public String getGalleryname() {
		return gallery.getGalleryname();
	}

	/**
	 * @param galleryname The galleryname to set.
	 */
	public void setGalleryname(String galleryname) {
		gallery.setGalleryname ( galleryname );
	}

	/**
	 * @return Returns the gallerytext.
	 */
	public String getGallerytext() {
		return gallery.getGallerytext();
	}

	/**
	 * @param gallerytext The gallerytext to set.
	 */
	public void setGallerytext(String gallerytext) {
		gallery.setGallerytext ( gallerytext );
	}

	/**
	 * @return Returns the id.
	 */
	public Long getId() {
		return gallery.getId();
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(Long id) {
		gallery.setId ( id );
	}

	/**
	 * @return Returns the ranking.
	 */
	public float getRanking() {
		return gallery.getRanking();
	}

	/**
	 * @param ranking The ranking to set.
	 */
	public void setRanking(float ranking) {
		gallery.setRanking ( ranking );
	}

	/**
	 * @return Returns the thumbheight.
	 */
	public int getThumbheight() {
		return gallery.getThumbheight();
	}

	/**
	 * @param thumbheight The thumbheight to set.
	 */
	public void setThumbheight(int thumbheight) {
		gallery.setThumbheight ( thumbheight );
	}

	/**
	 * @return Returns the thumbwidth.
	 */
	public int getThumbwidth() {
		return gallery.getThumbwidth();
	}

	/**
	 * @param thumbwidth The thumbwidth to set.
	 */
	public void setThumbwidth(int thumbwidth) {
		gallery.setThumbwidth ( thumbwidth );
	}


	/**
	 * @return Returns the gallery.
	 */
	public Gallery getGallery() {
		return gallery;
	}


	/**
	 * @param gallery The gallery to set.
	 */
	public void setGallery(Gallery gallery) {
		this.gallery = gallery;
	}

	public void setImages(SortedSet<?> gimages) {
		gallery.setImages(gimages);
	}
	
	public SortedSet<?> getImages() {
		return gallery.getImages();
	}


	public List<?> getAvailableGalleries() {
		return availableGalleries;
	}


	public void setAvailableGalleries(List<?> availableGalleries) {
		this.availableGalleries = availableGalleries;
	}


	public int getTarget_gallery_id() {
		return target_gallery_id;
	}


	public void setTarget_gallery_id(int target_gallery_id) {
		this.target_gallery_id = target_gallery_id;
	}

	public int getNumGalleries() {
		return availableGalleries.size();
	}

	public void setNumGalleries(int numGalleries) {
		this.numGalleries = numGalleries;
	}
	
}
