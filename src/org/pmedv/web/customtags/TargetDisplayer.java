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
import java.util.ResourceBundle;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.Node;
import org.pmedv.web.ServerUtil;

/**
 * This tag displays a hidden inpit field indicating the redirect target
 * for login and logout actions.
 * 
 * @author Matthias Pueski
 *
 */

public class TargetDisplayer extends TagSupport {

	private static final long serialVersionUID = 6964509179480725471L;

	private static ResourceBundle resources = ResourceBundle.getBundle("MessageResources");
	
	private static final Log log = LogFactory.getLog(TargetDisplayer.class);
	
	public TargetDisplayer() {
		super();
	}

	public int doStartTag() throws JspException {
		
		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);
		
		Node node = (Node)pageContext.getSession().getAttribute("node");
		StringBuffer content = new StringBuffer();		
		
		try {
			
			if (node != null) {
				log.debug("Requested node is : "+node.getName());

				Config cfg = (Config)DAOManager.getInstance().getConfigDAO().findByID(1L);
				
				String path = null;
				
				if (ServerUtil.isMultiDomainEnabled(cfg)) {
					if (node.getPath().contains("/")) {
						path = node.getPath().substring(node.getPath().indexOf("/")+1)+".html";
					}
					else {
						path = node.getPath()+".html";
					}
				}
				else {
					path = node.getPath()+".html";
				}				
				
				content.append("<input name=\"target\" value=\""+path+"\" type=\"hidden\">");
				
			}
			else {
				content.append(resources.getString("content.error.contentname"));
			}

		}
		catch (NullPointerException n) {
			content.append(resources.getString("content.error.contentname"));
		}
		
		try {
			pageContext.getOut().print(content.toString());
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
			
		return EVAL_BODY_INCLUDE;
	}
	

	public void setPageContext(PageContext pageContext) {
		this.pageContext = pageContext;
	}

}
