var primaryKey = "id";
var gridForm;
var grid; 
var toolbar;
var currentCategoryWindow;
var downloadEditForm;
var downloadEditWindow;
var ctxMenu;

//This is the definition of our category object
var categoryObj = Ext.data.Record.create([
	{ name: primaryKey , type: "int" },
	{ name: "name", type: "string" },
	{ name: "description", type: "int" },
	{ name: "position", type: "int" }
]);

/* This is the definition of our download object */

var downloadObj = Ext.data.Record.create([
	{ name: "name", type: "string" },
	{ name: "description", type: "string" },
	{ name: "path", type: "string" },
	{ name: "filename", type: "string" },
	{ name: "ranking", type: "float" },
	{ name: "hits", type: "long" },
	{ name: "downloads", type: "long" },
	{ name: "uploader", type: "string" },
	{ name: "author", type: "string" },
	{ name: "license", type: "string" },
	{ name: "uploadTime", type: "date" },
	{ name: "uploadTimeFormatted", type: "string" },
	{ name: "tags", type: "string" },
	{ name: "mimetype", type: "string" },
	{ name: "domain", type: "string" },
	{ name: "filesize", type: "long" },
	{ name: "publicfile", type: "boolean" },
	{ name: "downloadable", type: "boolean" }	
]);

var categoryColumnModel = new Ext.grid.ColumnModel([
	{ header: "id", width: 30, sortable: true, dataIndex: "id" },
	{ header: "Name", width: 140, sortable: true, dataIndex: "name" },
	{ header: "Beschreibung", width: 140, sortable: true, dataIndex: "description" }
]);

var categorySelectionModel = new Ext.grid.RowSelectionModel({
	singleSelect : true
});

/* This is the data store definition for the user object */ 
var dataStore = new Ext.data.Store({
  url: "DownloadAdminAction.do?do=list",
  remoteSort: false,
  reader: new Ext.data.JsonReader({
    root: "results", 
    totalProperty: "total",
    id: name },
    downloadObj
  ),
  sortInfo:{field:'name', direction:'ASC'}
});
 
//This data store holds all available usergroup objects
var categoryStore = new Ext.data.Store({
	  url: "DownloadAdminAction.do?do=getCategories",
	  remoteSort: false,
	  reader: new Ext.data.JsonReader({
	    root: "results",
	    totalProperty: "total",
	    id: primaryKey },
	    categoryObj
	  ),
	  sortInfo:{field:'name', direction:'ASC'}
});

/* Override the load method of the datastore and bind some parameters */ 
 
dataStore.load({ //include the params object for pagination;
  params: {
    start: 0,
    limit: 100
  }
});
 
/* This is the column model definition for the editable grid */

var columnModel = new Ext.grid.ColumnModel([
	{ header: "Id", width: 50, sortable: true, dataIndex: "id" },
	{ header: "Autor", width: 150, sortable: true, dataIndex: "author" },
	{ header: "Name", width: 250, sortable: true, dataIndex: "name" },
	{ header: "Mimetype", width: 100, sortable: true, dataIndex: "mimetype" },
	{ header: "Pfad", width: 100, sortable: true, dataIndex: "path" },
	{ header: "Dateiname", width: 240, sortable: true, dataIndex: "filename" },
	{ header: "Gr&ouml;sse", width: 100, sortable: true, dataIndex: "filesize" },
	{ header: "Uploader", width: 100, sortable: true, dataIndex: "uploader" },
	{ header: "&Ouml;ffentlich", width: 100, sortable: true, dataIndex: "publicfile", renderer : checkboxRenderer }, 
	{ header: "Download", width: 100, sortable: true, dataIndex: "downloadable", renderer : checkboxRenderer }
]);

var selectionModel = new Ext.grid.RowSelectionModel(
	{ 
		singleSelect: true 
	}
);

function checkboxRenderer(checked){
	
    if (checked)
        return '<img src="./themes/experience/icons/green.png" align="middle">';
    else
        return '<img src="./themes/experience/icons/red.png" align="middle">';
} 
  
