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
import org.pmedv.beans.objects.documents.BiblioBean;
import org.pmedv.context.AppContext;
import org.pmedv.services.BiblioService;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/WEB-INF/applicationContext.xml"})

public class BiblioTest {

	@Test
	public void testCreate() {
		
	    ApplicationContext ctx = AppContext.getApplicationContext();	    
	    BiblioService service = (BiblioService)ctx.getBean("biblioService");
	    
	    assertNotNull(ctx);
	    assertNotNull(service);
	    
		BiblioBean b = new BiblioBean();
		
		b.setAddress("address1");
		b.setAnnote("annote1");
		b.setAuthor("author1");
		b.setBooktitle("booktitle1");
		b.setChapter("chapter1");
		b.setCreated(new Date());
		b.setCustom1("a");
		b.setCustom2("b");
		b.setCustom3("c");
		b.setCustom4("d");
		b.setCustom5("e");
		b.setEdition("f");
		b.setEditor("g");
		b.setEdition("edition1");
		b.setHowpublish("howpublish1");
		b.setIdentifier("identifier1");
		b.setInstitution("institution1");
		b.setIsbn("123445678");
		b.setJournal("journal1");
		b.setLastmodified(new Date());
		b.setLastmodifiedby("root");
		b.setMonth("January");
		b.setNote("note1");
		b.setNumber(1);
		b.setOrganization("org1");
		b.setPages(10);
		b.setPublisher("publisher1");
		b.setReportType("repType1");
		b.setSchool("school1");
		b.setSeries("series1");
		b.setSubmittedBy("submit1");
		b.setType("0");
		b.setUrl("url1");
		b.setVolume("volume1");		
	    
	    assertEquals(Long.valueOf(1),service.create(b));
		
	}

	@Test
	public void testFindAndDelete() {
	    
	    ApplicationContext ctx = AppContext.getApplicationContext();	    
	    BiblioService service = (BiblioService)ctx.getBean("biblioService");
	    
	    assertNotNull(ctx);
	    assertNotNull(service);

	    BiblioBean b = service.findByName("identifier1");
	    
	    assertNotNull(b);	    
	    assertEquals(true,service.delete(b));
	    
	}
	
}
