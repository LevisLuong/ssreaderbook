package httpservlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import Others.config;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import model.Book;

/**
 * Servlet implementation class AddBook
 */
@WebServlet("/AddBook")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10, // 2MB
maxFileSize = 1024 * 1024 * 100, // 100MB
maxRequestSize = 1024 * 1024 * 500)
// 500MB
public class AddBook extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Directory where uploaded files will be saved, its relative to the web
	 * application directory.
	 */

	int BUFFER_LENGTH = 4096;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
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

		String title = request.getParameter("title");
		String author = request.getParameter("author");
		String summary = request.getParameter("summary");
		int idcategory = Integer.parseInt(request.getParameter("idcategory"));
		Part imgCoverPart = request.getPart("imagecover"); // cover

		System.out.println("title: " + title + ",author:" + author
				+ ",category:" + idcategory);
		Book book;
		try {
			book = new Book();
			// save cover
			// gets absolute path of the web application
			// constructs path of the directory to save uploaded file
			String coverfilename = "";
			if (imgCoverPart != null) {
				try {
					String uploadCoverPath = config.getUrlBookDir(request)
							+ config.UPLOAD_BOOK_DIR + book.getIdAuto()
							+ File.separator + config.UPLOAD_COVERBOOK_DIR;

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

			book.setImagecover(coverfilename);
			book.setTitle(title);
			book.setAuthor(author);
			book.setSummary(summary);
			book.setIdcategory(idcategory);
			book.setUploader((String)request.getSession().getAttribute("username"));
			
			out.println("<html>");
			out.println("<head>");
			out.println("  <meta http-equiv=\"refresh\" content=\"3;url=managebookchapter.jsp?idbook="
					+ book.getIdAuto()
					+ "&titlebook="
					+ book.getTitle()
					+ "\"/>");
			out.println(" </head>");
			out.println(" <body>");
			if (book.addBook() == 1) {
				// add success
				out.println("<p align=\"center\"><font color=red>Thêm sách thành công. Tự chuyển trang sau 3 giây</font></p>");
			} else {
				// add fail
				out.println("<p align=\"center\"><font color=red>Thêm sách thất bại.</font></p>");
			}
			out.println("<p align=\"center\"><font color=red><a href='managebookchapter.jsp?idbook="
					+ book.getIdAuto()
					+ "&titlebook="
					+ book.getTitle()
					+ "'>Click vào đây để chuyển tiếp</a></font></p>");
			out.println(" </body>");
			out.println("</html>");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			out.println("<html>");
			out.println("<head>");
			out.println(" </head>");
			out.println(" <body>");
			// add fail
			out.println("<p align=\"center\"><font color=red>Thêm Sách thất bại. Vui lòng kiểm tra và thử lại !</font></p>");
			out.println("<p align=\"center\"><font color=red><a href='addbook.jsp'>Click vào đây để up lại</a></font></p>");
			out.print("<p align='center' style='max-height: 150px;overflow: auto'>Lỗi cho developer: <pre class'javascript'>"
					+ e.getMessage() + "</pre></p>");
			out.println(" </body>");
			out.println("</html>");
		}

	}
}
