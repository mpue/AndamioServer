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
import org.pmedv.beans.objects.AccountBean;
import org.pmedv.beans.objects.FolderBean;
import org.pmedv.beans.objects.MailServerAccountType;
import org.pmedv.beans.objects.MessageBean;
import org.pmedv.beans.objects.MessageStatus;
import org.pmedv.beans.objects.UserBean;
import org.pmedv.context.AppContext;
import org.pmedv.services.UserMailService;
import org.pmedv.services.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/WEB-INF/applicationContext.xml"})

public class UserMailServiceTest {

	@Test
	public void a_testCreateAccount1() {
		
	    ApplicationContext ctx = AppContext.getApplicationContext();	    
	    assertNotNull(ctx);

	    UserService userService = (UserService)ctx.getBean("userService");
	    UserMailService mailService = (UserMailService)ctx.getBean("userMailService");
	    
	    assertNotNull(userService);
	    assertNotNull(mailService);
	    
	    UserBean testUser = new UserBean();	    
	    testUser.setName("Peter");
	    
	    assertEquals(userService.create(testUser), true);
	    
	    testUser = (UserBean)userService.findByName("Peter");
	    assertNotNull(testUser);
	    
	    AccountBean account = new AccountBean();
	    account.setName("default");
	    account.setType(MailServerAccountType.INTERNAL);
	    
	    mailService.addAccount(testUser,account);
	    
	    ArrayList<AccountBean> accounts = mailService.getAccounts(testUser);	    
	    assertEquals(accounts.size(), 1);
	    
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
	    
	    testUser = (UserBean)userService.findByName("Peter");
	    assertNotNull(testUser);

	    for (AccountBean ab : accounts) {
	    	
	    	if (ab.getName().equals("default")) {
	    
	    		ArrayList<FolderBean> folders = mailService.getFolders(ab);	    	
	    		assertEquals(folders.size(), 3);
	    		
	    	}
	    	
	    }
		
	}

	@Test
	public void b_testCreateAccount2() {
		
	    ApplicationContext ctx = AppContext.getApplicationContext();	    
	    assertNotNull(ctx);

	    UserService userService = (UserService)ctx.getBean("userService");
	    UserMailService mailService = (UserMailService)ctx.getBean("userMailService");
	    
	    assertNotNull(userService);
	    assertNotNull(mailService);
	    
	    UserBean testUser = new UserBean();	    
	    testUser.setName("Bernd");
	    
	    assertEquals(userService.create(testUser), true);
	    
	    testUser = (UserBean)userService.findByName("Bernd");
	    assertNotNull(testUser);
	    
	    AccountBean account = new AccountBean();
	    account.setName("default");
	    account.setType(MailServerAccountType.INTERNAL);
	    
	    mailService.addAccount(testUser,account);
	    
	    ArrayList<AccountBean> accounts = mailService.getAccounts(testUser);	    
	    assertEquals(accounts.size(), 1);
	    
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
	    
	    testUser = (UserBean)userService.findByName("Bernd");
	    assertNotNull(testUser);

	    for (AccountBean ab : accounts) {
	    	
	    	if (ab.getName().equals("default")) {
	    
	    		ArrayList<FolderBean> folders = mailService.getFolders(ab);	    	
	    		assertEquals(folders.size(), 3);
	    		
	    	}
	    	
	    }
		
	}
	
