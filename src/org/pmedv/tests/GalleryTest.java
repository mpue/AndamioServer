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
import org.pmedv.beans.objects.GalleryBean;
import org.pmedv.beans.objects.ImageBean;
import org.pmedv.context.AppContext;
import org.pmedv.core.crypto.MD5Crypter;
import org.pmedv.services.GalleryService;
import org.springframework.context.ApplicationContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/WEB-INF/applicationContext.xml"})

public class GalleryTest {
	
	byte[] myPass   = MD5Crypter.createMD5key("admin");
	final String encryptedPassword = MD5Crypter.createMD5String(myPass);			
	final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("admin",encryptedPassword);
	
	@Test
	public void testCreateFindAndDelete() {
		
	    ApplicationContext ctx = AppContext.getApplicationContext();    
	    assertNotNull(ctx);

	    SecurityContextHolder.getContext().setAuthentication(token);
	    
	    GalleryBean gb = new GalleryBean();
	    
	    gb.setDescription("Test1234");
	    gb.setGalleryname("Testgalerie4711");
	    gb.setGallerytext("TestText");
	    gb.setRanking(0);
	    gb.setThumbheight(160);
	    gb.setThumbwidth(120);
	    
	    GalleryService service = (GalleryService) ctx.getBean("galleryService");
	    
	    assertEquals(true,service.createGallery(gb));
	    
	    GalleryBean testBean = service.findGalleryByName("Testgalerie4711");
	    
	    assertNotNull(testBean);
	    
	    assertEquals("Test1234", testBean.getDescription());
	    assertEquals("TestText", testBean.getGallerytext());	    
	    assertEquals(160, testBean.getThumbheight());
	    assertEquals(120, testBean.getThumbwidth());
		
	    assertEquals(true,service.deleteGallery(testBean.getId()));
	    
	}
	
	@Test
	public void testCreateGalleryImagesAndRemove() {

		ApplicationContext ctx = AppContext.getApplicationContext();    
	    assertNotNull(ctx);
	    
	    GalleryService service = (GalleryService) ctx.getBean("galleryService");	    
	    
	    GalleryBean gb = new GalleryBean();
	    
	    gb.setDescription("Test1234");
	    gb.setGalleryname("Testgalerie4711");
	    gb.setGallerytext("TestText");
	    gb.setRanking(0);
	    gb.setThumbheight(160);
	    gb.setThumbwidth(120);
	    
	    
	    assertEquals(service.createGallery(gb),true);
	    
	    GalleryBean testBean = service.findGalleryByName("Testgalerie4711");
	    
	    assertNotNull(testBean);
	    assertNotNull(testBean.getId());
	    
	    assertEquals("Test1234", testBean.getDescription());
	    assertEquals("TestText", testBean.getGallerytext());	    
	    assertEquals(160, testBean.getThumbheight());
	    assertEquals(120, testBean.getThumbwidth());

	    ImageBean i1 = new ImageBean();
	    i1.setDescription("galleryImage1");
	    i1.setName("name1");
	    i1.setFilename("filename1");
	    i1.setLastmodified(new Date().getTime());
	    i1.setRanking(0);

	    ImageBean i2 = new ImageBean();
	    i2.setDescription("galleryImage2");
	    i2.setFilename("filename2");
	    i2.setName("name2");
	    i2.setLastmodified(new Date().getTime());
	    i2.setRanking(0);

	    ImageBean i3 = new ImageBean();
	    i3.setDescription("galleryImage3");
	    i3.setFilename("filename3");
	    i3.setName("name3");
	    i3.setLastmodified(new Date().getTime());
	    i3.setRanking(0);
	    
	    System.out.println("Gallery id is : "+testBean.getId());
	    
	    assertEquals(true,service.addImage(i1, testBean.getId()));
	    assertEquals(true,service.addImage(i2, testBean.getId()));
	    assertEquals(true,service.addImage(i3, testBean.getId()));
	    
	    GalleryBean g = service.findGalleryByName("Testgalerie4711");
	    
	    System.out.println("Gallery has "+g.getImages().size()+" images.");
	    
	    for (ImageBean img : g.getImages()) {
	    	System.out.println("Found image "+img.getFilename());
	    }

	    assertEquals(3, g.getImages().size());
	    
	    for (ImageBean img : g.getImages()) {
	    	assertEquals(true, service.removeImage(img.getId(), g.getId()));	
	    }
	    
	    g = service.findGalleryById(testBean.getId());	    
	    assertEquals(0, g.getImages().size());
	    
	    assertEquals(true,service.deleteGallery(testBean.getId()));
	    
	}
	
