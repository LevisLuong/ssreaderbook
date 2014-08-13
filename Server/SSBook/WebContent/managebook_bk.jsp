<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>SSBook</title>
<link rel="stylesheet" href="css/screen.css" type="text/css"
	media="screen" title="default" />
<!--[if IE]>
<link rel="stylesheet" media="all" type="text/css" href="css/pro_dropline_ie.css" />
<![endif]-->

<!--  jquery core -->
<script src="js/jquery/jquery-1.4.1.min.js" type="text/javascript"></script>

<!--  checkbox styling script -->
<script src="js/jquery/ui.core.js" type="text/javascript"></script>
<script src="js/jquery/ui.checkbox.js" type="text/javascript"></script>
<script src="js/jquery/jquery.bind.js" type="text/javascript"></script>
<script type="text/javascript">
	$(function() {
		$('input').checkBox();
		$('#toggle-all').click(function() {
			$('#toggle-all').toggleClass('toggle-checked');
			$('#mainform input[type=checkbox]').checkBox('toggle');
			return false;
		});
	});
</script>

<![if !IE 7]>

<!--  styled select box script version 1 -->
<script src="js/jquery/jquery.selectbox-0.5.js" type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$('.styledselect').selectbox({
			inputClass : "selectbox_styled"
		});
	});
</script>


<![endif]>

<!--  styled select box script version 2 -->
<script src="js/jquery/jquery.selectbox-0.5_style_2.js"
	type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$('.styledselect_form_1').selectbox({
			inputClass : "styledselect_form_1"
		});
		$('.styledselect_form_2').selectbox({
			inputClass : "styledselect_form_2"
		});
	});
</script>

<!--  styled select box script version 3 -->
<script src="js/jquery/jquery.selectbox-0.5_style_2.js"
	type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$('.styledselect_pages').selectbox({
			inputClass : "styledselect_pages"
		});
	});
</script>

<!--  styled file upload script -->
<script src="js/jquery/jquery.filestyle.js" type="text/javascript"></script>
<script type="text/javascript" charset="utf-8">
	$(function() {
		$("input.file_1").filestyle({
			image : "images/forms/choose-file.gif",
			imageheight : 21,
			imagewidth : 78,
			width : 310
		});
	});
</script>

<!-- Custom jquery scripts -->
<script src="js/jquery/custom_jquery.js" type="text/javascript"></script>

<!-- Tooltips -->
<script src="js/jquery/jquery.tooltip.js" type="text/javascript"></script>
<script src="js/jquery/jquery.dimensions.js" type="text/javascript"></script>
<script type="text/javascript">
	$(function() {
		$('a.info-tooltip ').tooltip({
			track : true,
			delay : 0,
			fixPNG : true,
			showURL : false,
			showBody : " - ",
			top : -35,
			left : 5
		});
	});
</script>
<!-- dialog confirm delete -->
<script>
	function selectPage(select) {
		top.location.href = this.form.menu2.options[this.form.menu2.selectedIndex].value;
		return false;
		window.location = "managebook.jsp?&key=${key}&typesearch=${typesearch}&category=${category}&order=5&page="
				+ select.value;
	}
</script>

<!--  date picker script -->
<link rel="stylesheet" href="css/datePicker.css" type="text/css" />
<script src="js/jquery/date.js" type="text/javascript"></script>
<script src="js/jquery/jquery.datePicker.js" type="text/javascript"></script>
<script type="text/javascript" charset="utf-8">
	$(function() {

		// initialise the "Select date" link
		$('#date-pick').datePicker(
		// associate the link with a date picker
		{
			createButton : false,
			startDate : '01/01/2005',
			endDate : '31/12/2020'
		}).bind(
		// when the link is clicked display the date picker
		'click', function() {
			updateSelects($(this).dpGetSelected()[0]);
			$(this).dpDisplay();
			return false;
		}).bind(
		// when a date is selected update the SELECTs
		'dateSelected', function(e, selectedDate, $td, state) {
			updateSelects(selectedDate);
		}).bind('dpClosed', function(e, selected) {
			updateSelects(selected[0]);
		});

		var updateSelects = function(selectedDate) {
			var selectedDate = new Date(selectedDate);
			$('#d option[value=' + selectedDate.getDate() + ']').attr(
					'selected', 'selected');
			$('#m option[value=' + (selectedDate.getMonth() + 1) + ']').attr(
					'selected', 'selected');
			$('#y option[value=' + (selectedDate.getFullYear()) + ']').attr(
					'selected', 'selected');
		}
		// listen for when the selects are changed and update the picker
		$('#d, #m, #y').bind('change', function() {
			var d = new Date($('#y').val(), $('#m').val() - 1, $('#d').val());
			$('#date-pick').dpSetSelected(d.asString());
		});

		// default the position of the selects to today
		var today = new Date();
		updateSelects(today.getTime());

		// and update the datePicker to reflect it...
		$('#d').trigger('change');
	});
</script>

<!-- MUST BE THE LAST SCRIPT IN <HEAD></HEAD></HEAD> png fix -->
<script src="js/jquery/jquery.pngFix.pack.js" type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$(document).pngFix();
	});
