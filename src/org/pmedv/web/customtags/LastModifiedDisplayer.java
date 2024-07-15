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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.Content;
import org.pmedv.pojos.User;

/**
 * Displays information about the last modification of a content.
 * 
 * @author Matthias Pueski
 * 
 */
public class LastModifiedDisplayer extends TagSupport {

	private static final long serialVersionUID = -8472332381271286682L;

	private static ResourceBundle resources = ResourceBundle.getBundle("MessageResources");
	private static final Log log = LogFactory.getLog(LastModifiedDisplayer.class);

	private boolean showDate;
	private boolean showUser;

	public LastModifiedDisplayer() {
		super();
	}

	public int doStartTag() throws JspException {

		StringBuffer lastModified = new StringBuffer();

		Long contentId = (Long) pageContext.getSession().getAttribute("contentId");

		if (contentId == null)
			return EVAL_BODY_INCLUDE;

		Content content = (Content) DAOManager.getInstance().getContentDAO().findByID(contentId);

		if (content != null) {

			if (content.getLastmodified() != null) {

				DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy kk:mm");
				String modDate = dateFormat.format(content.getLastmodified());

				String fullname = null;

				if (content.getLastmodifiedby() != null) {

					User user = (User) DAOManager.getInstance().getUserDAO().findByName(content.getLastmodifiedby());

					if (user != null) {

						if (user.getFirstName() != null && user.getLastName() != null)
							fullname = user.getFirstName() + " " + user.getLastName();

					}

				}

				lastModified.append(resources.getString("lastmodified.by"));
				lastModified.append(" : ");
				lastModified.append(modDate);

				if (fullname != null) {
					lastModified.append(" ");
					lastModified.append(resources.getString("from"));
					lastModified.append(" ");
					lastModified.append(fullname);
				}

			}

		}

		try {
			pageContext.getOut().print(lastModified.toString());
		}
		catch (IOException e) {
			log.error("Could not write to servlet output stream.");
		}

		return EVAL_BODY_INCLUDE;
	}

	public void setPageContext(PageContext pageContext) {
		this.pageContext = pageContext;
	}

	/**
	 * @return the showDate
	 */
	public boolean isShowDate() {
		return showDate;
	}

	/**
	 * @param showDate the showDate to set
	 */
	public void setShowDate(boolean showDate) {
		this.showDate = showDate;
	}

	/**
	 * @return the showUser
	 */
	public boolean isShowUser() {
		return showUser;
	}

	/**
	 * @param showUser the showUser to set
	 */
	public void setShowUser(boolean showUser) {
		this.showUser = showUser;
	}

}
