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
package org.pmedv.web.customtags;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.cms.common.Constants.Mode;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.Node;
import org.pmedv.pojos.User;
import org.pmedv.util.NodeHelper;
import org.pmedv.web.ServerUtil;

public class XMenuDisplayer extends TagSupport {

	private static final long serialVersionUID = 2868410646272059205L;
	private static final Log log = LogFactory.getLog(XMenuDisplayer.class);
	private static final ResourceBundle resources = ResourceBundle.getBundle("MessageResources");
	
	private User user = null;
	private boolean indent = false;
	private boolean multiDomains = false;
	private Config config = null;
	
	private Node selectedNode; // the current selected node;

	private String startNode;
	private String mode;
	private String selected;
	private String forceLevelIndent;
	private String orientation;
	private String type;
	private String exclude;

	private String rootNodeOnClickCallback;
	private String childNodeOnClickCallback;
	private String rootNodeOnMouseOverCallback;
	private String childNodeOnMouseOverCallback;	
	private String navigationCallback;
	private String target;

	private Locale locale = null;
	
	public XMenuDisplayer() {
		super();
		log.debug("Created instance of " + this.getClass());
		config = (Config) DAOManager.getInstance().getConfigDAO().findByID(1);
	
		String propertiesLoc = config.getBasepath() + "WEB-INF/application.properties";
		log.info("Loading application.properties from "+propertiesLoc);				
		FileInputStream is = null;
		try {
			is = new FileInputStream(new File(propertiesLoc));
			if (is != null) {			
				Properties p = new Properties();
				p.load(is);			
				String domains = p.getProperty("multiDomains");				
				if (domains != null && domains.equalsIgnoreCase("true")) {
					multiDomains = true;
				}
			} 
		}
		catch (Exception e) {
			log.info("could not load application.properties, proceeding with default configuration for all domains.");
			e.printStackTrace();
		}	
				
		
	}

	public int doStartTag() throws JspException {
		
		if (pageContext.getSession().getAttribute("currentLocale") != null) {			
			locale = (Locale)pageContext.getSession().getAttribute("currentLocale");
		}
		
		selectedNode = (Node) pageContext.getSession().getAttribute("node");
		log.info("Current node " + selectedNode.getPath());

		Config config = (Config) DAOManager.getInstance().getConfigDAO().findByID(1);

		log.info("Force level indent : " + forceLevelIndent);

		if (forceLevelIndent != null && forceLevelIndent.length() > 3) {
			indent = Boolean.valueOf(forceLevelIndent);
		}

		String login = (String) pageContext.getSession().getAttribute("login");

		if (login != null) {
			user = (User) DAOManager.getInstance().getUserDAO().findByName(login);
		}
		else
			user = null;

		try {

			StringBuffer menuString = new StringBuffer();

			if (type != null && type.equalsIgnoreCase("list")) {
				createListMenu(config, menuString);
			}
			else if (type != null && type.equalsIgnoreCase("studiesportal")) {
				createStudiesPortalMenu(config, menuString);
			}
			else {
				createTableMenu(config, menuString);
			}

			pageContext.getOut().print(menuString.toString());
		}
		catch (IOException ioe) {
			log.info("Could not write to servlet output stream : " + ioe.getMessage());
		}

		return EVAL_BODY_INCLUDE;
	}

