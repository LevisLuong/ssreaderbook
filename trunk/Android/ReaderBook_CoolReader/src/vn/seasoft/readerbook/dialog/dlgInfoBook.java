package vn.seasoft.readerbook.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import org.coolreader.CoolReader;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.app.DialogFragment;
import vn.seasoft.readerbook.HttpServices.COMMAND_API;
import vn.seasoft.readerbook.HttpServices.ErrorType;
import vn.seasoft.readerbook.HttpServices.OnHttpServicesListener;
import vn.seasoft.readerbook.HttpServices.ResultObject;
import vn.seasoft.readerbook.R;
import vn.seasoft.readerbook.ResultObjects.Result_GetBookChapter;
import vn.seasoft.readerbook.SSReaderApplication;
import vn.seasoft.readerbook.Util.AsyntaskDownloadFile;
import vn.seasoft.readerbook.Util.GlobalData;
import vn.seasoft.readerbook.actReadPictureBook;
import vn.seasoft.readerbook.adapter.BookChapterAdapter;
import vn.seasoft.readerbook.model.Book;
import vn.seasoft.readerbook.model.Book_Category;
import vn.seasoft.readerbook.model.Book_Chapter;
import vn.seasoft.readerbook.widget.SwipeDismissList;
import vn.seasoft.readerbook.widget.ViewError;

import java.util.List;

/**
 * User: XuanTrung
 * Date: 7/2/2014
 * Time: 10:59 AM
 */
public class dlgInfoBook extends DialogFragment implements OnHttpServicesListener {
    Context mContext;
    Book book;
    BookChapterAdapter adapter;
    View footerLoadmore;

    public dlgInfoBook(Context _context, Book _book) {
        setDialogType(DialogType.AlertDialog);
        mContext = _context;
        book = _book;
    }

    private ImageView dlginfoImgcover;
    private TextView dlginfoTxtTitle;
    private TextView dlginfoTxtAuthor;
    private TextView dlginfoTxtCategory;
    private TextView dlginfoTxtSummary;
    private LinearLayout dlginfoChapter;
    private ListView dlginfoListview;
    private RelativeLayout dlginfoContainer;

    private void assignViews(View root) {
        dlginfoImgcover = (ImageView) root.findViewById(R.id.dlginfo_imgcover);
        dlginfoTxtTitle = (TextView) root.findViewById(R.id.dlginfo_txtTitle);
        dlginfoTxtAuthor = (TextView) root.findViewById(R.id.dlginfo_txtAuthor);
        dlginfoTxtCategory = (TextView) root.findViewById(R.id.dlginfo_txtCategory);
        dlginfoTxtSummary = (TextView) root.findViewById(R.id.dlginfo_txtSummary);
        dlginfoTxtSummary.setMovementMethod(new ScrollingMovementMethod());
        dlginfoChapter = (LinearLayout) root.findViewById(R.id.dlginfo_chapter);
        dlginfoContainer = (RelativeLayout) root.findViewById(R.id.dlginfo_container);
        dlginfoListview = new ListView(mContext);
        adapter = new BookChapterAdapter(mContext);
        SwipeDismissList swiplist = new SwipeDismissList(dlginfoListview, new SwipeDismissList.OnDismissCallback() {
            @Override
            public SwipeDismissList.Undoable onDismiss(AbsListView listView, int position) {
                adapter.deleteBook(position);
                return null;
            }
        }, SwipeDismissList.UndoMode.SINGLE_UNDO);
    }

    void addViewContainer(RelativeLayout container, View addview) {
        container.removeAllViews();
        container.addView(addview);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        addview.setLayoutParams(layoutParams);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.book_info_dialog, container, false);
        footerLoadmore = inflater.inflate(R.layout.loadmore_layout, null, false);
        assignViews(v);
        UrlImageViewHelper.setUrlDrawable(dlginfoImgcover, GlobalData.getUrlImageCover(book));
        dlginfoTxtTitle.setText(book.getTitle());
        dlginfoTxtAuthor.setText(book.getAuthor());
        dlginfoTxtSummary.setText(book.getSummary());
        String book_category = (new Book_Category()).getByID(book.getIdcategory()).getCategory();
        dlginfoTxtCategory.setText(book_category);

        addViewContainer(dlginfoContainer, new ProgressBar(mContext));

