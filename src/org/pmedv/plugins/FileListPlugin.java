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
package org.pmedv.plugins;

import java.io.File;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.MissingResourceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.core.html.Attribute.Align;
import org.pmedv.core.html.Link;
import org.pmedv.core.html.Link.Target;
import org.pmedv.core.html.Table;
import org.pmedv.core.html.TableColumn;
import org.pmedv.core.html.TableRow;
import org.pmedv.pojos.Config;
import org.pmedv.web.ServerUtil;

public class FileListPlugin extends AbstractPlugin implements IPlugin, Serializable {

	private static final long serialVersionUID = -8710037384358952201L;

	private static transient final Log log = LogFactory.getLog(FileListPlugin.class);
	private static transient final DecimalFormat df = new DecimalFormat("0.00");
	private static transient final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	
	private static final Comparator<File> asc_fileNameComparator = new Comparator<File>() {
		@Override
		public int compare(File o1, File o2) {
			return o1.getName().compareTo(o2.getName());
		}		
	};

	private static final Comparator<File> asc_fileDateComparator = new Comparator<File>() {
		@Override
		public int compare(File o1, File o2) {
			return Long.valueOf(o1.lastModified()).compareTo(Long.valueOf(o2.lastModified()));
		}		
	};

	private static final Comparator<File> asc_fileSizeComparator = new Comparator<File>() {
		@Override
		public int compare(File o1, File o2) {
			return Long.valueOf(o1.length()).compareTo(Long.valueOf(o2.length()));
		}		
	};
	
	private static final Comparator<File> desc_fileNameComparator = new Comparator<File>() {
		@Override
		public int compare(File o1, File o2) {
			return o2.getName().compareTo(o1.getName());
		}		
	};

	private static final Comparator<File> desc_fileDateComparator = new Comparator<File>() {
		@Override
		public int compare(File o1, File o2) {
			return Long.valueOf(o2.lastModified()).compareTo(Long.valueOf(o1.lastModified()));
		}		
	};

	private static final Comparator<File> desc_fileSizeComparator = new Comparator<File>() {
		@Override
		public int compare(File o1, File o2) {
			return Long.valueOf(o2.length()).compareTo(Long.valueOf(o1.length()));
		}		
	};

	private static final HashMap<String, Comparator<File>> comparatorMap = new HashMap<String, Comparator<File>>();
	
	static {
		comparatorMap.put("date_asc",  asc_fileDateComparator);
		comparatorMap.put("file_asc",  asc_fileNameComparator);
		comparatorMap.put("size_asc",  asc_fileSizeComparator);
		comparatorMap.put("date_desc", desc_fileDateComparator);
		comparatorMap.put("file_desc", desc_fileNameComparator);
		comparatorMap.put("size_desc", desc_fileSizeComparator);
		
	}
	
	private Config config;
	
	boolean sortAscending = true;
	
	public FileListPlugin () {

		super();
		config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);
		// Locale.setDefault(Locale.ENGLISH);
		
		pluginID = "FileListPlugin";

