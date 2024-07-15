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
package org.pmedv.pojos.mailsystem;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * <p>
 * This class represents a folder inside the mail system, a folder
 * is a recursive structure which contains folders itself.
 * <p>
 * Thus every folder can contains messages and folders and
 * the user is able to organize his mails in folders, which
 * he can create, move or delete himself.
 * </p>
 * 
 * @author Matthias Pueski 3.8.2008
 * 
 *
 */
public class Folder implements Serializable, Comparable<Folder> {

	private static final long serialVersionUID = 5239271578721391278L;
	
	 /**
	  * Primary key
	  */
	private Long id;     
	
	/**
	 * The display name of the folder
	 */
    
    private String name;
    
    /**
     * These are the children folder of this folder
     */
    
    private SortedSet<Folder> children = new TreeSet<Folder>();
    
    /**
     * The parent of this folder, the value is "null", if there's no parent
     */
    
    private Folder parent;
    
    /**
     * The messages which this folder contains
     */
    
    private Set<Message> messages = new HashSet<Message>();
    
    /**
     * gets the id of the node
     * 
     * @return the id of the node
     */
    public Long getId() {
        return id;
    }
    /**
     * Sets the id of the node
     * 
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }    
    
    /**
     * Gets the name of the node
     * 
     * @return
     */
    public String getName() {
        return name;
    }   
    /**
     * Sets the name of the node
     * 
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Gets all the children of this folder
     * 
     * @return the associated chilren
     */
    public Set<Folder> getChildren() {
        return children;
    }

    /**
     * Sets the children of this folder
     * 
     * @param children
     */
    public void setChildren(SortedSet<Folder> children) {
        this.children = children;
    }
	/**
	 * Gets the parent of this folder
	 * 
	 * @return
	 */
	public Folder getParent() {
		return parent;
	}
	/**
	 * Sets the parent of this folder
	 * 
	 * @param parent the parent to set
	 */
	public void setParent(Folder parent) {
		this.parent = parent;
	}
	
	/**
	 * Checks if this folder is a root folder or not
	 * 
	 * @return true if this node is root false if not
	 */
	public boolean isRootFolder() {
		return (this.getParent() != null) ?  false : true;
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Folder o) {	
		if (o.equals(this))
			return 0;
		else 
			return -1;
	}
       

	/**
	 * Checks, if two folder objects are equal
	 * 
	 * @param obj
	 * @return
	 */
	public boolean equals(Folder obj) {

		if (this == obj) {
		    return true;
		}
		if (obj instanceof Folder) {
			Folder folder = (Folder) obj;		
			if ((this.name.equals(folder.getName())) && 
				 this.parent == folder.getParent())
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	
	public int hashCode() {
		return this.name.hashCode();
	}
    
	/**
	 * Checks if this folder has any children
	 * 
	 * @return true, if there are any, false if not
	 */
	public boolean hasChildren() {
		return getChildren().size() > 0 ? true : false;
	}
	
	/**
	 * The messages to get
	 * 
	 * @return a set of messages of this folder
	 */
	public Set<Message> getMessages() {
		return messages;
	}
	
	/**
	 * Sets the set of messages for this folder
	 * 
	 * @param messages the messages to set
	 */
	public void setMessages(Set<Message> messages) {
		this.messages = messages;
	}

}
