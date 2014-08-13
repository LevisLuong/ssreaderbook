<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Quan ly SSBOOk</title>
<link href='http://fonts.googleapis.com/css?family=Oxygen'
	rel='stylesheet' type='text/css'>
<style type="text/css">
#tablebook {
	font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
	width: 100%;
	border-collapse: collapse;
}

#tablebook td, #tablebook th {
	font-size: 1em;
	border: 1px solid #98bf21;
	padding: 3px 7px 2px 7px;
}

#tablebook th {
	font-size: 1.1em;
	text-align: center;
	padding-top: 5px;
	padding-bottom: 4px;
	background-color: #A7C942;
	color: #ffffff;
}

#tablebook tr.alt td {
	color: #000000;
	background-color: #EAF2D3;
}

body {
	text-align: center;
}

.container {
	margin-left: auto;
	margin-right: auto;
	width: 100%;
}

#div0 {
	display: table-cell;
	width: 50%;
}

#div1 {
	float: left;
	text-align: center;
	display: table-cell;
	background-color: #9C8DEB
}

#div2 {
	float: left;
	text-align: center;
	display: table-cell;
	background-color: #41EC88
}

#div3 {
	width: 361px;
	overflow: auto;
	background-color: yellow;
}
</style>

</head>
<body class="container">
	
	<h1>Quan ly chapter</h1>
	<p>
		<a href="managebook.jsp">Quan ly sach</a>
	</p>
	<p>
		<a href="addbookchapter.jsp?idbook=${param.idbook}">Them chap</a>
	</p>

	<div id="tablediv">
		<table id="tablebook">
			<tr>
				<th scope="col">id chapter</th>
				<th scope="col">Chapter</th>
				<th scope="col">filename</th>
				<th scope="col">Chuc nang</th>
			</tr>
			<jsp:include page="TableListBookChapter" />
		</table>
	</div>

</body>
</html>