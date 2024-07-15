/**
 * WeberknechtCMS - Open Source Content Management
 * 
 * Written and maintained by Matthias Pueski
 * 
 * Copyright (c) 2003-2011 Matthias Pueski
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 */
package org.pmedv.actions.forums;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.pmedv.cms.common.GlobalForwards;
import org.pmedv.cms.common.Params;
import org.pmedv.cms.daos.CategoryDAO;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.cms.daos.ThreadDAO;
import org.pmedv.forms.forums.ForumMainpageForm;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.User;
import org.pmedv.pojos.Usergroup;
import org.pmedv.pojos.forums.Attachment;
import org.pmedv.pojos.forums.Category;
import org.pmedv.pojos.forums.Forum;
import org.pmedv.pojos.forums.Posting;
import org.pmedv.session.UserSession;

/**
 * <p>
 * This is the controller contains all public forum actions.
 * </p>
 * <p>
 * From here the following actions are triggered:
 * </p>
 * <p>
 * <ul>
 * <li>showForums - shows the main page of the forums</li>
 * <li>markAllForumsRead - marks all forums as read for the current user.</li>
 * <li>showCategory - show all subforums for a specific category</li>
 * <li>showUnreadThreads - shows a list of all unread threads for the current user</li>
 * <li>showThread - show a specific thread with all posts</li>
 * <li>editPosting - edit a desired posting</li>
 * <li>savePosting - save the edited posting</li>
 * <li>deletePosting - deletes a selected posting</li>
 * 
 * @author Matthias Pueski
 * 
 */
public class ForumMainpageAction extends DispatchAction {

	private ResourceBundle resources = ResourceBundle.getBundle("MessageResources");
	
	/**
	 * Shows a brief overview of all forums
	 */
	public ActionForward showForums(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		getForums(form, request);
		return mapping.findForward(GlobalForwards.SHOW_FORUMS);
	}

	private void getForums(ActionForm form, HttpServletRequest request) {
		ForumMainpageForm forumForm = (ForumMainpageForm) form;		
		UserSession session = new UserSession(false, request);
		session.getAttributes();
		String username = session.getLogin();
		List<?> forums = DAOManager.getInstance().getForumDAO().findAllItems();		
		filterForumsByPermission(username, forums);
		
		if (request.getSession().getAttribute("lastActivity") == null)
			request.getSession().setAttribute("lastActivity", resources.getString("never"));		
		
		forumForm.setForums(forums);
	}

	/**
	 * Marks all forums read for the current user
	 */
	public ActionForward markAllForumsRead(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		UserSession session = new UserSession(false, request);
		session.getAttributes();
		String username = session.getLogin();

		if (username != null) {

			User user = (User) DAOManager.getInstance().getUserDAO().findByName(username);

			if (user != null) {
				// track last activity
				user.setLastActivity(new Date());
				DAOManager.getInstance().getUserDAO().update(user);
			}
		}

		getForums(form, request);

		return mapping.findForward(GlobalForwards.SHOW_FORUMS);
	}

	/**
	 * Shows a brief overview of a selected category
	 */
	public ActionForward showCategory(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		long category_id = Long.parseLong(request.getParameter(Params.ID_CATEGORY));
		
		ForumMainpageForm forumForm = (ForumMainpageForm) form;
		CategoryDAO categoryDAO = DAOManager.getInstance().getCategoryDAO();
		Category category = (Category) categoryDAO.findByID(category_id);
		forumForm.setCategory(category);
		forumForm.setThreads(category.getThreads());

		return mapping.findForward(GlobalForwards.SHOW_CATEGORY);

	}

	/**
	 * Shows a list of threads containing post unread by the current user
	 */
	public ActionForward showUnreadThreads(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		ForumMainpageForm forumForm = (ForumMainpageForm) form;
		ThreadDAO threadDAO = DAOManager.getInstance().getThreadDAO();

		UserSession session = new UserSession(false, request);
		session.getAttributes();
		String username = session.getLogin();

		if (username != null) {

			User user = (User) DAOManager.getInstance().getUserDAO().findByName(username);
			List<?> threads = threadDAO.getUnreadThreads(user);
			forumForm.setUnreadThreads(threads);
		}

		return mapping.findForward("showUnreadThreads");

	}

	/**
	 * Shows all postings of a specific thread
	 */
	public ActionForward showThread(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		ForumMainpageForm forumForm = (ForumMainpageForm) form;
		ThreadDAO threadDAO = DAOManager.getInstance().getThreadDAO();

		long thread_id = Long.parseLong(request.getParameter(Params.ID_THREAD));

		org.pmedv.pojos.forums.Thread thread = (org.pmedv.pojos.forums.Thread) threadDAO.findByID(thread_id);
		forumForm.setThread(thread);

		return mapping.findForward(GlobalForwards.SHOW_THREAD);
	}

	/**
	 * Edit a selected posting
	 */
	public ActionForward editPosting(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		UserSession session = new UserSession(false, request);
		session.getAttributes();
		String username = session.getLogin();

		if (username != null) {
			ForumMainpageForm forumForm = (ForumMainpageForm) form;

			long id = Long.parseLong(request.getParameter(Params.ID));
			Posting p = (Posting) DAOManager.getInstance().getPostingDAO().findByID(id);
			
			if (p != null && p.getUser().getName().equals(username)) {

				forumForm.setPostingtext(p.getPostingtext());
				forumForm.setTitle(p.getTitle());
				forumForm.setId(p.getId());

				return mapping.findForward("editPosting");
			}
			
		}
		getForums(form, request);
		return mapping.findForward(GlobalForwards.SHOW_FORUMS);
	}

