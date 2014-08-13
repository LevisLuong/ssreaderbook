package vn.seasoft.readerbook;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import org.holoeverywhere.HoloEverywhere;
import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.ThemeManager;
import org.holoeverywhere.app.Application;
import vn.seasoft.readerbook.HttpServices.OnHttpServicesListener;
import vn.seasoft.readerbook.HttpServices.ResultObject;
import vn.seasoft.readerbook.RequestObjects.Request_Server;
import vn.seasoft.readerbook.Util.GlobalData;
import vn.seasoft.readerbook.sqlite.RepoController;

public class SSReaderApplication extends Application implements OnHttpServicesListener{
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

    /**
     * Global request queue for Volley
     */
    private RequestQueue mRequestQueue;

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

    private static Request_Server requestServer;

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


    static SSReaderApplication instance;

    public static SSReaderApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        GlobalData.repo = new RepoController(this);
    }

    @Override
    public void onDataError(int errortype, String urlMethod) {

    }

    @Override
    public void onGetData(ResultObject resultData, String urlMethod, int id) {

    }
}
