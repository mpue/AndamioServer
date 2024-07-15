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
package org.pmedv.util;

import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

public class SampleStrutsPlugin implements PlugIn{

	private static final Log log = LogFactory.getLog(SampleStrutsPlugin.class); 
	
	private ModuleConfig config;
	private ActionServlet servlet;
	private String sampleValue;
	
	/**
	 * @param sampleValue the sampleValue to set
	 */
	public void setSampleValue(String sampleValue) {
		this.sampleValue = sampleValue;
	}

	@Override
	public void destroy() {
		
		log.info("destroying "+this.getClass());
		
	}

	@Override
	public void init(ActionServlet servlet, ModuleConfig config)	throws ServletException {
		
		log.info("Initializing "+this.getClass());
		
		this.config  = config;
		this.servlet = servlet;		
		
		log.info("SampleValue is : "+sampleValue);
		
	}

}
