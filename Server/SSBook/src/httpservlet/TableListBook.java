package httpservlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import Others.config;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Book;
import model.Book_Category;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

@WebServlet("/TableListBook")
public class TableListBook extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public TableListBook() {

	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String key = request.getParameter("key") == null ? "" : request
				.getParameter("key");
		int typesearch = 1;
		if (request.getParameter("typesearch") != null) {
			try {
				typesearch = Integer.parseInt(request
						.getParameter("typesearch"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		int category = 0;
		if (request.getParameter("category") != null) {
			try {
				category = Integer.parseInt(request.getParameter("category"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		int typeOrder = 1;
		if (request.getParameter("order") != null) {
			try {
				typeOrder = Integer.parseInt(request.getParameter("order"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		int page = 1;
		String parampage = request.getParameter("page");
		if (parampage != null) {
			try {
				page = Integer.parseInt(parampage);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		PrintWriter out = response.getWriter();
		Book bookQuery = new Book();
		ArrayList<Book> books = new ArrayList<Book>();
		try {
			int role = (int) request.getSession().getAttribute("role");
			books = bookQuery.searchBook(typesearch, key, category, page,
					typeOrder);
			int i = 1;
			for (Book book : books) {
				Book_Category bc = (new Book_Category()).getById(book
						.getIdcategory());
				if (book.getApproved() == 0) {
					out.println("<tr class=\"not-approved\">");
				}else{
					if (i % 2 == 0) {
						out.println("<tr class=\"alternate-row\">");
					} else {
						out.println("<tr>");
					}
				}
				
				String summary = book.getSummary();
				if (summary.length() > 100) {
					summary = summary.substring(0, 100) + "...";
				}
				out.println("<td><a href=\"managebookchapter.jsp?idbook="
						+ book.getIdBook() + "&titlebook=" + book.getTitle()
						+ "\">" + book.getIdBook() + "</a></td>");
				
				out.println("<td><a href=\"managebookchapter.jsp?idbook="
						+ book.getIdBook() + "&titlebook=" + book.getTitle()
						+ "\">" + book.getTitle() + "</a></td>");
				out.println("<td>" + book.getAuthor() + "</td>");
				out.println("<td>" + summary + "</td>");
				out.println("<td ><p align='center'><img border=\"0\" src=\".."
						+ File.separator
						+ config.UPLOAD_BOOK_DIR
						+ File.separator
						+ book.getIdBook()
						+ File.separator
						+ config.UPLOAD_COVERBOOK_DIR
						+ File.separator
						+ book.getImagecover()
						+ "\" width=\"60\" height=\"80\"></p><p align='center'>"
						+ book.getImagecover() + "</p></td>");
				if (bc != null) {
					out.println("<td>" + bc.getCategory() + "</td>");
				} else {
					out.println("<td>Chưa có danh mục</td>");
				}

				out.println("<td>" + book.countChapter() + "</td>");
				
				if (book.getUploader() == null) {
					out.println("<td></td>");
				} else {
					out.println("<td>" + book.getUploader() + "</td>");
				}
				out.println("<td>");
				if (role == 1) {
					out.println("<a title='Delete' class=\"icon-2 info-tooltip\" href=\"#\" onclick=\"confirmSubmit("
							+ book.getIdBook() + ")\"></a>");
				}
				out.println("<a title='Edit' class=\"icon-1 info-tooltip\" href=\"editbook.jsp?idbook="
						+ book.getIdBook() + " \"></a>" + "</td>");
				out.println("</tr>");
				i++;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int numberOfPages = (int) Math.ceil(bookQuery.getBooksCount() * 1.0
				/ Book.DatasOnPage);

		request.setAttribute("numberOfPages", numberOfPages);
		request.setAttribute("currentPage", page);
		request.setAttribute("key", key);
		request.setAttribute("category", category);
		request.setAttribute("order", typeOrder);
		request.setAttribute("typesearch", typesearch);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

}