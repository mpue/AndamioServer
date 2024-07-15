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
package org.pmedv.services;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.beans.objects.GalleryBean;
import org.pmedv.beans.objects.ImageBean;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.Gallery;
import org.pmedv.pojos.Image;

public class GalleryServiceImpl implements GalleryService {
	
	private static final Log log = LogFactory.getLog(GalleryServiceImpl.class);

	@Override
	public boolean createGallery(GalleryBean gallery) throws IllegalArgumentException {
		
		/* 

		Object credentials = null;
		Object principal = null;
		
		try {
			credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();
			principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} 
		catch (Exception e) {			
			 throw new IllegalArgumentException("Error : missing login credentials.");
		}
				
		AuthenticationService service = (AuthenticationService)AppContext.getApplicationContext().getBean("authenticationService");
		try {
			if (!service.isAuthenticatable(principal, credentials)) {
				throw new IllegalArgumentException("You are not allowed to create galleries.");
			}			
		}
		catch (IllegalArgumentException i) {
			throw(i);
		}
		
		*/
		
		if (gallery == null)
			throw new IllegalArgumentException("Gallery must not be null");
		
		if(gallery.getGalleryname().length() < 1)
			throw new IllegalArgumentException("You must provide a gallery name.");
		
		if (DAOManager.getInstance().getGalleryDAO().findByName(gallery.getGalleryname()) != null) 
			throw new IllegalArgumentException("A gallery with this name already exists.");
		
		Gallery g = new Gallery();
		
		g.setDescription(gallery.getDescription());
		g.setGalleryname(gallery.getGalleryname());
		g.setGallerytext(gallery.getGallerytext());
		g.setColumns(gallery.getColumns());
		g.setTemplate(gallery.getTemplate());
		g.setRanking(0);
		g.setThumbheight(gallery.getThumbheight());
		g.setThumbwidth(gallery.getThumbwidth());
		
		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);
		
		String galleryBase = config.getBasepath()+config.getGallerypath();

		File base = new File(galleryBase);
		
		if (!base.exists())
			base.mkdir();
		
		String galleryPath = config.getBasepath()+config.getGallerypath()+gallery.getGalleryname();
		
		File f = new File(galleryPath);
		
		if (!f.exists())
			f.mkdir();

		String thumbDir = galleryPath+"/thumbs";
		
		File t = new File(thumbDir);

		if (!t.exists())
			t.mkdir();
		
