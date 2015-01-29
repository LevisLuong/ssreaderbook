package vn.seasoft.sachcuatui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;
import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.Toast;
import vn.seasoft.sachcuatui.HttpServices.COMMAND_API;
import vn.seasoft.sachcuatui.HttpServices.ErrorType;
import vn.seasoft.sachcuatui.HttpServices.OnHttpServicesListener;
import vn.seasoft.sachcuatui.HttpServices.ResultObject;
import vn.seasoft.sachcuatui.ResultObjects.Result_GetSearchBook;
import vn.seasoft.sachcuatui.Util.GlobalData;
import vn.seasoft.sachcuatui.adapter.SearchBookAdapter;
import vn.seasoft.sachcuatui.model.Book;

/**
 * User: XuanTrung
 * Date: 7/10/2014
 * Time: 10:06 AM
 */
public class actSearchBooks extends Activity implements OnHttpServicesListener {
    Context mContext;
    ListView listview;
    SearchBookAdapter adapter;
    int index = 1;
    String query;

    View footerLoadmore;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_book);
        mContext = this;
        handleIntent(getIntent());
        // get the action bar
        ActionBar actionBar = getSupportActionBar();
        // Enabling Back navigation on Action Bar icon
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setSubtitle("Tìm với từ khóa: " + query);

        footerLoadmore = getLayoutInflater().inflate(R.layout.loadmore_layout, null, false);
        listview = (ListView) findViewById(R.id.listview);
        listview.addFooterView(footerLoadmore);
        adapter = new SearchBookAdapter(mContext, query);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i < adapter.getCount()) {
                    Book book = adapter.getItem(i);

                    Intent intent = new Intent(actSearchBooks.this, actInfoBook.class);
                    intent.putExtra("idbook", book.getIdbook());
                    intent.putExtra("titlebook", book.getTitle());
                    intent.putExtra("authorbook", book.getAuthor());
                    intent.putExtra("idcategory", book.getIdcategory());
                    intent.putExtra("countdownload", book.getCountdownload());
                    intent.putExtra("countview", book.getCountview());
                    intent.putExtra("summary", book.getSummary());
                    intent.putExtra("cover", book.getImagecover());
                    startActivity(intent);
                }
            }
        });
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;
                if ((lastInScreen == totalItemCount) && adapter.canLoadMoreData()) {
                    SSReaderApplication.getRequestServer(mContext, (OnHttpServicesListener) mContext).SearchBook(query, adapter.loadMoreData());
                }
            }
        });
        GlobalData.ShowProgressDialog(mContext, R.string.loading);
        SSReaderApplication.getRequestServer(this, this).SearchBook(query, index);
    }


    private void handleIntent(Intent intent) {
        query =
                intent.getStringExtra(SearchManager.QUERY);
        Log.i("Search book", "String query: " + query);
    }

    @Override
    public void onDataError(int errortype, String urlMethod) {
        GlobalData.DissmissProgress();
        ErrorType.getErrorMessage(mContext, errortype);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter = null;
        listview.setAdapter(null);
    }

    @Override
    public void onGetData(final ResultObject resultData, final String urlMethod, int id) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (urlMethod.equals(COMMAND_API.SEARCH_BOOK)) {
                    Result_GetSearchBook data = (Result_GetSearchBook) resultData;
                    adapter.SetListBooks(data.lstBooks);
                    if (!adapter.isHaveNew()) {
                        listview.removeFooterView(footerLoadmore);
                    }

                    adapter.notifyDataSetChanged();
                    listview.requestLayout();

                    if (adapter.getCount() == 0) {
                        Toast.makeText(mContext, "Không tìm thấy kết quả nào !", Toast.LENGTH_LONG).show();
                        ((TextView) findViewById(R.id.searchbook_error)).setVisibility(View.VISIBLE);

                    }
                }
            }
        });
        GlobalData.DissmissProgress();
    }
}
