package httpservlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Book;
import model.Book_Chapter;

/**
 * Servlet implementation class ApproveBookChapter
 */
@WebServlet("/ApproveBookChapter")
public class ApproveBookChapter extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ApproveBookChapter() {
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
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String titlebook = request.getParameter("titlebook");
		int id = Integer.parseInt(request.getParameter("idbookchapter"));
		int status = Integer.parseInt(request.getParameter("status"));
		Book_Chapter b = (new Book_Chapter()).getById(id);
		int idbook = b.getIdbook();

		if (b != null) {
			if (b.setApprovedDatabase(status) == 1) {
				
			} else {

			}
		}
		response.sendRedirect("managebookchapter.jsp?idbook=" + idbook
				+ "&titlebook=" + titlebook);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
