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
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.taglib.TagUtils;
import org.pmedv.actions.FormAction;

/**
 * This Tag displays an icon bar wherever you want it to have ;)
 * 
 * @author Matthias Pueski
 *
 */

public class ButtonbarDisplayer extends TagSupport {

	private static final long serialVersionUID = 2868410646273359205L;
	private static final ResourceBundle resources = ResourceBundle.getBundle("MessageResources");	
	private static final Log log = LogFactory.getLog(ButtonbarDisplayer.class);
	
	private String type = "";
	private String name = "";
	private String buttonStyle = "";
	private String align = "";
	private String width = "";
	
	public ButtonbarDisplayer() {
		super();
		log.debug("created instance of : "+this.getClass());
	}

	@SuppressWarnings("unchecked")
	public int doStartTag() throws JspException {
		
		String username = (String)pageContext.getSession().getAttribute("login");
		
		StringBuffer buttonbar = new StringBuffer();
		
		if (getType() == null || getType().length() < 1) return EVAL_BODY_INCLUDE;
		
		Object formActions = TagUtils.getInstance().lookup(pageContext, name, "formActions" , null); 
			
		buttonbar.append("<table border=\"0\" ");
		if (getWidth().length() > 1)		
			buttonbar.append("width=\""+getWidth()+"\" ");
		if (getAlign().length() > 1)
			buttonbar.append("align=\"center\" ");
		buttonbar.append(">");		
		buttonbar.append("<tr>");
		
		for (Iterator it = ((ArrayList<FormAction>)formActions).iterator();it.hasNext();) {
			
			FormAction f = (FormAction) it.next();
			
			buttonbar.append("<td valign=\"middle\" align=\"left\">");
			buttonbar.append("\n");
			
			if (getType().equals("basic")) {
				buttonbar.append("<input type=\"button\" ");
				if (getName().length() > 1) 
					buttonbar.append("name=\""+f.getKey()+"\" " );
				if (getButtonStyle().length() > 1)			
					buttonbar.append("class=\""+getButtonStyle()+"\" ");
				buttonbar.append("value=\"");
				buttonbar.append(resources.getString(f.getKey())+"\" ");
				
				if (f.getTarget().equalsIgnoreCase("submit")) {
					
					buttonbar.append(" onclick=\"document."+getName()+".submit()\" ");
				}
				if ( f.getTarget().length() >= 10 && f.getTarget().substring(0,10).equalsIgnoreCase("javascript") ) {
					
					buttonbar.append(" onclick='" + f.getTarget() + "'");
				}					
				else {
					buttonbar.append(" onclick=\"window.location.href = '"+f.getTarget()+"'\"");
				}
					
				
				buttonbar.append(">");
				buttonbar.append("\n");
			}
			else if (getType().equals("dojo")) {
				
				
				buttonbar.append("<button id=\""+resources.getString(f.getKey())+"\" dojoType=\"dijit.form.Button\" style=\"background: #F1F6FC;\"");	
				
				if (f.getTarget().equalsIgnoreCase("submit")) {
					
					buttonbar.append(" onclick=\"document."+getName()+".submit()\" ");
				}
				if ( f.getTarget().length() >= 10 && f.getTarget().substring(0,10).equalsIgnoreCase("javascript") ) {
					
					buttonbar.append(" onclick='" + f.getTarget() + "'");
				}
				else {
					
					buttonbar.append(" onclick=\"window.location.href = '"+f.getTarget()+"'\"");
				}					
				
				buttonbar.append(">");
				buttonbar.append("\n");
				buttonbar.append(resources.getString(f.getKey()));
				buttonbar.append("\n");
				buttonbar.append("</button>");
				buttonbar.append("\n");
			}
			buttonbar.append("</td>");
			buttonbar.append("\n");
			
		}
		buttonbar.append("<td width=\"100%\" align=\"right\">");	
		buttonbar.append("&nbsp;");
		buttonbar.append("\n");
		buttonbar.append("</td>");
		buttonbar.append("\n");
		buttonbar.append("</tr>");
		buttonbar.append("\n");
		buttonbar.append("</table>");		
		buttonbar.append("\n");
		
		try {
			pageContext.getOut().print(buttonbar.toString());
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

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the buttonStyle
	 */
	public String getButtonStyle() {
		return buttonStyle;
	}

	/**
	 * @param buttonStyle the buttonStyle to set
	 */
	public void setButtonStyle(String buttonStyle) {
		this.buttonStyle = buttonStyle;
	}

	/**
	 * @return the align
	 */
	public String getAlign() {
		return align;
	}

	/**
	 * @param align the align to set
	 */
	public void setAlign(String align) {
		this.align = align;
	}

	/**
	 * @return the width
	 */
	public String getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(String width) {
		this.width = width;
	}
	


}
