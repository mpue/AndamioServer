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
package org.pmedv.pojos.calendar;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.pmedv.pojos.User;

public class Calendar implements Serializable {

	private static final long serialVersionUID = 7646208634347531417L;

	private Long id;
	
	private String name;
	private String description;
	
	private boolean newRecord;
	
	// The users linked to this calendar
	private Set<User> users = new HashSet<User>();
	// The appointments linked to this calendar
	private Set<Appointment> appointments = new HashSet<Appointment>();
	// The meetings linked to this calendar
	private Set<Meeting> meetings = new HashSet<Meeting>();
	
	public Calendar() {
		
	}

	/**
	 * @return The descriptive Information about this group
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description of the group
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return The id of the group
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id The id of the group
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return returns the name of the group
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name of the group
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return returns the users assigned to this calendar
	 */
	public Set<User> getUsers() {
		return users;
	}

	/**
	 * @param users the users this group contains
	 */
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
	public boolean isNewRecord() {
		return newRecord;
	}

	public void setNewRecord(boolean newRecord) {
		this.newRecord = newRecord;
	}
	
	public Set<Appointment> getAppointments() {
		return appointments;
	}

	public void setAppointments(Set<Appointment> appointments) {
		this.appointments = appointments;
	}

	public Set<Meeting> getMeetings() {
		return meetings;
	}

	public void setMeetings(Set<Meeting> meetings) {
		this.meetings = meetings;
	}

	@Override
	
	public boolean equals(Object obj) {

		if (this == obj) {
		    return true;
		}
		if (obj instanceof Calendar) {
			Calendar calendar = (Calendar) obj;		
			if (this.id.equals(calendar.getId())) return true;
		}
		return false;
	}
	
	@Override
	
	public int hashCode() {
		return this.name.hashCode();
	}
	
}
