package vn.seasoft.readerbook;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import it.sephiroth.android.library.widget.AbsHListView;
import it.sephiroth.android.library.widget.AdapterView;
import it.sephiroth.android.library.widget.HListView;
import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.widget.ProgressBar;
import vn.seasoft.readerbook.HttpServices.COMMAND_API;
import vn.seasoft.readerbook.HttpServices.ErrorType;
import vn.seasoft.readerbook.HttpServices.OnHttpServicesListener;
import vn.seasoft.readerbook.HttpServices.ResultObject;
import vn.seasoft.readerbook.RequestObjects.Request_Server;
import vn.seasoft.readerbook.ResultObjects.Result_GetCategory;
import vn.seasoft.readerbook.ResultObjects.Result_GetMostRead;
import vn.seasoft.readerbook.ResultObjects.Result_GetNewest;
import vn.seasoft.readerbook.Util.GlobalData;
import vn.seasoft.readerbook.adapter.AdapterHListViewBook;
import vn.seasoft.readerbook.dialog.dlgConfirm;
import vn.seasoft.readerbook.model.Book;
import vn.seasoft.readerbook.model.Book_Category;
import vn.seasoft.readerbook.widget.ViewError;

/**
 * User: XuanTrung
 * Date: 7/29/2014
 * Time: 2:39 PM
 */
public class fmMain extends Fragment implements OnHttpServicesListener {

    AdapterHListViewBook adapterlvRead;
    AdapterHListViewBook adapterlvNewBook;
    AdapterHListViewBook adapterlvHotBook;
    View footerLoadmore;
    private Context mContext;
    private HListView fmmainLvread;
    private HListView fmmainLvnewbook;
    private HListView fmmainLvhotbook;
    private RelativeLayout fmmainContainerread;
    private RelativeLayout fmmainContainernewbook;
    private RelativeLayout fmmainContainerhotbook;

    Request_Server request_server;

    private void assignViews(View root) {

        fmmainContainerread = (RelativeLayout) root.findViewById(R.id.fmmain_containerread);
        fmmainContainernewbook = (RelativeLayout) root.findViewById(R.id.fmmain_containernewbook);
        fmmainContainerhotbook = (RelativeLayout) root.findViewById(R.id.fmmain_containerhotbook);

        fmmainContainerread.setBackgroundResource(R.drawable.bookshelf_layer_center);
        fmmainContainernewbook.setBackgroundResource(R.drawable.bookshelf_layer_center);
        fmmainContainerhotbook.setBackgroundResource(R.drawable.bookshelf_layer_center);

        addViewContainer(fmmainContainerread, new ProgressBar(mContext));
        addViewContainer(fmmainContainernewbook, new ProgressBar(mContext));
        addViewContainer(fmmainContainerhotbook, new ProgressBar(mContext));

        fmmainLvread = new HListView(mContext);
//        fmmainLvread.setBackgroundResource(R.drawable.bookshelf_layer_center);
        fmmainLvnewbook = new HListView(mContext);
//        fmmainLvnewbook.setBackgroundResource(R.drawable.bookshelf_layer_center);
        fmmainLvhotbook = new HListView(mContext);
//        fmmainLvhotbook.setBackgroundResource(R.drawable.bookshelf_layer_center);
        setListenerListview();

    }

