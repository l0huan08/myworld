<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
session.setAttribute("username", "");
session.setAttribute("role", "");
session.setAttribute("islogin", "");
%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="pragma" content="no-chache">
<title>Login</title>
<link rel="stylesheet" href="css/website.css"/>
<script src="js/jquery-1.11.1.min.js"></script>
<script>
$(document).ready( function() {
	$('#loginForm').submit( function() {
	    var username = $.trim($('#username').val());
	    var password = $.trim($('#password').val());
	    if (username=="") {
	    	  alert("username is null!");    
	    	  $('username').focus();
	    	  return false;
		}
	    else if (password==""){
	    	  alert("password is null!");
	    	  $('password').focus();
	    	  return false;
		}
	});
});
</script>

<title>Insert title here</title>

</head>
<body>

<header>
  Welcome To Big World!
</header>

<form id="loginForm" action="LoginServlet" method="post">
    <fieldset>
        <legend>User Info</legend>
        <p>
            username
            <input type="text" id="username" name="username" placeholder="username"/><br>
        </p>
        <p>
            password
            <input type="text" id="password" name="password" placeholder="password"/><br>
        </p>
        <p>
            <input type="submit" value="login"/>
            <input type="reset" value="reset"/>
        </p>
    </fieldset>
</form>

<footer>
  Copyright@ 2014.9.24 Li Huang
</footer>

</body>


<%
response.setHeader("Pragma","No-cache"); 
response.setHeader("Cache-Control","no-cache"); 
response.setDateHeader("Expires", -10);
%>

</html>