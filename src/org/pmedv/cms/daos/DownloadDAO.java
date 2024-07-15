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

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.pmedv.pojos.Download;
import org.pmedv.pojos.DownloadCategory;

public class DownloadDAO extends AbstractDAO {

	public DownloadDAO() {
	}
	
	@Override
	protected String getQueryAll() {		
		return "from Download download order by download.name";
	}

	@Override
	protected String getQueryById() {
		return "from Download download where download.id = ?";
	}

	@Override
	protected String getQueryByName() {
		return "from Download download where download.name = ?";
	}

	@Override
	protected String getQueryElementsByName() {
		return null;
	}
	
	public List<?> searchDownloads(String name, String author) throws IllegalArgumentException {
		
		if (author == null && name == null) {
			throw new IllegalArgumentException("You must provide an author or a name");
		}
		
		StringBuffer query = new StringBuffer();
		
		query.append("from Download download where download.publicfile = true and ");
		
		if (name != null) {
			query.append("download.name like ? ");
		}
		if (author != null) {
			if (name != null) {
				query.append("and ");
			}
			query.append("download.author like ? ");
		}
		
		query.append("order by download.name asc");
		
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
				
		List<?> result = null;
			
		try {
				
			if (name != null && author != null) {
				name = name.replaceAll("\\*", "%");
				author = author.replaceAll("\\*", "%");
				result = session.createQuery(query.toString()).setString(0, name).setString(1, author).list();				
			}
			else if (name != null && author == null) {
				name = name.replaceAll("\\*", "%");				
				result = session.createQuery(query.toString()).setString(0, name).list();
			}
			else {
				author = author.replaceAll("\\*", "%");
				result = session.createQuery(query.toString()).setString(0, author).list();
			}				
				
			session.flush();
			tx.commit();
		}
		catch (RuntimeException r) {
			r.printStackTrace();
			log.debug("Could not get any item.");
			tx.rollback();			
		}
	
		return result;
	}	
	
	public List<?> findLatestDownloads(int limit) {
		
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
				
		List<?> result = null;
			
		try {
			result = session.createQuery("from Download download where download.publicfile = true order by download.uploadTime desc").setMaxResults(limit).list();
				
			if (result.size() >= 1) {
			    log.debug("Fetching all objects of type "+result.get(0).getClass());		
			}
			session.flush();
			tx.commit();
		}
		catch (RuntimeException r) {
			r.printStackTrace();
			log.debug("Could not get any item.");
			tx.rollback();			
		}
	
		return result;
	}	
	
	public Object findByFileName(String Name) {

		Session session = sessionFactory.getCurrentSession();				
		Transaction tx = session.beginTransaction();
		
		Object o = null;
		
		try {
			o = session.createQuery("from Download download where download.filename = ?").setString(0,Name).uniqueResult();

			log.debug("Fetching object by name "+Name+" of type "+o.getClass());	
			
			session.flush();
			tx.commit();		

			return o;
		} 
		catch (RuntimeException r) {
			log.debug("Could not get any item.");
			tx.rollback();
		}
		
		return o;
		
	}	
	
	/**
	 * Adds a category to a download.
	 * 
	 * @param categoryId The id of the category to add
	 * @param downloadId The id of the download 
	 */
	public void addCategory(Long categoryId, Long downloadId) {

		// session/transaction start
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
	
		try {

			// get Item be given id	
			Download download = (Download) session.createQuery(
				    " from Download download where download.id = ?")
				    .setLong(0, downloadId)
				    .uniqueResult();

			// add new category
		    
			DownloadCategory category = (DownloadCategory) session.createQuery(
		    " from DownloadCategory category where category.id = ?")
		    .setLong(0, categoryId)
		    .uniqueResult();
			
			category.getDownloads().add(download);
			download.getCategories().add(category);
				    
		    // update 
		    
		    session.persist(download);
		    session.persist(category);
		    
			// session/transaction end
		    
		    tx.commit();
			
		}
		catch (RuntimeException r) {
			tx.rollback();
			log.info("Could not add category "+categoryId+" to download " + downloadId);
		}
	    
	}	
	
	/**
	 * removes a download from a category
	 * 
	 * @param categoryId the id of the category
	 * @param downloadId the id of the download
	 */
	public void removeCategory( Long categoryId,Long downloadId) {
		
		log.debug("Trying to remove download with id "+downloadId+" from category "+categoryId+".");
		
		// session/transaction start
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		try {
			// get Item be given id	
			Download download = (Download) session.createQuery(
				    " from Download download where download.id = ?")
				    .setLong(0, downloadId)
				    .uniqueResult();

			
			if (download != null) {
				log.debug("Found download : "+download.getId()+" : "+download.getName());
			}
			else {
				log.debug("Could not fetch download with id "+downloadId+".");
				tx.rollback();
				return;
			}
			
		    
			DownloadCategory category = (DownloadCategory) session.createQuery(
				    " from DownloadCategory category where category.id = ?")
				    .setLong(0, categoryId)
				    .uniqueResult();
			
			if (category != null) {
				log.debug("Found usergroup : "+category.getId()+" : "+category.getName());
			}
			else {
				log.debug("Could not fetch category with id "+categoryId+".");
				tx.rollback();
				return;
			}
			
			category.getDownloads().remove(download);
			download.getCategories().remove(category);
			
		    // update 
		    
		    session.persist(download);
		    session.persist(category);
		    
			// session/transaction end
		    
		    tx.commit();
			
		}
		catch (RuntimeException r) {
			tx.rollback();
			log.info("Could not remove category "+categoryId+" from download "+downloadId);
		}		


	}	
	
}
