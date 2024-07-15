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

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Static helper class containing some convenience methods for string handling  
 * 
 * @author Matthias Pueski
 *
 */
public class StringUtil {

	private static final Log log = LogFactory.getLog(StringUtil.class); 
	
	/**
	 * Replaces a string sequence inside a given string
	 * 
	 * @param s       The string to search in
	 * @param search  The string to search for
	 * @param replace The replacement string
	 * 
	 * @return a new string with the replaced subsequences
	 */
	public static String replace(String s, String search, String replace) {
		
		try {
			StringBuffer s2 = new StringBuffer();
			int i = 0, j = 0;
			int len = search.length();
	
			while (j > -1) {
				j = s.indexOf(search, i);
	
				if (j > -1) {
					s2.append(s.substring(i, j));
					s2.append(replace);
					i = j + len;
				}
			}
			s2.append(s.substring(i, s.length()));
	
			return s2.toString();
		}
		catch (NullPointerException n) {
			return "";
		}
		
	}

	/**
	 * Replaces all occurences starting with http:// with 
	 * a real href to the according url.
	 * 
	 * @param content the content to make the replacement for
	 * 
	 * @return the content containing links 
	 */
	public static String generateLinks(String content) {

		int beginIndex = content.indexOf("http://");

		String Output = "";

		do {

			if (beginIndex > -1) {
				int endIndex = content.indexOf(" ", beginIndex);

				Output += content.substring(0, beginIndex);
				String url = content.substring(beginIndex, endIndex);
				Output += "<a href=\"" + url + "\" target=\"blank\">" + url
						+ "</a>";

				content = content.substring(endIndex);
				beginIndex = content.indexOf("http://");

			}
			
		} while (beginIndex > -1);

		Output += content;

		return Output;

	}

	/**
	 * Slurps an inputStream to a String
	 * 
	 * @param in  
	 * @return
	 * @throws IOException
	 */
	public static String inputStreamToString (InputStream in) throws Exception {
		
		if (in == null)
			throw new IllegalArgumentException("Inputstream must not be null.");

			
		StringBuffer out = new StringBuffer();
		
		byte[] b = new byte[4096];
		
		for (int n; (n = in.read(b)) != -1;) {
		    out.append(new String(b, 0, n));
		}
		
	    return out.toString();
	    
	}

}
