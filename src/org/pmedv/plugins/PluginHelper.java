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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.pojos.Content;
import org.pmedv.pojos.Node;
import org.pmedv.util.PluginClassDiscovery;

/**
 * Static plugin helper class, provides basic access to plugins, like initializing and
 * two types of collections to access available plugins.
 * 
 * @author Matthias Pueski
 *
 */
public class PluginHelper {
	
	public static final String NO_REDIRECT = "noredirect";
	
	protected static final Log log = LogFactory.getLog(PluginHelper.class);

	public static HashMap<String,IPlugin> pluginMap  = new HashMap<String,IPlugin>();
	public static ArrayList<IPlugin>      pluginList = new ArrayList<IPlugin>();;
	
	private static boolean initialized = false;
	
	public static boolean isInitialized() {
		return initialized;
	}
		
	/**
	 * <p>
	 * This method builds up the plugin lists of this class, you should always query if the PluginHelper
	 * is already initialized with <code>PluginHelper.isInitialized()</code> to prevent duplicate loading of classes.
	 * </p>
	 * <p>
	 * After initializing you may query either a HashMap or an ArrayList of type plugin to do further processing.
	 * </p>
	 * <p>
	 * The method <code>getPluginList()</code> returns an <code>ArrayList</code> and the method <code>getPluginMap()</code> an <code>HashMap</code>.
	 * </p>
	 * <p>
	 * We could to this with a pure singleton, but since we need the actual classLoader of the invoking class, I use the
	 * "isInitialized" mechanism instead of a singleton.
	 * </p>
	 * @author Matthias Pueski
	 * 
	 * @param classLoader the class loader to use for class discovery
	 * 
	 * 
	 */
	public static void initIPluginClasses(ClassLoader classLoader) {
				
		if (initialized) return;
		
		Map<String, Set<Class<?>>> classMap = new HashMap<String, Set<Class<?>>>();
		Set<String> interfaceFilter = new HashSet<String>();
		Set<String> packageFilter   = new HashSet<String>();
		
		interfaceFilter.add("org.pmedv.plugins.IPlugin");
		packageFilter.add("org.pmedv.plugins");

		try {
			classMap = PluginClassDiscovery.findClasses(classLoader, interfaceFilter, packageFilter, null);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		for (Iterator<Set<Class<?>>> classSetIterator = classMap.values().iterator();classSetIterator.hasNext(); ) {
			
			Set<Class<?>> currentSet = (Set<Class<?>>)classSetIterator.next();
			
			for (Iterator <Class<?>> classIterator = currentSet.iterator();classIterator.hasNext();) {
				Class<?> currentClass = (Class<?>) classIterator.next();
							
				try {
					if (!pluginMap.containsKey(currentClass.getCanonicalName())) {
						IPlugin plugin  = (IPlugin)currentClass.newInstance();		
						pluginList.add(plugin);
						pluginMap.put(currentClass.getCanonicalName(), plugin);		
						log.debug("Initialized plugin "+currentClass.getName());
					}				
				} 
				catch (InstantiationException e) {
					log.fatal("couldn't initialize plugin, because "+e.getCause());
					log.fatal("original error was :  "+e.getMessage());
					continue;
				} 
				catch (IllegalAccessException e) {
					log.fatal("couldn't initialize plugin, because "+e.getCause());
					log.fatal("original error was :  "+e.getMessage());
					continue;
				} 
				catch (SecurityException e) {
					log.fatal("couldn't initialize plugin, because "+e.getCause());
					log.fatal("original error was :  "+e.getMessage());
					continue;
				} 
				catch (IllegalArgumentException e) {
					log.fatal("couldn't initialize plugin, because "+e.getCause());
					log.fatal("original error was :  "+e.getMessage());
					continue;
				}
			}
		}
		
		initialized = true;

	}
	
	/**
	 * Gets the plugin content, depending on the parameter values of the node
	 * 
	 * @param node The node to fetch the plugin content from
	 * @return the content of the plugin node
	 */
	public static Content getPluginContent(Node node,HttpServletRequest request, HttpServletResponse response) {
		
		Content content = null;
		
		StringTokenizer st = new StringTokenizer(node.getPluginparams(),",");
		
		HashMap<String,String> params = new HashMap<String, String>();
		
		while (st.hasMoreTokens()) {
			String token = st.nextToken();			
			String param[] = token.split("=");

			if (param.length > 1) {
				param[1] = param[1].replaceAll("_eq_", "=");
				params.put(param[0], param[1]);			
				log.debug("found parameter :"+param[0]+" value="+param[1]);
			}
		}

		content  = new Content();
		
		IPlugin plugin = PluginHelper.pluginMap.get(node.getPluginid());
		
		if (plugin != null) {

			plugin.init(params,request, response);
			
			String pluginContent = plugin.getContent();
			
			if (pluginContent == null) {
				content.setDescription(NO_REDIRECT);
				content.setContentstring(NO_REDIRECT);
				content.setContentname(NO_REDIRECT);
				return content;
			}
			
			content.setDescription(node.getName());
			content.setContentstring(pluginContent);
			content.setContentname(node.getName());
			
		}
		else {
			content.setDescription("Error");
			content.setContentstring("The plugin "+node.getPluginid()+" could not be initialized.");
			content.setContentname("Error");			
		}
		
		return content;
		
	}
	
	/**
	 * Loads the param descriptors for an IPlugin
	 * 
	 * @param plugin
	 * @return
	 */
	public static ArrayList<ParamDescriptor> getParamDescriptors(IPlugin plugin) {

		ArrayList<ParamDescriptor> paramDescriptors = new ArrayList<ParamDescriptor>();

		for (Iterator<String> it = plugin.getParamDescriptors().keySet().iterator(); it.hasNext();) {

			String key = (String) it.next();
			paramDescriptors.add(new ParamDescriptor(plugin.getParamDescriptors().get(key), key, ""));

		}
		return paramDescriptors;
	}
	
	public static ArrayList<ParamDescriptor> getParamDescriptors(String pluginid,ClassLoader classLoader) {
		ArrayList<ParamDescriptor> descriptors;
		
		log.debug("Loading param descriptor table for plugin " + pluginid);

		IPlugin plugin = PluginHelper.getPluginMap().get(pluginid);

		if (plugin != null) {
		
			PluginHelper.initIPluginClasses(classLoader);
			descriptors = getParamDescriptors(plugin);
	
			log.debug("Found " + descriptors.size() + " parameters.");
		
		}
		else {
			descriptors = new ArrayList<ParamDescriptor>();
		}
		return descriptors;
	}

	/**
	 * @return the pluginList
	 */
	public static ArrayList<IPlugin> getPluginList() {
		return pluginList;
	}

	/**
	 * @return the pluginMap
	 */
	public static HashMap<String, IPlugin> getPluginMap() {
		return pluginMap;
	}
	
}
