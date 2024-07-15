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
package org.pmedv.cms.common;


/**
 * <p>
 * This is a collection of all redirect targets used by struts action.
 * In fact these are mostly legacy, since we've switched to actions
 * which write to the output stream instead of calling a JSP redirect.
 * </p>
 * 
 * @author Matthias Pueski
 *
 */
public class GlobalForwards {

	public static final String SHOW_ADMIN_PANEL          = "showAdminPanel";
	public static final String SHOW_PROCESS_LIST         = "showProcessList";
	public static final String SHOW_PROCESS_DETAILS      = "showProcessDetails";
	public static final String SHOW_CLIENT_LIST          = "showClientList";
	public static final String SHOW_CLIENT_DETAILS       = "showClientDetails";
	public static final String SHOW_CONTACT_LIST         = "showContactList";
	public static final String SHOW_CONTACT_DETAILS      = "showContactDetails";
	public static final String SHOW_LOGIN_DIALOG         = "LoginDialog";
	public static final String SHOW_CONTENT_LIST         = "showContentList";
	public static final String SHOW_CONTENT_DETAILS      = "showContentDetails";
	public static final String SHOW_CONTENT_EDITOR       = "showContentEditor";
	public static final String SHOW_SINGLE_EDITOR        = "showSingleEditor";	
	public static final String SHOW_CONTENT_PREVIEW      = "showContentPreview";
	public static final String SHOW_STATISTICS           = "showStatistics";
	public static final String SHOW_GALLERY_DETAILS      = "showGalleryDetails";
	public static final String SHOW_GALLERY              = "showGallery";
	public static final String SHOW_GALLERY_LIST         = "showGalleryList";
	public static final String SHOW_GIMAGE_DETAILS       = "showGImageDetails";
	public static final String SHOW_USERGROUP_LIST       = "showUsergroupList";
	public static final String SHOW_USERGROUP_DETAILS    = "showUsergroupDetails";
	public static final String SHOW_USER_LIST            = "showUserList";
	public static final String SHOW_USER_DETAILS         = "showUserDetails";
	public static final String SHOW_NODE_LIST            = "showNodeList";
	public static final String SHOW_NODE_DETAILS         = "showNodeDetails";
	public static final String INSTALL_SUCCESS           = "installSuccess";
	public static final String INSTALL_CONFIG            = "installConfig";
	public static final String SHOW_CONFIG               = "showConfig"; 
	public static final String ADD_CONTENT               = "addContent";
	public static final String ADD_SUBCONTENT			 = "addSubContent";
	public static final String ADD_PROCESSGROUP          = "addProcessgroup";
	public static final String ADD_NODEGROUP             = "addNodegroup";
	public static final String ADD_USERGROUP             = "addUsergroup";
	public static final String ADD_AVATAR				 = "addAvatar";
	public static final String CREATE_CLIENT_SUCCESS     = "createClientSuccess";
	public static final String CREATE_CONTACT_SUCCESS    = "createContactSuccess";  
	public static final String CREATE_CONTENT_SUCCESS    = "createContentSuccess";
	public static final String CREATE_GALLERY_SUCCESS    = "createGallerySuccess";
	public static final String CREATE_MENU_SUCCESS       = "createMenuSuccess";
	public static final String CREATE_FORUM_SUCCESS      = "createForumSuccess";
	public static final String CREATE_PROCESS_SUCCESS    = "createProcessSuccess";
	public static final String CREATE_USER_SUCCESS       = "createUserSuccess";
	public static final String CREATE_USERGROUP_SUCCESS  = "createUsergroupSuccess";
	public static final String CREATE_PATIENT_SUCCESS    = "createPatientSuccess";
	public static final String SHOW_DIRECTORY_LIST       = "showDirectoryList";
	public static final String SHOW_FILE_DETAILS         = "showFileDetails";
	public static final String SHOW_FILE_NAME            = "showFileName";
	public static final String UPLOAD_FILE               = "uploadFile";
	public static final String IMPORT_NODES              = "importNodes";
	public static final String IMPORT_ODT                = "importODT";
	public static final String SITE_CONTACT              = "siteContact";
	public static final String SHOW_FORUM_LIST           = "showForumList";
	public static final String SHOW_FORUM_DETAILS        = "showForumDetails";
	public static final String SHOW_CATEGORY_LIST        = "showCategoryList";
	public static final String SHOW_CATEGORY_DETAILS     = "showCategoryDetails";
	public static final String SHOW_FORUMS               = "showForums";
	public static final String SHOW_CATEGORY             = "showCategory";
	public static final String SHOW_THREAD               = "showThread";
	public static final String SHOW_PLUGIN_LIST          = "showPluginList";
	public static final String SHOW_DOWNLOAD_LIST        = "showDownloadList";
	public static final String SHOW_DOWNLOAD_DETAILS     = "showDownloadDetails";
	public static final String SHOW_LANGUAGE_LIST        = "showLanguageList";
	public static final String SHOW_LANGUAGE_DETAILS     = "showLanguageDetails";
	public static final String SHOW_REGISTRATION_DIALOG  = "showRegistrationDialog";
	public static final String SHOW_JOB_BROWSER          = "jobBrowser";
	public static final String SHOW_FILE_BROWSER         = "fileBrowser";	
	public static final String AF_SHOW_CONTENT           = "/admin/ShowContent.do?do=edit&content_id=";

}
