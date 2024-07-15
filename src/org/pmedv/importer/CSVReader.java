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
package org.pmedv.importer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.Ostermiller.util.ExcelCSVParser;

/**
 * Utility class for reading and parsing CVS files.
 * 
 * @author Matthias Pueski
 *
 */

public class CSVReader {
	
	protected static final Log log = LogFactory.getLog(CSVReader.class);
	
	private Vector <String[]> Data;
	private Vector <String> Lines;

	/**
	 * Reads CVS-data from file and stores ist inside a vector of strings.
	 * Each element of this vector contains one line.
	 * 
	 * @param filepath
	 * @param filename
	 */
	
	public CSVReader(String filepath, String filename) {
			
		BufferedReader in;
		String line;
		Vector <String> strings = new Vector <String> ();
		
		try {
			in = new BufferedReader(new FileReader(filepath + filename));
			
			while ((line = in.readLine()) != null) {
				strings.add(line);
			}		
			
			in.close();
		}
		catch(IOException e) {
			log.debug("CSVReader : "+e.getMessage());
		}
		
		this.Lines = strings;		
	}
	
	/**
	 * Parses the CVS-data inside the vector "lines" and stores it inside 
	 * a vector of string arrays. Each element of the vector contains a string array
	 * containing the single columns of the cvs-data
	 *
	 */
	
	public void parseCSVData(char delimiter) {

		String[][] values = null;
		Vector <String[]> data = new Vector <String[]>();	
		int length=0;
		Iterator<String> iterator = Lines.iterator();
		
		while (iterator.hasNext()) {
			
			String s = (String)iterator.next();
			
			try {
				values = ExcelCSVParser.parse(new StringReader(s),delimiter);
				
				length = values[0].length;
				
				String lines[] = new String[length];
				
				for (int i=0; i < values[0].length;i++) {				
					lines[i] = values[0][i];
				}
				data.add(lines);				
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			catch (NullPointerException e) {
				continue;
			}
		}

		this.Data = data;
		
	}

	/**
	 * @return Returns the data.
	 */
	public Vector<String[]> getData() {
		return Data;
	}

	/**
	 * @param data The data to set.
	 */
	public void setData(Vector<String[]> data) {
		Data = data;
	}

	/**
	 * @return Returns the lines.
	 */
	public Vector<String> getLines() {
		return Lines;
	}

	/**
	 * @param lines The lines to set.
	 */
	public void setLines(Vector<String> lines) {
		Lines = lines;
	}

	 
}
