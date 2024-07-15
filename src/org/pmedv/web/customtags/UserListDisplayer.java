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
package org.pmedv.web.customtags;

import java.io.IOException;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.pmedv.cms.daos.DAOManager;
import org.pmedv.context.AppContext;
import org.pmedv.pojos.User;
import org.pmedv.pojos.Usergroup;
import org.pmedv.session.SessionCollector;
import org.pmedv.session.UserSession;
import org.springframework.context.ApplicationContext;

public class UserListDisplayer extends TagSupport {

	private static final long serialVersionUID = -6712132811638570683L;
	
	private static ResourceBundle resources = ResourceBundle.getBundle("MessageResources");

	private static final ApplicationContext ctx = AppContext.getApplicationContext();
	private static final SessionCollector collector = (SessionCollector)ctx.getBean("sessionCollector");
	
	@SuppressWarnings("unchecked")
	@Override
	public int doStartTag() throws JspException {
				
		StringBuffer userList = new StringBuffer();
		
		for (Iterator sessionIterator = collector.getSessions().values().iterator();sessionIterator.hasNext();) {
		
			UserSession session = (UserSession) sessionIterator.next();

			User user = (User)DAOManager.getInstance().getUserDAO().findByName(session.getLogin());
			boolean admin = false;
			
			for (Iterator it = user.getGroups().iterator();it.hasNext();) {
				Usergroup usergroup = (Usergroup) it.next();
				
				if (usergroup.getName().equalsIgnoreCase("admin")) 
					admin = true;				
			}
				
			if (admin)
				userList.append("<b>"+session.getLogin()+"</b>");
			else
				userList.append(session.getLogin());
			
			long inactiveTime = ((System.currentTimeMillis() - session.getSession().getLastAccessedTime()) / 1000);
			
			userList.append("("+(session.getSession().getMaxInactiveInterval()-inactiveTime)/60);
			
			String processName = (String)session.getSession().getAttribute("processname"); 
			
			if (processName == null)
				processName = resources.getString("menu.title");
			
			userList.append(" | "+processName+")");
			userList.append(", ");
			
		}
		
		try {
			pageContext.getOut().print(userList.toString());
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_BODY_INCLUDE;
		
	}
	
	public void setPageContext(PageContext pageContext) {
		this.pageContext = pageContext;
	}
	
}
