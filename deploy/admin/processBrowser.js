var gridForm;
var processForm;
var grid; 
var toolbar;
var primaryKey = "id";
var win;
var ctxMenu;

/* This is the definition of our process object */

var processObj = Ext.data.Record.create([
    { name: primaryKey , type: "int" },
	{ name: "name", type: "string" },
	{ name: "icon", type: "string" },
	{ name: "target", type: "string" },
	{ name: "position", type: "integer" },
	{ name: "active", type: "boolean" }
]);
  
/* This is the data store definition for the user object */ 

var dataStore = new Ext.data.Store({
  url: "ListProcesses.do?do=getJSON",
  remoteSort: true,
  reader: new Ext.data.JsonReader({
    root: "results", 
    totalProperty: "total",
    id: primaryKey },
    processObj
  )
});

/* This is the definition of the usergroup object */

var groupObj = Ext.data.Record.create([

	{ name: "description", type: "string" },
    { name: primaryKey , type: "int" },
	{ name: "name", type: "string" }

]);
 
/* Override the load method of the datastore and bind some parameters */ 
 
dataStore.load({ //include the params object for pagination;
  params: {
    start: 0,
    limit: 100
  }
});
 
/* This is the column model definition for the editable grid */

var columnModel = new Ext.grid.ColumnModel([
  { header: "", width: 100, sortable: true, dataIndex: "icon", renderer : getImage,editor: new Ext.form.TextField({allowBlank: false}) },
  { header: "Id", width: 100, sortable: true, dataIndex: primaryKey },
  { header: "Name", width: 200, sortable: true, dataIndex: "name", editor: new Ext.form.TextField({allowBlank: false})},
  { header: "Ziel", width: 200, sortable: true, dataIndex: "target",editor: new Ext.form.TextField({allowBlank: false})},
  { header: "Aktiv", width: 40, sortable: true, dataIndex: "active", renderer : check },
]);

var selectionModel = new Ext.grid.RowSelectionModel(
	{ 
		singleSelect: true 
	}
)

function check(active){

    if (active)
        return '<img src="./themes/experience/icons/green.png" align="middle" onclick="toggleActive();">';
    else
        return '<img src="./themes/experience/icons/red.png" align="middle" onclick="toggleActive();">';
}

function getImage(value) {

	var href = "";

	href = '<img src="./themes/experience/icons/iconbar/' + value + '">';

	return href;

}

function toggleActive()  {
    
    var record = grid.getSelectionModel().getSelected();
   	var poststr = "process_id=" + record.data.id;
	makePOSTRequest('ShowProcess.do?do=switchActive&async=true&', poststr);
    setTimeout("dataStore.reload()",500);
}

function getAnswer(answer) {
	if (answer == 'yes' ) {
		removeSelected();
	}
}

function removeSelected() {

	selectedRow = grid.getSelectionModel().getSelected();

	if (selectedRow && selectedRow.data.id > 1) {
		gridForm.submit({
			waitMsg: 'L&ouml;sche Prozess...',
			url: 'ShowProcess.do?do=delete&async=true&process_id=' + selectedRow.data.id,
			success: function(form, action){
				dataStore.remove(selectedRow);
			},
			failure: function(form, action){
				Ext.MessageBox.show({
			       title:'Fehler.',
			       msg: 'Prozess konnte nicht gel&ouml;scht werden.',
			       buttons: Ext.MessageBox.OK,
			       fn: getAnswer,
			       animEl: 'mb4',
			       icon: Ext.MessageBox.WARNING
			   });
			}

		});
	}
    else
        Ext.MessageBox.alert('Dieser Prozess kann nicht gelöscht werden.');

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

	gridForm.submit({
		waitMsg: '&Auml;nderungen werden gespeichert, bitte warten ...',
		url: 'ShowProcess.do?do=JSONUpdate',
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
		xtype: 'tbseparator'
	},	
	{
		text: 'Neuer Prozess',
		id : 'createButton',
		handler: createProcessDialog,
		iconCls : 'addprocess-button',
		icon : 'themes/experience/icons/small/application_form_add.png'			
	}, 
	{
		xtype: 'tbseparator'
	},		
	{
		text: 'Alles speichern',
		handler: storeAll,
		iconCls : 'save-button',
		icon : 'themes/experience/icons/small/disk.png'			
	}
    ])
}

