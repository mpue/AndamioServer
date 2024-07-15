<%@include file="../pageheader.jsp" %>

   <html:html locale="true">
   <head>

   <title><page:config property="sitename"/>An error occured</title>
	    <html:base/>
		<link rel="stylesheet" href="/<page:config property="localPath"/>/admin/style.css" type="text/css">
   </head>


	<body>

		<table width="750px" class="setup">
	       <tr height="30px">
	           <td align="center" colspan="2">
					&nbsp;               
	           </td>
	       </tr>
	       <tr height="15px">
	           <td class="text_center" colspan="2">
	               &nbsp;           
	           </td>       
	       </tr>       
	       <tr height="50px">
	           <td class="text_center" colspan="2">
					<img src="/<page:config property="localPath"/>admin/images/64x64_error.png" border="0" align="middle">	                          
	           </td>       
	       </tr>
	       <tr height="15px">
	           <td class="text_center" colspan="2">
	               &nbsp;           
	           </td>       
	       </tr>
			<tr height="30px">
				<td class="text_center" colspan="2">
					<h2><page:config property="sitename"/> - Critical Error</h2>
				</td>
			</tr>
			<tr>
				<td class="text_left" colspan="2">
					<p>Dear user,</p>
					
					<p>unfortunately an error occured, because of that your request can not be processed.</p>
					<p>
					Please try to reload this page with the <b>F5</b> key. 
					</p>
					<p>
					If the error still occurs, contact the administrator of this site and submit the error text below.
					</p>

					<p>We apologize any inconvenience.</p>
					<p><br></p>
				</td>			
			<tr>
				<td colspan="2" class="text_left">
				
				<p class="title_error">Original message :</p>
					<% String appName = getServletContext().getServletContextName(); %>
					
					
					<p class="title_error"><%= appName %>: Error</p>

					<p class="text_error">									
					Unhandled Exception (<%=
					request.getAttribute("javax.servlet.error.status_code") %>):
					<%= request.getAttribute("javax.servlet.error.exception_type") %>
					</p>
					
					<p class="title_error"><%=
					request.getAttribute("javax.servlet.error.message") %></p>
					
					<p class="text_error">occured while accessing: <%=
					request.getAttribute("javax.servlet.error.request_uri") %>
					(servlet: <%= request.getAttribute("javax.servlet.error.servlet_name")
					%>)</p>
					 <hr size="1">									
					<p class="text_error">
					<%
					Throwable e =
					(Throwable)request.getAttribute("javax.servlet.error.exception");
					StackTraceElement[] stack = e.getStackTrace();
					for(int n = 0; n < Math.min(15, stack.length); n++) { %>
					 <%= stack[n].toString() %>
					<% }
					
					if(e instanceof ServletException)
					 e = ((ServletException)e).getRootCause();
					else
					 e = e.getCause();
					
					if(e != null) { %>
					</p>
					<p class="text_error">
														
					<b>root cause:</b> [<%= e.getClass().getName() %>] <%=
					e.getMessage() %>
					 <% stack = e.getStackTrace();
					 for(int n = 0; n < Math.min(15, stack.length); n++) { %>
					  <%= stack[n].toString() %>
					 <% }
					} %>
					</p>
					<p><hr size="1"></p>					
				</td>
			</tr>
		
			<tr height="50px">
				<td class="text_center" colspan="2">
					<table class="Title" align="center">
						<tr>
							<td align="center">
								<html:link href="javascript:history.back()">
									<html:button styleClass="bs2" property="">Back</html:button>
								</html:link>
							</td>		
							<td align="center">
								<html:link href="">
									<html:button styleClass="bs2" property="" onclick="document.location.reload()">Reload</html:button>
								</html:link>
							</td>		
							<td align="center">
								<a href="/<page:config property="localPath"/>index.jsp">
									<html:button styleClass="bs2" property="">Main page</html:button>
								</a>
							</td>												
						</tr>
					</table>
				</td>			
			</tr>
		</table>
   </body>
</html:html>
