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
import org.pmedv.pojos.Content;
import org.pmedv.pojos.Revision;
import org.pmedv.pojos.User;
import org.pmedv.pojos.UserComment;

public class ContentDAO extends AbstractDAO {

	public ContentDAO() {
	}
	
	@Override
	protected String getQueryAll() {
		return "from Content";
	}

	@Override
	protected String getQueryById() {
		return "from Content content where content.id = ?";
	}

	@Override
	protected String getQueryByName() {
		return "from Content content where content.contentname = ?";
	}
	
	public long getMinId() {		
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		Object o = null;
		
		try {
			o = session.createQuery("select min(content.id) from Content content") .uniqueResult();
			
			session.flush();
			tx.commit();
		} 
		catch (RuntimeException e) {
			tx.rollback();
			log.error(e.getMessage());
		}
		
		return (Long)o;
	
	}

	@Override
	protected String getQueryElementsByName() {
		return null;
	}
	
	/**
	 * Adds a new revision to a specified content
	 * 
	 * @param id
	 * @param revision
	 */

	public void addRevision(Long id, Revision revision) {

		// session/transaction start
		
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		// get content by given id

		try {
			Content content = (Content) session.createQuery(
		    " from Content content where content.id = ?")
		    .setLong(0, id)
		    .uniqueResult();

			//add new rating
			
			revision.setContent(content);
			content.getRevisions().add(revision);
		    
		    //update revision
		
		    session.save(revision);
		    session.flush();
		    
			// session/transaction end
		    
		    tx.commit();
			
		}
		catch (RuntimeException r) {
			tx.rollback();
			log.fatal("Something went wrong while creating a new revision for content.");
		}
	    
	}	
	
	/**
	 * Removes specific avatar from user 
	 * 
	 * @param id         the user id
	 * @param avatar_id  the avatar id
	 */
	
	public void removeRevision(Long id, Long revision_id) {

		// session/transaction start
			
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		try {
			
			Content content = (Content) session.createQuery(
		    " from Content content where content.id = ?")
		    .setLong(0, id)
		    .uniqueResult();

			//remove revision from content
			
		    Iterator<Revision> revisionIterator = content.getRevisions().iterator();
		    
		    while (revisionIterator.hasNext()) {
		    	
		    	Revision currentRevision = (Revision)revisionIterator.next();
		    	
		    	if (currentRevision.getId().equals(revision_id)) {
		    		revisionIterator.remove();
		    	}
		    	
		    }
		    
		    session.update(content);  
		    
			// session/transaction end

		    session.flush();
		    tx.commit();
			
		}
		catch (RuntimeException r) {
			log.fatal("Could not remove revision from content with id "+id);
			tx.rollback();
		}
		
	}
	
	
	/**
	 * Adds a comment from a {@link User} to this content
	 * 
	 * @param id
	 * @param comment
	 */
	public void addComment(Long id, UserComment comment) {

		// session/transaction start
		
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		// get user by given id

		try {
			Content content = (Content) session.createQuery(
		    " from Content content where content.id = ?")
		    .setLong(0, id)
		    .uniqueResult();

			//add new comment
			content.getUserComments().add(comment);
		    
		    //update user
		
		    session.save(comment);
		    session.flush();
		    
			// session/transaction end
		    
		    tx.commit();
			
		}
		catch (RuntimeException h) {
			log.fatal("Could not add comment to content with id "+id);
			tx.rollback();
		}
	    
	}	
	
	/**
	 * Removes specific comment from the content 
	 * 
	 * @param id         the user id
	 * @param commentId  the comment id
	 */
	
	public void removeComment(Long id, Long commentId) {
		
		// session/transaction start
			
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		try {
			
			Content content = (Content) session.createQuery(
		    " from Content content where content.id = ?")
		    .setLong(0, id)
		    .uniqueResult();

			//remove comment from content
			
		    Iterator<UserComment> it = content.getUserComments().iterator();
		    
		    while (it.hasNext()) {
		    	
		    	UserComment currentComment = it.next();
		    	
		    	if (currentComment.getId().equals(commentId)) {
		    		it.remove();
		    	}
		    	
		    }
		    
		    session.update(content);  
		    
			// session/transaction end

		    session.flush();
		    tx.commit();
			
		}
		catch (RuntimeException h) {
			log.fatal("Could not remove comment from content with id "+id);
			tx.rollback();
		}
		
	}	

}
