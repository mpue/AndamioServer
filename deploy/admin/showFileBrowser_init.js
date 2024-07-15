var viewport;

// create namespace		
Ext.namespace('Weberknecht');

Weberknecht.filebrowser = function(){
		
    return {

        init: function() {

			Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
    		
    		getScreenDimensions();    		
			createLeftFileGrid();
			createRightFileGrid()
			
			var _width = screenWidth/2;
		 	
		    viewport = new Ext.Viewport({
		           layout:'border',
		           items:[
		               { // raw
		                   region:'north',
						   contentEl: 'north',   //div mit id: north
	  			   		   iconCls: 'panel-icon',		                   
		                   collapsible: false,
		                   title:processName,
		                   height: 105,
		                   minSize: 105,
		                   maxSize: 105,
						   margins:'0 0 5 0'
		               },{
		                   region:'east',
						   id : 'east',
						   title:'Verzeichnisanzeige : cmsroot',
		                   split:true,
		                   width: _width,
		                   // minSize: screenWidth/2,	                   
		                   height : 400,
		                   layout:'fit',
						   margins:'0 5 5 0', //oben, rechts, unten, links
		                   collapsible: true,
		                   items : rightFileGrid
		               },
					   {
		            	   id : 'center',
		                   region:'center',
		                   title:'Verzeichnisanzeige : cmsroot',
						   margins:'0 5 5 0', //oben, rechts, unten, links
		                   collapsible: false,
						   layout:'fit',						   
		                   // minSize: screenWidth/2,
		                   // width: screenWidth/2,		                   
						   height : 400,
						   items : leftFileGrid
		               }
					               
		            ]
		       });
			   	
				viewport.doLayout();
		       	
			}
		};
}();