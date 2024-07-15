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
package org.pmedv.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.core.crypto.MD5Crypter;
import org.pmedv.pojos.User;

public class LoginForm extends ActionForm
{

	private static final long serialVersionUID = 1L;

	private String username;
	private String password;
	
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
	
    public void reset(ActionMapping mapping, HttpServletRequest request) {
    	this.username = null;
    	this.password = null;
    }

    public ActionErrors validate( 
    	ActionMapping mapping, HttpServletRequest request ) {
    	
    	ActionErrors errors = new ActionErrors();    	
    	         
    	if( getUsername() == null || getUsername().length() < 1 ) {
    		errors.add("username",new ActionMessage("error.username.required"));
    	}
    	if( getPassword() == null || getPassword().length() < 1 ) {
    		errors.add("password",new ActionMessage("error.password.required"));
    	}

    	if (getUsername() != null && getPassword() != null) {
			
			User user = null;
			
			user = (User) DAOManager.getInstance().getUserDAO().findByName(getUsername());
	    	
	    	if (user == null) {
	    		errors.add("login",new ActionMessage("error.login.failed"));
	    		return errors;
	    	}
	    	
	    	byte[] passWordHash = MD5Crypter.createMD5key(getPassword());
	    	String encryptedPassword = MD5Crypter.createMD5String(passWordHash);
			String encryptedPassFromDB = user.getPassword();
			
			if (!encryptedPassword.equals(encryptedPassFromDB)) {
				errors.add("login",new ActionMessage("error.login.failed"));
			}

    	}

      return errors;
  }

	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return Returns the username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username The username to set.
	 */
	public void setUsername(String username) {
		this.username = username;
	}




}
