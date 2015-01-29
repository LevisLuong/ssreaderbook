package vn.seasoft.sachcuatui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.app.DialogFragment;
import vn.seasoft.sachcuatui.R;

/**
 * User: XuanTrung
 * Date: 7/2/2014
 * Time: 10:59 AM
 */
public class dlgNotice extends DialogFragment {
    Context mContext;

    DialogInterface.OnClickListener listener;
    String message;

    public dlgNotice(Context _context) {
        setDialogType(DialogType.AlertDialog);
        mContext = _context;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void setListener(DialogInterface.OnClickListener _listener) {
        listener = _listener;
    }

    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getSupportActivity());
        builder.setTitle("Thông báo");
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok_button, null);

        return builder.create();
    }

    public void setMessage(String _message) {
        message = _message;
    }

    public void setMessage(int idmessage) {
        message = mContext.getResources().getString(idmessage);
    }
}
