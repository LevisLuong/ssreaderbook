package vn.seasoft.readerbook;

import android.content.Intent;
import android.graphics.*;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.widget.ViewPager;
import vn.seasoft.readerbook.widget.CurlPage;
import vn.seasoft.readerbook.widget.CurlView;
import vn.seasoft.readerbook.widget.ViewPagerFragment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * User: XuanTrung
 * Date: 8/11/2014
 * Time: 2:45 PM
 */
public class actReadPictureBook extends Activity {
    private ViewPager mViewpager;
    List<Bitmap> lstBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);

        onNewIntent(getIntent());
        mViewpager = (ViewPager) findViewById(R.id.pager);
        ScreenSlidePagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mViewpager.setAdapter(pagerAdapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String urlzip = intent.getStringExtra("file");
        getBitmapFromZip(urlzip);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new ViewPagerFragment(lstBitmap.get(position));
        }

        @Override
        public int getCount() {
            return lstBitmap.size();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void getBitmapFromZip(final String zipFilePath) {
        lstBitmap = new ArrayList<Bitmap>();
        try {
            FileInputStream fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            while (zis.getNextEntry() != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(zis);
                lstBitmap.add(bitmap);
            }
        } catch (FileNotFoundException e) {
            Log.e("Zip log", "Extracting file: Error opening zip file - FileNotFoundException: ", e);
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Zip log", "Extracting file: Error opening zip file - IOException: ", e);
            e.printStackTrace();
        }
    }

    public class PageProvider implements CurlView.PageProvider {

        private Bitmap loadBitmap(int width, int height, int index) {
            Bitmap b = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            b.eraseColor(0xFFFFFFFF);
            Canvas c = new Canvas(b);
            Rect dest = new Rect(0, 0, width, height);
            Paint paint = new Paint();
            paint.setFilterBitmap(true);
            c.drawBitmap(lstBitmap.get(index), null, dest, paint);
            return b;
        }

        @Override
        public int getPageCount() {
            return lstBitmap.size();
        }

        @Override
        public void updatePage(CurlPage page, int width, int height, int index) {
            Bitmap front = loadBitmap(width, height, index);
            page.setTexture(front, CurlPage.SIDE_BOTH);
            page.setColor(Color.argb(127, 255, 255, 255),
                    CurlPage.SIDE_BACK);
        }

    }
}
