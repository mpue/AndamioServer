package org.pmedv.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;

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
import org.apache.xmlgraphics.util.MimeConstants;
import org.w3c.tidy.Tidy;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * <p>
 * Static helper class for document generation with Apache FOP
 * </p>
 * <p>
 * For further documentation visit <a href="http://xmlgraphics.apache.org/fop/">the apache FOP
 * site</a>
 * 
 * @author Matthias Pueski
 * 
 */
public class DocumentBuilder {

	private static final Log log = LogFactory.getLog(DocumentBuilder.class);

	/**
	 * <p>
	 * Converts an html string to a FOP processed document and writes it to the given
	 * <code>OutputStream</code>
	 * </p>
	 * <p>
	 * If you want to create a pdf document and need to write it to a servlet <code>OutputStream</code>
	 * you might call for example:
	 * <p>
	 * <code>
	 * <i>DocumentBuilder.createDocument(html, MimeConstants.MIME_PDF, xslLocation, response.getOutputStream());</i>
	 * </code> 
	 * </p>
	 * @param html The input as html string
	 * @param outputFormat the output format @see {@link MimeConstants}
	 * @param xslLocation the absolute location of the XSLT stylesheet
	 * 
	 * @param out the <code>OutputStream</code> to write to
	 */
	public static void createDocument(String html, String outputFormat, String xslLocation, OutputStream out) {

		/**
		 * Tidy cleans up html and converts it to xhtml
		 */
		
		Tidy tidy = new Tidy();
		tidy.setXHTML(true);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ByteArrayOutputStream documentOutputStream = new ByteArrayOutputStream();

		try {

			InputStream in = new ByteArrayInputStream(html.getBytes());
			tidy.parse(in, bos);

			log.debug(bos.toString());

			// 1. Instantiate a TransformerFactory.

			TransformerFactory tFactory = SAXTransformerFactory.newInstance();

			// 2. Use the TransformerFactory to process the stylesheet Source
			// and generate a Transformer.

			File xslFile = new File(xslLocation);

			try {

				Transformer transformer = tFactory.newTransformer(new StreamSource(xslFile));

				SAXParserFactory spf = SAXParserFactory.newInstance();
				
				spf.setNamespaceAware(true);
				spf.setValidating(false); // Turn off validation
				spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

				XMLReader reader;

				reader = spf.newSAXParser().getXMLReader();				
				transformer.transform(new SAXSource(reader, new InputSource(new StringReader(bos.toString()))),
						new StreamResult(documentOutputStream));
			}
			catch (SAXException e) {
				log.error("A SAXException occured : " + e.getMessage());
				return;
			}
			catch (ParserConfigurationException e) {
				log.error("A ParserConfigurationException occured : " + e.getMessage());
				return;
			}

			log.debug("Loading transformation from " + xslLocation);

			// 3. Use the Transformer to transform an XML Source and send the output to a Result object.

			log.debug(documentOutputStream.toString());

		}
		catch (TransformerConfigurationException e) {
			log.error("A TransformerConfigurationException occured : " + e.getMessage());
			return;
		}
		catch (TransformerException e) {
			log.error("A TransformerException occured : " + e.getMessage());
			return;
		}
		
		ByteArrayInputStream pdfInputStream = new ByteArrayInputStream(documentOutputStream.toByteArray());

		// Step 1: Construct a FopFactory

		FopFactory fopFactory = FopFactory.newInstance();
		
		// Step 2: Set up output stream.

		try {

			// Step 3: Construct fop with desired output format

			Fop fop = fopFactory.newFop(outputFormat, out);
			
			// Step 4: Setup JAXP using identity transformer

			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(); // identity

			// Step 5: Setup input and output for XSLT transformation

			Source src = new StreamSource(pdfInputStream);

			// Resulting SAX events (the generated FO) must be piped through to FOP
			
			Result res = new SAXResult(fop.getDefaultHandler());

			// Step 6: Start XSLT transformation and FOP processing
			
			transformer.transform(src, res);

			out.close();
		}
		catch (Exception e) {
			log.error("An Exception occured : " + e.getMessage());
		}

	}
	
	/**
	 * Applies an xslt transform based on a given document and an XSLT-stylesheet
	 * 
	 * @param xslLocation the location of the stylesheet
	 * @param document	  the document to be transformed
	 * @param out		  the output to write to
	 */
	public static void applyXsltTransform(String xslLocation, String document, OutputStream out) {

			// 1. Instantiate a TransformerFactory.

			TransformerFactory tFactory = SAXTransformerFactory.newInstance();

			// 2. Use the TransformerFactory to process the stylesheet Source
			// and generate a Transformer.

			File xslFile = new File(xslLocation);

			try {

				Transformer transformer = tFactory.newTransformer(new StreamSource(xslFile));

				SAXParserFactory spf = SAXParserFactory.newInstance();
				
				spf.setNamespaceAware(true);
				spf.setValidating(false); // Turn off validation
				spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

				XMLReader reader;

				reader = spf.newSAXParser().getXMLReader();				
				transformer.transform(new SAXSource(reader, new InputSource(new StringReader(document))),
						new StreamResult(out));
			}
			catch (SAXException e) {
				log.error("A SAXException occured : " + e.getMessage());
			}
			catch (ParserConfigurationException e) {
				log.error("A ParserConfigurationException occured : " + e.getMessage());
			}
			catch (TransformerConfigurationException e) {
				log.error("A TransformerConfigurationException occured : " + e.getMessage());
			}
			catch (TransformerException e) {
				log.error("A TransformerException occured : " + e.getMessage());
			}
		
	}
	
