package vn.seasoft.readerbook.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.viewpagerindicator.TabPageIndicator;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.coolreader.CoolReader;
import org.coolreader.crengine.BackgroundThread;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.app.DialogFragment;
import org.holoeverywhere.app.Fragment;
import urlimageviewhelper.UrlImageViewHelper;
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
import vn.seasoft.readerbook.fragment.fmChapter;
import vn.seasoft.readerbook.fragment.fmComment;
import vn.seasoft.readerbook.fragment.fmSummary;
import vn.seasoft.readerbook.model.Book;
import vn.seasoft.readerbook.model.Book_Category;
import vn.seasoft.readerbook.model.Book_Chapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * User: XuanTrung
 * Date: 7/2/2014
 * Time: 10:59 AM
 */
public class dlgInfoBook_tab extends DialogFragment implements OnHttpServicesListener {
    Context mContext;
    Book book;
    List<Book_Chapter> lstBookchapter;
    fmChapter fmchapter;


    public dlgInfoBook_tab() {

    }

    public dlgInfoBook_tab(Context _context, Book _book) {
        setDialogType(DialogType.Dialog);
        setStyle(STYLE_NORMAL, R.style.full_screen_dialog);
        mContext = _context;
        book = _book;
        lstBookchapter = new ArrayList<Book_Chapter>();
    }

    private ImageView dlginfoImgcover;
    private TextView dlginfoTxtTitle;
    private TextView dlginfoTxtAuthor;
    private TextView dlginfoTxtCategory;
    private TextView dlginfoTxtchapters;
    private TextView dlginfoTxtdownloads;
    private TextView dlginfoTxtview;
    private LinearLayout dlginfoSharefb;
    private TabPageIndicator dlginfoIndicator;
    private ViewPager dlginfoViewpager;

    private void assignViews(View root) {
        dlginfoImgcover = (ImageView) root.findViewById(R.id.dlginfo_imgcover);
        dlginfoTxtTitle = (TextView) root.findViewById(R.id.dlginfo_txtTitle);
        dlginfoTxtAuthor = (TextView) root.findViewById(R.id.dlginfo_txtAuthor);
        dlginfoTxtCategory = (TextView) root.findViewById(R.id.dlginfo_txtCategory);
        dlginfoTxtchapters = (TextView) root.findViewById(R.id.dlginfo_txtchapters);
        dlginfoTxtdownloads = (TextView) root.findViewById(R.id.dlginfo_txtdownloads);
        dlginfoTxtview = (TextView) root.findViewById(R.id.dlginfo_txtview);
        dlginfoSharefb = (LinearLayout) root.findViewById(R.id.dlginfo_sharefb);
        dlginfoIndicator = (TabPageIndicator) root.findViewById(R.id.dlginfo_indicator);
        dlginfoViewpager = (ViewPager) root.findViewById(R.id.dlginfo_viewpager);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

//        SSReaderApplication.getSocialAdapter().setListener(new DialogListener() {
//            @Override
//            public void onComplete(Bundle bundle) {
//                String content = "Mình đang xem \"" + book.getTitle() + " - " + book.getAuthor() + "\" từ ứng dụng \"Sách Của Tui\"";
//                dlgGoChapter dlg = new dlgGoChapter(mContext);
//                dlg.setTitle("Nội dung đăng lên Facebook");
//                dlg.setMessage(content);
//                dlg.setTypeEditText(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
//                dlg.setListener(new dlgGoChapter.IDialogEditText() {
//                    @Override
//                    public void getValue(String value) {
//                        asynPostFacebook asyn = new asynPostFacebook(mContext, value, UrlImageViewHelper.getCachedBitmap(GlobalData.getUrlImageCover(book)));
//                        asyn.execute();
//                    }
//                });
//                dlg.show(getSupportActivity());
//            }
//
//            @Override
//            public void onError(SocialAuthError socialAuthError) {
//
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onBack() {
//
//            }
//        });
    }

    boolean isFirst = true;

    @Override
    public void onResume() {
        super.onResume();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isFirst) {
                    lstBookchapter = (new Book_Chapter()).getByidBook(book.getIdbook());
                    sortChapter(old_asc);
                } else {
                    isFirst = false;
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

    boolean asc = true;
    boolean old_asc = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_book_info_tab, container, false);
        assignViews(v);
        UrlImageViewHelper.setUrlDrawable(dlginfoImgcover, GlobalData.getUrlImageCover(book));
        dlginfoTxtTitle.setText(book.getTitle());
        dlginfoTxtAuthor.setText(book.getAuthor());
        String book_category = (new Book_Category()).getByID(book.getIdcategory()).getCategory();
        dlginfoTxtCategory.setText(book_category);
        dlginfoTxtview.setText(book.getCountview() + "");
        dlginfoTxtdownloads.setText(book.getCountdownload() + "");

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

        //Init tab viewpager
        dlginfoViewpager.setAdapter(new ViewpagerAdapter(getChildFragmentManager()));
        dlginfoIndicator.setViewPager(dlginfoViewpager);

//        //update from server
//        GlobalData.ShowProgressDialog(mContext, R.string.please_wait);
//        SSReaderApplication.getRequestServer(mContext, this).getBookChapter(book.getIdbook(), 0);]
        fmchapter = new fmChapter(mContext, book, lstBookchapter);
        requestBookChapter();
        return v;
    }

