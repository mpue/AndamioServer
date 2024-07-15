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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.pmedv.beans.objects.UserBean;
import org.pmedv.file.FileManager;
import org.pmedv.pojos.Attribute;
import org.pmedv.pojos.Avatar;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.Country;
import org.pmedv.pojos.Gallery;
import org.pmedv.pojos.User;
import org.pmedv.pojos.UserComment;
import org.pmedv.pojos.UserProfile;
import org.pmedv.pojos.Usergroup;
import org.pmedv.pojos.calendar.Calendar;
import org.pmedv.pojos.mailsystem.Account;

public class UserDAO extends AbstractDAO {

	public UserDAO() {
	}
	
	@Override
	protected String getQueryAll() {		
		return "from User user order by user.name";
	}

	@Override
	protected String getQueryById() {
		return "from User user where user.id = ?";
	}

	@Override
	protected String getQueryByName() {
		return "from User user where user.name = ?";
	}

	@Override
	protected String getQueryElementsByName() {
		return "from User user where user.name like %?% order by user.name";
	}

	public User findByMail(String eMail) {
		
		User user = (User)getGenericElement("from User user where user.email = '"+eMail+"' order by user.email");
		
		return user;
	}
	
	public User createAndStore(User u, UserProfile p) {
		
		if (u != null) {

			Session session = sessionFactory.getCurrentSession();
			Transaction tx = session.beginTransaction();

			try {
				u.setUserProfile(p);
				p.setUser(u);				
				session.save(u);
				session.merge(u);
				tx.commit();
				
			    log.debug("Creating object of type "+u.getClass());		

			    return u;
			}
			catch (RuntimeException r) {
				
				r.printStackTrace();
				
				log.info("Could not save object "+u.getClass());
				tx.rollback();
				
				return null;
			}
			
		}
		
		return null;		

	}	
	
	public void addGroup(Long groupId, Long userId) {

		// session/transaction start
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
	
		try {

			// get Item be given id	
			User user = (User) session.createQuery(
				    " from User user where user.id = ?")
				    .setLong(0, userId)
				    .uniqueResult();

			// add new group
		    
			Usergroup usergroup = (Usergroup) session.createQuery(
		    " from Usergroup usergroup where usergroup.id = ?")
		    .setLong(0, groupId)
		    .uniqueResult();
			
			usergroup.getUsers().add(user);
			user.getGroups().add(usergroup);
				    
		    // update 
		    
		    session.persist(user);
		    session.persist(usergroup);
		    
			// session/transaction end
		    
		    tx.commit();
			
		}
		catch (RuntimeException r) {
			tx.rollback();
			log.info("Could not add group "+groupId+" to user "+userId);
		}
	    
	}
	
	public void addGroups(Long groupId, ArrayList<Long> userIds) {

		// session/transaction start
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
	
		try {

			Usergroup usergroup = (Usergroup) session.createQuery(
		    " from Usergroup usergroup where usergroup.id = ?")
		    .setLong(0, groupId)
		    .uniqueResult();
			
			for (Long id : userIds) {

				User user = (User) session.createQuery(
			    " from User user where user.id = ?")
			    .setLong(0, id)
			    .uniqueResult();

				usergroup.getUsers().add(user);
				user.getGroups().add(usergroup);
			    
			    session.persist(user);
			    
			    log.debug("Adding user with id "+id+ " to group "+usergroup.getName());

			}
			
		    session.persist(usergroup);
		    
		    tx.commit();
			
		}
		catch (RuntimeException r) {
			tx.rollback();
			log.info("Could not add group to user.");
		}
		
	    
	}

	/**
	 * <p>
	 * Removes an existing user from a group
	 * </p>
	 * 
	 * @param groupId
	 * @param userId
	 */
	
