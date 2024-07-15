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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.pmedv.cms.common.GlobalForwards;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.core.crypto.MD5Crypter;
import org.pmedv.forms.InstallForm;
import org.pmedv.importer.CSVReader;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.Content;
import org.pmedv.pojos.Node;
import org.pmedv.pojos.User;
import org.pmedv.pojos.Usergroup;
import org.pmedv.util.ResourceUtils;
import org.pmedv.util.SchemaInstaller;

/**
 * <p>
 * This is the installation action. It installs all necessary stuff needed to run
 * a basic WeberknechtCMS system. This action is being executed if there's no configuration 
 * object available which happens usually when the system is installed from scratch.
 * </p>
 * <p>	
 * The installation process triggers the creation of the following objects:
 * </p>
 * <p>
 * <ul type="square">
 * 		<li>The configuration object</li>
 * 		<li>the initial usergroups</li>
 * 		<li>the admin user</li>
 * 		<li>a sample node with a content</li> 		
 * </ul> 
 * </p>
 * <p>
 * The directories needed by the system and the database tables are created as well.
 * <p>
 * </p>
 * <p>
 * The schema installer is only instantiated if the config option &quot;createSchema&quot is set to true.
 * </p>
 * </p>
 * This is only needed for development purposes, since this needs to be turned off, if hibernate creates 
 * the schema. 
 * </p>
 * 
 * @author Matthias Pueski
 */
public class InstallationAction extends DispatchAction {

	public ActionForward init(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		/**
		 * We need to check if the system has already been installed, because
		 * otherwise it may be possible to damage a running system.
		 */
		Config config = (Config) DAOManager.getInstance().getConfigDAO().findByID(1);

		if (config != null) {
			ActionForward af = new ActionForward();
			af.setPath("/admin/Mainpage.do");
			return af;
		}

		InstallForm installForm = (InstallForm) form;

		Properties properties = new Properties();

		String basepath = getServlet().getServletContext().getRealPath("/");

		// switch delimiter due to compatibility reasons

		basepath = basepath.replace("\\", "/");

		String filename = basepath + "admin/install/configuration.properties";

		try {
			FileInputStream f = new FileInputStream(filename);
			properties.load(f);
			f.close();
		}
		catch (IOException e) {
			log.debug("Could not load properties file : " + filename);
		}

		installForm.setBasepath(basepath);
		installForm.setFromadress(properties.getProperty("address.from"));
		installForm.setGallerypath(properties.getProperty("gallerypath"));
		installForm.setProductimagepath(properties.getProperty("gallerypath"));
		installForm.setHostname(properties.getProperty("hostname"));
		installForm.setImagepath(properties.getProperty("image.path"));
		installForm.setImageurl(properties.getProperty("image.url"));
		installForm.setDownloadpath(properties.getProperty("downloadpath"));
		installForm.setImportpath(properties.getProperty("importpath"));
		installForm.setKeywords(properties.getProperty("keywords"));
		installForm.setLocalPath(properties.getProperty("localpath"));
		installForm.setSitename(properties.getProperty("sitename"));
		installForm.setTemplate(properties.getProperty("template"));
		installForm.setSmtphost(properties.getProperty("smtp.host"));
		installForm.setUsername(properties.getProperty("smtp.username"));
		installForm.setPassword(properties.getProperty("smtp.password"));
		installForm.setAdminemail(properties.getProperty("admin.email"));
		installForm.setAdminpass(properties.getProperty("admin.pass"));
		installForm.setAdmintemplate(properties.getProperty("admin.template"));
		installForm.setMaxAvatarHeight(properties.getProperty("maxAvatarHeight"));
		installForm.setMaxAvatarWidth(properties.getProperty("maxAvatarWidth"));
		installForm.setMaxFileUploadSize(properties.getProperty("maxFileUploadSize"));
		installForm.setMaxImageUploadSize(properties.getProperty("maxImageUploadSize"));
		installForm.setStartnode(properties.getProperty("startnode"));
		installForm.setContentpath(properties.getProperty("contentpath"));

		try {

			// InputStream is =
			// getClass().getClassLoader().getResourceAsStream("/application.properties");

			String dbPropertiesLoc = basepath + "WEB-INF/application.properties";

			FileInputStream is = new FileInputStream(new File(dbPropertiesLoc));

			Properties dbproperties = new Properties();
			dbproperties.load(is);

			installForm.setDbUrl(dbproperties.getProperty("db.url"));
			installForm.setDbPass(dbproperties.getProperty("db.password"));
			installForm.setDbUser(dbproperties.getProperty("db.user"));

		}
		catch (IOException e) {
			log.info("could not load database properties.");
		}

		return mapping.findForward(GlobalForwards.INSTALL_CONFIG);
	}

