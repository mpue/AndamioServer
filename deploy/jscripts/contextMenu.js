// replace the system context menu?
var _replaceContext = true;  
//is the mouse over the context menu?
var _mouseOverContext = false;
//disable the context menu?
var _noContext = false;  
//  context div
var _divContext;

// initialize the context menu
function initContext() {
	_divContext = document.getElementById("divContext");
	_divContext.onmouseover = function() {
		_mouseOverContext = true;
	};
	_divContext.onmouseout = function() {
		_mouseOverContext = false;
	};
	document.body.onmousedown = contextMouseDown;
	document.body.oncontextmenu = closeContext;
} // call from the onMouseDown event, passing the event if standards compliant

function contextMouseDown(event) { 
	if (_noContext || _mouseOverContext) 
		return; // IE is evil and doesn't pass the event object 
	if (event == null) 
		event = window.event; // we assume we have a standards compliant browser, but check if we have IE
	// only show the context menu if the right mouse button is pressed 
	// and a hyperlink has been clicked (the code can be made more selective)
		var target = event.target != null ? event.target : event.srcElement;  																			 
	if (event.button == 2 && (target.tagName.toLowerCase() == 'div' || target.tagName.toLowerCase() == 'a')) _replaceContext = true; 
	else if (!_mouseOverContext) _divContext.style.display = 'none'; 
} 

// hide context menu
function closeContext() { 
	_mouseOverContext = false;
	// call from the onContextMenu event, passing the event 
	_divContext.style.display = 'none'; 
} 

// if this function returns false, the browser's context menu will not show up 
function closeContext(event) {
	if (_noContext || _mouseOverContext) 
		return; // IE is evil and doesn't pass the event object 
	if (event == null) event = window.event; 
	// we assume we have a standards compliant browser, but check if we have IE 
	var target = event.target != null ? event.target : event.srcElement; 
	if (_replaceContext) { 
		var scrollTop = document.body.scrollTop ? document.body.scrollTop : document.documentElement.scrollTop; 
		var scrollLeft = document.body.scrollLeft ? document.body.scrollLeft : document.documentElement.scrollLeft; // hide the menu first to avoid an "up-then-over" visual effect 
		_divContext.style.display = 'none'; 
		_divContext.style.left = event.clientX + scrollLeft + 'px'; 
		_divContext.style.top = event.clientY + scrollTop + 'px'; 
		_divContext.style.display = 'block'; 
		_divContext.style.zIndex = 4;
		_replaceContext = false; return false; 
	} 
} 
