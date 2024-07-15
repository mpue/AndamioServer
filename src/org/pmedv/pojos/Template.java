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

import org.pmedv.beans.objects.GalleryType;
import org.pmedv.beans.objects.TemplateBean;

/**
 * This is the hibernate proxy for the object {@link TemplateBean}
 * 
 * @author Matthias Pueski
 *
 */
public class Template {
	
	private Long id;	
	private String name;
	private String description;
	private GalleryType galleryType;
	private String tableStyleClass;
	private String tableDataStyleClass;
	private String tableRowStyleClass;
	private boolean placeInDiv;
	private String divStyleClass;
	private String html;
	private String css;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the galleryType
	 */
	public GalleryType getGalleryType() {
		return galleryType;
	}
	/**
	 * @param galleryType the galleryType to set
	 */
	public void setGalleryType(GalleryType galleryType) {
		this.galleryType = galleryType;
	}
	/**
	 * @return the tableStyleClass
	 */
	public String getTableStyleClass() {
		return tableStyleClass;
	}
	/**
	 * @param tableStyleClass the tableStyleClass to set
	 */
	public void setTableStyleClass(String tableStyleClass) {
		this.tableStyleClass = tableStyleClass;
	}
	/**
	 * @return the tableDataStyleClass
	 */
	public String getTableDataStyleClass() {
		return tableDataStyleClass;
	}
	/**
	 * @param tableDataStyleClass the tableDataStyleClass to set
	 */
	public void setTableDataStyleClass(String tableDataStyleClass) {
		this.tableDataStyleClass = tableDataStyleClass;
	}
	/**
	 * @return the tableRowStyleClass
	 */
	public String getTableRowStyleClass() {
		return tableRowStyleClass;
	}
	/**
	 * @param tableRowStyleClass the tableRowStyleClass to set
	 */
	public void setTableRowStyleClass(String tableRowStyleClass) {
		this.tableRowStyleClass = tableRowStyleClass;
	}
	/**
	 * @return the placeInDiv
	 */
	public boolean isPlaceInDiv() {
		return placeInDiv;
	}
	/**
	 * @param placeInDiv the placeInDiv to set
	 */
	public void setPlaceInDiv(boolean placeInDiv) {
		this.placeInDiv = placeInDiv;
	}
	/**
	 * @return the divStyleClass
	 */
	public String getDivStyleClass() {
		return divStyleClass;
	}
	/**
	 * @param divStyleClass the divStyleClass to set
	 */
	public void setDivStyleClass(String divStyleClass) {
		this.divStyleClass = divStyleClass;
	}
	/**
	 * @return the html
	 */
	public String getHtml() {
		return html;
	}
	/**
	 * @param html the html to set
	 */
	public void setHtml(String html) {
		this.html = html;
	}
	/**
	 * @return the css
	 */
	public String getCss() {
		return css;
	}
	/**
	 * @param css the css to set
	 */
	public void setCss(String css) {
		this.css = css;
	}

	

}
