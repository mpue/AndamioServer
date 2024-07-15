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
import org.pmedv.pojos.Gallery;
import org.pmedv.pojos.Image;

public class GalleryDAO extends AbstractDAO {
		
	public GalleryDAO() {
	}
	
	@Override
	protected String getQueryAll() {
		return "from Gallery gallery order by gallery.galleryname";
	}

	@Override
	protected String getQueryById() {
		return "from Gallery gallery where gallery.id = ?";
	}

	@Override
	protected String getQueryByName() {
		return "from Gallery gallery where gallery.galleryname = ?";
	}	

	
	/**
	 * Adds an image to a gallery
	 * 
	 * @param id      The id of the gallery to add the image to
	 * @param image   The image to be added
	 */
	public boolean addImage(Long id, Image image) {

		// session/transaction start
		
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		// get gallery by given id

		try {
			Gallery gallery = (Gallery) session.createQuery(
		    " from Gallery gallery where gallery.id = ?")
		    .setLong(0, id)
		    .uniqueResult();

			// add new image
			
			image.setGallery(gallery);
			gallery.getImages().add(image);
		    
		    // update image
		
		    session.save(image);
		    session.flush();
		    
			// session/transaction end
		    
		    tx.commit();
			return true;
		}
		catch (RuntimeException r) {
			log.info("Something went wrong while storing image to gallery "+id);
			r.printStackTrace();
			tx.rollback();
			return false;
		}
	    
	}	
	

	

	/**
	 * Removes an image from a gallery
	 * 
	 * @param id  the gallery id to remove from
	 * @param image_id the id of the image to be removed
	 * 
	 */
	public boolean removeImage(Long id, Long image_id) {
		
		// session/transaction start
			
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		try {
			
			Gallery gallery = (Gallery) session.createQuery(
		    " from Gallery gallery where gallery.id = ?")
		    .setLong(0, id)
		    .uniqueResult();

			// remove image from gallery
			
		    Iterator<Image> imageIterator = gallery.getImages().iterator();
		    
		    while (imageIterator.hasNext()) {
		    	
		    	Image currentImage = (Image)imageIterator.next();
		    	
		    	if (currentImage.getId().equals(image_id)) {
		    		imageIterator.remove();
		    	}
		    	
		    }
		    
		    session.update(gallery);  
		    
			// session/transaction end

		    session.flush();
		    tx.commit();
			
		    return true;
		}
		catch (RuntimeException r) {
			log.fatal("Could not remove image from gallery with id "+id);
			tx.rollback();
			return false;
		}
		
	}	

	@Override
	protected String getQueryElementsByName() {
		return "from Gallery gallery where gallery.galleryname like ? order by gallery.galleryname";
	}

	
}
