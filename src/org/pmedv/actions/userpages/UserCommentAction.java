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

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.pmedv.cms.daos.ContentDAO;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.cms.daos.UserDAO;
import org.pmedv.forms.UserCommentForm;
import org.pmedv.pojos.User;
import org.pmedv.pojos.UserComment;


public class UserCommentAction extends DispatchAction {

	public ActionForward addPageComment(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		final ContentDAO contentDAO = DAOManager.getInstance().getContentDAO();
		final UserDAO userDAO = DAOManager.getInstance().getUserDAO();
		
		String login = (String)request.getSession().getAttribute("login");			
		
		if (login != null) {
			
			User author = (User)userDAO.findByName(login);
			
			if (author == null) {
				return null;
			}
			
			String comment = request.getParameter("comment");
			Long contentId = Long.valueOf(request.getParameter("content_id"));
			
			if (comment != null && comment.length() > 1) {
				
				
				UserComment usercomment = new UserComment();
				
				usercomment.setText(comment);
				usercomment.setCreated(new Date());
				usercomment.setAuthor(author.getFirstName()+ " "+author.getLastName());
				
				contentDAO.addComment(contentId, usercomment);
				
			}
		}
		
		return null;

	}
	
	public ActionForward deletePageComment (
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		final UserDAO userDAO = DAOManager.getInstance().getUserDAO();
		final ContentDAO contentDAO = DAOManager.getInstance().getContentDAO();
		
		Long commentId = Long.valueOf(request.getParameter("comment_id"));
		Long contentId = Long.valueOf(request.getParameter("content_id"));
				
		String login = (String)request.getSession().getAttribute("login");
		
		User user = null;
		
		if (login != null) {
			
			user = (User)userDAO.findByName(login);

			if (user != null && user.getName().equals("admin")) {
				contentDAO.removeComment(contentId, commentId);
			}

		}
		
		return null;

	}	
	
	public ActionForward addComment(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) {

		final UserCommentForm commentForm = (UserCommentForm)form;		
		final UserDAO userDAO = DAOManager.getInstance().getUserDAO();
	
		if (commentForm.getCommentText() != null && commentForm.getCommentText().length() > 1) {
			
			String login = (String)request.getSession().getAttribute("login");			
			User author = (User)userDAO.findByName(login);
			
			UserComment comment = new UserComment();
			
			comment.setText(commentForm.getCommentText());
			comment.setCreated(new Date());
			comment.setAuthor(author.getName());
			
			userDAO.addComment(commentForm.getId(), comment);
			
		}

		try {
			response.sendRedirect("UserAction.do?do=showProfile&user_id="+commentForm.getId());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;

	}
	
	public ActionForward delete (
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		final UserDAO userDAO = DAOManager.getInstance().getUserDAO();
		
		Long commentId = Long.valueOf(request.getParameter("comment_id"));
		
		String login = (String)request.getSession().getAttribute("login");
		
		User user = null;
		
		if (login != null) {
			
			user = (User)userDAO.findByName(login);

			if (user != null)		
				userDAO.removeComment(user.getId(), commentId);

		}
			
		if (user != null)

			try {
				response.sendRedirect("UserAction.do?do=showProfile&user_id="+user.getId());
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		
		return null;

	}
	
	
}
