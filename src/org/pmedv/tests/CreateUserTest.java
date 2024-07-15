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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/WEB-INF/applicationContext.xml" })
public class CreateUserTest {

	@Test
	public void testCreate() {

//		ApplicationContext ctx = AppContext.getApplicationContext();
//		assertNotNull(ctx);
//
//		UserDAO dao = (UserDAO) ctx.getBean("userDAO");
//
//		CSVReader csvReader = new CSVReader("c:/tmp/", "1000users3.csv");
//		csvReader.parseCSVData(',');
//		
//		int i = 0;
//
//		for (Object o : csvReader.getData()) {
//
//			System.out.println("Adding user "+i++);
//			
//			String[] userData = (String[]) o;
//
//			User u = new User();
//
//			u.setName(userData[0]);
//			u.setLastName(userData[1]);
//			u.setFirstName(userData[2]);
//			u.setEmail(userData[3]);
//			u.setOrt(userData[4]);
//
//			assertEquals(dao.createAndStore(u), true);
//
//		}

	}

}
