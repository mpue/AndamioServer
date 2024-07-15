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

var myMCEForm;
var mceEditor;
var tabPanel;
var configPanel;
var editorPanel;
var pluginPanel;
var toolBar
var contentEditor
var contentWindow;
var contentForm;
var pluginWindow;
var pluginEditWindow;
var pluginForm;
var pluginEditForm;
var linkWindow;
var linkEditWindow;
var linkForm;
var linkEditForm;
var primaryKey = "id";
var menuCombo;
var menuname;
var contentname;
var pagename;
var commentsAllowed;
var description;
var metatags;
var uploadDialog;
var appPath = "http://"+hostname+"/"+localpath;
var dirty = false;
var nothingSelected = true;
var numParams = 0;
var paramNames;
var updateNode;
var descriptorsLoaded = false;


// Plugin form fields

var fpluginid;
var fid;
var fname;
var fimage;
var ffchild;
var fexportable;

mceEditor = new MceEditor();

var remainingTime = maxInactiveInterval;

// This part takes track of the remaining session time

function time() {
	
	remainingTime--;
	
	/**
	 *  Session is over (to be exact : 5 second before session ends)
	 *  
	 *  Now the following should happen : Save the current content, if one is selected
	 *  and redirect to the admin overview. 
	 *  
	 */
	
	if (remainingTime <=1) {
		if (!nothingSelected) {
			saver();
		}
		document.location.href=('index.jsp');
	}
 	else
	 	document.TimerForm.time.value=remainingTime;	
}


setInterval('time()',1000);



var nodeObj = Ext.data.Record.create([

	{ name: primaryKey , type: "int" },
	{ name: "position", type: "int" },
	{ name: "type" , type: "int" },
	{ name: "name" , type: "string" },
	{ name: "linkurl", type: "string" },
	{ name: "pluginparams", type: "string" },
	{ name: "parent", type: "string" },
	{ name: "path", type: "string" },
	{ name: "image", type: "string" },
	{ name: "pluginid", type: "string" },	
	{ name: "exportable", type: "boolean" },
	{ name: "rel", type: "string" },
	{ name: "targetName", type: "string" },
	{ name: "firstchild", type: "boolean" }

]);

var descriptorObj = Ext.data.Record.create([

	{ name: "displayName", type: "string" },	                                            
	{ name: "paramId" , type: "string" },
	{ name: "value", type: "string" }

]);

var pluginObj = Ext.data.Record.create([

	{ name: primaryKey , type: "int" },
	{ name: "pluginID", type: "string" },
	{ name: "name", type: "string" }

]);

/* This is the definition of the usergroup object */

var groupObj = Ext.data.Record.create([

	{ name: "description", type: "string" },
    { name: primaryKey , type: "int" },
	{ name: "name", type: "string" }

]);

/**
 * This data store holds all available node objects
 */

var dataStore = new Ext.data.Store({
	  url: appPath + "admin/ShowNode.do?do=getJSON",
	  remoteSort: false,
	  reader: new Ext.data.JsonReader({
	    root: "rows", 
	    id: primaryKey },
	   nodeObj
  ),
  sortInfo:{field:'path', direction:'ASC'}
});


/**
 * This data store holds all available usergroup objects
 */

var usergroupStore = new Ext.data.Store({
	  url: "ListUsergroups.do?do=getJSON",
	  remoteSort: false,
	  reader: new Ext.data.JsonReader({
	    root: "results", 
	    totalProperty: "total",
	    id: primaryKey },
	    groupObj
	  ),
	  sortInfo:{field:'name', direction:'ASC'}
});

usergroupStore.load();


/**
 * The selection model of the usergroup data store
 */
var groupSelectionModel = new Ext.grid.RowSelectionModel(
	{ 
		singleSelect: false 
	}
);

/**
 * This is the column model definition for the editable grid of the groups
 */
var groupColumnModel = new Ext.grid.ColumnModel([                                            
  { header: "id", width: 30, sortable: true, dataIndex: "id" },
  { header: nameKey , width: 140, sortable: true, dataIndex: "name" },
  { header: descriptionKey, width: 140, sortable: true, dataIndex: "description" } 
]);

/**
 * <p>
 * Well, this is not very self explanatory. So we need some words about this function.
 * </p>
 * <p>
 * In order to edit any plugin in a convenient way, we must understand the architecture
 * of a plugin. 
 * </p>
 * <p>
 * Each plugin contains a HashMap of String pairs to display the available
 * plugin paramaters in a user friendly way. This function loads a datastore with the available plugin
 * parameters of the selected plugin. The datastore contains a list of descriptor objects 
 * after loading, which will be used later to build a directory of plugin params.
 * </p>
 * <p>
 * <p>
 * This directory is used to create the fields inside any plugin form (create/edit) and
 * this is why the createFields Method is called here.
 * </p>
 * @see org.pmedv.plugins
 * 
 * @param pluginid
 * @param plugin_params
 * @param _win
 * @param _form
 * @param setValues
 *
 * @return the newly created and initialized data store containing the plugin parameter descriptors
 */


function fillPluginForm(pluginid,plugin_params,_win,_form,setValues) {

	var descriptorStore = new Ext.data.Store({
		url: appPath + "admin/ShowNode.do?do=getParamDescriptorsAsJSON&pluginid="+pluginid,
		remoteSort: false,
		reader: new Ext.data.JsonReader({
			root: "rows", 
			id: primaryKey },
			descriptorObj
		),
		sortInfo:{field:'paramId', direction:'ASC'}
	});
	
	descriptorStore.on('load',function(){
		
		numParams = descriptorStore.getCount();		
		paramNames = new Array(numParams);

		/**
		 * We *need* this in order to be able to save the param fields
		 */
		
		for (i = 0; i < descriptorStore.getCount(); i++) {
			record = descriptorStore.getAt(i);
			paramNames[i] = record.data.paramId;
		}
	
		createFields(descriptorStore,_form);
		
		if(setValues)
			setPluginFieldValues(plugin_params);
		
		_win.doLayout();
	});
	
	descriptorStore.load();
}