		return DAOManager.getInstance().getGalleryDAO().createAndStore(g) != null;
	}

	@Override
	public boolean deleteGallery(Long galleryId) throws IllegalArgumentException {
		
		/*
		
		Object credentials = null;
		Object principal = null;
		
		try {
			credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();
			principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} 
		catch (Exception e) {
			throw new IllegalArgumentException("Error : missing login credentials.");
		}
				
		AuthenticationService service = (AuthenticationService)AppContext.getApplicationContext().getBean("authenticationService");
		try {
			if (!service.isAuthenticatable(principal, credentials)) {
				throw new IllegalArgumentException("You are not allowed to remove galleries.");
			}			
		}
		catch (IllegalArgumentException i) {
			throw(i);
		}

		*/
		
		if (galleryId == null)
			throw new IllegalArgumentException("Gallery id must not be null");
		
		Gallery gallery = (Gallery)DAOManager.getInstance().getGalleryDAO().findByID(galleryId);	
		
		if (gallery == null)
			throw new IllegalArgumentException("Gallery to be deleted does not exist.");
		
		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);
		String galleryPath = config.getBasepath()+config.getGallerypath()+gallery.getGalleryname();		
		File galleryFile = new File(galleryPath);
		
		String thumbPath = config.getBasepath()+config.getGallerypath()+gallery.getGalleryname()+"/thumbs";		
		File thumbFile = new File(thumbPath);
		
		File[] thumbs = thumbFile.listFiles();
		
		for (int i=0; i < thumbs.length;i++) {
			if (!thumbs[i].delete())
				throw new IllegalArgumentException("Could not delete thumbnail.");
		}
		
		File[] images = galleryFile.listFiles(); 

		for (int i=0; i < images.length;i++) {
			if (!images[i].delete())
				throw new IllegalArgumentException("Could not delete image.");
		}
		
		thumbFile.delete();
		galleryFile.delete();

		return DAOManager.getInstance().getGalleryDAO().delete(gallery);
		
	}

	@Override
	public GalleryBean findGalleryById(Long galleryId) throws IllegalArgumentException {
		
		if (galleryId == null)
			throw new IllegalArgumentException("Gallery id must not be null");		
		
		Gallery gallery = (Gallery)DAOManager.getInstance().getGalleryDAO().findByID(galleryId);
		
		GalleryBean g = null;
		
		if (gallery != null) {
			
			g = new GalleryBean();
			
			g.setId(gallery.getId());
			g.setDescription(gallery.getDescription());
			g.setGalleryname(gallery.getGalleryname());
			g.setGallerytext(gallery.getGallerytext());
			g.setColumns(gallery.getColumns());
			g.setTemplate(gallery.getTemplate());
			g.setRanking(gallery.getRanking());
			g.setThumbheight(gallery.getThumbheight());
			g.setThumbwidth(gallery.getThumbwidth());

			for (Image image : gallery.getImages()) {
				
				ImageBean i = new ImageBean();
				i.setDescription(image.getDescription());
				i.setFilename(image.getFilename());
				i.setId(image.getId());
				i.setLastmodified(image.getLastmodified());
				i.setRanking(image.getRanking());
				
				g.getImages().add(i);
				
			}
			
		}
		
		return g;
	}
	
	@Override
	public GalleryBean findGalleryByName(String name) throws IllegalArgumentException {
		
		if (name == null)
			throw new IllegalArgumentException("Name must not be null");
		
		Gallery gallery = (Gallery)DAOManager.getInstance().getGalleryDAO().findByName(name);
		
		GalleryBean g = null;
		
		if (gallery != null) {
			
			g = new GalleryBean();
			
			g.setId(gallery.getId());
			g.setDescription(gallery.getDescription());
			g.setGalleryname(gallery.getGalleryname());
			g.setGallerytext(gallery.getGallerytext());
			g.setColumns(gallery.getColumns());
			g.setTemplate(gallery.getTemplate());
			g.setRanking(gallery.getRanking());
			g.setThumbheight(gallery.getThumbheight());
			g.setThumbwidth(gallery.getThumbwidth());

			for (Image image : gallery.getImages()) {
				
				ImageBean i = new ImageBean();
				i.setDescription(image.getDescription());
				i.setFilename(image.getFilename());
				i.setName(image.getName());
				i.setId(image.getId());
				i.setLastmodified(image.getLastmodified());
				i.setRanking(image.getRanking());
				i.setGallery(g);
				
				g.getImages().add(i);
				
			}
			
		}
		
		return g;
	}
	

	@Override
	public boolean updateGallery(GalleryBean gallery) throws IllegalArgumentException {
		
		if (gallery == null)
			throw new IllegalArgumentException("Gallery must not be null");
		
		Gallery g = (Gallery)DAOManager.getInstance().getGalleryDAO().findByID(gallery.getRemoteId());
		
		if (g == null)
			throw new IllegalArgumentException("Gallery to be updated does not exist.");
		
		g.setDescription(gallery.getDescription());
		g.setGalleryname(gallery.getGalleryname());
		g.setGallerytext(gallery.getGallerytext());
		g.setColumns(gallery.getColumns());
		g.setTemplate(gallery.getTemplate());
		g.setRanking(gallery.getRanking());
		g.setThumbheight(gallery.getThumbheight());
		g.setThumbwidth(gallery.getThumbwidth());
		
		return DAOManager.getInstance().getGalleryDAO().update(g);
	}
	
	@Override
	public boolean addImage(ImageBean image, Long galleryId) throws IllegalArgumentException {
		
		if (galleryId == null)
			throw new IllegalArgumentException("Gallery id must not be null");

		if (image == null)
			throw new IllegalArgumentException("Image must not be null");
		
		Image iTest = (Image)DAOManager.getInstance().getGImageDAO().findByName(image.getFilename());
		
		if (iTest != null && iTest.getGallery().getId().equals(galleryId))
			throw new IllegalArgumentException("There is already an image inside this gallery with this filename");

		Gallery gallery = (Gallery)DAOManager.getInstance().getGalleryDAO().findByID(galleryId);
		
		if (gallery == null)
			throw new IllegalArgumentException("The gallery does not exist.");
		
		
		Image i = new Image();
		
		i.setDescription(image.getDescription());
		i.setFilename(image.getFilename());
		i.setName(image.getName());
		i.setLastmodified(new Date().getTime());
		i.setRanking(image.getRanking());
		
//		if (image.getData() != null) {
			
			Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);
			
			String galleryPath = config.getBasepath()+config.getGallerypath()+gallery.getGalleryname();
			
