<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Profile</title>
</head>
<body>
	<h1>User Profile</h1>
	Name : ${user.firstName} ${user.lastName}
	<br /> Email Id : ${user.emailId}
	<br />

	<h1>Open Rents :</h1>
	<table border="1">
		<tr>
			<th>Book</th>
			<th>Author</th>
			<th>ISBN</th>
			<th>Due Date</th>
			<th>Return</th>
			<th>Rating</th>
			<th>Review</th>
		</tr>

		<c:forEach var="r" items="${openRents}">

			<c:choose>
				<c:when test="${r.isDueDatePassed == true}">
					<font color="red">
				</c:when>
				<c:otherwise>
					<font color="green">
				</c:otherwise>
			</c:choose>

			<td><c:out value="${r.book.bookCatalog.name}" /></td>
			<td><c:out value="${r.book.bookCatalog.author}" /></td>
			<td><c:out value="${r.book.bookCatalog.isbn}" /></td>
			<td><c:out value="${r.dueDate}" /></td>
			<td><a
				href="/books/return?rentId=${r.id}&bookCatalogId=${r.book.bookCatalog.id}">Return</a></td>
			<c:set var="tempRating"
				value="${bookCatalogIdToRatingAndReviewMap[r.book.bookCatalog.id]}" />
			<td><c:choose>
					<c:when
						test="${not empty tempRating and not empty tempRating.rating}">
						${tempRating.rating}
					</c:when>
					<c:otherwise>
						<form:form method="post" action="/books/rating"
							modelAttribute="ratingAndReview">
							<form:hidden path="id.bookCatalogId"
								value="${r.book.bookCatalog.id}" />
							<form:radiobuttons path="rating" items="${possibleRatings}" />
							<input type="submit" value="Rate" />
						</form:form>
					</c:otherwise>
				</c:choose></td>
			<td><c:choose>
					<c:when
						test="${not empty tempRating and not empty tempRating.review}">
						${tempRating.review}
					</c:when>
					<c:otherwise>
						<form:form method="post" action="/books/review"
							modelAttribute="ratingAndReview">
							<form:hidden path="id.bookCatalogId"
								value="${r.book.bookCatalog.id}" />
							<form:textarea path="review" />
							<input type="submit" value="Review" />
						</form:form>
					</c:otherwise>
				</c:choose></td>
			</font>
		</c:forEach>
	</table>

	<br />
	<a href="/user/watching">Watching</a>
	<br />
	<a href="/user/pastRents">Past Rents</a>
	<br />
	<a href="/user/ratingAndReview">My Rating and Review</a>

</body>
</html>