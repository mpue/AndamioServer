package org.pmedv.cms.daos;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.pmedv.pojos.User;

public class ThreadDAO extends AbstractDAO {

	@Override
	protected String getQueryAll() {
		return "from Thread thread order by thread.name";
	}

	@Override
	protected String getQueryById() {
		return "from Thread thread where thread.id = ?";
	}

	@Override
	protected String getQueryByName() {
		return "from Thread thread where thread.name = ?";
	}

	@Override
	protected String getQueryElementsByName() {
		return "from Thread thread where thread.name like ? order by thread.name";
	}

	public  List<?> getUnreadThreads(User user) {

		Session session = sessionFactory.getCurrentSession();		
		Transaction tx = session.beginTransaction();
				
		List<?> result = null;
		
		try {
			
			if (user.getLastActivity() == null)
				result = session.createQuery("from Thread thread").list();
			else
				result = session.createQuery("from Thread thread where thread.lastPost >= ?").setTimestamp(0, user.getLastActivity()).list();
			
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
	
}
