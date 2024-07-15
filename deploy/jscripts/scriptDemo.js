importPackage(org.pmedv.core.html);

var response = "";
var userScript = new Object(); userScript.getResponse = function(name) { return response; }

var contentList = daos.getNodeDAO().findAllItems();
var username = session.getAttribute("login");

var table = new Table();
var row = new TableRow();
var column = new TableColumn();

table.setBorder(true);

column.setData("Hallo");			
row.addColumn(column);
table.addRow(row);

response += 'logged in as :'+ username + '<br>';

if (username == 'admin') {

	for (var i=0; i < contentList.size();i++) {		
		response += '<a href="'+contentList.get(i).getPath()+'.html">'+contentList.get(i).getName()+'</a>';
		response += '<br>';	
	}
	
}
else {	
	response += table.render();	
}

response += "<br>";

var i=0;

var it = parameters.keySet().iterator();

while (it.hasNext()) {
	var key = it.next();
	var value = parameters.get(key);
	response += key +":"+value;
	response += "<br>"; 	
}




