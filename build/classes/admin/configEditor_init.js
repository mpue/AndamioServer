var viewport;

// create namespace		
Ext.namespace('Weberknecht');

Weberknecht.configEditor = function(){
		
    return {

        init: function() {

			// Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
			
			createConfigForm();
			createToolBar();
			
			configForm.load({
				url:'ShowConfig.do?do=edit&async=true',
				method : 'GET',
				waitMsg: messageLoadingKey
			});
		 	
		    viewport = new Ext.Viewport({
		           layout:'border',
		           items:[
		               { // raw
		                   region:'north',
						   contentEl: 'north',   //div mit id: north
	  			   		   iconCls: 'panel-icon',		                   
		                   collapsible: false,
		                   title: processName,
		                   height: 105,
		                   minSize: 105,
		                   maxSize: 105,
						   margins:'0 0 5 0'
		               },
					   {
		                   region:'center',
						   margins:'0 5 5 0', //oben, rechts, unten, links
		                   collapsible: false,
						   layout:'fit',
						   minSize: 400,
		                   maxSize: 400,
						   height : 400,
						   items : configForm,
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