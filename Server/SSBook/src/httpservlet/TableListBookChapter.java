package httpservlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Book;
import model.Book_Category;
import model.Book_Chapter;
import Others.config;

/**
 * Servlet implementation class TableListBookChapter
 */
@WebServlet("/TableListBookChapter")
public class TableListBookChapter extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TableListBookChapter() {
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
		try {
			int idBook = Integer.parseInt(request.getParameter("idbook"));

			PrintWriter out = response.getWriter();
			Book_Chapter bookchapQuery = new Book_Chapter();
			ArrayList<Book_Chapter> bookchaps = new ArrayList<Book_Chapter>();
			bookchaps = bookchapQuery.getByIdBook(idBook);
			int i = 1;
			for (Book_Chapter bookchap : bookchaps) {
				if (i % 2 == 0) {
					out.println("<tr class=\"alternate-row\">");
				} else {
					out.println("<tr>");
				}

				out.println("<td>" + bookchap.getIdbook_chapter() + "</td>");
				out.println("<td>" + bookchap.getChapter() + "</td>");
				out.println("<td><a href='.." + File.separator
						+ config.UPLOAD_BOOK_DIR + File.separator
						+ bookchap.getIdbook() + File.separator
						+ bookchap.getFilename() + "'>"
						+ bookchap.getFilename() + "</a></td>");
				out.println("<td>" + bookchap.getFilesize() + "</td>");
				out.println("<td  class=\"options-width\">"
						+ "<form action=\"DeleteBookChapter\" method=post>"
						+ "<input type=hidden name=idbookchapter value="
						+ bookchap.getIdbook_chapter()
						+ ">"
						+ "<a title='Delete' class=\"icon-2 info-tooltip\" href=\"#\" onclick=\"$(this).closest('form').submit()\"></a>"
						// + "<input type=submit value='Delete'>"
						+ "</form>"
						+ "<a title='Edit' class=\"icon-1 info-tooltip\" href=\"editbookchapter.jsp?idbookchapter="
						+ bookchap.getIdbook_chapter() + " \"></a>" + "</td>");
				out.println("</tr>");
				i++;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
