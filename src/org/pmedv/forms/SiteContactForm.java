/**

	WeberknechtCMS - Open Source Content Management
	Written and maintained by Matthias Pueski 
	
	Copyright (c) 2009 Matthias Pueski
	
	This program is free software; you can redistribute it and/or
	modify it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 2
	of the License, or (at your option) any later version.
	
	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

*/
package org.pmedv.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.pmedv.pojos.Content;


public class SiteContactForm extends ActionForm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String vorname;
	private String nachname;		
	private String anschrift;
	private String land;
	private String plz;
	private String ort;
	private String telefon;
	private String telefax;
	private String email;
	private String message;
	private long id;

	private Content content;
	
	
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
	
    public void reset(ActionMapping mapping, HttpServletRequest request) {
    	this.vorname = null;
    	this.nachname = null;		
    	this.anschrift = null;
    	this.land = null;
    	this.plz = null;
    	this.ort = null;
    	this.telefon = null;
    	this.telefax = null;
    	this.email = null;
    	this.message = null;

    }

    public ActionErrors validate( 
    	ActionMapping mapping, HttpServletRequest request ) {
    	
    	ActionErrors errors = new ActionErrors();
           
    	if( getVorname() == null || getVorname().length() < 1 ) {
    		errors.add("vorname" ,new ActionMessage("error.vorname.required"));
    		errors.add("nachname",new ActionMessage("error.nachname.required"));    		
    		errors.add("address" ,new ActionMessage("error.adress.required"));
    	}

      return errors;
    }
	


	/**
	 * @return Returns the anschrift.
	 */
	public String getAnschrift() {
		return anschrift;
	}
	/**
	 * @param anschrift The anschrift to set.
	 */
	public void setAnschrift(String anschrift) {
		this.anschrift = anschrift;
	}
	/**
	 * @return Returns the email.
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email The email to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return Returns the land.
	 */
	public String getLand() {
		return land;
	}
	/**
	 * @param land The land to set.
	 */
	public void setLand(String land) {
		this.land = land;
	}
	/**
	 * @return Returns the nachname.
	 */
	public String getNachname() {
		return nachname;
	}
	/**
	 * @param nachname The nachname to set.
	 */
	public void setNachname(String nachname) {
		this.nachname = nachname;
	}
	/**
	 * @return Returns the ort.
	 */
	public String getOrt() {
		return ort;
	}
	/**
	 * @param ort The ort to set.
	 */
	public void setOrt(String ort) {
		this.ort = ort;
	}
	/**
	 * @return Returns the plz.
	 */
	public String getPlz() {
		return plz;
	}
	/**
	 * @param plz The plz to set.
	 */
	public void setPlz(String plz) {
		this.plz = plz;
	}
	/**
	 * @return Returns the telefax.
	 */
	public String getTelefax() {
		return telefax;
	}
	/**
	 * @param telefax The telefax to set.
	 */
	public void setTelefax(String telefax) {
		this.telefax = telefax;
	}
	/**
	 * @return Returns the telefon.
	 */
	public String getTelefon() {
		return telefon;
	}
	/**
	 * @param telefon The telefon to set.
	 */
	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}
	/**
	 * @return Returns the vorname.
	 */
	public String getVorname() {
		return vorname;
	}
	/**
	 * @param vorname The vorname to set.
	 */
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	/**
	 * @return Returns the id.
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return Returns the message.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message The message to set.
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return Returns the content.
	 */
	public Content getContent() {
		return content;
	}

	/**
	 * @param content The content to set.
	 */
	public void setContent(Content content) {
		this.content = content;
	}
	
	
	
}
