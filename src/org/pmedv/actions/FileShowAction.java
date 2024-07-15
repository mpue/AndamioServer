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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONSerializer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.pmedv.cms.common.GlobalForwards;
import org.pmedv.cms.common.Params;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.file.FileManager;
import org.pmedv.forms.FileForm;
import org.pmedv.pojos.Config;
 

public class FileShowAction extends AbstractPermissiveDispatchAction
{
	
	private static final Log log = LogFactory.getLog(FileShowAction.class);
	
	public FileShowAction() {
		super();
		log.debug("Executing "+this.getClass());
		setName("menu.filemanager");		
	}
	
  /** 
   * Traverse specified directory load contents and save them in the request
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
	

	public ActionForward editFile(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		if (!isAllowedToProcess(request, getName())) return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);
						      	
    	Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);
		String filename = request.getParameter(Params.FILENAME);

		log.info("Requested file is : "+filename);
		
		try {		
		
			String fileContent = FileUtils.readFileToString(new File(config.getBasepath()+filename));
			
			StringBuffer jsonString = new StringBuffer();
			
			jsonString.append("{\"success\":true,\"data\":");
			
			Data data = new Data();		
			data.setFilename(filename);
			data.setContent(fileContent);
			
			jsonString.append(JSONSerializer.toJSON(data));			
			jsonString.append("}");
			
			response.setContentLength(jsonString.length());
			response.setContentType("text/plain");
			
			PrintWriter out = response.getWriter();
			out.print(jsonString.toString());
			out.flush();
			
		} 
		catch (IOException e) {
			log.info("An exception occured : "+e.getStackTrace().toString());
		}
		return null;
			
	}
	
	public static  class Data {
		
		private String filename;
		private String content;

		/**
		 * @return the filename
		 */
		public String getFilename() {
		
			return filename;
		}
		
		/**
		 * @param filename the filename to set
		 */
		public void setFilename(String filename) {
		
			this.filename = filename;
		}
		
		/**
		 * @return the content
		 */
		public String getContent() {
		
			return content;
		}
		
		/**
		 * @param content the content to set
		 */
		public void setContent(String content) {
		
			this.content = content;
		}		
		
	}
	
	public ActionForward renameFile (
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		if (!isAllowedToProcess(request, getName())) return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);
		
		FileForm fileForm = (FileForm) form;		

		String dir = request.getParameter(Params.DIR);
		String filename = request.getParameter(Params.FILENAME);
		
		request.getSession().setAttribute(Params.FILENAME,filename);
		request.getSession().setAttribute(Params.DIR,dir);
		
		try {
			fileForm.setDir(dir);
			fileForm.setFilename(filename);			

		}
		catch (Exception i) {
			i.printStackTrace();
		}
		return mapping.findForward(GlobalForwards.SHOW_FILE_NAME);
			
	}
	
	public ActionForward saveName (
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		if (!isAllowedToProcess(request, getName())) return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);
  	
    	Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);

		FileForm fileForm = (FileForm) form;		

		String dir = request.getParameter(Params.DIR);
		String filename = request.getParameter(Params.FILENAME);
		
		request.setAttribute(Params.FILENAME,filename);
		request.setAttribute(Params.DIR,dir);
		
		try {
			FileManager fileManager = new FileManager();
			fileManager.setFilename(fileForm.getFilename());
			fileManager.setFilepath(config.getBasepath()+dir);
			fileManager.setNewName(fileForm.getNewName());
			fileManager.setNewPath(config.getBasepath()+dir);
			fileManager.rename();
		}
		catch (Exception i) {
			i.printStackTrace();
		}
        ActionForward af = new ActionForward();

        af.setPath("/admin/ListDirectories.do?do=enter&dir="+dir);

        return af;
	}
	
	
	
	public ActionForward saveFile(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		if (!isAllowedToProcess(request, getName())) return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);
      	
    	Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);
		String filename = request.getParameter(Params.FILENAME);
		
		try {		
			
			String filecontent = request.getParameter("content");
			
			FileUtils.writeStringToFile(new File(config.getBasepath()+filename), filecontent);
			
			String message = "{\"success\":true, message : \"Changes saved.\" }";
			
			response.setContentLength(message.length());
			response.setContentType("text/plain");
			
			PrintWriter out = response.getWriter();
			out.print(message);
			out.flush();
	
		} 
		catch (IOException e) {
			log.info("An exception occured : "+e.getStackTrace().toString());
		}
		
		return null;
		
	}
			
	
}

