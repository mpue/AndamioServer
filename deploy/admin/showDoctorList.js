var primaryKey = "id";
var doctorGridForm;
var doctorGrid; 
var doctorToolbar;
var win;
var confirmWin;
var doctorForm;
var patientForm;
var patientWin;
var patientListWin;
var ctxMenu;
var patientStore;

/* This is the definition of the doctor object */

doctorObj = Ext.data.Record.create([

    { name: primaryKey , type: "int" },
    { name: "firstname", type: "string" },
	{ name: "lastname", type: "string" },
	{ name: "login", type: "string" },
	{ name: "type", type: "string" }

]);

patientObj = Ext.data.Record.create([

    { name: primaryKey , type: "int" },
    { name: "randomNumber", type: "string" }

]);
  
/* This is the data store definition for the doctor object */ 

var dataStore = new Ext.data.Store({
  url: "DoctorAction.do?do=getJSON",
  remoteSort: false,
  reader: new Ext.data.JsonReader({
    root: "results", 
    totalProperty: "total",
    id: primaryKey },
    doctorObj
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
  { header: "Firstname", width: 140, sortable: true, dataIndex: "firstname" ,editor: new Ext.form.TextField({allowBlank: false})},
  { header: "Lastname", width: 140, sortable: true, dataIndex: "lastname" ,editor: new Ext.form.TextField({allowBlank: false})},
  { header: "Type", width: 140, sortable: true, dataIndex: "type" ,editor: new Ext.form.TextField({allowBlank: false})},   
  { header: "Login", width: 140, sortable: true, dataIndex: "login" ,editor: new Ext.form.TextField({allowBlank: false})},
  { header: "", width: 40, sortable: false, dataIndex: "id",renderer: remove , align : 'center' }
]);

var selectionModel = new Ext.grid.RowSelectionModel(
	{ 
		singleSelect: true 
	}
)

function remove(id) {
	return '<img src="themes/experience/icons/delete.png" border="0" align="middle" height="16px" width="16px" onclick="createDeleteConfirmDialog();"/>';
}

function getAnswer(answer) {
	if (answer == 'yes' ) {
		removeSelected();
	}
}

function removeSelected() {
	
	selectedRow = doctorGrid.getSelectionModel().getSelected();
	
	if (selectedRow) {
		doctorGridForm.submit({
			waitMsg: 'Deleting row, please wait...',
			url: 'DoctorAction.do?do=delete&id=' + selectedRow.data.id,
			success: function(form, action){
				dataStore.remove(selectedRow);
			},
			failure: function(form, action){
				Ext.MessageBox.show({
			       title:'Fehler.',
			       msg: 'Arzt/Praxis konnte nicht gel&ouml;scht werden.',
			       buttons: Ext.MessageBox.OK,
			       fn: getAnswer,
			       animEl: 'mb4',
			       icon: Ext.MessageBox.WARNING
			   });
			}
			
		});
	}
	
}
  
/* This function creates the toolbar of the doctor grid and defines the action handlers */  
  
function createToolBar(){

	doctorToolbar = new Ext.Toolbar([
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
		text: 'Neue(n) Arzt/Praxis anlegen',
		id : 'createButton',
		handler: createDoctorDialog,
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
	
	doctorGridForm.submit({
		waitMsg: '&Auml;nderungen werden gespeichert, bitte warten ...',
		url: 'DoctorAction.do?do=JSONUpdate',
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

/* this function creates an initializes the doctor grid */

function createDoctorGrid() {

	/* get form from html page */

	doctorGridForm = new Ext.BasicForm(
		Ext.get("Doctorgridupdate"), 
		{
		}
	); 
	
	createToolBar();

	
	/* Reload the data store just in case there have been made changes */
	
	dataStore.reload();

	/* define the grid */

	doctorGrid = new Ext.grid.EditorGridPanel(
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
		tbar : doctorToolbar,
		bbar: new Ext.PagingToolbar({
			pageSize: 100,
			store: dataStore,
			displayMsg: "{0} - {1} of {2} Records",
			displayInfo: true,
			emptyMsg: "No data to display"
		})
	});

	doctorGrid.on('rowcontextmenu', createContext);
}

function createDoctorDialog() {
	
	if (!doctorForm)
		createDoctorForm();	
	
	if(!win){
    	win = new Ext.Window({
            el:'DoctorWindow',
            layout:'fit',
			animateTarget : 'createButton',
            closeAction:'hide',
            plain: true,
            modal : true,
            items: doctorForm,
            height : 230,
            width : 480
        });
    }
	
	doctorForm.focus();
	
    win.show(this);	
}

function createPatientDialog(id) {
	
	var patientForm = new Ext.FormPanel({
		labelWidth: 90, // label settings here cascade unless overridden
		url: 'DoctorAction.do?do=addPatient&id='+id,
		frame: true,
		title: 'Bitte spezifizieren Sie den Patienten',
		bodyStyle: 'padding:5px 5px 0',
		items: [
			{ xtype: 'textfield', name: 'randomNumber', fieldLabel: 'Random-Nr.',width : 300 ,anchor : '95%' }			
		],
        		
		buttons: [{
			text: 'Save',
			handler : function() {
				patientForm.getForm().submit({
					url:'DoctorAction.do?do=addPatient&id='+id,
					method:'POST',
					waitMsg:'Speichere, bitte warten ...',
					waitTitle:'Speichere Patient',
					failure: function(patientForm, action) {
						Ext.MessageBox.show({
						       title:'Fehler',
						       msg: 'Patient existiert bereits.',
						       buttons: Ext.MessageBox.OK,
						       animEl: 'mb4',
						       icon: Ext.MessageBox.INFO
						});
					},
					success: function(patientForm, action){
						patientStore.reload();
						// userForm.destroy();
						patientWin.hide();
					}
				}); 			
			}
		}, {
			text: 'Cancel',
            handler: function(){
                patientWin.hide();
            }			
		}]
	});
	

	var patientWin = new Ext.Window({
        layout:'fit',
		animateTarget : 'addPatient',
        closeAction:'hide',
        plain: true,
        items: patientForm,
        height : 150,
        width : 480
    });
    
	
	patientForm.focus();	
    patientWin.show(win);	
}

function createDeleteConfirmDialog() {
	
	if (doctorGrid.getSelectionModel().getSelected()) {
	
		Ext.MessageBox.show({
	       title:'Wirklich l&ouml;schen?',
	       msg: 'Wollen Sie diese(n) Arzt/Praxis wirklich l&ouml;schen?',
	       buttons: Ext.MessageBox.YESNOCANCEL,
	       fn: getAnswer,
	       animEl: 'mb4',
	       icon: Ext.MessageBox.QUESTION
		});
	}
	else {
		Ext.MessageBox.show({
		       title:'Information',
		       msg: 'Keine Arzt/Praxis ausgew&auml;hlt.',
		       buttons: Ext.MessageBox.OK,
		       animEl: 'mb4',
		       icon: Ext.MessageBox.INFO
		});
	}
}

function createDoctorForm(){
	
	doctorForm = new Ext.FormPanel({
		labelWidth: 90, // label settings here cascade unless overridden
		url: 'DoctorAction.do?do=create',
		frame: true,
		title: 'Bitte spezifizieren Sie die Praxis/den Arzt',
		bodyStyle: 'padding:5px 5px 0',
		items: [
			{ xtype: 'textfield', name: 'firstname', fieldLabel: 'Vorname',width : 300 ,anchor : '95%' },
			{ xtype: 'textfield', name: 'lastname', fieldLabel: 'Nachname',width : 300 ,anchor : '95%' },
			{ xtype: 'textfield', name: 'login', fieldLabel: 'Login',width : 300 ,anchor : '95%' },
			{ xtype: 'textfield', name: 'type', fieldLabel: 'Typ',width : 300 ,anchor : '95%' },
			
		],
        		
		buttons: [{
			text: 'Save',
			handler : function() {
				doctorForm.getForm().submit({
					url:'DoctorAction.do?do=create',
					method:'POST',
					waitMsg:'Speichere, bitte warten ...',
					waitTitle:'Speichere Arzt/Praxis',
					failure: function(doctorForm, action) {
						Ext.MessageBox.show({
						       title:'Fehler',
						       msg: 'Praxis/Arzt existiert bereits.',
						       buttons: Ext.MessageBox.OK,
						       animEl: 'mb4',
						       icon: Ext.MessageBox.INFO
						});
					},
					success: function(doctorForm, action){
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

/**
 * The selection model of the usergroup data store
 */
var patientSelectionModel = new Ext.grid.RowSelectionModel(
	{
		singleSelect: false
	}
)

/**
 * This is the column model definition for the editable grid of the groups
 */
var patientColumnModel = new Ext.grid.ColumnModel([
  { header: "id", width: 30, sortable: true, dataIndex: "id" },
  { header: "Random-Nr.", width: 140, sortable: true, dataIndex: "randomNumber" }
]);


function createContext(grid, rowIndex, event){
    event.preventDefault();
    doctorGrid.getSelectionModel().selectRow(rowIndex);
    ctxMenu.showAt(event.getXY());
}

function createContextMenu() {

    ctxMenu = new Ext.menu.Menu({

        id:'ctx',

        items: [
            {
                id:'showPatients',
                handler:showPatients,
                text:'Patienten'               
            }            
        ]
    });
}
 
function showPatients() {
	
	var record = doctorGrid.getSelectionModel().getSelected();
	
	patientStore = getPatientStore(record);
	patientStore.load();

	var toolbar = new Ext.Toolbar([
	    {
			text: 'Patient hinzuf&uuml;gen',
			id : 'addPatient',
			handler : function() {				
				createPatientDialog(record.data.id);
	    	}
		},
	    {
			text: 'Patient(en) entfernen',
			id : 'removePatient',
			handler : function() {
				var selectedRow = patientSelectionModel.getSelected();
	    		removePatient(record.data.id,selectedRow.data.id);
				patientStore.remove(selectedRow);
	    	}
		}

	]);

	var patientGrid = new Ext.grid.EditorGridPanel(
	{
		store: patientStore,
		colModel: patientColumnModel,
		autoScroll: true,
		sm: patientSelectionModel,
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

	var patientEditWindow = new Ext.Window({
        layout:'fit',
        closeAction:'hide',
        plain: true,
        modal : true,
        items: patientGrid,
        height : 300,
        width : 500
    });

	patientEditWindow.on('hide', function(win){
		patientGrid.destroy();
		patientEditWindow.destroy();
	});

	patientEditWindow.setTitle("Patienten F&uuml;r "+record.data.lastname+","+record.data.firstname);
	patientEditWindow.show(this);
	patientEditWindow.focus();
}

function getPatientStore(record) {

	var store = new Ext.data.Store({
		url: "DoctorAction.do?do=getPatients&id="+record.data.id,
		remoteSort: false,
		reader: new Ext.data.JsonReader({
			root: "rows",
			id: primaryKey },
			patientObj
		),
		sortInfo:{field:'id', direction:'ASC'}
	});

	return store;
}

function removePatient(doctor_id,patient_id) {
	var poststr = "id=" + doctor_id + "&patient_id="+patient_id;
	makePOSTRequest('DoctorAction.do?do=removePatient&', poststr);
}