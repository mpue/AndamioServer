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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.pmedv.cms.common.GlobalForwards;
import org.pmedv.cms.common.Params;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.forms.UploadForm;
import org.pmedv.pojos.Config;

/**
 * This action performs the file uploading
 * 
 * @author Matthias Pueski
 * 
 */
public class UploadAction extends AbstractPermissiveDispatchAction {
	private static final Log log = LogFactory.getLog(UploadAction.class);

	// TODO : Use method from FileUtils

	public UploadAction() {
		super();
		log.debug("Executing " + this.getClass());
		setName("menu.filemanager");
	}

	public ActionForward upload(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (!isAllowedToProcess(request, getName()))
			return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);

		if (form instanceof UploadForm) {

			Config config = (Config) DAOManager.getInstance().getConfigDAO().findByID(1L);

			// this line is here for when the input page is upload-utf8.jsp,
			// it sets the correct character encoding for the response

			String encoding = request.getCharacterEncoding();
			if ((encoding != null) && (encoding.equalsIgnoreCase("utf-8"))) {
				response.setContentType("text/html; charset=utf-8");
			}

			UploadForm theForm = (UploadForm) form;

			// retrieve the text data
			String text = theForm.getTheText();

			// retrieve the query string value

			// String queryValue = theForm.getQueryParam();

			// retrieve the file representation
			FormFile file = theForm.getTheFile();

			// retrieve the file name
			String fileName = file.getFileName();

			// retrieve the content type
			String contentType = file.getContentType();

			String directory = theForm.getDirectory();

			boolean writeFile = theForm.getWriteFile();

			// retrieve the file size
			String size = (file.getFileSize() + " bytes");

			String data = null;

			try {
				// retrieve the file data

				InputStream stream = file.getInputStream();

				// write the file to the file specified

				OutputStream bos = new FileOutputStream(config.getBasepath() + directory + fileName);
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
					bos.write(buffer, 0, bytesRead);
				}
				bos.close();
				data = "The file has been written to \"" + config.getBasepath() + directory + fileName + "\"";
				log.debug(data);

				// close the stream

				stream.close();
			}
			catch (FileNotFoundException fnfe) {
				if (request.getParameter("async") != null) {

					PrintWriter out;

					try {
						out = response.getWriter();
						out.print("{\"success\":false,\"message\":\"Datei nicht gefunden.\"}");
						out.flush();

					}
					catch (IOException e) {
						e.printStackTrace();
					}

				}

			}
			catch (IOException ioe) {
				if (request.getParameter("async") != null) {

					PrintWriter out;

					try {
						out = response.getWriter();
						out.print("{\"success\":false,\"message\":\"Unbekannter Fehler aufgetreten.\"}");
						out.flush();

					}
					catch (IOException e) {
						e.printStackTrace();
					}

				}

			}

			// destroy the temporary file created

			file.destroy();