	@Test
	public void updateImageTest() {
		
	    ApplicationContext ctx = AppContext.getApplicationContext();    
	    assertNotNull(ctx);
	    
	    SecurityContextHolder.getContext().setAuthentication(token);
	    
	    GalleryService service = (GalleryService) ctx.getBean("galleryService");	    
	    
	    GalleryBean gb = new GalleryBean();
	    
	    gb.setDescription("Test1234");
	    gb.setGalleryname("Testgalerie4711");
	    gb.setGallerytext("TestText");
	    gb.setRanking(0);
	    gb.setThumbheight(160);
	    gb.setThumbwidth(120);
	    
	    assertEquals(service.createGallery(gb),true);
	    
	    GalleryBean testBean = service.findGalleryByName("Testgalerie4711");
	    
	    assertNotNull(testBean);
	    assertNotNull(testBean.getId());
	    
	    assertEquals("Test1234", testBean.getDescription());
	    assertEquals("TestText", testBean.getGallerytext());	    
	    assertEquals(160, testBean.getThumbheight());
	    assertEquals(120, testBean.getThumbwidth());

	    ImageBean i1 = new ImageBean();
	    i1.setDescription("galleryImage1");
	    i1.setFilename("filename1");
	    i1.setName("name1");
	    i1.setLastmodified(new Date().getTime());
	    i1.setRanking(0);

	    
	    System.out.println("Gallery id is : "+testBean.getId());
	    
	    assertEquals(true,service.addImage(i1, testBean.getId()));
	    
	    GalleryBean g = service.findGalleryByName("Testgalerie4711");
	    
	    System.out.println("Gallery has "+g.getImages().size()+" images.");
	    
	    for (ImageBean img : g.getImages()) {
	    	System.out.println("Found image "+img.getFilename());
	    }

	    assertEquals(1, g.getImages().size());	    
	    assertEquals(g.getImages().first().getFilename(),"filename1");
	    assertEquals(g.getImages().first().getName(),"name1");
	    
	    ImageBean ib = g.getImages().first();
	    
	    ib.setFilename("filename2");
	    ib.setName("name2");
	    
	    service.updateImage(ib);
	    
	    g = service.findGalleryByName("Testgalerie4711");
	    
	    assertEquals(g.getImages().first().getFilename(),"filename2");
	    assertEquals(g.getImages().first().getName(),"name2");
	    
	    for (ImageBean img : g.getImages()) {
	    	assertEquals(true, service.removeImage(img.getId(), g.getId()));	
	    }
	    
	    g = service.findGalleryById(testBean.getId());	    
	    assertEquals(0, g.getImages().size());
	    
	    assertEquals(true,service.deleteGallery(testBean.getId()));		
	}
	
	@Test
	public void copyImageTest() {
		
		SecurityContextHolder.getContext().setAuthentication(token);
		
	    ApplicationContext ctx = AppContext.getApplicationContext();    
	    assertNotNull(ctx);

	    GalleryService service = (GalleryService) ctx.getBean("galleryService");	    
	    
	    GalleryBean gb = new GalleryBean();
	    
	    gb.setDescription("Test1234");
	    gb.setGalleryname("Testgalerie4711");
	    gb.setGallerytext("TestText");
	    gb.setRanking(0);
	    gb.setThumbheight(160);
	    gb.setThumbwidth(120);
	    
	    assertEquals(service.createGallery(gb),true);

	    GalleryBean testBean = service.findGalleryByName("Testgalerie4711");
	    
	    assertNotNull(testBean);
	    assertNotNull(testBean.getId());
	    
	    assertEquals("Test1234", testBean.getDescription());
	    assertEquals("TestText", testBean.getGallerytext());	    
	    assertEquals(160, testBean.getThumbheight());
	    assertEquals(120, testBean.getThumbwidth());

	    ImageBean i1 = new ImageBean();
	    
	    i1.setDescription("galleryImage1");
	    i1.setFilename("filename1");
	    i1.setLastmodified(new Date().getTime());
	    i1.setRanking(0);
	    
	    System.out.println("Gallery id is : "+testBean.getId());
	    
	    assertEquals(true,service.addImage(i1, testBean.getId()));
	    
	    GalleryBean gb1 = new GalleryBean();
	    
	    gb1.setDescription("Test2345");
	    gb1.setGalleryname("Testgalerie0815");
	    gb1.setGallerytext("TestText");
	    gb1.setRanking(0);
	    gb1.setThumbheight(160);
	    gb1.setThumbwidth(120);
	    
	    assertEquals(service.createGallery(gb1),true);

	    GalleryBean testBean1 = service.findGalleryByName("Testgalerie0815");
	    
	    assertNotNull(testBean1);
	    assertNotNull(testBean1.getId());
	    
	    assertEquals("Test2345", testBean1.getDescription());
	    assertEquals("TestText", testBean1.getGallerytext());	    
	    assertEquals(160, testBean1.getThumbheight());
	    assertEquals(120, testBean1.getThumbwidth());
	    
	    GalleryBean g = service.findGalleryByName("Testgalerie4711");
	    
	    System.out.println("Gallery has "+g.getImages().size()+" images.");
	    
	    for (ImageBean img : g.getImages()) {
	    	System.out.println("Found image "+img.getFilename());
	    }

	    assertEquals(1, g.getImages().size());	    
	    assertEquals(g.getImages().first().getFilename(),"filename1");
	    
	    GalleryBean g1 = service.findGalleryByName("Testgalerie0815");
	    
	    assertNotNull(g1);
	    
	    ImageBean ib = g.getImages().first();

	    assertEquals(true,service.copyImage(ib.getId(),g1.getId()));
	    
	    g1 = service.findGalleryByName("Testgalerie0815");
	    
	    assertNotNull(g1);	    
	    assertEquals(1, g1.getImages().size());	    
	    assertEquals(g1.getImages().first().getFilename(),"filename1");
	    assertEquals(true,service.deleteGallery(g.getId()));	    
	    assertEquals(true,service.deleteGallery(g1.getId()));

	}
	