	/**
	 * Save an edited forum
	 */
	public ActionForward savePosting(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		UserSession session = new UserSession(false, request);
		session.getAttributes();
		String username = session.getLogin();

		if (username != null) {
		
			ForumMainpageForm forumForm = (ForumMainpageForm) form;
	
			long id = Long.parseLong(request.getParameter(Params.ID));
			Posting p = (Posting) DAOManager.getInstance().getPostingDAO().findByID(id);
	
			if (p != null && p.getUser().getName().equals(username)) {
	
				p.setPostingtext(forumForm.getPostingtext());
				p.setLastChange(new Date());
				p.setLastChangeBy((String) request.getSession().getAttribute("login"));
				DAOManager.getInstance().getPostingDAO().update(p);
	
				response.sendRedirect("Mainpage.do?do=showThread&thread_id=" + p.getThread().getId());
	
			}
		}
		
		return null;

	}

	/**
	 * Deletes a posting from a thread, currently this can only be done by osers of the admin group
	 */
	public ActionForward deletePosting(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		UserSession session = new UserSession(false, request);
		session.getAttributes();
		String username = session.getLogin();

		if (username != null) {
		
			ForumMainpageForm forumForm = (ForumMainpageForm) form;
	
			long id = Long.parseLong(request.getParameter(Params.ID));
			Posting p = (Posting) DAOManager.getInstance().getPostingDAO().findByID(id);
	
			if (p != null && p.getUser().getName().equals(username) || username.equals("admin")) {
				
				Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1L);
				String directory = "attachments/";
				
				for (Attachment a : p.getAttachments()) {
					File f = new File(config.getBasepath() + directory + a.getFilename());
					if (f.exists() && f != null) {
						if (!f.delete()) {
							log.error("could not delete "+f.getAbsolutePath());
						}
						else {
							log.info("deleted "+f.getAbsolutePath());
						}
					}
				}
				
				DAOManager.getInstance().getPostingDAO().delete(p);
				response.sendRedirect("Mainpage.do?do=showThread&thread_id=" + p.getThread().getId());
	
			}
		}
		
		return null;

	}

	public ActionForward showMemberList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		UserSession session = new UserSession(false, request);
		session.getAttributes();
		String username = session.getLogin();

		if (username != null) {
			ForumMainpageForm forumForm = (ForumMainpageForm) form;		
			forumForm.setUsers(DAOManager.getInstance().getUserDAO().findAllItems());			
			return mapping.findForward("memberList");			
		}
		return null;
	}

	public ActionForward getAttachment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		UserSession session = new UserSession(false, request);
		session.getAttributes();
		String username = session.getLogin();

		if (username != null) {

			Long attachmentId = Long.valueOf(request.getParameter("id"));
			
			if (attachmentId != null) {			
				Attachment a = (Attachment)DAOManager.getInstance().getAttachmentDAO().findByID(attachmentId);
				
				Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1L);
				String directory = "attachments/";			
				String filepath = config.getBasepath() + directory + a.getFilename();
				
				response.setHeader("Content-Disposition","attachment; filename="+a.getName()); 

				FileInputStream fis = new FileInputStream(filepath);
				ServletOutputStream sos = response.getOutputStream();			
				copyStream(fis, sos);
				sos.flush();
			}
			
		}
		else {
			return mapping.findForward("missingLogin");
		}
		return null;
		
	}
	
	/**
	 * Performs a prototype like autocomplete search for user search. This search simply checks
	 * the user name and returns a list if any matches are being found.
	 */
	public ActionForward autoComplete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		UserSession session = new UserSession(false, request);
		session.getAttributes();
		String username = session.getLogin();

		if (username != null) {
			Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1L);
			
			String query = request.getParameter("search");
			StringBuffer result = new StringBuffer();
			
			List<?> users = DAOManager.getInstance().getUserDAO().findAllItems();
			
			if (query != null) {
				
				log.info("Found "+users.size()+" users.");
				result.append("<ul>");			
				for (Object o : users) {				
					User u = (User)o;
					if (u.getName().contains(query))
						result.append("<li>"+u.getName()+"</li>");				
				}
				result.append("</ul>");
				
			}
			
			try {
				PrintWriter out = response.getWriter();
				out.write(result.toString());
			}
			catch (IOException e) {
				log.error("Could not write to servlet output.");
			}
			
		}
		
		return null;
	}	
	
	private void copyStream(InputStream fis, OutputStream fos) {
		try {
			byte[] buffer = new byte[0xFFFF];
			for (int len; (len = fis.read(buffer)) != -1;)
				fos.write(buffer, 0, len);
		}
		catch (IOException e) {
			System.err.println(e);
		}
		finally {
			if (fis != null) {
				try {
					fis.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void filterForumsByPermission(String username, List<?> forums) {
		if (username != null) {

			User user = (User) DAOManager.getInstance().getUserDAO().findByName(username);

			if (user != null) {
				for (Iterator<?> it = forums.iterator();it.hasNext();) {
					Forum f = (Forum)it.next();
					
					boolean remove = true;
					
					if (!f.isRestricted()) {
						remove = false;
					}
					else {
						// if the user is at least member of one group of the forums, 
						// don't remove the forum from the list.
						for (Iterator<Usergroup> groupIt = user.getGroups().iterator();groupIt.hasNext();) {
							if (f.getGroups().contains(groupIt.next()))
								remove = false;
						}						
					}
					
					if (remove)
						it.remove();
				}
			}
		}
		else { // anonymous user has no acces to any restricted forum
			for (Iterator<?> it = forums.iterator();it.hasNext();) {
				Forum f = (Forum)it.next();
				if (f.isRestricted())
					it.remove();
			}			
			
		}
	}	
}
