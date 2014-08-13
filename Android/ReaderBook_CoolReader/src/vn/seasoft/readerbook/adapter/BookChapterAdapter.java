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

import java.util.List;

/**
 * User: XuanTrung
 * Date: 7/11/2014
 * Time: 11:07 AM
 */
public class BookChapterAdapter extends BaseAdapter {
    Context context;
    List<Book_Chapter> lstBookChap;

    public BookChapterAdapter(Context _ct, List<Book_Chapter> _lstBookChap) {
        context = _ct;
        lstBookChap = _lstBookChap;
    }

    public void addData(List<Book_Chapter> _lst) {
        lstBookChap.addAll(_lst);
        notifyDataSetChanged();
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
        if (SSUtil.deleteBook(GlobalData.getUrlBook(book_chapter))) {
            book_chapter.setIsDownloaded(false);
            book_chapter.updateData();
        }
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
