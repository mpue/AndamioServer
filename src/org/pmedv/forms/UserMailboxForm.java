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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.pmedv.beans.objects.FolderBean;
import org.pmedv.beans.objects.MessageBean;
import org.pmedv.beans.objects.UserBean;

public class UserMailboxForm extends ActionForm {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public void reset(ActionMapping mapping, ServletRequest request) {
		user = new UserBean();
	}
	
	private UserBean user;
	private ArrayList<FolderBean> folder;
	private ArrayList<MessageBean> messages;
	private FolderBean currentFolder;
	

	private String receiver;
	private String subject;
	private String message;
	
	public Long getId() {
		return Long.valueOf(user.getId());
	}
	
	public void setId(Long id) {
		user.setId(id.intValue());
	}
	
	public UserBean getUser() {
		return user;
	}

	public void setUser(UserBean user) {
		this.user = user;
	}
	
	/**
	 * @return the folder
	 */
	public ArrayList<FolderBean> getFolder() {
	
		return folder;
	}
	
	/**
	 * @param folder the folder to set
	 */
	public void setFolder(ArrayList<FolderBean> folder) {
	
		this.folder = folder;
	}

	/**
	 * @return the messages
	 */
	public ArrayList<MessageBean> getMessages() {
	
		return messages;
	}

	
	/**
	 * @param messages the messages to set
	 */
	public void setMessages(ArrayList<MessageBean> messages) {
	
		this.messages = messages;
	}

	/**
	 * @return the currentFolder
	 */
	public FolderBean getCurrentFolder() {
	
		return currentFolder;
	}

	
	/**
	 * @param currentFolder the currentFolder to set
	 */
	public void setCurrentFolder(FolderBean currentFolder) {
	
		this.currentFolder = currentFolder;
	}

	
	/**
	 * @return the receiver
	 */
	public String getReceiver() {
		return receiver;
	}

	
	/**
	 * @param receiver the receiver to set
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
}
