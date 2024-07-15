var timeout;
var progressBarValue=0;

function init() {

	$( "#progressbar" ).progressbar({  
        value:0 
    });            
    $(function() {
        $("#uploadButton").button().click(function(event) {
            event.preventDefault();
            if ($("#uploadFile").val() == "" || $("#name").val() == "" || $("description").val() == "") {
            	$('#validateDialog').dialog('open');
            	return;
            }                    
            document.forms['FileUploadForm'].submit();
            submitUpload();        
        });
    }); 
    $(function() {
        $("#uploadDialog").dialog({
            autoOpen : false,
            show: 'fade', 
            hide: 'drop',
            modal: true
        });
    });
    $(function() {
        $("#validateDialog").dialog({
            autoOpen : false,
            show: 'fade', 
            hide: 'drop',
            modal: true 
        });
    });

    $(function() {
        $("#closeButton").button().click(function(event) {
        	$('#validateDialog').dialog('close');
        });
    }); 
	
}

function updateProgress(progressValue) {
	$("#progressbar").progressbar('value', progressValue);
}

function submitUpload() {
	$('#uploadDialog').dialog('open');
	timeout = setInterval("doRequest()", 100);
}

function doRequest() {

	var url = "http://localhost:8080/andamio/AsyncUpload";

	var done = false;
	
	$.get(url, {
		key : "value"
	}).done(function(xml) {
		
		var progress = $(xml).find('percent_complete').first().text();		
		
		if (progress) {
			updateProgress(parseInt(progress));
			if (progress >= 100) {
				done = true;
			}			
		}
		else {
			done = true;
		}
		
		if (done) {
			clearInterval(timeout);
			updateProgress(100);
			$('#uploadDialog').dialog('close');
			$("#uploadFile").val("");
			$("#name").val("");
			$("#description").val("");
			$("#author").val("");
			$("#license").val("");
			$("#tags").val("");
			$('#downloadable').prop('checked', false);
			$('#publicfile').prop('checked', false);
		}
		
	});

}