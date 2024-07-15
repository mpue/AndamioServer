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

import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.pmedv.actions.requests.content.DeleteContentRequest;
import org.pmedv.actions.requests.content.EditContentRequest;
import org.pmedv.actions.requests.content.ExportContentsRequest;
import org.pmedv.actions.requests.content.ImportContentsRequest;
import org.pmedv.actions.requests.content.PreviewContentRequest;
import org.pmedv.actions.requests.content.SaveContentRequest;
import org.pmedv.cms.common.GlobalForwards;
import org.pmedv.cms.common.Params;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.forms.ContentShowForm;
import org.pmedv.pojos.Content;
import org.pmedv.pojos.Node;

public class ContentShowAction extends AbstractPermissiveDispatchAction {
	
	private static final Log log = LogFactory.getLog(ContentShowAction.class);
	
	public ContentShowAction() {
		super("menu.contentmanager");
		log.debug("Executing "+this.getClass());
	}
	
	/** 
	 * loads content from the db and saves them in the request
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward edit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		if (!isAllowedToProcess(request, getName())) return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);
		
		ContentShowForm contentForm = (ContentShowForm) form;

		long content_id = Integer.parseInt(request.getParameter(Params.ID_CONTENT));

		Content content = (Content) DAOManager.getInstance().getContentDAO().findByID(content_id);
		contentForm.setContent(content);
		contentForm.setName(getName());
		contentForm.setIcon(getProcess().getIcon());		
		
		request.setAttribute("content_id",content_id);

		return mapping.findForward(GlobalForwards.SHOW_CONTENT_DETAILS);
	}
	
	/** 
	 * loads content from the db and saves them in the request
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward editContents(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		return new EditContentRequest(request,getName(),mapping,form).execute();
	}
	
	public ActionForward editContentsRCP(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		ContentShowForm contentForm = (ContentShowForm) form;
		
		long content_id = 0L;
		
		try {
			content_id = Integer.parseInt(request.getParameter(Params.ID_CONTENT));
			log.debug("Fount content_id : "+content_id);
		}
		catch (NumberFormatException n) {
			log.debug("Could not parse content_id, trying to locate first node.");
			
			for (Iterator<?> nodeIterator = DAOManager.getInstance().getNodeDAO().findAllRootNodes().iterator();nodeIterator.hasNext();) {
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
		} else {			
			log.debug("Keine Nodes gefunden, erzeuge Dummy-Node!");
			contentForm.setContent(null);
		}
		
		request.setAttribute("content_id",content_id);	
		
		return mapping.findForward(GlobalForwards.SHOW_SINGLE_EDITOR);	}
	
	public ActionForward editContentsNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		return new EditContentRequest(request,getName(),mapping,form).execute();
	}
	
	
	public ActionForward preview(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		return new PreviewContentRequest(request,getName(),mapping,form).execute();

	}

	/**
	 * Deletes selected content
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */

	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		return new DeleteContentRequest(request,getName(),mapping,form).execute();

	}

	/**
	 * Saves the modified content
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */

	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		return new SaveContentRequest(request,getName(),mapping,form).execute();
		
	}
	
	
	public ActionForward saveFromRCP (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		if (!isAllowedToProcess(request, getName())) return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);
		
		ContentShowForm contentForm = (ContentShowForm) form;

		int content_id = Integer.parseInt(request.getParameter(Params.ID).trim());

		Content content = (Content) DAOManager.getInstance().getContentDAO().findByID(content_id);

		log.debug("Content : "+contentForm.getContentstring());
		
		content.setContentname(contentForm.getContentname().trim());
		content.setPagename(contentForm.getPagename().trim());
		content.setContentstring(contentForm.getContentstring().trim());
		content.setDescription(contentForm.getDescription().trim());
		content.setMetatags(contentForm.getMetatags().trim());
		content.setLastmodified(new Date());

		DAOManager.getInstance().getContentDAO().update(content);

		request.setAttribute("content_id", content_id);

		contentForm.setID(content_id);
		
		return null;
	}
	
	public ActionForward exportContents(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		return  new ExportContentsRequest(request,getName(),mapping,form).execute(); 
		
	}

	public ActionForward importContents(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		return new ImportContentsRequest(request,getName(),mapping,form).execute();
	}
	
	
	
	
}
