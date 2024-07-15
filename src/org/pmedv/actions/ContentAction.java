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
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.pmedv.cms.common.GlobalForwards;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.forms.ContentForm;
import org.pmedv.pojos.Content;
import org.pmedv.pojos.Node;

public class ContentAction extends AbstractPermissiveAction {

	private static final ResourceBundle	resources	= ResourceBundle.getBundle("MessageResources", Locale.getDefault());

	public ContentAction() {

		super();
		log.debug("Executing " + this.getClass());
		setName("menu.contentmanager");
	}

	/**
	 * Creates a new content
	 */

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (!isAllowedToProcess(request, getName()))
			return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);

		ContentForm contentForm = (ContentForm) form;

		String parentNodeName = request.getParameter("parent");

		String message = null;

		if (parentNodeName.equals("root")) {

			Node node = new Node();

			Node checkNode = (Node) DAOManager.getInstance().getNodeDAO().findByName(contentForm.getContentname());

			if (checkNode != null && checkNode.getParent() == null) {
				message = "{\"success\":false, message : \"There is already such a content.\" }";
			}
			else {

				Content content = new Content();

				content.setContentname(contentForm.getContentname());
				content.setPagename(contentForm.getPagename());
				content.setContentstring(resources.getString("content.empty"));
				content.setDescription(contentForm.getDescription());
				String metatags = (contentForm.getMetatags().replace("\n", ""));
				content.setMetatags(StringEscapeUtils.escapeHtml(metatags));
				content.setLastmodified(new Date());
				content.setCreated(new Date());
				content.setCommentsAllowed(contentForm.isCommentsAllowed());
				content.setLastmodifiedby((String) request.getSession().getAttribute("login"));
				content.setCreated(new Date());

				DAOManager.getInstance().getContentDAO().createAndStore(content);

				node.setName(content.getContentname());
				node.setContent(content);
				node.setParent(null);
				node.setPosition(DAOManager.getInstance().getNodeDAO().getMaxPos() + 1);
				node.setType(Node.TYPE_CONTENT);

				DAOManager.getInstance().getNodeDAO().createAndStore(node);

				message = "{\"success\":true, message : \"Node successfully created.\" }";

			}

		}
		else {

			Node parentNode = null;

			String[] pathElements = parentNodeName.split("/");

			for (int i = 0; i < pathElements.length; i++) {
				log.debug("Found path element :" + pathElements[i]);
			}

			List<?> similarNodes = DAOManager.getInstance().getNodeDAO()
					.findElementsByName(pathElements[pathElements.length - 1]);

			for (Object o : similarNodes) {

				Node n = (Node) o;

				log.debug("Found similar node    : " + n.getName());
				log.debug("Similar node has Path : " + n.getPath());
				log.debug("Requested path is     : " + parentNodeName);

				if (parentNodeName.equals(n.getPath())) {
					parentNode = (Node) DAOManager.getInstance().getNodeDAO().findByID(n.getId());
					log.debug("Found matching node : " + n.getId() + " - " + n.getName());
				}

			}

			if (parentNode != null) {

				Node node = new Node();

				Node checkNode = (Node) DAOManager.getInstance().getNodeDAO().findByName(contentForm.getContentname());

				if (checkNode != null && checkNode.getParent().equals(parentNode)) {
					message = "{\"success\":false, message : \"There is already such a content.\" }";
				}
				else {

					Content content = new Content();

					content.setContentname(contentForm.getContentname());
					content.setPagename(contentForm.getPagename());
					content.setContentstring(resources.getString("content.empty"));
					content.setDescription(contentForm.getDescription());
					String metatags = (contentForm.getMetatags().replace("\n", ""));
					content.setMetatags(StringEscapeUtils.escapeHtml(metatags));
					content.setCommentsAllowed(contentForm.isCommentsAllowed());
					content.setLastmodified(new Date());
					content.setCreated(new Date());
					content.setLastmodifiedby((String) request.getSession().getAttribute("login"));
					content.setCreated(new Date());
					
					DAOManager.getInstance().getContentDAO().createAndStore(content);

					node.setName(content.getContentname());
					node.setContent(content);
					node.setParent(parentNode);
					node.setPosition(DAOManager.getInstance().getNodeDAO().getMaxPos() + 1);
					node.setType(Node.TYPE_CONTENT);

					DAOManager.getInstance().getNodeDAO().createAndStore(node);

					message = "{\"success\":true, message : \"Node successfully created.\" }";
				}

			}

		}

		if (message != null)

			try {
				PrintWriter out = response.getWriter();
				out.write(message);
			}
			catch (IOException e1) {

				e1.printStackTrace();
			}

		return null;

	}

}
