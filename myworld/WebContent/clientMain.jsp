<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="pragma" content="no-chache">
<title>Client Main</title>
<link rel="stylesheet" href="css/website.css"/>
<script src="js/jquery-1.11.1.min.js"></script>
 
<script>
$(document).ready( function() {
	var username = '<%=session.getAttribute("username") %>';
	var role = '<%=session.getAttribute("role") %>';
	var islogin = '<%=session.getAttribute("islogin") %>';
	if (islogin != "true" 
		|| username=="" 
		|| role=="") {
		alert("Your page has been out of date. Please login.");
		return false;
	}
});
</script>


</head>
<frameset rows='80,*' cols='*'>
    <frame src="clientTop.jsp">
    <frameset rows='*' cols='150,*' >
        <frame src="clientLeft.jsp">
        <frame name="mainFrame" src="clientCenter.jsp">
    </frameset>
</frameset>
<noframes><body>Your browser does not support frameset, please change your browser and try again.</body></noframes>

<%
response.setHeader("Pragma","No-cache"); 
response.setHeader("Cache-Control","no-cache"); 
response.setDateHeader("Expires", -10);
%>

</html>