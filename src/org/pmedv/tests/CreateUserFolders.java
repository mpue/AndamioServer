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

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pmedv.beans.objects.AccountBean;
import org.pmedv.beans.objects.FolderBean;
import org.pmedv.beans.objects.MailServerAccountType;
import org.pmedv.beans.objects.UserBean;
import org.pmedv.context.AppContext;
import org.pmedv.services.UserMailService;
import org.pmedv.services.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/WEB-INF/applicationContext.xml"})

public class CreateUserFolders {

	@Test
	public void a_testCreateAccount1() {
		
	    ApplicationContext ctx = AppContext.getApplicationContext();	    
	    assertNotNull(ctx);

	    UserService userService = (UserService)ctx.getBean("userService");
	    UserMailService mailService = (UserMailService)ctx.getBean("userMailService");
	    
	    UserBean peter = userService.findByName("Peter");
	    
	    AccountBean account = new AccountBean();
	    account.setName("default");
	    account.setType(MailServerAccountType.INTERNAL);
	    
	    mailService.addAccount(peter,account);
	    
	    ArrayList<AccountBean> accounts = mailService.getAccounts(peter);	    
	    
	    for (AccountBean ab : accounts) {
	    	
	    	if (ab.getName().equals("default")) {

	    	    FolderBean inbox = new FolderBean();
	    	    inbox.setName("key.inbox");
	    	    
	    	    mailService.addFolder(ab, inbox);

	    	    FolderBean outbox = new FolderBean();
	    	    outbox.setName("key.outbox");

	    	    mailService.addFolder(ab, outbox);
	    	    
	    	    FolderBean archive = new FolderBean();
	    	    archive.setName("key.archive");

	    	    mailService.addFolder(ab, archive);

	    	}
	    	
	    }
		
	}
	
}
