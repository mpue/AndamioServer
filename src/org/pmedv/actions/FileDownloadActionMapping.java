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

import org.apache.struts.action.ActionMapping;


/**
 * This class defines a custom action mapping is used when downloading a file
 * from the file system.  To use it, set the type of an action mapping in
 * struts-config.xml to this classname.  All the attributes in the config file
 * are set with set-property elements.  Struts handles populating the instance
 * of this class with those values for us and the instance will be passed to
 * the Action indicated in the mapping.
 *
 * @since Struts 1.2.5
 * @author <a href="mailto:frank@zammetti.com">Frank W. Zammetti</a>
 */
public class FileDownloadActionMapping extends ActionMapping {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

// Full path to the file to download.  Note that this is NOT relative to the
  // webapp, it can be anywhere on the file system (local or remote via mapped
  // drives or mounted volumes)
  private String filePath;

  // The content type header to be set in the response
  private String contentType;

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public String getFilePath() {
    return this.filePath;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public String getContentType() {
    return this.contentType;
  }

}
