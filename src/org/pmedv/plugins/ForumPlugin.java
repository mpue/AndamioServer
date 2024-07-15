/**
 * WeberknechtCMS - Open Source Content Management
 * 
 * Written and maintained by Matthias Pueski
 * 
 * Copyright (c) 2009 Matthias Pueski
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
package org.pmedv.plugins;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.cms.common.CMSProperties;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.Config;
import org.pmedv.web.ServerUtil;

/**
 * This plugin simply displays the forum inside an iframe. This can be used to embed the forum into
 * an existing site.
 * 
 * @author Matthias Pueski
 * 
 */
public class ForumPlugin extends AbstractPlugin implements IPlugin, Serializable {

	private static final long serialVersionUID = -355238485223935835L;

	protected static Log log = LogFactory.getLog(ForumPlugin.class);

	private Config config;

	public ForumPlugin() {
		super();
		pluginID = "ForumPlugin";
		paramDescriptors.put("plugin_iframe_height",resourceBundle.getString("plugin.iframe.field.height"));
		paramDescriptors.put("plugin_iframe_width", resourceBundle.getString("plugin.iframe.field.width"));
		paramDescriptors.put("plugin_iframe_style", resourceBundle.getString("plugin.iframe.style"));
		paramDescriptors.put("plugin_iframe_id", resourceBundle.getString("plugin.iframe.id"));
	}

	public String getContent() {

		config = (Config) DAOManager.getInstance().getConfigDAO().findByID(1L);
		String height = paramMap.get("plugin_iframe_height");
		String width = paramMap.get("plugin_iframe_width");
		String style = paramMap.get("plugin_iframe_style");
		String id = paramMap.get("plugin_iframe_id");

		String url = null;

		String proto = CMSProperties.getInstance().getAppProps().getProperty("protocol");

		if (ServerUtil.isMultiDomainEnabled(config)) {
			url = proto + "://" + ServerUtil.getHostUrl(request) + "/" + config.getLocalPath()
					+ "forum/Mainpage.do?do=showForums";
		}
		else {
			url = proto + "://" + config.getHostname() + "/" + config.getLocalPath()
					+ "forum/Mainpage.do?do=showForums";
		}

		StringBuffer sb = new StringBuffer();
		sb.append("<iframe src=\"" + url + "\" height=\"" + height + "\" width=\"" + width + "\" class=\"" + style + "\" id=\"" + id
				+ "\" frameborder=\"0\"></iframe>");

		return sb.toString();

	}

}
