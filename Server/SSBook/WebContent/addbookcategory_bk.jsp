<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<head>
<title>Add Book</title>
</head>
<body>
	
	<a href="managebookcategory.jsp"> <--- Quan ly danh muc</a>
	<form action="AddBookCategory" method="POST">
		<table>
			<tr>
				<td>Ten danh muc</td>
				<td><input type="text" name="title"></td>
			</tr>

		</table>
		<input type="submit" value="Them">
	</form>
</body>
</html>