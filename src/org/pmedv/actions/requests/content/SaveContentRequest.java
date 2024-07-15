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
package org.pmedv.actions.requests.content;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.pmedv.cms.common.GlobalForwards;
import org.pmedv.cms.common.Params;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.forms.ContentShowForm;
import org.pmedv.pojos.Content;

public class SaveContentRequest extends ActionRequest {

	public SaveContentRequest(HttpServletRequest request, String processName, ActionMapping mapping, ActionForm form) {
		super(request, processName, mapping, form);
	}

	@Override
	protected ActionForward doAction() {

		ContentShowForm contentForm = (ContentShowForm) form;

		int content_id = Integer.parseInt(request.getParameter(Params.ID).trim());

		Content content = (Content) DAOManager.getInstance().getContentDAO().findByID(content_id);

		log.debug("Content : "+contentForm.getContentstring());
		
		content.setContentname(contentForm.getContentname().trim());
		content.setPagename(contentForm.getPagename().trim());
		content.setContentstring(contentForm.getContentstring().trim());
		content.setDescription(contentForm.getDescription().trim());
		content.setCommentsAllowed(contentForm.isCommentsAllowed());
		content.setLastmodifiedby((String)request.getSession().getAttribute("login"));
		
		String metatags = (contentForm.getMetatags().replace("\n", ""));		
		content.setMetatags(StringEscapeUtils.escapeHtml(metatags));
		
		log.info(contentForm.getMetatags());
		
		content.setLastmodified(new Date());

		DAOManager.getInstance().getContentDAO().update(content);

		request.setAttribute("content_id", content_id);

		contentForm.setID(content_id);
		
		ActionForward af = new ActionForward();
		af.setPath(GlobalForwards.AF_SHOW_CONTENT+content_id);
				
		return af;	
	}

}
