package org.pmedv.actions.faq;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONSerializer;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.pmedv.cms.daos.FaqDAO;
import org.pmedv.context.AppContext;
import org.pmedv.pojos.plugins.Faq;

public class FaqAction extends DispatchAction {

	private FaqDAO faqDAO;

	public FaqAction() {
		faqDAO = (FaqDAO) AppContext.getApplicationContext().getBean("faqDAO");
	}

	public ActionForward getFaqs(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		List<?> faqList = faqDAO.findFaqsByStudyId("12345");

		if (faqList == null) {
			faqList = new ArrayList<Object>();
		}
		try {
			PrintWriter out = response.getWriter();
			response.setContentType("text/plain");
			out.print(JSONSerializer.toJSON(faqList).toString());
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public ActionForward createFaq(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		Faq f = new Faq();

		f.setAnswer(request.getParameter("answer"));
		f.setQuestion(request.getParameter("question"));
		f.setStudyId("12345");

		f = (Faq) faqDAO.createAndStore(f);

		if (f != null) {
			try {
				PrintWriter out = response.getWriter();
				response.setContentType("text/plain");
				out.print(JSONSerializer.toJSON(f).toString());
			}
			catch (IOException e1) {
				log.error("could not write faq to servlet output stream.");
			}

		}
		else {
			log.error("Could not create faq.");
		}

		return null;

	}

	public ActionForward deleteFaq(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Faq faq = (Faq) faqDAO.findByID(Long.valueOf(request.getParameter("id")));
		faqDAO.delete(faq);
		return null;
	}

	// public ActionForward getEvent(ActionMapping mapping, ActionForm form,
	// HttpServletRequest request, HttpServletResponse response) {
	//
	// Event event = (Event)eventDAO.findByID(Long.valueOf(request.getParameter("id")));
	//
	// try {
	// PrintWriter out = response.getWriter();
	// response.setContentType("text/plain");
	// out.print(JSONSerializer.toJSON(event).toString());
	// }
	// catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// return null;
	// }
	//
	// public ActionForward viewEvent(ActionMapping mapping, ActionForm form,
	// HttpServletRequest request, HttpServletResponse response) {
	// Event event = (Event)eventDAO.findByID(Long.valueOf(request.getParameter("id")));
	// request.getSession().setAttribute("event", event);
	// request.getSession().setAttribute("dates", event.getPossibleDates());
	// request.getSession().setAttribute("participants", event.getParticipants());
	// return mapping.findForward("viewEvent");
	// }
	//
	//
	// public ActionForward participate(ActionMapping mapping, ActionForm form,
	// HttpServletRequest request, HttpServletResponse response) {
	//
	// RequestUtil.dumpParams(request);
	//
	// Long id = Long.valueOf(request.getParameter("id"));
	// String participant = request.getParameter("participant");
	// String dates = request.getParameter("dates");
	// String[] dateArray = dates.split(",");
	//
	// Participant p = new Participant();
	// p.setLastname(participant);
	// p = eventDAO.addParticipant(id, p);
	//
	// try {
	// for (int i = 0; i < dateArray.length;i++) {
	// DateBean d = new DateBean();
	// d.setDate(sdf.parse(dateArray[i]));
	// participantDAO.addDateBean(p.getId(),d);
	// }
	// }
	// catch (ParseException e) {
	// log.error("could not parse a date.");
	// }
	//
	// return null;
	// }
	//
	// public ActionForward sendInvitations(ActionMapping mapping, ActionForm form,
	// HttpServletRequest request, HttpServletResponse response) {
	//
	// EventDAO eventDAO = (EventDAO)AppContext.getApplicationContext().getBean("eventDAO");
	// Event event = (Event)eventDAO.findByID(Long.valueOf(request.getParameter("id")));
	// String addresses = request.getParameter("addresses");
	// Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1L);
	//
	// boolean error = false;
	// String message = "Invitations have been successfully sent.";
	//
	// if (addresses != null && config != null) {
	//
	// String[] addressList = addresses.split(",");
	//
	// for (int i=0; i < addressList.length;i++) {
	//
	// SMTPMailer mailer = new
	// SMTPMailer(config.getSmtphost(),config.getUsername(),config.getPassword());
	//
	// mailer.setFrom(config.getFromadress());
	// mailer.setSubject(event.getTitle());
	// mailer.setText("Event test :");
	//
	// try {
	// mailer.createMessage(MessageType.TEXT_HTML);
	// mailer.setTo(addressList[i]);
	// mailer.addToRecipient(addressList[i]);
	// mailer.sendMessage();
	// }
	// catch (AuthenticationFailedException a) {
	// error = true;
	// message = "Authentication failed.";
	// }
	// catch (Exception e) {
	// error = true;
	// if (e.getMessage().contains("Unknown SMTP host"))
	// message = "Could not send message. Reason : Unknown SMTP host.";
	// else
	// message = "Could not send message for an unknown reason.";
	// }
	//
	// }
	//
	// }
	//
	// PrintWriter out;
	//
	// try {
	// out = response.getWriter();
	// if (error) {
	// out.print("{\"success\":false,\"message\":\""+message+".\"}");
	// }
	// else {
	// out.print("{\"success\":true,\"message\":\""+message+".\"}");
	// }
	// out.flush();
	// }
	// catch (IOException e) {
	// log.error("Could not write to servlet output stream.");
	// }
	//
	// return null;
	// }
	//
	// public ActionForward createEvent(ActionMapping mapping, ActionForm form,
	// HttpServletRequest request, HttpServletResponse response) {
	//
	// EventDAO eventDAO = (EventDAO)AppContext.getApplicationContext().getBean("eventDAO");
	//
	// RequestUtil.dumpParams(request);
	//
	// ArrayList<Date> selectedDates = new ArrayList<Date>();
	// String dates = request.getParameter("dates");
	// String[] datesArray = dates.split(",");
	//
	// for (int i=0; i < datesArray.length;i++) {
	//
	// if (datesArray[i].contains("->")) { // range found
	//
	// String[] range = datesArray[i].split("->");
	//
	// try {
	// Date start = sdf.parse(range[0].trim());
	// Date end = sdf.parse(range[1].trim());
	//
	// long interval = 1000 * 60 * 60 * 24; // 1 hour in millis
	// long endtime = end.getTime();
	// long curTime = start.getTime();
	// while (curTime <= endtime) {
	// selectedDates.add(new Date(curTime));
	// curTime += interval;
	// }
	//
	// }
	// catch (ParseException e) {
	// log.error("could not parse date."+datesArray[i]);
	// }
	//
	// }
	// else { // single date
	// try {
	// Date d = sdf.parse(datesArray[i]);
	// selectedDates.add(d);
	// }
	// catch (ParseException e) {
	// log.error("could not parse date."+datesArray[i]);
	// }
	// }
	//
	// }
	//
	// Collections.sort(selectedDates);
	//
	// Event e = new Event();
	//
	// e.setDescription(request.getParameter("description"));
	// e.setLocation(request.getParameter("location"));
	// e.setTitle(request.getParameter("title"));
	// e.setUsername((String)request.getSession().getAttribute("login"));
	//
	// e = (Event)eventDAO.createAndStore(e);
	//
	// if (e != null) {
	// for (Date d : selectedDates) {
	// DateBean db = new DateBean();
	// db.setDate(d);
	// eventDAO.addDateBean(e.getId(), db);
	// }
	//
	// try {
	// PrintWriter out = response.getWriter();
	// response.setContentType("text/plain");
	// out.print(JSONSerializer.toJSON(e).toString());
	// }
	// catch (IOException e1) {
	// log.error("could not write event to servlet output stream.");
	// }
	//
	// }
	// else {
	// log.error("Could not create event.");
	// }
	//
	// return null;
	//
	// }

}
