<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Profile</title>
</head>
<body>
<h1> User Profile </h1>
Name : ${user.firstName} ${user.lastName} <br/>
Email Id : ${user.emailId} <br/>

<h1>Open Rents : </h1>
<c:forEach var="r" items="${openRents}">
	<c:choose>
		<c:when test="${r.isDueDatePassed} == true">
			<font color="red">
		</c:when>
		<c:otherwise>
			<font color="green">
		</c:otherwise>
	</c:choose> 
		<c:out value="${r.book.bookCatalog.name}"/> to be returned by <c:out value="${r.dueDate}"/> 
		<!-- return button --> <!-- current rating or rate button -->
	</font> 
</c:forEach>

<br/><a href="/watching">Watching</a>
<br/><a href="/pastRents">Past Rents</a>
<br/><a href="/ratingAndReview">My Rating and Review </a>

</body>
</html>