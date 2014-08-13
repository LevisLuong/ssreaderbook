package org.coolreader.crengine;

import android.view.View;
import android.widget.RelativeLayout;
import org.coolreader.CoolReader;

public class ReaderViewLayout extends RelativeLayout implements Settings {
    private CoolReader activity;
    private ReaderView contentView;
    private StatusBar statusView;
    private CRToolBar toolbarView;
    private int statusBarLocation;
    private boolean fullscreen;
    private boolean nightMode;
    ReaderView.ToolbarBackgroundDrawable statusBackground;

    public CRToolBar getToolBar() {
        return toolbarView;
    }

    public StatusBar getStatusBar() {
        return statusView;
    }

    public void updateFullscreen(boolean fullscreen) {
        if (this.fullscreen == fullscreen)
            return;
        this.fullscreen = fullscreen;
        statusView.updateFullscreen(fullscreen);
        requestLayout();
    }

    public boolean isStatusbarVisible() {
        return statusBarLocation == VIEWER_STATUS_BOTTOM || statusBarLocation == VIEWER_STATUS_TOP;
    }

    public void updateSettings(Properties settings) {
        CoolReader.log.d("CoolReader.updateSettings()");
        nightMode = settings.getBool(PROP_NIGHT_MODE, false);
        statusBarLocation = VIEWER_STATUS_BOTTOM;
        statusView.setVisibility(VISIBLE);
        statusView.updateSettings(settings);
        requestLayout();
    }

    public void showMenu() {
        if (toolbarView.getVisibility() == View.VISIBLE) {
            toolbarView.SetInvisible();
        } else {
            toolbarView.SetVisible();
        }
    }

