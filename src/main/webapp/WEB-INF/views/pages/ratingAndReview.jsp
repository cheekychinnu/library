<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Rating and Reviews</title>
</head>
<body>
	<h1>Your Reviews and Ratings</h1>

	<table border="1">
		<tr>
			<th>Book</th>
			<th>Author</th>
			<th>ISBN</th>
			<th>Rating</th>
			<th>Review</th>
		</tr>

		<c:forEach var="r" items="${ratingAndReviewsForUser}">

			<td><c:out value="${r.bookCatalog.name}" /></td>
			<td><c:out value="${r.bookCatalog.author}" /></td>
			<td><c:out value="${r.bookCatalog.isbn}" /></td>
			<td><form:form method="post" action="/books/rating"
					modelAttribute="ratingAndReview">
					<form:hidden path="id.bookCatalogId" value="${r.bookCatalog.id}" />
					
					<c:forEach var="i" items="${possibleRatings}">
						<c:choose>
							<c:when test="${not empty r.rating and i==r.rating }">
								<input type="radio" name="rating" value="${i}" checked> ${i}<br>							
							</c:when>
							<c:otherwise>
								<input type="radio" name="rating" value="${i}"> ${i}<br>
							</c:otherwise>
						</c:choose>
					</c:forEach>
					<br/>
					<input type="submit" value="Update Rating" />
					<c:if test="${ratingResult[r.bookCatalog.id] == true}">
						<br/>Updated
					</c:if>
				</form:form></td>
			<td><form:form method="post" action="/books/review"
					modelAttribute="ratingAndReview">
					<form:hidden path="id.bookCatalogId"
						value="${r.bookCatalog.id}" />
						<textarea name="review" rows="10" cols="100">${r.review}</textarea><br/>
					<input type="submit" value="Update Review" />
					<c:if test="${reviewResult[r.bookCatalog.id] == true}">
						<br/>Updated
					</c:if>
				</form:form></td>
		</c:forEach>
	</table>

</body>
</html>