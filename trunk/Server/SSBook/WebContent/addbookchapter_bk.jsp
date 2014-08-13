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
	
	<a href="managebookchapter.jsp?idbook=${param.idbook}"> <--- Quan
		ly chap</a>
	<form action="AddBookChapter" method="POST"
		enctype="multipart/form-data">
		<table>
			<tr>
				<td>Chapter:</td>
				<td><input type="text" name="chapter"></td>
			</tr>
			<tr>
				<td>upload book:</td>
				<td><input type="file" name="filename"></td>
			</tr>
		</table>
		<input type=hidden name=idbook value='${param.idbook}'> <input
			type="submit" value="Them">
	</form>
</body>
</html>