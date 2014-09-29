package vn.seasoft.readerbook.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.app.DialogFragment;
import vn.seasoft.readerbook.R;
import vn.seasoft.readerbook.SSReaderApplication;
import vn.seasoft.readerbook.Util.GlobalData;

/**
 * User: XuanTrung
 * Date: 7/2/2014
 * Time: 10:59 AM
 */
public class dlgFeedback extends DialogFragment {
    Context mContext;

    public dlgFeedback() {
    }

    public dlgFeedback(Context _context) {
        setDialogType(DialogType.AlertDialog);
        mContext = _context;
    }

    private EditText dlgfeedbackTitlebook;
    private EditText dlgfeedbackAuthorbook;
    private EditText dlgfeedbackFeedback;

    private void assignViews(View root) {
        dlgfeedbackTitlebook = (EditText) root.findViewById(R.id.dlgfeedback_titlebook);
        dlgfeedbackAuthorbook = (EditText) root.findViewById(R.id.dlgfeedback_authorbook);
        dlgfeedbackFeedback = (EditText) root.findViewById(R.id.dlgfeedback_feedback);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_feedback, container, false);
        assignViews(v);
        return v;
    }

    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getSupportActivity());
        builder.setNegativeButton(R.string.cancel_button, null);
        builder.setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String titlebook = dlgfeedbackTitlebook.getText().toString().trim();
                String authorbook = dlgfeedbackAuthorbook.getText().toString().trim();
                String feedback = dlgfeedbackFeedback.getText().toString().trim();
                if (titlebook.equals("") && authorbook.equals("") && feedback.equals("")) {

                    return;
                }
                GlobalData.ShowProgressDialog(mContext, R.string.please_wait);
                SSReaderApplication.getRequestServer(mContext, SSReaderApplication.getInstance()).addFeedBack(titlebook, authorbook, feedback);

            }
        });
        return builder.create();
    }
}