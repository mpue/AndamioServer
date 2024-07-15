var primaryKey = "id";

var leftFileGrid; 
var leftToolbar;
var leftFileStore;
var leftColumnModel;
var leftSelectionModel;
var leftIndicatorPos = 0;
var leftOldPos = 0;

var rightFileGrid; 
var rightToolbar;
var rightFileStore;
var rightColumnModel;
var rightSelectionModel;
var rightIndicatorPos = 0;
var rightOldPos = 0;

var leftPath  = "/";
var rightPath = "/"; 

var uploadDialog;
var appPath = "http://"+hostname+"/"+localpath;
var downloadActionPath = appPath + "admin/fileDownload.do";

var firstload = true;
var direction = 'left';
var jobStore;

var editWindow;
var fileForm;

var screenWidth = 800, screenHeight = 600;

if (parseInt(navigator.appVersion) > 3) {
	
	if (navigator.appName == "Netscape") {
		screenWidth = window.innerWidth;
		screenHeight = window.innerHeight;
	}
	if (navigator.appName.indexOf("Microsoft") != -1) {
		screenWidth = document.body.offsetWidth;
		screenHeight = document.body.offsetHeight;
	}
}


var File = Ext.data.Record.create([
    { name: "content", type: "string" },
	{ name: "filename", type: "string" },
]);

EditArea = function(){
   Ext.apply(this, {
       init : function(textarea){
           textarea.on('render', function(textarea){
                editAreaLoader.init({
                    id: textarea.getId() // id of the textarea to transform              
                    ,start_highlight: true  // if start with highlight                    
                    ,allow_toggle: false
                    ,word_wrap: true
                    ,language: "en"
                    ,syntax: "java"
                    ,allow_resize: "both"
                    ,min_width : screenWidth - 90
                    ,min_height : screenHeight - 225
                 });           
           });
       }
   });
}

var editArea = new EditArea();

var jobObj = Ext.data.Record.create([
	{ name: "name", type: "string" },
	{ name: "description", type: "string" },
	{ name: "fireTime", type: "string" },
	{ name: "jobRunTime", type: "string" },
	{ name: "progress", type: "int" }
]);

leftColumnModel = new Ext.grid.ColumnModel([
	{ header: "Name", width: 250, sortable: true, dataIndex: "caption" },
	{ header: "Typ", width: 140, sortable: true, dataIndex: "type"},
	{ header: "Datum", width: 160, sortable: true, dataIndex: "date", align : 'right'},
	{ header: "Gr&ouml;sse", width: 80, sortable: true, dataIndex: "size", align : 'right' }
]);

rightColumnModel = new Ext.grid.ColumnModel([
	{ header: "Name", width: 250, sortable: true, dataIndex: "caption" },
	{ header: "Typ", width: 140, sortable: true, dataIndex: "type"},
	{ header: "Datum", width: 160, sortable: true, dataIndex: "date", align : 'right'},
	{ header: "Gr&ouml;sse", width: 80, sortable: true, dataIndex: "size", align : 'right' }
]);

leftSelectionModel = new Ext.grid.RowSelectionModel(
	{ 
		singleSelect: true 
	}
)

leftSelectionModel.on('rowselect', function(model,rowindex,record){
	direction = 'left';
});

rightSelectionModel = new Ext.grid.RowSelectionModel(
	{ 
		singleSelect: true 
	}
)

rightSelectionModel.on('rowselect', function(model,rowindex,record){
	direction = 'right';
});

/* This is the definition of our user object */

var fileObj = Ext.data.Record.create([

	{ name: "name", type: "string" },	
	{ name: "absolutePath", type: "string" },
	{ name: "caption", type: "string" },
	{ name: "date", type: "string" },	
	{ name: "size", type: "int" },
	{ name: "type", type: "string" },
	{ name: "path", type: "string" },

]);

var leftProxy = new Ext.data.HttpProxy ({
    url: 'ListDirectories.do?do=enter&dir=&getJSON=true'
});

