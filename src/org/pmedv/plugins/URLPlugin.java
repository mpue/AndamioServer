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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.core.util.URLReader;

public class URLPlugin extends AbstractPlugin implements IPlugin, Serializable {

	private static final long serialVersionUID = -6265435196608381519L;
	protected static Log log = LogFactory.getLog(URLPlugin.class);
	
	public URLPlugin() {
		super();
		pluginID = "URLPlugin";
		paramDescriptors.put("plugin_url_path", resourceBundle.getString("plugin.url.field.path"));
	}
	
	
	public String getContent() {
		
		String url = paramMap.get("plugin_url_path");
		
		StringBuffer sb = new StringBuffer();
		log.debug("Trying to fetch content from url : "+url);
		sb.append(URLReader.getDefaultContent(url,null));		
		return sb.toString();

	}

}
