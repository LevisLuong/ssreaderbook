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

import org.apache.commons.io.FileUtils;

import model.Book;
import model.Book_Chapter;
import Others.SSUtil;
import Others.config;

/**
 * Servlet implementation class EditBookChapter
 */
@WebServlet("/EditBookChapter")
@MultipartConfig
public class EditBookChapter extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EditBookChapter() {
		super();
		// TODO Auto-generated constructor stub
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			int idbookchap = Integer.parseInt(request
					.getParameter("idbookchapter"));
			PrintWriter out = response.getWriter();
			Book_Chapter bc = (new Book_Chapter()).getById(idbookchap);
			out.println(String
					.format("<tr>"
							+ "<th valign='top'>Chapter:</th>"
							+ "<td><input type='text' name='chapter' value='%s' class='inp-form' /></td></tr><tr>"
							+ "<th valign='top'>Tên file:</th>"
							+ "<td>%s<input type='file' name='filename' class='file_1' /></td>"
							+ "</tr>"
							+ "<input type=hidden name=idbookchapter value='%d'>",
							bc.getChapter(), bc.getFilename(),
							bc.getIdbook_chapter()));
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
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		String chapter = request.getParameter("chapter");
		int idbookchapter = Integer.parseInt(request
				.getParameter("idbookchapter"));
		Book_Chapter bc = (new Book_Chapter()).getById(idbookchapter);
		Part filePart = request.getPart("filename"); // Book
		// gets absolute path of the web application
		// constructs path of the directory to save uploaded file
		String filename = "";
		String uploadPath = config.getUrlBookDir(request)
				+ config.UPLOAD_BOOK_DIR + File.separator
				+ bc.getIdbook();
		if (filePart != null) {
			try {
				// creates the save directory if it does not exists
				File fileSaveDir = new File(uploadPath);
				if (!fileSaveDir.exists()) {
					fileSaveDir.mkdirs();
				}

				// save file upload
				filename = getFileName(filePart);
				filePart.write(uploadPath + File.separator + filename);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (!filename.equals("")) {
			bc.setFilesize(SSUtil.humanReadableByteCount(filePart.getSize(), true));
			//Check picture category
			Book b = (new Book()).getById(bc.getIdbook());
			if (b.getIdcategory() == 8) {
				bc.setFilename(SSUtil.unZipIt(uploadPath + File.separator + filename,
						uploadPath + File.separator + bc.getIdbook_chapter()));
			} else {
				bc.setFilename(filename);
			}
		}
		
		bc.setChapter(chapter);
		

		out.println("<html>");
		out.println("<head>");
		out.println("  <meta http-equiv=\"refresh\" content=\"3;url=managebookchapter.jsp?idbook="
				+ bc.getIdbook() + "\" />");
		out.println(" </head>");
		out.println(" <body>");
		if (bc.updateBook_Chapter() == 1) {
			// add success
			out.println("<p align=\"center\"><font color=red>Sửa chapter thành công. Tự chuyển trang sau 3 giây</font></p>");
		} else {
			// add fail
			out.println("<p align=\"center\"><font color=red>Sửa chapter thất bại.</font></p>");
		}
		out.println("<p align=\"center\"><font color=red><a href='managebookchapter.jsp?idbook="
				+ bc.getIdbook()
				+ "'>Click vào đây để chuyển tiếp</a></font></p>");
		out.println(" </body>");
		out.println("</html>");
	}

}