function getGroupStore(node_id) {

	var groupStore = new Ext.data.Store({
		url: appPath + "admin/ShowNode.do?do=getGroupsAsJSON&node_id="+node_id,
		remoteSort: false,
		reader: new Ext.data.JsonReader({
			root: "rows", 
			id: primaryKey },
			groupObj
		),
		sortInfo:{field:'name', direction:'ASC'}
	});
	
	return groupStore;
}

/**
 * This data store holds all available plugin objects
 */

var pluginStore = new Ext.data.Store({
	  url: appPath + "admin/ListPlugins.do?do=getJSON",
	  remoteSort: false,
	  reader: new Ext.data.JsonReader({
	    root: "rows", 
	    id: primaryKey },
	   nodeObj
  ),
  sortInfo:{field:'pluginID', direction:'ASC'}
});


function createParentCombo() {
	parentCombo = new Ext.form.ComboBox({
	    store: dataStore,
	    displayField: 'path',
	    typeAhead: true,
	    fieldLabel  : dialogParentKey,
	    name : 'parent',
	    forceSelection: true,
	    allowBlank : false,
	    triggerAction: 'all',
	    emptyText:'----------',
	    selectOnFocus:true,
	    width : 300
	});
	
	parentCombo.on('expand', function(combo){
		dataStore.reload();
	});
}

var relStore = new Ext.data.SimpleStore({
    fields: ['rel'],    
    data : [['lightbox', 'lightbox'],['none', 'none']]
    // next to change: combo.getStore().loadData( new_table );
 });

var targetStore = new Ext.data.SimpleStore({
    fields: ['targetType'],    
    data : [['_blank', '_blank'],['_self', '_self'],['_custom', '_custom']]
    // next to change: combo.getStore().loadData( new_table );
 });


function createRelCombo() {
	relCombo = new Ext.form.ComboBox({
		store : relStore,
	    displayField: 'rel',
	    typeAhead: true,
	    fieldLabel  : "Rel",
	    name : 'rel',
	    forceSelection: true,
	    allowBlank : false,
	    triggerAction: 'all',
	    emptyText:'----------',
	    selectOnFocus:true,
	    width : 300,
	    mode : 'local'
	});

}

function createTargetCombo() {
	targetCombo = new Ext.form.ComboBox({
		store : targetStore,
	    displayField: 'targetType',
	    typeAhead: true,
	    fieldLabel  : "Target Type",
	    name : 'targetType',
	    forceSelection: true,
	    allowBlank : false,
	    triggerAction: 'all',
	    emptyText:'----------',
	    selectOnFocus:true,
	    width : 300,
	    mode : 'local'
	});
	
}


var pluginCombo = new Ext.form.ComboBox({
    store: pluginStore,
    displayField: 'pluginID',
    typeAhead: true,
    fieldLabel  : 'Plugin',
    name : 'pluginid',
    valueField : 'pluginID',
    forceSelection: true,
    allowBlank : false,
    triggerAction: 'all',
    emptyText:'----------',
    selectOnFocus:true,
    width : 300
});

pluginCombo.on('select', function(combo,record,index){
	removeParamFields(pluginForm);
	fillPluginForm(record.data.pluginID,null,pluginWindow,pluginForm,false);	
});

function resetParams() {
	numParams = 0;
}

uploadDialog = new Ext.ux.UploadDialog.Dialog({
	url: appPath+'admin/UploadFile.do?do=upload&async=true&directory=import/',
	el : 'uploadWindow',				
	reset_on_hide: false,
	allow_close_on_upload: true,
	upload_autostart: false,
	post_var_name : 'theFile'
});	

uploadDialog.on('uploadsuccess', function(){
	importNodes();
	setTimeout("reloadTree()",2000);
	uploadDialog.hide();
});


var saveButton = new Ext.Button({
	xtype : 'tbbutton',
	id : 'saveBtn',
	text: saveKey ,
	handler : saveHandler,
	iconCls : 'save-button',
	icon : 'themes/experience/icons/small/disk.png'
	
});

function getAnswerForDirtyBeforeInsertPlugin(answer) {
	if (answer == 'yes' ) {
		Ext.getCmp('saveBtn').disable();
		dirty = false;
		createPluginDialog();
	}	
}

var toolMenu = new Ext.menu.Menu({
	
    id: 'toolMenu',
    items: [
    		{
    			text: newContentKey,
    			handler : function(){
    				createContentDialog();
    			},
    			iconCls : 'content-button',
    			icon : 'themes/experience/icons/small/contents.png'
    		}, 
    		{
    			text: newPluginKey,
    			handler : function(){
	    			if (dirty) {
	    				Ext.MessageBox.show({
	    					title: dialogWarningTitleKey,
	    					msg: dialogContinueKey,
	    					buttons: Ext.MessageBox.YESNOCANCEL,
	    					fn: getAnswerForDirtyBeforeInsertPlugin,
	    					animEl: 'mb4',
	    					icon: Ext.MessageBox.QUESTION
	    				});
	    			}    	
	    			else {
	    				createPluginDialog();
	    			}
    			},
    			iconCls : 'plugin-button',
    			icon : 'themes/experience/icons/small/plug.png'
    		}, 
    		{
    			text: newLinkKey,
    			handler : function(){
	    			if (dirty) {
	    				Ext.MessageBox.show({
	    					title: dialogWarningTitleKey,
	    					msg: dialogContinueKey,
	    					buttons: Ext.MessageBox.YESNOCANCEL,
	    					fn: getAnswerForDirtyBeforeInsertPlugin,
	    					animEl: 'mb4',
	    					icon: Ext.MessageBox.QUESTION
	    				});
	    			}    	
	    			else {
	    				createLinkDialog();
	    			}
    			},
    			iconCls : 'link-button',
    			icon : 'themes/experience/icons/small/link_offline.png'
    		}, 
    		{
    			text: previewKey,
    			handler : function() {
    				// window.open(docBaseUrlNoSlash+currentNode.attributes.nodepath,'preview','width=1024,height=768,scrollbars=yes')
    				preview();
    			},
    			iconCls : 'preview-button',
    			icon : 'themes/experience/icons/small/zoom.png',
    			id : 'previewItem'
    				
    		}, 
    		{
    			text: importContentsKey,
    			handler : function() {
    				uploadDialog.show(this);
    			},
    			iconCls : 'import-button',
    			icon : 'themes/experience/icons/small/folder_add.png'
    				
    		}, 		
    		{
    			text: exportContentsKey,
    			handler : exportNodesHandler,
    			iconCls : 'export-button',
    			icon : 'themes/experience/icons/small/folder_go.png'
    			
    		}, 		
    		{
    			text: 'Standard Metatags',
    			handler : insertBlankMetaTags,
    			iconCls : 'tags-button',
    			icon : 'themes/experience/icons/small/page_white_text.png',
    			id : 'metatagsItem'
    			
    		}	
                
    ]
});


