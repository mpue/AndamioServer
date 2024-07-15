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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.MissingResourceException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.core.html.Attribute.Align;
import org.pmedv.core.html.Table;
import org.pmedv.core.html.TableColumn;
import org.pmedv.core.html.TableRow;
import org.pmedv.core.util.URLReader;
import org.pmedv.plugins.objects.Answer;
import org.pmedv.plugins.objects.Interview;
import org.pmedv.plugins.objects.Question;
import org.pmedv.plugins.objects.QuestionType;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.User;
import org.pmedv.session.UserSession;
import org.pmedv.util.RequestUtil;
import org.pmedv.web.ServerUtil;

public class InterviewPlugin extends AbstractPlugin implements IPlugin, Serializable {

	private static final long serialVersionUID = 7680587189717028724L;

	protected static Log log = LogFactory.getLog(InterviewPlugin.class);
	
	private String formAction;
	private String pluginPage;
	private String reportPath;
	private String interviewPath;
	
	private transient User user;
	
	public InterviewPlugin() {
		
		super();
		
		pluginID = "InterviewPlugin";
		paramDescriptors.put("plugin_interview_path", resourceBundle.getString("plugin.interview.field.path"));		
		paramDescriptors.put("plugin_interview_reportpath", resourceBundle.getString("plugin.interview.field.reportpath"));
		paramDescriptors.put("plugin_userprofile_formaction", "Form Action");
		
		try {
			paramDescriptors.put("plugin_title", resourceBundle.getString("plugin.field.pagetitle"));	
		}
		catch (MissingResourceException m) {
			paramDescriptors.put("plugin_title", "plugin.field.pagetitle");
		}
		
		
	}
	
