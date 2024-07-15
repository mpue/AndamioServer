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

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.pmedv.cms.common.GlobalForwards;
import org.pmedv.cms.common.Params;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.forms.ContentShowForm;
import org.pmedv.pojos.Content;
import org.pmedv.pojos.Node;

public class EditContentRequest extends ActionRequest {

	public EditContentRequest(HttpServletRequest request, String processName, ActionMapping mapping, ActionForm form) {
		super(request, processName, mapping, form);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ActionForward doAction() {
		
		ContentShowForm contentForm = (ContentShowForm) form;
		
		long content_id = 0L;
		
		try {
			content_id = Integer.parseInt(request.getParameter(Params.ID_CONTENT));
			log.debug("Fount content_id : "+content_id);
		}
		catch (NumberFormatException n) {
			log.debug("Could not parse content_id, trying to locate first node.");
			
			for (Iterator nodeIterator = DAOManager.getInstance().getNodeDAO().findAllRootNodes().iterator();nodeIterator.hasNext();) {
				Node currentNode = (Node) nodeIterator.next();
				if (currentNode != null ) {
					if (currentNode.getContent() != null) {
						content_id = currentNode.getContent().getId();	
						break;
					}
				}
			}
		}
		
		if ( DAOManager.getInstance().getNodeDAO().findAllRootNodes().size() != 0 ) {
			
			Content content = (Content) DAOManager.getInstance().getContentDAO().findByID(content_id);
			contentForm.setContent(content);
			contentForm.setName(processName);
			contentForm.setIcon(process.getIcon());
		} else {
			
			log.debug("Keine Nodes gefunden, erzeuge Dummy-Node!");
			contentForm.setContent(null);
			contentForm.setName(processName);
			contentForm.setIcon(process.getIcon());
		}
		
		request.setAttribute("content_id",content_id);	
		
		return mapping.findForward(GlobalForwards.SHOW_CONTENT_EDITOR);
	}

}
