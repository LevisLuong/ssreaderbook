package vn.seasoft.readerbook.fragment;

import android.os.Bundle;
import android.text.method.ArrowKeyMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.holoeverywhere.app.Fragment;
import vn.seasoft.readerbook.R;

/**
 * User: XuanTrung
 * Date: 10/16/2014
 * Time: 10:53 AM
 */
public class fmSummary extends Fragment {
    String summary;
    private TextView fmsummaryTxtSummary;

    public fmSummary() {
    }

    public fmSummary(String _sm) {
        super();
        summary = _sm;
    }

    private void assignViews(View root) {
        fmsummaryTxtSummary = (TextView) root.findViewById(R.id.fmsummary_txtSummary);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fmsummary, container, false);
        assignViews(v);
        fmsummaryTxtSummary.setClickable(false);
        fmsummaryTxtSummary.setMovementMethod(new ArrowKeyMovementMethod());
        fmsummaryTxtSummary.setText(summary);
        return v;
    }


}
