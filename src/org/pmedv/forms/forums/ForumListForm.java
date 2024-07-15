package org.pmedv.forms.forums;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class ForumListForm extends ActionForm {
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List forums;
	 /**
	 * @return Returns the forums.
	 */
	 public List getForums() {
	  return forums;
	 }
	 
	 /**
	 * @param forums The customers to set.
	 */
	 
	 public void setForums(List forums) {
	  this.forums = forums;
	 }
	 
	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		forums = null;
	}
}