function createToolbar() {
	
	
	toolBar = new Ext.Toolbar({
		
		items: [
		{
			text: mainmenuKey,
			handler: hauptmenuHandler,
			iconCls : 'mainmenu-button',
			icon : 'themes/experience/icons/small/arrow_left.png'				
		}, 
		{
			xtype: 'tbseparator'
		},
		
		saveButton,		

		{
			text: 'Funktionen',
			id : 'toolsButton',
			menu : toolMenu
		}		
		]
	}); 		
	
	Ext.getCmp('saveBtn').disable();
}

function showUploadDialog(field_name, url, type, win) {
	uploadDialog.show(this);
}

function createContentEditor(editorTitle) {

	createToolbar();
	createEditorPanel();
	createConfigPanel();
	createTabPanel();

	contentEditor = new Ext.FormPanel({
	    title: editorTitle,
		layout: 'form',
		closable: true,
		tbar : toolBar,
		items: tabPanel,
	    submit:function() {
			alert('submit');
		} 		
		
	});
	
	contentEditor.on('beforeDestroy',function() { tinyMCE.removeInstances(this.id)},this);
	
}

function createTabPanel()  {

	tabPanel = new Ext.TabPanel({
		region:'center',
		margins:'0 0 5 0',
        deferredRender:false,
    	layoutOnTabChange: true,
        activeTab:0,
		tabPosition:'top',
		plain: false,
		items:[
	        editorPanel,
	        configPanel	        
	    ]
    });
	
	tabPanel.doLayout();

}

function createEditorPanel() {
	
	editorPanel = new Ext.Panel({
	    title:'Editor',
		layout: 'fit',
		items: [
			{ xtype: 'textarea', id : 'contentstring' , plugins: [mceEditor], name : 'contentstring' }
		]
	});				
}

function createConfigPanel() {
	
	var contentnameField = new Ext.form.TextField({ 
		xtype: 'textfield', 
		id: 'contentname',
		name: 'contentname', 
		fieldLabel:contentNameKey ,
		width : 300 ,
		anchor : '90%',
		enableKeyEvents : true 
	});
	
	contentnameField.on('keypress', function(){
		change();
	});

	var descriptionField = new Ext.form.TextField({ 
		xtype: 'textfield', 
		id: 'description',
		name: 'description', 
		fieldLabel:contentDescriptionKey ,
		width : 300 ,
		anchor : '90%',
		enableKeyEvents : true 
	});
	
	descriptionField.on('keypress', function(){
		change();
	});

	var pagenameField = new Ext.form.TextField({ 
		xtype: 'textfield', 
		id: 'pagename',
		name: 'pagename', 
		fieldLabel:contentPagenameKey ,
		width : 300 ,
		anchor : '90%',
		enableKeyEvents : true 
	});

	pagenameField.on('keypress', function(){
		change();
	});
	
    var commentsCheckbox = new Ext.form.Checkbox  ({ 
    	xtype: 'checkbox',  
    	id: 'commentsAllowed', 
    	name: 'commentsAllowed', 
    	fieldLabel: 'Allow comments',
		anchor : '90%',
		enableKeyEvents : true   
   	});	
   	
	commentsCheckbox.on('check', function(){
		change();
	});
	
	var metatagArea = new Ext.form.TextArea({ 
		xtype: 'textarea', 
		id: 'metatags',
		name: 'metatags', 
		fieldLabel:'Metatags' ,
		width : 300 ,
		height : 300 ,
		anchor : '90%',
		enableKeyEvents : true 
	});
	
	metatagArea.on('keypress', function(){
		change();
	});
		
	
	configPanel = new Ext.Panel({
	    title : configKey,
		layout : 'form',
		autoScroll:false,
		bodyStyle:'padding:5px 5px 0',
		closable: false,
		items: [
			contentnameField,
			descriptionField,
			pagenameField,
			commentsCheckbox,
			metatagArea
		]
	
	});

}

function createContentDialog() {

	if (!contentForm)
		createContentForm();	
	
	var contentnameField = Ext.getCmp('_contentname');
	var descriptionField = Ext.getCmp('_description');
	var pagenameField    = Ext.getCmp('_pagename');
	var commentsAllowedBox = Ext.getCmp('_commentsAllowed'); 
	var metatagArea      = Ext.getCmp('_metatags');
	
	contentnameField.setValue('');
	descriptionField.setValue('');
	pagenameField.setValue('');
	commentsAllowedBox.setValue(false);
	metatagArea.setValue('');
	
	if(!contentWindow){
    	contentWindow = new Ext.Window({
            el:'contentWindow',
            layout:'fit',

			animateTarget : 'createButton',
            closeAction:'hide',
            plain: true,
            items: contentForm,
            height : 400,
            width : 500
        });
    }

    contentWindow.show(this);
    
	var field = Ext.getCmp('_contentname');	
	field.focus(false,200);
	
}

