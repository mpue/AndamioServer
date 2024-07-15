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

import java.io.File;
import java.io.Serializable;
import java.util.Date;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.cms.daos.CalendarDAO;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.cms.daos.UserDAO;
import org.pmedv.context.AppContext;
import org.pmedv.core.crypto.MD5Crypter;
import org.pmedv.core.util.URLReader;
import org.pmedv.mail.SMTPMailer;
import org.pmedv.mail.SMTPMailer.MessageType;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.User;
import org.pmedv.pojos.UserProfile;
import org.pmedv.pojos.Usergroup;
import org.pmedv.pojos.calendar.Calendar;
import org.pmedv.util.ResourceUtils;
import org.pmedv.util.StringUtil;

/**
 * This is the user registration plugin.
 * 
 * @author Matthias Pueski
 *
 */
public class UserRegistrationPlugin extends AbstractPlugin implements IPlugin, Serializable {
		
	private static final long serialVersionUID = 7025408650986271562L;

	protected static Log log = LogFactory.getLog(UserProfilePlugin.class);
	
	private String formAction   = null;
	private String page		    = null;
	private String username     = null;
	private String firstName    = null;
	private String lastName     = null;
	private String title		= null;
	private String land			= null;
	private String city		    = null;
	private String telephone    = null;
	private String eMail	    = null;
	private String password 	= null;
	private String password_rep = null;
	private String captcha      = null;

	private String preserveUsername  = "";
	private String preserveFirstname = "";
	private String preserveLastname  = "";
	private String preserveTitle     = "";
	private String preserveLand	     = "";
	private String preserveOrt 	     = "";
	private String preserveTelefon   = "";
	private String preserveMail	     = "";

	private String defaultGroups = null;
	
	public UserRegistrationPlugin () {
		super();
		pluginID = "UserManagerPlugin";
		paramDescriptors.put("plugin_userprofile_formaction", "Form Action");
		paramDescriptors.put("plugin_userprofile_pagetitle",  "Seitentitel");
		paramDescriptors.put("plugin_userprofile_defaultgroups", "Gruppen");
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
			defaultGroups = paramMap.get("plugin_userprofile_defaultgroups");
		}
		catch (NullPointerException n) {
			log.debug("some parameter is null, nothing special.");
		}
		
		try {
			page = paramMap.get("plugin_page").trim();
		}
		catch (NullPointerException n) {
			page = null;
			log.debug("some parameter is null, nothing special.");
		}
		try {
			username = paramMap.get("plugin_username").trim();
			if (username != null)
				preserveUsername = username;
		}
		catch (NullPointerException n) {
			log.debug("some parameter is null, nothing special.");
		}
		try {
			firstName = paramMap.get("plugin_firstName").trim();
			if (firstName != null)
				preserveFirstname = firstName;
		}
		catch (NullPointerException n) {
			log.debug("some parameter is null, nothing special.");
		}
		try {
			lastName = paramMap.get("plugin_lastName").trim();
			if (lastName != null)
				preserveLastname = lastName;
		}
		catch (NullPointerException n) {
			log.debug("some parameter is null, nothing special.");
		}

