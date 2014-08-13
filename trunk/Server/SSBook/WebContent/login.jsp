<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Đăng nhập</title>
<link rel="stylesheet" href="css/screen.css" type="text/css"
	media="screen" title="default" />
<link rel="icon" type="image/png" href="images/favicon.ico"/>
	<!--  jquery core -->
<script src="js/jquery/jquery-1.4.1.min.js" type="text/javascript"></script>

<!-- Custom jquery scripts -->
<script src="js/jquery/custom_jquery.js" type="text/javascript"></script>

<!-- MUST BE THE LAST SCRIPT IN <HEAD></HEAD></HEAD> png fix -->
<script src="js/jquery/jquery.pngFix.pack.js" type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$(document).pngFix();
	});
</script>
</head>
<body id="login-bg">

	<!-- Start: login-holder -->
	<div id="login-holder">

		<!-- start logo -->
		<div id="logo-login">
			<font color="white" size="42">Đăng nhập để quản lý</font>
		</div>
		<!-- end logo -->

		<div class="clear"></div>

		<!--  start loginbox ................................................................................. -->
		<div id="loginbox">
			<!--  start login-inner -->
			<%
				if (session != null) {
					// pass the request along the filter chain
					if (session.getAttribute("username") != null) {
						response.sendRedirect("managebookcategory.jsp");
					}
				}
				if (request.getParameter("error") != null) {
			%>
			<p align=center>
				<font color=red>Sai tên đăng nhập hoặc mật khẩu. Vui lòng
				nhập lại.</font>
			</p>
			<%
				}
			%>

			<div id="login-inner">
				<form action="Login" method="post">
					<table border="0" cellpadding="0" cellspacing="0">
						<tr>
							<th>Tên đăng nhập</th>
							<td><input type="text" name="user" class="login-inp" /></td>
						</tr>
						<tr>
							<th>Mật khẩu</th>
							<td><input type="password" name="pass"
								onfocus="this.value=''" class="login-inp" /></td>
						</tr>
						<tr>
							<th></th>
							<td><input type="submit" name="pass" class="submit-login" /></td>
						</tr>
					</table>
				</form>
			</div>
			<!--  end login-inner -->
			<div class="clear"></div>
		</div>
		<!--  end loginbox -->

	</div>
	<!-- End: login-holder -->
</body>
</html>