var rightProxy = new Ext.data.HttpProxy ({
    url: 'ListDirectories.do?do=enter&dir=&getJSON=true'
});

function doRefresh(direction) {

	if (direction == 'left') {
		leftFileStore.reload();
		leftFileGrid.reconfigure(leftFileStore,leftColumnModel);
	}
	else {
		rightFileStore.reload();
		rightFileGrid.reconfigure(rightFileStore,rightColumnModel);		
	}
	
}

function createUploadDialog(direction) {

	var uploadPath;
	
	if (direction == 'left') {		
		uploadPath = leftPath;
	}
	else {
		uploadPath = rightPath;
	}	
	
	uploadPath += '/';
	
	uploadDialog = new Ext.ux.UploadDialog.Dialog({
		url: appPath+'admin/UploadFile.do?do=upload&async=true&directory='+uploadPath,				
		reset_on_hide: false,
		allow_close_on_upload: true,
		upload_autostart: false,
		post_var_name : 'theFile'
	});	
	
	uploadDialog.on('uploadsuccess', function(){
		
		doRefresh(direction);
		uploadDialog.destroy();
	});
	
}

function showRenameFileDialog(record,path,direction) {
	
	if (path == '/')
		path = '';
	else
		path += '/';
		
	var renameWindow;
	var renameForm;
	
	renameForm = new Ext.FormPanel({
		
		// label settings here cascade unless overridden
		
		labelWidth: 180, 
		url: appPath + "admin/ListDirectories.do",
		frame: true,
		title: 'Datei umbenennen',
		bodyStyle: 'padding:5px 5px 0',
		
		items: [
			{ xtype: 'textfield', id: 'oldName',  name: 'oldName', fieldLabel: 'Alter Name',width : 200 ,anchor : '90%', readOnly : true },		
			{ xtype: 'textfield', id: 'newName',  name: 'newName', fieldLabel: 'Neuer Name',width : 200 ,anchor : '90%', allowBlank : false },				    
		],
        		
		buttons: [{
			text: 'Save',
			handler : function() {				
				renameForm.getForm().submit({
					url: appPath + "admin/ListDirectories.do?do=rename",
					method:'POST',
					waitMsg:'Benenne um, bitte warten ...',
					waitTitle:'Datei umbenennen.',
					failure: function(pluginForm, action) {
						Ext.MessageBox.alert('Error',action.result.message);
					},
					success: function(pluginForm, action){						
						Ext.MessageBox.alert('Status',action.result.message);
						renameWindow.hide();
						doRefresh(direction);	
					}
				}); 			
			}
		}, {
			text: 'Cancel',
            handler: function(){
				renameWindow.hide();
			}			
		}]
	});	
	
	var field; 
	
	field = Ext.getCmp('oldName'); 		    		
	field.setValue(path+record.data.name);	
	field = Ext.getCmp('newName'); 		    		
	field.setValue(path+record.data.name);	
	
	renameWindow = new Ext.Window({
        layout:'fit',
		animateTarget : 'createButton',
        closeAction:'destroy',
        plain: true,
        title : 'Datei umbenennen',
        items : renameForm,
        modal : true,
        height : 170,
        width : 600
    });
	
	renameWindow.on('hide', function(){
		renameWindow.destroy();
		renameForm.destroy();
	});
	
	renameWindow.show(this);
	
	var field = Ext.getCmp('newName');	
	field.focus(true,200);
	
	
}