	@Test
	public void moveImageTest() {
		
		SecurityContextHolder.getContext().setAuthentication(token);
		
	    ApplicationContext ctx = AppContext.getApplicationContext();    
	    assertNotNull(ctx);

	    GalleryService service = (GalleryService) ctx.getBean("galleryService");	    
	    
	    GalleryBean gb = new GalleryBean();
	    
	    gb.setDescription("Test1234");
	    gb.setGalleryname("Testgalerie4711");
	    gb.setGallerytext("TestText");
	    gb.setRanking(0);
	    gb.setThumbheight(160);
	    gb.setThumbwidth(120);
	    
	    assertEquals(service.createGallery(gb),true);

	    GalleryBean testBean = service.findGalleryByName("Testgalerie4711");
	    
	    assertNotNull(testBean);
	    assertNotNull(testBean.getId());
	    
	    assertEquals("Test1234", testBean.getDescription());
	    assertEquals("TestText", testBean.getGallerytext());	    
	    assertEquals(160, testBean.getThumbheight());
	    assertEquals(120, testBean.getThumbwidth());

	    ImageBean i1 = new ImageBean();
	    
	    i1.setDescription("galleryImage1");
	    i1.setFilename("filename1");
	    i1.setLastmodified(new Date().getTime());
	    i1.setRanking(0);
	    
	    System.out.println("Gallery id is : "+testBean.getId());
	    
	    assertEquals(true,service.addImage(i1, testBean.getId()));
	    
	    GalleryBean gb1 = new GalleryBean();
	    
	    gb1.setDescription("Test2345");
	    gb1.setGalleryname("Testgalerie0815");
	    gb1.setGallerytext("TestText");
	    gb1.setRanking(0);
	    gb1.setThumbheight(160);
	    gb1.setThumbwidth(120);
	    
	    assertEquals(service.createGallery(gb1),true);

	    GalleryBean testBean1 = service.findGalleryByName("Testgalerie0815");
	    
	    assertNotNull(testBean1);
	    assertNotNull(testBean1.getId());
	    
	    assertEquals("Test2345", testBean1.getDescription());
	    assertEquals("TestText", testBean1.getGallerytext());	    
	    assertEquals(160, testBean1.getThumbheight());
	    assertEquals(120, testBean1.getThumbwidth());
	    
	    GalleryBean g = service.findGalleryByName("Testgalerie4711");
	    
	    System.out.println("Gallery has "+g.getImages().size()+" images.");
	    
	    for (ImageBean img : g.getImages()) {
	    	System.out.println("Found image "+img.getFilename());
	    }

	    assertEquals(1, g.getImages().size());	    
	    assertEquals(g.getImages().first().getFilename(),"filename1");
	    
	    GalleryBean g1 = service.findGalleryByName("Testgalerie0815");
	    
	    assertNotNull(g1);
	    
	    ImageBean ib = g.getImages().first();

	    assertEquals(true,service.moveImage(ib.getId(),g1.getId()));
	    
	    g1 = service.findGalleryByName("Testgalerie0815");
	    
	    assertNotNull(g1);	    
	    assertEquals(1, g1.getImages().size());	    
	    assertEquals(g1.getImages().first().getFilename(),"filename1");
	    
	    g = service.findGalleryByName("Testgalerie4711");
	    
	    assertEquals(0, g.getImages().size());
	    
	    assertEquals(true,service.deleteGallery(g.getId()));	    
	    assertEquals(true,service.deleteGallery(g1.getId()));

	}	
	
}
