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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.xmlgraphics.util.MimeConstants;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.Node;
import org.pmedv.util.DocumentBuilder;
import org.w3c.tidy.Tidy;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * <p>
 * The content builder is a proxy action which filters the content.
 * </p>
 * <p>
 * The <code>ContentBuilderAction</code> in its current form is only used
 * to generate watermarks for image galleries.
 *</p>
 * 
 * @author Matthias Pueski 
 *
 */
public class ContentBuilderAction extends DispatchAction {

	protected static Log log = LogFactory.getLog(ContentBuilderAction.class);

	public ActionForward getImage(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		boolean watermark = false;
		
		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);
		
		String imagename = request.getParameter("name")+".jpg";
		String imagePath = config.getBasepath() + config.getImagepath() + imagename;
		
		log.debug("Getting image from location : "+imagePath);
		
		File imageFile = new File(imagePath) ;
		
		try {
			
			if (request.getParameter("watermark") != null) {
			
				log.debug("generating image watermark.");
				
				String paramWatermark = request.getParameter("watermark");
				
				if (paramWatermark.equalsIgnoreCase("true")) {
					watermark = true;
				}
			}
			
			BufferedImage image = ImageIO.read(imageFile);
			
			// TODO : Skip thumbnail generation for small images, maybe we find a smarter solution later?
			
			if (image.getHeight() > 200 && image.getWidth() > 320 && watermark) {

				Graphics2D g2d = (Graphics2D)image.getGraphics();				
				g2d.drawImage(image, 0, 0, null);
				
				// Modify the image and add a watermark
				
				AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f);
				g2d.setComposite(alpha);
	
	            g2d.setColor(Color.white);
	            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	
	            g2d.setFont(new Font("Arial", Font.BOLD, 20));
	
	            // TODO : make watermarking configurable
	            
	            String watermarkString = "(c) Thorsten Janes";
	
	            FontMetrics fontMetrics = g2d.getFontMetrics();
	            Rectangle2D rect = fontMetrics.getStringBounds(watermarkString, g2d);
	
	            g2d.drawString(watermarkString,
	                          (image.getWidth()  - (int) rect.getWidth()) / 2,
	                           image.getHeight() - 30);
	
	            // Free graphic resources and write image to output stream
	            
	            g2d.dispose();
			}

			response.setContentType("image/jpeg");
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "jpg", out);
            
            out.close();                        
            
		} 
		catch (IOException e) {
			return null;
		}

		return null;
		
	}
	
	
	
	/**
	 * Reads any data from an url and passes it to the output stream 
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * 
	 * @return nothing
	 */
	public ActionForward passThrough(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		byte [] b = new byte[1];  
		
		DataInputStream dataIn = null;
		
		try {
			
			ServletOutputStream s =  response.getOutputStream();
			
			URL url = new URL(request.getParameter("url"));
			URLConnection urlConnection = url.openConnection();
			urlConnection.connect();
			dataIn = new DataInputStream(urlConnection.getInputStream());
			
			while(-1 != dataIn.read(b,0,1)) {			
				s.write(b,0,1);
			}
			
		} 
		catch (MalformedURLException e1) {			
			log.debug("URL was malformed, please submit a correct url.");
		} 
		catch (IOException e) {
			log.debug("IO Exception occured.");
		}

		return null;
	}
	
	public ActionForward createDocument(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);
		String appPath = config.getBasepath();	
		String xslLocation = appPath+"xsl/xhtml2fo.xsl";
		
		Node n = (Node) DAOManager.getInstance().getNodeDAO().findByID(5);
		
		String html = n.getContent().getContentstring();
		String name = "test.pdf";
		
		response.setHeader("Content-Disposition", "attachment; filename=" + name);
		response.setContentType("application/pdf");
		
		try {
			DocumentBuilder.createDocument(html, MimeConstants.MIME_PDF, xslLocation, response.getOutputStream());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public void createDocument(String content,String name,String type,HttpServletResponse response) {
		
        Tidy tidy = new Tidy();
        tidy.setXHTML(true);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ByteArrayOutputStream documentOutputStream = new ByteArrayOutputStream();

		try {
			
			InputStream in = new ByteArrayInputStream(content.getBytes());
			tidy.parse(in, bos);
			
			log.info(bos.toString());
			
			// 1. Instantiate a TransformerFactory.
			
			TransformerFactory tFactory = SAXTransformerFactory.newInstance();
			
			// 2. Use the TransformerFactory to process the stylesheet Source
			// and generate a Transformer.

			Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);			
			String appPath = config.getBasepath();		
			
			String xslLocation = "";
			
			if (type.equalsIgnoreCase("pdf"))
				xslLocation = appPath+"xsl/xhtml2fo.xsl";
			else if (type.equalsIgnoreCase("tex")) {
				xslLocation = appPath+"xsl/html2latex.xsl";
			}
			
			File xslFile = new File(xslLocation);

			try {

				Transformer transformer = tFactory.newTransformer(new StreamSource(xslFile));
				
				SAXParserFactory spf = SAXParserFactory.newInstance();
				spf.setNamespaceAware(true);
				spf.setValidating(false); // Turn off validation
				spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
				
				XMLReader rdr;
				
				rdr = spf.newSAXParser().getXMLReader();
				transformer.transform(new SAXSource(rdr, new InputSource(new StringReader(bos.toString()))),new StreamResult(documentOutputStream));
			}
			catch (SAXException e) {
				e.printStackTrace();
			}
			catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
			
			log.info("Loading transformation from "+xslLocation);
									
			// 3. Use the Transformer to transform an XML Source and send the output to a Result object.

			log.info(documentOutputStream.toString());

		} 
		catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} 
		catch (TransformerException e) {
			e.printStackTrace();
		}

		if (type.equalsIgnoreCase("pdf")) {
			response.setHeader("Content-Disposition", "attachment; filename=" + name + ".png");
			// response.setContentType("application.pdf");
			response.setContentType("image/png");

			ByteArrayInputStream pdfInputStream = new ByteArrayInputStream(documentOutputStream.toByteArray());
			
			// Step 1: Construct a FopFactory
			// (reuse if you plan to render multiple documents!)

			FopFactory fopFactory = FopFactory.newInstance();

			// Step 2: Set up output stream.
			// Note: Using BufferedOutputStream for performance reasons (helpful
			// with FileOutputStreams).

			try {
				OutputStream out = response.getOutputStream();
				
				// Step 3: Construct fop with desired output format
							
				Fop fop = fopFactory.newFop(MimeConstants.MIME_PNG, out);

				// Step 4: Setup JAXP using identity transformer

				TransformerFactory factory = TransformerFactory.newInstance();
				Transformer transformer = factory.newTransformer(); // identity
				
				// transformer

				// Step 5: Setup input and output for XSLT transformation
				// Setup input stream

				Source src = new StreamSource(pdfInputStream);

				// Resulting SAX events (the generated FO) must be piped through to FOP
				Result res = new SAXResult(fop.getDefaultHandler());

				// Step 6: Start XSLT transformation and FOP processing
				transformer.transform(src, res);

				out.close();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		else if (type.equalsIgnoreCase("tex")) {
			// response.setHeader("Content-Disposition", "attachment; filename=" + name + ".tex");
			
			response.setContentType("text/plain");
			
			try {
				PrintWriter out = response.getWriter();				
				out.println(documentOutputStream.toString());
				out.flush();
				out.close();				
			} 
			catch (IOException e) {
				e.printStackTrace();
			}

		}
		
		

	}	

}
