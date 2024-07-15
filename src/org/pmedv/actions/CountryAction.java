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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONSerializer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.pmedv.beans.objects.CountryBean;
import org.pmedv.context.AppContext;
import org.pmedv.services.RemoteAccessService;
import org.springframework.context.ApplicationContext;


public class CountryAction extends DispatchAction {

	private static final Log log = LogFactory.getLog(CountryAction.class);
	
	public ActionForward getUsers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
		
		
		ApplicationContext ctx = AppContext.getApplicationContext();
		RemoteAccessService<CountryBean> service = (RemoteAccessService<CountryBean>)ctx.getBean("countryService");

		Long countryId = Long.valueOf(request.getParameter("country_id"));
		CountryBean country = service.findById(countryId);

		if (country != null) {
			log.debug("Found country "+country.getName());			
			log.debug("Country has : "+country.getUsers().size()+" users.");
		}
			
		else
			log.debug("No country found.");
		
		try {
			PrintWriter out = response.getWriter();		
			out.write(JSONSerializer.toJSON(country.getUsers()).toString());
			out.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	public ActionForward getCountries(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
		
		ApplicationContext ctx = AppContext.getApplicationContext();
		RemoteAccessService<CountryBean> service = (RemoteAccessService<CountryBean>)ctx.getBean("countryService");
		
		ArrayList<CountryBean> countries = service.findAll();
		log.debug("found "+countries.size()+" countries.");
		
		try {
			PrintWriter out = response.getWriter();		
			out.write(JSONSerializer.toJSON(countries).toString());
			out.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		
		return null;
	}
}