			if (request.getParameter("async") != null) {

				PrintWriter out;

				try {
					out = response.getWriter();
					out.print("{\"success\":true,\"message\":\"Datei erfolgreich hochgeladen.\"}");
					out.flush();

				}
				catch (IOException e) {
					e.printStackTrace();
				}

			}
			else {

				ActionForward af = new ActionForward();
				af.setPath("/admin/ListDirectories.do?do=enter&dir=" + directory);
				return af;
			}

		}

		// this shouldn't happen i
		return null;
	}

	public ActionForward importFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		if (!isAllowedToProcess(request, getName()))
			return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);

		if (form instanceof UploadForm) {

			Config config = (Config) DAOManager.getInstance().getConfigDAO().findByID(1L);

			// this line is here for when the input page is upload-utf8.jsp,
			// it sets the correct character encoding for the response
			String encoding = request.getCharacterEncoding();
			if ((encoding != null) && (encoding.equalsIgnoreCase("utf-8"))) {
				response.setContentType("text/html; charset=utf-8");
			}

			UploadForm theForm = (UploadForm) form;

			// retrieve the text data
			String text = theForm.getTheText();

			// retrieve the query string value

			String queryValue = theForm.getQueryParam();

			// retrieve the file representation
			FormFile file = theForm.getTheFile();

			// retrieve the file name
			String fileName = file.getFileName();

			// retrieve the content type
			String contentType = file.getContentType();

			String directory = theForm.getDirectory();

			boolean writeFile = theForm.getWriteFile();

			// retrieve the file size
			String size = (file.getFileSize() + " bytes");

			String data = null;

			try {
				// retrieve the file data

				InputStream stream = file.getInputStream();

				// write the file to the file specified

				OutputStream bos = new FileOutputStream(config.getBasepath() + directory + fileName);
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
					bos.write(buffer, 0, bytesRead);
				}
				bos.close();
				data = "The file has been written to \"" + config.getBasepath() + directory + fileName + "\"";

				log.debug(data);
				// close the stream

				stream.close();
			}
			catch (FileNotFoundException fnfe) {
				return null;
			}
			catch (IOException ioe) {
				return null;
			}

			// destroy the temporary file created

			file.destroy();

			ActionForward af = new ActionForward();

			af.setPath("/admin/ShowNode.do?do=importNodes&replace=" + theForm.getReplaceNodes());

			return af;

		}

		// this shouldn't happen i
		return null;
	}

	public ActionForward importODT(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		if (!isAllowedToProcess(request, getName()))
			return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);

		if (form instanceof UploadForm) {

			Config config = (Config) DAOManager.getInstance().getConfigDAO().findByID(1L);

			// this line is here for when the input page is upload-utf8.jsp,
			// it sets the correct character encoding for the response
			String encoding = request.getCharacterEncoding();
			if ((encoding != null) && (encoding.equalsIgnoreCase("utf-8"))) {
				response.setContentType("text/html; charset=utf-8");
			}

			UploadForm theForm = (UploadForm) form;

			// retrieve the text data
			String text = theForm.getTheText();

			// retrieve the query string value

			String queryValue = theForm.getQueryParam();

			// retrieve the file representation
			FormFile file = theForm.getTheFile();

			// retrieve the file name
			String fileName = "document.odt";

			// retrieve the content type
			String contentType = file.getContentType();

			String directory = theForm.getDirectory();

			boolean writeFile = theForm.getWriteFile();

			// retrieve the file size
			String size = (file.getFileSize() + " bytes");

			String data = null;

			try {
				// retrieve the file data

				InputStream stream = file.getInputStream();

				// write the file to the file specified

				OutputStream bos = new FileOutputStream(config.getBasepath() + directory + fileName);
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
					bos.write(buffer, 0, bytesRead);
				}
				bos.close();
				data = "The file has been written to \"" + config.getBasepath() + directory + fileName + "\"";

				log.debug(data);
				// close the stream

				stream.close();
			}
			catch (FileNotFoundException fnfe) {
				return null;
			}
			catch (IOException ioe) {
				return null;
			}

			// destroy the temporary file created

			file.destroy();

			ActionForward af = new ActionForward();

			af.setPath("/admin/ShowNode.do?do=importODT");

			return af;

		}

		// this shouldn't happen i
		return null;
	}

	public ActionForward init(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		UploadForm uploadForm = new UploadForm();

		if (request.getParameter("importNodes") != null) {
			uploadForm.setDirectory("import/");
			return mapping.findForward(GlobalForwards.IMPORT_NODES);
		}
		if (request.getParameter("importODT") != null) {
			uploadForm.setDirectory("import/");
			return mapping.findForward(GlobalForwards.IMPORT_ODT);
		}

		else {
			String dir = request.getParameter(Params.DIR);
			uploadForm.setDirectory(dir);
			return mapping.findForward(GlobalForwards.UPLOAD_FILE);
		}

	}

}
