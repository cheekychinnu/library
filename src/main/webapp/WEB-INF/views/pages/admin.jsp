<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Admin</title>
</head>
<body>
${accessError}

<h1>Add a BookCatalog : </h1>
<div class="container">
		${addBookCatalogMessage}
		<div class="form-group form">
			<form:form name="input" method="post" action="admin/addBookCatalog"
				modelAttribute="bookCatalog">
				<form:errors path="name" />
		Name  : <form:input type="text" path="name" />
				<br />
				<form:errors path="author" />
		Author : <form:input type="text" path="author" />
				<br />
				<form:errors path="isbn" />
		ISBN : <form:input type="text" path="isbn" />
				<br />
				<input type="submit" value="Add To Catalog" />
			</form:form>
		</div>
	</div>

</body>
</html>