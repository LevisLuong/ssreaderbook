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
import vn.seasoft.readerbook.model.Book;

import java.util.List;

/**
 * User: XuanTrung
 * Date: 6/5/2014
 * Time: 3:27 PM
 */
public class GridBookAdapter extends BaseAdapter {
    private Context context;
    private List<Book> books;

    public GridBookAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
    }

    public void setData(List<Book> _books) {
        this.books = _books;
        notifyDataSetChanged();
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
        public final View root;

        public ViewHolder(View root) {
            griditemlabel = (TextView) root.findViewById(R.id.grid_item_label);
            griditemcover = (ImageView) root.findViewById(R.id.grid_item_cover);
            this.root = root;
        }
    }
}
