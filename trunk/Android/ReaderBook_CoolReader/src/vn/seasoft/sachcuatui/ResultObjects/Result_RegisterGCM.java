package vn.seasoft.sachcuatui.ResultObjects;

import org.json.JSONException;
import org.json.JSONObject;
import vn.seasoft.sachcuatui.HttpServices.ResultObject;

public class Result_RegisterGCM implements ResultObject {
    public static final String TAG = "REGISTER_GCM";
    public String regId = "";

    /*
    Thanh cong tra ve status OK
     */

    @Override
    public void setData(JSONObject obj) {
        try {
            if (!obj.isNull("regId")) {
                regId = obj.getString("regId");
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}