package com.vibrant.asp.customtext;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyTextLight extends TextView {
    public MyTextLight(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public MyTextLight(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public MyTextLight(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("icon_txt.ttf", context);
        setTypeface(customFont);
    }
}
