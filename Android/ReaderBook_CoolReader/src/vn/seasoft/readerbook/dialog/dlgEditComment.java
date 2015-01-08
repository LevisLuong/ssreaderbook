package vn.seasoft.readerbook.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.app.DialogFragment;
import org.holoeverywhere.widget.EditText;
import urlimageviewhelper.UrlImageViewHelper;
import vn.seasoft.readerbook.R;
import vn.seasoft.readerbook.Util.SSUtil;
import vn.seasoft.readerbook.Util.mSharedPreferences;
import vn.seasoft.readerbook.listener.IDialogEditText;

/**
 * User: XuanTrung
 * Date: 7/2/2014
 * Time: 10:59 AM
 */
public class dlgEditComment extends DialogFragment {
    Context mContext;

    IDialogEditText listener;
    String contentshare;
    String title;
    private ImageView dlgSharefbAvatar;
    private TextView dlgSharefbUsername;
    private EditText dlgEditText;

    public dlgEditComment(Context _context) {
        setDialogType(DialogType.AlertDialog);
        mContext = _context;
        contentshare = "";
    }

    public dlgEditComment(Context _context, String _title) {
        setDialogType(DialogType.AlertDialog);
        mContext = _context;
        contentshare = "";
        title = _title;
    }

    public void setMessage(String message) {
        this.contentshare = message;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    private void assignViews(View root) {
        dlgSharefbAvatar = (ImageView) root.findViewById(R.id.dlg_sharefb_avatar);
        dlgSharefbUsername = (TextView) root.findViewById(R.id.dlg_sharefb_username);
        dlgEditText = (EditText) root.findViewById(R.id.dlgEditText);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.dialog_sharefacebook, container, false);
        assignViews(view);
        UrlImageViewHelper.setUrlDrawable(dlgSharefbAvatar, SSUtil.getAvatarFacebookById(mSharedPreferences.getUserIDFacebook(mContext)));
        dlgSharefbUsername.setText(mSharedPreferences.getUserDisplay(mContext));
        dlgEditText.setText(contentshare);
        return view;
    }

    public void setListener(IDialogEditText _listener) {
        listener = _listener;
    }

    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getSupportActivity());
        builder.setTitle(title);
        builder.setNegativeButton(R.string.cancel_button, null);
        builder.setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.getValue(dlgEditText.getText().toString().trim());
            }
        });
        return builder.create();
    }
}
