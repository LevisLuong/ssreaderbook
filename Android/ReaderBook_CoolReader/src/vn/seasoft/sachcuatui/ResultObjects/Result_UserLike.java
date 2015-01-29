package vn.seasoft.sachcuatui.ResultObjects;

import org.json.JSONException;
import org.json.JSONObject;
import vn.seasoft.sachcuatui.HttpServices.ResultObject;

public class Result_UserLike implements ResultObject {
    public static final String TAG = "REGISTER_FACEBOOK";
    public int status = 0;

    /*
    Thanh cong tra ve status OK
     */

    @Override
    public void setData(JSONObject obj) {
        try {
            if (!obj.isNull("status")) {
                status = obj.getInt("status");
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}