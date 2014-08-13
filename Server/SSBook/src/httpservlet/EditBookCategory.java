package httpservlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Book;
import model.Book_Category;

/**
 * Servlet implementation class EditBookCategory
 */
@WebServlet("/EditBookCategory")
public class EditBookCategory extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditBookCategory() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			int idbook = Integer.parseInt(request.getParameter("idbook"));
			PrintWriter out = response.getWriter();
			Book_Category bc = (new Book_Category()).getById(idbook);
			out.println("<tr>"
					+ "<th valign='top'>Tên danh mục:</th>"
					+ "<td><input type=\"text\" class='inp-form' name='title' value='"+ bc.getCategory() +"'></td>"
					+ "</tr>"
					+ "<input type=hidden name=idbook value='"+ bc.getIdCategory() +"'>");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String title = request.getParameter("title");
		
		int idbook = Integer.parseInt(request.getParameter("idbook"));
		Book_Category bc = new Book_Category();

		bc.setIdCategory(idbook);
		bc.setCategory(title);
		
		out.println("<html>");
		out.println("<head>");
		out.println("  <meta http-equiv=\"refresh\" content=\"3;url=managebookcategory.jsp\" />");
		out.println(" </head>");
		out.println(" <body>");
		if (bc.updateBook() == 1) {
			// add success
			out.println("<p align=\"center\"><font color=red>Sửa danh mục thành công. Tự chuyển trang sau 3 giây</font></p>");
		} else {
			// add fail
			out.println("<p align=\"center\"><font color=red>Sửa danh mục thất bại.</font></p>");
		}
		out.println("<p align=\"center\"><font color=red><a href='managebookcategory.jsp'>Click vào đây để chuyển tiếp</a></font></p>");
		out.println(" </body>");
		out.println("</html>");

	}

}
