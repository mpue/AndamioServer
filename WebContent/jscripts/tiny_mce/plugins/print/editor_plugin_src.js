/**
 * $Id: editor_plugin_src.js,v 1.1 2011-06-20 13:59:27 mpue Exp $
 *
 * @author Moxiecode
 * @copyright Copyright � 2004-2007, Moxiecode Systems AB, All rights reserved.
 */

/* Import theme	specific language pack */
tinyMCE.importPluginLanguagePack('print');

var TinyMCE_PrintPlugin = {
	getInfo : function() {
		return {
			longname : 'Print',
			author : 'Moxiecode Systems AB',
			authorurl : 'http://tinymce.moxiecode.com',
			infourl : 'http://wiki.moxiecode.com/index.php/TinyMCE:Plugins/print',
			version : tinyMCE.majorVersion + "." + tinyMCE.minorVersion
		};
	},

	getControlHTML : function(cn)	{
		switch (cn) {
			case "print":
				return tinyMCE.getButtonHTML(cn, 'lang_print_desc', '{$pluginurl}/images/print.gif', 'mcePrint');
		}

		return "";
	},

	/**
	 * Executes	the	search/replace commands.
	 */
	execCommand : function(editor_id, element, command,	user_interface,	value) {
		// Handle commands
		switch (command) {
			case "mcePrint":
				tinyMCE.getInstanceById(editor_id).contentWindow.print();
				return true;
		}

		// Pass to next handler in chain
		return false;
	}
};

tinyMCE.addPlugin("print", TinyMCE_PrintPlugin);
