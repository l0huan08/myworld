<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Client Account</title>
<link rel="stylesheet" href="css/website.css"/>
<script src="js/jquery-1.11.1.min.js"></script>
<script>

$(document).ready( function(){
    var username = '<%=request.getParameter("username")%>';
    $.ajax ( {
        type: "post",
        url: "ClientAccountServlet?flag="+Math.random(),
        data: {'username':username},
        dataType: "html",
        success: function(data){
            if (data){
                $("#tbAccount").html(data);
            } else {
                return false;
            }
        },
        error: function(jqXHR,textStatus,errorThrown){
        	alert("Cannot get Client Accounts!");
        }
    });

    $("#btnTransfer").click( function() {
        var accountNumber = $("input[name='selAccount']:checked","#tbAccount").val();
        if (accountNumber==null ||  accountNumber=="" ){
            alert("Please select an account!");
        	return false;
        }
        location.href="makeTransfer.jsp?accountNumber="+accountNumber;
    });
    
    $("#btnStatement").click( function(){
    	var accountNumber = $("input[name='selAccount']:checked","#tbAccount").val();
        if (accountNumber==null ||  accountNumber=="" ){
            alert("Please select an account!");
        	return false;
        }
        location.href="accountStatement.jsp?accountNumber="+accountNumber;
    } );
    
} );


</script>
</head>
<body>
    <div id="tbAccount">
    
    </div>
    <p>
        <button id="btnTransfer">Transfer Money</button>
        <button id="btnStatement">View Statement</button>
    </p>
</body>
</html>