    public void requestBookChapter() {
        SSReaderApplication.getRequestServer(mContext, this).getBookChapter(book.getIdbook(), 0);
    }

    public void sortChapter() {
        if (!lstBookchapter.isEmpty()) {
            old_asc = asc;
            if (asc) {
                Collections.sort(lstBookchapter, new Comparator<Book_Chapter>() {
                    @Override
                    public int compare(Book_Chapter book_chapter, Book_Chapter t1) {
                        return (book_chapter.getIdbook_chapter() > t1.getIdbook_chapter()) ? -1 : (book_chapter.getIdbook_chapter() > t1.getIdbook_chapter()) ? 1 : 0;
                    }
                });
                asc = false;
            } else {
                Collections.sort(lstBookchapter, new Comparator<Book_Chapter>() {
                    @Override
                    public int compare(Book_Chapter book_chapter, Book_Chapter t1) {
                        return (book_chapter.getIdbook_chapter() < t1.getIdbook_chapter()) ? -1 : (book_chapter.getIdbook_chapter() > t1.getIdbook_chapter()) ? 1 : 0;
                    }
                });
                asc = true;
            }
            fmchapter.setListData(lstBookchapter);
        }
    }

    public void sortChapter(boolean asc) {
        if (!lstBookchapter.isEmpty()) {
            if (asc) {
                Collections.sort(lstBookchapter, new Comparator<Book_Chapter>() {
                    @Override
                    public int compare(Book_Chapter book_chapter, Book_Chapter t1) {
                        return (book_chapter.getIdbook_chapter() > t1.getIdbook_chapter()) ? -1 : (book_chapter.getIdbook_chapter() > t1.getIdbook_chapter()) ? 1 : 0;
                    }
                });
            } else {
                Collections.sort(lstBookchapter, new Comparator<Book_Chapter>() {
                    @Override
                    public int compare(Book_Chapter book_chapter, Book_Chapter t1) {
                        return (book_chapter.getIdbook_chapter() < t1.getIdbook_chapter()) ? -1 : (book_chapter.getIdbook_chapter() > t1.getIdbook_chapter()) ? 1 : 0;
                    }
                });
            }
            fmchapter.setListData(lstBookchapter);
        }
    }