function showCreateDirectoryDialog(direction) {
		
	var directoryWindow;
	var directoryForm;
	
	directoryForm = new Ext.FormPanel({
		
		// label settings here cascade unless overridden
		
		labelWidth: 180, 
		url: appPath + "admin/ListDirectories.do",
		frame: true,
		title: 'Verzeichnis erstellen',
		bodyStyle: 'padding:5px 5px 0',
		
		items: [
			{ xtype: 'textfield', id: 'dir',  name: 'dir', fieldLabel: 'Verzeichnisname',width : 200 ,anchor : '90%' },						    
		],
        		
		buttons: [{
			text: 'Anlegen',
			handler : function() {				
			
					field = Ext.getCmp('dir');
					
					var value = field.getValue();
					
					if (direction == 'left')					
						field.setValue(leftPath+"/"+value);	
					else
						field.setValue(rightPath+"/"+value);
					
					directoryForm.getForm().submit({
						url: appPath + "admin/ListDirectories.do?do=createDir",
						method:'POST',
						waitMsg:'Erstelle Verzeichnis, bitte warten ...',
						waitTitle:'Verzeichnis erstellen ...',
						failure: function(pluginForm, action) {
							Ext.MessageBox.alert('Fehler', action.result.message);
						},
						success: function(pluginForm, action){						
							Ext.MessageBox.alert('Status',action.result.message);
							directoryWindow.hide();
							doRefresh(direction);					
						}
					}); 			
				}
		}, 
		{
			text: 'Abbrechen',
            handler: function() {
				directoryWindow.hide();
			}			
		}]
	});	
			
	directoryWindow = new Ext.Window({
        layout:'fit',
		animateTarget : 'createDirButton',
        closeAction:'destroy',
        plain: true,
        title : 'Verzeichnis erstellen',
        items : directoryForm,
        modal : true,
        height : 150,
        width : 600
    });
	
	directoryWindow.on('hide', function(){
		directoryWindow.destroy();
		directoryForm.destroy();
	});
	
	directoryWindow.show(this);
	
	var field = Ext.getCmp('dir');
	
	field.focus(false,200);
	
}


/* This is the data store definition for the file object */ 

function createLeftStore(directory) {

	leftFileStore = new Ext.data.Store({
	  proxy : leftProxy,
	  remoteSort: false,
	  reader: new Ext.data.JsonReader({
	    root: "results", 
	    totalProperty: "total",
	    id: name },
	    fileObj
	  )	  
	});
	
	leftFileStore.on('load',function(){				
		leftSelectionModel.selectRow(leftIndicatorPos);
		leftFileGrid.getView().focusRow(leftIndicatorPos);			
	});
 
}

function createRightStore(directory) {

	rightFileStore = new Ext.data.Store({
	  proxy : rightProxy,
	  remoteSort: false,
	  reader: new Ext.data.JsonReader({
	    root: "results", 
	    totalProperty: "total",
	    id: name },
	    fileObj
	  )	  
	});
	
	rightFileStore.on('load',function(){	
		if (!firstload) {
			rightSelectionModel.selectRow(rightIndicatorPos);
			rightFileGrid.getView().focusRow(rightIndicatorPos);			
		}
		else 
			firstload = false;
	});
	
 
}

createLeftStore(currentDir);
createRightStore(currentDir);

function createCopyProgressWindow(record) {
	
	var progressTable;
	var finished = false;
	var intervalId;
	
	Ext.MessageBox.show({
        title: 'Bitte warten...',
        msg: 'Datei wird kopiert',
        progressText: 'Kopiere...',
        width:300,
        progress:true,
        closable:false,
        animEl: 'mb6'
    });

	jobStore = new Ext.data.Store({
		url: "JobAction.do?do=list",
		remoteSort: false,
		reader: new Ext.data.JsonReader({
			root: "results", 
			totalProperty: "total",
			id: name },
			jobObj
		),
		sortInfo:{field:'name', direction:'ASC'}
	});	
	
	jobStore.on('load',function(){				

		progressTable = new Array();
		
		for (i = 0; i < jobStore.getCount(); i++) {
			var job = jobStore.getAt(i);
			progressTable[job.data.name] = job.data.progress;				
		}
		
		var progressCount = progressTable['copyJob'+record.data.name];
		
		if (!progressCount) {
			
			finished = true;
			Ext.MessageBox.hide();
			clearInterval(intervalId);
			
			if (direction == 'left')			
				setTimeout("doRefresh('right')",500);
			else
				setTimeout("doRefresh('left')",500);
			
		}			
			
		Ext.MessageBox.updateProgress(progressCount/100, progressCount+'% kopiert.');

	});
	
	intervalId = setInterval('jobStore.load()',1000);

}
/**
 * This is the handler function for the copy action
 * 
 * @param direction the direction to use
 * 
 * @return
 */

