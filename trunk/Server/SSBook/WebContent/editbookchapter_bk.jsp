<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<head>
<title>Edit Book Chapter</title>
</head>
<body>
	
	<form action="EditBookChapter" method="POST"
		enctype="multipart/form-data">
		<table>
			<jsp:include page="EditBookChapter">
				<jsp:param name="idbookchapter" value="${param.idbookchapter}" />
			</jsp:include>
		</table>
		<input type="submit" value="Sua">
	</form>
</body>
</html>