package org.pmedv.util;

import java.util.ArrayList;
import java.util.Iterator;

import org.pmedv.beans.objects.UserBean;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.context.AppContext;
import org.pmedv.pojos.User;
import org.pmedv.services.UserService;
import org.pmedv.session.SessionCollector;
import org.pmedv.session.UserSession;
import org.springframework.context.ApplicationContext;


public class SessionUtil {

	/**
	 * gets a list of all users that are online
	 * 
	 * @return an <code>ArrayList</code> of online users.
	 */
	public static ArrayList<UserBean> getOnlineUsers() {

		final ApplicationContext ctx = AppContext.getApplicationContext();
		final SessionCollector collector = (SessionCollector)ctx.getBean("sessionCollector");
		final UserService userService = (UserService)ctx.getBean("userService");
		
		ArrayList<UserBean> users = new ArrayList<UserBean>();

		for (Iterator<UserSession> sessionIterator = collector.getSessions().values().iterator();sessionIterator.hasNext();) {
			
			UserSession session = (UserSession) sessionIterator.next();
			UserBean u = new UserBean();
			
			User user = (User)DAOManager.getInstance().getUserDAO().findByName(session.getLogin());
			
			u.setName(user.getName());
			
			users.add(u);
		}
		
		return users;
		
	}

	
}
