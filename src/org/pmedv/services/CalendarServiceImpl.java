package org.pmedv.services;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.beans.objects.UserBean;
import org.pmedv.beans.objects.calendar.AppointmentBean;
import org.pmedv.beans.objects.calendar.CalendarBean;
import org.pmedv.beans.objects.calendar.MeetingBean;
import org.pmedv.cms.daos.AppointmentDAO;
import org.pmedv.cms.daos.CalendarDAO;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.cms.daos.MeetingDAO;
import org.pmedv.cms.daos.UserDAO;
import org.pmedv.context.AppContext;
import org.pmedv.pojos.User;
import org.pmedv.pojos.calendar.Appointment;
import org.pmedv.pojos.calendar.Calendar;
import org.pmedv.pojos.calendar.Meeting;

/**
 * This is the default implementation for the {@link CalendarService}
 * 
 * @author Matthias Pueski
 * 
 */
public class CalendarServiceImpl implements CalendarService {

	private static final CalendarDAO calendarDAO = DAOManager.getInstance().getCalendarDAO();
	private static final UserDAO userDAO = DAOManager.getInstance().getUserDAO();
	private static final AppointmentDAO appointmentDAO = DAOManager.getInstance().getAppointmentDAO();
	private static final MeetingDAO meetingDAO = DAOManager.getInstance().getMeetingDAO();

	private static final Log log = LogFactory.getLog(CalendarServiceImpl.class);

	@Override
	public CalendarBean createCalendar(CalendarBean calendar) throws IllegalArgumentException {

		if (calendar.getName() == null || calendar.getName().length() < 3) {
			throw new IllegalArgumentException("A calendar must have a valid name.");
		}

		// Check if there's already a calendar with this name

		CalendarDAO dao = (CalendarDAO) AppContext.getApplicationContext().getBean("calendarDAO");
		Calendar c = (Calendar) dao.findByName(calendar.getName());

		if (c != null)
			throw new IllegalArgumentException("A calendar with this name already exists.");

		// Create new pojo and persist

		c = new Calendar();
		c.setDescription(calendar.getDescription());
		c.setName(calendar.getName());
		c = (Calendar) dao.createAndStore(c);

		if (c == null) {
			throw new IllegalArgumentException("Could not create calendar with name " + calendar.getName());
		}

		// set id from persisted object
		calendar.setId(c.getId());

		return calendar;
	}

	@Override
	public void deleteCalendar(CalendarBean calendar) throws IllegalArgumentException {

		Calendar cal = (Calendar) calendarDAO.findByID(calendar.getId());

		// remove all users from this calendar
		for (User u : cal.getUsers()) {
			userDAO.removeCalendar(cal.getId(), u.getId());
		}
		// remove all meetings too
		for (Meeting m : cal.getMeetings()) {
			calendarDAO.removeMeeting(m.getId(), cal.getId());
		}
		// delete the calendar, throw an IllegalArgumentException if this doesn't work out. Not very
		// beautiful,
		// but at least we get the message if something goes wrong
		if (!calendarDAO.delete(cal)) {
			log.error("Could not delete calendar with id " + cal.getId());
			throw new IllegalArgumentException("Could not delete calendar with id " + cal.getId());
		}

	}

	@Override
	public void addCalendar(UserBean user, CalendarBean calendar) throws IllegalArgumentException {
		UserDAO dao = DAOManager.getInstance().getUserDAO();
		dao.addCalendar(calendar.getId(), Long.valueOf(user.getId()));
	}

	@Override
	public void removeCalendar(UserBean user, CalendarBean calendar) throws IllegalArgumentException {
		UserDAO dao = DAOManager.getInstance().getUserDAO();
		dao.removeCalendar(calendar.getId(), Long.valueOf(user.getId()));
	}

	@Override
	public void updateCalendar(CalendarBean calendar) throws IllegalArgumentException {

		Calendar c = (Calendar) calendarDAO.findByName(calendar.getName());

		// Assure that there's no calendar with a different id and the same name just in case we
		// change the name
		if (c != null && !c.getId().equals(calendar.getId()))
			throw new IllegalArgumentException("A calendar with this name already exists.");

		// Pick the existing calendar and update name and description, leave all other values as
		// they are
		Calendar cal = (Calendar) calendarDAO.findByID(calendar.getId());
		cal.setDescription(calendar.getDescription());
		cal.setName(calendar.getName());
		calendarDAO.update(cal);
	}

