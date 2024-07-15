function tinyInit() {

	tinyMCE.init({
		language : "de",
		document_base_url : docBaseUrl,
		theme : "advanced",
		plugins : "layer,spellchecker,table,advhr,advimage,advlink,flash,paste,fullscreen,contextmenu,save,emotions,codehighlighting",
		theme_advanced_buttons1_add : "fontselect,fontsizeselect",
		theme_advanced_buttons2_add : "separator,forecolor,backcolor,liststyle",
		theme_advanced_buttons2_add_before: "cut,copy,paste,pastetext,pasteword,separator,",
		theme_advanced_buttons3_add_before : "tablecontrols,separator",
		theme_advanced_buttons3_add : "emotions,flash,advhr,insertlayer,moveforward,movebackward,absolute,codehighlighting",
		theme_advanced_toolbar_location : "top",
		theme_advanced_toolbar_align : "center",
		extended_valid_elements : "*[*]",
		file_browser_callback : "ajaxfilemanager",
		save_enablewhendirty : true,
		onchange_callback : "change",
		paste_use_dialog : false,
		theme_advanced_resizing : true,
		theme_advanced_statusbar_location : 'bottom',
		theme_advanced_resize_horizontal : true,
		apply_source_formatting : false,
		auto_reset_designmode : false,
		force_br_newlines : true,
		remove_linebreaks : false, 
		content_css : contentCss,
		force_p_newlines : false,	
		relative_urls : false,
		convert_urls : false,
		height: "98%",
	    formats: {
	        bold : {inline : 'b' },  
	    },		
		
	});

}

// tinyMCE.isNotDirty = true;

function change() {
	
	if (!nothingSelected) { 	
		Ext.getCmp('saveBtn').enable();
		dirty = true;
	}
}

function ajaxfilemanager(field_name, url, type, win) {
	var ajaxfilemanagerurl = "../../../tiny_mce/plugins/ajaxfilemanager/ajaxfilemanager.php";
	switch (type) {
		case "image":
			ajaxfilemanagerurl += "?type=img";
			break;
		case "media":
			ajaxfilemanagerurl += "?type=media";
			break;
		case "flash": //for older versions of tinymce
			ajaxfilemanagerurl += "?type=media";
			break;
		case "file":
			ajaxfilemanagerurl += "?type=files";
			break;
		default:
			return false;
	}
	var fileBrowserWindow = new Array();
	fileBrowserWindow["file"] = ajaxfilemanagerurl;
	fileBrowserWindow["title"] = "Ajax File Manager";
	fileBrowserWindow["width"] = "782";
	fileBrowserWindow["height"] = "440";
	fileBrowserWindow["close_previous"] = "no";
	tinyMCE.openWindow(fileBrowserWindow, {
	  window : win,
	  input : field_name,
	  resizable : "yes",
	  inline : "yes",
	  editor_id : tinyMCE.getWindowArg("editor_id")
	});
	
	return false;
}

/* Remove all editor instances below DOM node with specified ID
 * Use this to editor instances when closing a tab or panel containing TinyMCE editors
 * 
 * Sample beforedestroy event for a panel:
 * on('beforeDestroy',function() { tinyMCE.removeInstances(this.id) },this);
 */ 
tinyMCE.removeInstances = function(id){
    /* For 3.0 use this.editors instead of this.instances
    for ( var n in this.editors ) {
        var inst = this.editors[n];
        console.log('Searching for ' + '#' + id + ' #' + inst.id );
        if( Ext.select( '#' + id + ' #' + inst.id ).getCount() > 0 ) {
            console.log( 'Removing editor instance with id ' + inst.id )
            tinyMCE.execCommand( 'mceRemoveControl', false, inst.id );
        }
    }*/
    // 2.0
    for ( var n in this.instances ) {
        var inst = this.instances[n];
        console.log('Searching for ' + '#' + id + ' #' + inst.id );
        //if( Ext.select( inst.id ).getCount() > 0 ) {
            console.log( 'Removing editor instance with id ' + inst.id );
            tinyMCE.execCommand( 'mceRemoveControl', false, inst.id );
        // }
    }
};

/* TinyMCE plugin - Add as a plugin to any textarea field
 * 
 * Initialize by running
 *   mceEditor = new MceEditor();
 * before using the plugin.
 * 
 * Sample textarea config:
 * {
 *     xtype: 'textarea',
 *     fieldLabel: 'Test',
 *     name: 'test',
 *     plugins: [mceEditor]
 * }
 */
MceEditor = function(){
    Ext.apply(this, {
        init : function(textarea){
            textarea.on('render', function(textarea){
                tinyMCE.execCommand( 'mceAddControl', false, textarea.getId() );
                var inst = tinyMCE.getInstanceById(textarea.getId());
                inst.id = textarea.id;
            });
        }
    });
};