function createPluginDialog() {

    deselectContent();
    
	if (!pluginForm)
		createPluginForm();

    parentCombo.setValue('');
	pluginCombo.setValue('');
	
	var pluginNameField   = Ext.getCmp('_plugin_name');	
	var pluginImageField  = Ext.getCmp('_plugin_image');
	var pluginParamsField = Ext.getCmp('_plugin_params');

	pluginNameField.setValue('');  	
	pluginImageField.setValue(''); 
	pluginParamsField.setValue('');
		
	if(!pluginWindow){
    	pluginWindow = new Ext.Window({
            el:'pluginWindow',
            layout:'fit',
			animateTarget : 'createButton',
            closeAction:'hide',
            plain: true,
            items: pluginForm,
            height : 400,
            width : 550
        });
    	pluginWindow.on('hide', function(win){
    		removeParamFields(pluginForm);
    		resetParams();
    	});
    	
    }
	
	pluginForm.focus();	
    pluginWindow.show(this);	
}

function createPluginEditDialog() {
	
	if (!pluginEditForm)
		createPluginEditForm()
	
	if(!pluginEditWindow){
    	pluginEditWindow = new Ext.Window({
            el:'pluginEditWindow',
            layout:'fit',
			animateTarget : 'createButton',
            closeAction:'hide',
            plain: true,
            items: pluginEditForm,
            height : 500,
            width : 550
        });
    	
    	pluginEditWindow.on('hide', function(win){
    		removeParamFields(pluginEditForm);
    		resetParams();
    	});

    }
	
	pluginEditForm.focus();	
    pluginEditWindow.show(this);	
}

function createLinkDialog() {
	if (!linkForm)
		createLinkForm();	
	
	var linkField = Ext.getCmp('name');
	var urlField   = Ext.getCmp('linkurl');	
	var targetField  = Ext.getCmp('targetName');
	
	linkField.setValue('');  	
	urlField.setValue(''); 
	targetField.setValue('');
		
	if(!linkWindow){
		linkWindow = new Ext.Window({
            el:'linkWindow',
            layout:'fit',
			animateTarget : 'createButton',
            closeAction:'hide',
            plain: true,
            items: linkForm,
            height : 300,
            width : 550
        });
		linkWindow.on('hide', function(win){
    		removeParamFields(linkForm);
    		resetParams();
    	});
    	
    }
	
	linkForm.focus();	
	linkWindow.show(this);
}

function createLinkEditDialog() {
	
	if (!linkEditForm)
		createLinkEditForm()
	
	if(!linkEditWindow){
    	linkEditWindow = new Ext.Window({
            el:'linkEditWindow',
            layout:'fit',
			animateTarget : 'createButton',
            closeAction:'hide',
            plain: true,
            items: linkEditForm,
            height : 300,
            width : 550
        });
    	
    	linkEditWindow.on('hide', function(win){
    		removeParamFields(linkEditForm);
    		resetParams();
    	});

    }
	
	linkEditForm.focus();	
	linkEditWindow.show(this);	
}

function createLinkForm() {

	createRelCombo();
	createTargetCombo();
	createParentCombo();
	
	parentCombo.setValue('');
	relCombo.setValue('');
	targetCombo.setValue('');
	
	linkForm = new Ext.FormPanel({
		
		labelWidth: 90, // label settings here cascade unless overridden
		url: appPath + "admin/CreateLink.do",
		frame: true,
		title: 'Details',
		bodyStyle: 'padding:5px 5px 0',
		items: [
			{ xtype: 'textfield', id: 'name', name: 'name', fieldLabel: linkNameKey  ,width : 300 ,anchor : '90%', allowBlank : false },
			{ xtype: 'textfield', id: 'linkurl',  name: 'linkurl', fieldLabel: linkUrlKey ,width : 300 ,anchor : '90%', allowBlank : false },			
			parentCombo,
			relCombo,
			// targetCombo,
			{ xtype: 'textfield', id: 'targetName', name: 'targetName', fieldLabel: targetNameKey   ,width : 300 ,anchor : '90%', allowBlank : true }			
		],
        		
		buttons: [{
			text: 'Save',
			handler : function() {
				
				var values = linkForm.getForm().getValues();
				                  
				linkForm.getForm().submit({
					url: appPath + "admin/CreateLink.do",
					method : 'POST',
					waitMsg : saveWaitKey,
					waitTitle : dialogSavingLinkKey ,
					failure: function(linkForm, action) {
						Ext.MessageBox.alert('Error',action.result.message);	
					},
					success: function(linkForm, action){
						linkWindow.hide();
						Ext.MessageBox.alert('Status',action.result.message);
                        doReload();                        
                        remainingTime = maxInactiveInterval; // reset session
                        
					}
				}); 			
			}
		}, {
			text: 'Cancel',
            handler: function(){
                linkWindow.hide();
            }			
		}]
	});	
}

