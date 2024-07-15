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
package org.pmedv.comparator;

import java.io.Serializable;
import java.util.Comparator;

import org.pmedv.pojos.mailsystem.Folder;

public class FolderComparator implements Comparator<Folder>, Serializable {

	private static final long serialVersionUID = 7845317073852832216L;

	public int compare(Folder f1, Folder f2) {

		try {

			return f1.getName().compareTo(f2.getName());
		}
		catch (NullPointerException n) {
			return 0;
		}
	}

}
