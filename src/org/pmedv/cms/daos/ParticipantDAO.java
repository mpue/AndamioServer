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
import org.pmedv.pojos.calendar.Participant;

public class ParticipantDAO extends AbstractDAO {

	@Override
	protected String getQueryAll() {
		return "from Participant participant order by participant.id";
	}

	@Override
	protected String getQueryById() {
		return "from Participant participant where participant.id = ?";
	}

	@Override
	protected String getQueryByName() {
		return "from Participant participant where participant.lastname = ? order by participant.lastname";
	}

	@Override
	protected String getQueryElementsByName() {
		return "from Participant participant where participant.lastname like %?% order by participant.lastname";
	}

	/**
	 * Adds an dateBean to an existing participant
	 * 
	 * @param participantId the id of the calendar to add the dateBean to
	 * @param dateBean the dateBean to add
	 */
	public DateBean addDateBean(Long participantId, DateBean dateBean) {

		// session/transaction start
		
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		// get calendar by given id

		try {
			Participant participant = (Participant) session.createQuery(
		    " from Participant calendar where calendar.id = ?")
		    .setLong(0, participantId)
		    .uniqueResult();

			//add new dateBean
			
			participant.getPossibleDates().add(dateBean);
		    
		    //update 
		
		    session.save(dateBean);
		    session.flush();
		    session.merge(dateBean);

			// session/transaction end
		    
		    tx.commit();
		    
		    return dateBean;
			
		}
		catch (RuntimeException h) {
			log.error("Could not add dateBean " + dateBean.getDate().toString() + " to participant.");			
			tx.rollback();
			return null;
		}
	    
	}	
	
	/**
	 * Removes an dateBean from an participant
	 * 
	 * @param participantId    the participant id to remove the dateBean from
	 * @param dateBeanId the id of the dateBean to remove
	 */
	public void removeDateBean(Long participantId, Long dateBeanId) {

		// session/transaction start
			
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		try {
			
			Participant participant = (Participant) session.createQuery(
		    " from Participant calendar where calendar.id = ?")
		    .setLong(0, participantId)
		    .uniqueResult();

			//remove dateBean from participant
			
		    Iterator<DateBean> it = participant.getPossibleDates().iterator();
		    
		    while (it.hasNext()) {
		    	
		    	DateBean current = it.next();
		    	
		    	if (current.getId().equals(dateBeanId)) {
		    		it.remove();
		    	}
		    	
		    }
		    
		    session.update(participant);  
		    
			// session/transaction end

		    session.flush();
		    tx.commit();
			
		}
		catch (RuntimeException h) {
			log.fatal("Could not remove dateBean from participant with id "+participantId);
			tx.rollback();
		}
		
	}
	

}
