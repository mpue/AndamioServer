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
import java.util.HashSet;
import java.util.Set;

import org.pmedv.pojos.forums.Forum;

/**
 * This class represents an ordinary usergroup object, every usergroup can
 * have multiple users, multiple processes and multiple nodes assigned.
 * 
 * @see {@link User}
 * @see {@link Node}
 * @see {@link Process}
 * 
 * @author Matthias Pueski
 *
 */
public class Usergroup implements Serializable {

	private static final long serialVersionUID = -264630582310902731L;

	private Long id;
	
	private String name;
	private String description;
	
	private boolean newRecord;
	
	// The users linked to this group
	private Set<User> users     = new HashSet<User>();
	
	// The processes linked to this group
	private Set<Process> processes = new HashSet<Process>();
	
	// The nodes linked to this group
	private Set<Node> nodes = new HashSet<Node>();
	
	// the forums linked to this group
	private Set<Forum> forums = new HashSet<Forum>();
	
	public Usergroup() {
		
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
	 * @return returns the users assigned to this group
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

	public Set<Process> getProcesses() {
		return processes;
	}

	public void setProcesses(Set<Process> processes) {
		this.processes = processes;
	}

	/**
	 * @return the nodes
	 */
	public Set<Node> getNodes() {
		return nodes;
	}

	/**
	 * @param nodes the nodes to set
	 */
	public void setNodes(Set<Node> nodes) {
		this.nodes = nodes;
	}
	
	/**
	 * @return the forums
	 */
	public Set<Forum> getForums() {
		return forums;
	}
	
	/**
	 * @param forums the forums to set
	 */
	public void setForums(Set<Forum> forums) {
		this.forums = forums;
	}

	public boolean isNewRecord() {
		return newRecord;
	}

	public void setNewRecord(boolean newRecord) {
		this.newRecord = newRecord;
	}
	
	@Override
	
	public boolean equals(Object obj) {

		if (this == obj) {
		    return true;
		}
		if (obj instanceof Usergroup) {
			Usergroup group = (Usergroup) obj;		
			if (this.name.equals(group.getName())) return true;
		}
		return false;
	}
	
	@Override
	
	public int hashCode() {
		return this.name.hashCode();
	}
	
}