function createCopyJob(direction) {
	
	var record;
	var path;
	
	if (direction == 'left') {
		path = rightPath;
		record = leftSelectionModel.getSelected();
	}
	else {
		path = leftPath;
		record = rightSelectionModel.getSelected();
	}
	
	if (record.data.type == 'File') {
	
		if (leftPath == rightPath) {
			Ext.MessageBox.show({
			       title:'Information',
			       msg: 'Dateien k&ouml;nnen nicht auf sich selbst kopiert werden.',
			       buttons: Ext.MessageBox.OK,
			       icon: Ext.MessageBox.WARNING
			});				
		}	
		else {		
			copySelected(record,path);					
			createCopyProgressWindow(record);										
		}
		
	}
	else {
		
		Ext.MessageBox.show({
		       title:'Information',
		       msg: 'Es k&ouml;nnen keine Verzeichnisse kopiert werden.',
		       buttons: Ext.MessageBox.OK,
		       icon: Ext.MessageBox.WARNING
		});
		
	}
	
}

function createLeftToolbar(){

	leftToolbar = new Ext.Toolbar([
	{
		text: 'Hauptmen&uuml;',
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
		text: 'Aktualisieren',
		id : 'refreshLeftButton',
		handler: function(){
			doRefresh('left');
		},
		iconCls : 'refresh-button',
		icon : 'themes/experience/icons/small/refresh.png'	
	},
	{
		xtype: 'tbseparator'
	},
	{
		text: 'Dateien hochladen',
		id : 'uploadLeftButton',
		handler: function(){
			createUploadDialog('left');
			uploadDialog.show(this);
		},
		iconCls : 'upload-button',
		icon : 'themes/experience/icons/small/folder_go.png'	
		
	},
	{
		xtype: 'tbseparator'
	},
	{
		text: 'Verzeichnis erstellen',
		id : 'createDirLeftButton',
		handler: function(){
			showCreateDirectoryDialog('left');
		},
		iconCls : 'create-dir-button',
		icon : 'themes/experience/icons/small/folder_add.png'		
	
	},
	{
		xtype: 'tbseparator'
	},
	{
		text: 'Kopieren',
		id : 'copyLeftButton',
		handler: function(){
			direction = 'left';
			createCopyJob('left');			
		},
		iconCls : 'copy-button',
		icon : 'themes/experience/icons/small/disk_multiple.png'		
	}	
	
	])
}

function createRightToolbar(){

	rightToolbar = new Ext.Toolbar([
	{
		text: 'Aktualisieren',
		id : 'refreshRightButton',
		handler: function(){
			rightFileStore.reload();
			rightFileGrid.reconfigure(rightFileStore,rightColumnModel);			
		},
		iconCls : 'refresh-button',
		icon : 'themes/experience/icons/small/refresh.png'			
	},
	{
		xtype: 'tbseparator'
	},
	{
		text: 'Dateien hochladen',
		id : 'uploadRightButton',
		handler: function(){
			createUploadDialog('right');
			uploadDialog.show(this);
		},
		iconCls : 'upload-button',
		icon : 'themes/experience/icons/small/folder_go.png'			
	},	
	{
		xtype: 'tbseparator'
	},
	{
		text: 'Verzeichnis erstellen',
		id : 'createDirRightButton',
		handler: function(){
			showCreateDirectoryDialog('right');
		},
		iconCls : 'create-dir-button',
		icon : 'themes/experience/icons/small/folder_add.png'			
	},
	{
		xtype: 'tbseparator'
	},
	{
		text: 'Kopieren',
		id : 'copyLeftButton',
		handler: function(){
			direction = 'right';
			createCopyJob('right');			
		},
		iconCls : 'copy-button',
		icon : 'themes/experience/icons/small/disk_multiple.png'				
	}	
	])
}

