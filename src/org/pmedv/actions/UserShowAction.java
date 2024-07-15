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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.pmedv.cms.common.GlobalForwards;
import org.pmedv.cms.common.Params;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.core.crypto.MD5Crypter;
import org.pmedv.forms.UserShowForm;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.User;
import org.pmedv.pojos.Usergroup;


public class UserShowAction extends ObjectListAction {

    private static final Log log = LogFactory.getLog(UserShowAction.class);

    public UserShowAction() {
        super("menu.user");
        log.debug("Executing " + this.getClass());
    }

    /**
     * loads user from the db and saves him in the request
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward edit(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!isAllowedToProcess(request, getName())) {
            return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);
        }

        Long id = null;

        UserShowForm userForm = (UserShowForm) form;

        try {
            id = Long.parseLong(request.getParameter(Params.ID_USER));
            request.getSession().setAttribute(Params.ID_USER, id);
        } catch (NumberFormatException n) {
            id = (Long) request.getSession().getAttribute(Params.ID_USER);
        }

        User user = (User) DAOManager.getInstance().getUserDAO().findByID(id);
        user.setPassword("");
        userForm.setUser(user);

        request.getSession().setAttribute("user_id", id);

        if (request.getParameter("async") != null) {
            return null;
        }


        return mapping.findForward(GlobalForwards.SHOW_USER_DETAILS);
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
    public ActionForward getUserAsJson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!isAllowedToProcess(request, getName())) {
            return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);
        }

        User user = null;

        StringBuffer jsonString = new StringBuffer();

        try {
            int id = Integer.parseInt(request.getParameter(Params.ID_USER));

            log.debug("fetching user with id : " + id);

            user = (User) DAOManager.getInstance().getUserDAO().findByID(id);
        } catch (NumberFormatException e) {
            log.debug("User could not be found : " + request.getParameter(Params.ID_USER));
        }

        if (user != null) {

            response.setContentType("text/plain");

            log.debug("Found user with name : " + user.getName());


            jsonString.append("{\"success\":true,\"data\":{");
            jsonString.append("\"id\":\"" + user.getId() + "\",");
            jsonString.append("\"name\":\"" + user.getName() + "\",");
            jsonString.append("\"firstName\":\"" + user.getFirstName() + "\",");
            jsonString.append("\"lastName\":\"" + user.getLastName() + "\",");
            jsonString.append("\"title\":\"" + user.getTitle() + "\",");
            jsonString.append("\"land\":\"" + user.getLand() + "\",");
            jsonString.append("\"ort\":\"" + user.getOrt() + "\",");
            jsonString.append("\"telefon\":\"" + user.getTelefon() + "\",");
            jsonString.append("\"email\":\"" + user.getEmail() + "\",");
            jsonString.append("\"ranking\":" + user.getRanking() + ",");
            jsonString.append("\"password\":\"" + "" + "\",");
            jsonString.append("\"active\":" + user.getActive() + "");
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

    /**
     * deletes a user
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward delete(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!isAllowedToProcess(request, getName())) {
            return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);
        }

        int id = Integer.parseInt(request.getParameter(Params.ID_USER));

        User user = (User) DAOManager.getInstance().getUserDAO().findByID(id);

        // If user is admin, we cannot delete him.

        if (!user.getName().equalsIgnoreCase("admin")) {
            DAOManager.getInstance().getUserDAO().delete(user);
            
			Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);			
			String basepath = config.getBasepath();									
			
			String userDir = basepath+"users/"+user.getName();

			File f = new File(userDir);
			
			try {
				FileUtils.deleteDirectory(f);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
            
        }

        return null;

    }

    @SuppressWarnings("rawtypes")
	public ActionForward JSONUpdate(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        log.debug("JSONUpdate");
        log.debug("received JSON : " + request.getParameter("data"));

        String data = (String) request.getParameter("data");

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setRootClass(User.class);
        JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(data);
        List output = (List) JSONSerializer.toJava(jsonArray, jsonConfig);

        for (Iterator it = output.iterator(); it.hasNext();) {
            User currentUser = (User) it.next();

            if (currentUser.isNewRecord()) {
                DAOManager.getInstance().getUserDAO().createAndStore(currentUser);
            } else {
                User oldUser = (User) DAOManager.getInstance().getUserDAO().findByID(currentUser.getId());

                // Prevent groups from beeing dicarded, since we did not read them

                currentUser.setGroups(oldUser.getGroups());
                DAOManager.getInstance().getUserDAO().update(currentUser);
            }
        }

        return null;
    }

    /**
     * saves modified user to database
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward save(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!isAllowedToProcess(request, getName())) {
            return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);
        }

        UserShowForm userForm = (UserShowForm) form;

        User user = (User) DAOManager.getInstance().getUserDAO().findByID(Integer.parseInt(request.getParameter(Params.ID)));

        user.setActive(userForm.getActive());
        user.setFirstName(userForm.getFirstName());
        user.setLastName(userForm.getLastName());
        user.setEmail(userForm.getEmail());
        user.setTitle(userForm.getTitle());
        user.setLand(userForm.getLand());
        user.setName(userForm.getName());
        user.setOrt(userForm.getOrt());

        if (!userForm.getPassword().equals("")) {
            byte[] pass = MD5Crypter.createMD5key(userForm.getPassword());
            String password = MD5Crypter.createMD5String(pass);
            user.setPassword(password);
        }

        user.setRanking(userForm.getRanking());
        user.setTelefon(userForm.getTelefon());

        DAOManager.getInstance().getUserDAO().update(user);

        if (request.getParameter("async") != null) {
            return null;
        }

        return mapping.findForward(GlobalForwards.SHOW_USER_LIST);
    }

    public ActionForward addGroup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!isAllowedToProcess(request, getName())) {
            return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);
        }

        HttpSession session = request.getSession();

        UserShowForm userForm = (UserShowForm) form;

        Long group_id = Long.parseLong(request.getParameter(Params.ID_USERGROUP));
        Long user_id  = Long.parseLong(request.getParameter(Params.ID_USER));

        DAOManager.getInstance().getUserDAO().addGroup(group_id, user_id);

        User user = (User) DAOManager.getInstance().getUserDAO().findByID(user_id);
        userForm.setUser(user);

        if (request.getParameter("async") != null) {
            return null;
        }

        return mapping.findForward(GlobalForwards.SHOW_USER_DETAILS);
    }

    /**
     * Removes the group membership from the user for a specific group
     *
     * @param mapping  the servlet mapping to process
     * @param form     the struts action form
     * @param request  the servlet request to process
     * @param response the servlet responde to process
     *
     * @return the defined struts action forward
     */
    public ActionForward removeGroup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!isAllowedToProcess(request, getName())) {
            return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);
        }

        UserShowForm userForm = (UserShowForm) form;

        HttpSession session = request.getSession();
        Long user_id  = Long.parseLong(request.getParameter(Params.ID_USER));
        Long group_id = Long.parseLong(request.getParameter(Params.ID_USERGROUP));

        DAOManager.getInstance().getUserDAO().removeGroup(group_id, user_id);

        User user = (User) DAOManager.getInstance().getUserDAO().findByID(user_id);
        userForm.setUser(user);

        if (request.getParameter("async") != null) {
            return null;
        }

        return mapping.findForward(GlobalForwards.SHOW_USER_DETAILS);
    }

    /**
     * Removes a specific avatar from the user
     *
     * @param mapping  the servlet mapping to process
     * @param form     the struts action form
     * @param request  the servlet request to process
     * @param response the servlet responde to process
     *
     * @return the defined struts action forward
     */
    public ActionForward removeAvatar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!isAllowedToProcess(request, getName())) {
            return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);
        }

        UserShowForm userForm = (UserShowForm) form;

        HttpSession session = request.getSession();
        Long user_id = (Long) session.getAttribute(Params.ID_USER);
        Long avatar_id = Long.parseLong(request.getParameter(Params.ID_AVATAR));

        DAOManager.getInstance().getUserDAO().removeAvatar(user_id, avatar_id);

        User user = (User) DAOManager.getInstance().getUserDAO().findByID(user_id);
        userForm.setUser(user);

        return mapping.findForward(GlobalForwards.SHOW_USER_DETAILS);
    }

    public ActionForward getGroupsAsJSON(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        User user = null;

        Long id = Long.parseLong(request.getParameter(Params.ID_USER));
        user = (User) DAOManager.getInstance().getUserDAO().findByID(id);

        if (user != null) {

            Set<Usergroup> groups = user.getGroups();
            writeJSONList(Usergroup.class, groups, true, "id", request, response);

        }

        return null;
    }
}
