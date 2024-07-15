var viewport;
var toolbar;

// create namespace		
Ext.namespace('Weberknecht');

Weberknecht.administration = function(){
		
    return {

        init: function() {

			Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
		 	
		 	createToolBar();
		 	
		    viewport = new Ext.Viewport({
		           layout:'border',
		           items:[
					   {
		                   region:'center',
						   contentEl:'main-div', //div mit id: hilfe
		                   title:'Haupt&uuml;bersicht',
						   margins:'0 5 5 0', //oben, rechts, unten, links
		                   collapsible: false,
		                   autoScroll : true,
						   tbar : toolbar
		               },
					   {
		                   region:'south',
						   contentEl: 'south',   //div mit id: north
		                   title:'Status',
		                   height: 95,
		                   minSize: 95,
		                   maxSize: 95,
						   margins:'0 0 5 0',
   		                   collapsible: true
		               }		               
					               
		            ]

		       });
			   	
				viewport.doLayout();
		       	
			}
		};
}();

var contentMenu = new Ext.menu.Menu({
    id: 'contentMenu',
    items: [
        {
            text: 'Verwalten',
    		iconCls: 'contents-icon',		                               
			handler: function(){
				document.location.href = 'ShowContent.do?do=editContentsNew';
			}
        },
        {
            text: 'Erweiterte Verwaltung',
    		iconCls: 'node-icon',
			handler: function(){
				document.location.href = 'ListNodes.do';
			}

        },
        {
            text: 'Men&uuml;punkt anlegen',
    		iconCls: 'node_add-icon',
			handler: function(){
				document.location.href = 'CreateNode.do';
			}

        },
        
    ]
});

var moduleMenu = new Ext.menu.Menu({
    id: 'moduleMenu',
    items: [
        {
            text: 'Seiteninhalte verwalten',
    		iconCls: 'contents-icon',
			handler: function(){
				document.location.href = 'ShowContent.do?do=editContents';
			}
        },
        /*
        {
            text: 'Kontakte',
    		iconCls: 'contacts-icon',		                               
			handler: function(){
				document.location.href = 'ListContacts.do';
			}
        },
        {
            text: 'Bildergalerien',
    		iconCls: 'galleries-icon',
			handler: function(){
				document.location.href = 'ListGalleries.do';
			}
        },
        */
        {
            text: 'Dateimanager',
    		iconCls: 'filemanager-icon',
			handler: function(){
				document.location.href = 'ListDirectories.do?do=showBrowser';
			}
        },
    ]
});

var systemMenu = new Ext.menu.Menu({
    id: 'systemMenu',
    items: [
        {
            text: 'Einstellungen',
    		iconCls: 'config-icon',		                               
			handler: function(){
				document.location.href = 'ShowConfig.do?do=edit';
			}
        },
        {
            text: 'Erweiterungen',
    		iconCls: 'plug-icon',
			handler: function(){
				document.location.href = 'ListPlugins.do?do=list';
			}

        },
        {
            text: 'Prozesse',
    		iconCls: 'processes-icon',
			handler: function(){
				document.location.href = 'ListProcesses.do?do=list';
			}

        },
        {
            text: 'Benutzer',
    		iconCls: 'users-icon',
			handler: function(){
				document.location.href = 'ListUsers.do?do=list';
			}

        },        
        {
            text: 'Benutzergruppen',
    		iconCls: 'usergroups-icon',
			handler: function(){
				document.location.href = 'ListUsergroups.do?do=list';
			}

        }
                
    ]
});

function createToolBar(){

	toolbar = new Ext.Toolbar(
		[
		 	/*
			{
				text: 'Module',
				id : 'moduleButton',
				menu : moduleMenu
			},
			{
				text: 'Systemverwaltung',
				id : 'systemButton',
				menu : systemMenu
			},
			*/			
			{
				text: 'Abmelden',
				id : 'logoutButton',
				handler: function(){
					document.LogoutForm.submit();
				}
			},
			{
				text: 'Seitenansicht',
				id : 'showPage',
				handler: function(){
					document.location.href = docBaseUrl;
				}
			}			
		]
	);
}

