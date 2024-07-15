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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.Span;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.pmedv.core.html.Attribute;
import org.pmedv.core.html.Table;
import org.pmedv.core.html.TableColumn;
import org.pmedv.core.html.TableRow;
import org.pmedv.pojos.Node;

public class BlogUtil {

	private static final ResourceBundle resources = ResourceBundle.getBundle("MessageResources");
	private static final Log log = LogFactory.getLog(BlogUtil.class);
	
	/**
	 * Creates a sortet list of nodes in order to display a category list
	 * This is necessary, because we need to sort the nodes by the last modified time.
	 * 
	 * @param node the node to prepare the category from.
	 * @return
	 */
	public static List<Node> prepareCategory(Node node) {
		
		List <Node> nodes = new ArrayList<Node>();
		
		for (Node n : node.getChildren()) {
			nodes.add(n);
		}
		
		/*
		 * Sort the list by date.
		 */
		
		sortNodesByCreated(nodes);
		
		return nodes;
	}

	public static void sortNodesByCreated(List<Node> nodes) {
		
		Collections.sort( nodes, new Comparator<Node>()
		{
			public int compare(Node n1, Node n2) {
				
				if (n1.getContent().getCreated() != null && n2.getContent().getCreated() != null) {
					if (n1.getContent().getCreated().after(n2.getContent().getCreated())) {
						return -1;
					} 
					else if (n1.getContent().getCreated().before(n2.getContent().getCreated())) {
						return 1;
					} 
					else {
						return 0;
					}						
				}
				else 
					return -1;
				
			}

		});
	}

	/**
	 * Creates a blog entry and appends it to the given StringBuffer. 
	 * 
	 * @param tableBuffer    The StringBuffer to append to
	 * @param spanTagFilter  The TagNameFilter to use for filtering
	 * @param n				 The node to use for the blog entry
	 */
	public static void createBlogEntry(StringBuffer tableBuffer, TagNameFilter spanTagFilter, Node n) {
		
		boolean hasDescription = false;

		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

		String created;

		if (n.getContent() != null) {

		    if (n.getContent().getCreated() != null)
		        created = dateFormat.format(n.getContent().getCreated());
		    else
		        created = dateFormat.format(new Date());
		}
		else {
		    created = dateFormat.format(new Date());
		}

		Table table = new Table();

		table.setStyleClass("blogentry");
		table.setAlign(Attribute.Align.LEFT);
		table.setWidth("100%");
		table.setCellspacing("5px");
		table.setCellpadding("5px");

		TableRow row = new TableRow();

		TableColumn titleCol = new TableColumn();
		titleCol.setStyleClass("blogheader");
		titleCol.setData("<b><i>"+created +"</i> : "+n.getName()+"</b>");
		row.addColumn(titleCol);
		table.addRow(row);

		TableRow contentRow = new TableRow();

		TableColumn contentCol = new TableColumn();
		contentCol.setStyleClass("blogcontent");

		if (n.getContent() != null &&
		    n.getContent().getContentstring() != null &&
		    n.getContent().getContentstring().length() > 0) {

		    try {
		        Parser parser = new Parser();
		        parser.setInputHTML(n.getContent().getContentstring());
		        parser.setFeedback(Parser.STDOUT);

		        NodeList nodeList = parser.parse(spanTagFilter);

		        for (NodeIterator e = nodeList.elements();e.hasMoreNodes();) {

		            org.htmlparser.Node currentNode = e.nextNode();

		            if (currentNode instanceof Span) {
		                Span span = (Span) currentNode;

		                if (span.getAttribute("class") != null) {

		                    if (span.getAttribute("class").equalsIgnoreCase("pre")) {

		                        String html = new String(span.toHtml());

		                        html += "<p class=\"blogentrylink\"><a href=\""+ n.getPath() +".html\" target=\"_self\">"+resources.getString("blog.more")+"</a></p>";

		                        hasDescription = true;

		                        contentCol.setData(html);
		                    }
		                }

		            }
		        }
		        if (!hasDescription) {
		            StringBuffer html = new StringBuffer();
		            html.append(resources.getString("blog.nodescription"));
		            html.append("<p class=\"blogentrylink\"><a href=\""+ n.getPath() +".html\" target=\"_self\">"+resources.getString("blog.more")+"</a></p>");
		            contentCol.setData(html.toString());
		        }

		    }
		    catch (ParserException e) {
		        log.info("HTML Parser failed.");
		    }

		}

		contentRow.addColumn(contentCol);
		table.addRow(contentRow);

		tableBuffer.append(table.render());
	}
	
}