    public void setAutoScrool() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                int listViewSize = fmmainLvhotbook.getAdapter().getCount();


//                for (int index = 0; index < listViewSize; index++) {
//
//                    try {
//                        // it helps scrolling to stay smooth as possible (by experiment)
//                        Thread.sleep(60);
//                    } catch (InterruptedException e) {
//
//                    }
//                    if (index == listViewSize - 1) {
//                        index = 0;
//                        fmmainLvhotbook.setSelection(0);
//                    }
//                }
            }
        }).start();
    }

    void setListenerListview() {
        fmmainLvread.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = adapterlvRead.getItem(position);
//                dlgInfoBook_tab dlg = new dlgInfoBook_tab(mContext, book);
//                dlg.show(getSupportActivity());

                Intent intent = new Intent(getActivity(), actInfoBook.class);
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
        });
        fmmainLvread.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                dlgConfirm dlg = new dlgConfirm(mContext);
                dlg.setMessage("Bạn có chắc muốn xóa sách này khỏi danh sách đã đọc ?");
                dlg.setListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        adapterlvRead.removeBook(position);
                    }
                });
                dlg.show(getSupportActivity());
                return false;
            }
        });
        fmmainLvnewbook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < adapterlvNewBook.getCount()) {
                    Book book = adapterlvNewBook.getItem(position);
//                    dlgInfoBook_tab dlg = new dlgInfoBook_tab(mContext, book);
//                    dlg.show(getSupportActivity());

                    Intent intent = new Intent(getActivity(), actInfoBook.class);
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
        fmmainLvnewbook.setOnScrollListener(new AbsHListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsHListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsHListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;
                if ((lastInScreen == totalItemCount) && adapterlvNewBook.canLoadMoreData()) {
                    request_server.getNewest(adapterlvNewBook.loadMoreData());
                }
            }
        });
        fmmainLvhotbook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < adapterlvHotBook.getCount()) {
                    Book book = adapterlvHotBook.getItem(position);
//                    dlgInfoBook_tab dlg = new dlgInfoBook_tab(mContext, book);
//                    dlg.show(getSupportActivity());

                    Intent intent = new Intent(getActivity(), actInfoBook.class);
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
        fmmainLvhotbook.setOnScrollListener(new AbsHListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsHListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsHListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;
                if ((lastInScreen == totalItemCount) && adapterlvHotBook.canLoadMoreData()) {
                    request_server.getMostBook(adapterlvHotBook.loadMoreData());
                }
            }
        });
    }

    void addViewContainer(RelativeLayout container, View addview) {
        container.removeAllViews();
        RelativeLayout.LayoutParams layoutParams;
        if (addview instanceof HListView) {
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, 1);
        container.addView(addview, layoutParams);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across configuration changes.
        setRetainInstance(true);

        mContext = getActivity();

        request_server = new Request_Server(mContext);
        request_server.SetListener(this);

        adapterlvRead = new AdapterHListViewBook(mContext);
        adapterlvNewBook = new AdapterHListViewBook(mContext);
        adapterlvHotBook = new AdapterHListViewBook(mContext);

        try {
            getSupportActionBar().setSubtitle("Trang chủ");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        System.out.println("onAttach");
    }

    @Override
    public void onResume() {
        super.onResume();
        adapterlvRead.SetListBooks((new Book()).getAllData());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_main, container, false);
        footerLoadmore = inflater.inflate(R.layout.loadmore_hlistview, null, false);

        assignViews(rootView);

        fmmainLvnewbook.addFooterView(footerLoadmore);
        fmmainLvhotbook.addFooterView(footerLoadmore);

        fmmainLvread.setAdapter(adapterlvRead);
        fmmainLvnewbook.setAdapter(adapterlvNewBook);
        fmmainLvhotbook.setAdapter(adapterlvHotBook);

        //Listview read book
        adapterlvRead.SetListBooks((new Book()).getAllData());
        addViewContainer(fmmainContainerread, fmmainLvread);

        //Request server
        request_server.getMostBook(1);
        request_server.getNewest(1);

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapterlvRead = null;
        adapterlvNewBook = null;
        adapterlvHotBook = null;
        fmmainLvread.setAdapter(null);
        fmmainLvnewbook.setAdapter(null);
        fmmainLvhotbook.setAdapter(null);
    }

    @Override
    public void onDataError(int errortype, String urlMethod) {
        GlobalData.DissmissProgress();
        ErrorType.getErrorMessage(mContext, errortype);
        if (urlMethod.equals(COMMAND_API.GET_MOST_READ)) {
            addViewContainer(fmmainContainerhotbook, new ViewError(mContext, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addViewContainer(fmmainContainerhotbook, new ProgressBar(mContext));
                    request_server.getMostBook(adapterlvHotBook.reloadData());

                }
            }));
        }
        if (urlMethod.equals(COMMAND_API.GET_NEWEST)) {
            addViewContainer(fmmainContainernewbook, new ViewError(mContext, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addViewContainer(fmmainContainernewbook, new ProgressBar(mContext));
                    request_server.getNewest(adapterlvNewBook.reloadData());

                }
            }));
        }
    }

    public void scrollRight(final HListView h) {
        fmmainLvhotbook.smoothScrollToPositionFromLeft(adapterlvHotBook.getCount(), 0, 10000);
        new CountDownTimer(1000, 10) {

            public void onTick(long millisUntilFinished) {
                System.out.println("millisUntilFinished :" + millisUntilFinished);
                h.scrollTo((int) (1010 - millisUntilFinished), 0);
            }

            public void onFinish() {

            }
        }.start();
    }

    @Override
    public void onGetData(ResultObject resultData, String urlMethod, int id) {
        if (urlMethod.equals(COMMAND_API.GET_CATEGORY_BOOK)) {
            Result_GetCategory data = (Result_GetCategory) resultData;
            ((MainActivity) getActivity()).removeAllItemSlider();
            for (Book_Category bc : data.lstBookCategory) {
                ((MainActivity) getActivity()).addItemSliderMenu(bc);
            }
        }
        if (urlMethod.equals(COMMAND_API.GET_MOST_READ)) {
            Result_GetMostRead data = (Result_GetMostRead) resultData;
            if (adapterlvHotBook.SetListBooks(data.lstBooks)) {
                addViewContainer(fmmainContainerhotbook, fmmainLvhotbook);
//                fmmainLvhotbook.smoothScrollToPositionFromLeft(adapterlvHotBook.getCount(), 0, 10000);
//                scrollRight(fmmainLvhotbook);
            }
            if (!adapterlvHotBook.isHaveNew()) {
                fmmainLvhotbook.removeFooterView(footerLoadmore);
            }
        }
        if (urlMethod.equals(COMMAND_API.GET_NEWEST)) {
            Result_GetNewest data = (Result_GetNewest) resultData;
            if (adapterlvNewBook.SetListBooks(data.lstBooks)) {
                addViewContainer(fmmainContainernewbook, fmmainLvnewbook);
//                fmmainLvnewbook.smoothScrollToPositionFromLeft(adapterlvNewBook.getCount(), 0, 10000);
//                scrollRight(fmmainLvnewbook);
            }
            if (!adapterlvNewBook.isHaveNew()) {
                fmmainLvnewbook.removeFooterView(footerLoadmore);
            }
        }
        GlobalData.DissmissProgress();
    }
}
