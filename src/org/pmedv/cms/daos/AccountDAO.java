package org.pmedv.cms.daos;

import java.util.Iterator;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.pmedv.pojos.mailsystem.Account;
import org.pmedv.pojos.mailsystem.Folder;

public class AccountDAO extends AbstractDAO {

	@Override
	protected String getQueryAll() {
		return "from Account account order by account.name";
	}

	@Override
	protected String getQueryById() {
		return "from Account account where account.id = ?";
	}

	@Override
	protected String getQueryByName() {
		return "from Account account where account.name = ?";
	}

	@Override
	protected String getQueryElementsByName() {
		return "from Account account where account.name like ? order by account.name";
	}
	
	/**
	 * Adds an folder account to an account 
	 * 
	 * @param id      the id of the account
	 * @param folder  the folder to add
	 */
	public void addFolder(Long id, Folder folder) {

		// session/transaction start
		
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		// get account by given id

		try {
			Account account = (Account) session.createQuery(
		    " from Account account where account.id = ?")
		    .setLong(0, id)
		    .uniqueResult();

			// add new folder

			account.getFolders().add(folder);
		    
		    // update account
		
		    session.save(folder);
		    session.flush();
		    
			// session/transaction end
		    
		    tx.commit();
			
		}
		catch (RuntimeException h) {
			log.fatal("Could not add folder to account with id "+id);
			tx.rollback();
		}
	    
	}	
	
	/**
	 * Removes specific folder from account 
	 * 
	 * @param id       the account id
	 * @param folderId the folder id
	 */
	
	public void removeFolder(Long id, Long folderId) {
		
		// session/transaction start
			
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		try {
			
			Account account = (Account) session.createQuery(
		    " from Account account where account.id = ?")
		    .setLong(0, id)
		    .uniqueResult();

			// remove folder from account
			
		    Iterator<Folder> it = account.getFolders().iterator();
		    
		    while (it.hasNext()) {
		    	
		    	Folder currentFolder = it.next();
		    	
		    	if (currentFolder.getId().equals(folderId)) {
		    		it.remove();
		    	}
		    	
		    }
		    
		    session.update(account);  
		    
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
