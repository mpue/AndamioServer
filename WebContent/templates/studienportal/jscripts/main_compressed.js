var messageWindow;var data;var bgImages=new Array();var currentImage=0;function expandNode(a){var b=document.getElementById(a);b.style.display='block'}bgImages[0]='url(/andamio/templates/studienportal/images/logo-studienportal_st.png)';bgImages[1]='url(/andamio/templates/studienportal/images/logo-studienportal_pills.png)';bgImages[2]='url(/andamio/templates/studienportal/images/logo-studienportal_world.png)';function setHeight(a,b){var c=document.getElementById(a);c.style.height=b}function adjustContent(){var a=window.innerHeight-228;setHeight('content',a);setHeight('page',window.innerHeight-20)}function submitEnter(a,e){var b;if(window.event)b=window.event.keyCode;else if(e)b=e.which;else return true;if(b==13){a.form.submit();return false}else return true}function changeImage(){document.getElementById('header').style.backgroundImage=bgImages[currentImage];if(currentImage<2)currentImage++;else currentImage=0}function openIOFilesList(a){getAttachments(a)}function getAttachments(b){var c='/andamio/IOFileAction.do?do=getAttachments&docId='+b;new Ajax.Request(c,{method:'get',onComplete:function(a){if(200==a.status){data=a.responseText;processAttachments(b)}else{alert("An error occured during connect.\n\nPlease check the server settings.\n\nError code : "+a.status)}}})}function processAttachments(a){var b=eval(data);var c='';for(var i=0;i<b.length;i++){var d=b[i];c+='<a href="/andamio/IOFileAction.do?do=getAttachment&docID='+a+'&attachmentID='+b[i].attachID+'&name='+escape(d.name)+'">'attachmentData+=d.name;c+="</a>";c+="<br>"}var e=document.getElementById('filelist');e.innerHTML=c;Effect.Appear('messageDiv',{duration:0.5})}function hideDiv(a){Effect.Fade(a,{duration:0.5})}function initDrag(){new Draggable('messageDiv',{scroll:window})}