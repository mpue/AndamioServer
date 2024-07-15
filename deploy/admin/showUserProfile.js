var primaryKey = "id";
var userForm;
var userToolbar;

/* This is the definition of our user object */

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
  
  
/* This function creates the toolbar of the user grid and defines the action handlers */  
  
function createToolBar(){

	userToolbar = new Ext.Toolbar([
	{
		text: 'Hauptmen&uuml;',
		id : 'mainmenuButton',
		handler: function(){
			document.location.href = 'index.jsp';
		}
	},
	{
		xtype: 'tbseparator'
	},	
	{
		text: 'Alles speichern',
		handler: function(){
			
			userForm.submit({
				waitMsg: '&Auml;nderungen werden gespeichert, bitte warten ...',
				url: 'ShowUserProfile.do?do=JSONUpdate',
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

		}
	}])
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
			{ xtype: 'textfield', name: 'password', fieldLabel: 'Passwort',width : 300 ,anchor : '95%'},
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
					waitTitle:'Speichere Daten',
					failure: function(userForm, action) {
						Ext.MessageBox.alert('Konnte Daten nicht speichern.', "err");
					},
					success: function(userForm, action){
					}
				}); 			
			}
		}]
	});
}
   
