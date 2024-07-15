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
import org.pmedv.beans.objects.ConfigBean;
import org.pmedv.context.AppContext;
import org.pmedv.services.ConfigService;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/WEB-INF/applicationContext.xml"})

public class ConfigServiceTest {

	@Test
	public void testLoad() {
		
	    ApplicationContext ctx = AppContext.getApplicationContext();	    
	    assertNotNull(ctx);
	    
	    ConfigService cs = (ConfigService)ctx.getBean("configService");	    
	    assertNotNull(cs);

	    ConfigBean b = cs.getConfig();
	    assertNotNull(cs);
	    
	    assertEquals(b.getContentpath(),"content/");
	    assertEquals(b.getAdmintemplate(),"experience");
		
	}
	
	
	@Test
	public void testUpdate() {

	    ApplicationContext ctx = AppContext.getApplicationContext();	    
	    assertNotNull(ctx);
	    
	    ConfigService cs = (ConfigService)ctx.getBean("configService");	    
	    assertNotNull(cs);

	    ConfigBean cb = cs.getConfig();
	    assertNotNull(cb);

		cb.setAdmintemplate("a");
		cb.setBasepath("b");
		cb.setContentpath("c");
		cb.setDownloadpath("d");
		cb.setFromadress("e");
		cb.setGallerypath("f");
		cb.setHostname("g");
		cb.setImagepath("h");
		cb.setImageurl("i");
		cb.setImportpath("j");
		cb.setKeywords("k");
		cb.setLocalPath("l");
		cb.setMaxAvatarHeight(123);
		cb.setMaxAvatarWidth(456);
		cb.setMaxFileUploadSize(789);
		cb.setMaxImageUploadSize(123);
		cb.setPassword("m");
		cb.setProductimagepath("n");
		cb.setSitename("o");
		cb.setSmtphost("p");
		cb.setStartnode("q");
		cb.setTemplate("r");
		cb.setUsername("s");
	    
		assertEquals(true,cs.updateConfig(cb));
	    
		cb = cs.getConfig();
		
		assertEquals(cb.getAdmintemplate(),"a");
		assertEquals(cb.getBasepath(),"b");
		assertEquals(cb.getContentpath(),"c");
		assertEquals(cb.getDownloadpath(),"d");
		assertEquals(cb.getFromadress(),"e");
		assertEquals(cb.getGallerypath(),"f");
		assertEquals(cb.getHostname(),"g");
		assertEquals(cb.getImagepath(),"h");
		assertEquals(cb.getImageurl(),"i");
		assertEquals(cb.getImportpath(),"j");
		assertEquals(cb.getKeywords(),"k");
		assertEquals(cb.getLocalPath(),"l");
		assertEquals(cb.getMaxAvatarHeight(),123);
		assertEquals(cb.getMaxAvatarWidth(),456);
		assertEquals(cb.getMaxFileUploadSize(),789);
		assertEquals(cb.getMaxImageUploadSize(),123);
		assertEquals(cb.getPassword(),"m");
		assertEquals(cb.getProductimagepath(),"n");
		assertEquals(cb.getSitename(),"o");
		assertEquals(cb.getSmtphost(),"p");
		assertEquals(cb.getStartnode(),"q");
		assertEquals(cb.getTemplate(),"r");
		assertEquals(cb.getUsername(),"s");
		
		
		
	}
	
}
