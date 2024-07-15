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

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pmedv.beans.objects.AttributeBean;
import org.pmedv.beans.objects.DataType;
import org.pmedv.cms.daos.GalleryDAO;
import org.pmedv.cms.daos.ImageDAO;
import org.pmedv.cms.daos.UserDAO;
import org.pmedv.context.AppContext;
import org.pmedv.pojos.Attribute;
import org.pmedv.pojos.Gallery;
import org.pmedv.pojos.Image;
import org.pmedv.pojos.User;
import org.pmedv.pojos.UserComment;
import org.pmedv.pojos.UserProfile;
import org.pmedv.services.AttributeService;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/WEB-INF/applicationContext.xml"})

public class UserTest {

	@Test
	public void testCreate() {
		
	    ApplicationContext ctx = AppContext.getApplicationContext();	    
	    assertNotNull(ctx);
	    
	    UserDAO dao = (UserDAO) ctx.getBean("userDAO");
	    
	    User u = new User();
	    
	    u.setName("user1");
	    u.setJoinDate(new Date());
	    
	    UserProfile profile = new UserProfile();
	    profile.setPageContent("Hello, I am the content!");
	    
	    assertEquals(dao.createAndStore(u,profile), true);
	    
	    User test = (User)dao.findByName("User1");	    
	    assertNotNull(test);
	    
	    test.getUserProfile().setPageContent("Blah, blah, blah!");
	    
	    assertEquals(dao.update(test),true);
	    
	    test = (User)dao.findByName("User1");	    
	    assertNotNull(test);
	    
		assertEquals("Blah, blah, blah!", test.getUserProfile().getPageContent());
		
		assertEquals(dao.delete(test), true);
	}
	
	@Test
	public void testComment() {
		
	    ApplicationContext ctx = AppContext.getApplicationContext();	    
	    assertNotNull(ctx);
	    
	    UserDAO dao = (UserDAO) ctx.getBean("userDAO");
	    
	    User u = new User();
	    
	    u.setName("user1");
	    u.setJoinDate(new Date());
	    
	    UserProfile profile = new UserProfile();
	    profile.setPageContent("Hello, I am the content!");
	    
	    assertEquals(dao.createAndStore(u,profile), true);
	    
	    User test = (User)dao.findByName("User1");	    
	    assertNotNull(test);
	    
	    test.getUserProfile().setPageContent("Blah, blah, blah!");
	    
	    assertEquals(dao.update(test),true);
	    
	    test = (User)dao.findByName("User1");	    
	    assertNotNull(test);	    
		assertEquals("Blah, blah, blah!", test.getUserProfile().getPageContent());
		
		UserComment comment = new UserComment();
		comment.setText("Blah");
		comment.setCreated(new Date());
		
		dao.addComment(test.getId(), comment);
		
	    test = (User)dao.findByName("User1");	    
	    assertNotNull(test);	
	    assertEquals(test.getUserComments().size(), 1);
	    
	    for (UserComment c : test.getUserComments()) {	    	
	    	assertEquals(c.getText(), "Blah");
	    	assertNotNull(c.getCreated());
	    	dao.removeComment(test.getId(), c.getId());
	    }
	    
	    test = (User)dao.findByName("User1");	    
	    assertNotNull(test);
		assertEquals(test.getUserComments().size(), 0);
		
		assertEquals(dao.delete(test), true);
	}
	
