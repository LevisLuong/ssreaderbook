<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<jsp:include page="head.jsp">
	<jsp:param name="title" value="Quản lý danh mục" />
</jsp:include>
<script type="text/javascript">
	function confirmSubmit(id) {
		if (confirm("Bạn có chắc muốn xóa danh mục này?")) {
			window.location="DeleteBookCategory?idbook="+id;
		}
		return false;
	}
</script>
<body>
	<jsp:include page="header.jsp">
		<jsp:param name="select" value="1" />
	</jsp:include>

	<!-- start content-outer ........................................................................................................................START -->
	<div id="content-outer">
		<!-- start content -->
		<div id="content">

			<!--  start page-heading -->
			<div id="page-heading">
				<h1>Quản lý danh mục</h1>
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
											<td class="blue-left">Thêm danh mục: <a
												href="addbookcategory.jsp">Ấn vào đây để thêm danh mục mới</a></td>
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
										<tr>
											<th class="table-header-repeat line-left"><a href="">Id</a></th>
											<th class="table-header-repeat line-left minwidth-1"><a
												href="">Tên Danh mục</a></th>
											<th class="table-header-repeat line-left"><a href="">Số
													lượng sách</a></th>
											<th class="table-header-repeat line-left minwidth-1"><a
												href="">Chức năng</a></th>
										</tr>
										<jsp:include page="TableListCategory" />

									</table>
									<!--  end product-table................................... -->
								</form>
							</div>
							<!--  end content-table  -->

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