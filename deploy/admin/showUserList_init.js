var viewport;

// create namespace		
Ext.namespace('Weberknecht');

Weberknecht.userlist = function(){
		
    return {

        init: function() {

			Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
			
			createUserGrid();
            createContextMenu();
		 	
		    viewport = new Ext.Viewport({
		           layout:'border',
		           items:[
		               { // raw
		                   region:'north',
						   contentEl: 'north',   //div mit id: north
	  			   		   iconCls: 'panel-icon',		                   
		                   collapsible: false,
		                   title:'Benutzer',
		                   height: 105,
		                   minSize: 105,
		                   maxSize: 105,
						   margins:'0 0 5 0'
		               },
					   {
		                   region:'center',
		                   title:'Benutzerliste',
						   margins:'0 5 5 0', //oben, rechts, unten, links
		                   collapsible: false,
						   layout:'fit',
						   minSize: 400,
		                   maxSize: 400,
						   height : 400,
						   items : userGrid
		               }
					               
		            ]
		       });
			   	
				viewport.doLayout();
		       	
			}
		};
}();