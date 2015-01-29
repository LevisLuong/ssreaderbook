package vn.seasoft.sachcuatui.Util;

import android.content.Context;
import org.holoeverywhere.app.ProgressDialog;
import vn.seasoft.sachcuatui.HttpServices.Config;
import vn.seasoft.sachcuatui.model.Book;
import vn.seasoft.sachcuatui.model.Book_Chapter;
import vn.seasoft.sachcuatui.sqlite.RepoController;

import java.text.SimpleDateFormat;

/**
 * User: XuanTrung
 * Date: 7/9/2014
 * Time: 9:44 AM
 */
public class GlobalData {
    public static final String LOCATION_SAVE_BOOK = "SSBooks";
    public static final String PICTURE_BOOK = "Picture_Books";
    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static RepoController repo;
    static ProgressDialog progressDialog;

    public static void ShowProgressDialog(Context context, String msg) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    public static void ShowProgressDialog(Context context, int res) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(res));
        progressDialog.show();
    }

    public static void DissmissProgress() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    public static String getUrlImageCover(Book book) {
        return Config.IP_BOOKS_FILE_SERVER + book.getIdbook() + Config.FOLDER_BOOK_COVER + "/" + book.getImagecover();
    }

    public static String getUrlBook(Book_Chapter bookchap) {
        return Config.IP_BOOKS_FILE_SERVER + bookchap.getIdbook() + "/" + bookchap.getFilename();
    }

    public static String getUrlPictureBook(int idbook, int idbookchapter) {
        return Config.IP_BOOKS_FILE_SERVER + idbook + "/" + idbookchapter + "/";
    }
}
