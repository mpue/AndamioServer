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

package org.pmedv.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.ImageIcon;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatch;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.beans.objects.AccountBean;
import org.pmedv.beans.objects.FolderBean;
import org.pmedv.beans.objects.MailServerAccountType;
import org.pmedv.beans.objects.UserBean;
import org.pmedv.context.AppContext;
import org.pmedv.services.UserMailService;
import org.pmedv.services.UserService;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ByteArrayResource;

/**
 * Static utility class for handling resources
 * 
 * @author Matthias Pueski
 *
 */
public class ResourceUtils {
	
	private static final Log log = LogFactory.getLog(ResourceUtils.class);
	
	public static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy kk:mm");
	
	/**
	 * Writes a given <code>InputStream</code> to a file.
	 * 
	 * @param is
	 * @param out
	 */
	public static void writeStreamToFile(InputStream is, File out) {
		
	    byte[] b;

	    try {
	    	
	    	FileOutputStream fos = new FileOutputStream(out);
	    	
			b = new byte[is.available()];

			for (int n;(n = is.read(b)) != -1;) {			    	
		    	fos.write(b);			        
		    }
			
			fos.close();
			
		} 
	    catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * Creates a {@link ByteArrayResource} from an input stream
	 * 
	 * @param is  the stream to read from
	 * @return	  the resource
	 */
	
	public static ByteArrayResource createResourceFromStream(InputStream is) {
						
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
	    byte[] b;

	    try {
			b = new byte[is.available()];

			for (int n;(n = is.read(b)) != -1;) {			    	
		    	bos.write(b);			        
		    }
			
		} 
	    catch (IOException e) {
			e.printStackTrace();
		}
		
		return new ByteArrayResource(bos.toByteArray());
		
	}
	
	/**
	 * Configures a factory from a properties file read from an input stream
	 * 
	 * @param is       the stream to read the properties from 
	 * @param factory  the factory to configure
	 */
	public static Properties configureFactoryFromInputStream(InputStream is, XmlBeanFactory factory ) {
		
		PropertyPlaceholderConfigurer cfg = new PropertyPlaceholderConfigurer();
		
		Properties p = new Properties();

		try {
			p.load(is);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}

		cfg.setProperties(p);
		cfg.postProcessBeanFactory(factory);
		
		return p;
	}
	
    /**
     * Create an ImageIcon from the image at the specified path
     * 
     * @param path The path of the image
     * 
     * @return The image icon, or null if the image doesn't exist
     */
    public static ImageIcon createImageIcon(String path)
    {
    	
        URL u = Thread.currentThread().getContextClassLoader().getResource(path);
    	
    	// TODO : Check if that works with webstart
    	
        // URL u = ClassLoader.getSystemResource(realPath);//UIUtils.class.getResource(path);
        
        if(u == null)
            return null;
        return new ImageIcon(u);
    }
    
    /**
     * Prints a stackstrace into a string
     * 
     * @param t The throwable to print the trace for
     * 
     * @return a string containing the stacktrace
     */
    public static String getStackTrace(Throwable t)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        t.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }

