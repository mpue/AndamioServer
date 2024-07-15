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

import http.utils.multipartrequest.MultipartRequest;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UploadManager {

	private static final Log log = LogFactory.getLog(UploadManager.class);

	private String filename = "";
	private MultipartRequest parser;
	private String name;
	private String filepath;
	private String directory;

	public UploadManager() {
	}

	public void upload() throws Exception {

		try {
			InputStream in = parser.getFileContents(name);
			
			log.info("Available bytes "+in.available());
			
			if (in != null) {
				String filename = parser.getBaseFilename(name);

				BufferedInputStream input = new BufferedInputStream(in);
			
				/**
				 * This is an ugly hack, since the parser delivers two bytes of additional data at the beginning,
				 * and I don't know where they come from. Since The Upload manager is only used to upload images
				 * for the gallery this should work. But I need to check this anyway later on.
				 * 				   
				 */
				
				// TODO : Check where the two additional bytes come from.
				
				input.skip(2);
				
				log.debug(" reading : " + filepath + filename);
				FileOutputStream file = new FileOutputStream(new File(filepath, filename));

				// Now copy contents of file - not efficient but easy!!!

				int read;
				byte[] buffer = new byte[4096];

				while ((read = input.read(buffer)) != -1) {
					file.write(buffer, 0, read);
				}

				file.close();
				input.close();
			}
			
		} 
		catch (FileNotFoundException f) {
			log.debug(" File not found : " + filepath + filename);
		} 
		catch (IOException i) {
			i.printStackTrace();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete() throws Exception {

		String FileLocation = filepath + filename;
		File fileDelete = new File(FileLocation);
		if (fileDelete.delete() == true)
			log.debug("deleted file : " + fileDelete.getAbsoluteFile());
		else
			log.debug("couldn't delete file : " + fileDelete.getAbsoluteFile());
	}

	/**
	 * @return Returns the filepath.
	 */
	public String getFilepath() {
		return filepath;
	}

	/**
	 * @param filepath
	 *            The filepath to set.
	 */
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the parser.
	 */
	public MultipartRequest getParser() {
		return parser;
	}

	/**
	 * @param parser
	 *            The parser to set.
	 */
	public void setParser(MultipartRequest parser) {
		this.parser = parser;
	}

	/**
	 * @param directory
	 *            The directory to set.
	 */
	public void setDirectory(String directory) {
		this.directory = directory;
	}

	/**
	 * @return Returns the directory.
	 */
	public String getDirectory() {
		return directory;
	}

}
