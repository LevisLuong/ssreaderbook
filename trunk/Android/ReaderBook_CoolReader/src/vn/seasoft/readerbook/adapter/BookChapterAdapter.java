package vn.seasoft.readerbook.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import vn.seasoft.readerbook.R;
import vn.seasoft.readerbook.Util.GlobalData;
import vn.seasoft.readerbook.Util.SSUtil;
import vn.seasoft.readerbook.model.Book_Chapter;

import java.util.ArrayList;
import java.util.List;

/**
 * User: XuanTrung
 * Date: 7/11/2014
 * Time: 11:07 AM
 */
public class BookChapterAdapter extends BaseAdapter {
    Context context;
    List<Book_Chapter> lstBookChap;

    int index;
    boolean isLoading;
    boolean isHaveNew;

    int tempIndex;

    public BookChapterAdapter(Context _ct) {
        context = _ct;
        lstBookChap = new ArrayList<Book_Chapter>();

        isLoading = true;
        isHaveNew = true;
        index = 0;
        tempIndex = 0;
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

    public void setFromDataBase(List<Book_Chapter> _lst) {
        lstBookChap = _lst;
        notifyDataSetChanged();

    }

    public boolean SetListBooks(List<Book_Chapter> _lst) {
        boolean isNew = false;
        isLoading = false;
        index = tempIndex;
        if (index == 1) {
            lstBookChap = _lst;
            isNew = true;
        } else {
            lstBookChap.addAll(_lst);
        }
        if (_lst.size() < 10) {
            isHaveNew = false;
        } else {
            isHaveNew = true;
        }
        notifyDataSetChanged();
        return isNew;
    }

    public List<Book_Chapter> getList() {
        return lstBookChap;
    }

    @Override
    public int getCount() {
        return lstBookChap.size();
    }

    @Override
    public Book_Chapter getItem(int i) {
        return lstBookChap.get(i);
    }

    @Override
    public long getItemId(int i) {
        return lstBookChap.get(i).getIdbook();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.book_chapter_item, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Book_Chapter book_chapter = lstBookChap.get(i);
        if (book_chapter != null) {
            holder.bookchaptertxtchap.setText(book_chapter.getChapter());

            if (book_chapter.getCurrentread()) {
                holder.bookchaptertxtchap.setTextColor(Color.RED);
            } else {
                holder.bookchaptertxtchap.setTextColor(Color.BLACK);
            }
            if (book_chapter.getIsDownloaded()) {
                holder.bookchaptertxtfilesize.setText("Đã tải");
            } else {
                holder.bookchaptertxtfilesize.setText(book_chapter.getFilesize());
            }
        }
        return view;
    }

    public void setRead(int position) {
        for (Book_Chapter bookchap : lstBookChap) {
            bookchap.setCurrentread(false);
            bookchap.updateData();
        }
        getItem(position).setCurrentread(true);
        getItem(position).updateData();
    }

    public void setDownloaded(int position) {
        getItem(position).setIsDownloaded(true);
        getItem(position).updateData();
    }

    public void deleteBook(int position) {
        Book_Chapter book_chapter = lstBookChap.get(position);
        SSUtil.deleteBook(GlobalData.getUrlBook(book_chapter));
        book_chapter.setIsDownloaded(false);
        book_chapter.updateData();
        notifyDataSetChanged();
    }

    class ViewHolder {
        public final TextView bookchaptertxtchap;
        public final TextView bookchaptertxtfilesize;
        public final View root;

        public ViewHolder(View root) {
            bookchaptertxtchap = (TextView) root.findViewById(R.id.bookchapter_txtchap);
            bookchaptertxtfilesize = (TextView) root.findViewById(R.id.bookchapter_txtfilesize);
            this.root = root;
        }
    }
}