/* This function creates the toolbar of the user grid and defines the action handlers */  
  
function createToolBar(){

	toolbar = new Ext.Toolbar([
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
			text: 'Kategorien bearbeiten',
			id : 'categoryButton',
			handler: editCategories
		},
		
	]);
	
}

function editCategories() {
	
	categoryStore.load();
	
	var toolbar = new Ext.Toolbar([
	    {
			text: 'Kategorie hinzuf&uuml;gen',
			id : 'addCategory',
			handler : addCategory
		},
	    {
			text: 'Kategorie(n) entfernen',
			id : 'deleteCategory',
			handler : function() {
				var selectedRow = categorySelectionModel.getSelected();
	    		deleteCategory(selectedRow.data.id,categoryStore,selectedRow);	    		
	    	}
		}

	]);

	var categoryGrid = new Ext.grid.EditorGridPanel(
	{
		store: categoryStore,
		colModel: categoryColumnModel,
		autoScroll: true,
		sm: categorySelectionModel,
		stripeRows: true,
		clicksToEdit:1,
		closable: true,
		tbar : toolbar,
		bbar: new Ext.PagingToolbar({
			pageSize: 100,
			store: categoryStore,
			displayMsg: "{0} - {1} of {2} Records",
			displayInfo: true,
			emptyMsg: "No data to display"
		})
	});

	var categoryEditWindow = new Ext.Window({
        layout:'fit',
        closeAction:'hide',
        title : 'Download Kategorien',
        plain: true,
        items: categoryGrid,
        height : 300,
        width : 500
    });

	categoryEditWindow.on('hide', function(win){
		categoryGrid.destroy();
		categoryEditWindow.destroy();
	});

	categoryEditWindow.show(this);
	categoryEditWindow.focus();

}

function addCategory() {
	createCategoryDialog(categoryStore);	
}

/* this function creates an initializes the user grid */

function createGrid() {

	/* get form from html page */

	gridForm = new Ext.BasicForm(
		Ext.get("gridupdate"), 
		{
		}
	); 
	
	createToolBar();

	
	/* Reload the data store just in case there have been made changes */
	
	dataStore.reload();

	/* define the grid */

	grid = new Ext.grid.EditorGridPanel(
	{    
		store: dataStore,
		colModel: columnModel,
		autoScroll: true,
		sm: selectionModel,
		// height: 400,
		//width: 1280,
		stripeRows: true,
		clicksToEdit:1,
		closable: true,
		tbar : toolbar,
		bbar: new Ext.PagingToolbar({
			pageSize: 100,
			store: dataStore,
			displayMsg: "{0} - {1} of {2} Records",
			displayInfo: true,
			emptyMsg: "No data to display"
		})
	});

	grid.on('rowcontextmenu', createContext);	
	grid.on('rowdblclick', function(eventGrid, rowIndex, e) {
		  editSelectedDownload(rowIndex);
	}, this);
}

function createContext(grid, rowIndex, event){
    event.preventDefault();
    grid.getSelectionModel().selectRow(rowIndex);
    ctxMenu.showAt(event.getXY());
}

function createCategoryDialog(categoryStore) {
	
	var categoryForm = createCategoryForm(categoryStore);	

	var categoryWin = new Ext.Window({        
        layout:'fit',
		animateTarget : 'createButton',
        closeAction:'hide',
        plain: true,
        items: categoryForm,
        height : 180,
        width : 480
    });
	
	currentCategoryWindow = categoryWin;
	
	categoryForm.focus();	
	categoryWin.show(this);	
}