function createLinkEditForm(){
	
	createParentCombo();
	createRelCombo();
	createTargetCombo();
	
	linkEditForm = new Ext.FormPanel({
		
		labelWidth: 180, // label settings here cascade unless overridden
		url: appPath + "admin/ShowNode.do",
        method : 'GET',
		frame: true,
		title: detailsKey,
		bodyStyle: 'padding:5px 5px 0',
		items: [
		        { xtype: 'textfield', id: '_id', name: 'id', fieldLabel: "Id"  ,width : 300 ,anchor : '90%', allowBlank : false },		        
				{ xtype: 'textfield', id: '_linkname', name: 'name', fieldLabel: linkNameKey  ,width : 300 ,anchor : '90%', allowBlank : false },
				{ xtype: 'textfield', id: '_linkurl',  name: 'linkurl', fieldLabel: linkUrlKey ,width : 300 ,anchor : '90%', allowBlank : false },			
				parentCombo,
				relCombo,
				// targetCombo,
				{ xtype: 'textfield', id: '_targetname', name: 'targetName', fieldLabel: targetNameKey   ,width : 300 ,anchor : '90%', allowBlank : true }			
		],
        		
		buttons: [{
			text: saveKey ,
			handler : function() {
			
				fetchFields();
			
				/* create a new object with the updated values */
				
				var values = linkEditForm.getForm().getValues();
				
				var updateObj = new nodeObj({
					id   : Ext.getCmp('_id'),
					targetName : Ext.getCmp('_targetname'),
					linkurl : Ext.getCmp('_linkurl'),
					name : Ext.getCmp('_linkname'),			
					rel : values['rel']
				});
				
				/* call the submit method and provide the data parameters via the updateObj */
				
				linkEditForm.getForm().submit({
					url: appPath+ "admin/ShowNode.do?do=JSONUpdate",
					method:'POST',
					params : updateObj.data,
					waitMsg: saveWaitKey,
					waitTitle:saveWaitKey,
					failure: function(linkEditForm, action) {
						Ext.MessageBox.alert('Fehler',action.result.message);
					},
					success: function(linkEditForm, action){
						linkEditWindow.hide();
						tree.getRootNode().reload();									
						root.expand(true,true);						
						Ext.MessageBox.alert('Status',action.result.message);
						remainingTime = maxInactiveInterval; // reset session
					}
				});				 			
			}
		}, {
			text: 'Cancel',
            handler: function(){
                linkEditWindow.hide();
            }			
		}]
	});
}

function createContentForm(){

	createParentCombo();
	parentCombo.setValue('');
	
	contentForm = new Ext.FormPanel({
		
		labelWidth: 90, // label settings here cascade unless overridden
		url: appPath + "admin/CreateContent.do",
		frame: true,
		title: 'Details',
		bodyStyle: 'padding:5px 5px 0',
		items: [
			{ xtype: 'textfield', id: '_contentname', name: 'contentname', fieldLabel: contentNameKey  ,width : 300 ,anchor : '90%', allowBlank : false },
			{ xtype: 'textfield', id: '_description', name: 'description', fieldLabel: contentDescriptionKey ,width : 300 ,anchor : '90%', allowBlank : false },
			{ xtype: 'textfield', id: '_pagename',    name: 'pagename',    fieldLabel: contentPagenameKey   ,width : 300 ,anchor : '90%', allowBlank : false },
			{ xtype: 'checkbox',  id: '_commentsAllowed', name: 'commentsAllowed', fieldLabel: 'Allow comments', anchor : '90%'},				
			{ xtype: 'textarea',  id: '_metatags',    name: 'metatags',    fieldLabel: 'Metatags' ,width : 300 , height : 100 , anchor : '90%', allowBlank : true },
			parentCombo
		],
        		
		buttons: [{
			text: 'Save',
			handler : function() {
				
				var values = contentForm.getForm().getValues();
				
				parent = values['parent'];
				contentname = values['contentname']
				description = values['description']
				pagename = values['pagename']
				commentsAllowed = values['commentsAllowed']
				metatags = values['metatags']
				                  
				contentForm.getForm().submit({
					url: appPath + "admin/CreateContent.do?async=true",
					method : 'POST',
					waitMsg : saveWaitKey,
					waitTitle : dialogSavingContentKey ,
					failure: function(contentForm, action) {
						Ext.MessageBox.alert('Error',action.result.message);	
					},
					success: function(contentForm, action){					
						contentWindow.hide();
						Ext.MessageBox.alert('Status',action.result.message);
                        doReload();
                        setTimeout("loadLastCreatedNode()", "500");
                        remainingTime = maxInactiveInterval; // reset session
                        
					}
				}); 			
			}
		}, {
			text: 'Cancel',
            handler: function(){
                contentWindow.hide();
            }			
		}]
	});
}

/**
 * creates a fresh form for plugin node creation
 * @return
 */


function createPluginForm(){

	createParentCombo();
	parentCombo.setValue('');
		
	var commonFieldSet = new Ext.form.FieldSet({
	    id : 'commonFieldSet_',
		title : dialogCommonKey,
		autoHeight:true,
		items: [
			pluginCombo,
			{ xtype: 'textfield', id: '_plugin_name',   name: 'name', fieldLabel: contentPagetitleKey ,width : 200 ,anchor : '90%', allowBlank : false },		
			{ xtype: 'textfield', id: '_plugin_image',  name: 'image', fieldLabel: backgroundImageKey,width : 200 ,anchor : '90%' },		
			{ xtype: 'textfield', id: '_plugin_params', name: 'pluginparams', fieldLabel: parameterKey ,width : 200 ,anchor : '90%' },		
			parentCombo
		]
		
	});
	
	pluginForm = new Ext.FormPanel({
		
		// label settings here cascade unless overridden
		
		labelWidth: 180, 
		url: appPath + "admin/CreateNode.do",
		frame: true,
		title: 'Details',
		bodyStyle: 'padding:5px 5px 0',
		
		items: [
		    commonFieldSet
		],
        		
		buttons: [{
			text: saveKey,
			handler : function() {
			
				var pluginParams = '';
				
				/*
				 * We get all plugin param fields which we have created before
				 * and build a single request parameter 'pluginparams'
				 */
				
				for (i = 0;i < numParams;i++) {					
					var field = Ext.getCmp(paramNames[i]);
					pluginParams += paramNames[i]+'='+field.getValue();
					pluginParams += ','
				}
				
				/* Remove trailing comma */
				
				pluginParams = pluginParams.substring(0, pluginParams.length - 1);
			
				var values = pluginForm.getForm().getValues();
				
				values['pluginparams'] = pluginParams;
				
				pluginForm.getForm().setValues(values);
				
				pluginForm.getForm().submit({
					url: appPath + "admin/CreateNode.do?async=true&type=2",
					method:'POST',
					waitMsg: changeWaitKey,
					waitTitle:dialogSavingContentKey,
					failure: function(pluginForm, action) {
						Ext.Msg.show({
						   title: errorKey,
						   msg: pluginErrorValidationKey,
						   buttons: Ext.Msg.OK,
						   icon: Ext.MessageBox.ERROR
						});
					},
					success: function(pluginForm, action){
						pluginWindow.hide();
						Ext.MessageBox.alert('Status', messageSavedKey);
						tree.getRootNode().reload();	
						dataStore.reload();
						root.expand(true,true);
						remainingTime = maxInactiveInterval; // reset session
					}
				}); 			
			}
		}, {
			text: cancelKey,
            handler: function(){

                pluginWindow.hide();
            }			
		}]
	});
}

