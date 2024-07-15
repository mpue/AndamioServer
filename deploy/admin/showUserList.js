var primaryKey = "id";
var userGridForm;
var userGrid; 
var userToolbar;
var win;
var confirmWin;
var userEditWindow;
var userForm;
var userEditForm;
var userAttributesForm;
var userAttributesWindow;
var ctxMenu;

/* This is the definition of our user object */

userObj = Ext.data.Record.create([

	{ name: primaryKey , type: "int" },
	{ name: "joinDate", type: "string" },
	{ name: "position", type: "int" },
	{ name: "title", type: "string" },
	{ name: "name", type: "string" },
	{ name: "firstName", type: "string" },
	{ name: "lastName", type: "string" },
	{ name: "land", type: "string" },
	{ name: "ort", type: "string" },
	{ name: "active", type: "boolean" },
	{ name: "telefon", type: "string" },
	{ name: "password", type: "string" },
	{ name: "email", type: "string" },
	{ name: "ranking", type: "int" }

]);

/* This is the definition of the usergroup object */

var groupObj = Ext.data.Record.create([

	{ name: "description", type: "string" },
    { name: primaryKey , type: "int" },
	{ name: "name", type: "string" }

]);

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


/* This is the data store definition for the user object */ 

var dataStore = new Ext.data.Store({
  url: "ListUsers.do?do=getJSON",
  remoteSort: false,
  reader: new Ext.data.JsonReader({
    root: "results", 
    totalProperty: "total",
    id: primaryKey },
    userObj
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
  { header: "id", width: 30, sortable: true, dataIndex: "id" },
  { header: "Username", width: 140, sortable: true, dataIndex: "name" ,editor: new Ext.form.TextField({allowBlank: false}) },
  { header: "Title", width: 140, sortable: true, dataIndex: "title" ,editor: new Ext.form.TextField({allowBlank: false})},
  { header: "eMail", width: 140, sortable: true, dataIndex: "email" ,editor: new Ext.form.TextField({allowBlank: false})},
  { header: "Country", width: 140, sortable: true, dataIndex: "land" ,editor: new Ext.form.TextField({allowBlank: false})},
  { header: "City", width: 140, sortable: true, dataIndex: "ort" ,editor: new Ext.form.TextField({allowBlank: false})},
  { header: "Telephone", width: 140, sortable: true, dataIndex: "telefon" ,editor: new Ext.form.TextField({allowBlank: false})},
  { header: "Active", width: 50, sortable: true, dataIndex: "active",renderer: check },
]);

var selectionModel = new Ext.grid.RowSelectionModel(
	{ 
		singleSelect: true 
	}
)
/**
 * The selection model of the usergroup data store
 */
var groupSelectionModel = new Ext.grid.RowSelectionModel(
	{
		singleSelect: false
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

function getGroupStore(record) {

	var groupStore = new Ext.data.Store({
		url: "ShowUser.do?do=getGroupsAsJSON&user_id="+record.data.id,
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

function removeGroup(user_id,group_id) {
	var poststr = "user_id=" + user_id + "&usergroup_id="+group_id;
	makePOSTRequest('ShowUser.do?do=removeGroup&async=true&', poststr);
}

function addGroup(user_id,group_id) {
	var poststr = "user_id=" + user_id + "&usergroup_id="+group_id;
	makePOSTRequest('ShowUser.do?do=addGroup&async=true&', poststr);
}
 
function check(active){
	
	if (active)
		return '<input type="checkbox" name="active" checked="checked">';
	else
		return '<input type="checkbox" name="active">';
} 

function getAnswer(answer) {
	if (answer == 'yes' ) {
		removeSelected();
	}
}

function removeSelected() {
	
	selectedRow = userGrid.getSelectionModel().getSelected();
	
	if (selectedRow) {
		userGridForm.submit({
			waitMsg: 'L&ouml;sche Benutzer...',
			url: 'ShowUser.do?do=delete&async=true&user_id=' + selectedRow.data.id,
			success: function(form, action){
				dataStore.remove(selectedRow);
			},
			failure: function(form, action){
				Ext.MessageBox.show({
			       title:'Fehler.',
			       msg: 'Benutzer konnte nicht gel&ouml;scht werden.',
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

	userToolbar = new Ext.Toolbar([
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
		text: 'Neuer Benutzer',
		id : 'createButton',
		handler: createUserDialog,
		iconCls : 'adduser-button',
		icon : 'themes/experience/icons/small/user_add.png'			
		
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
	
	userGridForm.submit({
		waitMsg: '&Auml;nderungen werden gespeichert, bitte warten ...',
		url: 'ShowUser.do?do=JSONUpdate',
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

/* this function creates an initializes the user grid */

function createUserGrid() {

	/* get form from html page */

	userGridForm = new Ext.BasicForm(
		Ext.get("usergridupdate"), 
		{
		}
	); 
	
	createToolBar();

	
	/* Reload the data store just in case there have been made changes */
	
	dataStore.reload();

	/* define the grid */

	userGrid = new Ext.grid.EditorGridPanel(
	{    
		store: dataStore,
		colModel: columnModel,
		autoScroll: true,
		sm: selectionModel,
		// height: 400,
		//width: 1280,
		stripeRows: true,
		clicksToEdit:2,
		closable: true,
		tbar : userToolbar,
		bbar: new Ext.PagingToolbar({
			pageSize: 100,
			store: dataStore,
			displayMsg: "{0} - {1} of {2} Records",
			displayInfo: true,
			emptyMsg: "Keine Benutzer gefunden"
		})
	});

    userGrid.on('rowcontextmenu', createContext);
	
}

function createContextMenu() {

    ctxMenu = new Ext.menu.Menu({

        id:'userCtx',

        items: [
            {
                id:'refresh',
                handler:reloadGrid,
                text:'Aktualisieren',
                iconCls : 'refresh-icon'
            },
            {
                id:'delete',
                handler:deleteUser,
                text:'L&ouml;schen',
                iconCls : 'delete-icon'
            },
            {
                id:'edit',
                handler:editUser,
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
                id:'editAttributes',
                handler:editAttributes,
                text:'Attribute'
            }
            

        ]
    });
}

function createUserDialog() {
	
	if (!userForm)
		createUserForm();	
	
	if(!win){
    	win = new Ext.Window({
            el:'userWindow',
            layout:'fit',

			animateTarget : 'createButton',
            closeAction:'hide',
            plain: true,
            items: userForm,
            height : 320,
            width : 480
        });
    }
	
	userForm.focus();
	
    win.show(this);	
}

function createDeleteConfirmDialog() {
	
	Ext.MessageBox.show({
       title:'Wirklich l&ouml;schen?',
       msg: 'Wollen Sie den Benutzer wirklich l&ouml;schen??',
       buttons: Ext.MessageBox.YESNOCANCEL,
       fn: getAnswer,
       animEl: 'mb4',
       icon: Ext.MessageBox.QUESTION
   });
}

function createUserForm(){
	
	userForm = new Ext.FormPanel({
		labelWidth: 90, // label settings here cascade unless overridden
		url: 'CreateUser.do',
		frame: true,
		title: 'Benutzerdetails',
		bodyStyle: 'padding:5px 5px 0',
		items: [
			{ xtype: 'textfield', name: 'name', fieldLabel: 'Benutzername',width : 300 ,anchor : '95%' },
			{ xtype: 'textfield', name: 'firstName', fieldLabel: 'Vorname',width : 300 ,anchor : '95%' },
			{ xtype: 'textfield', name: 'lastName', fieldLabel: 'Nachname',width : 300 ,anchor : '95%' },
			{ xtype: 'textfield', inputType : 'password', name: 'password', fieldLabel: 'Passwort',width : 300 ,anchor : '95%'},
			{ xtype: 'textfield', name: 'title', fieldLabel: 'Titel' ,width : 300,anchor : '95%'},
			{ xtype: 'textfield', name: 'email', fieldLabel: 'eMail' ,width : 300,anchor : '95%'},			
			{ xtype: 'textfield', name: 'land', fieldLabel: 'Land' ,width : 300,anchor : '95%'},			
			{ xtype: 'textfield', name: 'ort', fieldLabel: 'Ort' ,width : 300,anchor : '95%'},			
			{ xtype: 'textfield', name: 'telefon', fieldLabel: 'Telefon' ,width : 300,anchor : '95%'},			
		],
        		
		buttons: [{
			text: 'Save',
			handler : function() {
				userForm.getForm().submit({
					url:'CreateUser.do?async=true',
					method:'POST',
					waitMsg:'Speichere, bitte warten ...',
					waitTitle:'Saving user',
					failure: function(userForm, action) {
						Ext.MessageBox.alert('Konnte Benutzer nicht speichern.', "err");
					},
					success: function(userForm, action){
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

function reloadGrid(){
    dataStore.reload();
}

function deleteUser() {
    createDeleteConfirmDialog();
}

function editGroups(){
    var record = userGrid.getSelectionModel().getSelected();
    createGroupEditDialog(record);
}

function editUser() {

    createUserEditDialog();

    var record = userGrid.getSelectionModel().getSelected();

    userEditForm.load({
        url:'ShowUser.do?do=getUserAsJson&user_id='+record.data.id,
        method : 'GET',
        waitMsg:'Loading...'
    });

}

function createContext(grid, rowIndex, event){
    event.preventDefault();
    grid.getSelectionModel().selectRow(rowIndex);
    ctxMenu.showAt(event.getXY());
}


function createUserEditForm(){
	
	userEditForm = new Ext.FormPanel({
		labelWidth: 90, // label settings here cascade unless overridden
		url: 'ShowUser.do',
		frame: true,
		title: 'Benutzerdetails',
		bodyStyle: 'padding:5px 5px 0',
		items: [
            { xtype: 'textfield', name: 'id', fieldLabel: 'Id',width : 300 ,anchor : '95%',readOnly : true },
			{ xtype: 'textfield', name: 'name', fieldLabel: 'Benutzername',width : 300 ,anchor : '95%' },
			{ xtype: 'textfield', name: 'firstName', fieldLabel: 'Vorname',width : 300 ,anchor : '95%' },
			{ xtype: 'textfield', name: 'lastName', fieldLabel: 'Nachname',width : 300 ,anchor : '95%' },
			{ xtype: 'textfield', inputType : 'password', name: 'password', fieldLabel: 'Passwort',width : 300 ,anchor : '95%'},
			{ xtype: 'textfield', name: 'title', fieldLabel: 'Titel' ,width : 300,anchor : '95%'},
			{ xtype: 'textfield', name: 'email', fieldLabel: 'eMail' ,width : 300,anchor : '95%'},			
			{ xtype: 'textfield', name: 'land', fieldLabel: 'Land' ,width : 300,anchor : '95%'},			
			{ xtype: 'textfield', name: 'ort', fieldLabel: 'Ort' ,width : 300,anchor : '95%'},			
			{ xtype: 'textfield', name: 'telefon', fieldLabel: 'Telefon' ,width : 300,anchor : '95%'},
            { xtype: 'checkbox', name: 'active', fieldLabel: 'Aktiv' ,width : 100,anchor : '95%'},
		],
        		
		buttons: [{
			text: 'Save',
			handler : function() {
				userEditForm.getForm().submit({
					url:'ShowUser.do?do=save&async=true',
					method:'POST',
					waitMsg:'Speichere, bitte warten ...',
					waitTitle:'Saving user',
					failure: function(userEditForm, action) {
						Ext.MessageBox.alert('Konnte Benutzer nicht speichern.', "err");
					},
					success: function(userEditForm, action){
						dataStore.reload();
						userEditWindow.hide();
					}
				}); 			
			}
		}, {
			text: 'Cancel',
            handler: function(){

                userEditWindow.hide();
            }			
		}]
	});
}

function editAttributes() {

	createUserAttributesDialog();

}


function createUserAttributesForm(){
	
	userAttributesForm = new Ext.FormPanel({
		labelWidth: 90, // label settings here cascade unless overridden
		url: 'ShowUser.do',
		frame: true,
		title: 'Attribute',
		bodyStyle: 'padding:5px 5px 0',
        		
		buttons: [{
			text: 'Save',
			handler : function() {
			userAttributesForm.getForm().submit({
					url:'ShowUser.do?do=save&async=true',
					method:'POST',
					waitMsg:'Speichere, bitte warten ...',
					waitTitle:'Saving user',
					failure: function(userAttributesForm, action) {

					},
					success: function(userAttributesForm, action){

					}
				}); 			
			}
		}, {
			text: 'Cancel',
            handler: function(){

				userAttributesWindow.hide();
            }			
		}]
	});
}


function createUserEditDialog() {

	if (!userEditForm)
		createUserEditForm();

	if(!userEditWindow){
    	userEditWindow = new Ext.Window({
            el:'userEditWindow',
            layout:'fit',
            closeAction:'hide',
            plain: true,
            items: userEditForm,
            height : 400,
            width : 550
        });

    }

	userEditForm.focus();
    userEditWindow.show(this);

}

function createUserAttributesDialog() {

	if (!userAttributesForm)
		createUserAttributesForm();

	if(!userAttributesWindow){
		
		userAttributesWindow = new Ext.Window({
            el:'userAttributesWindow',
            layout:'fit',
            closeAction:'hide',
            plain: true,
            items: userAttributesForm,
            height : 400,
            width : 550
        });

    }

	userAttributesForm.focus();
	userAttributesWindow.show(this);

}
   