    public void gotochapter() {
        if (!lstBookchapter.isEmpty()) {
            dlgGoChapter dlg = new dlgGoChapter(mContext);
            dlg.setTitle("Nhập chương để đọc");
            dlg.setTextView("/" + dlginfoTxtchapters.getText().toString());
            dlg.setListener(new dlgGoChapter.IDialogEditText() {
                @Override
                public void getValue(String value) {
                    try {
                        int page = Integer.parseInt(value.trim());
                        if (page > 0 && page <= lstBookchapter.size()) {
                            page = page - 1;
                            if (!asc) {
                                page = lstBookchapter.size() - 1 - page;
                            }
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

    public void loadBook(final int position) {
        final Book_Chapter book_chapter = lstBookchapter.get(position);
        if (book.getIdcategory() == 8) {
            book.addNewData();
            fmchapter.setLoadBook(position);
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
                    fmchapter.setLoadBook(position);
                    Intent t = new Intent(mContext, CoolReader.class);
                    t.putExtra(CoolReader.OPEN_FILE_PARAM, urlResultMood);
                    mContext.startActivity(t);
                }

                @Override
                public void onCanceled() {
                    org.holoeverywhere.widget.Toast.makeText(mContext, R.string.download_fail, org.holoeverywhere.widget.Toast.LENGTH_SHORT).show();
                }
            });
            download.startDownload();
        }

        SSReaderApplication.getRequestServer(mContext).addCountBook(book.getIdbook());
    }

    private class ViewpagerAdapter extends FragmentPagerAdapter {


        public ViewpagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new fmSummary(book.getSummary());
                case 1:
                    return new fmComment(mContext, book.getIdbook());
                case 2:
                    return fmchapter;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Tóm Tắt";
                case 1:
                    return "Bình Luận";
                case 2:
                    return "Chương";
            }
            return super.getPageTitle(position);
        }
    }

    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getSupportActivity());
        builder.setNegativeButton(R.string.cancel_button, null);
        builder.setPositiveButton(R.string.doctiep_button, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });
//        builder.setPositiveButton(R.string.doctiep_button, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
////                Intent t = new Intent(getActivity(), actReadPictureBook.class);
////                File f = new File(Environment.getExternalStorageDirectory().getPath() + "/Download/dragonball.zip");
////                t.putExtra("file", f.getAbsolutePath());
////                startActivity(t);
//                GlobalData.ShowProgressDialog(mContext, R.string.please_wait);
//                BackgroundThread.instance().executeGUI(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (!lstBookchapter.isEmpty()) {
//                            int position;
//                            position = -1;
//                            for (int y = 0; y < lstBookchapter.size(); y++) {
//                                Book_Chapter book_chapter = lstBookchapter.get(y);
//                                if (book_chapter.getCurrentread()) {
//                                    position = y;
//                                    break;
//                                }
//                            }
//                            if (position == -1) {
//                                if (!asc) {
//                                    position = lstBookchapter.size() - 1;
//                                } else {
//                                    position = 0;
//                                }
//                            }
//                            loadBook(position);
//                        }
//                        GlobalData.DissmissProgress();
//                    }
//                });
//
//            }
//        });
        final AlertDialog adialog = builder.create();
        adialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button = adialog.getButton(DialogInterface.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //                GlobalData.ShowProgressDialog(mContext, R.string.please_wait);
                        BackgroundThread.instance().executeGUI(new Runnable() {
                            @Override
                            public void run() {
                                if (!lstBookchapter.isEmpty()) {
                                    int position;
                                    position = -1;
                                    for (int y = 0; y < lstBookchapter.size(); y++) {
                                        Book_Chapter book_chapter = lstBookchapter.get(y);
                                        if (book_chapter.getCurrentread()) {
                                            position = y;
                                            break;
                                        }
                                    }
                                    if (position == -1) {
                                        if (!asc) {
                                            position = lstBookchapter.size() - 1;
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
            }
        });
        return adialog;
    }

    @Override
    public void onDataError(int errortype, String urlMethod) {
        GlobalData.DissmissProgress();
        ErrorType.getErrorMessage(mContext, errortype);
    }

    @Override
    public void onGetData(final ResultObject resultData, String urlMethod, int id) {
        if (urlMethod.equals(COMMAND_API.GET_BOOK_CHAPTER)) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Result_GetBookChapter data = (Result_GetBookChapter) resultData;
                    lstBookchapter = data.lstBookChaps;
                    if (fmchapter != null) {
                        fmchapter.setListData(lstBookchapter);
                    }
                    dlginfoTxtchapters.setText(lstBookchapter.size() + "");
                }
            });
        }
        GlobalData.DissmissProgress();
    }
}