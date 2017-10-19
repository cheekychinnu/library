<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Watching for</title>
</head>
<body>
	<c:forEach var="r" items="${watching}">
	
		<c:out value="${r.bookCatalog.name}"/> 
		<c:choose>
		<c:when test="${r.bookCatalog.isAvailable} == true">
			<!-- link to rent -->RENT
		</c:when>
	</c:choose>  
</c:forEach>

<br/><a href="/user">Back To Profile</a>
	
</body>
</html>