package vn.seasoft.readerbook.Util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
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
    public static String loadIDDevice(final Context c) {
        final TelephonyManager mTelephonyMgr = (TelephonyManager) c
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = mTelephonyMgr.getDeviceId();
        if (imei == null || imei.equals("000000000000000") || imei.equals("")) {
            imei = "35"
                    + // we make this look like a valid IMEI
                    Build.BOARD.length() % 10 + Build.BRAND.length() % 10
                    + Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10
                    + Build.DISPLAY.length() % 10 + Build.HOST.length() % 10
                    + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10
                    + Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10
                    + Build.TAGS.length() % 10 + Build.TYPE.length() % 10
                    + Build.USER.length() % 10; // 13 digits
        }
        return imei;
    }
}
