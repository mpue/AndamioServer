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
import org.pmedv.forms.ProcessForm;
import org.pmedv.pojos.Usergroup;
 

/**
 * The process action creates a new process.
 * 
 * @author Matthias Pueski
 *
 */
public class ProcessAction  extends AbstractPermissiveAction {

	public ProcessAction() {
		super();
		log.debug("Executing "+this.getClass());
		setName("menu.processes");		
	}

	
	public ActionForward execute(
			ActionMapping mapping,
		    ActionForm form,
		    HttpServletRequest request,
		    HttpServletResponse response) throws Exception{
			      
		if (!isAllowedToProcess(request, getName())) return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);
		
		ProcessForm processForm = (ProcessForm) form;
		
		org.pmedv.pojos.Process checkProcess = (org.pmedv.pojos.Process) DAOManager.getInstance()
				.getProcessDAO().findByName(processForm.getName());
		
		if (checkProcess != null) {
	    	
			PrintWriter out = response.getWriter();
	    	
	    	String message = "{\"success\":false, message : \"There is already a process with this name.\" }";
	    	out.write(message);

	    	return null;
	    	
		}
		
		org.pmedv.pojos.Process process = new org.pmedv.pojos.Process();
		
		process.setActive(processForm.getActive());
		process.setIcon(processForm.getIcon());
		process.setName(processForm.getName());
		process.setTarget(processForm.getTarget());

        int newPos = DAOManager.getInstance().getProcessDAO().getMaxPos() + 1;

        process.setPosition(newPos);

		DAOManager.getInstance().getProcessDAO().createAndStore( process );

        // refetch, to get the id

        process = (org.pmedv.pojos.Process)DAOManager.getInstance().getProcessDAO().findByName(processForm.getName());
        Usergroup adminGroup = (Usergroup)DAOManager.getInstance().getUsergroupDAO().findByName("admin");

        DAOManager.getInstance().getProcessDAO().addGroup(adminGroup.getId(), process.getId());
        	
    	PrintWriter out = response.getWriter();
    	
    	String message = "{\"success\":true }";
    	out.write(message);

		return null;
		
	}
	
}


