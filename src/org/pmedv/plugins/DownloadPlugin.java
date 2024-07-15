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
package org.pmedv.plugins;

import java.io.File;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.pmedv.cms.daos.DAOManager;
import org.pmedv.core.util.URLReader;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.Download;
import org.pmedv.util.StringUtil;

/**
 * <p>
 * This is the download plugin. The download plugin is dynamically loaded and
 * implements the download functionality. The system can consist as many
 * downloads as the user wants to have.
 * 
 * @author Matthias Pueski
 * @since version 1.98
 * @version 12.6.2007
 * 
 */
public class DownloadPlugin extends AbstractPlugin implements IPlugin, Serializable {

	private static final long serialVersionUID = 8438727720201951227L;

	public DownloadPlugin() {
		super();
		pluginID = "DownloadPlugin";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.pmedv.plugins.IPlugin#getContent()
	 */
	@SuppressWarnings("rawtypes")
	public String getContent() {

		List downloads = DAOManager.getInstance().getDownloadDAO().findAllItems();
		Config config = (Config) DAOManager.getInstance().getConfigDAO().findByID(1);
		String url = this.protocol + "://"+request.getServerName()+":" + this.request.getServerPort() + "/" + config.getLocalPath() + "templates/"
				+ config.getTemplate() + "/download.jsp";
		String formLayout = URLReader.getDefaultContent(url, null);

		StringBuffer s = new StringBuffer();

		s.append("<table class=\"downloads\">");
		s.append("\n");

		boolean odd = true;

		s.append("<tr class=\"downloadtitle\">");
		s.append("<td class=\"downloadname\">");
		s.append(this.resourceBundle.getString("downloadname"));
		s.append("</td>");
		s.append("<td class=\"downloadsize\">");
		s.append(this.resourceBundle.getString("downloadsize"));
		s.append("</td>");
		s.append("<td class=\"downloadsize\">");
		s.append("&nbsp;");
		s.append("</td>");
		s.append("</tr>");

		for (Iterator downloadIterator = downloads.iterator(); downloadIterator.hasNext();) {
			Download download = (Download) downloadIterator.next();

			String filePath = config.getBasepath() + config.getDownloadpath() + download.getFilename();
			File f = new File(filePath);

			if (f != null) {
				if (odd)
					s.append("<tr class=\"odd\">");
				else
					s.append("<tr class=\"even\">");
				s.append("\n");
				s.append("<td class=\"downloadname\">");
				s.append("<a href=\""+this.protocol + "://"+request.getServerName()+":" + this.request.getServerPort() + "/" + config.getLocalPath()+"fileDownload.do?download_id=");
				s.append(download.getId());
				s.append("\" target=\"_self\">");
				s.append(download.getName());
				s.append("</a>");
				s.append("</td>");
				s.append("<td class=\"size\">");

				if (f.length() > 1024L) {
					s.append(f.length() / 1024L + " kBytes");
				}
				else if (f.length() > 1048576L) {
					s.append(f.length() / 1024L + " MBytes");
				}
				s.append("</td>");
				s.append("<td class=\"hits\">");
				s.append(download.getHits() + " " + this.resourceBundle.getString("downloaded"));
				s.append("</td>");
				s.append("\n");
				s.append("</tr>");
				s.append("\n");
				if (odd)
					s.append("<tr class=\"odd\">");
				else
					s.append("<tr class=\"even\">");
				s.append("\n");
				s.append("<td class=\"downloaddescription\" colspan=\"2\">");
				s.append(download.getDescription());
				s.append("</td>");
				s.append("\n");
				s.append("</tr>");
				s.append("\n");
			}

			odd = !odd;
		}

		s.append("</table>");
		s.append("\n");

		formLayout = StringUtil.replace(formLayout, "##DOWNLOADS##", s.toString());

		return formLayout;
	}

}
