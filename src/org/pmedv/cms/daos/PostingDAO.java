package org.pmedv.cms.daos;

import java.util.Iterator;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.pmedv.pojos.forums.Attachment;
import org.pmedv.pojos.forums.Posting;

public class PostingDAO extends AbstractDAO {

	@Override
	protected String getQueryAll() {
		return "from Posting posting order by posting.date";
	}

	@Override
	protected String getQueryById() {
		return "from Posting posting where posting.id = ?";
	}

	@Override
	protected String getQueryByName() {
		return "from Posting posting where posting.title = ? order by posting.title";
	}

	@Override
	protected String getQueryElementsByName() {
		return "from Posting posting where posting.title like %?% order by posting.title";
	}

	/**
	 * Adds an attachment to a posting
	 * 
	 * @param posting_id the id of the posting to add the attachment to
	 * @param attachment the attachment to add
	 * 
	 * @return true if adding was successful
	 */
	public boolean addAttachment(Long posting_id, Attachment attachment) {

		// session/transaction start

		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		// get posting by given id

		try {
			Posting posting = (Posting) session.createQuery(" from Posting posting where posting.id = ?").setLong(0, posting_id)
					.uniqueResult();

			// add new attachment

			posting.getAttachments().add(attachment);

			// update attachment

			session.save(attachment);
			session.flush();

			// session/transaction end

			tx.commit();

		}
		catch (RuntimeException r) {
			log.error("Could not add attachment.");
			log.error(r.getMessage());
			tx.rollback();
			return false;
		}

		return true;

	}

	/**
	 * Removes an attachment from a posting
	 * 
	 * @param id the id of the posting to remove the attachment from
	 * @param attachment_id the id of the attachment being removed
	 * 
	 * @return true, if remove was successful
	 */
	public boolean removeAttachment(Long id, Long attachment_id) {

		// session/transaction start

		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		try {

			Posting p = (Posting) session.load(Posting.class, id);

			// remove attachment from posting

			for (Iterator<Attachment> iter = p.getAttachments().iterator(); iter.hasNext();) {

				Attachment attachment = iter.next();

				if (attachment.getId().equals(attachment_id)) {
					iter.remove();
				}

			}

			session.update(p);

			// session/transaction end

			session.flush();
			tx.commit();

		}
		catch (RuntimeException r) {
			log.error("Could not remove attachment.");
			log.error(r.getMessage());
			tx.rollback();
			return false;
		}

		return true;
	}

}
