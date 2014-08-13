package vn.seasoft.readerbook.Util;

import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * User: XuanTrung
 * Date: 7/11/2014
 * Time: 9:46 AM
 */
public class SSUtil {
    public static File downloadBook(String UrlBook) {
        try {
            File myDir = null;
            if (Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState())) {
                myDir = new File(Environment.getExternalStorageDirectory()
                        + "/" + GlobalData.LOCATION_SAVE_BOOK);

                if (myDir != null) {
                    if (!myDir.mkdirs()) {
                        if (!myDir.exists()) {
                            Log.d("CameraSample", "failed to create directory");
                            myDir = null;
                        }
                    }
                }

            } else {
                //Toast.makeText(GlobalData.getCurContext(), R.string.Error43, Toast.LENGTH_LONG).show();
            }
            if (myDir != null) {
                if (UrlBook.contains("file://")) {
                    return new File(UrlBook);
                }
                String FileName = UrlBook.substring(
                        UrlBook.lastIndexOf("/") + 1,
                        UrlBook.lastIndexOf("."));
                String extFile = UrlBook.substring(UrlBook.lastIndexOf("."), UrlBook.length());
                System.out.println("filename record: " + FileName);
                FileName = "SSBook" + FileName.hashCode() + extFile;
                File file = new File(myDir, FileName);
                return file;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static boolean deleteBook(String urlfile){
        File file = SSUtil.downloadBook(urlfile);
        return file.delete();
    }
}
