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

import java.util.Date;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pmedv.cms.daos.CalendarDAO;
import org.pmedv.cms.daos.MeetingDAO;
import org.pmedv.cms.daos.UserDAO;
import org.pmedv.context.AppContext;
import org.pmedv.pojos.User;
import org.pmedv.pojos.UserProfile;
import org.pmedv.pojos.calendar.Appointment;
import org.pmedv.pojos.calendar.Calendar;
import org.pmedv.pojos.calendar.Meeting;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/WEB-INF/applicationContext.xml"})

public class CalendarTest extends TestCase {
	
	private static final Log log = LogFactory.getLog(CalendarTest.class);
	
	private CalendarDAO calendarDAO;
	private UserDAO userDAO;
	private MeetingDAO meetingDAO;
	
	private User test1;
	private User test2;
	
	private Calendar testCal1;
	private Calendar testCal2;
	
	private Date testDate;
	
	@Before
	public void setUp() throws Exception {

		log.info("setUp");
		
		ApplicationContext ctx = AppContext.getApplicationContext();	    
	    assertNotNull(ctx);
	    
	    userDAO = (UserDAO) ctx.getBean("userDAO");
	    calendarDAO = (CalendarDAO)ctx.getBean("calendarDAO");
	    
	    User u = new User();	    
	    u.setName("User1");
	    u.setJoinDate(new Date());
	    
	    UserProfile profile = new UserProfile();
	    profile.setPageContent("Hello, I am the content!");
	    
	    assertEquals(userDAO.createAndStore(u,profile), true);
	    
	    test1 = (User)userDAO.findByName("User1");	    
	    assertNotNull(test1);
	    
	    User u2 = new User();	    
	    u2.setName("User2");
	    u2.setJoinDate(new Date());
	    
	    UserProfile profile2 = new UserProfile();
	    profile2.setPageContent("Hello, I am the content!");
	    
	    assertEquals(userDAO.createAndStore(u2,profile2), true);	    
	    
	    test2 = (User)userDAO.findByName("User2");	    
	    assertNotNull(test1);
	    
	    Calendar c1 = new Calendar();
	    c1.setName("Personal_test1");
	    c1.setDescription("Personal calendar for user User1");
	    
	    assertNotNull(calendarDAO.createAndStore(c1));

	    Calendar c2 = new Calendar();
	    c2.setName("Personal_test2");
	    c2.setDescription("Personal calendar for user User2");
	    
	    assertNotNull(calendarDAO.createAndStore(c2));
	    
	    meetingDAO = (MeetingDAO)ctx.getBean("meetingDAO");
	    assertNotNull(meetingDAO);
		
	    testDate = new Date();
	}

	@Test
	public void testCreate() {
			    
	    testCal1 = (Calendar)calendarDAO.findByName("Personal_test1");
	    assertNotNull(testCal1);
	    userDAO.addCalendar(testCal1.getId(), test1.getId());
	    
	    testCal2 = (Calendar)calendarDAO.findByName("Personal_test2");
	    assertNotNull(testCal2);
	    userDAO.addCalendar(testCal2.getId(), test2.getId());
	    
	    test1 = (User)userDAO.findByName("User1");	    
	    assertNotNull(test1);
	    assertEquals(1,test1.getCalendars().size());

	    test2 = (User)userDAO.findByName("User2");	    
	    assertNotNull(test2);
	    assertEquals(1,test2.getCalendars().size());
		
		Meeting m = new Meeting();
		m.setShortDescription("Short");
		m.setLongDescription("Long");
		m.setStart(testDate);
		m.setEnd(testDate);
		meetingDAO.createAndStore(m);
		
		Appointment a = new Appointment();
		a.setLongDescription("longDescription");
		a.setShortDescription("shortDescription");
		a.setStart(new Date());
		a.setEnd(new Date());
		
		m = (Meeting)meetingDAO.findByName("Short");
		assertNotNull(m);
		
	    test1 = (User)userDAO.findByName("User1");	    
	    assertNotNull(test1);
	    assertEquals(1,test1.getCalendars().size());
		
	    for (Calendar cal : test1.getCalendars()) {
	    	calendarDAO.addMeeting(m.getId(), cal.getId());
	    	a =calendarDAO.addAppointment(cal.getId(), a);
	    	System.out.println("new appointment has id"+a.getId());
	    }
	    
	    test1 = (User)userDAO.findByName("User1");	    
	    assertNotNull(test1);

	    for (Calendar cal : test1.getCalendars()) {
	    	assertEquals(1, cal.getMeetings().size());
	    }
	    
	    test2 = (User)userDAO.findByName("User2");	    
	    assertNotNull(test2);
	    assertEquals(test2.getCalendars().size(),1);
		
	    for (Calendar cal : test2.getCalendars()) {
	    	calendarDAO.addMeeting(m.getId(), cal.getId());	
	    }
	    
	    test2 = (User)userDAO.findByName("User2");	    
	    assertNotNull(test2);

	    for (Calendar cal : test2.getCalendars()) {
	    	assertEquals(1, cal.getMeetings().size());
	    }
	    
		m = (Meeting)meetingDAO.findByName("Short");
		assertNotNull(m);

		assertEquals(2, m.getCalendars().size());
		
		for (Calendar c : m.getCalendars()) {
			for (User u : c.getUsers()) {
				System.out.println(u.getName());
			}			
		}
	    
	}

	@After
	public void tearDown() throws Exception {
		
		log.info("TearDown");

		userDAO.removeCalendar(testCal1.getId(), test1.getId());	    
		assertEquals(userDAO.delete(test1), true);
		
	}
	
}
