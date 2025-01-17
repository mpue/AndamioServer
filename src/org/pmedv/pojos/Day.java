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
package org.pmedv.pojos;

public class Day {
	
	public Day() {		
	}

	private int date;
	private int dayIndex;
	/**
	 * @return Returns the date.
	 */
	public int getDate() {
		return date;
	}
	/**
	 * @param date The date to set.
	 */
	public void setDate(int date) {
		this.date = date;
	}
	/**
	 * @return Returns the dayIndex.
	 */
	public int getDayIndex() {
		return dayIndex;
	}
	/**
	 * @param dayIndex The dayIndex to set.
	 */
	public void setDayIndex(int dayIndex) {
		this.dayIndex = dayIndex;
	}
	
	
	
}
