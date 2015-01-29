package vn.seasoft.sachcuatui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import urlimageviewhelper.UrlImageViewHelper;
import vn.seasoft.sachcuatui.R;
import vn.seasoft.sachcuatui.Util.GlobalData;
import vn.seasoft.sachcuatui.Util.SSUtil;
import vn.seasoft.sachcuatui.model.Book;
import vn.seasoft.sachcuatui.model.Book_Chapter;

import java.util.ArrayList;
import java.util.List;

/**
 * User: XuanTrung
 * Date: 7/29/2014
 * Time: 2:47 PM
 */
public class AdapterHListViewBook extends BaseAdapter {
    List<Book> lstBooks;
    LayoutInflater inflater;

    int index;
    boolean isLoading;
    boolean isHaveNew;

    int tempIndex;

    Context context;

    public AdapterHListViewBook(Context _context) {
        inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        lstBooks = new ArrayList<Book>();
        isLoading = true;
        isHaveNew = true;
        index = 1;
        tempIndex = 1;
        context = _context;
    }

    public boolean canLoadMoreData() {
        return (!isLoading && isHaveNew);
    }

    public boolean isHaveNew() {
        return isHaveNew;
    }

    public int loadMoreData() {
        isLoading = true;
        index++;
        tempIndex = index;
        return tempIndex;
    }

    public int reloadData() {
        isLoading = true;
        tempIndex = index;
        return index;
    }

    public boolean SetListBooks(List<Book> _lst) {
        boolean isNew = false;
        isLoading = false;
        index = tempIndex;
        if (index == 1) {
            lstBooks = _lst;
            isNew = true;
        } else {
            lstBooks.addAll(_lst);
        }
        if (_lst.size() < 10) {
            isHaveNew = false;
        } else {
            isHaveNew = true;
        }
        notifyDataSetChanged();
        return isNew;
    }

    public void removeBook(int position) {
        Book book = lstBooks.get(position);
        List<Book_Chapter> lstbookchap = (new Book_Chapter()).getByidBook(book.getIdbook());
        for (Book_Chapter bc : lstbookchap) {
            if (book.getIdcategory() == 8) {
                SSUtil.deletePictureBook(context, bc);
            } else {
                SSUtil.deleteBook(GlobalData.getUrlBook(bc));
            }

            bc.deleteData();
        }
        book.deleteData();
        lstBooks.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return lstBooks.size();
    }

    @Override
    public Book getItem(int i) {
        return lstBooks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.hlistview_item, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Book book = lstBooks.get(i);
        if (book != null) {
            UrlImageViewHelper.setUrlDrawable(holder.griditemcover, GlobalData.getUrlImageCover(book), R.drawable.book_exam);
            String titlebook = book.getTitle();
            if (titlebook.length() > 10) {
                titlebook = titlebook.substring(0, 10) + "...";
            }
            holder.griditemtitle.setText(titlebook);
        }
        return view;
    }

    class ViewHolder {
        public final ImageView griditemcover;
        public final View root;
        public final TextView griditemtitle;

        public ViewHolder(View root) {
            griditemcover = (ImageView) root.findViewById(R.id.grid_item_cover);
            griditemtitle = (TextView) root.findViewById(R.id.grid_item_title);
            this.root = root;
        }
    }
}
