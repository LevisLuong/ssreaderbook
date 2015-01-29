package vn.seasoft.sachcuatui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.SeekBar;
import org.holoeverywhere.widget.SeekBar.OnSeekBarChangeListener;
import org.holoeverywhere.widget.Toast;
import urlimageviewhelper.UrlImageViewCallback;
import urlimageviewhelper.UrlImageViewHelper;
import vn.seasoft.sachcuatui.Util.GlobalData;
import vn.seasoft.sachcuatui.Util.SSUtil;
import vn.seasoft.sachcuatui.dialog.dlgConfirm;
import vn.seasoft.sachcuatui.dialog.dlgNumberPicker;
import vn.seasoft.sachcuatui.model.Book;
import vn.seasoft.sachcuatui.model.Book_Chapter;
import vn.seasoft.sachcuatui.widget.ImageViewTouchViewPager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * User: XuanTrung
 * Date: 8/11/2014
 * Time: 2:45 PM
 */
public class actReadPictureBook extends Activity {
    final String TAG = "actReadPictureBook";
    List<String> lstPicture;
    Context mContext;

    int idbook;
    int idbookchapter;
    Book book;
    Book_Chapter book_chapter;
    int position;
    private ImageViewTouchViewPager vpPager;
    private TextView vpTxtPageBottom;
    private LinearLayout vpLnTop;
    private ImageButton vpBtnback;
    private TextView vpTxtTitleBook;
    private TextView vpTxtTitleChapter;
    private TextView vpTxtPageTop;
    private org.holoeverywhere.widget.SeekBar vpSeekbar;
    private RelativeLayout vpEmpty;
    private LinearLayout vpLnBottom;
    private LinearLayout vpLnContainerMain;
    private ImageButton vpPreviouschapter;
    private ImageButton vpSelectpage;
    private ImageButton vpNextchapter;
    private ImageButton vpMenu;
    private ImageButton vpDeleteCache;

