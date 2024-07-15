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
package org.pmedv.actions;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.pmedv.beans.objects.DirectoryObject;
import org.pmedv.cms.common.GlobalForwards;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.file.DirectoryPrintVisitor;
import org.pmedv.forms.ConfigShowForm;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.Node;
import org.pmedv.util.FileHelper;
import org.pmedv.web.ServerUtil;

/**
 * The <code>ConfigAction</code> provides all functions which
 * are needed for the configuration of the system.
 * 
 * @author Matthias Pueski
 *
 */
public class ConfigAction extends ObjectListAction {

	public ConfigAction() {
		super("menu.configuration");
		log.debug("Executing " + this.getClass());
	}

	/**
	 * loads config from the db and saves it in the request
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward edit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		if (!isAllowedToProcess(request, getName()))
			return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);

		ConfigShowForm configForm = (ConfigShowForm) form;

		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);
		String RootDirectory = config.getBasepath();

		File file1 = new File(RootDirectory + "templates/");
		File file2 = new File(RootDirectory + "admin/themes/");

		ArrayList<DirectoryObject> templateDirectoryContents;
		ArrayList<DirectoryObject> adminTemplateDirectoryContents;

		try {
			templateDirectoryContents = FileHelper.traverse(file1,
					new DirectoryPrintVisitor());
			adminTemplateDirectoryContents = FileHelper.traverse(file2,
					new DirectoryPrintVisitor());
		} catch (IllegalArgumentException i) {
			templateDirectoryContents = new ArrayList<DirectoryObject>();
			adminTemplateDirectoryContents = new ArrayList<DirectoryObject>();
		}

		configForm.setConfig(config);
		configForm.setTemplateDirectoryContents(templateDirectoryContents);
		configForm.setAdminTemplateDirectoryContents(adminTemplateDirectoryContents);

		log.debug("Editing configuration.");
		
		
		StringBuffer jsonString = new StringBuffer();
		
		if (request.getParameter("async") != null) {
			jsonString.append("{\"success\":true,\"data\":{");
			jsonString.append("\"id\":\"" + config.getId() + "\",");			
			jsonString.append("\"admintemplate\":\"" + config.getAdmintemplate() + "\",");
			jsonString.append("\"keywords\":\"" + config.getKeywords() + "\",");			
			jsonString.append("\"basepath\":\"" + config.getBasepath() + "\",");
			jsonString.append("\"contentpath\":\"" + config.getContentpath() + "\",");
			jsonString.append("\"downloadpath\":\"" + config.getDownloadpath() + "\",");
			jsonString.append("\"fromadress\":\"" + config.getFromadress() + "\",");
			jsonString.append("\"gallerypath\":\"" + config.getGallerypath() + "\",");
			jsonString.append("\"hostname\":\"" + config.getHostname() + "\",");
			jsonString.append("\"imagepath\":\"" + config.getImagepath() + "\",");
			jsonString.append("\"imageurl\":\"" + config.getImageurl() + "\",");
			jsonString.append("\"importpath\":\"" + config.getImportpath() + "\",");
			jsonString.append("\"localPath\":\"" + config.getLocalPath() + "\",");
			jsonString.append("\"maxAvatarHeight\":" + config.getMaxAvatarHeight() + ",");			
			jsonString.append("\"maxAvatarWidth\":" + config.getMaxAvatarWidth() + ",");
			jsonString.append("\"maxFileUploadSize\":" + config.getMaxFileUploadSize() + ",");
			jsonString.append("\"maxImageUploadSize\":" + config.getMaxImageUploadSize() + ",");			
			jsonString.append("\"productimagepath\":\"" + config.getProductimagepath() + "\",");
			jsonString.append("\"sitename\":\"" + config.getSitename() + "\",");
			jsonString.append("\"smtphost\":\"" + config.getSmtphost() + "\",");
			jsonString.append("\"password\":\"" + config.getPassword() + "\",");			
			jsonString.append("\"startnode\":\"" + config.getStartnode() + "\",");
			jsonString.append("\"template\":\"" + config.getTemplate() + "\",");			
			jsonString.append("\"maintenanceMode\":" + config.isMaintenanceMode() + ",");
			jsonString.append("\"username\":\"" + config.getUsername()+ "\"");			
			jsonString.append("}}");

			try {
				
				response.setContentLength(jsonString.length());
				response.setContentType("text/plain");
				
				PrintWriter out = response.getWriter();
				out.print(jsonString.toString());
				out.flush();
			} catch (IOException e) {
				log.info("An exception occured : "+e.getStackTrace().toString());
			}
			return null;
		}
		else	
			return mapping.findForward(GlobalForwards.SHOW_CONFIG);
	}

	/**
	 * Store new configuration
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */

	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		if (!isAllowedToProcess(request, getName()))
			return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);

		ConfigShowForm configForm = (ConfigShowForm) form;

		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);

		config.setFromadress(configForm.getFromadress());
		config.setHostname(configForm.getHostname());
		config.setImagepath(configForm.getImagepath());
		config.setDownloadpath(configForm.getDownloadpath());
		config.setImageurl(configForm.getImageurl());
		config.setImportpath(configForm.getImportpath());
		config.setLocalPath(configForm.getLocalPath());
		config.setPassword(configForm.getPassword());
		config.setSmtphost(configForm.getSmtphost());
		config.setUsername(configForm.getUsername());
		config.setKeywords(configForm.getKeywords());
		config.setBasepath(configForm.getBasepath());
		config.setGallerypath(configForm.getGallerypath());
		config.setProductimagepath(configForm.getProductimagepath());
		config.setSitename(configForm.getSitename());
		config.setTemplate(configForm.getTemplate());
		config.setAdmintemplate(configForm.getAdmintemplate());
		config.setStartnode(configForm.getStartnode());
		config.setContentpath(configForm.getContentpath());
		config.setMaxAvatarHeight(configForm.getMaxAvatarHeight());
		config.setMaxAvatarWidth(configForm.getMaxAvatarWidth());
		config.setMaxFileUploadSize(configForm.getMaxFileUploadSize());
		config.setMaxImageUploadSize(configForm.getMaxImageUploadSize());
		config.setMaintenanceMode(configForm.isMaintenanceMode());
		
		DAOManager.getInstance().getConfigDAO().update(config);		

		log.debug("Saving configuration.");

		request.getSession().setAttribute("template",configForm.getAdmintemplate());
		
		if (request.getParameter("async") != null) {
			return null;
		}
		else		
			return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.pmedv.actions.IPermissiveDispatchAction#delete(org.apache.struts.
	 * action.ActionMapping, org.apache.struts.action.ActionForm,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		// Configuration is NOT deletable in any case !!!

		return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);
	}

	public ActionForward getJSON(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		writeJSONList(Config.class, true, request, response);
		return null;
	}
	
	public ActionForward getTemplatesJSON(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);
		String RootDirectory = config.getBasepath();

		File templateDir = new File(RootDirectory + "templates/");

		ArrayList<DirectoryObject> templateDirectoryContents;

		try {
			templateDirectoryContents = FileHelper.traverse(templateDir,new DirectoryPrintVisitor());
		} 
		catch (IllegalArgumentException i) {
			templateDirectoryContents = new ArrayList<DirectoryObject>();
		}
		
		writeJSONList(DirectoryObject.class, templateDirectoryContents, true, "id", request, response);
		return null;
	}

	public ActionForward getAdminTemplatesJSON(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);
		String RootDirectory = config.getBasepath();

		File adminTemplateDir = new File(RootDirectory + "admin/themes/");

		ArrayList<DirectoryObject> adminTemplateDirectoryContents;

		try {
			adminTemplateDirectoryContents = FileHelper.traverse(adminTemplateDir,new DirectoryPrintVisitor());
		} 
		catch (IllegalArgumentException i) {
			adminTemplateDirectoryContents = new ArrayList<DirectoryObject>();
		}
		
		writeJSONList(DirectoryObject.class, adminTemplateDirectoryContents, true, "id", request, response);
		return null;
	}
	
	public ActionForward getAvaliableNodesJSON(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		List<?> availableNodes    = DAOManager.getInstance().getNodeDAO().findAllItems();		
		
		writeJSONList(Node.class, availableNodes, true, "id", request, response);
		
		return null;
	}
	
		

}
