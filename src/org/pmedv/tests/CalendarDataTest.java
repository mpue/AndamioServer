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
package org.pmedv.tests;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pmedv.beans.objects.UserBean;
import org.pmedv.beans.objects.calendar.AppointmentBean;
import org.pmedv.beans.objects.calendar.CalendarBean;
import org.pmedv.context.AppContext;
import org.pmedv.services.CalendarService;
import org.pmedv.services.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/WEB-INF/applicationContext.xml"})

public class CalendarDataTest extends TestCase {
	
	private static final Log log = LogFactory.getLog(CalendarDataTest.class);
	private static CalendarService calendarService;
	private static UserService userService;
	
	@Before
	public void setUp() throws Exception {

		log.info("setUp");
		
		ApplicationContext ctx = AppContext.getApplicationContext();	    
	    assertNotNull(ctx);
	    
	    calendarService = (CalendarService)ctx.getBean("calendarService");
	    userService = (UserService)ctx.getBean("userService");
	}

	@Test
	public void testCreate() {
			    
		AppointmentBean a1 = new AppointmentBean();
		AppointmentBean a2 = new AppointmentBean();
		AppointmentBean a3 = new AppointmentBean();
		AppointmentBean a4 = new AppointmentBean();
		
		final SimpleTimeZone mez = new SimpleTimeZone(+1 * 60 * 60 * 1000, "ECT");
		mez.setStartRule(Calendar.MARCH, -1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
		mez.setEndRule(Calendar.OCTOBER, -1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
		final  java.util.Calendar calendar = GregorianCalendar.getInstance(mez);
		
		calendar.set(2010,11, 24);
		a1.setStart(calendar.getTime());
		a1.setEnd(calendar.getTime());
	    a1.setShortDescription("Heiligabend");
	    a1.setLongDescription("Jesus' Geburtstag");
	    
	    calendar.set(2010,11,18,10,0,0);
	    a2.setStart(calendar.getTime());
	    calendar.set(2010,11,18,12,0,0);
	    a2.setEnd(calendar.getTime());
	    a2.setShortDescription("Essen");
	    a2.setLongDescription("Essen mit Peter");
	    
	    calendar.set(2010,11,31);
		a3.setStart(calendar.getTime());
		a3.setEnd(calendar.getTime());
	    a3.setShortDescription("Sylvester");

	    calendar.set(2010,5,5);
		a4.setStart(calendar.getTime());
		a4.setEnd(calendar.getTime());
	    a4.setShortDescription("Pa Geburtstag");
	    		
	    UserBean  user = userService.findByName("mpue");
	    assertNotNull(user);
	    
	    for (CalendarBean c : user.getCalendars()) {
	    	if (c.getName().equals("mpue.default")) {
	    		calendarService.addAppointment(c, a1);
	    		calendarService.addAppointment(c, a2);
	    		calendarService.addAppointment(c, a3);
	    		calendarService.addAppointment(c, a4);
	    	}
	    }
	}

	@After
	public void tearDown() throws Exception {		
		log.info("TearDown");
	}
	
}
