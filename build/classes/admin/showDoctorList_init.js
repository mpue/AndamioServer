var viewport;

// create namespace		
Ext.namespace('Weberknecht');

Weberknecht.doctorlist = function(){
		
    return {

        init: function() {

			Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
			
			createDoctorGrid();
			createContextMenu();
			
		    viewport = new Ext.Viewport({
		           layout:'border',
		           items:[
		               { // raw
		                   region:'north',
						   contentEl: 'north',   //div mit id: north
	  			   		   iconCls: 'panel-icon',		                   
		                   collapsible: false,
		                   title:'Aerzte/Praxen',
		                   height: 105,
		                   minSize: 105,
		                   maxSize: 105,
						   margins:'0 0 5 0'
		               },{
		                   region:'east',
						   contentEl:'hilfe', //div mit id: hilfe
		                   title:'Hilfe',
		                   split:true,
		                   width: 200,
		                   minSize: 175,
		                   maxSize: 400,
						   margins:'0 5 5 0', //oben, rechts, unten, links
		                   collapsible: true
		               },
					   {
		                   region:'center',
		                   title:'Liste der Praxen/Aerzte',
						   margins:'0 5 5 0', //oben, rechts, unten, links
		                   collapsible: false,
						   layout:'fit',
						   minSize: 400,
		                   maxSize: 400,
						   height : 400,
						   items : doctorGrid
		               }
					               
		            ]
		       });
			   	
				viewport.doLayout();
		       	
			}
		};
}();