function createCategoryForm(categoryStore){
	
	var categoryForm = new Ext.FormPanel({
		labelWidth: 90, 
		url: 'DownloadAdminAction.do',
		frame: true,
		title: 'Kategoriedetails',
		bodyStyle: 'padding:5px 5px 0',
		items: [
				{ xtype: 'textfield', name: 'name', fieldLabel: 'Name',width : 300 ,anchor : '95%' },
				{ xtype: 'textfield', name: 'description', fieldLabel: 'Beschreibung',width : 300 ,anchor : '95%' }		
		],
        		
		buttons: [{
			text: 'Save',
			handler : function() {
				categoryForm.getForm().submit({
					url:'DownloadAdminAction.do?do=createCategory',
					method:'POST',
					waitMsg:'Speichere, bitte warten ...',
					waitTitle:'Saving category',
					failure: function(categoryForm, action) {
						Ext.MessageBox.alert('Konnte Kategorie nicht speichern.', "err");
					},
					success: function(categoryForm, action){
						categoryStore.reload();
						currentCategoryWindow.hide();
					}
				}); 			
			}
		}, {
			text: 'Cancel',
            handler: function() {
            	currentCategoryWindow.hide();
            }			
		}]
	});
	
	return categoryForm;
}

function deleteCategory(category_id,categoryStore,selectedRow) {
	
	var url ="DownloadAdminAction.do?do=deleteCategory";
	
	var oOptions = {
	    method: "post",
	    parameters: "id="+category_id,
	    onSuccess: function (oXHR, oJson) {
	    	var response = oXHR.responseText.evalJSON();			
	    	if (response.success) {	    		
	    		categoryStore.remove(selectedRow);
	    	}
	    	else {
				Ext.MessageBox.show({
			       title:'Fehler.',
			       msg: response.message,
			       buttons: Ext.MessageBox.OK,
			       animEl: 'mb4',
			       icon: Ext.MessageBox.WARNING
				});	       	    		
	    	}	    	
	    	
	    },
	    onFailure: function (oXHR, oJson) {
			Ext.MessageBox.show({
		       title:'Fehler.',
		       msg: 'Kategorie konnte nicht gel&ouml;scht werden.',
		       buttons: Ext.MessageBox.OK,
		       animEl: 'mb4',
		       icon: Ext.MessageBox.WARNING
			});	       
	    }
	};
	
	var oRequest = new Ajax.Request(url, oOptions);
	
}

function createContextMenu() {

    ctxMenu = new Ext.menu.Menu({

        id:'downloadCtx',

        items: [
            {
                id:'refresh',
                handler:reloadGrid,
                text:'Aktualisieren',
                iconCls : 'refresh-icon'
            },
            {
                id:'delete',
                handler:deleteDownload,
                text:'L&ouml;schen',
                iconCls : 'delete-icon'
            },
            {
                id:'edit',
                handler:editDownload,
                text:'Bearbeiten',
                iconCls : 'edit-icon'
            },

        ]
    });
}

