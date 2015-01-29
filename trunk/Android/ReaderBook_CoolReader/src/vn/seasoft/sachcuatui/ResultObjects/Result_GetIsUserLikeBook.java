package vn.seasoft.sachcuatui.ResultObjects;

import org.json.JSONException;
import org.json.JSONObject;
import vn.seasoft.sachcuatui.HttpServices.ResultObject;

public class Result_GetIsUserLikeBook implements ResultObject {
    public boolean isUserLike = false;

    /*
    Thanh cong tra ve so luong like cua sach
     */

    @Override
    public void setData(JSONObject obj) {
        try {
            if (obj.getInt("isuserlike") == 0) {
                isUserLike = false;
            } else {
                isUserLike = true;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}