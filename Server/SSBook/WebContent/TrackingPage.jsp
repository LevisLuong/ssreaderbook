<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<jsp:include page="head.jsp">
	<jsp:param name="title" value="Thống kê" />
</jsp:include>
<link rel="stylesheet" href="css/trackingpage.css" type="text/css"
	media="screen" title="default" />
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script type="text/javascript">
	google.load("visualization", "1", {
		packages : [ "corechart" ]
	});
	google.setOnLoadCallback(drawChart);
	function drawChart() {
		var data = google.visualization.arrayToDataTable([
				[ 'Ngày', 'Lượt truy cập'], [ '2004', 1000],
				[ '2005', 1170 ], [ '2006', 660],
				[ '2007', 1030] ]);
		var options = {
			
		};
		var chart = new google.visualization.LineChart(document
				.getElementById('chart_div'));

		chart.draw(data, options);
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
				<h1>Thống kê</h1>
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
								<table width="100%" cellpadding="0" cellspacing="0"
									class="today_stats">
									<tbody>
										<tr>
											<td><strong>2481</strong> Tổng lượt truy cập</td>
											<td><strong>1781</strong> Lượt truy cập trong ngày</td>
											<td><strong>583</strong> Lượt truy cập trong tháng</td>
										</tr>
									</tbody>
								</table>



								<div class="simplebox grid740" style="z-index: 680;">
									<div class="titleh" style="z-index: 670;">
										<h3>Statistics Chart</h3>
									</div>
									<div id="chart_div" class="body" style="z-index: 650;">
									</div>

								</div>

							</div>
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