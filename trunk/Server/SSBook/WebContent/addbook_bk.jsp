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
	
	<a href="managebook.jsp"> <--- Quan ly sach</a>
	<form action="addbook" method="POST" enctype="multipart/form-data">
		<table>
			<tr>
				<td>Title book:</td>
				<td><input type="text" name="title"></td>
			</tr>
			<tr>
				<td>Author book:</td>
				<td><input type="text" name="author"></td>
			</tr>
			<tr>
				<td>Summary book:</td>
				<td><textarea name="summary" rows="5" cols="40"></textarea></td>
			</tr>
			<tr>
				<td>Book category:</td>
				<td><jsp:include page="ListCategory" /></td>
			</tr>
			<tr>
				<td>upload cover book:</td>
				<td><input type="file" name="imagecover"></td>
			</tr>
		</table>
		<input type="submit" value="Them">
	</form>
</body>
</html>