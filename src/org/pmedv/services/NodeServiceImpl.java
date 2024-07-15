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
package org.pmedv.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.beans.objects.ContentBean;
import org.pmedv.beans.objects.NodeBean;
import org.pmedv.beans.objects.UsergroupBean;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.Content;
import org.pmedv.pojos.Node;
import org.pmedv.pojos.Usergroup;
import org.pmedv.util.NodeHelper;

/**
 * This is the remote implementation of the {@link NodeService}
 * 
 * @author Matthias Pueski
 * 
 */

public class NodeServiceImpl implements NodeService {

	private static final Log log = LogFactory.getLog(NodeService.class);

	@Override
	public ArrayList<NodeBean> findAll() {

		ArrayList<NodeBean> nodeBeans = new ArrayList<NodeBean>();

		List<?> rootNodes = DAOManager.getInstance().getNodeDAO().findAllItems();

		Iterator<?> rootNodesIterator = rootNodes.iterator();

		while (rootNodesIterator.hasNext()) {
			Node node = (Node) rootNodesIterator.next();

			NodeBean nodeBean = new NodeBean();

			if (node.getName() != null) {

				log.debug("Found node :" + node.getPath());

				nodeBean.setId(node.getId());
				nodeBean.setName(node.getName());
				nodeBean.setDisplayName(node.getDisplayName());
				nodeBean.setParent(null);
				nodeBean.setPluginid(node.getPluginid());
				nodeBean.setPluginparams(node.getPluginparams());
				nodeBean.setFirstchild(node.getFirstchild());
				nodeBean.setPosition(node.getPosition());
				nodeBean.setType(node.getType());
				nodeBean.setImage(node.getImage());
				nodeBean.setPath(node.getPath());
				nodeBean.setLinkurl(node.getLinkurl());
				nodeBean.setRel(node.getRel());
				nodeBean.setTargetName(node.getTargetName());

				ArrayList<UsergroupBean> groups = new ArrayList<UsergroupBean>();

				for (Usergroup group : node.getGroups()) {

					log.info("Adding group : " + group.getName());

					UsergroupBean ugb = new UsergroupBean();

					ugb.setDescription(group.getDescription());
					ugb.setId(group.getId());
					ugb.setName(group.getName());

					groups.add(ugb);
				}

				nodeBean.setGroups(groups);

				if (node.getContent() != null) {
					ContentBean contentBean = new ContentBean();
					Content content = node.getContent();

					contentBean.setContentname(content.getContentname());
					contentBean.setContentstring(content.getContentstring());
					contentBean.setDescription(content.getDescription());
					contentBean.setId(content.getId());
					contentBean.setPagename(content.getPagename());
					contentBean.setLastmodified(content.getLastmodified());
					contentBean.setLastmodifiedby(content.getLastmodifiedby());
					contentBean.setCreated(content.getCreated());

					nodeBean.setContentBean(contentBean);
				}
			}

			NodeHelper.loadChildNodeBeansFromNode(node, nodeBean);

			nodeBeans.add(nodeBean);
		}

		return nodeBeans;
	}

