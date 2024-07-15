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
package org.pmedv.actions;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONSerializer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.pmedv.context.AppContext;
import org.pmedv.session.ChatUserList;
import org.springframework.context.ApplicationContext;

public class ChatAction extends DispatchAction {

	private static final ApplicationContext ctx = AppContext.getApplicationContext();	
	private static final Log log = LogFactory.getLog(ChatAction.class); 
	
	public ActionForward getUsers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ChatUserList userlist = (ChatUserList)ctx.getBean("chatUserList");
		
		String jsonList = JSONSerializer.toJSON(userlist).toString();
		
		PrintWriter out = response.getWriter();
		out.write(jsonList);
		out.flush();
		
		return null;
	}

}