	public void removeGroup( Long groupId,Long userId) {
		
		log.debug("Trying to remove user with id "+userId+" from group "+groupId+".");
		
		// session/transaction start
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		try {
			// get Item be given id	
			User user = (User) session.createQuery(
				    " from User user where user.id = ?")
				    .setLong(0, userId)
				    .uniqueResult();
					
			// add new group
			
			if (user != null) {
				log.debug("Found user : "+user.getId()+" : "+user.getName());
			}
			else {
				log.debug("Could not fetch user with id "+userId+".");
				tx.rollback();
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
				tx.rollback();
				return;
			}
			
			// admin group is not removeable from admin
			
			if (usergroup.getName().equalsIgnoreCase("admin") && user.getName().equalsIgnoreCase("admin")) {
				tx.rollback();
				return;
			}
			
			usergroup.getUsers().remove(user);
			user.getGroups().remove(usergroup);
			
		    // update 
		    
		    session.persist(user);
		    session.persist(usergroup);
		    
			// session/transaction end
		    
		    tx.commit();
			
		}
		catch (RuntimeException r) {
			tx.rollback();
			log.info("Could not remove group "+groupId+" from user "+userId);
		}		


	}	
	
	public void addAvatar(Long id, Avatar avatar) {

		// session/transaction start
		
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		// get user by given id

		try {
			User user = (User) session.createQuery(
		    " from User user where user.id = ?")
		    .setLong(0, id)
		    .uniqueResult();

			//add new avatar
			
			avatar.setUser(user);
			user.getAvatars().add(avatar);
		    
		    //update user
		
		    session.save(avatar);
		    session.flush();
		    
			// session/transaction end
		    
		    tx.commit();
			
		}
		catch (RuntimeException h) {
			tx.rollback();
		}
	    
	}	
	
	/**
	 * Removes specific avatar from user 
	 * 
	 * @param id         the user id
	 * @param avatar_id  the avatar id
	 */
	
	public void removeAvatar(Long id, Long avatar_id) {

		FileManager fileManager = new FileManager();
		
		// session/transaction start
			
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		try {
			
			User user = (User) session.createQuery(
		    " from User user where user.id = ?")
		    .setLong(0, id)
		    .uniqueResult();

			//remove avatar from user
			
		    Iterator<Avatar> it = user.getAvatars().iterator();
		    
		    while (it.hasNext()) {
		    	
		    	Avatar currentAvatar = it.next();
		    	
		    	if (currentAvatar.getId().equals(avatar_id)) {
		    		it.remove();
					fileManager.setFilename(currentAvatar.getFilename());
		    	}
		    	
		    }
		    
		    session.update(user);  
		    
			// session/transaction end

		    session.flush();
		    tx.commit();

			try {
    			Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);
    			String filePath = config.getBasepath()+"avatars/";   			
				fileManager.setFilepath(filePath);
				log.debug("Removing avatar from user with id "+id +" from "+filePath);						
				fileManager.delete();
			} 
    		catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		catch (RuntimeException h) {
			log.fatal("Could not remove avatar from user with id "+id);
			tx.rollback();
		}
		
	}

	/**
	 * Adds a comment from another to this user.
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
			User user = (User) session.createQuery(
		    " from User user where user.id = ?")
		    .setLong(0, id)
		    .uniqueResult();

			//add new comment
			
			// comment.setUser(user);
			user.getUserComments().add(comment);
		    
		    //update user
		
		    session.save(comment);
		    session.flush();
		    
			// session/transaction end
		    
		    tx.commit();
			
		}
		catch (RuntimeException h) {
			tx.rollback();
		}
	    
	}	
	
	/**
	 * Removes specific comment from user 
	 * 
	 * @param id         the user id
	 * @param commentId  the comment id
	 */
	
	public void removeComment(Long id, Long commentId) {
		
		// session/transaction start
			
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		try {
			
			User user = (User) session.createQuery(
		    " from User user where user.id = ?")
		    .setLong(0, id)
		    .uniqueResult();

			//remove avatar from user
			
		    Iterator<UserComment> it = user.getUserComments().iterator();
		    
		    while (it.hasNext()) {
		    	
		    	UserComment currentComment = it.next();
		    	
		    	if (currentComment.getId().equals(commentId)) {
		    		it.remove();
		    	}
		    	
		    }
		    
		    session.update(user);  
		    
			// session/transaction end

		    session.flush();
		    tx.commit();
			
		}
		catch (RuntimeException h) {
			log.fatal("Could not remove comment from user with id "+id);
			tx.rollback();
		}
		
	}
	
	
	/**
	 * Adds a gallery to this user.
	 * 
	 * @param id  the id of the user
	 * @param gallery the gallery to add
	 */
	public void addGallery(Long id, Gallery gallery) {

		// session/transaction start
		
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		// get user by given id

		try {
			User user = (User) session.createQuery(
		    " from User user where user.id = ?")
		    .setLong(0, id)
		    .uniqueResult();

			//add new gallery

			user.getUserGalleries().add(gallery);
		    
		    //update user
		
		    session.save(gallery);
		    session.flush();
		    
			// session/transaction end
		    
		    tx.commit();
			
		}
		catch (RuntimeException h) {
			tx.rollback();
		}
	    
	}	
	
