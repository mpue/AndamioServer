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

import java.io.Serializable;
import java.util.MissingResourceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.core.util.URLReader;
import org.pmedv.pojos.Config;

public class JSPPlugin extends AbstractPlugin implements IPlugin, Serializable {

	private static final long serialVersionUID = -1191302731932522373L;

	protected static Log log = LogFactory.getLog(JSPPlugin.class);
	
	public JSPPlugin() {
		super();
		pluginID = "JSPPlugin";
		paramDescriptors.put("plugin_jsp_path", resourceBundle.getString("plugin.jsp.field.path"));
		
		try {
			paramDescriptors.put("plugin_title", resourceBundle.getString("plugin.field.pagetitle"));	
		}
		catch (MissingResourceException m) {
			paramDescriptors.put("plugin_title", "plugin.field.pagetitle");
		}
	}
	
	public String getContent() {
		
		try {
			setTitle(paramMap.get("plugin_title"));			
		}
		catch(NullPointerException n) {
			setTitle("");
		}

		Config config = (Config) DAOManager.getInstance().getConfigDAO().findByID(1);
		
		String path = paramMap.get("plugin_jsp_path");
		
		StringBuffer sb = new StringBuffer();
		
		String url = protocol+"://localhost:"+request.getServerPort()+"/"+config.getLocalPath()+path;
		log.debug("reading JSP from : "+url);
		
		sb.append(URLReader.getDefaultContent(url,null));
		
		return sb.toString();

	}

}
