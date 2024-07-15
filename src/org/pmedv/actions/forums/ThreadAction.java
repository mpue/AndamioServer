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
package org.pmedv.actions.forums;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.pmedv.cms.common.GlobalForwards;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.cms.daos.ForumDAO;
import org.pmedv.cms.daos.ThreadDAO;
import org.pmedv.forms.forums.ThreadForm;
import org.pmedv.pojos.User;
import org.pmedv.pojos.forums.Attachment;
import org.pmedv.pojos.forums.Posting;
import org.pmedv.session.UserSession;


public class ThreadAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
				
		ThreadForm threadForm = (ThreadForm)form;
		
		Long category_id = null;
		
		if (request.getParameter("category_id") != null && !request.getParameter("category_id").equals("null")) // fucking tomcat fucks, wtf???
			category_id = Long.parseLong(request.getParameter("category_id"));
		else
			category_id = (Long)request.getSession().getAttribute("category_id");
		
		if (category_id == null) return mapping.findForward(GlobalForwards.SHOW_FORUMS);
		
		Date currentDate = new Date();
			
		ForumDAO  forumDAO  = DAOManager.getInstance().getForumDAO();
		ThreadDAO threadDAO = DAOManager.getInstance().getThreadDAO();
		
		UserSession session = new UserSession(false,request);
		session.getAttributes();
		String username = session.getLogin();
		
		org.pmedv.pojos.forums.Thread newThread = null;
		
		if (username != null) {
			
				User user = (User)DAOManager.getInstance().getUserDAO().findByName(username);
				
				if (user != null) {
					
					org.pmedv.pojos.forums.Thread thread = new org.pmedv.pojos.forums.Thread();
					
					thread.setName("new");
					thread.setDate(currentDate);
					thread.setUser(user);
					
					forumDAO.addThread(category_id, thread);
				
					newThread = (org.pmedv.pojos.forums.Thread)threadDAO.findByName("new");
					
					Posting posting = new Posting();
					
					posting.setPostingtext(threadForm.getPostingtext());
					posting.setTitle(threadForm.getName());
					posting.setDate(currentDate);
					posting.setUser(user);

					posting = forumDAO.addPosting(newThread.getId(), posting);
					newThread.setName(threadForm.getName());
					newThread.setLastPost(currentDate);
					threadDAO.update(newThread);
					
					user.setRanking(user.getRanking() + 1);
					DAOManager.getInstance().getUserDAO().update(user);
					
					if (request.getSession().getAttribute("attachments") != null) {
						ArrayList<Attachment> attachments = (ArrayList<Attachment>)request.getSession().getAttribute("attachments");
						for (Attachment att : attachments) {						
							DAOManager.getInstance().getPostingDAO().addAttachment(posting.getId(), att);
						}
					}
					// clear attachments from session					
					request.getSession().setAttribute("attachments", null);						

				}
			
		}
		
		ActionForward af = new ActionForward();
		af.setPath("/forum/Mainpage.do?do=showThread&thread_id="+newThread.getId());
		return af;

	}
	
	
}
