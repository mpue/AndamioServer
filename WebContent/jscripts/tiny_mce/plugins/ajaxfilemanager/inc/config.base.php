<?
	/**
	 * sysem base config setting
	 * @author Logan Cai (cailongqun@yahoo.com.cn)
	 * @link www.phpletter.com
	 * @since 21/April/2007
	 *
	 */
	
@session_start();

// Define error levels
define('FATAL', E_USER_ERROR);
define('ERROR', E_USER_WARNING);
define('WARNING', E_USER_NOTICE);
error_reporting(E_ALL);	
//error_reporting(E_ALL ^ E_NOTICE);	
	
	
	//Directories Declarations	
	
	define('DIR_AJAX_ROOT', dirname(dirname(__FILE__)) . DIRECTORY_SEPARATOR) ; // the path to ajax file manager
	define('DIR_AJAX_INC', DIR_AJAX_ROOT . "inc" . DIRECTORY_SEPARATOR);
	define('DIR_AJAX_CLASSES', DIR_AJAX_ROOT .  "classes" . DIRECTORY_SEPARATOR);
	define("DIR_AJAX_LANGS", DIR_AJAX_ROOT . "langs" . DIRECTORY_SEPARATOR);
	
	//Class Declarations
	define('CLASS_FILE', DIR_AJAX_INC .'class.file.php');
	define("CLASS_UPLOAD", DIR_AJAX_INC .  'class.upload.php');
	define('CLASS_MANAGER', DIR_AJAX_INC . 'class.manager.php');
	//SCRIPT FILES declarations
	define('SPT_FUNCTION_BASE', DIR_AJAX_INC . 'function.base.php');
	//Access Control Setting
	/**
	 * turn off => 0
	 * by session => 1
	 */
	define('CONFIG_ACCESS_CONTROL_MODE', 0); 	
	define('CONFIG_LOGIN_INDEX', 'site_user'); //must set this when you turn the access control on
	define("CONFIG_LOGIN_USERNAME", 'admin');
	define('CONFIG_LOGIN_PASSWORD', 'admin');
	define('CONFIG_LOGIN_PAGE', 'ajax_login.php'); //the url to the login page
	
	//FILESYSTEM CONFIG
		/*
		* CONFIG_SYS_DEFAULT_PATH is the default folder where the files would be uploaded to
			and it must be a folder under the CONFIG_SYS_ROOT_PATH or the same folder
		*/
		
	define('CONFIG_SYS_DEFAULT_PATH', '../../../../content/'); 
	define('CONFIG_SYS_ROOT_PATH', '../../../../content/');	//the root folder where the files would be uploaded to
	define('CONFIG_SYS_INC_DIR_PATTERN', ''); //leave empty if you want to include all foldler
	define('CONFIG_SYS_EXC_DIR_PATTERN', ''); //leave empty if you want to include all folder
	define('CONFIG_SYS_INC_FILE_PATTERN', '');
	define('CONFIG_SYS_EXC_FILE_PATTERN', ''); 
	define('CONFIG_SYS_DELETE_RECURSIVE', 0); //delete all contents within a specific folder if set to be 1
	
	//UPLOAD OPTIONS CONFIG
	define('CONFIG_UPLOAD_MAXSIZE', 8 * 1024 * 1024); //by bytes
	//define('CONFIG_UPLOAD_MAXSIZE', 2048); //by bytes
	//define('CONFIG_UPLOAD_VALID_EXTS', 'txt');//
	define('CONFIG_UPLOAD_VALID_EXTS', 'gif,jpg,png,bmp,tif,GIF,JPG,PNG,BMP,TIF,zip,sit,rar,gz,tar,htm,html,mov,mpg,avi,asf,mpeg,wmv,aif,aiff,wav,mp3,swf,ppt,rtf,doc,pdf,xls,txt,xml,xsl,dtd');//
	define('CONFIG_UPLOAD_INVALID_EXTS', '');
	//define('CONFIG_UPLOAD_OVERRIDE_ALLOWED', 0);
	
	//URL Declartions
	//define('CONFIG_URL_PREVIEW_ROOT', '');
	define('CONFIG_URL_CREATE_FOLDER', 'ajax_create_folder.php');
	define('CONFIG_URL_DELETE', 'ajax_delete_file.php');
	define('CONFIG_URL_HOME', 'ajaxfilemanager.php');
	define("CONFIG_URL_UPLOAD", 'ajax_file_upload.php');
	define('CONFIG_URL_PREVIEW', 'ajax_preview.php');
	
	//General Option Declarations
	//define('CONFIG_GENERAL_FRIENDLY_PATH', true);
	//LANGAUGAE DECLARATIONNS
	define('CONFIG_LANG_INDEX', 'language'); //the index in the session
	define('CONFIG_LANG_DEFAULT', 'en');
?>