	@Test 	
	public void c_sendMessage() {
		
	    ApplicationContext ctx = AppContext.getApplicationContext();	    
	    assertNotNull(ctx);

	    UserService userService = (UserService)ctx.getBean("userService");
	    UserMailService mailService = (UserMailService)ctx.getBean("userMailService");
	    
	    assertNotNull(userService);
	    assertNotNull(mailService);

	    MessageBean testMessage = new MessageBean();
	    
	    testMessage.setReceived(new Date());
	    testMessage.setFrom("Peter");
	    testMessage.setStatus(MessageStatus.UNREAD);
	    testMessage.setSubject("Test");
		testMessage.setBody("This is a test.");
	    
	    UserBean peter = userService.findByName("Peter");
	    assertNotNull(peter);
	    UserBean bernd = userService.findByName("Bernd");
	    assertNotNull(bernd);
	    
	    mailService.sendMessage(peter, bernd, testMessage);
	    
	    ArrayList<AccountBean> petersAccounts = mailService.getAccounts(peter);	    
	        
	    for (AccountBean ab : petersAccounts) {
	    	
	    	if (ab.getName().equals("default")) {
	    
	    		ArrayList<FolderBean> folders = mailService.getFolders(ab);
	    		
	    		assertEquals(folders.size(), 3);
	    		
	    		for (FolderBean f : folders) {
	    			
	    			if (f.getName().equals("key.outbox")) {
	    				
	    				ArrayList <MessageBean> messages = mailService.getMessages(f);
	    				
	    				assertEquals(messages.size(), 1);
	    				
	    				for (MessageBean m : messages) {
	    					
	    					if (m.getSubject().equals("Test")) {
	    						assertEquals("This is a test.", m.getBody());
	    					}
	    					
	    				}
	    				
	    			}
	    			
	    		}
	    		
	    	}
	    }
	    
	    ArrayList<AccountBean> berndsAccounts = mailService.getAccounts(bernd);	    
        
	    for (AccountBean ab : berndsAccounts) {
	    	
	    	if (ab.getName().equals("default")) {
	    
	    		ArrayList<FolderBean> folders = mailService.getFolders(ab);
	    		assertEquals(folders.size(), 3);
	    		
	    		for (FolderBean f : folders) {
	    			
	    			if (f.getName().equals("key.inbox")) {
	    				
	    				ArrayList <MessageBean> messages = mailService.getMessages(f);
	    				
	    				assertEquals(messages.size(), 1);
	    				
	    				for (MessageBean m : messages) {
	    					
	    					if (m.getSubject().equals("Test")) {
	    						assertEquals("This is a test.", m.getBody());
	    					}
	    					
	    				}
	    				
	    			}
	    			
	    		}
	    		
	    	}
	    }

		
	}
	
	@Test
	
	public void d_copyMessage() {
		
	    ApplicationContext ctx = AppContext.getApplicationContext();	    
	    assertNotNull(ctx);

	    UserService userService = (UserService)ctx.getBean("userService");
	    UserMailService mailService = (UserMailService)ctx.getBean("userMailService");
	    
	    assertNotNull(userService);
	    assertNotNull(mailService);

	    UserBean peter = userService.findByName("Peter");
	    assertNotNull(peter);
	    
	    ArrayList<AccountBean> petersAccounts = mailService.getAccounts(peter);	    
        
	    MessageBean copymessage = null;
	    
	    for (AccountBean ab : petersAccounts) {
	    	
	    	if (ab.getName().equals("default")) {
	    
	    		ArrayList<FolderBean> folders = mailService.getFolders(ab);
	    		
	    		assertEquals(folders.size(), 3);
	    		
	    		for (FolderBean f : folders) {
	    			
	    			if (f.getName().equals("key.outbox")) {
	    				
	    				ArrayList <MessageBean> messages = mailService.getMessages(f);
	    				
	    				assertEquals(messages.size(), 1);
	    				
	    				for (MessageBean m : messages) {
	    					
	    					if (m.getSubject().equals("Test")) {
	    						
	    						
	    						copymessage = m;
	    						
	    					}
	    					
	    				}
	    				
	    			}
	    			
	    		}
	    		
	    	}
	    }

	    for (AccountBean ab : petersAccounts) {
	    	
	    	if (ab.getName().equals("default")) {
	    
	    		ArrayList<FolderBean> folders = mailService.getFolders(ab);
	    		
	    		assertEquals(folders.size(), 3);
	    		
	    		for (FolderBean f : folders) {
	    			
	    			if (f.getName().equals("key.archive")) {
	    				
	    				mailService.copyMessage(f, copymessage);
	    				
	    			}
	    		}
	    	}
	    }
		
	    petersAccounts = mailService.getAccounts(peter);

	    for (AccountBean ab : petersAccounts) {
	    	
	    	if (ab.getName().equals("default")) {
	    
	    		ArrayList<FolderBean> folders = mailService.getFolders(ab);
	    		
	    		assertEquals(folders.size(), 3);
	    		
	    		for (FolderBean f : folders) {
	    			
	    			if (f.getName().equals("key.archive")) {
	    				
	    				ArrayList <MessageBean> messages = mailService.getMessages(f);
	    				
	    				assertEquals(messages.size(), 1);
	    				
	    				for (MessageBean m : messages) {
	    					
	    					if (m.getSubject().equals("Test")) {
	    						assertEquals("This is a test.", m.getBody());
	    					}
	    					
	    				}
	    			
	    			}
	    		}
	    	}
	    }
	}

