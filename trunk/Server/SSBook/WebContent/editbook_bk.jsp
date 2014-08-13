<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<head>
<title>Edit Book</title>
</head>
<body>

	<a href="managebook.jsp"> <--- Quan ly sach</a>
	<form action="EditBook" method="POST" enctype="multipart/form-data">
		<table>
			<jsp:include page="EditBook">
				<jsp:param name="idbook" value="${param.idbook}" />
			</jsp:include>
		</table>
		<input type="submit" value="Sua">
	</form>
</body>
</html>