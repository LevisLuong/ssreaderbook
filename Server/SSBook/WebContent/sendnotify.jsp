<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<jsp:include page="head.jsp">
	<jsp:param name="title" value="Thống kê" />
</jsp:include>
<body>

	<jsp:include page="header.jsp">
		<jsp:param name="select" value="4" />
	</jsp:include>

	<!-- start content-outer ........................................................................................................................START -->
	<div id="content-outer">
		<!-- start content -->
		<div id="content">

			<!--  start page-heading -->
			<div id="page-heading">
				<h1>Gửi Thông Báo</h1>
			</div>
			<!-- end page-heading -->

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
						<!--  start content-table-inner ...................................................................... START -->
						<div id="content-table-inner">
							<form action="SendAllMessage" method="post">
								<!--  start table-content  -->
								<div id="table-content">

									<div id="message-green">
										<table border="0" width="100%" cellpadding="0" cellspacing="0">
											<tbody>
												<tr>
													<%
														String pushStatus = "";
														Object pushStatusObj = request.getAttribute("status");

														if (pushStatusObj != null) {
															pushStatus = pushStatusObj.toString();
													%>
													<td class="green-left"><%=pushStatus%></td>
													<td class="green-right"><a class="close-green"><img
															src="images/table/icon_close_green.gif" alt=""></a></td>
													<%
														}
													%>
												</tr>
											</tbody>
										</table>
									</div>

									<div id="message-blue">
										<table border="0" width="100%" cellpadding="0" cellspacing="0">
											<tbody>
												<tr>
													<td class="blue-left">Gửi Thông báo cho sách
														"${param.titlebook}"</td>

													<td class="blue-right"><a class="close-blue"><img
															src="images/table/icon_close_blue.gif" alt=""></a></td>

												</tr>
											</tbody>
										</table>
									</div>



									<table width="100%" cellpadding="0" cellspacing="0">
										<tbody>
											<tr>
												<th valign="top">Nội Dung:</th>
												<td colspan="2"><textarea rows="" cols=""
														name="message" class="form-textarea"></textarea></td>
												<td>
												<td><h3>Lưu ý :</h3>
													<ul>
														<li><b>{tuasach} </b>: thay cho tựa sách. VD: "Mật mã
															tây tạng - Hà Mã"</li>
														<li><b>{you}</b> : Thay cho tên. VD: "Chào {you}" trở
															thành "Chào bạn Envy" hoặc "Chào bạn"</li>
													</ul></td>
											<tr>
											<tr>
												<th>&nbsp;</th>
												<td valign="top"><input type="hidden"
													value="${param.idbook}" name="idbook" /> <input
													type="submit" value="Chấp nhận" class="form-submit" /> <input
													type="reset" value="" class="form-reset" /></td>
												<td></td>
											</tr>
										</tbody>
									</table>
								</div>
							</form>
							<!--  end table-content  -->
							<div class="clear"></div>
						</div> <!--  end content-table-inner ............................................END  -->
					</td>
					<td id="tbl-border-right"></td>
				</tr>
				<tr>
					<th class="sized bottomleft"></th>
					<td id="tbl-border-bottom">&nbsp;</td>
					<th class="sized bottomright"></th>
				</tr>
			</table>
			<div class="clear">&nbsp;</div>

		</div>

		<div class="clear">&nbsp;</div>

	</div>
	<!--  end content -->
	<div class="clear">&nbsp;</div>
	</div>
	<!--  end content-outer........................................................END -->

	<jsp:include page="footer.jsp" />

</body>
</html>