	private void createStudiesPortalMenu(Config config, StringBuffer menuString) {

		Iterator<?> itemIterator = null;

		String extension = ".html";

		if (startNode != null && startNode.length() > 1) {
			Node start = NodeHelper.locateNodeByName(startNode);

			if (start != null && start.getChildren() != null)
				itemIterator = start.getChildren().iterator();
			else
				return;

		}
		else {
			itemIterator = getRootMenuItems().iterator();
		}

		int category = 0;

		menuString.append("<table class=\"menu\" cellspacing=\"0\" cellpadding=\"0\">");
		menuString.append("\n");

		for (Iterator<?> it = itemIterator; it.hasNext();) {

			Node currentNode = (Node) it.next();

			if (exclude != null) {

				String excludeNodes[] = exclude.split(",");

				if (excludeNodes != null && excludeNodes.length > 1) {

					boolean excluded = false;

					for (int i = 0; i < excludeNodes.length; i++) {

						if (excludeNodes[i].equals(currentNode.getPath()))
							excluded = true;

					}
					if (excluded)
						continue;

				}
				else {
					if (currentNode.getPath().equals(exclude)) {
						continue;
					}
				}

			}

			if (!NodeHelper.hasAccessToNode(currentNode, user))
				continue;

			if (!currentNode.isHidden()) {
				
				String nodePath = currentNode.getPath();
				if (multiDomains)
					nodePath = nodePath.substring(nodePath.indexOf("/")+1);	


				menuString.append("<tr class=\"category\" valign=\"middle\">");
				menuString.append("<td class=\"category_" + category + "\" valign=\"middle\">");
				menuString.append("<p><b><a class=\"category\" href=\"http://" + ServerUtil.getHostUrl(pageContext.getRequest()) + "/"
						+ config.getLocalPath() + nodePath + extension + "\">" + currentNode.getName()
						+ "</a></b></p>");
				menuString.append("\n");
				menuString.append("</td>");
				menuString.append("\n");
				menuString.append("</tr>");
				menuString.append("\n");

			}

			if (currentNode.getChildren() != null) {

				menuString.append("<tr>");
				menuString.append("\n");
				menuString.append("<td>");
				menuString.append("\n");

				if (currentNode.equals(selectedNode) || currentNode.getChildren().contains(selectedNode)
						|| containsStudyItem(currentNode))
					menuString.append("<div id=\"category_children_" + currentNode.getId() + "\" class=\"category_children\">");
				else
					menuString.append("<div id=\"category_children_" + currentNode.getId()
							+ "\" class=\"category_children\" style=\"display : none;\">");

				menuString.append("\n");

				for (Node child : currentNode.getChildren()) {

					if (!NodeHelper.hasAccessToNode(child, user))
						continue;

					menuString.append("<div id=\"category_child_" + child.getId() + "\" class=\"category_child\">");
					menuString.append("\n");

					if (!child.isHidden()) {
						
						String childPath = child.getPath();
						if (multiDomains)
							childPath = childPath.substring(childPath.indexOf("/")+1);	

						if (selected != null && selected.equals(childPath)) {
							menuString.append("<p><a class=\"study\" href=\"http://" + ServerUtil.getHostUrl(pageContext.getRequest()) + "/"
									+ config.getLocalPath() + childPath + extension + "\"><span class=\"selected\">"
									+ child.getName() + "</span></a></p>");
						}
						else {
							menuString.append("<p><a class=\"study\" href=\"http://" + ServerUtil.getHostUrl(pageContext.getRequest()) + "/"
									+ config.getLocalPath() + childPath + extension + "\">" + child.getName() + "</a></p>");
						}

					}

					if (child.getChildren() != null) {

						if (child.equals(selectedNode) || child.getChildren().contains(selectedNode))
							menuString.append("<div id=\"category_child_subchildren_" + child.getId()
									+ "\" class=\"category_child_subchildren\">");
						else
							menuString.append("<div id=\"category_child_subchildren_" + child.getId()
									+ "\" class=\"category_child_subchildren\" style=\"display : none;\">");

						menuString.append("\n");

						for (Node subChild : child.getChildren()) {

							if (!NodeHelper.hasAccessToNode(subChild, user))
								continue;

							menuString.append("<div id=\"category_child_subchild_" + child.getId()
									+ "\" class=\"category_child_subchild\">");
							menuString.append("\n");

							if (!subChild.isHidden()) {

								if (selected != null && selected.equals(subChild.getPath())) {
									menuString.append("<p><a class=\"studyitem\" href=\"http://" + ServerUtil.getHostUrl(pageContext.getRequest()) + "/"
											+ config.getLocalPath() + subChild.getPath() + extension
											+ "\" class=\"leftbox\"><span class=\"selected\">" + subChild.getName()
											+ "</selected></a></p>");
								}
								else {
									menuString.append("<p><a class=\"studyitem\" href=\"http://" + ServerUtil.getHostUrl(pageContext.getRequest()) + "/"
											+ config.getLocalPath() + subChild.getPath() + extension + "\" class=\"leftbox\">"
											+ subChild.getName() + "</a></p>");
								}

							}

							menuString.append("</div>");
							menuString.append("\n");

						}

						menuString.append("</div>");
						menuString.append("\n");

					}

					menuString.append("</div>");
					menuString.append("\n");

				}

				menuString.append("</div>");
				menuString.append("\n");

				menuString.append("</td>");
				menuString.append("\n");
				menuString.append("</tr>");
				menuString.append("\n");

			}

			category++;

		}

		menuString.append("</table>");
		menuString.append("\n");

	}