	@Override
	public Long createNode(NodeBean nodeBean, NodeBean parent) throws IllegalArgumentException {

		if (nodeBean.getName() == null)
			throw new IllegalArgumentException("Name of the node must not be null.");

		Long nodeId = 0L;
		Node parentNode = null;

		if (parent != null) {

			Long remoteId = parent.getRemoteId();

			if (remoteId == null)
				throw new IllegalArgumentException("Remote id of parent must not be null.");

			log.info("Fetching node parent with id " + parent.getRemoteId());

			parentNode = (Node) DAOManager.getInstance().getNodeDAO().findByID(parent.getRemoteId());

			if (parentNode == null) {
				throw new IllegalArgumentException("Somehow the parent does not exist in the database.");
			}
			
			log.info("Found parent " + parentNode.getPath());

		}

		Node node = new Node();

		node.setName(nodeBean.getName());
		node.setDisplayName(nodeBean.getDisplayName());
		node.setParent(parentNode);
		node.setPluginid(nodeBean.getPluginid());
		node.setPluginparams(nodeBean.getPluginparams());
		node.setFirstchild(nodeBean.getFirstchild());
		node.setPosition(nodeBean.getPosition());
		node.setType(nodeBean.getType());
		node.setImage(nodeBean.getImage());

		node.setContent(null);
		
		if (node.getType() == Node.TYPE_LINK) {
			node.setRel(nodeBean.getRel());
			node.setTargetName(nodeBean.getTargetName());
			node.setLinkurl(nodeBean.getLinkurl());
		}
		else if (node.getType() == Node.TYPE_CONTENT) {

			if (nodeBean.getContentBean() != null) {
				Content content = new Content();
				content.setContentname(nodeBean.getContentBean().getContentname());
				content.setDescription(nodeBean.getContentBean().getDescription());
				content.setLastmodified(new Date());
				content.setPagename(nodeBean.getContentBean().getPagename());
				content.setContentstring(nodeBean.getContentBean().getContentstring());
				content.setLastmodified(nodeBean.getContentBean().getLastmodified());
				content.setLastmodifiedby(nodeBean.getContentBean().getLastmodifiedby());
				content.setCreated(content.getCreated());

				DAOManager.getInstance().getContentDAO().createAndStore(content);
				node.setContent(content);
				log.debug("Found content : " + content.getContentname());
				
			}

		}
		else if (nodeBean.getType() == Node.TYPE_PLUGIN) {
			node.setPluginparams(nodeBean.getPluginparams());
			node.setPluginid(nodeBean.getPluginid());			
		}

		if (DAOManager.getInstance().getNodeDAO().createAndStore(node) != null) {

			List<?> nodes = DAOManager.getInstance().getNodeDAO().findAllItems();

			for (Iterator<?> it = nodes.iterator(); it.hasNext();) {

				Node currentNode = (Node) it.next();

				if (currentNode.equals(node)) {
					nodeId = currentNode.getId();
				}

			}

		}

		return nodeId;

	}

	@Override
	public boolean deleteNode(NodeBean nodeBean) throws IllegalArgumentException {

		if (nodeBean == null)
			throw new IllegalArgumentException("Node must no be null.");
		if (nodeBean.getId() == null)
			throw new IllegalArgumentException("Node id must no be null.");

		Node node = (Node) DAOManager.getInstance().getNodeDAO().findByID(nodeBean.getRemoteId());
		
		if (node == null) {
			log.error("Could not delete node "+nodeBean);
			return false;
		}
		
		return DAOManager.getInstance().getNodeDAO().delete(node);

	}

	@Override
	public ArrayList<NodeBean> findAllRootNodes() {

		ArrayList<NodeBean> nodeBeans = new ArrayList<NodeBean>();

		List<?> rootNodes = DAOManager.getInstance().getNodeDAO().findAllRootNodes();

		Iterator<?> rootNodesIterator = rootNodes.iterator();

		while (rootNodesIterator.hasNext()) {
			Node node = (Node) rootNodesIterator.next();

			NodeBean nodeBean = new NodeBean();

			if (node.getName() != null) {

				log.debug("Found root node :" + node.getPath());

				nodeBean.setId(node.getId());
				nodeBean.setRemoteId(node.getId());
				nodeBean.setName(node.getName());
				nodeBean.setDisplayName(node.getDisplayName());
				nodeBean.setParent(null);
				nodeBean.setPluginid(node.getPluginid());
				nodeBean.setPluginparams(node.getPluginparams());
				nodeBean.setFirstchild(node.getFirstchild());
				nodeBean.setPosition(node.getPosition());
				nodeBean.setType(node.getType());
				nodeBean.setImage(node.getImage());
				nodeBean.setPath(node.getPath());
				nodeBean.setRel(node.getRel());
				nodeBean.setTargetName(node.getTargetName());
				nodeBean.setLinkurl(node.getLinkurl());

				ArrayList<UsergroupBean> groups = new ArrayList<UsergroupBean>();

				for (Usergroup group : node.getGroups()) {

					log.info("Adding group : " + group.getName());

					UsergroupBean ugb = new UsergroupBean();

					ugb.setDescription(group.getDescription());
					ugb.setId(group.getId());
					ugb.setName(group.getName());

					groups.add(ugb);
				}

				nodeBean.setGroups(groups);

				if (node.getContent() != null) {
					ContentBean contentBean = new ContentBean();
					Content content = node.getContent();

					contentBean.setContentname(content.getContentname());
					contentBean.setContentstring(content.getContentstring());
					contentBean.setDescription(content.getDescription());
					contentBean.setId(content.getId());
					contentBean.setPagename(content.getPagename());
					contentBean.setLastmodified(content.getLastmodified());
					contentBean.setLastmodifiedby(content.getLastmodifiedby());
					contentBean.setCreated(content.getCreated());

					nodeBean.setContentBean(contentBean);
				}
			}

			NodeHelper.loadChildNodeBeansFromNode(node, nodeBean);

			nodeBeans.add(nodeBean);
		}

		return nodeBeans;
	}

