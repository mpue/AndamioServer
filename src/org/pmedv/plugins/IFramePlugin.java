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

public class IFramePlugin extends AbstractPlugin implements IPlugin, Serializable {

	private static final long serialVersionUID = -5835496142515212802L;

	protected static Log log = LogFactory.getLog(IFramePlugin.class);

	public IFramePlugin() {
		super();
		pluginID = "IFramePlugin";
		paramDescriptors.put("plugin_iframe_path",  resourceBundle.getString("plugin.iframe.field.path"));
		paramDescriptors.put("plugin_iframe_height",resourceBundle.getString("plugin.iframe.field.height"));
		paramDescriptors.put("plugin_iframe_width", resourceBundle.getString("plugin.iframe.field.width"));
		paramDescriptors.put("plugin_iframe_style", resourceBundle.getString("plugin.iframe.style"));
		paramDescriptors.put("plugin_iframe_id",  "Id");
	}

	public String getContent() {

		String url = paramMap.get("plugin_iframe_path");
		String height = paramMap.get("plugin_iframe_height");
		String width = paramMap.get("plugin_iframe_width");
		String style = paramMap.get("plugin_iframe_style");
		String id = paramMap.get("plugin_iframe_id");
		
		StringBuffer sb = new StringBuffer();
		sb.append("<iframe src=\"" + url + "\" height=\"" + height + "\" width=\"" + width
				+ "\" class=\""+style+"\" id=\""+id+"\" name=\""+id+"\" frameborder=\"0\"></iframe>");
		
		return sb.toString();

	}

}
