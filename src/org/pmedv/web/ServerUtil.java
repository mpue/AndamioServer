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
package org.pmedv.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.Content;
import org.pmedv.pojos.SiteRequest;

/**
 * Static utilility containing some covenience methods for request
 * processing purposes.
 * 
 * @author Matthias Pueski
 *
 */
public class ServerUtil {

	private static final Log log = LogFactory.getLog(ServerUtil.class);

	public static String getRequestUrl(HttpServletRequest req) {
	    String reqUrl = req.getRequestURL().toString();
	    String queryString = req.getQueryString();   // d=789
	    if (queryString != null) {
	        reqUrl += "?"+queryString;
	    }
	    return reqUrl;
	}
	
	/**
	 * Get the caller URL for this request
	 * 
	 * @param request The request, to get the URL for
	 * 
	 * @return the host URL of the request
	 */
	public static String getHostUrl(ServletRequest request) {

		StringBuffer hostUrl = new StringBuffer();

		hostUrl.append(request.getServerName());

		// we only need to append the according port if it is not port 80
		if (request.getServerPort() != 80)
			hostUrl.append(":" + request.getServerPort());

		log.debug("Host url is : " + hostUrl);

		return hostUrl.toString();

	}

	/**
	 * Sets the template of a domain specified by the request domain
	 * 
	 * @param request
	 */
	public static String getDomainTemplate(HttpServletRequest request) {
		
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("domainTemplates.properties");

		if (is != null) {

			Properties p = new Properties();

			try {
				p.load(is);

				String template = p.getProperty(request.getServerName());

				if (template != null) {
					log.info("setting template for domain " + request.getServerName() + " to " + template);
					return template;
				}

			}
			catch (IOException e) {
				log.info("could not load domainLanguages.properties, proceeding with default template for all domains.");
			}

		}

		return null;
	}

	/**
	 * Sets the language of a domain specified by the request domain
	 * 
	 * @param request
	 */
	public static void setDomainLanguage(HttpServletRequest request) {

		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("domainLanguages.properties");

		if (is != null) {

			Properties p = new Properties();

			try {
				p.load(is);

				String language = p.getProperty(request.getServerName());

				if (language != null) {

					Locale l = new Locale(language);

					request.getSession().setAttribute("currentLocale", l);
					log.info("setting language for domain " + request.getServerName() + " to " + l);

				}

			}
			catch (IOException e) {
				log.info("could not load domainLanguages.properties, proceeding with default language for all domains.");
			}

		}
	}

	/**
	 * Determines if the systems multi domain capability is enabled or not.
	 * 
	 * @param config
	 * 
	 * @return true if multiple domains are supported, false if not
	 */
	public static boolean isMultiDomainEnabled(Config config) {

		if (config == null)
			throw new IllegalArgumentException("Config must not be null");
		
		String propertiesLoc = config.getBasepath() + "WEB-INF/application.properties";
		log.info("Loading application.properties from " + propertiesLoc);
		FileInputStream is = null;
		try {
			is = new FileInputStream(new File(propertiesLoc));
		}
		catch (FileNotFoundException e1) {
			log.info("could not load application.properties, proceeding with default configuration for all domains.");
		}
		if (is != null) {

			Properties p = new Properties();

			try {
				p.load(is);
				String multiDomains = p.getProperty("multiDomains");
				if (multiDomains != null && multiDomains.equalsIgnoreCase("true")) {
					return true;
				}
			}
			catch (IOException e) {
				log.info("could not load application.properties, proceeding with default configuration for all domains.");
			}

		}
		return false;
	}

	/**
	 * Builds a path for a node depending on domain name. This method simply concatenates
	 * the server name to the requested node in case multiple domains are enabled.
	 * 
	 * @param name
	 * @param serverName
	 * @param config
	 * @return
	 */
	public static String mapNameToDomainName(String name, String serverName, Config config) {
		if (isMultiDomainEnabled(config))
			return serverName + "/" + name;
		log.info("using default domain.");
		return name;
	}
	
	/**
	 * This function sets the necessary response parameters to disable the 
	 * browser caching of the page
	 * 
	 * @param response The response object to use
	 */
	public static void preventBrowserCaching(HttpServletResponse response) {
		response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", 0); //prevents caching on a proxy server as well
	}
	
	/**
	 * Logs a site request into the database.
	 * 
	 * @param request  the request being logged
	 * @param content  the requested content
	 * @param time	   the date for the log entry
	 * @param username the username of the current user
	 */
	public static void logRequest(HttpServletRequest request, Content content, Date time, String username) {
			
		SiteRequest siterequest = new SiteRequest();

		if (username == null)
			username = "guest";
		
		siterequest.setContentname(content.getContentname());
		siterequest.setUseragent(request.getHeader("User-Agent"));
		siterequest.setUserip(request.getRemoteHost());
		siterequest.setUsername(username);
		siterequest.setReqtime(time.toString());
		siterequest.setDomain(request.getServerName());

		DAOManager.getInstance().getSiteRequestDAO().createAndStore(siterequest);
	}
	
}