	@Override
	public NodeBean findNodeByName(String name) throws IllegalArgumentException {

		if (name == null || name.length() < 1)
			throw new IllegalArgumentException("Name must not be null or shorter than 1.");

		Node node = (Node) DAOManager.getInstance().getNodeDAO().findByName(name);

		if (node == null)
			return null;

		NodeBean nodeBean = new NodeBean();

		nodeBean.setId(node.getId());
		nodeBean.setName(node.getName());
		nodeBean.setDisplayName(node.getDisplayName());
		
		if (node.getParent() != null) {
			NodeBean parentNodeBean = findNodeById(node.getParent().getId());
			nodeBean.setParent(parentNodeBean);
		}
		else
			nodeBean.setParent(null);

		if (node.getContent() != null) {

			ContentBean contentBean = new ContentBean();

			Content content = node.getContent();

			contentBean.setContentname(content.getContentname());
			contentBean.setContentstring(content.getContentstring());
			contentBean.setDescription(content.getDescription());
			contentBean.setId(content.getId());
			contentBean.setPagename(content.getPagename());
			contentBean.setLastmodified(content.getLastmodified());
			contentBean.setLastmodifiedby(content.getLastmodifiedby());
			contentBean.setCreated(content.getCreated());

			nodeBean.setContentBean(contentBean);
		}

		nodeBean.setPluginid(node.getPluginid());
		nodeBean.setPluginparams(node.getPluginparams());
		nodeBean.setFirstchild(node.getFirstchild());
		nodeBean.setPosition(node.getPosition());
		nodeBean.setType(node.getType());
		nodeBean.setImage(node.getImage());
		nodeBean.setPath(node.getPath());
		nodeBean.setRel(node.getRel());
		nodeBean.setTargetName(node.getTargetName());
		nodeBean.setLinkurl(node.getLinkurl());
		
		ArrayList<UsergroupBean> groups = new ArrayList<UsergroupBean>();

		for (Usergroup group : node.getGroups()) {

			UsergroupBean ugb = new UsergroupBean();

			ugb.setDescription(group.getDescription());
			ugb.setId(group.getId());
			ugb.setName(group.getName());

			groups.add(ugb);
		}

		nodeBean.setGroups(groups);

		NodeHelper.loadChildNodeBeansFromNode(node, nodeBean);

		return nodeBean;

	}

	@Override
	public boolean updateNode(NodeBean nodeBean) throws IllegalArgumentException {

		if (nodeBean.getRemoteId() == null)
			throw new IllegalArgumentException("Remote node id must not be null.");

		Node node = (Node) DAOManager.getInstance().getNodeDAO().findByID(nodeBean.getRemoteId());

		if (node == null)
			throw new IllegalArgumentException("Node doesn't exist.");
		else {

			Node parent = null;

			if (nodeBean.getParent() != null) {
				parent = (Node) DAOManager.getInstance().getNodeDAO().findByID(nodeBean.getParent().getRemoteId());
			}

			node.setName(nodeBean.getName());
			node.setDisplayName(nodeBean.getDisplayName());
			node.setParent(parent);
			node.setPluginid(node.getPluginid());
			node.setPluginparams(nodeBean.getPluginparams());
			node.setFirstchild(nodeBean.getFirstchild());
			node.setPosition(nodeBean.getPosition());
			node.setType(nodeBean.getType());
			node.setImage(nodeBean.getImage());
			node.setRel(nodeBean.getRel());
			node.setTargetName(nodeBean.getTargetName());
			node.setLinkurl(nodeBean.getLinkurl());

			if (node.getContent() != null) {

				ContentBean contentBean = nodeBean.getContentBean();

				node.getContent().setContentname(contentBean.getContentname());
				node.getContent().setContentstring(contentBean.getContentstring());
				node.getContent().setDescription(contentBean.getDescription());
				node.getContent().setPagename(contentBean.getPagename());
				node.getContent().setLastmodified(new Date());
				node.getContent().setLastmodifiedby(contentBean.getLastmodifiedby());
				node.getContent().setCreated(contentBean.getCreated());

				DAOManager.getInstance().getContentDAO().update(node.getContent());

			}

			return DAOManager.getInstance().getNodeDAO().update(node);
		}

	}

