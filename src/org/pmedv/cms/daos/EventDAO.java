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
import org.pmedv.pojos.calendar.DateBean;
import org.pmedv.pojos.calendar.Event;
import org.pmedv.pojos.calendar.Participant;

public class EventDAO extends AbstractDAO {

	@Override
	protected String getQueryAll() {
		return "from Event event order by event.id";
	}

	@Override
	protected String getQueryById() {
		return "from Event event where event.id = ?";
	}

	@Override
	protected String getQueryByName() {
		return "from Event event where event.title = ? order by event.title";
	}

	@Override
	protected String getQueryElementsByName() {
		return "from Event event where event.title like %?% order by event.title";
	}

	/**
	 * Adds an dateBean to an existing event
	 * 
	 * @param eventId the id of the calendar to add the dateBean to
	 * @param dateBean the dateBean to add
	 */
	public DateBean addDateBean(Long eventId, DateBean dateBean) {

		// session/transaction start
		
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		// get calendar by given id

		try {
			Event event = (Event) session.createQuery(
		    " from Event calendar where calendar.id = ?")
		    .setLong(0, eventId)
		    .uniqueResult();

			//add new dateBean
			
			event.getPossibleDates().add(dateBean);
		    
		    //update 
		
		    session.save(dateBean);
		    session.flush();
		    session.merge(dateBean);

			// session/transaction end
		    
		    tx.commit();
		    
		    return dateBean;
			
		}
		catch (RuntimeException h) {
			log.error("Could not add dateBean " + dateBean.getDate().toString() + " to event.");			
			tx.rollback();
			return null;
		}
	    
	}	
	
	/**
	 * Removes an dateBean from an event
	 * 
	 * @param eventId    the event id to remove the dateBean from
	 * @param dateBeanId the id of the dateBean to remove
	 */
	public void removeDateBean(Long eventId, Long dateBeanId) {

		// session/transaction start
			
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		try {
			
			Event event = (Event) session.createQuery(
		    " from Event calendar where calendar.id = ?")
		    .setLong(0, eventId)
		    .uniqueResult();

			//remove dateBean from event
			
		    Iterator<DateBean> it = event.getPossibleDates().iterator();
		    
		    while (it.hasNext()) {
		    	
		    	DateBean current = it.next();
		    	
		    	if (current.getId().equals(dateBeanId)) {
		    		it.remove();
		    	}
		    	
		    }
		    
		    session.update(event);  
		    
			// session/transaction end

		    session.flush();
		    tx.commit();
			
		}
		catch (RuntimeException h) {
			log.fatal("Could not remove dateBean from event with id "+eventId);
			tx.rollback();
		}
		
	}
	
	/**
	 * Adds an participant to an existing event
	 * 
	 * @param eventId the id of the calendar to add the participant to
	 * @param participant the participant to add
	 */
	public Participant addParticipant(Long eventId, Participant participant) {

		// session/transaction start
		
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		// get calendar by given id

		try {
			Event event = (Event) session.createQuery(
		    " from Event calendar where calendar.id = ?")
		    .setLong(0, eventId)
		    .uniqueResult();

			//add new participant
			
			event.getParticipants().add(participant);
		    
		    //update 
		
		    session.save(participant);
		    session.flush();
		    session.merge(participant);

			// session/transaction end
		    
		    tx.commit();
		    
		    return participant;
			
		}
		catch (RuntimeException h) {
			log.error("Could not add participant " + participant.getLastname() + " to event.");			
			tx.rollback();
			return null;
		}
	    
	}	
	
	/**
	 * Removes an participant from an event
	 * 
	 * @param eventId    the event id to remove the participant from
	 * @param participantId the id of the participant to remove
	 */
	public void removeParticipant(Long eventId, Long participantId) {

		// session/transaction start
			
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		try {
			
			Event event = (Event) session.createQuery(
		    " from Event calendar where calendar.id = ?")
		    .setLong(0, eventId)
		    .uniqueResult();

			//remove participant from event
			
		    Iterator<Participant> it = event.getParticipants().iterator();
		    
		    while (it.hasNext()) {
		    	
		    	Participant current = it.next();
		    	
		    	if (current.getId().equals(participantId)) {
		    		it.remove();
		    	}
		    	
		    }
		    
		    session.update(event);  
		    
			// session/transaction end

		    session.flush();
		    tx.commit();
			
		}
		catch (RuntimeException h) {
			log.fatal("Could not remove participant from event with id "+eventId);
			tx.rollback();
		}
		
	}	
	
}
