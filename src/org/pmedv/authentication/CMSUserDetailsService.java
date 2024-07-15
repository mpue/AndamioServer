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
package org.pmedv.authentication;

import org.pmedv.cms.daos.DAOManager;
import org.pmedv.cms.daos.UserDAO;
import org.springframework.dao.DataAccessException;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.User;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;

/**
 * The <code>CMSUserDetailsService</code> authorizes a user against the user 
 * database in a spring convenient manner. This service is used by the frontend
 * administration tool (AndamioManager). 
 * 
 * @author Matthias Pueski
 *
 */
public class CMSUserDetailsService implements UserDetailsService {

	@SuppressWarnings("serial")
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {

		// TODO : More detailed user checking, comparison and ROLE definitions.
		
		GrantedAuthority[] authorities = new GrantedAuthority[1];

		authorities[0] = new GrantedAuthority() {

			@Override
			public String getAuthority() {

				return "ROLE_USER";
			}

			@Override
			public int compareTo(Object o) {
				return 0;
			}

		};

		User user;

		UserDAO userDAO = (UserDAO) DAOManager.getInstance().getUserDAO();

		org.pmedv.pojos.User sysUser = (org.pmedv.pojos.User) userDAO.findByName(username);

		if (sysUser != null) {
			user = new User(sysUser.getName(), sysUser.getPassword(), true, true, true, true, authorities);
		}
		else {
			throw new UsernameNotFoundException("user not found.");
		}

		return user;
	}

}
