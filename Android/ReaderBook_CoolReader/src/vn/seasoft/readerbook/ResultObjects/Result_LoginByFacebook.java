package vn.seasoft.readerbook.ResultObjects;

import org.json.JSONException;
import org.json.JSONObject;
import vn.seasoft.readerbook.HttpServices.ResultObject;

public class Result_LoginByFacebook implements ResultObject {
    public static final String TAG = "LOGIN_BY_FACEBOOK";
    public int status = 0;

    public int idUser;
    public String idUserFacebook;
    public String displayName;

    /*
    _ dang nhap thanh cong tra ve user
    _ chua dang ky tra ve status STATUS_CREATED de gui len register facebook
     */
    @Override
    public void setData(JSONObject obj) {
        try {
            if (obj.isNull("status")) {
                JSONObject ObjData = obj.getJSONObject("data");
                idUserFacebook = ObjData.getString("idfacebook");
                displayName = ObjData.getString("displayname");
                idUser = ObjData.getInt("iduser");
            } else {
                status = obj.getInt("status");
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}