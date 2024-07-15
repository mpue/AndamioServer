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
package org.pmedv.cms.daos;

import java.util.Iterator;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.pmedv.pojos.calendar.Appointment;
import org.pmedv.pojos.calendar.Calendar;
import org.pmedv.pojos.calendar.Meeting;

/**
 * The <code>CalendarDAO</code> provides access to the users
 * calendars. Each user can have multiple calendars assigned,
 * but has one default calendar. Thus each user can share 
 * each of his calendars with all other users.
 * 
 * @author Matthias Pueski
 *
 */
public class CalendarDAO extends AbstractDAO {

	public CalendarDAO() {
	}
	
	@Override
	protected String getQueryAll() {
		return "from Calendar calendar order by calendar.name";
	}

	@Override
	protected String getQueryById() {
		return "from Calendar calendar where calendar.id = ?";
	}

	@Override
	protected String getQueryByName() {
		return "from Calendar calendar where calendar.name like ? order by calendar.name";
	}

	@Override
	protected String getQueryElementsByName() {
		return null;
	}
	
	/**
	 * Adds an appointment to an existing calendar
	 * 
	 * @param calendarId the id of the calendar to add the appointment to
	 * @param appointment the appointment to add
	 */
	public Appointment addAppointment(Long calendarId, Appointment appointment) {

		// session/transaction start
		
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		// get calendar by given id

		try {
			Calendar cal = (Calendar) session.createQuery(
		    " from Calendar calendar where calendar.id = ?")
		    .setLong(0, calendarId)
		    .uniqueResult();

			//add new appointment
			
			appointment.setCalendar(cal);
			cal.getAppointments().add(appointment);
		    
		    //update 
		
		    session.save(appointment);
		    session.flush();
		    session.merge(appointment);

			// session/transaction end
		    
		    tx.commit();
		    
		    return appointment;
			
		}
		catch (RuntimeException h) {
			log.error("Could not add appointment "+appointment.getShortDescription()+" to calendar.");			
			tx.rollback();
			return null;
		}
	    
	}	
	
	/**
	 * Removes an appointment from a calendar
	 * 
	 * @param calendarId    the calendar id to remove the appointment from
	 * @param appointmentId the id of the appointment to remove
	 */
	public void removeAppointment(Long calendarId, Long appointmentId) {

		// session/transaction start
			
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		try {
			
			Calendar cal = (Calendar) session.createQuery(
		    " from Calendar calendar where calendar.id = ?")
		    .setLong(0, calendarId)
		    .uniqueResult();

			//remove appointment from calendar
			
		    Iterator<Appointment> it = cal.getAppointments().iterator();
		    
		    while (it.hasNext()) {
		    	
		    	Appointment current = it.next();
		    	
		    	if (current.getId().equals(appointmentId)) {
		    		it.remove();
		    	}
		    	
		    }
		    
		    session.update(cal);  
		    
			// session/transaction end

		    session.flush();
		    tx.commit();
			
		}
		catch (RuntimeException h) {
			log.fatal("Could not remove appointment from calendar with id "+calendarId);
			tx.rollback();
		}
		
	}

	/**
	 * Adds an existing meeting to an existing calendar
	 * 
	 * @param meetingId
	 * @param calendarId
	 */
	public void addMeeting(Long meetingId, Long calendarId) {

		// session/transaction start

		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		try {

			// get meeting be given id

			Meeting meeting = (Meeting) session.createQuery(" from Meeting meeting where meeting.id = ?").setLong(0, meetingId)
					.uniqueResult();

			// get calendar by id

			Calendar cal = (Calendar) session.createQuery(" from Calendar calendar where calendar.id = ?").setLong(0, calendarId)
					.uniqueResult();

			meeting.getCalendars().add(cal);
			cal.getMeetings().add(meeting);

			// update

			session.persist(meeting);
			session.persist(cal);

			// session/transaction end

			tx.commit();

		}
		catch (RuntimeException r) {
			tx.rollback();
			log.info("Could not add meeting " + meetingId + " to calendar " + calendarId);
		}

	}

	/**
	 * Removes a meeting from a calendar
	 * 
	 * @param meetingId  the id of the meeting being removed
	 * @param calendarId the id of the calendar to remove the meeting from
	 */
	public void removeMeeting(Long meetingId, Long calendarId) {

		log.debug("Trying to remove meeting with id " + meetingId + " from calendar " + calendarId + ".");

		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		try {

			Meeting meeting = (Meeting) session.createQuery(" from Meeting meeting where meeting.id = ?")
				.setLong(0, meetingId).uniqueResult();

			if (meeting != null) {
				log.debug("Found meeting : " + meeting.getId());
			}
			else {
				log.debug("Could not fetch meeting with id " + meetingId + ".");
				tx.rollback();
				return;
			}

			Calendar cal = (Calendar) session.createQuery(" from Calendar calendar where calendar.id = ?")
				.setLong(0, calendarId).uniqueResult();

			if (cal != null) {
				log.debug("Found calendar : " + cal.getId());
			}
			else {
				log.debug("Could not fetch calendar with id " + calendarId + ".");
				tx.rollback();
				return;
			}

			cal.getMeetings().remove(meeting);
			meeting.getCalendars().remove(cal);
			
			session.persist(cal);
			session.persist(meeting);

			tx.commit();

		}
		catch (RuntimeException r) {
			tx.rollback();
			log.info("Could not remove meeting " + meetingId + " from calendar " + calendarId);
		}

	}

}
