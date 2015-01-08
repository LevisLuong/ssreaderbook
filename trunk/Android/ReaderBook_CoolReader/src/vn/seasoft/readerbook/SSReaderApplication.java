package vn.seasoft.readerbook;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;
import org.holoeverywhere.HoloEverywhere;
import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.ThemeManager;
import org.holoeverywhere.app.Application;
import org.holoeverywhere.widget.Toast;
import vn.seasoft.readerbook.GCMService.GCMUtilities;
import vn.seasoft.readerbook.HttpServices.COMMAND_API;
import vn.seasoft.readerbook.HttpServices.ErrorType;
import vn.seasoft.readerbook.HttpServices.OnHttpServicesListener;
import vn.seasoft.readerbook.HttpServices.ResultObject;
import vn.seasoft.readerbook.RequestObjects.Request_Server;
import vn.seasoft.readerbook.ResultObjects.Result_LoginByFacebook;
import vn.seasoft.readerbook.ResultObjects.Result_RegisterGCM;
import vn.seasoft.readerbook.Util.GlobalData;
import vn.seasoft.readerbook.Util.SSUtil;
import vn.seasoft.readerbook.Util.mSharedPreferences;
import vn.seasoft.readerbook.listener.IActionFacebook;
import vn.seasoft.readerbook.sqlite.RepoController;

import java.util.HashMap;

public class SSReaderApplication extends Application implements OnHttpServicesListener {
    final static String TAG = "SSReaderBook";

    static {
        HoloEverywhere.DEBUG = true;

        HoloEverywhere.PREFERENCE_IMPL = HoloEverywhere.PreferenceImpl.XML;

        LayoutInflater.registerPackage(SSReaderApplication.class.getPackage().getName() + ".widget");

//        // Android 2.* incorrect process FULLSCREEN flag when we are modify
//        // DecorView of Window. This hack using HoloEverywhere Slider
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            ThemeManager.modify(ThemeManager.FULLSCREEN);
//        }

        ThemeManager.map(ThemeManager.MIXED,
                R.style.Holo_Theme_Light);
        ThemeManager.map(ThemeManager.DARK,
                R.style.Holo_Theme_NoActionBar);
        ThemeManager.map(ThemeManager.LIGHT,
                R.style.Holo_Theme_Light_NoActionBar);

        ThemeManager.map(ThemeManager.DARK | ThemeManager.FULLSCREEN,
                R.style.Holo_Theme_NoActionBar_Fullscreen);
        ThemeManager.map(ThemeManager.LIGHT | ThemeManager.FULLSCREEN,
                R.style.Holo_Theme_Light_NoActionBar_Fullscreen);

        ThemeManager.setDefaultTheme(ThemeManager.LIGHT);
    }

    // The following line should be changed to include the correct property id.
    private static final String PROPERTY_ID = "UA-54609330-1";
    public static IActionFacebook actionFBListener;
    public static int actionFB = 0;//0: login | 1: post | 2: sign out
    static SSReaderApplication instance;
    //adapter provider for social
    private static SocialAuthAdapter socialAdapter = null;
    private static Request_Server requestServer;
    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();
    /**
     * Global request queue for Volley
     */
    private RequestQueue mRequestQueue;

    public static SocialAuthAdapter getSocialAdapter() {
        return socialAdapter;
    }

    public static Request_Server getRequestServer(final Context context) {
        if (requestServer == null) {
            requestServer = new Request_Server(context);
        }
        return requestServer;
    }

    public static Request_Server getRequestServer(final Context context, OnHttpServicesListener listener) {
        if (requestServer == null) {
            requestServer = new Request_Server(context);
        }
        requestServer.SetListener(listener);
        return requestServer;
    }

    public static SSReaderApplication getInstance() {
        return instance;
    }

    public static void authorizeFB(Context context, IActionFacebook listener) {
        actionFB = 0;
        actionFBListener = listener;
        socialAdapter.authorize(context, SocialAuthAdapter.Provider.FACEBOOK);
    }

    public static void signOutFB(Context context, IActionFacebook listener) {
        actionFB = 2;
        actionFBListener = listener;
        socialAdapter.authorize(context, SocialAuthAdapter.Provider.FACEBOOK);
    }

    public static void postToWall(Context context, IActionFacebook listener) {
        actionFB = 1;
        actionFBListener = listener;
        socialAdapter.authorize(context, SocialAuthAdapter.Provider.FACEBOOK);
    }