</script>
</head>
<body>
	<!-- Start: page-top-outer -->
	<div id="page-top-outer">

		<!-- Start: page-top -->
		<div id="page-top">

			<!-- start logo -->
			<div id="logo">
				<font color="white" size="42">Quản lý sách SSBook</font>
			</div>
			<!-- end logo -->

			<!--  start top-search -->
			<div id="top-search">
				<form action="managebook.jsp" method=get>
					<table border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td><input type="text" name="key" value="Tìm Sách"
								onfocus="if (this.value=='Tìm Sách') { this.value=''; }"
								class="top-search-inp" /></td>
							<td><select name="typesearch" class="styledselect">
									<option value="1">Tựa sách</option>
									<option value="2">ID Danh mục</option>
									<option value="3">ID sách</option>
							</select></td>
							<td><input type="image"
								src="images/shared/top_search_btn.gif" /></td>
						</tr>
					</table>
				</form>
			</div>
			<!--  end top-search -->
			<div class="clear"></div>

		</div>
		<!-- End: page-top -->

	</div>
	<!-- End: page-top-outer -->

	<div class="clear">&nbsp;</div>

	<!--  start nav-outer-repeat................................................................................................. START -->
	<div class="nav-outer-repeat">
		<!--  start nav-outer -->
		<div class="nav-outer">
			<!-- start nav-right -->
			<div id="nav-right">

				<div class="nav-divider">&nbsp;</div>
				<div class="showhide-account">
					<img src="images/shared/nav/nav_myaccount.gif" width="93"
						height="14" alt="" />
				</div>
				<div class="nav-divider">&nbsp;</div>
				<a href="Logout" id="logout"><img
					src="images/shared/nav/nav_logout.gif" width="64" height="14"
					alt="" /></a>
				<div class="clear">&nbsp;</div>

				<!--  start account-content -->
				<div class="account-content">
					<div class="account-drop-inner">
						<a href="" id="acc-settings">Thay Doi Mat Khau</a>
						<div class="clear">&nbsp;</div>
						<div class="acc-line">&nbsp;</div>
					</div>
				</div>
				<!--  end account-content -->

			</div>
			<!-- end nav-right -->


			<!--  start nav -->
			<div class="nav">
				<div class="table">
					<ul class="select">
						<li><a href="managebookcategory.jsp"><b>Quản lý danh
									mục</b> <!--[if IE 7]><!--></a> <!--<![endif]--> <!--[if lte IE 6]></td></tr></table></a><![endif]-->
						</li>
					</ul>

					<div class="nav-divider">&nbsp;</div>

					<ul class="current">
						<li><a href="managebook.jsp"><b>Quản lý sách</b></a></li>
					</ul>
					<div class="clear"></div>
				</div>
				<div class="clear"></div>
			</div>
			<!--  start nav -->

		</div>
		<div class="clear"></div>
		<!--  start nav-outer -->
	</div>
	<!--  start nav-outer-repeat................................................... END -->

	<div class="clear"></div>

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
											<td class="blue-left">Them sach: <a href="addbook.jsp">An
													vao day de them sach</a></td>
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
											<th class="table-header-repeat line-left minwidth-1"><a
												href="managebook.jsp?&key=${key}&typesearch=${typesearch}&category=${category}&order=1">Id
													sách</a></th>
											<th class="table-header-repeat line-left minwidth-1"><a
												href="managebook.jsp?&key=${key}&typesearch=${typesearch}&category=${category}&order=2">Tựa
													sách</a></th>
											<th class="table-header-repeat line-left minwidth-1"><a
												href="managebook.jsp?&key=${key}&typesearch=${typesearch}&category=${category}&order=3">Tác
													giả</a></th>
											<th class="table-header-repeat line-left minwidth-1"><a
												href="managebook.jsp?&key=${key}&typesearch=${typesearch}&category=${category}&order=4">Tóm
													tắt</a></th>
											<th class="table-header-repeat line-left minwidth-1"><a
												href="managebook.jsp?&key=${key}&typesearch=${typesearch}&category=${category}&order=5">Hình
													cover</a></th>
											<th class="table-header-options line-left minwidth-1"><a
												href="managebookcategory.jsp">Danh mục</a></th>
											<th class="table-header-options"><a href="">Chức
													năng</a></th>
											<jsp:include page="TableListBook" />
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

									<td>
										<form name="selectform">
											<select name="select" class="styledselect_pages"
												onchange="window.top.location.href = 'managebook.jsp?key=${key}&typesearch=${typesearch}&category=${category}&order=${order}&page=' +  this.form.select.options[this.form.select.selectedIndex].value">
												<c:forEach begin="1" end="${numberOfPages}" var="i">
													<option value="${i}">${i}</option>
												</c:forEach>
											</select>
										</form>
									</td>
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

	<div class="clear">&nbsp;</div>

	<!-- start footer -->
	<div id="footer">
		<!--  start footer-left -->
		<div id="footer-left">
			Sea Soft coporation. <span id="spanYear"></span> <a href="">www.ssc.com</a>.
			All rights reserved.
		</div>
		<!--  end footer-left -->
		<div class="clear">&nbsp;</div>
	</div>
	<!-- end footer -->

</body>
</html>