package vn.seasoft.readerbook.ResultObjects;

import org.json.JSONException;
import org.json.JSONObject;
import vn.seasoft.readerbook.HttpServices.ResultObject;

public class Result_GetCountLikeBook implements ResultObject {
    public int countlike = 0;

    /*
    Thanh cong tra ve so luong like cua sach
     */

    @Override
    public void setData(JSONObject obj) {
        try {
            countlike = obj.getInt("countlike");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}