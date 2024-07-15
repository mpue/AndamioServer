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
package org.pmedv.plugins;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.core.crypto.MD5Crypter;
import org.pmedv.core.util.URLReader;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.User;
import org.pmedv.session.UserSession;
import org.pmedv.util.StringUtil;


/**
 * This is the user profile plugin. This plugin gives the visitor the ability to manage his own profile.
 * 
 * @author Matthias Pueski
 *
 */
public class UserProfilePlugin extends AbstractPlugin implements IPlugin, Serializable {
		
	private static final long serialVersionUID = 266675804365992495L;

	protected static Log log = LogFactory.getLog(UserProfilePlugin.class);
	
	private String formAction   = null;
	private String page		    = null;
	private String username     = null;
	private String firstName    = null;
	private String lastName     = null;
	private String title		= null;
	private String land			= null;
	private String ort 		    = null;
	private String telefon 	    = null;
	private String eMail	    = null;
	private String password 	= null;
	private String password_rep = null;
	
	public UserProfilePlugin () {
		super();
		pluginID = "UserManagerPlugin";
		paramDescriptors.put("plugin_userprofile_formaction", "Form Action");
		paramDescriptors.put("plugin_userprofile_pagetitle", resourceBundle.getString("plugin.field.pagetitle"));
	}
	
	/* (non-Javadoc)
	 * @see org.pmedv.plugins.IPlugin#getContent()
	 */
	public String getContent() {
		
		Config config = (Config) DAOManager.getInstance().getConfigDAO().findByID(1);

		StringBuffer s = new StringBuffer();
			
		try {
			formAction = paramMap.get("plugin_userprofile_formaction").trim();
		}	
		catch (NullPointerException n) {
			log.debug("some parameter is null, nothing special.");
		}
		try {
			page = paramMap.get("plugin_page").trim();
		}
		catch (NullPointerException n) {
			log.debug("some parameter is null, nothing special.");
		}
		try {
			username = paramMap.get("plugin_username").trim();
		}
		catch (NullPointerException n) {
			log.debug("some parameter is null, nothing special.");
		}
		try {
			firstName = paramMap.get("plugin_firstName").trim();
		}
		catch (NullPointerException n) {
			log.debug("some parameter is null, nothing special.");
		}
		try {
			lastName = paramMap.get("plugin_lastName").trim();
		}
		catch (NullPointerException n) {
			log.debug("some parameter is null, nothing special.");
		}
		try {
			title = paramMap.get("plugin_title").trim();
		}
		catch (NullPointerException n) {
			log.debug("some parameter is null, nothing special.");
		}
		try {
			land = paramMap.get("plugin_land").trim();
		}
		catch (NullPointerException n) {
			log.debug("some parameter is null, nothing special.");
		}
		try {
			ort = paramMap.get("plugin_ort").trim();
		}
		catch (NullPointerException n) {
			log.debug("some parameter is null, nothing special.");
		}
		try {
			telefon = paramMap.get("plugin_telefon").trim();
		}
		catch (NullPointerException n) {
			log.debug("some parameter is null, nothing special.");
		}
		try {
			eMail = paramMap.get("plugin_email").trim();
		}
		catch (NullPointerException n) {
			log.debug("some parameter is null, nothing special.");
		}
		try {
			password = paramMap.get("plugin_password").trim();
		}
		catch (NullPointerException n) {
			log.debug("some parameter is null, nothing special.");
		}
		try {
			password_rep = paramMap.get("plugin_password_repeat").trim();
		}
		catch (NullPointerException n) {
			log.debug("some parameter is null, nothing special.");
		}

		log.debug("page   : " + page);
		
		try {
		
			if (page.equals("saved")) {
				
				User user = (User)DAOManager.getInstance().getUserDAO().findByName(username);
				
				if (user != null) {			

					if (password != null && password_rep != null) {
						if (password.equals(password_rep)) {
							updateUser(user,password);
							s.append(resourceBundle.getString("plugin.userprofile.password.changed"));
						}
						else {
							s.append(resourceBundle.getString("plugin.userprofile.passwords.dontmatch"));
						}							
					}
					else {
						updateUser(user);
						s.append(resourceBundle.getString("plugin.userprofile.changes.saved"));
					}
					
				}
				
				s.append(getMainpage());
				setTitle(paramMap.get("plugin_userprofile_pagetitle"));
			}			
			else {
				setTitle(paramMap.get("plugin_userprofile_pagetitle"));
				s.append(getMainpage());
			}
		}
		catch (NullPointerException n) {			
			s.append(getMainpage());
			setTitle(paramMap.get("plugin_userprofile_pagetitle"));
		}
		
		paramMap.put("plugin_page", null);
						
		return s.toString();
	}
	
	/**
	 * Shows the user's mailbox
	 * 
	 * @return
	 * @throws NullPointerException
	 */
	public String getMainpage() throws NullPointerException {
		
		Config config = (Config) DAOManager.getInstance().getConfigDAO().findByID(1);
		
		StringBuffer s = new StringBuffer();
		
		UserSession sessionManager = new UserSession();
		sessionManager.init(false, request);
		sessionManager.getAttributes();
		
		if (sessionManager.getLogin() != null) {

			User user = (User)DAOManager.getInstance().getUserDAO().findByName(sessionManager.getLogin());
			
			String url = protocol+"://localhost:"+request.getServerPort()+"/"+config.getLocalPath()+"templates/"+config.getTemplate()+"/UserProfile.jsp";

			String formLayout = URLReader.getDefaultContent(url,null); 
			
			formLayout = StringUtil.replace(formLayout, "##USERNAME##", user.getName());
			formLayout = StringUtil.replace(formLayout, "##FIRSTNAME##", user.getFirstName());
			formLayout = StringUtil.replace(formLayout, "##LASTNAME##", user.getLastName());
			formLayout = StringUtil.replace(formLayout, "##TITLE##" , user.getTitle());
			formLayout = StringUtil.replace(formLayout, "##LAND##" , user.getLand());
			formLayout = StringUtil.replace(formLayout, "##ORT##", user.getOrt());
			formLayout = StringUtil.replace(formLayout, "##TELEFON##" , user.getTelefon());
			formLayout = StringUtil.replace(formLayout, "##EMAIL##", user.getEmail());
			
			s.append("<form name=\"UserProfileForm\" method=\"post\" action=\""+formAction+"&plugin_page=saved\">");
			s.append(formLayout);
			s.append("</form>");
		}


		else
			s.append("You must be logged in in order to change your profile.");
		
		return s.toString();
	}
	
	private void updateUser(User user) {
		
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setTitle(title);
		user.setEmail(eMail);
		user.setLand(land);
		user.setOrt(ort);
		user.setTelefon(telefon);
		
		DAOManager.getInstance().getUserDAO().update(user);

	}

	private void updateUser(User user, String password) {

		user.setFirstName(firstName);
		user.setLastName(lastName);		
		user.setTitle(title);
		user.setEmail(eMail);
		user.setLand(land);
		user.setOrt(ort);
		user.setTelefon(telefon);
		
		byte[] pass   = MD5Crypter.createMD5key(password);
		String encryptedPass = MD5Crypter.createMD5String(pass);	

		user.setPassword(encryptedPass);
		
		DAOManager.getInstance().getUserDAO().update(user);

	}

}
