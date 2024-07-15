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

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.pmedv.pojos.Usergroup;

/**
 * This data access object is responsible for maintaining the processes of the system
 * 
 * @author Matthias Pueski
 *
 *
 */

public class ProcessDAO extends AbstractDAO {

	public ProcessDAO() {

	}
	
	@Override
	protected String getQueryAll() {		
		return "from Process process order by process.position";
	}

	@Override
	protected String getQueryById() {
		return "from Process process where process.id = ?";
	}

	@Override
	protected String getQueryByName() {
		return "from Process process where process.name = ?";
	}

	@Override
	protected String getQueryElementsByName() {
		return "from Process process where process.name like ? order by process.name";
	}
	
	/**
	 * Adds a usergroup to a process in order to allow the user to execute this process
	 * 
	 * @param groupId
	 * @param processId
	 */
	
	public void addGroup(long groupId, long processId) {

		// session/transaction start
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		try {
			// get Item be given id	
			org.pmedv.pojos.Process process = (org.pmedv.pojos.Process) session.createQuery(
				    " from Process process where process.id = ?")
				    .setLong(0, processId)
				    .uniqueResult();

			// add new group
		    
			Usergroup usergroup = (Usergroup) session.createQuery(
		    " from Usergroup usergroup where usergroup.id = ?")
		    .setLong(0, groupId)
		    .uniqueResult();
			
			usergroup.getProcesses().add(process);
			process.getGroups().add(usergroup);
				    
		    // update 
		    
		    session.persist(process);
		    session.persist(usergroup);
		    
			// session/transaction end
		    
		    tx.commit();
			
		}
		catch (RuntimeException r) {
			tx.rollback();
			log.info("Couldn't add group "+groupId+" to process "+processId);
		}
		
	    
	}

	/**
	 * Removes an existing group from a process 
	 * 
	 * @param MenuitemId
	 * @param contentId
	 */
	
	public void removeGroup( long groupId,long processId) {

		log.debug("Trying to remove process with id "+processId+" from group "+groupId+".");
				
		// session/transaction start
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		try {

			// get Item be given id	
			org.pmedv.pojos.Process process = (org.pmedv.pojos.Process) session.createQuery(
				    " from Process process where process.id = ?")
				    .setLong(0, processId)
				    .uniqueResult();

			// add new group
			
			if (process != null) {
				log.debug("Found process : "+process.getId()+" : "+process.getName());
			}
			else {
				log.debug("Could not fetch process with id "+processId+".");
				return;
			}
			
		    
			Usergroup usergroup = (Usergroup) session.createQuery(
		    " from Usergroup usergroup where usergroup.id = ?")
		    .setLong(0, groupId)
		    .uniqueResult();
			
			if (usergroup != null) {
				log.debug("Found usergroup : "+usergroup.getId()+" : "+usergroup.getName());
			}
			else {
				log.debug("Could not fetch usergroup with id "+groupId+".");
				return;
			}
			
			usergroup.getProcesses().remove(process);
			process.getGroups().remove(usergroup);
			
		    // update 
		    
		    session.persist(process);
		    session.persist(usergroup);
		    
			// session/transaction end
		    tx.commit();
			
		}
		catch (RuntimeException r) {
			tx.rollback();
			log.info("Couldn't remove group "+groupId+" from process "+processId);
		}

	}	

	/**
	 * Gets the maximum position of the process list
	 *
	 * @return the position
	 */
	
	public int getMaxPos() {

		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		Object o = null;

		try {

			o = session.createQuery("select max(process.position) from Process process") .uniqueResult();

			session.flush();
			tx.commit();

			log.debug("Maxpos : " + o);

		}
		catch (RuntimeException r) {
			log.error("Could not get maxpos.");
			log.error(r.getMessage());
			tx.rollback();
			return -1;
		}

		if ( o != null )
			return (Integer)o;
		else
			return 0;

	}

	/**
	 * gets the previous position relative from a position
	 * 
	 * @param position the position to locate the previous position from
	 * @return the position
	 */

	public int getPreviousPosition(int position) {
		
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		Object o = null;
		
		try {
			o = session.createQuery("select max(process.position) from Process process where process.position < ?") .setInteger(0, position).uniqueResult();
			session.flush();
			tx.commit();
		}
		catch (RuntimeException r) {
			log.error("Could not get previous pos.");
			log.error(r.getMessage());
			tx.rollback();
			return -1;
		}
			
		
		return (Integer)o;
	
	}
	
	/**
	 * gets the next position relative from a position
	 * 
	 * @param position the position to locate the next position from
	 * @return the position
	 */
	
	public int getNextPosition(int position) {
		
		Session session = sessionFactory.getCurrentSession();

		Transaction tx = session.beginTransaction();
		
		Object o = null;
		
		try {
			o = session.createQuery("select min(process.position) from Process process where process.position > ?") .setInteger(0, position).uniqueResult();
		}
		catch (RuntimeException r) {
			log.error("Could not get previous pos.");
			log.error(r.getMessage());
			tx.rollback();	
			return -1;
		}
		
		return (Integer)o;
	
	}

	/**
	 * Gets the previous process relative to a position
	 * 
	 * @param position the position to locate the previous process from
	 * 
	 * @return the according process
	 */

	public org.pmedv.pojos.Process getPreviousProcess(int position) {
		
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		Object o = null;
		
		try {
			int previousPosition = (Integer)session.createQuery("select max(process.position) from Process process where process.position < ?") .setInteger(0, position).uniqueResult();			
			o = session.createQuery("from Process process where process.position = ?").setInteger(0, previousPosition).uniqueResult();			
		}
		catch (RuntimeException r) {
			log.error("Could not get previous process.");
			log.error(r.getMessage());
			tx.rollback();	
			return null;
		}
		
		return (org.pmedv.pojos.Process)o;
		
	}
	
	
	/**
	 * Gets the next process relative to a position
	 * 
	 * @param position the position to locate the next process from
	 * 
	 * @return the according process
	 */
	
	public org.pmedv.pojos.Process getNextProcess(int position) {
		
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		Object o = null;
		
		try {
			int previousPosition = (Integer)session.createQuery("select min(process.position) from Process process where process.position > ?") .setInteger(0, position).uniqueResult();			
			o = session.createQuery("from Process process where process.position = ?").setInteger(0, previousPosition).uniqueResult();			
		}
		catch (RuntimeException r) {
			log.error("Could not get next process.");
			log.error(r.getMessage());
			tx.rollback();	
			return null;
		}
		
		return (org.pmedv.pojos.Process)o;
		
	}
	
}
