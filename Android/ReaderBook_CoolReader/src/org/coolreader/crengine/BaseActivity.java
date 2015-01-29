package org.coolreader.crengine;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.ClipboardManager;
import android.util.DisplayMetrics;
import android.view.*;
import android.view.WindowManager.LayoutParams;
import org.coolreader.db.CRDBService;
import org.coolreader.db.CRDBServiceAccessor;
import org.coolreader.sync.SyncServiceAccessor;
import org.holoeverywhere.ThemeManager;
import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.preference.SharedPreferences;
import org.holoeverywhere.widget.TextView;
import vn.seasoft.sachcuatui.R;
import vn.seasoft.sachcuatui.Util.SSUtil;
import vn.seasoft.sachcuatui.Util.mSharedPreferences;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

public class BaseActivity extends Activity implements Settings {

    public static final int DEF_SCREEN_BACKLIGHT_TIMER_INTERVAL = 3 * 60 * 1000;
    static final DictInfo dicts[] = {
            new DictInfo("Fora", "Fora Dictionary", "com.ngc.fora", "com.ngc.fora.ForaDictionary", Intent.ACTION_SEARCH, 0),
            new DictInfo("ColorDict", "ColorDict", "com.socialnmobile.colordict", "com.socialnmobile.colordict.activity.Main", Intent.ACTION_SEARCH, 0),
            new DictInfo("ColorDictApi", "ColorDict new / GoldenDict", "com.socialnmobile.colordict", "com.socialnmobile.colordict.activity.Main", Intent.ACTION_SEARCH, 1),
            new DictInfo("AardDict", "Aard Dictionary", "aarddict.android", "aarddict.android.Article", Intent.ACTION_SEARCH, 0),
            new DictInfo("AardDictLookup", "Aard Dictionary Lookup", "aarddict.android", "aarddict.android.Lookup", Intent.ACTION_SEARCH, 0),
            new DictInfo("Dictan", "Dictan Dictionary", "info.softex.dictan", "", Intent.ACTION_VIEW, 2),
            new DictInfo("FreeDictionary.org", "Free Dictionary . org", "org.freedictionary.MainActivity", "org.freedictionary", Intent.ACTION_VIEW, 0),
            new DictInfo("LingoQuizLite", "Lingo Quiz Lite", "mnm.lite.lingoquiz", "mnm.lite.lingoquiz.ExchangeActivity", "lingoquiz.intent.action.ADD_WORD", 0).setDataKey("EXTRA_WORD"),
            new DictInfo("LingoQuiz", "Lingo Quiz", "mnm.lingoquiz", "mnm.lingoquiz.ExchangeActivity", "lingoquiz.intent.action.ADD_WORD", 0).setDataKey("EXTRA_WORD"),
            new DictInfo("LEODictionary", "LEO Dictionary", "org.leo.android.dict", "org.leo.android.dict.LeoDict", "android.intent.action.SEARCH", 0),
    };
    private static final Logger log = L.create("ba");
    private final static int SYSTEM_UI_FLAG_LOW_PROFILE = 1;
    private final static int MIN_BACKLIGHT_LEVEL_PERCENT = DeviceInfo.MIN_SCREEN_BRIGHTNESS_PERCENT;
    private final static int MIN_BRIGHTNESS_IN_BROWSER = 12;
    // Store system locale here, on class creation
    private static final Locale defaultLocale = Locale.getDefault();
    protected static String PREF_FILE = "CR3LastBook";
    protected static String PREF_LAST_BOOK = "LastBook";
    protected static String PREF_LAST_LOCATION = "LastLocation";
    protected static String PREF_LAST_NOTIFICATION = "LastNoticeNumber";
    private static long lastUserActivityTime;
    private static String PREF_HELP_FILE = "HelpFile";
    protected View contentView;

    //    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus && (DeviceInfo.getSDKLevel() >= 19)) {
//            int flag = 0;
//            if (mFullscreen)
//                flag |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN;
//
//            mDecorView.setSystemUiVisibility(flag);
//        }
//    }
    ScreenBacklightControl backlightControl = new ScreenBacklightControl();
    private CRDBServiceAccessor mCRDBService;
    private SyncServiceAccessor mSyncService;
    private SettingsManager mSettingsManager;
    private boolean mIsStarted = false;
    private boolean mPaused = false;
    private String mVersion = "3.1";
    private int densityDpi = 160;
    private float diagonalInches = 4;
    private int preferredItemHeight = 36;
    private boolean mFullscreen = false;
    private int lastSystemUiVisibility = -1;
    private int currentKeyBacklightLevel = 1;
    private boolean keyBacklightOff = true;
    private int screenBacklightBrightness = -1; // use default
    //private boolean brightnessHackError = false;
    private boolean brightnessHackError = DeviceInfo.SAMSUNG_BUTTONS_HIGHLIGHT_PATCH;
    private Runnable backlightTimerTask = null;
    private int screenBacklightDuration = DEF_SCREEN_BACKLIGHT_TIMER_INTERVAL;
    private int mScreenUpdateMode = 0;
    private int mScreenUpdateInterval = 0;
    private boolean mNightMode = false;
    private String currentLanguage;
    private int currentProfile = 0;
    private Boolean hasHardwareMenuKey = null;

    static public int stringToInt(String value, int defValue) {
        if (value == null)
            return defValue;
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            return defValue;
        }
    }

    public static DictInfo[] getDictList() {
        return dicts;
    }

    protected void unbindCRDBService() {
        if (mCRDBService != null) {
            mCRDBService.unbind();
            mCRDBService = null;
        }
    }


//    private InterfaceTheme currentTheme = InterfaceTheme.LIGHT;
//
//    public InterfaceTheme getCurrentTheme() {
//        return currentTheme;
//    }
//
//    public void setCurrentTheme(String themeCode) {
//        InterfaceTheme theme = InterfaceTheme.findByCode(themeCode);
//        if (theme != null && currentTheme != theme) {
//            setCurrentTheme(theme);
//        }
//    }

    protected void unbindSyncService() {
        if (mSyncService != null) {
            mSyncService.unbind();
            mSyncService = null;
        }
    }

    protected void bindSyncService() {
        if (mSyncService == null) {
            mSyncService = new SyncServiceAccessor(this);
            mSyncService.bind(new Runnable() {
                @Override
                public void run() {
                    log.i("Initialization after SyncService is bound");
                    BackgroundThread.instance().postGUI(new Runnable() {
                        @Override
                        public void run() {
                            FileInfo downloadDirectory = Services.getScanner().getDownloadDirectory();
                            if (downloadDirectory != null && mSyncService != null)
                                mSyncService.setSyncDirectory(new File(downloadDirectory.getPathName()));
                        }
                    });
                }
            });
        }
    }

    protected void bindCRDBService() {
        if (mCRDBService == null) {
            mCRDBService = new CRDBServiceAccessor(this, Engine.getInstance(this).getPathCorrector());
        }
        mCRDBService.bind(null);
    }

//    public void setCurrentTheme(InterfaceTheme theme) {
//        log.i("setCurrentTheme(" + theme + ")");
//        currentTheme = theme;
//        getApplication().setTheme(theme.getThemeId());
//        setTheme(theme.getThemeId());
//        updateBackground();
//    }

    /**
     * Wait until database is bound.
     *
     * @param readyCallback to be called after DB is ready
     */
    public void waitForCRDBService(Runnable readyCallback) {
        if (mCRDBService == null) {
            mCRDBService = new CRDBServiceAccessor(this, Engine.getInstance(this).getPathCorrector());
        }
        mCRDBService.bind(readyCallback);
    }

    public CRDBServiceAccessor getDBService() {
        return mCRDBService;
    }

    public CRDBService.LocalBinder getDB() {
        return mCRDBService != null ? mCRDBService.get() : null;
    }

    public SyncServiceAccessor getSyncService() {
        return mSyncService;
    }

    public Properties settings() {
        return mSettingsManager.mSettings;
    }
//	private final static int SYSTEM_UI_FLAG_HIDE_NAVIGATION = 2;
//
//	private final static int SYSTEM_UI_FLAG_VISIBLE = 0;

