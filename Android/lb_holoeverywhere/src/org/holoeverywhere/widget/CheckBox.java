
package org.holoeverywhere.widget;

import android.content.Context;
import android.util.AttributeSet;

import org.holoeverywhere.FontLoader.FontStyleProvider;

public class CheckBox extends android.widget.CheckBox implements FontStyleProvider {
    private String mFontFamily;

    private int mFontStyle;

    public CheckBox(Context context) {
        this(context, null);
    }

    public CheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.checkboxStyle);
    }

    public CheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TextView.construct(this, context, attrs, defStyle);
    }

    @Override
    public String getFontFamily() {
        return mFontFamily;
    }

    @Override
    public int getFontStyle() {
        return mFontStyle;
    }

    @Override
    public void setAllCaps(boolean allCaps) {
        TextView.setAllCaps(this, allCaps);
    }

    @Override
    public void setFontStyle(String fontFamily, int fontStyle) {
        mFontFamily = fontFamily;
        mFontStyle = fontStyle;
        TextView.setFontStyle(this, fontFamily, fontStyle);
    }

    @Override
    public void setTextAppearance(Context context, int resid) {
        TextView.setTextAppearance(this, context, resid);
    }
}
