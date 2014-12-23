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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.viewpagerindicator.TabPageIndicator;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.app.Dialog;
import org.holoeverywhere.app.DialogFragment;
import org.holoeverywhere.app.Fragment;
import urlimageviewhelper.UrlImageViewHelper;
import vn.seasoft.readerbook.R;
import vn.seasoft.readerbook.SSReaderApplication;
import vn.seasoft.readerbook.Util.GlobalData;
import vn.seasoft.readerbook.fragment.fmChapter;
import vn.seasoft.readerbook.fragment.fmComment;
import vn.seasoft.readerbook.fragment.fmSummary;
import vn.seasoft.readerbook.model.Book;
import vn.seasoft.readerbook.model.Book_Category;

/**
 * User: XuanTrung
 * Date: 7/2/2014
 * Time: 10:59 AM
 */
public class dlgInfoBook_tab extends DialogFragment {
    Context mContext;
    Book book;
    fmChapter fmchapter;
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
    public dlgInfoBook_tab() {

    }
    public dlgInfoBook_tab(Context _context, Book _book) {
        setDialogType(DialogType.Dialog);
        mContext = _context;
        book = _book;
    }

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
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setStyle(STYLE_NO_TITLE, R.style.full_screen_dialog);
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
        fmchapter = new fmChapter(mContext, book);
        return v;
    }

    public void setCountChapter(String number) {
        dlginfoTxtchapters.setText(number);
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
                        fmchapter.loadContinueBook();
                    }
                });
            }
        });
        return adialog;
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
                    return fmchapter;
                case 2:
                    return new fmComment(mContext, book.getIdbook());

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
                    return "Chương";
                case 2:
                    return "Bình Luận";
            }
            return super.getPageTitle(position);
        }
    }
}