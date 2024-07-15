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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONSerializer;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.pmedv.cms.common.GlobalForwards;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.forms.UserListForm;
import org.pmedv.pojos.User;
import org.pmedv.pojos.Usergroup;
import org.pmedv.pojos.UsergroupProxy;
 
/**
 * This class is responsible for delivering userlists
 * 
 * @author Matthias Pueski
 *
 */

public class UserListAction extends ObjectListAction {
	
	public UserListAction() {
		super("menu.user");
		log.debug("Executing "+this.getClass());		
	}

	/** 
	 * loads a list of users from the db and stores them in the request
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	@SuppressWarnings("unchecked")
	public ActionForward list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		if (!isAllowedToProcess(request, getName())) return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);
		
		UserListForm userListForm = (UserListForm) form;

		List<?> users =  null; 
			
		String name = userListForm.getName();

		if (name != null)	
			users = DAOManager.getInstance().getUserDAO().findElementsByName(name);
		else
			users = DAOManager.getInstance().getUserDAO().findAllItems(); 
		
		userListForm.setUsers((List<User>) users);
		
		return mapping.findForward(GlobalForwards.SHOW_USER_LIST);
	}
	
	public ActionForward getJSON(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		if (!isAllowedToProcess(request, getName())) return null;
		
		writeJSONList(User.class, request,response);
	
		return null;
	}
	
	public ActionForward getJSONWithoutMeta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
				
		if (!isAllowedToProcess(request, getName())) return null;
		
		writeJSONList(User.class,false, request,response);
	
		return null;
	}

	public ActionForward getUsergroupsAsJSON(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String login = (String)request.getSession().getAttribute("login");
		
		ArrayList<UsergroupProxy> groups = new ArrayList<UsergroupProxy>();
		
		if (login != null) {
		
			User user = (User)DAOManager.getInstance().getUserDAO().findByName(login);
			
			if (user != null) {
				groups = new ArrayList<UsergroupProxy>();
				for (Usergroup group : user.getGroups()) {
					groups.add(new UsergroupProxy(group));
				}				
			}
			
		}
		
		try {
			PrintWriter out = response.getWriter();
			response.setContentType("text/plain");
			out.print(JSONSerializer.toJSON(groups).toString());
		}
		catch (IOException e) {
			e.printStackTrace();
		}				

		
		return null;
	}

}
