/**
 * WeberknechtCMS - Open Source Content Management Written and maintained by Matthias Pueski
 * 
 * Copyright (c) 2003-2011 Matthias Pueski
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 */
package org.pmedv.actions.forums;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.pmedv.actions.ObjectListAction;
import org.pmedv.cms.common.Params;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.cms.daos.ForumDAO;
import org.pmedv.forms.forums.ForumShowForm;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.Usergroup;
import org.pmedv.pojos.forums.Attachment;
import org.pmedv.pojos.forums.Category;
import org.pmedv.pojos.forums.Forum;
import org.pmedv.pojos.forums.Posting;

/**
 * @author mpue
 * 
 */
public class ForumShowAction extends ObjectListAction {

	public ForumShowAction() {
		super("menu.forums");
		log.debug("Executing " + this.getClass());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.pmedv.actions.IPermissiveDispatchAction#delete(org.apache.struts.action.ActionMapping,
	 * org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		if (!isAllowedToProcess(request, getName())) return null;

		ForumDAO forumDAO = DAOManager.getInstance().getForumDAO();

		long forum_id = Long.parseLong(request.getParameter(Params.ID_FORUM));

		forumDAO.delete(forumDAO.findByID(forum_id));

		return null;
	}

	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		if (!isAllowedToProcess(request, getName())) return null;
		return mapping.findForward("showForumsList");
	}

	public ActionForward getJSON(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		if (!isAllowedToProcess(request, getName())) return null;

		log.debug("creating JSON list of " + this.getClass());

		writeJSONList(Forum.class, true, request, response);

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.pmedv.actions.IPermissiveDispatchAction#edit(org.apache.struts.action.ActionMapping,
	 * org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		if (!isAllowedToProcess(request, getName())) return null;
		
		ForumShowForm forumShowForm = (ForumShowForm) form;

		long forum_id = Long.parseLong(request.getParameter(Params.ID_FORUM));

		Forum forum = (Forum) DAOManager.getInstance().getForumDAO().findByID(forum_id);
		forumShowForm.setForum(forum);

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.pmedv.actions.IPermissiveDispatchAction#save(org.apache.struts.action.ActionMapping,
	 * org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		if (!isAllowedToProcess(request, getName())) return null;
		
		ForumShowForm forumShowForm = (ForumShowForm) form;
		ForumDAO forumDAO = DAOManager.getInstance().getForumDAO();

		long forum_id = Long.parseLong(request.getParameter(Params.ID));

		Forum forum = (Forum) forumDAO.findByID(forum_id);

		forum.setDescription(forumShowForm.getDescription());
		forum.setName(forumShowForm.getName());
		forum.setStatus(forumShowForm.getStatus());
		forum.setCategories(forumShowForm.getCategories());
		forum.setPosition(forumShowForm.getPosition());

		forumDAO.update(forum);

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.pmedv.actions.IPermissiveDispatchAction#save(org.apache.struts.action.ActionMapping,
	 * org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		if (!isAllowedToProcess(request, getName())) return null;

		ForumShowForm forumShowForm = (ForumShowForm) form;
		Forum forum = new Forum();

		forum.setDescription(forumShowForm.getDescription());
		forum.setName(forumShowForm.getName());
		forum.setStatus(forumShowForm.getStatus());
		forum.setCategories(forumShowForm.getCategories());
		forum.setPosition(forumShowForm.getPosition());

		DAOManager.getInstance().getForumDAO().create(forum);

		return null;
	}

	/**
	 * Returns the content metadata from this node as xml
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward getForumAsJson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		if (!isAllowedToProcess(request, getName())) return null;

		Forum forum = null;

		StringBuffer jsonString = new StringBuffer();

		try {
			long id = Long.parseLong(request.getParameter(Params.ID_FORUM));

			log.debug("fetching forum with id : " + id);

			forum = (Forum) DAOManager.getInstance().getForumDAO().findByID(id);
		}
		catch (NumberFormatException e) {
			log.debug("Forum could not be found : " + request.getParameter(Params.ID_FORUM));
		}

		if (forum != null) {

			response.setContentType("text/plain");

			log.debug("Found user with name : " + forum.getName());

			jsonString.append("{\"success\":true,\"data\":{");
			jsonString.append("\"id\":\"" + forum.getId() + "\",");
			jsonString.append("\"name\":\"" + forum.getName() + "\",");
			jsonString.append("\"description\":\"" + forum.getDescription() + "\",");
			jsonString.append("\"status\":\"" + forum.getStatus() + "\",");
			jsonString.append("\"position\":\"" + forum.getPosition() + "\",");
			jsonString.append("\"categories\":\"" + forum.getCategories() + "\"");
			jsonString.append("}}");

			try {
				PrintWriter out = response.getWriter();
				out.println(jsonString.toString());
				out.flush();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	public ActionForward JSONUpdate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		if (!isAllowedToProcess(request, getName())) return null;
		
		String data = (String) request.getParameter("data");

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setRootClass(Forum.class);
		JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(data);
		List<?> output = (List<?>) JSONSerializer.toJava(jsonArray, jsonConfig);

		for (Iterator<?> it = output.iterator(); it.hasNext();) {
			DAOManager.getInstance().getForumDAO().update(it.next());
		}

		return null;
	}

	/**
	 * Creates a JSON List of all categories of a forum
	 */
	public ActionForward getCategories(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		if (!isAllowedToProcess(request, getName())) return null;
		
		Forum forum = null;

		StringBuffer jsonString = new StringBuffer();

		try {
			long id = Long.parseLong(request.getParameter(Params.ID_FORUM));
			log.debug("fetching forum with id : " + id);
			forum = (Forum) DAOManager.getInstance().getForumDAO().findByID(id);
		}
		catch (NumberFormatException e) {
			log.debug("Forum could not be found : " + request.getParameter(Params.ID_FORUM));
		}

		if (forum != null) {
			response.setContentType("text/plain");			
			writeJSONList(Category.class, forum.getCategories(), true, "id",request,response);
			
		}

		return null;
	}

	public ActionForward removeCategory(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		if (!isAllowedToProcess(request, getName())) return null;
		
		boolean error = false;
	
		Long forumId = Long.valueOf(request.getParameter("forum_id"));
		Long categoryId = Long.valueOf(request.getParameter("category_id"));
		
		error = !DAOManager.getInstance().getForumDAO().removeCategory(forumId,categoryId);
		
		PrintWriter out;
		
		try {
			out = response.getWriter();
			if (error) {
				out.print("{\"success\":false,\"message\":\"Could not remove category.\"}");					
			}
			else {
				out.print("{\"success\":true,\"message\":\"Category has been removed.\"}");
			}
			out.flush();
		} 
		catch (IOException e) {
			log.error("Could not write to servlet output stream.");
		}		
		
		return null;
	}
	
	public ActionForward createCategory(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
	
		if (!isAllowedToProcess(request, getName())) return null;
		
		ForumShowForm forumShowForm = (ForumShowForm) form;
		Long forumId = Long.valueOf(request.getParameter("forum_id"));
		
		Category cat = new Category();
		cat.setDescription(forumShowForm.getDescription());
		cat.setName(forumShowForm.getName());
		cat.setPosition(forumShowForm.getPosition());

		boolean error = !DAOManager.getInstance().getForumDAO().addCategory(forumId, cat);
		
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
	

    public ActionForward getGroupsAsJSON(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
    	
    	if (!isAllowedToProcess(request, getName())) return null;
    	
        Forum forum = null;

        Long id = Long.parseLong(request.getParameter("forum_id"));
        forum = (Forum) DAOManager.getInstance().getForumDAO().findByID(id);

        if (forum != null) {

            Set<Usergroup> groups = forum.getGroups();
            writeJSONList(Usergroup.class, groups, true, "id", request, response);

        }

        return null;
    }	
    
    /**
     * Adds a group membership from the forum for a specific group
     *
     */    
    public ActionForward addGroup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

    	if (!isAllowedToProcess(request, getName())) return null;

        HttpSession session = request.getSession();

        Long group_id = Long.parseLong(request.getParameter(Params.ID_USERGROUP));
        Long forum_id  = Long.parseLong(request.getParameter(Params.ID_FORUM));

        DAOManager.getInstance().getForumDAO().addGroup(group_id, forum_id);

        return null;

    }

    /**
     * Removes the group membership from the forum for a specific group
     *
     */
    public ActionForward removeGroup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

    	if (!isAllowedToProcess(request, getName())) return null;
    	
        HttpSession session = request.getSession();
        Long forum_id  = Long.parseLong(request.getParameter(Params.ID_FORUM));
        Long group_id = Long.parseLong(request.getParameter(Params.ID_USERGROUP));

        DAOManager.getInstance().getForumDAO().removeGroup(group_id, forum_id);

        return null;
        
    }    
    
    public ActionForward deleteThread(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

    	if (!isAllowedToProcess(request, getName())) return null;
    	
    	Long thread_id = Long.valueOf(request.getParameter("thread_id"));
    	Long category_id = null;
    	Category category = null;

		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1L);
		String directory = "attachments/";
    	
    	if (thread_id != null) {
    		
    		org.pmedv.pojos.forums.Thread t = (org.pmedv.pojos.forums.Thread)DAOManager.getInstance().getThreadDAO().findByID(thread_id);
    		
    		if (t != null) {

    			category_id = t.getCategory().getId();
    			category = (Category)DAOManager.getInstance().getCategoryDAO().findByID(category_id);
    			
    			for (Posting p : t.getPostings()) {
    				
    				for (Attachment a : p.getAttachments()) {
    					File f = new File(config.getBasepath() + directory + a.getFilename());
    					if (f.exists() && f != null) {
    						if (!f.delete()) {
    							log.error("could not delete "+f.getAbsolutePath());
    						}
    						else {
    							log.info("deleted "+f.getAbsolutePath());
    						}
    					}
    				}
    			}
    			
    			DAOManager.getInstance().getThreadDAO().delete(t);
    			
    		}
    	}

    	if (category != null) {
    		try {
				response.sendRedirect("/"+config.getLocalPath()+"forum/Mainpage.do?do=showCategory&category_id="+category_id);
			}
			catch (IOException e) {
				e.printStackTrace();
			}    		
    	}
    	
    	return null;
    }
}
