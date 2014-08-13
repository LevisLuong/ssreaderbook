package org.coolreader.crengine;

import android.app.Dialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.*;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;

public class BaseDialog extends Dialog {
    BaseActivity activity;

    public BaseDialog(BaseActivity activity) {
        //super(activity, fullscreen ? R.style.Dialog_Fullscreen : R.style.Dialog_Normal);
        //super(activity, fullscreen ? R.style.Dialog_Fullscreen : android.R.style.Theme_Dialog); //android.R.style.Theme_Light_NoTitleBar_Fullscreen : android.R.style.Theme_Light
        super(activity);
        setOwnerActivity(activity);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//		requestWindowFeature(Window.FEATURE_OPTIONS_PANEL);
        if (!DeviceInfo.EINK_SCREEN) {
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.alpha = 1.0f;
            lp.dimAmount = 0.0f;
            if (!DeviceInfo.EINK_SCREEN)
                lp.format = DeviceInfo.PIXEL_FORMAT;
            lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
            lp.horizontalMargin = 0;
            lp.verticalMargin = 0;
            lp.windowAnimations = 0;
            lp.layoutAnimationParameters = null;
            //lp.memoryType = WindowManager.LayoutParams.MEMORY_TYPE_PUSH_BUFFERS;
            getWindow().setAttributes(lp);
        }
        Log.i("cr3", "BaseDialog.window=" + getWindow());
        setCancelable(true);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                onClose();
            }
        });
        onCreate();
    }

    protected void onCreate() {
        // when dialog is created
    }

    protected void onClose() {
        // when dialog is closed
    }


    /**
     * Set View's gesture handlers for LTR and RTL horizontal fling
     *
     * @param view
     * @param ltrHandler, pass null to call onNegativeButtonClick
     * @param rtlHandler, pass null to call onPositiveButtonClick
     */
    public void setFlingHandlers(View view, Runnable ltrHandler, Runnable rtlHandler) {
        if (ltrHandler == null)
            ltrHandler = new Runnable() {
                @Override
                public void run() {
                    // cancel
                }
            };
        if (rtlHandler == null)
            rtlHandler = new Runnable() {
                @Override
                public void run() {
                    // ok
                }
            };
        final GestureDetector detector = new GestureDetector(new MyGestureListener(ltrHandler, rtlHandler));
        view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return detector.onTouchEvent(event);
            }
        });
    }

    private class MyGestureListener extends SimpleOnGestureListener {
        Runnable ltrHandler;
        Runnable rtlHandler;

        public MyGestureListener(Runnable ltrHandler, Runnable rtlHandler) {
            this.ltrHandler = ltrHandler;
            this.rtlHandler = rtlHandler;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            if (e1 == null || e2 == null)
                return false;
            int thresholdDistance = activity.getPalmTipPixels() * 2;
            int thresholdVelocity = activity.getPalmTipPixels();
            int x1 = (int) e1.getX();
            int x2 = (int) e2.getX();
            int y1 = (int) e1.getY();
            int y2 = (int) e2.getY();
            int dist = x2 - x1;
            int adist = dist > 0 ? dist : -dist;
            int ydist = y2 - y1;
            int aydist = ydist > 0 ? ydist : -ydist;
            int vel = (int) velocityX;
            if (vel < 0)
                vel = -vel;
            if (vel > thresholdVelocity && adist > thresholdDistance && adist > aydist * 2) {
                if (dist > 0) {
                    Log.d("cr3", "LTR fling detected");
                    if (ltrHandler != null) {
                        ltrHandler.run();
                        return true;
                    }
                } else {
                    Log.d("cr3", "RTL fling detected");
                    if (rtlHandler != null) {
                        rtlHandler.run();
                        return true;
                    }
                }
            }
            return false;
        }

    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        activity.onUserActivity();
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            return true;
//        }
//        if (this.view != null) {
//            if (this.view.onKeyDown(keyCode, event))
//                return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    protected View view;
}
