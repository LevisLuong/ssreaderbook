package Others;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

public class config {
	// public static final String IP_SERVER = "http://103.27.60.125:8080/";
	public static final String UPLOAD_BOOK_DIR = "rs_ssbooks/books/";
	public static final String UPLOAD_COVERBOOK_DIR = "cover_books/";

	public static String getUrlBookDir(HttpServletRequest request) {
		String url = request.getServletContext().getRealPath("");
		url = url.substring(0, url.lastIndexOf(File.separator));
		return url + File.separator;
	}
}
