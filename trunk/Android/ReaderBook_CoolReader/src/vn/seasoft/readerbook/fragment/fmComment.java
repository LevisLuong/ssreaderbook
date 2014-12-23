package vn.seasoft.readerbook.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.etiennelawlor.quickreturn.library.enums.QuickReturnType;
import com.etiennelawlor.quickreturn.library.listeners.SpeedyQuickReturnListViewOnScrollListener;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.widget.Toast;
import vn.seasoft.readerbook.HttpServices.COMMAND_API;
import vn.seasoft.readerbook.HttpServices.ErrorType;
import vn.seasoft.readerbook.HttpServices.OnHttpServicesListener;
import vn.seasoft.readerbook.HttpServices.ResultObject;
import vn.seasoft.readerbook.R;
import vn.seasoft.readerbook.ResultObjects.Result_GetCommentsBook;
import vn.seasoft.readerbook.ResultObjects.Result_UserComment;
import vn.seasoft.readerbook.SSReaderApplication;
import vn.seasoft.readerbook.Util.GlobalData;
import vn.seasoft.readerbook.Util.SSUtil;
import vn.seasoft.readerbook.Util.mSharedPreferences;
import vn.seasoft.readerbook.adapter.CommentBookAdapter;
import vn.seasoft.readerbook.dialog.dlgEditComment;
import vn.seasoft.readerbook.listener.IDialogEditText;
import vn.seasoft.readerbook.listener.ILoginFacebook;
import vn.seasoft.readerbook.model.Comment;
import vn.seasoft.readerbook.widget.ViewError;

import java.util.Date;

/**
 * User: XuanTrung
 * Date: 10/16/2014
 * Time: 11:20 AM
 */
public class fmComment extends Fragment implements OnHttpServicesListener {

    Comment tempComment;

    ListView listview;
    CommentBookAdapter adapter;


    Context context;
    int idbook;
    View footerLoadmore;
    Comment postcomment;
    private RelativeLayout fmcommentContainer;
    private LinearLayout fmcommentQuickreturn;
    private EditText fmcommentEdtcomment;
    private ImageView fmcommentBtncomment;
    private ProgressBar fmcommentProgressbarcomment;


    public fmComment(Context _ct, int idbook) {
        super();
        context = _ct;
        this.idbook = idbook;
    }