function createLeftFileGrid() {

	/* get form from html page */
	
	createLeftToolbar();

	/* define the grid */

	leftFileGrid = new Ext.grid.GridPanel(
	{    
		id : 'leftPanel',
		store: leftFileStore,
		colModel: leftColumnModel,
		autoScroll: true,
		sm: leftSelectionModel,
		stripeRows: true,
		tbar : leftToolbar,
		bbar: new Ext.PagingToolbar({
			pageSize: 9999,
			store: leftFileStore,
			displayMsg: "{0} - {1} of {2} Records",
			displayInfo: true,
			emptyMsg: "No data to display"
		})
	});
	
	leftFileGrid.on('rowdblclick', function(grid,index,object){		
		var record = leftFileStore.getAt(index);		
		selectFile(record,'left');
		
	});
	
	leftFileGrid.on('rowcontextmenu', createLeftContext);
	leftFileStore.reload();
	
}

function createRightFileGrid() {
	
	createRightToolbar();
	
	rightFileGrid = new Ext.grid.GridPanel(
	{    
		id : 'rightPanel',
		store: rightFileStore,
		colModel: rightColumnModel,
		autoScroll: true,
		sm: rightSelectionModel,
		// height: 400,
		//width: 1280,
		stripeRows: true,
		tbar : rightToolbar,
		bbar: new Ext.PagingToolbar({
			pageSize: 9999,
			store: rightFileStore,
			displayMsg: "{0} - {1} of {2} Records",
			displayInfo: true,
			emptyMsg: "No data to display"
		})
	});
	
	rightFileGrid.on('rowdblclick', function(grid,index,object){		
		var record = rightFileStore.getAt(index);		
		selectFile(record,'right');
		
	});
	rightFileGrid.on('rowcontextmenu', createRightContext);
	
	rightFileStore.reload();
	
}

function selectFile(_record,direction) {

	if (_record.data.type == 'Directory') {

		var path = _record.data.path;
        var region;
		
		if (direction == 'left') {
			leftFileStore.proxy.conn.url = "ListDirectories.do?do=enter&dir="+path+"&getJSON=true"
			doRefresh(direction);
			region = Ext.getCmp('center');
			region.setTitle('Verzeichnisanzeige : '+path);	
			leftPath = path;
		}	
		else if(direction == 'right') {		
			rightFileStore.proxy.conn.url = "ListDirectories.do?do=enter&dir="+path+"&getJSON=true"
			rightFileStore.reload();
			rightFileGrid.reconfigure(rightFileStore,rightColumnModel);
			region = Ext.getCmp('east');
			region.setTitle('Verzeichnisanzeige : '+path);
			rightPath = path;
		}	
		
	}	

}

function copySelected(_record,destination) {

	var fileLocation = _record.data.absolutePath;
	
	if (_record.data.type == 'File') {
		copyFile(fileLocation,destination,_record.data.name);
	}	
	
}

/*
var key = new Ext.KeyMap(document, [
	{
	
        key: Ext.EventObject.ENTER,
        
        fn: function(){

            var record;

    		if (direction == 'left') {
	    		record = leftSelectionModel.getSelected();        		
	    		
	    		if (record.data.caption == '<b>..</b>')
	    			leftIndicatorPos = leftOldPos;
	    		else {
	        		leftOldPos = leftFileGrid.getStore().indexOf(record);
	    			leftIndicatorPos = 0;
	    		}	
	    			
	    		selectFile(record,'left');
    		}
    		else {
	    		record = rightSelectionModel.getSelected();        		
	    		
	    		if (record.data.caption == '<b>..</b>')
	    			rightIndicatorPos = rightOldPos;
	    		else {
	        		rightOldPos = rightFileGrid.getStore().indexOf(record);
	    			rightIndicatorPos = 0;
	    		}	
	    			
	    		selectFile(record,'right');

    		}
    	}
    }, 
    {
        key: Ext.EventObject.DOWN,
        fn: function() {
    	}
    }, 
    {
        key: Ext.EventObject.SPACE,
        fn: function() {

            var record;

    		if (direction == 'left') {    			
    			record = leftSelectionModel.getSelected();
    			leftIndicatorPos = leftFileGrid.getStore().indexOf(record);
    			direction = 'right';
    			leftSelectionModel.clearSelections();
    			Ext.getCmp('rightPanel').focus();    			
   				rightSelectionModel.selectRow(rightIndicatorPos);
    			rightFileGrid.getView().focusRow(rightIndicatorPos);	    			
    		}
    		else {
    			record = rightSelectionModel.getSelected();
    			rightIndicatorPos = rightFileGrid.getStore().indexOf(record);
    			direction = 'left';    			
    			rightSelectionModel.clearSelections();
    			Ext.getCmp('leftPanel').focus();
    			leftSelectionModel.selectRow(leftIndicatorPos);
    			leftFileGrid.getView().focusRow(leftIndicatorPos);
    			
    		}
    	}
    }, 
    
]);	
*/


