package org.pmedv.cms.daos;

import java.util.Iterator;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.pmedv.pojos.forums.Category;
import org.pmedv.pojos.forums.Thread;

/**
 * <p>
 * Category data access object
 * </p>
 * 
 * This object allows basic access to a category as well as adding and
 * removing threads.  
 * 
 * @author Matthias Pueski
 *
 */
public class CategoryDAO extends AbstractDAO {

	@Override
	protected String getQueryAll() {
		return "from Category";
	}

	@Override
	protected String getQueryById() {
		return "from Category category where category.id = ?";
	}

	@Override
	protected String getQueryByName() {
		return "from Category category where category.name like ? order by category.name";
	}

	@Override
	protected String getQueryElementsByName() {
		return null;
	}
	
	public int getMaxPos() {	
		
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		Object o = null;
		
		try {

			o = session.createQuery("select max(category.position) from Category category") .uniqueResult();
			
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
	 * Adds a thread to a specific category
	 * 
	 * @param id the id of the category
	 * @param thread the thread to be added
	 */

	@SuppressWarnings("unchecked")
	public void addThread(Long id, org.pmedv.pojos.forums.Thread thread) {

		// session/transaction start
		
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		// get Category by given id

		try {
			Category category = (Category) session.createQuery(
		    " from category category where category.id = ?")
		    .setLong(0, id)
		    .uniqueResult();

			//add new rating
			
			thread.setCategory(category);
			category.getThreads().add(thread);
		    
		    //update course
		
		    session.save(thread);
		    session.flush();
		    
			// session/transaction end
		    
		    tx.commit();
			
		}
		catch (RuntimeException r) {
			log.error("Could not add thread.");
			log.error(r.getMessage());
			tx.rollback();
		}
	}	
	
	/**
	 * Removes specific thread from category 
	 * 
	 * @param id the id of the category
	 * @param thread_id the id of the thread
	 */
	
	public void removeThread(Long id, Long thread_id) {
		
		// session/transaction start
		
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		try {
			
			Category c = (Category) session.load(Category.class, id);

			//remove thread from category
		
		    for (Iterator<Thread> myThreadIterator = c.getThreads().iterator();myThreadIterator.hasNext();) {
		    	org.pmedv.pojos.forums.Thread myCurrentThread = (org.pmedv.pojos.forums.Thread)myThreadIterator.next();
		    	
		    	if (myCurrentThread.getId().equals(thread_id)) {
		    		myThreadIterator.remove();
		    	}
		    	
		    }
		    
		    session.update(c);  
		    
			// session/transaction end
		    
		    session.flush();
		    tx.commit();
			
		}
		catch (RuntimeException r) {
			log.error("Could not remove thread.");
			log.error(r.getMessage());
			tx.rollback();
		}

	    
	}


}