	private boolean containsStudyItem(Node node) {

		boolean isStudyElement = false;

		end:

		for (Node n : node.getChildren()) {

			if (n.getChildren().contains(selectedNode)) {
				isStudyElement = true;
				break end;
			}

		}

		return isStudyElement;
	}

	private void createTableMenu(Config config, StringBuffer menuString) {

		String extension;
		menuString.append("<table border=\"0\" class=\"menu\">\n");

		int level = 1;

		Iterator<?> itemIterator = null;

		if (startNode != null && startNode.length() > 1) {
			Node start = NodeHelper.locateNodeByName(startNode);
			itemIterator = start.getChildren().iterator();
		}
		else {
			itemIterator = getRootMenuItems().iterator();
		}

		if (orientation != null && orientation.equalsIgnoreCase("horizontal")) {
			menuString.append("<tr class=\"menuitem\">");
		}

		for (Iterator<?> it = itemIterator; it.hasNext();) {

			Node currentNode = (Node) it.next();

			if (exclude != null) {

				String excludeNodes[] = exclude.split(",");

				if (excludeNodes != null && excludeNodes.length > 1) {

					boolean excluded = false;

					for (int i = 0; i < excludeNodes.length; i++) {

						if (excludeNodes[i].equals(currentNode.getPath()))
							excluded = true;

					}
					if (excluded)
						continue;

				}
				else {
					if (currentNode.getPath().equals(exclude)) {
						continue;
					}
				}
			}

			if (NodeHelper.hasAccessToNode(currentNode, user)) {

				if (getMode().equalsIgnoreCase(Mode.BLOG) && currentNode.getType() == Node.TYPE_CONTENT)
					extension = ".blog";
				else
					extension = ".html";

				if (orientation == null || orientation.equalsIgnoreCase("vertical")) {
					menuString.append("<tr class=\"blogmenuitem\">");
				}

				menuString.append("<td class=\"level" + level + "\">\n");
				
				if (currentNode.getType() == Node.TYPE_LINK) {
					
					menuString.append("<a class=\"mainlevel\" href=\"");
					menuString.append(currentNode.getLinkurl());
					menuString.append("\" target=\""+currentNode.getTargetName()+"\">");

					if (selected != null && selected.equals(currentNode.getName()))
						menuString.append("<b>");

					menuString.append(currentNode.getName());

					if (selected != null && selected.equals(currentNode.getName()))
						menuString.append("</b>");

					menuString.append("</a>");
					
				}
				else {
					
					menuString.append("<a class=\"mainlevel\" href=\"http://");
					menuString.append(ServerUtil.getHostUrl(pageContext.getRequest()));
					menuString.append("/");
					menuString.append(config.getLocalPath());
					
					String childpath = currentNode.getPath();
					
					if (multiDomains)
						childpath = childpath.substring(childpath.indexOf("/")+1);				
					
					menuString.append(childpath);
					menuString.append(extension);
					menuString.append("\" target=\"_self\">");

					if (selected != null && selected.equals(currentNode.getName()))
						menuString.append("<b>");

					menuString.append(currentNode.getName());

					if (selected != null && selected.equals(currentNode.getName()))
						menuString.append("</b>");

					menuString.append("</a>");

				}
				
				menuString.append("</td>");

				if (orientation == null || orientation.equalsIgnoreCase("vertical")) {
					menuString.append("</tr>");
				}

				menuString.append("\n");

				if (getMode().equalsIgnoreCase(Mode.WEBSITE) && currentNode.hasChildren())
					loadChildNodesForTable(currentNode, menuString, level, config);
			}
		}

		if (orientation != null && orientation.equalsIgnoreCase("horizontal")) {
			menuString.append("</tr>");
		}

		menuString.append("</table>\n");
	}

