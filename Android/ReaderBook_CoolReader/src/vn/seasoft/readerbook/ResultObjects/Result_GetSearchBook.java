package vn.seasoft.readerbook.ResultObjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import vn.seasoft.readerbook.HttpServices.ResultObject;
import vn.seasoft.readerbook.model.Book;

import java.util.ArrayList;
import java.util.List;

public class Result_GetSearchBook implements ResultObject {
    public static final String TAG = "GET_SEARCH_BOOK";
    public List<Book> lstBooks;

    @Override
    public void setData(JSONObject obj) {
        lstBooks = new ArrayList<Book>();
        try {
            JSONArray jaBook = new JSONArray(obj.getString("data"));
            for (int i = 0; i < jaBook.length(); i++) {
                JSONObject Obj = jaBook.getJSONObject(i);
                Book book = new Book();
                book.setIdbook(Obj.getInt("idbook"));
                book.setTitle(Obj.getString("title"));
                book.setAuthor(Obj.getString("author"));
                book.setSummary(Obj.getString("summary"));
                book.setIdcategory(Obj.getInt("idcategory"));
                book.setCountview(Obj.getInt("countview"));
                book.setCountdownload(Obj.getInt("countdownload"));
                if (!Obj.isNull("imagecover"))
                    book.setImagecover(Obj.getString("imagecover").replace(" ", "%20"));
                lstBooks.add(book);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}