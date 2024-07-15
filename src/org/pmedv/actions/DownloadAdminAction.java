package org.pmedv.actions;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonBeanProcessor;
import net.sf.json.processors.JsonBeanProcessorMatcher;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.pmedv.cms.common.Params;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.forms.DownloadShowForm;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.Download;
import org.pmedv.pojos.DownloadCategory;

public class DownloadAdminAction extends ObjectListAction {
	
	public DownloadAdminAction() {
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

			// eliminate non public files
			for (Iterator<Download> it = downloads.iterator();it.hasNext();) {
				Download d = it.next();
				if (!d.isPublicfile()) {
					it.remove();
				}
			}

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
	
	
	public ActionForward browse(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("showDownloadList");
	}
	
	public ActionForward getCategories(
			ActionMapping mapping,
		    ActionForm form,
		    HttpServletRequest request,
		    HttpServletResponse response) throws Exception{
	
		List<?> categories = DAOManager.getInstance().getDownloadCategoryDAO().findAllItems();		
		writeJSONList(DownloadCategory.class, categories,true,"id", request, response);		
		return null;
		
	}

	public ActionForward createCategory(
			ActionMapping mapping,
		    ActionForm form,
		    HttpServletRequest request,
		    HttpServletResponse response) throws Exception{
		
		DownloadCategory cat = new DownloadCategory();
		cat.setDescription(request.getParameter("description"));
		cat.setName(request.getParameter("name"));

		boolean error = DAOManager.getInstance().getDownloadCategoryDAO().createAndStore(cat) == null;
		
		PrintWriter out;
		
		try {
			out = response.getWriter();
			if (error) {
				out.print("{\"success\":false,\"message\":\"Could not create category.\"}");					
			}
			else {
				out.print("{\"success\":true,\"message\":\"Category has been created.\"}");
			}
			out.flush();
		} 
		catch (IOException e) {
			log.error("Could not write to servlet output stream.");
		}	
		
		return null;
		
	}
	
	public ActionForward deleteCategory(
			ActionMapping mapping,
		    ActionForm form,
		    HttpServletRequest request,
		    HttpServletResponse response) throws Exception{
		
		boolean error = false;
		
		Long categoryId = Long.valueOf(request.getParameter("id"));		
		DownloadCategory cat = (DownloadCategory) DAOManager.getInstance().getDownloadCategoryDAO().findByID(categoryId);		
		error = !DAOManager.getInstance().getDownloadCategoryDAO().delete(cat);
		
		PrintWriter out;
		
		try {
			out = response.getWriter();
			if (error) {
				out.print("{\"success\":false,\"message\":\"Could not delete category.\"}");					
			}
			else {
				out.print("{\"success\":true,\"message\":\"Category has been deleted.\"}");
			}
			out.flush();
		} 
		catch (IOException e) {
			log.error("Could not write to servlet output stream.");
		}		
		
		return null;
	}
	
    public ActionForward getDownloadAsJson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
    	
    	Long id = Long.valueOf(request.getParameter("download_id"));
    	
    	Download d = (Download)DAOManager.getInstance().getDownloadDAO().findByID(id);
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append("{\"success\":true,\"data\":");    	
    	sb.append(JSONSerializer.toJSON(d,getConfig(Download.class)).toString());
    	sb.append("}");
    	
		try {
			PrintWriter out = response.getWriter();
			out.println(sb.toString());
			out.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
    	
    	return null;
    }
    
    public ActionForward save(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
    
    	DownloadShowForm f = (DownloadShowForm)form;
    	
    	Download download = (Download) DAOManager.getInstance().getDownloadDAO().findByID(Integer.parseInt(request.getParameter(Params.ID)));
    	
    	download.setAuthor(f.getAuthor());
    	download.setDescription(f.getDescription());
    	download.setDomain(f.getDomain());
    	download.setDownloadable(f.isDownloadable());
    	download.setDownloads(f.getDownloads());
    	download.setFilename(f.getFilename());
    	download.setFilesize(f.getFilesize());
    	download.setHits(f.getHits());
    	download.setLicense(f.getLicense());
    	download.setMimetype(f.getMimetype());
    	download.setName(f.getName());
    	download.setPath(f.getPath());
    	download.setPublicfile(f.isPublicfile());
    	download.setRanking(f.getRanking());
    	download.setTags(f.getTags());
    	download.setUploader(f.getUploader());
    	
    	DAOManager.getInstance().getDownloadDAO().update(download);

    	// TODO : JSON error handling foe EXTJS
    	
    	return null;
    }
    
    public ActionForward deleteDownload(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
    	
    	int id = Integer.parseInt(request.getParameter(Params.ID));
    	
    	Download d = (Download)DAOManager.getInstance().getDownloadDAO().findByID(id);
    	
    	Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1L);
    	
    	String filePath = config.getBasepath()+d.getPath()+d.getFilename();
    	
    	File f = new File(filePath);
    	
    	// TODO : Error handling
    	
    	if (f.exists()) {
    		f.delete();
    	}
    	
    	if (d != null) {
    		DAOManager.getInstance().getDownloadDAO().delete(d);
    	}
    	
    	return null;
    }
	
    private JsonConfig getConfig(Class<?> dataObject) {
	    JsonConfig c = new JsonConfig();
	    
	    c.setJsonBeanProcessorMatcher(JsonBeanProcessorMatcher.DEFAULT);
	    c.registerJsonBeanProcessor(dataObject, new JsonBeanProcessor() {

			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				log.debug("processBean : "+bean.getClass());
				JSONObject j = new JSONObject();
				
		    	for (int i=0;i < bean.getClass().getMethods().length;i++) {

		    		if (!bean.getClass().getMethods()[i].getReturnType().equals(Class.class) &&		    			
		    		    !bean.getClass().getMethods()[i].getReturnType().equals(Set.class)) {
		    		
		    			if (bean.getClass().getMethods()[i].getName().startsWith("get") ||
		    			   (bean.getClass().getMethods()[i].getName().startsWith("is")	 &&
		    				bean.getClass().getMethods()[i].getReturnType().equals(boolean.class))) {
		    				
		    				Method method;
		    				Object fieldValue = null;
		    				
		    				try {
		    					method = bean.getClass().getMethod(bean.getClass().getMethods()[i].getName(), null);				
		    					fieldValue = method.invoke(bean, new Object[0]);							
		    				} 
		    				catch (Exception e) {
		    					e.printStackTrace();
		    				}
		    				String memberName = bean.getClass().getMethods()[i].getName();				    		
		    				
		    				if (bean.getClass().getMethods()[i].getName().startsWith("get")) {
		    					memberName = memberName.substring(3,4).toLowerCase() + memberName.substring(4,memberName.length());		    					
		    				}
		    				else {
		    					memberName = memberName.substring(2,3).toLowerCase() + memberName.substring(3,memberName.length());
		    				}
		    				
		    				if (fieldValue == null)
		    					fieldValue = new Object();
		    				j.element(memberName, fieldValue,jsonConfig);
		    				
		    			}
		    			
		    		}
		    		
		    	}
				
				return j;
				
			}
	    	
	    });
    	
	    return c;
    }
    
}
