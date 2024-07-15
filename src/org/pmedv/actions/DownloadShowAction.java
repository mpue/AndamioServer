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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.pmedv.cms.common.GlobalForwards;
import org.pmedv.cms.common.Params;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.file.FileManager;
import org.pmedv.forms.DownloadShowForm;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.Download;
 

public class DownloadShowAction  extends AbstractPermissiveDispatchAction {
	
	public DownloadShowAction() {
		super();
		log.debug("Executing "+this.getClass());
		setName("menu.downloads");		
	}

	public ActionForward edit(
			ActionMapping mapping,
		    ActionForm form,
		    HttpServletRequest request,
		    HttpServletResponse response) throws Exception{
		
		if (!isAllowedToProcess(request, getName())) return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);
		
		DownloadShowForm downloadShowForm = (DownloadShowForm) form;
			
		int download_id  = Integer.parseInt(request.getParameter(Params.ID_DOWNLOAD));
		
		Download download = (Download)DAOManager.getInstance().getDownloadDAO().findByID(download_id);
			
	    log.debug("editing download with id "+download_id);
	    
	    downloadShowForm.setName(download.getName());
	    downloadShowForm.setDescription(download.getDescription());
	    downloadShowForm.setFilename(download.getFilename());
	    downloadShowForm.setId(download.getId());
	    downloadShowForm.setPath(download.getPath());

        return mapping.findForward(GlobalForwards.SHOW_DOWNLOAD_DETAILS);
		
	}
	
	/**
	 * Removes download from db and local store. 
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	
	public ActionForward delete(
			ActionMapping mapping,
		    ActionForm form,
		    HttpServletRequest request,
		    HttpServletResponse response) throws Exception{
		
		if (!isAllowedToProcess(request, getName())) return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);
				
		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);
		
		int download_id  = Integer.parseInt(request.getParameter(Params.ID_DOWNLOAD));
		
		Download download = (Download)DAOManager.getInstance().getDownloadDAO().findByID(download_id);
		
	    log.debug("removing download with id"+ download_id);
		
	    DAOManager.getInstance().getDownloadDAO().delete(download);
	    
		// TODO : remove hardcoded path elements
		
		FileManager filemanager = new FileManager();
		filemanager.setFilename(download.getFilename());
		filemanager.setFilepath(config.getDownloadpath());
		filemanager.delete();
		
		// Create custom Action Mapping
		
        ActionForward af = new ActionForward();

        af.setPath("/admin/ListDownloads.do");

        return af;
		
	}
	
	
	/**
	 * Saves modified download (contents only, file remains unchanged)
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	
	public ActionForward save(
			ActionMapping mapping, 
			ActionForm form,
			HttpServletRequest request, 
			HttpServletResponse response) {

		if (!isAllowedToProcess(request, getName())) return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);
		
	    DownloadShowForm downloadShowForm = (DownloadShowForm) form;
	    
	    int  download_id      = Integer.parseInt(request.getParameter(Params.ID));
	    
	    Download download = (Download)DAOManager.getInstance().getDownloadDAO().findByID(download_id);

	    download.setName(downloadShowForm.getName());
	    download.setDescription(downloadShowForm.getDescription());

	    DAOManager.getInstance().getDownloadDAO().update(download);
        ActionForward af = new ActionForward();

        af.setPath("/admin/ListDownloads.do");

        return af;

	}
		
	
}


