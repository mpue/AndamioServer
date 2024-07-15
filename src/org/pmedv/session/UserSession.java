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
package org.pmedv.session;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.pojos.User;

/**
 * User: Matthias Pueski Date: 10.03.2003 Time: 20:32:52
 */
public class UserSession {

	protected final static Log log = LogFactory.getLog(UserSession.class);

	private HttpSession session;
	private String login;
	private String pass;
	private String permission;
	private String sessionID;
	private Date lastAccessedTime;
	private Date creationTime;
	private int timeInactive;
	private String currentPage;
	private User user;

	public UserSession(boolean auto, HttpServletRequest request) {
		init(auto, request);
	}

	public UserSession() {
	}

	public void init(boolean auto, HttpServletRequest request) {
		session = request.getSession(auto);
		if (auto) {
			session.setAttribute("login", "null");
			session.setAttribute("pass", "null");
			session.setAttribute("permission", "false");
			session.setMaxInactiveInterval(3600); // 60 Minutes
		}
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getLogin() {
		return login;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getPass() {
		return pass;
	}

	public boolean isNew() {
		return session.isNew();
	}

	public void getSessionInfo(HttpSession session) {
		sessionID = session.getId();
		lastAccessedTime = new Date(session.getLastAccessedTime());
		creationTime = new Date(session.getCreationTime());
		timeInactive = session.getMaxInactiveInterval() / 60;
	}

	public void setAttributes() {
		session.setAttribute("login", login);
		session.setAttribute("pass", pass);
		session.setAttribute("permission", permission);
	}

	public void getAttributes() {
		try {
			login = (String) session.getAttribute("login");
			pass = (String) session.getAttribute("pass");
			permission = (String) session.getAttribute("permission");
		}
		catch (NullPointerException n) {
			log.debug("Seems to be that there is no valid session, please login.");
		}
	}

	public boolean permit() {
		String permit = "false";
		boolean permission = false;
		try {
			permit = (String) session.getAttribute("permission");
		}
		catch (Exception e) {
			return false;
		}
		if (permit == null)
			permission = false;
		else if (permit.equals("true"))
			permission = true;
		return permission;
	}

	public void kill() {
		session.invalidate();
	}

	public String getSessionID() {
		return session.getId();
	}

	public Date getLastAccess() {
		return new Date(session.getLastAccessedTime());
	}

	public Date getCreationTime() {
		return new Date(session.getCreationTime());
	}

	public String getMaxInactiveInterval() {
		return Integer.toString(session.getMaxInactiveInterval() / 60);
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getPermission() {
		return (permission);
	}

	/**
	 * @return Returns the session.
	 */
	public HttpSession getSession() {
		return session;
	}

	/**
	 * @param session The session to set.
	 */
	public void setSession(HttpSession session) {
		// SessionManager.session = session;
	}

	@SuppressWarnings("unchecked")
	public void setProcessPermissions(List processes) {
		Iterator processListIterator = processes.iterator();
		while (processListIterator.hasNext()) {
			org.pmedv.pojos.Process currentProcess = (org.pmedv.pojos.Process) processListIterator.next();
			session.setAttribute(currentProcess.getName(), "true");
			log.debug("Setting user process permission for process : " + currentProcess.getName());
		}
		session.setAttribute("permissionsset", "true");
	}

	public boolean permissionsAreSet() {
		try {
			if (session.getAttribute("permissionsset").equals("true"))
				return true;
			else
				return false;
		}
		catch (NullPointerException n) {
			return false;
		}
	}

	/**
	 * @return the currentPage
	 */
	public String getCurrentPage() {
		return currentPage;
	}

	/**
	 * @param currentPage the currentPage to set
	 */
	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public Date getLastAccessedTime() {
		return lastAccessedTime;
	}

	public void setLastAccessedTime(Date lastAccessedTime) {
		this.lastAccessedTime = lastAccessedTime;
	}

	public int getTimeInactive() {
		return timeInactive;
	}

	public void setTimeInactive(int timeInactive) {
		this.timeInactive = timeInactive;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

}
