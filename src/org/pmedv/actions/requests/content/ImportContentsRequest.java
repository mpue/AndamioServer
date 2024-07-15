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

import java.beans.XMLDecoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.pmedv.beans.objects.ContentBean;
import org.pmedv.beans.objects.DirectoryObject;
import org.pmedv.cms.common.GlobalForwards;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.file.DirectoryPrintVisitor;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.Content;
import org.pmedv.util.FileHelper;

public class ImportContentsRequest extends ActionRequest {

	public ImportContentsRequest(HttpServletRequest request, String processName, ActionMapping mapping, ActionForm form) {
		super(request, processName, mapping, form);
	}

	@Override
	protected ActionForward doAction() {
		
		log.debug("Starting import of contents.");
		
		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);
		
		String RootDirectory = config.getBasepath();
		
	    File file = new File(RootDirectory+config.getImportpath());
	
	    log.debug("Trying to import from "+RootDirectory+config.getImportpath());
	    ArrayList<DirectoryObject> importDirectoryContents;
	    
	    try {
	    	importDirectoryContents = FileHelper.traverse(file, new DirectoryPrintVisitor());	
	    }
	    catch (IllegalArgumentException i) {
	    	importDirectoryContents = new ArrayList<DirectoryObject>();
	    }
	    
	    int fileIdx = 1;
	    
	    for (Iterator<DirectoryObject> fileIterator = importDirectoryContents.iterator(); fileIterator.hasNext(); ) {
	    	
	    	DirectoryObject dirObj = (DirectoryObject) fileIterator.next();
	    	
	    	if (dirObj.getName().startsWith("content") && dirObj.getName().endsWith(".xml")) {
	    		
	    		String id = dirObj.getName().substring(dirObj.getName().lastIndexOf("_")+1,dirObj.getName().lastIndexOf("."));
	    		
	    		log.debug("Found content to import with id "+id +" : "+dirObj.getName());
	    		
			    try {
			    	FileInputStream fis = new FileInputStream(config.getBasepath()+config.getImportpath()+"content_"+id+".xml");
					XMLDecoder xmlDecoder = new XMLDecoder(fis);
					ContentBean contentBean = (ContentBean)xmlDecoder.readObject();
					xmlDecoder.close();
					fis.close();
					
					log.debug("Found content with id : "+contentBean.getId());
					log.debug("Name : " +contentBean.getContentname());
					
					Content content = new Content();
					content.setContentname(contentBean.getContentname());
					content.setContentstring(contentBean.getContentstring());
					content.setDescription(contentBean.getDescription());
					content.setPagename(contentBean.getPagename());
					content.setMetatags(contentBean.getMetatags());
					
					DAOManager.getInstance().getContentDAO().createAndStore(content);
			    }
			    catch (FileNotFoundException e) {
					e.printStackTrace();
				} 
			    catch (IOException e) {
					e.printStackTrace();
				}
			    fileIdx++;	    		
			}
	    }
		
		return mapping.findForward(GlobalForwards.SHOW_CONTENT_LIST);
	}

}