function checkDelete(answer) {
	
	if (answer =='yes') {
		
		var filename;
		var record;
		
		if (direction == 'left') {
			path = leftPath;
			record = leftSelectionModel.getSelected();
		}
		else {
			path = rightPath;
			record = rightSelectionModel.getSelected();
		}
		
		if (path == "/")
			filename = path+record.data.name;
		else
			filename = path + "/" + record.data.name;
			
		deleteFile(filename);	
		
		if (direction == 'left')			
			setTimeout("doRefresh('left')",500);
		else
			setTimeout("doRefresh('right')",500);

	}	
	
}

function doDeleteFile(direction,record){

	if (record.data.type == 'Directory') {

		Ext.MessageBox.show({
			title:'Verzeichnis wirklich l&ouml;schen?',
			msg: 'Wenn sie fortfahren, wird das Verzeichnis endg&uuml;ltig entfernt. Sind sie sicher das sie das wollen?',
			buttons: Ext.MessageBox.YESNOCANCEL,
			fn: checkDelete,
			animEl: 'mb4',
			icon: Ext.MessageBox.QUESTION
		});

		/*
		Ext.MessageBox.show({
		       title:'Information',
		       msg: 'Verzeichnisse k&ouml;nnen nicht gel&ouml;scht werden.',
		       buttons: Ext.MessageBox.OK,
		       icon: Ext.MessageBox.WARNING
		});
		*/
	}
	else {

		Ext.MessageBox.show({
			title:'Datei wirklich l&ouml;schen?',
			msg: 'Wenn sie fortfahren, wird die Datei endg&uuml;ltig entfernt. Sind sie sicher das sie das wollen?',
			buttons: Ext.MessageBox.YESNOCANCEL,
			fn: checkDelete,
			animEl: 'mb4',
			icon: Ext.MessageBox.QUESTION
		});
		
	}

}

var ctxMenuLeft = new Ext.menu.Menu({

    id:'leftCtx',

    items: [
        {
            id:'refresh',
            handler:reloadLeft,            
            text:'Aktualisieren',  
            iconCls : 'refresh-icon'
        },
        {
            id:'delete',
            handler:deleteLeft,            
            text:'L&ouml;schen',    
            iconCls : 'delete-icon'
        },
        {
            id:'rename',
            handler:renameLeft,            
            text:'Umbenennen',
            iconCls : 'edit-icon'
        },        
        {
            id:'download',
            handler:downloadFile,            
            text:'Download'            
        }, 
        {
            id:'edit',
            handler:editFile,            
            text:'Edit'            
        }    
        
    ]    
    
});

function createLeftContext(grid,row,e){
	e.preventDefault();
	leftSelectionModel.selectRow(row);
	ctxMenuLeft.showAt(e.getXY());
}
   
function reloadLeft() {
	leftFileStore.reload();
}

function deleteLeft() {
	var	record = leftSelectionModel.getSelected();
	doDeleteFile('left',record);
}

