package httpservlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Others.SSUtil;
import model.User;

/**
 * Servlet implementation class ChangePassword
 */
@WebServlet("/ChangePassword")
public class ChangePassword extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ChangePassword() {
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
		String oldpass = request.getParameter("oldpass");
		String newpass = request.getParameter("newpass");
		String checkpass = request.getParameter("checkpass");

		oldpass = SSUtil.md5(oldpass);

		User usr = new User().getByUsername((String) request.getSession()
				.getAttribute("username"));
		
		if (!oldpass.equals(usr.getPassword())) {
			out.println("Lỗi: ");
			out.println("Mật khẩu hiện tại không đúng");
			return;
		}

		if (newpass.equals("")) {
			out.println("Lỗi: ");
			out.println("Vui lòng nhập mật khẩu mới !");
			return;
		}
		
		if (!newpass.equals(checkpass)) {
			out.println("Lỗi: ");
			out.println("Mật khẩu mới không giống nhau");
			return;
		}
		newpass = SSUtil.md5(newpass);
		usr.setPassword(newpass);

		
		out.println("<html>");
		out.println("<head>");
		out.println("  <meta http-equiv=\"refresh\" content=\"3;url=managebook.jsp \"/>");
		out.println(" </head>");
		out.println(" <body>");
		if (usr.updateUser() == 1) {
			// add success
			out.println("<p align=\"center\"><font color=red>Đổi mật khẩu thành công. Tự chuyển trang sau 3 giây</font></p>");
		} else {
			// add fail
			out.println("<p align=\"center\"><font color=red>Đổi mật khẩu thất bại.</font></p>");
		}
		out.println("<p align=\"center\"><font color=red><a href='managebook.jsp'>Click vào đây để chuyển tiếp</a></font></p>");
		out.println(" </body>");
		out.println("</html>");

	}

}
