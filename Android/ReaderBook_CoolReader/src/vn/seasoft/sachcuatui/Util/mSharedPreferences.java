package vn.seasoft.sachcuatui.Util;

import android.content.Context;
import android.content.SharedPreferences;

public class mSharedPreferences {
    private static String PREFS_NAME = "MyPrefsFile";


    public static boolean getBooleanVariable(Context context, String saveName) {
        SharedPreferences settings = context
                .getSharedPreferences(PREFS_NAME, 0);
        return settings.getBoolean(saveName, true);
    }

    public static void saveBooleanVariable(Context context, String saveName,
                                           boolean variable) {
        SharedPreferences settings = context
                .getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(saveName, variable);

        // Commit the edits!
        editor.commit();
    }

    public static int getUserID(Context context) {
        SharedPreferences settings = context
                .getSharedPreferences(PREFS_NAME, 0);
        return settings.getInt("IdUser", 0);
    }

    public static void saveUserId(Context context, int idUser) {
        SharedPreferences settings = context
                .getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("IdUser", idUser);
        // Commit the edits!
        editor.commit();
    }

    public static String getUserIDFacebook(Context context) {
        SharedPreferences settings = context
                .getSharedPreferences(PREFS_NAME, 0);
        return settings.getString("IdUserFacebook", "");
    }

    public static void saveUserIdFacebook(Context context, String idUserfacebook) {
        SharedPreferences settings = context
                .getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("IdUserFacebook", idUserfacebook);
        // Commit the edits!
        editor.commit();
    }

    public static String getUserDisplay(Context context) {
        SharedPreferences settings = context
                .getSharedPreferences(PREFS_NAME, 0);
        return settings.getString("UserDisplay", "");
    }

    public static void saveUserDisplay(Context context, String userDisplay) {
        SharedPreferences settings = context
                .getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("UserDisplay", userDisplay);
        // Commit the edits!
        editor.commit();
    }

    public static String getRegIdGcm(Context context) {
        SharedPreferences settings = context
                .getSharedPreferences(PREFS_NAME, 0);
        return settings.getString("IDGCM", "");
    }

    public static void saveRegIdGcm(Context context, String regId) {
        SharedPreferences settings = context
                .getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("IDGCM", regId);
        // Commit the edits!
        editor.commit();
    }

    public static int getAppVersion(Context context) {
        SharedPreferences settings = context
                .getSharedPreferences(PREFS_NAME, 0);
        return settings.getInt("AppVersion", 0);
    }

    public static void saveAppVersion(Context context, int appversion) {
        SharedPreferences settings = context
                .getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("AppVersion", appversion);
        // Commit the edits!
        editor.commit();
    }

    public static int getAppVersionSettingBook(Context context) {
        SharedPreferences settings = context
                .getSharedPreferences(PREFS_NAME, 0);
        return settings.getInt("AppVersionSetting", 0);
    }

    public static void saveAppVersionSettingBook(Context context, int appversion) {
        SharedPreferences settings = context
                .getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("AppVersionSetting", appversion);
        // Commit the edits!
        editor.commit();
    }
}