<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Books</title>
</head>
<body>

	<table>
		<tr>
			<th>Title</th>
			<th>Author</th>
			<th>ISBN</th>
			<th>Average Rating</th>
			<th>Your Rating</th>
			<th>Rent/Watch/Return</th>
		</tr>

		<c:forEach var="b" items="${allBookCatalogs}">
			<tr>
				<td>${b.bookCatalog.name}</td>
				<td>${b.bookCatalog.author}</td>
				<td>${b.bookCatalog.isbn}</td>
				<td>${b.bookCatalog.averageRating}</td>
				<td>${b.userRating}</td>
				<td><c:choose>
						<c:when test="${not empty b.currentOpenRent}">
							<a href="/library/books/return?rentId=${b.currentOpenRent.id}">Return</a>
						</c:when>
						<c:otherwise>
							<c:when test="${b.isAvailable} == true">
								<c:when test="${b.isAlreadyRented} == true">
									<a href="/library/books/rent?bookCatalogId=${b.bookCatalog.id}"> Rent Again </a>
								</c:when>
								<c:otherwise>
									<a href="/library/books/rent?bookCatalogId=${b.bookCatalog.id}"> Rent </a>
								</c:otherwise>
							</c:when>
							<c:otherwise>
								<a href="/library/books/watch?bookCatalogId=${b.bookCatalog.id}">Watch</a>
							</c:otherwise>
						</c:otherwise>
					</c:choose>
					<c:when test="${isDueDateMissed} == true">
						Pending Due
					</c:when>
					<c:when test="not empty ${rentResult}">
						${rentResult}
					</c:when>
					<c:when test="not empty ${watchMessage}">
						${watchMessage}
					</c:when>
				</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>