package vn.seasoft.readerbook.ResultObjects;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import vn.seasoft.readerbook.HttpServices.ResultObject;
import vn.seasoft.readerbook.model.Book_Chapter;

import java.util.ArrayList;
import java.util.List;

public class Result_GetBookChapter implements ResultObject {
    public static final String TAG = "GET_CHAPTER_BOOK";
    public List<Book_Chapter> lstBookChaps;

    @Override
    public void setData(JSONObject obj) {
        Log.i(TAG, "get data from server: " + obj.toString());
        lstBookChaps = new ArrayList<Book_Chapter>();
        try {
            JSONArray jaBook = new JSONArray(obj.getString("data"));
            for (int i = 0; i < jaBook.length(); i++) {
                JSONObject Obj = jaBook.getJSONObject(i);
                Book_Chapter bc = new Book_Chapter();
                bc.setIdbook_chapter(Obj.getInt("idbook_chapter"));
                bc.setChapter(Obj.getString("chapter"));
                bc.setIdbook(Obj.getInt("idbook"));
                bc.setFilename(Obj.getString("filename").replace(" ","%20"));
                if (!Obj.isNull("filesize")) {
                    bc.setFilesize(Obj.getString("filesize"));
                }
                bc.addNewData();
                lstBookChaps.add(bc);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}