    public ReaderViewLayout(CoolReader context, ReaderView contentView) {
        super(context);
        this.activity = context;
        this.contentView = contentView;
        this.statusView = new StatusBar(context);
        statusBackground = contentView.createToolbarBackgroundDrawable();
        this.statusView.setBackgroundDrawable(statusBackground);
        //toolbarBackground = contentView.createToolbarBackgroundDrawable();
        this.toolbarView = new CRToolBar((BaseActivity) context, contentView);
        //this.toolbarView.setBackgroundDrawable(toolbarBackground);
        this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        this.addView(contentView.getSurface());
        //this.addView(statusView);
        this.addView(toolbarView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        statusView.setFocusable(false);
        toolbarView.SetInvisible();
        statusView.setFocusableInTouchMode(false);
        contentView.getSurface().setFocusable(true);
        contentView.getSurface().setFocusableInTouchMode(true);
        updateFullscreen(activity.isFullscreen());
        updateSettings(context.settings());
        //onThemeChanged(activity.getCurrentTheme());
    }

//    public void onThemeChanged(InterfaceTheme theme) {
////			if (DeviceInfo.EINK_SCREEN) {
////				statusView.setBackgroundColor(0xFFFFFFFF);
////				toolbarView.setBackgroundColor(0xFFFFFFFF);
////			} else if (nightMode) {
////				statusView.setBackgroundColor(0xFF000000);
////				toolbarView.setBackgroundColor(0xFF000000);
////			} else {
////				statusView.setBackgroundResource(theme.getReaderStatusBackground());
////				toolbarView.setBackgroundResource(theme.getReaderToolbarBackground(toolbarView.isVertical()));
////			}
//        statusView.onThemeChanged(theme);
//    }


//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        CoolReader.log.v("onLayout(" + l + ", " + t + ", " + r + ", " + b + ")");
//        //toolbarView.layout(l, t, r, b);
//        r -= l;
//        b -= t;
//        t = 0;
//        l = 0;
////        statusView.setVisibility(isStatusbarVisible() ? VISIBLE : GONE);
////        boolean toolbarVisible = toolbarLocation != VIEWER_TOOLBAR_NONE && (!fullscreen || !hideToolbarInFullscren);
//        boolean landscape = r > b;
////        Rect toolbarRc = new Rect(l, t, r, b);
////        if (toolbarVisible) {
////            int location = toolbarLocation;
////            if (location == VIEWER_TOOLBAR_SHORT_SIDE)
////                location = landscape ? VIEWER_TOOLBAR_LEFT : VIEWER_TOOLBAR_TOP;
////            else if (location == VIEWER_TOOLBAR_LONG_SIDE)
////                location = landscape ? VIEWER_TOOLBAR_TOP : VIEWER_TOOLBAR_LEFT;
////            switch (location) {
////                case VIEWER_TOOLBAR_LEFT:
////                    //toolbarView.setBackgroundResource(activity.getCurrentTheme().getReaderToolbarBackground(true));
////                    toolbarRc.right = l + toolbarView.getMeasuredWidth();
////                    //toolbarView.layout(l, t, l + toolbarView.getMeasuredWidth(), b);
////                    l += toolbarView.getMeasuredWidth();
////                    break;
////                case VIEWER_TOOLBAR_RIGHT:
////                    //toolbarView.setBackgroundResource(activity.getCurrentTheme().getReaderToolbarBackground(true));
////                    toolbarRc.left = r - toolbarView.getMeasuredWidth();
////                    //toolbarView.layout(r - toolbarView.getMeasuredWidth(), t, r, b);
////                    r -= toolbarView.getMeasuredWidth();
////                    break;
////                case VIEWER_TOOLBAR_TOP:
////                    //toolbarView.setBackgroundResource(activity.getCurrentTheme().getReaderToolbarBackground(false));
////                    toolbarRc.bottom = t + toolbarView.getMeasuredHeight();
////                    //toolbarView.layout(l, t, r, t + toolbarView.getMeasuredHeight());
////                    t += toolbarView.getMeasuredHeight();
////                    break;
////                case VIEWER_TOOLBAR_BOTTOM:
////                    //toolbarView.setBackgroundResource(activity.getCurrentTheme().getReaderToolbarBackground(false));
////                    toolbarRc.top = b - toolbarView.getMeasuredHeight();
////                    //toolbarView.layout(l, b - toolbarView.getMeasuredHeight(), r, b);
////                    b -= toolbarView.getMeasuredHeight();
////                    break;
////            }
////            //toolbarBackground.setLocation(location);
////        }
//        Rect statusRc = new Rect(l, t, r, b);
//        if (statusBarLocation == VIEWER_STATUS_TOP) {
//            statusRc.bottom = t + statusView.getMeasuredHeight();
//            //statusView.layout(l, t, r, t + statusView.getMeasuredHeight());
//            t += statusView.getMeasuredHeight();
//        } else if (statusBarLocation == VIEWER_STATUS_BOTTOM) {
//            statusRc.top = b - statusView.getMeasuredHeight();
//            //statusView.layout(l, b - statusView.getMeasuredHeight(), r, b);
//            b -= statusView.getMeasuredHeight();
//        }
//        statusBackground.setLocation(statusBarLocation);
//
//        contentView.getSurface().layout(l, t, r, b);
//
//
//        statusView.layout(statusRc.left, statusRc.top, statusRc.right, statusRc.bottom);
//
////        if (activity.isFullscreen()) {
////            BackgroundThread.instance().postGUI(new Runnable() {
////                @Override
////                public void run() {
////                    CoolReader.log.v("Invalidating toolbar ++++++++++");
////                    toolbarView.forceLayout();
////                    contentView.getSurface().invalidate();
////                    toolbarView.invalidate();
////                }
////            }, 100);
////        }
//
//        //			toolbarView.invalidate();
////			toolbarView.requestLayout();
//        //invalidate();
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int w = MeasureSpec.getSize(widthMeasureSpec);
//        int h = MeasureSpec.getSize(heightMeasureSpec);
//        setMeasuredDimension(w, h);
//
//        boolean statusVisible = statusBarLocation == VIEWER_STATUS_BOTTOM || statusBarLocation == VIEWER_STATUS_TOP;
////        boolean toolbarVisible = toolbarLocation != VIEWER_TOOLBAR_NONE && (!fullscreen || !hideToolbarInFullscren);
////        boolean landscape = w > h;
////        if (toolbarVisible) {
////            int location = toolbarLocation;
////            if (location == VIEWER_TOOLBAR_SHORT_SIDE)
////                location = landscape ? VIEWER_TOOLBAR_LEFT : VIEWER_TOOLBAR_TOP;
////            else if (location == VIEWER_TOOLBAR_LONG_SIDE)
////                location = landscape ? VIEWER_TOOLBAR_TOP : VIEWER_TOOLBAR_LEFT;
////            switch (location) {
////                case VIEWER_TOOLBAR_LEFT:
////                case VIEWER_TOOLBAR_RIGHT:
////                    toolbarView.setVertical(true);
////                    toolbarView.measure(MeasureSpec.makeMeasureSpec(MeasureSpec.AT_MOST, w),
////                            MeasureSpec.makeMeasureSpec(MeasureSpec.AT_MOST, h));
////                    w -= toolbarView.getMeasuredWidth();
////                    break;
////                case VIEWER_TOOLBAR_TOP:
////                case VIEWER_TOOLBAR_BOTTOM:
////                    toolbarView.setVertical(false);
////                    toolbarView.measure(MeasureSpec.makeMeasureSpec(MeasureSpec.AT_MOST, w),
////                            MeasureSpec.makeMeasureSpec(MeasureSpec.AT_MOST, h));
////                    h -= toolbarView.getMeasuredHeight();
////                    break;
////            }
////        }
//        if (statusVisible) {
//            statusView.measure(MeasureSpec.makeMeasureSpec(MeasureSpec.AT_MOST, w),
//                    MeasureSpec.makeMeasureSpec(MeasureSpec.UNSPECIFIED, 0));
//            h -= statusView.getMeasuredHeight();
//        }
////        toolbarView.measure(MeasureSpec.makeMeasureSpec(MeasureSpec.AT_MOST, w),
////                MeasureSpec.makeMeasureSpec(MeasureSpec.AT_MOST, h));
//
//        contentView.getSurface().measure(MeasureSpec.makeMeasureSpec(MeasureSpec.AT_MOST, w),
//                MeasureSpec.makeMeasureSpec(MeasureSpec.AT_MOST, h));
//    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        requestLayout();
    }
}