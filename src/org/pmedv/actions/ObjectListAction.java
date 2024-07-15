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

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonBeanProcessor;
import net.sf.json.processors.JsonBeanProcessorMatcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.Content;
import org.pmedv.pojos.Node;
import org.pmedv.pojos.Process;
import org.pmedv.pojos.User;
import org.pmedv.pojos.UserProfile;
import org.pmedv.pojos.Usergroup;
import org.pmedv.pojos.forums.Forum;

public abstract class ObjectListAction extends AbstractPermissiveDispatchAction {

	private Class<?> dataObject;
	
	protected static final Log log = LogFactory.getLog(ObjectListAction.class);
	
	private ResourceBundle resources = ResourceBundle.getBundle("MessageResources",Locale.getDefault());
	
	public ObjectListAction(String name) {
		super(name);
	}
	
	public ObjectListAction() {		
	}
	
	/**
	 * Writes a JSON List of available objects to the output with paging  
	 * 
	 * @param response
	 * 
	 * @return the JSONString containing the objects
	 */
	
	@SuppressWarnings("unchecked")
	protected String writeJSONList(Class<?> dataObject,int limit, int start,HttpServletRequest request,HttpServletResponse response) {
		
		response.setContentType("text/plain");
	    PrintWriter out;
	    
	    StringBuffer jsonBuffer = new StringBuffer();

	    jsonBuffer.append("{'metaData': {");
	    jsonBuffer.append("\n");
	    jsonBuffer.append("totalProperty: 'results',");
	    jsonBuffer.append("\n");
	    jsonBuffer.append("root: 'rows',");
	    jsonBuffer.append("\n");
	    jsonBuffer.append("id: 'id',");
	    jsonBuffer.append("\n");
	    jsonBuffer.append("fields: [");
	    jsonBuffer.append("\n");
	    
	    Object memberNames[] = getPublicMembers(dataObject);
	    
	    Arrays.sort(memberNames);
	
    	for (int i=0;i < memberNames.length;i++) {
    		
    		String memberName = (String)memberNames[i];
    		
    	    jsonBuffer.append(" {name: '"+memberName+"'}");
    	    if (i < memberNames.length -1 )
    	    	jsonBuffer.append(",");
    	    else
    	    	jsonBuffer.append("]");
    	    jsonBuffer.append("\n");
    	    
    	}
    	
    	jsonBuffer.append("},");
    	jsonBuffer.append("\n");
    	
    	@SuppressWarnings("rawtypes")
		List items = DAOManager.getInstance().getBaseDAO().findAllItems(dataObject,limit,start);
    	
    	/*
    	 * This function should be as generic, as possible, but in case of the process list
    	 * we need to eliminate all processes, that the user is not allowed for.
    	 */ 
    	
    	
    	if (dataObject.equals(org.pmedv.pojos.Process.class)) {
    		String login = (String) request.getSession().getAttribute("login");
    		
    		log.debug("Current user is : "+login);
    		log.debug("Filtering available processes.");
    		
    		items = filterProcesses(items, login);
    	}
    	
    			    
	    JsonConfig c = new JsonConfig();
	    
	    c.setJsonBeanProcessorMatcher(JsonBeanProcessorMatcher.DEFAULT);
	    c.registerJsonBeanProcessor(dataObject, new JsonBeanProcessor() {

			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {

				JSONObject j = new JSONObject();
				
		    	for (int i=0;i < bean.getClass().getMethods().length;i++) {
		    		
		    		if (bean.getClass().getMethods()[i].getName().startsWith("get") &&
		    			!bean.getClass().getMethods()[i].getReturnType().equals(Class.class) &&
		    			!bean.getClass().getMethods()[i].getReturnType().equals(Content.class) &&	
		    		    !bean.getClass().getMethods()[i].getReturnType().equals(Set.class)) {
		    			
		    			Method method;
		    			Object fieldValue = null;
		    			
		    			try {
							method = bean.getClass().getMethod(bean.getClass().getMethods()[i].getName(), null);							
							fieldValue = method.invoke(bean, new Object[0]);							
						} 
		    			catch (SecurityException e1) {
							e1.printStackTrace();
						} 
						catch (NoSuchMethodException e1) {
							e1.printStackTrace();
						}
		    		    catch (Exception e) {
		    		    	e.printStackTrace();
		    		    }
		    		    String memberName = bean.getClass().getMethods()[i].getName();				    		
			    		
		    		    memberName = memberName.substring(3,4).toLowerCase() + memberName.substring(4,memberName.length());
			    		
			    		if (fieldValue == null)
			    			fieldValue = new Object();
			    		j.element(memberName, fieldValue,jsonConfig);

		    		}
		    	}
				
				return j;
				
			}
	    	
	    });
	            
	    
	    JSONArray jsonArray = JSONArray.fromObject(items,c);
	    
	    Integer count = DAOManager.getInstance().getBaseDAO().getCount(dataObject);
	    
	    jsonBuffer.append("'results': "+count+", 'rows': ");
	    jsonBuffer.append("\n");
    	jsonBuffer.append(jsonArray);
    	jsonBuffer.append("\n");
    	jsonBuffer.append("}");
    	jsonBuffer.append("\n");
		
		try {
			out = response.getWriter();
			out.print(jsonBuffer);			
		    out.flush();
		    
		} 
		catch (IOException e) {
			e.printStackTrace();
		}

	    
		return jsonBuffer.toString();
	}
	
