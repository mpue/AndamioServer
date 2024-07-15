package org.pmedv.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.pmedv.beans.objects.ContactBean;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.Contact;

/**
 * This is the remote implementation of the {@link ContactService}
 * 
 * @author Matthias Pueski (13.05.2010)
 *
 */

public class ContactServiceImpl implements ContactService {

	@Override
	public Long createContact(ContactBean contactBean) throws IllegalArgumentException {

		Contact contact = new Contact();
		
		contact.setAdress(contactBean.getAdress());
		contact.setCity(contactBean.getCity());
		contact.setCompany(contactBean.getCompany());
		contact.setCreated(new Date());
		contact.setDateOfBirth(contactBean.getDateOfBirth());
		contact.setEmail(contactBean.getEmail());
		contact.setFirstname(contactBean.getFirstname());
		contact.setLastChange(new Date());
		contact.setLastname(contactBean.getLastname());
		contact.setLetterSalutation(contactBean.getLetterSalutation());
		contact.setNote(contactBean.getNote());
		contact.setPhone(contactBean.getNote());
		contact.setTitle(contactBean.getPhone());
		contact.setWebsite(contactBean.getWebsite());
		
		if (DAOManager.getInstance().getContactDAO().createAndStore(contact) != null) {			
			return DAOManager.getInstance().getContactDAO().getMaxId();			
		}
		
		return null;
	}

	@Override
	public boolean deleteContact(Long contactId) throws IllegalArgumentException {
		
		Contact contact = (Contact)DAOManager.getInstance().getContactDAO().findByID(contactId);		
		return DAOManager.getInstance().getContactDAO().delete(contact);
		
	}

	@Override
	public ArrayList<ContactBean> getContacts() {
		
		ArrayList<ContactBean> _contacts = new ArrayList<ContactBean>();
		
		List<?> contacts = DAOManager.getInstance().getContactDAO().findAllItems();
		
		for (Iterator<?> it = contacts.iterator();it.hasNext();) {
			
			Contact contact = (Contact) it.next();
			
			ContactBean contactBean = new ContactBean();
			
			contactBean.setId(contact.getId());
			contactBean.setAdress(contact.getAdress());
			contactBean.setCity(contact.getCity());
			contactBean.setCompany(contact.getCompany());
			contactBean.setCreated(contact.getCreated());
			contactBean.setDateOfBirth(contact.getDateOfBirth());
			contactBean.setEmail(contact.getEmail());
			contactBean.setFirstname(contact.getFirstname());
			contactBean.setLastChange(contact.getLastChange());
			contactBean.setLastname(contact.getLastname());
			contactBean.setLetterSalutation(contact.getLetterSalutation());
			contactBean.setNote(contact.getNote());
			contactBean.setPhone(contact.getNote());
			contactBean.setTitle(contact.getPhone());
			contactBean.setWebsite(contact.getWebsite());
			
			_contacts.add(contactBean);
		}
		
		return _contacts;
	}

	@Override
	public ContactBean findContactById(Long contactId) throws IllegalArgumentException {

		Contact contact = (Contact)DAOManager.getInstance().getContactDAO().findByID(contactId);
		
		if (contact == null)
			throw new IllegalArgumentException("Contact with id "+contactId+" does not exist.");
		
		ContactBean contactBean = new ContactBean();
		
		contactBean.setId(contact.getId());
		contactBean.setAdress(contact.getAdress());
		contactBean.setCity(contact.getCity());
		contactBean.setCompany(contact.getCompany());
		contactBean.setCreated(contact.getCreated());
		contactBean.setDateOfBirth(contact.getDateOfBirth());
		contactBean.setEmail(contact.getEmail());
		contactBean.setFirstname(contact.getFirstname());
		contactBean.setLastChange(contact.getLastChange());
		contactBean.setLastname(contact.getLastname());
		contactBean.setLetterSalutation(contact.getLetterSalutation());
		contactBean.setNote(contact.getNote());
		contactBean.setPhone(contact.getNote());
		contactBean.setTitle(contact.getPhone());
		contactBean.setWebsite(contact.getWebsite());
		
		return contactBean;
	}

