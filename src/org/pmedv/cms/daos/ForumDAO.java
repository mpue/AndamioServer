package org.pmedv.cms.daos;

import java.util.Iterator;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.pmedv.pojos.Node;
import org.pmedv.pojos.Usergroup;
import org.pmedv.pojos.forums.Category;
import org.pmedv.pojos.forums.Forum;
import org.pmedv.pojos.forums.Posting;
import org.pmedv.pojos.forums.Thread;

/**
 * <p>
 * Forum data access object
 * </p>
 * 
 * This object allows basic access to a forum as well as adding and removing categories.
 * 
 * @author mpue
 * 
 */
public class ForumDAO extends AbstractDAO {

	@Override
	protected String getQueryAll() {
		return "from Forum";
	}

	@Override
	protected String getQueryById() {
		return "from Forum forum where forum.id = ?";
	}

	@Override
	protected String getQueryByName() {
		return "from Forum forum where forum.name like ? order by forum.name";
	}

	@Override
	protected String getQueryElementsByName() {
		return null;
	}

	/**
	 * @return the maximum position of the forums
	 */
	public int getMaxPos() {

		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		Object o = null;

		try {

			o = session.createQuery("select max(forum.position) from Forum forum").uniqueResult();

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

		if (o != null)
			return (Integer) o;
		else
			return 0;

	}

	/**
	 * Adds a category to a specific forum
	 * 
	 * @param forum_id
	 *            the id of the forum
	 * @param category
	 *            the category to be added
	 */

	@SuppressWarnings("unchecked")
	public boolean addCategory(Long forum_id, Category category) {

		// session/transaction start

		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		// get Forum by given id

		try {
			Forum forum = (Forum) session.createQuery(" from Forum forum where forum.id = ?").setLong(0,
					forum_id).uniqueResult();

			// add new category

			category.setForum(forum);
			forum.getCategories().add(category);

			// update category

			session.save(category);
			session.flush();

			// session/transaction end

			tx.commit();

		}
		catch (RuntimeException r) {
			log.error("Could not add category.");
			log.error(r.getMessage());
			tx.rollback();
			return false;
		}
		
		return true;

	}

	/**
	 * Removes specific category from forum
	 * 
	 * @param id
	 *            the id of the forum
	 * @param category_id
	 *            the id of the category
	 */

	public boolean removeCategory(Long id, Long category_id) {

		// session/transaction start

		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		try {

			Forum f = (Forum) session.load(Forum.class, id);

			// remove rating from course

			for (Iterator<Category> myCategoryIterator = f.getCategories().iterator(); myCategoryIterator.hasNext();) {

				Category myCurrentCategory = (Category) myCategoryIterator.next();

				if (myCurrentCategory.getId().equals(category_id)) {
					myCategoryIterator.remove();
				}

			}

			session.update(f);

			// session/transaction end

			session.flush();
			tx.commit();

		}
		catch (RuntimeException r) {
			log.error("Could not remove category.");
			log.error(r.getMessage());
			tx.rollback();
			return false;
		}

		return true;
	}

	/**
	 * Adds a thread to a specific category
	 * 
	 * @param category_id the id of the category
	 * @param thread      the thread to be added
	 */

	public void addThread(Long category_id, org.pmedv.pojos.forums.Thread thread) {

		// session/transaction start

		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		// get Forum by given id

		try {
			Category category = (Category) session.createQuery(
					" from Category category where category.id = ?").setLong(0, category_id).uniqueResult();

			// add new rating

			thread.setCategory(category);
			category.getThreads().add(thread);

			// update course

			session.save(category);
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
	 * Removes specific thread from a category
	 * 
	 * @param category_id the id of the category
	 * @param thread_id   the id of the thread
	 */

	public void removeThread(Long category_id, Long thread_id) {

		// session/transaction start

		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		try {

			Category c = (Category) session.load(Category.class, category_id);

			// remove rating from course

			for (Iterator<Thread> myThreadIterator = c.getThreads().iterator(); myThreadIterator.hasNext();) {

				org.pmedv.pojos.forums.Thread myCurrentThread = (org.pmedv.pojos.forums.Thread) myThreadIterator
						.next();

				if (myCurrentThread.getId().equals(category_id)) {
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

	/**
	 * Adds a posting to a specific thread
	 * 
	 * @param thread_id  the id of the thread
	 * @param posting    the posting to add
	 */
	
	public Posting addPosting(Long thread_id, Posting posting) {
		
		// session/transaction start

		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		// get Forum by given id

		try {
			org.pmedv.pojos.forums.Thread thread = (org.pmedv.pojos.forums.Thread) session.createQuery(
					" from Thread thread where thread.id = ?").setLong(0, thread_id).uniqueResult();

			// add new posting

			posting.setThread(thread);
			thread.getPostings().add(posting);

			session.save(posting);
			session.flush();
			session.merge(posting);
			// session/transaction end

			tx.commit();
			return posting;

		}
		catch (RuntimeException r) {
			log.error("Could not add posting.");
			log.error(r.getMessage());
			tx.rollback();
			return null;
		}

	}

	/**
	 * Removes a specific thread from a category
	 * 
	 * @param thread_id  the id of the thread
	 * @param posting_id the id of the posting
	 */

	public void removePosting(Long thread_id, Long posting_id) {

		// session/transaction start

		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		try {

			org.pmedv.pojos.forums.Thread t = (org.pmedv.pojos.forums.Thread) session.load(Category.class,
					thread_id);

			// remove rating from course

			for (Iterator<Posting> postingIterator = t.getPostings().iterator(); postingIterator.hasNext();) {

				Posting myCurrentPosting = (Posting) postingIterator.next();

				if (myCurrentPosting.getId().equals(posting_id)) {
					postingIterator.remove();
				}

			}

			session.update(t);

			// session/transaction end

			session.flush();
			tx.commit();

		}
		catch (RuntimeException r) {
			log.error("Could not remove posting.");
			log.error(r.getMessage());
			tx.rollback();
		}

	}
	
	public void addGroup(long groupId, long forumId) {

		// session/transaction start
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		try {
			
			// get Item be given id	
			Forum forum = (Forum) session.createQuery(
				    " from Forum forum where forum.id = ?")
				    .setLong(0, forumId)
				    .uniqueResult();
	
			// add new group		    
			Usergroup usergroup = (Usergroup) session.createQuery(
		    " from Usergroup usergroup where usergroup.id = ?")
		    .setLong(0, groupId)
		    .uniqueResult();
			
			usergroup.getForums().add(forum);
			forum.getGroups().add(usergroup);
				    
		    // update 		    
		    session.persist(forum);
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
	 * Removes an existing forum from a group. 
	 * The forum and the group aren't deleted themselves
	 * 
	 * @param groupId
	 * @param forumId
	 */

	public void removeGroup( long groupId,long forumId) {

		log.debug("Trying to remove node with id "+forumId+" from group "+groupId+".");
		
		// session/transaction start
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		try {
			
			// get Item be given id	
			Forum forum = (Forum) session.createQuery(
				    " from Forum forum where forum.id = ?")
				    .setLong(0, forumId)
				    .uniqueResult();
	
			// add new group			
			if (forum != null) {
				log.debug("Found forum : "+forum.getId()+" : "+forum.getName());
			}
			else {
				log.debug("Could not fetch forum with id "+forumId+".");
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
			
			usergroup.getForums().remove(forum);
			forum.getGroups().remove(usergroup);
			
		    // update 
		    
		    session.persist(forum);
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

}
