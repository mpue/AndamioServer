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
package org.pmedv.cms.daos;

import org.pmedv.context.AppContext;
import org.springframework.context.ApplicationContext;

/**
 * Thic class is simple facade for all data access objects.
 * 
 * @author Matthias Pueski
 * 
 */
public class DAOManager {

	private BaseDAO baseDAO = null;
	private NodeDAO nodeDAO = null;
	private ConfigDAO configDAO = null;
	private ContentDAO contentDAO = null;
	private ProcessDAO processDAO = null;
	private UserDAO userDAO = null;
	private UsergroupDAO usergroupDAO = null;
	private GalleryDAO galleryDAO = null;
	private ImageDAO imageDAO = null;
	private ContactDAO contactDAO = null;
	private DownloadDAO downloadDAO = null;
	private SiteRequestDAO siteRequestDAO = null;
	private TemplateDAO templateDAO = null;
	private BiblioDAO biblioDAO = null;
	private CategoryDAO categoryDAO = null;
	private ForumDAO forumDAO = null;
	private ThreadDAO threadDAO = null;
	private PostingDAO postingDAO = null;
	private AccountDAO accountDAO = null;
	private FolderDAO folderDAO = null;
	private MessageDAO messageDAO = null;
	private CountryDAO countryDAO = null;
	private CalendarDAO calendarDAO = null;
	private MeetingDAO meetingDAO = null;
	private AppointmentDAO appointmentDAO = null;
	private FaqDAO faqDAO = null;
	private AttachmentDAO attachmentDAO = null;
	private DownloadCategoryDAO downloadCategoryDAO = null;
	
	private static DAOManager instance = null;

	private ApplicationContext ctx;

	protected DAOManager() {
		this.ctx = AppContext.getApplicationContext();
	}

	/**
	 * This method returns the singleton instance of the data object manager, which is used to gain
	 * access to the data objects.
	 * 
	 * @return the singleton instance of the data object manager
	 */

	public static DAOManager getInstance() {
		if (instance == null) {
			instance = new DAOManager();
		}
		return instance;
	}

	public NodeDAO getNodeDAO() {
		nodeDAO = (NodeDAO) ctx.getBean("nodeDAO");
		return nodeDAO;
	}

	public ConfigDAO getConfigDAO() {
		configDAO = (ConfigDAO) ctx.getBean("configDAO");
		return configDAO;
	}

	public ContentDAO getContentDAO() {
		contentDAO = (ContentDAO) ctx.getBean("contentDAO");
		return contentDAO;
	}

	public ProcessDAO getProcessDAO() {
		processDAO = (ProcessDAO) ctx.getBean("processDAO");
		return processDAO;
	}

	public UserDAO getUserDAO() {
		userDAO = (UserDAO) ctx.getBean("userDAO");
		return userDAO;
	}

	public UsergroupDAO getUsergroupDAO() {
		usergroupDAO = (UsergroupDAO) ctx.getBean("usergroupDAO");
		return usergroupDAO;
	}

	public GalleryDAO getGalleryDAO() {
		galleryDAO = (GalleryDAO) ctx.getBean("galleryDAO");
		return galleryDAO;
	}

	public ImageDAO getGImageDAO() {
		imageDAO = (ImageDAO) ctx.getBean("imageDAO");
		return imageDAO;
	}

	public ContactDAO getContactDAO() {
		contactDAO = (ContactDAO) ctx.getBean("contactDAO");
		return contactDAO;
	}

	public DownloadDAO getDownloadDAO() {
		downloadDAO = (DownloadDAO) ctx.getBean("downloadDAO");
		return downloadDAO;
	}

	public SiteRequestDAO getSiteRequestDAO() {
		siteRequestDAO = (SiteRequestDAO) ctx.getBean("siteRequestDAO");
		return siteRequestDAO;
	}

	public BaseDAO getBaseDAO() {
		baseDAO = (BaseDAO) ctx.getBean("baseDAO");
		return baseDAO;
	}

	public TemplateDAO getTemplateDAO() {
		templateDAO = (TemplateDAO) ctx.getBean("templateDAO");
		return templateDAO;
	}

	public BiblioDAO getBiblioDAO() {
		biblioDAO = (BiblioDAO) ctx.getBean("biblioDAO");
		return biblioDAO;
	}

	public ForumDAO getForumDAO() {
		forumDAO = (ForumDAO) ctx.getBean("forumDAO");
		return forumDAO;
	}

	public CategoryDAO getCategoryDAO() {
		categoryDAO = (CategoryDAO) ctx.getBean("categoryDAO");
		return categoryDAO;
	}

	public ThreadDAO getThreadDAO() {
		threadDAO = (ThreadDAO) ctx.getBean("threadDAO");
		return threadDAO;
	}

	public AccountDAO getAccountDAO() {
		accountDAO = (AccountDAO) ctx.getBean("accountDAO");
		return accountDAO;
	}

	public FolderDAO getFolderDAO() {
		folderDAO = (FolderDAO) ctx.getBean("folderDAO");
		return folderDAO;
	}

	public MessageDAO getMessageDAO() {
		messageDAO = (MessageDAO) ctx.getBean("messageDAO");
		return messageDAO;
	}

	/**
	 * @return the countryDAO
	 */
	public CountryDAO getCountryDAO() {
		countryDAO = (CountryDAO) ctx.getBean("countryDAO");
		return countryDAO;
	}

	public CalendarDAO getCalendarDAO() {
		calendarDAO = (CalendarDAO) ctx.getBean("calendarDAO");
		return calendarDAO;
	}

	public MeetingDAO getMeetingDAO() {
		meetingDAO = (MeetingDAO) ctx.getBean("meetingDAO");
		return meetingDAO;
	}

	public AppointmentDAO getAppointmentDAO() {
		appointmentDAO = (AppointmentDAO) ctx.getBean("appointmentDAO");
		return appointmentDAO;
	}

	public FaqDAO getFaqDAO() {
		faqDAO = (FaqDAO) ctx.getBean("faqDAO");
		return faqDAO;
	}

	public PostingDAO getPostingDAO() {
		postingDAO = (PostingDAO) ctx.getBean("postingDAO");
		return postingDAO;
	}

	public AttachmentDAO getAttachmentDAO() {
		attachmentDAO = (AttachmentDAO)ctx.getBean("attachmentDAO");
		return attachmentDAO;
	}

	public DownloadCategoryDAO getDownloadCategoryDAO() {
		downloadCategoryDAO = (DownloadCategoryDAO)ctx.getBean("downloadCategoryDAO");
		return downloadCategoryDAO;
	}

}
