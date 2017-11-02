<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Watching for</title>
</head>
<body>
	<table border="1">
		<tr>
			<th>Book</th>
			<th>Author</th>
			<th>ISBN</th>
			<th>Rent if available</th>
		</tr>
		<c:forEach var="r" items="${watching}">
			<tr>
				<td><c:out value="${r.bookCatalog.name}" /></td>
				<td><c:out value="${r.bookCatalog.author}" /></td>
				<td><c:out value="${r.bookCatalog.isbn}" /></td>
				<td><c:if test="${r.bookCatalog.isAvailable == true}">
				<a href="/books/rent?bookCatalogId=${r.bookCatalog.id}">
				Rent
				</a>
					</c:if></td>
		</c:forEach>
	</table>
	<br />
	<a href="/user">Back To Profile</a>

</body>
</html>