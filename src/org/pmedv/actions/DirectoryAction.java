/**
 * WeberknechtCMS - Open Source Content Management Written and maintained by Matthias Pueski
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
package org.pmedv.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.pmedv.beans.objects.DirectoryObject;
import org.pmedv.cms.common.GlobalForwards;
import org.pmedv.cms.common.Params;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.context.AppContext;
import org.pmedv.file.DirectoryPrintVisitor;
import org.pmedv.forms.DirectoryListForm;
import org.pmedv.jobs.SchedulerControl;
import org.pmedv.jobs.core.CopyJob;
import org.pmedv.pojos.Config;
import org.pmedv.util.FileHelper;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;

/**
 * <p>
 * The <code>DirectoryAction</code> provides the actions for a selected directory.
 * </p>
 * <p>
 * All methods except the showBrowser method are called from the ExtJS application
 * </p>
 * 
 * @author Matthias Pueski
 * 
 */
public class DirectoryAction extends ObjectListAction {

	public DirectoryAction() {
		super("menu.filemanager");
		log.debug("Executing " + this.getClass());
	}

	private static final Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);
	private static final String rootDir = config.getBasepath();

	public ActionForward showBrowser(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		if (!isAllowedToProcess(request, getName()))
			return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);

		return mapping.findForward(GlobalForwards.SHOW_FILE_BROWSER);

	}

	/**
	 * Traverse specified directory load contents and save them in the request
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		if (!isAllowedToProcess(request, getName()))
			return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);

		File file = new File(rootDir);

		ArrayList<DirectoryObject> directoryContents = FileHelper.traverse(file, new DirectoryPrintVisitor());
		DecimalFormat Formatted = new DecimalFormat("0.00");

		if (request.getParameter("getJSON") != null) {

			writeJSONList(DirectoryObject.class, directoryContents, true, "id", request, response);

			return null;
		}
		else {

			DirectoryListForm directoryListForm = (DirectoryListForm) form;

			directoryListForm.setDirectoryContents(directoryContents);
			directoryListForm.setCurrentDir(rootDir);
			directoryListForm.setTotalSize(Formatted.format(FileHelper.getTotalSizeInKbytes(file,
					new DirectoryPrintVisitor())));
			directoryListForm.setNumDirs(FileHelper.getNumDirs(file, new DirectoryPrintVisitor()));
			directoryListForm.setNumFiles(FileHelper.getNumFiles(file, new DirectoryPrintVisitor()));

			
			
			return mapping.findForward(GlobalForwards.SHOW_DIRECTORY_LIST);

		}

	}

	/**
	 * Enter specified directory and list contents
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */

	public ActionForward enter(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		if (!isAllowedToProcess(request, getName()))
			return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);

		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);
		String rootDirectory = config.getBasepath();
		String currentDir = "";
		String parentDir = "";

		String dir = null;

		dir = request.getParameter(Params.DIR);

		if (dir == null)
			dir = (String) request.getSession().getAttribute(Params.DIR);

		// check for occurences of ".." to prevent traversal to directories
		// outside of our application

		if (dir.indexOf("..") > -1) {
			currentDir = rootDirectory;
			parentDir = currentDir;
		}
		else {
			currentDir = rootDirectory + dir;
			parentDir = currentDir.substring(0, currentDir.lastIndexOf("/"));
		}

		DirectoryListForm directoryListForm = (DirectoryListForm) form;

		File file = new File(currentDir);

		ArrayList<DirectoryObject> directoryContents = FileHelper.traverse(file, new DirectoryPrintVisitor(),
				parentDir, rootDir);

		Collections.sort(directoryContents, new Comparator<Object>() {

			public int compare(Object o1, Object o2) {

				DirectoryObject d1 = (DirectoryObject) o1;
				DirectoryObject d2 = (DirectoryObject) o2;

				return d1.getType().compareTo(d2.getType());
			}

		});

		if (request.getParameter("getJSON") != null) {

			writeJSONList(DirectoryObject.class, directoryContents, true, "id", request, response);

			return null;
		}
		else {
			DecimalFormat Formatted = new DecimalFormat("0.00");

			directoryListForm.setDirectoryContents(directoryContents);
			directoryListForm.setCurrentDir(currentDir);
			directoryListForm.setEnterDir(dir);
			directoryListForm.setTotalSize(Formatted.format(FileHelper.getTotalSizeInKbytes(file,
					new DirectoryPrintVisitor())));
			directoryListForm.setNumDirs(FileHelper.getNumDirs(file, new DirectoryPrintVisitor()));
			directoryListForm.setNumFiles(FileHelper.getNumFiles(file, new DirectoryPrintVisitor()));

			return mapping.findForward(GlobalForwards.SHOW_DIRECTORY_LIST);
		}

	}

	public ActionForward deleteFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		if (!isAllowedToProcess(request, getName()))
			return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);

		String filename = request.getParameter(Params.FILENAME);

		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);
		String baseDir = config.getBasepath();

		try {
			File f = new File(baseDir + filename);
			f.delete();			
		}
		catch (Exception e) {
			log.debug("Could not delete : " + baseDir + filename);
		}

		return null;

	}

	public ActionForward copyFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		if (!isAllowedToProcess(request, getName()))
			return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);

		String source = request.getParameter("source");
		String destination = request.getParameter("dest");
		String targetName = request.getParameter("targetName");

		log.debug("Target : " + targetName);

		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);
		String baseDir = config.getBasepath();

		destination = baseDir + destination + "/" + targetName;

		SchedulerControl sc = (SchedulerControl) AppContext.getApplicationContext().getBean(
				"schedulerControl");
		Scheduler s = sc.getScheduler();

		JobDetail copyJob = new JobDetail("copyJob" + targetName, Scheduler.DEFAULT_GROUP, CopyJob.class);

		copyJob.getJobDataMap().put("source", source);
		copyJob.getJobDataMap().put("destination", destination);
		copyJob.setDescription("Copyjob : " + targetName);

		SimpleTrigger copyTrigger = new SimpleTrigger("copyTrigger" + targetName, null, new Date(), null, 0,
				0L);

		try {
			s.scheduleJob(copyJob, copyTrigger);
			log.debug("Triggering copy job");
		}
		catch (SchedulerException e) {
			log.debug("Error scheduling copy job");
		}

		log.debug("Copying from " + source + " to " + destination);

		return null;
	}

	public ActionForward createDir(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		if (!isAllowedToProcess(request, getName()))
			return mapping.findForward("showAdminPanel");

		String newDir = request.getParameter(Params.DIR);

		log.debug("Adding directory " + request.getParameter(Params.DIR));

		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);
		String baseDir = config.getBasepath();

		String message = null;

		try {
			
			File f = new File(baseDir + newDir);
			
			if (!f.mkdir()) {
				message = "{\"success\":false, message : \"Could not create directory.\" }";
			}
			else {
				message = "{\"success\":true, message : \"Directory created.\" }";
			}
		}
		catch (Exception e) {

			log.debug("Could not create directory : " + baseDir + newDir);

			message = "{\"success\":false, message : \"Could not create directory.\" }";

		}

		if (message != null)

			try {
				PrintWriter out = response.getWriter();
				out.write(message);
			}
			catch (IOException e1) {

				e1.printStackTrace();
			}

		return null;

	}

	public ActionForward rename(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);

		String baseDir = config.getBasepath();

		String oldName = request.getParameter("oldName");
		String newName = request.getParameter("newName");

		oldName = baseDir + oldName;
		newName = baseDir + newName;

		log.debug("Renaming " + oldName + " to " + newName);

		String message = null;
		
		if (newName.contains("..")) {
			message = "{\"success\":false, message : \"Error renaming " + oldName + " to " + newName+". Directory contains invalid path sequence.\" }";			
		}
		else {

			File f = new File(oldName);
			
			if (f.renameTo(new File(newName))) {				
				message = "{\"success\":true, message : \"Successfully renamed.\" }";
			}
			else {
				log.debug("Error renaming " + oldName + " to " + newName);
				message = "{\"success\":false, message : \"Error renaming " + oldName + " to " + newName+"\" }";				
			}
			
		}
		
		
		if (message != null)

			try {
				PrintWriter out = response.getWriter();
				out.write(message);
			}
			catch (IOException e1) {

				e1.printStackTrace();
			}


		return null;

	}

	public ActionForward download(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		if (!isAllowedToProcess(request, getName()))
			return mapping.findForward(GlobalForwards.SHOW_ADMIN_PANEL);

		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);

		String dir = request.getParameter(Params.DIR);
		String filename = request.getParameter(Params.FILENAME);

		// Prevent downloading files from outside of our jail

		if (!(dir.indexOf("..") > -1)) {

			String archiveUri = config.getBasepath() + dir + filename;

			log.debug("Trying to download " + archiveUri);

			try {

				File file = new File(archiveUri);
				FileInputStream fileIn = new FileInputStream(file);
				OutputStream out = response.getOutputStream();

				response.setContentType("application/octet");

				response.setContentLength((int) file.length());

				byte[] buffer = new byte[2048];
				int bytesRead = fileIn.read(buffer);
				while (bytesRead >= 0) {
					if (bytesRead > 0)
						out.write(buffer, 0, bytesRead);
					bytesRead = fileIn.read(buffer);
				}
				out.flush();
				out.close();
				fileIn.close();
			}
			catch (FileNotFoundException f) {
				log.debug("File not found : " + archiveUri);
			}
			catch (IOException i) {
				i.printStackTrace();
			}

		}

		return null;

	}

}
