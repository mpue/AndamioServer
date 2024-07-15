package org.pmedv.forms.forums;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.pmedv.pojos.forums.Category;
import org.pmedv.pojos.forums.Posting;

/**
 * This class represents the model for the forum main page
 * 
 * @author Matthias Pueski
 * 
 */
public class ForumMainpageForm extends ActionForm {

	private static final long serialVersionUID = -5694186252019953176L;

	private List forums;
	private Category category;
	private Set threads;
	private String threadName;
	private List unreadThreads;
	private String postingtext;
	private String title;
	private Long id;
	private List users;

	private org.pmedv.pojos.forums.Thread thread;
	private Posting posting;

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

		ActionErrors errors = new ActionErrors();

		if (getThreadName() == null || getThreadName().length() < 1) {
			errors.add("name", new ActionMessage("error.name.required"));
		}

		return errors;
	}

	/**
	 * @return the forums of this form
	 */
	public List getForums() {
		return forums;
	}

	/**
	 * Sets the available forums
	 * 
	 * @param forums
	 */
	public void setForums(List forums) {
		this.forums = forums;
	}

	/**
	 * @return the current category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * Sets the current category
	 * 
	 * @param category
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	public Set getThreads() {
		return threads;
	}

	public void setThreads(Set threads) {
		this.threads = threads;
	}

	public Posting getPosting() {
		return posting;
	}

	public void setPosting(Posting posting) {
		this.posting = posting;
	}

	public org.pmedv.pojos.forums.Thread getThread() {
		return thread;
	}

	public void setThread(org.pmedv.pojos.forums.Thread thread) {
		this.thread = thread;
	}

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	public List getUnreadThreads() {
		return unreadThreads;
	}

	public void setUnreadThreads(List unreadThreads) {
		this.unreadThreads = unreadThreads;
	}

	public String getPostingtext() {
		return postingtext;
	}

	public void setPostingtext(String postingtext) {
		this.postingtext = postingtext;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the users
	 */
	public List getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(List users) {
		this.users = users;
	}

}
