<?
	/**
	 * sysem base config setting
	 * @author Logan Cai (cailongqun@yahoo.com.cn)
	 * @link www.phpletter.com
	 * @since 22/April/2007
	 *
	 */
	define('DATE_TIME_FORMAT', 'd/M/Y H:i:s');
	//Label
		//Top Action
		define('LBL_ACTION_REFRESH', 'Aktualisieren');
		define("LBL_ACTION_DELETE", 'Loeschen');
		//File Listing
	define('LBL_NAME', 'Name');
	define('LBL_SIZE', 'Groesse');
	define('LBL_MODIFIED', 'Geaendert am');
		//File Information
	define('LBL_FILE_INFO', 'Datei Information:');
	define('LBL_FILE_NAME', 'Name:');	
	define('LBL_FILE_CREATED', 'Erstellt am:');
	define("LBL_FILE_MODIFIED", 'Geändert am:');
	define("LBL_FILE_SIZE", 'Datei Groesse:');
	define('LBL_FILE_TYPE', 'Datei Type:');
	define("LBL_FILE_WRITABLE", 'Schreibbar?');
	define("LBL_FILE_READABLE", 'Lesbar?');
		//Folder Information
	define('LBL_FOLDER_INFO', 'Verzeichnis Informationen');
	define("LBL_FOLDER_PATH", 'Pfad:');
	define("LBL_FOLDER_CREATED", 'Erstellt am:');
	define("LBL_FOLDER_MODIFIED", 'Geaendert am:');
	define('LBL_FOLDER_SUDDIR', 'Unterverzeichnis:');
	define("LBL_FOLDER_FIELS", 'Dateien:');
	define("LBL_FOLDER_WRITABLE", 'Schreibbar?');
	define("LBL_FOLDER_READABLE", 'Lesbar?');
		//Preview
	define("LBL_PREVIEW", 'Vorschau');
	//Buttons
	define('LBL_BTN_SELECT', 'Auswaehlen');
	define('LBL_BTN_CANCEL', 'Abbrechen');
	define("LBL_BTN_UPLOAD", 'Hochladen');
	define('LBL_BTN_CREATE', 'Erstellen');
	define("LBL_BTN_NEW_FOLDER", 'Neues Verzeichnis');
	//ERROR MESSAGES
		//deletion
	define('ERR_NOT_FILE_SELECTED', 'Bitte Datei auswählen.');
	define('ERR_NOT_DOC_SELECTED', 'Keine Datei(en) zum Löschen ausgewählt.');
	define('ERR_DELTED_FAILED', 'Kann die ausgewählten Dateien nicht löschen.');
	define('ERR_FOLDER_PATH_NOT_ALLOWED', 'Der Verzeichnispfad ist nicht erlaubt.');
		//class manager
	define("ERR_FOLDER_NOT_FOUND", 'Das ausgewählte Verzeichnis kann nicht gefunden werden: ');
		//rename
	define('ERR_RENAME_FORMAT', 'Please give it a name which only contain letters, digits, space, hyphen and underscore.');
	define('ERR_RENAME_EXISTS', 'Please give it a name which is unique under the folder.');
	define('ERR_RENAME_FILE_NOT_EXISTS', 'The file/folder does not exist.');
	define('ERR_RENAME_FAILED', 'Unable to rename it, please try again.');
	define('ERR_RENAME_EMPTY', 'Please give it a name.');
	define("ERR_NO_CHANGES_MADE", 'No changes has been made.');
	define('ERR_RENAME_FILE_TYPE_NOT_PERMITED', 'You are not permitted to change the file to such extension.');
		//folder creation
	define('ERR_FOLDER_FORMAT', 'Please give it a name which only contain letters, digits, space, hyphen and underscore.');
	define('ERR_FOLDER_EXISTS', 'Please give it a name which is unique under the folder.');
	define('ERR_FOLDER_CREATION_FAILED', 'Unable to create a folder, please try again.');
	define('ERR_FOLDER_NAME_EMPTY', 'Please give it  a name.');
	
		//file upload
	define("ERR_FILE_NAME_FORMAT", 'Please give it a name which only contain letters, digits, space, hyphen and underscore.');
	define('ERR_FILE_NOT_UPLOADED', 'No file has been selected for uploading.');
	define('ERR_FILE_TYPE_NOT_ALLOWED', 'You are not allowed to upload such file type.');
	define('ERR_FILE_MOVE_FAILED', 'Failed to move the file.');
	define('ERR_FILE_NOT_AVAILABLE', 'The file is unavailable.');
	define('ERROR_FILE_TOO_BID', 'Datei zu gross. (max: %s)');
	

	//Tips
	define('TIP_FOLDER_GO_DOWN', 'Single Click to get to this folder...');
	define("TIP_DOC_RENAME", 'Double Click to edit...');
	define('TIP_FOLDER_GO_UP', 'Single Click to get to the parent folder...');
	define("TIP_SELECT_ALL", 'Select All');
	define("TIP_UNSELECT_ALL", 'Unselect All');
	//WARNING
	define('WARNING_DELETE', 'Are you sure to delete selected files.');
	//Preview
	define('PREVIEW_NOT_PREVIEW', 'Keine Vorschau moeglich.');
	define('PREVIEW_OPEN_FAILED', 'Kann die Datei nicht oeffnen.');
	define('PREVIEW_IMAGE_LOAD_FAILED', 'Kann das Bild nicht laden.');

	//Login
	define('LOGIN_PAGE_TITLE', 'Dateiverwaltung');
	define('LOGIN_FORM_TITLE', 'Anmeldung Dateiverwaltung');
	define('LOGIN_USERNAME', 'Benutzername:');
	define('LOGIN_PASSWORD', 'Passwort:');
	define('LOGIN_FAILED', 'Ungueltiger Benutzername/Passwort.');
	
	
?>