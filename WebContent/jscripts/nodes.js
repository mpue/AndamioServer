function getNode(id) {

	var url = 'http://##HOSTNAME##/##LOCALPATH##admin/ShowNode.do?do=getNodeAsJson&node_id='+id;

	new Ajax.Request(url, {
		method: 'get',
		onSuccess: function(transport) {
			var node = transport.responseText.evalJSON();
			loadNodeCallback(node);
	  	}
	});
	
}
