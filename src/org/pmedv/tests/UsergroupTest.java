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
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pmedv.beans.objects.UsergroupBean;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.cms.daos.UserDAO;
import org.pmedv.context.AppContext;
import org.pmedv.pojos.User;
import org.pmedv.pojos.Usergroup;
import org.pmedv.services.RemoteAccessService;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/WEB-INF/applicationContext.xml"})

public class UsergroupTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testCreate() {
		
	    ApplicationContext ctx = AppContext.getApplicationContext();	    
	    assertNotNull(ctx);
	    
	    RemoteAccessService<UsergroupBean> service = (RemoteAccessService<UsergroupBean>)ctx.getBean("usergroupService");
	    assertNotNull(service);
	    
	    UsergroupBean ugb = new UsergroupBean();
	    
	    ugb.setName("assholes");
	    ugb.setDescription("This group contains members, which are assholes.");
	    
	    Long id = service.create(ugb);
	    
	    assertNotNull(id);
	    
	    UsergroupBean group = service.findById(id);
	    
	    assertNotNull(group);
	    
	    assertEquals(group.getName(), "assholes");
	    assertEquals(group.getDescription(),"This group contains members, which are assholes.");
	
	    assertEquals(service.delete(group), true);
	    
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testUpdate() {
		
	    ApplicationContext ctx = AppContext.getApplicationContext();	    
	    assertNotNull(ctx);
	    
	    RemoteAccessService<UsergroupBean> service = (RemoteAccessService<UsergroupBean>)ctx.getBean("usergroupService");
	    assertNotNull(service);
	    
	    UsergroupBean ugb = new UsergroupBean();
	    
	    ugb.setName("assholes");
	    ugb.setDescription("This group contains members, which are assholes.");
	    
	    Long id = service.create(ugb);	    
	    assertNotNull(id);
	    
	    UsergroupBean group = service.findById(id);	    
	    assertNotNull(group);
	    
	    group.setName("idiots");
	    group.setDescription("This group contains members, which are idiots.");
	    
	    assertEquals(service.update(group), true);
	    
	    group = service.findByName("idiots");
	    assertNotNull(group);
	    
	    assertEquals(group.getName(), "idiots");
	    assertEquals(group.getDescription(),"This group contains members, which are idiots.");
	    
	    assertEquals(service.delete(group), true);
	    
	}
	
//	@Test	
//	public void lazyLoadingTest()  {
//		
//		Usergroup adminGroup = (Usergroup)DAOManager.getInstance().getUsergroupDAO().findByName("admin",true);		
//		
//		for (User u : adminGroup.getUsers()) {
//			System.out.println(u.getName());
//		}
//		
//	}
	
	@Test	
	public void addMultipleUsersToGroup()  {
	    ApplicationContext ctx = AppContext.getApplicationContext();	    
	    assertNotNull(ctx);

	    UserDAO dao = DAOManager.getInstance().getUserDAO();
	    
	    ArrayList<Long> userIds = new ArrayList<Long>();
	    
	    List<?> users = dao.findAllItems();
	    
	    for (Object o : users) {
	    	
	    	User u = (User)o;	    	
	    	userIds.add(u.getId());
	    	
	    }
	    
	    dao.addGroups(3L, userIds);
	    
	    
	}	
	
	

	
}