	@Test
	
	public void d_moveMessage() {
		
	    ApplicationContext ctx = AppContext.getApplicationContext();	    
	    assertNotNull(ctx);

	    UserService userService = (UserService)ctx.getBean("userService");
	    UserMailService mailService = (UserMailService)ctx.getBean("userMailService");
	    
	    assertNotNull(userService);
	    assertNotNull(mailService);

	    UserBean peter = userService.findByName("Peter");
	    assertNotNull(peter);
	    
	    ArrayList<AccountBean> petersAccounts = mailService.getAccounts(peter);	    
        
	    MessageBean moveMessage = null;
	    FolderBean source = null;
	    
	    for (AccountBean ab : petersAccounts) {
	    	
	    	if (ab.getName().equals("default")) {
	    
	    		ArrayList<FolderBean> folders = mailService.getFolders(ab);
	    		
	    		assertEquals(folders.size(), 3);
	    		
	    		for (FolderBean f : folders) {
	    			
	    			if (f.getName().equals("key.archive")) {
	    				
	    				source = f;
	    				
	    				ArrayList <MessageBean> messages = mailService.getMessages(f);
	    				
	    				assertEquals(messages.size(), 1);
	    				
	    				for (MessageBean m : messages) {
	    					
	    					if (m.getSubject().equals("Test")) {
	    						moveMessage = m;	    						
	    					}
	    					
	    				}
	    				
	    			}
	    			
	    		}
	    		
	    	}
	    }

	    for (AccountBean ab : petersAccounts) {
	    	
	    	if (ab.getName().equals("default")) {
	    
	    		ArrayList<FolderBean> folders = mailService.getFolders(ab);
	    		
	    		assertEquals(folders.size(), 3);
	    		
	    		for (FolderBean f : folders) {
	    			
	    			if (f.getName().equals("key.inbox")) {
	    				
	    				mailService.moveMessage(source, f, moveMessage);
	    				
	    			}
	    		}
	    	}
	    }
	    
	    petersAccounts = mailService.getAccounts(peter);	    
	    
	    for (AccountBean ab : petersAccounts) {
	    	
	    	if (ab.getName().equals("default")) {
	    
	    		ArrayList<FolderBean> folders = mailService.getFolders(ab);
	    		
	    		assertEquals(folders.size(), 3);
	    		
	    		for (FolderBean f : folders) {	    			  			
	    			
	    			if (f.getName().equals("key.inbox")) {
	    				
	    				ArrayList<MessageBean> messages = mailService.getMessages(f);
	    				assertEquals( 1,messages.size());
	    			}
	    		}
	    	}
	    }
		
	}
	
	
	@Test
	public void y_testDeleteUser1() {
		
	    ApplicationContext ctx = AppContext.getApplicationContext();	    
	    assertNotNull(ctx);

	    UserService userService = (UserService)ctx.getBean("userService");
	    
	    UserBean u = userService.findByName("Peter");
	    assertNotNull(u);
	    
	    assertEquals(userService.delete(u), true);
		
	}

	@Test
	public void z_testDeleteUser2() {
		
	    ApplicationContext ctx = AppContext.getApplicationContext();	    
	    assertNotNull(ctx);

	    UserService userService = (UserService)ctx.getBean("userService");
	    
	    UserBean u = userService.findByName("Bernd");
	    assertNotNull(u);
	    
	    assertEquals(userService.delete(u), true);
		
	}
	
}
