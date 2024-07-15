var galleryService = ctx.getBean("galleryService");
var gallery = galleryService.findGalleryByName("3D");
var images = gallery.getImages().toArray();

response += '<table border="0" cellspacing="5px" cellpadding="5px" align="center">';
response += '<tr>';

for (var i = 0;i < gallery.getImages().size(); i++) {
	
	response += '<td>'
	response += '<img src="./gallery/'+gallery.getGalleryname()+'/thumbs/thb_'+images[i].getFilename()+'"/>';
	response += '</td>'
		
	if (i % gallery.getColumns() == 0) {
		response += '</tr>';
		response += '<tr>';
	}
	
}

response += '</tr>';	
response += '</table>';

