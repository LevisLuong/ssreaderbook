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
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.widget.WebDialog;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthError;
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
import vn.seasoft.readerbook.Util.SSUtil;
import vn.seasoft.readerbook.Util.asynPostFacebook;
import vn.seasoft.readerbook.actReadPictureBook;
import vn.seasoft.readerbook.adapter.BookChapterAdapter;
import vn.seasoft.readerbook.model.Book;
import vn.seasoft.readerbook.model.Book_Category;
import vn.seasoft.readerbook.model.Book_Chapter;
import vn.seasoft.readerbook.widget.SwipeDismissList;
import vn.seasoft.readerbook.widget.ViewError;

import java.util.Collections;
import java.util.Comparator;
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

    public dlgInfoBook() {
    }

    public dlgInfoBook(Context _context, Book _book) {
        setDialogType(DialogType.AlertDialog);
        mContext = _context;
        book = _book;
    }


    private ImageButton dlginfoSortChapter;
    private LinearLayout dlginfoSharefb;
    private TextView dlginfoSelectchapters;
    private TextView dlginfoTxtchapters;
    private TextView dlginfoTxtview;
    private TextView dlginfoTxtdownloads;
    private ImageView dlginfoImgcover;
    private TextView dlginfoTxtTitle;
    private TextView dlginfoTxtAuthor;
    private TextView dlginfoTxtCategory;
    private TextView dlginfoTxtSummary;
    private ListView dlginfoListview;
    private RelativeLayout dlginfoContainer;

    private void assignViews(View root) {

        dlginfoSelectchapters = (TextView) root.findViewById(R.id.dlginfo_selectchapter);
        dlginfoSortChapter = (ImageButton) root.findViewById(R.id.dlginfo_sortchapter);
        dlginfoTxtchapters = (TextView) root.findViewById(R.id.dlginfo_txtchapters);
        dlginfoSharefb = (LinearLayout) root.findViewById(R.id.dlginfo_sharefb);
        dlginfoContainer = (RelativeLayout) root.findViewById(R.id.dlginfo_container);
        dlginfoTxtview = (TextView) root.findViewById(R.id.dlginfo_txtview);
        dlginfoTxtdownloads = (TextView) root.findViewById(R.id.dlginfo_txtdownloads);
        dlginfoImgcover = (ImageView) root.findViewById(R.id.dlginfo_imgcover);
        dlginfoTxtTitle = (TextView) root.findViewById(R.id.dlginfo_txtTitle);
        dlginfoTxtAuthor = (TextView) root.findViewById(R.id.dlginfo_txtAuthor);
        dlginfoTxtCategory = (TextView) root.findViewById(R.id.dlginfo_txtCategory);
        dlginfoTxtSummary = (TextView) root.findViewById(R.id.dlginfo_txtSummary);
        dlginfoTxtTitle.setMovementMethod(new ScrollingMovementMethod());
        dlginfoTxtAuthor.setMovementMethod(new ScrollingMovementMethod());
        dlginfoTxtSummary.setMovementMethod(new ScrollingMovementMethod());
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        SSReaderApplication.getSocialAdapter().setListener(new DialogListener() {
            @Override
            public void onComplete(Bundle bundle) {
                asynPostFacebook asyn = new asynPostFacebook(mContext, book.getTitle(), book.getAuthor(), UrlImageViewHelper.getCachedBitmap(GlobalData.getUrlImageCover(book)));
                asyn.execute();
            }

            @Override
            public void onError(SocialAuthError socialAuthError) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onBack() {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        List<Book_Chapter> lstBookDatabase = (new Book_Chapter()).getByidBook(book.getIdbook());
        if (!lstBookDatabase.isEmpty()) {
            adapter.setFromDataBase(lstBookDatabase);

            int position = 0;
            for (Book_Chapter bookchap : adapter.getList()) {
                if (bookchap.getCurrentread()) {
                    break;
                }
                position++;
            }
            dlginfoListview.setSelection(position);

            addViewContainer(dlginfoContainer, dlginfoListview);
        }

        SSUtil.App_Log("dlginfobook", "app resume notify listview");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(getSupportActivity(), requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            // Respond to session state changes, ex: updating the view
            if (state.isOpened()) {
                SSUtil.App_Log("Post facebook !!!!!!!!", "Post facebook !!!!!!!!");
                publishFeedDialog();
            } else if (state.isClosed()) {
                SSUtil.App_Log("not login", "not login !!!!!!!!");
                publishFeedDialog();
            }
        }
    }

    private Session.StatusCallback statusCallback =
            new SessionStatusCallback();

    boolean asc = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_book_info, container, false);
        footerLoadmore = inflater.inflate(R.layout.loadmore_layout, null, false);
        assignViews(v);
        UrlImageViewHelper.setUrlDrawable(dlginfoImgcover, GlobalData.getUrlImageCover(book));
        dlginfoTxtTitle.setText(book.getTitle());
        dlginfoTxtAuthor.setText(book.getAuthor());
        dlginfoTxtSummary.setText(book.getSummary());
        String book_category = (new Book_Category()).getByID(book.getIdcategory()).getCategory();
        dlginfoTxtCategory.setText(book_category);
        dlginfoTxtview.setText(book.getCountview() + "");
        dlginfoTxtdownloads.setText(book.getCountdownload() + "");

        dlginfoSelectchapters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!adapter.getList().isEmpty()) {
                    dlgEditText dlg = new dlgEditText(mContext);
                    dlg.setListener(new dlgEditText.IDialogEditText() {
                        @Override
                        public void getValue(String value) {
                            try {
                                int page = Integer.parseInt(value.trim());
                                if (page > 0 && page <= adapter.getCount()) {
                                    page = page - 1;
                                    loadBook(page);
                                } else {
                                    org.holoeverywhere.widget.Toast.makeText(mContext, "Không có Chapter này !", Toast.LENGTH_SHORT).show();
                                }
                            } catch (NumberFormatException e) {
                                org.holoeverywhere.widget.Toast.makeText(mContext, "Không có Chapter này !", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    dlg.show(getSupportActivity());
                }
            }
        });
        dlginfoSortChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!adapter.getList().isEmpty()) {
                    if (asc) {
                        Collections.sort(adapter.getList(), new Comparator<Book_Chapter>() {
                            @Override
                            public int compare(Book_Chapter book_chapter, Book_Chapter t1) {
                                return (book_chapter.getIdbook_chapter() > t1.getIdbook_chapter()) ? -1 : (book_chapter.getIdbook_chapter() > t1.getIdbook_chapter()) ? 1 : 0;
                            }
                        });
                        asc = false;
                    } else {
                        Collections.sort(adapter.getList(), new Comparator<Book_Chapter>() {
                            @Override
                            public int compare(Book_Chapter book_chapter, Book_Chapter t1) {
                                return (book_chapter.getIdbook_chapter() < t1.getIdbook_chapter()) ? -1 : (book_chapter.getIdbook_chapter() > t1.getIdbook_chapter()) ? 1 : 0;
                            }
                        });
                        asc = true;
                    }

                    adapter.notifyDataSetChanged();
                }
            }
        });
        dlginfoSharefb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                asynPostFacebook task = new asynPostFacebook(mContext, book.getTitle(), book.getAuthor(), UrlImageViewHelper.getCachedBitmap(GlobalData.getUrlImageCover(book)));
