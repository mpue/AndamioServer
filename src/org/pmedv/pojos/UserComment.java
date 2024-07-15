/**

	Andamio CMS - Open Source Content Management
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class UserComment implements Comparable<UserComment>{
	
	private Long id;
	private String text;
	// private User user;
	private Date created;
	private String author;

	
	/**
	 * @return the author
	 */
	public String getAuthor() {
	
		return author;
	}

	
	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
	
		this.author = author;
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
	 * @return the text
	 */
	public String getText() {
	
		return text;
	}
	
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
	
		this.text = text;
	}

	/**
	 * @return the created
	 */
	public Date getCreated() {
	
		return created;
	}


	
	/**
	 * @param created the created to set
	 */
	public void setCreated(Date created) {
	
		this.created = created;
	}
	
	
//	/**
//	 * @return the user
//	 */
//	public User getUser() {
//	
//		return user;
//	}
//
//	
//	/**
//	 * @param user the user to set
//	 */
//	public void setUser(User user) {
//	
//		this.user = user;
//	}


	@Override
	public int compareTo(UserComment o) {
			
		if (o.getCreated() != null && getCreated() != null) {
			if (o.getCreated().after(getCreated())) {
				return -1;
			} 
			else if (o.getCreated().before(getCreated())) {
				return 1;
			} 
			else {
				return 0;
			}						
		}
		else 
			return -1;

	}

	public String getHumanReadableTime() {
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy kk:mm");
		if (created == null) {
			created = new Date();
		}
		return dateFormat.format(created);
	}
	
}