function createDownloadEditForm(){
	
	downloadEditForm = new Ext.FormPanel({
		labelWidth: 90, // label settings here cascade unless overridden
		url: 'DownloadAdminAction.do',
		frame: true,
		title: 'Download bearbeiten',
		bodyStyle: 'padding:5px 5px 0',
		items: [
            { xtype: 'textfield', name: 'id', fieldLabel: 'Id',width : 300 ,anchor : '95%',readOnly : true },
			{ xtype: 'textfield', name: 'name', fieldLabel: 'Name',width : 300 ,anchor : '95%' },
			{ xtype: 'textfield', name: 'description', fieldLabel: 'Beschreibung',width : 300 ,anchor : '95%' },
			{ xtype: 'textfield', name: 'path', fieldLabel: 'Pfad',width : 300 ,anchor : '95%' },
			{ xtype: 'textfield', name: 'filename', fieldLabel: 'Dateiname',width : 300 ,anchor : '95%' },
			{ xtype: 'textfield', name: 'ranking', fieldLabel: 'Ranking',width : 300 ,anchor : '95%' },
			{ xtype: 'textfield', name: 'hits', fieldLabel: 'Hits',width : 300 ,anchor : '95%' },
			{ xtype: 'textfield', name: 'downloads', fieldLabel: 'Downloads',width : 300 ,anchor : '95%' },
			{ xtype: 'textfield', name: 'uploader', fieldLabel: 'Uploader',width : 300 ,anchor : '95%' },
			{ xtype: 'textfield', name: 'author', fieldLabel: 'Autor',width : 300 ,anchor : '95%' },
			{ xtype: 'textfield', name: 'license', fieldLabel: 'Lizenz',width : 300 ,anchor : '95%' },
			{ xtype: 'textfield', name: 'uploadTimeFormatted', fieldLabel: 'Upload time',width : 300 ,anchor : '95%',readOnly : true },
			{ xtype: 'textfield', name: 'tags', fieldLabel: 'Tags',width : 300 ,anchor : '95%' },
			{ xtype: 'textfield', name: 'mimetype', fieldLabel: 'Mimetyp',width : 300 ,anchor : '95%' },
			{ xtype: 'textfield', name: 'domain', fieldLabel: 'Domain',width : 300 ,anchor : '95%' },
			{ xtype: 'textfield', name: 'filesize', fieldLabel: 'Gr&ouml;sse',width : 300 ,anchor : '95%' },
			{ xtype: 'checkbox', name: 'publicfile', fieldLabel: '&Ouml;ffentlich',width : 300 ,anchor : '95%' },
			{ xtype: 'checkbox', name: 'downloadable', fieldLabel: 'Download',width : 300 ,anchor : '95%' }			
		],
        		
		buttons: [{
			text: 'Save',
			handler : function() {
				downloadEditForm.getForm().submit({
					url:'DownloadAdminAction.do?do=save',
					method:'POST',
					waitMsg:'Speichere, bitte warten ...',
					waitTitle:'Speichere download',
					failure: function(downloadEditForm, action) {
						Ext.MessageBox.alert('Konnte Download nicht speichern.', "err");
					},
					success: function(downloadEditForm, action){
						dataStore.reload();
						downloadEditWindow.hide();
					}
				}); 			
			}
		}, {
			text: 'Cancel',
            handler: function(){
                downloadEditWindow.hide();
            }			
		}]
	});
}

function createDownloadEditDialog() {

	if (!downloadEditForm)
		createDownloadEditForm();

	if(!downloadEditWindow){
    	downloadEditWindow = new Ext.Window({
    		el : 'downloadEditWindow',
            layout:'fit',
            closable : true, 
            closeAction:'hide',
            plain: true,
            items: downloadEditForm,
            height : 570,
            width : 550
        });

    }

	downloadEditForm.focus();
    downloadEditWindow.show(this);

}

function editDownload() {

    createDownloadEditDialog();

    var record = grid.getSelectionModel().getSelected();

    downloadEditForm.load({
        url:'DownloadAdminAction.do?do=getDownloadAsJson&download_id='+record.data.id,
        method : 'GET',
        waitMsg:'Loading...'
    });

}

function editSelectedDownload(row) {

    createDownloadEditDialog();

    var record = dataStore.getAt(row);

    downloadEditForm.load({
        url:'DownloadAdminAction.do?do=getDownloadAsJson&download_id='+record.data.id,
        method : 'GET',
        waitMsg:'Loading...'
    });

}

function deleteDownload() {
	var record = grid.getSelectionModel().getSelected();
    gridForm.submit({
        waitMsg: 'L&ouml;sche Benutzer...',
        url: 'DownloadAdminAction.do?do=deleteDownload&id=' + record.data.id,
        success: function(form, action){
            dataStore.remove(grid.getSelectionModel().getSelected());
            // dataStore.reload();
        },
        failure: function(form, action){
            Ext.MessageBox.show({
               title:'Fehler.',
               msg: 'Download konnte nicht gel&ouml;scht werden.',
               buttons: Ext.MessageBox.OK,
               animEl: 'mb4',
               icon: Ext.MessageBox.WARNING
           });
        }
        
    });
}

function reloadGrid() {
	dataStore.reload();
}