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

public class ProcessComparator implements Comparator<org.pmedv.pojos.Process>, Serializable {

	private static final long serialVersionUID = -3756470244216397521L;

	public int compare(org.pmedv.pojos.Process p1, org.pmedv.pojos.Process p2) {

		try {

			String pos1 = Integer.toString(p1.getPosition());
			String pos2 = Integer.toString(p2.getPosition());

			if (pos1.length() < 2)
				pos1 = "0" + pos1;
			if (pos2.length() < 2)
				pos2 = "0" + pos2;

			// return this result
			return pos1.compareTo(pos2);
		}
		catch (NullPointerException n) {
			return 0;
		}
	}

}
