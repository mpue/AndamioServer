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

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.pmedv.pojos.calendar.Calendar;
import org.pmedv.pojos.mailsystem.Account;

/**
 * This class represents a user of this cms, which can be assigned to multiple
 * groups Any user can have one or more avatars (in order to switch between
 * multiple avatars)
 * 
 * @author Matthias Pueski
 */
public class User implements Serializable, Comparable<User> {

	private static final long	serialVersionUID	= -3620618813220576716L;

	private Long				id;
	private String				name;
	private String				firstName;
	private String				lastName;
	private String				title;
	private String				land;
	private String				ort;
	private String				telefon;
	private String				email;
	private int					ranking;
	private String				password;
	private Date				joinDate;
	private Date				birthDate;
	private Date 				lastActivity;
	private boolean				active;
	private boolean				newRecord;

	private UserProfile			userProfile;

	private Set<Avatar>		 avatars   	   = new HashSet<Avatar>();
	private Set<Usergroup>	 groups		   = new HashSet<Usergroup>();
	private Set<Account>	 accounts	   = new HashSet<Account>();
	private Set<UserComment> userComments  = new HashSet<UserComment>();
	private Set<Gallery>	 userGalleries = new HashSet<Gallery>();
	private Set<Attribute>   attributes    = new HashSet<Attribute>();
	private Set<Country>     countries     = new HashSet<Country>();
	private Set<Calendar>    calendars     = new HashSet<Calendar>();
	
	public User() {

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
	 * @return Returns the title
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
	
	/**
	 * Gets the date of the last activity of this user
	 * 
	 * @return
	 */
	public Date getLastActivity() {
		return lastActivity;
	}
	
	/**
	 * Sets the date of last activity
	 * 
	 * @param lastActivity
	 */
	public void setLastActivity(Date lastActivity) {
		this.lastActivity = lastActivity;
	}

	/**
	 * @return Returns the groups, the user is member of
	 */

	public Set<Usergroup> getGroups() {

		return groups;
	}

	/**
	 * @param groups The groups, the use belongs to
	 */

	public void setGroups(Set<Usergroup> groups) {

		this.groups = groups;
	}

	/**
	 * @return the avatars
	 */
	public Set<Avatar> getAvatars() {

		return avatars;
	}

	/**
	 * @param avatars the avatars to set
	 */
	public void setAvatars(Set<Avatar> avatars) {

		this.avatars = avatars;
	}

	public boolean isNewRecord() {

		return newRecord;
	}

	public void setNewRecord(boolean newRecord) {

		this.newRecord = newRecord;
	}

	/**
	 * @return the accounts
	 */
	public Set<Account> getAccounts() {

		return accounts;
	}

	/**
	 * @param accounts the accounts to set
	 */
	public void setAccounts(Set<Account> accounts) {

		this.accounts = accounts;
	}

	public UserProfile getUserProfile() {

		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {

		this.userProfile = userProfile;
	}

	/**
	 * @return the userComments
	 */
	public Set<UserComment> getUserComments() {

		return userComments;
	}

	/**
	 * @param userComments the userComments to set
	 */
	public void setUserComments(Set<UserComment> userComments) {

		this.userComments = userComments;
	}

	/**
	 * @return the userGalleries
	 */
	public Set<Gallery> getUserGalleries() {

		return userGalleries;
	}

	/**
	 * @param userGalleries the userGalleries to set
	 */
	public void setUserGalleries(Set<Gallery> userGalleries) {

		this.userGalleries = userGalleries;
	}
	
	public Set<Attribute> getAttributes() {
		return attributes;
	}
	
	public void setAttributes(Set<Attribute> attributes) {
		this.attributes = attributes;
	}
	
	/**
	 * @return the countries
	 */
	public Set<Country> getCountries() {
		return countries;
	}


	/**
	 * @param countries the countries to set
	 */
	public void setCountries(Set<Country> countries) {
		this.countries = countries;
	}

	public Set<Calendar> getCalendars() {
		return calendars;
	}

	public void setCalendars(Set<Calendar> calendars) {
		this.calendars = calendars;
	}
	
	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}
		if ((obj != null) && (obj.getClass().equals(this.getClass()))) {
			User user = (User) obj;
			if (this.id == user.getId())
				return true;
		}
		return false;
	}

	@Override
	public int compareTo(User u) {
		return this.getName().compareTo(u.getName());
	}

	@Override
	public String toString() {

		StringBuilder s = new StringBuilder();

		s.append(getId());
		s.append(",");
		s.append(getName());

		return s.toString();

	}

}
