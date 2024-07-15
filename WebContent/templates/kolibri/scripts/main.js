function expandNode(id) {
	
	var element = document.getElementById(id);	
	element.style.display = 'block';
	
}

function setHeight(id,height) {
	var element document.getElementById(id);
	element.style.height = height;
}

function adjustContent() {
	
	var height = window.innerHeight - 225;
	setHeight('content',height);
	
}