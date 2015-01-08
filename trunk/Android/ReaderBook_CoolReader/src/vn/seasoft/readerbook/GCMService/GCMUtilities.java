/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package vn.seasoft.readerbook.GCMService;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import vn.seasoft.readerbook.HttpServices.OnHttpServicesListener;
import vn.seasoft.readerbook.SSReaderApplication;
import vn.seasoft.readerbook.Util.SSUtil;
import vn.seasoft.readerbook.Util.mSharedPreferences;

import java.io.IOException;

/**
 * Helper class providing methods and constants common to other classes in the
 * app.
 */
public final class GCMUtilities {
    /**
     * Google API project id registered to use GCM.
     */
    static final String SENDER_ID = "31000856013";

    /**
     * Tag used on log messages.
     */
    public static final String TAG = "GCMService";

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public static boolean checkPlayServices(Context context) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Log.i(TAG, "This device is not supported.");
            } else {
                Log.i(TAG, "This device is not supported.");
            }
            return false;
        }
        return true;
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * <p/>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     */
    static String getRegistrationId(Context context) {
        String registrationId = mSharedPreferences.getRegIdGcm(context);
        if (registrationId.equals("")) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = mSharedPreferences.getAppVersion(context);
        int currentVersion = SSUtil.getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    public static void registerGCM(Context context) {
        // Check device for Play Services APK.
        if (checkPlayServices(context)) {
            // If this check succeeds, proceed with normal processing.
            // Otherwise, prompt user to get valid Play Services APK.
            String regid;
            regid = getRegistrationId(context);
            if (regid.equals("")) {
                SSUtil.App_Log(TAG,"Bắt đầu đăng ký mới RegId");
                registerInBackground(context);
            }else{
                SSUtil.App_Log(TAG,"Đã đăng ký RegId:" + regid);
            }
        }else{
            SSUtil.App_Log(TAG,"Không tìm thấy play services !!!");
        }
    }

    static void registerInBackground(final Context context) {
        new AsyncTask<Void,Void,Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
                    String regid = gcm.register(SENDER_ID);
                    SSReaderApplication.getRequestServer(context,(OnHttpServicesListener)SSReaderApplication.getInstance()).registerGCM(regid);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void msg) {

            }
        }.execute(null, null, null);
    }
}
