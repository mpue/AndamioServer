/**

	Weberknecht AndamioManager - Open Source Content Management
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

import org.pmedv.beans.objects.NodeBean;



/**
 * A DifferenceEntry is generated for a node compare, once a remote node differs
 * to a local node (new or changed), a new difference entry is created and added to a list
 * of differing nodes. 
 * 
 * @author Matthias Pueski
 *
 */
public class DifferenceEntry {

	private NodeBean node;
	private DifferenceType differenceType;
	
	/**
	 * Creates a new <code>DifferenceEntry</code> with a node an the type of the difference.
	 * 
	 * @param node
	 * @param differenceType
	 */
	public DifferenceEntry(NodeBean node, DifferenceType differenceType) {
		this.node = node;
		this.differenceType = differenceType;
	}
	
	/**
	 * Gets the node
	 * 
	 * @return the node
	 */
	public NodeBean getNode() {
		return node;
	}
	
	/**
	 * Gets the type of difference
	 * 
	 * @return the DifferenceType
	 */
	
	public DifferenceType getDifferenceType() {
		return differenceType;
	}
	
}
