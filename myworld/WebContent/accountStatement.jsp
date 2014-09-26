<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link rel="stylesheet" href="js/jquery-ui-1.11.1.custom/jquery-ui.min.css" />
<link rel="stylesheet" href="css/website.css" />
<script src="js/jquery-1.11.1.min.js"></script>
<script src="js/jquery-ui-1.11.1.custom/jquery-ui.min.js"></script>
<script>




$(document).ready(function(){
	$("#fromDate").datepicker({
		dateFormat:"yy-mm-dd"
	});
	$("#toDate").datepicker({
		dateFormat:"yy-mm-dd"
	});
	
	
	function showStatement() {
		var accountNumber= <%=request.getParameter("accountNumber")%>;
		var fromDate = $("#fromDate").val();
		var toDate = $("#toDate").val();
		$.ajax({
			type: "GET",
			url: "ShowStatementServlet",
			data: {
				"accountNumber":accountNumber,
				"fromDate":fromDate,
				"toDate":toDate
				},
			dataType: "html",
			success: function(data){
				$("#statement").html(data);
				alert("Statement shown");
			},
			error: function() {
				alert("Error: Cannot show statement!");	
			}
		});
	}

	
	$("#btnStatement").click( function() {
		showStatement();
	});
} );

</script>
</head>
<body>
	<form id="statementForm">
	<p>
	   from <input type="text" id="fromDate" />
	   to <input type="text" id="toDate" />
	   <input type="button" id="btnStatement" value="Show Statement" /> 
	</p>
	</form>
	<div id="statement">
	</div>
</body>
</html>