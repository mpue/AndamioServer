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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.pmedv.cms.common.CMSProperties;
import org.pmedv.cms.common.GlobalForwards;
import org.pmedv.cms.common.Params;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.core.util.URLReader;
import org.pmedv.forms.SiteContactForm;
import org.pmedv.mail.SMTPMailer;
import org.pmedv.mail.SMTPMailer.MessageType;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.Content;
import org.pmedv.pojos.User;

/**
 * This action sends a mail from the site contact to all selected users
 * 
 * @author Matthias Pueski
 *
 */
public class SiteContactAction  extends DispatchAction {

	private static final Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);	
	private static final Log log = LogFactory.getLog(SiteContactAction.class);
	
	/**
	 * Send contact form by mail, to all given users.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward send(
			ActionMapping mapping,
		    ActionForm form,
		    HttpServletRequest request,
		    HttpServletResponse response) throws Exception{
		
		SiteContactForm contactForm = (SiteContactForm) form;
		String protocol = CMSProperties.getInstance().getAppProps().getProperty("protocol");
		String templateUrl = protocol+"://localhost:"+request.getServerPort()+"/"+config.getLocalPath()+"templates/"+config.getTemplate()+"/contactFormResponse.jsp";
		
		TagNameFilter divTagFilter = new TagNameFilter("DIV");
		
		log.debug("Trying to send a message.");
		log.info("Receivers :" +request.getParameter(Params.CONTACT_USERS));
		
		String[] usernames = request.getParameter(Params.CONTACT_USERS).split(";");
		
		for (int i=0; i< usernames.length;i++) {
			
			try {
			
				User currentUser = (User)DAOManager.getInstance().getUserDAO().findByName(usernames[i]);
				
				if (currentUser != null) {
					
					String mailerText = URLReader.getDefaultContent(templateUrl,null);
					
					if (contactForm.getVorname() != null && contactForm.getNachname() != null) {
						String fromFullname  = contactForm.getVorname()+" "+contactForm.getNachname();
						mailerText = mailerText.replace("##FROM##",fromFullname);
					}
					
					if (currentUser.getName() != null)
						mailerText = mailerText.replace("##CURRENTUSER##", currentUser.getName());
					
					if (contactForm.getAnschrift() != null)
						mailerText = mailerText.replace("##ADDRESS##",contactForm.getAnschrift());
					
					if (contactForm.getTelefon() != null)
						mailerText = mailerText.replace("##TELEPHONE##",contactForm.getTelefon());
					
					if (contactForm.getTelefax() != null)
						mailerText = mailerText.replace("##FAX##",contactForm.getTelefax());
					
					if (contactForm.getEmail() != null)
						mailerText = mailerText.replace("##EMAIL##",contactForm.getEmail());
					
					if (contactForm.getLand() != null)
						mailerText = mailerText.replace("##COUNTRY##",contactForm.getLand());
					
					if (contactForm.getMessage() != null)
						mailerText = mailerText.replace("##MESSAGE##",contactForm.getMessage());
										
					String from     = config.getFromadress();
					String to       = currentUser.getEmail();
					String host     = config.getSmtphost();
					String username = config.getUsername();
					String password = config.getPassword();
		
					SMTPMailer mailer = new SMTPMailer(config.getSmtphost(),config.getUsername(),config.getPassword());

					mailer.setFrom(from);					
					mailer.setText(mailerText);

					try {
				        Parser parser = new Parser();
				        parser.setInputHTML(mailerText);
				        parser.setFeedback(Parser.STDOUT);
				        
				        NodeList nodeList = parser.parse(divTagFilter);

				        for (NodeIterator e = nodeList.elements();e.hasMoreNodes();) {

				            org.htmlparser.Node currentNode = e.nextNode();

				            if (currentNode instanceof Div) {
				            	
				                Div currentDiv = (Div) currentNode;

				                if (currentDiv.getAttribute("id") != null) {
				                	String id = currentDiv.getAttribute("id");
				                	if (id.equalsIgnoreCase("subject")) {								                		
				                		
				                		String subject = currentDiv.toHtml();
				                		
				                		int startPos = subject.indexOf(">");
				                		int endPos   = subject.lastIndexOf("<");
				                		
				                		subject = subject.substring(startPos+1,endPos);
				                		
				                		mailer.setSubject(subject);            						                		
				                	}
				                	
				                }
				                
				            }   

				        }
				        
					}					
				    catch (ParserException e) {
				        log.info("HTML Parser failed.");
				    }
					
					mailer.createMessage(MessageType.TEXT_HTML);					
					mailer.addToRecipient(to);														    
					mailer.sendMessage();
				
					log.debug("Message has been sent to : "+to);
					
				}
				
			} 
			catch (RuntimeException e) {
				log.error("Error sending message.");
				e.printStackTrace();
			}
			
		}
		
		return null;
	}
	
	/**
	 * Initialize the contact form
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward fill(
			ActionMapping mapping,
		    ActionForm form,
		    HttpServletRequest request,
		    HttpServletResponse response) throws Exception{
			      
		SiteContactForm contactForm = (SiteContactForm) form;
		
	    long contact_id = Long.parseLong(request.getParameter(Params.ID_CONTACT));
	     
	    Content content = (Content) DAOManager.getInstance().getContentDAO().findByName("Kontaktanfrage");
	    
	    contactForm.setContent(content);
	    contactForm.setId(contact_id);
	    
	    return mapping.findForward(GlobalForwards.SITE_CONTACT);
	    
	}
	
	
}