//			java.awt.Image img = ImageUtils.getImageFromArray(image.getData().getPixels(), image.getData().getWidth(), image.getData().getHeight());			
//			log.info("got image data "+img.getWidth(null)+"x"+image.getData().getHeight());
//			log.info("image data is "+image.getData().getPixels().length);
//			
//			BufferedImage bdest = new BufferedImage(image.getData().getWidth(),image.getData().getHeight(),BufferedImage.TYPE_INT_RGB);
//			
//			Graphics2D g2 = bdest.createGraphics();
//			g2.drawImage(img,0,0,null);

			try {
				//	ImageIO.write(bdest, "JPG", new File(galleryPath+"/"+image.getFilename()));

				// copy image
				
				String sourceFilePath = config.getBasepath()+config.getImportpath()+image.getFilename();
				String destFilePath   = galleryPath+"/"+image.getFilename();
				
				log.info("copying file from "+sourceFilePath+" to "+destFilePath);				
				FileUtils.copyFile(new File(sourceFilePath), new File(destFilePath));
				log.info("Deleting file "+sourceFilePath);	
				
				File f = new File(sourceFilePath);					
				f.delete();
				
				// copy thumbnail

				String sourceThumbFilePath = config.getBasepath()+config.getImportpath()+"thb_"+image.getFilename();
				String destThumbFilePath   = galleryPath+"/thumbs/thb_"+image.getFilename();
				
				log.info("copying file from "+sourceThumbFilePath+" to "+destThumbFilePath);				
				FileUtils.copyFile(new File(sourceThumbFilePath), new File(destThumbFilePath));
				log.info("Deleting file "+sourceThumbFilePath);
				
				f = new File(sourceThumbFilePath);
				f.delete();

				
			} 
			catch (Exception e) {
				e.printStackTrace();
			}

