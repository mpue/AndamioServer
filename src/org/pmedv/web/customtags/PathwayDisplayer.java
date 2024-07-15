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
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.cms.common.CMSProperties;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.core.html.Link;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.Node;
import org.pmedv.web.ServerUtil;

/**
 * The pathway displayer displays a linked pathway for the 
 * currently selected node. 
 * 
 * @author Matthias Pueski
 *
 */
public class PathwayDisplayer extends TagSupport {

	private static final long serialVersionUID = 2868410646273359205L;
	private static ResourceBundle resources = ResourceBundle.getBundle("MessageResources");
	private static final Log log = LogFactory.getLog(PathwayDisplayer.class);
	private static final Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);
	
	private String nodeName;
	private String separator;
	private String exclude;
 
	
	public PathwayDisplayer() {
		super();
	}

	public int doStartTag() throws JspException {
		
		ArrayList<Node> pathwayNodeList = new ArrayList<Node>();
		StringBuffer pathWay = new StringBuffer();
		
		try {
			
			Node node = (Node)pageContext.getSession().getAttribute("node");						
			if (node == null) {		
				try {
					pageContext.getOut().print("No node selected");
				} 
				catch (IOException e) {
					log.error("Could not write to servlet output stream.");
				}
				return EVAL_BODY_INCLUDE;
				
			}
			log.info("requested node is "+node.getPath());
			pathwayNodeList.add(node);
			
			// save current node

			Node oldNode = node;

			// get All NodeNames and add them into a list
			
			while (node.getParent() != null) {			
				node = node.getParent();
				pathwayNodeList.add(node);
			}
			
			// reverse the list to get the node order from top to down
			
			Collections.reverse(pathwayNodeList);			
			
			for (int i=0; i < pathwayNodeList.size();i++) {
				
				if (exclude != null) {
					
					String excludeNodes[] = exclude.split(",");
					
					if (excludeNodes != null && excludeNodes.length > 1) {
					
						boolean excluded = false;
						
						for (int j=0; j < excludeNodes.length;j++) {
							  
							if (excludeNodes[j].equals(pathwayNodeList.get(i).getPath())) 
								excluded = true;
							
						}
						if (excluded)
							continue;
						
					}
					else {
						if (pathwayNodeList.get(i).getPath().equals(exclude)) {
							continue;
						}
					}
					
				}
				
				// current node isn't selectable
				
				
				if (!pathwayNodeList.get(i).equals(oldNode)) {
					
					Link link = new Link();

					StringBuffer href = new StringBuffer();
					
					href.append(CMSProperties.getInstance().getAppProps().getProperty("protocol")+"://");
					href.append(ServerUtil.getHostUrl(pageContext.getRequest()));
					href.append("/");
					href.append(config.getLocalPath());
					
					String path;
					String name;
					
					// Strip the language tag from both node name and path
					
					if (pathwayNodeList.get(i).isHidden()) { 
						path = pathwayNodeList.get(i).getPath().substring(0,pathwayNodeList.get(i).getPath().lastIndexOf("_"));
						name = pathwayNodeList.get(i).getName().substring(0,pathwayNodeList.get(i).getName().lastIndexOf("_"));
					}
					else {
						path = pathwayNodeList.get(i).getPath();
						name = pathwayNodeList.get(i).getName();
					}
					
					href.append(path);
					href.append(".html");
					
					link.setHref(href.toString());
					link.setStyleClass("sublevelmainnav");					
					link.setData(name);
					link.setTitle(name);
					
					pathWay.append(link.render());
					
				}
				else {

					String name;
					
					if (pathwayNodeList.get(i).isHidden()) { 
						name = pathwayNodeList.get(i).getName().substring(0,pathwayNodeList.get(i).getName().lastIndexOf("_"));
					}
					else {

						name = pathwayNodeList.get(i).getName();
					}

					pathWay.append(name);
					
				}
				
				if (i < pathwayNodeList.size() -1 ) {
					pathWay.append("&nbsp;" + separator+ "&nbsp;");
				}	
				
			}
			
		}
		catch (NullPointerException n) {			
			n.printStackTrace();			
			pathWay.append(resources.getString("content.error.contentname"));
		}

		try {
			pageContext.getOut().print(pathWay.toString());
		} 
		catch (IOException e) {
			log.error("Could not write to servlet output stream.");			
		}
		
		return EVAL_BODY_INCLUDE;
	}
	

	public void setPageContext(PageContext pageContext) {
		this.pageContext = pageContext;
	}
	
	
	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	
	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public String getExclude() {
		return exclude;
	}
	
	public void setExclude(String exclude) {	
		this.exclude = exclude;
	}
}