	private void createListMenu(Config config, StringBuffer menuString) {

		String extension;

		int level = 0;

		Iterator<?> itemIterator = null;

		if (startNode != null && startNode.length() > 1) {
			log.info("Start node " + startNode);
			Node start = NodeHelper.locateNodeByName(startNode);

			if (start != null && start.hasChildren())
				itemIterator = start.getChildren().iterator();
		}
		else {
			itemIterator = getRootMenuItems().iterator();
		}

		if (itemIterator == null)
			return;

		ArrayList<Node> childNodes = new ArrayList<Node>();
		ArrayList<Node> rootNodes = new ArrayList<Node>();
		

		for (Iterator<?> it = itemIterator; it.hasNext();) {

			Node currentNode = (Node) it.next();
			rootNodes.add(currentNode);			
			NodeHelper.getChildNodes(childNodes, currentNode);

		}

		StringBuffer parameters = new StringBuffer();

		for (int i = 0; i < rootNodes.size(); i++) {

			parameters.append("'" + rootNodes.get(i).getId() + "'");
			parameters.append(",");

		}
		
		for (int i = 0; i < childNodes.size(); i++) {

			parameters.append("'" + childNodes.get(i).getId() + "'");

			if (i < childNodes.size() - 1)
				parameters.append(",");

		}

		if (navigationCallback != null)
			menuString.append("<div id=\"navigation-close-div\" class=\"navigation-close-div\" onMouseOver=\"" + navigationCallback + "([" + parameters
					+ "])\"></div>");
		else
			menuString.append("<div class=\"navigation-close-div\"></div>");

		menuString.append("\n");
		
		menuString.append("<div class=\"navigation-div\" id=\"navigation-div\">");
		menuString.append("\n");
		menuString.append("<div class=\"navigation-h-back\"></div>");
		menuString.append("\n");
		menuString.append("<ul id=\"Navigation\" class=\"Navigation_ul\">");
		menuString.append("\n");
		
		for (Node currentNode : rootNodes) {

			if (exclude != null && currentNode.getName().equals(exclude))
				continue;

			if (NodeHelper.hasAccessToNode(currentNode, user)) {

				if (getMode().equalsIgnoreCase(Mode.BLOG) && currentNode.getType() == Node.TYPE_CONTENT)
					extension = ".blog";
				else
					extension = ".html";

				if (!currentNode.isHidden()) {

					menuString.append(" <li class=\"Navigation_ul_li\">");
					menuString.append("<a href=\"http://");
					menuString.append(ServerUtil.getHostUrl(pageContext.getRequest()));
					menuString.append("/");
					
					String nodePath = currentNode.getPath();
					
					if (multiDomains)
						nodePath = nodePath.substring(nodePath.indexOf("/")+1);	
					
					menuString.append(config.getLocalPath());
					menuString.append(nodePath);
					menuString.append(extension);

					if (target != null)
						menuString.append("&raw=true");

					menuString.append("\"");

					if (target != null) {
						menuString.append(" target=\"");
						menuString.append(target);
						menuString.append("\"");
					}
					else {
						menuString.append(" target=\"_self\"");
					}

					if (rootNodeOnClickCallback != null) {
						menuString.append(" onclick=\"");
						menuString.append(rootNodeOnClickCallback + "(");
						menuString.append("'" + currentNode.getId() + "'");
						menuString.append(",[");

						for (int i = 0; i < rootNodes.size(); i++) {
							menuString.append("'" + rootNodes.get(i).getId() + "'");
							if (i < rootNodes.size() - 1)
								menuString.append(",");
						}

						menuString.append("]");
						menuString.append(");");
						menuString.append("\"");
					}

					if (rootNodeOnMouseOverCallback != null) {
						menuString.append(" onmouseover=\"");
						menuString.append(rootNodeOnMouseOverCallback + "(");
						menuString.append("'" + currentNode.getId() + "'");
						menuString.append(",[");

						for (int i = 0; i < rootNodes.size(); i++) {
							menuString.append("'" + rootNodes.get(i).getId() + "'");
							if (i < rootNodes.size() - 1)
								menuString.append(",");
						}

						menuString.append("]");
						menuString.append(");");
						menuString.append("\"");
					}
					
					
					menuString.append(">");
					
					if (locale != null) {
						
						Node node = NodeHelper.locateNodeByName(currentNode.getPath()+"_"+locale.toString());						

						if (!node.getName().equals(resources.getString("content.error.contentname"))) 
							currentNode.setDisplayName(node.getDisplayName());
							
					}
					
					String name = null;
					
					if (currentNode.getDisplayName() != null)
						name = currentNode.getDisplayName();
					else
						name = currentNode.getName();

					if (selected != null && selected.equals(currentNode.getName()))
						menuString.append("<b>");

					menuString.append(name);

					if (selected != null && selected.equals(currentNode.getName()))
						menuString.append("</b>");

					menuString.append("</a>");

					menuString.append("\n");
					
					
				}
				

				if (getMode().equalsIgnoreCase(Mode.WEBSITE) && currentNode.hasChildren())
					loadChildNodesForList(currentNode, menuString, level, config);
				
				if (currentNode.isHidden()) {
					menuString.append("</li>");
					menuString.append("\n");
				}
				
			}
		}

		menuString.append("</ul>\n");
		menuString.append("</div>\n");
	}

