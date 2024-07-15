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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.pmedv.pojos.Usergroup;

/**
 * <p>
 * Basic abstract class for the hibernate persistence layer. 
 * This class provides basic access to a hibernate entity in pojo mode.
 * </p>
 * <p> 
 * The following three abstract methods have to be implemented:
 * </p>
 * <p>
 * <ul type="square">
 * <li><code>public abstract String getQueryAll()</code></li>
 * <li><code>public abstract String getQueryById()</code></li>
 * <li><code>public abstract String getQueryByName()</code></li>
 * </p>
 * <p>
 * These three methods have to return a query String in HQL (Hibernate query language)<br>
 * </p>
 * @author Matthias Pueski <br>
 * @version 5.8.2006<br>
 *  
 */
 
public abstract class AbstractDAO {

	protected abstract String getQueryAll();
	protected abstract String getQueryById ();	
	protected abstract String getQueryByName();
	protected abstract String getQueryElementsByName();	
	
	protected static final Log log = LogFactory.getLog(AbstractDAO.class);
	
    protected SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
 
    public SessionFactory getSessionFactory() {
    	return sessionFactory;
    }
    
	/**
	 * 
	 * @return all items from the peristant storage of a specific type
	 */
	
	public  List<?> findAllItems() {

		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		List<?> result = session.createQuery(getQueryAll()).list();
			
		try {
				
			if (result.size() >= 1) {
			    log.debug("Fetching all objects of type "+result.get(0).getClass());		
			}
			session.flush();
			tx.commit();
		}
		catch (RuntimeException r) {
			log.debug("Could not get any item.");
			tx.rollback();
		}
	
		return result;
	}	
	
	/**
	 * Finds all items of a specific class
	 * 
	 * @param item
	 * 
	 * @return A list with objects
	 */
	public List<?> findAllItems(Class<?> item) {

		String entityName = item.getSimpleName();
		
		log.debug("Loading all objects for Class "+entityName);
		
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		List<?> result = session.createQuery("from "+entityName).list();
			
		try {
				
			if (result.size() >= 1) {
			    log.debug("Fetching all objects of type "+result.get(0).getClass());		
			}
			session.flush();
			tx.commit();
		}
		catch (RuntimeException r) {
			log.debug("Could not get any item.");
			tx.rollback();
		}
	
		return result;
	}
	
	/**
	 * Determines how many items of a specific class exist in the database
	 * 
	 * @param item the item to look for
	 * 
	 * @return the number of the items
	 */
	public Integer getCount(Class<?> item) {
		
		String entityName = item.getSimpleName();
		
		log.debug("Loading all objects for Class "+entityName);
		
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		Integer result = (Integer)session.createQuery("select count(e) from "+entityName+" e").uniqueResult();

		try {
			session.flush();
			tx.commit();
		}
		catch (RuntimeException r) {
			log.debug("Could not get any item.");
			tx.rollback();
		}
	
		
		return result;
	}
	
	/**
	 * Finds all items and provides a cursor with a limit and a start
	 * 
	 * @param item  the class to look for
	 * @param limit the maximum number of results
	 * @param start the offset to start the query from
	 * 
	 * @return a list of desired items
	 */
	public List<?> findAllItems(Class<?> item,int limit, int start) {

		String entityName = item.getSimpleName();
		
		log.debug("Loading all objects for Class "+entityName);
		
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		Criteria c = session.createCriteria(item);
		c.setFirstResult(start);
		c.setMaxResults(limit);
		
		List<?> result = null;
			
		try {
			result = c.list();
				
			if (result.size() >= 1) {
			    log.debug("Fetching all objects of type "+result.get(0).getClass());		
			}
			session.flush();
			tx.commit();
		}
		catch (RuntimeException r) {
			r.printStackTrace();
			log.debug("Could not get any item.");
			tx.rollback();			
		}
	
		return result;
	}
	
	/**
	 * returns a generic list of objects from persistent storage
	 * 
	 * @param query       the query string in "hibernate query language"
	 * @param queryString the element name as string to search for
	 * @return list of desired objects
	 */
	
	public List<?> getGenericList(String query,String queryString) {

		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		List<?> result = session.createQuery(query).setString(0, queryString).list();
			
		try {
				
			if (result.size() >= 1) {				
			    log.debug("Fetching all objects of type "+result.get(0).getClass());		
			}
			session.flush();
			tx.commit();
		}
		catch (RuntimeException r) {
			log.debug("Could not get any item.");
			tx.rollback();
		}

	
		return result;
	}	
	
	/**
	 * @param query
	 * @return
	 */
	public Object getGenericElement(String query) {

		Session session = sessionFactory.getCurrentSession();				
		Transaction tx = session.beginTransaction();
		
		Object o = null;
		
		try {			
			o = session.createQuery(query).uniqueResult();
	
			session.flush();
			tx.commit();		
		}
		catch (RuntimeException r) {
			log.debug("Could not get any item for query <<"+query +">>.");
			tx.rollback();
		}

		return o;
		
	}
	
	
	/**
	 * loads a specific object from the persistant storage by primary key (id)
	 *  
	 * @param id the id of the object
	 * @return the item specified by the id
	 */
	
	public Object findByID(long id) {
		
		Session session = sessionFactory.getCurrentSession();		
		Transaction tx = session.beginTransaction();

		Object o = null;
		
		try {
			 o =  session.createQuery(getQueryById())
		    .setLong(0, id)
		    .uniqueResult();		
		
			session.flush();
			tx.commit();
					
		}
		catch (RuntimeException r) {
			log.debug("Could not get any item.");
			tx.rollback();
		}
		
		return o;
	}

