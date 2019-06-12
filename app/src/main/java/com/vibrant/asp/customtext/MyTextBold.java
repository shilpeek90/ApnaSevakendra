package com.vibrant.asp.customtext;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyTextBold extends TextView {
    public MyTextBold(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public MyTextBold(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public MyTextBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("icon_blck.ttf", context);
        setTypeface(customFont);
    }
}
