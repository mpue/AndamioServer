/* TinyMCE config
 * Refer to http://wiki.moxiecode.com/index.php/TinyMCE:Configuration for further information
 */
tinyMCE.init({
    content_css: "/css/tiny_mce.css", // Custom CSS for the content of the editor
    mode: 'none',
    //skin : "o2k7", // this skin is only available for 3.0, but leaving it there doesn't hurt 2.0
    cleanup_on_startup: true,
    plugins: "table",
    theme: "advanced",
    width: "100%",
    height: "498px",
    autoScroll: true,
    theme_advanced_toolbar_location: "top",
    theme_advanced_toolbar_align: "left",
    theme_advanced_path_location: "bottom",
    forced_root_block: "p",
    theme_advanced_buttons1: "formatselect,bold,italic,bullist,numlist,outdent,indent,sub,sup,charmap",
    theme_advanced_buttons2: "cut,copy,paste,undo,redo,separator,link,unlink,separator,cleanup,removeformat,separator,code",
    theme_advanced_buttons3: "tablecontrols",
    valid_elements: "-h1,-h2,-h3,-h4,-h5,-h6,p,ul,ol,-li,-em/i,-strong/b,blockquote,img[src,image-id],-a[href],div[class],table[class],tr[valign],th[align|valign|width],td[align|valign|width|rowspan|colspan],tbody,thead,tfoot",
    relative_urls: false,
    debug: false
});

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
        //console.log('Searching for ' + '#' + id + ' #' + inst.id );
        if( Ext.select( '#' + id + ' #' + inst.id ).getCount() > 0 ) {
            //console.log( 'Removing editor instance with id ' + inst.id )
            tinyMCE.execCommand( 'mceRemoveControl', false, inst.id );
        }
    }
}

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
                // For 3.0 use setContent instead of setHTML
                // inst.setContent(textarea.getValue());
                
                // 2.0
                inst.setHTML(textarea.getValue());
                
                // 2.0 doesn't automatically add an ID to an editor instance, so we need to do it manually
                inst.id = textarea.id;
            });
        }
    });
}
mceEditor = new MceEditor();  