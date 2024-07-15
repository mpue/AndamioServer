/**

	AndamioCMS - Open Source Content Management
	Written and maintained by Matthias Pueski 
	
	Copyright (c) 2012 Matthias Pueski
	
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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.Config;


/**
 * This tag displays the path to the page stylesheet
 * 
 * @author pueskma
 *
 */
public class StylesheetDisplayer extends TagSupport {


	private static final Log log = LogFactory.getLog(StylesheetDisplayer.class);
	
	public StylesheetDisplayer() {
		super();
	}

	public int doStartTag() throws JspException {
		
		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);

		StringBuffer content = new StringBuffer();		

		// <link href="/<page:config property="localPath"/>templates/pueski/css/template.css" rel="stylesheet" type="text/css" />
		
		content.append("<link href=\"/");
		content.append(config.getLocalPath());
		content.append("/templates/");
		content.append(config.getTemplate());
		content.append("/css/template.css");
		content.append("\" rel=\"stylesheet\" type=\"text/css\"/>");
		
		try {
			pageContext.getOut().print(content.toString());
		} 
		catch (IOException e) {
			log.error("Could not expand the stylesheet path.");
		}
			
		return EVAL_BODY_INCLUDE;
	}
	

	public void setPageContext(PageContext pageContext) {
		this.pageContext = pageContext;
	}

}
