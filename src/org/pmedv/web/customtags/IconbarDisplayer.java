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

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.pmedv.cms.common.CMSProperties;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.Config;
import org.pmedv.web.ServerUtil;

/**
 * This Tag displays an icon bar wherever you want it to have ;)
 * 
 * @author Matthias Pueski
 *
 */
public class IconbarDisplayer extends TagSupport {

	private static final long serialVersionUID = 2868410646273359205L;
	
	private String type;
	private Config config;
	
	public IconbarDisplayer() {
		super();
	}

	@SuppressWarnings("unchecked")
	public int doStartTag() throws JspException {

		StringBuffer menubar = new StringBuffer();
		ResourceBundle resources = ResourceBundle.getBundle("MessageResources");
		
		config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);
		
		String basePath = CMSProperties.getInstance().getAppProps().getProperty("protocol")+
									"://"+ServerUtil.getHostUrl(pageContext.getRequest()) + "/" + 
									config.getLocalPath()+"admin/themes/"+
									config.getAdmintemplate()+"/icons/panel/";
						  
		
		HttpSession session = pageContext.getSession();
		
		if (getType() == null || getType().length() < 1) return EVAL_BODY_INCLUDE;
			
		java.util.Iterator processIterator = DAOManager.getInstance().getProcessDAO().findAllItems().iterator();
		
		if (getType().equals("basic")) {
			
			int index = 0;
			
			menubar.append("<table border=\"0\" width=\"100%\" align=\"center\" class=\"menubar\">");
			menubar.append("<tr height=\"60px\">");
			
			while (processIterator.hasNext()) {
				
				org.pmedv.pojos.Process  currentProcess = (org.pmedv.pojos.Process) processIterator.next();
				
				if (session.getAttribute(resources.getString(currentProcess.getName())) != null) {
					
					menubar.append("<td valign=\"middle\" align=\"center\" width=\"100px\">");

					menubar.append("<table border=\"0\" align=\"center\">");
					
					menubar.append("<tr>");
					menubar.append("<td align=\"center\">");
					
					menubar.append("<a href=\""+ currentProcess.getTarget() +"\" target=\"_self\">");
					menubar.append("<img src=\"");
					menubar.append(basePath);
					menubar.append(currentProcess.getIcon()+"\"");
					menubar.append(" border=\"0\" align=\"middle\"");
					menubar.append("/>");  
					menubar.append("</a>");
					
					menubar.append("</td>");
					menubar.append("</tr>");
					
					menubar.append("<tr>");
					menubar.append("<td style=\"font-family: Verdana;font-size: 10pt; \">");
					menubar.append(resources.getString(currentProcess.getName()));
					menubar.append("</td>");
					menubar.append("</tr>");
					
					menubar.append("</table>");
					
					menubar.append("</td>");				
	 
					index++;
					
				}				
			}
			
			menubar.append("</tr>");
			menubar.append("</table>");			
			
		}
		else if (getType().equals("fisheye")) {
			
		}
		else {
			
		}

		
		try {
			pageContext.getOut().print(menubar.toString());
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
			
		return EVAL_BODY_INCLUDE;
	}
	

	public void setPageContext(PageContext pageContext) {
		this.pageContext = pageContext;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	


}
