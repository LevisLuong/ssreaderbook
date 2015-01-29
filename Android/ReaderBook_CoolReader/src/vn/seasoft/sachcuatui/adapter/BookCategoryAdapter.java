package vn.seasoft.sachcuatui.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import org.holoeverywhere.app.Fragment;
import vn.seasoft.sachcuatui.model.Book_Category;

import java.util.ArrayList;
import java.util.List;

public class BookCategoryAdapter extends FragmentPagerAdapter {

    List<Book_Category> lstBookCate;

    public BookCategoryAdapter(FragmentManager fm, List<Book_Category> _lst) {
        super(fm);
        lstBookCate = new ArrayList<Book_Category>();
        lstBookCate = _lst;
        lstBookCate.add(0, new Book_Category(0, "Các sách đã đọc"));
    }

    @Override
    public Fragment getItem(int position) {
//        return fmListBook.getInstance(lstBookCate.get(position));
        return null;
    }

    @Override
    public int getCount() {
        return lstBookCate.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return lstBookCate.get(position).getCategory();
    }
}