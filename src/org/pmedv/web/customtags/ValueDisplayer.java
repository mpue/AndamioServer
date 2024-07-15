/**
 * WeberknechtCMS - Open Source Content Management 
 * Written and maintained by Matthias Pueski
 * 
 * Copyright (c) 2003-2011 Matthias Pueski
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
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

/**
 * This tag displays values from session or request. 
 * In order to specify which scope is being used, the user must provide the scope,
 * which can be either &quot;request&quot; or &quot;session&quot;
 * 
 * @author Matthias Pueski
 * 
 */

public class ValueDisplayer extends TagSupport {

	private static final long serialVersionUID = -3853749198567353118L;

	private static ResourceBundle resources = ResourceBundle.getBundle("MessageResources");

	private static final Log log = LogFactory.getLog(ValueDisplayer.class);

	public ValueDisplayer() {
		super();
	}

	private String scope;
	private String property;

	public int doStartTag() throws JspException {

		Config config = (Config) DAOManager.getInstance().getConfigDAO().findByID(1);

		StringBuffer content = new StringBuffer();
		String value = null;
		
		if (scope.equalsIgnoreCase("request")) {
			value = String.valueOf(pageContext.getRequest().getAttribute(property));
		}
		else if (scope.equalsIgnoreCase("session")) {
			value = String.valueOf(pageContext.getSession().getAttribute(property));
		}
		else {
			content.append(getClass().getCanonicalName()+" : invalid scope "+scope);
		}
		
		try {
			content.append(value);
		}
		catch (NullPointerException n) {
			content.append(resources.getString("content.error.contentname"));
		}

		try {
			pageContext.getOut().print(content.toString());
		}
		catch (IOException e) {
			log.error("Could not write tag.");
		}

		return EVAL_BODY_INCLUDE;
	}

	public void setPageContext(PageContext pageContext) {
		this.pageContext = pageContext;
	}

	/**
	 * @return the scope
	 */
	public String getScope() {
		return scope;
	}

	/**
	 * @param scope the scope to set
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

	/**
	 * @return the property
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * @param property the property to set
	 */
	public void setProperty(String property) {
		this.property = property;
	}

}