	@Test
	public void testGallery() {
		
	    ApplicationContext ctx = AppContext.getApplicationContext();	    
	    assertNotNull(ctx);
	    
	    UserDAO dao = (UserDAO) ctx.getBean("userDAO");
	    GalleryDAO gdao = (GalleryDAO)ctx.getBean("galleryDAO");
	    ImageDAO imageDAO = (ImageDAO)ctx.getBean("imageDAO");
	    
	    User u = new User();
	    
	    u.setName("user1");
	    u.setJoinDate(new Date());
	    
	    UserProfile profile = new UserProfile();
	    profile.setPageContent("Hello, I am the content!");
	    
	    assertEquals(dao.createAndStore(u,profile), true);
	    
	    User test = (User)dao.findByName("User1");	    
	    assertNotNull(test);

	    Gallery g1 = new Gallery();
	    g1.setGalleryname("gallery1");
	    
	    Gallery g2 = new Gallery();
	    g2.setGalleryname("gallery2");

	    dao.addGallery(test.getId(),g1);
	    dao.addGallery(test.getId(),g2);
	    
	    test = (User)dao.findByName("User1");	    
	    assertNotNull(test);
		assertEquals(test.getUserGalleries().size(), 2);
		
		Image i1 = new Image();
		i1.setName("image1");
		i1.setFilename("file1");
		
		Image i2 = new Image();
		i2.setName("image2");
		i2.setFilename("file2");
		
		for (Gallery userGallery : test.getUserGalleries()) {
			assertEquals(gdao.addImage(userGallery.getId(), i1),true);
			assertEquals(gdao.addImage(userGallery.getId(), i2),true);
		}
		
	    test = (User)dao.findByName("User1");	    
	    assertNotNull(test);

	    UserComment c1 = new UserComment();
	    c1.setAuthor("Test1");
	    c1.setCreated(new Date());
	    c1.setText("Blah1");
	    
	    UserComment c2 = new UserComment();
	    c2.setAuthor("Test2");
	    c2.setCreated(new Date());
	    c2.setText("Blah2");

	    
	    for (Gallery userGallery : test.getUserGalleries()) {	    	
	    	assertEquals(userGallery.getImages().size(), 2);
	    	
	    	for (Image i : userGallery.getImages()) {
	    		imageDAO.addComment(i.getId(), c1);
	    		imageDAO.addComment(i.getId(), c2);
	    	}
	    	
	    }
	    
	    test = (User)dao.findByName("User1");	    
	    assertNotNull(test);

	    for (Gallery userGallery : test.getUserGalleries()) {
	    	
	    	assertEquals(userGallery.getImages().size(), 2);
	    	
	    	for (Image i : userGallery.getImages()) {
		    	assertEquals(i.getUserComments().size(), 2);
	    	}
	    	
	    }
	    
		assertEquals(dao.delete(test), true);
	}	
	
	@Test
	public void testAttributes() {
		
	    ApplicationContext ctx = AppContext.getApplicationContext();	    
	    assertNotNull(ctx);
	    
	    UserDAO dao = (UserDAO) ctx.getBean("userDAO");
	    
	    User u = new User();
	    
	    u.setName("user1");
	    u.setJoinDate(new Date());
	    
	    UserProfile profile = new UserProfile();
	    profile.setPageContent("Hello, I am the content!");
	    
	    assertEquals(dao.createAndStore(u,profile), true);
	    
	    User test = (User)dao.findByName("User1");	    
	    assertNotNull(test);
	    
	    Attribute a = new Attribute();
	    a.setKey("key.gender");
	    a.setValue("male");
	    a.setDataType(DataType.STRING);
	    a.setDescription("The gender of the user");
	    
	    dao.addAttribute(test.getId(), a);
	    
	    test = (User)dao.findByName("User1");	    
	    assertNotNull(test);
	    
	    assertEquals(test.getAttributes().size(), 1);
	    
	    for (Attribute attr : test.getAttributes()) {
	    	
	    	assertEquals(attr.getKey(), "key.gender");
	    	assertEquals(attr.getValue(),"male");
	    	assertEquals(attr.getDataType(),DataType.STRING);
	    	assertEquals(attr.getDescription(),"The gender of the user");
	    	
	    }
	    
	    for (Attribute attr : test.getAttributes()) {
	    	
	    	if (attr.getKey().equals("key.gender")) {	    		
	    		attr.setValue("female");	    		
	    		dao.update(test);	    		
	    	}
	    	
	    }
	    
	    test = (User)dao.findByName("User1");	    
	    assertNotNull(test);

	    for (Attribute attr : test.getAttributes()) {
	    	
	    	if (attr.getKey().equals("key.gender")) {
	    		assertEquals(attr.getValue(),"female");
	    	}
	    	
	    }
	    
		assertEquals(dao.delete(test), true);
	}

	@Test
	public void testCreateAttributes() {
		
	    ApplicationContext ctx = AppContext.getApplicationContext();	    
	    assertNotNull(ctx);
	    
	    UserDAO dao = (UserDAO) ctx.getBean("userDAO");
	    
	    AttributeService service = (AttributeService)ctx.getBean("attributeService");
	    assertNotNull(service);

	    
	    User test = (User)dao.findByName("admin");	    
	    assertNotNull(test);

	    for (AttributeBean ab : service.findAll()) {
	    	
		    Attribute a = new Attribute();
		    
		    a.setKey(ab.getKey());
		    a.setValue(ab.getValue());
		    a.setDataType(ab.getDataType());
		    a.setDescription(ab.getDescription());
		    
		    dao.addAttribute(test.getId(), a);
	    	
	    }
	    
	    
	}
		
	
}
