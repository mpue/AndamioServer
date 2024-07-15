var primaryKey = "id";
var userGridForm;

userObj = Ext.data.Record.create([

	{ name: primaryKey , type: "int" },
	{ name: "joinDate", type: "string" },
	{ name: "position", type: "int" },
	{ name: "title", type: "string" },
	{ name: "name", type: "string" },
	{ name: "land", type: "string" },
	{ name: "ort", type: "string" },
	{ name: "active", type: "boolean" },
	{ name: "telefon", type: "string" },
	{ name: "password", type: "string" },
	{ name: "email", type: "string" },
	{ name: "ranking", type: "int" }

]);
 
//the data store
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
 
dataStore.load({ //include the params object for pagination;
  params: {
    start: 0,
    limit: 100
  }
});
 
//the column model
var columnModel = new Ext.grid.ColumnModel([
  { header: "id", width: 30, sortable: true, dataIndex: "id" },
  { header: "Username", width: 140, sortable: true, dataIndex: "name" ,editor: new Ext.form.TextField({allowBlank: false}) },
  { header: "Title", width: 140, sortable: true, dataIndex: "title" ,editor: new Ext.form.TextField({allowBlank: false})},
  { header: "eMail", width: 140, sortable: true, dataIndex: "email" ,editor: new Ext.form.TextField({allowBlank: false})},
  { header: "Country", width: 140, sortable: true, dataIndex: "land" ,editor: new Ext.form.TextField({allowBlank: false})},
  { header: "City", width: 140, sortable: true, dataIndex: "ort" ,editor: new Ext.form.TextField({allowBlank: false})},
  { header: "Telephone", width: 140, sortable: true, dataIndex: "telefon" ,editor: new Ext.form.TextField({allowBlank: false})},
  { header: "Active", width: 50, sortable: true, dataIndex: "active",renderer: check },
  { header: "Edit", width: 50, sortable: false, dataIndex: "id",renderer: edit }
]);

var selectionModel = new Ext.grid.RowSelectionModel(
	{ 
		singleSelect: true 
	}
)


 
function check(active){
	return '<input type="checkbox" checked="' + active + '">';
} 

function edit(id) {
	return '<a href="ShowUser.do?do=edit&user_id='+id+'">Edit</a>';
}
 
var toolbar = new Ext.Toolbar(
	[
		{
			text : 'Add user',
			handler : function () {
				dataStore.add(
					new Ext.data.Record(
						{
							name : 'new User',
							email : 'email Address',
							title : 'User',
							land : 'new Country',
							newRecord : true
						}
					)
				)
			}
		},
		{
			text : 'Remove user',
			handler : function () {
				selectedRow = userGrid.getSelectionModel().getSelected();
				if (selectedRow) {
					userGridForm.submit(
						{
							waitMsg : 'Deleting row, please wait...',
							url : 'ShowUser.do?do=delete&async=true&user_id='+selectedRow.data.id,
							success:function(form,action) {
								dataStore.remove(selectedRow);							
							} ,
							failure:function(form,action) {
								alert('Could not delete user!');						
							} 
							
						}
					);
				}
			}
		},
		{
			text : 'Save changes.',
			handler : function () {
				jsonData = "[";
				
				for (i = 0;i < dataStore.getCount();i++) {
					record = dataStore.getAt(i);
					if (record.data.newrecord || record.dirty) {
						jsonData += Ext.util.JSON.encode(record.data) +","				
					}
				}
				
				jsonData = jsonData.substring(0,jsonData.length-1) + "]";
				
				userGridForm.submit(
					{
						waitMsg : 'Saving changes, please wait...',
						url : 'ShowUser.do?do=JSONUpdate',
						params : { data:jsonData},
						success:function(form,action) {
							alert("Saved successfully");							
						} ,
						failure:function(form,action) {
							alert('Could not update!');						
						} 
					}
				);	
			}
		}		
	]
) 
 
makeGrid = function () {

	userGridForm = new Ext.BasicForm(
		Ext.get("updategrid"), 
		{
		}
	);
	

  userGrid = new Ext.grid.EditorGridPanel({    
    store: dataStore,
    colModel: columnModel,
    autoScroll: true,
    sm: selectionModel,
    height: 400,
    width: 1280,
    stripeRows: true,
    clicksToEdit:1,
    tbar : toolbar,
    bbar: new Ext.PagingToolbar({
      pageSize: 100,
      store: dataStore,
      displayMsg: "{0} - {1} of {2} Records",
      displayInfo: true,
      emptyMsg: "No data to display"
    })
  }); //end grid;
  userGrid.render("userList");
  
  // var gridHeaderPanel = userGrid.getView().getHeaderPanel(true);

  		
  	
 
};
 
Ext.onReady(makeGrid);

