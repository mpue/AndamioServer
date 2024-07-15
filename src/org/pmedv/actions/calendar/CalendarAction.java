/**
 * WeberknechtCMS - Open Source Content Management Written and maintained by Matthias Pueski
 * 
 * Copyright (c) 2003-2011 Matthias Pueski
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
package org.pmedv.actions.calendar;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.pmedv.actions.ObjectListAction;
import org.pmedv.beans.objects.UserBean;
import org.pmedv.beans.objects.UsergroupBean;
import org.pmedv.beans.objects.calendar.AppointmentBean;
import org.pmedv.beans.objects.calendar.AppointmentBeanProxy;
import org.pmedv.beans.objects.calendar.CalendarBean;
import org.pmedv.cms.daos.CalendarDAO;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.cms.daos.UserDAO;
import org.pmedv.cms.daos.UsergroupDAO;
import org.pmedv.context.AppContext;
import org.pmedv.pojos.User;
import org.pmedv.pojos.Usergroup;
import org.pmedv.services.CalendarService;
import org.pmedv.services.UserService;
import org.pmedv.services.UsergroupServiceImpl;
import org.pmedv.util.CalendarUtils;
import org.pmedv.util.RequestUtil;
import org.springframework.context.ApplicationContext;

public class CalendarAction extends ObjectListAction {

	public CalendarAction() {
		super("Tarzan");
	}

	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

	public ActionForward getAppointments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		UserService userService = (UserService) AppContext.getApplicationContext().getBean("userService");
		String userName = request.getParameter("username");

		String appointmentList = null;

		if (userName != null) {

			UserBean user = userService.findByName(userName);

			ArrayList<AppointmentBeanProxy> appointments = new ArrayList<AppointmentBeanProxy>();

			Long currentCal = (Long) request.getSession().getAttribute("currentCal");
			if (currentCal != null)
				for (CalendarBean cal : user.getCalendars()) {
					if (currentCal.equals(cal.getId())) {
						for (AppointmentBean app : cal.getAppointments()) {
							appointments.add(new AppointmentBeanProxy(app));
						}
						break;
					}
				}

			Collections.sort(appointments);

			appointmentList = JSONSerializer.toJSON(appointments).toString();

		}

		if (appointmentList != null) {
			try {
				PrintWriter out = response.getWriter();
				response.setContentType("text/plain");
				out.print(appointmentList);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	public ActionForward getCalendars(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		UserService userService = (UserService) AppContext.getApplicationContext().getBean("userService");
		String userName = request.getParameter("username");

		ArrayList<CalendarBean> calendars = null;

		if (userName != null) {
			UserBean user = userService.findByName(userName);

			calendars = new ArrayList<CalendarBean>();

			for (CalendarBean cal : user.getCalendars()) {
				calendars.add(cal);
			}

			Collections.sort(calendars);

		}

		if (calendars != null) {
			writeJSONList(CalendarBean.class, calendars, false, "id", request, response);
		}

		return null;
	}

	public ActionForward isAdmin(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		UserService userService = (UserService) AppContext.getApplicationContext().getBean("userService");
		String userName = request.getParameter("username");

		if (userName != null) {
			UserBean user = userService.findByName(userName);
			
			boolean isAdmin = false;

			for (UsergroupBean group : user.getGroups()) {
				if (group.getName().toLowerCase().equals("admin")){
					isAdmin = true;
					break;
				}
			}
			try {
				PrintWriter out = response.getWriter();
				out.print(isAdmin);
				out.flush();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			
		}

		return null;
	}
	
	public ActionForward getGroups(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		UserService userService = (UserService) AppContext.getApplicationContext().getBean("userService");
		UsergroupServiceImpl userGroupService = (UsergroupServiceImpl) AppContext.getApplicationContext().getBean("usergroupService");
		String userName = request.getParameter("username");

		ArrayList<UsergroupBean> groups = null;

		if (userName != null) {
			UserBean user = userService.findByName(userName);

			groups = new ArrayList<UsergroupBean>();
			
			boolean isAdmin = false;

			for (UsergroupBean group : user.getGroups()) {
				groups.add(group);
				if (group.getName().toLowerCase().equals("admin")){
					isAdmin = true;
					break;
				}
			}
			
			if (isAdmin){
				groups.clear();
				for (UsergroupBean usergroup : userGroupService.findAll()) {
					groups.add(usergroup);
				}
			}
			Collections.sort(groups);
		}

		if (groups != null) {
			writeJSONList(UsergroupBean.class, groups, false, "id", request, response);
		}

		return null;
	}

	public ActionForward selectGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		UsergroupDAO usergroupDao = DAOManager.getInstance().getUsergroupDAO();
		UserDAO userDao = DAOManager.getInstance().getUserDAO();

		String userName = request.getParameter("username");
		Long groupId = Long.parseLong(request.getParameter("groupId"));

		ArrayList<User> users = null;

		if (userName != null && groupId != null) {
			Usergroup group = (Usergroup) usergroupDao.findByID(groupId);
			User currentUser = (User) userDao.findByName(userName);

			users = new ArrayList<User>();

			for (User user : group.getUsers()) {
				if (!user.getId().equals(currentUser.getId())) {
					user.setUserProfile(null);
					users.add(user);
				}
			}
			Collections.sort(users);
		}

		if (users != null) {
			writeJSONList(User.class, users, false, "id", request, response);
		}

		return null;
	}

	public ActionForward shareCalender(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		UserDAO userDao = DAOManager.getInstance().getUserDAO();
		CalendarDAO calendarDAO = DAOManager.getInstance().getCalendarDAO();

		String usersAsJason = request.getParameter("users");

		if (usersAsJason != null) {
			JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(usersAsJason);
			List<?> users = (List<?>) JSONSerializer.toJava(jsonArray);
			for (Object object : users) {
				org.pmedv.pojos.calendar.Calendar calendar = (org.pmedv.pojos.calendar.Calendar) calendarDAO
						.findByID((Long) request.getSession().getAttribute("currentCal"));
				userDao.addCalendar(calendar.getId(), Long.valueOf((String) object));
			}
		}

		return null;
	}

	public ActionForward selectCalendar(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		request.getSession().setAttribute("currentCal", Long.valueOf(request.getParameter("calendarId")));

		return null;
	}

	public ActionForward getCurrentCalendar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		CalendarDAO calendarDAO = DAOManager.getInstance().getCalendarDAO();

		if (request.getSession().getAttribute("currentCal") != null){
			org.pmedv.pojos.calendar.Calendar calendar = (org.pmedv.pojos.calendar.Calendar) calendarDAO
					.findByID((Long) request.getSession().getAttribute("currentCal"));
	
			if (calendar != null) {
				StringBuffer jsonString = new StringBuffer();
	
				response.setContentType("text/plain");
	
				log.debug("Found calendar with name : " + calendar.getName());
	
				jsonString.append("{\"success\":true,\"data\":{");
				jsonString.append("\"name\":\"" + calendar.getName() + "\",");
				jsonString.append("\"description\":\"" + calendar.getDescription() + "\",");
				jsonString.append("\"id\":\"" + calendar.getId() + "\"");
				jsonString.append("}}");
	
				try {
					PrintWriter out = response.getWriter();
					out.println(jsonString.toString());
					out.flush();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	public ActionForward createCalendar(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		String calendarName = request.getParameter("name");
		String calendarDescription = request.getParameter("description");
		String username = request.getParameter("username");

		ApplicationContext ctx = AppContext.getApplicationContext();
		UserService userService = (UserService) ctx.getBean("userService");
		UserBean user = userService.findByName(username);

		log.debug("Creating calendar " + calendarName + " for user " + username);

		CalendarDAO calendarDAO = DAOManager.getInstance().getCalendarDAO();

		org.pmedv.pojos.calendar.Calendar calendar = new org.pmedv.pojos.calendar.Calendar();
		calendar.setName(calendarName);
		calendar.setDescription(calendarDescription);
		// If calendar creation was successfull, assign calendar to user

		calendar = (org.pmedv.pojos.calendar.Calendar) calendarDAO.createAndStore(calendar);

		if (calendar != null) {
			userService.addCalendar(calendar.getId(), (Long) (long) user.getId());
			userService.update(user);
		}
		else {
			log.error("could not create calendar " + calendarName + " for user " + username);
		}

		return null;
	}

	public ActionForward createAppointment(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		RequestUtil.dumpParams(request);

		ApplicationContext ctx = AppContext.getApplicationContext();
		String color = request.getParameter("color");
		String shortDescription = request.getParameter("shortDescription");
		String longDescription = request.getParameter("longDescription");
		String date = request.getParameter("date");

		String username = request.getParameter("username");

		UserService userService = (UserService) ctx.getBean("userService");
		UserBean user = userService.findByName(username);

		if (user == null) {
			log.error("could not find user " + username);
			return null;
		}

		CalendarBean currentCalendar = null;

		for (CalendarBean cal : user.getCalendars()) {
			if (cal.getId().equals((Long) request.getSession().getAttribute("currentCal"))) {
				currentCalendar = cal;
				break;
			}
		}

		if (currentCalendar == null) {
			log.error("could not find calendar for user " + username);
			return null;
		}

		try {
			Date startDate = sdf.parse(date);

			Calendar cal = CalendarUtils.getCal();
			cal.setTime(startDate);

			String[] startTime = request.getParameter("startTime").split(":");
			String[] endTime = request.getParameter("endTime").split(":");

			cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(startTime[0]));
			cal.set(Calendar.MINUTE, Integer.valueOf(startTime[1]));

			AppointmentBean appointment = new AppointmentBean();
			appointment.setStart(cal.getTime());

			cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(endTime[0]));
			cal.set(Calendar.MINUTE, Integer.valueOf(endTime[1]));

			appointment.setEnd(cal.getTime());
			appointment.setShortDescription(shortDescription);
			appointment.setLongDescription(longDescription);
			appointment.setColor(color);

			CalendarService service = (CalendarService) ctx.getBean("calendarService");
			service.addAppointment(currentCalendar, appointment);

		}
		catch (ParseException e) {
			log.error("could not parse date : " + date);
			return null;
		}

		return null;
	}

	public ActionForward deleteAppointment(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		ApplicationContext ctx = AppContext.getApplicationContext();
		Long appointmentId = Long.valueOf(request.getParameter("appointment_id"));

		if (appointmentId != null) {

			CalendarService service = (CalendarService) ctx.getBean("calendarService");
			AppointmentBean app = service.getAppointment(appointmentId);
			service.deleteAppointment(app);

		}

		return null;
	}

	public ActionForward getMeetings(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		try {
			PrintWriter out = response.getWriter();
			response.setContentType("text/plain");
			out.print("<h1>Meetings</h1>");
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

	public ActionForward getAppointment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		ApplicationContext ctx = AppContext.getApplicationContext();
		Long appointmentId = Long.valueOf(request.getParameter("appointment_id"));

		if (appointmentId != null) {

			CalendarService service = (CalendarService) ctx.getBean("calendarService");
			AppointmentBean app = service.getAppointment(appointmentId);
			response.setContentType("text/plain");
			try {
				PrintWriter out = response.getWriter();
				out.print(JSONSerializer.toJSON(app).toString());
				out.flush();
			}
			catch (IOException e) {
				log.error("Could not write appointment with id " + appointmentId + " to output stream.");
			}
		}

		return null;
	}

	public ActionForward saveAppointment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		RequestUtil.dumpParams(request);

		ApplicationContext ctx = AppContext.getApplicationContext();
		Long appointmentId = Long.valueOf(request.getParameter("appointment_id"));

		if (appointmentId != null) {

			CalendarService service = (CalendarService) ctx.getBean("calendarService");
			AppointmentBean app = service.getAppointment(appointmentId);

			String shortDescription = request.getParameter("shortDescription");
			String longDescription = request.getParameter("longDescription");
			String color = request.getParameter("color");
			String date = request.getParameter("date");
			
			Date startDate = sdf.parse(date);
			Calendar cal = CalendarUtils.getCal();
			
			cal.setTime(startDate);

			String[] startTime = request.getParameter("startTime").split(":");
			String[] endTime = request.getParameter("endTime").split(":");

			cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(startTime[0]));
			cal.set(Calendar.MINUTE, Integer.valueOf(startTime[1]));

			app.setStart(cal.getTime());

			cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(endTime[0]));
			cal.set(Calendar.MINUTE, Integer.valueOf(endTime[1]));

			app.setEnd(cal.getTime());

			app.setShortDescription(shortDescription);
			app.setLongDescription(longDescription);
			app.setColor(color);

			service.updateAppointment(app);

		}

		return null;

	}

	public ActionForward moveAppointment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		RequestUtil.dumpParams(request);

		ApplicationContext ctx = AppContext.getApplicationContext();
		Long appointmentId = Long.valueOf(request.getParameter("appointment_id"));

		if (appointmentId != null) {

			CalendarService service = (CalendarService) ctx.getBean("calendarService");
			AppointmentBean app = service.getAppointment(appointmentId);

			String[] startTime = null;
			String[] endTime = null;

			if (request.getParameter("startTime") != null && !request.getParameter("startTime").equals(""))
				startTime = request.getParameter("startTime").split(":");
			if (request.getParameter("endTime") != null && !request.getParameter("endTime").equals(""))
				endTime = request.getParameter("endTime").split(":");

			String dayString = request.getParameter("day").trim();
			String monthString = request.getParameter("month").trim();
			String yearString = request.getParameter("year").trim();

			int day = Integer.valueOf(dayString);
			int month = Integer.valueOf(monthString);
			int year = Integer.valueOf(yearString);

			Calendar cal = Calendar.getInstance();

			cal.setTime(app.getEnd());
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, month - 1);
			cal.set(Calendar.DAY_OF_MONTH, Math.abs(day));

			if (endTime != null) {
				cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(endTime[0]));
				cal.set(Calendar.MINUTE, Integer.valueOf(endTime[1]));
			}else if (startTime != null) {
				Calendar tempCal = Calendar.getInstance();
				tempCal.setTime(app.getStart());
				
				cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + (Integer.valueOf(startTime[0]) - tempCal.get(Calendar.HOUR_OF_DAY)));
				
				cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + (Integer.valueOf(startTime[1]) - tempCal.get(Calendar.MINUTE)));
			}

			app.setEnd(cal.getTime());
			
			cal.setTime(app.getStart());
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, month - 1);
			cal.set(Calendar.DAY_OF_MONTH, Math.abs(day));
			if (startTime != null) {
				cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(startTime[0]));
				cal.set(Calendar.MINUTE, Integer.valueOf(startTime[1]));
			}

			app.setStart(cal.getTime());



			service.updateAppointment(app);

		}

		return null;

	}
}