		try {
			title = paramMap.get("plugin_title").trim();
			if (title != null)
				preserveTitle = title;
		}
		catch (NullPointerException n) {
			log.debug("some parameter is null, nothing special.");			
		}
		try {
			land = paramMap.get("plugin_land").trim();
			if (land != null)
				preserveLand = land;
		}
		catch (NullPointerException n) {
			log.debug("some parameter is null, nothing special.");
		}
		try {
			city = paramMap.get("plugin_ort").trim();
			if (city != null)
				preserveOrt = city;
		}
		catch (NullPointerException n) {
			log.debug("some parameter is null, nothing special.");
		}
		try {
			telephone = paramMap.get("plugin_telefon").trim();
			if (telephone != null)
				preserveTelefon = telephone;
		}
		catch (NullPointerException n) {
			log.debug("some parameter is null, nothing special.");
		}
		try {
			eMail = paramMap.get("plugin_email").trim();
			if (eMail != null)
				preserveMail = eMail;
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
		try {
			captcha = paramMap.get("plugin_captcha").trim();
		}
		catch (NullPointerException n) {
			log.debug("some parameter is null, nothing special.");
		}

		log.debug("page   : " + page);
		
		try {
			
			setTitle(paramMap.get("plugin_userprofile_pagetitle"));
			
			if (page.equals("register")) {

				if (password != null && password_rep != null && username != null && eMail != null) {
					
					String _captcha = (String)request.getSession().getAttribute(nl.captcha.servlet.Constants.SIMPLE_CAPCHA_SESSION_KEY);
					
					if (captcha.equals(_captcha)) {
						
						if (password.equals(password_rep) && password.length() >= 8) {
							
							User user = null;
							
							user = (User)DAOManager.getInstance().getUserDAO().findByName(username); 
							
							if (user != null) {
								s.append(resourceBundle.getString("plugin.registration.userwithnameexists"));
								s.append(getMainpage());
								return s.toString();
							}
							else {

								user = DAOManager.getInstance().getUserDAO().findByMail(eMail);
								
								if (user != null) {
									s.append(resourceBundle.getString("plugin.registration.userwithmailexists"));
									s.append(getMainpage());
									return s.toString();
								}
								else {
									
									User newUser = new User();
									
									newUser.setEmail(eMail);
									newUser.setJoinDate(new Date());
									newUser.setLand(land);
									newUser.setName(username);
									newUser.setFirstName(firstName);
									newUser.setLastName(lastName);
									newUser.setOrt(city);
									newUser.setTitle("User");
									newUser.setTelefon(telephone);
									newUser.setActive(true);
									newUser.setRanking(0);
									
									UserProfile profile = new UserProfile();									
									profile.setPageContent(resourceBundle.getString("user.profile.empty.content"));
									
									byte[] pass   = MD5Crypter.createMD5key(password);
									String encryptedPass = MD5Crypter.createMD5String(pass);	
									
									newUser.setPassword(encryptedPass);
								
									DAOManager.getInstance().getUserDAO().createAndStore(newUser,profile);

									// refetch newly created user, in order to get the id.
									
									newUser = (User)DAOManager.getInstance().getUserDAO().findByName(username);
									Usergroup users = (Usergroup)DAOManager.getInstance().getUsergroupDAO().findByName("user");																										
									DAOManager.getInstance().getUserDAO().addGroup(users.getId(), newUser.getId());
									
									if (defaultGroups != null) {
										
										// add user to specified default groups
										
										String[] groupNames = defaultGroups.split(",");
										
										for (String groupName : groupNames) {

											Usergroup g = (Usergroup)DAOManager.getInstance().getUsergroupDAO().findByName(groupName);																										
											
											if (g != null) {
												DAOManager.getInstance().getUserDAO().addGroup(g.getId(), newUser.getId());												
											}
											
										}
										
									}

									if (!createDefaultCalendar(newUser)) {
										log.error("could not create default calendar for user "+newUser.getName());										
									}
									
									String basepath = config.getBasepath();									
									String userDir = basepath+"users/"+username;
									
									File f = new File(userDir);
									
									if (!f.exists())
										f.mkdir();
									
									String userGalleryDir = basepath+"users/"+username+"/galleries/";

									File f1 = new File(userGalleryDir);
									
									if (!f1.exists())
										f1.mkdir();

									String avatarDir = basepath+"users/"+username+"/avatars/";

									f1 = new File(avatarDir);
									
									if (!f1.exists())
										f1.mkdir();
									
									ResourceUtils.createDefaultMailAccount(newUser.getName());
									
									SMTPMailer mailer = new SMTPMailer(config.getSmtphost(),config.getUsername(),config.getPassword());
									
									String responseTextUrl = protocol+"://"+request.getServerName()+":"+request.getServerPort()+"/"+config.getLocalPath()+"templates/"+config.getTemplate()+"/registrationResponse.jsp";
									
									mailer.setFrom(config.getFromadress());
									mailer.setTo(eMail);
									
									String subject = resourceBundle.getString("plugin.registration.success");									
									subject = StringUtil.replace(subject, "##SITENAME##", config.getSitename());
									
									mailer.setSubject(subject);
									mailer.setText(URLReader.getDefaultContent(responseTextUrl,null));
									
									try {										
										mailer.createMessage(MessageType.TEXT_HTML);
										mailer.addToRecipient(eMail);
										mailer.sendMessage();									
									} 
									catch (AuthenticationFailedException a) {
										s.append("<p class=\"error\">");
										s.append(resourceBundle.getString("plugin.registration.mailnotsent"));
										s.append("<p>");
										s.append("<p class=\"error\">");
										s.append(resourceBundle.getString("plugin.registration.authenticationfailure"));
										s.append("<p>");
										resetFields();										
										return s.toString();										
									}
									catch (MessagingException e) {
										s.append("<p class=\"error\">");
										s.append(resourceBundle.getString("plugin.registration.mailnotsent"));
										s.append("<p>");
										s.append("<p class=\"error\">");
										s.append(e.getMessage());
										s.append("<p>");
										e.printStackTrace();
										resetFields();										
										return s.toString();
									} 
									catch (Exception e) {
										s.append("<p class=\"error\">");
										s.append(resourceBundle.getString("plugin.registration.mailnotsent"));
										s.append("<p>");
										s.append("<p class=\"error\">");
										s.append(e.getMessage());
										s.append("<p>");
										e.printStackTrace();
										resetFields();										
										return s.toString();
									}
									
								}
								
							}
							
							String message = resourceBundle.getString("plugin.registration.success");
							message = StringUtil.replace(message, "##SITENAME##", config.getSitename());
							
							s.append(message);
							resetFields();
							return s.toString();
						}
						else {
							s.append(resourceBundle.getString("plugin.registration.passwordsdontmatch"));
							s.append(getMainpage());
							return s.toString();
						}													
					}
					else {
						s.append(resourceBundle.getString("plugin.registration.captchasdontmatch"));
						s.append(getMainpage());
						return s.toString();
					}
					
				}
				else {
					s.append(resourceBundle.getString("plugin.registration.allfieldsrequried"));
					s.append(getMainpage());
					return s.toString();
				}
				
			}			
			else {
				s.append(getMainpage());
				resetFields();
				return s.toString();
			}
		}
		catch (NullPointerException n) {			
			s.append(getMainpage());
			resetFields();
			return s.toString();
		}
		
	}
	