//                task.execute();
                dlgConfirm dlg = new dlgConfirm(mContext);
                dlg.setListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // start Facebook Login
//                        Session session = Session.getActiveSession();
//                        if (session != null && !session.isOpened() && !session.isClosed()) {
//                            session.openForRead(new Session.OpenRequest(getSupportActivity())
//                                    .setPermissions(Arrays.asList("publish_actions"))
//                                    .setCallback(statusCallback));
//                        } else {
//                            Session.openActiveSession(getSupportActivity(), true, statusCallback);
//                        }
//                        uiHelper.onResume();

                        SSReaderApplication.getSocialAdapter().authorize(mContext, SocialAuthAdapter.Provider.FACEBOOK);
                    }
                });
                dlg.setMessage("Bạn có chắc muốn chia sẻ lên Facebook?");
                dlg.show(getSupportActivity());
            }
        });
        addViewContainer(dlginfoContainer, new ProgressBar(mContext));
        dlginfoListview.addFooterView(footerLoadmore);
        dlginfoListview.setAdapter(adapter);
        dlginfoListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                if (i < adapter.getCount()) {
                    loadBook(i);
                }
            }
        });

        dlginfoTxtchapters.setText(adapter.getCount() + "");
        //update from server
        SSReaderApplication.getRequestServer(mContext, this).getBookChapter(book.getIdbook(), 0);
        return v;
    }

    private void loadBook(final int position) {
        final Book_Chapter book_chapter = adapter.getItem(position);
        if (book.getIdcategory() == 8) {
            book.addNewData();
            adapter.setRead(position);
            adapter.setDownloaded(position);
            Intent t = new Intent(getActivity(), actReadPictureBook.class);
            t.putExtra("arrbook", book_chapter.getFilename());
            t.putExtra("idbook", book_chapter.getIdbook());
            t.putExtra("idbookchapter", book_chapter.getIdbook_chapter());
            t.putExtra("position", book_chapter.getReadposition());
            mContext.startActivity(t);
        } else {
            AsyntaskDownloadFile download = new AsyntaskDownloadFile(mContext, GlobalData.getUrlBook(book_chapter));
            download.setListenerDownload(new AsyntaskDownloadFile.IDownLoadMood() {
                @Override
                public void onDownloadComplete(String urlResultMood) {
                    book.addNewData();
                    adapter.setRead(position);
                    adapter.setDownloaded(position);
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
        SSReaderApplication.getRequestServer(mContext).addCountBook(book.getIdbook());
    }

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
                    int position;
                    position = 0;
                    for (int y = 0; y < adapter.getCount(); y++) {
                        Book_Chapter book_chapter = adapter.getItem(y);
                        if (book_chapter.getCurrentread()) {
                            position = y;
                            break;
                        }
                    }
                    loadBook(position);
                }
            }
        });
        return builder.create();
    }

    private void publishFeedDialog() {
        Bundle params = new Bundle();
        params.putString("name", "Facebook SDK for Android");
        params.putString("caption", "Build great social apps and get more installs.");
        params.putString("description", "The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps.");
        params.putString("link", "https://developers.facebook.com/android");
        params.putString("picture", "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");

        WebDialog feedDialog = (
                new WebDialog.FeedDialogBuilder(getActivity(),
                        Session.getActiveSession(),
                        params))
                .setOnCompleteListener(new WebDialog.OnCompleteListener() {

                    @Override
                    public void onComplete(Bundle values,
                                           FacebookException error) {
                        if (error == null) {
                            // When the story is posted, echo the success
                            // and the post Id.
                            final String postId = values.getString("post_id");
                            if (postId != null) {
                                Toast.makeText(getActivity(),
                                        "Posted story, id: " + postId,
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                // User clicked the Cancel button
                                Toast.makeText(getActivity().getApplicationContext(),
                                        "Publish cancelled",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else if (error instanceof FacebookOperationCanceledException) {
                            // User clicked the "x" button
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Publish cancelled",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Generic, ex: network error
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Error posting story",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                })
                .build();
        feedDialog.show();
    }

    @Override
    public void onDataError(int errortype, String urlMethod) {
        GlobalData.DissmissProgress();
        ErrorType.getErrorMessage(mContext, errortype);
        if (adapter.getList().isEmpty()) {
            addViewContainer(dlginfoContainer, new ViewError(mContext, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addViewContainer(dlginfoContainer, new ProgressBar(mContext));
                    SSReaderApplication.getRequestServer(mContext).getBookChapter(book.getIdbook(), 0);
                }
            }));
        }
    }

    @Override
    public void onGetData(ResultObject resultData, String urlMethod, int id) {
        if (urlMethod.equals(COMMAND_API.GET_BOOK_CHAPTER)) {
            Result_GetBookChapter data = (Result_GetBookChapter) resultData;

            if (adapter.SetListBooks(data.lstBookChaps)) {
                addViewContainer(dlginfoContainer, dlginfoListview);
            }
            dlginfoListview.removeFooterView(footerLoadmore);
            dlginfoTxtchapters.setText(adapter.getCount() + "");
        }
        GlobalData.DissmissProgress();
    }
}