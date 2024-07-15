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

import org.pmedv.beans.objects.MailServerAccountType;



/**
 * This class represents a mail account for a user. 
 * 
 * A user may have as many accounts as he wants to have, but has at least 
 * one local account for internal messaging between the users.
 * 
 *  
 * @author Matthias Pueski 3.8.2008
 *
 */
public class Account implements Serializable {

	private static final long serialVersionUID = -8201848202415980812L;

	/**
     * Primary Key
     */
    private Long id;
    
    /**
     * The name of this mail account
     */
    
    private String name;
    
    /**
     * The type of this account
     */
    private MailServerAccountType type;
    
    /**
     * The host for receiving mails (pop server for example)
     */
    private String recvhost;
    
    /**
     * The host to send mails with (e.g. SMTP server)
     */
    
    private String sendhost;
    
    /**
     * The username for the remote host
     */
    private String remoteuser;
    
    /**
     * The password for the remote server
     */
    
    private String remotepass;
    
    /**
     * The folder structure for this account
     */
    private  Set<Folder> folders = new HashSet<Folder>();
    
	/**
	 * @return the folders
	 */
	public Set<Folder> getFolders() {
		return folders;
	}

	/**
	 * @param folders the folders to set
	 */
	public void setFolders(Set<Folder> folders) {
		this.folders = folders;
	}

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
	 * @return the recvhost
	 */
	public String getRecvhost() {
		return recvhost;
	}

	/**
	 * @param recvhost the recvhost to set
	 */
	public void setRecvhost(String recvhost) {
		this.recvhost = recvhost;
	}

	/**
	 * @return the remotepass
	 */
	public String getRemotepass() {
		return remotepass;
	}

	/**
	 * @param remotepass the remotepass to set
	 */
	public void setRemotepass(String remotepass) {
		this.remotepass = remotepass;
	}

	/**
	 * @return the remoteuser
	 */
	public String getRemoteuser() {
		return remoteuser;
	}

	/**
	 * @param remoteuser the remoteuser to set
	 */
	public void setRemoteuser(String remoteuser) {
		this.remoteuser = remoteuser;
	}

	/**
	 * @return the sendhost
	 */
	public String getSendhost() {
		return sendhost;
	}

	/**
	 * @param sendhost the sendhost to set
	 */
	public void setSendhost(String sendhost) {
		this.sendhost = sendhost;
	}

	/**
	 * @return the type
	 */
	public MailServerAccountType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(MailServerAccountType type) {
		this.type = type;
	}
    
}
