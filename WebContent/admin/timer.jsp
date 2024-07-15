<%@page import="org.pmedv.session.UserSession"%>
<%
	UserSession _session = new UserSession();
	_session.init(false,request);
	_session.getAttributes();
%>


<script type='text/javascript'> 

var remainingTime = 15;

function time() {
	remainingTime--;
	if (remainingTime <=1)
		document.location.href=('Logout.do?do=logout&target=Adminpage&username=<%= _session.getLogin() %>');
 	else
	 	document.TimerForm.time.value=remainingTime;	
}


setInterval('time()',1000);

</script>
<form name="TimerForm">
	Sekunden bis zum Logout : <input name="time" class="bs1" size="6" readonly="readonly" />
</form>

