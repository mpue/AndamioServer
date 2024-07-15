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

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.core.crypto.MD5Crypter;
import org.pmedv.forms.RegistrationForm;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.User;
import org.pmedv.pojos.UserProfile;
import org.pmedv.pojos.Usergroup;

/**
 * This action is responsible for the user registration process
 * 
 * @author Matthias Pueski
 *
 */
public class RegistrationAction extends DispatchAction {
	
	public ActionForward prepare (
			ActionMapping mapping,
		    ActionForm form,
		    HttpServletRequest request,
		    HttpServletResponse response) throws Exception{
		
		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);
		
		ResourceBundle resources = ResourceBundle.getBundle("MessageResources",Locale.getDefault());
		RegistrationForm registrationForm = (RegistrationForm) form; 
		
		registrationForm.setSessionID(request.getSession().getId());
		registrationForm.setMessage(resources.getString("registration.welcome"));
		registrationForm.setSuccess(false);
		
		ActionForward af = new ActionForward();
        af.setPath("/templates/"+config.getTemplate()+"/registration.jsp");
        return af;	
		
	}

	public ActionForward submit (
			ActionMapping mapping,
		    ActionForm form,
		    HttpServletRequest request,
		    HttpServletResponse response) throws Exception{
		
		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);
		
		ActionForward af = new ActionForward();
        af.setPath("/templates/"+config.getTemplate()+"/registration.jsp");
		
		ResourceBundle resources = ResourceBundle.getBundle("MessageResources",Locale.getDefault());
		RegistrationForm registrationForm = (RegistrationForm) form; 
		
		User user = (User) DAOManager.getInstance().getUserDAO().findByName(registrationForm.getUsername());
		
		if (user != null) {
			registrationForm.setMessage(resources.getString("registration.userexists"));
			registrationForm.setSuccess(false);		
			return af;
		}
		else {
			user = (User) DAOManager.getInstance().getUserDAO().findByMail(registrationForm.getEMail());
			
			if (user != null) {
				registrationForm.setMessage(resources.getString("registration.emailexists"));
				registrationForm.setSuccess(false);		
				return af;			
			}
		}
		
		if (registrationForm.getEMail().length()           < 1 ||	
			registrationForm.getFirstName().length() 	   < 1 || 
			registrationForm.getLastName().length()        < 1 ||
			registrationForm.getUsername().length()        < 1 ||
			registrationForm.getPassword().length()        < 1 ||
			registrationForm.getPasswordConfirm().length() < 1) {
			
			registrationForm.setMessage(resources.getString("registration.incomplete"));
			registrationForm.setSuccess(false);
			
			return af;
			
		}
		else {
			
			String captcha = (String)request.getSession().getAttribute(nl.captcha.servlet.Constants.SIMPLE_CAPCHA_SESSION_KEY) ;
			
			if (!captcha.equals(registrationForm.getCaptcha())) {
				 registrationForm.setMessage(resources.getString("registration.captchasdontmatch"));
				 registrationForm.setSuccess(false);
				 return af;
				
			}
			if (!registrationForm.getPassword().equals(registrationForm.getPasswordConfirm())) {
				 registrationForm.setMessage(resources.getString("registration.passwordsdontmatch"));
				 registrationForm.setSuccess(false);
				 return af;
			}
			else {

				user = new User();

				byte[] pass   = MD5Crypter.createMD5key(registrationForm.getPassword());
				String password = MD5Crypter.createMD5String(pass);	

				user.setActive(true);
				user.setEmail(registrationForm.getEMail());
				user.setName(registrationForm.getUsername());
				user.setPassword(password);
				user.setTitle("User");
				
				UserProfile profile = new UserProfile();
				profile.setPageContent(resources.getString("user.emptyprofile"));
				
				DAOManager.getInstance().getUserDAO().createAndStore(user, profile);				

				user = (User)DAOManager.getInstance().getUserDAO().findByName(registrationForm.getUsername());
				
				Usergroup usergroup = (Usergroup)DAOManager.getInstance().getUsergroupDAO().findByName("user");
				
				DAOManager.getInstance().getUserDAO().addGroup(usergroup.getId(), user.getId());
				
				registrationForm.setMessage(resources.getString("registration.success"));
				registrationForm.setSuccess(true);
				return af;
			}
			
		}
		
	}

}
