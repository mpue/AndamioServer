package org.pmedv.cms.daos;

import java.util.Iterator;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.pmedv.pojos.mailsystem.Folder;
import org.pmedv.pojos.mailsystem.Message;

public class FolderDAO extends AbstractDAO {

	@Override
	protected String getQueryAll() {
		return "from Folder folder order by folder.name";
	}

	@Override
	protected String getQueryById() {
		return "from Folder folder where folder.id = ?";
	}

	@Override
	protected String getQueryByName() {
		return "from Folder folder where folder.name = ?";
	}

	@Override
	protected String getQueryElementsByName() {
		return "from Folder folder where folder.name like ? order by folder.name";
	}
	
	/**
	 * Adds a message to a folder 
	 * 
	 * @param id
	 * @param message
	 */
	public void addMessage(Long id, Message message) {

		// session/transaction start
		
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		// get folder by given id

		try {
			Folder folder = (Folder) session.createQuery(
		    " from Folder folder where folder.id = ?")
		    .setLong(0, id)
		    .uniqueResult();

			// add new message

			folder.getMessages().add(message);
		    
		    // update folder
		
		    session.save(message);
		    session.flush();
		    
			// session/transaction end
		    
		    tx.commit();
			
		}
		catch (RuntimeException h) {
			log.fatal("Could not add message to folder with id "+id);
			tx.rollback();
		}
	    
	}	
	
	/**
	 * Removes specific message from a folder 
	 * 
	 * @param id        the folder id
	 * @param messageId the message id
	 */
	
	public void removeMessage(Long id, Long messageId) {
		
		// session/transaction start
			
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		try {
			
			Folder folder = (Folder) session.createQuery(
		    " from Folder folder where folder.id = ?")
		    .setLong(0, id)
		    .uniqueResult();

			// remove message from folder
			
		    Iterator<Message> it = folder.getMessages().iterator();
		    
		    while (it.hasNext()) {
		    	
		    	Message currentMessage = it.next();
		    	
		    	if (currentMessage.getId().equals(messageId)) {
		    		it.remove();
		    	}
		    	
		    }
		    
		    session.update(folder);  
		    
			// session/transaction end

		    session.flush();
		    tx.commit();
			
		}
		catch (RuntimeException h) {
			log.fatal("Could not remove folder from account with id "+id);
			tx.rollback();
		}
		
	}

}
