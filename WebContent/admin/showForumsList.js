var primaryKey = "id";
var forumsGridForm;
var forumsGrid; 
var forumsToolbar;
var win;
var confirmWin;
var forumEditWindow;
var forumForm;
var forumEditForm;
var ctxMenu;
var currentCategoryWindow;

//This is the definition of our category object
var categoryObj = Ext.data.Record.create([
	{ name: primaryKey , type: "int" },
	{ name: "name", type: "string" },
	{ name: "description", type: "int" },
	{ name: "position", type: "int" }
]);

// This is the definition of our forum object 
var forumsObj = Ext.data.Record.create([
	{ name: primaryKey , type: "int" },
	{ name: "name", type: "string" },
	{ name: "description", type: "int" },
	{ name: "status", type: "int" }
]);

// This is the definition of the usergroup object 
var groupObj = Ext.data.Record.create([

	{ name: "description", type: "string" },
    { name: primaryKey , type: "int" },
	{ name: "name", type: "string" }

]);

// This data store holds all available usergroup objects
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

// This is the data store definition for the forum object  
var dataStore = new Ext.data.Store({
  url: "ForumShowAction.do?do=getJSON",
  remoteSort: false,
  reader: new Ext.data.JsonReader({
    root: "results", 
    totalProperty: "total",
    id: primaryKey },
    forumsObj
  ),
  sortInfo:{field:'position', direction:'ASC'}
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
  { header: "id", width: 30, sortable: true, dataIndex: "id" },
  { header: "name", width: 140, sortable: true, dataIndex: "name" ,editor: new Ext.form.TextField({allowBlank: false}) },
  { header: "description", width: 140, sortable: true, dataIndex: "description" ,editor: new Ext.form.TextField({allowBlank: false})},
  { header: "status", width: 140, sortable: true, dataIndex: "status" ,editor: new Ext.form.TextField({allowBlank: false})},
]);

//selection model for the forum grid
var forumSelectionModel = new Ext.grid.RowSelectionModel(
	{ 
		singleSelect: true 
	}
)

// selection model for the usergroup grid
var groupSelectionModel = new Ext.grid.RowSelectionModel(
	{ 
		singleSelect: true 
	}
)
// selection model for the category grid
var categorySelectionModel = new Ext.grid.RowSelectionModel(
	{ 
		singleSelect: true 
	}
)

/**
 * This is the column model definition for the editable grid of the groups
 */
var groupColumnModel = new Ext.grid.ColumnModel([
	{ header: "id", width: 30, sortable: true, dataIndex: "id" },
	{ header: "Name", width: 140, sortable: true, dataIndex: "name" },
	{ header: "Beschreibung", width: 140, sortable: true, dataIndex: "description" } ,
]);

/**
 * This is the column model definition for the editable grid of the categories
 */
var categoryColumnModel = new Ext.grid.ColumnModel([
	{ header: "id", width: 30, sortable: true, dataIndex: "id" },
	{ header: "Name", width: 140, sortable: true, dataIndex: "name" },
	{ header: "Beschreibung", width: 140, sortable: true, dataIndex: "description" } ,
	{ header: "Position", width: 40, sortable: true, dataIndex: "position" } ,
]);

