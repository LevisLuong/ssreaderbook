package vn.seasoft.readerbook.Util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import urlimageviewhelper.UrlImageViewHelper;
import vn.seasoft.readerbook.model.Book_Chapter;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

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
                FileName = "SSBook" + FileName.hashCode() + extFile;
                File file = new File(myDir, FileName);
                return file;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File downloadPictureBook(Context context, String Urlpicture) {
        try {
            if (Urlpicture.contains("file://")) {
                return new File(Urlpicture);
            }
//                String FileName = UrlBook.substring(
//                        UrlBook.lastIndexOf("/") + 1,
//                        UrlBook.lastIndexOf("."));
//                String extFile = UrlBook.substring(UrlBook.lastIndexOf("."), UrlBook.length());
//                FileName = "SSBook" + FileName.hashCode() + extFile;
            File file = new File(context.getFileStreamPath(UrlImageViewHelper.getFilenameForUrl(Urlpicture)).getAbsolutePath());
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean deleteBook(String urlfile) {
        File file = SSUtil.downloadBook(urlfile);
        return file.delete();
    }

    public static void deletePictureBook(Context context, Book_Chapter bc) {
        List<String> lstPicture = Arrays.asList(bc.getFilename().split(","));
        for (String filechapter : lstPicture) {
            String url = GlobalData.getUrlPictureBook(bc.getIdbook(), bc.getIdbook_chapter()) + filechapter;
            UrlImageViewHelper.remove(context, url);
        }
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

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {

            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);

            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    public static void App_Log(String tag, String logMsg) {
        Log.i(tag, logMsg);
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit)
            return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1)
                + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
}
