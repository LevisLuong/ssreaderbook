package org.coolreader.crengine;

import android.content.DialogInterface;
import android.os.Bundle;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.app.DialogFragment;
import vn.seasoft.sachcuatui.R;

public class NoticeDialog extends DialogFragment {

    String message;
    BaseActivity mActivity;

    public NoticeDialog(BaseActivity activity, String _message) {
        setDialogType(DialogType.AlertDialog);
        message = _message;
        mActivity = activity;
    }

    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getSupportActivity());
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setMessage(message);
        prepareBuilder(builder);
        return builder.create();
    }

    protected void prepareBuilder(AlertDialog.Builder builder) {
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
    }
}
