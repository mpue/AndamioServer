var html;
var data;
var userdata;

function getCountries() {

	var url = 'http://##HOSTNAME##/##LOCALPATH##CountryAction.do?do=getCountries';

	new Ajax.Request(url, {
		method: 'get',
		onSuccess: function(transport) {
	    	data = transport.responseText;
	    	processCountries();
	  	}
	});
	
}

function processCountries() {
	
	var countries = eval(data); 
	
	var select = document.getElementsByTagName('select')[0];
	
	select.options.length = 0; // clear out existing items
	
	select.options.add(new Option("Please select", 0))
	
	for(var i=0; i < countries.length; i++) {
	    var d = countries[i];
	    select.options.add(new Option(d.name, d.id))
	}
	
}

function getUsers(countryId) {
	
	if (countryId == '0') {
		userdata = '[]';
		processUsers();
		return;
	}

	var url = 'http://##HOSTNAME##/##LOCALPATH##CountryAction.do?do=getUsers&country_id='+countryId;

	new Ajax.Request(url, {
		method: 'get',
		onSuccess: function(transport) {
	    	userdata = transport.responseText;
	    	processUsers();
	  	}
	});
	
}

function processUsers() {
	
	var users = eval(userdata); 	

	var contacts = '';
		
	for(var i=0; i < users.length; i++) {
	    
		var d = users[i];
	    
		contacts += '<b>'
	    contacts += users[i].lastName;
	    contacts += ',';
	    contacts += users[i].firstName;
	    contacts += '</b>'	    	
	    contacts += '<br>';
	    contacts += 'Telephone : ' + users[i].telefon + '<br>'; 
	    contacts += 'Mail      : <a href="mailto:'+users[i].email+'">'+users[i].email+'</a>'
	    contacts +='<br><br>';
	    
	}

	if (users.length == 0)
		contacts = "No contacts found.";

	var info = document.getElementById('users');
	info.innerHTML = contacts;
	
}

function processSelection(selection) {
	getUsers(selection);
	processUsers();
}
