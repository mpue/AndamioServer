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

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.pmedv.cms.common.GlobalForwards;
import org.pmedv.cms.common.Params;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.forms.UsergroupShowForm;
import org.pmedv.pojos.Usergroup;
 

/**
 * This action is responsible for all the stuff that is needed
 * for usergroup management.
 * 
 * @author Matthias Pueski
 *
 */
public class UsergroupShowAction extends AbstractPermissiveDispatchAction
{
	public UsergroupShowAction() {
		super();
		log.debug("Executing "+this.getClass());
		setName("menu.usergroups");		
	}

	/** 
   * loads the usergroup from the db and passes it to the request
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
	
	public ActionForward edit(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		if (!isAllowedToProcess(request, getName())) return mapping.findForward("showAdminPanel");
			
	    UsergroupShowForm groupForm = (UsergroupShowForm) form;
	    
	    long usergroup_id = Integer.parseInt(request.getParameter(Params.ID_USERGROUP));
		
	    Usergroup group =  (Usergroup) DAOManager.getInstance().getUsergroupDAO().findByID(usergroup_id);
	    groupForm.setUsergroup(group);
	    
	    return mapping.findForward(GlobalForwards.SHOW_USERGROUP_DETAILS);
	}
  
	/** 
	 * Deletes the selected usergroup
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	
	public ActionForward delete(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		if (!isAllowedToProcess(request, getName())) return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);

		long usergroup_id = Integer.parseInt(request.getParameter(Params.ID_USERGROUP));
				    		    
	    Usergroup group = (Usergroup)DAOManager.getInstance().getUsergroupDAO().findByID(usergroup_id);			    
	    DAOManager.getInstance().getUsergroupDAO().delete(group);
			    		
		if (request.getParameter("async") != null) {
			return null;
		}
	    
		return mapping.findForward(GlobalForwards.SHOW_USERGROUP_LIST);
	    
	}
	
	/**
	 * Updates the usergroup from a JSON String
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * 
	 * @return
	 */
	public ActionForward JSONUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		log.debug("JSONUpdate");
		log.debug("received JSON : "+request.getParameter("data"));
				
		String data = (String)request.getParameter("data");
		
		JsonConfig jsonConfig = new JsonConfig();  
		jsonConfig.setRootClass( Usergroup.class );  
		JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON( data );  
		List<?> output = (List<?>) JSONSerializer.toJava( jsonArray,jsonConfig );  
		
		for (Iterator<?> it = output.iterator();it.hasNext();) {
			Usergroup currentGroup = (Usergroup)it.next();
			
			if (currentGroup.isNewRecord()) {
				DAOManager.getInstance().getUsergroupDAO().createAndStore(currentGroup);
			}
			else {
				Usergroup group = (Usergroup) DAOManager.getInstance().getUsergroupDAO().findByID(currentGroup.getId());
				
				group.setName(currentGroup.getName());
				group.setDescription(currentGroup.getDescription());
				
				DAOManager.getInstance().getUsergroupDAO().update(group);
			}
		}
		
		return null;
	}
	
	/**
	 * Saves the modified usergroup
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
  
	public ActionForward save(
			ActionMapping mapping, 
			ActionForm form,
			HttpServletRequest request, 
			HttpServletResponse response) {
		
		if (!isAllowedToProcess(request, getName())) return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);

	    UsergroupShowForm groupForm = (UsergroupShowForm) form;

	    long usergroup_id = Integer.parseInt(request.getParameter(Params.ID));
		
	    Usergroup usergroup =  (Usergroup) DAOManager.getInstance().getUsergroupDAO().findByID(usergroup_id);

	    usergroup.setName(groupForm.getName());
	    usergroup.setDescription(groupForm.getDescription());
		
	    DAOManager.getInstance().getUsergroupDAO().update(usergroup);
		
		return mapping.findForward(GlobalForwards.SHOW_USERGROUP_LIST);
	}
	

  
}
