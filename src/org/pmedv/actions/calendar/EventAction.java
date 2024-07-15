/**

	WeberknechtCMS - Open Source Content Management
	Written and maintained by Matthias Pueski 
	
	Copyright (c) 2003-2011 Matthias Pueski
	
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
package org.pmedv.actions.calendar;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.mail.AuthenticationFailedException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONSerializer;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.cms.daos.EventDAO;
import org.pmedv.cms.daos.ParticipantDAO;
import org.pmedv.context.AppContext;
import org.pmedv.mail.SMTPMailer;
import org.pmedv.mail.SMTPMailer.MessageType;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.calendar.DateBean;
import org.pmedv.pojos.calendar.Event;
import org.pmedv.pojos.calendar.Participant;
import org.pmedv.util.RequestUtil;


public class EventAction extends DispatchAction {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");	
	
	private EventDAO eventDAO;
	private ParticipantDAO participantDAO;
	
	public EventAction() {
		eventDAO = (EventDAO)AppContext.getApplicationContext().getBean("eventDAO");
		participantDAO = (ParticipantDAO)AppContext.getApplicationContext().getBean("participantDAO");
	}
	
	public ActionForward getEvents(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
				
		List<Object> eventList = (List<Object>) eventDAO.findAllItems();
		
		if (eventList == null ) {
			eventList = new ArrayList<Object>();						
		}
		try {
			PrintWriter out = response.getWriter();
			response.setContentType("text/plain");
			out.print(JSONSerializer.toJSON(eventList).toString());
		}
		catch (IOException e) {
			e.printStackTrace();
		}			
		
		return null;
	}
	
	public ActionForward getEvent(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		Event event = (Event)eventDAO.findByID(Long.valueOf(request.getParameter("id")));
		
		try {
			PrintWriter out = response.getWriter();
			response.setContentType("text/plain");
			out.print(JSONSerializer.toJSON(event).toString());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public ActionForward viewEvent(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {		
		Event event = (Event)eventDAO.findByID(Long.valueOf(request.getParameter("id")));
		request.getSession().setAttribute("event", event);		
		request.getSession().setAttribute("dates", event.getPossibleDates());
		request.getSession().setAttribute("participants", event.getParticipants());
		return mapping.findForward("viewEvent");
	}

	public ActionForward deleteEvent(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {		
		Event event = (Event)eventDAO.findByID(Long.valueOf(request.getParameter("id")));
		eventDAO.delete(event);
		return null;
	}	
	
	public ActionForward participate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		RequestUtil.dumpParams(request);
		
		Long id = Long.valueOf(request.getParameter("id"));		
		String participant = request.getParameter("participant");
		String dates = request.getParameter("dates");		
		String[] dateArray = dates.split(",");
		
		Participant p = new Participant();
		p.setLastname(participant);		
		p = eventDAO.addParticipant(id, p);
		
		try {
			for (int i = 0; i < dateArray.length;i++) {
				DateBean d = new DateBean();
				d.setDate(sdf.parse(dateArray[i]));
				participantDAO.addDateBean(p.getId(),d);	
			}
		}
		catch (ParseException e) {
			log.error("could not parse a date.");
		}
		
		return null;
	}
	
	public ActionForward sendInvitations(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		EventDAO eventDAO = (EventDAO)AppContext.getApplicationContext().getBean("eventDAO");
		Event event = (Event)eventDAO.findByID(Long.valueOf(request.getParameter("id")));
		String addresses = request.getParameter("addresses");
		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1L);
		
		boolean error = false;
		String message = "Invitations have been successfully sent.";
		
		if (addresses != null && config != null) {
			
			String[] addressList = addresses.split(",");
			
			for (int i=0; i < addressList.length;i++) {

				SMTPMailer mailer = new SMTPMailer(config.getSmtphost(),config.getUsername(),config.getPassword());
				
				mailer.setFrom(config.getFromadress());
				mailer.setSubject(event.getTitle());
				mailer.setText("Event test :");
				
				try {
					mailer.createMessage(MessageType.TEXT_HTML);
					mailer.setTo(addressList[i]);
					mailer.addToRecipient(addressList[i]);					
					mailer.sendMessage();				
				} 
				catch (AuthenticationFailedException a) {
					error = true;
					message = "Authentication failed.";
				}
				catch (Exception e) {
					error = true;
					if (e.getMessage().contains("Unknown SMTP host"))
						message = "Could not send message. Reason : Unknown SMTP host.";
					else
						message = "Could not send message for an unknown reason.";					
				}
				
			}
			
		}
		
		PrintWriter out;
		
		try {
			out = response.getWriter();
			if (error) {
				out.print("{\"success\":false,\"message\":\""+message+".\"}");					
			}
			else {
				out.print("{\"success\":true,\"message\":\""+message+".\"}");
			}
			out.flush();
		} 
		catch (IOException e) {
			log.error("Could not write to servlet output stream.");
		}
		
		return null;
	}
	
	public ActionForward createEvent(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		EventDAO eventDAO = (EventDAO)AppContext.getApplicationContext().getBean("eventDAO");
		
		RequestUtil.dumpParams(request);

		ArrayList<Date> selectedDates = new ArrayList<Date>();		
		String dates = request.getParameter("dates");		
		String[] datesArray = dates.split(",");

		for (int i=0; i < datesArray.length;i++) {

			if (datesArray[i].contains("->")) { // range found
				
				String[] range = datesArray[i].split("->");
				
				try {
					Date start = sdf.parse(range[0].trim());
					Date end   = sdf.parse(range[1].trim());
					
					long interval = 1000 * 60 * 60 * 24; // 1 hour in millis
					long endtime = end.getTime();
					long curTime = start.getTime();
					while (curTime <= endtime) {
						selectedDates.add(new Date(curTime));
						curTime += interval;
					}
					
				}
				catch (ParseException e) {
					log.error("could not parse date."+datesArray[i]);
				}
				
			}
			else { // single date
				try {
					Date d = sdf.parse(datesArray[i]);
					selectedDates.add(d);
				}
				catch (ParseException e) {
					log.error("could not parse date."+datesArray[i]);
				}
			}
			
		}
		
		Collections.sort(selectedDates);

		Event e = new Event();
		
		e.setDescription(request.getParameter("description"));
		e.setLocation(request.getParameter("location"));
		e.setTitle(request.getParameter("title"));
		e.setUsername((String)request.getSession().getAttribute("login"));
		
		e = (Event)eventDAO.createAndStore(e);
		
		if (e != null) {
			for (Date d : selectedDates) {			
				DateBean db = new DateBean();
				db.setDate(d);
				eventDAO.addDateBean(e.getId(), db);	
			}
			
			try {
				PrintWriter out = response.getWriter();
				response.setContentType("text/plain");
				out.print(JSONSerializer.toJSON(e).toString());
			}
			catch (IOException e1) {
				log.error("could not write event to servlet output stream.");
			}					
			
		}
		else {
			log.error("Could not create event.");
		}
		
		return null;
		
	}
	
	
}
