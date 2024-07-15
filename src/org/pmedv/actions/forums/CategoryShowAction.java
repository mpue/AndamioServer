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
import org.pmedv.actions.AbstractPermissiveDispatchAction;
import org.pmedv.cms.common.GlobalForwards;
import org.pmedv.cms.common.Params;
import org.pmedv.cms.daos.CategoryDAO;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.cms.daos.ForumDAO;
import org.pmedv.forms.forums.CategoryShowForm;
import org.pmedv.pojos.forums.Category;

/**
 * @author mpue
 *
 */
public class CategoryShowAction extends AbstractPermissiveDispatchAction {

	/* (non-Javadoc)
	 * @see org.pmedv.actions.IPermissiveDispatchAction#delete(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		ForumDAO forumDAO = DAOManager.getInstance().getForumDAO();
				
		Long category_id  = Long.parseLong(request.getParameter(Params.ID_CATEGORY));
		Long forum_id     = Long.parseLong(request.getParameter(Params.ID_FORUM));
				
		forumDAO.removeCategory(forum_id, category_id);
		
		ActionForward af = new ActionForward();
        af.setPath("/admin/ShowForum.do?do=edit&forum_id="+forum_id);
		
		return af;
	}

	/* (non-Javadoc)
	 * @see org.pmedv.actions.IPermissiveDispatchAction#edit(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward edit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		CategoryShowForm categoryShowForm = (CategoryShowForm) form;
		
		Long category_id  = Long.parseLong(request.getParameter("category_id"));
		Category category = (Category) DAOManager.getInstance().getCategoryDAO().findByID(category_id.longValue());
		categoryShowForm.setCategory(category);
		
		return mapping.findForward(GlobalForwards.SHOW_CATEGORY_DETAILS);
	}

	/* (non-Javadoc)
	 * @see org.pmedv.actions.IPermissiveDispatchAction#save(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		CategoryShowForm categoryShowForm = (CategoryShowForm) form;
		
		Long category_id  = Long.parseLong(request.getParameter(Params.ID));
		Long forum_id     = Long.parseLong(request.getParameter(Params.ID_FORUM));
		
		CategoryDAO categoryDAO  = DAOManager.getInstance().getCategoryDAO();
		Category category = (Category) categoryDAO.findByID(category_id);
		
		category.setDescription(categoryShowForm.getDescription());
		category.setName(categoryShowForm.getName());
		
		categoryDAO.update(category);
		
		ActionForward af = new ActionForward();
        af.setPath("/admin/ShowForum.do?do=edit&forum_id="+forum_id);
		
		return af;

	}

}