function getGroupStore(record) {

	var groupStore = new Ext.data.Store({
		url: "ForumShowAction.do?do=getGroupsAsJSON&forum_id="+record.data.id,
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

function getCategoryStore(record) {

	var categoryStore = new Ext.data.Store({
		url: "ForumShowAction.do?do=getCategories&forum_id="+record.data.id,
		remoteSort: false,
		reader: new Ext.data.JsonReader({
			root: "rows",
			id: primaryKey },
			categoryObj
		),
		sortInfo:{field:'name', direction:'ASC'}
	});

	return categoryStore;
}

/**
 * Creates a new dialog in order to edit forum categories
 *
 * @param record
 * @return
 */

function createCategoryEditDialog(record) {

	var categoryStore = getCategoryStore(record);
	categoryStore.load();

	var toolbar = new Ext.Toolbar([
	    {
			text: 'Kategorie hinzuf&uuml;gen',
			id : 'addCategory',
			handler : function() {
				createCategoryDialog(categoryStore,record.data.id);
	    	}
		},
	    {
			text: 'Kategorie(n) entfernen',
			id : 'removeCategory',
			handler : function() {
				var selectedRow = categorySelectionModel.getSelected();
	    		removeCategory(record.data.id,selectedRow.data.id,categoryStore,selectedRow);	    		
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
			store: dataStore,
			displayMsg: "{0} - {1} of {2} Records",
			displayInfo: true,
			emptyMsg: "No data to display"
		})
	});

	var categoryEditWindow = new Ext.Window({
        layout:'fit',
        closeAction:'hide',
        title : 'Kategorien : Forum '+record.data.id,
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

/**
 * Creates a new dialog in order to edit group associations
 *
 * @param record
 * @return
 */

function createGroupEditDialog(record) {

	var groupStore = getGroupStore(record);
	groupStore.load();

	var toolbar = new Ext.Toolbar([
	    {
			text: 'Gruppe hinzuf&uuml;gen',
			id : 'addGroup',
			handler : function() {
	    		createGroupSelectionDialog(groupStore,record);
	    	}
		},
	    {
			text: 'Gruppe(n) entfernen',
			id : 'removeGroup',
			handler : function() {
				var selectedRow = groupSelectionModel.getSelected();
	    		removeGroup(record.data.id,selectedRow.data.id);
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
			displayMsg: "{0} - {1} of {2} Records",
			displayInfo: true,
			emptyMsg: "No data to display"
		})
	});

	var groupEditWindow = new Ext.Window({
        layout:'fit',
        closeAction:'hide',
        title : 'Gruppenberechtigungen',
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
* @param record
*
* @return
*/

function createGroupSelectionDialog(groupStore, record) {

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
		title : 'Verf&uuml;gbare Gruppen',
		plain: true,
		items: groupSelectGrid,
		buttons: [{
			text: 'Ok',
			handler : function() {
				for (i = 0;i < groupSelectionModel.getCount();i++) {
					var rec = groupSelectionModel.getSelections()[i];
                    addGroup(record.data.id,rec.data.id);
				}
				groupStore.add(groupSelectionModel.getSelections());
				// groupStore.reload();
				groupSelectionWindow.hide();
       	   	}
        },
		{
		    text: 'Cancel',
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

/**
 * removes a group from a forum
 * 
 * @param forum_id the id of the forum to remove the group from
 * @param group_id the id of the group being removed
 */
function removeGroup(forum_id,group_id) {
	var poststr = "forum_id=" + forum_id + "&usergroup_id="+group_id;
	makePOSTRequest('ForumShowAction.do?do=removeGroup&', poststr);
}
/**
 * Adds a group to a forum
 * 
 * @param forum_id the id of the forum to add the group to
 * @param group_id the id of the group to be added
 */
function addGroup(forum_id,group_id) {
	var poststr = "forum_id=" + forum_id + "&usergroup_id="+group_id;
	makePOSTRequest('ForumShowAction.do?do=addGroup&', poststr);
}
/**
 * Removes a category from a forum
 * 
 * @param forum_id
 * @param category_id
 */
function removeCategory(forum_id,category_id,categoryStore,selectedRow) {
	
	var url ="ForumShowAction.do?do=removeCategory";
	
	var oOptions = {
	    method: "post",
	    parameters: "forum_id="+forum_id+"&category_id="+category_id,
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

function removeSelected() {
	
	selectedRow = forumsGrid.getSelectionModel().getSelected();
	
	if (selectedRow) {
		forumsGridForm.submit({
			waitMsg: 'L&ouml;sche Forum...',
			url: 'ForumShowAction.do?do=delete&forum_id=' + selectedRow.data.id,
			success: function(form, action){
				dataStore.remove(selectedRow);
			},
			failure: function(form, action){
				Ext.MessageBox.show({
			       title:'Fehler.',
			       msg: 'Forum konnte nicht gel&ouml;scht werden.',
			       buttons: Ext.MessageBox.OK,
			       animEl: 'mb4',
			       icon: Ext.MessageBox.WARNING
			   });
			}
			
		});
	}
	
}
  
/* This function creates the toolbar of the user grid and defines the action handlers */  
  
function createToolBar(){

	forumsToolbar = new Ext.Toolbar([
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
		text: 'Neues Forum',
		id : 'createButton',
		handler: createForumDialog,
		iconCls : 'addforum-button',
		icon : 'themes/experience/icons/small/forum_add.png'			
		
	}, 
	{
		xtype: 'tbseparator'
	},	
	{
		text: 'Alles speichern',
		handler: storeAll,
		iconCls : 'save-button',
		icon : 'themes/experience/icons/small/disk.png'			
		
	}])
}

function storeAll() {
	/* From now on the jason array passed to the struts action is constructed */
	
	jsonData = "[";
	
	/* Iterate through all records and append the current to the json array */
	
	for (i = 0; i < dataStore.getCount(); i++) {
		record = dataStore.getAt(i);
		if (record.data.newrecord || record.dirty) {
			jsonData += Ext.util.JSON.encode(record.data) + ","
		}
	}
	
	/* remove trailing comma */
	
	jsonData = jsonData.substring(0, jsonData.length - 1) + "]";
	
	/* submit json data via form */
	
	forumsGridForm.submit({
		waitMsg: '&Auml;nderungen werden gespeichert, bitte warten ...',
		url: 'ForumShowAction.do?do=JSONUpdate',
		params: {
			data: jsonData
		},
		success: function(form, action){
			Ext.MessageBox.show({
		       title:'Gespeichert',
		       msg: 'Daten erfolgreich gespeichert.',
		       buttons: Ext.MessageBox.OK,
		       fn: getAnswer,
		       animEl: 'mb4',
		       icon: Ext.MessageBox.INFO
		   });
		},
		failure: function(form, action){
			Ext.MessageBox.show({
		       title:'Fehler',
		       msg: 'Daten konnten nicht gespeichert werden!',
		       buttons: Ext.MessageBox.OK,
		       fn: getAnswer,
		       animEl: 'mb4',
		       icon: Ext.MessageBox.WARNING
		   });
		}
	});
	
	/* finally commit data, to make the store reflect the current data */
	
	dataStore.commitChanges();	
}

/* this function creates and initializes the forums grid */

function createForumsGrid() {

	/* get form from html page */

	forumsGridForm = new Ext.BasicForm(
		Ext.get("forumgridupdate"), 
		{
		}
	); 
	
	createToolBar();
	
	/* Reload the data store just in case there have been made changes */
	
	dataStore.reload();

	/* define the grid */

	forumsGrid = new Ext.grid.EditorGridPanel(
	{    
		store: dataStore,
		colModel: columnModel,
		autoScroll: true,
		sm: forumSelectionModel,
		// height: 400,
		//width: 1280,
		stripeRows: true,
		clicksToEdit:2,
		closable: true,
		tbar : forumsToolbar,
		bbar: new Ext.PagingToolbar({
			pageSize: 100,
			store: dataStore,
			displayMsg: "{0} - {1} of {2} Records",
			displayInfo: true,
			emptyMsg: "Keine Foren gefunden"
		})
	});

	forumsGrid.on('rowcontextmenu', createContext);
	
}

function createContextMenu() {

    ctxMenu = new Ext.menu.Menu({

        id:'forumCtx',

        items: [
            {
                id:'refresh',
                handler:reloadGrid,
                text:'Aktualisieren',
                iconCls : 'refresh-icon'
            },
            {
                id:'delete',
                handler:deleteForum,
                text:'L&ouml;schen',
                iconCls : 'delete-icon'
            },
            {
                id:'edit',
                handler:editForum,
                text:'Bearbeiten',
                iconCls : 'edit-icon'
            },
            {
                id:'editGroups',
                handler:editGroups,
                text:'Gruppen',
                iconCls : 'usergroups-icon'
            },
            {
                id:'editCategories',
                handler:editCategories,
                text:'Kategorien'
            }
            

        ]
    });
}

function createContext(grid, rowIndex, event){
    event.preventDefault();
    grid.getSelectionModel().selectRow(rowIndex);
    ctxMenu.showAt(event.getXY());
}

function createForumDialog() {
	
	if (!forumForm)
		createForumForm();	
	
	if(!win){
    	win = new Ext.Window({
            el:'forumWindow',
            layout:'fit',
			animateTarget : 'createButton',
            closeAction:'hide',
            plain: true,
            items: forumForm,
            height : 320,
            width : 480
        });
    }
	
	forumForm.focus();
	
    win.show(this);	
}

function createCategoryDialog(categoryStore,forumId) {
	
	var categoryForm = createCategoryForm(categoryStore,forumId);	

	var categoryWin = new Ext.Window({        
        layout:'fit',
		animateTarget : 'createButton',
        closeAction:'hide',
        plain: true,
        items: categoryForm,
        height : 320,
        width : 480
    });
	
	currentCategoryWindow = categoryWin;
	
	categoryForm.focus();	
	categoryWin.show(this);	
}

function createDeleteConfirmDialog() {
	
	Ext.MessageBox.show({
       title:'Wirklich l&ouml;schen?',
       msg: 'Wollen Sie das Forum wirklich l&ouml;schen??',
       buttons: Ext.MessageBox.YESNOCANCEL,
       fn: getAnswer,
       animEl: 'mb4',
       icon: Ext.MessageBox.QUESTION
   });
}

function reloadGrid(){
    dataStore.reload();
}

function deleteForum() {
    createDeleteConfirmDialog();
}

function editGroups(){
    var record = forumsGrid.getSelectionModel().getSelected();
    createGroupEditDialog(record);
}

function editCategories() {
    var record = forumsGrid.getSelectionModel().getSelected();
    createCategoryEditDialog(record);
}

function getAnswer(answer) {
	if (answer == 'yes' ) {
		removeSelected();
	}
}

function createForumForm(){
	
	forumForm = new Ext.FormPanel({
		labelWidth: 90, // label settings here cascade unless overridden
		url: 'ForumShowAction.do',
		frame: true,
		title: 'Forumdetails',
		bodyStyle: 'padding:5px 5px 0',
		items: [
				{ xtype: 'textfield', name: 'name', fieldLabel: 'Name',width : 300 ,anchor : '95%' },
				{ xtype: 'textfield', name: 'description', fieldLabel: 'Beschreibung',width : 300 ,anchor : '95%' },
				{ xtype: 'textfield', name: 'status', fieldLabel: 'Status',width : 300 ,anchor : '95%' },
				{ xtype: 'textfield', name: 'position', fieldLabel: 'Position' ,width : 300,anchor : '95%'},		
		],
        		
		buttons: [{
			text: 'Save',
			handler : function() {
				forumForm.getForm().submit({
					url:'ForumShowAction.do?do=create',
					method:'POST',
					waitMsg:'Speichere, bitte warten ...',
					waitTitle:'Saving forum',
					failure: function(forumForm, action) {
						Ext.MessageBox.alert('Konnte Forum nicht speichern.', "err");
					},
					success: function(forumForm, action){
						dataStore.reload();
						win.hide();
					}
				}); 			
			}
		}, {
			text: 'Cancel',
            handler: function(){

                win.hide();
            }			
		}]
	});
}

function createCategoryForm(categoryStore,forumId){
	
	var categoryForm = new Ext.FormPanel({
		labelWidth: 90, 
		url: 'ForumShowAction.do',
		frame: true,
		title: 'Kategoriedetails',
		bodyStyle: 'padding:5px 5px 0',
		items: [
				{ xtype: 'textfield', name: 'name', fieldLabel: 'Name',width : 300 ,anchor : '95%' },
				{ xtype: 'textfield', name: 'description', fieldLabel: 'Beschreibung',width : 300 ,anchor : '95%' },				
				{ xtype: 'textfield', name: 'position', fieldLabel: 'Position' ,width : 300,anchor : '95%'},		
		],
        		
		buttons: [{
			text: 'Save',
			handler : function() {
				categoryForm.getForm().submit({
					url:'ForumShowAction.do?do=createCategory&forum_id='+forumId,
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

function editForum() {

	createForumEditDialog();

    var record = forumsGrid.getSelectionModel().getSelected();

    forumEditForm.load({
        url:'ForumShowAction.do?do=getForumAsJson&forum_id='+record.data.id,
        method : 'GET',
        waitMsg:'Loading...'
    });

}

function createForumEditForm(){
	
	forumEditForm = new Ext.FormPanel({
		labelWidth: 90, 
		url: 'ForumShowAction.do',
		frame: true,
		title: 'Forumdetails',
		bodyStyle: 'padding:5px 5px 0',
		items: [
            { xtype: 'textfield', name: 'id', fieldLabel: 'Id',width : 300 ,anchor : '95%',readOnly : true },
			{ xtype: 'textfield', name: 'name', fieldLabel: 'Name',width : 300 ,anchor : '95%' },
			{ xtype: 'textfield', name: 'description', fieldLabel: 'Beschreibung',width : 300 ,anchor : '95%' },
			{ xtype: 'textfield', name: 'status', fieldLabel: 'Status',width : 300 ,anchor : '95%' },
			{ xtype: 'textfield', name: 'position', fieldLabel: 'Position' ,width : 300,anchor : '95%'},
		],
        		
		buttons: [{
			text: 'Speichern',
			handler : function() {
				forumEditForm.getForm().submit({
					url:'ForumShowAction.do?do=save',
					method:'POST',
					waitMsg:'Speichere, bitte warten ...',
					waitTitle:'Saving forum',
					failure: function(forumEditForm, action) {
						Ext.MessageBox.alert('Konnte Forum nicht speichern.', "err");
					},
					success: function(forumEditForm, action){
						dataStore.reload();
						forumEditWindow.hide();
					}
				}); 			
			}
		}, {
			text: 'Abbrechen',
            handler: function(){

                forumEditWindow.hide();
            }			
		}]
	});
}

function createForumEditDialog() {

	if (!forumEditForm)
		createForumEditForm()

	if(!forumEditWindow){
		forumEditWindow = new Ext.Window({
            el:'forumEditWindow',
            layout:'fit',
            closeAction:'hide',
            plain: true,
            items: forumEditForm,
            height : 400,
            width : 550
        });

    }

	forumEditForm.focus();
	forumEditWindow.show(this);

}
   
