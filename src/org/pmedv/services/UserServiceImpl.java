/**
 * WeberknechtCMS - Open Source Content Management 
 * 
 * Written and maintained by Matthias Pueski
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
package org.pmedv.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.beans.objects.AttributeBean;
import org.pmedv.beans.objects.AvatarBean;
import org.pmedv.beans.objects.CountryBean;
import org.pmedv.beans.objects.UserBean;
import org.pmedv.beans.objects.UsergroupBean;
import org.pmedv.beans.objects.calendar.AppointmentBean;
import org.pmedv.beans.objects.calendar.CalendarBean;
import org.pmedv.beans.objects.calendar.MeetingBean;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.core.util.IProgressMonitor;
import org.pmedv.pojos.Attribute;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.Country;
import org.pmedv.pojos.User;
import org.pmedv.pojos.UserProfile;
import org.pmedv.pojos.Usergroup;
import org.pmedv.pojos.calendar.Appointment;
import org.pmedv.pojos.calendar.Calendar;
import org.pmedv.pojos.calendar.Meeting;
import org.pmedv.util.ResourceUtils;
import org.pmedv.util.SessionUtil;

public class UserServiceImpl implements UserService {
	
	private static final Log log = LogFactory.getLog(UserServiceImpl.class);

	@Override
	public boolean create(UserBean user) throws IllegalArgumentException {

		if (user == null)
			throw new IllegalArgumentException("UserBean must not be null.");
		if (user.getName() == null)
			throw new IllegalArgumentException("Username must not be null.");
		if (user.getName().length() < 4)
			throw new IllegalArgumentException("Username must be at least 4 digits long.");
		if (user.getName().equalsIgnoreCase("admin")) {
			throw new IllegalArgumentException("Administrative authorities cannot be created.");
		}

		User u = (User) DAOManager.getInstance().getUserDAO().findByName(user.getName());

		if (u != null)
			throw new IllegalArgumentException("A user with this name already exists.");

		// create user (copy values from bean to pojo)		
		u = new User();

		u.setName(user.getName());
		u.setFirstName(user.getFirstName());
		u.setLastName(user.getLastName());
		u.setActive(user.isActive());
		u.setEmail(user.getEmail());
		u.setJoinDate(new Date());
		u.setLand(user.getLand());
		u.setName(user.getName());
		u.setNewRecord(true);
		u.setOrt(user.getOrt());
		u.setPassword(user.getPassword());
		u.setRanking(0);
		u.setTelefon(user.getTelefon());
		u.setTitle(user.getTitle());
			
		// create and refetch user to get the id
		
		UserProfile profile = new UserProfile();									
		profile.setPageContent("empty");
		
		boolean create = DAOManager.getInstance().getUserDAO().createAndStore(u,profile) != null;		
		u = (User)DAOManager.getInstance().getUserDAO().findByName(u.getName());
		
		// create default user calendar if create was successful
		if (create) {
			
//			log.debug("Creating default calendar for user "+user.getName());
//			
//			CalendarDAO calendarDAO = (CalendarDAO)AppContext.getApplicationContext().getBean("calendarDAO");
//			
//			Calendar calendar = new Calendar();
//			calendar.setName(user.getName()+".default");
//			calendar.setDescription("Personal calendar of "+user.getName());
//			// If calendar creation was successfull, assign calendar to user
//			
//			calendar = (Calendar) calendarDAO.createAndStore(calendar);
//			
//			if (calendar != null) {
//				
//				calendar = (Calendar)calendarDAO.findByName(user.getName()+".default");
//				
//				UserDAO userDAO = DAOManager.getInstance().getUserDAO();
//				userDAO.addCalendar(calendar.getId(), u.getId());
//						
//			}
//			else {
//				log.error("could not create default calendar for user "+user.getName());
//			}

			Usergroup users = (Usergroup)DAOManager.getInstance().getUsergroupDAO().findByName("user");																										
			DAOManager.getInstance().getUserDAO().addGroup(users.getId(), u.getId());
			
			Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);	
			
			String basepath = config.getBasepath();									
			String userDir = basepath+"users/"+u.getName();
			
			File f = new File(userDir);
			
			if (!f.exists())
				f.mkdir();
			
			String userGalleryDir = basepath+"users/"+u.getName()+"/galleries/";

			File f1 = new File(userGalleryDir);
			
			if (!f1.exists())
				f1.mkdir();

			String avatarDir = basepath+"users/"+u.getName()+"/avatars/";

			f1 = new File(avatarDir);
			
			if (!f1.exists())
				f1.mkdir();
			
			ResourceUtils.createDefaultMailAccount(u.getName());			
			
			return true;
		}
		
		log.error("Could not create user "+user.getName());
		
		return false;
	}

	@Override
	public boolean delete(UserBean user) throws IllegalArgumentException {

		if (user == null)
			throw new IllegalArgumentException("UserBean must not be null.");
		if (user.getName() == null)
			throw new IllegalArgumentException("Username must not be null.");
		if (user.getName().equalsIgnoreCase("admin"))
			throw new IllegalArgumentException("Administrative authorities cannot be deleted.");

		User u = (User) DAOManager.getInstance().getUserDAO().findByID(user.getId());
		
		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);			
		String basepath = config.getBasepath();									
		
		String userDir = basepath+"users/"+u.getName();

		File f = new File(userDir);
		
		try {
			FileUtils.deleteDirectory(f);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return DAOManager.getInstance().getUserDAO().delete(u);

	}

	@Override
	public UserBean findById(Long id) throws IllegalArgumentException {

		UserBean u = new UserBean();

		User user = (User) DAOManager.getInstance().getUserDAO().findByID(id);

		u.setId(user.getId().intValue());
		u.setName(user.getName());
		u.setFirstName(user.getFirstName());
		u.setLastName(user.getLastName());
		u.setActive(user.getActive());
		u.setEmail(user.getEmail());
		u.setJoinDate(user.getJoinDate());
		u.setLand(user.getLand());
		u.setName(user.getName());
		u.setNewRecord(false);
		u.setOrt(user.getOrt());
		u.setPassword(user.getPassword());
		u.setRanking(user.getRanking());
		u.setTelefon(user.getTelefon());
		u.setTitle(user.getTitle());

		if (SessionUtil.getOnlineUsers().contains(u))
			u.setOnline(true);
		else
			u.setOnline(false);

		copyGroups(u, user);
		copyAttributes(u, user);
		copyCountries(u, user);		
		copyCalendars(u,user);
		
		return u;

	}

	@Override
	public UserBean findByName(String name) throws IllegalArgumentException {

		UserBean u = new UserBean();

		User user = (User) DAOManager.getInstance().getUserDAO().findByName(name);

		u.setId(user.getId().intValue());
		u.setName(user.getName());
		u.setFirstName(user.getFirstName());
		u.setLastName(user.getLastName());
		u.setActive(user.getActive());
		u.setEmail(user.getEmail());
		u.setJoinDate(user.getJoinDate());
		u.setLand(user.getLand());
		u.setName(user.getName());
		u.setNewRecord(false);
		u.setOrt(user.getOrt());
		u.setPassword(user.getPassword());
		u.setRanking(user.getRanking());
		u.setTelefon(user.getTelefon());
		u.setTitle(user.getTitle());
		
		if (SessionUtil.getOnlineUsers().contains(u))
			u.setOnline(true);
		else
			u.setOnline(false);

		copyGroups(u, user);
		copyAttributes(u, user);
		copyCountries(u, user);
		copyCalendars(u, user);

		return u;

	}

	@Override
	public UserBean findSimilarByName(String name) throws IllegalArgumentException {

		return null;
	}

	@Override
	public ArrayList<UserBean> getUsers() {

		ArrayList<UserBean> onlineUsers = SessionUtil.getOnlineUsers();
		
		ArrayList<UserBean> users = new ArrayList<UserBean>();

		List<?> _users = DAOManager.getInstance().getUserDAO().findAllItems();

		for (Iterator<?> it = _users.iterator(); it.hasNext();) {

			User user = (User) it.next();

			UserBean u = new UserBean();

			u.setId(user.getId().intValue());
			u.setName(user.getName());
			u.setFirstName(user.getFirstName());
			u.setLastName(user.getLastName());
			u.setActive(user.getActive());
			u.setEmail(user.getEmail());
			u.setJoinDate(user.getJoinDate());
			u.setLand(user.getLand());
			u.setName(user.getName());
			u.setNewRecord(false);
			u.setOrt(user.getOrt());
			u.setPassword(user.getPassword());
			u.setRanking(user.getRanking());
			u.setTelefon(user.getTelefon());
			u.setTitle(user.getTitle());
			
			if (onlineUsers.contains(u))
				u.setOnline(true);
			else
				u.setOnline(false);

			copyGroups(u, user);
			copyAttributes(u, user);
			copyCountries(u, user);
			copyCalendars(u, user);
			
			users.add(u);
		}

		return users;
	}

	@Override
	public boolean update(UserBean user) throws IllegalArgumentException {

		User u = (User) DAOManager.getInstance().getUserDAO().findByID(user.getId());

		u.setName(user.getName());
		u.setFirstName(user.getFirstName());
		u.setLastName(user.getLastName());
		u.setActive(user.isActive());
		u.setEmail(user.getEmail());
		u.setJoinDate(user.getJoinDate());
		u.setLand(user.getLand());
		u.setName(user.getName());
		u.setNewRecord(false);
		u.setOrt(user.getOrt());
		u.setPassword(user.getPassword());
		u.setRanking(user.getRanking());
		u.setTelefon(user.getTelefon());
		u.setTitle(user.getTitle());

		return DAOManager.getInstance().getUserDAO().update(u);

	}

	@Override
	public void addAvatar(UserBean user, AvatarBean avatar) throws IllegalArgumentException {

	}

	@Override
	public void removeAvatar(UserBean user, AvatarBean avatar) throws IllegalArgumentException {

	}

	@Override
	public void addGroup(UsergroupBean group, UserBean user) throws IllegalArgumentException {

		if (group == null)
			throw new IllegalArgumentException("Group must not be null");
		if (user == null)
			throw new IllegalArgumentException("User must not be null");
		if (group.getId() == null)
			throw new IllegalArgumentException("Group id must not be null");
		if (user.getId() == 0)
			throw new IllegalArgumentException("User id must not be 0");

		DAOManager.getInstance().getUserDAO().addGroup(group.getId(), Long.valueOf(user.getId()));

	}
	
	@Override
	public void addCalendar(Long calendarId, Long userId) throws IllegalArgumentException {

		if (calendarId == null)
			throw new IllegalArgumentException("Calendar id must not be null");
		if (userId == 0)
			throw new IllegalArgumentException("User id must not be 0");

		DAOManager.getInstance().getUserDAO().addCalendar(calendarId, userId);

	}

	@Override
	public void removeGroup(UsergroupBean group, UserBean user) throws IllegalArgumentException {

		if (group == null)
			throw new IllegalArgumentException("Group must not be null");
		if (user == null)
			throw new IllegalArgumentException("User must not be null");
		if (group.getId() == null)
			throw new IllegalArgumentException("Group id must not be null");
		if (user.getId() == 0)
			throw new IllegalArgumentException("User id must not be 0");

		DAOManager.getInstance().getUserDAO().removeGroup(group.getId(), Long.valueOf(user.getId()));

	}

	@Override
	public void addAttribute(UserBean user, AttributeBean attribute) throws IllegalArgumentException {

		if (user == null)
			throw new IllegalArgumentException("User must not be null.");
		if (attribute == null)
			throw new IllegalArgumentException("Attribute must not be null.");

		User u = (User) DAOManager.getInstance().getUserDAO().findByID(user.getId());

		if (u == null)
			throw new IllegalArgumentException("User does not exist");

		Attribute a = new Attribute();

		a.setDataType(attribute.getDataType());
		a.setDescription(attribute.getDescription());
		a.setKey(attribute.getKey());
		a.setValue(attribute.getValue());

		if (u.getAttributes().contains(a))
			throw new IllegalArgumentException("user has already this attribute.");

		DAOManager.getInstance().getUserDAO().addAttribute(u.getId(), a);

	}

	@Override
	public void removeAttribute(UserBean user, AttributeBean attribute) throws IllegalArgumentException {

		if (user == null)
			throw new IllegalArgumentException("User must not be null.");
		if (attribute == null)
			throw new IllegalArgumentException("Attribute must not be null.");

		User u = (User) DAOManager.getInstance().getUserDAO().findByID(user.getId());

		if (u == null)
			throw new IllegalArgumentException("User does not exist");

		DAOManager.getInstance().getUserDAO().removeAttribute(u.getId(), attribute.getKey());

	}

	@Override
	public ArrayList<UserBean> getUsers(int limit, int start) {

		ArrayList<UserBean> onlineUsers = SessionUtil.getOnlineUsers();
		
		ArrayList<UserBean> users = new ArrayList<UserBean>();

		List<?> _users = DAOManager.getInstance().getBaseDAO().findAllItems(User.class, limit, start);

		for (Iterator<?> it = _users.iterator(); it.hasNext();) {

			User user = (User) it.next();

			UserBean u = new UserBean();

			u.setId(user.getId().intValue());
			u.setName(user.getName());
			u.setFirstName(user.getFirstName());
			u.setLastName(user.getLastName());
			u.setActive(user.getActive());
			u.setEmail(user.getEmail());
			u.setJoinDate(user.getJoinDate());
			u.setLand(user.getLand());
			u.setName(user.getName());
			u.setNewRecord(false);
			u.setOrt(user.getOrt());
			u.setPassword(user.getPassword());
			u.setRanking(user.getRanking());
			u.setTelefon(user.getTelefon());
			u.setTitle(user.getTitle());

			if (onlineUsers.contains(u))
				u.setOnline(true);
			else
				u.setOnline(false);

			copyGroups(u, user);
			copyAttributes(u, user);
			copyCountries(u, user);
			
			// We do not copy the calendars here, since we do not nee them inside any lists

			users.add(u);
		}

		return users;
	}

	public void addUsersToGroup(ArrayList<UserBean> users, UsergroupBean group, IProgressMonitor monitor) {

	}

	@Override
	public void addCountry(UserBean user, CountryBean country) throws IllegalArgumentException {

		if (country == null)
			throw new IllegalArgumentException("Country must not be null");
		if (user == null)
			throw new IllegalArgumentException("User must not be null");
		if (country.getId() == null)
			throw new IllegalArgumentException("Country id must not be null");
		if (user.getId() == 0)
			throw new IllegalArgumentException("User id must not be 0");

		DAOManager.getInstance().getUserDAO().addCountry(country.getId(), Long.valueOf(user.getId()));

	}

	@Override
	public void removeCountry(UserBean user, CountryBean country) throws IllegalArgumentException {

		if (country == null)
			throw new IllegalArgumentException("Country must not be null");
		if (user == null)
			throw new IllegalArgumentException("User must not be null");
		if (country.getId() == null)
			throw new IllegalArgumentException("Country id must not be null");
		if (user.getId() == 0)
			throw new IllegalArgumentException("User id must not be 0");

		DAOManager.getInstance().getUserDAO().removeCountry(country.getId(), Long.valueOf(user.getId()));

	}

	@Override
	public long getCount() {
		return DAOManager.getInstance().getBaseDAO().getCount(User.class);
	}

	/**
	 * Copies the calendars of the user from pojo to bean
	 * 
	 * @param u
	 * @param user
	 */
	private void copyCalendars(UserBean u, User user) {
		for (Calendar currentCal : user.getCalendars()) {
			
			CalendarBean cal = new CalendarBean();
			
			cal.setId(currentCal.getId());
			cal.setName(currentCal.getName());
			cal.setDescription(currentCal.getDescription());
			
			// Copy meetings
			for (Meeting mee : currentCal.getMeetings())  {
				
				MeetingBean m = new MeetingBean();
				
				m.setEnd(mee.getEnd());
				m.setStart(mee.getStart());
				m.setLongDescription(mee.getLongDescription());
				m.setShortDescription(mee.getShortDescription());
				
				cal.getMeetings().add(m);
				
			}
			// copy appointments
			for (Appointment app : currentCal.getAppointments()) {
				
				AppointmentBean a = new AppointmentBean();
				
				a.setEnd(app.getEnd());
				a.setId(app.getId());
				a.setLongDescription(app.getLongDescription());
				a.setShortDescription(app.getShortDescription());
				a.setStart(app.getStart());
				a.setColor(app.getColor());
				a.setCalendar(cal);
				
				cal.getAppointments().add(a);
			}
			
			u.getCalendars().add(cal);
		}
	}

	/**
	 * Copies the countries of the user from pojo to bean
	 * 
	 * @param u
	 * @param user
	 */
	private void copyCountries(UserBean u, User user) {
		for (Country currentCountry : user.getCountries()) {

			CountryBean cb = new CountryBean();

			cb.setId(currentCountry.getId());
			cb.setName(currentCountry.getName());
			cb.setCode(currentCountry.getCode());

			u.getCountries().add(cb);
		}
	}

	/**
	 * Copies the Attributes of the user from pojo to bean
	 * 
	 * @param u
	 * @param user
	 */
	private void copyAttributes(UserBean u, User user) {
		for (Attribute currentAttribute : user.getAttributes()) {

			AttributeBean ab = new AttributeBean();

			ab.setDataType(currentAttribute.getDataType());
			ab.setDescription(currentAttribute.getDescription());
			ab.setValue(currentAttribute.getValue());
			ab.setId(currentAttribute.getId());
			ab.setKey(currentAttribute.getKey());

			u.getAttributes().add(ab);

		}
	}

	/**
	 * Copies the usergroups of the user from pojo to bean
	 * 
	 * @param u
	 * @param user
	 */
	private void copyGroups(UserBean u, User user) {
		for (Usergroup currentGroup : user.getGroups()) {

			UsergroupBean ugb = new UsergroupBean();

			ugb.setId(currentGroup.getId());
			ugb.setDescription(currentGroup.getDescription());
			ugb.setName(currentGroup.getName());

			u.getGroups().add(ugb);

		}
	}
}