	@Override
	public ArrayList<ContactBean> findContactsByName(String name) throws IllegalArgumentException {

		ArrayList<ContactBean> _contacts = new ArrayList<ContactBean>();
		
		List<?> contacts = DAOManager.getInstance().getContactDAO().findElementsByName(name);
		
		for (Iterator<?> it = contacts.iterator();it.hasNext();) {
			
			Contact contact = (Contact) it.next();
			
			ContactBean contactBean = new ContactBean();
			
			contactBean.setId(contact.getId());
			contactBean.setAdress(contact.getAdress());
			contactBean.setCity(contact.getCity());
			contactBean.setCompany(contact.getCompany());
			contactBean.setCreated(contact.getCreated());
			contactBean.setDateOfBirth(contact.getDateOfBirth());
			contactBean.setEmail(contact.getEmail());
			contactBean.setFirstname(contact.getFirstname());
			contactBean.setLastChange(contact.getLastChange());
			contactBean.setLastname(contact.getLastname());
			contactBean.setLetterSalutation(contact.getLetterSalutation());
			contactBean.setNote(contact.getNote());
			contactBean.setPhone(contact.getNote());
			contactBean.setTitle(contact.getPhone());
			contactBean.setWebsite(contact.getWebsite());
			
			_contacts.add(contactBean);
		}
		
		return _contacts;

	}

	@Override
	public boolean updateContact(ContactBean contactBean) throws IllegalArgumentException {

		Contact contact = (Contact)DAOManager.getInstance().getContactDAO().findByID(contactBean.getId());
		
		if (contact == null)
			throw new IllegalArgumentException("Contact with id "+contactBean.getId()+" does not exist.");
		
		contact.setAdress(contactBean.getAdress());
		contact.setCity(contactBean.getCity());
		contact.setCompany(contactBean.getCompany());
		contact.setCreated(contactBean.getCreated());
		contact.setDateOfBirth(contactBean.getDateOfBirth());
		contact.setEmail(contactBean.getEmail());
		contact.setFirstname(contactBean.getFirstname());
		contact.setLastChange(new Date());
		contact.setLastname(contactBean.getLastname());
		contact.setLetterSalutation(contactBean.getLetterSalutation());
		contact.setNote(contactBean.getNote());
		contact.setPhone(contactBean.getNote());
		contact.setTitle(contactBean.getPhone());
		contact.setWebsite(contactBean.getWebsite());
		
		return DAOManager.getInstance().getContactDAO().update(contact);
		
	}
	
	@Override
	public ArrayList<ContactBean> getContacts(int limit, int start) {
		
		ArrayList<ContactBean> _contacts = new ArrayList<ContactBean>();
		
		List<?> contacts = DAOManager.getInstance().getBaseDAO().findAllItems(Contact.class, limit, start);
		
		for (Iterator<?> it = contacts.iterator();it.hasNext();) {
			
			Contact contact = (Contact) it.next();
			
			ContactBean contactBean = new ContactBean();
			
			contactBean.setId(contact.getId());
			contactBean.setAdress(contact.getAdress());
			contactBean.setCity(contact.getCity());
			contactBean.setCompany(contact.getCompany());
			contactBean.setCreated(contact.getCreated());
			contactBean.setDateOfBirth(contact.getDateOfBirth());
			contactBean.setEmail(contact.getEmail());
			contactBean.setFirstname(contact.getFirstname());
			contactBean.setLastChange(contact.getLastChange());
			contactBean.setLastname(contact.getLastname());
			contactBean.setLetterSalutation(contact.getLetterSalutation());
			contactBean.setNote(contact.getNote());
			contactBean.setPhone(contact.getNote());
			contactBean.setTitle(contact.getPhone());
			contactBean.setWebsite(contact.getWebsite());
			
			_contacts.add(contactBean);
		}
		
		return _contacts;
		
	}

	@Override
	public long getCount() {
		return DAOManager.getInstance().getBaseDAO().getCount(Contact.class);
	}

}
