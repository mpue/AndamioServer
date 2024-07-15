/**

	Weberknecht AndamioManager - Open Source Content Management
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
package org.pmedv.actions.userpages;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONSerializer;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.pmedv.beans.objects.AccountBean;
import org.pmedv.beans.objects.FolderBean;
import org.pmedv.beans.objects.MessageBean;
import org.pmedv.beans.objects.MessageStatus;
import org.pmedv.beans.objects.UserBean;
import org.pmedv.cms.common.CMSProperties;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.context.AppContext;
import org.pmedv.forms.UserMailboxForm;
import org.pmedv.pojos.Config;
import org.pmedv.services.UserMailService;
import org.pmedv.services.UserService;
import org.pmedv.web.ServerUtil;
import org.springframework.context.ApplicationContext;

public class UserMailboxAction extends DispatchAction {

	private static final ResourceBundle	resources	= ResourceBundle.getBundle("MessageResources");

	private String protocol;
	
	public UserMailboxAction() {
		protocol = CMSProperties.getInstance().getAppProps().getProperty("protocol");
	}
	
	public ActionForward showFolder(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		UserMailboxForm mailboxForm = (UserMailboxForm) form;

		String folderName = request.getParameter("name");
		
		if (folderName == null)
			folderName = "key.inbox";
		
		ApplicationContext ctx = AppContext.getApplicationContext();

		UserService userService = (UserService) ctx.getBean("userService");
		UserMailService mailService = (UserMailService) ctx.getBean("userMailService");

		String login = (String) request.getSession().getAttribute("login");

		if (login != null) {

			UserBean user = userService.findByName(login);

			if (user != null) {
				request.getSession().setAttribute("owner", true);
				mailboxForm.setUser(user);

				ArrayList<AccountBean> accounts = mailService.getAccounts(user);

				for (AccountBean ab : accounts) {

					if (ab.getName().equals("default")) {

						ArrayList<FolderBean> folders = mailService.getFolders(ab);
						
						log.info("User "+user.getName()+" has "+folders.size()+" folders.");
						
						mailboxForm.setFolder(folders);

						for (FolderBean f : folders) {
							
							if (f.getName().equals(folderName)) {
								
								mailboxForm.setCurrentFolder(f);
								request.getSession().setAttribute("currentFolder", f);
								
								ArrayList<MessageBean> messages = mailService.getMessages(f);								
								log.info("User "+user.getName()+" has "+messages.size()+" messages in folder "+f.getName());
								mailboxForm.setMessages(messages);
								
							}
							
						}
						
					}

				}

			}

			return mapping.findForward("showUserMailbox");
		}
		else {
			Config cfg = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);

			try {
				response.sendRedirect(protocol+"://" + ServerUtil.getHostUrl(request) + "/" + cfg.getLocalPath() + cfg.getStartnode() + ".html");
			}
			catch (IOException e) {
				log.error("Could not redirect to : " + protocol+"://" + ServerUtil.getHostUrl(request) + "/" + cfg.getLocalPath()
						+ cfg.getStartnode() + ".html");
			}
		}
		return null;

	}
	
	public ActionForward deleteMessage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		ApplicationContext ctx = AppContext.getApplicationContext();

		UserService userService = (UserService) ctx.getBean("userService");
		UserMailService mailService = (UserMailService) ctx.getBean("userMailService");

		FolderBean currentFolder = (FolderBean) request.getSession().getAttribute("currentFolder");		
		String login = (String) request.getSession().getAttribute("login");

		Long messageId = Long.valueOf(request.getParameter("message_id"));
		
		if (login != null) {
		
			MessageBean m = mailService.findMessageById(messageId);						
			UserBean user = userService.findByName(login);

			if (user != null) {

				/**
				 *  Since there's no backward reference from messages to users,
				 *  we need to check if the user is the owner of the message to be deleted.
				 */
				
				
				ArrayList<AccountBean> accounts = mailService.getAccounts(user);

				for (AccountBean ab : accounts) {
					
					/**
					 * Pick the default account
					 */
					
					if (ab.getName().equals("default")) {

						ArrayList<FolderBean> folders = mailService.getFolders(ab);

						/**
						 * Inspect all folders
						 */
						
						for (FolderBean f : folders) {							
							if (f.getName().equals(currentFolder.getName())) {

								ArrayList<MessageBean> messages = mailService.getMessages(f);								
								
								/**
								 * Finally if the folder contains the message, the user must be owner
								 * and the message can safely be deleted.
								 */
								
								if (messages.contains(m)) {
									mailService.removeMessage(currentFolder, m);									
								}
							}
						}
					}
				}
				
				ActionForward af = new ActionForward();
				af.setPath("/users/MailboxAction.do?do=showFolder&name="+currentFolder.getName());
				return af;

			}
			
		}
		else {
			Config cfg = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);

			try {
				response.sendRedirect(protocol+"://" + ServerUtil.getHostUrl(request) + "/" + cfg.getLocalPath() + cfg.getStartnode() + ".html");
			}
			catch (IOException e) {
				log.error("Could not redirect to : " + protocol+"://" + ServerUtil.getHostUrl(request) + "/" + cfg.getLocalPath()
						+ cfg.getStartnode() + ".html");
			}
		}
		return null;

	}

	public ActionForward sendMessage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		String subject = request.getParameter("subject");
		String receiver = request.getParameter("receiver");
		String body = request.getParameter("message");

		
		ApplicationContext ctx = AppContext.getApplicationContext();

		UserService userService = (UserService) ctx.getBean("userService");
		UserMailService mailService = (UserMailService) ctx.getBean("userMailService");
		
		String login = (String) request.getSession().getAttribute("login");
		
		if (login != null) {

			UserBean sendinguser   = userService.findByName(login);
			UserBean receivingUser = userService.findByName(receiver);
			
			if (sendinguser != null && receivingUser != null) {
			
				MessageBean message = new MessageBean();
				
				message.setSubject(subject);
				message.setBody(body);
				message.setFrom(login);
				message.setReceived(new Date());
				message.setStatus(MessageStatus.UNREAD);
				
				mailService.sendMessage(sendinguser, receivingUser, message);
				
			}
			
		}
		
		return null;
	}
	
	public ActionForward replyMessage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		String subject = request.getParameter("subject");
		String receiver = request.getParameter("receiver");
		String body = request.getParameter("message");
		Long messageId = Long.valueOf(request.getParameter("message_id"));
		
		ApplicationContext ctx = AppContext.getApplicationContext();

		UserService userService = (UserService) ctx.getBean("userService");
		UserMailService mailService = (UserMailService) ctx.getBean("userMailService");
		
		String login = (String) request.getSession().getAttribute("login");
		
		if (login != null) {

			UserBean sendinguser   = userService.findByName(login);
			UserBean receivingUser = userService.findByName(receiver);
			
			if (sendinguser != null && receivingUser != null) {
			
				MessageBean message = new MessageBean();
				
				message.setSubject(subject);
				message.setBody(body);
				message.setFrom(login);
				message.setReceived(new Date());
				message.setStatus(MessageStatus.UNREAD);
				// message.setInReplyToMessageId(messageId);
				
				mailService.sendMessage(sendinguser, receivingUser, message);
				
			}
			
		}
		
		return null;
	}

	
	public ActionForward showMessage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		ApplicationContext ctx = AppContext.getApplicationContext();

		UserService userService = (UserService) ctx.getBean("userService");
		UserMailService mailService = (UserMailService) ctx.getBean("userMailService");

		FolderBean currentFolder = (FolderBean) request.getSession().getAttribute("currentFolder");		
		String login = (String) request.getSession().getAttribute("login");

		Long messageId = Long.valueOf(request.getParameter("message_id"));
		
		if (login != null) {
		
			MessageBean m = mailService.findMessageById(messageId);						
			UserBean user = userService.findByName(login);

			if (user != null) {

				/**
				 *  Since there's no backward reference from messages to users,
				 *  we need to check if the user is the owner of the message to be shown.
				 */
				
				
				ArrayList<AccountBean> accounts = mailService.getAccounts(user);

				for (AccountBean ab : accounts) {
					
					/**
					 * Pick the default account
					 */
					
					if (ab.getName().equals("default")) {

						ArrayList<FolderBean> folders = mailService.getFolders(ab);

						/**
						 * Inspect all folders
						 */
						
						for (FolderBean f : folders) {							
							if (f.getName().equals(currentFolder.getName())) {

								ArrayList<MessageBean> messages = mailService.getMessages(f);								
								
								/**
								 * Finally if the folder contains the message, the user must be owner
								 * and the message can safely be shown.
								 */
								
								if (messages.contains(m)) {
									
									String data = JSONSerializer.toJSON(m).toString(); 
									
									try {
										PrintWriter out = response.getWriter();
										out.write(data);
										out.flush();
									}
									catch (IOException e) {
										log.error("Could not write message to outputstream");
									}
								
								}
							}
						}
					}
				}
			}
		
		}
		
		return null;

	}	
	
}