        dlginfoListview.addFooterView(footerLoadmore);
        dlginfoListview.setAdapter(adapter);
        dlginfoListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;
                if ((lastInScreen == totalItemCount) && adapter.canLoadMoreData()) {
                    System.out.println("Load more books");
                    SSReaderApplication.getRequestServer(mContext).getBookChapter(book.getIdbook(), adapter.loadMoreData());
                }
            }
        });
        dlginfoListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                if (i < adapter.getCount()) {
                    if (book.getIdcategory() == 8) {
                        book.addNewData();
                        adapter.setRead(i);
                        adapter.setDownloaded(i);
                        adapter.notifyDataSetChanged();
                        Intent t = new Intent(getActivity(), actReadPictureBook.class);
                        t.putExtra("arrbook", adapter.getItem(i).getFilename());
                        t.putExtra("idbook", adapter.getItem(i).getIdbook());
                        t.putExtra("idbookchapter", adapter.getItem(i).getIdbook_chapter());
                        mContext.startActivity(t);
                    } else {
                        AsyntaskDownloadFile download = new AsyntaskDownloadFile(mContext, GlobalData.getUrlBook(adapter.getItem(i)));
                        download.setListenerDownload(new AsyntaskDownloadFile.IDownLoadMood() {
                            @Override
                            public void onDownloadComplete(String urlResultMood) {
                                book.addNewData();
                                adapter.setRead(i);
                                adapter.setDownloaded(i);
                                adapter.notifyDataSetChanged();
                                Intent t = new Intent(mContext, CoolReader.class);
                                t.putExtra(CoolReader.OPEN_FILE_PARAM, urlResultMood);
                                mContext.startActivity(t);
                            }

                            @Override
                            public void onCanceled() {

                            }
                        });
                        download.startDownload();
                    }
                }
            }
        });
        //load from database
        List<Book_Chapter> lstBookDatabase = (new Book_Chapter()).getByidBook(book.getIdbook());
        if (!lstBookDatabase.isEmpty()) {
            adapter.setFromDataBase(lstBookDatabase);
            addViewContainer(dlginfoContainer, dlginfoListview);
        }
        //update from server
        SSReaderApplication.getRequestServer(mContext, this).getBookChapter(book.getIdbook(), adapter.loadMoreData());
        return v;
    }

    Book_Chapter curBookRead;

    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getSupportActivity());
        builder.setNegativeButton("Bỏ qua", null);
        builder.setPositiveButton("Đọc Tiếp", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                Intent t = new Intent(getActivity(), actReadPictureBook.class);
//                File f = new File(Environment.getExternalStorageDirectory().getPath() + "/Download/dragonball.zip");
//                t.putExtra("file", f.getAbsolutePath());
//                startActivity(t);
                if (!adapter.getList().isEmpty()) {
                    curBookRead = adapter.getItem(0);
                    for (Book_Chapter bookchap : adapter.getList()) {
                        if (bookchap.getCurrentread()) {
                            curBookRead = bookchap;
                            break;
                        }
                    }
                    if (book.getIdcategory() == 8) {
                        book.addNewData();
                        curBookRead.setCurrentread(true);
                        curBookRead.setIsDownloaded(true);
                        curBookRead.updateData();
                        Intent t = new Intent(getActivity(), actReadPictureBook.class);
                        t.putExtra("arrbook", curBookRead.getFilename());
                        t.putExtra("idbook", curBookRead.getIdbook());
                        t.putExtra("idbookchapter", curBookRead.getIdbook_chapter());
                        mContext.startActivity(t);
                    } else {
                        AsyntaskDownloadFile download = new AsyntaskDownloadFile(mContext, GlobalData.getUrlBook(curBookRead));
                        download.setListenerDownload(new AsyntaskDownloadFile.IDownLoadMood() {
                            @Override
                            public void onDownloadComplete(String urlResultMood) {
                                book.addNewData();
                                curBookRead.setCurrentread(true);
                                curBookRead.setIsDownloaded(true);
                                curBookRead.updateData();
                                Intent t = new Intent(mContext, CoolReader.class);
                                t.putExtra(CoolReader.OPEN_FILE_PARAM, urlResultMood);
                                mContext.startActivity(t);
                            }

                            @Override
                            public void onCanceled() {

                            }
                        });
                        download.startDownload();
                    }
                }
            }
        });
        return builder.create();
    }

    @Override
    public void onDataError(int errortype, String urlMethod) {
        GlobalData.DissmissProgress();
        ErrorType.getErrorMessage(mContext, errortype);
        addViewContainer(dlginfoContainer, new ViewError(mContext, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addViewContainer(dlginfoContainer, new ProgressBar(mContext));
                SSReaderApplication.getRequestServer(mContext).getBookChapter(book.getIdbook(), adapter.reloadData());
            }
        }));

        //load from database
        List<Book_Chapter> lstBookDatabase = (new Book_Chapter()).getByidBook(book.getIdbook());
        if (!lstBookDatabase.isEmpty()) {
            adapter.setFromDataBase(lstBookDatabase);
            addViewContainer(dlginfoContainer, dlginfoListview);
            dlginfoListview.removeFooterView(footerLoadmore);
        }
    }

    @Override
    public void onGetData(ResultObject resultData, String urlMethod, int id) {
        GlobalData.DissmissProgress();
        if (urlMethod.equals(COMMAND_API.GET_BOOK_CHAPTER)) {
            Result_GetBookChapter data = (Result_GetBookChapter) resultData;
            if (adapter.SetListBooks(data.lstBookChaps)) {
                addViewContainer(dlginfoContainer, dlginfoListview);
            }

            if (!adapter.isHaveNew()) {
                dlginfoListview.removeFooterView(footerLoadmore);
            }
        }
    }
}