    private void assignViews(View root) {
        fmcommentContainer = (RelativeLayout) root.findViewById(R.id.fmcomment_container);
        fmcommentProgressbarcomment = (ProgressBar) root.findViewById(R.id.fmcomment_progressbarcomment);
        fmcommentQuickreturn = (LinearLayout) root.findViewById(R.id.fmcomment_quickreturn);
        fmcommentEdtcomment = (EditText) root.findViewById(R.id.fmcomment_edtcomment);
        fmcommentBtncomment = (ImageView) root.findViewById(R.id.fmcomment_btncomment);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new CommentBookAdapter(context);
        adapter.setListener(new CommentBookAdapter.IAdapterComment() {
            @Override
            public void DeleteComment(final Comment comment) {
                tempComment = comment;
                GlobalData.ShowProgressDialog(context, R.string.please_wait);
                SSReaderApplication.getRequestServer(context, (OnHttpServicesListener) fmComment.this).userDeleteCommentBook(comment.getIdcomment());
            }

            @Override
            public void EditComment(final Comment comment) {
                tempComment = comment;
                dlgEditComment dlg = new dlgEditComment(context);
                dlg.setTitle("Chỉnh sửa bình luận");
                dlg.setMessage(comment.getContent());
                dlg.setListener(new IDialogEditText() {
                    @Override
                    public void getValue(String value) {
                        if (value.equals("")) {
                            org.holoeverywhere.widget.Toast.makeText(context, "Vui lòng nhập nội dung bình luận", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        tempComment.setContent(value);
                        GlobalData.ShowProgressDialog(context, R.string.please_wait);
                        SSReaderApplication.getRequestServer(context, (OnHttpServicesListener) fmComment.this).userEditCommentBook(comment.getIdcomment(), value);
                    }
                });
                dlg.show(getSupportActivity());
            }
        });
        listview = new ListView(context);
        listview.setDivider(null);
        listview.setCacheColorHint(android.R.color.transparent);
        listview.setSelector(android.R.color.transparent);
        //-----------------------------------------//
        SSUtil.addViewContainer(fmcommentContainer, new ProgressBar(context), true);
        SSReaderApplication.getRequestServer(context, this).getCommentsBook(idbook, 1);
    }

    void comment(Comment comment) {
        fmcommentProgressbarcomment.setVisibility(View.VISIBLE);
        fmcommentBtncomment.setVisibility(View.GONE);
        SSReaderApplication.getRequestServer(context, this).userCommentBook(idbook, comment.getIduser(), comment.getContent());
    }

    void submitComment() {
        final String content = fmcommentEdtcomment.getText().toString().trim();
        if (!content.equals("")) {
            postcomment = new Comment();
            String iduserfacebook = mSharedPreferences.getUserIDFacebook(context);
            if (iduserfacebook.equals("")) {
                SSReaderApplication.authorizeFB(context, new ILoginFacebook() {
                    @Override
                    public void LoginSuccess() {
                        String iduserfacebook = mSharedPreferences.getUserIDFacebook(context);
                        String username = mSharedPreferences.getUserDisplay(context);
                        int iduser = mSharedPreferences.getUserID(context);
                        postcomment.setUsername(username);
                        postcomment.setIduser(iduser);
                        postcomment.setIduserfacebook(iduserfacebook);
                        postcomment.setContent(content);
                        postcomment.setIdbook(idbook);
                        postcomment.setDatecomment(new Date());
                        comment(postcomment);
                    }
                });
            } else {
                String username = mSharedPreferences.getUserDisplay(context);
                int iduser = mSharedPreferences.getUserID(context);
                postcomment.setUsername(username);
                postcomment.setIduser(iduser);
                postcomment.setIduserfacebook(iduserfacebook);
                postcomment.setContent(content);
                postcomment.setIdbook(idbook);
                postcomment.setDatecomment(new Date());
                comment(postcomment);
            }
        } else {
            org.holoeverywhere.widget.Toast.makeText(context, "Vui lòng nhập nội dung bình luận", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fmcomment, container, false);
        assignViews(v);
        footerLoadmore = inflater.inflate(R.layout.loadmore_layout, null, false);
        listview.addFooterView(footerLoadmore);
        listview.setAdapter(adapter);
        //Init quickreturn listview
        fmcommentBtncomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitComment();
            }
        });
        fmcommentEdtcomment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(android.widget.TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_SEND) {
                    submitComment();
                    handled = true;
                }
                return handled;
            }

        });
        SpeedyQuickReturnListViewOnScrollListener quickreturn = new SpeedyQuickReturnListViewOnScrollListener(getActivity(), QuickReturnType.FOOTER, null, fmcommentQuickreturn);
        quickreturn.registerExtraOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;
                if ((lastInScreen == totalItemCount) && adapter.canLoadMoreData()) {
                    SSReaderApplication.getRequestServer(context, (OnHttpServicesListener) fmComment.this).getCommentsBook(idbook, adapter.loadMoreData());
                }
            }
        });
        listview.setOnScrollListener(quickreturn);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (fmcommentQuickreturn.getVisibility() == View.VISIBLE) {
                    fmcommentQuickreturn.setVisibility(View.GONE);
                } else {
                    fmcommentQuickreturn.setVisibility(View.VISIBLE);
                }
            }
        });
        if (!adapter.isEmpty()) {
            SSUtil.addViewContainer(fmcommentContainer, listview, false);
            if (!adapter.isHaveNew()) {
                listview.removeFooterView(footerLoadmore);
            }
        } else {
            TextView emptyview = new TextView(context);
            emptyview.setText("Chưa có bình luận nào");
            emptyview.setTextSize(24);
            emptyview.setGravity(Gravity.CENTER);
            SSUtil.addViewContainer(fmcommentContainer, emptyview, true);
        }
        return v;
    }

    @Override
    public void onDataError(int errortype, String urlMethod) {
        ErrorType.getErrorMessage(context, errortype);
        if (urlMethod.equals(COMMAND_API.USER_COMMENT)) {
            fmcommentProgressbarcomment.setVisibility(View.GONE);
            fmcommentBtncomment.setVisibility(View.VISIBLE);
        }
        if (urlMethod.equals(COMMAND_API.GET_COMMENTS)) {
            if (adapter.isEmpty()) {
                ViewError viewError = new ViewError(context, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SSReaderApplication.getRequestServer(context, fmComment.this).getCommentsBook(idbook, adapter.reloadData());
                    }
                });
                viewError.setColorText(Color.BLACK);
                SSUtil.addViewContainer(fmcommentContainer, viewError, true);
            }
        }
        GlobalData.DissmissProgress();
    }

    @Override
    public void onGetData(ResultObject resultData, String urlMethod, int id) {
        if (urlMethod.equals(COMMAND_API.GET_COMMENTS)) {
            Result_GetCommentsBook data = (Result_GetCommentsBook) resultData;
            if (adapter.SetListComments(data.lstComment)) {
                if (adapter.isEmpty()) {
                    TextView emptyview = new TextView(context);
                    emptyview.setText("Chưa có bình luận nào");
                    emptyview.setTextSize(24);
                    emptyview.setGravity(Gravity.CENTER);
                    SSUtil.addViewContainer(fmcommentContainer, emptyview, true);
                } else {
                    SSUtil.addViewContainer(fmcommentContainer, listview, false);
                }
            }
            if (!adapter.isHaveNew()) {
                listview.removeFooterView(footerLoadmore);
            }
        }
        if (urlMethod.equals(COMMAND_API.USER_COMMENT)) {
            Result_UserComment data = (Result_UserComment) resultData;
            if (data.idcomment != 0) {
                postcomment.setIdcomment(data.idcomment);
                fmcommentProgressbarcomment.setVisibility(View.GONE);
                fmcommentBtncomment.setVisibility(View.VISIBLE);
                if (adapter.isEmpty()) {
                    SSUtil.addViewContainer(fmcommentContainer, listview, false);
                    listview.removeFooterView(footerLoadmore);
                }
                fmcommentEdtcomment.setText("");
                adapter.addItem(postcomment);
            }
        }
        if (urlMethod.equals(COMMAND_API.USER_EDIT_COMMENT)) {
            adapter.editItem(tempComment);
        }
        if (urlMethod.equals(COMMAND_API.USER_DELETE_COMMENT)) {
            adapter.deleteItem(tempComment);
            if (adapter.isEmpty()) {
                TextView emptyview = new TextView(context);
                emptyview.setText("Chưa có bình luận nào");
                emptyview.setTextSize(24);
                emptyview.setGravity(Gravity.CENTER);
                SSUtil.addViewContainer(fmcommentContainer, emptyview, true);
            }
        }
        GlobalData.DissmissProgress();
    }
}
