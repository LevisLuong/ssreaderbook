package vn.seasoft.readerbook;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import org.holoeverywhere.addon.AddonSlider;
import org.holoeverywhere.addon.Addons;
import org.holoeverywhere.app.Activity;
import org.holoeverywhere.slider.SliderMenu;
import vn.seasoft.readerbook.Util.GlobalData;
import vn.seasoft.readerbook.model.Book_Category;

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
        context = this;
        getSupportActionBar().setTitle(R.string.app_name);

        sliderMenu = addonSlider().obtainDefaultSliderMenu(R.layout.slider_default_list_layout);
        sliderMenu.add("Trang chủ", fmMain.class, SliderMenu.BLUE).setTag("home");

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
                Log.i("Search:", query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    public void removeAllItemSlider() {
        sliderMenu.removeAll();
    }

    public void addItemSliderMenu(Book_Category bc) {
        Bundle bundle = new Bundle();
        bundle.putInt("idbookcategory", bc.getIdcategory());
        sliderMenu.add(bc.getCategory(), fmListBook.class, bundle, SliderMenu.BLUE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SSReaderApplication.getRequestServer(this).setUserOnline();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SSReaderApplication.getRequestServer(this).setUserOffline();
    }
}
