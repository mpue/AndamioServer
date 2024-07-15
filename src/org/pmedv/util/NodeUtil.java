/**

	Weberknecht AndamioManager - Open Source Content Management
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

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.pmedv.beans.objects.NodeBean;
import org.pmedv.beans.objects.NodeRepository;


/**
 * This utility class provides some convenient helper methods for the node
 * system. 
 * 
 * @author Matthias Pueski (July 2009)
 */
public class NodeUtil {

	private static final Log			log				= LogFactory.getLog(NodeUtil.class);
	private static final String			PATH_DELIMITER	= "/";

	public static ArrayList<NodeBean>	nodes;
	
	
	/**
	 * Compares two nodes recursively and generates a list of changes between
	 * all children.
	 * 
	 * @param n1 The left node
	 * @param n2 The right node
	 * 
	 * @return an array list of <code>DifferenceEntry</code>
	 */
	
	public static ArrayList<DifferenceEntry> compareNodes(NodeBean n1, NodeBean n2) {
		
		ArrayList<DifferenceEntry> differenceList  = new ArrayList<DifferenceEntry>();
		ArrayList<NodeBean> linearNodeList1 = new ArrayList<NodeBean>(); 
		ArrayList<NodeBean> linearNodeList2 = new ArrayList<NodeBean>();
		
		loadChildren(linearNodeList1, n1);
		loadChildren(linearNodeList2, n2);

		for (NodeBean rightNode : linearNodeList2) {

			if (!linearNodeList1.contains(rightNode)) {
				differenceList.add(new DifferenceEntry(rightNode,DifferenceType.NEW));
			}
			else {
			
				for (Iterator<NodeBean> it = linearNodeList1.iterator();it.hasNext();) {
					
					NodeBean current = it.next();
					
					if (current.equals(rightNode)) {
						
						if (current.getContentBean() != null && rightNode.getContentBean() != null) {
						
							if (!current.getContentBean().getContentstring().equals(rightNode.getContentBean())) {
								differenceList.add(new DifferenceEntry(rightNode,DifferenceType.CHANGED));
							}
						}
						
					}
					
				}
				
			}
			
		}
		
		return differenceList;
		
	}
	
	/**
	 * Loads the children of any node recursively into an array list
	 * 
	 * @param nodes an array list to load the nodes to
	 * @param node the node to load the children for
	 */
	public static void loadChildren(ArrayList<NodeBean> nodes,NodeBean node) {

		if (node.getChildren() != null) {

			for (NodeBean currentNode : node.getChildren()) {

				currentNode.setParent(node);

				nodes.add(currentNode);

				if (currentNode.getChildren() != null) {
					loadChildren(nodes,currentNode);
				}

			}

		}

	}	

	/**
	 * <p>
	 * Loads all the children of a specified node into the local node list. This
	 * method creates a backward link from any node to its parent in order to
	 * gather the path for any node. 
	 * </p>
	 * <p>
	 * This method is also useful if you want to have a linear list of the node tree. 
	 * <p>
	 * 
	 * @param node The node to load the children for
	 */
	
	public static void loadChildren(NodeBean node) {

		if (node.getChildren() != null) {

			for (NodeBean currentNode : node.getChildren()) {

				currentNode.setParent(node);

				nodes.add(currentNode);

				if (currentNode.getChildren() != null) {
					loadChildren(currentNode);
				}

			}

		}

	}

	/**
	 * Finds the max child id of a child inside a single node. This methods
	 * searches inside a child node recursively, such that all children and
	 * theri according children are examined.
	 * 
	 * @param node The node to search in.
	 * @return The highest node id
	 */
	
	public static Long findMaxChildId(NodeBean node) {

		Long maxId = 0L;

		if (node.getChildren() != null) {

			for (NodeBean currentNode : node.getChildren()) {

				if (currentNode.getId() > maxId) {
					maxId = currentNode.getId();

					if (currentNode.getChildren() != null) {

						Long maxChildId = findMaxChildId(currentNode);

						if (maxChildId > maxId) {
							maxId = maxChildId;
						}

					}

				}

			}

		}
		else {
			return node.getId();
		}

		return maxId;

	}

	public static NodeBean	foundNode	= null;

	/**
	 * Finds a node by its id inside the node. This method may return the node
	 * itself. After a successful call, the found node is stored inside the
	 * field <code>foundNode</code>. This makes all the recursion stuff much
	 * easier. Just do not forget to reset this field, before querying the
	 * repository.
	 * 
	 * @param node The node to examine
	 * @param id the id of the node to find
	 */
	
