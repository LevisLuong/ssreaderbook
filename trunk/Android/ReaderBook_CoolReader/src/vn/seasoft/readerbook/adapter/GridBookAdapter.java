package vn.seasoft.readerbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import org.holoeverywhere.widget.ProgressBar;
import vn.seasoft.readerbook.R;
import vn.seasoft.readerbook.Util.GlobalData;
import vn.seasoft.readerbook.model.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * User: XuanTrung
 * Date: 6/5/2014
 * Time: 3:27 PM
 */
public class GridBookAdapter extends BaseAdapter {
    private Context context;
    private List<Book> books;

    int index;
    boolean isLoading;
    boolean isHaveNew;

    int tempIndex;

    public GridBookAdapter(Context context) {
        this.context = context;
        this.books = new ArrayList<Book>();

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

    public boolean SetListBooks(List<Book> _lst) {
        boolean isNew = false;
        isLoading = false;
        index = tempIndex;
        if (index == 1) {
            books = _lst;
            isNew = true;
        } else {
            books.addAll(_lst);
        }
        if (_lst.size() < 10) {
            isHaveNew = false;
        } else {
            isHaveNew = true;
        }
        notifyDataSetChanged();
        return isNew;
    }

    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gridbook_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Book book = books.get(position);
        if (book != null) {
            holder.griditemlabel.setText(book.getTitle());
            UrlImageViewHelper.setUrlDrawable(holder.griditemcover, GlobalData.getUrlImageCover(book), R.drawable.book_exam);
        }
        if (isHaveNew && (position == (books.size() - 1))) {
            holder.griditemProgress.setVisibility(View.VISIBLE);
        } else {
            holder.griditemProgress.setVisibility(View.GONE);
        }
        return convertView;
    }


    @Override
    public Book getItem(int position) {
        // TODO Auto-generated method stub
        return books.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    private class ViewHolder {
        public final TextView griditemlabel;
        public final ImageView griditemcover;
        public final ProgressBar griditemProgress;
        public final View root;

        public ViewHolder(View root) {
            griditemlabel = (TextView) root.findViewById(R.id.grid_item_label);
            griditemcover = (ImageView) root.findViewById(R.id.grid_item_cover);
            griditemProgress = (ProgressBar) root.findViewById(R.id.grid_item_progress);
            this.root = root;
        }
    }
}
