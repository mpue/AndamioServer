package org.pmedv.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.cms.daos.DownloadDAO;
import org.pmedv.core.crypto.MD5Crypter;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.Download;
import org.pmedv.pojos.User;
import org.pmedv.util.ResourceUtils;

public class AsyncUploadServlet extends HttpServlet implements Servlet {

	private static final long serialVersionUID = 2740693677625051632L;

	private static final Log log = LogFactory.getLog(AsyncUploadServlet.class);
	
	private final Config config;
	
	public AsyncUploadServlet() {
		super();
		config = (Config) DAOManager.getInstance().getConfigDAO().findByID(1);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		FileUploadListener listener = null;
		StringBuffer sb = new StringBuffer();
		long bytesRead = 0, contentLength = 0;

		if (session == null) {
			return;
		}
		else if (session != null) {
			listener = (FileUploadListener) session.getAttribute("LISTENER");

			if (listener == null) {
				return;
			}
			else {
				bytesRead = listener.getBytesRead();
				contentLength = listener.getContentLength();
			}
		}

		response.setContentType("text/xml");

		sb.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
		sb.append("<response>\n");
		sb.append("\t<bytes_read>" + bytesRead + "</bytes_read>\n");
		sb.append("\t<content_length>" + contentLength + "</content_length>\n");

		if (bytesRead == contentLength) {
			sb.append("\t<finished />\n");
			session.setAttribute("LISTENER", null);
		}
		else {
			sb.append("\t<percent_complete>" + listener.getPercentComplete() + "</percent_complete>\n");
		}
		sb.append("</response>\n");
		out.println(sb.toString());
		out.flush();
		out.close();
	}

	@SuppressWarnings({ "rawtypes", "unused" })
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		FileUploadListener listener = new FileUploadListener();
		HttpSession session = request.getSession();
		session.setAttribute("LISTENER", listener);

		upload.setProgressListener(listener);
		
		List uploadedItems = null;
		FileItem fileItem = null;
		
		// String directory = (String)request.getSession().getAttribute("uploadDirectory");
		String directory ="upload/data/";
		// we need to know for which domain the download is created
		// String domain    = (String)request.getSession().getAttribute("uploadDomain");
		String domain = "localhost";
		String filePath = config.getBasepath() + directory;
		String userName = (String) request.getSession().getAttribute("login");

		String name = null;
		String description = null;
		String fileName = null;
		String author = null;
		String license = null;
		String tags = null;
		boolean publicfile = false;
		boolean downloadable = false;
		String categories = null;
		String mimetype = null;
		long filesize = 0L;
		
		User user = null;
		
		if (userName != null) {
			user = (User) DAOManager.getInstance().getUserDAO().findByName(userName);
		}
		
		try {
			uploadedItems = upload.parseRequest(request);
			Iterator i = uploadedItems.iterator();
		
			while (i.hasNext()) {
				fileItem = (FileItem) i.next();
				if (fileItem.isFormField() == false) {
					if (fileItem.getSize() > 0) {
						File uploadedFile = null;
						String myFullFileName = fileItem.getName();
						fileName = ""; 
						String slashType = (myFullFileName.lastIndexOf("\\") > 0) ? "\\" : "/";
						int startIndex = myFullFileName.lastIndexOf(slashType);
						fileName = myFullFileName.substring(startIndex + 1, myFullFileName.length());
												
						String extension = fileName.substring(fileName.lastIndexOf("."));
						String tempName = fileName+System.currentTimeMillis();
						fileName = MD5Crypter.createMD5String(tempName.getBytes());
						fileName += extension;
						
						uploadedFile = new File(filePath, fileName);
						fileItem.write(uploadedFile);

						filesize = uploadedFile.length();
						
						mimetype = ResourceUtils.getMimeType(uploadedFile);
					}
				}
				else {
					
					if (fileItem.getFieldName().equalsIgnoreCase("name")) {
						name = fileItem.getString();						
					}
					else if(fileItem.getFieldName().equalsIgnoreCase("description")) {
						description = fileItem.getString();
					}
					else if(fileItem.getFieldName().equalsIgnoreCase("author")) {
						author = fileItem.getString();
					}
					else if(fileItem.getFieldName().equalsIgnoreCase("license")) {
						license = fileItem.getString();
					}
					else if(fileItem.getFieldName().equalsIgnoreCase("tags")) {
						tags = fileItem.getString();
					}
					else if(fileItem.getFieldName().equalsIgnoreCase("categories")) {
						categories = fileItem.getString();
					}					
					else if(fileItem.getFieldName().equalsIgnoreCase("publicfile")) {
						if(fileItem.getString() != null && "on".equalsIgnoreCase(fileItem.getString())) {
							publicfile = true;
						}
					}
					else if(fileItem.getFieldName().equalsIgnoreCase("downloadable")) {
						if(fileItem.getString() != null && "on".equalsIgnoreCase(fileItem.getString())) {
							downloadable = true;
						}
					}
										
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			log.error("Error uploading file : "+fileName);
		}
		
		Download d = new Download();
		
		d.setFilename(fileName);
		d.setName(name);
		d.setDescription(description);
		d.setHits(0);
		d.setPath(directory);
		d.setRanking(0.0f);
		d.setAuthor(author);
		d.setLicense(license);
		d.setDownloadable(downloadable);
		d.setPublicfile(publicfile);
		d.setTags(tags);
		d.setDomain(domain);
		d.setMimetype(mimetype);
		d.setFilesize(filesize);
		
		if (user != null) {
			d.setUploader(user.getLastName()+", "+user.getFirstName());	
		}
		
		d.setUploadTime(new Date());
		
		DownloadDAO dao = DAOManager.getInstance().getDownloadDAO();
		
		d = (Download) dao.createAndStore(d);

		// add categories if any
		if (categories != null && categories.length() > 0) {
			
			String[] categoryList = categories.split(" ");
			
			for (String value : categoryList) {				
				Long id = Long.valueOf(value);
				dao.addCategory(id, d.getId());				
			}
			
		}
		
	}
}