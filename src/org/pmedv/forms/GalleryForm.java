/**
 * WeberknechtCMS - Open Source Content Management
 * 
 * Written and maintained by Matthias Pueski
 * 
 * Copyright (c) 2009 Matthias Pueski
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 */
package org.pmedv.forms;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;
import org.pmedv.pojos.User;
import com.itextpdf.text.pdf.codec.GifImage;

@SuppressWarnings("unchecked")
public class GalleryForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String galleryname = null;
	private String description = null;
	private int thumbheight = 120;
	private int thumbwidth = 160;
	private String gallerytext = null;
	private float ranking = 0.0f;
	private Set images = null;
	private FormFile file;
	private String imageName;
	private String imageDescription;
	private User user;

	public void reset() {
		galleryname = null;
		description = null;
		thumbheight = 0;
		thumbwidth = 0;
		gallerytext = null;
		ranking = 0.0f;

	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		if (getGalleryname() == null || getGalleryname().length() < 1) {
			errors.add("galleryname", new ActionMessage("error.galleryname.required"));
		}

		return errors;
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
	public Set getImages() {
		return images;
	}

	/**
	 * @param gimages The gimages to set.
	 */
	public void setImages(Set images) {
		this.images = images;
	}

	public FormFile getFile() {
		return file;
	}

	public void setFile(FormFile file) {
		this.file = file;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getImageDescription() {
		return imageDescription;
	}

	public void setImageDescription(String imageDescription) {
		this.imageDescription = imageDescription;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
