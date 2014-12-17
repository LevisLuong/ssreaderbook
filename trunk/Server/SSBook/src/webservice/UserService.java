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

import model.Comment;
import model.User_Like;
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
	public String loginUserByFacebook(
			@FormParam("idfacebook") String idfacebook,
			@FormParam("displayname") String displayname,
			@FormParam("email") String email) {
		String json = "";
		User_Login user = new User_Login().loginByFacebook(idfacebook);
		if (user != null) {
			user.setLastlogin(new Timestamp(new Date().getTime()));
			user.setDisplayname(displayname);
			user.updateLastloginDisplayName();
			json = convertToJson(user);
		} else {
			user = new User_Login();
			user.setIduser(user.getIdAuto());
			user.setIdfacebook(idfacebook);
			user.setDisplayname(displayname);
			user.setEmail(email);
			user.setLastlogin(new Timestamp(new Date().getTime()));
			int status = user.addUser();
			if (status == Status.STATUS_CREATED) {
				json = convertToJson(user);
			} else {
				return returnError(Status.ERROR_INTERNALSERVERERROR);
			}

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
	@Path("/getCountLikeBook")
	@Produces("application/json;charset=utf-8")
	public String getCountLikeBook(@FormParam("idbook") int idbook) {
		int countlike = 0;
		countlike = new User_Like().getCountLikeBook(idbook);

		return "{\"countlike\":" + countlike + "}";
	}

	@POST
	@Path("/getIsUserLikeBook")
	@Produces("application/json;charset=utf-8")
	public String getIsUserLikeBook(@FormParam("idbook") int idbook,
			@FormParam("iduser") int iduser) {
		int countlike = 0;
		countlike = new User_Like().isUserLikeBook(idbook, iduser);

		return "{\"isuserlike\":" + countlike + "}";
	}

	@POST
	@Path("/UserLike")
	@Produces("application/json;charset=utf-8")
	public String userLike(@FormParam("idbook") int idbook,
			@FormParam("iduser") int iduser) {
		User_Like userlike = new User_Like();
		userlike.setIdbook(idbook);
		userlike.setIduser(iduser);
		int status = userlike.userLike();
		if (status == Status.ERROR_INTERNALSERVERERROR) {
			return returnError(Status.ERROR_INTERNALSERVERERROR);
		} else {
			return returnStatus(Status.STATUS_OK);
		}
	}

	@POST
	@Path("/UserDisLike")
	@Produces("application/json;charset=utf-8")
	public String userDisLike(@FormParam("idbook") int idbook,
			@FormParam("iduser") int iduser) {
		User_Like userlike = new User_Like();
		userlike.setIdbook(idbook);
		userlike.setIduser(iduser);
		int status = userlike.userdisLike();
		if (status == Status.ERROR_INTERNALSERVERERROR) {
			return returnError(Status.ERROR_INTERNALSERVERERROR);
		} else {
			return returnStatus(Status.STATUS_OK);
		}
	}

	@POST
	@Path("/UserEditComment")
	@Produces("application/json;charset=utf-8")
	public String userEditComment(@FormParam("idcomment") int idcomment,
			@FormParam("content") String content) {
		Comment comment = new Comment().getByID(idcomment);
		comment.setContent(content);
		int status = comment.updateComment();
		if (status == 0) {
			return returnError(Status.ERROR_INTERNALSERVERERROR);
		} else {
			return "{\"data\":" + idcomment + "}";
		}
	}

	@POST
	@Path("/UserDeleteComment")
	@Produces("application/json;charset=utf-8")
	public String userDeleteComment(@FormParam("idcomment") int idcomment) {
		Comment comment = new Comment().getByID(idcomment);
		int status = comment.deleteComment();
		if (status == 0) {
			return returnError(Status.ERROR_INTERNALSERVERERROR);
		} else {
			return "{\"data\":" + idcomment + "}";
		}
	}

	@POST
	@Path("/UserComment")
	@Produces("application/json;charset=utf-8")
	public String userComment(@FormParam("idbook") int idbook,
			@FormParam("iduser") int iduser,
			@FormParam("content") String content) {
		Comment comment = new Comment();
		comment.setIdcomment(comment.getIdAuto());
		comment.setContent(content);
		comment.setIdbook(idbook);
		comment.setIduser(iduser);
		int status = comment.addComment();
		if (status == 0) {
			return returnError(Status.ERROR_INTERNALSERVERERROR);
		} else {
			return "{\"data\":" + comment.getIdcomment() + "}";
		}
	}

	@POST
	@Path("/GetCommentsBook")
	@Produces("application/json;charset=utf-8")
	public String getCommentsBook(@FormParam("idbook") int idbook,
			@FormParam("index") int index) {
		String json = "";
		try {
			List<Comment> comments = new Comment().getListCommentByIdBook(
					idbook, index);
			json = "{\"data\":[";
			for (Comment comment : comments) {
				User_Login user = new User_Login().getByID(comment.getIduser());
				json = json + "{\"idcomment\":" + comment.getIdcomment()
						+ ",\"content\":\"" + comment.getContent()
						+ "\",\"iduser\":" + comment.getIduser()
						+ ",\"username\":\"" + user.getDisplayname()
						+ "\",\"iduserfacebook\":\"" + user.getIdfacebook()
						+ "\",\"idbook\":" + comment.getIdbook()
						+ ",\"datecreated\":\"" + comment.getDatecreated()
						+ "\"},";

			}
			json = json + "]}";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return returnError(Status.ERROR_INTERNALSERVERERROR);
		}
		return json;
	}
}
