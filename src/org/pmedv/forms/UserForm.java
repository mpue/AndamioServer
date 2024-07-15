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
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.User;

@SuppressWarnings("unchecked")
public class UserForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	private String name = null;
	private String firstName = null;
	private String lastName = null;
	private String title = null;
	private String land = null;
	private String ort = null;
	private String telefon = null;
	private String email = null;
	private int ranking = 0;
	private String password = null;
	private Date joinDate = new Date();
	private Date birthDate = new Date();

	private boolean active = true;
	private int position = 0;
	private String usergroup = null;
	private List<?> availableGroups = DAOManager.getInstance().getUsergroupDAO().findAllItems();
	private List<?> users;

	/**
	 * Reset all properties to their default values.
	 * 
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {

		this.name = null;
		this.firstName = null;
		this.lastName = null;
		this.title = null;
		this.land = null;
		this.ort = null;
		this.telefon = null;
		this.email = null;
		this.ranking = 0;
		this.password = null;
		this.joinDate = null;
		this.active = true;
		this.position = 0;
		this.usergroup = null;
	}

	/**
	 * Validation
	 */

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

		ActionErrors errors = new ActionErrors();

		if (getName() == null || getName().length() < 1) {
			errors.add("name", new ActionMessage("error.benutzername.required"));
		}

		User user = (User) DAOManager.getInstance().getUserDAO().findByName(getName());

		if (user != null)
			errors.add("name", new ActionMessage("error.user.alreadyexists"));

		return errors;
	}

	/**
	 * @return Returns the active.
	 */
	public boolean getActive() {

		return active;
	}

	/**
	 * @param active The active to set.
	 */
	public void setActive(boolean active) {

		this.active = active;
	}

	/**
	 * @return Returns the email.
	 */
	public String getEmail() {

		return email;
	}

	/**
	 * @param email The email to set.
	 */
	public void setEmail(String email) {

		this.email = email;
	}

	/**
	 * @return Returns the title.
	 */
	public String getTitle() {

		return title;
	}

	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {

		this.title = title;
	}

	/**
	 * @return Returns the joinDate.
	 */
	public Date getJoinDate() {

		return joinDate;
	}

	/**
	 * @param joinDate The joinDate to set.
	 */
	public void setJoinDate(Date joinDate) {

		this.joinDate = joinDate;
	}

	/**
	 * @return Returns the land.
	 */
	public String getLand() {

		return land;
	}

	/**
	 * @param land The land to set.
	 */
	public void setLand(String land) {

		this.land = land;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {

		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {

		this.name = name;
	}

	/**
	 * @return Returns the ort.
	 */
	public String getOrt() {

		return ort;
	}

	/**
	 * @param ort The ort to set.
	 */
	public void setOrt(String ort) {

		this.ort = ort;
	}

	/**
	 * @return Returns the password.
	 */
	public String getPassword() {

		return password;
	}

	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password) {

		this.password = password;
	}

	/**
	 * @return Returns the position.
	 */
	public int getPosition() {

		return position;
	}

	/**
	 * @param position The position to set.
	 */
	public void setPosition(int position) {

		this.position = position;
	}

	/**
	 * @return Returns the ranking.
	 */
	public int getRanking() {

		return ranking;
	}

	/**
	 * @param ranking The ranking to set.
	 */
	public void setRanking(int ranking) {

		this.ranking = ranking;
	}

	/**
	 * @return Returns the telefon.
	 */
	public String getTelefon() {

		return telefon;
	}

	/**
	 * @param telefon The telefon to set.
	 */
	public void setTelefon(String telefon) {

		this.telefon = telefon;
	}

	/**
	 * @return the availableGroups
	 */
	public List<?> getAvailableGroups() {

		return availableGroups;
	}

	/**
	 * @param availableGroups the availableGroups to set
	 */
	public void setAvailableGroups(List<?> availableGroups) {

		this.availableGroups = availableGroups;
	}

	/**
	 * @return the usergroup
	 */
	public String getUsergroup() {

		return usergroup;
	}

	/**
	 * @param usergroup the usergroup to set
	 */
	public void setUsergroup(String usergroup) {

		this.usergroup = usergroup;
	}

	/**
	 * @return the users
	 */
	public List<?> getUsers() {

		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(List<?> users) {

		this.users = users;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {

		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {

		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {

		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {

		this.lastName = lastName;
	}

	/**
	 * @return the birthDate
	 */
	public Date getBirthDate() {

		return birthDate;
	}

	/**
	 * @param birthDate the birthDate to set
	 */
	public void setBirthDate(Date birthDate) {

		this.birthDate = birthDate;
	}

}
