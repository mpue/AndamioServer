/**
 * WeberknechtCMS - Open Source Content Management Written and maintained by Matthias Pueski
 * 
 * Copyright (c) 2009 Matthias Pueski
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 */
package org.pmedv.plugins;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.core.util.URLReader;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.User;
import org.pmedv.pojos.Usergroup;
import org.pmedv.pojos.plugins.Faq;
import org.pmedv.session.UserSession;

/**
 * This is the faq plugin. This plugin gives the visitor the ability to view, and if user is admin
 * to manage, faqs
 * 
 * @author Steffen Hochmuth
 * @author Matthias Pueski
 * 
 */
public class FaqPlugin extends AbstractPlugin implements IPlugin, Serializable {

	private static final long serialVersionUID = 266675804365992495L;

	protected static Log log = LogFactory.getLog(FaqPlugin.class);

	private String formAction = null;
	private String page = null;
	private String answer = null;
	private String question = null;
	private String studyId = null;
	private boolean isAdmin = false;
	
	public FaqPlugin() {
		super();
		pluginID = "FaqPlugin";
		paramDescriptors.put("plugin_faq_formaction", "Form Action");
		paramDescriptors.put("plugin_faq_pagetitle", resourceBundle.getString("plugin.field.pagetitle"));
		paramDescriptors.put("plugin_faq_studyId", "Studiennummer");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.pmedv.plugins.IPlugin#getContent()
	 */
	public String getContent() {

		checkAdmin();
		
		StringBuffer s = new StringBuffer();

		try {
			formAction = paramMap.get("plugin_faq_formaction").trim();
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
			question = paramMap.get("plugin_faq_question").trim();
		}
		catch (NullPointerException n) {
			question = null;
			log.debug("some parameter is null, nothing special.");
		}
		try {
			studyId = paramMap.get("plugin_faq_studyId").trim();
		}
		catch (NullPointerException n) {
			log.debug("some parameter is null, nothing special.");
		}
		try {
			answer = paramMap.get("plugin_faq_answer").trim();
		}
		catch (NullPointerException n) {
			answer = null;
			log.debug("some parameter is null, nothing special.");
		}
		log.debug("page   : " + page);

		try {

			if (page.equals("saved") && isAdmin) {

				if (question != null && answer != null && question.length() > 0 && answer.length() > 0) {
					Faq faq = new Faq();
					faq.setQuestion(question);
					faq.setAnswer(answer);
					faq.setStudyId(studyId);
					
					DAOManager.getInstance().getFaqDAO().createAndStore(faq);
					
					paramMap.put("plugin_faq_question", null);
					paramMap.put("plugin_faq_answer", null);
					paramMap.put("plugin_page", null);
				}
								
				s.append(getMainpage());
				setTitle(paramMap.get("plugin_faq_pagetitle"));				
			}
			else if(page.equals("delete") && isAdmin) {				
				Long id = Long.valueOf(request.getParameter("id"));
				Faq f = (Faq)DAOManager.getInstance().getFaqDAO().findByID(id);
				
				if (f != null)
					DAOManager.getInstance().getFaqDAO().delete(f);

				s.append(getMainpage());
				setTitle(paramMap.get("plugin_faq_pagetitle"));				
			}
			
			else {
				setTitle(paramMap.get("plugin_faq_pagetitle"));
				s.append(getMainpage());
			}
		}
		catch (NullPointerException n) {
			s.append(getMainpage());
			setTitle(paramMap.get("plugin_faq_pagetitle"));
		}

		isAdmin = false;

		return s.toString();
	}

	/**
	 * Shows the list of Faqs
	 * 
	 * @return
	 * @throws NullPointerException
	 */
	public String getMainpage() throws NullPointerException {

		Config config = (Config) DAOManager.getInstance().getConfigDAO().findByID(1);

		StringBuffer s = new StringBuffer();


		String url = protocol + "://localhost:" + request.getServerPort() + "/" + config.getLocalPath()
			+ "templates/" + config.getTemplate() + "/faq.jsp";

		String formLayout = URLReader.getDefaultContent(url,null);
		
		
		if (isAdmin)
			formLayout = formLayout.replaceAll("##DISPLAY##", "block");
		else
			formLayout = formLayout.replaceAll("##DISPLAY##", "none");
		
		StringBuffer faqList = new StringBuffer();

		faqList.append("<table class=\"faq\">");
		
		for (Object o : DAOManager.getInstance().getFaqDAO().findFaqsByStudyId(studyId)) {
			
			Faq f = (Faq) o;			
			faqList.append("<tr><td class=\"question\">Question:\n </td><td>");
			faqList.append(f.getQuestion());
			
			if (isAdmin) {
				faqList.append("</td><td><a href=\""+formAction+"&plugin_page=delete&id="+f.getId()+"\">delete</a></td></tr>");
				faqList.append("<tr><td class=\"answer\" colspan=\"1\">Answer:\n </td><td colspan=\"2\">");
			}
			else {
				faqList.append("</td></tr>");
				faqList.append("<tr><td class=\"answer\" colspan=\"1\">Answer:\n </td><td colspan=\"1\">");				
			}
			
			faqList.append(f.getAnswer());
			faqList.append("</td></tr>");				
		}
		
		faqList.append("</table>");			
		formLayout = formLayout.replaceAll("##FAQLIST##", faqList.toString());
		
		s.append("<form name=\"FaqForm\" method=\"post\" action=\"" + formAction
				+ "&plugin_page=saved\">");
		s.append(formLayout);
		s.append("</form>");


		return s.toString();
	}

	private void checkAdmin() {
		UserSession sessionManager = new UserSession();
		sessionManager.init(false, request);
		sessionManager.getAttributes();
		
		if (sessionManager.getLogin() != null) {

			User user = (User) DAOManager.getInstance().getUserDAO().findByName(sessionManager.getLogin());

			for (Usergroup u : user.getGroups()) {
				if (u.getName().equals("admin")) {
					isAdmin = true;
					break;
				}
			}
			
		}
	}

}
