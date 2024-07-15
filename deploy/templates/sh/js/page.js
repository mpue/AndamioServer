Page = Backbone.Model.extend({
	
	/**
	 * Init function of the page object
	 */
	initialize : function() {
		
		this.initializeMenu();
	
		this.titles = ['Gib uns Deinen Sound',
		               'Die Welt klingt nach Dir',
		               'Sounds hochladen - einfach geil!',
		               'Besser hochladen, als vergammeln lassen!',
		               'Sounds hochladen, wir sind die geilsten!',
		               'Wenn nicht ich wer dann?',
		               'Wenn nicht jetzt, wann dann?',
		               'Her mit dem Sound!'];
		
		$("#left-ui-button").click($.proxy(function(e) {
			this.runEffect();
			this.moveIt(document.getElementById('main-window'), '35px', '235px');
			$('#main-window').removeAttr('style');
			return false;			
	    }, this));
		
		$("#left-ui-button2").click($.proxy(function(e) {
			this.runEffect();
			this.moveIt(document.getElementById('main-window'), '35px', '10px');
			$('#main-window').animate({
				width : '99%'
			}, '1000');
			return false;			
	    }, this));
		
		$("#searchButton").button();
		
		$("#searchButton").click($.proxy(function(e) {
			this.submitSearch();
			return false;			
	    }, this));	
				
	},
	
	loadStartpage : function() {		
		$("#AUSTAUSCHER").html('<iframe width="100%" height="800px" frameBorder="0" scrolling="no" src="upload/asyncUpload.jsp"><iframe>');
		var random = Math.floor(Math.random() * this.titles.length);
		var title = this.titles[random];
		document.title = title;	
		$("#search").hide();
		$("#AUSTAUSCHER").show();
	},
	
	/**
	 * Load a page asnchronuosly from a specific url
	 * 
	 * @param url
	 */
	loadPage : function(url) {

		$.get(url, {}).done(function(xml) {
			var content = $(xml).find('contentstring').first().text();
			var pagename = $(xml).find('pagename').first().text();
			document.title = pagename;
			$('#AUSTAUSCHER').html(content);
			$('#main-content-title').html(pagename);
			$("#search").hide();
			$("#AUSTAUSCHER").show();
		});
		
	},
	
	searchPage : function() {
		$("#AUSTAUSCHER").hide();
		$('#main-content-title').html("Sounds hochladen durchsuchen");
		$("#search").show();		
	},
	
	submitSearch : function() {		
		var author = $("#searchAuthor").val();
		var name = $("#searchName").val();
		downloadManager.searchDownloads(author,name);		
	},
	
	/**
	 * Helper function
	 * 
	 * @param obj
	 * @param mvTop
	 * @param mvLeft
	 */
	
	moveIt : function(obj, mvTop, mvLeft) {		
		obj.style.position = "absolute";
		obj.style.top = mvTop;
		obj.style.left = mvLeft;
	},
	
	/**
	 * Slide the menu
	 */
	runEffect : function() {
		// run the effect
		$("#left-ui").toggle("slide", 500);
	},
	
	/**
	 * Initialize the main menu of the page
	 */
	initializeMenu : function() {
		$('.upload-button').click($.proxy(function(e) {
			this.loadStartpage();
	    }, this));		
		$('.suggest-button').click($.proxy(function(e) {
			this.loadPage("suggest.xml");
	    }, this));		
		$('.rates-button').click($.proxy(function(e) {
			this.loadPage("rates.xml");
	    }, this));		
		$('.latest-button').click($.proxy(function(e) {
			this.loadPage("latest.xml");
	    }, this));		
		$('.views-button').click($.proxy(function(e) {
			this.loadPage("views.xml");
	    }, this));		
		$('.dlhits-button').click($.proxy(function(e) {
			this.loadPage("dlhits.xml");
	    }, this));		
		$('.format-button').click($.proxy(function(e) {
			this.loadPage("format.xml");
	    }, this));		
		$('.categorie-button').click($.proxy(function(e) {
			this.loadPage("category.xml");
	    }, this));		
		$('.autor-button').click($.proxy(function(e) {
			this.loadPage("author.xml");
	    }, this));		
		$('.search-button').click($.proxy(function(e) {
			this.searchPage();
	    }, this));		
	}, 

	/**
	 * getters and setters for the page properties
	 * 
	 */
	
	setName : function(_name) {
		this.set({
			 name : _name
		});
	},
	
	getName : function() {
		return this.get("name");
	},	
	
	setTitle : function(_title) {
		this.set({
			 title : _title
		});
	},
	
	getTitle : function() {
		return this.get("title");
	}	
	
});