	/**
	 * Determines the mime type for a given file
	 * 
	 * @param fileToBeOpened the file to get the mime type for
	 * 
	 * @return the mime type
	 */
	public static String getMimeType(File fileToBeOpened) {

		String fileExtension = fileToBeOpened.getName().substring(fileToBeOpened.getName().lastIndexOf(".") + 1);

		String mimeType = null;

		try {
			MagicMatch match = Magic.getMagicMatch(fileToBeOpened, true);
			match.getMimeType();
			mimeType = match.getMimeType();
			log.info("found mime type " + mimeType);
		}
		catch (MagicMatchNotFoundException m) {

			if (fileToBeOpened.length() < 1) { // empty file, assume text mime
				// type
				mimeType = "text/plain";
				log.info("found empty file, assuming text format.");
			}
			else {
				log.info("Mimetype for file " + fileToBeOpened.getName() + " not found.");

				if (fileExtension.equalsIgnoreCase("txt") || fileExtension.equalsIgnoreCase("xsl")
						|| fileExtension.equalsIgnoreCase("log") || fileExtension.equalsIgnoreCase("css"))
					mimeType = "text/plain";
				else if (fileExtension.equalsIgnoreCase("xml")) {
					mimeType = "text/xhtml";
				}
				else if (fileExtension.equalsIgnoreCase("mp3")) {
					mimeType = "audio/mpeg";
				} 
				else if (fileExtension.equalsIgnoreCase("ogg")) {
					mimeType = "audio/ogg";
				} 				
				else if (fileExtension.equalsIgnoreCase("wav")) {
					mimeType = "audio/x-wav";
				} 
				else if (fileExtension.equalsIgnoreCase("wma")) {
					mimeType = "audio/x-ms-wma";
				} 
				
				else
					mimeType = "unknown";
			}
			return mimeType;

		}
		catch (MagicParseException e) {
			return "unknown";
		}
		catch (MagicException e) {
			return "unknown";
		}

		if (mimeType.startsWith("text")) {

			if (fileExtension.equalsIgnoreCase("xml") || fileExtension.equalsIgnoreCase("xsl")
					|| fileExtension.equalsIgnoreCase("jnlp"))
				mimeType = "text/xhtml";
			else if (fileExtension.equalsIgnoreCase("htm") || fileExtension.equalsIgnoreCase("html")
					|| fileExtension.equalsIgnoreCase("xhtml"))
				mimeType = "text/xhtml";
			else if (fileExtension.equalsIgnoreCase("bsh") || fileExtension.equalsIgnoreCase("BSH"))
				mimeType = "text/cpp";

		}

		if (mimeType.endsWith("x-c") || mimeType.endsWith("java")) {
			mimeType = "text/cpp";
		}

		if (mimeType.equals("text/html") || mimeType.equals("text/sgml"))
			mimeType = "text/xhtml";

		if (mimeType.contains("???")) {
			
			if (fileExtension.equalsIgnoreCase("txt") || 
				fileExtension.equalsIgnoreCase("xsl") || 
				fileExtension.equalsIgnoreCase("log") || 
				fileExtension.equalsIgnoreCase("css"))
				mimeType = "text/plain";
			else if (fileExtension.equalsIgnoreCase("xml")) {
				mimeType = "text/xhtml";
			}
			else if (fileExtension.equalsIgnoreCase("mp3")) {
				mimeType = "audio/mpeg";
			} 
			else if (fileExtension.equalsIgnoreCase("ogg")) {
				mimeType = "audio/ogg";
			} 				
			else if (fileExtension.equalsIgnoreCase("wav")) {
				mimeType = "audio/x-wav";
			} 
			else if (fileExtension.equalsIgnoreCase("wma")) {
				mimeType = "audio/x-ms-wma";
			} 
			
			else
				mimeType = "unknown";

		}
		
		log.info("using mime type " + mimeType);

		return mimeType;
	}
	
	/**
	 * Copy from one stream to another
	 * 
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	public static void copyStream(final InputStream in, final OutputStream out) throws IOException {
		try {
			final int BUFF_SIZE = 8192;
			final byte[] buffer = new byte[BUFF_SIZE];
			int amountRead, totalSize = -1;

			log.info("Remaining bytes : " + in.available());

			while (true) {
				amountRead = in.read(buffer);
				if (amountRead == -1)
					break;
				out.write(buffer, 0, amountRead);
			}
		}
		finally {
			if (in != null)
				in.close();
			if (out != null)
				out.close();
		}
	}	
	
	// TODO : wtf am I doing here, move me to another class!!!
	/**
	 * Creates the default mail account for a given username
	 * 
	 * @param username the username to create the account for
	 */
	public static void createDefaultMailAccount(String username) {
		
		ApplicationContext ctx = AppContext.getApplicationContext();
		
	    UserService userService = (UserService)ctx.getBean("userService");
	    UserMailService mailService = (UserMailService)ctx.getBean("userMailService");
	    
	    UserBean user = userService.findByName(username);
	    
	    AccountBean account = new AccountBean();
	    account.setName("default");
	    account.setType(MailServerAccountType.INTERNAL);
	    
	    mailService.addAccount(user,account);
	    
	    ArrayList<AccountBean> accounts = mailService.getAccounts(user);	    
	    
	    for (AccountBean ab : accounts) {
	    	
	    	if (ab.getName().equals("default")) {

	    	    FolderBean inbox = new FolderBean();
	    	    inbox.setName("key.inbox");
	    	    
	    	    mailService.addFolder(ab, inbox);

	    	    FolderBean outbox = new FolderBean();
	    	    outbox.setName("key.outbox");

	    	    mailService.addFolder(ab, outbox);
	    	    
	    	    FolderBean archive = new FolderBean();
	    	    archive.setName("key.archive");

	    	    mailService.addFolder(ab, archive);

	    	}
	    	
	    }

	}	
}
