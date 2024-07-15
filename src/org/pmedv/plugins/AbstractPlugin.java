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
package org.pmedv.plugins;

import java.util.HashMap;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pmedv.cms.common.CMSProperties;

public abstract class AbstractPlugin {
	
	protected ResourceBundle resourceBundle = ResourceBundle.getBundle("MessageResources");
	
	public static final String PLUGIN_PREFIX = "plugin_";
	
	protected HashMap <String, String> paramMap;
	protected HashMap <String, String> paramDescriptors;
	
	protected String pluginID;
	protected String name;
	protected String title;
	protected String protocol;
	protected transient HttpServletRequest request;
	protected transient HttpServletResponse response;
	
	protected AbstractPlugin() {
		name = this.getClass().getCanonicalName();
		protocol = CMSProperties.getInstance().getAppProps().getProperty("protocol");
		this.paramDescriptors  = new HashMap<String, String>();
	}
	
	public HashMap<String, String> getParamlist() {
		return paramMap;
	}

	public void setParamMap(HashMap<String, String> params) {
		this.paramMap = params;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the pluginID
	 */
	public String getPluginID() {
		return pluginID;
	}
	
	public void init(HashMap<String, String> params,HttpServletRequest request, HttpServletResponse response) {
		this.paramMap = params;
		this.request  = request;
		this.response = response;
	}

	public HashMap<String, String> getParamDescriptors() {
		return paramDescriptors;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the request
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * @param request the request to set
	 */
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	
}
