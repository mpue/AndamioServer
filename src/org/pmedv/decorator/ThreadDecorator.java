package org.pmedv.decorator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.pmedv.context.AppContext;
import org.pmedv.pojos.Usergroup;
import org.pmedv.session.SessionCollector;
import org.pmedv.session.UserSession;
import org.springframework.context.ApplicationContext;

public class ThreadDecorator extends CustomizableTableDecorator {
	
	public ThreadDecorator() {
		super();
	}
	
	public String getDate() {
		org.pmedv.pojos.forums.Thread thread = (org.pmedv.pojos.forums.Thread) getCurrentRowObject();
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy kk:mm");
		return dateFormat.format(thread.getDate());
	}
	
	public String getUser() {
		org.pmedv.pojos.forums.Thread thread = (org.pmedv.pojos.forums.Thread) getCurrentRowObject();
		return thread.getUser().getName();	
	}
	
	public String getDeleteLink() {
		org.pmedv.pojos.forums.Thread thread = (org.pmedv.pojos.forums.Thread) getCurrentRowObject();
		return "<a href=\"/"+myConfig.getLocalPath()+"admin/ForumShowAction.do?do=deleteThread&thread_id="+thread.getId()+"\">delete</a>";	
		
	}
	
}
