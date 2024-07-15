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

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

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

public class YMenuDisplayer extends TagSupport {
	
	private static final long serialVersionUID = 2868410646272059205L;
	private static final Log log = LogFactory.getLog(YMenuDisplayer.class);
	
	private User user = null;
	private boolean indent = false;
	
	private String startNode;
	private String mode;
	private String selected;
	private String forceLevelIndent;
	private String orientation;
	private String type;
	private String exclude;
	
	
	

	public YMenuDisplayer() {
		super();		
		log.debug("Created instance of "+this.getClass());		
	}

	public int doStartTag() throws JspException {
	
		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1L);
		
		log.info("Force level indent : "+forceLevelIndent);

		if (forceLevelIndent != null && forceLevelIndent.length() > 3) {
			indent = Boolean.valueOf(forceLevelIndent);
		}
		
		String login = (String)pageContext.getSession().getAttribute("login"); 
		
		if (login != null) {
			user = (User)DAOManager.getInstance().getUserDAO().findByName(login);
		}
		else 
			user = null;
		
		try {
			
			StringBuffer menuString = new StringBuffer();

			if (type != null && type.equalsIgnoreCase("list")) {
				createListMenu(config, menuString);
			}
			if (type != null && type.equalsIgnoreCase("duorama")) {
				
				Iterator<?> itemIterator = null; 
				
				String extension = ".html";
				
				if (startNode != null && startNode.length() > 1) {
					Node start = NodeHelper.locateNodeByName(startNode);				
					itemIterator = start.getChildren().iterator();				
				}
				else {
					itemIterator = getRootMenuItems().iterator();	
				}
				
				for (Iterator<?> it = itemIterator;it.hasNext();) {
					
					Node currentNode = (Node)it.next();
					
					if (exclude != null) {
						
						String excludeNodes[] = exclude.split(",");
						
						if (excludeNodes != null && excludeNodes.length > 1) {
						
							boolean excluded = false;
							
							for (int i=0; i < excludeNodes.length;i++) {
								  
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
						
			
					if (NodeHelper.hasAccessToNode(currentNode,user)) {
						menuString.append("<a href=\"http://");
						menuString.append(ServerUtil.getHostUrl(pageContext.getRequest()));
						menuString.append("/");
						
						if (selected != null && selected.equals(currentNode.getName())) {
							
						}
						
						menuString.append(config.getLocalPath());
						menuString.append(currentNode.getPath());
						menuString.append(extension);
						menuString.append("\" target=\"_self\">");
						menuString.append(currentNode.getName());						
						menuString.append("</a>");

					}
				}
				
			}
			else {
				createTableMenu(config, menuString);	
			}
			
			pageContext.getOut().print(menuString.toString());
		}	
		catch (IOException ioe) {
			log.info("Could not write to servlet output stream : "+ioe.getMessage());
		}
		
		return EVAL_BODY_INCLUDE;
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
		
		for (Iterator<?> it = itemIterator;it.hasNext();) {
			
			Node currentNode = (Node)it.next();
			
			if (exclude != null) {
				
				String excludeNodes[] = exclude.split(",");
				
				if (excludeNodes != null && excludeNodes.length > 1) {
				
					boolean excluded = false;
					
					for (int i=0; i < excludeNodes.length;i++) {
						  
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
			
			if (NodeHelper.hasAccessToNode(currentNode,user)) {
				
				if (getMode().equalsIgnoreCase(Mode.BLOG) && currentNode.getType() == Node.TYPE_CONTENT)
					extension = ".blog";
				else
					extension = ".html";					
			
				if (orientation == null || orientation.equalsIgnoreCase("vertical")) {
					menuString.append("<tr class=\"blogmenuitem\">");
				}
				
				menuString.append("<td class=\"level"+level+"\">\n");					
				menuString.append("<a class=\"mainlevel\" href=\"http://");
				menuString.append(ServerUtil.getHostUrl(pageContext.getRequest()));
				menuString.append("/");
				menuString.append(config.getLocalPath());
				menuString.append(currentNode.getPath());
				menuString.append(extension);
				menuString.append("\" target=\"_self\">");
				
				if (selected != null && selected.equals(currentNode.getName()))
					menuString.append("<b>");
					
				menuString.append(currentNode.getName());
				
				if (selected != null && selected.equals(currentNode.getName()))
					menuString.append("</b>");
				
				menuString.append("</a>");
				menuString.append("</td>");
				
				if (orientation == null || orientation.equalsIgnoreCase("vertical")) {
					menuString.append("</tr>");
				}
				
				menuString.append("\n");
			
				if (getMode().equalsIgnoreCase(Mode.WEBSITE) && currentNode.hasChildren())
					loadChildNodesForTable(currentNode, menuString,level,config);
			}
		}
		
		if (orientation != null && orientation.equalsIgnoreCase("horizontal")) {
			menuString.append("</tr>");
		}
		
		menuString.append("</table>\n");
	}
	
	private void createListMenu(Config config, StringBuffer menuString) {
		
		String extension;
		menuString.append("<ul id=\"tree\" class=\"tree\">");

		int level = 1;
		
		Iterator<?> itemIterator = null; 
		
		if (startNode != null && startNode.length() > 1) {
			log.info("Start node "+startNode);
			Node start = NodeHelper.locateNodeByName(startNode);
			
			if (start != null && start.hasChildren())			
				itemIterator = start.getChildren().iterator();				
		}
		else {
			itemIterator = getRootMenuItems().iterator();	
		}

		if (itemIterator == null)
			return;
		
		for (Iterator<?> it = itemIterator;it.hasNext();) {
						
			Node currentNode = (Node)it.next();
			
			if (exclude != null && currentNode.getName().equals(exclude))
				continue;
		
			if (NodeHelper.hasAccessToNode(currentNode,user)) {
				
				if (getMode().equalsIgnoreCase(Mode.BLOG) && currentNode.getType() == Node.TYPE_CONTENT)
					extension = ".blog";
				else
					extension = ".html";					
			
				
				menuString.append("<li>");					
				menuString.append("<a id=\""+currentNode.getId()+"\" href=\"http://");
				menuString.append(ServerUtil.getHostUrl(pageContext.getRequest()));
				menuString.append("/");
				menuString.append(config.getLocalPath());
				menuString.append(currentNode.getPath());
				menuString.append(extension);
				menuString.append("\" target=\"_self\">");
				
				if (selected != null && selected.equals(currentNode.getName()))
					menuString.append("<b>");
					
				menuString.append(currentNode.getName());
				
				if (selected != null && selected.equals(currentNode.getName()))
					menuString.append("</b>");
				
				menuString.append("</a>");
				
				menuString.append("\n");
			
				if (getMode().equalsIgnoreCase(Mode.WEBSITE) && currentNode.hasChildren())
					loadChildNodesForList(currentNode, menuString,level,config);
				
				menuString.append("</li>");
			}
		}
		
		
		menuString.append("</ul>\n");
	}
	
	
	private void loadChildNodesForTable(Node node, StringBuffer menuString, int level, Config config) {
		
		if (!node.getChildren().isEmpty()) {
			
			level++;
			
			for (Node subNode : node.getChildren()) {
			
				if (exclude != null && subNode.getName().equals(exclude))
					continue;
				
				if (NodeHelper.hasAccessToNode(subNode,user)) {
				
					menuString.append("<td class=\"level");
					menuString.append(level);
					menuString.append("\">\n");
					
					if (indent) {
						
						log.info("Indenting level "+level);
						
						for (int i=0;i<level;i++)
							menuString.append("&nbsp;&nbsp;&nbsp;&nbsp;");
					}
					
					menuString.append("<a class=\"mainlevel\" href=\"http://"); 
					menuString.append(ServerUtil.getHostUrl(pageContext.getRequest()));
					menuString.append("/");
					menuString.append(config.getLocalPath());
					menuString.append(subNode.getPath());
					menuString.append(".html\" target=\"_self\">");

					if (selected != null && selected.equals(subNode.getName()))
						menuString.append("<b>");

					menuString.append(subNode.getName());

					if (selected != null && selected.equals(subNode.getName()))
						menuString.append("</b>");
					
					menuString.append("</a>");				
					menuString.append("</td>\n");
					
					loadChildNodesForTable(subNode, menuString,level,config);
				}
				
			}			
					
		}
	}
	
	private void loadChildNodesForList(Node node, StringBuffer menuString, int level, Config config) {
		
		if (!node.getChildren().isEmpty()) {

			level++;

			menuString.append("<ul class=\"tree\">");
			
			for (Node subNode : node.getChildren()) {

				if (exclude != null && subNode.getName().equals(exclude))
					continue;
				
				if (NodeHelper.hasAccessToNode(subNode,user)) {
					
					menuString.append("<li>");
					menuString.append("<a id=\""+subNode.getId()+"\" href=\"http://"); 
					menuString.append(ServerUtil.getHostUrl(pageContext.getRequest()));
					menuString.append("/");
					menuString.append(config.getLocalPath());
					menuString.append(subNode.getPath());
					menuString.append(".html\" target=\"_self\">");

					if (selected != null && selected.equals(subNode.getName()))
						menuString.append("<b>");

					menuString.append(subNode.getName());

					if (selected != null && selected.equals(subNode.getName()))
						menuString.append("</b>");
					
					menuString.append("</a>");														
					loadChildNodesForList(subNode, menuString,level,config);
					menuString.append("</li>");
				}
				
			}		

			menuString.append("</ul>");
			
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
	
}
