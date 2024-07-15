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
package org.pmedv.plugins;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.context.AppContext;
import org.pmedv.core.util.URLReader;
import org.pmedv.pojos.Config;
import org.springframework.context.ApplicationContext;

/**
 * <p>
 * The <code>ScriptPlugin</code> takes a JavaScript as an argument executes it and renders the
 * output of the script to the browser. The purpose of this plugin is to be able to write plugins
 * from scripts, which are modifiable at runtime.
 * </p>
 * <p>
 * Since scripts are interpreted at runtime, there's no need to restart the context anymore.
 * </p>
 * 
 * @author Matthias Pueski
 *
 */
public class ScriptPlugin extends AbstractPlugin implements IPlugin, Serializable {

	private static final long serialVersionUID = -2231039068420924445L;

	protected static Log log = LogFactory.getLog(ScriptPlugin.class);
	
	private transient HashMap<String,String> dynParameterMap;
	private transient HashMap<String,String> initParameterMap;
	private transient static final String responseHeader = "var response = \"\"; var userScript = new Object(); userScript.getResponse = function(name) { return response; };\n";										
	private transient static final ApplicationContext ctx = AppContext.getApplicationContext();

	
	/**
	 * The <code>responseHeader</code> line is needed in order to be able to get the response back from the script.
	 * 
	 * To get the response the from the script we simply write:
	 * 
	 * Invocable inv = (Invocable) engine;
     * Object userScript = engine.get("userScript");
	 *
	 * // invoke the method named "getResponse" on the script object "userScript"
	 *       
  	 * response =  (String)inv.invokeMethod(userScript, "getResponse");
	 * 
	 */
	
	
	public ScriptPlugin() {
		super();
		pluginID = "ScriptPlugin";
		
		/**
		 * This is the path to the script, it must be given relative to the root of the web application
		 */
		
		paramDescriptors.put("plugin_script_path", resourceBundle.getString("plugin.script.field.path"));
		paramDescriptors.put("plugin_init_params", resourceBundle.getString("plugin.init.params"));
	}
	
	public String getContent() {
	
		Config config = (Config) DAOManager.getInstance().getConfigDAO().findByID(1);
		
		String initParams = null; 
		
		try {
			initParams = paramMap.get("plugin_init_params");		
		}
		catch (NullPointerException n) {
			log.debug("No initial parameters given.");
		}
		
		
		String path = paramMap.get("plugin_script_path");
	
		/**
		 * This map is build in order to be able to get the request parameters inside
		 * the script.
		 */
		
		dynParameterMap = new HashMap<String, String>();
		
		for (Enumeration<?> e = request.getParameterNames();e.hasMoreElements();) {
			String parameterName = (String)e.nextElement();
			dynParameterMap.put(parameterName, request.getParameter(parameterName));
		}
		
		String url = protocol+"://localhost:"+request.getServerPort()+"/"+config.getLocalPath()+path;
		log.debug("reading Script from : "+url);
		
		/**
		 * Create a new script engine and set the objects for the script
		 */
		
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("JavaScript");
		String scriptContent = URLReader.getDefaultContent(url,null);

		/**
		 * This is, what we actually pass to the script 
		 * 
		 * - A reference to the data access objects
		 * - The user session
		 * - The application context
		 * - all parameters from the request
		 * 
		 */
		
		engine.put("daos", DAOManager.getInstance());
		engine.put("session", request.getSession());
		engine.put("ctx", ctx);
		engine.put("parameters", dynParameterMap);
		
		if (initParams != null)		
			engine.put("initParams", initParams);
		
		String response = null;
		
		try {
			
			log.debug("Evaluating script: \n"+responseHeader+scriptContent);
			
			engine.eval(responseHeader+scriptContent);

	        Invocable inv = (Invocable) engine;

	        /**
	         * get script object on which we want to call the method
	         */
	        Object userScript = engine.get("userScript");

	        /**
	         * Invoke the method named "getResponse" on the script object "userScript"
	         */
	        
			response =  (String)inv.invokeMethod(userScript, "getResponse");

		}
		catch (Exception e) {						
			response = e.getMessage().toString();
		}
		
		log.debug("Response : "+response);
		
		return response;

	}

}