/**
 *  get the basic form fields
 */

function fetchFields() {
    fid         = Ext.getCmp('id');
    fpluginid   = Ext.getCmp('_plugin_id');
    fname       = Ext.getCmp('_plugin_name_');
    fimage      = Ext.getCmp('_plugin_image_');
    ffchild     = Ext.getCmp('_plugin_firstchild');
    fexportable = Ext.getCmp('_plugin_exportable');
}

/**
 * removes the dynamically allocated param fields
 * @return
 */

function removeParamFields(_form) {
	
	var paramFieldSet = Ext.getCmp('paramFieldSet');
	
	for (i = 0;i < numParams;i++) {					
		var field = Ext.getCmp(paramNames[i]);
		if (field) {
			paramFieldSet.remove(field);
		}
	}	
	_form.remove(paramFieldSet);
}

/**
 * creates a fresh form to edit a plugin
 * 
 * @return
 */

function createPluginEditForm(){
	
	createParentCombo();
	
	pluginEditForm = new Ext.FormPanel({
		
		labelWidth: 180, // label settings here cascade unless overridden
		url: appPath + "admin/ShowNode.do",
        method : 'GET',
		frame: true,
		title: detailsKey,
		bodyStyle: 'padding:5px 5px 0',
		items: [
		    parentCombo
		],
        		
		buttons: [{
			text: saveKey ,
			handler : function() {
	
				var pluginParams = '';
				
				/*
				 * We get all plugin param fields which we have created before
				 * and build a single request parameter 'pluginparams'
				 */
				
				for (i = 0;i < numParams;i++) {					
					var field = Ext.getCmp(paramNames[i]);
					pluginParams += paramNames[i]+'='+field.getValue();
					pluginParams += ',';
				}
				
				/* Remove trailing comma */
				
				pluginParams = pluginParams.substring(0, pluginParams.length - 1);
			
				fetchFields();
			
				/* create a new object with the updated values */
				
				var updateObj = new nodeObj({
					id   : fid.getValue(),
					pluginid : fpluginid.getValue(),
					image : fimage.getValue(),
					name : fname.getValue(),
					firstchild : ffchild.getValue(),
					pluginparams : pluginParams
					
				});
				
				/* call the submit method and provide the data parameters via the updateObj */
				
				pluginEditForm.getForm().submit({
					url: appPath+ "admin/ShowNode.do?do=JSONUpdate",
					method:'POST',
					params : updateObj.data,
					waitMsg: saveWaitKey,
					waitTitle:saveWaitKey,
					failure: function(pluginEditForm, action) {
						Ext.MessageBox.alert('Fehler',action.result.message);
					},
					success: function(pluginEditForm, action){
						pluginEditWindow.hide();
						tree.getRootNode().reload();									
						root.expand(true,true);						
						Ext.MessageBox.alert('Status',action.result.message);
						remainingTime = maxInactiveInterval; // reset session
					}
				});
				
				/* clear the form from dynamic fields */
				
				removeParamFields(pluginEditForm);
				 			
			}
		}, {
			text: 'Cancel',
            handler: function(){
                pluginEditWindow.hide();
                removeParamFields(pluginEditForm);
            }			
		}],
		reader: {
			  read: function(response) {
		
			    // Decode the incoming data
			    var resText = Ext.decode(response.responseText);
	    
			    // Decode the nested plugin_params

			    var data = resText['data']; 		    

			    // This is a little bit more complicated, we need to check if any field exist already
			    // if this is the case, the field has to be removed and reinstantiated, if we would not 
			    // do so, the fields would appear as often as we create the plugin form.
			    
			    fetchFields();
			    
			    // remove field from form if it exists
		    
			    if (fid)
			    	pluginEditForm.remove(fid);
			    if (fpluginid)
			    	pluginEditForm.remove(fpluginid);
			    if(fname)
			    	pluginEditForm.remove(fname);
			    if (fimage)
			    	pluginEditForm.remove(fimage);
			    if (ffchild)
			    	pluginEditForm.remove(ffchild);
			    if (fexportable)
			    	pluginEditForm.remove(fexportable);
			    
			    
			    // (re)create fields
			    
			    fid       = new Ext.form.TextField({ xtype: 'textfield', 
			    									 id: 'id',  
			    									 name: 'id', 
			    									 fieldLabel: 'Id',
			    									 width : 200 ,
			    									 anchor : '90%', 
			    									 readOnly : true , 
			    									 hidden : false});
			    
			    fpluginid = new Ext.form.TextField({ xtype: 'textfield', 
			    									 id: '_plugin_id',   
			    									 name: 'pluginid', 
			    									 fieldLabel: 'Plugin',
			    									 width : 200 ,
			    									 anchor : '90%', 
			    									 readOnly : true });
			    
			    fname 	  = new Ext.form.TextField ({ xtype: 'textfield', 
			    									  id: '_plugin_name_',   
			    									  name: 'name', 
			    									  fieldLabel: contentPagenameKey,
			    									  width : 200 ,
			    									  anchor : '90%' });
			    
			    fimage    = new Ext.form.TextField ({ xtype: 'textfield', 
			    									  id: '_plugin_image_',  
			    									  name: 'image', 
			    									  fieldLabel: backgroundImageKey,
			    									  width : 200 ,
			    									  anchor : '90%' });
			    
			    ffchild   = new Ext.form.Checkbox  ({ xtype: 'checkbox',  
			    									  id: '_plugin_firstchild', 
			    									  name: 'firstchild', 
			    									  fieldLabel: jumpToFirstChildKey  });

			    fexportable   = new Ext.form.Checkbox  ({ xtype: 'checkbox',  
													  id: '_plugin_exportable', 
													  name: 'exportable', 
													  fieldLabel: 'Export' });

			    
			    /*
			     * Put the data from the response into their according fields
			     */
			    
			    parentCombo.setValue(data['parent']);
			    fpluginid.setValue(data['pluginid']);
			    fid.setValue(data['id']);
			    fname.setValue(data['name']);
			    fimage.setValue(data['image']);
			    ffchild.setValue(data['firstchild']);
			    fexportable.setValue(data['exportable']);
			    
			    /*
			     * add the fields to the form 
			     */
			    
			    var commonFieldSet = Ext.getCmp('commonFieldSet');
			    
			    if (commonFieldSet)
			    	pluginEditForm.remove(commonFieldSet);
			    	
				commonFieldSet = new Ext.form.FieldSet({
				    id : 'commonFieldSet',
					title : commonKey,
					autoHeight:true					
				});

				commonFieldSet.add(fid);
				commonFieldSet.add(fpluginid);
				commonFieldSet.add(fname);
				commonFieldSet.add(fimage);
				commonFieldSet.add(ffchild);
				commonFieldSet.add(fexportable);

				pluginEditForm.add(commonFieldSet);
			    
			    /* 
			     * Split up the parameter 'pluginparams' 
			     */ 
			    
			    var params = data['pluginparams'];
			    var plugin_params = params.split(',');
			    fillPluginForm(data['pluginid'],plugin_params,pluginEditWindow,pluginEditForm,true);			    			    			   
			    
			    return resText;
			    		     
			  }
			  
		}		
	});
}

