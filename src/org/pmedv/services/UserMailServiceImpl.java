package org.pmedv.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import org.pmedv.beans.objects.AccountBean;
import org.pmedv.beans.objects.FolderBean;
import org.pmedv.beans.objects.MessageBean;
import org.pmedv.beans.objects.MessageStatus;
import org.pmedv.beans.objects.UserBean;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.User;
import org.pmedv.pojos.mailsystem.Account;
import org.pmedv.pojos.mailsystem.Folder;
import org.pmedv.pojos.mailsystem.Message;


public class UserMailServiceImpl implements UserMailService{
	
	@Override
	public void addAccount(UserBean user, AccountBean account) throws IllegalArgumentException {
		
		if (user == null) throw new IllegalArgumentException("User must not be null.");
		if (account == null) throw new IllegalArgumentException ("Account must not be null.");
		
		User u = (User) DAOManager.getInstance().getUserDAO().findByID(user.getId());
		
		if (u == null)
			throw new IllegalArgumentException("User does not exist.");
		
		Account a = new Account();
		
		a.setName(account.getName());
		a.setType(account.getType());
		a.setRecvhost(account.getRecvhost());
		a.setRemotepass(account.getRemotepass());
		a.setRemoteuser(account.getRemoteuser());
		a.setSendhost(account.getSendhost());
		a.setType(account.getType());
		
		DAOManager.getInstance().getUserDAO().addAccount(u.getId(), a);
		
	}

	@Override
	public void addFolder(AccountBean account, FolderBean folder) throws IllegalArgumentException {

		if (folder == null) throw new IllegalArgumentException("Folder must not be null.");
		if (account == null) throw new IllegalArgumentException ("Account must not be null.");
		
		Account a = (Account) DAOManager.getInstance().getAccountDAO().findByID(account.getId());
		
		if (a == null)
			throw new IllegalArgumentException("Account does not exist");
		
		Folder f = new Folder();
		f.setName(folder.getName());
		
		DAOManager.getInstance().getAccountDAO().addFolder(a.getId(), f);
		
	}

	@Override
	public void addMessage(FolderBean folder, MessageBean message) throws IllegalArgumentException {
		
		if (folder == null) throw new IllegalArgumentException("folder must not be null.");
		if (message == null) throw new IllegalArgumentException ("Message must not be null.");
		
		Folder target = (Folder)DAOManager.getInstance().getFolderDAO().findByID(folder.getId());
		
		if (target == null)
			throw new IllegalArgumentException("folder does not exist.");
		
		Message m = new Message();

		m.setReceived(message.getReceived());
		m.setSender(message.getFrom());
		m.setStatus(message.getStatus());
		m.setBody(message.getBody());
		m.setSubject(message.getSubject());
		// m.setInReplyToMessageId(message.getInReplyToMessageId());
		
		DAOManager.getInstance().getFolderDAO().addMessage(target.getId(), m);
		
	}

	@Override
	public void copyMessage(FolderBean targetFolder, MessageBean message)
			throws IllegalArgumentException {

		if (targetFolder == null) throw new IllegalArgumentException("targetFolder must not be null.");
		if (message == null) throw new IllegalArgumentException ("Message must not be null.");
		
		Folder target = (Folder)DAOManager.getInstance().getFolderDAO().findByID(targetFolder.getId());
		
		if (target == null)
			throw new IllegalArgumentException("target folder does not exist.");
		
		Message m = new Message();

		m.setReceived(message.getReceived());
		m.setSender(message.getFrom());
		m.setStatus(message.getStatus());
		m.setBody(message.getBody());
		m.setSubject(message.getSubject());
		
		DAOManager.getInstance().getFolderDAO().addMessage(target.getId(), m);
		
	}

	@Override
	public ArrayList<AccountBean> getAccounts(UserBean user) throws IllegalArgumentException {
		
		if (user == null) throw new IllegalArgumentException("User must not be null.");
		
		User u = (User) DAOManager.getInstance().getUserDAO().findByID(user.getId());
	
		if (u == null)
			throw new IllegalArgumentException("User does not exist.");

		ArrayList<AccountBean> accounts = new ArrayList<AccountBean>();
		
		for (Account a : u.getAccounts()) {
			
			AccountBean ab = new AccountBean();
			
			ab.setId(a.getId());
			ab.setName(a.getName());
			ab.setRecvhost(a.getRecvhost());
			ab.setRemotepass(a.getRemotepass());
			ab.setRemoteuser(a.getRemoteuser());
			ab.setSendhost(a.getSendhost());
			ab.setType(a.getType());
			
			accounts.add(ab);
		}
		
		return accounts;
		
	}

	@Override
	public ArrayList<FolderBean> getFolders(AccountBean account) throws IllegalArgumentException {

		if (account == null) throw new IllegalArgumentException("Account must not be null.");
		
		Account a = (Account)DAOManager.getInstance().getAccountDAO().findByID(account.getId());
		
		if (a == null)
			throw new IllegalArgumentException("Account does not exist.");
		
		ArrayList<FolderBean> folders = new ArrayList<FolderBean>();
		
		for (Folder f : a.getFolders()) {
			
			FolderBean fb = new FolderBean();			
			fb.setName(f.getName());
			fb.setId(f.getId());
			
			folders.add(fb);
			
		}
		
		return folders;
		
	}

