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
import org.pmedv.plugins.IPlugin;
import org.pmedv.plugins.PluginHelper;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.Node;

/**
 * This tag displays the title of the page. 
 * 
 * Originally invented by Thorsten Schminkel, but after big pain rewritten by me. 
 * 
 * @author Matthias Pueski
 *
 */
public class PagetitleDisplayer extends TagSupport {

	private static final long serialVersionUID = 2868410646273359205L;
	private static ResourceBundle resources = ResourceBundle.getBundle("MessageResources");
	
	private static final Log log = LogFactory.getLog(PagetitleDisplayer.class);
	
	public PagetitleDisplayer() {
		super();
	}

	public int doStartTag() throws JspException {
		
		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);
		
		Node node = (Node)pageContext.getSession().getAttribute("node");
		StringBuffer pagetitleString = new StringBuffer();		
		pagetitleString.append(config.getSitename()+" - ");

		try {
			
			if (node != null) {
				log.debug("Requested node is : "+node.getName());
				if (node.getType() == Node.TYPE_CONTENT) {
					pagetitleString.append( node.getContent().getPagename() );
				}
				else if (node.getType() == Node.TYPE_PLUGIN){
					
					IPlugin plugin = PluginHelper.pluginMap.get(node.getPluginid());
					
					if (plugin.getTitle() != null)					
						pagetitleString.append(plugin.getTitle());
					else
						pagetitleString.append(node.getName());
					
				}				
			}
			else {
				pagetitleString.append(resources.getString("content.error.contentname"));
			}

		}
		catch (NullPointerException n) {
			pagetitleString.append(resources.getString("content.error.contentname"));
		}
		
		try {
			pageContext.getOut().print(pagetitleString.toString());
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
