var primaryKey = "id";
var configForm;
var toolbar;

/* This is the definition of our config object */

configObj = Ext.data.Record.create([

	{ name: primaryKey , type: "int" },
	{ name: "admintemplate", type: "string" },
	{ name: "basepath", type: "string" },
	{ name: "contentpath", type: "string" },
	{ name: "downloadpath", type: "string" },
	{ name: "fromadress", type: "string" },
	{ name: "gallerypath", type: "string" },
	{ name: "hostname", type: "string" },
	{ name: "imagepath", type: "string" },
	{ name: "imageurl", type: "string" },
	{ name: "importpath", type: "string" },
	{ name: "keywords", type: "string" },
	{ name: "localPath", type: "string" },
	{ name: "maxAvatarHeight", type: "int" },
	{ name: "maxAvatarWidth", type: "int" },
	{ name: "maxFileUploadSize", type: "int" },
	{ name: "maxImageUploadSize", type: "int" },
	{ name: "password", type: "string" },
	{ name: "productimagepath", type: "string" },
	{ name: "sitename", type: "string" },
	{ name: "smtphost", type: "string" },
	{ name: "startnode", type: "string" },
	{ name: "template", type: "string" },
	{ name: "username", type: "string" },
	{ name: "maintenanceMode", type: "boolean" }
	
]);

/*
 * This file object is needed to acces the templates comboboxes
 */

var fileObj = Ext.data.Record.create([

	{ name: "name", type: "string" },	
	{ name: "absolutePath", type: "string" },
	{ name: "caption", type: "string" },
	{ name: "date", type: "string" },	
	{ name: "size", type: "int" },
	{ name: "type", type: "string" },
	{ name: "path", type: "string" },

]);

/*
 * The node object is needed to fill the cobobox with available nodes
 */

var nodeObj = Ext.data.Record.create([

	{ name: primaryKey , type: "int" },
	{ name: "position", type: "int" },
	{ name: "type", type: "int" },	
	{ name: "name", type: "string" },
	{ name: "linkurl", type: "string" },
	{ name: "pluginparams", type: "string" },
	{ name: "parent", type: "string" },
	{ name: "path", type: "string" },
	{ name: "image", type: "string" },
	{ name: "pluginid", type: "string" },	
	{ name: "firstchild", type: "boolean" }

]);


var templateStore = new Ext.data.Store({
	url: 'ShowConfig.do?do=getTemplatesJSON',
	remoteSort: false,
	reader: new Ext.data.JsonReader({
		root: "results", 
		totalProperty: "total",
		id: name },
		fileObj
	)  
});

var adminTemplateStore = new Ext.data.Store({
	url: 'ShowConfig.do?do=getAdminTemplatesJSON',
	remoteSort: false,
	reader: new Ext.data.JsonReader({
		root: "results", 
		totalProperty: "total",
		id: name },
		fileObj
	)
});

var nodeStore = new Ext.data.Store({
	url: 'ShowConfig.do?do=getAvaliableNodesJSON',
	remoteSort: false,
	reader: new Ext.data.JsonReader({
		root: "results", 
		totalProperty: "total",
		id: name },
		nodeObj
	) 
});

var templateCombo = new Ext.form.ComboBox({
    store: templateStore,
    displayField: 'name',
    typeAhead: true,
    fieldLabel  : usertemplateKey,
    name : 'template',
    forceSelection: true,
    triggerAction: 'all',
    emptyText:'----------',
    selectOnFocus:true,
    width : 300
});

var adminTemplateCombo = new Ext.form.ComboBox({
    store: adminTemplateStore,
    displayField: 'name',
    typeAhead: true,
    fieldLabel  : admintemplateKey,
    name : 'admintemplate',
    forceSelection: true,
    triggerAction: 'all',
    emptyText:'----------',
    selectOnFocus:true,
    width : 300
});

var nodeCombo = new Ext.form.ComboBox({
    store: nodeStore,
    displayField: 'path',
    typeAhead: true,
    fieldLabel  : startnodeKey,
    name : 'startnode',
    forceSelection: true,
    triggerAction: 'all',
    emptyText:'----------',
    selectOnFocus:true,
    width : 300
});





/* This function creates the toolbar of the user grid and defines the action handlers */  
  
