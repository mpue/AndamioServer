var MenuID = false;
var MenuTimer = false;

function showMenu(nr) {
  if (MenuTimer) {
     clearTimeout(MenuTimer);
     MenuTimer = false;
  }
  newMenuID = document.getElementById("submenu" + nr);
  if (MenuID && MenuID != newMenuID)
    MenuID.style.display = "none";
  MenuID = newMenuID;
  MenuID.style.display = "block";
}

function hideMenuTimeout() {
  if (MenuTimer) {
     clearTimeout(MenuTimer);
  }
  MenuTimer = setTimeout('hideMenu()',1000);
}

function hideMenu() {
  if (MenuID)
    MenuID.style.display = "none";
}

var base64s = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/';

function decode(encStr)
{
 var bits;
 var decOut = '';
 var i = 0;
 for(; i<encStr.length; i += 4)
 {
  bits = (base64s.indexOf(encStr.charAt(i))    & 0xff) <<18 |
         (base64s.indexOf(encStr.charAt(i +1)) & 0xff) <<12 |
         (base64s.indexOf(encStr.charAt(i +2)) & 0xff) << 6 |
          base64s.indexOf(encStr.charAt(i +3)) & 0xff;
  decOut += String.fromCharCode((bits & 0xff0000) >>16, 
(bits & 0xff00) >>8, bits & 0xff);
 }
 if(encStr.charCodeAt(i -2) == 61)
 {
  return(decOut.substring(0, decOut.length -2));
 }
 else if(encStr.charCodeAt(i -1) == 61)
 {
  return(decOut.substring(0, decOut.length -1));
 }
 else {return(decOut)};
}

function printmail(text) {
  document.write('<a href="mailto:'+decode(text)+'" >'+decode(text)+'</a>');
}

function hideAllChildren() {
	var elements = document.getElementsByTagName('div');
	for (var i=0;i < elements.length;i++) if(elements[i].id.indexOf('node') > -1) elements[i].style.display = 'none'; 
}

function expandNode(id) {
	hideAllChildren();
	var element = document.getElementById(id);
	element.style.display = 'block';
}

