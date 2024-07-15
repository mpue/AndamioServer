var tree;
var treeLoader;
var root;
var firstTreeCallSection = true;
var currentNode;

Ext.override(Ext.tree.TreeLoader,{
    processResponse : function(response, node, callback){
        var json = response.responseText;
        try {
            var o = eval("("+json+")");
            
            node.beginUpdate();
            
            for (var i = 0, len = o.length; i < len; i++){

                var n = this.createNode(o[i]);

				n.attributes.type = o[i].type;
				n.attributes.node_id = o[i].node_id;
				n.attributes.restricted = o[i].restricted;
				n.attributes.nodepath = o[i].nodepath;
				
				if (o[i].type == 'content')
					n.attributes.content_id = o[i].content_id;

                if(n){
                    node.appendChild(n);
                }
                
            }
            
            node.endUpdate();
            
            if(typeof callback == "function"){
                callback(this, node);
            }
        }
        catch(e){
            this.handleFailure(response);
        }
    }

});

var ctxMenu = new Ext.menu.Menu({

    id:'copyCtx',

    items: [
        {
            id:'refresh',
            handler:reloadTree,            
            text: refreshKey,
            iconCls : 'refresh-icon'            	
        },
        {
            id:'edit',
            handler:ctxEditNode,            
            text: editKey,
            iconCls : 'edit-icon'
        },
        {
            id:'delete',
            handler:deleteNode,            
            text: deleteKey,
            iconCls : 'delete-icon'            	
        },
        {
            id:'nodeup',
            handler:moveNodeUp,            
            text: moveUpKey,
            iconCls : 'up-icon'            	
        },
        {
            id:'nodedown',
            handler:moveNodeDown,            
            text: moveDownKey,
            iconCls : 'down-icon'            	
        },
        {
            id:'editGroups',
            handler:ctxEditGroups,            
            text: groupsKey,
            iconCls : 'usergroups-icon'
        }
        
    ]    
});


function createContext(node, e){
	
    if((node.attributes.type == 'content') ||
       (node.attributes.type == 'plugin') ||
       (node.attributes.type == 'link')){

    	Ext.getCmp('edit').setDisabled(false);
    	Ext.getCmp('delete').setDisabled(false);
    	Ext.getCmp('nodeup').setDisabled(false);
    	Ext.getCmp('nodedown').setDisabled(false);
    	Ext.getCmp('editGroups').setDisabled(false);
    }
    else {
    	Ext.getCmp('edit').setDisabled(true);
    	Ext.getCmp('delete').setDisabled(true);
    	Ext.getCmp('nodeup').setDisabled(true);
    	Ext.getCmp('nodedown').setDisabled(true);
    	Ext.getCmp('editGroups').setDisabled(true);    	
    }

    var selModel = tree.getSelectionModel();
	selModel.select(node);
    ctxMenu.show(node.ui.getAnchor());
	
}

function ctxEditNode(){
	editNode(tree.getSelectionModel().getSelectedNode());
}

function ctxEditGroups() {
	editGroups(tree.getSelectionModel().getSelectedNode());
}

function checkDelete(answer) {
	if (answer =='yes') {

        deselectContent();
        
		var selected = tree.getSelectionModel().getSelectedNode();
		
	    if(selected.attributes.type == 'content'){
			removeContentAndNode(selected);
	    }
	    else {
		    removeNode(selected);
		}

		setTimeout("reloadTree()",500);		
	}	
}

function deleteNode(){

	Ext.MessageBox.show({
		title:dialogDeleteNodeTitleKey,
		msg: dialogDeleteNodekey,
		buttons: Ext.MessageBox.YESNOCANCEL,
		fn: checkDelete,
		animEl: 'mb4',
		icon: Ext.MessageBox.QUESTION
	});

}

function reloadTree() {
	tree.getRootNode().reload();									
	root.expand(true,true);
}

function addNode(){
	alert('add');
}

function moveNodeUp() {
	var selected = tree.getSelectionModel().getSelectedNode();
	nodeUp(selected); 
	setTimeout("reloadTree()",500);
}

function moveNodeDown() {
	var selected = tree.getSelectionModel().getSelectedNode();
	nodeDown(selected); 
	setTimeout("reloadTree()",500);
}

var yesproceed = false;

function getAnswer(answer) {
	if (answer == 'yes' ) {
		Ext.getCmp('saveBtn').disable();
		setTimeout("editNode(currentNode)",500);
		// editNode(currentNode);
		dirty = false;
	}	
}

