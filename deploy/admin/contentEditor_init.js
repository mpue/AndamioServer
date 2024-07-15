var viewport;

// create namespace		
Ext.namespace('Weberknecht');

Weberknecht.app = function(){
		
    return {

        init: function() {
			
			/*
			 * Erzeugen des Baums
			 */ 
			createContentTree();
			
			/*
			 * Erzeugen des Contenteditors
			 */
			createContentEditor("Editor");
			
			Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
		 	
		    viewport = new Ext.Viewport({
		           layout:'border',
		           items:[
		               { 
		                   region:'north',
						   contentEl: 'north',   
	  			   		   iconCls: 'panel-icon',		                   
		                   collapsible: false,
		                   title: processName,
		                   height: 105,
		                   minSize: 105,
		                   maxSize: 105,
						   margins:'0 0 5 0'
		               },
		               {
		                   region:'west',
		                   title: contentsKey,
		                   collapsible: true,
		                   split:true,
		                   width: 225,
		                   minSize: 175,
		                   maxSize: 400,
		                   layout:'fit',
		                   margins:'0 0 5 5', //oben, rechts, unten, links
						   items : tree,
				           stateId : '_tree',
				           id : '_tree',
				           stateful: true,
				           stateEvents : ["drop","close","collapse","expand"]
						   
		               },		               
					   {
		                   region:'center',
						   margins:'0 5 5 0', //oben, rechts, unten, links
		                   collapsible: false,
						   layout:'fit',
						   items : contentEditor
		               },
					   {
		                   region:'south',
						   contentEl: 'south',   //div mit id: north
		                   title:'Status',
		                   height: 95,
		                   minSize: 95,
		                   maxSize: 95,
						   margins:'0 0 5 0',
		                   collapsible: true,
				           stateId : '_status',
				           id : '_status',
				           stateful: true,
				           stateEvents : ["drop","close","collapse","expand"]
		                   
		               }			               
					               
		            ]
		       });
			   	
				viewport.doLayout();				
		      	tinyMCE.execCommand('mceRemoveControl',false,'contentstring');
		      	document.getElementById("contentstring").value = pleaseSelectNodeKey;
		      	tinyMCE.execCommand('mceAddControl',false,'contentstring');

		      	deselectContent();
			}
		};
}();