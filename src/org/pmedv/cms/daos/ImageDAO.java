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
import org.pmedv.pojos.Image;
import org.pmedv.pojos.UserComment;


public class ImageDAO extends AbstractDAO {

	public ImageDAO() {
	}
	
	@Override
	protected String getQueryAll() {		
		return "from Image image order by image.description";
	}

	@Override
	protected String getQueryById() {
		return "from Image image where image.id = ?";
	}

	@Override
	protected String getQueryByName() {
		return "from Image image where image.filename = ?";
	}

	@Override
	protected String getQueryElementsByName() {
		return null;
	}
	
	/**
	 * Adds a comment from a user to this image.
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
			Image image = (Image) session.createQuery(
		    " from Image image where image.id = ?")
		    .setLong(0, id)
		    .uniqueResult();

			//add new comment

			image.getUserComments().add(comment);
		    
		    //update user
		
		    session.save(comment);
		    session.flush();
		    
			// session/transaction end
		    
		    tx.commit();
			
		}
		catch (RuntimeException h) {
			h.printStackTrace();
			tx.rollback();
		}
	    
	}	
	
	/**
	 * Removes specific comment from image 
	 * 
	 * @param id         the user id
	 * @param commentId  the comment id
	 */
	
	public void removeComment(Long id, Long commentId) {
		
		// session/transaction start
			
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		try {
			
			Image image = (Image) session.createQuery(
		    " from Image image where image.id = ?")
		    .setLong(0, id)
		    .uniqueResult();

			//remove avatar from user
			
		    Iterator<UserComment> it = image.getUserComments().iterator();
		    
		    while (it.hasNext()) {
		    	
		    	UserComment currentComment = it.next();
		    	
		    	if (currentComment.getId().equals(commentId)) {
		    		it.remove();
		    	}
		    	
		    }
		    
		    session.update(image);  
		    
			// session/transaction end

		    session.flush();
		    tx.commit();
			
		}
		catch (RuntimeException h) {
			log.fatal("Could not remove comment from user with id "+id);
			tx.rollback();
		}
		
	}
	
}
