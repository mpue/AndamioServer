var gridForm;
var grid; 
var toolbar;

/* This is the definition of our job object */

var jobObj = Ext.data.Record.create([
	{ name: "name", type: "string" },
	{ name: "description", type: "string" },
	{ name: "fireTime", type: "string" },
	{ name: "jobRunTime", type: "string" },
	{ name: "progress", type: "int" }
]);
  
/* This is the data store definition for the user object */ 

var dataStore = new Ext.data.Store({
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
 
/* Override the load method of the datastore and bind some parameters */ 
 
dataStore.load({ //include the params object for pagination;
  params: {
    start: 0,
    limit: 100
  }
});
 
/* This is the column model definition for the editable grid */

var columnModel = new Ext.grid.ColumnModel([
  { header: "Name", width: 200, sortable: true, dataIndex: "name" },
  { header: "Beschreibung", width: 200, sortable: true, dataIndex: "description" },
  { header: "Gestartet", width: 200, sortable: true, dataIndex: "fireTime" },
  { header: "Fortschritt", width: 200, sortable: true, dataIndex: "progress" },
]);

var selectionModel = new Ext.grid.RowSelectionModel(
	{ 
		singleSelect: true 
	}
)
 
  
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
	}])
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
	
}
