package vn.seasoft.readerbook.RequestObjects;

import android.content.Context;
import vn.seasoft.readerbook.HttpServices.COMMAND_API;
import vn.seasoft.readerbook.HttpServices.RequestObject;
import vn.seasoft.readerbook.ResultObjects.*;

public class Request_Server extends RequestObject {
    Context mContext;

    public Request_Server(Context context) {
        super(context);
        mContext = context;
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
