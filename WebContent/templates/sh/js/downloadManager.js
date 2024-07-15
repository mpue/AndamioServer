DownloadManager = Backbone.Model.extend({

	initialize : function() {
		this.latestDownloads = new DownloadList();
		this.loadLatestDownloads(this);
	},
	
	getLatestDownloads  : function() {
		return this.latestDownloads;
	},
	
	addDownload : function (download) {
		this.latestDownloads.add(download);
	},

	openPlayer : function(url) {		
		$('#playerDialog').dialog('open');		
		$("#player").html('<iframe width="450px" height="180px" frameBorder="0" scrolling="no" src="'+url+'"><iframe>');  		
	},
	
	rate : function(id) {
		$("#ratingId").val(id);
		$('.rateSound').rating('select',0); 
		$('#ratingDialog').dialog('open');			
	},
	
	submitRating : function(_id,_value) {
		$.post("UserDownloadAction.do?do=rateSound", { id: _id, value : _value } );
	},
	
	createEntry : function(download) {
		
		var text = '';
		
        text += '<span id="show-sound-info" class="show-sound-info1">';
        text += '<table width="100%"><tr><td width="80%" align="left">';
        text += '<b>'+download.getName()+'</b>';
        text += '<br>' + download.getAuthor();
        text += '<br>' + download.getUploadTime();

        var playerUrl = 'media/player.jsp?play='+download.getFilename()+'&caption='+ 
		download.getAuthor()+'%20'+download.getName()+'&autoplay=true';
        
        text += '<br><a href="#" onclick="downloadManager.openPlayer(\'' + playerUrl + 
        		'\')">play</a> | <a href="media/' + download.getFilename() + '">download</a>' +
        		' | <a href="#" onclick="downloadManager.rate(\''+download.getId()+'\');">bewerten</a>';
        text += '</td><td width="100px" nowrap="nowrap" align="left">';
        
        text += this.appendRating(download);        	
        
        text += '<br>' + download.getViews() + ' | '+ download.getDownloads();
        text += '<br><a href="format.html">format</a>';
        text += '<br>' + (parseInt(download.getFilesize()) / 1024).toFixed(2) + 'kByte';
        text += '</td></tr></table>';
        text += '<hr size="1">';
        text += '</span>';
		
        return text;
        
	},
	
	appendRating : function(download) {
		
		var rating = "";
		
		for (var i = 1; i < 6;i++) {
			if (i == download.getRating()) {
				rating += '<input class="rating" type="radio" name="rating_'+download.getId()+'" value="'+i+'" checked="checked" disabled="disabled"/>';
			}
			else {
				rating += '<input class="rating" type="radio" name="rating_'+download.getId()+'" value="'+i+'" disabled="disabled"/>';	
			}			
		}
		
		return rating;
	},

	loadLatestDownloads : function(manager) {

		var url = "UserDownloadAction.do?do=getLatestDownloads&limit=10";

		$.getJSON(url, function(result) {			
			$.each(result, function(item) {				
				manager.addDownload(manager.parseDownload(this));			
			});
		});

	},
	
	parseDownload : function(result) {
		var d = new Download();
		d.setId(result.id);
		d.setName(result.name);
		d.setDescription(result.description);
		d.setUploadTime(result.uploadTimeFormatted);
		d.setFilesize(result.filesize);
		d.setAuthor(result.author);
		d.setFilename(result.filename);
		d.setViews(result.hits);
		d.setDownloads(result.downloads);
		d.setRating(result.ranking);
		d.setPath(result.path);
		return d;
	},
	
	listLatestDownloads : function() {
		var downloads = $("#downloads");
		for (var i = 0;i < this.latestDownloads.size();i++) {
			var d = this.latestDownloads.get(i);
			downloads.append(this.createEntry(d));
		}
		$(".rating").rating();
	},
	
	getDownloads : function() {

		var url = "UserDownloadAction.do?do=getDownloads";

		$.getJSON(url, function(result) {
			var downloads = $("#downloads");
			$.each(result, function(item) {
				downloads.append('<tr class="download"' + ' id="' + this.id + '"><td>'+this.name+'</td><td>'+this.description+'</td></tr>>');
			});
		});

	},	
	
	searchDownloads : function(author,name) {

		var url = "UserDownloadAction.do?do=searchDownloads&author="+author+"&name="+name;

		$.getJSON(url, function(result) {
			var searchResults = $("#AUSTAUSCHER");
			searchResults.html("");
			$("#AUSTAUSCHER").append('<button id="newSearch">Neue Suche</button>');
			$("#newSearch").button();
			$("#newSearch").click(function(e) {
				page.searchPage();
			});				
			$.each(result, function(item) {				
				var download = downloadManager.parseDownload(this);				
				searchResults.append(downloadManager.createEntry(download));
			});
			$("#AUSTAUSCHER").show();
			$("#search").hide();
			$(".rating").rating();
		});

	}	

});