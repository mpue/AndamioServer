package org.pmedv.cms.daos;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;


/**
 * <p>
 * Faq data access object
 * </p>
 * 
 * This object allows basic access to create and remove faqs
 * 
 * @author Steffen Hochmuth
 *
 */
public class FaqDAO extends AbstractDAO {

	@Override
	protected String getQueryAll() {
		return "from Faq";
	}

	@Override
	protected String getQueryById() {
		return "from Faq faq where faq.id = ?";
	}

	@Override
	protected String getQueryByName() {
		return null;
	}

	@Override
	protected String getQueryElementsByName() {
		return null;
	}
	
	public  List<?> findFaqsByStudyId(String studyId) {

		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		List<?> result = session.createQuery("from Faq faq where faq.studyId=?").setString(0, studyId).list();
			
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
	
//	/**
//	 * Insert a specific faq
//	 * 
//	 * @param faq the faq to be inserted
//	 */
//
//	public void insertFaq(Faq faq) {
//
//		// session/transaction start
//		
//		Session session = sessionFactory.getCurrentSession();
//		Transaction tx = session.beginTransaction();
//
//		try {   
//		    //update course
//		    session.save(faq);
//		    session.flush();
//		    
//			// session/transaction end
//		    tx.commit();
//		}
//		catch (RuntimeException r) {
//			log.error("Could not insert faq.");
//			log.error(r.getMessage());
//			tx.rollback();
//		}
//	}	
//	
//	/**
//	 * delete a specific faq
//	 * 
//	 * @param faq the faq to be deleted
//	 */	
//	public void deleteFaq(Faq faq) {
//		
//		// session/transaction start
//		
//		Session session = sessionFactory.getCurrentSession();
//		Transaction tx = session.beginTransaction();
//
//		try {	    
//		    session.delete(faq);
//		    
//			// session/transaction end
//		    
//		    session.flush();
//		    tx.commit();
//			
//		}
//		catch (RuntimeException r) {
//			log.error("Could not delete faq.");
//			log.error(r.getMessage());
//			tx.rollback();
//		}
//	}
}