function createContentTree(){
   
	treeLoader = new Ext.tree.TreeLoader(
	{
		requestMethod : 'GET',
		dataUrl:'Tree.do?do=getNodesAsJSON',
		listeners: {
            beforeload: function(treeLoader, node) {
                this.baseParams.id = node.attributes.node_id;
            }
        }		

	});
		
    tree = new Ext.tree.TreePanel({
        el:'tree-div',
        animate:true, 
        autoScroll:true,
        loader:treeLoader ,
        enableDD:false,
        containerScroll: true,
		draggable:false,
		folderSort : true,
        dropConfig: {appendOnly:true}
    });
    
	/* 'click any node' function */
	tree.on('dblclick', function( node ) {
		
		currentNode = node;
		
		remainingTime = maxInactiveInterval; // reset session interval
		
		if (dirty) {
			Ext.MessageBox.show({
				title: dialogWarningTitleKey,
				msg: dialogContinueKey,
				buttons: Ext.MessageBox.YESNOCANCEL,
				fn: getAnswer,
				animEl: 'mb4',
				icon: Ext.MessageBox.QUESTION
			});
		}
		else {		
			Ext.getCmp('saveBtn').disable();
			
			if (currentNode.attributes.type == 'content') {
				
				nothingSelected = false;
				
			    Ext.getCmp('contentname').setDisabled(false);
			    Ext.getCmp('description').setDisabled(false);
			    Ext.getCmp('pagename').setDisabled(false);
			    Ext.getCmp('metatags').setDisabled(false);
			    Ext.getCmp('commentsAllowed').setDisabled(false);
			    Ext.getCmp('previewItem').setDisabled(false);
			    Ext.getCmp('metatagsItem').setDisabled(false);
			    
				contentEditor.load({
					url:'ShowNode.do?do=getContentAsJson&content_id='+currentNode.attributes.content_id,
                    method : 'GET',
					waitMsg: messageLoadingKey,
					success : function(form, action){					
						var metatagsField = Ext.getCmp('metatags');						
						var s = metatagsField.getValue(); 
						
						var str = s.convertHTMLEntity();
											
						var newstr = "";
						var c;
						for (var i = 0; i<str.length; i++) {

							c = str.charAt(i);
							
							if (i > 1 && c == '<') {
								newstr = newstr + '\n';
							}
							
							newstr = newstr + c;

						}
						metatagsField.setValue(newstr);
						
					}
				});				
				
				retrieveHTML('ShowNode.do?do=getContent&node_id=' + currentNode.attributes.node_id );
				document.getElementById("id").value = currentNode.attributes.content_id;
			}
			else {

                editNode(currentNode);

                /*
				createPluginEditDialog();
				
				pluginEditForm.load({
					url:'ShowNode.do?do=getNodeAsJson&node_id='+node.attributes.node_id,
                    method : 'GET',
					waitMsg:'Loading...'
				});
                */
			}	
			
			dirty = false;
		
		}
		
			
	});
    
	
	tree.on("beforeappend", function(tree, parent, node){

		if(node.attributes.type == 'plugin'){
	        node.iconCls = "link";
	    }
	    else if(node.attributes.type == 'link'){
	        node.iconCls = "plugin";
	    }
		
	    else if(node.attributes.type == 'content'){
	    	
	    	if (node.attributes.restricted) {    		
	    		node.iconCls = "content_restricted";
	    	}
	    	
	    	else {
	    		node.iconCls = "content";
	    	}
	    	
	    }
	    
	});
	
    // set the root node
    root = new Ext.tree.AsyncTreeNode({
        text: nodesKey, 
        draggable:false,
		id : 'root'
    });

    tree.setRootNode(root);
    tree.render();
	root.expand(true,true);
	
	/*
	 * Select the first node in our tree
	 */
	tree.on('append', function(tree, p, node){
       	if( firstTreeCallSection ) {
           node.select.defer(100, node); // select node
		   firstTreeCallSection = false; // set to false, our first node was found!
       }
    });
    
    tree.on('contextmenu', createContext);
       
}

function editGroups(node) {
	createGroupEditDialog(node.attributes.node_id);	
}

function editNode(node) {

	deselectContent();
	
	if (node.attributes.type == 'link') {

		createLinkEditDialog();

	    linkEditForm.load({
	        url:'ShowNode.do?do=getNodeAsJson&node_id='+node.attributes.node_id,
	        method : 'GET',
	        waitMsg: messageLoadingKey
	    });
	}
	else { // plugin or content

	    createPluginEditDialog();

	    pluginEditForm.load({
	        url:'ShowNode.do?do=getNodeAsJson&node_id='+node.attributes.node_id,
	        method : 'GET',
	        waitMsg: messageLoadingKey
	    });
	}

    viewport.doLayout();
    
}

function deselectContent() {
	
	nothingSelected = true;
    
	document.getElementById("id").value = 0;
    tinyMCE.execCommand('mceRemoveControl',false,'contentstring');
    document.getElementById("contentstring").value = noContentSelectedKey;
    tinyMCE.execCommand('mceAddControl',false,'contentstring');

    Ext.getCmp('contentname').setValue('');
    Ext.getCmp('description').setValue('');
    Ext.getCmp('pagename').setValue('');
    Ext.getCmp('commentsAllowed').setValue(false);
    Ext.getCmp('metatags').setValue('');
    
    Ext.getCmp('contentname').setDisabled(true);
    Ext.getCmp('description').setDisabled(true);
    Ext.getCmp('pagename').setDisabled(true);
    Ext.getCmp('commentsAllowed').setDisabled(true);
    Ext.getCmp('metatags').setDisabled(true);
    
    Ext.getCmp('previewItem').setDisabled(true);
    Ext.getCmp('metatagsItem').setDisabled(true);
    
}