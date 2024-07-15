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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.pmedv.cms.common.GlobalForwards;
import org.pmedv.cms.daos.ContentDAO;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.forms.SiteRequestForm;
 

public class ShowStatisticsAction extends AbstractPermissiveAction {
	
	public ShowStatisticsAction() {
		super();
		log.debug("Executing "+this.getClass());
		setName("menu.statistics");		
	}
	
	public ActionForward execute(
			ActionMapping mapping,
		    ActionForm form,
		    HttpServletRequest request,
		    HttpServletResponse response) throws Exception{
		
		if (!isAllowedToProcess(request, getName())) return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);	
		
		SiteRequestForm siterequestForm = (SiteRequestForm) form;
		
		ContentDAO     contentDAO     = DAOManager.getInstance().getContentDAO();
		
		int totalrequests = DAOManager.getInstance().getSiteRequestDAO().findAllItems().size();
		int contentcount = contentDAO.findAllItems().size();
		
		log.debug("total number of requests : "+totalrequests);
		log.debug("total number of contents : "+contentcount);
		
		siterequestForm.setTotalrequests(totalrequests);
		siterequestForm.setContentcount(contentcount);
		
		return mapping.findForward(GlobalForwards.SHOW_STATISTICS);
	}

}
