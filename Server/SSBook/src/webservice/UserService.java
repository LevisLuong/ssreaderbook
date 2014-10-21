package webservice;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import model.Book;
import model.Comment;
import model.User_Login;
import Others.Status;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Path("/user")
public class UserService {

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
		return "{\"status\":" + status + "}";
	}

	public String returnError(int error) {

		return "{\"errorcode\":" + error + "}";
	}

	@POST
	@Path("/LoginByFacebook")
	@Produces("application/json;charset=utf-8")
	public String loginUserByFacebook(@FormParam("idfacebook") String idfacebook) {
		String json = "";
		User_Login user = new User_Login().loginByFacebook(idfacebook);
		if (user != null) {
			user.setLastlogin(new Timestamp(new Date().getTime()));
			user.updateLastlogin();
			json = convertToJson(user);
		} else {
			json = returnStatus(Status.STATUS_CREATED);
		}
		return json;
	}

	@POST
	@Path("/RegisterUserFacebook")
	@Produces("application/json;charset=utf-8")
	public String registerUserFacebook(
			@FormParam("idfacebook") String idfacebook,
			@FormParam("displayname") String displayname,
			@FormParam("email") String email) {
		User_Login user = new User_Login();
		user.setIdfacebook(idfacebook);
		user.setDisplayname(displayname);
		user.setEmail(email);
		user.setIduser(user.getIdAuto());
		int status = user.addUser();
		if (status == Status.ERROR_INTERNALSERVERERROR) {
			return returnError(Status.ERROR_INTERNALSERVERERROR);
		} else {
			return convertToJson(user);
		}
	}

	@POST
	@Path("/UserLike")
	@Produces("application/json;charset=utf-8")
	public String userLike(@FormParam("idbook") int idbook) {
		Book book = new Book().getById(idbook);
		int status = book.addUserLike(idbook);
		if (status == 0) {
			return returnError(Status.ERROR_INTERNALSERVERERROR);
		} else {
			return returnStatus(Status.STATUS_OK);
		}
	}

	@POST
	@Path("/UserComment")
	@Produces("application/json;charset=utf-8")
	public String userComment(@FormParam("idbook") int idbook,
			@FormParam("iduser") int iduser,
			@FormParam("content") String content) {
		Comment comment = new Comment();
		comment.setContent(content);
		comment.setIdbook(idbook);
		comment.setIduser(iduser);
		int status = comment.addComment();
		if (status == 0) {
			return returnError(Status.ERROR_INTERNALSERVERERROR);
		} else {
			return returnStatus(Status.STATUS_OK);
		}
	}

	@POST
	@Path("/GetCommentsBook")
	@Produces("application/json;charset=utf-8")
	public String getCommentsBook(@FormParam("idbook") int idbook,
			@FormParam("index") int index) {
		String json = "";
		try {
			List<Comment> comment = new Comment().getListCommentByIdBook(
					idbook, index);
			json = convertToJson(comment);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return returnError(Status.ERROR_INTERNALSERVERERROR);
		}
		return json;
	}
}
