var galleryService = ctx.getBean("galleryService");
var gallery = galleryService.findGalleryByName(initParams);

if (gallery != null) {

	var images = gallery.getImages().toArray();
	
	response += '<table border="0" cellspacing="5px" cellpadding="5px" align="center">';
	response += '<tr>';
	
	for (var i = 0;i < gallery.getImages().size(); i++) {
		
		response += '<td align="center">';
		response += '<a href="./gallery/'+gallery.getGalleryname()+'/'+images[i].getFilename()+'" target="_blank"/>';
		response += '<img src="./gallery/'+gallery.getGalleryname()+'/thumbs/thb_'+images[i].getFilename()+'" border="0"/>';
		response += '</a>';
		response += '<br>';
		response += '<div align="center">'+images[i].getName()+'</div>';
		response += '</td>';
			
		if ((i + 1) % gallery.getColumns() == 0) {
			response += '</tr>';
			response += '<tr>';
		}
		
	}
	
	response += '</tr>';	
	response += '</table>';
	
}
else {
	response = "The gallery named &quot;"+initParams+"&quot; is not available.";
}
	