//			}
		
		log.info("Adding image "+image.getFilename()+" to gallery with id : "+galleryId);		
		
		return DAOManager.getInstance().getGalleryDAO().addImage(galleryId, i);
	
	}

	@Override
	public boolean removeImage(Long imageId, Long galleryId) throws IllegalArgumentException {
		
		if (galleryId == null)
			throw new IllegalArgumentException("Gallery id must not be null");

		if (imageId == null)
			throw new IllegalArgumentException("Image id must not be null");
		
		Gallery g = (Gallery)DAOManager.getInstance().getGalleryDAO().findByID(galleryId);
		
		if (g == null)
			throw new IllegalArgumentException("The gallery does not exist.");

		Image i = (Image)DAOManager.getInstance().getGImageDAO().findByID(imageId); 

		if (i == null)
			throw new IllegalArgumentException("The image does not exist.");
		
		return DAOManager.getInstance().getGalleryDAO().removeImage(galleryId, imageId);	
	}

	@Override
	public boolean updateImage(ImageBean image) throws IllegalArgumentException {
		
		if (image == null)
			throw new IllegalArgumentException("Image must not be null");

		Image i = (Image)DAOManager.getInstance().getGImageDAO().findByID(image.getId());

		if (i == null)
			throw new IllegalArgumentException("The image does not exist.");
		
		i.setDescription(image.getDescription());
		i.setFilename(image.getFilename());
		i.setLastmodified(new Date().getTime());
		i.setName(image.getName());
		i.setRanking(image.getRanking());
		
		return DAOManager.getInstance().getGImageDAO().update(i);
	}

	
	@Override
	public boolean copyImage(Long imageId, Long targetGalleryId) throws IllegalArgumentException {
		
		if (targetGalleryId == null)
			throw new IllegalArgumentException("Gallery id must not be null");

		if (imageId == null)
			throw new IllegalArgumentException("Image id must not be null");
		
		Image image = (Image)DAOManager.getInstance().getGImageDAO().findByID(imageId);
		
		Image newImage = new Image();
		
		newImage.setDescription(image.getDescription());
		newImage.setFilename(image.getFilename());
		newImage.setName(image.getName());		
		newImage.setLastmodified(new Date().getTime());
		newImage.setPath(image.getPath());
		newImage.setRanking(image.getRanking());
	
		return DAOManager.getInstance().getGalleryDAO().addImage(targetGalleryId, image);
		
	}
	
	@Override
	public boolean moveImage(Long imageId, Long targetGalleryId) throws IllegalArgumentException {

		if (targetGalleryId == null)
			throw new IllegalArgumentException("Gallery id must not be null");

		if (imageId == null)
			throw new IllegalArgumentException("Image id must not be null");
		
		Image image = (Image)DAOManager.getInstance().getGImageDAO().findByID(imageId);		

		if (DAOManager.getInstance().getGalleryDAO().removeImage(image.getGallery().getId(),imageId))  {

			Image newImage = new Image();
			
			newImage.setDescription(image.getDescription());
			newImage.setFilename(image.getFilename());
			newImage.setName(image.getName());
			newImage.setLastmodified(new Date().getTime());
			newImage.setPath(image.getPath());
			newImage.setRanking(image.getRanking());
			
			return DAOManager.getInstance().getGalleryDAO().addImage(targetGalleryId, image);
		}
			
		else
			return false;

	}

	@Override
	public ArrayList<GalleryBean> findAll() {
		
		ArrayList<GalleryBean> galleries = new ArrayList<GalleryBean>();
		
		List<?> current = DAOManager.getInstance().getGalleryDAO().findAllItems();
		
		for (Object o : current) {
			
			Gallery gallery = (Gallery) o;
			
			GalleryBean g = new GalleryBean();
			
			g.setId(gallery.getId());
			g.setDescription(gallery.getDescription());
			g.setGalleryname(gallery.getGalleryname());
			g.setGallerytext(gallery.getGallerytext());
			g.setColumns(gallery.getColumns());
			g.setTemplate(gallery.getTemplate());
			g.setRanking(gallery.getRanking());
			g.setThumbheight(gallery.getThumbheight());
			g.setThumbwidth(gallery.getThumbwidth());
			
			for (Image image : gallery.getImages()) {
				
				ImageBean i = new ImageBean();
				i.setDescription(image.getDescription());
				i.setFilename(image.getFilename());
				i.setName(image.getName());
				i.setId(image.getId());
				i.setLastmodified(image.getLastmodified());
				i.setRanking(image.getRanking());
				i.setGallery(g);
				
				g.getImages().add(i);
				
			}
			
			galleries.add(g);
			
		}
		
		return galleries;
		
	}
	

}
