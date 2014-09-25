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
import org.holoeverywhere.addon.AddonSlider;
import org.holoeverywhere.addon.Addons;
import org.holoeverywhere.app.Activity;
import org.holoeverywhere.slider.SliderMenu;
import vn.seasoft.readerbook.Util.GlobalData;
import vn.seasoft.readerbook.dialog.dlgAboutApp;
import vn.seasoft.readerbook.dialog.dlgConfirm;
import vn.seasoft.readerbook.dialog.dlgFeedback;
import vn.seasoft.readerbook.model.Book_Category;

import java.util.List;

@Addons(AddonSlider.class)
public class MainActivity extends Activity {
    Context context;
    SliderMenu sliderMenu;

    public AddonSlider.AddonSliderA addonSlider() {
        return addon(AddonSlider.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addonSlider().onCreate(savedInstanceState);
        context = this;
        getSupportActionBar().setTitle(R.string.app_name);
        sliderMenu = addonSlider().obtainDefaultSliderMenu(R.layout.slider_default_list_layout);
        sliderMenu.add("Trang chủ", fmMain.class, SliderMenu.BLUE).setTag("home");
        List<Book_Category> lst = (new Book_Category()).getAllData();
        if (!lst.isEmpty()) {
            for (Book_Category bc : lst) {
                addItemSliderMenu(bc);
            }
        }
        GlobalData.ShowProgressDialog(this, R.string.loading);
        SSReaderApplication.getRequestServer(this).getCategoryBook();
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
//            case R.id.action_tutorial:
//                Toast.makeText(getBaseContext(), "Hướng dẫn sử dụng !", Toast.LENGTH_SHORT).show();
//                break;
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
        SSReaderApplication.getRequestServer(this).setUserOnline();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        addonSlider().onDestroy();
        SSReaderApplication.getRequestServer(this).setUserOffline();
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
