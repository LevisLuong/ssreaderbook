package vn.seasoft.readerbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import vn.seasoft.readerbook.R;
import vn.seasoft.readerbook.Util.GlobalData;
import vn.seasoft.readerbook.Util.SSUtil;
import vn.seasoft.readerbook.model.Book;
import vn.seasoft.readerbook.model.Book_Chapter;

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

    public AdapterHListViewBook(Context _context) {
        inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        lstBooks = new ArrayList<Book>();
        isLoading = true;
        isHaveNew = true;
        index = 1;
        tempIndex = 1;
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
            SSUtil.deleteBook(GlobalData.getUrlBook(bc));
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
            view = inflater.inflate(R.layout.gridbook_item, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Book book = lstBooks.get(i);
        if (book != null) {
            holder.griditemlabel.setText("                              ");
            UrlImageViewHelper.setUrlDrawable(holder.griditemcover, GlobalData.getUrlImageCover(book), R.drawable.book_exam);
        }
        return view;
    }

    class ViewHolder {
        public final TextView griditemlabel;
        public final ImageView griditemcover;
        public final View root;

        public ViewHolder(View root) {
            griditemlabel = (TextView) root.findViewById(R.id.grid_item_label);
            griditemcover = (ImageView) root.findViewById(R.id.grid_item_cover);
            this.root = root;
        }
    }
}