	public static void locateById(NodeBean node, Long id) {

		if (node.getId().equals(id))
			foundNode = node;

		if (node.getChildren() != null) {

			for (Iterator<NodeBean> it = node.getChildren().iterator(); it.hasNext();) {

				NodeBean currentNode = it.next();

				if (currentNode.getId().equals(id)) {
					foundNode = currentNode;
				}
				else {
					locateById(currentNode, id);
				}

			}

		}

	}
	
	/**
	 * <p>
	 * Removes all backward parent relations from all children recursively.
	 * </p>
	 * <p>
	 * This method is needed to clean up a node tree before serializing with JAXB
	 * in oder to prevent cyclic hierarchies.
	 * </p>
	 * @param node The node to start from
	 * 	 
	 */
	
	public static void eliminateParentLinks(NodeBean node) {

		if (node.getChildren() != null) {

			for (Iterator<NodeBean> it = node.getChildren().iterator(); it.hasNext();) {

				NodeBean currentNode = it.next();
				currentNode.setParent(null);
				
				eliminateParentLinks(currentNode);
				
			}

		}

	}
	
	/**
	 * <p>
	 * Removes all plugins from a node structure.
	 * </p>
	 * <p>
	 * Notice that, if a plugin node has children that they are removed too!
	 * </p>
	 * 
	 * @param node The node to start from
	 */
	
	public static void removePluginNodes(NodeBean node) {

		if (node.getChildren() != null) {

			for (Iterator<NodeBean> it = node.getChildren().iterator(); it.hasNext();) {

				NodeBean currentNode = it.next();
				
				if (currentNode.getType() == NodeBean.TYPE_PLUGIN)
					removeChildById(node, currentNode.getId());
				
				removePluginNodes(currentNode);
				
			}

		}

	}
	
	/**
	 * Sets all children to published state recursively
	 * 
	 * @param node the node to start from 
	 */
	public static void setAllChildrenPublished(NodeBean node) {

		if (node.getChildren() != null) {

			for (Iterator<NodeBean> it = node.getChildren().iterator(); it.hasNext();) {

				NodeBean currentNode = it.next();				
				currentNode.setPublished(true);
				
				/*
				 * Set remote id to local in order to track the node the right way
				 */
				
				currentNode.setRemoteId(currentNode.getId());
				
				setAllChildrenPublished(currentNode);
				
			}

		}

	}
	
	/**
	 * Removes a specified child from a node, this method affects all childs
	 * recursively.
	 * 
	 * @param node The node to remove the child from
	 * @param id the id of the child to remove
	 */
	
	public static void removeChildById(NodeBean node, Long id) {

		if (node.getChildren() != null) {

			for (Iterator<NodeBean> it = node.getChildren().iterator(); it.hasNext();) {

				NodeBean currentNode = it.next();

				if (currentNode.getId().equals(id)) {
					it.remove();
				}
				else {
					removeChildById(currentNode, id);
				}

			}

		}

	}

	/**
	 * Updates a specified child to its new state
	 * 
	 * @param parent The parent node to update
	 * @param child The child node to update
	 */
	public static void updateChild(NodeBean parent, NodeBean child) {

		if (parent.getChildren() != null) {

			for (Iterator<NodeBean> it = parent.getChildren().iterator(); it.hasNext();) {

				NodeBean currentNode = it.next();

				if (currentNode.getId().equals(child.getId())) {

					currentNode.setFirstchild(child.getFirstchild());
					currentNode.setImage(child.getImage());
					currentNode.setName(child.getName());
					currentNode.setPluginid(child.getPluginid());
					currentNode.setPluginparams(child.getPluginparams());
					currentNode.setPublished(child.isPublished());
					currentNode.setChanged(child.isChanged());
					currentNode.setContentBean(child.getContentBean());
					currentNode.setPosition(child.getPosition());
					currentNode.setRemoteId(child.getRemoteId());
					currentNode.setChildren(child.getChildren());
					currentNode.setType(child.getType());

				}
				else {
					if (currentNode.getChildren() != null)
						updateChild(currentNode, child);
				}

			}

		}

	}

	/**
	 * Checks if a node consists a specific child.
	 * 
	 * @param node
	 * @param child
	 * @return true if the child exists under this parent
	 */
	public static boolean existsAsChild(NodeBean node, NodeBean child) {

		if (node.getChildren() != null) {

			for (NodeBean currentNode : node.getChildren()) {
				if (currentNode.getName().equals(child.getName()))
					return true;
			}

		}
		return false;

	}

	/**
	 * Prints all children of a node to standart out
	 * 
	 * @param node
	 * @param depth
	 */
	public static void printChildren(NodeBean node, int depth) {

		if (node.getChildren() != null) {

			depth++;

			for (NodeBean child : node.getChildren()) {

				for (int i = 0; i < depth; i++)
					System.out.print("----");

				System.out.println(child);

				printChildren(child, depth);

			}
		}

	}

