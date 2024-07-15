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
package org.pmedv.services;

import java.io.File;
import java.util.ArrayList;

import org.pmedv.beans.objects.ConfigBean;
import org.pmedv.cms.common.Constants;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.Config;

/**
 * This is the implementation of the {@link ConfigService}
 * 
 * @author Matthias Pueski
 *
 */
public class ConfigServiceImpl implements ConfigService {

	@Override
	public ConfigBean getConfig() {

		/*
		 * The only configuration we got is the object with the id 1 which
		 * is initially created at the installation process of the system 
		 */
		
		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1L);
	
		/*
		 * Now copy the configuration values to the configuration proxy object.
		 * This is needed, because of the addional hibernate overload. 
		 */
		
		ConfigBean cb = new ConfigBean();
		
		cb.setAdmintemplate(config.getAdmintemplate());
		cb.setBasepath(config.getBasepath());
		cb.setContentpath(config.getContentpath());
		cb.setDownloadpath(config.getDownloadpath());
		cb.setFromadress(config.getFromadress());
		cb.setGallerypath(config.getGallerypath());
		cb.setHostname(config.getHostname());
		cb.setId(config.getId());
		cb.setImagepath(config.getImagepath());
		cb.setImageurl(config.getImageurl());
		cb.setImportpath(config.getImportpath());
		cb.setKeywords(config.getKeywords());
		cb.setLocalPath(config.getLocalPath());
		cb.setMaxAvatarHeight(config.getMaxAvatarHeight());
		cb.setMaxAvatarWidth(config.getMaxAvatarWidth());
		cb.setMaxFileUploadSize(config.getMaxFileUploadSize());
		cb.setMaxImageUploadSize(config.getMaxImageUploadSize());
		cb.setPassword(config.getPassword());
		cb.setProductimagepath(config.getProductimagepath());
		cb.setSitename(config.getSitename());
		cb.setSmtphost(config.getSmtphost());
		cb.setStartnode(config.getStartnode());
		cb.setTemplate(config.getTemplate());
		cb.setUsername(config.getUsername());
		cb.setMaintenanceMode(config.isMaintenanceMode());
		
		return cb;
	
	}

	@Override
	public boolean updateConfig(ConfigBean configBean) {
		
		/*
		 * The only configuration we got is the object with the id 1 which
		 * is initially created at the installation process of the system 
		 */
		
		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1L);
		
		config.setAdmintemplate(configBean.getAdmintemplate());
		config.setBasepath(configBean.getBasepath());
		config.setContentpath(configBean.getContentpath());
		config.setDownloadpath(configBean.getDownloadpath());
		config.setFromadress(configBean.getFromadress());
		config.setGallerypath(configBean.getGallerypath());
		config.setHostname(configBean.getHostname());
		config.setImagepath(configBean.getImagepath());
		config.setImageurl(configBean.getImageurl());
		config.setImportpath(configBean.getImportpath());
		config.setKeywords(configBean.getKeywords());
		config.setLocalPath(configBean.getLocalPath());
		config.setMaxAvatarHeight(configBean.getMaxAvatarHeight());
		config.setMaxAvatarWidth(configBean.getMaxAvatarWidth());
		config.setMaxFileUploadSize(configBean.getMaxFileUploadSize());
		config.setMaxImageUploadSize(configBean.getMaxImageUploadSize());
		config.setPassword(configBean.getPassword());
		config.setProductimagepath(configBean.getProductimagepath());
		config.setSitename(configBean.getSitename());
		config.setSmtphost(configBean.getSmtphost());
		config.setStartnode(configBean.getStartnode());
		config.setTemplate(configBean.getTemplate());
		config.setUsername(configBean.getUsername());
		config.setMaintenanceMode(configBean.isMaintenanceMode());

		return DAOManager.getInstance().getConfigDAO().update(config);

	}

	@Override
	public ArrayList<String> getAvailableUserThemes() {
		
		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1L);
		String rootDirectory = config.getBasepath();

		ArrayList<String> userThemes;
		
		try {
			File templateDir = new File(rootDirectory + "templates/");
			
			String[] templateNames = templateDir.list();
			
			userThemes = new ArrayList<String>();
			
			for (int i=0; i < templateNames.length;i++) {
				userThemes.add(templateNames[i]);
			}
		}
		catch (Exception e) {
			return new ArrayList<String>();
		}
		
		return userThemes;
	}

	@Override
	public String getServerVersion() {
		return Constants.VERSION;
	}

}
