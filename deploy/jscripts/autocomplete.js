function initAutoComplete() {
	new Ajax.Autocompleter("search", "autocomplete_choices", "/##LOCALPATH##Mainpage.do?do=autoComplete", {
		  paramName: "search", 
		  minChars: 2, 
	});				
}

			