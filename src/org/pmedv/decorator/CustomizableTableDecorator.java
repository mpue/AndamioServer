package org.pmedv.decorator;

import java.util.ResourceBundle;

import org.displaytag.decorator.TableDecorator;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.Config;

/**
 * This class is the standard decorator for all displaytag tables. 
 * 
 * @author Matthias Pueski
 * 
 */
public abstract class CustomizableTableDecorator extends TableDecorator {

	protected ResourceBundle myResourceBundle = ResourceBundle.getBundle("MessageResources");

	Config myConfig;

	public CustomizableTableDecorator() {
		super();

		myConfig = (Config) DAOManager.getInstance().getConfigDAO().findByID(1);

	}

	/**
	 * Test method which always returns a null value.
	 * 
	 * @return <code>null</code>
	 */

	public String getNullValue() {
		return null;
	}

}