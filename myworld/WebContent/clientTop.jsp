<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>clientTop Banner</title>
<link rel="stylesheet" href="css/website.css"/>
<script src="js/jquery-1.11.1.min.js"></script>
<script>
$(document).ready( function() {
	var username = '<%=session.getAttribute("username") %>';
	$('#username').html(username);
});
</script>
<style type="text/css">
    body {
        background-color: #00E68A
    }
</style>
</head>
<body>
    <h3 align="right">
        Welcome,<span id="username">?</span> !
        <a href="LogoutServlet" target="_top">logout</a>
    </h3>
</body>
</html>