	private void loadChildNodesForTable(Node node, StringBuffer menuString, int level, Config config) {

		if (!node.getChildren().isEmpty()) {

			level++;

			for (Node subNode : node.getChildren()) {

				if (exclude != null && subNode.getName().equals(exclude))
					continue;

				if (NodeHelper.hasAccessToNode(subNode, user)) {

					if (orientation == null || orientation.equalsIgnoreCase("vertical")) {
						menuString.append("<tr class=\"menuitem\">");
					}

					menuString.append("<td class=\"level");
					menuString.append(level);
					menuString.append("\">\n");

					if (indent) {

						log.info("Indenting level " + level);

						for (int i = 0; i < level; i++)
							menuString.append("&nbsp;&nbsp;&nbsp;&nbsp;");
					}

					if (subNode.getType() == Node.TYPE_LINK) {
						menuString.append("<a class=\"mainlevel\" href=\"");
						menuString.append(subNode.getLinkurl());
						menuString.append("\" target=\""+subNode.getTargetName()+"\">");

						if (selected != null && selected.equals(subNode.getName()))
							menuString.append("<b>");

						menuString.append(subNode.getName());

						if (selected != null && selected.equals(subNode.getName()))
							menuString.append("</b>");

						menuString.append("</a>");						
					}
					else {

						menuString.append("<a class=\"mainlevel\" href=\"http://");
						menuString.append(ServerUtil.getHostUrl(pageContext.getRequest()));
						menuString.append("/");
						menuString.append(config.getLocalPath());
						
						String subNodePath = subNode.getPath();
						
						if (multiDomains)
							subNodePath = subNodePath.substring(subNodePath.indexOf("/")+1);				
						
						menuString.append(subNodePath);
						menuString.append(".html\" target=\"_self\">");

						log.info("Selected item : " + selected);

						if (selected != null && selected.equals(subNode.getPath()))
							menuString.append("<b>");

						menuString.append(subNode.getName());

						if (selected != null && selected.equals(subNode.getPath()))
							menuString.append("</b>");

						menuString.append("</a>");
						
					}
					
					menuString.append("</td>\n");

					if (orientation == null || orientation.equalsIgnoreCase("vertical")) {
						menuString.append("</tr>");
					}

					loadChildNodesForTable(subNode, menuString, level, config);
				}

			}

		}

	}

