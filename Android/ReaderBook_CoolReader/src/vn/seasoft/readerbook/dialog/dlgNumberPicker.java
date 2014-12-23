package vn.seasoft.readerbook.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.app.DialogFragment;
import org.holoeverywhere.widget.NumberPicker;
import vn.seasoft.readerbook.R;

/**
 * User: XuanTrung
 * Date: 7/2/2014
 * Time: 10:59 AM
 */
public class dlgNumberPicker extends DialogFragment {
    Context mContext;

    IDialogNumberPicker listener;
    String message;
    int MinValue, MaxValue, DefaultValue;
    NumberPicker numberPicker;

    public dlgNumberPicker(Context _context, int minValue, int maxValue, int defaultValue) {
        setDialogType(DialogType.AlertDialog);
        mContext = _context;
        MinValue = minValue;
        MaxValue = maxValue;
        DefaultValue = defaultValue;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.dialog_numberpicker, container, false);
        numberPicker = (NumberPicker) view.findViewById(R.id.numberpicker);
        numberPicker.setMaxValue(MaxValue);
        numberPicker.setMinValue(MinValue);
        numberPicker.setValue(DefaultValue);
        return view;
    }

    public void setListener(IDialogNumberPicker _listener) {
        listener = _listener;
    }

    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getSupportActivity());
        builder.setTitle("Chọn Trang");
        builder.setNegativeButton(R.string.cancel_button, null);
        builder.setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.getValue(numberPicker.getValue());
            }
        });
        return builder.create();
    }

    public interface IDialogNumberPicker {
        public void getValue(int value);
    }
}