	/**
	 * Removes a gallery from a user 
	 * 
	 * @param id         the user id
	 * @param galleryId  the gallery id
	 */
	
	public void removeGallery(Long id, Long galleryId) {
		
		// session/transaction start
			
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		try {
			
			User user = (User) session.createQuery(
		    " from User user where user.id = ?")
		    .setLong(0, id)
		    .uniqueResult();

			//remove gallery from user
			
		    Iterator<Gallery> it = user.getUserGalleries().iterator();
		    
		    while (it.hasNext()) {
		    	
		    	Gallery currentGallery = it.next();
		    	
		    	if (currentGallery.getId().equals(galleryId)) {
		    		it.remove();
		    	}
		    	
		    }
		    
		    session.update(user);  
		    
			// session/transaction end

		    session.flush();
		    tx.commit();
			
		}
		catch (RuntimeException h) {
			log.fatal("Could not remove comment from user with id "+id);
			tx.rollback();
		}
		
	}	
	
	/**
	 * Adds an attribute to a user 
	 * 
	 * @param id
	 * @param attribute
	 */
	public void addAttribute(Long id, Attribute attribute) {

		// session/transaction start
		
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		// get user by given id

		try {
			User user = (User) session.createQuery(
		    " from User user where user.id = ?")
		    .setLong(0, id)
		    .uniqueResult();

			//add new comment
			
			// comment.setUser(user);
			user.getAttributes().add(attribute);
		    
		    //update user
		
		    session.save(attribute);
		    session.flush();
		    
			// session/transaction end
		    
		    tx.commit();
			
		}
		catch (RuntimeException h) {
			log.fatal("Could not add attribute to user with id "+id);
			tx.rollback();
		}
	    
	}	
	
	/**
	 * Removes specific attribute from user 
	 * 
	 * @param id          the user id
	 * @param attributeId the attribute id
	 */
	
	public void removeAttribute(Long id, String key) {
		
		// session/transaction start
			
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		try {
			
			User user = (User) session.createQuery(
		    " from User user where user.id = ?")
		    .setLong(0, id)
		    .uniqueResult();

			//remove avatar from user
			
		    Iterator<Attribute> it = user.getAttributes().iterator();
		    
		    while (it.hasNext()) {
		    	
		    	Attribute currentAttribute = it.next();
		    	
		    	if (currentAttribute.getKey().equals(key)) {
		    		it.remove();
		    	}
		    	
		    }
		    
		    session.update(user);  
		    
			// session/transaction end

		    session.flush();
		    tx.commit();
			
		}
		catch (RuntimeException h) {
			log.fatal("Could not remove attribute from user with id "+id);
			tx.rollback();
		}
		
	}	
	
	/**
	 * Adds an mail account to a user 
	 * 
	 * @param id
	 * @param account
	 */
	public void addAccount(Long id, Account account) {

		// session/transaction start
		
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		// get user by given id

		try {
			User user = (User) session.createQuery(
		    " from User user where user.id = ?")
		    .setLong(0, id)
		    .uniqueResult();

			//add new comment
			
			// comment.setUser(user);
			user.getAccounts().add(account);
		    
		    //update user
		
		    session.save(account);
		    session.flush();
		    
			// session/transaction end
		    
		    tx.commit();
			
		}
		catch (RuntimeException h) {
			log.fatal("Could not add account to user with id "+id);
			tx.rollback();
		}
	    
	}	
	
	/**
	 * Removes specific attribute from user 
	 * 
	 * @param id          the user id
	 * @param accountId the account id
	 */
	
	public void removeAccount(Long id, Long accountId) {
		
		// session/transaction start
			
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		try {
			
			User user = (User) session.createQuery(
		    " from User user where user.id = ?")
		    .setLong(0, id)
		    .uniqueResult();

			//remove avatar from user
			
		    Iterator<Account> it = user.getAccounts().iterator();
		    
		    while (it.hasNext()) {
		    	
		    	Account currentAccount = it.next();
		    	
		    	if (currentAccount.getId().equals(accountId)) {
		    		it.remove();
		    	}
		    	
		    }
		    
		    session.update(user);  
		    
			// session/transaction end

		    session.flush();
		    tx.commit();
			
		}
		catch (RuntimeException h) {
			log.fatal("Could not remove account from user with id "+id);
			tx.rollback();
		}
		
	}		
	