	private void loadChildNodesForList(Node node, StringBuffer menuString, int level, Config config) {

		String ulClass = null;
		String liClass = null;
		
		
		if (!node.getChildren().isEmpty()) {
		
			level++;
			
			if (level > 1) {
				ulClass = "Navigation_ul_li_ul_sub";
				liClass = "Navigation_ul_li_ul_sub_li";
			}
				
			else {
				ulClass = "Navigation_ul_li_ul";
				liClass = "Navigation_ul_li_ul_li";
			}
			
			menuString.append("<ul style=\"display:none;\" id=\"" + node.getId() + "\" class=\""+ ulClass +"\">");
			menuString.append("\n");

			
			
			if (level > 1) {
				menuString.append("<div class=\"navigation-h-back-sub\"></div>");	
				menuString.append("\n");
				menuString.append("<div class=\"navigation-v-back-sub\"></div>");
				menuString.append("\n");
				menuString.append("<div class=\"navigation-v-hidden_back\"></div>");
				menuString.append("\n");			
				
			}
			else {
				menuString.append("<div class=\"navigation-v-back\"></div>");					
				menuString.append("\n");
				menuString.append("<div class=\"navigation-v-hidden_back\"></div>");
				menuString.append("\n");						
			}
			
			for (Node subNode : node.getChildren()) {

				if (exclude != null && subNode.getName().equals(exclude))
					continue;

				if (NodeHelper.hasAccessToNode(subNode, user)) {
					
					if (!subNode.isHidden()) {
						
						menuString.append("<li class=\""+liClass+"\">");
						menuString.append("<a href=\"http://");
						menuString.append(ServerUtil.getHostUrl(pageContext.getRequest()));
						menuString.append("/");
						menuString.append(config.getLocalPath());
						String nodePath = subNode.getPath();
						
						if (multiDomains)
							nodePath = nodePath.substring(nodePath.indexOf("/")+1);	
						
						menuString.append(nodePath);
						menuString.append(".html");

						if (target != null)
							menuString.append("&raw=true");

						menuString.append("\"");

						if (target != null) {
							menuString.append(" target=\"");
							menuString.append(target);
							menuString.append("\"");
						}
						else {
							menuString.append(" target=\"_self\"");
						}

						if (childNodeOnClickCallback != null) {
							menuString.append(" onclick=\"");
							menuString.append(childNodeOnClickCallback + "(");
							menuString.append("'" + subNode.getParent().getId() + "',");
							menuString.append("'" + subNode.getId() + "'");
							menuString.append(");");
							menuString.append("\"");
						}

						if (childNodeOnMouseOverCallback != null) {
							menuString.append(" onmouseover=\"");
							menuString.append(childNodeOnMouseOverCallback + "(");
							menuString.append("'" + subNode.getParent().getId() + "',");
							menuString.append("'" + subNode.getId() + "'");
							menuString.append(", [ ");
							
							int index = 0;
							
							for (Node child : subNode.getParent().getChildren()) {
								
								if (!child.getId().equals(subNode.getId())) {
									
									menuString.append(child.getId());
									
									if (index < subNode.getParent().getChildren().size() - 2 )
										menuString.append(",");
									
									index++;
								}

							}
							
							
							menuString.append("]);");
							menuString.append("\"");
						}
						
						menuString.append(">");

						if (locale != null) {
							
							Node _node = NodeHelper.locateNodeByName(subNode.getPath()+"_"+locale.toString());						

							if (!_node.getName().equals(resources.getString("content.error.contentname"))) 
								subNode.setDisplayName(_node.getDisplayName());
								
						}
						
						String name = null;
						
						if (subNode.getDisplayName() != null)
							name = subNode.getDisplayName();
						else
							name = subNode.getName();

						
						if (selected != null && selected.equals(subNode.getName()))
							menuString.append("<b>");

						menuString.append(name);

						if (selected != null && selected.equals(subNode.getName()))
							menuString.append("</b>");

						menuString.append("</a>");
						menuString.append("\n");
					}
					
					loadChildNodesForList(subNode, menuString, level, config);
					
					if (!subNode.isHidden()) {
						menuString.append("</li>");
						menuString.append("\n");
					}
				}

			}

			menuString.append("</ul>");
			menuString.append("\n");
		}
	}

