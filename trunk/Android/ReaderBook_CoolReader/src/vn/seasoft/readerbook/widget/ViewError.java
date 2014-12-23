package vn.seasoft.readerbook.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.widget.LinearLayout;
import org.holoeverywhere.widget.TextView;
import vn.seasoft.readerbook.R;

public class ViewError extends LinearLayout {
    Button btnRetry;
    TextView txtcontent;
    OnClickListener onCLickRetryListener;

    public ViewError(Context context, OnClickListener _listener) {
        super(context);
        onCLickRetryListener = _listener;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.view_error, this);
        txtcontent = (TextView) v.findViewById(R.id.viewerror_txtcontent);
        btnRetry = (Button) v.findViewById(R.id.viewerror_btnretry);
        if (onCLickRetryListener != null) {
            btnRetry.setOnClickListener(onCLickRetryListener);
        }
        setColorText(Color.WHITE);
    }

    public ViewError(Context context, AttributeSet attrs) {

        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.view_error, this);
    }

    public ViewError setColorText(int color) {
        txtcontent.setTextColor(color);
        btnRetry.setTextColor(color);
        return this;
    }

    public void setContent(String content) {
        txtcontent.setText(content);
    }
}