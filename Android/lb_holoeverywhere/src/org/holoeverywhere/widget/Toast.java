
package org.holoeverywhere.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.R;
import org.holoeverywhere.ThemeManager;
import org.holoeverywhere.app.ContextThemeWrapperPlus;

public class Toast extends android.widget.Toast {
    public static final int LENGTH_LONG = android.widget.Toast.LENGTH_LONG;
    public static final int LENGTH_SHORT = android.widget.Toast.LENGTH_SHORT;

    public Toast(Context context) {
        super(context);
    }

    public static Toast makeText(Context context, CharSequence s, int duration) {
        // Fallback mode
        TypedArray a = context.obtainStyledAttributes(new int[]{R.attr.holoTheme});
        if (a.getInt(0, ThemeManager.INVALID) == ThemeManager.INVALID) {
            context = new ContextThemeWrapperPlus(context, ThemeManager.getThemeResource(ThemeManager.getDefaultTheme()));
        }
        a.recycle();

        final View view = LayoutInflater.inflate(context, R.layout.transient_notification);
        ((TextView) view.findViewById(android.R.id.message)).setText(s);

        Toast toast = new Toast(context);
        toast.setView(view);
        toast.setDuration(duration);
        return toast;
    }

    public static Toast makeText(Context context, int resId, int duration) {
        return Toast.makeText(context, context.getResources().getString(resId), duration);
    }

    @Override
    public void setText(CharSequence s) {
        final View view = getView();
        final TextView textView = view != null ? (TextView) view.findViewById(android.R.id.message) : null;
        if (textView == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        textView.setText(s);
    }
}
