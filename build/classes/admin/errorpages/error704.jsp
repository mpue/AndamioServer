<%@include file="../pageheader.jsp" %>    
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Unkritischer Fehler!</title>
	<link rel="stylesheet" href="./style.css" type="text/css">
	</head>
	<body>
		<table class="Title" align=center>
			<tr>
				<td class="desc_left">
					Meldung :
				</td>
			</tr>
		</table>	
		<table class="standardT" align="center">	
			<tr>
				<td align=center>
					<img src="./images/buttons/notify.png" height="70px" width="70px" border="0" align="middle">			
				</td>		
			</tr>
			<tr>			
				<td class="text_center">
					<table border="0" width="80%" align="center">
						<tr>
							<td class="text_left">
								<p>Sehr geehrter Benutzer.</p>
								
								<p>Leider ist ein unkritischer Fehler aufgetreten und Ihre Anfrage kann nicht 
								verarbeitet werden. Versuchen Sie diese Seite mit F5 neu zu laden. Sollte der Fehler weiterhin auftreten,
								so kontaktieren Sie bitte den Administrator dieser Seite.</p>

								<p>Vielen Dank f&uuml;r Ihr Verst&auml;ndnis</p>
								<p><br></p>
							 <hr size="1">								
							</td>
						</tr>
						<tr>
							<td class="text_left">
								<p class="title_error">Die Originalfehlermeldung lautete :</p>
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
									for(int n = 0; n < Math.min(5, stack.length); n++) { %>
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
									 for(int n = 0; n < Math.min(5, stack.length); n++) { %>
									  <%= stack[n].toString() %>
									 <% }
									} %>
									</p>
									<p>
										<hr size="1">	
									</p>	

							</td>
						</tr>

					</table>

				</td>		
			</tr>	
	
		</table>	
		<table class="Title" align="center">
			<tr>
				<td align="center">
					<html:link href="javascript:history.back()">
						<html:button styleClass="bs2" property="">Zur&uuml;ck</html:button>
					</html:link>
				</td>		
				<td align="center">
					<html:link href="">
						<html:button styleClass="bs2" property="" onclick="document.location.reload()">Neu laden</html:button>
					</html:link>
				</td>		
				<td align="center">
					<html:link href="./index.jsp">
						<html:button styleClass="bs2" property=""><bean:message key="button.mainmenu"/></html:button>
					</html:link>
				</td>												
			</tr>
		</table>
	
	</body>
</html>