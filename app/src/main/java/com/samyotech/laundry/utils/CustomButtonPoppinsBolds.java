package com.samyotech.laundry.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;


public class CustomButtonPoppinsBolds extends Button {
    public CustomButtonPoppinsBolds(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public CustomButtonPoppinsBolds(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public CustomButtonPoppinsBolds(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    public CustomButtonPoppinsBolds(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("Poppins_Bold.ttf", context);
        setTypeface(customFont);
    }
}