	@Override
	public AppointmentBean addAppointment(CalendarBean calendar, AppointmentBean appointment)
			throws IllegalArgumentException {

		if (calendar == null)
			throw new IllegalArgumentException("calendar must not be null");
		if (appointment == null)
			throw new IllegalArgumentException("appointment must not be null");

		// Check if the calendar really exists
		Calendar cal = (Calendar) calendarDAO.findByID(calendar.getId());

		if (cal == null) {
			throw new IllegalArgumentException("A calendar with the id " + calendar.getId()
					+ " does not exist.");
		}

		// Create a new Appointment
		Appointment a = new Appointment();
		a.setStart(appointment.getStart());
		a.setEnd(appointment.getEnd());
		a.setLongDescription(appointment.getLongDescription());
		a.setShortDescription(appointment.getShortDescription());
		a.setColor(appointment.getColor());

		// persist and update id
		a = calendarDAO.addAppointment(cal.getId(), a);

		if (a == null)
			throw new IllegalArgumentException(
					"Could not retrieve id from database, something went VERY wrong,");

		appointment.setId(a.getId());

		return appointment;

	}

	@Override
	public void deleteAppointment(AppointmentBean appointment) throws IllegalArgumentException {
		calendarDAO.removeAppointment(appointment.getCalendar().getId(), appointment.getId());
	}

	@Override
	public void updateAppointment(AppointmentBean appointment) throws IllegalArgumentException {

		Appointment a = (Appointment) appointmentDAO.findByID(appointment.getId());

		a.setStart(appointment.getStart());
		a.setEnd(appointment.getEnd());
		a.setShortDescription(appointment.getShortDescription());
		a.setLongDescription(appointment.getLongDescription());
		a.setColor(appointment.getColor());
		
		appointmentDAO.update(a);
	}

	@Override
	public MeetingBean addMeeting(MeetingBean meeting, CalendarBean cal) throws IllegalArgumentException {

		Meeting m = new Meeting();
		m.setStart(meeting.getEnd());
		m.setEnd(meeting.getEnd());
		m.setShortDescription(meeting.getShortDescription());
		m.setLongDescription(meeting.getLongDescription());

		m = (Meeting) meetingDAO.createAndStore(m);
		calendarDAO.addMeeting(m.getId(), cal.getId());

		meeting.setId(m.getId());

		return meeting;

	}

	@Override
	public void updateMeeting(MeetingBean meeting) throws IllegalArgumentException {

		Meeting m = (Meeting) meetingDAO.findByID(meeting.getId());

		m.setStart(meeting.getEnd());
		m.setEnd(meeting.getEnd());
		m.setShortDescription(meeting.getShortDescription());
		m.setLongDescription(meeting.getLongDescription());

		meetingDAO.update(m);

	}

	@Override
	public void deleteMeeting(MeetingBean meeting) throws IllegalArgumentException {

		Meeting m = (Meeting) meetingDAO.findByID(meeting.getId());

		for (Calendar c : m.getCalendars()) {
			calendarDAO.removeMeeting(m.getId(), c.getId());
		}

		if (!meetingDAO.delete(m)) {
			log.error("Could not delete meeting with id " + m.getId());
			throw new IllegalArgumentException("Could not delete meeting with id " + m.getId());
		}

	}

	@Override
	public void inviteUsers(MeetingBean meeting, List<CalendarBean> calendars)
			throws IllegalArgumentException {

		Meeting m = (Meeting) meetingDAO.findByID(meeting.getId());

		for (CalendarBean c : calendars) {
			calendarDAO.addMeeting(m.getId(), c.getId());
		}

	}

	@Override
	public void removeFromMeeting(MeetingBean meeting, List<CalendarBean> calendars)
			throws IllegalArgumentException {

		Meeting m = (Meeting) meetingDAO.findByID(meeting.getId());

		for (CalendarBean c : calendars) {
			calendarDAO.removeMeeting(m.getId(), c.getId());
		}

	}

	@Override
	public AppointmentBean getAppointment(Long id) throws IllegalArgumentException {
		
		Appointment a = (Appointment) appointmentDAO.findByID(id);
				
		if (a != null) {
			AppointmentBean appointment = new AppointmentBean();
			appointment.setStart(a.getStart());
			appointment.setEnd(a.getEnd());
			appointment.setShortDescription(a.getShortDescription());
			appointment.setLongDescription(a.getLongDescription());
			appointment.setColor(a.getColor());
			appointment.setId(a.getId());
			
		    Calendar calendar = (Calendar)calendarDAO.findByID(a.getCalendar().getId());
		    
			CalendarBean cal = new CalendarBean();
			
			cal.setId(calendar.getId());
			cal.setName(calendar.getName());
			cal.setDescription(calendar.getDescription());
			
			appointment.setCalendar(cal);
			
			return appointment;
		}
		
		return null;
	}

}
