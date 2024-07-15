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

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.Node;

/**
 * This class provides a basic displayer for static content.
 * 
 * @author Matthias Pueski
 *
 */
public class StaticContentDisplayer extends TagSupport {

	private static final long serialVersionUID = 2868410646273359205L;
	
	private String path;
	
	public StaticContentDisplayer() {
		super();
	}

	public int doStartTag() throws JspException {
		
		StringBuffer staticContentString = new StringBuffer();

		if (path == null || path.length() < 1) {
			staticContentString.append("You must provide a path.");
		} 
		else {

			List<?> nodes = DAOManager.getInstance().getNodeDAO().findAllItems();
			
			for (Object o : nodes) {
				
				Node node = (Node)o;

				if (node.getPath().equals(path)) {
					
					if (node.getType() == Node.TYPE_CONTENT)
						staticContentString.append(node.getContent().getContentstring());
				}
				
			}
			
		}
		
		if (staticContentString.length() < 1) {
			staticContentString.append("Node not found!");
		}

		try {
			pageContext.getOut().print(staticContentString.toString());
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return EVAL_BODY_INCLUDE;
	}
	
	/**
	 * @return the path
	 */
	public String getPath() {
	
		return path;
	}

	
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
	
		this.path = path;
	}


}
