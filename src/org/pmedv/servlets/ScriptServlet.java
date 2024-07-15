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
 * 
 */
package org.pmedv.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.User;
import org.pmedv.web.ServerUtil;

/**
 * <p>
 * The ScriptServlet is used in order to be able to replace tokens inside a
 * JavaScript file. It simply reads the given script and replaces the following
 * tokens:
 * </p>
 * <p>
 * <ul>
 * <li>##LOCALPATH## - This is the path relative to the CMS root</li>
 * <li>##HOSTNAME## - The name of the host running the CMS on</li>
 * <li>##TEMPLATE## - The current template for the root</li>
 * </ul>
 * </p>
 * <p>
 * If any user session exists the following token is also being replaced:
 * </p>
 * <p>
 * ##USERNAME## - the login name of the current user.
 * </p>
 * 
 * @author Matthias Pueski
 * 
 */
public class ScriptServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(ScriptServlet.class);

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/javascript");

		Config config = (Config) DAOManager.getInstance().getConfigDAO().findByID(1L);
		String filepath = config.getBasepath() + "jscripts/";

		log.debug("Took script path " + filepath);

		String scriptName = request.getParameter("name");

		if (scriptName != null) {

			log.debug("Reading script from file " + filepath + scriptName);
			String scriptContent = FileUtils.readFileToString(new File(filepath + scriptName));

			scriptContent = scriptContent.replaceAll("##LOCALPATH##", config.getLocalPath());
			scriptContent = scriptContent.replaceAll("##HOSTNAME##", ServerUtil.getHostUrl(request));
			scriptContent = scriptContent.replaceAll("##TEMPLATE##", config.getTemplate());

			try {
				Long userId = (Long) request.getSession().getAttribute("userId");
				User user = (User) DAOManager.getInstance().getUserDAO().findByID(userId);

				if (user != null)
					scriptContent = scriptContent.replaceAll("##USERNAME##", user.getName());
			}
			catch (Exception e) {
				log.debug("anonymous, not replacing any token.");
			}

			PrintWriter out = response.getWriter();
			out.write(scriptContent);
			out.flush();

		}

	}

}
