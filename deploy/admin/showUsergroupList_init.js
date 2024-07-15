var viewport;

// create namespace		
Ext.namespace('Weberknecht');

Weberknecht.usergrouplist = function(){
		
    return {

        init: function() {

			Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
			
			createUsergroupGrid();
		 	
		    viewport = new Ext.Viewport({
		           layout:'border',
		           items:[
		               { // raw
		                   region:'north',
						   contentEl: 'north',   //div mit id: north
	  			   		   iconCls: 'panel-icon',		                   
		                   collapsible: false,
		                   title:'Benutzergruppen',
		                   height: 105,
		                   minSize: 105,
		                   maxSize: 105,
						   margins:'0 0 5 0'
		               },
					   {
		                   region:'center',
		                   title:'Benutzergruppenliste',
						   margins:'0 5 5 0', //oben, rechts, unten, links
		                   collapsible: false,
						   layout:'fit',
						   minSize: 400,
		                   maxSize: 400,
						   height : 400,
						   items : usergroupGrid
		               }
					               
		            ]
		       });
			   	
				viewport.doLayout();
		       	
			}
		};
}();