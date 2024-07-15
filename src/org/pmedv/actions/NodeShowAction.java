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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.pmedv.cms.common.GlobalForwards;
import org.pmedv.cms.common.Params;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.forms.NodeShowForm;
import org.pmedv.plugins.ParamDescriptor;
import org.pmedv.plugins.PluginHelper;
import org.pmedv.pojos.Content;
import org.pmedv.pojos.Node;
import org.pmedv.pojos.Usergroup;
import org.pmedv.util.NodeHelper;
import org.pmedv.util.RequestUtil;
import org.pmedv.util.SiteHelper;

/**
 * <p>
 * This class is responsible for all node operations
 * </p>
 * <p>
 * There are some methods for AJAX operations included, these methods have a
 * null target an just fill the output stream for the AJAX requests.
 * </p>
 * 
 * @author Matthias Pueski
 */
public class NodeShowAction extends ObjectListAction {

	private static final Log	log	= LogFactory.getLog(NodeShowAction.class);

	public NodeShowAction() {

		super("menu.contentmanager");
		log.debug("Executing " + this.getClass());
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		if (!isAllowedToProcess(request, getName()))
			return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);

		Long id = Long.parseLong(request.getParameter(Params.ID_NODE));

		NodeHelper.deleteNode(id);

		return null;

	}

	/**
	 * Gets the content from a node in plain html and writes it to the
	 * outputstream 
	 */
	public ActionForward getContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		if (!isAllowedToProcess(request, getName()))
			return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);

		Long id = Long.parseLong(request.getParameter(Params.ID_NODE));

		String content = NodeHelper.getContent(id);

		if (content != null) {

			response.setContentType("text/html");
			PrintWriter out;

			try {
				out = response.getWriter();
				out.println(content);
				out.flush();
			}
			catch (IOException e) {
				e.printStackTrace();
			}

		}

		return null;
	}


	/**
	 * Returns the content metadata from this node as xml
	 * 
	 */
	public ActionForward getContentAsJson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		if (!isAllowedToProcess(request, getName()))
			return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);

		Content content = null;

		StringBuffer jsonString = new StringBuffer();

		try {
			int id = Integer.parseInt(request.getParameter(Params.ID_CONTENT));

			log.debug("fetching content from node with id : " + id);

			content = (Content) DAOManager.getInstance().getContentDAO().findByID(id);
		}
		catch (NumberFormatException e) {
			log.debug("Content could not be found : " + request.getParameter(Params.ID_CONTENT));
		}

		if (content != null) {

			response.setContentType("text/plain");

			log.debug("Found content with name : " + content.getContentname());

			jsonString.append("{\"success\":true,\"data\":{");
			jsonString.append("\"contentname\":\"" + content.getContentname() + "\",");
			jsonString.append("\"description\":\"" + content.getDescription() + "\",");
			jsonString.append("\"metatags\":\"" + content.getMetatags() + "\",");
			jsonString.append("\"commentsAllowed\":" + content.isCommentsAllowed() + ",");
			jsonString.append("\"pagename\":\"" + content.getPagename() + "\"");
			jsonString.append("}}");

			try {
				PrintWriter out = response.getWriter();
				out.println(jsonString.toString());
				out.flush();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * Returns the content metadata from this node as xml
	 */
	public ActionForward getNodeAsJson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		if (!isAllowedToProcess(request, getName()))
			return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);

		Node node = null;

		StringBuffer jsonString = new StringBuffer();

		Long id = -1L;

		try {

			if (request.getParameter("getLast") != null) {
				id = DAOManager.getInstance().getNodeDAO().getLastCreatedId();
			}
			else {
				id = Long.parseLong(request.getParameter(Params.ID_NODE));
			}

			node = (Node) DAOManager.getInstance().getNodeDAO().findByID(id);

			log.debug("fetching node with id : " + id);

		}
		catch (NumberFormatException e) {
			log.debug("Node could not be found : " + id);
		}

		if (node != null) {

			response.setContentType("text/plain");

			log.debug("Found node with name : " + node.getName());

			String parentName = "root";

			if (node.getParent() != null)
				parentName = node.getParent().getName();

			jsonString.append("{\"success\":true,\"data\":{");
			jsonString.append("\"id\":\"" + node.getId() + "\",");
			jsonString.append("\"position\":\"" + node.getPosition() + "\",");
			jsonString.append("\"type\":\"" + node.getType() + "\",");
			jsonString.append("\"name\":\"" + node.getName() + "\",");
			jsonString.append("\"linkurl\":\"" + node.getLinkurl() + "\",");
			jsonString.append("\"pluginparams\":\"" + node.getPluginparams() + "\",");
			jsonString.append("\"parent\":\"" + parentName + "\",");
			jsonString.append("\"image\":\"" + node.getImage() + "\",");
			jsonString.append("\"pluginid\":\"" + node.getPluginid() + "\",");
			jsonString.append("\"exportable\":\"" + node.isExportable() + "\",");
			jsonString.append("\"targetName\":\"" + node.getTargetName() + "\",");
			jsonString.append("\"rel\":\"" + node.getRel() + "\",");
			jsonString.append("\"firstchild\":\"" + node.getFirstchild() + "\"");
			jsonString.append("}}");

			try {
				PrintWriter out = response.getWriter();
				out.println(jsonString.toString());
				out.flush();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * Returns a Google-Sitemap as XML
	 * 
	 */
	public ActionForward getSitemapAsXML(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		response.setContentType("text/xml");
		PrintWriter out;

		try {
			out = response.getWriter();
			out.println(SiteHelper.createSiteMapXML(request));
			out.flush();

		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public ActionForward JSONUpdate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		RequestUtil.dumpParams(request);

		NodeShowForm nodeForm = (NodeShowForm) form;

		Node node = (Node) DAOManager.getInstance().getNodeDAO().findByID(Integer.parseInt(request.getParameter(Params.ID)));

		log.debug("Trying to update node: " + node.getName());

		node.setFirstchild(nodeForm.getFirstchild());
		node.setImage(nodeForm.getImage());
		node.setName(nodeForm.getName());
		node.setPluginparams(nodeForm.getPluginparams());
		node.setExportable(nodeForm.isExportable());
		node.setRel(nodeForm.getRel());
		node.setTargetName(nodeForm.getTargetName());
		node.setLinkurl(nodeForm.getLinkurl());
		
		String parentName = request.getParameter("parent");

		Node parentNode = null;

		if (parentName != null && !parentName.equalsIgnoreCase("root")) {

			String[] pathElements = parentName.split("/");

			for (int i = 0; i < pathElements.length; i++) {
				log.debug("Found path element :" + pathElements[i]);
			}

			List<?> similarNodes = DAOManager.getInstance().getNodeDAO()
					.findElementsByName(pathElements[pathElements.length - 1]);

			for (Object o : similarNodes) {

				Node n = (Node) o;

				log.debug("Found similar node    : " + n.getName());
				log.debug("Similar node has Path : " + n.getPath());
				log.debug("Requested path is     : " + parentName);

				if (parentName.equals(n.getPath())) {
					parentNode = (Node) DAOManager.getInstance().getNodeDAO().findByID(n.getId());
					log.debug("Found matching node : " + n.getId() + " - " + n.getName());
				}

			}

		}

		String message = null;

		if (node.equals(parentNode)) {
			log.info("Node can not be its own parent");
			message = "{\"success\":false, message : \"Node can not be its own parent.\" }";
		}
		else {

			if (parentNode != null && NodeHelper.getHighestParent(parentNode).equals(node)) {
				message = "{\"success\":false, message : \"A node can not be child of one of its children or subchildren.\" }";
			}
			else {

				node.setParent(parentNode);
				log.info("updating node : " + node);
				message = "{\"success\":true, message : \"Node successfully changed.\" }";
				DAOManager.getInstance().getNodeDAO().update(node);

			}

		}

		if (message != null)

			try {
				PrintWriter out = response.getWriter();
				out.write(message);
			}
			catch (IOException e1) {
				log.error("Could not write JSON message to servlet output stream : \n" + message);
			}

		return null;

	}

	/**
	 * Adds this node to a specified group
	 * 
	 */
	public ActionForward addGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		if (!isAllowedToProcess(request, getName()))
			return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);

		Long group_id = Long.parseLong(request.getParameter(Params.ID_USERGROUP));
		Long node_id = Long.parseLong(request.getParameter(Params.ID_NODE));

		log.debug("Getting node_id " + node_id + " from session " + request.getSession().getId());

		DAOManager.getInstance().getNodeDAO().addGroup(group_id, node_id);

		return null;
	}

	/**
	 * Removes this node from a specified group
	 */
	public ActionForward removeGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		if (!isAllowedToProcess(request, getName()))
			return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);

		Long group_id = Long.parseLong(request.getParameter(Params.ID_USERGROUP));
		Long node_id = Long.parseLong(request.getParameter(Params.ID_NODE));

		DAOManager.getInstance().getNodeDAO().removeGroup(group_id, node_id);

		return null;
	}

	/**
	 * Sets the node one position up 
	 */
	public ActionForward elementUp(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		if (!isAllowedToProcess(request, getName()))
			return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);

		Long id = Long.parseLong(request.getParameter(Params.ID_NODE));

		NodeHelper.moveUp(id);

		return null;
	}

	/**
	 * Sets the node one position down
	 */
	public ActionForward elementDown(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		if (!isAllowedToProcess(request, getName()))
			return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);

		Long id = Long.parseLong(request.getParameter(Params.ID_NODE));

		NodeHelper.moveDown(id);

		return null;

	}

	/**
	 * Export all nodes and their according contents to html files
	 */
	public ActionForward staticExport(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		if (!isAllowedToProcess(request, getName()))
			return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);

		NodeHelper.doStaticExport(request);

		return null;

	}

	/**
	 * Export all nodes and their according contents to xml-files
	 */
	public ActionForward exportNodes(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		if (!isAllowedToProcess(request, getName()))
			return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);

		String date = NodeHelper.doXMLExport();

		ActionForward af = new ActionForward();

		af.setPath("/admin/fileDownload.do?filePath=export/" + "nodes_" + date + ".zip&contentType=application/zip");

		return af;
	}

	/**
	 * Import all nodes and their according contents from xml-files
	 */
	public ActionForward importNodes(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		if (!isAllowedToProcess(request, getName()))
			return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);

		NodeHelper.doImport(true);

		return null;

	}

	public ActionForward getJSON(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		writeJSONList(Node.class, request, response);

		return null;

	}

	public ActionForward getJSONWithoutMeta(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		writeJSONList(Node.class, false, request, response);

		return null;

	}

	public ActionForward getParamDescriptorsAsJSON(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		String pluginid = request.getParameter("pluginid");

		ArrayList<ParamDescriptor> descriptors = PluginHelper.getParamDescriptors(pluginid, this.getClass().getClassLoader());

		writeJSONList(ParamDescriptor.class, descriptors, true, "id", request, response);

		return null;
	}

	/**
	 * Creates a json list containing all groups of a specified node
	 */

	public ActionForward getGroupsAsJSON(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		Node node = null;

		Long id = Long.parseLong(request.getParameter(Params.ID_NODE));
		node = (Node) DAOManager.getInstance().getNodeDAO().findByID(id);

		if (node != null) {

			Set<Usergroup> groups = node.getGroups();
			writeJSONList(Usergroup.class, groups, true, "id", request, response);

		}

		return null;
	}

}
