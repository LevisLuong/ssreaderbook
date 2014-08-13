package vn.seasoft.readerbook.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.app.DialogFragment;

/**
 * User: XuanTrung
 * Date: 7/2/2014
 * Time: 10:59 AM
 */
public class dlgConfirm extends DialogFragment {
    Context mContext;

    DialogInterface.OnClickListener listener;
    String message;

    public dlgConfirm(Context _context) {
        setDialogType(DialogType.AlertDialog);
        mContext = _context;

    }

    public void setListener(DialogInterface.OnClickListener _listener) {
        listener = _listener;
    }

    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getSupportActivity());
        builder.setTitle("Thông báo");
        builder.setMessage(message);
        if (listener != null) {
            builder.setNegativeButton(android.R.string.cancel, null);
            builder.setPositiveButton(android.R.string.ok, listener);
        } else {
            builder.setPositiveButton(android.R.string.ok, null);
        }

        return builder.create();
    }

    public void setMessage(String _message) {
        message = _message;
    }

}