	@SuppressWarnings("unchecked")
	public ActionForward saveConfig(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		log.info("Creating new configuration.");

		Config config = (Config) DAOManager.getInstance().getConfigDAO().findByID(1);

		if (config != null) {
			ActionForward af = new ActionForward();
			af.setPath("/admin/Mainpage.do");
			return af;
		}

		String basepath = getServlet().getServletContext().getRealPath("/");

		InstallForm installForm = (InstallForm) form;

		Properties properties = new Properties();

		String filename = basepath + "admin/install/configuration.properties";

		try {
			FileInputStream f = new FileInputStream(filename);
			properties.load(f);
			f.close();
		}
		catch (IOException e) {
			log.debug("File not found : " + filename);
		}

		/**
		 * Schema installation
		 */
		
		boolean installSchema = Boolean.valueOf(properties.getProperty("createSchema"));

		if (installSchema) {

			String schemaLocation = basepath + "admin/install/schema.sql";

			SchemaInstaller installer = new SchemaInstaller(schemaLocation, installForm.getDbUrl(), installForm.getDbUser(),
					installForm.getDbPass());

			// TODO: Redirect to installation failed page.

			if (!installer.doInstall()) {
				return null;
			}

		}

		// switch delimiter due to compatibility reasons

		basepath = basepath.replace("\\", "/");

		config = new Config();

		/**
		 * Load initial configuration from form and properties file
		 */

		config.setHostname(installForm.getHostname());
		config.setLocalPath(installForm.getLocalPath());
		config.setImportpath(properties.getProperty("importpath"));
		config.setImagepath(properties.getProperty("image.path"));
		config.setImageurl(properties.getProperty("image.url"));
		config.setBasepath(basepath);
		config.setDownloadpath(properties.getProperty("downloadpath"));
		config.setGallerypath(properties.getProperty("gallerypath"));
		config.setProductimagepath(properties.getProperty("gallerypath"));
		config.setSitename(installForm.getSitename());
		config.setTemplate(properties.getProperty("template"));
		config.setSmtphost(installForm.getSmtphost());
		config.setUsername(installForm.getUsername());
		config.setPassword(installForm.getPassword());
		config.setFromadress(installForm.getFromadress());
		config.setAdmintemplate(properties.getProperty("admin.template"));
		config.setMaxAvatarHeight(Integer.parseInt(properties.getProperty("maxAvatarHeight")));
		config.setMaxAvatarWidth(Integer.parseInt(properties.getProperty("maxAvatarWidth")));
		config.setMaxFileUploadSize(Integer.parseInt(properties.getProperty("maxFileUploadSize")));
		config.setMaxImageUploadSize(Integer.parseInt(properties.getProperty("maxImageUploadSize")));
		config.setStartnode(properties.getProperty("startnode"));
		config.setContentpath(properties.getProperty("contentpath"));
		config.setMaintenanceMode(false);

		DAOManager.getInstance().getConfigDAO().createAndStore(config);

		/**
		 * Create the default usergroups
		 */

		CSVReader csvReader = new CSVReader(basepath + "admin/install/", "usergroups.csv");
		csvReader.parseCSVData(',');

		for (Object o : csvReader.getData()) {

			String[] groupData = (String[]) o;

			Usergroup group = new Usergroup();
			group.setName(groupData[0]);
			group.setDescription(groupData[1]);

			DAOManager.getInstance().getUsergroupDAO().createAndStore(group);
		}

		/**
		 * Create the default admin user
		 */

		User user = new User();
		user.setName("admin");
		user.setTitle("Admin");
		user.setFirstName("admin");
		user.setLastName("admin");
		user.setEmail(installForm.getAdminemail());
		byte[] pass = MD5Crypter.createMD5key(installForm.getAdminpass());
		String password = MD5Crypter.createMD5String(pass);
		user.setPassword(password);
		user.setActive(true);
		DAOManager.getInstance().getUserDAO().createAndStore(user);
		// admin needs an account too.
		ResourceUtils.createDefaultMailAccount("admin");
		
		/**
		 * Put the admin into the admin group
		 */

		DAOManager.getInstance().getUserDAO().addGroup(1L, 1L);

		csvReader = new CSVReader(basepath + "admin/install/", "processes.csv");
		csvReader.parseCSVData(',');

		Iterator processIterator = csvReader.getData().iterator();

		Usergroup admin = (Usergroup) DAOManager.getInstance().getUsergroupDAO().findByName("admin");

		/**
		 * Create default processes
		 */

		while (processIterator.hasNext()) {

			org.pmedv.pojos.Process process = new org.pmedv.pojos.Process();

			String[] processData = (String[]) processIterator.next();

			process.setTarget(processData[0]);
			process.setName(processData[1]);
			process.setIcon(processData[2]);
			process.setPosition(Integer.parseInt(processData[3]));
			process.setActive(true);
			DAOManager.getInstance().getProcessDAO().createAndStore(process);

		}

		/**
		 * And finally make them all accessible by the administrator
		 */

		Iterator processesIterator = DAOManager.getInstance().getProcessDAO().findAllItems().iterator();

		while (processesIterator.hasNext()) {
			org.pmedv.pojos.Process currentProcess = (org.pmedv.pojos.Process) processesIterator.next();
			DAOManager.getInstance().getProcessDAO().addGroup(admin.getId(), currentProcess.getId());
		}

		/**
		 * Create the default welcome content and the according node
		 */

		String contentString = properties.getProperty("startcontent.content");
		String contentName = properties.getProperty("startcontent.name");
		String contentDescription = properties.getProperty("startcontent.description");

		Content content = new Content();

		content.setContentname(contentName);
		content.setContentstring(contentString);
		content.setCreated(new Date());
		content.setDescription(contentDescription);
		content.setLastmodified(new Date());
		content.setPagename(contentName);

		DAOManager.getInstance().getContentDAO().createAndStore(content);

		content = (Content) DAOManager.getInstance().getContentDAO().findByName(contentName);

		/**
		 * We need a node to put the content in.
		 */

		Node node = new Node();

		node.setName(contentName);
		node.setContent(content);
		node.setFirstchild(false);
		node.setParent(null);
		node.setPosition(1);
		node.setType(Node.TYPE_CONTENT);

		DAOManager.getInstance().getNodeDAO().createAndStore(node);

		/**
		 * Create default directories used by the system
		 */

		boolean b = false;

		b = new File(basepath + properties.getProperty("importpath")).mkdir();
		b = new File(basepath + "export/").mkdir();
		b = new File(basepath + "temp/").mkdir();
		b = new File(basepath + properties.getProperty("contentpath")).mkdir();
		b = new File(basepath + properties.getProperty("attachmentpath")).mkdir();
		b = new File(basepath + properties.getProperty("gallerypath")).mkdir();
		b = new File(basepath + properties.getProperty("gallerypath") + "thumbs").mkdir();

		if (!b) {				
			log.info("Error creating paths.");
		}

		return mapping.findForward(GlobalForwards.INSTALL_SUCCESS);
		
	}

}
