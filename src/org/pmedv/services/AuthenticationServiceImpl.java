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
package org.pmedv.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.User;

public class AuthenticationServiceImpl implements AuthenticationService {

	private static final Log log = LogFactory.getLog(AuthenticationServiceImpl.class);
	
	@Override
	public boolean isAuthenticatable(Object principal, Object credentials) throws IllegalArgumentException {

		if (principal == null || credentials == null)
			throw new IllegalArgumentException("Username and password must both not be null");
		
		if (principal instanceof String && credentials instanceof String) {
			
			String username = (String) principal;
			String encryptedPassword = (String) credentials;
			
			if (username.length() < 1 || encryptedPassword.length() < 1)
				throw new IllegalArgumentException("Both, username and password must be provided.");
			
			User user = (User)DAOManager.getInstance().getUserDAO().findByName(username);
			
			if (user != null) {
				
				log.info("Logging in for user : "+username);
				
				if (user.getPassword().equals(encryptedPassword)) {
					log.info("login ok, proceed");
					return true;
				}
					
			}	
			
		}
		
		log.info("wrong credentials.");
		
		return false;
	}

}