function createFields(_store,_form) {
	
	var paramFieldSet = new Ext.form.FieldSet({
	    id : 'paramFieldSet',
		title : parameterKey,
		autoHeight:true	
	});

	
	for (i = 0; i < _store.getCount(); i++) {

		var record = _store.getAt(i);
		
		var field = Ext.getCmp(record.data.paramId); 
    	
    	if (field)
    		_form.remove(field);
    	
    	/*
    	 * Create the according field,set the value and add it to the form
    	 */
    	
    	field = new Ext.form.TextField({ xtype: 'textfield', id: record.data.paramId, name: record.data.paramId, fieldLabel:record.data.displayName ,width : 300 ,anchor : '90%',allowBlank : false });
    	
    	paramFieldSet.add(field);
	
	}	

	_form.add(paramFieldSet);
}

function setPluginFieldValues(plugin_params) {

	if (plugin_params)
	
	    for (var i = 0; i < plugin_params.length; i++) {
	
		    /**
		     * Determine the name and the value of each param and put the name into an array
		     */
	
	    	var j = plugin_params[i].split('=');		    	
	    	var paramName = j[0];
	    	
	    	var field; 
	    	
	    	field = Ext.getCmp(j[0]);
	    	
	    	if (field)
	    		field.setValue(j[1]);			    	
	
	    }
	
}

function setContentEditor(title) {

	myMCEForm.setTitle(title);
	
	return myMCEForm;
}

function doProceed(answer) {
	
	if (answer == 'yes' ) {
		document.location.href = 'index.jsp';
	}
	
}

function hauptmenuHandler() {
	
	if (dirty) {
		Ext.MessageBox.show({
			title: dialogWarningTitleKey,
			msg: dialogContinueKey,
			buttons: Ext.MessageBox.YESNOCANCEL,
			fn: doProceed,
			animEl: 'mb4',
			icon: Ext.MessageBox.QUESTION
		});
	}
	else {
		document.location.href = 'index.jsp';
	}
	
}

function exportNodesHandler() {
	document.location.href = 'ShowNode.do?do=exportNodes';
}

/**
 * This function is called, when the save button is pressed
 * @return
 */

function saveHandler() {
	saver();
	Ext.getCmp('saveBtn').disable();
	dirty = false;
	Ext.MessageBox.alert('Status', changeSuccessKey );	
}

/**
 * Creates a new dialog in order to edit group associations
 * 
 * @param node_id
 * @return
 */

function createGroupEditDialog(node_id) {
	
	var groupStore = getGroupStore(node_id);
	groupStore.load();
	
	var toolbar = new Ext.Toolbar([
	    {
			text: addGroupKey,
			id : 'addGroup',
			handler : function() {
	    		createGroupSelectionDialog(groupStore,node_id);	    		
	    	}
		},
	    {
			text: removeGroupKey,
			id : 'removeGroup',
			handler : function() {
				var selectedRow = groupSelectionModel.getSelected();
	    		removeGroup(node_id,selectedRow.data.id);
	    		groupStore.remove(selectedRow);	    		
	    	}
		} 
		
	]);
	
	var groupGrid = new Ext.grid.EditorGridPanel(
	{    
		store: groupStore,
		colModel: groupColumnModel,
		autoScroll: true,
		sm: groupSelectionModel,
		stripeRows: true,
		clicksToEdit:1,
		closable: true,
		tbar : toolbar,
		bbar: new Ext.PagingToolbar({
			pageSize: 100,
			store: dataStore,
			displayMsg: "{0} - {1} "+fromKey + "{2} "+recordsKey,
			displayInfo: true,
			emptyMsg: emptyMessageKey
		})
	});

	var groupEditWindow = new Ext.Window({
        layout:'fit',
        closeAction:'hide',
        title : groupRightsKey,
        plain: true,
        items: groupGrid,
        height : 300,
        width : 500
    });
    	
	groupEditWindow.on('hide', function(win){
		groupGrid.destroy();
		groupEditWindow.destroy();
	});
		
	groupEditWindow.show(this);	
	groupEditWindow.focus();
	
}

/**
* Creates a new dialog in order to select groups
*
* @param groupStore
* @param node_id
*
* @return
*/

