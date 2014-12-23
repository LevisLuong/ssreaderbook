package org.coolreader.db;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import org.coolreader.crengine.MountPathCorrector;

import java.util.ArrayList;

public class CRDBServiceAccessor {
    private final static String TAG = "cr3db";
    private Activity mActivity;
    private CRDBService.LocalBinder mService;
    private boolean mServiceBound;
    private MountPathCorrector pathCorrector;
    private ArrayList<Runnable> onConnectCallbacks = new ArrayList<Runnable>();
    private boolean bindIsCalled;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = ((CRDBService.LocalBinder) service);
            Log.i(TAG, "connected to CRDBService");
            if (pathCorrector != null)
                mService.setPathCorrector(pathCorrector);
            if (onConnectCallbacks.size() != 0) {
                // run once
                for (Runnable callback : onConnectCallbacks)
                    callback.run();
                onConnectCallbacks.clear();
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
            Log.i(TAG, "disconnected from CRDBService");
        }
    };

    public CRDBServiceAccessor(Activity activity, MountPathCorrector pathCorrector) {
        mActivity = activity;
        this.pathCorrector = pathCorrector;
    }

    public CRDBService.LocalBinder get() {
        if (mService == null)
            throw new RuntimeException("no service");
        return mService;
    }

    public void bind(final Runnable boundCallback) {
        if (mService != null) {
            Log.v(TAG, "CRDBService is already bound");
            if (boundCallback != null)
                boundCallback.run();
            return;
        }
        Log.v(TAG, "binding CRDBService");
        if (boundCallback != null)
            onConnectCallbacks.add(boundCallback);
        if (!bindIsCalled) {
            bindIsCalled = true;
            if (mActivity.bindService(new Intent(mActivity,
                    CRDBService.class), mServiceConnection, Context.BIND_AUTO_CREATE)) {
                mServiceBound = true;
            } else {
                Log.e(TAG, "cannot bind CRDBService");
            }
        }
    }

    public void unbind() {
        Log.v(TAG, "unbinding CRDBService");
        if (mServiceBound) {
            // Detach our existing connection.
            mActivity.unbindService(mServiceConnection);
            mServiceBound = false;
        }
    }

}
