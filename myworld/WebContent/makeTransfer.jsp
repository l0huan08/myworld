<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script src="js/jquery-1.11.1.min.js"></script>
<script>

function getInfo(){
	var accountNumber = <%=request.getParameter("accountNumber")%>;
	$("#fromAccount").val(accountNumber);
	
	$.ajax({
		   type: "GET",
		   url: "GetTransferToAccountsServlet",
		   dataType: "html",
		   data:{"accountNumber":accountNumber},
		   success: function(data) {
			   if (data) {
				   $("#toAccount").html(data);
			   } else {
				   alert("Cannot Read Account from Server!");
				   return false;
			   }
		   },
		   error: function(jqXHR,textStatus,errorThrown) {
			   alert("Cannot get transfers from server.");
		   }	
	});
}

$(document).ready( function() {
	getInfo();

});

</script>
</head>
<body>
    <h2>Transfer Money!</h2>
    <form id="transferForm" action="MakeTransferServlet" method="post">
    	<p>
    		From:
    		<input id="fromAccount" type="hidden" name="fromAccount"/>
    	</p>
        <p>To: 
            <select id="toAccount" name="toAccount">
            <option>Nothing</option>
            </select>
        </p>
        <p>Amount:<input id="amount" type="text" name="amount"/> Dollars</p>
        <p>Memo<textarea rows="4" cols="20" name="memo"></textarea></p>
        <p>
            <input type="submit" id="submit" name="submitTransfer" value="Confirm" />
            <input type="button" id="cancel" name="submitTransfer" value="Cancel" />
        </p>
    </form>
</body>
</html>