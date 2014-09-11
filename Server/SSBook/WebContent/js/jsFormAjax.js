$(document).ready(function() {
	var uploadForm = $('#MyUploadForm');
	var output = $('#output');

	var options = {
		success : afterSuccess, // post-submit callback
	// reset the form after successful submit
	};

	uploadForm.submit(function() {
		$(this).ajaxSubmit(options);
		// return false to prevent standard browser submit and page navigation
		return false;
	});

	// when upload progresses
	function OnProgress(event, position, total, percentComplete) {
	}

	// after succesful upload
	function afterSuccess(data) {
		if (data.search("Lỗi") == -1) {
			uploadForm.hide();
			output.html(data);
		} else {
			output.html(data).css("color", "red");
		}

	}

	// function to check file size before uploading.
	function beforeSubmit() {
	}
});
