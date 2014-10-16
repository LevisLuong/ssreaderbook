package vn.seasoft.readerbook.ResultObjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import vn.seasoft.readerbook.HttpServices.ResultObject;
import vn.seasoft.readerbook.Util.GlobalData;
import vn.seasoft.readerbook.model.Book_Category;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class Result_GetCategory implements ResultObject {
    public static final String TAG = "GET_CATEGORY";
    public List<Book_Category> lstBookCategory;

    @Override
    public void setData(final JSONObject obj) {
        lstBookCategory = new ArrayList<Book_Category>();
        try {
            GlobalData.repo.book_category.getDao().callBatchTasks(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    JSONArray jaCate = new JSONArray(obj.getString("data"));
                    for (int i = 0; i < jaCate.length(); i++) {
                        JSONObject cateObj = jaCate.getJSONObject(i);
                        Book_Category book_category = new Book_Category();
                        book_category.setIdcategory(cateObj.getInt("idcategory"));
                        book_category.setCategory(cateObj.getString("category"));
                        book_category.addNewData();
                        lstBookCategory.add(book_category);
                    }
                    return null;
                }
            });
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}