    /**
     * @return The Volley Request queue, the queue will be created if it is null
     */
    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    /**
     * Adds the specified request to the global queue, if tag is specified
     * then it is used else Default TAG is used.
     *
     * @param req
     * @param tag
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        VolleyLog.d("Adding request to queue: %s", req.getUrl());

        getRequestQueue().add(req);
    }

    /**
     * Adds the specified request to the global queue using the Default TAG.
     *
     * @param req
     */
    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        Log.i(TAG, "Request to server: " + req.getBodyContentType());
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    /**
     * Cancels all pending requests by the specified TAG, it is important
     * to specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void cancelPendingRequests() {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(TAG);
        }
    }

    synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID)
                    : analytics.newTracker(R.xml.global_tracker);
            mTrackers.put(trackerId, t);

        }
        return mTrackers.get(trackerId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        GCMUtilities.registerGCM(instance);
        GlobalData.repo = new RepoController(this);
        socialAdapter = new SocialAuthAdapter((new DialogListener() {
            @Override
            public void onComplete(Bundle bundle) {
                switch (actionFB) {
                    case 0://Login
                        socialAdapter.getUserProfileAsync(new ProfileDataListener());
                        break;
                    case 1://Post to wall
                        if (actionFBListener != null) {
                            actionFBListener.LoginSuccess();
                        }
                        break;
                    case 2://Logout
                        //dang xuat provider
                        SSReaderApplication.getSocialAdapter().signOut(instance, "facebook");
                        //clear shared preference
                        mSharedPreferences.saveUserDisplay(instance, "");
                        mSharedPreferences.saveUserId(instance, 0);
                        mSharedPreferences.saveUserIdFacebook(instance, "");
                        Toast.makeText(instance, "Đăng xuất thành công !", Toast.LENGTH_SHORT).show();
                        if (actionFBListener != null) {
                            actionFBListener.LoginSuccess();
                        }
                        break;
                }
            }

            @Override
            public void onError(SocialAuthError socialAuthError) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onBack() {

            }
        }));
    }

    @Override
    public void onDataError(int errortype, String urlMethod) {
        GlobalData.DissmissProgress();
        ErrorType.getErrorMessage(this, errortype);
    }

    @Override
    public void onGetData(ResultObject resultData, String urlMethod, int id) {
        if (urlMethod.equals(COMMAND_API.ADD_FEEDBACK)) {
            if (urlMethod.equals(COMMAND_API.ADD_FEEDBACK)) {
                Toast.makeText(getInstance(), "Cảm ơn bạn đã phản hồi", Toast.LENGTH_SHORT).show();
            }
        }
        if (urlMethod.equals(COMMAND_API.LOGIN_BY_FACEBOOK)) {
            Result_LoginByFacebook data = (Result_LoginByFacebook) resultData;
            Toast.makeText(instance, "Đăng nhập thành công !", Toast.LENGTH_SHORT).show();
            mSharedPreferences.saveUserId(instance, data.idUser);
            mSharedPreferences.saveUserIdFacebook(instance, data.idUserFacebook);
            mSharedPreferences.saveUserDisplay(instance, data.displayName);
            if (actionFBListener != null) {
                actionFBListener.LoginSuccess();
            }
        }
        if (urlMethod.equals(COMMAND_API.REGISTER_GCM)) {
            Result_RegisterGCM data = (Result_RegisterGCM) resultData;
            SSUtil.App_Log(GCMUtilities.TAG, "Server trả về RegId: " + data.regId);
            mSharedPreferences.saveRegIdGcm(instance, data.regId);
            mSharedPreferences.saveAppVersion(instance, SSUtil.getAppVersion(instance));
        }
        GlobalData.DissmissProgress();
    }

    /**
     * Enum used to identify the tracker that needs to be used for tracking.
     * <p/>
     * A single tracker is usually enough for most purposes. In case you do need multiple trackers,
     * storing them all in Application object helps ensure that they are created only once per
     * application instance.
     */
    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
    }

    // To receive the profile response after authentication
    private final class ProfileDataListener implements SocialAuthListener<Profile> {

        @Override
        public void onExecute(String provider, Profile t) {
            SSUtil.App_Log("Thong tin facebook:", "Id: " + t.getValidatedId() + ",Display name:" + t.getFullName() + ",Email:" + t.getEmail());
            getRequestServer(instance, instance).loginByFacebook(t.getValidatedId(), t.getFullName(), t.getEmail());
        }

        @Override
        public void onError(SocialAuthError e) {

        }
    }


}