	public String getContent() {
		
		Config config = (Config) DAOManager.getInstance().getConfigDAO().findByID(1);

		StringBuffer sb = new StringBuffer();
		
		setTitle(paramMap.get("plugin_title"));
		
		UserSession sessionManager = new UserSession();
		sessionManager.init(false, request);
		sessionManager.getAttributes();
		
		if (sessionManager.getLogin() != null) {
			user = (User)DAOManager.getInstance().getUserDAO().findByName(sessionManager.getLogin());
		}
		
		if (user == null) {
			sb.append(resourceBundle.getString("login.required"));
			return sb.toString();
		}
		
		RequestUtil.dumpParams(request);
		
		interviewPath = paramMap.get("plugin_interview_path");
		
		try {
			formAction = paramMap.get("plugin_userprofile_formaction").trim();
		}	
		catch (NullPointerException n) {
			log.debug("some parameter is null, nothing special.");
		}

		reportPath = paramMap.get("plugin_interview_reportpath");
		
		try {
			formAction = paramMap.get("plugin_userprofile_formaction").trim();
		}	
		catch (NullPointerException n) {
			log.debug("some parameter is null, nothing special.");
		}
		
		try {
			pluginPage = paramMap.get("plugin_page").trim();
		}	
		catch (NullPointerException n) {
			log.debug("some parameter is null, nothing special.");
		}

		if (pluginPage != null) {
			
			if (pluginPage.equalsIgnoreCase("saved")) {
				
				pluginPage = null;
			
				String reportLocation = config.getBasepath()+interviewPath;
				File reportFile = new File(reportLocation);
				
				if (!reportFile.exists()) {
					
					sb.append("<p>Could not locate "+reportLocation+"</p>");
					sb.append("<b>Please check the location of the XML file.</p>");
					return sb.toString();
				}
				
				try {
				
					/**
					 * Add the user to the participants list, this is needed in order to prevent the user to take part multiple times
					 * on the same interview.
					 */
					
					String content = FileUtils.readFileToString(reportFile,"UTF-8");				
					Unmarshaller u = (Unmarshaller)JAXBContext.newInstance(Interview.class).createUnmarshaller();				
					Interview interview = (Interview)u.unmarshal(new StringReader(content));
									
					processInterview(interview);
					
					if (interview.getParticipatedUsers() == null) {
						ArrayList<String> users = new ArrayList<String>();
						users.add(user.getName());
						interview.setParticipatedUsers(users);
					}
					else {
						interview.getParticipatedUsers().add(user.getName());
					}
					
					File output = new File(reportLocation);
					FileOutputStream fos = new FileOutputStream(output);
					
					Marshaller m = (Marshaller)JAXBContext.newInstance(Interview.class).createMarshaller();
					m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );					
					m.marshal(interview, fos);
					
					fos.close();
					
					String url = protocol+"://localhost:"+request.getServerPort()+"/"+config.getLocalPath()+"templates/"+config.getTemplate()+"/interviewResponse.jsp";				
					String responseText = URLReader.getDefaultContent(url,null);				
					sb.append(responseText);
				}
				catch (Exception e) {
					log.info("An exception occured : "+e.getStackTrace().toString());
				}
				
			}
			
		}
		else {

			String reportLocation = config.getBasepath()+interviewPath;
			log.debug("reading Interview from : "+reportLocation);
			File reportFile = new File(reportLocation);
			
			if (!reportFile.exists()) {
				
				sb.append("<p>Could not locate "+reportLocation+"</p>");
				sb.append("<p><b>Please check the location of the XML file.</b></p>");
				return sb.toString();
			}
			
			try {
				
				String content = FileUtils.readFileToString(reportFile,"UTF-8");				
				Unmarshaller u = (Unmarshaller)JAXBContext.newInstance(Interview.class).createUnmarshaller();				
				Interview interview = (Interview)u.unmarshal(new StringReader(content));
				
				if (interview.getParticipatedUsers() != null)
				
					for (String userName : interview.getParticipatedUsers()) {
						
						if (userName.equals(user.getName())) {
	
							String url = protocol+"://"+ServerUtil.getHostUrl(request)+"/"+config.getLocalPath()+"templates/"+config.getTemplate()+"/alreadyDone.jsp";				
							String responseText = URLReader.getDefaultContent(url,null);				
							sb.append(responseText);
							return sb.toString();
						}						
	
					}
				
				sb.append("<div class=\"interviewTitle\">");
				sb.append(interview.getText());
				sb.append("</div>");
				sb.append("<div class=\"interview\">");
				sb.append("<form name=\"UserProfileForm\" method=\"post\" action=\""+formAction+"&plugin_page=saved\">");
				createFormLayout(sb, interview);			
				sb.append("</form>");
				sb.append("</div>");
				
				log.debug("Form layout: \n"+sb);
				
			}
			catch (JAXBException e) {				
				sb.append("<p>An XML error occured due to the following reason :</p>");

				sb.append("<p><b>");
				sb.append(e.getCause().getMessage());
				sb.append("</b></p>");

			}
			catch (IOException e) {
				
				sb.append("<p>An I/O error occured due to the following reason :</p>");

				sb.append("<p><b>");
				sb.append(e.getCause().getMessage());
				sb.append("</b></p>");
				
			}
			
		}
		
