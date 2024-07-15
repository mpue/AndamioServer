package org.pmedv.pojos.forums;

import java.util.Date;
import java.util.Set;

import org.pmedv.pojos.User;

/**
 * <p>
 * This class represents a forum thread.
 * </p>
 * 
 * @author Matthias Pueski
 * @date 21.04.2007
 * 
 * @email pueski@gmx.de
 * 
 */

public class Thread {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private Date date;
	private Date lastPost;
	private User user;
	private Category category;
	private Set<Posting> postings;

	public Thread() {
	}

	/**
	 * @return the name of the thread
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the thread
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the id of the thread
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id of the thread
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the associated category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * Sets the category
	 * 
	 * @param category
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	/**
	 * @return the postings associated with this thread
	 */
	public Set<Posting> getPostings() {
		return postings;
	}

	/**
	 * Sets the thread postings
	 * 
	 * @param postings
	 */
	public void setPostings(Set<Posting> postings) {
		this.postings = postings;
	}

	/**
	 * Returns the user of this thread
	 * 
	 * @return the user of this thread
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Sets the user of this thread
	 * 
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the creation date of this thread
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Sets the creation date of this thread
	 * 
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = new Date(date.getTime());
	}

	/**
	 * Gets the date on which the last post has been created
	 * 
	 * @return the date
	 */
	public Date getLastPost() {
		return lastPost;
	}

	/**
	 * Sets the date on which the last post has been created
	 * 
	 * @param lastPost the date to set
	 */
	public void setLastPost(Date lastPost) {
		this.lastPost = lastPost;
	}

}