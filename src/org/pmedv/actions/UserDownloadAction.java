package org.pmedv.actions;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.Download;
import org.pmedv.pojos.DownloadCategory;

public class UserDownloadAction extends ObjectListAction {
	
	public UserDownloadAction() {
		super("menu.downloads");		
	}

	public ActionForward list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {		
		writeJSONList(Download.class, request, response);		
		return null;		
	}

	@SuppressWarnings("unchecked")
	public ActionForward getLatestDownloads(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		if (null != request.getParameter("limit")) {
			
			int limit = Integer.parseInt(request.getParameter("limit"));			
			
			List<Download> downloads = (List<Download>) DAOManager.getInstance().getDownloadDAO().findLatestDownloads(limit);
			writeJSONList(Download.class,downloads,false,"id",request,response);	
			
		}
		
		return null;		
	}

	public ActionForward getDownloads(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		if (null != request.getParameter("limit") && null != request.getParameter("start")) {
			int limit = Integer.parseInt(request.getParameter("limit"));
			int start = Integer.parseInt(request.getParameter("start"));			
			
			if (null != request.getParameter("latest" )) {
				
			}
			else {				
				@SuppressWarnings("unchecked")				
				List<Download> downloads = (List<Download>) DAOManager.getInstance().getDownloadDAO().findAllItems(Download.class, limit, start);

				// eliminate non public files
				for (Iterator<Download> it = downloads.iterator();it.hasNext();) {
					Download d = it.next();
					if (!d.isPublicfile()) {
						it.remove();
					}
				}
				
				Collections.sort(downloads);
				writeJSONList(Download.class,downloads,false,"id",request,response);	
			}
			
		}
		else {
			writeJSONList(Download.class,false,request, response);					
		}
		
		return null;		
	}
	
	@SuppressWarnings("unchecked")
	public ActionForward searchDownloads(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String author = request.getParameter("author");
		String name = request.getParameter("name");
		
		List<Download> downloads = (List<Download>) DAOManager.getInstance().getDownloadDAO().searchDownloads(name, author);
		writeJSONList(Download.class,downloads,false,"id",request,response);	
		
		return null;		
	}	
	
	public ActionForward getCategories(
			ActionMapping mapping,
		    ActionForm form,
		    HttpServletRequest request,
		    HttpServletResponse response) throws Exception{
	
		List<?> categories = DAOManager.getInstance().getDownloadCategoryDAO().findAllItems();		
		writeJSONList(DownloadCategory.class, categories,false,"id", request, response);		
		return null;
		
	}
	
	public ActionForward rateSound(
			ActionMapping mapping,
		    ActionForm form,
		    HttpServletRequest request,
		    HttpServletResponse response) throws Exception{

		// TODO:
		// this rating function is for testing purposes only
		// in fact what we need is a real average of all ratings
		// of the file. For that we need to store all ratings for each file.
		
		long id = Long.valueOf(request.getParameter("id"));
		float value = Float.valueOf(request.getParameter("value")); 
		
		Download d = (Download) DAOManager.getInstance().getDownloadDAO().findByID(id);		
		float newRanking = (d.getRanking() + value) / 2;		
		if (newRanking > 5) {
			newRanking = 5.0f;
		}
		d.setRanking(Math.round(newRanking));
		DAOManager.getInstance().getDownloadDAO().update(d);
		
		return null;
		
	}	

    
}
