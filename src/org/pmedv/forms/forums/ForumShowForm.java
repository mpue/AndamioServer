package org.pmedv.forms.forums;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.pmedv.forms.AbstractActionForm;
import org.pmedv.pojos.forums.Category;
import org.pmedv.pojos.forums.Forum;

public class ForumShowForm extends AbstractActionForm
{
	private static final long serialVersionUID = 1L;
	
	private Forum forum; 
	
	public ForumShowForm() {
	}
	
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
	
    public void reset(ActionMapping mapping, HttpServletRequest request) {
    	this.forum = new Forum();
    }

	public boolean equals(Object rhs) {
		return forum.equals(rhs);
	}

	public int hashCode() {
		assert false : "hashCode not designed";
		return 42; 
	}

	
	/**
	 * @return The description of the forum
	 */
	public String getDescription() {
		return forum.getDescription();
	}

	/**
	 * Sets the description of the forum
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		forum.setDescription(description);
	}

	/**
	 * @return The position of the forum
	 */
	public int getPosition() {
		return forum.getPosition();
	}

	/**
	 * Sets the position of the forum
	 * 
	 * @param position
	 */
	public void setPosition(int position) {
		forum.setPosition(position);
	}

	/**
	 * @return The status of the forum
	 */
	public int getStatus() {
		return forum.getStatus();
	}

	/**
	 * Sets the status of the forum
	 * 
	 * @param status
	 */
	public void setStatus(int status) {
		forum.setStatus(status);
	}
	
	/**
	 * @return The name of the forum
	 */
	public String getName() {
		return forum.getName();
	}

	/**
	 * Sets the name of the forum
	 * 
	 * @param status
	 */
	public void setName(String name) {
		forum.setName(name);
	}

	/**
	 * @return the forum 
	 */
	public Forum getForum() {
		return forum;
	}

	/**
	 * Sets the forum to the form
	 * 
	 * @param forum
	 */
	public void setForum(Forum forum) {
		this.forum = forum;
	}
	
	/**
	 * @return Returns the id.
	 */
	public Long getId() {
		return forum.getId();
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(Long id) {
		forum.setId ( id );
	}
	
	/**
	 * @return the categories of the forum
	 */
	public Set<Category> getCategories() {
		return forum.getCategories();
	}
	
	/**
	 * 
	 * Sets the categories of the forum
	 * 
	 * @param categories
	 */
	public void setCategories(Set<Category> categories) {
		forum.setCategories(categories);
	}

}