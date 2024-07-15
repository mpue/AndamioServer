/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var room = 
{
  _last: "",
  _username: null,
  _chatTopic: "topic://CHAT.LOBBY",
  
  join: function()
  {
    var name = $('username').value;
    if (name == null || name.length==0 )
    {
      alert('Please enter a username!');
    }
    else
    {
       this._username=name;
       
       amq.addListener('chat',this._chatTopic,room._chat);
       $('join').className='hidden';
       $('joined').className='';
       $('phrase').focus();
       Behaviour.apply();
       amq.sendMessage(this._chatTopic, "<message type='join' from='" + room._username + "'/>");
       refresh();
    }
  },
  
  leave: function()
  {
       amq.removeListener('chat',this._chatTopic);
       // switch the input form
       $('join').className='';
       $('joined').className='hidden';
       $('username').focus();
       Behaviour.apply();
       amq.sendMessage(this._chatTopic, "<message type='leave' from='" + room._username + "'/>");
       refresh();
       room._username=null;
  },
  
  chat: function(text)
  {
    if (text != null && text.length>0 )
    {
        // TODO more encoding?
        text=text.replace('<','&lt;');
        text=text.replace('>','&gt;');
        amq.sendMessage(this._chatTopic, "<message type='chat' from='" + room._username + "'>" + text + "</message>");
    }
  },
  
  _chat: function(message) 
  {
     var chat=$('chat');
     var type=message.getAttribute('type');
     var from=message.getAttribute('from');
         
     switch(type)
     {
       case 'chat' :
       {
          var text=message.childNodes[0].data;
     
          if ( from == room._last )
            from="...";
          else
          {
            room._last=from;
            from+=":";
          }
     
          chat.innerHTML += "<span class=\"from\">"+from+"&nbsp;</span><span class=\"text\">"+text+"</span><br/>";
          break;
       }
       
       case 'ping' :
       {
          $('members').innerHTML+="<span class=\"member\">"+from+"</span><br/>";
	  break;
       }
       
       case 'join' :
       {
          // $('members').innerHTML="";
          if (room._username!=null) 
        	  amq.sendMessage(this._chatTopic, "<message type='ping' from='" + room._username + "'/>");
          	  chat.innerHTML += "<span class=\"alert\"><span class=\"from\">"+from+"&nbsp;</span><span class=\"text\">has joined the room!</span></span><br/>";
          refresh();
	  break;
       }
       
       case 'leave':
       {
          // $('members').innerHTML="";
          if (room._username!=null)
            amq.sendMessage(this._chatTopic, "<message type='ping' from='" + room._username + "'/>");
          chat.innerHTML += "<span class=\"alert\"><span class=\"from\">"+from+"&nbsp;</span><span class=\"text\">has left the room!</span></span><br/>";
          refresh();
	  break;
       }
     }
     
     chat.scrollTop = chat.scrollHeight - chat.clientHeight;
     
  },
  
  _poll: function(first)
  {
     if (first ||  $('join').className=='hidden' && $('joined').className=='hidden')
     {
       $('join').className='';
       $('joined').className='hidden';
       $('username').focus();
      Behaviour.apply();
     }
  }
};

amq.addPollHandler(room._poll);


var chatBehaviours = 
{ 
  '#username' : function(element)
  {
    element.setAttribute("autocomplete","OFF"); 
    element.onkeyup = function(ev)
    {  
        var keyc=getKeyCode(ev);
        if (keyc==13 || keyc==10)
        {
      	  room.join();
	  return false;
	}
	return true;
    } 
  },
  
  '#joinB' : function(element)
  {
    element.onclick = function(event)
    {
      room.join();
      return true;
    }
  },
  
  '#phrase' : function(element)
  {
    element.setAttribute("autocomplete","OFF");
    element.onkeyup = function(ev)
    {   
        var keyc=getKeyCode(ev);
           
        if (keyc==13 || keyc==10)
        {
          var text = $F('phrase');
          $('phrase').value='';
          room.chat(text);
	  return false;
	}
	return true;
    }
  },
  
  '#sendB' : function(element)
  {
    element.onclick = function(event)
    {
      var text = $F('phrase');
      $('phrase').value='';
      room.chat(text);
    }
  },
  
  
  '#leaveB' : function(element)
  {
    element.onclick = function()
    {
      room.leave();
      return false;
    }
  }
};

Behaviour.register(chatBehaviours); 

window.onbeforeunload = function(){
	room.leave();
}

function getUsers(url) {
	if (window.XMLHttpRequest) { // Non-IE browsers
		req = new XMLHttpRequest();
			
		if (req.overrideMimeType) {
           	req.overrideMimeType('text/xml');
		}
                    
      	req.onreadystatechange = readyStateChange;
      	try {
        	req.open("GET", url, true);
		} 
		catch (e) {
			alert(e);
		}

      	req.send(null);

    } 
    else if (window.ActiveXObject) { // IE
	   	req = new ActiveXObject("Microsoft.XMLHTTP");

    	if (req) {
	        req.onreadystatechange = readyStateChange;
       		req.open("GET", url, true);
	        req.send();
	    }
    }
}

function readyStateChange() {
	if (req.readyState == 4) { // Complete
    	if (req.status == 200) { // OK response        	

        	var myObject = eval('(' + req.responseText + ')');

			var users = "";
    	
    		for (var i=0; i < myObject.userNames.length;i++) {
	    		users += myObject.userNames[i];
	    		users += '<br>';
    		}		

    		document.getElementById("members").innerHTML = users;
    	
   		}
   		else {
   			alert("Scheisse passiert!");
    	}
	}
}

function refresh() {
	setTimeout('getUsers("http://localhost:8080/andamio/users/ChatAction.do?do=getUsers")',300);
}








