package org.pmedv.forms.forums;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.core.crypto.MD5Crypter;
import org.pmedv.forms.AbstractActionForm;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.forums.Attachment;

public class PostingForm extends AbstractActionForm {
	
	private static final long serialVersionUID = -3900456689704174382L;

	private String title;
	private String postingtext;
	private ArrayList<Attachment> attachments;
	private FormFile uploadFile;

	public PostingForm() {
		attachments = new ArrayList<Attachment>();
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		setName(null);
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		
		ActionErrors errors = new ActionErrors();
		
		if (getTitle() == null || getTitle().length() < 1) {
			errors.add("title", new ActionMessage("error.title.required"));
		}		
		if  (request.getParameter("thread_id") != null && 
			!request.getParameter("thread_id").equals("null")) {
			request.getSession().setAttribute("thread_id", Long.valueOf(request.getParameter("thread_id")));
		}		
		if  (request.getParameter("category_id") != null && 
			!request.getParameter("category_id").equals("null")) {
			request.getSession().setAttribute("category_id", Long.valueOf(request.getParameter("category_id")));
		}		
		/**
		 * This is part of the hack below. The view (WebContent/forum/createPosting.jsp) contains two submit buttons, 
		 * which trigger some JavaScript. The javascript decides whether the form is being submitted or
		 * a new attachment will be added. If an attachment will be added, the parameter addAttachment
		 * is set to true, and the web flow is forced to add an error to the struts request.
		 * 
		 * See also ThreadForm, ThreadAction and WebContent/forum/createThread.jsp
		 * 
		 */
		
		if (request.getParameter("addAttachment") != null && 
			request.getParameter("addAttachment").equalsIgnoreCase("true")) {
			errors.add("name", new ActionMessage("error.empty"));
		}
		
		/** 
		 * This is less or more a sloppy hack:
		 * 
		 * We perform the file upload actually here, because we need to be able to add attachments
		 * to a posting before the posting is being created. 
		 */
		
		if (uploadFile != null && uploadFile.getFileName() != null && 
			uploadFile.getFileName().length() > 0) {
			
			Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1L);
			String directory = "attachments/";

			String timestamp = new Date().toString();			
			String filename  = MD5Crypter.createMD5String((uploadFile.getFileName()+timestamp).getBytes());
			String extension = uploadFile.getFileName().substring(uploadFile.getFileName().lastIndexOf(".")); 
			
			
			Attachment att = new Attachment();
			att.setName(uploadFile.getFileName());
			att.setFilename(filename + extension);
			att.setHits(0);
			att.setUploadTime(new Date());
			
			try {
				InputStream stream = uploadFile.getInputStream();
				OutputStream bos = new FileOutputStream(config.getBasepath() + directory + att.getFilename());

				int bytesRead = 0;
				byte[] buffer = new byte[8192];

				while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
					bos.write(buffer, 0, bytesRead);
				}

				bos.close();
				stream.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			if (request.getSession().getAttribute("attachments") != null) {
				ArrayList<Attachment> attachments = (ArrayList<Attachment>)request.getSession().getAttribute("attachments");
				
				attachments.add(att);
				request.getSession().setAttribute("attachments", attachments);
				this.attachments.clear();
				
				for (Attachment a : attachments) {
					this.attachments.add(a);
				}				
			}
			else {
				attachments.add(att);
				request.getSession().setAttribute("attachments", attachments);
			}
		}
		
		return errors;
	}

	public String getPostingtext() {
		return postingtext;
	}

	public void setPostingtext(String postingtext) {
		this.postingtext = postingtext;
	}

	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the attachments
	 */
	public ArrayList<Attachment> getAttachments() {
		return attachments;
	}

	/**
	 * @param attachments the attachments to set
	 */
	public void setAttachments(ArrayList<Attachment> attachments) {
		this.attachments = attachments;
	}

	/**
	 * @return the uploadFile
	 */
	public FormFile getUploadFile() {
		return uploadFile;
	}

	/**
	 * @param uploadFile the uploadFile to set
	 */
	public void setUploadFile(FormFile uploadFile) {
		this.uploadFile = uploadFile;
	}
}