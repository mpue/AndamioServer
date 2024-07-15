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
import java.lang.reflect.Method;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.Config;

/**
 * The <code>ConfigTag</code> is able to display any field of the
 * {@link Config} object.
 * 
 * @author Matthias Pueski
 *
 */
public class ConfigTag extends TagSupport {
	
	private static final long serialVersionUID = 2868410646272059205L;
	private static final Log log = LogFactory.getLog(ConfigTag.class);
	

	public ConfigTag() {
		super();		
	}
	
	private String property;

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

    @Override
	public int doStartTag() throws JspException {
		
    	Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1L);
	
    	StringBuffer output = new StringBuffer();
		
    	for (int i=0;i < config.getClass().getMethods().length;i++) {
    		    		
    		if (config.getClass().getMethods()[i].getName().startsWith("get")) {
    			
    			Method method;
    			Object fieldValue = null;
    			
    			try {
    				
    				Class <?> configClass = config.getClass();

					method = configClass.getMethod(config.getClass().getMethods()[i].getName(), (Class<?>[])null);
					
					if (method.getName().substring(3).equalsIgnoreCase(property)) {
						fieldValue = method.invoke(config, new Object[0]);
						log.debug("requested field value :"+fieldValue);				
						output.append(fieldValue);
					}
					
				} 
    			catch (SecurityException e1) {
					e1.printStackTrace();
				} 
				catch (NoSuchMethodException e1) {
					e1.printStackTrace();
				}
    		    catch (Exception e) {
    		    	e.printStackTrace();
    		    }

    		}
    	}
		
		
		try {
			pageContext.getOut().print(output.toString());
		} 
		catch (IOException e) {
			log.info("Could not print property.");
		}
		
		return EVAL_BODY_INCLUDE;
	}


    @Override
	public void setPageContext(PageContext pageContext) {
		this.pageContext = pageContext;
	}


}
