package org.pmedv.decorator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.pmedv.cms.common.CMSProperties;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.Avatar;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.User;
import org.pmedv.pojos.Usergroup;
import org.pmedv.pojos.forums.Attachment;
import org.pmedv.pojos.forums.Posting;
import org.pmedv.web.ServerUtil;

/**
 * This class decorates the postings inside a thread
 * 
 * @author mpue
 * @version 1.2 
 * @since pmCMS 2.0
 *
 */
public class PostingDecorator extends CustomizableTableDecorator {

	private static final String protocol = CMSProperties.getInstance().getAppProps().getProperty("protocol");
	private Config config;	
	private static final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy kk:mm");
	
	public PostingDecorator() {
		super();
		config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1L);
	}
	
	/**
	 * Gets the posting date 
	 * 
	 * @return the pre-formatted date
	 */
	public String getDate() {
		Posting posting = (Posting)getCurrentRowObject();
		return dateFormat.format(posting.getDate());
	}
	
	/**
	 * Creates the decoration for the user info on the left side including avatar image
	 * 
	 * @return the html string containing the user information
	 */
	public String getUserInfo() {
		
		StringBuffer href = new StringBuffer();
		String avatarImage = "";
		
		Posting posting = (Posting)getCurrentRowObject();
		
		for (Avatar currentAvatar : posting.getUser().getAvatars()) {
			
			if (currentAvatar.isDefaultImage()) {				
				avatarImage = protocol+"://"+ServerUtil.getHostUrl(getPageContext().getRequest()) + "/"+ 
					config.getLocalPath()+"users/"+posting.getUser().getName()+"/avatars/"+currentAvatar.getFilename();		
				break;
			}
		}
		
		href.append("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" width=\"100%\">");

		href.append("<tr>");
		href.append("<td>");
		href.append("<p  class=\"userName\">");
	    href.append(posting.getUser().getName());
	    href.append("</p>");   
		href.append("</td>");
	    href.append("</tr>");

	    href.append("<tr>");
		href.append("<td>");	    
	    href.append("<p  class=\"userTitle\">");
	    href.append(posting.getUser().getTitle());
	    href.append("</p>");   
		href.append("</td>");
	    href.append("</tr>");

	    if (avatarImage.length() > 0) {
		    href.append("<tr>");
			href.append("<td align=\"center\">");	    	    
		    href.append("<img src=\"" + avatarImage +"\" border=\"0\" align=\"center\" valign=\"middle\">");
			href.append("</td>");
		    href.append("</tr>");	    	
	    }

	    href.append("<tr>");
		href.append("<td>");	    
	    href.append("<p  class=\"userInfo\">");
	    href.append("Posts : "+posting.getUser().getRanking());
	    href.append("</p>");   
		href.append("</td>");
	    href.append("</tr>");
	    
	    if (posting.getUser().getJoinDate() != null) {
		    href.append("<tr>");
			href.append("<td>");	    
		    href.append("<p  class=\"joinDate\">");
		    href.append("Registered : " + dateFormat.format(posting.getUser().getJoinDate()));
		    href.append("</p>");   
			href.append("</td>");
		    href.append("</tr>");
	    }
	    
	    href.append("</table>");	    
	    
		return href.toString();
		
	}
	
	/**
	 * This method generates the decorated posting
	 * 
	 * @return the posting text
	 */
	public String getPosting () {

		Usergroup adminGroup = new Usergroup();
		adminGroup.setName("admin");
		
		String login = (String)getPageContext().getSession().getAttribute("login");		
		User user = (User)DAOManager.getInstance().getUserDAO().findByName(login);
		
		StringBuffer href = new StringBuffer();
				
		Posting posting = (Posting)getCurrentRowObject();
		
		// display posting with title and date
		
		href.append("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
		href.append("<tr>");
		href.append("<td>");
		href.append("<p class=\"postingTitle\">");		
		href.append(posting.getTitle());		
		href.append("</p>");
		href.append("</td>");
		href.append("<td>");
		href.append("<p class=\"postingDate\">");
		href.append("&nbsp;("+dateFormat.format(posting.getDate())+")");
		href.append("</p>");
		href.append("</td>");

		// show edit and delete buttons depending on the current user 
		
		if (login != null && (posting.getUser().getName().equals(login) || user.getGroups().contains(adminGroup))) {
			href.append("<td>");
			href.append("<p class=\"editPosting\">");
			href.append("<input type=\"button\" class=\"editPosting\" name=\"editPosting\" value=\"Edit\" onclick=\"document.location.href='Mainpage.do?do=editPosting&id="+posting.getId()+"'\"/>");
			href.append("</p>");
			href.append("</td>");
			
			if (user.getGroups().contains(adminGroup)) {
				href.append("<td>");
				href.append("<p class=\"deletePosting\">");			
				href.append("<input type=\"button\" class=\"deletePosting\" name=\"editPosting\" value=\"Delete\" onclick=\"document.location.href='Mainpage.do?do=deletePosting&id="+posting.getId()+"'\"/>");
				href.append("</p>");
				href.append("</td>");	
			}
	
		}
		
		href.append("</table>");
		href.append("<p class=\"postingText\">");
		href.append(posting.getPostingtext());
		href.append("</p>");
		
		if (posting.getLastChange() != null) {
			href.append("<p class=\"lastChange\">");
			href.append("Last change "+dateFormat.format(posting.getLastChange())+" by "+posting.getLastChangeBy());
			href.append("</p>");
		}
		
		href.append("<p>Attachments : </p>");
		href.append("<ul>");
		
		for (Attachment a : posting.getAttachments()) {
			href.append("<li><a href=\"Mainpage.do?do=getAttachment&id="+a.getId()+"\">"+a.getName()+"</a></li>");
		}
		
		href.append("</ul>");
		
		return href.toString();
		
	}
	
}
