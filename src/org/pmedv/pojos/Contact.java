package org.pmedv.pojos;

import java.io.Serializable;
import java.util.Date;

/**
 * the <code>ContactBean</code> represents a human contact
 * with all necessary data. This bean is used for the contact module,
 * mass mailings and further purposes.
 * 
 * @author Matthias Pueski
 *
 */

public class Contact implements Serializable, Comparable<Contact> {

	private static final long serialVersionUID = 6353710569420435348L;

	private Long id;
	
	private String firstname;
	private String lastname;
	private String title;
	private String company;
	private String phone;
	private String email;
	private String website;
	private String adress;
	private String city;
	private String letterSalutation;
	private Date   created;
	private Date   lastChange;
	private Date   dateOfBirth;	
	private String note;


	
	public Contact() {
		
	}
	
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}
	
	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}
	
	/**
	 * @param lastname the lastname to set
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}
	
	/**
	 * @param company the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
	}
	
	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}
	
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * @return the website
	 */
	public String getWebsite() {
		return website;
	}
	
	/**
	 * @param website the website to set
	 */
	public void setWebsite(String website) {
		this.website = website;
	}
	
	/**
	 * @return the adress
	 */
	public String getAdress() {
		return adress;
	}
	
	/**
	 * @param adress the adress to set
	 */
	public void setAdress(String adress) {
		this.adress = adress;
	}
	
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	
	/**
	 * @return the letterSalutation
	 */
	public String getLetterSalutation() {
		return letterSalutation;
	}
	
	/**
	 * @param letterSalutation the letterSalutation to set
	 */
	public void setLetterSalutation(String letterSalutation) {
		this.letterSalutation = letterSalutation;
	}
	
	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}
	
	/**
	 * @param created the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}
	
	/**
	 * @return the lastChange
	 */
	public Date getLastChange() {
		return lastChange;
	}
	
	/**
	 * @param lastChange the lastChange to set
	 */
	public void setLastChange(Date lastChange) {
		this.lastChange = lastChange;
	}
	
	/**
	 * @return the dateOfBirth
	 */
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	
	/**
	 * @param dateOfBirth the dateOfBirth to set
	 */
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}
	
	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}
	
	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
		    return true;
		}
		if ((obj != null) && (obj.getClass().equals(this.getClass()))) {
			
			Contact contact = (Contact) obj;		
						
			if (contact.getId() != null && this.id != null)
				if (this.getId().equals(contact.getId())) {
					return true;
				}				
			
		}
		return false;
	}

	@Override
	public int compareTo(Contact o) {
		return this.getLastname().compareTo(o.getLastname());	
	}
	
	@Override
	public String toString() {

		StringBuffer contact = new StringBuffer();
		
		contact.append(lastname);
		contact.append(",");
		contact.append(firstname);
		
		return contact.toString();
		
	}
	
}
