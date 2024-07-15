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

import java.util.Date;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.pmedv.pojos.Avatar;
import org.pmedv.pojos.User;
import org.pmedv.pojos.Usergroup;

public class UserShowForm extends ActionForm {

	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean equals(Object rhs) {
		return user.equals(rhs);
	}

	public int hashCode() {
		assert false : "hashCode not designed";
		return 42;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		user = new User();
	}

	public long getId() {
		return user.getId();
	}

	public void setId(Long id) {
		user.setId(id);
	}

	/**
	 * @return Returns the active.
	 */
	public boolean getActive() {
		return user.getActive();
	}

	/**
	 * @param active The active to set.
	 */
	public void setActive(boolean active) {
		user.setActive(active);
	}

	/**
	 * @return Returns the email.
	 */
	public String getEmail() {
		return user.getEmail();
	}

	/**
	 * @param email The email to set.
	 */
	public void setEmail(String email) {
		user.setEmail(email);
	}

	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return user.getTitle();
	}

	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		user.setTitle(title);
	}

	/**
	 * @return Returns the joinDate.
	 */
	public Date getJoinDate() {
		return user.getJoinDate();
	}

	/**
	 * @param joinDate The joinDate to set.
	 */
	public void setJoinDate(Date joinDate) {
		user.setJoinDate(joinDate);
	}

	/**
	 * @return Returns the land.
	 */
	public String getLand() {
		return user.getLand();
	}

	/**
	 * @param land The land to set.
	 */
	public void setLand(String land) {
		user.setLand(land);
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return user.getName();
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		user.setName(name);
	}

	/**
	 * @return Returns the ort.
	 */
	public String getOrt() {
		return user.getOrt();
	}

	/**
	 * @param ort The ort to set.
	 */
	public void setOrt(String ort) {
		user.setOrt(ort);
	}

	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return user.getPassword();
	}

	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		user.setPassword(password);
	}

	/**
	 * @return Returns the ranking.
	 */
	public int getRanking() {
		return user.getRanking();
	}

	/**
	 * @param ranking The ranking to set.
	 */
	public void setRanking(int ranking) {
		user.setRanking(ranking);
	}

	/**
	 * @return Returns the telefon.
	 */
	public String getTelefon() {
		return user.getTelefon();
	}

	/**
	 * @param telefon The telefon to set.
	 */
	public void setTelefon(String telefon) {
		user.setTelefon(telefon);
	}

	/**
	 * @return the groups the user belongs to
	 */
	public Set<Usergroup> getGroups() {
		return user.getGroups();
	}

	/**
	 * @param groups
	 */
	public void setGroups(Set<Usergroup> groups) {
		user.setGroups(groups);
	}

	/**
	 * @return the avatars of the user
	 */
	public Set<Avatar> getAvatars() {
		return user.getAvatars();
	}

	/**
	 * @param avatars
	 */
	public void setAvatars(Set<Avatar> avatars) {
		user.setAvatars(avatars);
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {

		return user.getFirstName();
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {

		user.setFirstName(firstName);
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {

		return user.getLastName();
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {

		user.setLastName(lastName);
	}

	/**
	 * @return the birthDate
	 */
	public Date getBirthDate() {

		return user.getBirthDate();
	}

	/**
	 * @param birthDate the birthDate to set
	 */
	public void setBirthDate(Date birthDate) {

		user.setBirthDate(birthDate);
	}

}
