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
package org.pmedv.actions.requests.content;

import java.beans.XMLEncoder;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.pmedv.beans.objects.ContentBean;
import org.pmedv.cms.common.GlobalForwards;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.Content;

public class ExportContentsRequest extends ActionRequest {

	public ExportContentsRequest(HttpServletRequest request, String processName, ActionMapping mapping, ActionForm form) {
		super(request, processName, mapping, form);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ActionForward doAction() {
		
		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);
		
		for (Iterator contentIterator = DAOManager.getInstance().getContentDAO().findAllItems().iterator();contentIterator.hasNext(); ) {

			Content currentContent = (Content) contentIterator.next();
			
			ContentBean contentBean = new ContentBean();
			
			contentBean.setContentname(currentContent.getContentname());
			contentBean.setContentstring(currentContent.getContentstring());
			contentBean.setDescription(currentContent.getDescription());
			contentBean.setPagename(currentContent.getPagename());
			contentBean.setMetatags(currentContent.getMetatags());
			
			contentBean.setId(currentContent.getId());
			
		    try {
				FileOutputStream fos = new FileOutputStream(config.getBasepath()+"export/"+"content_"+currentContent.getId()+".xml");
				XMLEncoder xmlEncoder = new XMLEncoder(fos);
				xmlEncoder.writeObject(contentBean);
				xmlEncoder.close();
				fos.close();
				
				log.debug("Wrote content to file "+config.getBasepath()+"export/"+"content_"+currentContent.getId()+".xml");
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		return mapping.findForward(GlobalForwards.SHOW_CONTENT_LIST);
	}

}