	/**
	 * Writes a JSON List of available objects to the output with paging  
	 * 
	 * @param response
	 * 
	 * @return the JSONString containing the objects
	 */
	
	protected String writeJSONList(Class<?> dataObject, Collection<?> resultSet,boolean metadata,String id,HttpServletRequest request,HttpServletResponse response) {
		
		response.setContentType("text/plain");
	    PrintWriter out;
	    
	    StringBuffer jsonBuffer = new StringBuffer();

	    if (metadata) {
	    
		    jsonBuffer.append("{'metaData': {");
		    jsonBuffer.append("\n");
		    jsonBuffer.append("totalProperty: 'results',");
		    jsonBuffer.append("\n");
		    jsonBuffer.append("root: 'rows',");
		    jsonBuffer.append("\n");
		    jsonBuffer.append("id: '"+id+"',");
		    jsonBuffer.append("\n");
		    jsonBuffer.append("fields: [");
		    jsonBuffer.append("\n");
		    
		    Object memberNames[] = getPublicMembers(dataObject);
		    
		    Arrays.sort(memberNames);
		
	    	for (int i=0;i < memberNames.length;i++) {
	    		
	    		String memberName = (String)memberNames[i];
	    		
	    	    jsonBuffer.append(" {name: '"+memberName+"'}");
	    	    if (i < memberNames.length -1 )
	    	    	jsonBuffer.append(",");
	    	    else
	    	    	jsonBuffer.append("]");
	    	    jsonBuffer.append("\n");
	    	    
	    	}
	    	
	    	jsonBuffer.append("},");
	    	jsonBuffer.append("\n");

	    }	
	    	
    	Collection<?> items = resultSet;
    			    
	    JsonConfig c = new JsonConfig();
	    
	    c.setJsonBeanProcessorMatcher(JsonBeanProcessorMatcher.DEFAULT);
	    c.registerJsonBeanProcessor(dataObject, new JsonBeanProcessor() {

			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				log.debug("processBean : "+bean.getClass());
				JSONObject j = new JSONObject();
				
				log.debug("processBean : Bean has "+bean.getClass().getMethods().length+" getter.");
				
		    	for (int i=0;i < bean.getClass().getMethods().length;i++) {
		    		
	    			if (bean.getClass().getMethods()[i].getName().startsWith("get") ||
	    			   (bean.getClass().getMethods()[i].getName().startsWith("is")	 &&
	    				bean.getClass().getMethods()[i].getReturnType().equals(boolean.class))) {
	
		    			if(!bean.getClass().getMethods()[i].getReturnType().equals(Class.class) &&
		    			!bean.getClass().getMethods()[i].getReturnType().equals(Content.class) &&
		    			!bean.getClass().getMethods()[i].getReturnType().equals(Forum.class) &&
		    			!bean.getClass().getMethods()[i].getReturnType().equals(ArrayList.class) &&
		    		    !bean.getClass().getMethods()[i].getReturnType().equals(Set.class)) {
			    			
			    			Method method;
			    			Object fieldValue = null;
			    			
			    			try {
								method = bean.getClass().getMethod(bean.getClass().getMethods()[i].getName(), null);
								fieldValue = method.invoke(bean, new Object[0]);							
							} 
			    			catch (SecurityException e1) {
								e1.printStackTrace();
							} 
							catch (NoSuchMethodException e1) {
								e1.printStackTrace();
							}
			    		    catch (Exception e) {
			    		    	e.printStackTrace();
			    		    }
			    		    String memberName = bean.getClass().getMethods()[i].getName();				    		
				    		
		    				if (bean.getClass().getMethods()[i].getName().startsWith("get")) {
		    					memberName = memberName.substring(3,4).toLowerCase() + memberName.substring(4,memberName.length());		    					
		    				}
		    				else {
		    					memberName = memberName.substring(2,3).toLowerCase() + memberName.substring(3,memberName.length());
		    				}
				    		
				    		if (fieldValue == null)
				    			fieldValue = new Object();
				    		j.element(memberName, fieldValue,jsonConfig);
	
			    		}
			    	}
    			}
				
				return j;
				
			}
	    	
	    });
	            
	    
	    JSONArray jsonArray = JSONArray.fromObject(items,c);
	    
