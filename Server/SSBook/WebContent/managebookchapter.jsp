<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<jsp:include page="head.jsp">
	<jsp:param name="title" value="Quản lý chapter sách" />
</jsp:include>

<script type="text/javascript">
	function confirmSubmit(id,titlebook) {
		if (confirm("Bạn có chắc muốn xóa chapter này?")) {
			window.location="DeleteBookChapter?idbookchapter="+id+"&titlebook="+titlebook;
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
				<h1>Quản lý Chapter</h1>
				<h2><a href="managebook.jsp">${param.titlebook}</a></h2>
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
											<td class="blue-left">Thêm chapter: <a
												href="addbookchapter.jsp?idbook=${param.idbook}&titlebook=${param.titlebook}">Ấn vào đây để thêm Chapter mới</a></td>
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
											<th class="table-header-repeat line-left"><a href="">ID</a></th>
											<th class="table-header-repeat line-left minwidth-1"><a
												href="">Tên Chapter</a></th>
											<th class="table-header-repeat line-left"><a href="">Tên
													File</a></th>
											<th class="table-header-repeat line-left minwidth-1"><a
												href="">Kích thước file</a></th>
											<th class="table-header-repeat line-left"><a href="">Chức
													năng</a></th>
										</tr>
										<jsp:include page="TableListBookChapter" />

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