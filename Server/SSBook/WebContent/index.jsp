<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Quan ly SSBook</title>
</head>
<script type="text/javascript">
	function checkInput() {
		//Kiểm tra user name:
		if (document.getElementById('input').value == "xuantrung") {
			var menu = document.getElementById('div-menu');
			menu.style.display = 'block';
			return false;
		}
		return true;
	}
</script>
<body>
	<script type="text/javascript" src="js/zerocopy/ZeroClipboard.js"></script>
	<script type="text/javascript" src="js/zerocopy/main_zcopy.js"></script>

	<input type="text" id="input" name="pass" size="25" maxlength="100"
		class="text" />
	<button onclick="checkInput()">Try it</button>

	<div id="div-menu" class="main-menu" style="display: none;">
		<button id="target-to-copy" data-clipboard-text="FGfndUJrd8E2>/m">copy</button>
	</div>
</body>
</html>