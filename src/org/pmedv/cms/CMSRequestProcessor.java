/**
 * Weberknecht AndamioManager - Open Source Content Management Written and maintained by Matthias Pueski
 * 
 * Copyright (c) 2009 Matthias Pueski
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 */
package org.pmedv.cms;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.RequestProcessor;

public class CMSRequestProcessor extends RequestProcessor {

	private static final Log log = LogFactory.getLog(CMSRequestProcessor.class);
	
	@Override
	protected ActionForward processActionPerform(HttpServletRequest request, HttpServletResponse response,
			Action action, ActionForm form, ActionMapping mapping) throws IOException, ServletException {

		ActionForward af = super.processActionPerform(request, response, action, form, mapping);
		log.debug("User agent : "+ request.getHeader("User-Agent"));
		request.setAttribute("userAgent",request.getHeader("User-Agent") );
		return af;

	}

}
