/**

	Weberknecht AndamioManager - Open Source Content Management
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
package org.pmedv.actions.userpages;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.pmedv.beans.objects.DataType;
import org.pmedv.cms.common.Params;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.cms.daos.UserDAO;
import org.pmedv.forms.UserProfileForm;
import org.pmedv.pojos.Attribute;
import org.pmedv.pojos.Avatar;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.Gallery;
import org.pmedv.pojos.User;
import org.pmedv.pojos.UserComment;
import org.pmedv.pojos.UserProfile;
import org.pmedv.session.UserSession;

public class UserProfileAction extends DispatchAction {

	private static final ResourceBundle	resources	= ResourceBundle.getBundle("MessageResources");

	public ActionForward showProfile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		final UserProfileForm profileForm = (UserProfileForm) form;
		final UserDAO userDAO = DAOManager.getInstance().getUserDAO();

		final int userId = Integer.valueOf(request.getParameter(Params.ID_USER)).intValue();
		final User user = (User) userDAO.findByID(userId);

		/**
		 * It might happen, that a user has no profile. System users, that take
		 * not part of the public portal pages (such as the admin user) have no
		 * user profile by default.
		 */

		if (user.getUserProfile() == null) {

			UserProfile profile = new UserProfile();

			profile.setPageContent(resources.getString("error.user.noprofile"));
			profile.setUser(user);
			user.setUserProfile(profile);

		}

		profileForm.setUser(user);

		log.info("User has " + user.getUserComments().size() + " comments.");

		ArrayList<UserComment> comments = new ArrayList<UserComment>();

		for (UserComment comment : user.getUserComments()) {

			log.info("Adding comment " + comment.getText());

			comments.add(comment);
		}

		Collections.sort(comments);
		profileForm.setComments(comments);

		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		
		for (Attribute a : user.getAttributes()) {		
			attributes.add(a);			
		}
		
		Collections.sort(attributes);
		profileForm.setAttributes(attributes);
		
		ArrayList<Avatar> avatars = new ArrayList<Avatar>();
		
		for (Avatar a : user.getAvatars()) {
			avatars.add(a);
		}
		
		profileForm.setAvatars(avatars);
		
		request.getSession().setAttribute("editing", false);

		String login = (String) request.getSession().getAttribute("login");

		if (login != null && login.equals(user.getName()))
			request.getSession().setAttribute("owner", true);
		else
			request.getSession().setAttribute("owner", false);



		return mapping.findForward("userProfile");

	}

	public ActionForward editProfile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		final UserProfileForm profileForm = (UserProfileForm) form;

		/**
		 * This action is called when a user adds a comment
		 */

		final UserDAO userDAO = DAOManager.getInstance().getUserDAO();

		final Long userId = profileForm.getId();
		final User user = (User) userDAO.findByID(userId);

		/**
		 * It might happen, that a user has no profile. System users, that take
		 * not part of the public portal pages (such as the admin user) have no
		 * user profile by default.
		 */

		if (user.getUserProfile() == null) {

			UserProfile profile = new UserProfile();

			profile.setPageContent(resources.getString("error.user.noprofile"));
			profile.setUser(user);
			user.setUserProfile(profile);

		}

		profileForm.setUser(user);

		String login = (String) request.getSession().getAttribute("login");

		if (login.equals(user.getName()))
			request.getSession().setAttribute("editing", true);
		
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		
		for (Attribute a : user.getAttributes()) {		
			attributes.add(a);			
		}
		
		Collections.sort(attributes);
		profileForm.setAttributes(attributes);


		return mapping.findForward("editProfile");
	}

	public ActionForward saveProfile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		final UserProfileForm profileForm = (UserProfileForm) form;
		final UserDAO userDAO = DAOManager.getInstance().getUserDAO();

		final int userId = Integer.valueOf(request.getParameter(Params.ID)).intValue();
		final User user = (User) userDAO.findByID(userId);

		String login = (String) request.getSession().getAttribute("login");
		
		ArrayList<Attribute> booleanAttributes = new ArrayList<Attribute>(); 

		if (login.equals(user.getName())) {

	        for (Enumeration<?> e = request.getParameterNames();e.hasMoreElements();) {

	        	String param = (String)e.nextElement();

	        	if (param.startsWith("key.")) {
	        		
	        		for (Attribute a : user.getAttributes()) {
	        			
	        			if (a.getKey().equals(param)) {

	        				/**
	        				 * If the attribute is of boolean type, put it into the list of boolean attributes
	        				 * Since a html checkbox only send a value if it is checked, we collect the attributes
	        				 * here in order to turn all non checked values off.
	        				 */
	        				
	        				if (a.getDataType().equals(DataType.BOOLEAN)) {
	        					booleanAttributes.add(a);	        					
	        				}
	        				else {
	        					a.setValue(request.getParameter(param));	
	        				}
	        				
	        			}
	        			
	        		}
	        		
	        	}
	        	
	        }
			
	        /**
	         * Iterate again through all user attributes, if the list contains one
	         * of the user attributes it must be set to true. If it is not in the list, 
	         * the according checkbox has not been checked and the value must be false.
	         */
	        
	        for (Attribute a : user.getAttributes()) {
	        	
	        	if (a.getDataType().equals(DataType.BOOLEAN)) {
	        		if (booleanAttributes.contains(a))
	        			a.setValue("true");
	        		else
	        			a.setValue("false");
	        	}
	        	
	        }
	        
			user.getUserProfile().setPageContent(profileForm.getPageContent());
			user.setLand(profileForm.getCountry());
			user.setOrt(profileForm.getCity());
			userDAO.update(user);

			profileForm.setUser(user);
		}

		try {
			response.sendRedirect("UserAction.do?do=showProfile&user_id=" + userId);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}
	
	

	public ActionForward showGalleries(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		final UserProfileForm profileForm = (UserProfileForm) form;
		final UserDAO userDAO = DAOManager.getInstance().getUserDAO();

		long userId = Long.valueOf(request.getParameter("user_id"));

		User user = (User) userDAO.findByID(userId);

		ArrayList<Gallery> galleries = new ArrayList<Gallery>();

		for (Gallery gallery : user.getUserGalleries()) {
			log.info("Adding gallery : " + gallery.getGalleryname());
			galleries.add(gallery);
		}

		Collections.sort(galleries);

		profileForm.setGalleries(galleries);
		profileForm.setUser(user);

		String login = (String) request.getSession().getAttribute("login");

		if (login != null && login.equals(user.getName()))
			request.getSession().setAttribute("owner", true);
		else
			request.getSession().setAttribute("owner", false);

		return mapping.findForward("showGalleries");

	}
	
	public ActionForward addAvatar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		UserProfileForm profileForm = (UserProfileForm) form;

		UserSession session = new UserSession(false,request);
		session.getAttributes();
		String username = session.getLogin();
		
		if (username != null) {
			
			User user = (User)DAOManager.getInstance().getUserDAO().findByName(username);
			
			if (user != null) {
				profileForm.setUser(user);			
			}

		}

		return mapping.findForward("addAvatar");

	}

	public ActionForward uploadAvatar(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		UserProfileForm profileForm = (UserProfileForm) form;

		final Long userId = profileForm.getId();
		final String login = (String) request.getSession().getAttribute("login");
		final UserDAO userDAO = DAOManager.getInstance().getUserDAO();

		User user = (User) userDAO.findByName(login);

		if (login != null && login.equals(user.getName()) && userId != null) {

			try {

				FormFile file = profileForm.getFile();

				InputStream stream = file.getInputStream();

				// write the file to the file specified

				Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);				
				String basepath = config.getBasepath();									

				final String avatarDir = basepath + "users/" + user.getName() + "/avatars/";
				final String imageOutputPath = avatarDir + file.getFileName();

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

				Avatar a = new Avatar();

				a.setFilename(file.getFileName());
				a.setDescription(profileForm.getDescription());
				
				if (profileForm.isDefaultImage()) { 
					
					a.setDefaultImage(true);

					for (Avatar av : user.getAvatars()) {
						av.setDefaultImage(false);
					}
					
				}
				
				DAOManager.getInstance().getUserDAO().update(user);
				DAOManager.getInstance().getUserDAO().addAvatar(user.getId(), a);

			}
			catch (FileNotFoundException e) {
				log.error("file not found.");
			}
			catch (IOException e) {
				log.error("I/O exception occured.");
			}

		}

		try {
			response.sendRedirect("UserAction.do?do=showProfile&user_id=" + userId);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;

	}
}
