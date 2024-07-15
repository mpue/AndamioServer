var viewport;

// create namespace		
Ext.namespace('Weberknecht');

Weberknecht.jobBrowser = function(){
		
    return {

        init: function() {

			Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
			
			createGrid();
		 	
		    viewport = new Ext.Viewport({
		           layout:'border',
		           items:[
		               { // raw
		                   region:'north',
						   contentEl: 'north',   //div mit id: north
	  			   		   iconCls: 'panel-icon',		                   
		                   collapsible: false,
		                   title:'Jobs',
		                   height: 105,
		                   minSize: 105,
		                   maxSize: 105,
						   margins:'0 0 5 0'
		               },
					   {
		                   region:'center',
		                   title:'Job&uuml;bersicht',
						   margins:'0 5 5 0', //oben, rechts, unten, links
		                   collapsible: false,
						   layout:'fit',
						   minSize: 400,
		                   maxSize: 400,
						   height : 400,
						   items :grid
		               }
					               
		            ]
		       });
			   	
				viewport.doLayout();
		       	
			}
		};
}();