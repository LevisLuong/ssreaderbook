package httpservlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Others.config;

/**
 * Servlet implementation class CheckChapterBook
 */
@WebServlet("/CheckChapterBook")
public class CheckChapterBook extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CheckChapterBook() {
		super();
		// TODO Auto-generated constructor stub
	}


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");

		PrintWriter out = response.getWriter();
		
		String bookPath = config.getUrlBookDir(request)
				+ config.UPLOAD_BOOK_DIR;

		System.out.println("Duong dan den thu muc: " + bookPath);
		// File for dir contain books
		File fileBookDir = new File(bookPath);

		for (final File fileEntry : fileBookDir.listFiles()) {
			boolean isContain = false;
			for (File fileChild : fileEntry.listFiles()){
				if (fileChild.getName().contains("epub")) {
					isContain = true;
				}
			}
			if (!isContain) {
				out.println("----==========Thu muc khong co chua file epub: "
						+ fileEntry.getName() + "</br>");
			}
		}
	}

}
