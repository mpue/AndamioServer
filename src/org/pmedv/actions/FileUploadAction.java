/**

	AndamioCMS - Open Source Content Management
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatch;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.forms.FileUploadForm;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.Download;
import org.pmedv.pojos.User;
import org.pmedv.util.ResourceUtils;

public class FileUploadAction extends DispatchAction {

	public ActionForward prepareUpload(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		FileUploadForm uploadForm = (FileUploadForm) form;
		
		String directory = uploadForm.getDirectory();

		if (directory == null) {
			directory = (String) request.getSession().getAttribute("directory");
		}
		else {
			request.getSession().setAttribute("directory", directory);
		}
		return mapping.findForward("upload");
			
	}
	
	public ActionForward upload(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		String email = null;
		
		if (form instanceof FileUploadForm) {

			Config config = (Config) DAOManager.getInstance().getConfigDAO().findByID(1);

			String mimeFile = config.getBasepath() + "WEB-INF/classes/allowedMimeTypes.properties";
			ArrayList<String> mimeTypes = readFileToArray(new File(mimeFile));
			
			// this line is here for when the input page is upload-utf8.jsp,
			// it sets the correct character encoding for the response

			String encoding = request.getCharacterEncoding();
			if ((encoding != null) && (encoding.equalsIgnoreCase("utf-8"))) {
				response.setContentType("text/html; charset=utf-8");
			}

			FileUploadForm uploadForm = (FileUploadForm) form;
			
			// retrieve the file representation
			FormFile file = uploadForm.getUploadFile();
			// retrieve the file name
			String fileName = file.getFileName();
			// retrieve the content type
			String contentType = file.getContentType();
			String directory = uploadForm.getDirectory();

			if (directory == null) {
				directory = (String) request.getSession().getAttribute("directory");
			}
			else {
				request.getSession().setAttribute("directory", directory);
			}
			
			User user = null;
			
			String userName = (String) request.getSession().getAttribute("login");
			
			if (userName != null) {
				user = (User) DAOManager.getInstance().getUserDAO().findByName(userName);
			}
			
			String name = uploadForm.getName();
			String description = uploadForm.getDescription();
			
			boolean writeFile = uploadForm.getWriteFile();

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

				File f = new File(config.getBasepath() + directory + fileName);				
				String mimetype = getMimeType(f);
				log.info("File has mime type : " + mimetype);

				if (!mimeTypes.contains(mimetype)) {					
					ActionMessages actionMessages = new ActionMessages();
			        actionMessages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("fileTypeNotSupported"));
			        if (!f.delete()) {
			        	log.error("Could not delete "+f.getName());
			        }
			        saveErrors(request, actionMessages);
			        return mapping.findForward("upload");					
				}
				
				
			}
			catch (FileNotFoundException fnfe) {
				
				ActionMessages actionMessages = new ActionMessages();
		        actionMessages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("noFileSpecified"));				
				
		        saveErrors(request, actionMessages);
		        log.error(ResourceUtils.getStackTrace(fnfe));
		        return mapping.findForward("upload");
				
				
			}
			catch (IOException ioe) {
				log.error(ResourceUtils.getStackTrace(ioe));
			}

			// destroy the temporary file created

			file.destroy();
			
			Download d = new Download();
			d.setFilename(fileName);
			d.setName(name);
			d.setDescription(description);
			d.setHits(0);
			d.setPath(directory);
			d.setRanking(0.0f);
			
			if (user != null) {
				d.setUploader(user.getLastName()+", "+user.getFirstName());	
			}
			
			d.setUploadTime(new Date());
			
			DAOManager.getInstance().getDownloadDAO().createAndStore(d);

		}

		if (request.getParameter("redirect") != null) {
			
			try {				
				response.sendRedirect(request.getParameter("redirect"));				
			}
			catch (IOException e) {
				log.error(ResourceUtils.getStackTrace(e));
				log.error("could not redirect to "+request.getParameter("redirect"));				
				return null;
			}
			
		}
		
		return null;

	}

	public ActionForward success(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("success");		
	}
	
	private String getMimeType(File fileToBeOpened) {

		String fileExtension = fileToBeOpened.getName().substring(fileToBeOpened.getName().lastIndexOf(".") + 1);

		String mimeType = null;

		try {
			MagicMatch match = Magic.getMagicMatch(fileToBeOpened, true);
			match.getMimeType();
			mimeType = match.getMimeType();
			log.info("found mime type " + mimeType);
		}
		catch (MagicMatchNotFoundException m) {

			if (fileToBeOpened.length() < 1) { // empty file, assume text mime
				// type
				mimeType = "text/plain";
				log.info("found empty file, assuming text format.");
			}
			else {
				log.info("Mimetype for file " + fileToBeOpened.getName() + " not found.");

				if (fileExtension.equalsIgnoreCase("txt") || fileExtension.equalsIgnoreCase("xsl")
						|| fileExtension.equalsIgnoreCase("log") || fileExtension.equalsIgnoreCase("css"))
					mimeType = "text/plain";
				else if (fileExtension.equalsIgnoreCase("xml")) {
					mimeType = "text/xhtml";
				}
				else if (fileExtension.equalsIgnoreCase("mp3")) {
					mimeType = "audio/mpeg";
				} 
				else if (fileExtension.equalsIgnoreCase("ogg")) {
					mimeType = "audio/ogg";
				} 				
				else if (fileExtension.equalsIgnoreCase("wav")) {
					mimeType = "audio/x-wav";
				} 
				else if (fileExtension.equalsIgnoreCase("wma")) {
					mimeType = "audio/x-ms-wma";
				} 
				
				else
					mimeType = "unknown";
			}
			return mimeType;

		}
		catch (MagicParseException e) {
			return "unknown";
		}
		catch (MagicException e) {
			return "unknown";
		}

		if (mimeType.startsWith("text")) {

			if (fileExtension.equalsIgnoreCase("xml") || fileExtension.equalsIgnoreCase("xsl")
					|| fileExtension.equalsIgnoreCase("jnlp"))
				mimeType = "text/xhtml";
			else if (fileExtension.equalsIgnoreCase("htm") || fileExtension.equalsIgnoreCase("html")
					|| fileExtension.equalsIgnoreCase("xhtml"))
				mimeType = "text/xhtml";
			else if (fileExtension.equalsIgnoreCase("bsh") || fileExtension.equalsIgnoreCase("BSH"))
				mimeType = "text/cpp";

		}

		if (mimeType.endsWith("x-c") || mimeType.endsWith("java")) {
			mimeType = "text/cpp";
		}

		if (mimeType.equals("text/html") || mimeType.equals("text/sgml"))
			mimeType = "text/xhtml";

		if (mimeType.contains("???")) {
			
			if (fileExtension.equalsIgnoreCase("txt") || 
				fileExtension.equalsIgnoreCase("xsl") || 
				fileExtension.equalsIgnoreCase("log") || 
				fileExtension.equalsIgnoreCase("css"))
				mimeType = "text/plain";
			else if (fileExtension.equalsIgnoreCase("xml")) {
				mimeType = "text/xhtml";
			}
			else if (fileExtension.equalsIgnoreCase("mp3")) {
				mimeType = "audio/mpeg";
			} 
			else if (fileExtension.equalsIgnoreCase("ogg")) {
				mimeType = "audio/ogg";
			} 				
			else if (fileExtension.equalsIgnoreCase("wav")) {
				mimeType = "audio/x-wav";
			} 
			else if (fileExtension.equalsIgnoreCase("wma")) {
				mimeType = "audio/x-ms-wma";
			} 
			
			else
				mimeType = "unknown";

		}
		
		log.info("using mime type " + mimeType);

		return mimeType;
	}

	public static ArrayList<String> readFileToArray(File file) {

		ArrayList<String> lines = new ArrayList<String>();
		
		StringBuffer fileBuffer;
		String fileString = null;
		String line;

		try {
			FileReader in = new FileReader(file);
			BufferedReader dis = new BufferedReader(in);
			fileBuffer = new StringBuffer();

			while ((line = dis.readLine()) != null) {
				lines.add(line);
			}

			in.close();
			dis.close();
			fileString = fileBuffer.toString();
		}
		catch (IOException e) {
			return null;
		}
		return lines;
	}	
	
}
