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
/**
 * 
 */
package org.pmedv.plugins;

import java.io.Serializable;
import java.util.Iterator;

/**
 * @author Matthias Pueski
 *
 */
public class SamplePlugin extends AbstractPlugin implements IPlugin, Serializable {

	private static final long serialVersionUID = -1346454313765157404L;

	public SamplePlugin() {
		super();
		pluginID = "SamplePlugin";
		paramDescriptors.put("plugin_testparam1","Hello I am Param1");
		paramDescriptors.put("plugin_testparam2","Hello I am Param2");
	}
	
	/* (non-Javadoc)
	 * @see org.pmedv.plugins.IPlugin#getContent()
	 */
	@SuppressWarnings("rawtypes")
	public String getContent() {		
		
		StringBuffer output = new StringBuffer();
		
		output.append("<H2>Hello, I'm a sample plugin, and these are the passed parameters :<H2>");
		
		for (Iterator it = paramMap.keySet().iterator();it.hasNext();) {
			
			String key = (String)it.next();
			String value = paramMap.get(key);
			output.append(paramDescriptors.get(key));
			output.append(" : ");
			output.append(value);
			output.append("<br>");
			
		}
		
		return output.toString();
	}

}
