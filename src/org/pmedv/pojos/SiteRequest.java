/**
 * WeberknechtCMS - Open Source Content Management Written and maintained by Matthias Pueski
 * 
 * Copyright (c) 2009 Matthias Pueski
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 */
package org.pmedv.pojos;

/**
 * Feb 8, 2007 : 8:36:10 PM
 * 
 * @author Matthias Pueski
 */

public class SiteRequest {

	private static final String SEPARATOR = "#";

	public SiteRequest() {

	}

	private Long id;
	private String username;
	private String reqtime;
	private String userip;
	private String useragent;
	private String contentname;
	private String domain;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUseragent() {
		return useragent;
	}

	public void setUseragent(String useragent) {
		this.useragent = useragent;
	}

	public String getUserip() {
		return userip;
	}

	public void setUserip(String userip) {
		this.userip = userip;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getReqtime() {
		return reqtime;
	}

	public void setReqtime(String reqtime) {
		this.reqtime = reqtime;
	}

	public String getContentname() {
		return contentname;
	}

	public void setContentname(String contentname) {
		this.contentname = contentname;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Override
	public String toString() {

		StringBuffer s = new StringBuffer();

		s.append(id);
		s.append(SEPARATOR);
		s.append(username);
		s.append(SEPARATOR);
		s.append(reqtime);
		s.append(SEPARATOR);
		s.append(userip);
		s.append(SEPARATOR);
		s.append(useragent);
		s.append(SEPARATOR);
		s.append(contentname);

		return s.toString();

	}

}
