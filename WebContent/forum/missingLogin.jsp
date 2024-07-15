<%@include file="pageheader.jsp" %>	

	<body>

	<div id="logo"></div>
	<table class="Title" align="left"> 
		<tr>
			<td align="left"  valign="middle">
				<h1 style="color:#000">Hoppla!</h1>
			</td>
		</tr>
	</table>
	<br/>

	<table class="standardT" align="center">	

		<tr>
			<td>
				<p>
					Du bist nicht angemeldet!
				</p>
				<p>
					Um Anh&auml;nge herunterladen zu k&ouml;nnen,must Du Dich mit einem 
					g&uuml;ltigen Benutzernamen anmelden. (<a class="mini" href="../Registrieren.html" target="_self">Wo bekomme ich den her?</a>) 
				</p>
				<p>
					Danke f&uuml;r Dein Verständnis
				</p>
			</td>
		</tr>
	</table>
	<br/>
	<table class="standardT" align="center">	

		<tr>
			<td align="center"  valign="middle">
				<div class="clear">
					<a class="button" href="#" onclick="this.blur();document.location.href='Mainpage.do?do=showForums';return false;"><span>Foren&uuml;bersicht</span></a>
				</div>					
			</td>
		</tr>
	</table>	


	<%@include file="pagefooter.jsp" %>
	
	</body>

</html>
		