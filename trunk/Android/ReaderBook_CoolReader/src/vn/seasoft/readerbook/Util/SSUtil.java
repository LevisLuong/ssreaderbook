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
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import urlimageviewhelper.UrlImageViewHelper;
import vn.seasoft.readerbook.model.Book_Chapter;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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

    public static String calTimeDistance(Date d) {
        if (d != null) {
            Calendar c = Calendar.getInstance();
            long miliseconds = c.getTimeInMillis();
            long diffInMiliSec = miliseconds - d.getTime();

            long diffInSeconds = diffInMiliSec / 1000;

            StringBuffer sb = new StringBuffer();

            long sec = (diffInSeconds >= 60 ? diffInSeconds % 60
                    : diffInSeconds);
            long min = (diffInSeconds = (diffInSeconds / 60)) >= 60 ? diffInSeconds % 60
                    : diffInSeconds;
            long hrs = (diffInSeconds = (diffInSeconds / 60)) >= 24 ? diffInSeconds % 24
                    : diffInSeconds;
            long days = (diffInSeconds = (diffInSeconds / 24)) >= 30 ? diffInSeconds % 30
                    : diffInSeconds;
            long months = (diffInSeconds = (diffInSeconds / 30)) >= 12 ? diffInSeconds % 12
                    : diffInSeconds;
            long years = (diffInSeconds = (diffInSeconds / 12));

            if (years > 0) {
                if (years == 1) {
                    sb.append("1" + " năm trước");
                } else {
                    sb.append(years + " năm trước");
                }
                /*
                 * if (years <= 6 && months > 0) { if (months == 1) {
				 * sb.append(" and 1 month"); } else { sb.append(" and " +
				 * months + " months"); } }
				 */
            } else if (months > 0) {
                if (months == 1) {
                    sb.append("1" + " tháng trước");
                } else {
                    sb.append(months + " tháng trước");
                }
                /*
                 * if (months <= 6 && days > 0) { if (days == 1) {
				 * sb.append(" and 1 day"); } else { sb.append(" and " + days +
				 * " days"); } }
				 */
            } else if (days > 0) {
                if (days == 1) {
                    sb.append("1" + " ngày trước");
                } else {
                    sb.append(days + " ngày trước");
                }
                /*
                 * if (days <= 3 && hrs > 0) { if (hrs == 1) {
				 * sb.append(" and 1 hour"); } else { sb.append(" and " + hrs +
				 * " hours"); } }
				 */
            } else if (hrs > 0) {
                if (hrs == 1) {
                    sb.append("1" + " giờ trước");
                } else {
                    sb.append(hrs + " giờ trước");
                }
                /*
                 * if (min > 1) { sb.append(" and " + min + " minutes"); }
				 */
            } else if (min > 0) {
                if (min == 1) {
                    sb.append("1" + " phút trước");
                } else {
                    sb.append(min + " phút trước");
                }
                /*
                 * if (sec > 1) { sb.append(" and " + sec + " seconds"); }
				 */
            } else {
                if (sec <= 1) {
                    sb.append("1" + " giây trước");
                } else {
                    sb.append(sec + " giây trước");
                }
            }

            return sb.toString();
        }
        return "";
    }

    public static String getAvatarFacebookById(String id) {
        return "https://graph.facebook.com/" + id + "/picture?type=normal";
    }

    public static void addViewContainer(RelativeLayout container, View addview, boolean isCenter) {
        try {
            ((ViewGroup) addview.getParent()).removeView(addview);
        } catch (Exception e) {
        }
        try {
            container.removeAllViews();
        } catch (Exception e) {
        }
        try {
            container.addView(addview);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (isCenter) {
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            }
            addview.setLayoutParams(layoutParams);
        } catch (Exception e) {
        }
    }

    public static void addViewContainerWithoutRemove(RelativeLayout container, View addview) {
        try {
            container.addView(addview);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            addview.setLayoutParams(layoutParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
