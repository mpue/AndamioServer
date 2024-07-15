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
import org.pmedv.forms.NodeForm;
import org.pmedv.pojos.Content;
import org.pmedv.pojos.Node;
 

/**
 * This action creates a new node.
 * 
 * @author Matthias Pueski
 *
 */
public class NodeAction extends AbstractPermissiveAction {
	
	public NodeAction() {
		super();
		log.debug("Executing "+this.getClass());
		setName("menu.contentmanager");		
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		if (!isAllowedToProcess(request, getName())) return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);
		
		NodeForm nodeForm = (NodeForm) form;
		
		/* we get data from extjs */
		if ((request.getParameter("parent") != null) && (!request.getParameter("parent").equals(""))) {
			
			Node node = new Node();
			
			String parentName = request.getParameter("parent");
			
			Node parentNode = null;
			
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
					log.debug("Found matching node : "+n.getId()+" - "+n.getName());
				}
				
			}
			
			node.setParent(parentNode);
			node.setName(nodeForm.getName());
			node.setType(Node.TYPE_PLUGIN);
			node.setImage(nodeForm.getImage());
			node.setPluginid(nodeForm.getPluginid());
			node.setPluginparams(nodeForm.getPluginparams());
			
			try {
				node.setPosition(DAOManager.getInstance().getNodeDAO().getMaxPos()+1);
			}
			catch (NullPointerException n) {
				node.setPosition(0);
			}
			
			DAOManager.getInstance().getNodeDAO().createAndStore(node);
			
		}
		
		/* regular processing for legacy purposes */
		
		else {
			
			Node node = new Node();
			
			if ((request.getParameter("parent") != null) && (!request.getParameter("parent").equals(""))) {
				String parentName = request.getParameter("parent");
				
				Node parentNode = (Node) DAOManager.getInstance().getNodeDAO().findByName(parentName);
				node.setParent(parentNode);
			}
			if ((request.getParameter("type") != null) && (!request.getParameter("type").equals(""))) {
				nodeForm.setType(Integer.parseInt(request.getParameter("type")));
				
			}
			if ((request.getParameter("contentName") != null) && (!request.getParameter("contentName").equals(""))) {
				Content content = (Content) DAOManager.getInstance().getContentDAO().findByName(request.getParameter("contentName"));		
				node.setContent(content);			
			}
			
			node.setName(nodeForm.getName());
			node.setType(nodeForm.getType());
			node.setImage(nodeForm.getImage());
			node.setPluginid(nodeForm.getPluginid());
			node.setPluginparams(nodeForm.getPluginparams());
			node.setLinkurl(nodeForm.getLinkurl());
			
			try {
				node.setPosition(DAOManager.getInstance().getNodeDAO().getMaxPos()+1);
			}
			catch (NullPointerException n) {
				node.setPosition(0);
			}
			
			DAOManager.getInstance().getNodeDAO().createAndStore(node);
			
		}
		

		if (request.getParameter("async") != null)
			return null;
		else
			return mapping.findForward(GlobalForwards.SHOW_NODE_LIST);

	}
}
