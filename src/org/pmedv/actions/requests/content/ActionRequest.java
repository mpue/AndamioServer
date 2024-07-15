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

import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.pmedv.actions.AdministrationAction;
import org.pmedv.cms.common.GlobalForwards;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.session.UserSession;

public abstract class ActionRequest {

	protected static final Log log = LogFactory.getLog(ActionRequest.class);
	
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected ActionMapping mapping;
	protected ActionForm form;
	protected String processName;
	protected org.pmedv.pojos.Process process;
	protected UserSession session;
	
	public ActionRequest(HttpServletRequest request,String processName,ActionMapping mapping, ActionForm form) {
		this.request     = request;
		this.processName = processName;
		this.mapping	 = mapping;
		this.form 		 = form;
	}
	
	public ActionRequest(HttpServletRequest request,HttpServletResponse response,String processName,ActionMapping mapping, ActionForm form) {
		this.request     = request;
		this.response	 = response;
		this.processName = processName;
		this.mapping	 = mapping;
		this.form 		 = form;
	}
	
	protected abstract ActionForward doAction();
	
	public ActionForward execute() {
		if (!isAllowedToProcess()) return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);
		return doAction();
	}
	
	/**
	 * <p>
	 * Checks if a user is allowed to request some specific action respective process.
	 * Returns true, if so and false if not. Permissions are set set in 
	 * </p>
	 * <p>
	 * {@link AdministrationAction}
	 * </p>
	 * @param request The actual servlet request of the action
	 * @param processName the process name
	 * @return
	 */

	protected boolean isAllowedToProcess() {

		try {
			
			process = (org.pmedv.pojos.Process)DAOManager.getInstance().getProcessDAO().findByName(processName);
			
			if (process.getActive() == null) return false;
			if (!process.getActive())        return false;

			session = new UserSession();

			session.init(false, request);
			session.getAttributes();
						
			ResourceBundle resources = ResourceBundle.getBundle("MessageResources");
			String permission = (String)session.getSession().getAttribute(resources.getString(process.getName()));
			
			if (permission.equalsIgnoreCase("true")) {
				log.debug("User is allowed for : "+process.getName());
				session.getSession().setAttribute("processicon",process.getIcon() );
				session.getSession().setAttribute("processname",resources.getString(process.getName()));
				return true;
			}
			else {
				return false;
			}
		} 
		catch (NullPointerException n) {
			return false;
		}

	}
	
    /**
     * <p>Save the specified error messages keys into the appropriate request
     * attribute for use by the &lt;html:errors&gt; tag, if any messages
     * are required. Otherwise, ensure that the request attribute is not
     * created.</p>
     *
     * @param request The servlet request we are processing
     * @param errors Error messages object
     * @since Struts 1.2
     */
    protected void saveErrors(HttpServletRequest request, ActionMessages errors) {

        // Remove any error messages attribute if none are required
        if ((errors == null) || errors.isEmpty()) {
            request.removeAttribute(Globals.ERROR_KEY);
            return;
        }

        // Save the error messages we need
        request.setAttribute(Globals.ERROR_KEY, errors);

    }
	
}
