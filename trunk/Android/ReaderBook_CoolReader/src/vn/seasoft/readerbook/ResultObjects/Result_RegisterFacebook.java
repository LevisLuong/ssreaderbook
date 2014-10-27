package vn.seasoft.readerbook.ResultObjects;

import org.json.JSONException;
import org.json.JSONObject;
import vn.seasoft.readerbook.HttpServices.ResultObject;

public class Result_RegisterFacebook implements ResultObject {
    public static final String TAG = "REGISTER_FACEBOOK";
    public int status = 0;

    public int idUser;
    public String displayName;

    /*
    dang ky thanh cong tra ve user
     */
    @Override
    public void setData(JSONObject obj) {
        try {
            JSONObject Obj = obj.getJSONObject("data");
            idUser = Obj.getInt("iduser");
            displayName = Obj.getString("displayname");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}