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

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.pmedv.pojos.Node;
import org.pmedv.pojos.Usergroup;

public class NodeDAO extends AbstractDAO {

	public NodeDAO() {
		
	}
	
	@Override
	protected String getQueryAll() {
		return "from Node node order by node.position";
	}

	@Override
	protected String getQueryById() {
		return "from Node node where node.id = ?";
	}

	@Override
	protected String getQueryByName() {
		return "from Node node where node.name = ?";
	}

	@Override
	protected String getQueryElementsByName() {
		return "from Node node where node.name like ?";
	}
	
	/**
	 * Fetches all root nodes of the the tree
	 * 
	 * @return a list of all root nodes sorted by position
	 */
	public List<?> findAllRootNodes() {
		
		Session session = sessionFactory.getCurrentSession();		
		Transaction tx = session.beginTransaction();
		
		List<?> result = null;
		
		try {
			result = session.createQuery("from Node node where node.parent is null order by node.position").list();
			log.debug("Anzahl Nodes: " + result.size());
			session.flush();
			tx.commit();					
		}
		catch (RuntimeException r) {
			log.error("RuntimeException occured during fetching nodes.");
			log.error(r.getMessage());
			tx.rollback();
		}
				
		return result;
		
	}
	
	/**
	 * Gets the maximum position of the tree
	 * 
	 * @return the position
	 */
	public int getMaxPos() {	
		
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		Object o = null;
		
		try {

			o = session.createQuery("select max(node.position) from Node node") .uniqueResult();
			
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
	 * Gets the previuos node relative to a position
	 * 
	 * @param position the position of the node
	 * @return the previous node
	 */
	
	
	public Node getPreviousNode(int position) {
		
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		Object o = null;
		
		try {
		
			int previousPosition = (Integer)session.createQuery("select max(node.position) from Node node where node.position < ?") .setInteger(0, position).uniqueResult();
			o = session.createQuery("from Node node where node.position = ?").setInteger(0, previousPosition).uniqueResult();
			
			session.flush();
			tx.commit();
		
		}
		catch (RuntimeException r) {
			log.error("Could not get previous node.");
			log.error(r.getMessage());
			tx.rollback();
		}		
		return (Node)o;
	}
	
	/**
	 * Gets the next node relative to a position
	 * 
	 * @param position the position of the node
	 * @return the previous node
	 */
	public Node getNextNode(int position) {
		
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		Object o = null;
		
		try {		
			int previousPosition = (Integer)session.createQuery("select min(node.position) from Node node where node.position > ?") .setInteger(0, position).uniqueResult();	
			o = session.createQuery("from Node node where node.position = ?").setInteger(0, previousPosition).uniqueResult();
			
			session.flush();
			tx.commit();
		}
		catch (RuntimeException r) {
			log.error("Could not get next node.");
			log.error(r.getMessage());
			tx.rollback();
		}
		
		return (Node)o;
	}
	
	/**
	 * Finds the previous root node relative to a position
	 * 
	 * @param position the position to search from
	 * @return the previous root node 
	 */
	public Node getPreviousRootNode(int position) {
		
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		Object o = null;
			
		try {
			int previousPosition = (Integer)session.createQuery("select max(node.position) from Node node where node.position < ? and node.parent is null") .setInteger(0, position).uniqueResult();			
			o = session.createQuery("from Node node where node.position = ?").setInteger(0, previousPosition).uniqueResult();
			
			session.flush();
			tx.commit();
			
			return (Node)o;
		}
		catch (RuntimeException r) {
			log.error("Could not get previous root node.");
			log.error(r.getMessage());
			tx.rollback();
			return null;
		}

	}
	
	/**
	 * Finds the next root node relative to a position
	 * 
	 * @param position the position to search from
	 * @return the next root node
	 */
	public Node getNextRootNode(int position) {
		
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		try {
			int nextPosition = (Integer)session.createQuery("select min(node.position) from Node node where node.position > ? and node.parent is null") .setInteger(0, position).uniqueResult();
			Object o = session.createQuery("from Node node where node.position = ?").setInteger(0, nextPosition).uniqueResult();
			
			session.flush();
			tx.commit();
			
			return (Node)o;
		}
		catch (RuntimeException r) {
			log.error("Could not get next root node.");
			log.error(r.getMessage());
			tx.rollback();
			return null;
		}
		
	}
	
	
	/**
	 * Finds the previous child node relative to a position on the same parent node
	 * 
	 * @param position the position to search from 
	 * @param parent_id the id of the parent node
	 * @return the previous child node
	 */
	public Node getPreviousChildNode(int position,long parent_id) {
		
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		try {
			int previousPosition = 
				(Integer)session.createQuery("select max(node.position) from Node node where node.position < ? and node.parent = ?") 
				.setInteger(0, position)
				.setLong(1, parent_id)
				.uniqueResult();			
					
			Object o = session.createQuery("from Node node where node.position = ?").setInteger(0, previousPosition).uniqueResult();
			
			session.flush();
			tx.commit();
			
			return (Node)o;
		}
		catch (RuntimeException r) {
			log.error("Could not get previous child node.");
			log.error(r.getMessage());
			tx.rollback();
			return null;
		}

	}
	
	/**
	 * Finds the next child node relative to a position on the same parent node
	 * 
	 * @param position the position to search from 
	 * @param parent_id the id of the parent node
	 * @return the next child node
	 */
	
	public Node getNextChildNode(int position,long parent_id) {
		
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		try {
			int nextPosition = (Integer)session.createQuery("select min(node.position) from Node node where node.position > ? and node.parent = ?")
			.setInteger(0, position)
			.setLong(1, parent_id)
			.uniqueResult();
			Object o = session.createQuery("from Node node where node.position = ?").setInteger(0, nextPosition).uniqueResult();
			
			session.flush();
			tx.commit();
			
			return (Node)o;
		}
		catch (RuntimeException r) {
			log.error("Could not get next child node.");
			log.error(r.getMessage());
			tx.rollback();
			return null;
		}
		
	}
	
	
	public void addGroup(long groupId, long nodeId) {

		// session/transaction start
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		try {
			
			// get Item be given id	
			Node node = (Node) session.createQuery(
				    " from Node node where node.id = ?")
				    .setLong(0, nodeId)
				    .uniqueResult();
	
			// add new group
		    
			Usergroup usergroup = (Usergroup) session.createQuery(
		    " from Usergroup usergroup where usergroup.id = ?")
		    .setLong(0, groupId)
		    .uniqueResult();
			
			usergroup.getNodes().add(node);
			node.getGroups().add(usergroup);
				    
		    // update 
		    
		    session.persist(node);
		    session.persist(usergroup);
		    
			// session/transaction end
		    
		    tx.commit();
		}
		catch (RuntimeException r) {
			log.error("Could not add group.");
			log.error(r.getMessage());
			tx.rollback();
		}
	    
	}

	/**
	 * Removes an existing node from a group. 
	 * The node and the group aren't deleted themselves
	 * 
	 * @param groupId
	 * @param nodeId
	 */

	public void removeGroup( long groupId,long nodeId) {

		log.debug("Trying to remove node with id "+nodeId+" from group "+groupId+".");
		
		// session/transaction start
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		try {
			
			// get Item be given id	
			Node node = (Node) session.createQuery(
				    " from Node node where node.id = ?")
				    .setLong(0, nodeId)
				    .uniqueResult();
	
			// add new group
			
			if (node != null) {
				log.debug("Found node : "+node.getId()+" : "+node.getName());
			}
			else {
				log.debug("Could not fetch node with id "+nodeId+".");
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
			
			usergroup.getNodes().remove(node);
			node.getGroups().remove(usergroup);
			
		    // update 
		    
		    session.persist(node);
		    session.persist(usergroup);
		    
			// session/transaction end
		    tx.commit();
		}
		catch (RuntimeException r) {
			log.error("Could not remove group.");
			log.error(r.getMessage());
			tx.rollback();
		}

		
	}

	/**
	 * Gets the id of the last node created
	 *
	 * @return the id
	 */


	public Long getLastCreatedId() {

		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		Long lastId = -1L;

		try {

			lastId = (Long)session.createQuery("select max(node.id) from Node node ") .uniqueResult();

			session.flush();
			tx.commit();

		}
		catch (RuntimeException r) {
			log.error("Could not get last created node.");
			log.error(r.getMessage());
			tx.rollback();
		}

		return lastId;
        
	}


}