	/**
	 * Creates the default calendar for the user.
	 * 
	 * @param user
	 * @return
	 */
	private boolean createDefaultCalendar(User user) {
		
		log.debug("Creating default calendar for user "+user.getName());
		
		CalendarDAO calendarDAO = (CalendarDAO)AppContext.getApplicationContext().getBean("calendarDAO");
		
		Calendar calendar = new Calendar();
		calendar.setName(user.getName()+".default");
		calendar.setDescription("Personal calendar of "+user.getName());
		
		// If calendar creation was successfull, assign calendar to user		
		calendar = (Calendar) calendarDAO.createAndStore(calendar);
		
		if (calendar != null) {
			
			calendar = (Calendar)calendarDAO.findByName(user.getName()+".default");
			
			UserDAO userDAO = DAOManager.getInstance().getUserDAO();
			userDAO.addCalendar(calendar.getId(), user.getId());
	
			return true;
		}
		else {			
			return false;
		}
	}

	/**
	 * Resets all fields to default values;
	 */
	private void resetFields(){

		setTitle(paramMap.get("plugin_userprofile_pagetitle"));
		
		preserveUsername = "";
		preserveFirstname= "";
		preserveLastname = "";
		preserveTitle    = "";
		preserveLand	 = "";
		preserveOrt 	 = "";
		preserveTelefon  = "";
		preserveMail	 = "";
		paramMap.put("plugin_page", null);
		page = null;
		
	}
	
	/**
	 * Shows the plugins mainpage
	 * 
	 * @return
	 * @throws NullPointerException
	 */
	public String getMainpage() throws NullPointerException {
		
		Config config = (Config) DAOManager.getInstance().getConfigDAO().findByID(1);
		
		StringBuffer s = new StringBuffer();
			
		String url = protocol+"://"+request.getServerName()+":"+request.getServerPort()+"/"+config.getLocalPath()+"templates/"+config.getTemplate()+"/registration.jsp";

		String formLayout = URLReader.getDefaultContent(url,null); 

		formLayout = StringUtil.replace(formLayout, "##USERNAME##", preserveUsername);
		formLayout = StringUtil.replace(formLayout, "##FIRSTNAME##", preserveFirstname);
		formLayout = StringUtil.replace(formLayout, "##LASTNAME##", preserveLastname);
		formLayout = StringUtil.replace(formLayout, "##TITLE##" , preserveTitle);		
		formLayout = StringUtil.replace(formLayout, "##LAND##" , preserveLand);
		formLayout = StringUtil.replace(formLayout, "##ORT##", preserveOrt);
		formLayout = StringUtil.replace(formLayout, "##TELEFON##" , preserveTelefon);
		formLayout = StringUtil.replace(formLayout, "##EMAIL##", preserveMail);
		
		s.append("<form name=\"UserProfileForm\" method=\"post\" action=\""+formAction+"&plugin_page=register\">");
		s.append(formLayout);
		s.append("</form>");
		
		return s.toString();
	}
	


	
}