		try {
			paramDescriptors.put("plugin_description", resourceBundle.getString("plugin.filelist.field.description"));			
		}
		catch (MissingResourceException m) {
			paramDescriptors.put("plugin_description", "plugin.filelist.field.description");
		}
		try {
			paramDescriptors.put("plugin_directory", resourceBundle.getString("plugin.filelist.field.directory"));	
		}
		catch (MissingResourceException m) {
			paramDescriptors.put("plugin_directory", "plugin.filelist.field.directory");
		}
		try {
			paramDescriptors.put("plugin_title", resourceBundle.getString("plugin.field.pagetitle"));	
		}
		catch (MissingResourceException m) {
			paramDescriptors.put("plugin_title", "plugin.field.pagetitle");
		}

	}	
	
	public String getContent() {
		
		if (request.getSession().getAttribute("login") == null) {
			return "You must be logged in in order to view files.";
		}
		
		String requestUrl = ServerUtil.getRequestUrl(request);
		String[] data = requestUrl.split("&");
		String nodeName = data[1].split("=")[1];
		
		String docBase = protocol+"://"+ServerUtil.getHostUrl(request)+"/"+config.getLocalPath()+nodeName+".html";
		
		StringBuilder content = new StringBuilder();
		
		String description;
		String directory;
		String title;
		
		try {
			description = paramMap.get("plugin_description").trim();
		}
		catch (NullPointerException n) {
			description = "";
		}		
		try {
			directory = paramMap.get("plugin_directory").trim();
		}
		catch (NullPointerException n) {
			directory = "";
		}
		try {
			title = paramMap.get("plugin_title").trim();
		}
		catch (NullPointerException n) {
			title = "";
		}
		
		if (directory.length() > 1) {
			
			String dirToRead = config.getBasepath()+directory;
			
			log.info("Reading directory : "+dirToRead);
			
			String downloadBase = protocol+"://"+ServerUtil.getHostUrl(request)+"/"+config.getLocalPath()+directory+"/";
			
			File dir = new File(dirToRead);			
			File[] files = dir.listFiles();
			
			if (files == null) {
				return "Directory <b>"+directory+"</b> does not exist.";
			}
			
			ArrayList<File> fileList = new ArrayList<File>();
			
			for (int i=0; i < files.length;i++) {
				fileList.add(files[i]);
			}
			
			String order = "file_asc";
			
			if (request.getParameter("sort") != null) {				
				order = request.getParameter("sort");
				Collections.sort(fileList, comparatorMap.get(order));
				sortAscending = !sortAscending;
			}
			else {
				sortAscending = true;
				Collections.sort(fileList, comparatorMap.get("file_asc"));
			}
			
			content.append("<p class=\"description\">");
			content.append(description);
			content.append("</p>");					
			
			Table t = new Table();
			t.setStyleClass("downloadTable");
			t.setCellpadding("2px");
			t.setCellspacing("2px");
			
			TableRow titlerow = new TableRow();
			titlerow.setStyleClass("fileTableHeader");
			
			TableColumn c1 = new TableColumn();
						
			if (sortAscending) {				
				c1.setData("<a href=\""+docBase+"&sort=date_asc\" target=\"_self\">Date</a>");				
			}
			else {
				c1.setData("<a href=\""+docBase+"&sort=date_desc\" target=\"_self\">Date</a>");
			}
			TableColumn c2 = new TableColumn();
			
			if (sortAscending) {
				c2.setData("<a href=\""+docBase+"&sort=file_asc\" target=\"_self\">File</a>");
			}
			else {
				c2.setData("<a href=\""+docBase+"&sort=file_desc\" target=\"_self\">File</a>");
			}
			
			TableColumn c3 = new TableColumn();
			
			if (sortAscending) {
				c3.setData("<a href=\""+docBase+"&sort=size_asc\" target=\"_self\">Size</a>");
			}				
			else {
				c3.setData("<a href=\""+docBase+"&sort=size_desc\" target=\"_self\">Size</a>");
			}
				
			if (order.equals("date_asc")) {
				c1.setStyleClass("dateHeader_asc");
				c2.setStyleClass("fileHeader");				
				c3.setStyleClass("sizeHeader");
			}
			else if (order.equals("date_desc")) {
				c1.setStyleClass("dateHeader_desc");
				c2.setStyleClass("fileHeader");				
				c3.setStyleClass("sizeHeader");
			}
			else if (order.equals("file_asc")) {				
				c1.setStyleClass("dateHeader");
				c2.setStyleClass("fileHeader_asc");
				c3.setStyleClass("sizeHeader");
			}
			else if (order.equals("file_desc")) {
				c1.setStyleClass("dateHeader");
				c2.setStyleClass("fileHeader_desc");
				c3.setStyleClass("sizeHeader");
			}			
			else if (order.equals("size_asc")) {
				c1.setStyleClass("dateHeader");
				c2.setStyleClass("fileHeader");
				c3.setStyleClass("sizeHeader_asc");
			}
			else if (order.equals("size_desc")) {
				c1.setStyleClass("dateHeader");
				c2.setStyleClass("fileHeader");
				c3.setStyleClass("sizeHeader_desc");
			}
			else {
				c1.setStyleClass("dateHeader");
				c2.setStyleClass("fileHeader");
				c3.setStyleClass("sizeHeader");				
			}
			titlerow.addColumn(c1);
			titlerow.addColumn(c2);
			titlerow.addColumn(c3);
			
			t.addRow(titlerow);
			
			String rowstyle = "even";
			
			for (int i = 0;i < files.length;i++) {
				
				if (files[i].isFile()) {
					
					TableRow row = new TableRow();
					
					if (rowstyle.equals("odd")) {
						row.setStyleClass("even");
						rowstyle = "even";
					}
					else {
						row.setStyleClass("odd");
						rowstyle = "odd";
					}
					
					TableColumn dateColumn = new TableColumn();
					
					dateColumn.setStyleClass("fileDate");					
					dateColumn.setData(sdf.format(fileList.get(i).lastModified()));
					
					Link l = new Link();

					l.setName(files[i].getName());
					l.setHref(downloadBase+fileList.get(i).getName());
					l.setTitle(fileList.get(i).getName());
					l.setData(fileList.get(i).getName());
					l.setTarget(Target.BLANK);
					
					TableColumn linkColumn = new TableColumn();
					
					linkColumn.setStyleClass("fileLink");					
					linkColumn.setData(l.render());
					
					TableColumn sizeColumn = new TableColumn();
					
					sizeColumn.setStyleClass("fileSize");
					sizeColumn.setAlign(Align.RIGHT);
					sizeColumn.setData(df.format(((float)fileList.get(i).length()) / 1024)+" kBytes");
					
					row.addColumn(dateColumn);
					row.addColumn(linkColumn);
					row.addColumn(sizeColumn);
					
					t.addRow(row);
				}
				
			}
			
			content.append(t.render());
			setTitle(title);
			
		}
		else 
			return "No files found.";
		
		return content.toString();
	}

	
}