	/**
	 * Assigns a country to a user
	 * 
	 * @param countryId the id of the country to assign
	 * @param userId the id of the user to assign the country to
	 */

	public void addCountry(Long countryId, Long userId) {

		// session/transaction start

		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		try {

			// get Item be given id

			User user = (User) session.createQuery(" from User user where user.id = ?").setLong(0, userId)
					.uniqueResult();

			// add new country

			Country country = (Country) session.createQuery(" from Country country where country.id = ?").setLong(0, countryId)
					.uniqueResult();

			country.getUsers().add(user);
			user.getCountries().add(country);

			// update

			session.persist(user);
			session.persist(country);

			// session/transaction end

			tx.commit();

		}
		catch (RuntimeException r) {
			tx.rollback();
			log.info("Could not add country " + countryId + " to user " + userId);
		}

	}

	/**
	 * <p>
	 * Removes an existing user from a country
	 * </p>
	 * <p>
	 * The user and the country aren't deleted themselves
	 * </p>
	 * 
	 * @param countryId
	 * @param userId
	 */

	public void removeCountry(Long countryId, Long userId) {

		log.debug("Trying to remove user with id " + userId + " from country " + countryId + ".");

		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		try {

			User user = (User) session.createQuery(" from User user where user.id = ?")
				.setLong(0, userId).uniqueResult();

			if (user != null) {
				log.debug("Found user : " + user.getId());
			}
			else {
				log.debug("Could not fetch user with id " + userId + ".");
				tx.rollback();
				return;
			}

			Country country = (Country) session.createQuery(" from Country country where country.id = ?")
				.setLong(0, countryId).uniqueResult();

			if (country != null) {
				log.debug("Found country : " + country.getId());
			}
			else {
				log.debug("Could not fetch country with id " + countryId + ".");
				tx.rollback();
				return;
			}

			country.getUsers().remove(user);
			user.getCountries().remove(country);

			session.persist(user);
			session.persist(country);

			tx.commit();

		}
		catch (RuntimeException r) {
			tx.rollback();
			log.info("Could not remove country " + countryId + " from user " + userId);
		}

	}


	/**
	 * Assigns an existing calendar to a user
	 * 
	 * @param calendarId
	 * @param userId
	 */
	public void addCalendar(Long calendarId, Long userId) {

		// session/transaction start

		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		try {

			// get Item be given id

			User user = (User) session.createQuery(" from User user where user.id = ?").setLong(0, userId)
					.uniqueResult();

			// add new calendar

			Calendar cal = (Calendar) session.createQuery(" from Calendar calendar where calendar.id = ?").setLong(0, calendarId)
					.uniqueResult();

			cal.getUsers().add(user);
			user.getCalendars().add(cal);

			// update

			session.persist(user);
			session.persist(cal);

			// session/transaction end

			tx.commit();

		}
		catch (RuntimeException r) {
			tx.rollback();
			log.info("Could not add calendar " + calendarId + " to user " + userId);
		}

	}

	/**
	 * <p>
	 * Removes an existing calendar from a user
	 * </p>
	 * <p>
	 * The user and the calendar aren't deleted themselves
	 * </p>
	 * 
	 * @param calendarId
	 * @param userId
	 */

	public void removeCalendar(Long calendarId, Long userId) {

		log.debug("Trying to remove user with id " + userId + " from calendar " + calendarId + ".");

		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		try {

			User user = (User) session.createQuery(" from User user where user.id = ?")
				.setLong(0, userId).uniqueResult();

			if (user != null) {
				log.debug("Found user : " + user.getId());
			}
			else {
				log.debug("Could not fetch user with id " + userId + ".");
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

			cal.getUsers().remove(user);
			user.getCalendars().remove(cal);

			session.persist(user);
			session.persist(cal);

			tx.commit();

		}
		catch (RuntimeException r) {
			tx.rollback();
			log.info("Could not remove calendar " + calendarId + " from user " + userId);
		}

	}
	
}
