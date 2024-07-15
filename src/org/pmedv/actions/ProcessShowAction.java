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
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.pmedv.cms.common.GlobalForwards;
import org.pmedv.cms.common.Params;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.forms.ProcessShowForm;
import org.pmedv.pojos.Process;
import org.pmedv.pojos.Usergroup;
import org.pmedv.session.UserSession;

@SuppressWarnings("unchecked")

public class ProcessShowAction extends ObjectListAction {

    public ProcessShowAction() {
        super("menu.processes");
        log.debug("Executing " + this.getClass());
    }

    /**
     * loads Process from the db and saves them in the request
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

        ProcessShowForm processForm = (ProcessShowForm) form;

        UserSession session = new UserSession();
        session.init(false, request);
        session.getAttributes();

        try {
            if (session.getLogin().equals("admin")) {

                Long id = Long.parseLong(request.getParameter(Params.ID_PROCESS));

                Process process = (Process) DAOManager.getInstance().getProcessDAO().findByID(id);
                processForm.setProcess(process);
                request.getSession().setAttribute(Params.ID_PROCESS, id);

            }
        } catch (NullPointerException n) {
            return mapping.findForward(GlobalForwards.SHOW_LOGIN_DIALOG);
        }

        return mapping.findForward(GlobalForwards.SHOW_PROCESS_DETAILS);
    }

    /**
     * deletes a Process
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

        int id = Integer.parseInt(request.getParameter(Params.ID_PROCESS));

        // get out, if process is the processmanager himself

        Process process = (Process) DAOManager.getInstance().getProcessDAO().findByID(id);

        if (id > 1) {
            DAOManager.getInstance().getProcessDAO().delete(process);
        }

        if (request.getParameter("async") != null) {
            return null;
        }

        return mapping.findForward(GlobalForwards.SHOW_PROCESS_LIST);

    }

    /**
     * saves modified Process to database
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

        ProcessShowForm processForm = (ProcessShowForm) form;

        Process process = (Process) DAOManager.getInstance().getProcessDAO().findByID(Integer.parseInt(request.getParameter(Params.ID)));

        process.setIcon(processForm.getIcon());
        process.setName(processForm.getName());
        process.setTarget(processForm.getTarget());

        DAOManager.getInstance().getProcessDAO().update(process);

        return mapping.findForward(GlobalForwards.SHOW_PROCESS_LIST);
    }

    public ActionForward addGroup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!isAllowedToProcess(request, getName())) {
            return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);
        }

        ProcessShowForm processForm = (ProcessShowForm) form;

        Long group_id   = Long.parseLong(request.getParameter(Params.ID_USERGROUP));
        Long process_id = Long.parseLong(request.getParameter(Params.ID_PROCESS));

        log.debug("Adding usergroup with id "+group_id+ " to process with id "+process_id);

        DAOManager.getInstance().getProcessDAO().addGroup(group_id, process_id);

        Process process = (Process) DAOManager.getInstance().getProcessDAO().findByID(process_id);
        processForm.setProcess(process);

        if (request.getParameter("async") != null) {
            return null;
        }

        return mapping.findForward(GlobalForwards.SHOW_PROCESS_DETAILS);
    }

    public ActionForward removeGroup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!isAllowedToProcess(request, getName())) {
            return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);
        }

        ProcessShowForm processShowForm = (ProcessShowForm) form;

        Long group_id = Long.parseLong(request.getParameter(Params.ID_USERGROUP));
        Long process_id = Long.parseLong(request.getParameter(Params.ID_PROCESS));

        log.debug("Removing usergroup with id "+group_id+ " from process with id "+process_id);

        if (process_id > 1) {
            DAOManager.getInstance().getProcessDAO().removeGroup(group_id, process_id);
        }

        Process process = (Process) DAOManager.getInstance().getProcessDAO().findByID(process_id);
        processShowForm.setProcess(process);

        if (request.getParameter("async") != null) {
            return null;
        }

        return mapping.findForward(GlobalForwards.SHOW_PROCESS_DETAILS);
    }

    /**
     * Invert active status of this process object and persist
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward switchActive(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!isAllowedToProcess(request, getName())) {
            return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);
        }

        int id = Integer.parseInt(request.getParameter(Params.ID_PROCESS));

        if (id > 1) {

            Process process = (Process) DAOManager.getInstance().getProcessDAO().findByID(id);

            if (process.getActive() == null) {
                process.setActive(true);
            } else {
                if (process.getActive()) {
                    process.setActive(false);
                } else {
                    process.setActive(true);
                }
            }

            DAOManager.getInstance().getProcessDAO().update(process);
        }

        if (request.getParameter("async") != null) {
            return null;
        }

        return mapping.findForward(GlobalForwards.SHOW_PROCESS_LIST);
    }

    public ActionForward elementUp(
            ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        int process_id = Integer.parseInt(request.getParameter(Params.ID_PROCESS));

        org.pmedv.pojos.Process process = (org.pmedv.pojos.Process) DAOManager.getInstance().getProcessDAO().findByID(process_id);

        if (process.getPosition() > 1) {
            Process previousProcess = (Process) DAOManager.getInstance().getProcessDAO().getPreviousProcess(process.getPosition());

            int temp = previousProcess.getPosition();
            previousProcess.setPosition(process.getPosition());
            process.setPosition(temp);

            DAOManager.getInstance().getProcessDAO().update(previousProcess);
            DAOManager.getInstance().getProcessDAO().update(process);

        }

        if (request.getParameter("async") != null) {
            return null;
        }

        return mapping.findForward(GlobalForwards.SHOW_PROCESS_LIST);
    }

    public ActionForward elementDown(
            ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        int process_id = Integer.parseInt(request.getParameter(Params.ID_PROCESS));

        org.pmedv.pojos.Process process = (org.pmedv.pojos.Process) DAOManager.getInstance().getProcessDAO().findByID(process_id);

        if (process.getPosition() < DAOManager.getInstance().getProcessDAO().getMaxPos()) {
            Process nextProcess = (Process) DAOManager.getInstance().getProcessDAO().getNextProcess(process.getPosition());

            int temp = nextProcess.getPosition();
            nextProcess.setPosition(process.getPosition());
            process.setPosition(temp);

            DAOManager.getInstance().getProcessDAO().update(nextProcess);
            DAOManager.getInstance().getProcessDAO().update(process);

        }

        if (request.getParameter("async") != null) {
            return null;
        }


        return mapping.findForward(GlobalForwards.SHOW_PROCESS_LIST);
    }

	public ActionForward JSONUpdate(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        log.debug("JSONUpdate");
        log.debug("received JSON : " + request.getParameter("data"));

        String data = (String) request.getParameter("data");

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setRootClass(org.pmedv.pojos.Process.class);
        JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(data);
        List output = (List) JSONSerializer.toJava(jsonArray, jsonConfig);

        for (Iterator it = output.iterator(); it.hasNext();) {
            org.pmedv.pojos.Process currentProcess = (org.pmedv.pojos.Process) it.next();

            if (currentProcess.isNewRecord()) {
                DAOManager.getInstance().getUserDAO().createAndStore(currentProcess);
            } else {
                org.pmedv.pojos.Process oldProcess = (org.pmedv.pojos.Process) DAOManager.getInstance().getProcessDAO().findByID(currentProcess.getId());

                // Prevent groups from beeing dicarded, since we did not read them

                currentProcess.setGroups(oldProcess.getGroups());
                DAOManager.getInstance().getProcessDAO().update(currentProcess);
            }
        }

        return null;
    }

    public ActionForward getJSON(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        // if (!isAllowedToProcess(request, getName())) return null;

        writeJSONList(org.pmedv.pojos.Process.class, false, request, response);

        return null;
    }

    public ActionForward getGroupsAsJSON(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        org.pmedv.pojos.Process process = null;

        Long id = Long.parseLong(request.getParameter(Params.ID_PROCESS));
        process = (org.pmedv.pojos.Process) DAOManager.getInstance().getProcessDAO().findByID(id);

        if (process != null) {

            Set<Usergroup> groups = process.getGroups();
            writeJSONList(Usergroup.class, groups, true, "id", request, response);

        }

        return null;
    }
}