	/**
	 * Prints all children and their contents to standard out
	 * 
	 * @param node
	 * @param depth
	 */
	public static void printChildrenAndContent(NodeBean node, int depth) {

		if (node.getChildren() != null)

			for (NodeBean child : node.getChildren()) {

				System.out.println(child);

				if (child.getContentBean() != null) {
					System.out.println(child.getContentBean().getContentstring());
				}

				printChildren(child, depth);

			}

	}

	/**
	 * <p>
	 * Creates the HTML menu for any given node. This function constructs a table menu with links to the
	 * according nodes (recursively). The nodes are taken from the specified repository.
	 * </p>
	 * <p>
	 * The node for which the menu should be created must be provided, because the menu links of 
	 * each node must correspond to the nodes hierarchy level.
	 * </p>
	 * 
	 * @param repository   The repository to load the nodes from
	 * @param currentNode  The node to create the menu for
	 * @param extension    The extension to append on each link target (e.g. "*.html)	 * 
	 * @param indent       Indicates wheter the child links should be indented or not
	 * 
	 * @return A String containing the html code for the menu 
	 */
	public static String createMenu(NodeRepository repository, NodeBean currentNode,String extension, boolean indent) {

		StringBuffer menuString = new StringBuffer();

		int level = 0;

		menuString.append("<table class=\"menu\">\n");
		
		for (Iterator<NodeBean> it = repository.getNodes().iterator();it.hasNext();) {
			
			NodeBean node = it.next();

			menuString.append("<tr class=\"blogmenuitem\"><td class=\"level" + level + "\" valign=\"middle\">\n");
			
			long hierarchyLevel = NodeUtil.getLevelForNode(currentNode.getId(), repository);
			
			StringBuffer parentPathBuffer = new StringBuffer();
			
			for (int i=0; i < hierarchyLevel;i++) {
				parentPathBuffer.append("../");
			}
			
			menuString.append("<a class=\"mainlevel\" href=\"" + parentPathBuffer + NodeUtil.getPathForNode(node.getId(), repository) + extension
					+ "\" target=\"_self\">");

			// if (selected != null && selected.equals(currentNode.getName()))
			// menuString.append("<b>");

			menuString.append(node.getName());

			// if (selected != null && selected.equals(currentNode.getName()))
			// menuString.append("</b>");

			menuString.append("</a>");
			menuString.append("</td><tr>\n");

			createChildLinks(node, currentNode, menuString, repository, extension, indent, level);
			
		}
		

		menuString.append("</table>\n");

		return menuString.toString();

	}

	/**
	 * Creates all child menu links recursively for a given node
	 * 
	 * @param node         The node to create the child links for
	 * @param currentNode  The current selected node (needed for the correct hierarchy level)
	 * @param menuString   The StringBuffer to write the menu content to
	 * @param repository   The repository to take nodes from
	 * @param extension    The extension to append to the links (e.g. "*.html")
	 * @param indent       Determines if the child nodes should be indented or not
	 * @param level        The current level of hierarchy (0 at start)
	 */
	private static void createChildLinks(NodeBean node, NodeBean currentNode,
			StringBuffer menuString, NodeRepository repository,
			String extension, boolean indent, int level) {

		if (node.getChildren() != null && !node.getChildren().isEmpty()) {

			level++;

			for (NodeBean subNode : node.getChildren()) {

				menuString.append("<tr><td class=\"level" + level + "\">\n");

				if (indent) {

					log.info("Indenting level " + level);

					for (int i = 0; i < level; i++)
						menuString.append("&nbsp;&nbsp;&nbsp;&nbsp;");
				}
				
				long hierarchyLevel = NodeUtil.getLevelForNode(currentNode.getId(), repository);
				
				StringBuffer parentPathBuffer = new StringBuffer();
				
				for (int i=0; i < hierarchyLevel;i++) {
					parentPathBuffer.append("../");
				}
				
				menuString.append("<a class=\"mainlevel\" href=\"" + parentPathBuffer + NodeUtil.getPathForNode(subNode.getId(), repository)
						+ extension + "\" target=\"_self\">");

				// menuString.append("<a href=\""+subNode.getPath()+".html"+"\" target=\"_self\">");

				// if (selected != null && selected.equals(subNode.getName()))
				// menuString.append("<b>");

				menuString.append(subNode.getName());

				// if (selected != null && selected.equals(subNode.getName()))
				// menuString.append("</b>");

				menuString.append("</a>");
				menuString.append("</td><tr>\n");

				createChildLinks(subNode,currentNode, menuString, repository, extension, indent, level);

			}

		}
	}

