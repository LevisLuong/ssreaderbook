package vn.seasoft.readerbook.RequestObjects;

import android.content.Context;
import vn.seasoft.readerbook.HttpServices.COMMAND_API;
import vn.seasoft.readerbook.HttpServices.RequestObject;
import vn.seasoft.readerbook.ResultObjects.*;
import vn.seasoft.readerbook.Util.SSUtil;

public class Request_Server extends RequestObject {
    Context mContext;

    public Request_Server(Context context) {
        super(context);
        mContext = context;
    }

    public void getCommentsBook(int idbook, int index) {
        request.initParam();
        request.addParam("idbook", idbook);
        request.addParam("index", index);
        request.requestStringToServer(new Result_GetCommentsBook(), COMMAND_API.GET_COMMENTS);
    }

    public void userCommentBook(int idbook, String iduserfacebook, String username, String content) {
        request.initParam();
        request.addParam("idbook", idbook);
        request.addParam("iduserfacebook", iduserfacebook);
        request.addParam("username", username);
        request.addParam("content", content);
        request.requestStringToServer(new Result_UserComment(), COMMAND_API.USER_COMMENT);
    }

    public void userLikeBook(int idbook, int iduser) {
        request.initParam();
        request.addParam("idbook", idbook);
        request.addParam("iduser", iduser);
        request.requestStringToServer(new Result_UserLike(), COMMAND_API.USER_LIKE);
    }

    public void userDisLikeBook(int idbook, int iduser) {
        request.initParam();
        request.addParam("idbook", idbook);
        request.addParam("iduser", iduser);
        request.requestStringToServer(new Result_UserDisLike(), COMMAND_API.USER_DISLIKE);
    }

    public void getCountLikeBook(int idbook) {
        request.initParam();
        request.addParam("idbook", idbook);
        request.requestStringToServer(new Result_GetCountLikeBook(), COMMAND_API.GET_COUNT_LIKE_BOOK);
    }

    public void isUserLikeBook(int idbook, int iduser) {
        request.initParam();
        request.addParam("idbook", idbook);
        request.addParam("iduser", iduser);
        request.requestStringToServer(new Result_GetIsUserLikeBook(), COMMAND_API.IS_USER_LIKE_BOOK);
    }


    public void loginByFacebook(String iduserfacebook, String displayname, String email) {
        request.initParam();
        request.addParam("idfacebook", iduserfacebook);
        request.addParam("displayname", displayname);
        request.addParam("email", email);
        request.requestStringToServer(new Result_LoginByFacebook(), COMMAND_API.LOGIN_BY_FACEBOOK);
    }

    public void addFeedBack(String titlebook, String authorbook, String feedback) {
        request.initParam();
        request.addParam("titlebook", titlebook);
        request.addParam("authorbook", authorbook);
        request.addParam("feedback", feedback);
        request.requestStringToServer(new Result_GetFeedBack(), COMMAND_API.ADD_FEEDBACK);
    }

    public void addCountBook(int idbook) {
        request.initParam();
        request.addParam("idbook", idbook);
        request.requestStringToServer(null, COMMAND_API.ADD_COUNT_BOOK);
    }

    public void setUserOnline() {
        request.initParam();
        request.addParam("imei", SSUtil.loadIDDevice(mContext));
        request.requestStringToServer(null, COMMAND_API.SET_USER_ONLINE);
    }

    public void setUserOffline() {
        request.initParam();
        request.addParam("imei", SSUtil.loadIDDevice(mContext));
        request.requestStringToServer(null, COMMAND_API.SET_USER_OFFLINE);
    }

    public void getCategoryBook() {
        request.initParam();
        request.requestStringToServer(new Result_GetCategory(), COMMAND_API.GET_CATEGORY_BOOK);
    }

    public void getMostBook(int index) {
        request.initParam();
        request.addParam("index", index);
        request.requestStringToServer(new Result_GetMostRead(), COMMAND_API.GET_MOST_READ);
    }

    public void getNewest(int index) {
        request.initParam();
        request.addParam("index", index);
        request.requestStringToServer(new Result_GetNewest(), COMMAND_API.GET_NEWEST);
    }

    public void getBookByCategory(int idcategory, int index) {
        request.initParam();
        request.addParam("idcategory", idcategory);
        request.addParam("index", index);
        request.requestStringToServer(new Result_GetBookByCategory(), COMMAND_API.GET_BOOK_BY_CATEGORY);
    }

    public void SearchBook(String key, int index) {
        request.initParam();
        request.addParam("keywork", key);
        request.addParam("index", index);
        request.requestStringToServer(new Result_GetSearchBook(), COMMAND_API.SEARCH_BOOK);
    }

    public void getBookChapter(int idbook, int index) {
        request.initParam();
        request.addParam("idbook", idbook);
        request.addParam("index", index);
        request.requestStringToServer(new Result_GetBookChapter(), COMMAND_API.GET_BOOK_CHAPTER);
    }
}
