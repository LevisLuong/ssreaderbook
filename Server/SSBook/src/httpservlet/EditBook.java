package httpservlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import Others.config;
import model.Book;
import model.Book_Category;

/**
 * Servlet implementation class EditBook
 */
@WebServlet("/EditBook")
@MultipartConfig
public class EditBook extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private String getFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		System.out.println("content-disposition header= " + contentDisp);
		String[] tokens = contentDisp.split(";");
		for (String token : tokens) {
			if (token.trim().startsWith("filename")) {
				return token.substring(token.indexOf("=") + 2,
						token.length() - 1);
			}
		}
		return "";
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			int idbook = Integer.parseInt(request.getParameter("idbook"));
			PrintWriter out = response.getWriter();
			Book book = (new Book()).getById(idbook);
			List<Book_Category> bcs = (new Book_Category()).getAllCategory();
			out.write(String
					.format("<tr>"
							+ "<th valign='top'>Tựa sách:</th>"
							+ "<td><input type='text' name='title' value='%s' class='inp-form' />"
							+ "</td>"
							+ "<td></td>"
							+ "</tr>"
							+ "<tr>"
							+ "<th valign='top'>Tác giả:</th>"
							+ "<td><input type='text' name='author' value='%s' class='inp-form' />"
							+ "</td>"
							+ "</tr>"
							+ "<tr>"
							+ "<th valign='top'>Tóm tắt:</th><td><textarea rows='' cols='' name='summary' class='form-textarea'>%s</textarea></td><td></td></tr><tr>",
							book.getTitle(), book.getAuthor(),
							book.getSummary()));
			out.write("<th valign='top'>Chuyên mục:</th><td><select name='idcategory' class='styledselect_form_1'>");
			for (Book_Category book_Category : bcs) {
				out.write("<option value='" + book_Category.getIdCategory()
						+ "' ");
				if (book_Category.getIdCategory() == book.getIdcategory()) {
					out.write("selected=\"selected\"");
				}
				out.write(">" + book_Category.getCategory() + "</option>");

			}
			out.write("</select>");

			out.write("</td><td></td></tr><tr><th>Upload hình cover:</th>"
					+ "<td>"
					+ book.getImagecover()
					+ "<input type='file' name='imagecover' class='file_1' /></td><td><div class='bubble-left'></div><div class='bubble-inner'>JPEG, PNG, GIF 5MB max perimage</div><div class='bubble-right'></div></td></tr><tr><th>&nbsp;</th><td valign='top'><input type='submit' value='Chấp nhận' class='form-submit' /> <input type='reset' value='' class='form-reset' /></td><td></td></tr>"
					+ "<input type=hidden name=idbook value='"+ book.getIdBook() +"'>");

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
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String title = request.getParameter("title");
		String author = request.getParameter("author");
		String summary = request.getParameter("summary");
		System.out.println("title: " + title + ",author:" + author);

		int idcategory = Integer.parseInt(request.getParameter("idcategory"));

		System.out.println("title: " + title + ",author:" + author
				+ ",category:" + idcategory);
		int idbook = Integer.parseInt(request.getParameter("idbook"));

		Part imgCoverPart = request.getPart("imagecover"); // cover
		// save cover
		// gets absolute path of the web application
		// constructs path of the directory to save uploaded file
		String coverfilename = "";
		if (imgCoverPart != null) {
			try {
				String uploadCoverPath = config.getUrlBookDir(request)
						+ config.UPLOAD_BOOK_DIR + idbook + File.separator
						+ config.UPLOAD_COVERBOOK_DIR;

				// creates the save directory if it does not exists
				File fileSaveDir = new File(uploadCoverPath);
				if (!fileSaveDir.exists()) {
					fileSaveDir.mkdirs();
				}

				// save file upload
				coverfilename = getFileName(imgCoverPart);
				imgCoverPart.write(uploadCoverPath + coverfilename);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Book book = (new Book()).getById(idbook);

		if (!coverfilename.equals("")) {
			book.setImagecover(coverfilename);
		}
		book.setTitle(title);
		book.setAuthor(author);
		book.setSummary(summary);
		book.setIdcategory(idcategory);

		out.println("<html>");
		out.println("<head>");
		out.println("  <meta http-equiv=\"refresh\" content=\"3;url=managebook.jsp\" />");
		out.println(" </head>");
		out.println(" <body>");
		if (book.updateBook() == 1) {
			// add success
			out.println("<p align=\"center\"><font color=red>Sửa sách thành công. Tự chuyển trang sau 3 giây</font></p>");
		} else {
			// add fail
			out.println("<p align=\"center\"><font color=red>Sửa sách thất bại.</font></p>");
		}
		out.println("<p align=\"center\"><font color=red><a href='managebook.jsp'>Click vào đây để chuyển tiếp</a></font></p>");
		out.println(" </body>");
		out.println("</html>");
	}
}
