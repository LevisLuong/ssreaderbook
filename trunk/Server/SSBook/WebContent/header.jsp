<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
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
				<img src="images/shared/nav/nav_myaccount.gif" width="10"
					height="14" alt="" />
			</div>
			<div class="showhide-accountname">
				<%=session.getAttribute("username")%>
			</div>
			<div class="nav-divider">&nbsp;</div>
			<a href="Logout" id="logout"><img
				src="images/shared/nav/nav_logout.gif" width="64" height="14" alt="" /></a>
			<div class="clear">&nbsp;</div>

			<!--  start account-content -->
			<div class="account-content">
				<div class="account-drop-inner">
					<a href="changepass.jsp" id="acc-settings">Thay đổi mật khẩu</a>
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
				<!-- Check select menu 1 -->
				<%
					if (request.getParameter("select").equals("1")) {
				%>
				<ul class="current">
					<%
						} else {
					%>
					<ul class="select">
						<%
							}
						%>
						<li><a href="managebookcategory.jsp"><b>Quản lý danh
									mục</b> <!--[if IE 7]><!--></a> <!--<![endif]--> <!--[if lte IE 6]></td></tr></table></a><![endif]-->
						</li>
					</ul>
					<!-- Check select menu 2 -->
					<div class="nav-divider">&nbsp;</div>

					<%
						if (request.getParameter("select").equals("2")) {
					%>
					<ul class="current">
						<%
							} else {
						%>
						<ul class="select">
							<%
								}
							%>
							<li><a href="managebook.jsp"><b>Quản lý sách</b></a></li>
						</ul>
						<!-- Check select menu 3 -->
						<div class="nav-divider">&nbsp;</div>

						<%
							if (request.getParameter("select").equals("3")) {
						%>
						<ul class="current">
							<%
								} else {
							%>
							<ul class="select">
								<%
									}
								%>
								<li><a href="managefeedback.jsp"><b>Trang xử lý
											phản hồi</b></a></li>
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
</html>