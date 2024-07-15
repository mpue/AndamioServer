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
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.pmedv.cms.common.GlobalForwards;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.core.crypto.MD5Crypter;
import org.pmedv.forms.UserForm;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.User;
import org.pmedv.pojos.Usergroup;
 

/**
 * This action creates a new user
 * 
 * @author Matthias Pueski
 *
 */
public class UserAction  extends AbstractPermissiveAction {

	public UserAction() {
		super();
		log.debug("Executing "+this.getClass());
		setName("menu.user");		
	}
	
	public ActionForward execute(
			ActionMapping mapping,
		    ActionForm form,
		    HttpServletRequest request,
		    HttpServletResponse response) throws Exception{

		UserForm userForm = (UserForm) form;
		
		if (!isAllowedToProcess(request, getName())) return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);
			      
		User user = new User();
		
		user.setActive(userForm.getActive());
		user.setEmail(userForm.getEmail());
		user.setTitle(userForm.getTitle());
		user.setJoinDate(userForm.getJoinDate());
		user.setLand(userForm.getLand());
		user.setName(userForm.getName());
		user.setFirstName(userForm.getFirstName());
		user.setLastName(userForm.getLastName());
		user.setOrt(userForm.getOrt());
		byte[] pass = MD5Crypter.createMD5key(userForm.getPassword());
		String password = MD5Crypter.createMD5String(pass);	
		user.setPassword(password);	
		user.setRanking(userForm.getRanking());
		user.setTelefon(userForm.getTelefon());
		user.setJoinDate(new Date());
		user.setBirthDate(new Date());
		
		DAOManager.getInstance().getUserDAO().createAndStore( user );
		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);
		
		String basepath = config.getBasepath();									
		String userDir = basepath+"users/"+user.getName();
		
		File f = new File(userDir);
		
		if (!f.exists())
			f.mkdir();
		
		String userGalleryDir = basepath+"users/"+user.getName()+"/galleries/";

		File f1 = new File(userGalleryDir);
		
		if (!f1.exists())
			f1.mkdir();

		
		user = (User)DAOManager.getInstance().getUserDAO().findByName(user.getName());
		
		if ((request.getParameter("usergroup") != null) && (request.getParameter("usergroup") != "")) {
		
			String groupname = request.getParameter("usergroup");
			Usergroup group = (Usergroup) DAOManager.getInstance().getUsergroupDAO().findByName(groupname);
			
			if (group != null)
				DAOManager.getInstance().getUserDAO().addGroup(group.getId(), user.getId());

		}
		
		userForm.setUsers(DAOManager.getInstance().getUserDAO().findAllItems());

		return null;
		
	}
	
}


