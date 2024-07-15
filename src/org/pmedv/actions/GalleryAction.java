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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.pmedv.beans.objects.GalleryBean;
import org.pmedv.context.AppContext;
import org.pmedv.services.GalleryService;
import org.springframework.context.ApplicationContext;
 

public class GalleryAction extends ObjectListAction {

	public GalleryAction() {
		super("menu.gallerymanager");
		log.debug("Executing "+this.getClass());		
	}

	public ActionForward list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		// if (!isAllowedToProcess(request, getName())) return null;
		
	    ApplicationContext ctx = AppContext.getApplicationContext();    
	    GalleryService service = (GalleryService) ctx.getBean("galleryService");
	    List <GalleryBean> galleries = service.findAll();
		
		writeJSONList(GalleryBean.class,galleries,true,"id", request,response);
	
		return null;
	}


}
