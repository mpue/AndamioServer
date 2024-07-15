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
package org.pmedv.util;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.pmedv.beans.objects.ContentBean;
import org.pmedv.beans.objects.DirectoryObject;
import org.pmedv.beans.objects.NodeBean;
import org.pmedv.beans.objects.UsergroupBean;
import org.pmedv.cms.common.CMSProperties;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.core.util.URLReader;
import org.pmedv.file.DirectoryPrintVisitor;
import org.pmedv.plugins.PluginHelper;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.Content;
import org.pmedv.pojos.Node;
import org.pmedv.pojos.User;
import org.pmedv.pojos.Usergroup;
import org.pmedv.web.ServerUtil;

public class NodeHelper {
	
	private static final ResourceBundle resources = ResourceBundle.getBundle("MessageResources");

	private static final Log log = LogFactory.getLog(NodeHelper.class);

	private static final int CONVERT_IMAGE = 0;
	private static final int CONVERT_LINK = 1;
	private static final int CONVERT_ROOT_LINK = 2;
	private static Config config;

	private static final ArrayList<PathConverter> converters = new ArrayList<PathConverter>();
	private static NodeFilter imageTagFilter = new NodeClassFilter(ImageTag.class);
	private static NodeFilter linkTagFilter = new NodeClassFilter(LinkTag.class);

	private static Node highestParent;

	static {

		config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);

