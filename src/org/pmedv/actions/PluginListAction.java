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
package org.pmedv.actions;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.pmedv.cms.common.GlobalForwards;
import org.pmedv.forms.PluginListForm;
import org.pmedv.plugins.AbstractPlugin;
import org.pmedv.plugins.IPlugin;
import org.pmedv.plugins.PluginHelper;
 

public class PluginListAction extends ObjectListAction {
	
	/**<p>
	 * A plugin classifier is a small proxy object for a <code>AbstractPlugin</code>
	 * since the processing via JSON would have been to complicated. This object
	 * holds just the necessary informaton for creating plugins from the UI.
	 * </p>
	 * @author Matthias Pueski
	 *
	 */
	private class PluginClassifier {
		
		public PluginClassifier(String name,String pluginID) {
			this.pluginID = pluginID;
			this.name = name;
		}
		
		private String pluginID;
		private String name;
		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}
		/**
		 * @return the pluginID
		 */
		public String getPluginID() {
			return pluginID;
		}
		/**
		 * @param pluginID the pluginID to set
		 */
		public void setPluginID(String pluginID) {
			this.pluginID = pluginID;
		}
		
		
	}
	
	
	public PluginListAction() {
		super("menu.plugins");
		log.debug("Executing "+this.getClass());	
	}


	public ActionForward list( ActionMapping mapping, ActionForm form,
								  HttpServletRequest request,HttpServletResponse response) {
		
		if (!isAllowedToProcess(request, getName())) return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);
		
		PluginListForm pluginListForm = (PluginListForm) form;	
		
		if (!PluginHelper.isInitialized())
			PluginHelper.initIPluginClasses(this.getClass().getClassLoader());
			
		pluginListForm.setPlugins(PluginHelper.getPluginList());			
		
		return mapping.findForward(GlobalForwards.SHOW_PLUGIN_LIST);
	}

	public ActionForward getJSON(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		ArrayList<PluginClassifier> plugins = new ArrayList<PluginClassifier>();
		
		if (!PluginHelper.isInitialized())
			PluginHelper.initIPluginClasses(this.getClass().getClassLoader());
		
		for (Iterator<IPlugin> pluginIterator = PluginHelper.getPluginList().iterator();pluginIterator.hasNext();) {
			AbstractPlugin p = (AbstractPlugin) pluginIterator.next();
			
			PluginClassifier pc = new PluginClassifier(p.getPluginID(),p.getName());
			
			plugins.add(pc);			
		}
		
		writeJSONList(PluginClassifier.class, plugins, true, "id",request, response);
		
		return null;
	}
	
	
}
