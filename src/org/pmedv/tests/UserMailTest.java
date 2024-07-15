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
import org.pmedv.beans.objects.MailServerAccountType;
import org.pmedv.beans.objects.MessageStatus;
import org.pmedv.cms.daos.AccountDAO;
import org.pmedv.cms.daos.FolderDAO;
import org.pmedv.cms.daos.UserDAO;
import org.pmedv.context.AppContext;
import org.pmedv.pojos.User;
import org.pmedv.pojos.UserProfile;
import org.pmedv.pojos.mailsystem.Account;
import org.pmedv.pojos.mailsystem.Folder;
import org.pmedv.pojos.mailsystem.Message;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/WEB-INF/applicationContext.xml" })
public class UserMailTest {

	@Test
	public void testCreateAccount() {

		ApplicationContext ctx = AppContext.getApplicationContext();
		assertNotNull(ctx);

		UserDAO dao = (UserDAO) ctx.getBean("userDAO");

		User u = new User();

		u.setName("user1");
		u.setJoinDate(new Date());

		UserProfile profile = new UserProfile();
		profile.setPageContent("Hello, I am the content!");

		assertEquals(dao.createAndStore(u, profile), true);

		User test = (User) dao.findByName("User1");
		assertNotNull(test);

		Account a = new Account();
		a.setName("Personal");
		a.setType(MailServerAccountType.INTERNAL);

		dao.addAccount(test.getId(), a);

		test = (User) dao.findByName("User1");
		assertNotNull(test);

		assertEquals(test.getAccounts().size(), 1);

		for (Account ac : test.getAccounts()) {
			dao.removeAccount(test.getId(), ac.getId());
		}

		test = (User) dao.findByName("User1");
		assertNotNull(test);

		assertEquals(test.getAccounts().size(), 0);

		assertEquals(dao.delete(test), true);
	}

	@Test
	public void testFolders() {

		ApplicationContext ctx = AppContext.getApplicationContext();
		assertNotNull(ctx);

		UserDAO dao = (UserDAO) ctx.getBean("userDAO");
		AccountDAO accountDAO = (AccountDAO) ctx.getBean("accountDAO");

		assertNotNull(dao);
		assertNotNull(accountDAO);

		User u = new User();

		u.setName("user1");
		u.setJoinDate(new Date());

		UserProfile profile = new UserProfile();
		profile.setPageContent("Hello, I am the content!");

		assertEquals(dao.createAndStore(u, profile), true);

		User test = (User) dao.findByName("User1");
		assertNotNull(test);

		Account a = new Account();
		a.setName("Personal");
		a.setType(MailServerAccountType.INTERNAL);

		dao.addAccount(test.getId(), a);

		test = (User) dao.findByName("User1");
		assertNotNull(test);

		for (Account ac : test.getAccounts()) {

			if (ac.getName().equals("Personal")) {

				Folder inbox = new Folder();
				inbox.setName("Inbox");

				accountDAO.addFolder(ac.getId(), inbox);

			}

		}

		test = (User) dao.findByName("User1");
		assertNotNull(test);

		for (Account ac : test.getAccounts()) {

			if (ac.getName().equals("Personal")) {
				assertEquals(ac.getFolders().size(), 1);
			}
		}

		assertEquals(dao.delete(test), true);

	}

	@Test
	public void testMessages() {

		ApplicationContext ctx = AppContext.getApplicationContext();
		assertNotNull(ctx);

		UserDAO dao = (UserDAO) ctx.getBean("userDAO");
		AccountDAO accountDAO = (AccountDAO) ctx.getBean("accountDAO");
		FolderDAO folderDAO = (FolderDAO) ctx.getBean("folderDAO");

		assertNotNull(dao);
		assertNotNull(accountDAO);

		User u = new User();

		u.setName("user1");
		u.setJoinDate(new Date());

		UserProfile profile = new UserProfile();
		profile.setPageContent("Hello, I am the content!");

		assertEquals(dao.createAndStore(u, profile), true);

		User test = (User) dao.findByName("User1");
		assertNotNull(test);

		Account a = new Account();
		a.setName("Personal");
		a.setType(MailServerAccountType.INTERNAL);

		dao.addAccount(test.getId(), a);

		test = (User) dao.findByName("User1");
		assertNotNull(test);

		for (Account ac : test.getAccounts()) {

			if (ac.getName().equals("Personal")) {

				Folder inbox = new Folder();
				inbox.setName("Inbox");

				accountDAO.addFolder(ac.getId(), inbox);

			}

		}

		test = (User) dao.findByName("User1");
		assertNotNull(test);

		for (Account ac : test.getAccounts()) {

			if (ac.getName().equals("Personal")) {

				Message m = new Message();

				m.setSender("Peter");
				m.setBody("Hallo, ich bin eine Nachricht!");
				m.setStatus(MessageStatus.UNREAD);
				m.setSubject("Hallo!");

				for (Folder f : ac.getFolders()) {

					if (f.getName().equals("Inbox")) {

						folderDAO.addMessage(f.getId(), m);

					}

				}

			}
		}

		test = (User) dao.findByName("User1");
		assertNotNull(test);

		for (Account ac : test.getAccounts()) {

			if (ac.getName().equals("Personal")) {
				assertEquals(ac.getFolders().size(), 1);

				for (Folder f : ac.getFolders()) {

					if (f.getName().equals("Inbox")) {

						assertEquals(f.getMessages().size(), 1);

					}

				}

			}
			
		}

		assertEquals(dao.delete(test), true);

	}

}
