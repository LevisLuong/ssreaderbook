package vn.seasoft.readerbook.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.app.DialogFragment;
import org.holoeverywhere.widget.EditText;
import vn.seasoft.readerbook.R;
import vn.seasoft.readerbook.listener.IDialogEditText;

/**
 * User: XuanTrung
 * Date: 7/2/2014
 * Time: 10:59 AM
 */
public class dlgEditText extends DialogFragment {
    Context mContext;

    IDialogEditText listener;
    String message;
    String title;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public dlgEditText(Context _context) {
        setDialogType(DialogType.AlertDialog);
        mContext = _context;
        message = "";
    }

    public dlgEditText(Context _context, String _title) {
        setDialogType(DialogType.AlertDialog);
        mContext = _context;
        message = "";
        title = _title;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    private int typeEditText = InputType.TYPE_CLASS_NUMBER;

    public void setTypeEditText(int type) {
        typeEditText = type;
    }

    EditText editText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.dialog_edittext, container, false);
        editText = (EditText) view.findViewById(R.id.dlgEditText);
        editText.setInputType(typeEditText);
        editText.setText(message);
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
                listener.getValue(editText.getText().toString());
            }
        });
        return builder.create();
    }

}
