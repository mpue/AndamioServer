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
package org.pmedv.servlets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.herberlin.wwwutil.CgiResponse;
import de.herberlin.wwwutil.ContentLengthInputStream;

/**
 * Servlet calls php executable as cgi
 * to handle php request.
 *
 * @author hans joachim herbertz
 * created 04.01.2004
 */

@SuppressWarnings("unchecked")

public class PhpServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String VERSION="PHPServlet/1.2";

	/**
	 * The web.xml configuration parameter name for php - executable
	 */
	public static final String PHP_EXE = "php.executable";
	/**
	 * The web.xml configuration parameter name
	 */
	public static final String USE_MULTIPART_FORMDATA = "php.use.multipart.formdata";

	private static final Log logger = LogFactory.getLog(PhpServlet.class);
	/**
	 * The executable php file
	 */
	private File php = null;

	private boolean useFormdata=false;

	/**
	 * Additional environment values as read from configuration
	 */
	private List environmentList=null;
	/**
	 * Initializes php and checks if the file exists.
	 * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
	 */
	public void init() throws ServletException {

		logger.info(VERSION);

		php = new File(getInitParameter(PHP_EXE));
		if (!php.exists()) {
			ServletException e =
				new ServletException("PHP executable not found: " + php);
			logger.debug("init"+e);
			throw e;
		}
		logger.info("PHP is: " + php);

		useFormdata = Boolean.valueOf(getInitParameter(USE_MULTIPART_FORMDATA));

		logger.info("USE_MULTIPART_FORMDATA is: " + useFormdata);

		// Read more init - parameters
		environmentList=new LinkedList();
		Enumeration en=getInitParameterNames();
		while (en.hasMoreElements()) {
			String key=(String)en.nextElement();
			if (key.startsWith("php")) {
				continue;
			}
			String value=getInitParameter(key);

			if (value!=null && System.getProperty(value)!=null ) {
				value=System.getProperty(value);
			}
			logger.info("Adding to env: "+key+"="+value+".");
			environmentList.add(key+"="+value);
		}

	}

	/**
	 * Make the cgi environment for php executable.
	 * @param req
	 */
	private String[] getServerVars(HttpServletRequest req) {

		// Making server variables
		List sv = new LinkedList(environmentList);
		// Defined CGI variables
		sv.add("SERVER_SOFTWARE=" + getServletContext().getServerInfo());
		sv.add("SERVER_NAME=" + req.getServerName());
		sv.add("GATEWAY_INTERFACE=CGI/1.1");
		sv.add("SERVER_PROTOCOL=" + req.getProtocol());
		sv.add("SERVER_PORT=" + req.getServerPort());
		sv.add("REQUEST_METHOD=" + req.getMethod());
		sv.add("PATH_INFO=" + req.getRequestURL());
		sv.add("SCRIPT_NAME=" + req.getRequestURI());
		sv.add("SCRIPT_FILENAME=" + getPathTranslated(req));
		sv.add(
			"PATH_TRANSLATED="
				+ getPathTranslated(req));
		if (req.getQueryString() != null)
			sv.add("QUERY_STRING=" + req.getQueryString());
		sv.add("REMOTE_HOST=" + req.getRemoteHost());
		sv.add("REMOTE_ADDR=" + req.getRemoteAddr());

		// For authorisation the servlet leads the
		// authorization-header unchanged to php as
		// HTTP_AUTHORIZATION while php is able to
		// parse this and set its own variables:
		// PHP_AUTH_USER and PHP_AUTH_PW

		// not used
		//	if (req.getRemoteUser() != null) {
		//		sv.add("REMOTE_USER=" + req.getRemoteUser());
		//		sv.add("REMOTE_IDENT=" + req.getRemoteUser());
		//	}

		// PHP CGI redirect status
		// This is an environment variable set as required
		// by php 4 to prevent from unauthorizated execution.
		sv.add("REDIRECT_STATUS=200");

		// additional server  variables from the browser
		Enumeration en = req.getHeaderNames();
		while (en.hasMoreElements()) {

			String key = (String) en.nextElement();
			if (key.equalsIgnoreCase("content-length")) {
				sv.add("CONTENT_LENGTH" + "=" + req.getContentLength());
			} else if (key.equalsIgnoreCase("content-type")) {
				sv.add("CONTENT_TYPE=" + req.getContentType());
			} else {
				String value = req.getHeader(key);
				key = "HTTP_" + key.replace('-', '_').toUpperCase();
				sv.add(key + "=" + value);
			}

		}
		String[] serverVars = (String[]) sv.toArray(new String[sv.size()]);
		logger.info("ServerVars set to: " + sv);
		return serverVars;
	}

    /**
     * Returns the pathTranslated of the current request.
     * @param req
     * @return
     */
    private String getPathTranslated(HttpServletRequest req) {
        String path=getServletContext().getRealPath(req.getServletPath());
        logger.info("getPathTranslated="+path);
        return path;

    }
	/**
	 * Write in to out
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	private void doWrite(InputStream in, OutputStream out) throws IOException {
		int c = -1;
		byte[] buffer = new byte[512];
		while ((c = in.read(buffer)) > -1) {
			out.write(buffer, 0, c);
			out.flush();
		}
	}
	/**
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {

		HttpSession session = req.getSession();
		
		// get out if no session exists, since we don't want php scripts to be executed by everyone

//		try {
//			if (!((String)session.getAttribute("permission")).equalsIgnoreCase("true")) 
//				return;			
//		}
//		catch (NullPointerException n) {
//			return;
//		}
		
        // PHP prior to 4.2 needs the executable as commandline argument,
        // PHP 4.3 uses PATH_TRANSLATED
		String command = php.getAbsolutePath()+" "+ getPathTranslated(req);

		if (req.getQueryString() != null) {
			command = command + " " + req.getQueryString();
		}

		Process process=null;
		try {
			// call php
			logger.info("Command is: " + command);
			process =
				Runtime.getRuntime().exec(
					command,
					getServerVars(req),
					php.getParentFile());

			// write post data if any to php
			if (req.getContentLength() > 0) {
				InputStream clientIn =
					new ContentLengthInputStream(
						req.getInputStream(),
						req.getContentLength());
				OutputStream phpOut = process.getOutputStream();
				doWrite(clientIn, phpOut);
				phpOut.close();
				clientIn.close();
			}

			// open input from php
			BufferedInputStream in =
				new BufferedInputStream(process.getInputStream());

			// CgiResponse parses php headers
			CgiResponse cgiResp =null;
            try {
                // php < 4.3.0 does not return a status header
                // if a file was not found but close the stream
                cgiResp= new CgiResponse(in);
            } catch (Throwable t) {
            	t.printStackTrace();
            	logger.info("Not found: "+req.getRequestURI());
                resp.sendError(404,"Not Found: "+req.getRequestURI());
                return;
            }

			// check for cgi status code
            if (cgiResp.getHeader("Location") != null) {
                logger.info("Sending redirect: " + cgiResp.getHeader("Location"));
                resp.sendRedirect(cgiResp.getHeader("Location"));
                return;
            } else if (cgiResp.getStatus().intValue() != 200) {
				int status = cgiResp.getStatus().intValue();
				logger.info("CGI returns status: " + status);
				resp.sendError(status);
				return;
			}
            logger.info("Status line="+cgiResp.getFirstLine());


			// open client output
			BufferedOutputStream out =
				new BufferedOutputStream(resp.getOutputStream());

			// Set php headers
			String[][] phpHeaders = cgiResp.getAllHeaders();
			for (int i = 0; i < phpHeaders.length; i++) {
				resp.setHeader(phpHeaders[i][0], phpHeaders[i][1]);
			}
			out.flush();

			// write body
			doWrite(in, out);
			in.close();
			out.close();

		} catch (Throwable t) {
			ServletException ex = new ServletException(t);
			logger.debug("doGet :"+ex);
			throw ex;
		} finally {
			// kill the process
			if (process!=null) {process.destroy();}
		}
	}

	/**
	 * Parse post data if encoding is application/x-www-form-urlencoded,
	 * else send server error
	 *
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {

		if (useFormdata || "application/x-www-form-urlencoded"
			.equalsIgnoreCase(req.getContentType())) {
			doGet(req, resp);
		} else {
				logger.info(
				"Bad content type for POST request: " + req.getContentType());
			resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
		}
	}

}
