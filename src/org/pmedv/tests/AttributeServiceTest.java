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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pmedv.beans.objects.AttributeBean;
import org.pmedv.beans.objects.DataType;
import org.pmedv.context.AppContext;
import org.pmedv.services.AttributeService;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/WEB-INF/applicationContext.xml"})

public class AttributeServiceTest {

	@Test
	public void testCreateAttributes() {
		
	    ApplicationContext ctx = AppContext.getApplicationContext();	    
	    assertNotNull(ctx);
	    
	    AttributeService service = (AttributeService)ctx.getBean("attributeService");
	    assertNotNull(service);
		
	    AttributeBean a1 = new AttributeBean();
	    
	    a1.setKey("key.blah");
	    a1.setValue("12345678");
	    a1.setDataType(DataType.INTEGER);
	    a1.setDescription("The money of the user");

	    assertNotNull(service.create(a1));
	    
	}
	
	
	@Test
	public void testLoadAttributes() {
		
	    ApplicationContext ctx = AppContext.getApplicationContext();	    
	    assertNotNull(ctx);
	    
	    AttributeService service = (AttributeService)ctx.getBean("attributeService");
	    assertNotNull(service);
	    
	    ArrayList<AttributeBean> attributes = service.findAll();
	    
	    for (AttributeBean attribute : attributes) {
	    	System.out.println(attribute);	    	
	    }
		
	}
	
	@Test
	public void testFindAndUpdateAttributes() {
		
	    ApplicationContext ctx = AppContext.getApplicationContext();	    
	    assertNotNull(ctx);
	    
	    AttributeService service = (AttributeService)ctx.getBean("attributeService");
	    assertNotNull(service);
	    
	    AttributeBean a = service.findByKey("key.blah");
	    assertNotNull(a);
	    
	    assertEquals(a.getDataType(),DataType.INTEGER);
	    assertEquals(a.getValue(),"12345678");
	    assertEquals(a.getDescription(), "The money of the user");
	    
	    a.setDataType(DataType.STRING);
	    a.setDescription("Blahblah");
	    a.setValue("12345");
	    
	    assertEquals(service.update(a), true);
	    
	    a = service.findByKey("key.blah");
	    assertNotNull(a);

	    assertEquals(a.getDataType(),DataType.STRING);
	    assertEquals(a.getValue(),"12345");
	    assertEquals(a.getDescription(), "Blahblah");
	    
	}
	
	@Test
	public void testFindAndDeleteAttributes() {
		
	    ApplicationContext ctx = AppContext.getApplicationContext();	    
	    assertNotNull(ctx);
	    
	    AttributeService service = (AttributeService)ctx.getBean("attributeService");
	    assertNotNull(service);
	    
	    AttributeBean a = service.findByKey("key.blah");
	    assertNotNull(a);
	    
	    assertEquals(service.delete(a), true);	    
	    assertEquals(service.findAll().size(),0);
	    
	}
	
}