	public void setPageContext(PageContext pageContext) {
		this.pageContext = pageContext;
	}

	private List<?> getRootMenuItems() {
		return DAOManager.getInstance().getNodeDAO().findAllRootNodes();
	}

	/**
	 * @return the forceLevelIndent
	 */
	public String getForceLevelIndent() {
		return forceLevelIndent;
	}

	/**
	 * @param forceLevelIndent the forceLevelIndent to set
	 */
	public void setForceLevelIndent(String forceLevelIndent) {
		this.forceLevelIndent = forceLevelIndent;
	}

	/**
	 * @return the selected
	 */
	public String getSelected() {
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(String selected) {
		this.selected = selected;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	/**
	 * @return the startNode
	 */
	public String getStartNode() {
		return startNode;
	}

	/**
	 * @param startNode the startNode to set
	 */
	public void setStartNode(String startNode) {
		this.startNode = startNode;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the exclude
	 */
	public String getExclude() {

		return exclude;
	}

	public void setExclude(String exclude) {

		this.exclude = exclude;
	}



	/**
	 * @return the navigationCallback
	 */
	public String getNavigationCallback() {
		return navigationCallback;
	}

	/**
	 * @param navigationCallback the navigationCallback to set
	 */
	public void setNavigationCallback(String navigationCallback) {
		this.navigationCallback = navigationCallback;
	}
	
	
	/**
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	/**
	 * @return the rootNodeOnClickCallback
	 */
	public String getRootNodeOnClickCallback() {
		return rootNodeOnClickCallback;
	}

	/**
	 * @param rootNodeOnClickCallback the rootNodeOnClickCallback to set
	 */
	public void setRootNodeOnClickCallback(String rootNodeOnClickCallback) {
		this.rootNodeOnClickCallback = rootNodeOnClickCallback;
	}

	/**
	 * @return the childNodeOnClickCallback
	 */
	public String getChildNodeOnClickCallback() {
		return childNodeOnClickCallback;
	}

	/**
	 * @param childNodeOnClickCallback the childNodeOnClickCallback to set
	 */
	public void setChildNodeOnClickCallback(String childNodeOnClickCallback) {
		this.childNodeOnClickCallback = childNodeOnClickCallback;
	}

	/**
	 * @return the rootNodeOnMouseOverCallback
	 */
	public String getRootNodeOnMouseOverCallback() {
		return rootNodeOnMouseOverCallback;
	}

	/**
	 * @param rootNodeOnMouseOverCallback the rootNodeOnMouseOverCallback to set
	 */
	public void setRootNodeOnMouseOverCallback(String rootNodeOnMouseOverCallback) {
		this.rootNodeOnMouseOverCallback = rootNodeOnMouseOverCallback;
	}

	/**
	 * @return the childNodeOnMouseOverCallback
	 */
	public String getChildNodeOnMouseOverCallback() {
		return childNodeOnMouseOverCallback;
	}

	/**
	 * @param childNodeOnMouseOverCallback the childNodeOnMouseOverCallback to set
	 */
	public void setChildNodeOnMouseOverCallback(String childNodeOnMouseOverCallback) {
		this.childNodeOnMouseOverCallback = childNodeOnMouseOverCallback;
	}

}
