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
package org.pmedv.pojos;

import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.pmedv.plugins.XGalleryPlugin;
import org.pmedv.services.GalleryService;

/**
 * <p>
 * The gallery object is used to build up an html picture gallery.
 * </p>
 * <p>
 * Currently there's no web front end for the gallery, thus in order
 * to build galleries, the AndamioManager is needed. Every gallery contains
 * a <code>SortedSet</code> of {@link Image} objects.
 * </p>
 * 
 * @see {@link XGalleryPlugin}
 * @see {@link Image}
 * @see {@link GalleryEnumUserType}
 * @see {@link GalleryService}
 * 
 * @author Matthias Pueski
 *
 */
@SuppressWarnings("unchecked")

public class Gallery implements Comparable<Gallery>{
			
	private Long id;

	/**
	 * The name of the gallery is used to locate the right gallery
	 */
	
	private String galleryname;
	
	private String description;
	private int thumbheight;
	private int thumbwidth;
	private int columns;
	
	/**
	 * The template is provided by the AndamioManager
	 */	
	private String template;
	private String gallerytext;
	private float ranking;
	private SortedSet<Image> images  = new TreeSet<Image>();
		
	public Gallery() {
		
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

	/**
	 * @return Returns the galleryname.
	 */
	public String getGalleryname() {
		return galleryname;
	}

	/**
	 * @param galleryname The galleryname to set.
	 */
	public void setGalleryname(String galleryname) {
		this.galleryname = galleryname;
	}

	/**
	 * @return Returns the gallerytext.
	 */
	public String getGallerytext() {
		return gallerytext;
	}

	/**
	 * @param gallerytext The gallerytext to set.
	 */
	public void setGallerytext(String gallerytext) {
		this.gallerytext = gallerytext;
	}

	/**
	 * @return Returns the id.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return Returns the ranking.
	 */
	public float getRanking() {
		return ranking;
	}

	/**
	 * @param ranking The ranking to set.
	 */
	public void setRanking(float ranking) {
		this.ranking = ranking;
	}

	/**
	 * @return Returns the thumbheight.
	 */
	public int getThumbheight() {
		return thumbheight;
	}

	/**
	 * @param thumbheight The thumbheight to set.
	 */
	public void setThumbheight(int thumbheight) {
		this.thumbheight = thumbheight;
	}

	/**
	 * @return Returns the thumbwidth.
	 */
	public int getThumbwidth() {
		return thumbwidth;
	}

	/**
	 * @param thumbwidth The thumbwidth to set.
	 */
	public void setThumbwidth(int thumbwidth) {
		this.thumbwidth = thumbwidth;
	}

	/**
	 * @return Returns the gimages.
	 */
	public SortedSet<Image> getImages() {
		return images;
	}

	/**
	 * @param gimages The gimages to set.
	 */
	public void setImages(SortedSet images) {
		this.images = images;
	}

	/**
	 * @return the columns
	 */
	public int getColumns() {
		return columns;
	}

	/**
	 * @param columns the columns to set
	 */
	public void setColumns(int columns) {
		this.columns = columns;
	}

	/**
	 * @return the template
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * @param template the template to set
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	@Override
	public int compareTo(Gallery o) {
		
		return (StringUtils.upperCase(getGalleryname()).compareTo(StringUtils.upperCase(o.getGalleryname())));
		
	}
	
}
