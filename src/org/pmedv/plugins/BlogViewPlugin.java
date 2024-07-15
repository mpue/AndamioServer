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
import java.util.ArrayList;
import java.util.List;

import org.htmlparser.filters.TagNameFilter;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.Node;
import org.pmedv.util.BlogUtil;

/**
 * <p>
 * This plugin displays an overview of all renetly added blog entries.
 * the number of entries to be displayed is controlled by the field <i>plugin_blog_maxentries</i>
 * </p>
 * <p>
 * In order to parametrize this plugin you may add parameters to the param descriptor
 * map inside the constructor.   
 * </p>
 * @see org.pmedv.plugins.SamplePlugin
 * 
 * @author Matthias Pueski
 * @version 27.2.2009
 *
 */
public class BlogViewPlugin extends AbstractPlugin implements IPlugin, Serializable {
		
	private static final long serialVersionUID = 7104049537478810618L;

	public BlogViewPlugin () {
		super();
		pluginID = "BlogViewPlugin";
		
		/*
		 * These are the plugIn parameters as mentioned, those params 
		 * are used to control the plugin and they are displayed as text input 
		 * fields in the NodeShowAction JSP Page
		 */
		
		paramDescriptors.put("plugin_blog_maxentries", resourceBundle.getString("plugin.blog.field.maxentries"));

	}
	
	/* (non-Javadoc)
	 * @see org.pmedv.plugins.IPlugin#getContent()
	 */
    @Override
	public String getContent() {
		
		// Fetch the params from the param map

		int  maxentries;
		
		try {
			maxentries = Integer.parseInt(paramMap.get("plugin_blog_maxentries").trim());
		}
		catch (NullPointerException n) {
			maxentries = 0;
		}
	
		List<?> nodes = DAOManager.getInstance().getNodeDAO().findAllItems();
		
		
		StringBuffer tableBuffer    = new StringBuffer();
		TagNameFilter spanTagFilter = new TagNameFilter("SPAN");
		ArrayList<Node> nodeList    = new ArrayList<Node>();

		int contentCount = 0;
		
		for (Object o : nodes) {

			Node currentNode = (Node)o;
			
            if (currentNode.getType() == Node.TYPE_CONTENT) {
            	
            	// the node to be added must a parent, if it has
            	// no parent it is a category node and must not be listed here 
            	
            	if (currentNode.getParent() != null) {
            		nodeList.add(currentNode);
            		contentCount++;            		
            	}
            }

		}

		if (contentCount < maxentries) {
			maxentries = contentCount;
		}

		BlogUtil.sortNodesByCreated(nodeList);
		
		for (int i=0; i < maxentries;i++) {
			BlogUtil.createBlogEntry(tableBuffer, spanTagFilter,nodeList.get(i));
		}
		
		return tableBuffer.toString();
		
	}


}