    private void assignViews() {
        vpPager = (ImageViewTouchViewPager) findViewById(R.id.vp_pager);
        vpTxtPageBottom = (TextView) findViewById(R.id.vp_txtPageBottom);
        vpLnTop = (LinearLayout) findViewById(R.id.vp_lnTop);
        vpBtnback = (ImageButton) findViewById(R.id.vp_btnback);
        vpTxtTitleBook = (TextView) findViewById(R.id.vp_txtTitleBook);
        vpTxtTitleChapter = (TextView) findViewById(R.id.vp_txtTitleChapter);
        vpTxtPageTop = (TextView) findViewById(R.id.vp_txtPageTop);
        vpSeekbar = (org.holoeverywhere.widget.SeekBar) findViewById(R.id.vp_seekbar);
        vpEmpty = (RelativeLayout) findViewById(R.id.vp_empty);
        vpLnBottom = (LinearLayout) findViewById(R.id.vp_lnBottom);
        vpPreviouschapter = (ImageButton) findViewById(R.id.vp_previouschapter);
        vpSelectpage = (ImageButton) findViewById(R.id.vp_selectpage);
        vpNextchapter = (ImageButton) findViewById(R.id.vp_nextchapter);
        vpMenu = (ImageButton) findViewById(R.id.vp_menu);
        vpDeleteCache = (ImageButton) findViewById(R.id.vp_deletecache);
        vpLnContainerMain = (LinearLayout) findViewById(R.id.vp_containermenu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.view_pager);
        mContext = this;
        assignViews();
        onNewIntent(getIntent());

        vpPager.setAdapter(new ImagePagerAdapter());
        vpPager.setCurrentItem(position, false);

        vpPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                vpTxtPageTop.setText((i + 1) + "/" + lstPicture.size());
                vpTxtPageBottom.setText((i + 1) + "/" + lstPicture.size());
                vpSeekbar.setProgress(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        vpNextchapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vpPager.setCurrentItem(vpPager.getCurrentItem() + 1, true);
            }
        });
        vpPreviouschapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vpPager.setCurrentItem(vpPager.getCurrentItem() - 1, true);
            }
        });

        vpSeekbar.setOnSeekBarChangeListener(null);
        vpSeekbar.setMax(lstPicture.size());
        vpSeekbar.setProgress(position);

        vpSeekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                vpPager.setCurrentItem(i, true);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        vpEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetInvisible();
            }
        });
        vpMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetVisible();
            }
        });
        vpBtnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        vpSelectpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlgNumberPicker dlg = new dlgNumberPicker(mContext, 1, lstPicture.size(), vpPager.getCurrentItem() + 1);
                dlg.setListener(new dlgNumberPicker.IDialogNumberPicker() {
                    @Override
                    public void getValue(int value) {
                        vpPager.setCurrentItem(value - 1, true);
                    }
                });
                dlg.show(getSupportFragmentManager());
            }
        });
        vpDeleteCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlgConfirm dlg = new dlgConfirm(mContext);
                dlg.setMessage("Bạn có chắc muốn xóa bộ nhớ tạm cho máy !");
                dlg.setListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SSUtil.deletePictureBook(mContext, book_chapter);
                        org.holoeverywhere.widget.Toast.makeText(mContext, "Đã xóa khỏi bộ nhớ máy", Toast.LENGTH_SHORT).show();
                    }
                });
                dlg.show(getSupportFragmentManager());
            }
        });
        //set defalut value
        vpTxtPageTop.setText((position + 1) + "/" + lstPicture.size());
        vpTxtPageBottom.setText((position + 1) + "/" + lstPicture.size());
        SetInvisible();
    }

    public void SetVisible() {
        Animation slideUp = AnimationUtils.loadAnimation(mContext, R.anim.slide_up);
        Animation slideDown = AnimationUtils.loadAnimation(mContext, R.anim.slide_down);
        vpLnContainerMain.setVisibility(View.VISIBLE);
        vpLnTop.startAnimation(slideDown);
        vpLnBottom.startAnimation(slideUp);
    }

    public void SetInvisible() {
        vpLnContainerMain.setVisibility(View.GONE);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        lstPicture = new ArrayList<String>();
        String arrBook = intent.getStringExtra("arrbook");
        arrBook = arrBook + ",z_endchapter";
        lstPicture = Arrays.asList(arrBook.split(","));
        Collections.sort(lstPicture);
        idbook = intent.getIntExtra("idbook", 0);
        idbookchapter = intent.getIntExtra("idbookchapter", 0);
        book = (new Book()).getByID(idbook);
        book_chapter = (new Book_Chapter()).getByID(idbookchapter);

        position = intent.getIntExtra("position", 0);
        SSUtil.App_Log(TAG, "position: " + position);

        vpTxtTitleBook.setText(book.getTitle());
        vpTxtTitleChapter.setText(book_chapter.getChapter());


    }

    @Override
    public void onPause() {
        super.onPause();
        Book_Chapter bc = ((new Book_Chapter()).getByID(idbookchapter));
        bc.setReadposition(vpPager.getCurrentItem());
        bc.updateData();
        SSUtil.App_Log(TAG, "Update position: " + vpPager.getCurrentItem());
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (vpLnContainerMain.getVisibility() == View.VISIBLE) {
                SetInvisible();
                return false;
            }
        }
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (vpLnContainerMain.getVisibility() == View.VISIBLE) {
                SetInvisible();
            } else {
                SetVisible();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    class ImagePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return lstPicture.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            View itemView;
            if (position == lstPicture.size() - 1) {
                itemView = getLayoutInflater().inflate(R.layout.view_pager_endchapter,
                        container, false);
                Button btnNextChapter = (Button) itemView.findViewById(R.id.vp_endchapter_nextchapter);
                btnNextChapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Book_Chapter nextChapter = book_chapter.getNextChapter();
                        if (nextChapter != null) {
                            book_chapter.setCurrentread(false);
                            book_chapter.updateData();
                            nextChapter.setCurrentread(true);
                            nextChapter.setIsDownloaded(true);
                            nextChapter.updateData();
                            Intent t = new Intent(mContext, actReadPictureBook.class);
                            t.putExtra("arrbook", nextChapter.getFilename());
                            t.putExtra("idbook", nextChapter.getIdbook());
                            t.putExtra("idbookchapter", nextChapter.getIdbook_chapter());
                            t.putExtra("position", nextChapter.getReadposition());
                            mContext.startActivity(t);
                            finish();
                        } else {
                            Toast.makeText(mContext, "Đã hết chapter", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Button btnBack = (Button) itemView.findViewById(R.id.vp_endchapter_back);
                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
            } else {
                itemView = getLayoutInflater().inflate(R.layout.view_pager_page,
                        container, false);
                final ImageViewTouch imv_Object = (ImageViewTouch) itemView
                        .findViewById(R.id.viewpage_imageView);
                final ProgressBar progressBar = (ProgressBar) itemView.findViewById(R.id.viewpage_progress);
                String url = GlobalData.getUrlPictureBook(idbook, idbookchapter) + lstPicture.get(position);
                imv_Object.setScaleType(ImageView.ScaleType.FIT_XY);
                UrlImageViewHelper.setUrlDrawable(imv_Object, url, R.drawable.ic_menu_autoscroll, UrlImageViewHelper.CACHE_DURATION_ONE_WEEK, new UrlImageViewCallback() {

                    @Override
                    public void onLoaded(ImageView imageView, Bitmap loadedBitmap, String url, boolean loadedFromCache) {
                        if (loadedBitmap != null) {
                            imv_Object.zoomTo(1.0f, 20);
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
            ((ViewPager) container).addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((RelativeLayout) object);

        }

        @Override
        public void startUpdate(ViewGroup container) {
            // TODO Auto-generated method stub
            super.startUpdate(container);

        }
    }

    //    public void getBitmapFromZip(final String zipFilePath) {
//        lstBitmap = new ArrayList<Bitmap>();
//        try {
//            FileInputStream fis = new FileInputStream(zipFilePath);
//            ZipInputStream zis = new ZipInputStream(fis);
//            while (zis.getNextEntry() != null) {
//                Bitmap bitmap = BitmapFactory.decodeStream(zis);
//                lstBitmap.add(bitmap);
//            }
//        } catch (FileNotFoundException e) {
//            Log.e("Zip log", "Extracting file: Error opening zip file - FileNotFoundException: ", e);
//            e.printStackTrace();
//        } catch (IOException e) {
//            Log.e("Zip log", "Extracting file: Error opening zip file - IOException: ", e);
//            e.printStackTrace();
//        }
//    }

//    public class PageProvider implements CurlView.PageProvider {
//
//        private Bitmap loadBitmap(int width, int height, int index) {
//            Bitmap b = Bitmap.createBitmap(width, height,
//                    Bitmap.Config.ARGB_8888);
//            b.eraseColor(0xFFFFFFFF);
//            Canvas c = new Canvas(b);
//            Rect dest = new Rect(0, 0, width, height);
//            Paint paint = new Paint();
//            paint.setFilterBitmap(true);
//            c.drawBitmap(lstBitmap.get(index), null, dest, paint);
//            return b;
//        }
//
//        @Override
//        public int getPageCount() {
//            return lstBitmap.size();
//        }
//
//        @Override
//        public void updatePage(CurlPage page, int width, int height, int index) {
//            Bitmap front = loadBitmap(width, height, index);
//            page.setTexture(front, CurlPage.SIDE_BOTH);
//            page.setColor(Color.argb(127, 255, 255, 255),
//                    CurlPage.SIDE_BACK);
//        }
//
//    }
}