function createToolBar(){

	toolbar = new Ext.Toolbar([
	{
		text: mainmenuKey,
		id : 'mainmenuButton',
		handler: function(){
			document.location.href = 'index.jsp';
		},
		iconCls : 'mainmenu-button',
		icon : 'themes/experience/icons/small/arrow_left.png'							
	},
	{
		xtype: 'tbseparator'
	},	
	{
		text: saveKey,
		handler: function(){
			
			configForm.getForm().submit({
				waitMsg: changeWaitKey,
				url: 'ShowConfig.do?do=save&async=true',
				success: function(form, action){
					Ext.MessageBox.show({
				       title: dialogSavedKey,
				       msg: changeSuccessKey ,
				       buttons: Ext.MessageBox.OK,
				       animEl: 'mb4',
				       icon: Ext.MessageBox.INFO
				   });
				},
				failure: function(form, action){
					Ext.MessageBox.show({
				       title: dialogErrorKey,
				       msg: changeFailKey,
				       buttons: Ext.MessageBox.OK,
				       fn: getAnswer,
				       animEl: 'mb4',
				       icon: Ext.MessageBox.WARNING
				   });
				}
			});

		},
		iconCls : 'save-button',
		icon : 'themes/experience/icons/small/disk.png'					
	}])
}

function createConfigForm(){
	
	var commonFieldSet = new Ext.form.FieldSet({
	    id : 'commonFieldSet',
		title : titleGeneralKey,
		autoHeight:true,		
		items: [
			nodeCombo,		
			{ xtype: 'textfield', name: 'sitename', fieldLabel: sitenameKey ,width : 300 },
			{ xtype: 'checkbox', name:  'maintenanceMode', fieldLabel: 'Maintenance' ,width : 100,anchor : '95%'},
			{ xtype: 'textfield', name: 'keywords', fieldLabel: keywordKey ,width : 300 },
			{ xtype: 'textfield', name: 'hostname', fieldLabel: hostnameKey ,width : 300 },
			{ xtype: 'textfield', name: 'maxFileUploadSize', fieldLabel: maxuploadKey,width : 300 },
			templateCombo,
			adminTemplateCombo			
		]        				
	});
	
	var pathFieldSet = new Ext.form.FieldSet({
	    id : 'pathFieldSet',
		title : titlePathsKey,
		autoHeight:true,		
		items: [				
				{ xtype: 'textfield', name: 'basepath', fieldLabel: basepathKey,width : 300  },
				{ xtype: 'textfield', name: 'localPath', fieldLabel: localpathKey,width : 300 },			
				{ xtype: 'textfield', name: 'contentpath', fieldLabel: contentpathKey,width : 300  },			
				{ xtype: 'textfield', name: 'downloadpath', fieldLabel: downloadpathKey,width : 300 },			
				{ xtype: 'textfield', name: 'gallerypath', fieldLabel: gallerypathKey,width : 300 },				
				{ xtype: 'textfield', name: 'imagepath', fieldLabel: imagepathKey,width : 300  },
				{ xtype: 'textfield', name: 'importpath', fieldLabel: importpathKey,width : 300 },
				{ xtype: 'textfield', name: 'productimagepath', fieldLabel: productpathKey,width : 300 }		
		]
	});

	var imageFieldSet = new Ext.form.FieldSet({
	    id : 'imageFieldSet',
		title : titleImagesKey,
		autoHeight:true,		
		items: [
			{ xtype: 'textfield', name: 'imageurl', fieldLabel: gallerypathKey,width : 300 },	
			{ xtype: 'textfield', name: 'maxImageUploadSize', fieldLabel: maximageuploadKey ,width : 300  },						
			{ xtype: 'textfield', name: 'maxAvatarHeight', fieldLabel: maxAvatarHeightKey,width : 300  },			
			{ xtype: 'textfield', name: 'maxAvatarWidth', fieldLabel: maxAvatarWidthKey,width : 300  }
		]        	
	});
	
	var mailFieldSet = new Ext.form.FieldSet({
	    id : 'mailFieldSet',
		title : titleEmailKey,
		autoHeight:true,		
		items: [				
				{ xtype: 'textfield', name: 'username', fieldLabel: usernameKey ,width : 300  },
				{ xtype: 'textfield', name: 'password',inputType : 'password', fieldLabel: passwordKey ,width : 300  },
		        { xtype: 'textfield', name: 'fromadress', fieldLabel: fromadressKey ,width : 300  },
				{ xtype: 'textfield', name: 'smtphost', fieldLabel: smtphostKey ,width : 300  },
		]        		
	});
	
	configForm = new Ext.FormPanel({
		labelWidth: 190, // label settings here cascade unless overridden
		url: 'ShowConfig.do',
		frame: true,
		title: dialogConfigKey,
		bodyStyle: 'padding:5px 5px 0',
		autoScroll: true,
		items: [
		    commonFieldSet,
		    pathFieldSet,
		    imageFieldSet,
		    mailFieldSet
		]        		
	});
	
}

	
				

   
