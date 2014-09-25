<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<jsp:include page="head.jsp">
	<jsp:param name="title" value="Quản lý phản hồi" />
</jsp:include>

<script type="text/javascript">
	function confirmSubmit(id, titlebook) {
		if (confirm("Bạn có chắc muốn xóa chapter này?")) {
			window.location = "DeleteBookChapter?idbookchapter=" + id
					+ "&titlebook=" + titlebook;
		}
		return false;
	}
</script>
<body>
	<jsp:include page="header.jsp">
		<jsp:param name="select" value="3" />
	</jsp:include>

	<!-- start content-outer ........................................................................................................................START -->
	<div id="content-outer">
		<!-- start content -->
		<div id="content">

			<!--  start page-heading -->
			<div id="page-heading">
				<h1>Quản lý phản hồi</h1>
			</div>
			<!-- end page-heading -->

			<table border="0" width="100%" cellpadding="0" cellspacing="0"
				id="content-table" class="tablesorter">
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
								<!--  start product-table ..................................................................................... -->
								<table border="0" width="100%" cellpadding="0" cellspacing="0"
									id="product-table">
									<thead>
										<tr>
											<th class="table-header-repeat line-left"><span>ID</span></th>
											<th class="table-header-repeat line-left minwidth-1"><span>Tựa
													sách</span></th>
											<th class="table-header-repeat line-left"><span>Tác
													giả</span></th>
											<th class="table-header-repeat line-left minwidth-1"><span>Ý
													kiến phản hồi</span></th>
											<th class="table-header-repeat line-left"><span>Chức
													năng</span></th>
										</tr>
									</thead>
									<tbody>
										<jsp:include page="TableListFeedBack" />
									</tbody>
								</table>
								<!--  end product-table................................... -->
							</div>
							<!--  end content-table  -->
							<!-- paging -->
							<jsp:include page="module_paging.jsp" />
							<!-- end paging -->
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