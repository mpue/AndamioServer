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
import java.util.Date;

import org.pmedv.beans.objects.MessageStatus;

/**
 * A message object is located inside a folder and represents
 * an ordinary message between users.
 * 
 * A message consists:
 * 
 * The subject, which is displayed inside the Mail overview
 * The body, which is the text of the message and
 * The status, which indicates if the message has been read, or not for example.
 * 
 * @author Matthias Pueski 3.8.2008
 *
 */
public class Message implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6355704044967015554L;

	/**
	 * Primary key
	 */
	
	private Long id;
	
	/**
	 * The subject of the message
	 */
	
	private String subject;
	
	/**
	 * The message body
	 */
	
	private String body;
	
	/**
	 * The status of that message, either read or unread
	 */
	
	private MessageStatus status;
	
	/**
	 * The sender of the message
	 */
	private String sender;
	
	/**
	 * The date on which the message was received
	 */
	
	private Date received;
	
	/**
	 * The date on which the message was received
	 */
	
	private Date sent;
	
	/**
	 * If the message is a reply message, the reference id of the original message is stored here
	 */
	
	private Long inReplyToMessageId;
	
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
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}
	
	/**
	 * @return the status
	 */
	public MessageStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(MessageStatus status) {
		this.status = status;
	}

	
	/**
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}

	
	/**
	 * @param sender the sender to set
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	/**
	 * @return the received
	 */
	public Date getReceived() {
		return received;
	}

	
	/**
	 * @param received the received to set
	 */
	public void setReceived(Date received) {
		this.received = received;
	}
	
	
	/**
	 * @return the sent
	 */
	public Date getSent() {
	
		return sent;
	}
	
	/**
	 * @param sent the sent to set
	 */
	public void setSent(Date sent) {
	
		this.sent = sent;
	}

	/**
	 * @return the inReplyToMessageId
	 */
	public Long getInReplyToMessageId() {
	
		return inReplyToMessageId;
	}


	
	/**
	 * @param inReplyToMessageId the inReplyToMessageId to set
	 */
	public void setInReplyToMessageId(Long inReplyToMessageId) {
	
		this.inReplyToMessageId = inReplyToMessageId;
	}

	
}
