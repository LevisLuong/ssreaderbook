package webservice;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import model.Book;
import model.Book_Category;
import model.Book_Chapter;
import model.FeedBack;
import model.User_Online;
import Others.Status;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Path("/")
public class BookService {

	@Context
	HttpServletRequest request;

	public String convertToJson(Object obj) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
				.create();
		String json = gson.toJson(obj);
		json = "{\"data\":" + json + "}";
		return json;
	}

	public String returnStatus(int status) {
		return "{\"status\":\"" + status + "\"}";
	}

	public String returnError(int error) {
		return "{\"errorcode\":\"" + error + "\"}";
	}

	@POST
	@Path("/GetCategory")
	@Produces("application/json;charset=utf-8")
	public String categoryBook() {
		String bookjson = null;
		try {
			ArrayList<Book_Category> bookCategory = (new Book_Category())
					.getAllCategory();
			bookjson = convertToJson(bookCategory);
			System.out.println("book to json: " + bookjson);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError(Status.ERROR_INTERNALSERVERERROR);
		}
		return bookjson;
	}

	/*
	 * Cac ham lien quan den nguoi dung
	 */
	@POST
	@Path("/UserOnline")
	@Produces("application/json;charset=utf-8")
	public String UserOnline(@FormParam("imei") String imei) {
		AddUserOnline(imei);
		return "{}";
	}

	@POST
	@Path("/UserLeaveApp")
	@Produces("application/json;charset=utf-8")
	public String UserLeft(@FormParam("imei") String imei) {
		(new User_Online())
				.deleteUserByIP(request.getRemoteAddr() + "|" + imei);
		return "{}";
	}

	@POST
	@Path("/UserFeedBack")
	@Produces("application/json;charset=utf-8")
	public String UserFeedBack(@FormParam("titlebook") String titlebook,
			@FormParam("authorbook") String authorbook,
			@FormParam("feedback") String feedback) {
		FeedBack fb = new FeedBack();
		fb.setTitlebook(titlebook);
		fb.setAuthorbook(authorbook);
		fb.setFeedback(feedback);
		fb.addFeedback();
		return "{}";
	}

	public void updateCountUserOnline(String imei) {
		User_Online uo = (new User_Online()).getUserOnlineByIP(request
				.getRemoteAddr() + "|" + imei);
		uo.updateUserOnline();
	}

	public void AddUserOnline(String imei) {
		User_Online uo = new User_Online();
		uo.setIpuser(request.getRemoteAddr() + "|" + imei);
		uo.addUserOnline();
	}

	/*
	 * Cac services lien quan den book chapter
	 */
	@POST
	@Path("/GetChapterBook")
	@Produces("application/json;charset=utf-8")
	public String GetChapterBook(@FormParam("idbook") int idbook,
			@FormParam("index") int index) {
		String bookjson = null;
		try {
			ArrayList<Book_Chapter> bookchaps = (new Book_Chapter())
					.getByIdBook(idbook, index);
			// convert array chapter to json
			bookjson = convertToJson(bookchaps);
			if (index <= 1) {
				(new Book()).getById(idbook).updateCountView();
			}
			System.out.println("book to json: " + bookjson);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError(Status.ERROR_INTERNALSERVERERROR);
		}
		return bookjson;
	}

	@POST
	@Path("/GetAllChapterBook")
	@Produces("application/json;charset=utf-8")
	public String GetAllChapterBook(@FormParam("idbook") int idbook) {
		String bookjson = null;
		try {
			ArrayList<Book_Chapter> bookchaps = (new Book_Chapter())
					.getAllByIdBook(idbook);
			// convert array chapter to json
			bookjson = convertToJson(bookchaps);
			System.out.println("book to json: " + bookjson);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError(Status.ERROR_INTERNALSERVERERROR);
		}
		return bookjson;
	}

	@POST
	@Path("/SetApproveChapter")
	@Produces("application/json;charset=utf-8")
	public String SetApproveChapter(
			@FormParam("idbookchapter") int idbookchapter,
			@FormParam("status") int status) {
		String bookjson = null;
		try {
			Book_Chapter bc = new Book_Chapter();
			bc.setIdbook_chapter(idbookchapter);
			bc.setApprovedDatabase(status);
			// convert array chapter to json
			bookjson = "{}";
		} catch (Exception e) {
			e.printStackTrace();
			return returnError(Status.ERROR_INTERNALSERVERERROR);
		}
		return bookjson;
	}

	/*
	 * Cac service lien quan den book
	 */
	@POST
	@Path("/GetBooksByCategory")
	@Produces("application/json;charset=utf-8")
	public String getBooksByCategory(@FormParam("idcategory") int idcategory,
			@FormParam("index") int index) {
		System.out.println("id category: " + idcategory);
		String bookjson = null;
		try {
			ArrayList<Book> books = (new Book()).getAllBookByCategory(
					idcategory, index, true);
			bookjson = convertToJson(books);
			System.out.println("book to json: " + bookjson);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError(Status.ERROR_INTERNALSERVERERROR);
		}
		return bookjson;
	}

	@POST
	@Path("/SearchBook")
	@Produces("application/json;charset=utf-8")
	public String SearchBook(@FormParam("keywork") String keywork,
			@FormParam("index") int index) {
		String bookjson = null;
		try {
			ArrayList<Book> books = (new Book()).searchBook(keywork, 0, index,
					true);
			bookjson = convertToJson(books);
			bookjson = bookjson + keywork + "}";
			System.out.println("book to json: " + bookjson);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError(Status.ERROR_INTERNALSERVERERROR);
		}
		return bookjson;
	}

	@POST
	@Path("/GetMostRead")
	@Produces("application/json;charset=utf-8")
	public String GetMostRead(@FormParam("index") int index) {
		String bookjson = null;
		try {
			if (index == 0) {
				index = 1;
			}
			ArrayList<Book> books = (new Book()).getMostRead(index, true);
			// convert array chapter to json
			bookjson = convertToJson(books);
			System.out.println("book to json: " + bookjson);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError(Status.ERROR_INTERNALSERVERERROR);
		}
		return bookjson;
	}

	@POST
	@Path("/GetNewest")
	@Produces("application/json;charset=utf-8")
	public String GetNewest(@FormParam("index") int index) {
		String bookjson = null;
		try {
			if (index == 0) {
				index = 1;
			}
			ArrayList<Book> books = (new Book()).getNewest(index, true);
			// convert array chapter to json
			bookjson = convertToJson(books);
			System.out.println("book to json: " + bookjson);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError(Status.ERROR_INTERNALSERVERERROR);
		}
		return bookjson;
	}

	@POST
	@Path("/AddCountBook")
	@Produces("application/json;charset=utf-8")
	public String AddCountBook(@FormParam("idbook") int idbook) {
		try {
			(new Book()).getById(idbook).updateCountDownload();
		} catch (Exception e) {
			e.printStackTrace();
			return returnError(Status.ERROR_INTERNALSERVERERROR);
		}
		return "{}";
	}
}
