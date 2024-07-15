<!--
    Licensed to the Apache Software Foundation (ASF) under one or more
    contributor license agreements.  See the NOTICE file distributed with
    this work for additional information regarding copyright ownership.
    The ASF licenses this file to You under the Apache License, Version 2.0
    (the "License"); you may not use this file except in compliance with
    the License.  You may obtain a copy of the License at
   
    http://www.apache.org/licenses/LICENSE-2.0
   
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
-->
<%@ taglib uri="/tags/pagetags" prefix="page" %>
<html>
<head>
<title>Chat</title>
<script type="text/javascript" src="/<page:config property="localPath"/>chat/amq/amq.js"></script>
<script type="text/javascript">amq.uri='amq';</script>
<script type="text/javascript" src="chat.js"></script>
<link rel="stylesheet" href="chat.css" type="text/css">
<link rel="stylesheet" href="style.css" type="text/css">

<script type="text/javascript">
</script>


</head>
<body>

<h1>Chat</h1>
Welcome to the lobby
<br>
<br>



<div id="chatroom">
	<div id="chat"></div>

	<div id="members"></div>

	<div id="input">
		<div id="join" class="hidden">Username:&nbsp;<input id="username" type="text" value="<%= request.getSession().getAttribute("login") %>"/>
			<input id="joinB" class="button" type="submit" name="join" value="Join" />
		</div>
	        <div id="joined" class="hidden">Chat:&nbsp;<input id="phrase" type="text"></input> 
	        		<input id="sendB" class="button" type="submit" name="join" value="Send" />
	        		<input id="leaveB" class="button" type="submit" name="join" value="Leave" />
	        	</div>
	</div>
</div>
<div id="memberList">
</div>

</body>
</html>