	/**
	 * loads a specific object from the persistant storage by primary key (id)
	 * 
	 * @param id
	 * @return
	 */
	public Object findByID(int id) {
		
		Object o = null;

		Session session = sessionFactory.getCurrentSession();		
		Transaction tx = session.beginTransaction();
		
		try {
			o =  session.createQuery(getQueryById())
		    .setLong(0, id)
		    .uniqueResult();		

		    log.debug("Fetching object by id of type "+o.getClass());					
			
		    session.flush();
			tx.commit();			
		}
		catch (RuntimeException r) {
			log.debug("Could not get any item.");
			tx.rollback();
		}
		
		return o;
	}

	/**
	 * Fetches a specific object by name from the database
	 * 
	 * @param Name the name of the object inside the persistent storage
	 * @return the object specified by the name
	 */
	
	public Object findByName(String Name) {

		Session session = sessionFactory.getCurrentSession();				
		Transaction tx = session.beginTransaction();
		
		Object o = null;
		
		try {
			o = session.createQuery(getQueryByName()).setString(0,Name).uniqueResult();

			log.debug("Fetching object by name "+Name+" of type "+o.getClass());	
			
			session.flush();
			tx.commit();		

			return o;
		} 
		catch (RuntimeException r) {
			log.debug("Could not get any item.");
			tx.rollback();
		}
		
		return o;
		
	}
	
	/**
	 * Fetches a specific object by name from the database
	 * 
	 * @param Name the name of the object inside the persistent storage
	 * @return the object specified by the name
	 */
	
	public Object findByName(String Name, boolean lazy) {

		Session session = sessionFactory.getCurrentSession();				
		Transaction tx = session.beginTransaction();
		
		Object o = null;
		
		try {
			o = session.createQuery(getQueryByName()).setString(0,Name).uniqueResult();

			log.debug("Fetching object by name "+Name+" of type "+o.getClass());	
			
			if (lazy) {
				
				if (o instanceof Usergroup) {
				
					Usergroup ug = (Usergroup)o;
					Hibernate.initialize(ug.getUsers());
			
					session.flush();
					tx.commit();		

					return ug;
				}
				
			}
			else {
				session.flush();
				tx.commit();		
				return o;
			}
				
		} 
		catch (RuntimeException r) {
			log.debug("Could not get any item.");
			tx.rollback();
		}
		
		
		return o;
		
	}
	
	public List<?> findElementsByName(String Name) {

		Session session = sessionFactory.getCurrentSession();		
		Transaction tx = session.beginTransaction();
				
		List<?> result = null;
		
		try {
			
			result = session.createQuery(getQueryElementsByName()).setString(0,Name).list();
			
			session.flush();
			tx.commit();		
	
			return result;
		}
		catch (RuntimeException r) {
			log.debug("Could not get any item.");
			tx.rollback();
		}
		
		return result;

	}
	
	
	/**
	 * Creates a new object inside the persistent storage
	 * 
	 * @param o
	 */
	
	public Object createAndStore(Object o) {
		
		if (o != null) {

			Session session = sessionFactory.getCurrentSession();
			Transaction tx = session.beginTransaction();

			try {
				session.save(o);
				session.merge(o);
				tx.commit();
				
			    log.debug("Creating object of type "+o.getClass());		

			    return o;
			}
			catch (RuntimeException r) {
				
				r.printStackTrace();
				
				log.info("Could not save object "+o.getClass());
				tx.rollback();
				
				return null;
			}
			
		}
		
		return null;		

	}
	
	/**
	 * Creates a new object inside the persistent storage
	 * 
	 * @param o
	 */
	
	public void create(Object o) {
		
		if (o != null) {

			Session session = sessionFactory.getCurrentSession();
			Transaction tx = session.beginTransaction();

			try {
				session.save(o);				
				tx.commit();
				
			    log.debug("Creating object of type "+o.getClass());		
				
			}
			catch (RuntimeException r) {
				log.info("Could not save object "+o.getClass());
				tx.rollback();
			}

		}				

	}
	
	/**
	 * Deletes a specific object from the persistent storage
	 * 
	 * @param o the object to be deleted
	 */

	public boolean delete(Object o) {
		
		if (o != null) {

			Session session = sessionFactory.getCurrentSession();				
			Transaction tx = session.beginTransaction();
			
			try {
				session.setFlushMode(FlushMode.COMMIT);	
				session.delete( o );	
				session.flush();
				tx.commit();

			    log.debug("Deleting object of type "+o.getClass());		
				
			    return true;
			} 
			catch (RuntimeException r) {
				log.info("Could not delete object "+o.getClass());
				r.printStackTrace();
				tx.rollback();
				return false;
			}

		}
		
		return false;

	}

	/**
	 * Updates a specific object inside the persistent storage
	 * 
	 * @param o the object to be updated
	 */
	
	public boolean update(Object o) {
		
		if (o != null) {

			Session session = sessionFactory.getCurrentSession();		
			Transaction tx = session.beginTransaction();
			
			try {
				session.update( o );
				session.flush();
				tx.commit();

				log.debug("Updating object of type "+o.getClass());		
				return true;
			} 
			catch (RuntimeException r) {
				log.info("Could not update object "+o.getClass());
				tx.rollback();
				return false;
			}
			
		}			
		return false;

	}
	
	
}