function createGroupSelectionDialog(groupStore, node_id) {
	
	var groupSelectGrid = new Ext.grid.GridPanel(
	{    
		store: usergroupStore,
		colModel: groupColumnModel,
		autoScroll: true,
		sm: groupSelectionModel,
		stripeRows: true,
		closable: true
	});

	var groupSelectionWindow = new Ext.Window({
		layout:'fit',
		closeAction:'hide',
		title : availableGroupsKey,
		plain: true,
		items: groupSelectGrid,
		buttons: [{
			text: 'Ok',
			handler : function() {							
				for (i = 0;i < groupSelectionModel.getCount();i++) {
					var rec = groupSelectionModel.getSelections()[i];					
					addGroup(node_id,rec.data.id);
				}				
				groupStore.add(groupSelectionModel.getSelections());				
				// groupStore.reload();
				groupSelectionWindow.hide();
       	   	}		
        }, 
		{
		    text: cancelKey,
	        handler: function(){
        		groupSelectionWindow.hide();
			}			
		}],       
       height : 300,
       width : 500
   });
   	
	groupSelectionWindow.on('hide', function(win){
		groupSelectGrid.destroy();
		groupSelectionWindow.destroy();
	});
		
	groupSelectionWindow.show(this);	
	groupSelectionWindow.focus();
	
}

function loadLastCreatedNode() {

    var url = 'ShowNode.do?do=getNodeAsJson&getLast=true'

    if (window.XMLHttpRequest) { // Non-IE browsers
        html = new XMLHttpRequest();

        html.onreadystatechange = loadLastNodeState;

        try {
            html.open("GET", url, true);
        }
        catch (e) {
            alert(e);
        }

        html.send(null);

    }
    else if (window.ActiveXObject) { // IE
        html = new ActiveXObject("Microsoft.XMLHTTP");

        if (html) {
            html.onreadystatechange = loadLastNodeState;
            html.open("GET", url, true);
            html.send();
        }
    }
    remainingTime = maxInactiveInterval;
}

function doReload() {
    dataStore.reload();
    tree.getRootNode().reload();
    root.expand(true,true);
}

function loadLastNodeState() {
    if (html.readyState == 4) { // Complete
      	if (html.status == 200) { // OK response

            var response = html.responseText;
            var node = Ext.util.JSON.decode(response).data;

            currentNode = tree.getNodeById(node.id);
            tree.getSelectionModel().select(currentNode);
            
            contentEditor.load({
                url:'ShowNode.do?do=getContentAsJson&content_id='+currentNode.attributes.content_id,
                method : 'GET',
                waitMsg: messageLoadingKey,                
				success : function(form, action){					
					var metatagsField = Ext.getCmp('metatags');						
					var s = metatagsField.getValue(); 
					
					var str = s.convertHTMLEntity();
										
					var newstr = "";
					var c;
					for (var i = 0; i<str.length; i++) {
	
						c = str.charAt(i);
						
						if (i > 1 && c == '<') {
							newstr = newstr + '\n';
						}
						
						newstr = newstr + c;
	
					}

					metatagsField.setValue(newstr);
				
            	}
                
            });
            retrieveHTML('ShowNode.do?do=getContent&node_id=' + currentNode.attributes.node_id );
            document.getElementById("id").value = currentNode.attributes.content_id;
   		}
   		else {
            alert("Node could not be loaded.")
   		}
	}
}

function insertBlankMetaTags() {
	
	var tags =
		'&lt;meta name=&quot;author&quot; content=&quot;&quot;&gt;'+
		'&lt;meta name=&quot;publisher&quot; content=&quot;&quot;&gt;'+
		'&lt;meta name=&quot;copyright&quot; content=&quot;&quot;&gt;'+
		'&lt;meta name=&quot;description&quot; content=&quot;&quot;&gt;'+
		'&lt;meta name=&quot;keywords&quot; content=&quot;&quot;&gt;'+
		'&lt;meta name=&quot;page-topic&quot; content=&quot;&quot;&gt;'+
		'&lt;meta name=&quot;page-type&quot; content=&quot;&quot;&gt;'+
		'&lt;meta name=&quot;audience&quot; content=&quot;&quot;&gt;'+
		'&lt;meta http-equiv=&quot;content-language&quot; content=&quot;de&quot;&gt;'+
		'&lt;meta name=&quot;robots&quot; content=&quot;index, follow&quot;&gt;'+
		'&lt;meta name=&quot;DC.Creator&quot; content=&quot;&quot;&gt;'+
		'&lt;meta name=&quot;DC.Publisher&quot; content=&quot;&quot;&gt;'+
		'&lt;meta name=&quot;DC.Rights&quot; content=&quot;&quot;&gt;'+
		'&lt;meta name=&quot;DC.Description&quot; content=&quot;&quot;&gt;'+
		'&lt;meta name=&quot;DC.Language&quot; content=&quot;de&quot;&gt;';	

		
		var metatagsField = Ext.getCmp('metatags');						
		
		var str = tags.convertHTMLEntity();
							
		var newstr = "";
		var c;
		for (var i = 0; i<str.length; i++) {

			c = str.charAt(i);
			
			if (i > 1 && c == '<') {
				newstr = newstr + '\n';
			}
			
			newstr = newstr + c;

		}

		metatagsField.setValue(newstr);
		change();	

}

function preview() {

	var iMyWidth;
	var iMyHeight;

	// half the screen width minus half the new window width (plus 5 pixel borders).
 
	iMyWidth = (window.screen.width / 2) - (75 + 10);
	
	// half the screen height minus half the new window height (plus title and status bars).
 
	iMyHeight = (window.screen.height / 2) - (100 + 50);

	var win = window
			.open(
					docBaseUrlNoSlash+currentNode.attributes.nodepath,
					"Preview",
					"status=no,height=768,width=1024,resizable=yes,left="
					+ iMyWidth
					+ ",top="
					+ iMyHeight
					+ ",screenX="
					+ iMyWidth
					+ ",screenY="
					+ iMyHeight
					+ ",toolbar=no,menubar=no,scrollbars=yes,location=no,directories=no");
	win.focus();
	
}
