package vn.seasoft.sachcuatui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthError;
import org.coolreader.CoolReader;
import org.coolreader.crengine.BackgroundThread;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.app.DialogFragment;
import urlimageviewhelper.UrlImageViewHelper;
import vn.seasoft.sachcuatui.HttpServices.COMMAND_API;
import vn.seasoft.sachcuatui.HttpServices.ErrorType;
import vn.seasoft.sachcuatui.HttpServices.OnHttpServicesListener;
import vn.seasoft.sachcuatui.HttpServices.ResultObject;
import vn.seasoft.sachcuatui.R;
import vn.seasoft.sachcuatui.ResultObjects.Result_GetBookChapter;
import vn.seasoft.sachcuatui.SSReaderApplication;
import vn.seasoft.sachcuatui.Util.AsyntaskDownloadFile;
import vn.seasoft.sachcuatui.Util.GlobalData;
import vn.seasoft.sachcuatui.Util.asynPostFacebook;
import vn.seasoft.sachcuatui.actReadPictureBook;
import vn.seasoft.sachcuatui.adapter.BookChapterAdapter;
import vn.seasoft.sachcuatui.listener.IDialogEditText;
import vn.seasoft.sachcuatui.model.Book;
import vn.seasoft.sachcuatui.model.Book_Category;
import vn.seasoft.sachcuatui.model.Book_Chapter;
import vn.seasoft.sachcuatui.widget.ViewError;

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
    boolean asc = true;
    private ImageButton dlginfoSortChapter;
    private LinearLayout dlginfoSharefb;
    private ImageButton dlginfoSelectchapters;
    private TextView dlginfoTxtSortChapters;
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
    public dlgInfoBook() {
        super();
    }

    public dlgInfoBook(Context _context, Book _book) {
        setDialogType(DialogType.AlertDialog);
        mContext = _context;
        book = _book;
    }

    private void assignViews(View root) {
        dlginfoTxtSortChapters = (TextView) root.findViewById(R.id.dlginfo_txtsortchapter);
        dlginfoSelectchapters = (ImageButton) root.findViewById(R.id.dlginfo_selectchapter);
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
//        new SwipeDismissList(dlginfoListview, new SwipeDismissList.OnDismissCallback() {
//            @Override
//            public SwipeDismissList.Undoable onDismiss(AbsListView listView, int position) {
//                adapter.deleteBook(position);
//                return null;
//            }
//        }, SwipeDismissList.UndoMode.SINGLE_UNDO);
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
                String content = "Mình đang xem \"" + book.getTitle() + " - " + book.getAuthor() + "\" từ ứng dụng \"Sách Của Tui\"";
                dlgGoChapter dlg = new dlgGoChapter(mContext);
                dlg.setTitle("Nội dung đăng lên Facebook");
                dlg.setMessage(content);
                dlg.setTypeEditText(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                dlg.setListener(new IDialogEditText() {
                    @Override
                    public void getValue(String value) {
                        asynPostFacebook asyn = new asynPostFacebook(mContext, value, UrlImageViewHelper.getCachedBitmap(GlobalData.getUrlImageCover(book)));
                        asyn.execute();
                    }
                });
                dlg.show(getSupportActivity());
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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
                    if (position == adapter.getCount()) {
                        position = 0;
                    }
                    dlginfoListview.setSelection(position);
                    addViewContainer(dlginfoContainer, dlginfoListview);
                    dlginfoTxtchapters.setText(adapter.getCount() + "");
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_book_info, container, false);
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
                    dlgGoChapter dlg = new dlgGoChapter(mContext);
                    dlg.setTitle("Nhập chương để đọc");
                    dlg.setListener(new IDialogEditText() {
                        @Override
                        public void getValue(String value) {
                            try {
                                int page = Integer.parseInt(value.trim());
                                if (page > 0 && page <= adapter.getCount()) {
                                    page = page - 1;
                                    loadBook(page);
                                } else {
                                    org.holoeverywhere.widget.Toast.makeText(mContext, "Không có chương này !", Toast.LENGTH_SHORT).show();
                                }
                            } catch (NumberFormatException e) {
                                org.holoeverywhere.widget.Toast.makeText(mContext, "Không có chương này !", Toast.LENGTH_SHORT).show();
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
                sortChapter();
            }
        });
        dlginfoTxtSortChapters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortChapter();
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
        dlginfoListview.setAdapter(adapter);
        dlginfoListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                if (i < adapter.getCount()) {
                    loadBook(i);
                }
            }
        });

        //update from server
        GlobalData.ShowProgressDialog(mContext, R.string.please_wait);
        SSReaderApplication.getRequestServer(mContext, this).getBookChapter(book.getIdbook(), 0);
        return v;
    }

    void sortChapter() {
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

    private void loadBook(final int position) {
        final Book_Chapter book_chapter = adapter.getItem(position);
        if (book.getIdcategory() == 8) {
            book.addNewData();
            adapter.setLoadBook(position);
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
                    adapter.setLoadBook(position);
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
        builder.setNegativeButton(R.string.cancel_button, null);
        builder.setPositiveButton(R.string.doctiep_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                Intent t = new Intent(getActivity(), actReadPictureBook.class);
//                File f = new File(Environment.getExternalStorageDirectory().getPath() + "/Download/dragonball.zip");
//                t.putExtra("file", f.getAbsolutePath());
//                startActivity(t);
                GlobalData.ShowProgressDialog(mContext, R.string.please_wait);
                BackgroundThread.instance().executeGUI(new Runnable() {
                    @Override
                    public void run() {
                        if (!adapter.getList().isEmpty()) {
                            int position;
                            position = -1;
                            for (int y = 0; y < adapter.getCount(); y++) {
                                Book_Chapter book_chapter = adapter.getItem(y);
                                if (book_chapter.getCurrentread()) {
                                    position = y;
                                    break;
                                }
                            }
                            if (position == -1) {
                                if (!asc) {
                                    position = adapter.getCount() - 1;
                                } else {
                                    position = 0;
                                }
                            }
                            loadBook(position);
                        }
                        GlobalData.DissmissProgress();
                    }
                });

            }
        });
        return builder.create();
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
    public void onGetData(final ResultObject resultData, String urlMethod, int id) {
        if (urlMethod.equals(COMMAND_API.GET_BOOK_CHAPTER)) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Result_GetBookChapter data = (Result_GetBookChapter) resultData;
                    if (adapter.SetListBooks(data.lstBookChaps)) {
                        addViewContainer(dlginfoContainer, dlginfoListview);
                    }
                    dlginfoTxtchapters.setText(adapter.getCount() + "");
                }
            });
        }
        GlobalData.DissmissProgress();
    }
}