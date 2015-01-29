package vn.seasoft.sachcuatui.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;
import org.holoeverywhere.widget.Toast;
import vn.seasoft.sachcuatui.SSReaderApplication;
import vn.seasoft.sachcuatui.listener.IActionFacebook;

public class asynPostFacebook extends AsyncTask<Void, Void, Integer> {

    String titlebook;
    String authorbook;
    Context mContext;
    Bitmap image;
    String content;
    int result = 0;

    public asynPostFacebook(Context context, String _content, Bitmap imgCover) {
        mContext = context;
        image = imgCover;
        content = _content;
    }

    private Bitmap addText(Bitmap b) {
        Bitmap workingBitmap = Bitmap.createBitmap(b);
        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas c = new Canvas(mutableBitmap);
        Paint paint = new Paint();
        paint.setColor(Color.CYAN);
        paint.setTextSize(20);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        // draw text to the Canvas center
        // overlay background
        c.drawText("Sách Của Tui", 100, 100, paint);
        c.drawPoint(30.0f, 50.0f, paint);
        return mutableBitmap;
    }

    @Override
    protected Integer doInBackground(Void... arg0) {
        // TODO Auto-generated method stub
        content = content + ". Thưởng thức thêm những cuốn Sách - Truyện hay tại: http://goo.gl/vDxSKx #SáchCủaTui";
        try {
            if (image != null) {
                result = SSReaderApplication.getSocialAdapter().uploadImage(content, "upload_book.jpg", image, 100);
                //                SSReaderApplication.getSocialAdapter().updateStory("test update story facebook", "update name", "captio", "descrition", "http://goo.gl/vDxSKx", "http://1.bp.blogspot.com/-IWytx68TUxw/TxmawGii-BI/AAAAAAAABVk/_TusUdVRAN0/s1600/twitter+facebook.jpg", new SocialAuthListener<Integer>() {
                //                    @Override
                //                    public void onExecute(String s, Integer integer) {
                //
                //                    }
                //
                //                    @Override
                //                    public void onError(SocialAuthError socialAuthError) {
                //
                //                    }
                //                });
            } else {
                SSReaderApplication.postToWall(mContext, new IActionFacebook() {
                    @Override
                    public void LoginSuccess() {
                        SSReaderApplication.getSocialAdapter().updateStatus(content, new SocialAuthListener<Integer>() {
                            @Override
                            public void onExecute(String s, Integer integer) {

                            }

                            @Override
                            public void onError(SocialAuthError socialAuthError) {

                            }
                        }, true);
                    }
                });
                result = 200;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(Integer result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        if (result == 200) {
            Toast.makeText(mContext, "Đã chia sẻ lên Facebook !", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "Chia sẻ thất bại. Vui lòng thử lại !", Toast.LENGTH_SHORT).show();
        }
    }
}
