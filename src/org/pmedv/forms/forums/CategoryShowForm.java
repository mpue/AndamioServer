package org.pmedv.forms.forums;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.pmedv.forms.AbstractActionForm;
import org.pmedv.pojos.forums.Category;

public class CategoryShowForm extends AbstractActionForm
{

	private static final long serialVersionUID = 1L;
	
	private Category category;
	
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
	
	public CategoryShowForm() {
	}
	
	/* (non-Javadoc)
	 * @see org.pmedv.forms.AbstractActionForm#getName()
	 */
	public String getName() {
		return category.getName();
	}
	
	/* (non-Javadoc)
	 * @see org.pmedv.forms.AbstractActionForm#setName(java.lang.String)
	 */
	public void setName(String name) {
		category.setName(name);
	}
	
	/**
	 * @return the id of the category
	 */
	public Long getId() {
		return category.getId();
	}

	/**
	 * 
	 * @param id The id to set
	 */
	public void setId(Long id) {
		category.setId(id);
	}
	
	public boolean equals(Object rhs) {
		return category.equals(rhs);
	}
	
	public int hashCode() {
		assert false : "hashCode not designed";
		return 42; 
	}
	
    public void reset(ActionMapping mapping, HttpServletRequest request) {
    	category = new Category();
    }

	public String getDescription() {
		return category.getDescription();
	}

	public void setDescription(String description) {
		category.setDescription(description);
	}

	public int getPosition() {
		return category.getPosition();
	}

	public void setPosition(int position) {
		category.setPosition(position);
	}

	/**
	 * @return the category of the form
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * Sets the category of the form
	 * 
	 * @param category
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

}