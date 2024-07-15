package org.pmedv.forms.forums;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.pmedv.forms.AbstractActionForm;

public class ForumForm extends AbstractActionForm
{

	private static final long serialVersionUID = 1L;
	
	private String description;
	private int status;
	private int position;
	
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
	
	public ForumForm() {
	}
	
    public ActionErrors validate( 
        	ActionMapping mapping, HttpServletRequest request ) {
        	
        	ActionErrors errors = new ActionErrors();
               
        	if( getName() == null || getName().length() < 1 ) {
        		errors.add("name",new ActionMessage("error.name.required"));
        	}

          return errors;
    }
	
    public void reset(ActionMapping mapping, HttpServletRequest request) {
    	this.description = null;
    	this.position    = 0;
    	this.status      = 0;
    }

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}





}