	@Override
	public NodeBean findNodeById(Long id) throws IllegalArgumentException {

		if (id == null)
			throw new IllegalArgumentException("Node id must not be null.");

		Node node = (Node) DAOManager.getInstance().getNodeDAO().findByID(id);

		if (node == null)
			throw new IllegalArgumentException("Node doesn't exist.");
		else {

			NodeBean nodeBean = new NodeBean();

			nodeBean.setId(node.getId());
			nodeBean.setName(node.getName());
			nodeBean.setDisplayName(node.getDisplayName());

			NodeBean parentNode = null;

			if (node.getParent() != null) {

				// TODO : We need to populate the parent references recursively

				parentNode = new NodeBean();

				parentNode.setId(node.getParent().getId());
				parentNode.setName(node.getParent().getName());
				parentNode.setDisplayName(node.getParent().getDisplayName());
				parentNode.setPluginid(node.getParent().getPluginid());
				parentNode.setPluginparams(node.getParent().getPluginparams());
				parentNode.setFirstchild(node.getParent().getFirstchild());
				parentNode.setPosition(node.getParent().getPosition());
				parentNode.setType(node.getParent().getType());
				parentNode.setImage(node.getParent().getImage());
				parentNode.setPath(node.getParent().getPath());
				parentNode.setRel(node.getRel());
				parentNode.setTargetName(node.getTargetName());
				parentNode.setLinkurl(node.getLinkurl());

				ArrayList<UsergroupBean> groups = new ArrayList<UsergroupBean>();

				for (Usergroup group : node.getParent().getGroups()) {

					UsergroupBean ugb = new UsergroupBean();

					ugb.setDescription(group.getDescription());
					ugb.setId(group.getId());
					ugb.setName(group.getName());

					groups.add(ugb);
				}

				parentNode.setGroups(groups);

			}

			if (node.getContent() != null) {

				ContentBean contentBean = new ContentBean();

				Content content = node.getContent();

				contentBean.setContentname(content.getContentname());
				contentBean.setContentstring(content.getContentstring());
				contentBean.setDescription(content.getDescription());
				contentBean.setId(content.getId());
				contentBean.setPagename(content.getPagename());
				contentBean.setLastmodified(content.getLastmodified());
				contentBean.setLastmodifiedby(content.getLastmodifiedby());
				contentBean.setCreated(content.getCreated());

				nodeBean.setContentBean(contentBean);
			}

			nodeBean.setParent(parentNode);
			nodeBean.setPluginid(node.getPluginid());
			nodeBean.setPluginparams(node.getPluginparams());
			nodeBean.setFirstchild(node.getFirstchild());
			nodeBean.setPosition(node.getPosition());
			nodeBean.setType(node.getType());
			nodeBean.setImage(node.getImage());
			nodeBean.setPath(node.getPath());
			nodeBean.setRel(node.getRel());
			nodeBean.setTargetName(node.getTargetName());
			nodeBean.setLinkurl(node.getLinkurl());
						
			ArrayList<UsergroupBean> groups = new ArrayList<UsergroupBean>();

			for (Usergroup group : node.getGroups()) {

				UsergroupBean ugb = new UsergroupBean();

				ugb.setDescription(group.getDescription());
				ugb.setId(group.getId());
				ugb.setName(group.getName());

				groups.add(ugb);
			}

			nodeBean.setGroups(groups);

			NodeHelper.loadChildNodeBeansFromNode(node, nodeBean);
			return nodeBean;
		}
	}

	@Override
	public boolean publishAllNodes(ArrayList<NodeBean> nodes) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doStaticExport(ServletRequest request) {
		NodeHelper.doStaticExport(request);
		return true;
	}

	@Override
	public void addGroup(NodeBean node, UsergroupBean group) {

		if (node == null || node.getId() == null)
			throw new IllegalArgumentException("Node or node id must not be null.");
		if (group == null || group.getId() == null)
			throw new IllegalArgumentException("Group or group id must not be null");

		DAOManager.getInstance().getNodeDAO().addGroup(group.getId(), node.getId());

	}

	@Override
	public void removeGroup(NodeBean node, UsergroupBean group) {

		if (node == null || node.getId() == null)
			throw new IllegalArgumentException("Node or node id must not be null.");
		if (group == null || group.getId() == null)
			throw new IllegalArgumentException("Group or group id must not be null");

		DAOManager.getInstance().getNodeDAO().removeGroup(group.getId(), node.getId());

	}

}
