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
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.pmedv.cms.common.GlobalForwards;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.Node;

/**
 * This class is responsible for the management of the main content tree.
 * of the extjs application 
 * 
 * @author Matthias Pueski
 * 
 * 9.2.2008
 *
 */
public class TreeAction extends AbstractPermissiveDispatchAction {
	
	public static final Log log = LogFactory.getLog(TreeAction.class);
	
	public TreeAction() {
		super();
		log.debug("Executing "+this.getClass());
		setName("menu.contentmanager");		
	}
	
	/**
	 * Writes a json list of all children of a specific tree node to the output stream
	 * <p>
	 * A json node looks like this for example
	 * </p>
	 * <p>
	 * {"text":"debug.js","id":"source\/debug.js","leaf":true,"cls":"file"}
	 * </p>
	 * if node="root", all root nodes are returned
	 * </p>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * 
	 * @return nothing
	 */
	
	public ActionForward getNodesAsJSON(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		if (!isAllowedToProcess(request, getName())) return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);
		
		StringBuffer jsonList = new StringBuffer();
		Collection<?> nodes = null;
		
		log.debug("Fetching children for node " +request.getParameter("id"));
		
		// just in case the parameter node is missing, we return no nodes 
		
		if (request.getParameter("id") != null) {
			
			if (request.getParameter("id").equals("undefined")) {
				nodes = DAOManager.getInstance().getNodeDAO().findAllRootNodes();		
			}
			else {
				
				Long id = Long.parseLong(request.getParameter("id"));
				
				Node node = (Node)DAOManager.getInstance().getNodeDAO().findByID(id);			
				
				if (node != null)			
					if ( node.hasChildren() )
						nodes = node.getChildren();
			}
		}
		
		// if there are any nodes 
		
		if (nodes != null) {

			jsonList.append("[");
			
			// Iterate over all nodes and build the json array 
			
			for (Iterator<?> it = nodes.iterator();it.hasNext();) {
				
				Node currentNode = (Node)it.next();
				
				jsonList.append("{ \"text\":\""+currentNode.getName()+"\",");
				jsonList.append("\"id\":\""+currentNode.getId()+"\",");
				jsonList.append("\"node_id\":\""+currentNode.getId()+"\",");
				jsonList.append("\"position\":\""+currentNode.getPosition()+"\",");				
				jsonList.append("\"nodepath\":\""+currentNode.getPath()+".html\",");
				
				if (!currentNode.hasChildren())
					jsonList.append("\"leaf\":true,");			
				else
					jsonList.append("\"restricted\":false,");								
				if (currentNode.getType() == Node.TYPE_CONTENT) {
					if (currentNode.getContent() != null)
						jsonList.append("\"content_id\":\""+ currentNode.getContent().getId()+"\",");
					jsonList.append("\"type\":\"content\",");
					if (currentNode.isRestricted())
						jsonList.append("\"cls\":\"content_restricted\"},");
					else
						jsonList.append("\"cls\":\"content\"},");
				}
				else if (currentNode.getType() == Node.TYPE_LINK) {
					jsonList.append("\"type\":\"link\",");
					jsonList.append("\"cls\":\"link\"},");					
				}
				else if (currentNode.getType() == Node.TYPE_PLUGIN) {	
					jsonList.append("\"type\":\"plugin\",");
					jsonList.append("\"cls\":\"plugin\"},");					
				}
				else {
					jsonList.append("\"type\":\"file\",");
					jsonList.append("\"cls\":\"file\"},");					
				}
				
			}
			
			jsonList.setCharAt(jsonList.length()-1, ' ');
			jsonList.append("]");
		}
		
		// write the array to the output of this servlet 
		
		try {
			PrintWriter out = response.getWriter();
			out.print(jsonList);			
		    out.flush();
		    
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		// since we are using extjs, this method doesn't need to to return any value 
		
		return null;
	}

	@SuppressWarnings("unchecked")
	public ActionForward getDeferredNodesAsJSON(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		if (!isAllowedToProcess(request, getName())) return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);
		
		StringBuffer jsonList = new StringBuffer();
		Collection<?> nodes = null;
		
		log.debug("Fetching children for node " +request.getParameter("node"));
		
		/* just in case the parameter node is missing, we return no nodes */
		
		if (request.getParameter("node") != null) {
			
			if (request.getParameter("node").equals("root")) {
				nodes = DAOManager.getInstance().getNodeDAO().findAllRootNodes();		
			}
			else {
				Node node = (Node)DAOManager.getInstance().getNodeDAO().findByName(request.getParameter("node"));			
				if ( node.hasChildren() )
					nodes = node.getChildren();
			}
		}
		
		/* if there are any nodes */
		
		if (nodes != null) {

			jsonList.append("[");
			
			/* Iterate over all nodes and build the json array */
			
			for (Iterator it = nodes.iterator();it.hasNext();) {
				
				Node currentNode = (Node)it.next();
				
				jsonList.append("{ \"name\":\""+currentNode.getName()+"\",");
				jsonList.append("\"id\":"+currentNode.getId()+",");
				jsonList.append("\"position\":"+currentNode.getPosition()+",");		
				
				if (currentNode.getContent() != null) {
					jsonList.append("\"content_id\":"+currentNode.getContent().getId()+",");
					log.debug("Found content : "+currentNode.getContent().getId());
				}
				else {
					jsonList.append("\"content_id\":0,");
				}
				jsonList.append("\"type\":"+currentNode.getType()+"},");
			
			}
								
			jsonList.setCharAt(jsonList.length()-1, ' ');
			jsonList.append("]");
		}
		
		/* write the array to the output of this servlet */
		
		try {
			PrintWriter out = response.getWriter();
			out.print(jsonList);			
		    out.flush();
		    
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		/* since we are using rcp, this method doesn't need to to return any value */
		
		return null;
	}
	
	
}
