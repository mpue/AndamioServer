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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.pojos.Node;
import org.pmedv.util.NodeHelper;

public class NodeAliasPlugin extends AbstractPlugin implements IPlugin, Serializable {

	private static final long serialVersionUID = -3532001258807966598L;

	protected static Log log = LogFactory.getLog(NodeAliasPlugin.class);
	
	public NodeAliasPlugin() {
		super();
		pluginID = "NodeAliasPlugin";
		paramDescriptors.put("plugin_node_path", resourceBundle.getString("plugin.node.path"));
	}
	
	public String getContent() {
		
		String path = null;
		
		try {
			path = paramMap.get("plugin_node_path");	
		}
		catch (NullPointerException n) {
			return "You must provide a path to a node.";
		}
		
		if (path == null || path.trim().length() < 1)
			return "You must provide a path to a node.";
		
		Node node = NodeHelper.locateNodeByName(path);
		
		if (node == null)
			return "The specified node does not exist.";
		
		if (node.getContent() == null)
			return "The specified node has no content.";
		
		return node.getContent().getContentstring();

	}

}