	/**
	 * Applies an xslt transform based on a given document and an XSLT-stylesheet
	 * 
	 * @param xslLocation the location of the stylesheet
	 * @param document	  the document to be transformed
	 * @param out		  the output to write to
	 */
	public static void applyXsltTransform(File xslFile, String document, OutputStream out) {

			// 1. Instantiate a TransformerFactory.

			TransformerFactory tFactory = SAXTransformerFactory.newInstance();

			// 2. Use the TransformerFactory to process the stylesheet Source
			// and generate a Transformer.

			try {

				Transformer transformer = tFactory.newTransformer(new StreamSource(xslFile));

				SAXParserFactory spf = SAXParserFactory.newInstance();
				
				spf.setNamespaceAware(true);
				spf.setValidating(false); // Turn off validation
				spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

				XMLReader reader;

				reader = spf.newSAXParser().getXMLReader();				
				transformer.transform(new SAXSource(reader, new InputSource(new StringReader(document))),
						new StreamResult(out));
			}
			catch (SAXException e) {
				log.error("A SAXException occured : " + e.getMessage());
			}
			catch (ParserConfigurationException e) {
				log.error("A ParserConfigurationException occured : " + e.getMessage());
			}
			catch (TransformerConfigurationException e) {
				log.error("A TransformerConfigurationException occured : " + e.getMessage());
			}
			catch (TransformerException e) {
				log.error("A TransformerException occured : " + e.getMessage());
			}
		
	}
	
	public static void createPDFfromFOP(String fopDocument, OutputStream out) {
		
		ByteArrayInputStream pdfInputStream = new ByteArrayInputStream(fopDocument.getBytes());

		// Step 1: Construct a FopFactory

		FopFactory fopFactory = FopFactory.newInstance();
		
		// Step 2: Set up output stream.

		try {

			// Step 3: Construct fop with desired output format

			Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);
			
			// Step 4: Setup JAXP using identity transformer

			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(); // identity

			// Step 5: Setup input and output for XSLT transformation

			Source src = new StreamSource(pdfInputStream);

			// Resulting SAX events (the generated FO) must be piped through to FOP
			
			Result res = new SAXResult(fop.getDefaultHandler());

			// Step 6: Start XSLT transformation and FOP processing
			
			transformer.transform(src, res);

			out.close();
		}
		catch (Exception e) {
			log.error("An Exception occured : " + e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public static void createTextDocument(String html, String xslLocation, OutputStream out) {

		/**
		 * Tidy cleans up html and converts it to xhtml
		 */
		
		Tidy tidy = new Tidy();
		tidy.setXHTML(true);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {

			InputStream in = new ByteArrayInputStream(html.getBytes());
			tidy.parse(in, bos);

			log.debug(bos.toString());

			// 1. Instantiate a TransformerFactory.

			TransformerFactory tFactory = SAXTransformerFactory.newInstance();

			// 2. Use the TransformerFactory to process the stylesheet Source
			// and generate a Transformer.

			File xslFile = new File(xslLocation);

			try {

				Transformer transformer = tFactory.newTransformer(new StreamSource(xslFile));

				SAXParserFactory spf = SAXParserFactory.newInstance();
				
				spf.setNamespaceAware(true);
				spf.setValidating(false); // Turn off validation
				spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

				XMLReader reader;

				reader = spf.newSAXParser().getXMLReader();				
				transformer.transform(new SAXSource(reader, new InputSource(new StringReader(bos.toString()))),
						new StreamResult(out));
			}
			catch (SAXException e) {
				log.error("A SAXException occured : " + e.getMessage());
				return;
			}
			catch (ParserConfigurationException e) {
				log.error("A ParserConfigurationException occured : " + e.getMessage());
				return;
			}

			log.debug("Loading transformation from " + xslLocation);

			// 3. Use the Transformer to transform an XML Source and send the output to a Result object.

			log.debug(out.toString());

		}
		catch (TransformerConfigurationException e) {
			log.error("A TransformerConfigurationException occured : " + e.getMessage());
			return;
		}
		catch (TransformerException e) {
			log.error("A TransformerException occured : " + e.getMessage());
			return;
		}


	}
	

}
