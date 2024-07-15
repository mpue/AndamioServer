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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pmedv.beans.objects.UserBean;
import org.pmedv.context.AppContext;
import org.pmedv.services.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/WEB-INF/applicationContext.xml"})

public class UserServiceTest {

	@Test
	public void testFind() {
		
	    ApplicationContext ctx = AppContext.getApplicationContext();	    
	    assertNotNull(ctx);
	    
	    UserService service = (UserService)ctx.getBean("userService");
	    assertNotNull(service);
	    
	    ArrayList<UserBean> users = service.getUsers();
	    
	    assertEquals(users.size(),1);
	    
	}

	@Test
	public void testCreate() {
		
	    ApplicationContext ctx = AppContext.getApplicationContext();	    
	    assertNotNull(ctx);
	    
	    UserService service = (UserService)ctx.getBean("userService");
	    assertNotNull(service);
	    
	    UserBean user = new UserBean();
	    user.setActive(true);
	    user.setEmail("peter@gmx.de");
	    user.setJoinDate(new Date());
	    user.setLand("Deutschland");
	    user.setName("Peter");
	    user.setFirstName("Peter");
	    user.setLastName("Katheter");
	    user.setOrt("Cologne");
	    user.setPassword("12345");
	    user.setRanking(0);
	    user.setTelefon("12345");
	    user.setTitle("Master of desaster");
	    
	    assertEquals(service.create(user), true);
	    
	    ArrayList<UserBean> users = service.getUsers();	    
	    assertEquals(users.size(),2);
	    
	    user = service.findByName("Peter");
	    assertNotNull(user);
	    
	    assertEquals(user.isActive(), true);
	    assertEquals(user.getEmail(),"peter@gmx.de");
	    assertEquals(user.getLand(),"Deutschland");
	    assertEquals(user.getName(),"Peter");
	    assertEquals(user.getFirstName(),"Peter");
	    assertEquals(user.getLastName(),"Katheter");
	    assertEquals(user.getOrt(),"Cologne");
	    assertEquals(user.getPassword(),"12345");
	    assertEquals(user.getRanking(),0);
	    assertEquals(user.getTelefon(),"12345");
	    assertEquals(user.getTitle(),"Master of desaster");
	    
	    assertEquals(service.delete(user), true);
	    
	    users = service.getUsers();	    
	    assertEquals(users.size(),1);
	    
	}

	@Test
	public void testUpdate() {
		
	    ApplicationContext ctx = AppContext.getApplicationContext();	    
	    assertNotNull(ctx);
	    
	    UserService service = (UserService)ctx.getBean("userService");
	    assertNotNull(service);
	    
	    UserBean user = new UserBean();
	    user.setActive(true);
	    user.setEmail("peter@gmx.de");
	    user.setJoinDate(new Date());
	    user.setLand("Deutschland");
	    user.setName("Peter");
	    user.setFirstName("Peter");
	    user.setLastName("Katheter");	    
	    user.setOrt("Cologne");
	    user.setPassword("12345");
	    user.setRanking(0);
	    user.setTelefon("12345");
	    user.setTitle("Master of desaster");
	    
	    assertEquals(service.create(user), true);
	    
	    ArrayList<UserBean> users = service.getUsers();	    
	    assertEquals(users.size(),2);
	    
	    user = service.findByName("Peter");
	    assertNotNull(user);

	    user.setEmail("_peter@gmx.de");
	    user.setLand("_Deutschland");
	    user.setName("_Peter");
	    user.setFirstName("_Peter");
	    user.setLastName("_Katheter");	    
	    user.setOrt("_Cologne");
	    user.setPassword("_12345");
	    user.setRanking(1);
	    user.setTelefon("_12345");
	    user.setTitle("_Master of desaster");

	    assertEquals(service.update(user), true);
	    
	    user = service.findByName("_Peter");
	    assertNotNull(user);
	    
	    assertEquals(user.isActive(), true);
	    assertEquals(user.getEmail(),"_peter@gmx.de");
	    assertEquals(user.getLand(),"_Deutschland");
	    assertEquals(user.getName(),"_Peter");
	    assertEquals(user.getFirstName(),"_Peter");
	    assertEquals(user.getLastName(),"_Katheter");
	    assertEquals(user.getOrt(),"_Cologne");
	    assertEquals(user.getPassword(),"_12345");
	    assertEquals(user.getRanking(),1);
	    assertEquals(user.getTelefon(),"_12345");
	    assertEquals(user.getTitle(),"_Master of desaster");
	    
	    assertEquals(service.delete(user), true);
	    
	    users = service.getUsers();	    
	    assertEquals(users.size(),1);
	    
	}

	
}
