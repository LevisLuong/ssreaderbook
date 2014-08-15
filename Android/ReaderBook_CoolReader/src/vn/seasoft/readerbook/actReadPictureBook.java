package vn.seasoft.readerbook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import org.holoeverywhere.app.Activity;
import vn.seasoft.readerbook.Util.GlobalData;
import vn.seasoft.readerbook.widget.ImageViewTouchViewPager;

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
    private ImageViewTouchViewPager mViewpager;
    List<String> lstPicture;

    int idbook;
    int idbookchapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);
        onNewIntent(getIntent());
        mViewpager = (ImageViewTouchViewPager) findViewById(R.id.pager);
        mViewpager.setAdapter(new ImagePagerAdapter());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        lstPicture = new ArrayList<String>();
        String arrBook = intent.getStringExtra("arrbook");
        lstPicture = Arrays.asList(arrBook.split(","));
        Collections.sort(lstPicture);
        idbook = intent.getIntExtra("idbook", 0);
        idbookchapter = intent.getIntExtra("idbookchapter", 0);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
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
            View itemView = getLayoutInflater().inflate(R.layout.view_pager_page,
                    container, false);
            final ImageViewTouch imv_Object = (ImageViewTouch) itemView
                    .findViewById(R.id.imageView);
            String url = GlobalData.getUrlPictureBook(idbook, idbookchapter) + lstPicture.get(position);
            imv_Object.setScaleType(ImageView.ScaleType.FIT_XY);
            UrlImageViewHelper.setUrlDrawable(imv_Object, url,
                    R.drawable.book_exam, UrlImageViewHelper.CACHE_DURATION_ONE_WEEK, new UrlImageViewCallback() {

                        @Override
                        public void onLoaded(ImageView imageView, Bitmap loadedBitmap, String url, boolean loadedFromCache) {
                            imv_Object.zoomTo(1.0f, 20);
                        }
                    });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
