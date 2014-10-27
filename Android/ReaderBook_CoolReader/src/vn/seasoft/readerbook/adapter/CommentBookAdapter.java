package vn.seasoft.readerbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import urlimageviewhelper.UrlImageViewHelper;
import vn.seasoft.readerbook.R;
import vn.seasoft.readerbook.Util.SSUtil;
import vn.seasoft.readerbook.model.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * User: XuanTrung
 * Date: 7/11/2014
 * Time: 11:07 AM
 */
public class CommentBookAdapter extends BaseAdapter {
    Context context;
    List<Comment> lstComments;

    int index;
    boolean isLoading;
    boolean isHaveNew;
    int tempIndex;

    public CommentBookAdapter(Context _ct) {
        context = _ct;
        isLoading = true;
        isHaveNew = true;
        index = 1;
        tempIndex = 1;
        lstComments = new ArrayList<Comment>();
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

    public void addItem(Comment comment) {
        lstComments.add(0, comment);
        notifyDataSetChanged();
    }

    public boolean SetListComments(List<Comment> _lst) {
        boolean isNew = false;
        index = tempIndex;
        if (index == 1) {
            isNew = true;
            lstComments = _lst;
        } else {
            lstComments.addAll(_lst);
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
        return lstComments.size();
    }

    @Override
    public Comment getItem(int i) {
        return lstComments.get(i);
    }

    @Override
    public long getItemId(int i) {
        return lstComments.get(i).getIdcomment();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.comment_item, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Comment comment = lstComments.get(i);
        if (comment != null) {
            holder.commenttime.setText(SSUtil.calTimeDistance(comment.getDatecomment()));
            holder.commentusername.setText(comment.getUsername());
            holder.commentcontent.setText(comment.getContent());
            UrlImageViewHelper.setUrlDrawable(holder.commentavatar, SSUtil.getAvatarFacebookById(comment.getIduserfacebook()));
        }
        return view;
    }


    private class ViewHolder {
        public final ImageView commentavatar;
        public final TextView commentusername;
        public final TextView commenttime;
        public final TextView commentcontent;
        public final View root;

        public ViewHolder(View root) {
            commentavatar = (ImageView) root.findViewById(R.id.comment_avatar);
            commentusername = (TextView) root.findViewById(R.id.comment_username);
            commenttime = (TextView) root.findViewById(R.id.comment_time);
            commentcontent = (TextView) root.findViewById(R.id.comment_content);
            this.root = root;
        }
    }
}
