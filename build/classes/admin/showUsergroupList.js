var primaryKey = "id";
var usergroupGridForm;
var usergroupGrid; 
var usergroupToolbar;
var win;
var confirmWin;
var usergroupForm;

/* This is the definition of the usergroup object */

usergroupObj = Ext.data.Record.create([

	{ name: "description", type: "string" },
    { name: primaryKey , type: "int" },
	{ name: "name", type: "string" }

]);
  
/* This is the data store definition for the usergroup object */ 

var dataStore = new Ext.data.Store({
  url: "ListUsergroups.do?do=getJSON",
  remoteSort: false,
  reader: new Ext.data.JsonReader({
    root: "results", 
    totalProperty: "total",
    id: primaryKey },
    usergroupObj
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
  { header: "Name", width: 140, sortable: true, dataIndex: "name"},
  { header: "Beschreibung", width: 140, sortable: true, dataIndex: "description" ,editor: new Ext.form.TextField({allowBlank: false})},  
  { header: "", width: 40, sortable: false, dataIndex: "id",renderer: remove, align : 'center' }
]);

var selectionModel = new Ext.grid.RowSelectionModel(
	{ 
		singleSelect: true 
	}
);

function remove(id) {
	if (id > 1)
		return '<img src="themes/experience/icons/delete.png" border="0" align="middle" height="16px" width="16px" onclick="createDeleteConfirmDialog();"/>';
	else return '';
}

function getAnswer(answer) {
	if (answer == 'yes' ) {
		removeSelected();
	}
}

function removeSelected() {
	
	selectedRow = usergroupGrid.getSelectionModel().getSelected();
	
	if (selectedRow) {
		usergroupGridForm.submit({
			waitMsg: 'Deleting row, please wait...',
			url: 'ShowUsergroup.do?do=delete&async=true&usergroup_id=' + selectedRow.data.id,
			success: function(form, action){
				dataStore.remove(selectedRow);
			},
			failure: function(form, action){
				Ext.MessageBox.show({
			       title:'Fehler.',
			       msg: 'Gruppe konnte nicht gel&ouml;scht werden.',
			       buttons: Ext.MessageBox.OK,
			       fn: getAnswer,
			       animEl: 'mb4',
			       icon: Ext.MessageBox.WARNING
			   });
			}
			
		});
	}
	
}
  
/* This function creates the toolbar of the usergroup grid and defines the action handlers */  
  
function createToolBar(){

	usergroupToolbar = new Ext.Toolbar([
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
		text: 'Neue Gruppe anlegen',
		id : 'createButton',
		handler: createUsergroupDialog,
		iconCls : 'addgroup-button',
		icon : 'themes/experience/icons/small/group_add.png'					
	}, 
	{
		xtype: 'tbseparator'
	},		
	{
		text: 'Alles speichern',
		handler: storeAll,
		iconCls : 'save-button',
		icon : 'themes/experience/icons/small/disk.png'				
	}]);
}

function storeAll() {
	/* From now on the jason array passed to the struts action is constructed */
	
	jsonData = "[";
	
	/* Iterate through all records and append the current to the json array */
	
	for (var i = 0; i < dataStore.getCount(); i++) {
		record = dataStore.getAt(i);
		if (record.data.newrecord || record.dirty) {
			jsonData += Ext.util.JSON.encode(record.data) + ",";
		}
	}
	
	/* remove trailing comma */
	
	jsonData = jsonData.substring(0, jsonData.length - 1) + "]";
	
	/* submit json data via form */
	
	usergroupGridForm.submit({
		waitMsg: '&Auml;nderungen werden gespeichert, bitte warten ...',
		url: 'ShowUsergroup.do?do=JSONUpdate',
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

/* this function creates an initializes the usergroup grid */

function createUsergroupGrid() {

	/* get form from html page */

	usergroupGridForm = new Ext.BasicForm(
		Ext.get("usergroupgridupdate"), 
		{
		}
	); 
	
	createToolBar();

	
	/* Reload the data store just in case there have been made changes */
	
	dataStore.reload();

	/* define the grid */

	usergroupGrid = new Ext.grid.EditorGridPanel(
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
		tbar : usergroupToolbar,
		bbar: new Ext.PagingToolbar({
			pageSize: 100,
			store: dataStore,
			displayMsg: "{0} - {1} of {2} Records",
			displayInfo: true,
			emptyMsg: "No data to display"
		})
	});
	
}

function createUsergroupDialog() {
	
	if (!usergroupForm)
		createUsergroupForm();	
	
	if(!win){
    	win = new Ext.Window({
            el:'usergroupWindow',
            layout:'fit',

			animateTarget : 'createButton',
            closeAction:'hide',
            plain: true,
            items: usergroupForm,
            height : 170,
            width : 480
        });
    }
	
	usergroupForm.focus();
	
    win.show(this);	
}

function createDeleteConfirmDialog() {
	
	if (usergroupGrid.getSelectionModel().getSelected()) {
	
		Ext.MessageBox.show({
	       title:'Wirklich l&ouml;schen?',
	       msg: 'Wollen Sie diese Gruppe wirklich l&ouml;schen?',
	       buttons: Ext.MessageBox.YESNOCANCEL,
	       fn: getAnswer,
	       animEl: 'mb4',
	       icon: Ext.MessageBox.QUESTION
		});
	}
	else {
		Ext.MessageBox.show({
		       title:'Information',
		       msg: 'Keine Gruppe ausgew&auml;hlt.',
		       buttons: Ext.MessageBox.OK,
		       animEl: 'mb4',
		       icon: Ext.MessageBox.INFO
		});
	}
}

function createUsergroupForm(){
	
	usergroupForm = new Ext.FormPanel({
		labelWidth: 90, // label settings here cascade unless overridden
		url: 'CreateUsergroup.do',
		frame: true,
		title: 'Bitte spezifizieren Sie die Gruppe',
		bodyStyle: 'padding:5px 5px 0',
		items: [
			{ xtype: 'textfield', name: 'name', fieldLabel: 'Gruppenname',width : 300 ,anchor : '95%' },
			{ xtype: 'textfield', name: 'description', fieldLabel: 'Beschreibung' ,width : 300,anchor : '95%'},
		],
        		
		buttons: [{
			text: 'Save',
			handler : function() {
				usergroupForm.getForm().submit({
					url:'CreateUsergroup.do?async=true',
					method:'POST',
					waitMsg:'Speichere, bitte warten ...',
					waitTitle:'Speichere Gruppe',
					failure: function(usergroupForm, action) {
						Ext.MessageBox.show({
						       title:'Fehler',
						       msg: 'Die Gruppe existiert bereits.',
						       buttons: Ext.MessageBox.OK,
						       animEl: 'mb4',
						       icon: Ext.MessageBox.INFO
						});
					},
					success: function(usergroupForm, action){
						dataStore.reload();
						// userForm.destroy();
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
   
