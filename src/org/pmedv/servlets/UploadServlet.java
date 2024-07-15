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
package org.pmedv.servlets;

import http.utils.multipartrequest.MultipartRequest;
import http.utils.multipartrequest.ServletMultipartRequest;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.Config;
import org.pmedv.util.UploadManager;

public class UploadServlet extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	private static final Log	log					= LogFactory.getLog(UploadServlet.class);

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");

		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);

		String filepath = null;

		if (request.getParameter("content") != null) {
			filepath = config.getBasepath() + config.getContentpath();
		}
		else {
			filepath = config.getBasepath() + config.getImportpath();
		}

		UploadManager uploadManager = new UploadManager();

		try {

			MultipartRequest parser = new ServletMultipartRequest(request, MultipartRequest.MAX_READ_BYTES,
					MultipartRequest.IGNORE_FILES_IF_MAX_BYES_EXCEEDED, null);

			for (Enumeration<?> e = parser.getFileParameterNames(); e.hasMoreElements();) {

				String name = (String) e.nextElement();

				log.info("Receiving file : " + name);

				uploadManager.setFilepath(filepath);
				uploadManager.setName(name);
				uploadManager.setParser(parser);
				uploadManager.upload();
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

}
