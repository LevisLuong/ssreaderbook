package vn.seasoft.readerbook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.viewpagerindicator.TabPageIndicator;
import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.Fragment;
import urlimageviewhelper.UrlImageViewHelper;
import vn.seasoft.readerbook.HttpServices.*;
import vn.seasoft.readerbook.RequestObjects.Request_Server;
import vn.seasoft.readerbook.ResultObjects.Result_GetCountLikeBook;
import vn.seasoft.readerbook.ResultObjects.Result_GetIsUserLikeBook;
import vn.seasoft.readerbook.ResultObjects.Result_UserDisLike;
import vn.seasoft.readerbook.ResultObjects.Result_UserLike;
import vn.seasoft.readerbook.Util.GlobalData;
import vn.seasoft.readerbook.Util.asynPostFacebook;
import vn.seasoft.readerbook.Util.mSharedPreferences;
import vn.seasoft.readerbook.dialog.dlgShareFacebook;
import vn.seasoft.readerbook.fragment.fmChapter;
import vn.seasoft.readerbook.fragment.fmComment;
import vn.seasoft.readerbook.fragment.fmSummary;
import vn.seasoft.readerbook.listener.IDialogEditText;
import vn.seasoft.readerbook.listener.ILoginFacebook;
import vn.seasoft.readerbook.model.Book;
import vn.seasoft.readerbook.model.Book_Category;

/**
 * User: XuanTrung
 * Date: 7/2/2014
 * Time: 10:59 AM
 */
public class actInfoBook extends Activity implements OnHttpServicesListener {
    Context mContext;
    Book book;
    fmChapter fmchapter;
    fmComment fmcomment;

    Request_Server request_service;

    boolean isUserLikeBook = false;

    private ImageView dlginfoImgcover;
    private TextView dlginfoTxtTitle;
    private TextView dlginfoTxtAuthor;
    private TextView dlginfoTxtCategory;
    private TextView dlginfoTxtchapters;
    private TextView dlginfoTxtdownloads;
    private TextView dlginfoTxtview;
    private TabPageIndicator dlginfoIndicator;
    private ViewPager dlginfoViewpager;
    private Button btnLike, btnContRead;

    private void assignViews() {
        dlginfoImgcover = (ImageView) findViewById(R.id.dlginfo_imgcover);
        dlginfoTxtTitle = (TextView) findViewById(R.id.dlginfo_txtTitle);
        dlginfoTxtAuthor = (TextView) findViewById(R.id.dlginfo_txtAuthor);
        dlginfoTxtCategory = (TextView) findViewById(R.id.dlginfo_txtCategory);
        dlginfoTxtchapters = (TextView) findViewById(R.id.dlginfo_txtchapters);
        dlginfoTxtdownloads = (TextView) findViewById(R.id.dlginfo_txtdownloads);
        dlginfoTxtview = (TextView) findViewById(R.id.dlginfo_txtview);
        dlginfoIndicator = (TabPageIndicator) findViewById(R.id.dlginfo_indicator);
        dlginfoViewpager = (ViewPager) findViewById(R.id.dlginfo_viewpager);
        btnLike = (Button) findViewById(R.id.dlginfo_like);
        btnContRead = (Button) findViewById(R.id.dlginfo_cont_read);
    }

