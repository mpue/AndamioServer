package org.pmedv.web.customtags;

/**
 * An extension of the struts form tag to allow arbitrary form attributes to be set. Intended to be
 * used primarily for introducing the autocomplete attribute supported by most browsers.
 * 
 */
public class FormTag extends org.apache.struts.taglib.html.FormTag {

	private static final long serialVersionUID = 6926005017309472093L;

	private String autocomplete = null;

	/**
	 * @return Returns the autocomplete.
	 */
	public String getAutocompletes() {
		return autocomplete;
	}

	/**
	 * A string of autocomplete to injected into the form element.
	 * 
	 * @param autocomplete The autocomplete to set.
	 */
	public void setAutocomplete(String autocomplete) {
		this.autocomplete = autocomplete;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.struts.taglib.html.FormTag#renderOtherAttributes(java.lang.StringBuffer)
	 */
	@Override
	protected void renderOtherAttributes(StringBuffer results) {
		if (autocomplete != null) {
			results.append(" autocomplete=\""+autocomplete+"\"");  
		}
	}
}
