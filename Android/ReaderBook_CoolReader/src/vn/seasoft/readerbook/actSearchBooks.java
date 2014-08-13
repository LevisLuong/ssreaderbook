package vn.seasoft.readerbook;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.ListView;
import vn.seasoft.readerbook.HttpServices.COMMAND_API;
import vn.seasoft.readerbook.HttpServices.ErrorType;
import vn.seasoft.readerbook.HttpServices.OnHttpServicesListener;
import vn.seasoft.readerbook.HttpServices.ResultObject;
import vn.seasoft.readerbook.ResultObjects.Result_GetSearchBook;
import vn.seasoft.readerbook.Util.GlobalData;
import vn.seasoft.readerbook.adapter.SearchBookAdapter;
import vn.seasoft.readerbook.dialog.dlgInfoBook;
import vn.seasoft.readerbook.model.Book;

import java.util.List;

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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_book);
        mContext = this;
        // get the action bar
        ActionBar actionBar = getSupportActionBar();
        // Enabling Back navigation on Action Bar icon
        actionBar.setDisplayHomeAsUpEnabled(true);

        listview = (ListView) findViewById(R.id.listview);
        adapter = new SearchBookAdapter(mContext);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dlgInfoBook dlg = new dlgInfoBook(mContext, adapter.getItem(i));
                dlg.show((Activity) mContext);
            }
        });
        handleIntent(getIntent());
    }


    private void handleIntent(Intent intent) {
        String query =
                intent.getStringExtra(SearchManager.QUERY);
        Log.i("Search book", "String query: " + query);

        List<Book> lstSearchDatabase = (new Book()).getSearchBook(query);
        adapter.addData(lstSearchDatabase);

        GlobalData.ShowProgressDialog(mContext, R.string.loading);
        SSReaderApplication.getRequestServer(this, this).SearchBook(query, index);
    }

    @Override
    public void onDataError(int errortype, String urlMethod) {
        GlobalData.DissmissProgress();
        ErrorType.getErrorMessage(mContext, errortype);
    }

    @Override
    public void onGetData(ResultObject resultData, String urlMethod, int id) {
        GlobalData.DissmissProgress();
        if (urlMethod.equals(COMMAND_API.SEARCH_BOOK)) {
            Result_GetSearchBook data = (Result_GetSearchBook) resultData;
            adapter.addData(data.lstBooks);
        }
    }
}
