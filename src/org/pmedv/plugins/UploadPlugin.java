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
 * The upload plugin allows the user to upload any file into a specified directory. All allowed mime
 * types are configured inside WEB-INF/classes/allowedMimeTypes.properties. For each allowd mime
 * type there must be an entry.
 * 
 * @author Matthias Pueski
 * 
 */
public class UploadPlugin extends AbstractPlugin implements IPlugin, Serializable {

	private static final long serialVersionUID = -5835496142515212802L;
	protected static Log log = LogFactory.getLog(UploadPlugin.class);

	private Config config;

	public UploadPlugin() {
		super();
		pluginID = "UploadPlugin";
		paramDescriptors.put("plugin_upload_height", resourceBundle.getString("plugin.upload.field.height"));
		paramDescriptors.put("plugin_upload_width", resourceBundle.getString("plugin.upload.field.width"));
		paramDescriptors.put("plugin_upload_style", resourceBundle.getString("plugin.upload.style"));
		paramDescriptors.put("plugin_upload_directory", resourceBundle.getString("plugin.upload.directory"));
	}

	public String getContent() {

		config = (Config) DAOManager.getInstance().getConfigDAO().findByID(1L);
		String height = paramMap.get("plugin_upload_height");
		String width = paramMap.get("plugin_upload_width");
		String style = paramMap.get("plugin_upload_style");
		String directory = paramMap.get("plugin_upload_directory")+"/";

		String url = null;

		String proto = CMSProperties.getInstance().getAppProps().getProperty("protocol");

		if (ServerUtil.isMultiDomainEnabled(config)) {
			url = proto + "://" + ServerUtil.getHostUrl(request) + "/" + config.getLocalPath()
					+ "upload/FileUpload.do?do=prepareUpload&directory=" + directory;
		}
		else {
			url = proto + "://" + config.getHostname() + "/" + config.getLocalPath()
					+ "upload/FileUpload.do?do=prepareUpload&directory=" + directory;
		}

		StringBuffer sb = new StringBuffer();
		sb.append("<iframe src=\"" + url + "\" height=\"" + height + "\" width=\"" + width + "\" class=\"" + style
				+ "\" frameborder=\"0\"></iframe>");
		
		return sb.toString();

	}

}
