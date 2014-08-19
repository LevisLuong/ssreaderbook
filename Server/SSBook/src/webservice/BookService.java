package webservice;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import model.Book;
import model.Book_Category;
import model.Book_Chapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Path("/")
public class BookService {

	public String convertToJson(Object obj) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
				.create();
		String json = gson.toJson(obj);
		json = "{\"data\":" + json + "}";
		return json;
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
		}
		return bookjson;
	}

	@POST
	@Path("/GetBooksByCategory")
	@Produces("application/json;charset=utf-8")
	public String getBooksByCategory(@FormParam("idcategory") int idcategory,
			@FormParam("index") int index) {
		System.out.println("id category: " + idcategory);
		String bookjson = null;
		try {
			ArrayList<Book> books = (new Book()).getAllBookByCategory(
					idcategory, index);
			bookjson = convertToJson(books);
			System.out.println("book to json: " + bookjson);
		} catch (Exception e) {
			e.printStackTrace();
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
			ArrayList<Book> books = (new Book()).searchBook(keywork, 0, index);
			bookjson = convertToJson(books);
			System.out.println("book to json: " + bookjson);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bookjson;
	}

	@POST
	@Path("/GetChapterBook")
	@Produces("application/json;charset=utf-8")
	public String GetChapterBook(@FormParam("idbook") int idbook,
			@FormParam("index") int index) {
		String bookjson = null;
		try {
			ArrayList<Book_Chapter> bookchaps = (new Book_Chapter())
					.getByIdBook(idbook, index);
			// update countview book
			if (index == 1) {
				(new Book()).getById(idbook).updateCountView();
			}
			// convert array chapter to json
			bookjson = convertToJson(bookchaps);
			System.out.println("book to json: " + bookjson);
		} catch (Exception e) {
			e.printStackTrace();
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
			ArrayList<Book> books = (new Book()).getMostRead(index);
			// convert array chapter to json
			bookjson = convertToJson(books);
			System.out.println("book to json: " + bookjson);
		} catch (Exception e) {
			e.printStackTrace();
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
			ArrayList<Book> books = (new Book()).getNewest(index);
			// convert array chapter to json
			bookjson = convertToJson(books);
			System.out.println("book to json: " + bookjson);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bookjson;
	}
}
