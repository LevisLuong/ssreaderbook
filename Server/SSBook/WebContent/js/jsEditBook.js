		$(document)
				.ready(
						function() {
							var progressbox = $('#progressbox');
							var progressbar = $('#progressbar');
							var statustxt = $('#statustxt');
							var uploadForm = $('#MyUploadForm');
							var inputFile = $('#inputFile');
							var output = $('#output');
							var completed = '0%';

							var options = {
								beforeSubmit : beforeSubmit, // pre-submit callback 
								uploadProgress : OnProgress,
								success : afterSuccess, // post-submit callback 
								resetForm : true
							// reset the form after successful submit 
							};

							uploadForm.submit(function() {
								$(this).ajaxSubmit(options);
								// return false to prevent standard browser submit and page navigation 
								return false;
							});

							//when upload progresses	
							function OnProgress(event, position, total,
									percentComplete) {
								//Progress bar
								progressbar.width(percentComplete + '%') //update progressbar percent complete
								statustxt.html(percentComplete + '%'); //update status text
								if (percentComplete > 50) {
									statustxt.css('color', '#fff'); //change status text to white after 50%
								}
							}

							//after succesful upload
							function afterSuccess(data) {
								progressbox.hide();
								output.html(data);
							}

							//function to check file size before uploading.
							function beforeSubmit() {
								//check whether browser fully supports all File API
								if (window.File && window.FileReader
										&& window.FileList && window.Blob) {
									if (inputFile.val()) //check empty input filed
									{
										var fsize = inputFile[0].files[0].size; //get file size
										var ftype = inputFile[0].files[0].type; // get file type

										//allow only valid image file types 
										switch (ftype) {
										case 'image/png':
										case 'image/gif':
										case 'image/jpeg':
										case 'image/pjpeg':
											break;
										default:
											output
													.html(
															"<b>"
																	+ ftype
																	+ "</b>Không hỗ trợ định dạng hình này!")
													.css("color", "red");
											return false
										}

										//Allowed file size is less than 2 MB (2097152)
										if (fsize > 2097152) {
											output
													.html(
															"<b>"
																	+ bytesToSize(fsize)
																	+ "</b> File quá lớn! <br />Vui lòng resize lại kích thước file.")
													.css("color", "red");
											return false
										}
									}
									
									//Progress bar
									progressbox.show(); //show progressbar
									progressbar.width(completed); //initial value 0% of progressbar
									statustxt.html(completed); //set status text
									statustxt.css('color', '#000'); //initial color of status text
									uploadForm.hide(); //hide content
									output.html("Đang tải tập tin lên...");
								} else {
									//Output error to older unsupported browsers that doesn't support HTML5 File API
									output
											.html(
													"Vui lòng xài trình duyệt khác")
											.css("color", "red");
									;
									return false;
								}
							}
						});
