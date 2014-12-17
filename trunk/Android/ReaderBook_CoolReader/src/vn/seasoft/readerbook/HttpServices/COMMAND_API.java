package vn.seasoft.readerbook.HttpServices;

/**
 * Created by XuanTrung on 6/30/2014.
 */
public class COMMAND_API {
    public final static String ADD_FEEDBACK = "api/UserFeedBack";
    public final static String ADD_COUNT_BOOK = "api/AddCountBook";
    public final static String SET_USER_ONLINE = "api/UserOnline";
    public final static String SET_USER_OFFLINE = "api/UserLeaveApp";
    public final static String GET_CATEGORY_BOOK = "api/GetCategory";
    public final static String SEARCH_BOOK = "api/SearchBook";
    public final static String GET_BOOK_BY_CATEGORY = "api/GetBooksByCategory";
    public final static String GET_BOOK_CHAPTER = "api/GetChapterBook";
    public final static String GET_MOST_READ = "api/GetMostRead";
    public final static String GET_NEWEST = "api/GetNewest";
    //User
    public final static String LOGIN_BY_FACEBOOK = "api/user/LoginByFacebook";
    public final static String REGISTER_FACEBOOK = "api/user/RegisterUserFacebook";

    public final static String USER_LIKE = "api/user/UserLike";
    public final static String USER_DISLIKE = "api/user/UserDisLike";
    public final static String GET_COUNT_LIKE_BOOK = "api/user/getCountLikeBook";
    public final static String IS_USER_LIKE_BOOK = "api/user/getIsUserLikeBook";

    public final static String USER_COMMENT = "api/user/UserComment";
    public final static String USER_EDIT_COMMENT = "api/user/UserEditComment";
    public final static String USER_DELETE_COMMENT = "api/user/UserDeleteComment";
    public final static String GET_COMMENTS = "api/user/GetCommentsBook";
}
