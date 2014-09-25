<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<jsp:include page="head.jsp">
	<jsp:param name="title" value="Quản lý sách" />
	<jsp:param name="sorttable" value="no" />
</jsp:include>

<script type="text/javascript">
	function confirmSubmit(id) {
		if (confirm("Bạn có chắc muốn xóa sách này?")) {
			window.location = "DeleteBook?idbook=" + id;
		}
		return false;
	}
</script>
<body>

	<jsp:include page="header.jsp">
		<jsp:param name="select" value="2" />
	</jsp:include>

	<!-- start content-outer ........................................................................................................................START -->
	<div id="content-outer">
		<!-- start content -->
		<div id="content">

			<!--  start page-heading -->
			<div id="page-heading">
				<h1>Quản lý sách</h1>
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

							<!--  start table-content  -->
							<div id="table-content">
								<!--  start message-red -->
								<div id="message-red">
									<table border="0" width="100%" cellpadding="0" cellspacing="0">
										<tr>
											<td class="blue-left">Thêm sách: <a href="addbook.jsp">Ấn
													vào đây để thêm sách mới</a></td>
											<td class="blue-right"><a class="close-blue"><img
													src="images/table/icon_close_blue.gif" alt="" /></a></td>
										</tr>
									</table>
								</div>
								<!--  end message-red -->
								<!--  start product-table ..................................................................................... -->
								<form id="mainform" action="">
									<table border="0" width="100%" cellpadding="0" cellspacing="0"
										id="product-table">
										<thead>
											<tr>
												<%
													String key = request.getParameter("key") == null ? "" : request
															.getParameter("key");
													int typesearch = 1;
													if (request.getParameter("typesearch") != null) {
														try {
															typesearch = Integer.parseInt(request
																	.getParameter("typesearch"));
														} catch (Exception e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}
													}
													int category = 0;
													if (request.getParameter("category") != null) {
														try {
															category = Integer.parseInt(request
																	.getParameter("category"));
														} catch (Exception e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}
													}
													int typeOrder = 1;
													if (request.getParameter("order") != null) {
														try {
															typeOrder = Integer.parseInt(request.getParameter("order"));
														} catch (Exception e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}
													}

													request.setAttribute("key", key);
													request.setAttribute("category", category);
													request.setAttribute("order", typeOrder);
													request.setAttribute("typesearch", typesearch);
												%>
												<th class="table-header-repeat line-left"><a
													href="managebook.jsp?&key=${key}&typesearch=${typesearch}&category=${category}&order=1"><span>Id</span></a></th>
												<th class="table-header-repeat line-left minwidth-1"><a
													href="managebook.jsp?&key=${key}&typesearch=${typesearch}&category=${category}&order=2"><span>Tựa
															sách</span></a></th>
												<th class="table-header-repeat line-left minwidth-1"><a
													href="managebook.jsp?&key=${key}&typesearch=${typesearch}&category=${category}&order=3"><span>Tác
															giả</span></a></th>
												<th class="table-header-repeat line-left"><a
													href="managebook.jsp?&key=${key}&typesearch=${typesearch}&category=${category}&order=4"><span>Tóm
															tắt</span></a></th>
												<th class="table-header-repeat line-left minwidth-1"><a
													href="managebook.jsp?&key=${key}&typesearch=${typesearch}&category=${category}&order=5"><span>Hình
															cover</span></a></th>
												<th class="table-header-repeat line-left minwidth-1"><a
													href="managebookcategory.jsp"><span>Danh mục</span></a></th>
												<th class="table-header-repeat line-left"><a href=""><span>Số
															lượng chapter</span></a></th>
												<th class="table-header-repeat line-left"><a href=""><span>Người
															Upload</span></a></th>
												<th class="table-header-repeat line-left minwidth-1"><a
													href=""><span>Chức năng</span></a></th>
										</thead>
										<tbody>
											<jsp:include page="TableListBook" />
										</tbody>
										</tr>


									</table>
									<!--  end product-table................................... -->
								</form>
							</div>
							<!--  end content-table  -->

							<!--  start paging..................................................... -->
							<table border="0" cellpadding="0" cellspacing="0"
								id="paging-table">
								<tr>
									<td><a
										href="managebook.jsp?page=1&key=${key}&typesearch=${typesearch}&category=${category}&order=${order}"
										class="page-far-left"></a> <a
										href="managebook.jsp?page=${currentPage - 1}&key=${key}&typesearch=${typesearch}&category=${category}&order=${order}"
										class="page-left"></a>
										<div id="page-info">
											Trang <strong>${currentPage}</strong> / ${numberOfPages}
										</div> <a
										href="managebook.jsp?page=${currentPage + 1}&key=${key}&typesearch=${typesearch}&category=${category}&order=${order}"
										class="page-right"></a> <a
										href="managebook.jsp?page=${numberOfPages}&key=${key}&typesearch=${typesearch}&category=${category}&order=${order}"
										class="page-far-right"></a></td>
								</tr>
							</table>
							<!--  end paging................ -->

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
		<!--  end content -->
		<div class="clear">&nbsp;</div>
	</div>
	<!--  end content-outer........................................................END -->

	<jsp:include page="footer.jsp" />

</body>
</html>