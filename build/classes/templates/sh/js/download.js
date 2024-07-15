/**
 * The base object of a download, it hold all necessary properties
 * and is used for download the files, to create queryies and to display
 * available downloads on pages. Programmers should not create download object
 * themselves, but use the according DownloadManager, wich takes care of all 
 * downloads. 
 */
Download = Backbone.Model.extend({

	setId : function(_id) {
		this.set({
			id : _id
		});
	},

	getId : function() {
		return this.get("id");
	},
	
	
	setName : function(_name) {
		this.set({
			name : _name
		});
	},

	getName : function() {
		return this.get("name");
	},

	setPath : function(_path) {
		this.set({
			path : _path
		});
	},

	getPath : function() {
		return this.get("path");
	},
	
	
	setFilename : function(_filename) {
		this.set({
			filename : _filename
		});
	},

	getFilename : function() {
		return this.get("filename");
	},

	setRating : function(_rating) {
		this.set({
			rating : _rating
		});
	},

	getRating : function() {
		return this.get("rating");
	},
	
	setViews : function(_views) {
		this.set({
			views : _views
		});
	},

	getViews : function() {
		return this.get("views");
	},

	setDownloads : function(_downloads) {
		this.set({
			downloads : _downloads
		});
	},

	getDownloads : function() {
		return this.get("downloads");
	},
	
	
	setDescription : function(_description) {
		this.set({
			description : _description
		});
	},

	getDescription : function() {
		return this.get("description");
	},
	
	setAuthor : function(_author) {
		this.set({
			author : _author
		});
	},

	getAuthor : function() {
		return this.get("author");
	},	


	setUploadTime : function(_uploadTime) {
		this.set({
			uploadTime : _uploadTime
		});
	},

	getUploadTime : function() {
		return this.get("uploadTime");
	},
	
	setFilesize : function(_size) {
		this.set({
			filesize : _size
		});
	},

	getFilesize : function() {
		return this.get("filesize");
	}
	

});

/**
 * The model for a list of download objects
 */
var Downloads = Backbone.Collection.extend({
	model : Download
});

/**
 * A concrete DownloadList. Jold a collection of Downloads and
 * provides some convenience method to access the Download objects.
 */
DownloadList = Backbone.Model.extend({

	initialize : function() {
		this.downloads = new Downloads();
	},

	add : function(download) {
		this.downloads.add(download);
	},

	get : function(index) {
		return this.downloads.at(index);
	},

	size : function() {
		return this.downloads.length;
	}

});