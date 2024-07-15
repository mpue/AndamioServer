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

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.pmedv.pojos.Attribute;
import org.pmedv.pojos.Avatar;
import org.pmedv.pojos.Gallery;
import org.pmedv.pojos.User;
import org.pmedv.pojos.UserComment;
import org.pmedv.pojos.UserProfile;

public class UserProfileForm extends ActionForm {

	private ArrayList<UserComment> comments;
	private ArrayList<Gallery> galleries;
	private ArrayList<Attribute> attributes;
	private ArrayList<Avatar> avatars;
	private FormFile file;
	private String description;
	private boolean defaultImage = false;
	private User user;

	private static final long serialVersionUID = 1L;

	@Override
	public void reset(ActionMapping mapping, ServletRequest request) {
		user = new User();
		UserProfile p = new UserProfile();
		user.setUserProfile(p);
	}

	public Long getId() {
		return user.getId();
	}

	public void setId(Long id) {
		user.setId(id);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getPageContent() {
		return user.getUserProfile().getPageContent();
	}

	public void setPageContent(String content) {

		if (user.getUserProfile() == null) {
			UserProfile p = new UserProfile();
			user.setUserProfile(p);
		}

		user.getUserProfile().setPageContent(content);
	}

	public int hashCode() {
		assert false : "hashCode not designed";
		return 42;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		user = new User();
	}

	public String getCountry() {
		return user.getLand();
	}

	public void setCountry(String country) {
		user.setLand(country);
	}

	public String getCity() {
		return user.getOrt();
	}

	public void setCity(String city) {
		user.setOrt(city);
	}

	/**
	 * @return the comments
	 */
	public ArrayList<UserComment> getComments() {

		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(ArrayList<UserComment> comments) {

		this.comments = comments;
	}

	/**
	 * @return the galleries
	 */
	public ArrayList<Gallery> getGalleries() {

		return galleries;
	}

	/**
	 * @param galleries the galleries to set
	 */
	public void setGalleries(ArrayList<Gallery> galleries) {

		this.galleries = galleries;
	}

	/**
	 * @return the attributes
	 */
	public ArrayList<Attribute> getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(ArrayList<Attribute> attributes) {
		this.attributes = attributes;
	}

	public ArrayList<Avatar> getAvatars() {
		return avatars;
	}

	public void setAvatars(ArrayList<Avatar> avatars) {
		this.avatars = avatars;
	}

	public FormFile getFile() {
		return file;
	}

	public void setFile(FormFile file) {
		this.file = file;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isDefaultImage() {
		return defaultImage;
	}

	public void setDefaultImage(boolean defaultImage) {
		this.defaultImage = defaultImage;
	}

}
