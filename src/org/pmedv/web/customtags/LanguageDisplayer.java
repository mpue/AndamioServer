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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.pmedv.cms.common.CMSProperties;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.core.html.Link;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.Node;
import org.pmedv.web.ServerUtil;

/**
 * This class provides a basic displayer for a language chooser bar.
 * 
 * @author Matthias Pueski
 * 
 */
public class LanguageDisplayer extends TagSupport {

	private static final long serialVersionUID = 2868410646273359205L;

	private String languages;

	public LanguageDisplayer() {
		super();
	}

	public int doStartTag() throws JspException {

		Config config = (Config) DAOManager.getInstance().getConfigDAO().findByID(1L);
		String baseUrl = CMSProperties.getInstance().getAppProps().getProperty("protocol")+
				"://" + ServerUtil.getHostUrl(pageContext.getRequest()) + "/" 
				+ config.getLocalPath() + "LocaleAction.do?do=setLocale&locale=";

		Node currentNode = (Node) pageContext.getSession().getAttribute("node");

		StringBuffer content = new StringBuffer();

		if (languages == null)
			return EVAL_BODY_INCLUDE;

		String[] langs = languages.split(",");

		content.append("<span class=\"languageBar\">");

		for (int i = 0; i < langs.length; i++) {

			Link link = new Link();

			String path = null;

			if (currentNode.isHidden())
				path = currentNode.getPath().substring(0, currentNode.getPath().lastIndexOf("_"));
			else
				path = currentNode.getPath();

			link.setHref(baseUrl + langs[i] + "&path=" + path);
			link.setData(langs[i].toUpperCase());
			link.setStyleClass("language");
			link.setTarget("_self");

			content.append(link.render());

			if (i < langs.length - 1)
				content.append("|");

		}

		content.append("</span>");

		try {
			pageContext.getOut().print(content.toString());
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return EVAL_BODY_INCLUDE;
	}

	/**
	 * @return the languages
	 */
	public String getLanguages() {
		return languages;
	}

	/**
	 * @param languages the languages to set
	 */
	public void setLanguages(String languages) {
		this.languages = languages;
	}

}
