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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.pmedv.cms.common.GlobalForwards;
import org.pmedv.cms.common.Params;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.forms.ProcessgroupAddForm;
 
/**
 * This action adds a process to a group in order to restrict the access to this process. 
 * 
 * @author Matthias Pueski
 *
 */
@SuppressWarnings("unchecked")

public class ProcessgroupAddAction extends AbstractPermissiveAction
{ 
	public ProcessgroupAddAction() {
		super();
		log.debug("Executing "+this.getClass());
		setName("menu.processes");		
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

		Long process_id = (Long)request.getSession().getAttribute(Params.ID_PROCESS);
		
		ProcessgroupAddForm processgroupAddForm = (ProcessgroupAddForm) form;

	    List usergroups =  DAOManager.getInstance().getUsergroupDAO().findAllItems();
	    
	    org.pmedv.pojos.Process process = (org.pmedv.pojos.Process)DAOManager.getInstance().getProcessDAO().findByID(process_id);
	    
	    processgroupAddForm.setUsergroups(usergroups);
	    processgroupAddForm.setProcess(process);
			    
	    return mapping.findForward(GlobalForwards.ADD_PROCESSGROUP);
	}
}

