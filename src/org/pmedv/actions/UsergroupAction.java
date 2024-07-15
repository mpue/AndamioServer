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

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.pmedv.cms.common.GlobalForwards;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.forms.UsergroupForm;
import org.pmedv.pojos.Usergroup;
 

/**
 * This action creates a new usergroup
 * 
 * @author Matthias Pueski
 *
 */
public class UsergroupAction  extends AbstractPermissiveAction {
	
	public UsergroupAction() {
		super();
		log.debug("Executing "+this.getClass());
		setName("menu.usergroups");		
	}

	public ActionForward execute(
			ActionMapping mapping,
		    ActionForm form,
		    HttpServletRequest request,
		    HttpServletResponse response) throws Exception{
		
		boolean created = false;
		
		if (!isAllowedToProcess(request, getName())) return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);
		
		UsergroupForm groupForm = (UsergroupForm) form;
		Usergroup usergroup = new Usergroup();
		
		usergroup.setDescription(groupForm.getDescription());
		usergroup.setName(groupForm.getName());
		
		if (DAOManager.getInstance().getUsergroupDAO().findByName(usergroup.getName()) == null) {		
			DAOManager.getInstance().getUsergroupDAO().createAndStore( usergroup );
			created = true;
		}
			
		if (request.getParameter("async") != null) {

        	PrintWriter out;
			out = response.getWriter();
			
			if (created) {
        	    out.print("{\"success\":true,\"message\":\"Gruppe erfolgreich angelegt.\"}");			       	    
			}
			else {
        	    out.print("{\"failure\":true,\"message\":\"Gruppe existiert bereits.\"}");				
			}
			
			out.flush();	
			
			return null;
		}
			
		else	
			return mapping.findForward(GlobalForwards.CREATE_USERGROUP_SUCCESS);
		
	}
	
}


