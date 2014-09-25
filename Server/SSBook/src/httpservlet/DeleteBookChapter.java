package httpservlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Book_Category;
import model.Book_Chapter;

/**
 * Servlet implementation class DeleteBookChapter
 */
@WebServlet("/DeleteBookChapter")
public class DeleteBookChapter extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeleteBookChapter() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
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
		String titlebook = request.getParameter("titlebook");
		int id = Integer.parseInt(request.getParameter("idbookchapter"));
		Book_Chapter b = (new Book_Chapter()).getById(id);
		int idbook = b.getIdbook();
		if (b != null) {
			if (b.deleteBook_Chapter() == 1) {

			} else {

			}
		}
		response.sendRedirect("managebookchapter.jsp?idbook=" + idbook
				+ "&titlebook=" + titlebook);
	}
}
