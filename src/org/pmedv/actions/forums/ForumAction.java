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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.pmedv.actions.AbstractPermissiveAction;
import org.pmedv.cms.common.GlobalForwards;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.cms.daos.ForumDAO;
import org.pmedv.forms.forums.ForumForm;
import org.pmedv.pojos.forums.Forum;

public class ForumAction  extends AbstractPermissiveAction {

	public ForumAction() {
		super();
		log.debug("Executing "+this.getClass());
		setName("menu.forums");		
	}
	
	/**
	 * Creates a new and fresh forum
	 */
	
	public ActionForward execute(
			ActionMapping mapping,
		    ActionForm form,
		    HttpServletRequest request,
		    HttpServletResponse response) throws Exception{
			      
		if (!isAllowedToProcess(request, getName())) return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);
		
		ForumForm forumForm = (ForumForm) form;
		
		ForumDAO forumDAO = DAOManager.getInstance().getForumDAO();
		Forum forum = new Forum();
		
		forum.setDescription(forumForm.getDescription());
		forum.setName(forumForm.getName());
		
		try {
			forum.setPosition(forumDAO.getMaxPos()+1);
		}
		catch (NullPointerException n) {
			forum.setPosition(0);
		}
		
		forumDAO.createAndStore( forum );
			
		return mapping.findForward(GlobalForwards.CREATE_FORUM_SUCCESS);
		
	}
	
}

