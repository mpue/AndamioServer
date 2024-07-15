/**

	WeberknechtCMS - Open Source Content Management
	Written and maintained by Matthias Pueski 
	
	Copyright (c) 2003-2011 Matthias Pueski
	
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
package org.pmedv.actions.userpages;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.pmedv.cms.common.Params;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.cms.daos.GalleryDAO;
import org.pmedv.cms.daos.UserDAO;
import org.pmedv.forms.GalleryForm;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.Gallery;
import org.pmedv.pojos.Image;
import org.pmedv.pojos.User;

public class UserGalleryAction extends DispatchAction {

	public ActionForward addGallery(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		GalleryForm galleryForm = (GalleryForm) form;

		final String login = (String) request.getSession().getAttribute("login");

		final UserDAO userDAO = DAOManager.getInstance().getUserDAO();

		User user = (User) userDAO.findByName(login);

		Gallery g = new Gallery();

		g.setGalleryname(galleryForm.getGalleryname());
		g.setDescription(galleryForm.getDescription());
		g.setGallerytext(galleryForm.getGallerytext());

		g.setThumbheight(120);
		g.setThumbheight(160);

		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);
		
		String basepath = config.getBasepath();									
		String userGalleryDir = basepath + "users/" + user.getName() + "/galleries/" + galleryForm.getGalleryname();

		File f1 = new File(userGalleryDir);

		if (!f1.exists())
			f1.mkdir();

		userDAO.addGallery(user.getId(), g);
		
		ActionForward af = new ActionForward();
		af.setPath("/users/UserAction.do?do=showGalleries&user_id=" + user.getId());

		return af;

	}

	public ActionForward createGallery(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		return mapping.findForward("createGallery");

	}

	public ActionForward deleteGallery(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		final String login = (String) request.getSession().getAttribute("login");
		final UserDAO userDAO = DAOManager.getInstance().getUserDAO();
		final GalleryDAO galleryDAO = DAOManager.getInstance().getGalleryDAO();
		final Long galleryId = Long.valueOf(request.getParameter(Params.ID_GALLERY));

		Gallery gallery = (Gallery) galleryDAO.findByID(galleryId);

		User user = (User) userDAO.findByName(login);

		if (login != null && login.equals(user.getName()) && galleryId != null) {

			userDAO.removeGallery(user.getId(), galleryId);

			Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);			
			String basepath = config.getBasepath();									

			String userGalleryDir = basepath + "users/" + user.getName() + "/galleries/";

			File f = new File(userGalleryDir + gallery.getGalleryname());

			try {
				FileUtils.deleteDirectory(f);
			}
			catch (IOException e) {
				e.printStackTrace();
			}

		}

		ActionForward af = new ActionForward();
		af.setPath("/users/UserAction.do?do=showGalleries&user_id=" + user.getId());

		return af;

	}

	public ActionForward editGallery(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		GalleryForm galleryForm = (GalleryForm) form;

		final Long galleryId = Long.valueOf(request.getParameter(Params.ID_GALLERY));
		final String login = (String) request.getSession().getAttribute("login");
		final UserDAO userDAO = DAOManager.getInstance().getUserDAO();

		User user = (User) userDAO.findByName(login);

		if (login != null && login.equals(user.getName()) && galleryId != null) {

			Gallery g = (Gallery) DAOManager.getInstance().getGalleryDAO().findByID(galleryId);

			galleryForm.setDescription(g.getDescription());
			galleryForm.setGalleryname(g.getGalleryname());
			galleryForm.setGallerytext(g.getGallerytext());
			galleryForm.setId(g.getId());
			galleryForm.setImages(g.getImages());

		}
		else {
			ActionForward af = new ActionForward();
			af.setPath("/users/UserAction.do?do=showGalleries&user_id=" + user.getId());

			return af;
		}

		return mapping.findForward("editGallery");

	}

	public ActionForward saveGallery(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		GalleryForm galleryForm = (GalleryForm) form;

		final Long galleryId = galleryForm.getId();
		final String login = (String) request.getSession().getAttribute("login");
		final UserDAO userDAO = DAOManager.getInstance().getUserDAO();

		User user = (User) userDAO.findByName(login);

		if (login != null && login.equals(user.getName()) && galleryId != null) {

			Gallery g = (Gallery) DAOManager.getInstance().getGalleryDAO().findByID(galleryId);

			g.setDescription(galleryForm.getDescription());
			g.setGalleryname(galleryForm.getGalleryname());
			g.setGallerytext(galleryForm.getGallerytext());

			DAOManager.getInstance().getGalleryDAO().update(g);

		}

		ActionForward af = new ActionForward();
		af.setPath("/users/UserAction.do?do=showGalleries&user_id=" + user.getId());

		return af;

	}

	public ActionForward addImage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		GalleryForm galleryForm = (GalleryForm) form;

		final Long galleryId = Long.valueOf(request.getParameter(Params.ID_GALLERY));

		galleryForm.setId(galleryId);

		return mapping.findForward("addImage");

	}

	public ActionForward uploadImage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		GalleryForm galleryForm = (GalleryForm) form;

		final Long galleryId = galleryForm.getId();
		final String login = (String) request.getSession().getAttribute("login");
		final UserDAO userDAO = DAOManager.getInstance().getUserDAO();

		User user = (User) userDAO.findByName(login);

		if (login != null && login.equals(user.getName()) && galleryId != null) {

			try {
				Gallery gallery = (Gallery) DAOManager.getInstance().getGalleryDAO().findByID(galleryId);

				FormFile file = galleryForm.getFile();

				InputStream stream = file.getInputStream();

				// write the file to the file specified

				Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);				
				String basepath = config.getBasepath();									

				final String userGalleryDir = basepath + "users/" + user.getName() + "/galleries/";
				final String imageOutputPath = userGalleryDir + gallery.getGalleryname() + "/" + file.getFileName();

				OutputStream bos = new FileOutputStream(imageOutputPath);

				int bytesRead = 0;
				byte[] buffer = new byte[8192];

				while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
					bos.write(buffer, 0, bytesRead);
				}

				bos.close();
				log.debug("The file has been written to \"" + imageOutputPath + "\"");

				// close the stream

				stream.close();
				file.destroy();

				Image image = new Image();

				image.setFilename(file.getFileName());
				image.setName(galleryForm.getImageName());
				image.setLastmodified(new Date().getTime());
				image.setDescription(galleryForm.getImageDescription());

				DAOManager.getInstance().getGalleryDAO().addImage(galleryId, image);

			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}

		}

		ActionForward af = new ActionForward();
		af.setPath("/users/GalleryAction.do?do=editGallery&gallery_id=" + galleryId);

		return af;

	}

	public ActionForward deleteImage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		final Long imageId = Long.valueOf(request.getParameter("image_id"));
		final Long galleryId = Long.valueOf(request.getParameter("gallery_id"));
		final String login = (String) request.getSession().getAttribute("login");
		final UserDAO userDAO = DAOManager.getInstance().getUserDAO();

		Image image = (Image) DAOManager.getInstance().getGImageDAO().findByID(imageId);
		Gallery gallery = (Gallery) DAOManager.getInstance().getGalleryDAO().findByID(galleryId);
		User user = (User) userDAO.findByName(login);

		if (login != null && login.equals(user.getName()) && imageId != null) {

			Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);			
			String basepath = config.getBasepath();									

			final String userGalleryDir = basepath + "users/" + user.getName() + "/galleries/";
			final String imagePath = userGalleryDir + gallery.getGalleryname() + "/" + image.getFilename();

			File f = new File(imagePath);

			if (f.exists())
				f.delete();

			DAOManager.getInstance().getGalleryDAO().removeImage(galleryId, imageId);

		}

		ActionForward af = new ActionForward();
		af.setPath("/users/GalleryAction.do?do=editGallery&gallery_id=" + galleryId);

		return af;

	}

	public ActionForward showGallery(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		GalleryForm galleryForm = (GalleryForm) form;

		final Long userId = Long.valueOf(request.getParameter("user_id"));
		final Long galleryId = Long.valueOf(request.getParameter("gallery_id"));

		Gallery gallery = (Gallery) DAOManager.getInstance().getGalleryDAO().findByID(galleryId);
		User user = (User) DAOManager.getInstance().getUserDAO().findByID(userId);

		galleryForm.setImages(gallery.getImages());
		galleryForm.setUser(user);
		galleryForm.setGalleryname(gallery.getGalleryname());

		return mapping.findForward("showGallery");
	}

}
