<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<jsp:include page="head.jsp">
	<jsp:param name="title" value="Thêm sách" />
</jsp:include>
<body>
	<jsp:include page="header.jsp">
		<jsp:param name="select" value="2" />
	</jsp:include>

	<!-- start content-outer -->
	<div id="content-outer">
		<!-- start content -->
		<div id="content">


			<div id="page-heading">
				<h1>Thêm Sách</h1>
			</div>

			<%
				if (request.getParameter("success") != null) {
			%>
			<p align=center>
				<font>Thêm sách thành công. Tự chuyển trang sau
					${param.success} giây</font>
			</p>
			<%
				} else {
			%>


			<table border="0" width="100%" cellpadding="0" cellspacing="0"
				id="content-table">
				<tr>
					<th rowspan="3" class="sized"><img
						src="images/shared/side_shadowleft.jpg" width="20" height="300"
						alt="" /></th>
					<th class="topleft"></th>
					<td id="tbl-border-top">&nbsp;</td>
					<th class="topright"></th>
					<th rowspan="3" class="sized"><img
						src="images/shared/side_shadowright.jpg" width="20" height="300"
						alt="" /></th>
				</tr>
				<tr>
					<td id="tbl-border-left"></td>
					<td>
						<!--  start content-table-inner -->
						<div id="content-table-inner">

							<table border="0" width="100%" cellpadding="0" cellspacing="0">
								<tr valign="top">
									<td>
										<!-- start id-form -->
										<form action="addbook" method="POST"
											enctype="multipart/form-data">
											<table border="0" cellpadding="0" cellspacing="0"
												id="id-form">
												<tr>
													<th valign="top">Tựa sách:</th>
													<td><input type="text" name="title" class="inp-form" /></td>
													<td></td>
												</tr>
												<tr>
													<th valign="top">Tác giả:</th>
													<td><input type="text" name="author" class="inp-form" /></td>
												</tr>
												<tr>
													<th valign="top">Tóm tắt:</th>
													<td><textarea rows="" cols="" name="summary"
															class="form-textarea"></textarea></td>
													<td></td>
												</tr>
												<tr>
													<th valign="top">Chuyên mục:</th>
													<td><jsp:include page="ListCategory" /></td>
													<td></td>
												</tr>

												<tr>
													<th>Upload hình cover:</th>
													<td><input type="file" name="imagecover"
														class="file_1" /></td>
													<td>
														<div class="bubble-left"></div>
														<div class="bubble-inner">JPEG, PNG, GIF 5MB max per
															image</div>
														<div class="bubble-right"></div>
													</td>
												</tr>

												<tr>
													<th>&nbsp;</th>
													<td valign="top"><input type="submit"
														value="Chấp nhận" class="form-submit" /> <input
														type="reset" value="" class="form-reset" /></td>
													<td></td>
												</tr>
											</table>
										</form> <!-- end id-form  -->
									</td>
									<td></td>
								</tr>
								<tr>
									<td><img src="images/shared/blank.gif" width="695"
										height="1" alt="blank" /></td>
									<td></td>
								</tr>
							</table>

							<div class="clear"></div>


						</div> <!--  end content-table-inner  -->
					</td>
					<td id="tbl-border-right"></td>
				</tr>
				<tr>
					<th class="sized bottomleft"></th>
					<td id="tbl-border-bottom">&nbsp;</td>
					<th class="sized bottomright"></th>
				</tr>
			</table>

			<%
				}
			%>
			<div class="clear">&nbsp;</div>

		</div>
		<!--  end content -->
		<div class="clear">&nbsp;</div>
	</div>
	<!--  end content-outer -->

	<jsp:include page="footer.jsp" />

</body>
</html>