		converters.add(new ImageLinkConverter());
		converters.add(new LinkConverter());
		converters.add(new RootLinkConverter());
	}

	public static void getAllParents(ArrayList<Node> parents,Node n) {
		
		if (n == null) return;
		
		if (n.getParent() != null) {
			parents.add(n.getParent());
			getAllParents(parents,n.getParent());
		}
		
	}
	
	public static Node getHighestParent(Node n) {
		evalParent(n);
		return highestParent;
	}

	private static void evalParent(Node n) {
		if (n != null) {
			highestParent = n;
			evalParent(n.getParent());
		}
	}

	/**
	 * Checks if a user has access to a node. A node is by definition restricted,
	 * if it has any usergroup associated. A node is also restricted if any parent
	 * node (from the current node to the top node) is restricted. A node can have 
	 * different restrictions than any parent node. In this case the node is visible 
	 * to the users that are in the same group as the node.  
	 * 
	 * @param node the node to check the access for
	 * @param user the user to check
	 *  
	 * @return true if the user has access, false if not
	 */
	public static boolean hasAccessToNode(Node node, User user) {

		ArrayList<Node> parents = new ArrayList<Node>();			
		getAllParents(parents, node);
		
		if (user == null) { // anonymous user
			
			log.debug("Anonymous user tries to access node "+node.getPath());
			
			// Node is restricted by itself
			
			if (!node.getGroups().isEmpty()) {
			
				for (Usergroup nodeGroup : node.getGroups()) {
					if (nodeGroup.getName().equals("unregistered"))
						return true;
				}
				
				return false;
			}
						
			for (Node parent : parents) {
				
				// If any parent is restricted, the user has no access
				
				if (!parent.getGroups().isEmpty())
					return false;
				
			}
			
			return true;

		}
		else { // at least a registered user
			
			log.debug("User "+user.getName()+" tries to access node "+node.getPath());
			
			if (!node.getGroups().isEmpty()) {

				for (Usergroup nodeGroup : node.getGroups()) {
					if (nodeGroup.getName().equals("unregistered"))
						return false;
				}
				
				for (Usergroup group : user.getGroups()) {
					
					if (node.getGroups().contains(group))
						return true;
					
				}
				
			}
			else {
				
				// Node has no parents and no groups, thus is accessible
				
				if (node.getParent() == null) {
					log.info("Node "+node.getName() +" is accessible for user.");
					return true;
				}

				boolean hasAccess = false;
				boolean hasRestrictedParent = false;
				
				/**
				 * Examine all parents, if there's one parent where the groups match
				 * the user has access to that node
				 */
				
				for (Node parent : parents) {
					
					if (!parent.getGroups().isEmpty()) {
						
						hasRestrictedParent = true;
						
						if(groupsMatch(user.getGroups(), parent.getGroups()))
							hasAccess = true;
						
					}
					
				}
				
				/**
				 *  hasAccess is false, but there are no restricted parents,
				 *  so the user can access this node
				 */
				
				if (!hasAccess && !hasRestrictedParent)
					hasAccess = true;

				return hasAccess;
			}
			
			log.info("Node "+node.getName() +" is not accessible for user.");
			
			return false;
		}
		
	}
	
	
	/**
	 * Checks if there's a match between two sets of groups
	 * 
	 * @param userGroups
	 * @param nodeGroups
	 * @return
	 */
	public static boolean groupsMatch(Set<Usergroup> userGroups, Set<Usergroup> nodeGroups) {
		
		boolean match = false;
		
		for (Usergroup group : userGroups) {
			
			if (nodeGroups.contains(group)) {
				match = true;
			}
			
		}

		return match;
	}

	public static void deleteNode(Long id) {
		Node node = (Node) DAOManager.getInstance().getNodeDAO().findByID(id);

		log.debug("Deleting node with id " + node.getId());

		DAOManager.getInstance().getNodeDAO().delete(node);
	}
	
	public static String doXMLExport() {

		String rootDirectory = config.getBasepath();
		String exportPath = "export/";
		
		StringBuffer dateBuffer = new StringBuffer();

		log.debug("Importing from path " + rootDirectory + exportPath);

		try {

			List<?> rootNodes = DAOManager.getInstance().getNodeDAO().findAllRootNodes();

			Iterator<?> rootNodesIterator = rootNodes.iterator();

			while (rootNodesIterator.hasNext()) {
				Node node = (Node) rootNodesIterator.next();

				NodeBean nodeBean = new NodeBean();

				if (node.getName() != null) {
					log.debug("Found root node :" + node.getPath());

					nodeBean.setId(node.getId());
					nodeBean.setName(node.getName());
					nodeBean.setParent(null);
					nodeBean.setPluginid(node.getPluginid());
					nodeBean.setPluginparams(node.getPluginparams());
					nodeBean.setFirstchild(node.getFirstchild());
					nodeBean.setPosition(node.getPosition());
					nodeBean.setType(node.getType());
					nodeBean.setImage(node.getImage());

					if (node.getContent() != null) {
						ContentBean contentBean = new ContentBean();
						Content content = node.getContent();

						contentBean.setContentname(content.getContentname());
						contentBean.setContentstring(content.getContentstring());
						contentBean.setDescription(content.getDescription());
						contentBean.setId(content.getId());
						contentBean.setPagename(content.getPagename());
						contentBean.setLastmodified(content.getLastmodified());
						contentBean.setCreated(content.getCreated());
						contentBean.setMetatags(content.getMetatags());
						contentBean.setCommentsAllowed(content.isCommentsAllowed());
						
						nodeBean.setContentBean(contentBean);
					}
				}

				loadChildNodeBeansFromNode(node, nodeBean);

				try {
					FileOutputStream fos = new FileOutputStream(config.getBasepath() + exportPath + "node_" + nodeBean.getId()
							+ ".xml");
					XMLEncoder xmlEncoder = new XMLEncoder(fos);
					xmlEncoder.writeObject(nodeBean);
					xmlEncoder.close();
					fos.close();

					log.debug("Wrote node to file " + config.getBasepath() + exportPath + "node_" + nodeBean.getId() + ".xml");

				} 
				catch (FileNotFoundException e) {
					e.printStackTrace();
				} 
				catch (IOException e) {
					e.printStackTrace();
				}

			}

			ZipUtils.zipDir(config.getBasepath() + exportPath+"content.zip", config.getBasepath() + config.getContentpath());
			
			File file = new File(rootDirectory + exportPath);

			ArrayList<DirectoryObject> fileList;

			try {
				fileList = org.pmedv.util.FileHelper.traverse(file, new DirectoryPrintVisitor(), new FileFilter() {
					public boolean accept(File pathname) {
						if (pathname.getName().endsWith(".xml") || pathname.getName().endsWith("content.zip"))
							return true;
						else
							return false;
					}
				});
			} 
			catch (IllegalArgumentException i) {
				fileList = new ArrayList<DirectoryObject>();
			}

			dateBuffer.append(CalendarUtils.getYear());

			if (CalendarUtils.getMonth() > 10)
				dateBuffer.append(CalendarUtils.getMonth());
			else
				dateBuffer.append("0" + CalendarUtils.getMonth());
			if (CalendarUtils.getDay() > 10)
				dateBuffer.append(CalendarUtils.getDay());
			else
				dateBuffer.append("0" + CalendarUtils.getDay());

			ZipUtils.createZipArchive("nodes_" + dateBuffer.toString() + ".zip", rootDirectory + exportPath, rootDirectory
					+ exportPath, fileList);
			log.debug("Exporting to path " + rootDirectory + config.getImportpath());

			for (Iterator<DirectoryObject> it = fileList.iterator(); it.hasNext();) {
				DirectoryObject d = (DirectoryObject) it.next();

				/*
				 * Delete all xml files
				 */
				
				if (!d.getType().equalsIgnoreCase("directory") && !d.getName().endsWith("zip")) {
					
					File f = new File(rootDirectory + exportPath + d.getName());					
					f.delete();
					
				}
			}

		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		return dateBuffer.toString();
	}

	/**
	 * Loads all child nodes recursively from a <code>Node</code> into a <code>NodeBean</code>
	 * 
	 * @param node the node to load the child nodes from
	 * @param nodeBean the node bean to store the children into
	 */
	public static void loadChildNodeBeansFromNode(Node node, NodeBean nodeBean) {

		if (!node.getChildren().isEmpty() && node.getChildren() != null) {

			Set<Node> childNodes = node.getChildren();

			Iterator<Node> childNodesIter = childNodes.iterator();

			ArrayList<NodeBean> childBeans = new ArrayList<NodeBean>();

			while (childNodesIter.hasNext()) {
				Node subNode = (Node) childNodesIter.next();

				NodeBean subNodeBean = new NodeBean();

				if (subNode.getName() != null) {
					
					log.debug("Found child node : " + subNode.getPath());
					subNodeBean.setId(subNode.getId());
					subNodeBean.setRemoteId(subNode.getId());
					subNodeBean.setName(subNode.getName());
					subNodeBean.setParent(nodeBean);
					subNodeBean.setPluginid(subNode.getPluginid());
					subNodeBean.setPluginparams(subNode.getPluginparams());
					subNodeBean.setPosition(subNode.getPosition());
					subNodeBean.setType(subNode.getType());
					subNodeBean.setImage(subNode.getImage());
					
					ArrayList<UsergroupBean> groups = new ArrayList<UsergroupBean>();
					
					for (Usergroup group : node.getGroups()) {
						
						UsergroupBean ugb = new UsergroupBean();
						
						ugb.setDescription(group.getDescription());
						ugb.setId(group.getId());
						ugb.setName(group.getName());
						
						groups.add(ugb);
					}
					
					subNodeBean.setGroups(groups);

					if (subNode.getContent() != null) {
						
						ContentBean contentBean = new ContentBean();
						Content content = subNode.getContent();

						contentBean.setContentname(content.getContentname());
						contentBean.setContentstring(content.getContentstring());
						contentBean.setDescription(content.getDescription());
						contentBean.setId(content.getId());
						contentBean.setPagename(content.getPagename());
						contentBean.setMetatags(content.getMetatags());
						contentBean.setCommentsAllowed(content.isCommentsAllowed());
						
						subNodeBean.setContentBean(contentBean);
					}

					childBeans.add(subNodeBean);
				}

				if (!subNode.getChildren().isEmpty())
					loadChildNodeBeansFromNode(subNode, subNodeBean);

			}
			nodeBean.setChildren(childBeans);

		}

	}

	/**
	 * Exports all children recursively of a <code>Node</code> into ststic html files.
	 * 
	 * @param node the node to export
	 */
	public static void staticExportChildren(Node node) {

		String exportPath = config.getBasepath() + "export/";

		File f = new File(exportPath + node.getPath());
		
		if (!node.getChildren().isEmpty()) {

			if (!f.exists()) {
				log.info("creating directory : " + exportPath + node.getPath());
				if (!f.mkdir()) {
					log.info("Could not create child directory " + exportPath + node.getPath());
				}
			}

			Set<Node> childNodes = node.getChildren();

			Iterator<Node> childNodesIter = childNodes.iterator();

			while (childNodesIter.hasNext()) {
				Node subNode = (Node) childNodesIter.next();

				if (subNode.getName() != null) {
					log.debug("Found child node : " + subNode.getPath());

					StringBuffer urlBuffer = new StringBuffer();

					urlBuffer.append(CMSProperties.getInstance().getAppProps().getProperty("protocol")+"://");
					urlBuffer.append(config.getHostname());
					urlBuffer.append("/");
					urlBuffer.append(config.getLocalPath());

					// Remove whitespaces from URLs

					urlBuffer.append(StringUtil.replace(subNode.getPath(), " ", "%20"));
					urlBuffer.append(".html");

					log.info("connecting to: " + urlBuffer.toString());

					String content = URLReader.getDefaultContent(urlBuffer.toString(),null);

					content = convertChildLocations(content, imageTagFilter, converters, node);
					content = convertChildLocations(content, linkTagFilter, converters, node);

					f = new File(exportPath + subNode.getPath() + ".html");
					
					try {
						FileUtils.writeStringToFile(f, content);	
					}
					catch (IOException e) {
						log.info("Could not write node to : " + exportPath + subNode.getPath() + ".html");
					}	

				}

				if (!subNode.getChildren().isEmpty())
					staticExportChildren(subNode);

			}

		}

	}

	/**
	 * Loads all child nodes recursively from a <code>NodeBean</code> into a <code>Node</code> in order to store
	 * the node structure into the database.
	 * 
	 * @param node
	 * @param nodeBean
	 */
	public static void loadChildNodesFromNodeBean(Node node, NodeBean nodeBean, boolean create) {

		if (nodeBean.getChildren() == null)
			return;

		if (!nodeBean.getChildren().isEmpty()) {

			Iterator<NodeBean> childNodesIter = nodeBean.getChildren().iterator();

			// ArrayList<Node> children = new ArrayList<Node>();

			SortedSet<Node> children = new TreeSet<Node>();

			while (childNodesIter.hasNext()) {
				NodeBean subNodeBean = (NodeBean) childNodesIter.next();

				Node subNode = new Node();

				if (subNodeBean.getName() != null) {
					
					log.debug("Found child node : " + subNodeBean.getName());
					// subNode.setId(subNodeBean.getRemoteId());
					subNode.setName(subNodeBean.getName());
					subNode.setParent(node);
					subNode.setPluginid(subNodeBean.getPluginid());
					subNode.setPluginparams(subNodeBean.getPluginparams());
					subNode.setPosition(subNodeBean.getPosition());
					subNode.setType(subNodeBean.getType());
					subNode.setImage(subNodeBean.getImage());

					if (subNodeBean.getContentBean() != null) {

						Content content = new Content();						
						
						ContentBean contentBean = subNodeBean.getContentBean();
						content.setContentname(contentBean.getContentname());
						content.setContentstring(contentBean.getContentstring());
						content.setDescription(contentBean.getDescription());
						// content.setId(contentBean.getId());
						content.setPagename(contentBean.getPagename());
						content.setMetatags(contentBean.getMetatags());
						content.setCommentsAllowed(contentBean.isCommentsAllowed());

						log.debug("Found content : " + content.getContentname());

						if (create)
							DAOManager.getInstance().getContentDAO().createAndStore(content);

						subNode.setContent(content);

					}

					children.add(subNode);

					// Create and store

					if (create)
						DAOManager.getInstance().getNodeDAO().createAndStore(subNode);
				}

				if (subNodeBean.getChildren() != null)
					if (!subNodeBean.getChildren().isEmpty())
						loadChildNodesFromNodeBean(subNode, subNodeBean, create);

			}

			node.getChildren().addAll(children);

		}

	}

	private static class ImageLinkConverter implements PathConverter {

		@Override
		public String convert(String url, Node node) {
			log.info("converting image url " + url + " for node" + node.getPath());
			return url;
		}

	}

	private static class LinkConverter implements PathConverter {

		@Override
		public String convert(String url, Node node) {

			String basePath = CMSProperties.getInstance().getAppProps().getProperty("protocol")+
				"://" + config.getHostname() + "/" + config.getLocalPath();
			
			StringBuffer pathExt = new StringBuffer();
			log.info("current node " + node.getPath());

			StringTokenizer st = new StringTokenizer(node.getPath(), "/");
			int numTokens = st.countTokens();

			for (int i = 0; i < numTokens; i++) {
				pathExt.append("../");
			}

			String newPath = pathExt.toString();

			String newUrl;

			newUrl = StringUtil.replace(url, basePath, newPath);

			log.info("old url is : " + url);
			log.info("new url is : " + newUrl);

			// log.info("converting link url "+url+" for node "+node.getPath()+" tokens : "+numTokens);
			// log.info("new url is : "+newUrl);

			return newUrl;
		}

	}

	private static class RootLinkConverter implements PathConverter {

		@Override
		public String convert(String url, Node node) {

			String basePath = CMSProperties.getInstance().getAppProps().getProperty("protocol")+
				"://" + config.getHostname() + "/" + config.getLocalPath();

			return StringUtil.replace(url, basePath, "./");
		}

	}

	private static String convertChildLocations(String content, NodeFilter tagFilter, ArrayList<PathConverter> converters,
			Node node) {

		Parser parser = new Parser();

		try {

			parser.setInputHTML(content);
			parser.setFeedback(Parser.STDOUT);

			NodeList nodeList = parser.parse(tagFilter);

			for (NodeIterator e = nodeList.elements(); e.hasMoreNodes();) {

				org.htmlparser.Node currentNode = e.nextNode();

				if (currentNode instanceof ImageTag) {

					ImageTag img = (ImageTag) currentNode;

					if (img.getAttribute("src") != null) {
						img.setAttribute("src", converters.get(CONVERT_IMAGE).convert(img.getAttribute("src"), node));
					}

				}

				else if (currentNode instanceof LinkTag) {

					LinkTag link = (LinkTag) currentNode;

					if (link.getAttribute("href") != null) {
						content = StringUtil.replace(content, link.getAttribute("href"), converters.get(CONVERT_LINK).convert(
								link.getAttribute("href"), node));

					}

				}

			}

		} catch (ParserException e1) {
			log.info("Parser could not parse content, exception occured.");
		}

		return content;
	}

	/**
	 * 
	 * Converts all links of a node to a form that allows to use
	 * the node local, this method is only needed for a static export.
	 * 
	 * @param content       
	 * @param tagFilter
	 * @param converters
	 * @param node
	 * @return
	 */

	public static String convertRootLocations(String content, NodeFilter tagFilter, ArrayList<PathConverter> converters, Node node) {

		Parser parser = new Parser();

		try {

			parser.setInputHTML(content);
			parser.setFeedback(Parser.STDOUT);

			NodeList nodeList = parser.parse(tagFilter);

			for (NodeIterator e = nodeList.elements(); e.hasMoreNodes();) {

				org.htmlparser.Node currentNode = e.nextNode();

				if (currentNode instanceof ImageTag) {

					ImageTag img = (ImageTag) currentNode;

					if (img.getAttribute("src") != null) {
						img.setAttribute("src", converters.get(CONVERT_IMAGE).convert(img.getAttribute("src"), node));
					}

				}

				else if (currentNode instanceof LinkTag) {

					LinkTag link = (LinkTag) currentNode;

					if (link.getAttribute("href") != null) {
						content = StringUtil.replace(content, link.getAttribute("href"), converters.get(CONVERT_ROOT_LINK)
								.convert(link.getAttribute("href"), node));

					}

				}

			}

		} catch (ParserException e1) {
			log.info("Parser could not parse content, exception occured.");
		}

		return content;
	}

	/**
	 * Triggers a static export to html files.
	 */
	public static void doStaticExport(ServletRequest request) {
		
		String exportPath = config.getBasepath() + "export/";

		try {

			List<?> rootNodes = DAOManager.getInstance().getNodeDAO().findAllRootNodes();

			Iterator<?> rootNodesIterator = rootNodes.iterator();

			while (rootNodesIterator.hasNext()) {
				Node node = (Node) rootNodesIterator.next();
				NodeHelper.staticExportChildren(node);

			}

		} 
		catch (Exception e) {
			e.printStackTrace();
		}

		try {

			List<?> rootNodes = DAOManager.getInstance().getNodeDAO().findAllRootNodes();

			Iterator<?> rootNodesIterator = rootNodes.iterator();

			while (rootNodesIterator.hasNext()) {
				Node node = (Node) rootNodesIterator.next();

				if (node.getName() != null) {
					log.debug("Found root node :" + node.getPath());

					StringBuffer urlBuffer = new StringBuffer();

					urlBuffer.append(CMSProperties.getInstance().getAppProps().getProperty("protocol")+"://");
					urlBuffer.append(ServerUtil.getHostUrl(request));
					urlBuffer.append("/");
					urlBuffer.append(config.getLocalPath());

					// Remove whitespaces from URLs

					urlBuffer.append(StringUtil.replace(node.getPath(), " ", "%20"));
					urlBuffer.append(".html");

					log.info("connecting to: " + urlBuffer.toString());

					String content = URLReader.getDefaultContent(urlBuffer.toString(),null);

					content = NodeHelper.convertRootLocations(content, linkTagFilter, converters, node);

					File f = new File(exportPath + node.getPath() + ".html");

					try {
						FileUtils.writeStringToFile(f, content);	
					}
					catch (IOException e) {
						log.info("Could not write node to : " + exportPath + node.getPath() + ".html");
					}	

				}

			}

		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	
	/**
	 * Triggers an import from an uploaded zip files containing NodeBeans
	 * 
	 * @param replace should be set to true if all existing nodes should be replaced
	 * 
	 * @return
	 */
	public static boolean doImport(boolean replace) {

		log.debug("Starting import of nodes.");

		File importFile = null;
		
		String rootDirectory = config.getBasepath();
		
		File[] importFiles = new File(rootDirectory + config.getImportpath()).listFiles();
		
		/**
		 * Now check, if we have any input file. Since this directory is always emptied after
		 * the import, there can be only the recently uploaded file.
		 * 
		 */
		
		for (int i = 0; i < importFiles.length; i++) {
			
			if (!importFiles[i].isDirectory() && importFiles[i].getName().startsWith("nodes"))
				importFile = importFiles[i];
		}

		if (importFile == null) return false; // No file found, return 
		
		try {
			// FileUtils.extractZipFile(importFile.getAbsolutePath(), rootDirectory + config.getImportpath());
			ZipUtils.extractZipArchive(importFile.getAbsolutePath(), rootDirectory + config.getImportpath());
			
			File test = new File(rootDirectory + config.getImportpath()+"content.zip");
			
			if (test.exists())
				ZipUtils.extractZipArchive(rootDirectory + config.getImportpath()+"content.zip",rootDirectory+config.getContentpath());
			
		} 
		catch (Exception e1) {
			return false;
		}

		File file = new File(rootDirectory + config.getImportpath());

		log.debug("Trying to import from " + rootDirectory + config.getImportpath());

		ArrayList<DirectoryObject> importDirectoryContents;

		try {
			importDirectoryContents = org.pmedv.util.FileHelper.traverse(file, new DirectoryPrintVisitor());
		} catch (IllegalArgumentException i) {
			importDirectoryContents = new ArrayList<DirectoryObject>();
		}

		int fileIdx = 1;

		if (replace) {

			List<?> nodes = DAOManager.getInstance().getNodeDAO().findAllItems();

			for (Iterator<?> it = nodes.iterator(); it.hasNext();) {
				Node currentNode = (Node) it.next();

				DAOManager.getInstance().getNodeDAO().delete(currentNode);
			}

			List<?> contents = DAOManager.getInstance().getContentDAO().findAllItems();

			for (Iterator<?> it = contents.iterator(); it.hasNext();) {
				Content currentContent = (Content) it.next();

				DAOManager.getInstance().getContentDAO().delete(currentContent);
			}

		}

		for (Iterator<DirectoryObject> fileIterator = importDirectoryContents.iterator(); fileIterator.hasNext();) {

			DirectoryObject dirObj = (DirectoryObject) fileIterator.next();

			if (dirObj.getName().startsWith("node") && dirObj.getName().endsWith(".xml")) {

				String id = dirObj.getName().substring(dirObj.getName().lastIndexOf("_") + 1, dirObj.getName().lastIndexOf("."));

				log.debug("Found node to import with id " + id + " : " + dirObj.getName());

				try {
					FileInputStream fis = new FileInputStream(config.getBasepath() + config.getImportpath() + "node_" + id + ".xml");
					XMLDecoder xmlDecoder = new XMLDecoder(fis);
					NodeBean nodeBean = (NodeBean) xmlDecoder.readObject();
					xmlDecoder.close();
					fis.close();

					log.debug("Found node with id : " + nodeBean.getId());
					log.debug("Name : " + nodeBean.getName());

					Node node = new Node();

					if (nodeBean.getContentBean() != null) {
						Content content = new Content();

						content.setContentname(nodeBean.getContentBean().getContentname());
						content.setDescription(nodeBean.getContentBean().getDescription());
						content.setLastmodified(new Date());
						content.setPagename(nodeBean.getContentBean().getPagename());
						content.setContentstring(nodeBean.getContentBean().getContentstring());
						content.setLastmodified(nodeBean.getContentBean().getLastmodified());
						content.setCreated(nodeBean.getContentBean().getCreated());
						content.setMetatags(nodeBean.getContentBean().getMetatags());
						content.setCommentsAllowed(nodeBean.getContentBean().isCommentsAllowed());
						
						DAOManager.getInstance().getContentDAO().createAndStore(content);

						node.setContent(content);

						log.debug("Found content : " + content.getContentname());
					} 
					else
						node.setContent(null);

					if (nodeBean.getType() == Node.TYPE_PLUGIN) {
						node.setPluginparams(nodeBean.getPluginparams());
						node.setPluginid(nodeBean.getPluginid());
					}
					node.setName(nodeBean.getName());
					node.setPosition(nodeBean.getPosition());
					node.setType(nodeBean.getType());
					node.setFirstchild(nodeBean.getFirstchild());
					node.setImage(nodeBean.getImage());

					// create and store

					DAOManager.getInstance().getNodeDAO().createAndStore(node);
					loadChildNodesFromNodeBean(node, nodeBean, true);

				} 
				catch (FileNotFoundException e) {
					return false;
				} 
				catch (IOException e) {
					return false;
				}
				fileIdx++;
			}
		}

		/** 
		 * Now delete all files inside the import directory
		 */
		
		importFiles = new File(rootDirectory + config.getImportpath()).listFiles();
		
		for (int i = 0; i < importFiles.length; i++) {
			
			if (!importFiles[i].isDirectory()) 
				importFiles[i].delete();
		}

		
		return true;
	}	
	
	/**
	 * Moves a node up inside the tree by its position
	 * 
	 * @param id
	 */
	public static void moveUp(Long id) {
		
		Node node = (Node) DAOManager.getInstance().getNodeDAO().findByID(id);

		Node previousNode;

		if (node.isRootNode()) {
			previousNode = (Node) DAOManager.getInstance().getNodeDAO().getPreviousRootNode(node.getPosition());
		} 
		else {
			previousNode = (Node) DAOManager.getInstance().getNodeDAO().getPreviousChildNode(node.getPosition(),
					node.getParent().getId());
		}

		/*
		 * If there's no previous node, we do nothing
		 */

		if (previousNode != null) {
			
			int oldpos = node.getPosition();
			int newpos = previousNode.getPosition();
			
			node.setPosition(newpos);
			previousNode.setPosition(oldpos);

			if (node.getPosition() != previousNode.getPosition()) {
				DAOManager.getInstance().getNodeDAO().update(node);
				DAOManager.getInstance().getNodeDAO().update(previousNode);
			}
		}
	}	
	
	/**
	 * Moves a node down inside the tree by its position 
	 * 
	 * @param id
	 */
	public static void moveDown(Long id) {
		
		Node node = (Node) DAOManager.getInstance().getNodeDAO().findByID(id);

		Node nextNode;

		if (node.isRootNode()) {
			nextNode = (Node) DAOManager.getInstance().getNodeDAO().getNextRootNode(node.getPosition());
		} else {
			nextNode = (Node) DAOManager.getInstance().getNodeDAO()
					.getNextChildNode(node.getPosition(), node.getParent().getId());
		}

		if (nextNode != null) {

			int oldpos = node.getPosition();
			int newpos = nextNode.getPosition();
			node.setPosition(newpos);
			nextNode.setPosition(oldpos);

			if (node.getPosition() != nextNode.getPosition()) {
				DAOManager.getInstance().getNodeDAO().update(node);
				DAOManager.getInstance().getNodeDAO().update(nextNode);
			}

		}
	}
	
	/**
	 * Performs an import of an open office file into the node tree
	 */
	public static void doODTImport() {

		log.debug("Starting import of odt.");
		
		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);
		
		String RootDirectory   = config.getBasepath();
		String importFilePath  = RootDirectory + config.getImportpath() + "document.odt";
		String contentFilePath = RootDirectory + config.getImportpath() + "content.xml";

		try {
			ZipUtils.extractZipArchive(importFilePath, RootDirectory + config.getImportpath());
			SiteHelper.createContentFromODT(new File(contentFilePath), "document");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Gets the content of a node
	 * 
	 * @param nodeId the id of the node to get the content from
	 * @return
	 */
	public static String getContent(Long nodeId) {
		
		Node node = null;
		String contentString = null;

		try {
			log.debug("fetching content from node with id : " + nodeId);
			node = (Node) DAOManager.getInstance().getNodeDAO().findByID(nodeId);

		} 
		catch (NumberFormatException e) {

			log.debug("node_id "+ nodeId + " is not a a valid node id.");
			log.debug("Trying to find node by name.");

			String name = String.valueOf(nodeId);
			log.debug("fetching content from node with name : " + name);
			node = (Node) DAOManager.getInstance().getNodeDAO().findByName(name);
		}

		if (node != null) {

			Content content = node.getContent();

			if (content != null) {

				contentString = node.getContent().getContentstring();
				log.debug("Found content with name : " + node.getContent().getContentname());

			} else {
				contentString = "<p>Node : " + node.getName() + "Plugin : " + node.getPluginid();
			}

		} 
		else {
			log.debug("The node with name " + nodeId + " could not be found.");
		}
		
		return contentString;
		
	}

	/**
	 * Locates a node by its name or its full path. This method is needed in order
	 * to resolve a given path from an url.
	 * 
	 * @param name the name or the path of the node to be found.
	 * 
	 * @return the found node or null if nothing was found
	 * 
	 */
	public static Node locateNodeByName(String name) {
		
		if (name == null) {
			
			Node node = new Node();
			
			node.setName("None");
			node.setType(Node.TYPE_CONTENT);
			
			Content content = new Content();
			
			content.setContentname("None");
			content.setContentstring("None");

			node.setContent(content);
			
			return node;
			
		}
		
		Content content = null;
		Node node = null;
		
		/*
		 * And now we check if a single node or a path was given, if the path
		 * was given, there must be a "/" inside. We need the parent name, to
		 * identify the needed node.
		 */

		String[] pathElements = name.split("/");

		for (int i = 0; i < pathElements.length; i++) {
			log.debug("Found path element :" + pathElements[i]);
		}

		List<?> similarNodes = DAOManager.getInstance().getNodeDAO()
				.findElementsByName(pathElements[pathElements.length - 1]);
		
		for (Object o : similarNodes) {

			Node n = (Node) o;
			
			log.debug("Found similar node    : " + n.getName());
			log.debug("Similar node has Path : " + n.getPath());				
			log.debug("Requested path is     : " + name);
			
			if (name.equals(n.getPath())) {
				node = (Node) DAOManager.getInstance().getNodeDAO().findByID(n.getId());
				log.debug("Found matching node : "+n.getId()+" - "+n.getName());
			}
			
		}

		if (node == null) {

			content = new Content();
			content.setDescription(resources.getString("content.error.description"));
			content.setContentstring(resources.getString("content.error.contentstring"));
			content.setContentname(resources.getString("content.error.contentname"));

			node = new Node();

			node.setChildren(null);
			node.setContent(content);
			node.setParent(null);
			node.setName(resources.getString("content.error.contentname"));
			node.setType(Node.TYPE_CONTENT);
			
		} 	
		
		return node;
	}
	
	/**
	 * Performs a fulltext search with a given search string within all existing text nodes and all existent
	 * plugin nodes. This method is needed because the nodes are not indexed at all.
	 * 
	 * @param searchString  the string to search for
	 * @param request       the current servlet request (this is needed because the plugins have to be initalized with the current request)
	 * @param loader	    the current classloader (this is needed because the plugins have to be initalized with the current classloader)
	 * @param username		the username is needed for skipping restricted nodes
	 * 
	 * @return				a list of matching nodes if there are any.
	 */
	public static final List<?> findMatchingNodes(String searchString, HttpServletRequest request, HttpServletResponse response,ClassLoader loader, String username) {
	
		List<?> foundNodes;

		boolean multiDomain = ServerUtil.isMultiDomainEnabled(config); 
		
		foundNodes = DAOManager.getInstance().getNodeDAO().findAllItems();

		log.info("Found " + foundNodes.size() + " nodes.");

		if (!PluginHelper.isInitialized())
			PluginHelper.initIPluginClasses(loader);

		// First eliminate all nodes, which don't contain our search string

		try {
			for (Iterator<?> nodeIterator = foundNodes.iterator(); nodeIterator.hasNext();) {
				
				Node currentNode = (Node) nodeIterator.next();

				if (currentNode.getType() == Node.TYPE_CONTENT) {

					log.info("examining node : " + currentNode.getName());
					log.info("node has content : " + currentNode.getContent().getContentname());

					if (currentNode.getContent().getContentstring() == null) {
						nodeIterator.remove();
					} 
					else {
						if (!currentNode.getContent().getContentname().contains(searchString)
								&& !currentNode.getContent().getContentstring().contains(searchString)
								&& !currentNode.getName().contains(searchString)
								|| (multiDomain && !currentNode.getPath().startsWith(request.getServerName()+"/"))) {
								log.info("removing node : "+currentNode.getName());
								nodeIterator.remove();
						}													
					}
				}
				else if (currentNode.getType() == Node.TYPE_PLUGIN) {
					
					// TODO : make file browser plugins searchable
					if (currentNode.getPluginid().startsWith("org.pmedv.plugins.File")) {
						nodeIterator.remove();
						continue;
					}
					
					Content content = PluginHelper.getPluginContent(currentNode, request, response);
					
					if (content != null) {
						if (!content.getContentname().contains(searchString)
								&& !content.getContentstring().contains(searchString)
								&& !currentNode.getName().contains(searchString)
								|| (multiDomain && !currentNode.getPath().startsWith(request.getServerName()+"/"))) {
								log.info("removing node : "+currentNode.getName());
								nodeIterator.remove();
						}													
						
					}
					
				}
				// TODO : Add behaviour for link type node.
				else nodeIterator.remove();

				
			}
		} 
		catch (Exception e) {
			log.info("Error occured while searching.");
			e.printStackTrace();
		}

		log.info("Found " + foundNodes.size() + " nodes which fit search criteria.");

		// Now eliminate all nodes that are not associated to the current usergroup

		User user = null;
		
		if (username != null) {
			user = (User) DAOManager.getInstance().getUserDAO().findByName(username);	
		}

		for (Iterator<?> nodeIterator = foundNodes.iterator(); nodeIterator.hasNext();) {
			Node node = (Node)nodeIterator.next();			
			if (!hasAccessToNode(node, user)) {			
				nodeIterator.remove();				
			}
		}				
		
		return foundNodes;
	}
	
	public static void getChildNodes(ArrayList<Node> nodes, Node node) {
		
		if (node.getChildren() != null)
		
		for (Node child : node.getChildren()) {
			nodes.add(child);
			getChildNodes(nodes, child);
		}
		
	}
	
}
