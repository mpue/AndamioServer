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
package org.pmedv.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class can be used to obtain a list of all classes in a classpath.
 *
 * <em>Caveat:</em> When used in environments which utilize multiple class loaders--such as
 * a J2EE Container like Tomcat--it is important to select the correct classloader
 * otherwise the classes returned, if any, will be incompatible with those declared
 * in the code employing this class lister.
 * to get a reference to your classloader within an instance method use:
 *  <code>this.getClass().getClassLoader()</code> or
 *  <code>Thread.currentThread().getContextClassLoader()</code> anywhere else
 * <p>
 * @author Kris Dover <krisdover@hotmail.com> / Matthias Pueski <pueski@gmx.de>
 * @version 0.2.1
 * @since   0.1.0
 */
public class PluginClassDiscovery {
	
	private static final Log log = LogFactory.getLog(PluginClassDiscovery.class);
	
	/**
	 * Searches the classpath for all classes matching a specified search criteria, 
	 * returning them in a map keyed with the interfaces they implement or null if they
	 * have no interfaces. The search criteria can be specified via interface, package
	 * and jar name filter arguments
	 * <p>
	 * @param classLoader       The classloader whose classpath will be traversed
	 * @param interfaceFilter   A Set of fully qualified interface names to search for
	 *                          or null to return classes implementing all interfaces
	 * @param packageFilter     A Set of fully qualified package names to search for or
	 *                                             or null to return classes in all packages
	 * @param jarFilter              A Set of jar file names to search for or null to return
	 *                                            classes from all jars
	 * @throws ClassNotFoundException if the current thread's classloader cannot load
	 *                 a requested class for any reason
	 */
	public static Map<String, Set<Class<?>>> findClasses(ClassLoader classLoader,
			Set<String> interfaceFilter, Set<String> packageFilter,
			Set<String> jarFilter) throws ClassNotFoundException {
		Map<String, Set<Class<?>>> classTable = new HashMap<String, Set<Class<?>>> ();
		Object[] classPaths;
		try {
			// get a list of all classpaths
			classPaths = ((java.net.URLClassLoader) classLoader).getURLs();
		} 
		catch (ClassCastException cce) {
			// or cast failed; tokenize the system classpath
			classPaths = System.getProperty("java.class.path", "").split(
					File.pathSeparator);
		}

		for (int h = 0; h < classPaths.length; h++) {
			Enumeration<?> files = null;
			JarFile module = null;
			// for each classpath ...
			File classPath = new File(
					(URL.class).isInstance(classPaths[h]) ? ((URL) classPaths[h])
							.getFile()
							: classPaths[h].toString());
			if (classPath.isDirectory() && jarFilter == null) { // is our classpath a directory and jar filters are not active?
				List<String> dirListing = new ArrayList<String>();
				// get a recursive listing of this classpath
				recursivelyListDir(dirListing, classPath, new StringBuffer());
				// an enumeration wrapping our list of files
				files = Collections.enumeration(dirListing);
			} 
			else if (classPath.getName().endsWith(".jar")) { // is our classpath a jar?
				// skip any jars not list in the filter
				if (jarFilter != null
						&& !jarFilter.contains(classPath.getName())) {
					continue;
				}
				try {
					// if our resource is a jar, instantiate a jarfile using the full path to resource
					module = new JarFile(classPath);
				} 
				catch (MalformedURLException mue) {
					throw new ClassNotFoundException("Bad classpath. Error: "+ mue.getMessage());
				} 
				catch (IOException io) {
					throw new ClassNotFoundException(
							"jar file '"
									+ classPath.getName()
									+ "' could not be instantiate from file path. Error: "
									+ io.getMessage());
				}
				// get an enumeration of the files in this jar
				files = module.entries();
			}

			// for each file path in our directory or jar
			while (files != null && files.hasMoreElements()) {
				// get each fileName
				String fileName = files.nextElement().toString();
				// we only want the class files
				if (fileName.endsWith(".class")) {
					// convert our full filename to a fully qualified class name
					String className = fileName.replaceAll("/", ".").substring(
							0, fileName.length() - 6);
					// debug class list
					// log.debug("Found class : "+className);
					// skip any classes in packages not explicitly requested in our package filter          
					try {
						if (packageFilter != null
								&& !packageFilter.contains(className.substring(0,
										className.lastIndexOf(".")))) {
							continue;
						}
					}
					catch (StringIndexOutOfBoundsException s) {
						continue;
					}
					// get the class for our class name
					Class<?> theClass = null;
					try {
						theClass = Class.forName(className, false, classLoader);
					} 
					catch (NoClassDefFoundError e) {
						log.debug("Skipping class '" + className
								+ "' for reason " + e.getMessage());
						continue;
					}
					// skip interfaces
					if (theClass.isInterface()) {
						continue;
					}
					//then get an array of all the interfaces in our class
					Class<?>[] classInterfaces = theClass.getInterfaces();

					// for each interface in this class, add both class and interface into the map
					String interfaceName = null;
					for (int i = 0; i < classInterfaces.length
							|| (i == 0 && interfaceFilter == null); i++) {
						if (i < classInterfaces.length) {
							interfaceName = classInterfaces[i].getName();
							// was this interface requested?
							if (interfaceFilter != null	&& !interfaceFilter.contains(interfaceName)) {
								continue;
							}
						}
						// is this interface already in the map?
						if (classTable.containsKey(interfaceName)) {
							// if so then just add this class to the end of the list of classes implementing this interface
							classTable.get(interfaceName).add(theClass);
						}
						else {
							// else create a new list initialised with our first class and put the list into the map
							Set<Class<?>> allClasses = new HashSet<Class<?>>();
							allClasses.add(theClass);
							classTable.put(interfaceName, allClasses);
						}
					}

				}
			}

			// close the jar if it was used
			if (module != null) {
				try {
					module.close();
				} 
				catch (IOException ioe) {
					throw new ClassNotFoundException("The module jar file '"
							+ classPath.getName()
							+ "' could not be closed. Error: "
							+ ioe.getMessage());
				}
			}

		} // end for loop

		return classTable;
	} // end method

	/**
	 * Recursively lists a directory while generating relative paths. This is a helper function for findClasses.
	 * Note: Uses a StringBuffer to avoid the excessive overhead of multiple String concatentation
	 *
	 * @param dirListing     A list variable for storing the directory listing as a list of Strings
	 * @param dir                 A File for the directory to be listed
	 * @param relativePath A StringBuffer used for building the relative paths
	 */
	private static void recursivelyListDir(List<String> dirListing, File dir,
			StringBuffer relativePath) {
		int prevLen; // used to undo append operations to the StringBuffer

		// if the dir is really a directory 
		if (dir.isDirectory()) {
			// get a list of the files in this directory
			File[] files = dir.listFiles();
			// for each file in the present dir
			for (int i = 0; i < files.length; i++) {
				// store our original relative path string length
				prevLen = relativePath.length();
				// call this function recursively with file list from present
				// dir and relateveto appended with present dir
				recursivelyListDir(dirListing, files[i], relativePath.append(
						prevLen == 0 ? "" : "/").append(files[i].getName()));
				//  delete subdirectory previously appended to our relative path
				relativePath.delete(prevLen, relativePath.length());
			}
		} 
		else {
			// this dir is a file; append it to the relativeto path and add it to the directory listing
			dirListing.add(relativePath.toString());
		}
	}
}
