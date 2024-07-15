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
package org.pmedv.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.pmedv.cms.common.GlobalForwards;
import org.pmedv.cms.common.Params;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.forms.UsergroupAddForm;
import org.pmedv.pojos.User;
 

/**
 * This action adds a group to the specified user
 * 
 * @author Matthias Pueski
 *
 */

@SuppressWarnings("unchecked")

public class UsergroupAddAction extends AbstractPermissiveAction
{ 
	public UsergroupAddAction() {
		super();
		log.debug("Executing "+this.getClass());
		setName("menu.user");		
	}
	
  /**	
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
	
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) {

		if (!isAllowedToProcess(request, getName())) return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);
		
		// Long user_id = Long.parseLong(request.getParameter(Params.ID_USER));
		
		HttpSession session = request.getSession();
		
		Long user_id = (Long)session.getAttribute(Params.ID_USER);
		
		log.debug("Adding groups to user with id : "+user_id);
		
		UsergroupAddForm usergroupAddForm = (UsergroupAddForm) form;

	    List usergroups =  DAOManager.getInstance().getUsergroupDAO().findAllItems();
	    
	    User user = (User)DAOManager.getInstance().getUserDAO().findByID(user_id);
	    
	    usergroupAddForm.setUsergroups(usergroups);
	    usergroupAddForm.setUser(user);
			    
	    return mapping.findForward(GlobalForwards.ADD_USERGROUP);
	}
}

