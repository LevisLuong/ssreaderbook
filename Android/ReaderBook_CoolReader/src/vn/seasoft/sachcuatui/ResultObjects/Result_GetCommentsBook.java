package vn.seasoft.sachcuatui.ResultObjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import vn.seasoft.sachcuatui.HttpServices.ResultObject;
import vn.seasoft.sachcuatui.Util.GlobalData;
import vn.seasoft.sachcuatui.model.Comment;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Result_GetCommentsBook implements ResultObject {
    public static final String TAG = "GET_COMMENTS_FACEBOOK";
    public int status = 0;

    public List<Comment> lstComment;

    /*
    Tra ve list comments
     */
    @Override
    public void setData(JSONObject obj) {
        lstComment = new ArrayList<Comment>();

        try {
            JSONArray jaComment = new JSONArray(obj.getString("data"));
            for (int i = 0; i < jaComment.length(); i++) {
                JSONObject job = jaComment.getJSONObject(i);
                Comment comment = new Comment();
                comment.setIdcomment(job.getInt("idcomment"));
                comment.setIdbook(job.getInt("idbook"));
                comment.setIduser(job.getInt("iduser"));
                comment.setIduserfacebook(job.getString("iduserfacebook"));
                comment.setUsername(job.getString("username"));
                comment.setContent(job.getString("content"));
                try {
                    comment.setDatecomment(GlobalData.simpleDateFormat.parse(job.getString("datecreated")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                lstComment.add(comment);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}