	    if (metadata) {	    
		    jsonBuffer.append("'results': "+items.size()+", 'rows': ");
		    jsonBuffer.append("\n");
	    }
	    
    	jsonBuffer.append(jsonArray);
    	
    	if (metadata) {
	    	jsonBuffer.append("\n");
	    	jsonBuffer.append("}");
	    	jsonBuffer.append("\n");
    	}
	    	
		try {
			out = response.getWriter();
			out.print(jsonBuffer);			
		    out.flush();
		    
		} 
		catch (IOException e) {
			e.printStackTrace();
		}

	    
		return jsonBuffer.toString();
	}
	

	/**
	 * Writes a JSON List of available objects to the output with paging  
	 * 
	 * @param response
	 * 
	 * @return the JSONString containing the objects
	 */
	

	@SuppressWarnings("unchecked")
	protected String writeJSONList(Class<?> dataObject,HttpServletRequest request,HttpServletResponse response) {
		
		response.setContentType("text/plain");
	    PrintWriter out;
	    
	    StringBuffer jsonBuffer = new StringBuffer();

	    jsonBuffer.append("{'metaData': {");
	    jsonBuffer.append("\n");
	    jsonBuffer.append("totalProperty: 'results',");
	    jsonBuffer.append("\n");
	    jsonBuffer.append("root: 'rows',");
	    jsonBuffer.append("\n");
	    jsonBuffer.append("id: 'id',");
	    jsonBuffer.append("\n");
	    jsonBuffer.append("fields: [");
	    jsonBuffer.append("\n");
	    
	    Object memberNames[] = getPublicMembers(dataObject);
	    
	    Arrays.sort(memberNames);
	
    	for (int i=0;i < memberNames.length;i++) {
    		
    		String memberName = (String)memberNames[i];
    		
    	    jsonBuffer.append(" {name: '"+memberName+"'}");
    	    if (i < memberNames.length -1 )
    	    	jsonBuffer.append(",");
    	    else
    	    	jsonBuffer.append("]");
    	    jsonBuffer.append("\n");
    	    
    	}
    	
    	jsonBuffer.append("},");
    	jsonBuffer.append("\n");
    	
    	List items = DAOManager.getInstance().getBaseDAO().findAllItems(dataObject);
    	
    	/*
    	 * This function should be as generic, as possible, but in case of the process list
    	 * we need to eliminate all processes, that the user is not allowed for.
    	 */ 
    	
    	if (dataObject.equals(org.pmedv.pojos.Process.class)) {
    		// String login = (String) request.getSession().getAttribute("login");
    		
    		String login ="admin";
    		
    		log.debug("Current user is : "+login);
    		log.debug("Filtering available processes.");
    		
    		items = filterProcesses(items, login);
    	}
    	
    	if (dataObject.equals(Node.class)) {
    		Node root = new Node();
    		
    		root.setId(0L);
    		root.setName("root");
    		root.setParent(null);
    		root.setChildren(null);
    		root.setPosition(0);
    		root.setType(Node.TYPE_CONTENT);
    		
    		items.add(root);
    		
    		Collections.sort(items, new Comparator<Object>() {
    			
				@Override
				public int compare(Object o1, Object o2) {
					
					Node n1 = (Node)o1;
					Node n2 = (Node)o2;
					
					return (n1.getPath().compareTo(n2.getPath()));				
				}
    			
    		});
    	}
    			    
	    JsonConfig c = new JsonConfig();
	    
	    c.setJsonBeanProcessorMatcher(JsonBeanProcessorMatcher.DEFAULT);
	    c.registerJsonBeanProcessor(dataObject, new JsonBeanProcessor() {

			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				log.debug("processBean : "+bean.getClass());
				JSONObject j = new JSONObject();
				
		    	for (int i=0;i < bean.getClass().getMethods().length;i++) {

		    		if (!bean.getClass().getMethods()[i].getReturnType().equals(UserProfile.class) &&
		    			!bean.getClass().getMethods()[i].getReturnType().equals(Class.class) &&
		    			!bean.getClass().getMethods()[i].getReturnType().equals(Content.class) &&	
		    		    !bean.getClass().getMethods()[i].getReturnType().equals(Set.class)) {
		    		
		    			if (bean.getClass().getMethods()[i].getName().startsWith("get") ||
		    			   (bean.getClass().getMethods()[i].getName().startsWith("is")	 &&
		    				bean.getClass().getMethods()[i].getReturnType().equals(boolean.class))) {
		    				
		    				Method method;
		    				Object fieldValue = null;
		    				
		    				try {
		    					method = bean.getClass().getMethod(bean.getClass().getMethods()[i].getName(), null);				
		    					fieldValue = method.invoke(bean, new Object[0]);							
		    				} 
		    				catch (Exception e) {
		    					e.printStackTrace();
		    				}
		    				String memberName = bean.getClass().getMethods()[i].getName();				    		
		    				
		    				if (bean.getClass().getMethods()[i].getName().startsWith("get")) {
		    					memberName = memberName.substring(3,4).toLowerCase() + memberName.substring(4,memberName.length());		    					
		    				}
		    				else {
		    					memberName = memberName.substring(2,3).toLowerCase() + memberName.substring(3,memberName.length());
		    				}
		    				
		    				if (fieldValue == null)
		    					fieldValue = new Object();
		    				j.element(memberName, fieldValue,jsonConfig);
		    				
		    			}
		    			
		    		}
		    		
		    	}
				
				return j;
				
			}
	    	
	    });
	            
	    
	    JSONArray jsonArray = JSONArray.fromObject(items,c);
	    
	    jsonBuffer.append("'results': "+items.size()+", 'rows': ");
	    jsonBuffer.append("\n");
    	jsonBuffer.append(jsonArray);
    	jsonBuffer.append("\n");
    	jsonBuffer.append("}");
    	jsonBuffer.append("\n");
		
		try {
			out = response.getWriter();
			out.print(jsonBuffer);			
		    out.flush();
		    
		} 
		catch (IOException e) {
			e.printStackTrace();
		}

	    
		return jsonBuffer.toString();
	}
	
	@SuppressWarnings("unchecked")
	protected String writeJSONList(Class<?> dataObject,boolean metadata,HttpServletRequest request,HttpServletResponse response) {
		
		response.setContentType("text/plain");
	    PrintWriter out;
	    
    
	    StringBuffer jsonBuffer = new StringBuffer();

	    if (metadata) {
		    jsonBuffer.append("{'metaData': {");
		    jsonBuffer.append("\n");
		    jsonBuffer.append("totalProperty: 'results',");
		    jsonBuffer.append("\n");
		    jsonBuffer.append("root: 'rows',");
		    jsonBuffer.append("\n");
		    jsonBuffer.append("id: 'id',");
		    jsonBuffer.append("\n");
		    jsonBuffer.append("fields: [");
		    jsonBuffer.append("\n");
		    
		    Object memberNames[] = getPublicMembers(dataObject);
		    
		    Arrays.sort(memberNames);
		
	    	for (int i=0;i < memberNames.length;i++) {
	    		
	    		String memberName = (String)memberNames[i];
	    		
	    	    jsonBuffer.append(" {name: '"+memberName+"'}");
	    	    if (i < memberNames.length -1 )
	    	    	jsonBuffer.append(",");
	    	    else
	    	    	jsonBuffer.append("]");
	    	    jsonBuffer.append("\n");
	    	    
	    	}
	    	
	    	jsonBuffer.append("},");
	    	jsonBuffer.append("\n");
	    	
	    }
	    
    	
    	List items = DAOManager.getInstance().getBaseDAO().findAllItems(dataObject);
    	
    	/*
    	 * This fuction should be as generic, as possible, but in case of the process list
    	 * we need to eliminate all processes, that the user is not allowed for.
    	 */ 
    	

        /*
    	if (dataObject.equals(org.pmedv.pojos.Process.class)) {
    		// String login = (String) request.getSession().getAttribute("login");
    		
    		String login ="admin";
    		
    		log.debug("Current user is : "+login);
    		log.debug("Filtering available processes.");
    		
    		items = filterProcesses(items, login);
    	}
    	*/
    			    
	    JsonConfig c = new JsonConfig();
	    
	    c.setJsonBeanProcessorMatcher(JsonBeanProcessorMatcher.DEFAULT);
	    c.registerJsonBeanProcessor(dataObject, new JsonBeanProcessor() {

			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {

				JSONObject j = new JSONObject();
				
		    	for (int i=0;i < bean.getClass().getMethods().length;i++) {
		    		
		    		if (bean.getClass().getMethods()[i].getName().startsWith("get") &&
		    			!bean.getClass().getMethods()[i].getReturnType().equals(Class.class) &&
		    			!bean.getClass().getMethods()[i].getReturnType().equals(Content.class) &&	
		    		    !bean.getClass().getMethods()[i].getReturnType().equals(Set.class)) {
		    			
		    			Method method;
		    			Object fieldValue = null;
		    			
		    			try {
							method = bean.getClass().getMethod(bean.getClass().getMethods()[i].getName(), null);
							fieldValue = method.invoke(bean, new Object[0]);							
						} 
		    			catch (SecurityException e1) {
							e1.printStackTrace();
						} 
						catch (NoSuchMethodException e1) {
							e1.printStackTrace();
						}
		    		    catch (Exception e) {
		    		    	e.printStackTrace();
		    		    }
		    		    String memberName = bean.getClass().getMethods()[i].getName();				    		
			    		
		    		    memberName = memberName.substring(3,4).toLowerCase() + memberName.substring(4,memberName.length());
			    		
			    		if (fieldValue == null)
			    			fieldValue = new Object();
			    		j.element(memberName, fieldValue,jsonConfig);

		    		}
		    	}
				
				return j;
				
			}
	    	
	    });
	            
	    
	    JSONArray jsonArray = JSONArray.fromObject(items,c);

	    if (metadata) {
		    jsonBuffer.append("'results': "+items.size()+", 'rows': ");
		    jsonBuffer.append("\n");	    	
	    }
	    
    	jsonBuffer.append(jsonArray);
    	
    	if (metadata) {
        	jsonBuffer.append("\n");
        	jsonBuffer.append("}");
        	jsonBuffer.append("\n");    		
    	}		
		try {
			out = response.getWriter();
			out.print(jsonBuffer);			
		    out.flush();		   
		} 
		catch (IOException e) {
			e.printStackTrace();
		}

	    
		return jsonBuffer.toString();
	}


	
	protected Object[] getPublicMembers(Class<?> dataObject) {
		
		ArrayList<String> members = new ArrayList<String>();
		
    	for (int i=0;i < dataObject.getMethods().length;i++) {
    		
			if(!dataObject.getMethods()[i].getReturnType().equals(Class.class) &&
			!dataObject.getMethods()[i].getReturnType().equals(Forum.class) &&
		    !dataObject.getMethods()[i].getReturnType().equals(Set.class)) {
				
				if (dataObject.getMethods()[i].getName().startsWith("get")) {					
					String memberName = dataObject.getMethods()[i].getName();
					memberName = memberName.substring(3,4).toLowerCase() + memberName.substring(4,memberName.length());				
					members.add(memberName);
				}
				else if (dataObject.getMethods()[i].getName().startsWith("is") && 
						 dataObject.getMethods()[i].getReturnType().equals(boolean.class)) {					
					String memberName = dataObject.getMethods()[i].getName();
					memberName = memberName.substring(2,3).toLowerCase() + memberName.substring(3,memberName.length());				
					members.add(memberName);
				}
				
			}
    	
    	}
    	    	
    	return members.toArray();
	}
	
	protected List<?> filterProcesses(List<Process>processes,String username) {
		
		List<Process> userProcesses = new ArrayList<Process>();
		
		// Fetch user and finally check if he exists or has already been logged on
		
		if (username == null ) {
			return new ArrayList<Process>();
		}
		
		User user = (User)DAOManager.getInstance().getUserDAO().findByName(username);
		
		if (user != null) {
			
			// get all groups, the user belongs to
			
			Set<Usergroup> groups = user.getGroups();			
			
			// user belongs to at least one group
			
			if (groups.size() > 0) {
				
				// Iterate through all existing processes
				
				Iterator<Process> processIterator = processes.iterator();
				
				while (processIterator.hasNext()) {
		
					Process currentProcess = (Process) processIterator.next();
					
					log.debug("assigned process :"+currentProcess.getName());
					
					// For each process iterate through all groups the user belongs to
					
					Iterator<Usergroup> userGroupIterator = groups.iterator();								
					
					while (userGroupIterator.hasNext()) {
						
						Usergroup currentGroup = (Usergroup) userGroupIterator.next();
						
						log.debug("user belongs to group : " + currentGroup.getName());
						
						// and finally if usergroup and process group are the same, add it to the list of user processes.
						
						if (currentProcess.getGroups().contains(currentGroup))
							if (currentProcess.getActive() != null)
								if (currentProcess.getActive()) 
									userProcesses.add(currentProcess);			
						
					}	
				}
			}
		}
		
		return userProcesses;
		
	}

	public Class<?> getDataObject() {
		return dataObject;
	}

	public void setDataObject(Class<?> dataObject) {
		this.dataObject = dataObject;
	}

	
}