//	public void simulateTouch() {
//		// Obtain MotionEvent object
//		long downTime = SystemClock.uptimeMillis();
//		long eventTime = SystemClock.uptimeMillis() + 10;
//		float x = 0.0f;
//		float y = 0.0f;
//		// List of meta states found here: developer.android.com/reference/android/view/KeyEvent.html#getMetaState()
//		int metaState = 0;
//		MotionEvent motionEvent = MotionEvent.obtain(
//		    downTime,
//		    downTime,
//		    MotionEvent.ACTION_DOWN,
//		    x,
//		    y,
//		    metaState
//		);
//		MotionEvent motionEvent2 = MotionEvent.obtain(
//			    downTime,
//			    eventTime,
//			    MotionEvent.ACTION_UP,
//			    x,
//			    y,
//			    metaState
//			);
//		//motionEvent.setEdgeFlags(flags)
//		// Dispatch touch event to view
//		//new Handler().dispatchMessage(motionEvent);
//		if (getContentView() != null) {
//			getContentView().dispatchTouchEvent(motionEvent);
//			getContentView().dispatchTouchEvent(motionEvent2);
//		}
//
//	}

    protected void startServices() {
        // create settings
        mSettingsManager = new SettingsManager(this);
        // create rest of settings
        Services.startServices(this);
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        log.i("BaseActivity.onCreate() entered");
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setTheme(ThemeManager.LIGHT | ThemeManager.FULLSCREEN);
//        mDecorView = getWindow().getDecorView();

        try {
            PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
            mVersion = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // ignore
        }
        log.i("CoolReader version : " + getVersion());

        Display d = getWindowManager().getDefaultDisplay();
        DisplayMetrics m = new DisplayMetrics();
        d.getMetrics(m);
        try {
            Field fld = m.getClass().getField("densityDpi");
            if (fld != null) {
                Object v = fld.get(m);
                if (v != null && v instanceof Integer) {
                    densityDpi = ((Integer) v).intValue();
                    log.i("Screen density detected: " + densityDpi + "DPI");
                }
            }
        } catch (Exception e) {
            log.e("Cannot find field densityDpi, using default value");
        }
        float widthInches = m.widthPixels / densityDpi;
        float heightInches = m.heightPixels / densityDpi;
        diagonalInches = (float) Math.sqrt(widthInches * widthInches + heightInches * heightInches);

        log.i("diagonal=" + diagonalInches + "  isSmartphone=" + isSmartphone());
        //log.i("CoolReader.window=" + getWindow());
        if (!DeviceInfo.EINK_SCREEN) {
            LayoutParams lp = new LayoutParams();
            lp.alpha = 1.0f;
            lp.dimAmount = 0.0f;
            if (!DeviceInfo.EINK_SCREEN)
                lp.format = DeviceInfo.PIXEL_FORMAT;
            lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
            lp.horizontalMargin = 0;
            lp.verticalMargin = 0;
            lp.windowAnimations = 0;
            lp.layoutAnimationParameters = null;
            lp.memoryType = LayoutParams.MEMORY_TYPE_NORMAL;
            getWindow().setAttributes(lp);
        }

        // load settings
        Properties props = settings();

        String lang = props.getProperty(ReaderView.PROP_APP_LOCALE, Lang.DEFAULT.code);
        setLanguage(lang);

        //String theme = props.getProperty(ReaderView.PROP_APP_THEME, "LIGHT");
        //setCurrentTheme(theme);

        setScreenBacklightDuration(props.getInt(ReaderView.PROP_APP_SCREEN_BACKLIGHT_LOCK, 3));

        setFullscreen(props.getBool(ReaderView.PROP_APP_FULLSCREEN, true));

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        int backlight = props.getInt(ReaderView.PROP_APP_SCREEN_BACKLIGHT, -1);
        if (backlight < -1 || backlight > 100)
            backlight = -1;
        setScreenBacklightLevel(backlight);

        bindSyncService();
        bindCRDBService();
    }

    public int getScreenOrientation() {
        return getResources().getConfiguration().orientation;
    }

    public void setScreenOrientation(int orientation) {
        setRequestedOrientation(orientation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindCRDBService();
        unbindSyncService();
    }

    @Override
    protected void onStart() {
        super.onStart();

//		Properties props = settings().get();
//		String theme = props.getProperty(ReaderView.PROP_APP_THEME, DeviceInfo.FORCE_LIGHT_THEME ? "WHITE" : "LIGHT");
//		setCurrentTheme(theme);

        mIsStarted = true;
        mPaused = false;
        onUserActivity();
    }

    @Override
    protected void onStop() {
        mIsStarted = false;
        super.onStop();
    }

    @Override
    protected void onPause() {
        log.i("CoolReader.onPause() : saving reader state");
        mIsStarted = false;
        mPaused = true;
//		setScreenUpdateMode(-1, mReaderView);
        einkRefresh();
        releaseBacklightControl();
        super.onPause();
    }

    protected void einkRefresh() {
        EinkScreen.Refresh();
    }

    @Override
    protected void onResume() {
        log.i("CoolReader.onResume()");
        mPaused = false;
        mIsStarted = true;
        backlightControl.onUserActivity();
        super.onResume();
    }

    public boolean isStarted() {
        return mIsStarted;
    }

    public String getVersion() {
        return mVersion;
    }

    public Properties loadSettings(int profile) {
        return mSettingsManager.loadSettings(profile);
    }

    public void saveSettings(int profile, Properties settings) {
        mSettingsManager.saveSettings(profile, settings);
    }

    public void saveSettings(File f, Properties settings) {
        mSettingsManager.saveSettings(f, settings);
    }

    public int getPalmTipPixels() {
        return densityDpi / 3; // 1/3"
    }

    public int getDensityDpi() {
        return densityDpi;
    }

    public float getDiagonalInches() {
        return diagonalInches;
    }

    public boolean isSmartphone() {
        return diagonalInches <= 5.8;
    }

    public int getPreferredItemHeight() {
        return preferredItemHeight;
    }

    public void updateBackground() {
        TypedArray a = getTheme().obtainStyledAttributes(new int[]{android.R.attr.windowBackground, android.R.attr.background, android.R.attr.textColor, android.R.attr.colorBackground, android.R.attr.colorForeground, android.R.attr.listPreferredItemHeight});
        int bgRes = a.getResourceId(0, 0);
        //int clText = a.getColor(1, 0);
        int clBackground = a.getColor(2, 0);
        //int clForeground = a.getColor(3, 0);
        preferredItemHeight = a.getDimensionPixelSize(5, 36);
        //View contentView = getContentView();
        if (contentView != null) {
            if (bgRes != 0) {
                //Drawable d = getResources().getDrawable(bgRes);
                //log.v("Setting background resource " + d.getIntrinsicWidth() + "x" + d.getIntrinsicHeight());
                //contentView.setBackgroundResource(null);
                contentView.setBackgroundResource(bgRes);
                //getWindow().setBackgroundDrawableResource(bgRes);//Drawable(d);
                //getWindow().setBackgroundDrawable(d);
            } else if (clBackground != 0) {
                contentView.setBackgroundColor(clBackground);
                //getWindow().setBackgroundDrawable(Utils.solidColorDrawable(clBackground));
            }
        } else {
//			if (bgRes != 0)
//				getWindow().setBackgroundDrawableResource(bgRes);
//			else if (clBackground != 0)
//				getWindow().setBackgroundDrawable(Utils.solidColorDrawable(clBackground));
        }
        a.recycle();
    }

    public boolean isFullscreen() {
        return mFullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        if (mFullscreen != fullscreen) {
            mFullscreen = fullscreen;
            applyFullscreen(getWindow());
        }
    }

    public void applyFullscreen(Window wnd) {
        if (mFullscreen) {
            //mActivity.getWindow().requestFeature(Window.)
            wnd.setFlags(LayoutParams.FLAG_FULLSCREEN,
                    LayoutParams.FLAG_FULLSCREEN);
        } else {
            wnd.setFlags(0,
                    LayoutParams.FLAG_FULLSCREEN);
        }
        setSystemUiVisibility();
    }

    protected boolean wantHideNavbarInFullscreen() {
        return false;
    }

    public boolean setSystemUiVisibility() {
        if (DeviceInfo.getSDKLevel() >= DeviceInfo.HONEYCOMB) {
            int flags = 0;
            if (getKeyBacklight() == 0)
                flags |= SYSTEM_UI_FLAG_LOW_PROFILE;
//			if (isFullscreen() && wantHideNavbarInFullscreen() && isSmartphone())
//				flags |= SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            setSystemUiVisibility(flags);
//			if (isFullscreen() && DeviceInfo.getSDKLevel() >= DeviceInfo.ICE_CREAM_SANDWICH)
//				simulateTouch();
            return true;
        }
        return false;
    }

    //private boolean systemUiVisibilityListenerIsSet = false;
    @TargetApi(11)
    @SuppressLint("NewApi")
    private boolean setSystemUiVisibility(int value) {
        if (DeviceInfo.getSDKLevel() >= DeviceInfo.HONEYCOMB) {
            if (DeviceInfo.getSDKLevel() < 19) {
//			if (!systemUiVisibilityListenerIsSet && contentView != null) {
//				contentView.setOnSystemUiVisibilityChangeListener(new OnSystemUiVisibilityChangeListener() {
//					@Override
//					public void onSystemUiVisibilityChange(int visibility) {
//						lastSystemUiVisibility = visibility;
//					}
//				});
//			}
                boolean a4 = DeviceInfo.getSDKLevel() >= DeviceInfo.ICE_CREAM_SANDWICH;
                if (!a4)
                    value &= SYSTEM_UI_FLAG_LOW_PROFILE;
                if (value == lastSystemUiVisibility && value != SYSTEM_UI_FLAG_LOW_PROFILE)// && a4)
                    return false;
                lastSystemUiVisibility = value;

                View view;
                //if (a4)
                view = getWindow().getDecorView(); // getReaderView();
                //else
                //	view = mActivity.getContentView(); // getReaderView();

                if (view == null)
                    return false;
                Method m;
                try {
                    m = view.getClass().getMethod("setSystemUiVisibility", int.class);
                    m.invoke(view, value);
                    return true;
                } catch (SecurityException e) {
                    // ignore
                } catch (NoSuchMethodException e) {
                    // ignore
                } catch (IllegalArgumentException e) {
                    // ignore
                } catch (IllegalAccessException e) {
                    // ignore
                } catch (InvocationTargetException e) {
                    // ignore
                }
            }
        }
        return false;
    }

    public int getKeyBacklight() {
        return currentKeyBacklightLevel;
    }

    public boolean setKeyBacklight(int value) {
        currentKeyBacklightLevel = value;
        // Try ICS way
        if (DeviceInfo.getSDKLevel() >= DeviceInfo.HONEYCOMB) {
            setSystemUiVisibility();
        }
        // thread safe
        return Engine.getInstance(this).setKeyBacklight(value);
    }

    public boolean isKeyBacklightDisabled() {
        return keyBacklightOff;
    }

    public void setKeyBacklightDisabled(boolean disabled) {
        keyBacklightOff = disabled;
        onUserActivity();
    }

    public void setScreenBacklightLevel(int percent) {
        if (percent < -1)
            percent = -1;
        else if (percent > 100)
            percent = -1;
        screenBacklightBrightness = percent;
        onUserActivity();
    }

    private void turnOffKeyBacklight() {
        if (!isStarted())
            return;
        if (DeviceInfo.getSDKLevel() >= DeviceInfo.HONEYCOMB) {
            setKeyBacklight(0);
        }
        // repeat again in short interval
        if (!Engine.getInstance(this).setKeyBacklight(0)) {
            //log.w("Cannot control key backlight directly");
            return;
        }
        // repeat again in short interval
        Runnable task = new Runnable() {
            @Override
            public void run() {
                if (!isStarted())
                    return;
                if (!Engine.getInstance(BaseActivity.this).setKeyBacklight(0)) {
                    //log.w("Cannot control key backlight directly (delayed)");
                }
            }
        };
        BackgroundThread.instance().postGUI(task, 1);
        //BackgroundThread.instance().postGUI(task, 10);
    }

    private void updateBacklightBrightness(float b) {
        Window wnd = getWindow();
        if (wnd != null) {
            LayoutParams attrs = wnd.getAttributes();
            boolean changed = false;
            if (b < 0 && b > -0.99999f) {
                //log.d("dimming screen by " + (int)((1 + b)*100) + "%");
                b = -b * attrs.screenBrightness;
                if (b < 0.15)
                    return;
            }
            float delta = attrs.screenBrightness - b;
            if (delta < 0)
                delta = -delta;
            if (delta > 0.01) {
                attrs.screenBrightness = b;
                changed = true;
            }
            if (changed) {
                log.d("Window attribute changed: " + attrs);
                wnd.setAttributes(attrs);
            }
        }
    }

    private void updateButtonsBrightness(float buttonBrightness) {
        Window wnd = getWindow();
        if (wnd != null) {
            LayoutParams attrs = wnd.getAttributes();
            boolean changed = false;
            // hack to set buttonBrightness field
            //float buttonBrightness = keyBacklightOff ? 0.0f : -1.0f;
            if (!brightnessHackError)
                try {
                    Field bb = attrs.getClass().getField("buttonBrightness");
                    if (bb != null) {
                        Float oldValue = (Float) bb.get(attrs);
                        if (oldValue == null || oldValue.floatValue() != buttonBrightness) {
                            bb.set(attrs, buttonBrightness);
                            changed = true;
                        }
                    }
                } catch (Exception e) {
                    log.e("WindowManager.LayoutParams.buttonBrightness field is not found, cannot turn buttons backlight off");
                    brightnessHackError = true;
                }
            //attrs.buttonBrightness = 0;
            if (changed) {
                log.d("Window attribute changed: " + attrs);
                wnd.setAttributes(attrs);
            }
            if (keyBacklightOff)
                turnOffKeyBacklight();
        }
    }

    protected void setDimmingAlpha(int alpha) {
        // override it
    }

    protected boolean allowLowBrightness() {
        // override to force higher brightness in non-reading mode (to avoid black screen on some devices when brightness level set to small value)
        return true;
    }

    public void onUserActivity() {
        if (backlightControl != null)
            backlightControl.onUserActivity();
        // Hack
        //if ( backlightControl.isHeld() )
        BackgroundThread.instance().executeGUI(new Runnable() {
            @Override
            public void run() {
                try {
                    float b;
                    int dimmingAlpha = 255;
                    // screenBacklightBrightness is 0..100
                    if (screenBacklightBrightness >= 0) {
                        int percent = screenBacklightBrightness;
                        if (!allowLowBrightness() && percent < MIN_BRIGHTNESS_IN_BROWSER)
                            percent = MIN_BRIGHTNESS_IN_BROWSER;
                        float minb = MIN_BACKLIGHT_LEVEL_PERCENT / 100.0f;
                        if (percent >= 10) {
                            // real brightness control, no colors dimming
                            b = (percent - 10) / (100.0f - 10.0f); // 0..1
                            b = minb + b * (1 - minb); // minb..1
                            if (b < minb) // BRIGHTNESS_OVERRIDE_OFF
                                b = minb;
                            else if (b > 1.0f)
                                b = 1.0f; //BRIGHTNESS_OVERRIDE_FULL
                        } else {
                            // minimal brightness with colors dimming
                            b = minb;
                            dimmingAlpha = 255 - (11 - percent) * 180 / 10;
                        }
                    } else {
                        // system
                        b = -1.0f; //BRIGHTNESS_OVERRIDE_NONE
                    }
                    setDimmingAlpha(dimmingAlpha);
                    //log.v("Brightness: " + b + ", dim: " + dimmingAlpha);
                    updateBacklightBrightness(b);
                    updateButtonsBrightness(keyBacklightOff ? 0.0f : -1.0f);
                } catch (Exception e) {
                    // ignore
                }
            }
        });
    }

    public boolean isWakeLockEnabled() {
        return screenBacklightDuration > 0;
    }

    /**
     * @param backlightDurationMinutes 0 = system default, 1 == 3 minutes, 2..5 == 2..5 minutes
     */
    public void setScreenBacklightDuration(int backlightDurationMinutes) {
        if (backlightDurationMinutes == 1)
            backlightDurationMinutes = 3;
        if (screenBacklightDuration != backlightDurationMinutes * 60 * 1000) {
            screenBacklightDuration = backlightDurationMinutes * 60 * 1000;
            if (screenBacklightDuration == 0)
                backlightControl.release();
            else
                backlightControl.onUserActivity();
        }
    }

    public void releaseBacklightControl() {
        backlightControl.release();
    }

    public int getScreenUpdateMode() {
        return mScreenUpdateMode;
    }

    public void setScreenUpdateMode(int screenUpdateMode, View view) {
        //if (mReaderView != null) {
        mScreenUpdateMode = screenUpdateMode;
        if (EinkScreen.UpdateMode != screenUpdateMode || EinkScreen.UpdateMode == 2) {
            EinkScreen.ResetController(screenUpdateMode, view);
        }
        //}
    }

    public int getScreenUpdateInterval() {
        return mScreenUpdateInterval;
    }

    public void setScreenUpdateInterval(int screenUpdateInterval, View view) {
        mScreenUpdateInterval = screenUpdateInterval;
        if (EinkScreen.UpdateModeInterval != screenUpdateInterval) {
            EinkScreen.UpdateModeInterval = screenUpdateInterval;
            EinkScreen.ResetController(mScreenUpdateMode, view);
        }
    }

    public View getContentView() {
        return contentView;
    }

    public void setContentView(View view) {
        this.contentView = view;
        super.setContentView(view);
        //systemUiVisibilityListenerIsSet = false;
        //updateBackground();
        //setCurrentTheme(currentTheme);
    }

    public boolean isNightMode() {
        return mNightMode;
    }

    public void setNightMode(boolean nightMode) {
        mNightMode = nightMode;
    }

    public ClipboardManager getClipboardmanager() {
        return (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
    }

    public void showHomeScreen() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    public String getLastGeneratedHelpFileSignature() {
        SharedPreferences pref = getSharedPreferences(PREF_FILE, 0);
        String res = pref.getString(PREF_HELP_FILE, null);
        return res;
    }

    public void setLastGeneratedHelpFileSignature(String v) {
        SharedPreferences pref = getSharedPreferences(PREF_FILE, 0);
        pref.edit().putString(PREF_HELP_FILE, v).commit();
    }

    public String getCurrentLanguage() {
        return currentLanguage;
    }

    public void setLanguage(String lang) {
        setLanguage(Lang.byCode(lang));
    }

    public void setLanguage(Lang lang) {
        try {
            Resources res = getResources();
            // Change locale settings in the app.
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = (lang == Lang.DEFAULT) ? defaultLocale : lang.getLocale();
            currentLanguage = (lang == Lang.DEFAULT) ? Lang.getCode(defaultLocale) : lang.code;
            res.updateConfiguration(conf, dm);
        } catch (Exception e) {
            log.e("error while setting locale " + lang, e);
        }
    }

    public void applyAppSetting(String key, String value) {
        boolean flg = "1".equals(value);
        if (key.equals(PROP_APP_FULLSCREEN)) {
            setFullscreen("1".equals(value));
        } else if (key.equals(PROP_APP_LOCALE)) {
            setLanguage(value);
        } else if (key.equals(PROP_APP_KEY_BACKLIGHT_OFF)) {
            setKeyBacklightDisabled(flg);
        } else if (key.equals(PROP_APP_SCREEN_BACKLIGHT_LOCK)) {
            int n = 0;
            try {
                n = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                // ignore
            }
            setScreenBacklightDuration(n);
        } else if (key.equals(PROP_NIGHT_MODE)) {
            setNightMode(flg);
        } else if (key.equals(PROP_APP_SCREEN_UPDATE_MODE)) {
            setScreenUpdateMode(stringToInt(value, 0), getContentView());
        } else if (key.equals(PROP_APP_SCREEN_UPDATE_INTERVAL)) {
            setScreenUpdateInterval(stringToInt(value, 10), getContentView());
        } else if (key.equals(PROP_APP_THEME)) {
            //setCurrentTheme(value);
        } else if (!DeviceInfo.EINK_SCREEN && PROP_APP_SCREEN_BACKLIGHT.equals(key)) {
            try {
                final int n = Integer.valueOf(value);
                // delay before setting brightness
                BackgroundThread.instance().postGUI(new Runnable() {
                    public void run() {
                        BackgroundThread.instance().postBackground(new Runnable() {
                            public void run() {
                                BackgroundThread.instance().postGUI(new Runnable() {
                                    public void run() {
                                        setScreenBacklightLevel(n);
                                    }
                                });
                            }
                        });
                    }
                }, 100);
            } catch (Exception e) {
                // ignore
            }
        } else if (key.equals(PROP_APP_FILE_BROWSER_HIDE_EMPTY_FOLDERS)) {
            Services.getScanner().setHideEmptyDirs(flg);
        }
    }

    public void showNotice(int questionResourceId, final Runnable action, final Runnable cancelAction) {
        NoticeDialog dlg = new NoticeDialog(this, this.getString(questionResourceId));
        dlg.show();
    }

    public void askConfirmation(int questionResourceId, final Runnable action) {
        askConfirmation(questionResourceId, action, null);
    }

    public void askConfirmation(int questionResourceId, final Runnable action, final Runnable cancelAction) {
        AlertDialog.Builder dlg = new AlertDialog.Builder(this);

        final TextView myView = new TextView(getApplicationContext());
        myView.setText(questionResourceId);
        //myView.setTextSize(12);
        dlg.setView(myView);
        //dlg.setTitle(questionResourceId);
        dlg.setPositiveButton(R.string.dlg_button_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                action.run();
            }
        });
        dlg.setNegativeButton(R.string.dlg_button_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                if (cancelAction != null)
                    cancelAction.run();
            }
        });
        dlg.show();
    }

    public void askConfirmation(String question, final Runnable action) {
        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        dlg.setTitle(question);
        dlg.setPositiveButton(R.string.dlg_button_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                action.run();
            }
        });
        dlg.setNegativeButton(R.string.dlg_button_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                // do nothing
            }
        });
        dlg.show();
    }

    public void directoryUpdated(FileInfo dir) {
        // override it to use
    }

    public void onSettingsChanged(Properties props, Properties oldProps) {
        // override for specific actions

    }

    public int getCurrentProfile() {
        if (currentProfile == 0) {
            currentProfile = mSettingsManager.getInt(PROP_PROFILE_NUMBER, 1);
            if (currentProfile < 1 || currentProfile > MAX_PROFILES)
                currentProfile = 1;
        }
        return currentProfile;
    }

    public void setCurrentProfile(int profile) {
        if (profile == 0 || profile == getCurrentProfile())
            return;
        log.i("Switching from profile " + currentProfile + " to " + profile);
        mSettingsManager.saveSettings(currentProfile, null);
        final Properties loadedSettings = mSettingsManager.loadSettings(profile);
        mSettingsManager.setSettings(loadedSettings, 0, true);
        currentProfile = profile;
    }

    public void setSetting(String name, String value, boolean notify) {
        mSettingsManager.setSetting(name, value, notify);
    }

    public void setSettings(Properties settings, int delayMillis, boolean notify) {
        mSettingsManager.setSettings(settings, delayMillis, notify);
    }

    public void notifySettingsChanged() {
        setSettings(mSettingsManager.get(), -1, true);
    }

    public boolean isPackageInstalled(String packageName) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(packageName, 0); //PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public boolean hasHardwareMenuKey() {
        if (hasHardwareMenuKey == null) {
            ViewConfiguration vc = ViewConfiguration.get(this);
            if (DeviceInfo.getSDKLevel() >= 14) {
                //boolean vc.hasPermanentMenuKey();
                try {
                    Method m = vc.getClass().getMethod("hasPermanentMenuKey", new Class<?>[]{});
                    try {
                        hasHardwareMenuKey = (Boolean) m.invoke(vc, new Object[]{});
                    } catch (IllegalArgumentException e) {
                        hasHardwareMenuKey = false;
                    } catch (IllegalAccessException e) {
                        hasHardwareMenuKey = false;
                    } catch (InvocationTargetException e) {
                        hasHardwareMenuKey = false;
                    }
                } catch (NoSuchMethodException e) {
                    hasHardwareMenuKey = false;
                }
            }
            if (hasHardwareMenuKey == null) {
                if (DeviceInfo.EINK_SCREEN)
                    hasHardwareMenuKey = false;
                else if (DeviceInfo.getSDKLevel() < DeviceInfo.ICE_CREAM_SANDWICH)
                    hasHardwareMenuKey = true;
                else
                    hasHardwareMenuKey = false;
            }
        }
        return hasHardwareMenuKey;
    }

    private static class SettingsManager {

        public static final Logger log = L.create("cr");
        private static final String SETTINGS_FILE_NAME = "cr3.ini";
        private static DefKeyAction[] DEF_KEY_ACTIONS = {
                new DefKeyAction(KeyEvent.KEYCODE_BACK, ReaderAction.NORMAL, ReaderAction.EXIT),
                new DefKeyAction(KeyEvent.KEYCODE_BACK, ReaderAction.LONG, ReaderAction.EXIT),
                new DefKeyAction(KeyEvent.KEYCODE_BACK, ReaderAction.DOUBLE, ReaderAction.EXIT),
                new DefKeyAction(KeyEvent.KEYCODE_DPAD_CENTER, ReaderAction.NORMAL, ReaderAction.RECENT_BOOKS),
                new DefKeyAction(KeyEvent.KEYCODE_DPAD_CENTER, ReaderAction.LONG, ReaderAction.BOOKMARKS),
                new DefKeyAction(KeyEvent.KEYCODE_DPAD_UP, ReaderAction.NORMAL, ReaderAction.PAGE_UP),
                new DefKeyAction(KeyEvent.KEYCODE_DPAD_DOWN, ReaderAction.NORMAL, ReaderAction.PAGE_DOWN),
                new DefKeyAction(KeyEvent.KEYCODE_DPAD_UP, ReaderAction.LONG, (DeviceInfo.EINK_SONY ? ReaderAction.PAGE_UP_10 : ReaderAction.REPEAT)),
                new DefKeyAction(KeyEvent.KEYCODE_DPAD_DOWN, ReaderAction.LONG, (DeviceInfo.EINK_SONY ? ReaderAction.PAGE_DOWN_10 : ReaderAction.REPEAT)),
                new DefKeyAction(KeyEvent.KEYCODE_DPAD_LEFT, ReaderAction.NORMAL, (DeviceInfo.NAVIGATE_LEFTRIGHT ? ReaderAction.PAGE_UP : ReaderAction.PAGE_UP_10)),
                new DefKeyAction(KeyEvent.KEYCODE_DPAD_RIGHT, ReaderAction.NORMAL, (DeviceInfo.NAVIGATE_LEFTRIGHT ? ReaderAction.PAGE_DOWN : ReaderAction.PAGE_DOWN_10)),
                new DefKeyAction(KeyEvent.KEYCODE_DPAD_LEFT, ReaderAction.LONG, ReaderAction.REPEAT),
                new DefKeyAction(KeyEvent.KEYCODE_DPAD_RIGHT, ReaderAction.LONG, ReaderAction.REPEAT),
                new DefKeyAction(KeyEvent.KEYCODE_VOLUME_UP, ReaderAction.NORMAL, ReaderAction.PAGE_UP),
                new DefKeyAction(KeyEvent.KEYCODE_VOLUME_DOWN, ReaderAction.NORMAL, ReaderAction.PAGE_DOWN),
                new DefKeyAction(KeyEvent.KEYCODE_VOLUME_UP, ReaderAction.LONG, ReaderAction.REPEAT),
                new DefKeyAction(KeyEvent.KEYCODE_VOLUME_DOWN, ReaderAction.LONG, ReaderAction.REPEAT),
                new DefKeyAction(KeyEvent.KEYCODE_MENU, ReaderAction.NORMAL, ReaderAction.READER_MENU),
                new DefKeyAction(KeyEvent.KEYCODE_MENU, ReaderAction.LONG, ReaderAction.OPTIONS),
                new DefKeyAction(KeyEvent.KEYCODE_CAMERA, ReaderAction.NORMAL, ReaderAction.NONE),
                new DefKeyAction(KeyEvent.KEYCODE_CAMERA, ReaderAction.LONG, ReaderAction.NONE),
                new DefKeyAction(KeyEvent.KEYCODE_SEARCH, ReaderAction.NORMAL, ReaderAction.SEARCH),
                new DefKeyAction(KeyEvent.KEYCODE_SEARCH, ReaderAction.LONG, ReaderAction.TOGGLE_SELECTION_MODE),

                new DefKeyAction(ReaderView.NOOK_KEY_NEXT_RIGHT, ReaderAction.NORMAL, ReaderAction.PAGE_DOWN),
                new DefKeyAction(ReaderView.NOOK_KEY_SHIFT_DOWN, ReaderAction.NORMAL, ReaderAction.PAGE_DOWN),
                new DefKeyAction(ReaderView.NOOK_KEY_PREV_LEFT, ReaderAction.NORMAL, ReaderAction.PAGE_UP),
                new DefKeyAction(ReaderView.NOOK_KEY_PREV_RIGHT, ReaderAction.NORMAL, ReaderAction.PAGE_UP),
                new DefKeyAction(ReaderView.NOOK_KEY_SHIFT_UP, ReaderAction.NORMAL, ReaderAction.PAGE_UP),

                new DefKeyAction(ReaderView.NOOK_12_KEY_NEXT_LEFT, ReaderAction.NORMAL, (DeviceInfo.EINK_NOOK ? ReaderAction.PAGE_UP : ReaderAction.PAGE_DOWN)),
                new DefKeyAction(ReaderView.NOOK_12_KEY_NEXT_LEFT, ReaderAction.LONG, (DeviceInfo.EINK_NOOK ? ReaderAction.PAGE_UP_10 : ReaderAction.PAGE_DOWN_10)),

                new DefKeyAction(ReaderView.KEYCODE_PAGE_BOTTOMLEFT, ReaderAction.NORMAL, ReaderAction.PAGE_UP),
//			new DefKeyAction(ReaderView.KEYCODE_PAGE_BOTTOMRIGHT, ReaderAction.NORMAL, ReaderAction.PAGE_UP),
                new DefKeyAction(ReaderView.KEYCODE_PAGE_TOPLEFT, ReaderAction.NORMAL, ReaderAction.PAGE_DOWN),
                new DefKeyAction(ReaderView.KEYCODE_PAGE_TOPRIGHT, ReaderAction.NORMAL, ReaderAction.PAGE_DOWN),
                new DefKeyAction(ReaderView.KEYCODE_PAGE_BOTTOMLEFT, ReaderAction.LONG, ReaderAction.PAGE_UP_10),
//			new DefKeyAction(ReaderView.KEYCODE_PAGE_BOTTOMRIGHT, ReaderAction.LONG, ReaderAction.PAGE_UP_10),
                new DefKeyAction(ReaderView.KEYCODE_PAGE_TOPLEFT, ReaderAction.LONG, ReaderAction.PAGE_DOWN_10),
                new DefKeyAction(ReaderView.KEYCODE_PAGE_TOPRIGHT, ReaderAction.LONG, ReaderAction.PAGE_DOWN_10),

                new DefKeyAction(ReaderView.SONY_DPAD_DOWN_SCANCODE, ReaderAction.NORMAL, ReaderAction.PAGE_DOWN),
                new DefKeyAction(ReaderView.SONY_DPAD_UP_SCANCODE, ReaderAction.NORMAL, ReaderAction.PAGE_UP),
                new DefKeyAction(ReaderView.SONY_DPAD_DOWN_SCANCODE, ReaderAction.LONG, ReaderAction.PAGE_DOWN_10),
                new DefKeyAction(ReaderView.SONY_DPAD_UP_SCANCODE, ReaderAction.LONG, ReaderAction.PAGE_UP_10),

                new DefKeyAction(KeyEvent.KEYCODE_8, ReaderAction.NORMAL, ReaderAction.PAGE_DOWN),
                new DefKeyAction(KeyEvent.KEYCODE_2, ReaderAction.NORMAL, ReaderAction.PAGE_UP),
                new DefKeyAction(KeyEvent.KEYCODE_8, ReaderAction.LONG, ReaderAction.PAGE_DOWN_10),
                new DefKeyAction(KeyEvent.KEYCODE_2, ReaderAction.LONG, ReaderAction.PAGE_UP_10),

                new DefKeyAction(ReaderView.KEYCODE_ESCAPE, ReaderAction.NORMAL, ReaderAction.PAGE_DOWN),
                new DefKeyAction(ReaderView.KEYCODE_ESCAPE, ReaderAction.LONG, ReaderAction.REPEAT),

//		    public static final int KEYCODE_PAGE_BOTTOMLEFT = 0x5d; // fwd
//		    public static final int KEYCODE_PAGE_BOTTOMRIGHT = 0x5f; // fwd
//		    public static final int KEYCODE_PAGE_TOPLEFT = 0x5c; // back
//		    public static final int KEYCODE_PAGE_TOPRIGHT = 0x5e; // back

        };
        private static DefTapAction[] DEF_TAP_ACTIONS = {
                new DefTapAction(1, false, ReaderAction.PAGE_DOWN),
                new DefTapAction(2, false, ReaderAction.READER_MENU),
                new DefTapAction(3, false, ReaderAction.PAGE_UP),
                new DefTapAction(4, false, ReaderAction.PAGE_DOWN),
                new DefTapAction(5, false, ReaderAction.READER_MENU),
                new DefTapAction(6, false, ReaderAction.PAGE_UP),
                new DefTapAction(7, false, ReaderAction.PAGE_DOWN),
                new DefTapAction(8, false, ReaderAction.READER_MENU),
                new DefTapAction(9, false, ReaderAction.PAGE_UP),
                //new DefTapAction(1, false, ReaderAction.GO_BACK), // back by link
//                new DefTapAction(2, false, ReaderAction.TOGGLE_DAY_NIGHT),
//                new DefTapAction(4, false, ReaderAction.PAGE_UP_10),
//                new DefTapAction(6, false, ReaderAction.PAGE_UP),
//                new DefTapAction(7, false, ReaderAction.PAGE_DOWN),
//                new DefTapAction(8, false, ReaderAction.PAGE_DOWN),
//                new DefTapAction(9, false, ReaderAction.PAGE_DOWN),
                //new DefTapAction(3, false, ReaderAction.TOGGLE_AUTOSCROLL),
//                new DefTapAction(6, false, ReaderAction.PAGE_DOWN_10),
//                new DefTapAction(7, false, ReaderAction.PAGE_DOWN_10),
//                new DefTapAction(8, false, ReaderAction.PAGE_DOWN_10),
//                new DefTapAction(9, false, ReaderAction.PAGE_DOWN_10),
//                new DefTapAction(5, false, ReaderAction.OPTIONS),
        };
        private static boolean DEBUG_RESET_OPTIONS = false;
        private final DisplayMetrics displayMetrics = new DisplayMetrics();
        private final File defaultSettingsDir;
        File propsFile;
        DelayedExecutor saveSettingsTask = DelayedExecutor.createBackground("saveSettings");
        private BaseActivity mActivity;
        private Properties mSettings;
        private boolean isSmartphone;

        public SettingsManager(BaseActivity activity) {
            this.mActivity = activity;
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            defaultSettingsDir = activity.getDir("settings", Context.MODE_PRIVATE);
            isSmartphone = activity.isSmartphone();
            mSettings = loadSettings();
        }

        public static Properties filterProfileSettings(Properties settings) {
            Properties res = new Properties();
            res.entrySet();
            for (Object k : settings.keySet()) {
                String key = (String) k;
                String value = settings.getProperty(key);
                boolean found = false;
                for (String pattern : Settings.PROFILE_SETTINGS) {
                    if (pattern.endsWith("*")) {
                        if (key.startsWith(pattern.substring(0, pattern.length() - 1))) {
                            found = true;
                            break;
                        }
                    } else if (pattern.equalsIgnoreCase(key)) {
                        found = true;
                        break;
                    } else if (key.startsWith("styles.")) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    res.setProperty(key, value);
                }
            }
            return res;
        }

        //int lastSaveId = 0;
        public void setSettings(Properties settings, int delayMillis, boolean notify) {
            Properties oldSettings = mSettings;
            mSettings = new Properties(settings);
            if (delayMillis >= 0) {
                saveSettingsTask.postDelayed(new Runnable() {
                    public void run() {
                        BackgroundThread.instance().postGUI(new Runnable() {
                            @Override
                            public void run() {
                                saveSettings(mSettings);
                            }
                        });
                    }
                }, delayMillis);
            }
            if (notify)
                mActivity.onSettingsChanged(mSettings, oldSettings);
        }

        public void setSetting(String name, String value, boolean notify) {
            Properties props = new Properties(mSettings);
            if (value.equals(mSettings.getProperty(name)))
                return;
            props.setProperty(name, value);
            setSettings(props, 1000, notify);
        }

        private boolean isValidFontFace(String face) {
            String[] fontFaces = Engine.getFontFaceList();
            if (fontFaces == null)
                return true;
            for (String item : fontFaces) {
                if (item.equals(face))
                    return true;
            }
            return false;
        }

        private boolean applyDefaultFont(Properties props, String propName, String defFontFace) {
            String currentValue = props.getProperty(propName);
            boolean changed = false;
            if (currentValue == null) {
                currentValue = defFontFace;
                changed = true;
            }
            if (!isValidFontFace(currentValue)) {
                if (isValidFontFace("Droid Sans"))
                    currentValue = "Droid Sans";
                else if (isValidFontFace("Roboto"))
                    currentValue = "Roboto";
                else if (isValidFontFace("Droid Serif"))
                    currentValue = "Droid Serif";
                else if (isValidFontFace("Arial"))
                    currentValue = "Arial";
                else if (isValidFontFace("Times New Roman"))
                    currentValue = "Times New Roman";
                else if (isValidFontFace("Droid Sans Fallback"))
                    currentValue = "Droid Sans Fallback";
                else {
                    String[] fontFaces = Engine.getFontFaceList();
                    if (fontFaces != null)
                        currentValue = fontFaces[0];
                }
                changed = true;
            }
            if (changed)
                props.setProperty(propName, currentValue);
            return changed;
        }

        public boolean fixFontSettings(Properties props) {
            boolean res = false;
            res = applyDefaultFont(props, ReaderView.PROP_FONT_FACE, DeviceInfo.DEF_FONT_FACE) || res;
            res = applyDefaultFont(props, ReaderView.PROP_STATUS_FONT_FACE, DeviceInfo.DEF_FONT_FACE) || res;
            res = applyDefaultFont(props, ReaderView.PROP_FALLBACK_FONT_FACE, "Droid Sans Fallback") || res;
            return res;
        }

        public Properties loadSettings(BaseActivity activity, File file) {
            Properties props = new Properties();

            if (file.exists() && !DEBUG_RESET_OPTIONS) {
                try {
                    FileInputStream is = new FileInputStream(file);
                    props.load(is);
                    log.v("" + props.size() + " settings items loaded from file " + propsFile.getAbsolutePath());
                } catch (Exception e) {
                    log.e("error while reading settings");
                }
            }

            // default key actions
            boolean menuKeyActionFound = false;
            for (DefKeyAction ka : DEF_KEY_ACTIONS) {
                props.applyDefault(ka.getProp(), ka.action.id);
                if (ReaderAction.READER_MENU.id.equals(ka.action.id))
                    menuKeyActionFound = true;
            }

            boolean menuTapActionFound = false;
            for (DefTapAction ka : DEF_TAP_ACTIONS) {
                String paramName = ka.longPress ? ReaderView.PROP_APP_TAP_ZONE_ACTIONS_TAP + ".long." + ka.zone : ReaderView.PROP_APP_TAP_ZONE_ACTIONS_TAP + "." + ka.zone;
                String value = props.getProperty(paramName);
                if (ReaderAction.READER_MENU.id.equals(value))
                    menuTapActionFound = true;
            }

            // default tap zone actions
            for (DefTapAction ka : DEF_TAP_ACTIONS) {
                String paramName = ka.longPress ? ReaderView.PROP_APP_TAP_ZONE_ACTIONS_TAP + ".long." + ka.zone : ReaderView.PROP_APP_TAP_ZONE_ACTIONS_TAP + "." + ka.zone;

                if (ka.zone == 5 && !activity.hasHardwareMenuKey() && !menuTapActionFound && !menuKeyActionFound) {
                    // force assignment of central tap zone
                    props.setProperty(paramName, ka.action.id);
                } else {
                    props.applyDefault(paramName, ka.action.id);
                }
            }

//            if (DeviceInfo.EINK_NOOK) {
//                props.applyDefault(ReaderView.PROP_PAGE_ANIMATION, ReaderView.PAGE_ANIMATION_NONE);
//            } else {
//                props.applyDefault(ReaderView.PROP_PAGE_ANIMATION, ReaderView.PAGE_ANIMATION_SLIDE2);
//            }
            props.applyDefault(ReaderView.PROP_PAGE_ANIMATION, ReaderView.PAGE_ANIMATION_PAPER);
            props.applyDefault(ReaderView.PROP_APP_LOCALE, Lang.DEFAULT.code);

            props.applyDefault(ReaderView.PROP_APP_THEME, "LIGHT");
            props.applyDefault(ReaderView.PROP_APP_THEME_DAY, "LIGHT");
            props.applyDefault(ReaderView.PROP_APP_THEME_NIGHT, "DARK");
            props.applyDefault(ReaderView.PROP_APP_SELECTION_PERSIST, "1");
            props.applyDefault(ReaderView.PROP_APP_SCREEN_BACKLIGHT_LOCK, "3");
            if ("1".equals(props.getProperty(ReaderView.PROP_APP_SCREEN_BACKLIGHT_LOCK)))
                props.setProperty(ReaderView.PROP_APP_SCREEN_BACKLIGHT_LOCK, "3");
            props.applyDefault(ReaderView.PROP_APP_BOOK_PROPERTY_SCAN_ENABLED, "0");
            props.applyDefault(ReaderView.PROP_APP_KEY_BACKLIGHT_OFF, DeviceInfo.SAMSUNG_BUTTONS_HIGHLIGHT_PATCH ? "0" : "1");
            props.applyDefault(ReaderView.PROP_LANDSCAPE_PAGES, DeviceInfo.ONE_COLUMN_IN_LANDSCAPE ? "0" : "1");
            // autodetect best initial font size based on display resolution
            int screenWidth = displayMetrics.widthPixels;//getWindowManager().getDefaultDisplay().getWidth();
            System.out.println("Screen width = " + screenWidth);
            int fontSize = 20;
            int fontSizeStatus = 14;
            String hmargin = "4";
            String vmargin = "2";
            if (screenWidth <= 320) {
                fontSize = 20;
                fontSizeStatus = 12;
                hmargin = "4";
                vmargin = "2";
            } else if (screenWidth <= 400) {
                fontSize = 25;
                fontSizeStatus = 14;
                hmargin = "10";
                vmargin = "4";
            } else if (screenWidth <= 600) {
                fontSize = 30;
                fontSizeStatus = 18;
                hmargin = "14";
                vmargin = "6";
            } else if (screenWidth <= 1000) {
                fontSize = 44;
                fontSizeStatus = 22;
                hmargin = "18";
                vmargin = "8";
            } else {
                fontSize = 60;
                fontSizeStatus = 33;
                hmargin = "22";
                vmargin = "10";
            }

            fixFontSettings(props);
            props.applyDefault(ReaderView.PROP_STATUS_LOCATION, VIEWER_STATUS_PAGE);
            props.applyDefault(ReaderView.PROP_STATUS_FONT_SIZE, String.valueOf(fontSizeStatus));
            props.applyDefault(ReaderView.PROP_STATUS_FONT_COLOR, "#FF000000"); // don't use separate color
            props.applyDefault(ReaderView.PROP_STATUS_FONT_COLOR_DAY, "#FF000000"); // don't use separate color
            props.applyDefault(ReaderView.PROP_STATUS_FONT_COLOR_NIGHT, "#80000000"); // don't use separate color

            props.applyDefault(ReaderView.PROP_FONT_SIZE, String.valueOf(fontSize));
            props.applyDefault(ReaderView.PROP_INTERLINE_SPACE, 150);
            props.applyDefault(ReaderView.PROP_FONT_HINTING, "2");
            props.applyDefault(ReaderView.PROP_FONT_COLOR, "#000000");
            props.applyDefault(ReaderView.PROP_FONT_COLOR_DAY, "#000000");
            props.applyDefault(ReaderView.PROP_FONT_COLOR_NIGHT, "#808080");
            props.applyDefault(ReaderView.PROP_BACKGROUND_COLOR, "#FFFFFF");
            props.applyDefault(ReaderView.PROP_BACKGROUND_COLOR_DAY, "#FFFFFF");
            props.applyDefault(ReaderView.PROP_BACKGROUND_COLOR_NIGHT, "#101010");

            props.setProperty(ReaderView.PROP_ROTATE_ANGLE, "0"); // crengine's rotation will not be user anymore
            props.setProperty(ReaderView.PROP_DISPLAY_INVERSE, "0");
            props.applyDefault(ReaderView.PROP_APP_FULLSCREEN, "1");
            props.applyDefault(ReaderView.PROP_APP_VIEW_AUTOSCROLL_SPEED, "1020");
            props.applyDefault(ReaderView.PROP_APP_SCREEN_BACKLIGHT, "-1");
            props.applyDefault(ReaderView.PROP_SHOW_BATTERY, "1");
            props.applyDefault(ReaderView.PROP_SHOW_POS_PERCENT, "0");
            props.applyDefault(ReaderView.PROP_SHOW_PAGE_COUNT, "1");
            props.applyDefault(ReaderView.PROP_SHOW_TIME, "1");
            props.applyDefault(ReaderView.PROP_FONT_ANTIALIASING, "2");
            props.applyDefault(ReaderView.PROP_APP_GESTURE_PAGE_FLIPPING, "1");
            props.applyDefault(ReaderView.PROP_APP_SHOW_COVERPAGES, "1");
            props.applyDefault(ReaderView.PROP_APP_COVERPAGE_SIZE, "2");
//            props.applyDefault(ReaderView.PROP_APP_SCREEN_ORIENTATION, DeviceInfo.EINK_SCREEN ? "0" : "4"); // "0"
            props.applyDefault(ReaderView.PROP_CONTROLS_ENABLE_VOLUME_KEYS, "1");
            props.applyDefault(ReaderView.PROP_APP_TAP_ZONE_HILIGHT, "0");
            props.applyDefault(ReaderView.PROP_APP_BOOK_SORT_ORDER, FileInfo.DEF_SORT_ORDER.name());
            props.applyDefault(ReaderView.PROP_APP_DICTIONARY, dicts[0].id);
            props.applyDefault(ReaderView.PROP_APP_FILE_BROWSER_HIDE_EMPTY_FOLDERS, "0");
            props.applyDefault(ReaderView.PROP_APP_SELECTION_ACTION, "0");
            props.applyDefault(ReaderView.PROP_APP_MULTI_SELECTION_ACTION, "0");

            props.applyDefault(ReaderView.PROP_EMBEDDED_STYLES, "0");
            props.applyDefault(ReaderView.PROP_EMBEDDED_FONTS, "0");
            props.applyDefault(ReaderView.PROP_TXT_OPTION_PREFORMATTED, "0");

            props.applyDefault(ReaderView.PROP_DEF_ALIGN, "text-align: justify");
            props.applyDefault(ReaderView.PROP_DEF_IDENT, "text-indent: 1.2em");

            props.applyDefault(ReaderView.PROP_FLOATING_PUNCTUATION, "1");
            props.applyDefault(ReaderView.PROP_FONT_KERNING_ENABLED, "0");


            props.applyDefault(ReaderView.PROP_IMG_SCALING_ZOOMOUT_BLOCK_MODE, "2");
            props.applyDefault(ReaderView.PROP_IMG_SCALING_ZOOMIN_BLOCK_MODE, "2");
            props.applyDefault(ReaderView.PROP_IMG_SCALING_ZOOMOUT_INLINE_MODE, "2");
            props.applyDefault(ReaderView.PROP_IMG_SCALING_ZOOMIN_INLINE_MODE, "2");
            props.applyDefault(ReaderView.PROP_IMG_SCALING_ZOOMOUT_BLOCK_SCALE, "3");
            props.applyDefault(ReaderView.PROP_IMG_SCALING_ZOOMIN_BLOCK_SCALE, "3");
            props.applyDefault(ReaderView.PROP_IMG_SCALING_ZOOMOUT_INLINE_SCALE, "3");
            props.applyDefault(ReaderView.PROP_IMG_SCALING_ZOOMIN_INLINE_SCALE, "3");

            props.applyDefault(ReaderView.PROP_PAGE_MARGIN_LEFT, hmargin);
            props.applyDefault(ReaderView.PROP_PAGE_MARGIN_RIGHT, hmargin);
            props.applyDefault(ReaderView.PROP_PAGE_MARGIN_TOP, vmargin);
            props.applyDefault(ReaderView.PROP_PAGE_MARGIN_BOTTOM, vmargin);

            props.applyDefault(ReaderView.PROP_APP_SCREEN_UPDATE_MODE, "0");
            props.applyDefault(ReaderView.PROP_APP_SCREEN_UPDATE_INTERVAL, "10");

            props.applyDefault(ReaderView.PROP_NIGHT_MODE, "0");

            if (props.getBool(ReaderView.PROP_NIGHT_MODE, false))
                props.applyDefault(ReaderView.PROP_PAGE_BACKGROUND_IMAGE, Engine.DEF_NIGHT_BACKGROUND_TEXTURE);
            else
                props.applyDefault(ReaderView.PROP_PAGE_BACKGROUND_IMAGE, Engine.DEF_DAY_BACKGROUND_TEXTURE);

            props.applyDefault(ReaderView.PROP_PAGE_BACKGROUND_IMAGE_DAY, Engine.DEF_DAY_BACKGROUND_TEXTURE);

            props.applyDefault(ReaderView.PROP_PAGE_BACKGROUND_IMAGE_NIGHT, Engine.DEF_NIGHT_BACKGROUND_TEXTURE);

            props.applyDefault(ReaderView.PROP_FONT_GAMMA, DeviceInfo.EINK_SCREEN ? "1.5" : "1.0");

            props.setProperty(ReaderView.PROP_MIN_FILE_SIZE_TO_CACHE, "100000");
            props.setProperty(ReaderView.PROP_FORCED_MIN_FILE_SIZE_TO_CACHE, "32768");

            //props.applyDefault(ReaderView.PROP_HYPHENATION_DICT, Engine.HyphDict.NONE.toString());

            props.applyDefault(ReaderView.PROP_APP_FILE_BROWSER_SIMPLE_MODE, "0");

            props.applyDefault(ReaderView.PROP_TOOLBAR_LOCATION, DeviceInfo.getSDKLevel() < DeviceInfo.HONEYCOMB ? Settings.VIEWER_TOOLBAR_NONE : Settings.VIEWER_TOOLBAR_SHORT_SIDE);
            props.applyDefault(ReaderView.PROP_TOOLBAR_HIDE_IN_FULLSCREEN, "1");


            if (!DeviceInfo.EINK_SCREEN) {
                props.applyDefault(ReaderView.PROP_APP_HIGHLIGHT_BOOKMARKS, "1");
                props.applyDefault(ReaderView.PROP_HIGHLIGHT_SELECTION_COLOR, "#AAAAAA");
                props.applyDefault(ReaderView.PROP_HIGHLIGHT_BOOKMARK_COLOR_COMMENT, "#AAAA55");
                props.applyDefault(ReaderView.PROP_HIGHLIGHT_BOOKMARK_COLOR_CORRECTION, "#C07070");
                props.applyDefault(ReaderView.PROP_HIGHLIGHT_SELECTION_COLOR_DAY, "#AAAAAA");
                props.applyDefault(ReaderView.PROP_HIGHLIGHT_BOOKMARK_COLOR_COMMENT_DAY, "#AAAA55");
                props.applyDefault(ReaderView.PROP_HIGHLIGHT_BOOKMARK_COLOR_CORRECTION_DAY, "#C07070");
                props.applyDefault(ReaderView.PROP_HIGHLIGHT_SELECTION_COLOR_NIGHT, "#808080");
                props.applyDefault(ReaderView.PROP_HIGHLIGHT_BOOKMARK_COLOR_COMMENT_NIGHT, "#A09060");
                props.applyDefault(ReaderView.PROP_HIGHLIGHT_BOOKMARK_COLOR_CORRECTION_NIGHT, "#906060");
            } else {
                props.applyDefault(ReaderView.PROP_APP_HIGHLIGHT_BOOKMARKS, "2");
                props.applyDefault(ReaderView.PROP_HIGHLIGHT_SELECTION_COLOR, "#808080");
                props.applyDefault(ReaderView.PROP_HIGHLIGHT_BOOKMARK_COLOR_COMMENT, "#000000");
                props.applyDefault(ReaderView.PROP_HIGHLIGHT_BOOKMARK_COLOR_CORRECTION, "#000000");
            }

            return props;
        }

        public File getSettingsFile(int profile) {
            if (profile == 0)
                return propsFile;
            return new File(propsFile.getAbsolutePath() + ".profile" + profile);
        }

        //Load exist file setting
        private Properties loadSettings() {
            File[] dataDirs = Engine.getDataDirectories(null, false, true);
            File existingFile = null;
            for (File dir : dataDirs) {
                File f = new File(dir, SETTINGS_FILE_NAME);
                if (f.exists() && f.isFile()) {
                    if (SSUtil.getAppVersion(mActivity.getApplicationContext()) != mSharedPreferences.getAppVersionSettingBook(mActivity.getApplicationContext())) {
                        f.delete();
                        mSharedPreferences.saveAppVersionSettingBook(mActivity.getApplicationContext(), SSUtil.getAppVersion(mActivity.getApplicationContext()));
                    } else {
                        existingFile = f;
                    }

                    break;
                }
            }
            if (existingFile != null) {
                propsFile = existingFile;
            } else {
                File propsDir = defaultSettingsDir;
                propsFile = new File(propsDir, SETTINGS_FILE_NAME);
                File dataDir = Engine.getExternalSettingsDir();
                if (dataDir != null) {
                    log.d("external settings dir: " + dataDir);
                    propsFile = Engine.checkOrMoveFile(dataDir, propsDir, SETTINGS_FILE_NAME);
                } else {
                    propsDir.mkdirs();
                }
            }

            Properties props = loadSettings(mActivity, propsFile);

            return props;
        }

        public Properties loadSettings(int profile) {
            File f = getSettingsFile(profile);
            if (!f.exists() && profile != 0)
                f = getSettingsFile(0);
            Properties res = loadSettings(mActivity, f);
            if (profile != 0) {
                res = filterProfileSettings(res);
                res.setInt(Settings.PROP_PROFILE_NUMBER, profile);
            }
            return res;
        }

        public void saveSettings(int profile, Properties settings) {
            if (settings == null)
                settings = mSettings;
            File f = getSettingsFile(profile);
            if (profile != 0) {
                settings = filterProfileSettings(settings);
                settings.setInt(Settings.PROP_PROFILE_NUMBER, profile);
            }
            saveSettings(f, settings);
        }

        public void saveSettings(File f, Properties settings) {
            try {
                log.v("saveSettings()");
                FileOutputStream os = new FileOutputStream(f);
                settings.store(os, "Cool Reader 3 settings");
                log.i("Settings successfully saved to file " + f.getAbsolutePath());
            } catch (Exception e) {
                log.e("exception while saving settings", e);
            }
        }

        public void saveSettings(Properties settings) {
            saveSettings(propsFile, settings);
        }

        public String getSetting(String name) {
            return mSettings.getProperty(name);
        }

        public String getSetting(String name, String defaultValue) {
            return mSettings.getProperty(name, defaultValue);
        }

        public boolean getBool(String name, boolean defaultValue) {
            return mSettings.getBool(name, defaultValue);
        }

        public int getInt(String name, int defaultValue) {
            return mSettings.getInt(name, defaultValue);
        }

        public Properties get() {
            return new Properties(mSettings);
        }

        private static class DefKeyAction {
            public int keyCode;
            public int type;
            public ReaderAction action;

            public DefKeyAction(int keyCode, int type, ReaderAction action) {
                this.keyCode = keyCode;
                this.type = type;
                this.action = action;
            }

            public String getProp() {
                return ReaderView.PROP_APP_KEY_ACTIONS_PRESS + ReaderAction.getTypeString(type) + keyCode;
            }
        }

        private static class DefTapAction {
            public int zone;
            public boolean longPress;
            public ReaderAction action;

            public DefTapAction(int zone, boolean longPress, ReaderAction action) {
                this.zone = zone;
                this.longPress = longPress;
                this.action = action;
            }
        }

    }

    private class ScreenBacklightControl {
        PowerManager.WakeLock wl = null;
        long lastUpdateTimeStamp;

        public ScreenBacklightControl() {
        }

        public void onUserActivity() {
            lastUserActivityTime = Utils.timeStamp();
            if (Utils.timeInterval(lastUpdateTimeStamp) < 5000)
                return;
            lastUpdateTimeStamp = android.os.SystemClock.uptimeMillis();
            if (!isWakeLockEnabled())
                return;
            if (wl == null) {
                PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                /* | PowerManager.ON_AFTER_RELEASE */, "cr3");
                log.d("ScreenBacklightControl: WakeLock created");
            }
            if (!isStarted()) {
                log.d("ScreenBacklightControl: user activity while not started");
                release();
                return;
            }

            if (!isHeld()) {
                log.d("ScreenBacklightControl: acquiring WakeLock");
                wl.acquire();
            }

            if (backlightTimerTask == null) {
                log.v("ScreenBacklightControl: timer task started");
                backlightTimerTask = new BacklightTimerTask();
                BackgroundThread.instance().postGUI(backlightTimerTask,
                        screenBacklightDuration / 10);
            }
        }

        public boolean isHeld() {
            return wl != null && wl.isHeld();
        }

        public void release() {
            if (wl != null && wl.isHeld()) {
                log.d("ScreenBacklightControl: wl.release()");
                wl.release();
            }
            backlightTimerTask = null;
            lastUpdateTimeStamp = 0;
        }

        private class BacklightTimerTask implements Runnable {

            @Override
            public void run() {
                if (backlightTimerTask == null)
                    return;
                long interval = Utils.timeInterval(lastUserActivityTime);
//				log.v("ScreenBacklightControl: timer task, lastActivityMillis = "
//						+ interval);
                int nextTimerInterval = screenBacklightDuration / 20;
                boolean dim = false;
                if (interval > screenBacklightDuration * 8 / 10) {
                    nextTimerInterval = nextTimerInterval / 8;
                    dim = true;
                }
                if (interval > screenBacklightDuration) {
                    log.v("ScreenBacklightControl: interval is expired");
                    release();
                } else {
                    BackgroundThread.instance().postGUI(backlightTimerTask, nextTimerInterval);
                    if (dim) {
                        updateBacklightBrightness(-0.9f); // reduce by 9%
                    }
                }
            }

        }

        ;

    }

}
