package httpservlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import model.Book;
import model.Book_Chapter;
import Others.SSUtil;
import Others.config;

/**
 * Servlet implementation class AddBookChapter
 */
@WebServlet("/AddBookChapter")
@MultipartConfig
public class AddBookChapter extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddBookChapter() {
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
	 * Utility method to get file name from HTTP header content-disposition
	 */
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		String chapter = request.getParameter("chapter");
		int idbook = Integer.parseInt(request.getParameter("idbook"));

		Part filePart = request.getPart("filename"); // Book
		// save cover
		// gets absolute path of the web application
		// constructs path of the directory to save uploaded file
		String filename = "";
		if (filePart != null) {
			String uploadPath = config.getUrlBookDir(request)
					+ config.UPLOAD_BOOK_DIR + idbook;

			// creates the save directory if it does not exists
			File fileSaveDir = new File(uploadPath);
			if (!fileSaveDir.exists()) {
				fileSaveDir.mkdirs();
			}

			// save file upload
			filename = getFileName(filePart);
			filePart.write(uploadPath + File.separator + filename);
		}
		Book_Chapter bc = new Book_Chapter();
		bc.setChapter(chapter);
		bc.setIdbook(idbook);
		bc.setFilename(filename);
		System.out.println("File size: " + filePart.getSize());
		System.out.println("File size s: "
				+ SSUtil.humanReadableByteCount(filePart.getSize(), true));
		bc.setFilesize(SSUtil.humanReadableByteCount(filePart.getSize(), true));

		out.println("<html>");
		out.println("<head>");
		out.println("  <meta http-equiv=\"refresh\" content=\"3;url=addbookchapter.jsp?idbook="
				+ idbook + "\" />");
		out.println(" </head>");
		out.println(" <body>");
		if (bc.addBook_Chapter() == 1) {
			// add success
			out.println("<p align=\"center\"><font color=red>Thêm chapter thành công. Tự chuyển trang sau 3 giây</font></p>");
		} else {
			// add fail
			out.println("<p align=\"center\"><font color=red>Thêm chapter thất bại.</font></p>");
		}
		out.println("<p align=\"center\"><font color=red><a href='addbookchapter.jsp?idbook="
				+ idbook + "'>Click vào đây để chuyển tiếp</a></font></p>");
		out.println(" </body>");
		out.println("</html>");
	}

}
