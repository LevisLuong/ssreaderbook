package vn.seasoft.readerbook;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import org.holoeverywhere.addon.AddonSlider;
import org.holoeverywhere.addon.Addons;
import org.holoeverywhere.app.Activity;
import org.holoeverywhere.slider.SliderMenu;
import urlimageviewhelper.UrlImageViewHelper;
import vn.seasoft.readerbook.Util.SSUtil;
import vn.seasoft.readerbook.Util.mSharedPreferences;
import vn.seasoft.readerbook.dialog.dlgAboutApp;
import vn.seasoft.readerbook.dialog.dlgConfirm;
import vn.seasoft.readerbook.dialog.dlgFeedback;
import vn.seasoft.readerbook.listener.ILoginFacebook;
import vn.seasoft.readerbook.model.Book_Category;

import java.util.List;

@Addons(AddonSlider.class)
public class MainActivity extends Activity {
    Context context;
    SliderMenu sliderMenu;
    AddonSlider.AddonSliderA addonSlider;


    public AddonSlider.AddonSliderA addonSlider() {
        return addon(AddonSlider.class);
    }

    private ImageView sliderMenuAvatar;
    private TextView sliderMenuUsername;
    private Button sliderMenuBtnlogin;

    private void assignViews(View v) {
        sliderMenuAvatar = (ImageView) v.findViewById(R.id.slider_menu_avatar);
        sliderMenuUsername = (TextView) v.findViewById(R.id.slider_menu_username);
        sliderMenuBtnlogin = (Button) v.findViewById(R.id.slider_menu_btnlogin);
    }

    void initSliderMenu() {
        sliderMenu = addonSlider.obtainDefaultSliderMenu(R.layout.slider_menu_main);
        sliderMenu.add("Trang chủ", fmMain.class, SliderMenu.BLUE).setTag("home");
        List<Book_Category> lst = (new Book_Category()).getAllData();
        if (!lst.isEmpty()) {
            for (Book_Category bc : lst) {
                addItemSliderMenu(bc);
            }
        }
        assignViews(addonSlider.getLeftView());
        sliderMenuBtnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int iduser = mSharedPreferences.getUserID(context);
                if (iduser == 0) {
                    SSReaderApplication.authorizeFB(context, new ILoginFacebook() {
                        @Override
                        public void LoginSuccess() {
                            sliderMenuAvatar.setVisibility(View.VISIBLE);
                            sliderMenuUsername.setVisibility(View.VISIBLE);
                            UrlImageViewHelper.setUrlDrawable(sliderMenuAvatar, SSUtil.getAvatarFacebookById(mSharedPreferences.getUserIDFacebook(context)));
                            sliderMenuUsername.setText(mSharedPreferences.getUserDisplay(context));
                            sliderMenuBtnlogin.setText("Đăng xuất");
                        }
                    });
                } else {
                    dlgConfirm dlg = new dlgConfirm(context);
                    dlg.setMessage("Bạn có chắc muốn đăng xuất ?");
                    dlg.setListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            SSReaderApplication.signOutFB(context, new ILoginFacebook() {
                                @Override
                                public void LoginSuccess() {
                                    //refresh state UI
                                    sliderMenuAvatar.setVisibility(View.GONE);
                                    sliderMenuUsername.setVisibility(View.GONE);
                                    sliderMenuBtnlogin.setText("Đăng nhập");
                                }
                            });
                        }
                    });
                    dlg.show(getSupportFragmentManager());
                }
            }
        });
    }

    void checkLogin() {
        int iduser = mSharedPreferences.getUserID(this);
        if (iduser == 0) {
            sliderMenuAvatar.setVisibility(View.GONE);
            sliderMenuUsername.setVisibility(View.GONE);
            sliderMenuBtnlogin.setText("Đăng nhập");
        } else {
            sliderMenuAvatar.setVisibility(View.VISIBLE);
            sliderMenuUsername.setVisibility(View.VISIBLE);
            UrlImageViewHelper.setUrlDrawable(sliderMenuAvatar, SSUtil.getAvatarFacebookById(mSharedPreferences.getUserIDFacebook(this)));
            sliderMenuUsername.setText(mSharedPreferences.getUserDisplay(this));
            sliderMenuBtnlogin.setText("Đăng xuất");
        }
    }

    @Override
    protected void onPostCreate(Bundle sSavedInstanceState) {
        super.onPostCreate(sSavedInstanceState);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addonSlider = addonSlider();
        addonSlider.onCreate(savedInstanceState);
        context = this;
        getSupportActionBar().setTitle(R.string.app_name);

        initSliderMenu();

        SSReaderApplication.getRequestServer(this).getCategoryBook();
        //Google analytics
        //add tracker google
        // Get tracker.
        Tracker t = ((SSReaderApplication) getApplication()).getTracker(
                SSReaderApplication.TrackerName.APP_TRACKER);
        // Set screen name.
        // Where path is a String representing the screen name.
        t.setScreenName("Trang chủ");
        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        // searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                MenuItemCompat.collapseActionView(searchItem);
                Intent t = new Intent(MainActivity.this, actSearchBooks.class);
                t.putExtra(SearchManager.QUERY, query);
                startActivity(t);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_feedback:
                dlgFeedback dlg = new dlgFeedback(context);
                dlg.show(getSupportFragmentManager());
                break;
            case R.id.action_tutorial:
                Intent browserIntent = new Intent(MainActivity.this, actTutorial.class);
                startActivity(browserIntent);
                break;
            case R.id.action_about:
                dlgAboutApp dlgAbout = new dlgAboutApp(context);
                dlgAbout.show();
                break;
            case R.id.action_exit:
                exitApp();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public synchronized void removeAllItemSlider() {
//        for (int i = 1; i < sliderMenu.getSize(); i++) {
//            sliderMenu.remove(i);
//        }
        while (sliderMenu.getSize() > 1) {
            sliderMenu.remove(1);
        }
    }

    public synchronized void addItemSliderMenu(Book_Category bc) {
        Bundle bundle = new Bundle();
        bundle.putInt("idbookcategory", bc.getIdcategory());
        sliderMenu.add(bc.getCategory(), fmListBook.class, bundle, SliderMenu.BLUE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        addonSlider().onResume();
        checkLogin();
//        SSReaderApplication.getRequestServer(this).setUserOnline();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        addonSlider().onDestroy();
//        SSReaderApplication.getRequestServer(this).setUserOffline();
    }

    @Override
    protected void onPause() {
        super.onPause();
        addonSlider().onPause();
    }

    public void exitApp() {
        dlgConfirm dlg = new dlgConfirm(context);
        dlg.setListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        dlg.setMessage(R.string.notice_exitapp);
        dlg.show(getSupportFragmentManager());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitApp();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
