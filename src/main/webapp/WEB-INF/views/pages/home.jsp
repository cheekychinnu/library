<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Library</title>
</head>
<body>
	${welcomeMessage} <br/>

	<c:choose>
		<c:when test="${empty loggedInUser}"> <br/>
			<a href="/register">Register</a> <br/>
			<a href="/login">Login</a> <br/>
		</c:when>
		<c:when test="${loggedInUser.id =='admin'}">
			<a href="/admin">Admin</a> <br/>
			<a href="/logout">Logout</a> <br/>
		</c:when>
		<c:otherwise>
		Hello ${loggedInUser.firstName}! <br/>
		<a href="/user">View Profile</a> <br/>
		<a href="/books/getAllBooks">View Library</a> <br/>
		<a href="/logout">Logout</a> <br/>
		</c:otherwise>
	</c:choose>
</body>
</html>