function downloadFile()  {
	
	var filename;
	var record;
	
	if (direction == 'left') {
		path = leftPath;
		record = leftSelectionModel.getSelected();
	}
	else {
		path = rightPath;
		record = rightSelectionModel.getSelected();
	}
	
	if (record.data.type == 'File') {

		if (path == "/")
			filename = path+record.data.name;
		else
			filename = path + "/" + record.data.name;

		
		var downloadpath = downloadActionPath + '?filePath=' + filename;
		
		document.location.href = downloadpath;
	}
	
}

function renameLeft() {
	showRenameFileDialog(leftSelectionModel.getSelected(),leftPath,'left');
}

var ctxMenuRight = new Ext.menu.Menu({

    id:'rightCtx',

    items: [
        {
            id:'refresh_r',
            handler:reloadRight,            
            text:'Aktualisieren',         
            iconCls : 'refresh-icon'            
        },
        {
            id:'delete_r',
            handler:deleteRight,            
            text:'L&ouml;schen',    
            iconCls : 'delete-icon'            
        },
        {
            id:'rename_r',
            handler:renameRight,            
            text:'Umbenennen',     
            iconCls : 'edit-icon'            
        },        
        {
            id:'download',
            handler:downloadFile,            
            text:'Download'
        },
        {
            id:'edit',
            handler:editFile,            
            text:'Edit'            
        }    
        
    ]    
    
});

function createRightContext(grid,row,e){
	e.preventDefault();
	rightSelectionModel.selectRow(row);
	ctxMenuRight.showAt(e.getXY());
}
   
function reloadRight() {
	rightFileStore.reload();
}

function deleteRight() {	
	doDeleteFile('right',rightSelectionModel.getSelected());
}

function renameRight() {
	showRenameFileDialog(rightSelectionModel.getSelected(),rightPath,'right');
}

function createFileForm(){

	fileForm = new Ext.FormPanel({
		
		labelWidth: 90, 
		url: appPath + "admin/ShowFile.do",
		frame: true,
		hideLabels : true,
		title: 'Details',
		bodyStyle: 'padding:5px 5px 0',
		items: [
			{ xtype: 'textarea',  id : '_content'  , name : 'content' , plugins: [editArea] },
			{ xtype: 'textfield', id : '_filename' , name : 'filename', hidden : true }
		],
        		
		buttons: [{
			text: 'Save',
			handler : function() {
			
				var contentField = Ext.getCmp('_content');
				var filenameField = Ext.getCmp('_filename');
			
				var updateObj = new File({
					content  : editAreaLoader.getValue('_content'),
					filename : filenameField.getValue()					
				});
			
				fileForm.getForm().submit({
					url: appPath + "admin/ShowFile.do?do=saveFile",
					method : 'POST',
					params : updateObj.data,
					waitMsg : saveWaitKey,
					waitTitle : saveWaitKey ,
					failure: function(fileForm, action) {
						Ext.MessageBox.alert('Error',action.result.message);	
					},
					success: function(fileForm, action){																	
					}
				}); 			
			}
		}, {
			text: 'Exit',
            handler: function(){				
				editWindow.hide();
            }			
		}]
	});
}



function editFile() {
	
	var filename;
	var record;
	
	if (direction == 'left') {
		path = leftPath;
		record = leftSelectionModel.getSelected();
	}
	else {
		path = rightPath;
		record = rightSelectionModel.getSelected();
	}
	
	if (record.data.type == 'File') {

		if (path == "/")
			filename = path+record.data.name;
		else
			filename = path + "/" + record.data.name;
		
	}
	
	if(!fileForm)
		createFileForm();
	
	if(!editWindow){
		editWindow = new Ext.Window({
            el:'editWindow',
            layout:'fit',
            closeAction:'hide',
            items : fileForm,
            plain: true,            
            height : screenHeight - 100,
            width  : screenWidth  - 50
        });
    }

	
	
	fileForm.load({
		url:'ShowFile.do?do=editFile&filename='+filename,
		method : 'GET',
		waitMsg: messageLoadingKey,
	    success: function(form, action) {		
			var content = Ext.getCmp('_content');		
			editAreaLoader.setValue('_content',content.getValue());		
		}
		
	});	
	
	editWindow.show(this);
}

