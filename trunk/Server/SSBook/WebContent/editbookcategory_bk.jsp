<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<head>
<title>Edit Book Category</title>
</head>
<body>
	
	<a href="managebookcategory.jsp"> <--- Quan ly danh muc</a>
	<form action="EditBookCategory" method="POST">
		<table>
			<jsp:include page="EditBookCategory">
				<jsp:param name="idbook" value="${param.idbook}" />
			</jsp:include>
		</table>
		<input type="submit" value="Sua">
	</form>
</body>
</html>