package vn.seasoft.readerbook.ResultObjects;

import org.json.JSONException;
import org.json.JSONObject;
import vn.seasoft.readerbook.HttpServices.ResultObject;

public class Result_UserDeleteComment implements ResultObject {
    public int idcomment = 0;

    /*
    Thanh cong tra ve status OK
     */
    @Override
    public void setData(JSONObject obj) {
        try {
            idcomment = obj.getInt("data");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}