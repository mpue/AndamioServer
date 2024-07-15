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

import java.util.ArrayList;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.actions.FormAction;

public abstract class AbstractBaseForm extends AbstractActionForm {
	
	private static final long serialVersionUID = 4004918433781617483L;

	protected ResourceBundle resources = ResourceBundle.getBundle("MessageResources");
	
	protected static final Log log = LogFactory.getLog(AbstractBaseForm.class);
	
	protected ArrayList<FormAction> formActions;
	
	protected AbstractBaseForm () {
		formActions = new ArrayList<FormAction>();
		createFormActions();
	}
	
	/**
	 * These method is used to build the list of available actions for the according form.
	 * It may be used to build up button bars, or ajax commands to control the form.
	 *
	 */
	protected abstract void createFormActions();

	/**
	 * @return the formActions
	 */
	public ArrayList<FormAction> getFormActions() {
		return formActions;
	}


	
}
