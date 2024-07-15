var viewport;

// create namespace		
Ext.namespace('Weberknecht');

Weberknecht.skeleton = function(){
		
    return {

        init: function() {

			Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
		 	
		    viewport = new Ext.Viewport({
		           layout:'border',
		           items:[
		               { // raw
		                   region:'north',
						   contentEl: 'north',   //div mit id: north
	  			   		   iconCls: 'panel-icon',		                   
		                   collapsible: false,
		                   title:'Hier Titel reinmachen',
		                   height: 95,
		                   minSize: 95,
		                   maxSize: 95,
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
						   contentEl:'main-div', //div mit id: hilfe
		                   title:'Main',
						   margins:'0 5 5 0', //oben, rechts, unten, links
		                   collapsible: false
		               }
					               
		            ]
		       });
			   	
				viewport.doLayout();
		       	
			}
		};
}();