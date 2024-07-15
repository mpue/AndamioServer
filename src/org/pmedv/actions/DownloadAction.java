package org.pmedv.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.DownloadCategory;

public class DownloadAction extends ObjectListAction {

	public ActionForward getCategories(
			ActionMapping mapping,
		    ActionForm form,
		    HttpServletRequest request,
		    HttpServletResponse response) throws Exception{
	
		List<?> categories = DAOManager.getInstance().getDownloadCategoryDAO().findAllItems();		
		writeJSONList(DownloadCategory.class, categories,false,"id", request, response);		
		return null;
		
	}
	

}
