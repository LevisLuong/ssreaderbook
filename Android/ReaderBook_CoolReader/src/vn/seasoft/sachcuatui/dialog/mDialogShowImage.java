package vn.seasoft.sachcuatui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView.ScaleType;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import vn.seasoft.sachcuatui.R;

public class mDialogShowImage extends Dialog {

    ImageViewTouch img;
    LayoutInflater layoutInflater;
    Context mContext;

    public mDialogShowImage(Context context, Bitmap bitmap) {
        super(context, R.style.DialogTranparent);
        // TODO Auto-generated constructor stub
        layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = (View) layoutInflater.inflate(R.layout.dialog_showimage,
                null);
        setContentView(view);
        this.setCancelable(true);

        img = (ImageViewTouch) view.findViewById(R.id.img);
        img.setScaleType(ScaleType.FIT_XY);
        img.setImageBitmap(bitmap);
        img.zoomTo(2.0f, 20);
    }
}