	/**
	 * Gets the hierarchical level of a node. The hierarchy starts with level 0
	 * 
	 * @param nodeId The node to get the level for
	 * @param repository the repository to search in
	 * 
	 * @return the level of the hierarchy of the node
	 */
	public static long getLevelForNode(Long nodeId, NodeRepository repository) {
		
		long level = 0L;
		
		nodes = new ArrayList<NodeBean>();

		for (Iterator<NodeBean> it = repository.getNodes().iterator(); it.hasNext();) {

			NodeBean rootNode = it.next();

			if (rootNode.getId().equals(nodeId)) {
				return level;
			}
			
			locateById(rootNode, nodeId);
			
			if (foundNode != null) {

				loadChildren(rootNode);

				for (Iterator<NodeBean> nodesIterator = nodes.iterator(); nodesIterator.hasNext();) {

					NodeBean currentNode = nodesIterator.next();

					if (currentNode.getId().equals(nodeId)) {

						while (currentNode.getParent() != null) {
							
							currentNode = currentNode.getParent();							
							level++;
							
						}
						
					}

				}

				foundNode = null;
			}

		}
		
		return level;
		
	}
	
	/**
	 * Creates the hierarchical path for a given node
	 * 
	 * @param nodeId      The id of the node to create the path for
	 * @param repository  The repository from which the node should be taken
	 * 
	 * @return            The path string for the node e.g. parent1/child1/subchild1 ...  and so on
	 */
	
	public static String getPathForNode(Long nodeId, NodeRepository repository) {

		StringBuffer path = new StringBuffer();

		nodes = new ArrayList<NodeBean>();

		for (Iterator<NodeBean> it = repository.getNodes().iterator(); it.hasNext();) {

			NodeBean rootNode = it.next();

			if (rootNode.getId().equals(nodeId)) {
				path.append(rootNode.getName());
				return path.toString();
			}
			
			locateById(rootNode, nodeId);
			
			if (foundNode != null) {

				loadChildren(rootNode);

				for (Iterator<NodeBean> nodesIterator = nodes.iterator(); nodesIterator.hasNext();) {

					NodeBean currentNode = nodesIterator.next();

					if (currentNode.getId().equals(nodeId)) {

						String name = currentNode.getName();

						while (currentNode.getParent() != null) {
							path.insert(0,currentNode.getParent().getName() + PATH_DELIMITER);
							currentNode = currentNode.getParent();
						}

						path.append(name);

					}

				}

				foundNode = null;
			}

		}

		return path.toString();

	}
	
	/**
	 * Replaces all local links to images with the specified targetLocation.
	 * This method is user to pass the node with its images to the server.
	 * 
	 * @param currentBean    The node to externalize the image links for
	 * @param targetLocation the new location for the image.
	 */
	public static ArrayList<String> externalizeImages(NodeBean currentBean,  String targetLocation) 
		throws IllegalArgumentException {
		
		if (currentBean.getContentBean() == null)
			throw new IllegalArgumentException("ContentBean must not be null");
		
		ArrayList<String> imageList = new ArrayList<String>();
		
		NodeFilter imageTagFilter = new NodeClassFilter(ImageTag.class);
		
		Parser parser = new Parser();
		
		try {

			parser.setInputHTML(currentBean.getContentBean().getContentstring());
			parser.setFeedback(Parser.STDOUT);

			NodeList nodeList = parser.parse(imageTagFilter);

			for (NodeIterator e = nodeList.elements(); e.hasMoreNodes();) {

				org.htmlparser.Node currentNode = e.nextNode();

				if (currentNode instanceof ImageTag) {

					ImageTag img = (ImageTag) currentNode;

					if (img.getAttribute("src") != null) {
						
						log.info("Found image location : "+img.getAttribute("src"));
						
						if (img.getAttribute("src").startsWith("file:///")) {
							String imagePath = img.getAttribute("src").substring(8);
							log.info("raw image path is "+imagePath);								
							String imageFileName = imagePath.substring(imagePath.lastIndexOf("/")+1);
							log.info("Image file name is "+imageFileName);
							
							imageList.add(imagePath);
							
							currentBean.getContentBean().setContentstring(
									StringUtil.replace(currentBean
											.getContentBean()
											.getContentstring(), img
											.getAttribute("src"), targetLocation
											+ imageFileName));
							
						}
						
					}

				}

			}

		} 
		catch (ParserException e1) {
			log.info("Parser could not parse content, exception occured.");
		}				
		
		return imageList;
	}

}
