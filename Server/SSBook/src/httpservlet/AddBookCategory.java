package httpservlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import model.Book;
import model.Book_Category;

/**
 * Servlet implementation class AddBookCategory
 */
@WebServlet("/AddBookCategory")
public class AddBookCategory extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddBookCategory() {
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
		String title = request.getParameter("title");

		Book_Category bc = new Book_Category();

		bc.setCategory(title);

		out.println("<html>");
		out.println("<head>");
		out.println("  <meta http-equiv=\"refresh\" content=\"3;url=managebookcategory.jsp\" />");
		out.println(" </head>");
		out.println(" <body>");
		if (bc.addBookCategory() == 1) {
			// add success
			out.println("<p align=\"center\"><font color=red>Thêm danh mục thành công. Tự chuyển trang sau 3 giây</font></p>");
		} else {
			// add fail
			out.println("<p align=\"center\"><font color=red>Thêm danh mục thất bại.</font></p>");
		}
		out.println("<p align=\"center\"><font color=red><a href='managebookcategory.jsp'>Click vào đây để chuyển tiếp</a></font></p>");
		out.println(" </body>");
		out.println("</html>");
	}

}
