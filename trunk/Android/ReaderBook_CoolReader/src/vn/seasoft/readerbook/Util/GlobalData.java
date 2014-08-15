package vn.seasoft.readerbook.Util;

import android.content.Context;
import org.holoeverywhere.app.ProgressDialog;
import vn.seasoft.readerbook.HttpServices.Config;
import vn.seasoft.readerbook.model.Book;
import vn.seasoft.readerbook.model.Book_Chapter;
import vn.seasoft.readerbook.sqlite.RepoController;

/**
 * User: XuanTrung
 * Date: 7/9/2014
 * Time: 9:44 AM
 */
public class GlobalData {
    public static RepoController repo;
    public static final String LOCATION_SAVE_BOOK = "SSBooks";
    public static boolean isLoadCategory = false;

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
