package com.vibrant.asp.customtext;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class MyButtonBold extends Button {
    public MyButtonBold(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public MyButtonBold(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public MyButtonBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("icon_blck.ttf", context);
        setTypeface(customFont);
    }
}