	@Override
	public ArrayList<MessageBean> getMessages(FolderBean folder) throws IllegalArgumentException {

		if (folder == null) throw new IllegalArgumentException("Folder must not be null.");
			
		Folder f = (Folder)DAOManager.getInstance().getFolderDAO().findByID(folder.getId());
		
		if (f == null)
			throw new IllegalArgumentException ("Folder does not exist.");
		
		ArrayList<MessageBean> messages = new ArrayList<MessageBean>();
		
		for (Message m : f.getMessages()) {
			
			MessageBean mb = new MessageBean();
			
			mb.setId(m.getId());
			mb.setBody(m.getBody());
			mb.setSubject(m.getSubject());
			mb.setFrom(m.getSender());
			mb.setReceived(m.getReceived());
			mb.setStatus(m.getStatus());
			// mb.setInReplyToMessageId(m.getInReplyToMessageId());
			
			messages.add(mb);
			
		}
		
		Collections.sort(messages);
		
		return messages;
		
	}

	@Override
	public void moveMessage(FolderBean sourceFolder, FolderBean targetFolder, MessageBean message)
			throws IllegalArgumentException {
		
		if (sourceFolder == null) throw new IllegalArgumentException("sourceFolder must not be null.");
		if (targetFolder == null) throw new IllegalArgumentException("targetFolder must not be null.");
		if (message == null) throw new IllegalArgumentException ("Message must not be null.");
		
		Folder target = (Folder)DAOManager.getInstance().getFolderDAO().findByID(targetFolder.getId());
		
		if (target == null)
			throw new IllegalArgumentException("target folder does not exist.");
		
		Folder source = (Folder)DAOManager.getInstance().getFolderDAO().findByID(sourceFolder.getId());
		
		if (source == null)
			throw new IllegalArgumentException("source folder does not exist.");
		
		Message m = new Message();
		
		m.setReceived(message.getReceived());
		m.setSender(message.getFrom());
		m.setStatus(message.getStatus());
		m.setBody(message.getBody());
		m.setSubject(message.getSubject());
		
		// Message m = (Message)DAOManager.getInstance().getMessageDAO().findByID(message.getId());
		
		DAOManager.getInstance().getFolderDAO().removeMessage(source.getId(), message.getId());
		DAOManager.getInstance().getFolderDAO().addMessage(target.getId(), m);

		
	}

	@Override
	public void removeAccount(UserBean user, AccountBean account) throws IllegalArgumentException {

		if (user == null) throw new IllegalArgumentException("User must not be null.");
		if (account == null) throw new IllegalArgumentException ("Account must not be null.");

		DAOManager.getInstance().getUserDAO().removeAccount(Long.valueOf(user.getId()), account.getId());
		
	}

	@Override
	public void removeFolder(AccountBean account, FolderBean folder) throws IllegalArgumentException {
		
		if (folder == null) throw new IllegalArgumentException("Folder must not be null.");
		if (account == null) throw new IllegalArgumentException ("Account must not be null.");

		DAOManager.getInstance().getAccountDAO().removeFolder(account.getId(), folder.getId());
		
	}

	@Override
	public void removeMessage(FolderBean folder, MessageBean message) throws IllegalArgumentException {
		
		if (folder == null) throw new IllegalArgumentException("Folder must not be null.");
		if (message == null) throw new IllegalArgumentException("Message must not be null.");
		
		DAOManager.getInstance().getFolderDAO().removeMessage(folder.getId(), message.getId());
		
	}

	@Override
	public void sendMessage(UserBean sender, UserBean receiver, MessageBean message)
			throws IllegalArgumentException {
		
		if (receiver == null) throw new IllegalArgumentException("Receiver must not be null.");
		if (sender == null) throw new IllegalArgumentException("Sender must not be null.");
		if (message == null) throw new IllegalArgumentException ("Message must not be null.");

		User s = (User) DAOManager.getInstance().getUserDAO().findByID(sender.getId());
		
		if (s == null)
			throw new IllegalArgumentException("Sender does not exist.");
		
		User r = (User) DAOManager.getInstance().getUserDAO().findByID(receiver.getId());
		
		if (r == null)
			throw new IllegalArgumentException("Receiver does not exist.");
		
		Message m = new Message();
		
		m.setBody(message.getBody());
		m.setSender(sender.getName());
		m.setStatus(MessageStatus.UNREAD);
		m.setSubject(message.getSubject());
		m.setReceived(new Date());
		// m.setInReplyToMessageId(message.getInReplyToMessageId());
		
		// Put mail into senders outbox
		
		for (Account a : s.getAccounts()) {
			
			if (a.getName().equalsIgnoreCase("default")) {
				
				for (Folder f : a.getFolders()) {
					
					if (f.getName().equals("key.outbox")) {
						
						DAOManager.getInstance().getFolderDAO().addMessage(f.getId(), m); 
						
					}
					
				}
				
			}
			
		}
		
		// put mail into receivers inbox
		
		for (Account a : r.getAccounts()) {
			
			if (a.getName().equalsIgnoreCase("default")) {
				
				for (Folder f : a.getFolders()) {
					
					if (f.getName().equals("key.inbox")) {
						
						DAOManager.getInstance().getFolderDAO().addMessage(f.getId(), m); 
						
					}
					
				}
				
			}
			
		}
		
		
	}

	@Override
	public AccountBean findAccountById(Long id) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FolderBean findFolderById(Long id) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MessageBean findMessageById(Long id) throws IllegalArgumentException {
		
		if (id == null) throw new IllegalArgumentException("message id must not be null.");
		
		Message m = (Message) DAOManager.getInstance().getMessageDAO().findByID(id);
		
		if (m == null) throw new IllegalArgumentException("message does not exist.");
		
		MessageBean mb = new MessageBean();
		
		mb.setId(m.getId());
		mb.setBody(m.getBody());
		mb.setSubject(m.getSubject());
		mb.setFrom(m.getSender());
		mb.setReceived(m.getReceived());
		mb.setStatus(m.getStatus());
		
		return mb;
	}



}
