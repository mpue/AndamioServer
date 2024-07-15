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
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.core.html.Link;
import org.pmedv.core.html.Table;
import org.pmedv.core.html.TableColumn;
import org.pmedv.core.html.TableRow;
import org.pmedv.core.util.URLReader;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.User;
import org.pmedv.session.UserSession;

/**
 * This is the user profile plugin. This plugin gives the visitor the ability to
 * manage his own profile.
 * 
 * @author Matthias Pueski
 * 
 */
public class UserSearchPlugin extends AbstractPlugin implements IPlugin, Serializable {

	private static final long serialVersionUID = 1540572020983684739L;

	protected static Log log = LogFactory.getLog(UserSearchPlugin.class);

	private String formAction = null;
	private String page = null;
	private String username = null;

	public UserSearchPlugin() {
		super();
		pluginID = "UserManagerPlugin";
		paramDescriptors.put("plugin_userprofile_formaction", "Form Action");
		paramDescriptors.put("plugin_userprofile_pagetitle", resourceBundle.getString("plugin.field.pagetitle"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.pmedv.plugins.IPlugin#getContent()
	 */
	public String getContent() {

		StringBuffer s = new StringBuffer();

		try {
			formAction = paramMap.get("plugin_userprofile_formaction").trim();
		}
		catch (NullPointerException n) {
			log.debug("some parameter is null, nothing special.");
		}
		try {
			page = paramMap.get("plugin_page").trim();
		}
		catch (NullPointerException n) {
			log.debug("some parameter is null, nothing special.");
		}
		try {
			username = paramMap.get("plugin_username").trim();
		}
		catch (NullPointerException n) {
			log.debug("some parameter is null, nothing special.");
		}
		try {
			title = paramMap.get("plugin_title").trim();
		}
		catch (NullPointerException n) {
			log.debug("some parameter is null, nothing special.");
		}

		log.debug("page   : " + page);

		try {
			setTitle(paramMap.get("plugin_userprofile_pagetitle"));

			if (page.equals("search")) {
				s.append(getMainpage());
			}
			else if (page.equals("results")) {
				s.append(getResults());
			}
			else {
				s.append(getMainpage());
			}
		}
		catch (NullPointerException n) {
			s.append(getMainpage());
			setTitle(paramMap.get("plugin_userprofile_pagetitle"));
		}

		paramMap.put("plugin_page", null);

		return s.toString();
	}

	public String getResults() throws NullPointerException {

		Config config = (Config) DAOManager.getInstance().getConfigDAO().findByID(1);

		StringBuffer results = new StringBuffer();

		log.info("Searching users with name containing " + username + ".");

		List<?> users = DAOManager.getInstance().getUserDAO().findAllItems();

		log.info("Found " + users.size() + " users.");

		boolean userFound = false;

		Table table = new Table();

		for (Iterator<?> it = users.iterator(); it.hasNext();) {

			User user = (User) it.next();

			if (user.getName().contains(username) || username.equals("*")) {

				userFound = true;

				TableRow row = new TableRow();

				TableColumn column = new TableColumn();

				Link link = new Link();
				link.setHref(protocol + "://" + request.getServerName() + ":" + request.getServerPort() + "/"
						+ config.getLocalPath() + "users/UserAction.do?do=showProfile&user_id=" + user.getId());
				link.setData(user.getName());
				link.setTarget("_self");

				column.setData(link.render());
				row.addColumn(column);
				table.addRow(row);

			}

		}

		if (userFound)
			results.append(table.render());
		else
			results.append(resourceBundle.getString("error.nousersfound"));

		String url = protocol + "://" + request.getServerName() + ":" + request.getServerPort() + "/" + config.getLocalPath()
				+ "templates/" + config.getTemplate() + "/UserSearchResults.jsp";
		String formLayout = URLReader.getDefaultContent(url, null);

		page = null;

		return formLayout.replace("##RESULTS##", results.toString());

	}

	public String getMainpage() throws NullPointerException {

		Config config = (Config) DAOManager.getInstance().getConfigDAO().findByID(1);

		StringBuffer s = new StringBuffer();

		UserSession sessionManager = new UserSession();
		sessionManager.init(false, request);
		sessionManager.getAttributes();

		if (sessionManager.getLogin() != null) {
			String url = protocol + "://" + request.getServerName() + ":" + request.getServerPort() + "/" + config.getLocalPath()
					+ "templates/" + config.getTemplate() + "/UserSearch.jsp";

			String formLayout = URLReader.getDefaultContent(url, null);

			if (formLayout == null) {
				return "Error : Missing resource " + url;
			}

			s.append("<form name=\"UserSearchForm\" method=\"post\" action=\"" + formAction + "&plugin_page=results\">");
			s.append(formLayout);
			s.append("</form>");
		}
		else
			s.append("You must be logged in in order to search for users.");

		return s.toString();
	}

}