    void setListenerButton() {
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int iduser = mSharedPreferences.getUserID(mContext);
                if (iduser == 0) {
                    SSReaderApplication.authorizeFB(mContext, new ILoginFacebook() {
                        @Override
                        public void LoginSuccess() {
                            GlobalData.ShowProgressDialog(mContext, R.string.please_wait);
                            request_service.isUserLikeBook(book.getIdbook(), mSharedPreferences.getUserID(mContext));
                        }
                    });
                } else {
                    GlobalData.ShowProgressDialog(mContext, R.string.please_wait);
                    if (isUserLikeBook) {
                        request_service.userDisLikeBook(book.getIdbook(), iduser);
                    } else {
                        request_service.userLikeBook(book.getIdbook(), iduser);
                    }
                }
            }
        });
        btnContRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fmchapter.loadContinueBook();
            }
        });
    }

    void getBookIntent(Intent intent) {
        int idbook = intent.getIntExtra("idbook", 0);
        String titlebook = intent.getStringExtra("titlebook");
        String authorbook = intent.getStringExtra("authorbook");
        int countview = intent.getIntExtra("countview", 0);
        int countdownload = intent.getIntExtra("countdownload", 0);
        int idcategory = intent.getIntExtra("idcategory", 0);
        String summary = intent.getStringExtra("summary");
        String urlcover = intent.getStringExtra("cover");
        book = new Book();
        book.setIdcategory(idcategory);
        book.setIdbook(idbook);
        book.setTitle(titlebook);
        book.setAuthor(authorbook);
        book.setCountdownload(countdownload);
        book.setCountview(countview);
        book.setSummary(summary);
        book.setImagecover(urlcover);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getBookIntent(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.dialog_book_info_tab);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        assignViews();
        setListenerButton();
        getBookIntent(getIntent());
        UrlImageViewHelper.setUrlDrawable(dlginfoImgcover, GlobalData.getUrlImageCover(book));
        dlginfoTxtTitle.setText(book.getTitle());
        dlginfoTxtAuthor.setText(book.getAuthor());
        String book_category = (new Book_Category()).getByID(book.getIdcategory()).getCategory();
        dlginfoTxtCategory.setText(book_category);
        dlginfoTxtview.setText(book.getCountview() + "");
        dlginfoTxtdownloads.setText(book.getCountdownload() + "");

        //Init tab viewpager
        dlginfoViewpager.setAdapter(new ViewpagerAdapter(getSupportFragmentManager()));
        dlginfoIndicator.setViewPager(dlginfoViewpager);

//        //update from server
//        GlobalData.ShowProgressDialog(mContext, R.string.please_wait);
//        SSReaderApplication.getRequestServer(mContext, this).getBookChapter(book.getIdbook(), 0);]
        fmchapter = new fmChapter(mContext, book);
        fmcomment = new fmComment(mContext, book.getIdbook());

        //Get data like function
        request_service = new Request_Server(mContext);
        request_service.SetListener(this);

        GlobalData.ShowProgressDialog(mContext, R.string.please_wait);
        request_service.getCountLikeBook(book.getIdbook());
        int iduser = mSharedPreferences.getUserID(this);
        if (iduser != 0) {
            request_service.isUserLikeBook(book.getIdbook(), iduser);
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_infobook, menu);
        return super.onCreateOptionsMenu(menu);
    }

    void shareFacebook() {
        String content = "Mình đang xem \"" + book.getTitle() + " - " + book.getAuthor() + "\" từ ứng dụng \"Sách Của Tui\" ";
        dlgShareFacebook dlg = new dlgShareFacebook(mContext);
        dlg.setTitle("Nội dung chia sẻ Facebook");
        dlg.setMessage(content);
        dlg.setListener(new IDialogEditText() {
            @Override
            public void getValue(String value) {
                asynPostFacebook asyn = new asynPostFacebook(mContext, value, UrlImageViewHelper.getCachedBitmap(GlobalData.getUrlImageCover(book)));
                asyn.execute();
            }
        });
        dlg.show(getSupportFragmentManager());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sharefb:
                int iduser = mSharedPreferences.getUserID(mContext);
                if (iduser == 0) {
                    SSReaderApplication.authorizeFB(mContext, new ILoginFacebook() {
                        @Override
                        public void LoginSuccess() {
                            shareFacebook();
                        }
                    });
                } else {
                    shareFacebook();
                }
                break;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataError(int errortype, String urlMethod) {
        ErrorType.getErrorMessage(mContext, errortype);
        GlobalData.DissmissProgress();
    }

    @Override
    public void onGetData(ResultObject resultData, String urlMethod, int id) {
        if (urlMethod.equals(COMMAND_API.GET_COUNT_LIKE_BOOK)) {
            Result_GetCountLikeBook data = (Result_GetCountLikeBook) resultData;
            btnLike.setText(data.countlike + "");
        }
        if (urlMethod.equals(COMMAND_API.IS_USER_LIKE_BOOK)) {
            Result_GetIsUserLikeBook data = (Result_GetIsUserLikeBook) resultData;
            isUserLikeBook = data.isUserLike;
            if (isUserLikeBook) {
                btnLike.setBackgroundResource(R.drawable.like_btn);
            } else {
                btnLike.setBackgroundResource(R.drawable.unlike_btn);
            }
        }
        if (urlMethod.equals(COMMAND_API.USER_LIKE)) {
            Result_UserLike data = (Result_UserLike) resultData;
            if (data.status == Status.STATUS_OK) {
                btnLike.setBackgroundResource(R.drawable.like_btn);
                int numLike = Integer.parseInt(btnLike.getText().toString()) + 1;
                btnLike.setText(numLike + "");
                isUserLikeBook = true;
            }
        }
        if (urlMethod.equals(COMMAND_API.USER_DISLIKE)) {
            Result_UserDisLike data = (Result_UserDisLike) resultData;
            if (data.status == Status.STATUS_OK) {
                btnLike.setBackgroundResource(R.drawable.unlike_btn);
                int numLike = Integer.parseInt(btnLike.getText().toString()) - 1;
                btnLike.setText(numLike + "");
                isUserLikeBook = false;
            }
        }
        GlobalData.DissmissProgress();
    }

    public void setCountChapter(String number) {
        dlginfoTxtchapters.setText(number);
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
                    return fmcomment;

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