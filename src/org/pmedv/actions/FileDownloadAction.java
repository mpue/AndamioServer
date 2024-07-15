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
/*
 * $Header: /var/lib/cvs/AndamioServer/src/org/pmedv/actions/FileDownloadAction.java,v 1.3 2013-02-07 20:08:40 mpue Exp $
 * $Revision: 1.3 $
 * $Date: 2013-02-07 20:08:40 $
 *
 * Copyright 2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.pmedv.actions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.pmedv.cms.common.Params;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.Download;

/**
 * This is an implementation of the DownloadAction class that allows for
 * downloading of files from the file system. This makes use of the custom
 * action mapping FileDownloadActionMapping so that the required parameters for
 * the download can be included in the action mapping. Note however that the
 * parameters can be optionally overriden by what comes in the request. Note
 * that the download can come from anywhere in the file system, not just
 * relative to the webapp, and that includes mapped drives or mounted volumes.
 * 
 * This class is based on a technique of <a
 * href="mailto:frank@zammetti.com">Frank W. Zammetti</a>
 * 
 * @since andamio 1.1
 * @author <a href="mailto:pueski@gmx.de">Matthias Pueski</a>
 */
public class FileDownloadAction extends AbstractDownloadAction {

	private static final Log log = LogFactory.getLog(FileDownloadAction.class);
	
	/**
	 * Returns the information on the file, or other stream, to be downloaded by
	 * this action.
	 * 
	 * @param mapping The ActionMapping used to select this instance.
	 * @param form The optional ActionForm bean for this request (if any).
	 * @param request The HTTP request we are processing.
	 * @param response The HTTP response we are creating.
	 * 
	 * @return The information for the file to be downloaded.
	 * 
	 * @throws Exception if an exception occurs.
	 */
	protected StreamInfo getStreamInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Config config = (Config) DAOManager.getInstance().getConfigDAO().findByID(1);

		FileDownloadActionMapping m = (FileDownloadActionMapping) mapping;
		String filePath = m.getFilePath();
		String contentType = m.getContentType();

		if (request.getParameter(Params.FILEPATH) != null) {

			String downloadPath = (String) request.getParameter(Params.FILEPATH);

			filePath = config.getBasepath() + downloadPath;

			String newFileName = "";

			if (downloadPath.indexOf("/") > -1) {
				newFileName = downloadPath.substring(downloadPath.lastIndexOf("/") + 1);
			}
			else {
				newFileName = downloadPath;
			}

			response.setHeader("Content-Disposition", "attachment; filename=" + newFileName);
		}

		if (request.getParameter(Params.ID_DOWNLOAD) != null) {
			long download_id = Long.parseLong(request.getParameter("download_id"));
			Download download = (Download) DAOManager.getInstance().getDownloadDAO().findByID(download_id);
			filePath = config.getBasepath() + config.getDownloadpath() + download.getFilename();

			// construct a human readable filename for download
			response.setHeader("Content-Disposition", "attachment; filename=" + download.getFilename());
			response.setHeader("Content-Length", String.valueOf(new File(filePath).length()));

			// increase download hitcount and store

			download.setHits(download.getHits() + 1);
			DAOManager.getInstance().getDownloadDAO().update(download);
		}
		if (request.getParameter(Params.CONTENTTYPE) != null) {
			contentType = (String) request.getParameter(Params.CONTENTTYPE);
		}
		if (request.getParameter("filename") != null) {

			for (Enumeration<?> e = request.getHeaderNames(); e.hasMoreElements();) {
				String header = (String) e.nextElement();
				log.info(header + "=" + request.getHeader(header));
			}

			String filename = request.getParameter("filename");
			Download download = (Download) DAOManager.getInstance().getDownloadDAO().findByFileName(filename);
			filePath = config.getBasepath() + config.getDownloadpath() + download.getFilename();

			// construct a human readable filename for download
			response.setHeader("Content-Disposition", "attachment; filename=" + download.getName());
			response.setHeader("Content-Length", String.valueOf(new File(filePath).length()));

			// increase download hitcount and store

			download.setDownloads(download.getDownloads() + 1);
			DAOManager.getInstance().getDownloadDAO().update(download);
		}

		return new SI(contentType, new File(filePath));
	}

	/**
	 * This class represents the pertinent details about the file to be
	 * downloaded.
	 * 
	 * @since andamio 1.1
	 * 
	 */
	public static class SI implements StreamInfo {

		private String contentType;
		private File file;

		public SI(String contentType, File file) {
			this.contentType = contentType;
			this.file = file;
		}

		public String getContentType() {
			return this.contentType;
		}

		/**
		 * Method to get a stream on the file to download
		 * 
		 * @return The InputStream wrapping the file to download
		 * 
		 */
		public InputStream getInputStream() throws IOException {
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			return bis;
		}

	}

}
