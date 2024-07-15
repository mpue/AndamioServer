package org.pmedv.pojos.forums;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.pmedv.pojos.User;

/**
 * This class represents a posting inside a specific thread. Every thread can have an unlimited
 * number of postings written by the users.
 * 
 * @author Matthias Pueski
 * 
 * @date 22.4.2007
 * 
 */
public class Posting implements Serializable, Comparable<Posting> {

	private static final long serialVersionUID = -3611732026169016428L;

	public Posting() {

	}

	private Long id;
	private String title;
	private String postingtext;
	private Date date;
	private Date lastChange;
	private Thread thread;
	private User user;
	private String lastChangeBy;
	private Set<Attachment> attachments;

	/**
	 * @return the id of the posting
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id of the posting
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the text of the posting
	 */
	public String getPostingtext() {
		return postingtext;
	}

	/**
	 * Sets the text of this posting
	 * 
	 * @param postingtext
	 */
	public void setPostingtext(String postingtext) {
		this.postingtext = postingtext;
	}

	/**
	 * @return the title of this posting
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title of this posting
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the associated thread
	 */
	public Thread getThread() {
		return thread;
	}

	/**
	 * Sets the thread, the posting belongs to
	 * 
	 * @param thread
	 */
	public void setThread(Thread thread) {
		this.thread = thread;
	}

	/**
	 * @return the date of the posting
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Sets the date of the posting;
	 * 
	 * @param date
	 */
	public void setDate(Date date) {
		this.date = date;
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
	 * @return the user of this posting
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Sets the user of this posting
	 * 
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the lastChangeBy
	 */
	public String getLastChangeBy() {
		return lastChangeBy;
	}

	/**
	 * @param lastChangeBy the lastChangeBy to set
	 */
	public void setLastChangeBy(String lastChangeBy) {
		this.lastChangeBy = lastChangeBy;
	}

	@Override
	public int compareTo(Posting o) {
		return date.compareTo(o.getDate());
	}

	/**
	 * @return the attachments
	 */
	public Set<Attachment> getAttachments() {
		return attachments;
	}

	/**
	 * @param attachments the attachments to set
	 */
	public void setAttachments(Set<Attachment> attachments) {
		this.attachments = attachments;
	}

}
