<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="/WEB-INF/views/include/head-include.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Register</title>
</head>
<body>
	${registrationError}
	<div class="container">
		<form:form name="input" method="post" action="register"
			modelAttribute="user">
			<form:errors path="id" />
		User ID  : <form:input type="text" path="id" />
			<br />
			<form:errors path="password" />
		Password  : <form:input type="password" path="password" />
			<br />
			<form:errors path="emailId" />
		Email     : <form:input type="text" path="emailId" />
			<br />
			<form:errors path="firstName" />
		First Name: <form:input type="text" path="firstName" />
			<br />
			<form:errors path="lastName" />
		Last Name : <form:input type="text" path="lastName" />
			<br />

			<input type="submit" value="Register" />
		</form:form>
	</div>
</body>
</html>