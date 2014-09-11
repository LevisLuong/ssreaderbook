package httpservlet;

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

/**
 * Servlet implementation class TableListCategory
 */
@WebServlet("/TableListCategory")
public class TableListCategory extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TableListCategory() {
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
			int role = (int)request.getSession().getAttribute("role");
			PrintWriter out = response.getWriter();
			Book_Category bookQuery = new Book_Category();
			ArrayList<Book_Category> bcs = new ArrayList<Book_Category>();
			bcs = bookQuery.getAllCategory();
			int i = 1;
			for (Book_Category bc : bcs) {
				if (i % 2 == 0) {
					out.println("<tr class=\"alternate-row\">");
				} else {
					out.println("<tr>");
				}

				out.println("<td> <a href=\"managebook.jsp?category="
						+ bc.getIdCategory() + "\">" + bc.getIdCategory()
						+ "</a></td>");

				out.println("<td><a href=\"managebook.jsp?category="
						+ bc.getIdCategory() + "\">" + bc.getCategory()
						+ "</a></td>");
				out.println("<td><a href=\"managebook.jsp?category="
						+ bc.getIdCategory() + "\">"
						+ bc.getNumBookByCate(bc.getIdCategory()) + "</a></td>");
				out.println("<td>");
				if (role == 1) {
					out.println("<a title='Delete' class=\"icon-2 info-tooltip\" href=\"#\" onclick=\"confirmSubmit("
							+ bc.getIdCategory() + ")\"></a>");
				}
				out.println("<a title='Edit' class=\"icon-1 info-tooltip\" href=\"editbookcategory.jsp?idbook="
						+ bc.getIdCategory() + "\" ></a>" + "</td>");

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
