<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<jsp:include page="head.jsp">
	<jsp:param name="title" value="Thêm chapter sách" />
</jsp:include>
<link rel="stylesheet" href="css/uploadbar.css" type="text/css" />
<body>
	<jsp:include page="header.jsp">
		<jsp:param name="select" value="2" />
	</jsp:include>
	<script type="text/javascript" src="js/jsAddChapter.js"></script>
	<!-- start content-outer -->
	<div id="content-outer">
		<!-- start content -->
		<div id="content">


			<div id="page-heading">
				<h1>Thêm Chapter</h1>
				<h2><a href="managebookchapter.jsp?idbook=${param.idbook}&titlebook=${param.titlebook}">${param.titlebook}</a></h2>
			</div>

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
						<div>test</div> <!--  start content-table-inner -->
						<div id="content-table-inner">
							<table border="0" width="100%" cellpadding="0" cellspacing="0">
								<tr>
									<div id="output"></div>
									<!--progress bar-->
									<div id="progressbox" style="display: none;">
										<div id="progressbar"></div>
										<div id="statustxt">0%</div>
									</div>
								</tr>
								<tr valign="top">
									<td>
										<!-- start id-form -->
										<form id="MyUploadForm" onsubmit="return false"
											action="AddBookChapter" method="POST"
											enctype="multipart/form-data">
											<table border="0" cellpadding="0" cellspacing="0"
												id="id-form">

												<tr>
													<th valign="top">Tên Chapter:</th>
													<td><input type="text" name="chapter" class="inp-form" /></td>
													<td></td>
												</tr>
												<tr>
													<th>Upload Chapter:</th>
													<td><input type="file" name="filename" id="inputFile"
														class="file_1" /></td>
												</tr>
												<tr>
													<th>&nbsp;</th>
													<td valign="top"><input type=hidden name=idbook
														value='${param.idbook}'> <input type="submit"
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

			<div class="clear">&nbsp;</div>

		</div>
		<!--  end content -->
		<div class="clear">&nbsp;</div>
	</div>
	<!--  end content-outer -->
	<jsp:include page="footer.jsp" />

</body>
</html>