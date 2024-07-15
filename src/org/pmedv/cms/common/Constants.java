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
package org.pmedv.cms.common;

/**
 * Collection of constants commonly used by all classes of
 * the system.
 * 
 * @author Matthias Pueski
 *
 */
public class Constants {

	public static final String VERSION = "3.1.1.iOMEDICO";
	public static final String APPNAME = "WeberknechtCMS";
	public static final String APPURL  = "http://www.weberknechtcms.org";
	public static final float  TAXRATE = 1.19f;
	
	public static interface Mode {
		public static final String BLOG = "blog";
		public static final String WEBSITE = "website";
		public static final String DOCUMENTUM = "documentum";
	}
	
}
