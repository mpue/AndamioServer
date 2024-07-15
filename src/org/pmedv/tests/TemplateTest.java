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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pmedv.beans.objects.GalleryType;
import org.pmedv.beans.objects.TemplateBean;
import org.pmedv.context.AppContext;
import org.pmedv.services.TemplateService;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/WEB-INF/applicationContext.xml"})

public class TemplateTest {

	@Test
	public void testCreate() {
		
	    ApplicationContext ctx = AppContext.getApplicationContext();	    
	    assertNotNull(ctx);
	    
	    TemplateService service = (TemplateService)ctx.getBean("templateService");
	    
	    TemplateBean t = new TemplateBean();
	    
	    t.setName("test");
	    t.setDescription("test");
	    t.setCss("css");
	    t.setHtml("html");
	    t.setDivStyleClass("divStyle");
	    t.setGalleryType(GalleryType.LIGHTBOX);
	    t.setName("test");
	    t.setPlaceInDiv(true);
	    t.setTableDataStyleClass("tableDataStyle");
	    t.setTableRowStyleClass("rowStyle");
	    t.setTableStyleClass("tableStyle");
	    
	    assertEquals(true,service.createTemplate(t));
	    	
	}

	@Test
	public void testCompare() {
		
	    ApplicationContext ctx = AppContext.getApplicationContext();	    
	    assertNotNull(ctx);
	    
	    TemplateService service = (TemplateService)ctx.getBean("templateService");
	    TemplateBean t = service.findTemplateByName("test");
	    
	    assertNotNull(t);
	    
	    assertEquals(t.getDescription(), "test");
	    assertEquals(t.getCss(), "css");
	    assertEquals(t.getHtml(), "html");
	    assertEquals(t.getDivStyleClass(), "divStyle");
	    assertEquals(t.getGalleryType(), GalleryType.LIGHTBOX);
	    assertEquals(t.getName(), "test");
	    assertEquals(t.isPlaceInDiv(), true);
	    assertEquals(t.getTableDataStyleClass(), "tableDataStyle");
	    assertEquals(t.getTableRowStyleClass(), "rowStyle");
	    assertEquals(t.getTableStyleClass(), "tableStyle");
		
	}

	@Test
	public void testDelete() {
		
	    ApplicationContext ctx = AppContext.getApplicationContext();	    
	    assertNotNull(ctx);
	    
	    TemplateService service = (TemplateService)ctx.getBean("templateService");
	    TemplateBean t = service.findTemplateByName("test");
	    
	    assertNotNull(t);
	    
	    assertEquals(true, service.deleteTemplate(t.getId()));	    
	    assertEquals(0, service.findAll().size());
		
	}
	
}