function createProcessDialog() {

	if (!processForm)
		createProcessForm();

	if(!win){
    	win = new Ext.Window({
            el:'processWindow',
            layout:'fit',
			animateTarget : 'createButton',
            closeAction:'hide',
            plain: true,
            items: processForm,
            height : 320,
            width : 480
        });
    }

	processForm.focus();

    win.show(this);
}

function createDeleteConfirmDialog() {

	Ext.MessageBox.show({
       title:'Wirklich l&ouml;schen?',
       msg: 'Wollen Sie den Prozess wirklich l&ouml;schen??',
       buttons: Ext.MessageBox.YESNOCANCEL,
       fn: getAnswer,
       animEl: 'mb4',
       icon: Ext.MessageBox.QUESTION
   });
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
		clicksToEdit:2,
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
}

function createProcessForm(){

	processForm = new Ext.FormPanel({
		labelWidth: 90, // label settings here cascade unless overridden
		url: 'CreateProcess.do',
		frame: true,
		title: 'Prozessdetails',
		bodyStyle: 'padding:5px 5px 0',
		items: [
			{ xtype: 'textfield', name: 'name', fieldLabel: 'Name',width : 300 ,anchor : '95%' },
			{ xtype: 'textfield', name: 'target', fieldLabel: 'Ziel' ,width : 300,anchor : '95%'},
			{ xtype: 'textfield', name: 'icon', fieldLabel: 'Icon' ,width : 300,anchor : '95%'},
			{ xtype: 'checkbox', name: 'active', fieldLabel: 'Aktiv' ,width : 300,anchor : '95%'}
		],

		buttons: [{
			text: 'Save',
			handler : function() {
				processForm.getForm().submit({
					url:'CreateProcess.do?async=true',
					method:'POST',
					waitMsg:'Speichere, bitte warten ...',
					waitTitle:'Speichere Prozess',
					failure: function(processForm, action) {
						Ext.MessageBox.alert('Konnte Prozess nicht speichern.', action.result.message);						
					},
					success: function(processForm, action){
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

function createContextMenu() {

    ctxMenu = new Ext.menu.Menu({

        id:'copyCtx',

        items: [
            {
                id:'refresh',
                handler:reloadGrid,
                text:'Aktualisieren',
                iconCls : 'refresh-icon'
            },
            {
                id:'delete',
                handler:deleteProcess,
                text:'L&ouml;schen',
                iconCls : 'delete-icon'
            },
            {
                id:'nodeup',
                handler:moveProcessUp,
                text:'Nach oben',
                iconCls : 'up-icon'
            },
            {
                id:'nodedown',
                handler:moveProcessDown,
                text:'Nach unten',
                iconCls : 'down-icon'
            },
            {
                id:'editGroups',
                handler:editGroups,
                text:'Gruppen',
                iconCls : 'usergroups-icon'
            }

        ]
    });
}

function createContext(grid, rowIndex, event){	
    event.preventDefault();
    selectionModel.selectRow(rowIndex);
    ctxMenu.showAt(event.getXY());
}

function reloadGrid() {
    dataStore.reload();
}

function deleteProcess() {
    createDeleteConfirmDialog();
}

function moveProcessUp() {
    var record = grid.getSelectionModel().getSelected();
	var poststr = "process_id=" + record.data.id;
 	makePOSTRequest('ShowProcess.do?do=elementUp&async=true&', poststr);
    setTimeout("dataStore.reload();",500);
}

function moveProcessDown() {
    var record = grid.getSelectionModel().getSelected();
	var poststr = "process_id=" + record.data.id;
 	makePOSTRequest('ShowProcess.do?do=elementDown&async=true&', poststr);
    setTimeout("dataStore.reload();",500);
}

function editGroups(){
    var record = grid.getSelectionModel().getSelected();
    createGroupEditDialog(record);
}

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
		url: "ShowProcess.do?do=getGroupsAsJSON&process_id="+record.data.id,
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

function removeGroup(process_id,group_id) {
	var poststr = "process_id=" + process_id + "&usergroup_id="+group_id;
	makePOSTRequest('ShowProcess.do?do=removeGroup&async=true&', poststr);
}

function addGroup(process_id,group_id) {
	var poststr = "process_id=" + process_id + "&usergroup_id="+group_id;
	makePOSTRequest('ShowProcess.do?do=addGroup&async=true&', poststr);
}
