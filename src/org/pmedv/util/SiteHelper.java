/**
 * WeberknechtCMS - Open Source Content Management 
 * 
 * Written and maintained by Matthias Pueski
 * 
 * Copyright (c) 2009 Matthias Pueski
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 */
package org.pmedv.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.pmedv.cms.common.CMSProperties;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.Content;
import org.pmedv.pojos.Node;
import org.pmedv.pojos.Usergroup;
import org.pmedv.web.ServerUtil;

public class SiteHelper {

	public static String createSiteMapXML(ServletRequest request) {

		String protocol = CMSProperties.getInstance().getAppProps().getProperty("protocol");
		
		Config config = (Config) DAOManager.getInstance().getConfigDAO().findByID(1);
		String HOSTED_DOMAIN = protocol+"://" + ServerUtil.getHostUrl(request);

		StringBuffer xmlBuffer = new StringBuffer();

		xmlBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		xmlBuffer.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

		Iterator<?> nodeIterator = DAOManager.getInstance().getNodeDAO().findAllItems().iterator();

		while (nodeIterator.hasNext()) {
			/*
			 * Pruefen ob der Node eine Gruppe zugeordnet ist wenn ja, ist die nicht fuer jeden
			 * sichtbar und wird dann nicht in der Sitemap gezeigt. wenn nein, wird diese
			 * aufgelistet.
			 */
			Node node = (Node) nodeIterator.next();
			Set<Usergroup> nodeGpoupSet = node.getGroups();

			if (nodeGpoupSet.isEmpty()) {

				xmlBuffer.append("<url>\n");

				xmlBuffer.append("<loc>" + HOSTED_DOMAIN + node.getName() + ".html</loc>\n");

				/*
				 * Pruefen ob der Node ein Inhalt oder ein Plugin angehaengt wurde Bei Plugins wird
				 * als lastmod heute genommen Bei Inhalten der gesetze Wert
				 */
				String lastmodString;
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Content content = node.getContent();
				if (content != null) {
					// Content
					Date lastmod = node.getContent().getLastmodified();

					if (lastmod != null) {
						lastmodString = formatter.format(lastmod);
					}
					else {
						lastmodString = formatter.format(System.currentTimeMillis());
					}
				}
				else {
					// Plugin
					lastmodString = formatter.format(System.currentTimeMillis());
				}

				xmlBuffer.append("<lastmod>" + lastmodString + "</lastmod>\n");
				xmlBuffer.append("<changefreq>daily</changefreq>\n");

				xmlBuffer.append("</url>\n");

			}
			else {
				// Node wird nicht in die Sitemap aufgenommen
			}

		}

		xmlBuffer.append("</urlset>\n");

		return xmlBuffer.toString();

	}

	public static void createContentFromODT(File file, String name) {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		OutputStreamWriter osw = null;

		try {
			osw = new OutputStreamWriter(bos, "ISO-8859-15");
		}
		catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		try {

			TransformerFactory tFactory = javax.xml.transform.TransformerFactory.newInstance();

			Config config = (Config) DAOManager.getInstance().getConfigDAO().findByID(1);
			String appPath = config.getBasepath();
			String xslLocation = appPath + "xsl/odt2xhtml.xsl";

			File xslFile = new File(xslLocation);

			Transformer transformer = tFactory.newTransformer(new StreamSource(xslFile));
			transformer.transform(new javax.xml.transform.stream.StreamSource(file), new StreamResult(osw));

			Content content = new Content();
			content.setContentname("Document");
			content.setContentstring(bos.toString());
			content.setDescription("Imported document");
			content.setPagename("Imported Document");
			content.setLastmodified(new Date());

			DAOManager.getInstance().getContentDAO().createAndStore(content);

		}
		catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}
		catch (TransformerException e) {
			e.printStackTrace();
		}

	}

}
