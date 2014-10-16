package vn.seasoft.readerbook.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import vn.seasoft.readerbook.R;
import vn.seasoft.readerbook.Util.GlobalData;
import vn.seasoft.readerbook.model.Book;

import java.util.List;

/**
 * User: XuanTrung
 * Date: 7/11/2014
 * Time: 11:07 AM
 */
public class SearchBookAdapter extends BaseAdapter {
    Context context;
    List<Book> lstBooks;

    int index;
    boolean isLoading;
    boolean isHaveNew;
    int tempIndex;

    public SearchBookAdapter(Context _ct, String strquery) {
        context = _ct;
        isLoading = true;
        isHaveNew = true;
        index = 1;
        tempIndex = 1;
        lstBooks = (new Book()).getSearchBook(strquery);
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
        index = tempIndex;
        if (index == 1) {
            isNew = true;
        }
        for (Book book : _lst) {
            Book checkBook = (new Book()).getByID(book.getIdbook());
            if (checkBook == null) {
                lstBooks.add(book);
            }
        }
        if (_lst.size() < 10) {
            isHaveNew = false;
        } else {
            isHaveNew = true;
        }
        isLoading = false;
        notifyDataSetChanged();
        return isNew;
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
        return lstBooks.get(i).getIdbook();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.search_book_item, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Book book = lstBooks.get(i);
        if (book != null) {
            UrlImageViewHelper.setUrlDrawable(holder.searchbookcover, GlobalData.getUrlImageCover(book), R.drawable.book_exam);
            holder.searchbooktitle.setText(Html.fromHtml("<b>Tựa sách: </b>" + book.getTitle()));
            holder.searchbookauthor.setText(Html.fromHtml("<b>Tác giả: </b>" + book.getAuthor()));
            String summary = book.getSummary();
            if (summary.length() > 100) {
                summary = summary.substring(0, 100) + "...";
            }
            holder.searchbooksummary.setText(Html.fromHtml("<b>Tóm tắt: </b>" + summary));
        }
        return view;
    }

    private class ViewHolder {
        public final ImageView searchbookcover;
        public final TextView searchbooktitle;
        public final TextView searchbookauthor;
        public final TextView searchbooksummary;
        public final View root;

        public ViewHolder(View root) {
            searchbookcover = (ImageView) root.findViewById(R.id.searchbook_cover);
            searchbooktitle = (TextView) root.findViewById(R.id.searchbook_title);
            searchbookauthor = (TextView) root.findViewById(R.id.searchbook_author);
            searchbooksummary = (TextView) root.findViewById(R.id.searchbook_summary);
            this.root = root;
        }
    }
}
