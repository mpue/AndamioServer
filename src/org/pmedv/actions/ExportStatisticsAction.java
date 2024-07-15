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

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.pmedv.cms.common.GlobalForwards;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.file.FileManager;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.SiteRequest;

/**
 * This action creates a csv (comma separated file) from all requests stored inside the statistics table
 * 
 * @author Matthias Pueski
 * 
 * @version 1.4 from 2007-05-03
 *
 */
public class ExportStatisticsAction extends AbstractPermissiveAction {

	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
    	StringBuffer buffer = new StringBuffer();  	

    	Config config = (Config) DAOManager.getInstance().getConfigDAO().findByID(1);
    	
    	for (Iterator requestIterator = DAOManager.getInstance().getSiteRequestDAO().findAllItems().iterator();requestIterator.hasNext();) {
    		SiteRequest currentRequest = (SiteRequest) requestIterator.next();
    		
    		buffer.append(currentRequest.getId()+","+
    					  currentRequest.getUsername()+","+
    					  currentRequest.getContentname()+","+
    					  currentRequest.getReqtime()+","+
    					  currentRequest.getUserip()+","+
    					  currentRequest.getUseragent()+"\n");    		
    	}
		
    	FileManager fileManager = new FileManager();
    	
    	fileManager.setFilename("statistics.csv");
    	fileManager.setFilepath(config.getBasepath());
    	fileManager.setFileContent(buffer.toString());
    	fileManager.writeFile();
    	    	
		return mapping.findForward(GlobalForwards.SHOW_STATISTICS);
	}


	
	

}
