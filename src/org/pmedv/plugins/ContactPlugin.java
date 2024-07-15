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
/**
 * 
 */
package org.pmedv.plugins;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.cms.common.Params;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.core.util.URLReader;
import org.pmedv.pojos.Config;

/**
 * @author Matthias Pueski
 *
 */
public class ContactPlugin extends AbstractPlugin implements IPlugin, Serializable {

	private static final long serialVersionUID = 5919329564763481601L;

	protected static Log log = LogFactory.getLog(ContactPlugin.class);
	
	public ContactPlugin() {
		super();
		pluginID = "ContactPlugin";
		paramDescriptors.put("plugin_contact_users", resourceBundle.getString("plugin.contact.field.users"));
		paramDescriptors.put("plugin_pagetitle", resourceBundle.getString("plugin.field.pagetitle"));
	}

	private String pageTitle;
	
	
	
	/* (non-Javadoc)
	 * @see org.pmedv.plugins.IPlugin#getContent()
	 */
	public String getContent() {
		
		try {
			pageTitle = paramMap.get("plugin_pagetitle");
		}
		catch (NullPointerException n) {
			// silently ignore
		}
	
		Config config = (Config) DAOManager.getInstance().getConfigDAO().findByID(1);
		
		String contact_id = paramMap.get("plugin_contact_users");
		String url = protocol+"://localhost:"+request.getServerPort()+"/"+config.getLocalPath()+"templates/"+config.getTemplate()+"/contact.jsp";
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("<input type=\"hidden\" name=\"contact_users\" value=\"");
		sb.append(contact_id);
		sb.append("\"");		
		sb.append(" id=\""+Params.CONTACT_USERS+"\">");
		
		log.info("Fetching contact form :"+ url);
		
		setTitle(pageTitle);
		
		String contactFormContent = URLReader.getDefaultContent(url,null);
		
		if (contactFormContent != null)
			sb.append(contactFormContent);
		else {
			sb.append(resourceBundle.getString("plugin.contact.message"));
		}
		
		return sb.toString();
	}


 
}
