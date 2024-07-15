var html;
var data;
var userdata;

function getCountries() {

	var url = 'http://localhost:8080/andamio/CountryAction.do?do=getCountries';

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
	
	for(var i=0; i < countries.length; i++) {
	    var d = countries[i];
	    select.options.add(new Option(d.name, d.id))
	}
	
}

function getUsers(countryId) {

	var url = 'http://localhost:8080/andamio/CountryAction.do?do=getUsers&country_id='+countryId;

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
	    contacts +='<br>';
	    contacts += '<a href="mailto:'+users[i].email+'">'+users[i].email+'</a>'
	    contacts +='<br><br>';
	}
	
	var info = document.getElementById('users');
	info.innerHTML = contacts;
	
}

function processSelection(selection) {
	getUsers(selection);
	processUsers();
}
