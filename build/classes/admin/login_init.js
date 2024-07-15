var viewport;
var loginWin;
var loginForm;

// create namespace		
Ext.namespace('Weberknecht');

Weberknecht.login = function(){
		
    return {

        init: function() {

			// Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
	
			createLoginForm();
			createLoginDialog();

		}
	}
}();

function createLoginForm(){
	
	loginForm = new Ext.FormPanel({
		labelWidth: 90, // label settings here cascade unless overridden
		url: 'LoginDialog.do?do=login&frontpage=false&async=true',
		frame: true,
		title: 'Bitte geben Sie Ihre Zugangsdaten ein',
		bodyStyle: 'padding:5px 5px 0',
		items: [
			{ xtype: 'textfield', id : 'username', name: 'username', fieldLabel: 'Benutzername',width : 100 ,anchor : '90%', allowBlank : false },
			{ xtype: 'textfield', id : 'password' ,inputType : 'password', name: 'password', fieldLabel: 'Passwort',width : 100 ,anchor : '90%', allowBlank : false  }
		],
        		
		buttons: [{
			text: 'Anmelden',
			handler : doLogin
		}]
	});
}

function doLogin() {
	loginForm.getForm().submit({
		url:'LoginDialog.do?do=login&frontpage=false&async=true',
		method:'POST',
		waitMsg:'Anmeldung, bitte warten ...',
		waitTitle:'Anmeldung...',
		failure: function(loginForm, action) {
			Ext.MessageBox.show({
			       title:'Fehler',
			       msg: 'Anmeldung fehlgeschlagen.',
			       buttons: Ext.MessageBox.OK,
			       animEl: 'mb4',
			       icon: Ext.MessageBox.INFO
			});
		},
		success: function(loginForm, action){
			loginWin.hide();
			document.location.href = 'index.jsp';
		}
	}); 			
	
}

function createLoginDialog() {
	
	if (!loginForm)
		createLoginForm();	
	
	if(!loginWin){
    	loginWin = new Ext.Window({
            el:'loginWindow',
            layout:'fit',
            iconCls: 'title-icon',		                               
			closable : false,	
            plain: true,
            items: loginForm,
            height : 200,
            width : 480
        });
    }
	
	loginForm.focus();
	
    loginWin.show();	
    
	var field = Ext.getCmp('username');	
	field.focus(true,200);

}
var key = new Ext.KeyMap(document, [{

    key: Ext.EventObject.ENTER,
    
    fn: function(){
		doLogin();
	}
}]);	
