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
package org.pmedv.sandbox;

import java.io.File;
import java.io.IOException;

import net.sf.json.JSONSerializer;

import org.apache.commons.io.FileUtils;


public class JSONTest {

	
	
	public static void main(String[] args) throws IOException {

		String fileContent = FileUtils.readFileToString(new File("G:/devel/weberknecht/tomcat/webapps/andamio/index.jsp"));
		
		Data d = new Data();
		d.setContent(fileContent);
		
		System.out.println(JSONSerializer.toJSON(d));
		
	}
	
	
	public static  class Data {
		
		private String filename;
		private String content;

		/**
		 * @return the filename
		 */
		public String getFilename() {
		
			return filename;
		}
		
		/**
		 * @param filename the filename to set
		 */
		public void setFilename(String filename) {
		
			this.filename = filename;
		}
		
		/**
		 * @return the content
		 */
		public String getContent() {
		
			return content;
		}
		
		/**
		 * @param content the content to set
		 */
		public void setContent(String content) {
		
			this.content = content;
		}		
		
	}
}