		return sb.toString();

	}

	public void createFormLayout(StringBuffer buffer, Interview interview) {
		
		Table table = new Table();
		
		table.setAlign(Align.CENTER);
		table.setWidth("90%");
		
		for (Question question : interview.getQuestions()) {
			
			TableRow row = new TableRow();

			TableRow lineRow = new TableRow();
			TableColumn lineColumn = new TableColumn();
			lineColumn.setColspan("2");
			lineColumn.setData("<hr size=\"1\">");			
			lineRow.addColumn(lineColumn);			
			table.addRow(lineRow);
			
			log.info(question.getText());
			
			TableColumn titleColumn = new TableColumn();
			titleColumn.setColspan("2");
			titleColumn.setData(question.getText());			
			row.addColumn(titleColumn);			
			table.addRow(row);
			table.addRow(lineRow);
			
			if(question.getType().equals(QuestionType.TEXT)) {
				
				TableRow answerRow = new TableRow();
				
				TableColumn textColumn = new TableColumn();
				textColumn.setColspan("2");
				textColumn.setData("<textarea rows=\"4\" cols=\"46\" class=\"answer\" name=\"q_"+question.getName()+"\"></textarea>");			
				
				answerRow.addColumn(textColumn);
				
				table.addRow(answerRow);				
				
			}
			else {
				
				int answerIndex = 0;
				
				for (Answer answer : question.getAnswers()) {
					
					TableRow answerRow = new TableRow();
					
					TableColumn leftColumn = new TableColumn();
					leftColumn.setStyleClass("answerText");
					leftColumn.setData(answer.getText());
					
					TableColumn rightColumn = new TableColumn();
					rightColumn.setStyleClass("answer");
					
					if (question.getType().equals(QuestionType.CHECKBOX)) {
						rightColumn.setData("<input type=\"checkbox\" name=\"q_"+question.getName()+"_"+answerIndex+"\" value=\""+answer.getName()+"\">");
					}
					else if (question.getType().equals(QuestionType.RADIO_CHOICE)) {
						rightColumn.setData("<input type= \"radio\" name=\"q_"+question.getName()+"\" value=\""+answer.getName()+"\">");
					}
					else if (question.getType().equals(QuestionType.TEXT)) {
						
					}
				
					answerRow.addColumn(leftColumn);
					answerRow.addColumn(rightColumn);
				
					table.addRow(answerRow);
					
					answerIndex++;
				}
			
			}
		}
		
		TableRow buttonRow = new TableRow();

		TableColumn buttonColumn = new TableColumn();
		buttonColumn.setColspan("2");
		buttonColumn.setAlign(Align.CENTER);
		buttonColumn.setData("<input type=\"submit\" value=\"Absenden\" class=\"interviewSubmit\" name=\"send\" id=\"send\">");					
		buttonRow.addColumn(buttonColumn);
		
		table.addRow(buttonRow);

		buffer.append(table.render());
		
	}

	void processInterview(Interview currentInterview) {
		
		Config config = (Config) DAOManager.getInstance().getConfigDAO().findByID(1);
		
		Interview interview = new Interview();		
		interview.setQuestions(new ArrayList<Question>());
		
        for (Enumeration<?> e = request.getParameterNames();e.hasMoreElements();) {
         
        	String paramName = (String)e.nextElement();

        	if (paramName.startsWith("q_")) {
        		
        		String questionName = paramName.substring(2);
        		
        		// Multiple choice
        		
        		if (questionName.indexOf("_") != -1) {
        			
        			int stopIndex = questionName.indexOf("_");        			
        			questionName = questionName.substring(0,stopIndex);        			
        			
        		}
        			
    			Question q = new Question();
    			q.setName(questionName);
    			q.setAnswers(new ArrayList<Answer>());
        			
    			Answer a = new Answer();
    			a.setName(request.getParameter(paramName));
    			
    			if (interview.getQuestions().contains(q)) {
    				
    				for (Question current : interview.getQuestions()) {
    				
    					if (current.getName().equals(q.getName())) {
    						q = current;
    						break;
    					}
    					
    				}
    				
    			}
    				
				for (Question originalQuestion : currentInterview.getQuestions()) {
					
					if (originalQuestion.getName().equals(questionName)) {
						
						q.setText(originalQuestion.getText());
						q.setType(originalQuestion.getType());

						if (originalQuestion.getType().equals(QuestionType.TEXT)) {
							a.setText(request.getParameter(paramName));
							a.setName(null);
							q.getAnswers().add(a);        							
						}
						else {

							for (Answer currentAnswer : originalQuestion.getAnswers()) {
    							
    							if (currentAnswer.equals(a)) {            								
    								a.setText(currentAnswer.getText());
    								q.getAnswers().add(a);
    							}
    							
    						}
							
						}
						
						
					}
					
				}
				
				if (!interview.getQuestions().contains(q))    				
					interview.getQuestions().add(q);
        		
        	}
        	
        }
        
		try {

			String reportLocation = config.getBasepath()+reportPath+"report_"+currentInterview.getName()+"_"+user.getName()+".xml";        
	        File report = new File(reportLocation);
	        FileOutputStream fos = new FileOutputStream(report);

			
			Marshaller m = (Marshaller)JAXBContext.newInstance(Interview.class).createMarshaller();
			m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );					
			m.marshal(interview, fos);
			fos.close();
			log.info("Wrote report to "+reportLocation);
			
		}
		catch (JAXBException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}
	
}
