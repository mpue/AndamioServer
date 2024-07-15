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
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.forms.ContentListForm;
 

public class ContentListAction extends AbstractPermissiveAction
{
		
	public ContentListAction() {
		super("menu.contentmanager");
		log.debug("Executing "+this.getClass());
		setName("menu.contentmanager");		
	}
	
  /** 
   * loads contentlist from the db and saves it in the request
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
			    
		ContentListForm contentListForm = (ContentListForm) form;

	    List<?> contents =  DAOManager.getInstance().getContentDAO().findAllItems();
	    contentListForm.setContents(contents);
	    contentListForm.setName(getName());
	    contentListForm.setIcon(getProcess().getIcon());

	    request.setAttribute("content",contents);
			    
	    return mapping.findForward(GlobalForwards.SHOW_CONTENT_LIST);
	}
}

