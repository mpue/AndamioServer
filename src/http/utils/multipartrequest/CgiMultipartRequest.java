package http.utils.multipartrequest;

import java.io.IOException;

/**
    A Multipart form data parser.  Parses an input stream and writes out any files found, 
    making available a hashtable of other url parameters.  As of version 1.17 the files can
    be saved to memory, and optionally written to a database, etc.
    
    <BR>
    <BR>
    Copyright (c)2001-2003 Jason Pell.
    <BR>

    <PRE>
    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.
    <BR>
    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.
    <BR>
    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
    <BR>    
    Email:  jasonpell@hotmail.com
    Url:    http://www.geocities.com/jasonpell
    </PRE>
	
	Wrapper for MultipartRequest
*/
public class CgiMultipartRequest extends MultipartRequest
{
	/** 
	 * Standard Constructor
	 *
	 * @param strSaveDirectory		The temporary directory to save the file from where they can then be moved to wherever by the
	 * 								calling process.  <b>If you specify <u>null</u> for this parameter, then any files uploaded
	 *								will be silently ignored.</B>
	 *
	 * @exception IllegalArgumentException 	If the System.getProperty("CONTENT_TYPE") does not contain a Content-Type of "multipart/form-data" or the boundary is not found.
	 * @exception IOException				If the System.getProperty("CONTENT_LENGTH") is higher than MAX_READ_BYTES or strSaveDirectory is invalid or cannot be written to.
	 *
	 * @see MultipartRequest#MAX_READ_BYTES
	 */
	public CgiMultipartRequest(String strSaveDirectory) throws IllegalArgumentException, IOException, NumberFormatException
	{
		super(null, 
			System.getProperty("CONTENT_TYPE"), 
			Integer.parseInt(System.getProperty("CONTENT_LENGTH")), 
			System.in, 
			strSaveDirectory,
			MultipartRequest.MAX_READ_BYTES,
			null);//encoding
	}

	/** 
	 * Standard Constructor
	 *
	 * @param strSaveDirectory		The temporary directory to save the file from where they can then be moved to wherever by the
	 * 								calling process.  <b>If you specify <u>null</u> for this parameter, then any files uploaded
	 *								will be silently ignored.</B>
	 * @param intMaxReadBytes		Overrides the MAX_BYTES_READ value, to allow arbitrarily long files.
	 *
	 * @exception IllegalArgumentException 	If the System.getProperty("CONTENT_TYPE") does not contain a Content-Type of "multipart/form-data" or the boundary is not found.
	 * @exception IOException				If the System.getProperty("CONTENT_LENGTH") is higher than MAX_READ_BYTES or strSaveDirectory is invalid or cannot be written to.
	 *
	 * @see MultipartRequest#MAX_READ_BYTES
	 */
	public CgiMultipartRequest(String strSaveDirectory, int intMaxReadBytes) throws IllegalArgumentException, IOException, NumberFormatException
	{
		super(null, 
			System.getProperty("CONTENT_TYPE"), 
			Integer.parseInt(System.getProperty("CONTENT_LENGTH")), 
			System.in, 
			strSaveDirectory,
			intMaxReadBytes,
			null);////encoding
	}

	/** 
	 * Memory Constructor
	 *
//	 * @param debug					A PrintWriter that can be used for debugging.
	 * @param intMaxReadBytes		Overrides the MAX_BYTES_READ value, to allow arbitrarily long files.
	 *
	 * @exception IllegalArgumentException 	If the System.getProperty("CONTENT_TYPE") does not contain a Content-Type of "multipart/form-data" or the boundary is not found.
	 * @exception IOException				If the System.getProperty("CONTENT_LENGTH") is higher than MAX_READ_BYTES or strSaveDirectory is invalid or cannot be written to.
	 *
	 * @see MultipartRequest#MAX_READ_BYTES
	 */
	public CgiMultipartRequest(int intMaxReadBytes) throws IllegalArgumentException, IOException, NumberFormatException
	{
 	    super(null, 
			System.getProperty("CONTENT_TYPE"), 
			Integer.parseInt(System.getProperty("CONTENT_LENGTH")), 
			System.in, 
			intMaxReadBytes,
			null);//encoding
	}
}